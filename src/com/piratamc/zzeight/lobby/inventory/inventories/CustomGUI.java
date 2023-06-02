package com.piratamc.zzeight.lobby.inventory.inventories;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.inventory.AbstractInventory;
import com.piratamc.zzeight.lobby.inventory.InventoryBuilder;
import com.piratamc.zzeight.lobby.inventory.InventoryItem;
import com.piratamc.zzeight.lobby.utility.ItemStackBuilder;
import com.piratamc.zzeight.lobby.utility.TextUtil;

public class CustomGUI extends AbstractInventory {

    private InventoryBuilder inventory;
    private FileConfiguration config;

    public CustomGUI(PiratesLobby plugin, FileConfiguration config) {
        super(plugin);
        this.config = config;
    }

    @Override
    public void onEnable() {

        InventoryBuilder inventoryBuilder = new InventoryBuilder(config.getInt("slots"), TextUtil.color(config.getString("title")));

        if (config.contains("refresh") && config.getBoolean("refresh.enabled")) {
            setInventoryRefresh(config.getLong("refresh.rate"));
        }

        for (String entry : config.getConfigurationSection("items").getKeys(false)) {

            try {
                ItemStackBuilder builder = ItemStackBuilder.getItemStack(config.getConfigurationSection("items." + entry));

                InventoryItem inventoryItem;
                if (!config.contains("items." + entry + ".actions")) {
                    inventoryItem = new InventoryItem(builder.build());
                } else {
                    inventoryItem = new InventoryItem(builder.build()).addClickAction(p -> getPlugin().getActionManager().executeActions(p, config.getStringList("items." + entry + ".actions")));
                }

                if (config.contains("items." + entry + ".slots")) {
                    for (String slot : config.getStringList("items." + entry + ".slots")) {
                        inventoryBuilder.setItem(Integer.parseInt(slot), inventoryItem);
                    }
                } else if (config.contains("items." + entry + ".slot")) {
                    int slot = config.getInt("items." + entry + ".slot");
                    if (slot == -1) {
                        while (inventoryBuilder.getInventory().firstEmpty() != -1) {
                            inventoryBuilder.setItem(inventoryBuilder.getInventory().firstEmpty(), inventoryItem);
                        }
                    } else inventoryBuilder.setItem(slot, inventoryItem);
                }

            } catch (Exception e) {
                e.printStackTrace();
                getPlugin().getLogger().warning("Ocorreu um erro ao carregar o ID do item '" + entry + "', pulando..");
            }
        }

        inventory = inventoryBuilder;

    }

    @Override
    protected Inventory getInventory() {
        return inventory.getInventory();
    }
}
