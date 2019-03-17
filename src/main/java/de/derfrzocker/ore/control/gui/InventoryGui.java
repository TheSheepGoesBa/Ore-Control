package de.derfrzocker.ore.control.gui;

import de.derfrzocker.ore.control.OreControl;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.concurrent.ExecutionException;

interface InventoryGui extends InventoryHolder {

    void onInventoryClick(InventoryClickEvent event);

    default void openSync(HumanEntity entity, Inventory inventory) {
        try {
            Bukkit.getScheduler().callSyncMethod(OreControl.getInstance(), () -> entity.openInventory(inventory)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}