package com.piratamc.zzeight.lobby.utility.reflection;

import org.bukkit.entity.ArmorStand;

import com.piratamc.zzeight.lobby.utility.universal.XMaterial;

public class ArmorStandName {

    public static String getName(ArmorStand stand) {
        if (XMaterial.supports(8)) return stand.getCustomName();

        String name = null;
        try {
            name = (String) ArmorStand.class.getMethod("getCustomName").invoke(stand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

}
