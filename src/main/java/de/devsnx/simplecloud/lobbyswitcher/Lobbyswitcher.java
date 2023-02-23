package de.devsnx.simplecloud.lobbyswitcher;

import de.devsnx.simplecloud.lobbyswitcher.listener.InventoryClickListener;
import de.devsnx.simplecloud.lobbyswitcher.listener.PlayerInteractListener;
import de.devsnx.simplecloud.lobbyswitcher.manager.InventoryManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author DevSnx
 * @since 23.02.2023
 */
public final class Lobbyswitcher extends JavaPlugin {
    
    public static Lobbyswitcher instance;
    public InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        inventoryManager = new InventoryManager();

        loadListeners();
    }

    private void loadListeners(){
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new PlayerInteractListener(), this);
        manager.registerEvents(new InventoryClickListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }

    public static Lobbyswitcher getInstance() {
        return instance;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
