package com.piratamc.zzeight.lobby.action.actions;

import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.action.Action;
import com.piratamc.zzeight.lobby.inventory.AbstractInventory;

public class MenuAction implements Action {

    @Override
    public String getIdentifier() {
        return "MENU";
    }

    @Override
    public void execute(PiratesLobby plugin, Player player, String data) {
        AbstractInventory inventory = plugin.getInventoryManager().getInventory(data);

        if (inventory != null) {
            inventory.openInventory(player);
        } else {
            plugin.getLogger().warning("[MENU] Action falhou: Menu '" + data + "' não foi encontrado.");
        }
    }
}
