package com.piratamc.zzeight.lobby.module.modules.hotbar.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piratamc.zzeight.lobby.config.ConfigType;
import com.piratamc.zzeight.lobby.module.modules.hotbar.HotbarItem;
import com.piratamc.zzeight.lobby.module.modules.hotbar.HotbarManager;

import java.util.List;

public class CustomItem extends HotbarItem {

    private List<String> actions;

    public CustomItem(HotbarManager hotbarManager, ItemStack item, int slot, String key) {
        super(hotbarManager, item, slot, key);
        actions = getPlugin().getConfigManager().getFile(ConfigType.SETTINGS).getConfig().getStringList("custom_join_items.items." + key + ".actions");
    }

    @Override
    protected void onInteract(Player player) {
        getPlugin().getActionManager().executeActions(player, actions);
    }
}
