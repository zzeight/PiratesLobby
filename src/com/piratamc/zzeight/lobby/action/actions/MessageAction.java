package com.piratamc.zzeight.lobby.action.actions;

import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.action.Action;
import com.piratamc.zzeight.lobby.utility.TextUtil;

public class MessageAction implements Action {

    @Override
    public String getIdentifier() {
        return "MESSAGE";
    }

    @Override
    public void execute(PiratesLobby plugin, Player player, String data) {
        if (data.contains("<center>") && data.contains("</center>")) data = TextUtil.getCenteredMessage(data);
        player.sendMessage(TextUtil.color(data));
    }
}
