package org.betterservernetwork.simplestartingkits;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class KitHandler implements Listener {
    ArrayList<String> players = new ArrayList<>();

    public KitHandler() {
        File file;

        try {
            file = new File("plugins/StartingKit/players.yaml");
        } catch (Exception ignored){
            System.out.println(
                    "Error loading data file (if this is your first time using the plugin then ignore this).");

            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        players.addAll(config.getKeys(false));

        System.out.println("Loaded Player Kit data.");
    }

    public void onDisable() {
        File file = new File("plugins/StartingKit/players.yaml");
        YamlConfiguration config = new YamlConfiguration();

        for (String uuid : players) {
            config.set(uuid, true);
        }

        try {
            config.save(file);
        } catch (IOException ignored) {
            System.out.println("Unexpected Error Saving Data");
        }

        System.out.println("Saved Player Kit data.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!players.contains(event.getPlayer().getUniqueId().toString())) {
            KitCommand.instance.openInventory(event.getPlayer(), false);

            players.add(event.getPlayer().getUniqueId().toString());
        }
    }
}
