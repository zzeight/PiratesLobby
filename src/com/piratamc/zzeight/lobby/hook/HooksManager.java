package com.piratamc.zzeight.lobby.hook;

import org.bukkit.Bukkit;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.hook.hooks.head.BaseHead;
import com.piratamc.zzeight.lobby.hook.hooks.head.DatabaseHead;
import com.piratamc.zzeight.lobby.utility.PlaceholderUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HooksManager {

    private Map<String, PluginHook> hooks;

    public HooksManager(PiratesLobby plugin) {
        hooks = new HashMap<>();

        // Base64 head
        hooks.put("BASE64", new BaseHead());

        // PlaceholderAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            hooks.put("PLACEHOLDER_API", null);
            PlaceholderUtil.PAPI = true;
            plugin.getLogger().info(" Hooked into PlaceholderAPI");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            hooks.put("HEAD_DATABASE", new DatabaseHead());
            plugin.getLogger().info(" Hooked into HeadDatabase");
        }

        hooks.values().stream().filter(Objects::nonNull).forEach(pluginHook -> pluginHook.onEnable(plugin));

    }

    public boolean isHookEnabled(String id) {
        return hooks.containsKey(id);
    }

    public PluginHook getPluginHook(String id) {
        return hooks.get(id);
    }

}
