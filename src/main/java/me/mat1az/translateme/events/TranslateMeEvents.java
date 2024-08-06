package me.mat1az.translateme.events;

import me.mat1az.translateme.models.ColorSet;
import me.mat1az.translateme.models.Language;
import me.mat1az.translateme.services.TranslateService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class TranslateMeEvents implements Listener {

    private final JavaPlugin plugin;

    public TranslateMeEvents(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        //TODO Permissions here
        event.setLine(0, event.getLine(0).replace("&", "§"));
        event.setLine(1, event.getLine(1).replace("&", "§"));
        event.setLine(2, event.getLine(2).replace("&", "§"));
        event.setLine(3, event.getLine(3).replace("&", "§"));
    }

    @EventHandler
    public void onGUI(InventoryClickEvent e) {
        Component title = e.getView().title();
        if (title.equals(MiniMessage.builder().build().deserialize("<rainbow>" + plugin.getName() + "</rainbow>"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasCustomModelData()) {
                int id = e.getCurrentItem().getItemMeta().getCustomModelData();
                TranslateService ts = TranslateService.getInstance();
                Player p = (Player) e.getWhoClicked();
                switch (id) {
                    case 178765 -> {
                        Inventory inv = plugin.getServer().createInventory(null, InventoryType.HOPPER, title);
                        for (Language l : ts.getLanguages()) {
                            ItemStack b = new ItemStack(Material.WRITABLE_BOOK);
                            ItemMeta bm = b.getItemMeta();
                            bm.displayName(Component.text(l.getName()));
                            bm.setCustomModelData(Integer.parseInt(String.valueOf(id) + l.getID()));
                            b.setItemMeta(bm);
                            inv.addItem(b);
                        }
                        p.openInventory(inv);
                    }
                    case 245645 -> {
                        Inventory inv = plugin.getServer().createInventory(null, 54, title);
                        for (ColorSet c : ts.getColorSets()) {
                            ItemStack b = new ItemStack(Material.WRITABLE_BOOK);
                            ItemMeta bm = b.getItemMeta();
                            bm.displayName(MiniMessage.builder().build().deserialize("<gradient:" + c.getA().getValue() + ':' + c.getB().getValue() + '>' + c.getName() + "</gradient>"));
                            bm.setCustomModelData(Integer.parseInt(String.valueOf(id) + c.getID()));
                            b.setItemMeta(bm);
                            inv.addItem(b);
                        }
                        p.openInventory(inv);
                    }
                    default -> {
                        String s = String.valueOf(id);
                        String func = "";
                        if (s.contains("178765")) {
                            func = "language";
                        } else if (s.contains("245645")) {
                            func = "color";
                        }
                        if (!func.isBlank()) {
                            p.performCommand(plugin.getName().toLowerCase() + ' ' + func + ' ' + s.substring(6));
                        }
                    }
                }
            }
        }
    }
}
