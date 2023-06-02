package com.piratamc.zzeight.lobby.action.actions;

import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.action.Action;

public class CloseInventoryAction implements Action {

    @Override
    public String getIdentifier() {
        return "CLOSE";
    }

    @Override
    public void execute(PiratesLobby plugin, Player player, String data) {
        player.closeInventory();
    }
}
