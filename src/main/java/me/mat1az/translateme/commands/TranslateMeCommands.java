package me.mat1az.translateme.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.mat1az.translateme.models.Color;
import me.mat1az.translateme.models.UserColor;
import me.mat1az.translateme.services.TranslateService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TranslateMeCommands implements BasicCommand {

    private final JavaPlugin plugin;

    public TranslateMeCommands(JavaPlugin main) {
        this.plugin = main;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if (args.length > 0 && stack.getSender() instanceof Player p) {
            //TODO: Permissions!!!
            TranslateService ts = TranslateService.getInstance();
            switch (args[0]) {
                case "color" -> {
                    if (args.length > 1) {
                        try {
                            int a = Integer.parseInt(args[1]);
                            int b = Integer.parseInt(args[2]);
                            int c = Integer.parseInt(args[3]);
                            if (ts.setUserColor(p.getUniqueId(), new UserColor(a, b, c)) > 0) {
                                p.sendRichMessage('[' + plugin.getName() + "]<green> Color changed successfully.");
                            } else {
                                p.sendRichMessage('[' + plugin.getName() + "]<red> Error changing your color. Please contact an administrator.");
                            }

                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
                case "reload" -> {
                    plugin.reloadConfig();
                    p.sendRichMessage('[' + plugin.getName() + "]<green> Config reloaded.");
                }
                //DEBUG
                case "message" -> {
                    if (args.length > 1) {
                        try {
                            int id = Integer.parseInt(args[1]);
                            p.sendRichMessage('[' + plugin.getName() + "]<green> " + ts.getMessage(id, p.getUniqueId()));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
                case "usercolor" -> {
                    p.sendRichMessage('[' + plugin.getName() + "]<green> " + ts.getUserColor(p.getUniqueId()));
                }
                case "colors" -> {
                    for (Color c : ts.getColors()) {
                        p.sendRichMessage('[' + plugin.getName() + "]<green> " + c);
                    }
                }
                default -> {
                    try {
                        int language = Integer.parseInt(args[0]);
                        if (ts.setLanguage(p.getUniqueId(), language) > 0) {
                            p.sendRichMessage('[' + plugin.getName() + "]<green> Language changed successfully.");
                        } else {
                            p.sendRichMessage('[' + plugin.getName() + "]<red> Error changing your language. Please contact an administrator.");
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
    }
}
