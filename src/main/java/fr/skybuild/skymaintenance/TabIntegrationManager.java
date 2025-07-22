package fr.skybuild.skymaintenance;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.nametag.NameTagManager;
import me.neznamy.tab.api.tablist.HeaderFooterManager;

public class TabIntegrationManager {

    private final boolean enabled;
    private final String header;
    private final String footer;
    private final String suffix;

    private final HeaderFooterManager headerFooterManager;
    private final NameTagManager nameTagManager;

    public TabIntegrationManager(SkyMaintenance plugin) {
        this.enabled = plugin.getConfig().getBoolean("tab-integration.enabled");
        this.header = plugin.colorize(plugin.getConfig().getString("tab-integration.header", ""));
        this.footer = plugin.colorize(plugin.getConfig().getString("tab-integration.footer", ""));
        this.suffix = plugin.colorize(plugin.getConfig().getString("tab-integration.suffix", ""));

        TabAPI tabAPI = enabled ? TabAPI.getInstance() : null;
        this.headerFooterManager = (tabAPI != null) ? tabAPI.getHeaderFooterManager() : null;
        this.nameTagManager = (tabAPI != null) ? tabAPI.getNameTagManager() : null;
    }

    public void applyToPlayer(TabPlayer player) {
        if (!enabled || player == null) return;

        if (headerFooterManager != null) {
            headerFooterManager.setHeader(player, header);
            headerFooterManager.setFooter(player, footer);
        }

        if (nameTagManager != null) {
            nameTagManager.setSuffix(player, suffix);
        }
    }

    public void resetPlayer(TabPlayer player) {
        if (!enabled || player == null) return;

        if (headerFooterManager != null) {
            headerFooterManager.setHeader(player, null);
            headerFooterManager.setFooter(player, null);
        }

        if (nameTagManager != null) {
            nameTagManager.setSuffix(player, null);
        }
    }
}
