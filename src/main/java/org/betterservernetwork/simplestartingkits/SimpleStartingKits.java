package org.betterservernetwork.simplestartingkits;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SimpleStartingKits extends JavaPlugin {
    KitCommand command;
    KitHandler handler;

    @Override
    public void onEnable() {
        command = new KitCommand();
        handler = new KitHandler();

        Bukkit.getPluginManager().registerEvents(command, this);
        Bukkit.getPluginManager().registerEvents(handler, this);

        Objects.requireNonNull(getCommand("kit")).setExecutor(command);
        Objects.requireNonNull(getCommand("kit")).setTabCompleter(command);

        System.out.println("Enabled.");
    }

    @Override
    public void onDisable() {
        command.onDisable();
        handler.onDisable();
    }
}
