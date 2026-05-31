package me.example.hidenseek.listeners;

import me.example.hidenseek.game.GameManager;
import me.example.hidenseek.gui.BlockSelectGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GUIListener implements Listener {

    private final BlockSelectGUI gui;

    public GUIListener(GameManager game) {
        this.gui = new BlockSelectGUI(game);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        String title = e.getView().getTitle();
        if (!title.startsWith("§8")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;

        Player p = (Player) e.getWhoClicked();
        Material mat = e.getCurrentItem().getType();

        // CATEGORY NAVIGATION
        switch (title) {

            case "§8Choose Category":
                if (mat == Material.OAK_LEAVES) gui.openNature(p);
                if (mat == Material.BOOKSHELF) gui.openBlocks(p);
                return;

            case "§8Nature Blocks":
            case "§8Block Props":

                if (mat == Material.ARROW) {
                    gui.open(p);
                    return;
                }

                gui.select(p, mat);
                return;
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getView().getTitle().startsWith("§8")) {
            e.setCancelled(true);
        }
    }
}
