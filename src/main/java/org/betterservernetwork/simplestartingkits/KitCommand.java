package org.betterservernetwork.simplestartingkits;

import org.betterservernetwork.simplestartingkits.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter, Listener {
    public static KitCommand instance;

    HashMap<Integer, ItemStack> startingKit = new HashMap<>();

    public KitCommand() {
        instance = this;

        File file;

        try {
            file = new File("plugins/StartingKit/startingKit.yaml");
        } catch (Exception ignored) {
            System.out.println(
                    "Error loading data file (if this is your first time using the plugin then ignore this).");

            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            startingKit.put(Integer.parseInt(key), (ItemStack) config.get(key));
        }

        System.out.println("Loaded Kit data.");
    }

    public void onDisable() {
        File file = new File("plugins/StartingKit/startingKit.yaml");
        YamlConfiguration config = new YamlConfiguration();

        for (Integer slot : startingKit.keySet()) {
            config.set(slot + "", startingKit.get(slot));
        }

        try {
            config.save(file);
        } catch (IOException ignored) {
            System.out.println("Unexpected Error Saving Data");
        }

        System.out.println("Saved Kit data.");
    }

    public void openInventory(Player player, boolean save) {
        Inventory inventory = Bukkit.getServer().createInventory(new KitHolder(player, save), 27,
                save ? "Starting Kit (Editing)" : "Starting Kit");

        for (Integer slot : startingKit.keySet()) {
            inventory.setItem(slot, startingKit.get(slot));
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof KitHolder) {
            if (((KitHolder) event.getInventory().getHolder()).save) {
                startingKit.clear();
                ItemStack[] items = event.getInventory().getContents();
                for (int i = 0; i < items.length; i++) {
                    if (items[i] != null && items[i].getType() != Material.AIR) {
                        startingKit.put(i, items[i]);
                    }
                }
            } else {
                for (ItemStack item : event.getInventory().getContents()) {
                    if (item == null) continue;

                    Player player = ((KitHolder) event.getInventory().getHolder()).player;
                    if (Tools.playerHasSpace(player, item)) {
                        player.getInventory().addItem(item);
                    } else {
                        player.getLocation().getWorld().dropItem(player.getLocation(), item);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command, @NotNull String s,
            @NotNull String[] arguments) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;

        switch (arguments.length) {
            case 1:
                if (!arguments[0].equals("edit")) {
                    player.sendMessage(ChatColor.RED + "Correct usage: " +
                            ChatColor.RESET + "/kit (edit|give <player>)");
                    return true;
                }

                openInventory(player, true);
                break;
            case 2:
                if (!arguments[0].equals("give")) {
                    player.sendMessage(ChatColor.RED + "Correct usage: " +
                            ChatColor.RESET + "/kit (edit|give <player>)");
                    return true;
                }

                if (arguments[1].equals("@a")) {
                    for (Object currentPlayer : Bukkit.getServer().getOnlinePlayers().toArray()) {
                        openInventory((Player) currentPlayer, false);
                    }
                    return true;
                } else if (arguments[1].equals("@s")) {
                    openInventory(player, false);
                    return true;
                }

                Player givePlayer = Bukkit.getServer().getPlayer(arguments[1]);

                if (givePlayer == null) {
                    player.sendMessage(ChatColor.RED + "Player [" + arguments[1] + "] not found.");
                    return true;
                }

                openInventory(givePlayer, false);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Correct usage: " +
                        ChatColor.RESET + "/kit (edit|give <player>)");
                break;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] arguments) {
        if (arguments.length == 1) {
            ArrayList<String> array = new ArrayList<>();
            array.add("edit");
            array.add("give");
            return array;
        } else if (arguments.length == 2 && arguments[0].equals("give")) {
            ArrayList<String> array = new ArrayList<>();

            array.add("@a");
            array.add("@s");

            for (Player player :
                    Bukkit.getServer().getOnlinePlayers()) {
                array.add(player.getName());
            }

            return array;
        } else {
            return new ArrayList<>();
        }
    }
}
