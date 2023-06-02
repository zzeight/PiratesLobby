package com.piratamc.zzeight.lobby.command.commands;

import cl.bgm.minecraft.util.commands.CommandContext;
import cl.bgm.minecraft.util.commands.annotations.Command;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.piratamc.zzeight.lobby.PermissionsHub;
import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.config.Messages;
import com.piratamc.zzeight.lobby.module.ModuleType;
import com.piratamc.zzeight.lobby.module.modules.chat.ChatLock;

public class LockchatCommand {

    private final PiratesLobby plugin;

    public LockchatCommand(PiratesLobby plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"lockchat"},
            desc = "Bloqueia o chat"
    )
    public void lockchat(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!sender.hasPermission(PermissionsHub.COMMAND_LOCKCHAT.getPermission())) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }

        ChatLock chatLockModule = (ChatLock) plugin.getModuleManager().getModule(ModuleType.CHAT_LOCK);

        if (chatLockModule.isChatLocked()) {
            Bukkit.getOnlinePlayers().forEach(player -> Messages.CHAT_UNLOCKED_BROADCAST.send(player, "%player%", sender.getName()));
            chatLockModule.setChatLocked(false);
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> Messages.CHAT_LOCKED_BROADCAST.send(player, "%player%", sender.getName()));
            chatLockModule.setChatLocked(true);
        }
    }
}