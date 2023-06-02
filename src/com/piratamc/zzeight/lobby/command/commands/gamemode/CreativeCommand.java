package com.piratamc.zzeight.lobby.command.commands.gamemode;

import cl.bgm.minecraft.util.commands.CommandContext;
import cl.bgm.minecraft.util.commands.annotations.Command;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PermissionsHub;
import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.Messages;

public class CreativeCommand {

    public CreativeCommand(PiratesLobby plugin) {
    }

    @Command(
            aliases = {"gmc"},
            desc = "Muda para o modo criativo",
            usage = "[player]",
            max = 1
    )
    public void creative(final CommandContext args, final CommandSender sender) throws CommandException {
        if (args.argsLength() == 0) {
            if (!(sender instanceof Player)) throw new CommandException("Console cannot change gamemode");

            Player player = (Player) sender;
            if (!player.hasPermission(PermissionsHub.COMMAND_GAMEMODE.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", "CREATIVE");
            player.setGameMode(GameMode.CREATIVE);
        } else if (args.argsLength() == 1) {
            if (!sender.hasPermission(PermissionsHub.COMMAND_GAMEMODE_OTHERS.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            Player player = Bukkit.getPlayer(args.getString(0));
            if (player == null) {
                Messages.INVALID_PLAYER.send(sender, "%player%", args.getString(0));
                return;
            }

            if (sender.getName().equals(player.getName())) {
                Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", "CREATIVE");
            } else {
                Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", "CREATIVE");
                Messages.GAMEMODE_CHANGE_OTHER.send(sender, "%player%", player.getName(), "%gamemode%", "CREATIVE");
            }
            player.setGameMode(GameMode.CREATIVE);
        }
    }
}
