package com.piratamc.zzeight.lobby.module.modules.player;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.piratamc.zzeight.lobby.PermissionsHub;
import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.ConfigType;
import com.piratamc.zzeight.lobby.config.Messages;
import com.piratamc.zzeight.lobby.cooldown.CooldownType;
import com.piratamc.zzeight.lobby.module.Module;
import com.piratamc.zzeight.lobby.module.ModuleType;

import java.util.List;
import java.util.UUID;

public class DoubleJump extends Module {

    private long cooldownDelay;
    private double launch;
    private double launchY;
    private List<String> actions;

    public DoubleJump(PiratesLobby plugin) {
        super(plugin, ModuleType.DOUBLE_JUMP);
    }

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig(ConfigType.SETTINGS);
        cooldownDelay = config.getLong("double_jump.cooldown", 0);
        launch = config.getDouble("double_jump.launch_power", 1.3);
        launchY = config.getDouble("double_jump.launch_power_y", 1.2);
        actions = config.getStringList("double_jump.actions");

        if (launch > 4.0) launch = 4.0;
        if (launchY > 4.0) launchY = 4.0;
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {

        Player player = event.getPlayer();

        // Perform checks
        if (player.hasPermission(new Permission(PermissionsHub.DOUBLE_JUMP_BYPASS.getPermission(), PermissionDefault.FALSE))) return;
        else if (inDisabledWorld(player.getLocation())) return;
        else if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        else if (!event.isFlying()) return;
        else if (player.getWorld().getBlockAt(player.getLocation().subtract(0, 2, 0)).getType() == Material.AIR) {
            event.setCancelled(true);
            return;
        }

        // All pre-checks passed, now handle double jump
        event.setCancelled(true);

        // Check for cooldown
        UUID uuid = player.getUniqueId();
        if (!tryCooldown(uuid, CooldownType.DOUBLE_JUMP, cooldownDelay)) {
            Messages.DOUBLE_JUMP_COOLDOWN.send(player, "%time%", getCooldown(uuid, CooldownType.DOUBLE_JUMP));
            return;
        }

        // Execute double jump
        player.setVelocity(player.getLocation().getDirection().multiply(launch).setY(launchY));
        executeActions(player, actions);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR && !inDisabledWorld(player.getLocation())) {
            player.getPlayer().setAllowFlight(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR)
            player.getPlayer().setAllowFlight(true);
    }
}
