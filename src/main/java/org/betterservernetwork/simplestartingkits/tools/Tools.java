package org.betterservernetwork.simplestartingkits.tools;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tools {
    public static boolean playerHasSpace(Player player, ItemStack item) {
        int freeSlots = 0;
        int maxStackSize = item.getMaxStackSize();

        for (ItemStack invItem : player.getInventory().getStorageContents()) {
            if (invItem == null) {
                freeSlots += maxStackSize;
            } else if (invItem.isSimilar(item)) {
                int amount = invItem.getAmount();
                freeSlots += maxStackSize - amount;
            }
        }

        return freeSlots >= item.getAmount();
    }
}
