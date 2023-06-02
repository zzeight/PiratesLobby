package com.piratamc.zzeight.lobby.action.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.action.Action;
import com.piratamc.zzeight.lobby.utility.TextUtil;

public class BroadcastMessageAction implements Action {

    @Override
    public String getIdentifier() {
        return "BROADCAST";
    }

    @Override
    public void execute(PiratesLobby plugin, Player player, String data) {
        if (data.contains("<center>") && data.contains("</center>")) data = TextUtil.getCenteredMessage(data);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(TextUtil.color(data));
        }
    }
}