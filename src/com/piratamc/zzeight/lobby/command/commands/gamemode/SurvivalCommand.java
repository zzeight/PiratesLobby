package com.piratamc.zzeight.lobby.command.commands.gamemode;

import cl.bgm.minecraft.util.commands.CommandContext;
import cl.bgm.minecraft.util.commands.annotations.Command;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;

import com.piratamc.zzeight.lobby.PermissionsHub;
import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.Messages;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SurvivalCommand {

    public SurvivalCommand(PiratesLobby plugin) {
    }

    @Command(
            aliases = {"gms"},
            desc = "Muda para o modo survival",
            usage = "[player]",
            max = 1
    )
    public void survival(final CommandContext args, final CommandSender sender) throws CommandException {
        if (args.argsLength() == 0) {
            if (!(sender instanceof Player)) throw new CommandException("Console cannot change gamemode");

            Player player = (Player) sender;
            if (!player.hasPermission(PermissionsHub.COMMAND_GAMEMODE.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", "SURVIVAL");
            player.setGameMode(GameMode.SURVIVAL);
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

            Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", "SURVIVAL");
            Messages.GAMEMODE_CHANGE_OTHER.send(sender, "%player%", player.getName(), "%gamemode%", "SURVIVAL");
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}
