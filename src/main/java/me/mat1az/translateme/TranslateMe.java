package me.mat1az.translateme;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.mat1az.translateme.commands.TranslateMeCommands;
import me.mat1az.translateme.events.TranslateMeEvents;
import me.mat1az.translateme.services.TranslateService;
import me.mat1az.translateme.utils.DBHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TranslateMe extends JavaPlugin {

    @Override
    public void onEnable() {
        configSetup();
        dbSetup();
        registerCommands();
        registerEvents();
        TranslateService.instance = new TranslateService(this);
        backupSetup();
    }

    @Override
    public void onDisable() {
        TranslateService.instance.backup();
    }

    private void dbSetup() {
        DBHelper.getInstance(this).initDBInstance();
    }

    private void configSetup() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
            getLogger().info("Created config.yml file.");
        }
    }

    /**
     * Backup database into file every 2 minutes
     */
    public void backupSetup() {
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (TranslateService.instance.backup()) {
                this.getServer().broadcast(Component.text('[' + getPluginMeta().getName() + "] Database saved."));
            }
        }, 0, 20 * 120);
    }

    private void registerCommands() {
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(this.getPluginMeta().getName().toLowerCase(), new TranslateMeCommands(this));
        });
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new TranslateMeEvents(this), this);
    }
}
