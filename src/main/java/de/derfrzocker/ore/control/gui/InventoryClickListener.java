package de.derfrzocker.ore.control.gui;

import de.derfrzocker.ore.control.OreControl;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InventoryClickListener implements Listener {

    private final Set<HumanEntity> playerSet = Collections.synchronizedSet(new HashSet<>());

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof InventoryGui))
            return;

        event.setCancelled(true);

        if (playerSet.contains(event.getWhoClicked()))
            return;

        playerSet.add(event.getWhoClicked());

        Bukkit.getScheduler().runTaskAsynchronously(OreControl.getInstance(), () -> {
            try {
                ((InventoryGui) event.getView().getTopInventory().getHolder()).onInventoryClick(event);
            } catch (Exception e) {
                event.getWhoClicked().sendMessage("§4Error while execute gui action, see console for more information.");
                event.getWhoClicked().sendMessage("§4Please report the error to the Developer.");
                e.printStackTrace();
            } finally {
                playerSet.remove(event.getWhoClicked());
            }
        });
    }

}