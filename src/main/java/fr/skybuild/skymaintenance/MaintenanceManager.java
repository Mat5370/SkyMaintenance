package fr.skybuild.skymaintenance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaintenanceManager implements CommandExecutor {

    private final SkyMaintenance plugin;
    private boolean maintenanceActive = false;
    private Timer timer = null;
    private TimerTask currentTask = null;

    private void logAction(String actor, String action, String detail) {
        try {
            File logFile = new File(plugin.getDataFolder(), "logs.txt");
            FileWriter writer = new FileWriter(logFile, true);
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("[" + timestamp + "] " + actor + " → " + action + (detail != null ? " (" + detail + ")" : "") + "\n");
            writer.close();
        } catch (IOException e) {
            plugin.getLogger().warning("Impossible d'écrire dans logs.txt : " + e.getMessage());
        }
    }

    private String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("messages." + key, "§c[SkyMaintenance] Message non défini : " + key));
    }

    public MaintenanceManager(SkyMaintenance plugin) {
        this.plugin = plugin;
    }

    public boolean isMaintenanceActive() {
        return maintenanceActive;
    }

    public void startMaintenance(long durationMillis, CommandSender sender) {
        maintenanceActive = true;
        sender.sendMessage(getMessage("maintenance.enabled.sender"));

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p instanceof Player && !p.equals(sender)) {
                p.sendMessage(getMessage("maintenance.enabled.broadcast"));
            }
        });

        kickUnwhitelistedPlayers();

        if (durationMillis > 0) {
            timer = new Timer();
            currentTask = new TimerTask() {
                @Override
                public void run() {
                    stopMaintenance();
                }
            };
            timer.schedule(currentTask, durationMillis);
        } else {
            currentTask = null;
        }
    }

    public void stopMaintenance(CommandSender sender) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        currentTask = null;
        maintenanceActive = false;

        sender.sendMessage(getMessage("maintenance.disabled.sender"));

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p instanceof Player && !p.equals(sender)) {
                p.sendMessage(getMessage("maintenance.disabled.broadcast"));
            }
        });
    }

    public void stopMaintenance() {
        currentTask = null;
        maintenanceActive = false;

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendMessage(getMessage("maintenance.disabled.broadcast"));
        });
    }

    private void kickUnwhitelistedPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("maintenance.whitelist")) {
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.maintenance.kick")));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getMessage("usage"));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "on":
                if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission("maintenance.toggle")) {
                    sender.sendMessage(getMessage("no-permission.toggle"));
                    return true;
                }

                if (maintenanceActive) {
                    sender.sendMessage(getMessage("already.enabled"));
                    return true;
                }

                long duration = -1;
                if (args.length >= 2) {
                    try {
                        duration = parseDuration(args[1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(getMessage("invalid.duration"));
                        return true;
                    }
                }

                startMaintenance(duration, sender);
                logAction(sender.getName(), "ACTIVATION", (duration > 0 ? (duration / 1000) + "s" : "indéterminée"));
                break;

            case "off":
                if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission("maintenance.toggle")) {
                    sender.sendMessage(getMessage("no-permission.toggle"));
                    return true;
                }

                if (!maintenanceActive) {
                    sender.sendMessage(getMessage("already.disabled"));
                    return true;
                }

                stopMaintenance(sender);
                logAction(sender.getName(), "DÉSACTIVATION", null);
                break;

            case "status":
                if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission("maintenance.status")) {
                    sender.sendMessage(getMessage("no-permission.status"));
                    return true;
                }

                if (maintenanceActive) {
                    sender.sendMessage(getMessage("status.active"));
                    sender.sendMessage(getRemainingTimeMessage());
                } else {
                    sender.sendMessage(getMessage("status.inactive"));
                }
                break;

            default:
                sender.sendMessage(getMessage("usage"));
                break;
        }

        return true;
    }

    private long parseDuration(String input) {
        input = input.toLowerCase();
        if (input.endsWith("s")) {
            return TimeUnit.SECONDS.toMillis(Long.parseLong(input.replace("s", "")));
        } else if (input.endsWith("m")) {
            return TimeUnit.MINUTES.toMillis(Long.parseLong(input.replace("m", "")));
        } else if (input.endsWith("h")) {
            return TimeUnit.HOURS.toMillis(Long.parseLong(input.replace("h", "")));
        }
        throw new IllegalArgumentException("Format invalide");
    }

    public String getRemainingTimeMessage() {
        if (currentTask == null) {
            return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.duration.indeterminate", "&7Durée : indéterminée."));
        }

        long remaining = currentTask.scheduledExecutionTime() - System.currentTimeMillis();
        if (remaining <= 0) {
            return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.duration.indeterminate", "&7Durée : indéterminée."));
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60;

        return ChatColor.translateAlternateColorCodes('&',
            plugin.getConfig().getString("messages.duration.remaining", "&7Durée restante : &e{minutes}m {seconds}s.")
            .replace("{minutes}", String.valueOf(minutes))
            .replace("{seconds}", String.valueOf(seconds)));
    }
}
