package fr.skybuild.skymaintenance;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMaintenance extends JavaPlugin {

    private static SkyMaintenance instance;
    private MaintenanceManager maintenanceManager;

    @Override
    public void onEnable() {
        instance = this;
        maintenanceManager = new MaintenanceManager(this);
        getCommand("maintenance").setExecutor(maintenanceManager);
        Bukkit.getPluginManager().registerEvents(new MaintenanceListener(maintenanceManager), this);
        getLogger().info("SkyMaintenance activé !");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkyMaintenance désactivé !");
    }

    public static SkyMaintenance getInstance() {
        return instance;
    }

    public MaintenanceManager getMaintenanceManager() {
        return maintenanceManager;
    }
}
