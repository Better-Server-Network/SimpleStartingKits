package org.betterservernetwork.simplestartingkits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class KitHolder implements InventoryHolder {
    public Player player;
    public boolean save;

    public KitHolder(Player player, boolean save) {
        this.player = player;
        this.save = save;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
