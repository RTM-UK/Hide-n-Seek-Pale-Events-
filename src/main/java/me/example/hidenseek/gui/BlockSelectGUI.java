package me.example.hidenseek.gui;

import me.example.hidenseek.game.GameManager;
import me.example.hidenseek.game.PlayerData;
import me.example.hidenseek.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BlockSelectGUI {

    private final GameManager game;

    public BlockSelectGUI(GameManager game) {
        this.game = game;
    }

    // =====================================================
    // OPEN MAIN CATEGORY MENU
    // =====================================================
    public void open(Player p) {

        Inventory inv = Bukkit.createInventory(null, 9, "§8Choose Category");

        inv.setItem(2, item(Material.OAK_LEAVES, "§aNature", "§7Leaves, plants, etc."));
        inv.setItem(4, item(Material.BOOKSHELF, "§eBlocks", "§7Furniture & structures"));
        inv.setItem(6, item(Material.BARREL, "§bProps", "§7Misc disguises"));

        p.openInventory(inv);
    }

    // =====================================================
    // CATEGORY PAGES
    // =====================================================
    public void openNature(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "§8Nature Blocks");

        inv.setItem(2, item(Material.OAK_LEAVES, "§aOak Leaves", "§7Click to select"));
        inv.setItem(3, item(Material.SPRUCE_LEAVES, "§aSpruce Leaves", "§7Click to select"));

        inv.setItem(8, item(Material.ARROW, "§7Back", "§cReturn"));

        p.openInventory(inv);
    }

    public void openBlocks(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "§8Block Props");

        inv.setItem(3, item(Material.BOOKSHELF, "§eBookshelf", "§7Click to select"));
        inv.setItem(5, item(Material.BARREL, "§bBarrel", "§7Click to select"));

        inv.setItem(8, item(Material.ARROW, "§7Back", "§cReturn"));

        p.openInventory(inv);
    }

    // =====================================================
    // ITEM BUILDER
    // =====================================================
    private ItemStack item(Material mat, String name, String lore) {

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(List.of(lore));

        item.setItemMeta(meta);
        return item;
    }

    // =====================================================
    // SELECT BLOCK
    // =====================================================
    public void select(Player p, Material mat) {

        PlayerData d = game.data.get(p.getUniqueId());
        if (d == null) return;

        d.block = mat;

        p.closeInventory();

        MessageUtil.success(p, "Selected: §f" + mat.name());
        MessageUtil.actionGood(p, "Disguise locked!");
    }
}
