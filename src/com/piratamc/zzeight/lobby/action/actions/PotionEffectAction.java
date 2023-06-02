package com.piratamc.zzeight.lobby.action.actions;

import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.action.Action;
import com.piratamc.zzeight.lobby.utility.universal.XPotion;

public class PotionEffectAction implements Action {

    @Override
    public String getIdentifier() {
        return "EFFECT";
    }

    @Override
    public void execute(PiratesLobby plugin, Player player, String data) {
        String[] args = data.split(";");
        player.addPotionEffect(XPotion.matchXPotion(args[0]).get().parsePotion(1000000, Integer.parseInt(args[1]) - 1));
    }
}