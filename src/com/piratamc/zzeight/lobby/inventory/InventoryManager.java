package com.piratamc.zzeight.lobby.inventory;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.inventory.inventories.CustomGUI;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryManager {

    private PiratesLobby plugin;

    private Map<String, AbstractInventory> inventories;

    public InventoryManager() {
        inventories = new HashMap<>();
    }

    public void onEnable(PiratesLobby plugin) {
        this.plugin = plugin;

        loadCustomMenus();

        inventories.values().forEach(AbstractInventory::onEnable);

        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
    }

    private void loadCustomMenus() {

        File directory = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus");

        if (!directory.exists()) {
            directory.mkdir();
            File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus", "serverselector.yml");
            try (final InputStream inputStream = this.plugin.getResource("serverselector.yml")) {
                file.createNewFile();
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                final OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Load all menu files
        File[] yamlFiles = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "menus").listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (yamlFiles == null) return;

        for (File file : yamlFiles) {
            String name = file.getName().replace(".yml", "");
            if (inventories.containsKey(name)) {
                plugin.getLogger().warning("Inventário com o nome '" + file.getName() + "' já existe, pulando duplicação..");
                continue;
            }

            CustomGUI customGUI;
            try {
                customGUI = new CustomGUI(plugin, YamlConfiguration.loadConfiguration(file));
            } catch (Exception e) {
                plugin.getLogger().severe("Não foi possível carregar '" + name + "' (YAML error).");
                e.printStackTrace();
                continue;
            }

            inventories.put(name, customGUI);
            plugin.getLogger().info("Menus costumizados carregados '" + name + "'.");
        }
    }

    public void addInventory(String key, AbstractInventory inventory) {
        inventories.put(key, inventory);
    }

    public Map<String, AbstractInventory> getInventories() {
        return inventories;
    }

    public AbstractInventory getInventory(String key) {
        return inventories.get(key);
    }

    public void onDisable() {
        inventories.values().forEach(abstractInventory -> {
            for (UUID uuid : abstractInventory.getOpenInventories()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) player.closeInventory();
            }
            abstractInventory.getOpenInventories().clear();
        });
        inventories.clear();
    }
}
