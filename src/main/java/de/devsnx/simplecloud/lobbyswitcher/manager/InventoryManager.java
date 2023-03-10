package de.devsnx.simplecloud.lobbyswitcher.manager;

import de.devsnx.simplecloud.lobbyswitcher.Lobbyswitcher;
import de.devsnx.simplecloud.lobbyswitcher.utils.ItemCreator;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.service.ICloudService;
import eu.thesimplecloud.plugin.startup.CloudPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author DevSnx
 * @since 23.02.2023
 */
public class InventoryManager {

    private Inventory inv;

    public InventoryManager(){

        FileConfiguration cfg = Lobbyswitcher.getPlugin(Lobbyswitcher.class).getConfig();

        this.inv = Bukkit.createInventory(null,
                (cfg.getInt("gui.rows")*9),
                cfg.getString("gui.name"));

        refreshLobbys();
    }

    private void refreshLobbys(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Lobbyswitcher.getInstance(), new Runnable() {
            @Override
            public void run() {

                if(CloudAPI.getInstance().getCloudServiceGroupManager().getLobbyGroups() != null){

                    FileConfiguration cfg = Lobbyswitcher.getCfg();

                    int servers = 0;

                    for(ICloudService service : CloudAPI.getInstance().getCloudServiceManager().getAllCachedObjects()){

                        if(service.getName().startsWith("Lobby")){

                            if(service.isOnline()){

                                if(CloudPlugin.getInstance().thisService().getName().equalsIgnoreCase(service.getName())){

                                    ArrayList<String> lore = new ArrayList<>();

                                    for(String message : cfg.getStringList("layout.connected.lore")){
                                        message = message.replace("%MAXPLAYER%", String.valueOf(service.getMaxPlayers()));
                                        message = message.replace("%ONLINEPLAYER%", String.valueOf(service.getOnlineCount()));
                                        message = message.replace("%MOTD%", service.getMOTD());

                                        lore.add(message);
                                    }

                                    inv.setItem(servers, new ItemCreator().displayName(cfg.getString("gui.item-color") + service.getName()).material(Material.valueOf(cfg.getString("layout.connected.item"))).lore(lore).build());

                                    servers++;

                                }else{

                                    //show-starting-servers
                                    if(Lobbyswitcher.getInstance().getConfig().getBoolean("general.show-starting-servers") == true){

                                        if(service.isStartingOrVisible()){

                                            if(service.getOnlineCount() == service.getMaxPlayers()){

                                                ArrayList<String> lore = new ArrayList<>();

                                                for(String message : cfg.getStringList("layout.full.lore")){
                                                    message = message.replace("%MAXPLAYER%", String.valueOf(service.getMaxPlayers()));
                                                    message = message.replace("%ONLINEPLAYER%", String.valueOf(service.getOnlineCount()));
                                                    message = message.replace("%MOTD%", service.getMOTD());

                                                    lore.add(message);
                                                }

                                                inv.setItem(servers, new ItemCreator().displayName(cfg.getString("gui.item-color") + service.getName()).material(Material.valueOf(cfg.getString("layout.empty.item"))).lore(lore).build());

                                                servers++;

                                            }else if(service.getOnlineCount() < service.getMaxPlayers()){

                                                ArrayList<String> lore = new ArrayList<>();

                                                for(String message : cfg.getStringList("layout.empty.lore")){
                                                    message = message.replace("%MAXPLAYER%", String.valueOf(service.getMaxPlayers()));
                                                    message = message.replace("%ONLINEPLAYER%", String.valueOf(service.getOnlineCount()));
                                                    message = message.replace("%MOTD%", service.getMOTD());

                                                    lore.add(message);
                                                }

                                                inv.setItem(servers, new ItemCreator().displayName(cfg.getString("gui.item-color") + service.getName()).material(Material.valueOf(cfg.getString("layout.empty.item"))).lore(lore).build());

                                                servers++;

                                            }

                                        }

                                    }

                                    if(Lobbyswitcher.getInstance().getConfig().getBoolean("general.show-offline-servers") == true){

                                        if(service.getOnlineCount() == service.getMaxPlayers()){

                                            ArrayList<String> lore = new ArrayList<>();

                                            for(String message : cfg.getStringList("layout.full.lore")){
                                                message = message.replace("%MAXPLAYER%", String.valueOf(service.getMaxPlayers()));
                                                message = message.replace("%ONLINEPLAYER%", String.valueOf(service.getOnlineCount()));
                                                message = message.replace("%MOTD%", service.getMOTD());

                                                lore.add(message);
                                            }

                                            inv.setItem(servers, new ItemCreator().displayName(cfg.getString("gui.item-color") + service.getName()).material(Material.valueOf(cfg.getString("layout.empty.item"))).lore(lore).build());

                                            servers++;

                                        }else if(service.getOnlineCount() < service.getMaxPlayers()){

                                            ArrayList<String> lore = new ArrayList<>();

                                            for(String message : cfg.getStringList("layout.empty.lore")){
                                                message = message.replace("%MAXPLAYER%", String.valueOf(service.getMaxPlayers()));
                                                message = message.replace("%ONLINEPLAYER%", String.valueOf(service.getOnlineCount()));
                                                message = message.replace("%MOTD%", service.getMOTD());

                                                lore.add(message);
                                            }

                                            inv.setItem(servers, new ItemCreator().displayName(cfg.getString("gui.item-color") + service.getName()).material(Material.valueOf(cfg.getString("layout.empty.item"))).lore(lore).build());

                                            servers++;

                                        }

                                    }

                                }

                            }else{

                                ArrayList<String> lore = new ArrayList<>();

                                for(String message : cfg.getStringList("layout.offline.lore")){
                                    message = message.replace("%MAXPLAYER%", String.valueOf(service.getMaxPlayers()));
                                    message = message.replace("%ONLINEPLAYER%", String.valueOf(service.getOnlineCount()));
                                    message = message.replace("%MOTD%", service.getMOTD());

                                    lore.add(message);
                                }

                                inv.setItem(servers, new ItemCreator().displayName(cfg.getString("gui.item-color") + service.getName()).material(Material.valueOf(cfg.getString("layout.offline.item"))).lore(lore).build());

                                servers++;

                            }

                        }

                    }

                }else{

                    inv.setItem(4, new ItemCreator().material(Material.valueOf("RED_WOOL")).displayName("??cKeine Lobbyserver gefunden!").build());

                }

            }

        }, 60L, 1);

    }


    public Inventory getInv() {

        return inv;

    }

}
