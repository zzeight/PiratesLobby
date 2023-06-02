package com.piratamc.zzeight.lobby.module;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.ConfigType;
import com.piratamc.zzeight.lobby.module.modules.chat.AntiSwear;
import com.piratamc.zzeight.lobby.module.modules.chat.AutoBroadcast;
import com.piratamc.zzeight.lobby.module.modules.chat.ChatCommandBlock;
import com.piratamc.zzeight.lobby.module.modules.chat.ChatLock;
import com.piratamc.zzeight.lobby.module.modules.chat.CustomCommands;
import com.piratamc.zzeight.lobby.module.modules.hologram.HologramManager;
import com.piratamc.zzeight.lobby.module.modules.hotbar.HotbarManager;
import com.piratamc.zzeight.lobby.module.modules.player.DoubleJump;
import com.piratamc.zzeight.lobby.module.modules.player.PlayerListener;
import com.piratamc.zzeight.lobby.module.modules.player.PlayerVanish;
import com.piratamc.zzeight.lobby.module.modules.visual.scoreboard.ScoreboardManager;
import com.piratamc.zzeight.lobby.module.modules.visual.tablist.TablistManager;
import com.piratamc.zzeight.lobby.module.modules.world.AntiWorldDownloader;
import com.piratamc.zzeight.lobby.module.modules.world.Launchpad;
import com.piratamc.zzeight.lobby.module.modules.world.LobbySpawn;
import com.piratamc.zzeight.lobby.module.modules.world.WorldProtect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleManager {

    private PiratesLobby plugin;
    private List<String> disabledWorlds;
    private Map<ModuleType, Module> modules = new HashMap<>();

    public void loadModules(PiratesLobby plugin) {
        this.plugin = plugin;

        if (!modules.isEmpty()) unloadModules();

        FileConfiguration config = plugin.getConfigManager().getFile(ConfigType.SETTINGS).getConfig();
        disabledWorlds = config.getStringList("disabled-worlds.worlds");

        if (config.getBoolean("disabled-worlds.invert")) {
            disabledWorlds = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
                for (String world : config.getStringList("disabled-worlds.worlds")) disabledWorlds.remove(world);
        }

        registerModule(new AntiWorldDownloader(plugin), "anti_wdl.enabled");
        registerModule(new DoubleJump(plugin), "double_jump.enabled");
        registerModule(new Launchpad(plugin), "launchpad.enabled");
        registerModule(new ScoreboardManager(plugin), "scoreboard.enabled");
        registerModule(new TablistManager(plugin), "tablist.enabled");
        registerModule(new AutoBroadcast(plugin), "announcements.enabled");
        registerModule(new AntiSwear(plugin), "anti_swear.enabled");
        registerModule(new ChatCommandBlock(plugin), "command_block.enabled");
        registerModule(new ChatLock(plugin));
        registerModule(new CustomCommands(plugin));
        registerModule(new PlayerListener(plugin));
        registerModule(new HotbarManager(plugin));
        registerModule(new WorldProtect(plugin));
        registerModule(new LobbySpawn(plugin));
        registerModule(new PlayerVanish(plugin));
        registerModule(new HologramManager(plugin));

        for (Module module : modules.values()) {
            try {
                module.setDisabledWorlds(disabledWorlds);
                module.onEnable();
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("============= PIRATESHUB MODULE LOAD ERROR =============");
                plugin.getLogger().severe("Ocorreu um erro ao carregar o módulo " + module.getModuleType() + "");
                plugin.getLogger().severe("Plugin vai se desligar..");
                plugin.getLogger().severe("============= PIRATESHUB MODULE LOAD ERROR =============");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                break;
            }
        }

        plugin.getLogger().info("Loaded " + modules.size() + " plugin modules.");
    }

    public void unloadModules() {
        for (Module module : modules.values()) {
            try {
                HandlerList.unregisterAll(module);
                module.onDisable();
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("There was an error unloading the " + module.getModuleType().toString() + " module.");
            }
        }
        modules.clear();
    }

    public Module getModule(ModuleType type) {
        return modules.get(type);
    }

    public void registerModule(Module module) {
        registerModule(module, null);
    }

    public void registerModule(Module module, String isEnabledPath) {
    	PiratesLobby plugin = module.getPlugin();
        if (isEnabledPath != null && !plugin.getConfigManager().getFile(ConfigType.SETTINGS).getConfig().getBoolean(isEnabledPath, false))
            return;

        plugin.getServer().getPluginManager().registerEvents(module, plugin);
        modules.put(module.getModuleType(), module);
    }

    public boolean isEnabled(ModuleType type) {
        return modules.containsKey(type);
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }
}
