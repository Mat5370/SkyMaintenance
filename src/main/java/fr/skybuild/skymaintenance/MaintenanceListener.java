package fr.skybuild.skymaintenance;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class MaintenanceListener implements Listener {

    private final MaintenanceManager manager;

    public MaintenanceListener(MaintenanceManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (manager.isMaintenanceActive() && !event.getPlayer().hasPermission("maintenance.whitelist")) {
            String message = ChatColor.RED + "🛠 Le serveur est en maintenance.\n" +
                    ChatColor.GRAY + "Merci de patienter.\n\n" +
                    manager.getRemainingTimeMessage();
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, message);
        }
    }
}
