package com.piratamc.zzeight.lobby.action.actions;

import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.action.Action;
import com.piratamc.zzeight.lobby.utility.TextUtil;
import com.piratamc.zzeight.lobby.utility.reflection.ActionBar;

public class ActionbarAction implements Action {
	
	@Override
	public String getIdentifier() {
		return "ACTIONBAR";
	}
	
	@Override
	public void execute(PiratesLobby plugin, Player player, String data) {
		ActionBar.sendActionBar(player, TextUtil.color(data));
	}
}
