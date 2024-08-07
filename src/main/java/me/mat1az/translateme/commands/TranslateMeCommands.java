package me.mat1az.translateme.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.mat1az.translateme.models.Color;
import me.mat1az.translateme.models.ColorSet;
import me.mat1az.translateme.models.UserColor;
import me.mat1az.translateme.services.TranslateService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TranslateMeCommands implements BasicCommand {

    private final JavaPlugin plugin;

    public TranslateMeCommands(JavaPlugin main) {
        this.plugin = main;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length > 0 && stack.getExecutor() instanceof Player p) {
            TranslateService ts = TranslateService.getInstance();
            switch (args[0]) {
                case "color" -> {
                    if (args.length > 1) {
                        try {
                            int a = Integer.parseInt(args[1]);
                            Integer[] dColors = Objects.requireNonNull(plugin.getConfig().getList("DEFAULT_COLOR_SET")).toArray(new Integer[0]);
                            if (ts.setUserColor(p.getUniqueId(), new UserColor(a, dColors[1], dColors[2])) > 0) {
                                p.sendRichMessage('[' + plugin.getName() + "]<green> Color changed successfully.");
                            } else {
                                p.sendRichMessage('[' + plugin.getName() + "]<red> Error changing your color. Please contact an administrator.");
                            }

                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
                case "admin" -> {
                    if (p.hasPermission(plugin.getName() + ".admin")) {
                        String f = args[1];
                        switch (f) {
                            case "sel" -> {
                                p.sendMessage(ts.getMessage(Integer.parseInt(args[2]), p.getUniqueId(), "Replace1"));
                                p.sendRichMessage('[' + plugin.getName() + "] " + ts.getMessage(Integer.parseInt(args[2]), p.getUniqueId(), "ReplaceTest").content());
                            }
                            case "reload" -> {
                                plugin.reloadConfig();
                                p.sendRichMessage('[' + plugin.getName() + "] Config reloaded.");
                                if (ts.reloadDB()) {
                                    p.sendRichMessage('[' + plugin.getName() + "] Database reloaded.");
                                }
                            }
                        }
                    }
                }
                //DEBUG
                case "usercolor" -> {
                    p.sendRichMessage('[' + plugin.getName() + "] " + ts.getUserColor(p.getUniqueId()));
                }
                case "colors" -> {
                    for (Color c : ts.getColors()) {
                        p.sendRichMessage('[' + plugin.getName() + "] " + c);
                    }
                }
                case "colorset" -> {
                    for (ColorSet c : ts.getColorSets()) {
                        p.sendRichMessage('[' + plugin.getName() + "] " + c);
                    }
                }
                //END DEBUG
                case "gui" -> {
                    Inventory inv = plugin.getServer().createInventory(null, InventoryType.HOPPER, MiniMessage.builder().build().deserialize("<rainbow>" + plugin.getName() + "</rainbow>"));
                    ItemStack language = new ItemStack(Material.CHERRY_SIGN);
                    ItemStack color = new ItemStack(Material.ORANGE_CONCRETE_POWDER);
                    ItemMeta l = language.getItemMeta();
                    ItemMeta c = color.getItemMeta();
                    ColorSet cs = ts.getColorSet(plugin.getConfig().getIntegerList("DEFAULT_COLOR_SET").getFirst());
                    l.displayName(MiniMessage.builder().build().deserialize("<gradient:" + cs.getA().getValue() + ":" + cs.getB().getValue() + ">Language Select</gradient>"));
                    c.displayName(MiniMessage.builder().build().deserialize("<gradient:" + cs.getA().getValue() + ":" + cs.getB().getValue() + ">Color Select</gradient>"));
                    l.setCustomModelData(178765);
                    c.setCustomModelData(245645);
                    language.setItemMeta(l);
                    color.setItemMeta(c);
                    inv.addItem(language);
                    inv.addItem(color);
                    p.openInventory(inv);
                }
                case "language" -> {
                    if (args.length > 1) {
                        try {
                            int language = Integer.parseInt(args[1]);
                            if (ts.setLanguage(p.getUniqueId(), language) > 0) {
                                p.sendRichMessage('[' + plugin.getName() + "]<green> Language changed successfully.");
                            } else {
                                p.sendRichMessage('[' + plugin.getName() + "]<red> Error changing your language. Please contact an administrator.");
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
                default -> {
                    p.sendRichMessage('[' + plugin.getName() + "]<red> Bad usage.");
                }
            }
        }
    }
}
