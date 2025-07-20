package fr.skybuild.skymaintenance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MaintenanceManager implements CommandExecutor {

    private final SkyMaintenance plugin;
    private boolean maintenanceActive = false;
    private Timer timer = null;
    private TimerTask currentTask = null;

    public MaintenanceManager(SkyMaintenance plugin) {
        this.plugin = plugin;
    }

    public boolean isMaintenanceActive() {
        return maintenanceActive;
    }

    public void startMaintenance(long durationMillis) {
        maintenanceActive = true;
        Bukkit.broadcastMessage(ChatColor.RED + "Maintenance activée !");
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

    public void stopMaintenance() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        currentTask = null;
        maintenanceActive = false;
        Bukkit.broadcastMessage(ChatColor.GREEN + "Maintenance désactivée !");
    }

    private void kickUnwhitelistedPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("maintenance.whitelist")) {
                player.kickPlayer(ChatColor.RED + "🛠 Le serveur est en maintenance.\n" +
                        ChatColor.GRAY + "Veuillez réessayer plus tard.");
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("maintenance.toggle")) {
            sender.sendMessage(ChatColor.RED + "⛔ Hey ! Tu n'as pas la permission d'utiliser cette commande.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Utilisation: /maintenance <on|off> [durée ex: 5m, 30s]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on":
                if (maintenanceActive) {
                    sender.sendMessage(ChatColor.RED + "La maintenance est déjà activée.");
                    return true;
                }

                long duration = -1;
                if (args.length >= 2) {
                    try {
                        duration = parseDuration(args[1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + "Format de durée invalide. Ex: 5m, 30s, 1h");
                        return true;
                    }
                }

                startMaintenance(duration);
                break;

            case "off":
                if (!maintenanceActive) {
                    sender.sendMessage(ChatColor.RED + "La maintenance n'est pas active.");
                    return true;
                }

                stopMaintenance();
                break;
            
            case "status":
                if (!sender.hasPermission("maintenance.status")) {
                    sender.sendMessage(ChatColor.RED + "⛔ Tu n'as pas la permission de consulter le statut de la maintenance.");
                    return true;
                }

                if (maintenanceActive) {
                    sender.sendMessage(ChatColor.YELLOW + "🛠 Maintenance actuellement active.");
                    sender.sendMessage(getRemainingTimeMessage());
                } else {
                    sender.sendMessage(ChatColor.GREEN + "✅ Le serveur n'est pas en maintenance.");
                }
                break;

            default:
                sender.sendMessage(ChatColor.YELLOW + "Utilisation: /maintenance <on|off> [durée]");
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
            return ChatColor.GRAY + "Durée : indéterminée.";
        }

        long remaining = currentTask.scheduledExecutionTime() - System.currentTimeMillis();
        if (remaining <= 0) {
            return ChatColor.GRAY + "Durée : indéterminée.";
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60;

        return ChatColor.GRAY + "Durée restante : " + ChatColor.YELLOW + minutes + "m " + seconds + "s.";
    }
}
