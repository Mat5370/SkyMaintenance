package fr.skybuild.skymaintenance;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class SkyMaintenance extends JavaPlugin {

    private static SkyMaintenance instance;
    private MaintenanceManager maintenanceManager;

    @Override
    public void onEnable() {
        instance = this;

        // Création du dossier plugins/SkyMaintenance et des fichiers
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        saveDefaultConfig();

    File readmeFile = new File(dataFolder, "readme.txt");
    if (!readmeFile.exists()) {
        try (FileWriter writer = new FileWriter(readmeFile)) {
            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("       SkyMaintenance - Plugin Bukkit\n");
            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            writer.write("Développé par : Mat5370\n");
            writer.write("Version : " + getDescription().getVersion() + "\n\n");

            writer.write("📌 Description :\n");
            writer.write("SkyMaintenance est un plugin léger et personnalisable permettant d’activer une période de maintenance sur votre serveur Minecraft (Paper, Spigot, Bukkit).\n");
            writer.write("Il permet de restreindre l’accès aux joueurs non autorisés pendant les opérations techniques ou les mises à jour.\n\n");

            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("       🔧 Fonctionnalités :\n");
            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("- Activation / désactivation de la maintenance via la commande : /maintenance <on|off> [durée]\n");
            writer.write("- Possibilité de définir une durée automatique d’expiration (ex: 5m, 30s, 1h…)\n");
            writer.write("- Déconnexion automatique des joueurs non-whitelistés\n");
            writer.write("- Gestion des permissions avancée\n");
            writer.write("- Commande /maintenance status pour consulter l’état en cours\n");
            writer.write("- Journalisation des activations / désactivations dans `logs.txt`\n");
            writer.write("- Messages entièrement personnalisables via `config.yml`\n");
            writer.write("- Compatible Paper 1.20+ (et versions Bukkit/Spigot compatibles)\n\n");

            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("       📜 Permissions :\n");
            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("- maintenance.toggle → Activer/désactiver la maintenance\n");
            writer.write("- maintenance.status → Voir le statut de la maintenance\n");
            writer.write("- maintenance.whitelist → Permet de rester connecté pendant la maintenance\n\n");

            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("       📁 Fichiers générés :\n");
            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("- config.yml → pour personnaliser tous les messages\n");
            writer.write("- logs.txt → journal des actions (activations/désactivations)\n");
            writer.write("- readme.txt → ce fichier :)\n\n");

            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("       📬 Crédits :\n");
            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            writer.write("Développé par Mat5370 pour le projet SkyBuild.\n");
            writer.write("Discord: https://discord.gg/pESQ2Twkaq\n\n");
            writer.write("Tu peux redistribuer ce plugin tant que tu respectes les crédits et que tu ne modifies pas les mentions de l’auteur original.\n\n");
            writer.write("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        } catch (IOException e) {
            getLogger().warning("Impossible de créer readme.txt : " + e.getMessage());
        }
    }

        File logsFile = new File(dataFolder, "logs.txt");
        if (!logsFile.exists()) {
            try {
                logsFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("Impossible de créer logs.txt : " + e.getMessage());
            }
        }

        // Commandes
        this.maintenanceManager = new MaintenanceManager(this);
        getCommand("maintenance").setExecutor(maintenanceManager);

        String version = getDescription().getVersion();
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "   _____ __  ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "  / ___// /__ " + ChatColor.AQUA + "SkyMaintenance v" + version);
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "  \\__ \\/ / _ \\" + ChatColor.GRAY + " Running on " + Bukkit.getBukkitVersion());
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + " ___/ / /  __/");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "/____/_/\\___/ ");
        Bukkit.getConsoleSender().sendMessage("");

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
