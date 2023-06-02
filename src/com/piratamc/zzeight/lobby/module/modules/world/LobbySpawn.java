package com.piratamc.zzeight.lobby.module.modules.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.ConfigType;
import com.piratamc.zzeight.lobby.module.Module;
import com.piratamc.zzeight.lobby.module.ModuleType;

public class LobbySpawn extends Module {

    private boolean spawnJoin;
    private Location location = null;

    public LobbySpawn(PiratesLobby plugin) {
        super(plugin, ModuleType.LOBBY);
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTask(getPlugin(), () -> {
            FileConfiguration config = getConfig(ConfigType.DATA);
            if (config.contains("spawn")) location = (Location) config.get("spawn");
        });
        spawnJoin = getConfig(ConfigType.SETTINGS).getBoolean("join_settings.spawn_join", false);
    }

    @Override
    public void onDisable() {
        getConfig(ConfigType.DATA).set("spawn", location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (spawnJoin && location != null) player.teleport(location);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (location != null && !inDisabledWorld(player.getLocation())) event.setRespawnLocation(location);
    }
}
