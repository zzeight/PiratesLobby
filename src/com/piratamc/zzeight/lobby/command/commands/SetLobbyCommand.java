package com.piratamc.zzeight.lobby.command.commands;

import cl.bgm.minecraft.util.commands.CommandContext;
import cl.bgm.minecraft.util.commands.annotations.Command;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PermissionsHub;
import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.Messages;
import com.piratamc.zzeight.lobby.utility.TextUtil;
import com.piratamc.zzeight.lobby.module.ModuleType;
import com.piratamc.zzeight.lobby.module.modules.world.LobbySpawn;

public class SetLobbyCommand {

    private final PiratesLobby plugin;

    public SetLobbyCommand(PiratesLobby plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"setlobby"},
            desc = "Seta a localização do lobby"
    )
    public void setlobby(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!sender.hasPermission(PermissionsHub.COMMAND_SET_LOBBY.getPermission())) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot set the spawn location.");
            return;
        }

        Player player = (Player) sender;
        if (plugin.getModuleManager().getDisabledWorlds().contains(player.getWorld().getName())) {
            sender.sendMessage(TextUtil.color("&cVocê não pode setar o lobby num mundo desativado."));
            return;
        }

        LobbySpawn lobbyModule = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY));
        lobbyModule.setLocation(player.getLocation());
        Messages.SET_LOBBY.send(sender);

    }

}