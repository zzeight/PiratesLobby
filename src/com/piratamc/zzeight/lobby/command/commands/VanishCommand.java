package com.piratamc.zzeight.lobby.command.commands;

import cl.bgm.minecraft.util.commands.CommandContext;
import cl.bgm.minecraft.util.commands.annotations.Command;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PermissionsHub;
import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.Messages;
import com.piratamc.zzeight.lobby.module.ModuleType;
import com.piratamc.zzeight.lobby.module.modules.player.PlayerVanish;

public class VanishCommand {

    private PiratesLobby plugin;

    public VanishCommand(PiratesLobby plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"vanish"},
            desc = "Desaparece no meio do ar!"
    )
    public void vanish(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!sender.hasPermission(PermissionsHub.COMMAND_VANISH.getPermission())) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot set the spawn location.");
            return;
        }

        Player player = (Player) sender;
        PlayerVanish vanishModule = ((PlayerVanish) plugin.getModuleManager().getModule(ModuleType.VANISH));
        vanishModule.toggleVanish(player);
    }

}
