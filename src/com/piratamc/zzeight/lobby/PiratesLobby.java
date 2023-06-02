package com.piratamc.zzeight.lobby;

import bin.cl.bgm.minecraft.util.commands.exceptions.CommandException;
import bin.cl.bgm.minecraft.util.commands.exceptions.CommandPermissionsException;
import bin.cl.bgm.minecraft.util.commands.exceptions.CommandUsageException;
import bin.cl.bgm.minecraft.util.commands.exceptions.MissingNestedCommandException;
import bin.cl.bgm.minecraft.util.commands.exceptions.WrappedCommandException;

import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.piratamc.zzeight.lobby.action.ActionManager;
import com.piratamc.zzeight.lobby.command.CommandManager;
import com.piratamc.zzeight.lobby.config.ConfigManager;
import com.piratamc.zzeight.lobby.config.ConfigType;
import com.piratamc.zzeight.lobby.config.Messages;
import com.piratamc.zzeight.lobby.cooldown.CooldownManager;
import com.piratamc.zzeight.lobby.hook.HooksManager;
import com.piratamc.zzeight.lobby.inventory.InventoryManager;
import com.piratamc.zzeight.lobby.module.ModuleManager;
import com.piratamc.zzeight.lobby.module.ModuleType;
import com.piratamc.zzeight.lobby.module.modules.hologram.HologramManager;
import com.piratamc.zzeight.lobby.utility.UpdateChecker;

import de.tr7zw.nbtapi.utils.MinecraftVersion;

public class PiratesLobby extends JavaPlugin {
	
	private static final int BSTATS_ID = 3151;
	
	private ConfigManager configManager;
    private ActionManager actionManager;
    private HooksManager hooksManager;
    private CommandManager commandManager;
    private CooldownManager cooldownManager;
    private ModuleManager moduleManager;
    private InventoryManager inventoryManager;
	
	public void onEnable() {
		long start = System.currentTimeMillis();
		
		getLogger().log(Level.INFO, "");
		getLogger().log(Level.INFO, "PiratesHub");
		getLogger().log(Level.INFO, "Version: " + getDescription().getVersion());
        getLogger().log(Level.INFO, "Author: 008");
        getLogger().log(Level.INFO, "");
		
        // Checar se está a usar Spigot
        try {
        	Class.forName("org.spigotmc.SpigotConfig");
        } catch (ClassNotFoundException ex) {
        	getLogger().severe("============= SPIGOT NÃO FOI DETETADO =============");
            getLogger().severe("PiratesHub necessita do Spigot, você pode fazer download");
            getLogger().severe("do Spigor aqui: https://www.spigotmc.org/wiki/spigot-installation/.");
            getLogger().severe("O plugin vai se desligar agora.");
            getLogger().severe("============= SPIGOT NÃO FOI DETETADO =============");
            getPluginLoader().disablePlugin(this);
            return;
        }
        
        MinecraftVersion.disableUpdateCheck();
        
        // Enable bStats metrics
        new Metrics(this, BSTATS_ID);

        // Check plugin hooks
        hooksManager = new HooksManager(this);

        // Load config files
        configManager = new ConfigManager();
        configManager.loadFiles(this);

        // If there were any configuration errors we should not continue
        if (!getServer().getPluginManager().isPluginEnabled(this)) return;

        // Command manager
        commandManager = new CommandManager(this);
        commandManager.reload();

        // Cooldown manager
        cooldownManager = new CooldownManager();

        // Inventory (GUI) manager
        inventoryManager = new InventoryManager();
        if (!hooksManager.isHookEnabled("HEAD_DATABASE")) inventoryManager.onEnable(this);

        // Core plugin modules
        moduleManager = new ModuleManager();
        moduleManager.loadModules(this);

        // Action system
        actionManager = new ActionManager(this);

        // Load update checker (if enabled)
        if (getConfigManager().getFile(ConfigType.SETTINGS).getConfig().getBoolean("update-check"))
            new UpdateChecker(this).checkForUpdate();

        // Register BungeeCord channels
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getLogger().log(Level.INFO, "");
        getLogger().log(Level.INFO, "Successfully loaded in " + (System.currentTimeMillis() - start) + "ms");
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        moduleManager.unloadModules();
        inventoryManager.onDisable();
        configManager.saveFiles();

    }

    public void reload() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        configManager.reloadFiles();

        inventoryManager.onDisable();
        inventoryManager.onEnable(this);

        getCommandManager().reload();

        moduleManager.loadModules(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        try {
            getCommandManager().execute(cmd.getName(), args, sender);
        } catch (CommandPermissionsException e) {
            Messages.NO_PERMISSION.send(sender);
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            //sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + "Usage: " + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An internal error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    public HologramManager getHologramManager() {
        return (HologramManager) moduleManager.getModule(ModuleType.HOLOGRAMS);
    }

    public HooksManager getHookManager() {
        return hooksManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }
}