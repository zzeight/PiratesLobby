package com.piratamc.zzeight.lobby.command.commands;

import cl.bgm.minecraft.util.commands.CommandContext;
import cl.bgm.minecraft.util.commands.annotations.Command;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.module.ModuleType;
import com.piratamc.zzeight.lobby.module.modules.world.LobbySpawn;
import com.piratamc.zzeight.lobby.utility.TextUtil;

public class LobbyCommand {

    private PiratesLobby plugin;

    public LobbyCommand(PiratesLobby plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"lobby"},
            desc = "Teleporta para o lobby"
    )
    public void lobby(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot teleport to spawn");
            return;
        }

        Location location = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY)).getLocation();
        if (location == null) {
            sender.sendMessage(TextUtil.color("&cO spawn não foi setado. &7(/setlobby)&c."));
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> ((Player) sender).teleport(location), 3L);

    }

}
