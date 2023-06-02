package com.piratamc.zzeight.lobby.action;

import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;

public interface Action {
	
	String getIdentifier();
	
	void execute(PiratesLobby plugin, Player player, String data);
}
