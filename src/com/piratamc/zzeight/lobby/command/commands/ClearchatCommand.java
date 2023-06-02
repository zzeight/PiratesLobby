package com.piratamc.zzeight.lobby.command.commands;

import cl.bgm.minecraft.util.commands.CommandContext;
import cl.bgm.minecraft.util.commands.annotations.Command;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PermissionsHub;
import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.Messages;

public class ClearchatCommand {

    public ClearchatCommand(PiratesLobby plugin) {
    }

    @Command(
            aliases = {"clearchat"},
            desc = "Limpa o chat global ou do jogador",
            usage = "[player]",
            max = 1
    )
    public void clearchat(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!(sender.hasPermission(PermissionsHub.COMMAND_CLEARCHAT.getPermission()))) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }

        if (args.argsLength() == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (int i = 0; i < 100; i++) {
                    player.sendMessage("");
                }
                Messages.CLEARCHAT.send(player, "%player%", sender.getName());
            }
        } else if (args.argsLength() == 1) {

            Player player = Bukkit.getPlayer(args.getString(0));
            if (player == null) {
                Messages.INVALID_PLAYER.send(sender, "%player%", args.getString(0));
                return;
            }

            for (int i = 0; i < 100; i++) {
                player.sendMessage("");
            }
            Messages.CLEARCHAT_PLAYER.send(sender, "%player%", sender.getName());
        }
    }
}

