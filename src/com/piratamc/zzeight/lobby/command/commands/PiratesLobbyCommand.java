package com.piratamc.zzeight.lobby.command.commands;

import cl.bgm.minecraft.util.commands.CommandContext;
import cl.bgm.minecraft.util.commands.annotations.Command;
import cl.bgm.minecraft.util.commands.exceptions.CommandException;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.piratamc.zzeight.lobby.PermissionsHub;
import com.piratamc.zzeight.lobby.PiratesLobby;
import com.piratamc.zzeight.lobby.module.modules.hotbar.HotbarItem;
import com.piratamc.zzeight.lobby.module.modules.hotbar.HotbarManager;
import com.piratamc.zzeight.lobby.module.modules.visual.scoreboard.ScoreboardManager;
import com.piratamc.zzeight.lobby.module.modules.world.LobbySpawn;
import com.piratamc.zzeight.lobby.command.CommandManager;
import com.piratamc.zzeight.lobby.config.Messages;
import com.piratamc.zzeight.lobby.inventory.AbstractInventory;
import com.piratamc.zzeight.lobby.inventory.InventoryManager;
import com.piratamc.zzeight.lobby.module.ModuleManager;
import com.piratamc.zzeight.lobby.module.ModuleType;
import com.piratamc.zzeight.lobby.module.modules.hologram.Hologram;
import com.piratamc.zzeight.lobby.utility.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PiratesLobbyCommand {

    private PiratesLobby plugin;

    public PiratesLobbyCommand(PiratesLobby plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"pirateshub", "phub"},
            desc = "Ver a informação do plugin"
    )
    public void main(final CommandContext args, final CommandSender sender) throws CommandException {

        PluginDescriptionFile pdfFile = plugin.getDescription();

		/*
		Command: help
		Description: displays help message
		*/
        if (args.argsLength() == 0 || args.getString(0).equalsIgnoreCase("help")) {

            if (!sender.hasPermission(PermissionsHub.COMMAND_PIRATESHUB_HELP.getPermission())) {
                sender.sendMessage(TextUtil.color("&8&l> &7Servidor está a rodar o &dPiratesHub &ev" + pdfFile.getVersion() + " &7Do &6008"));
                return;
            }

            sender.sendMessage("");
            sender.sendMessage(TextUtil.color("&d&lPiratesHub " + "&fv" + plugin.getDescription().getVersion()));
            sender.sendMessage(TextUtil.color("&7Autor: &f008"));
            sender.sendMessage("");
            sender.sendMessage(TextUtil.color(" &d/pirateshub info &8- &7&oMostra a informação da config atual"));
            sender.sendMessage(TextUtil.color(" &d/pirateshub scoreboard &8- &7&oAtiva/Desativa a scoreboard"));
            sender.sendMessage(TextUtil.color(" &d/pirateshub open <menu> &8- &7&oAbre um menu costumizado"));
            sender.sendMessage(TextUtil.color(" &d/pirateshub hologram &8- &7&oVeja a ajuda dos hologramas"));
            sender.sendMessage("");
            sender.sendMessage(TextUtil.color("  &d/vanish &8- &7&oAtiva/Desativa o vanish"));
            sender.sendMessage(TextUtil.color("  &d/fly &8- &7&oAtiva/Desativa o voo"));
            sender.sendMessage(TextUtil.color("  &d/setlobby &8- &7&oSeta o Spawn do lobby"));
            sender.sendMessage(TextUtil.color("  &d/lobby &8- &7&oTeleporta para o lobby"));
            sender.sendMessage(TextUtil.color("  &d/gamemode <gamemode> &8- &7&oSeta o gamemode"));
            sender.sendMessage(TextUtil.color("  &d/gmc &8- &7&oEntra no modo criativo"));
            sender.sendMessage(TextUtil.color("  &d/gms &8- &7&oEntra no modo survival"));
            sender.sendMessage(TextUtil.color("  &d/gma &8- &7&oEntra no modo aventureiro"));
            sender.sendMessage(TextUtil.color("  &d/gmsp &8- &7&oEntra no modo espectador"));
            sender.sendMessage(TextUtil.color("  &d/clearchat &8- &7&oLimpa o chat global"));
            sender.sendMessage(TextUtil.color("  &d/lockchat &8- &7&oBloqueia/Desbloqueia o chat global"));
            sender.sendMessage("");
            return;
        }

		/*
		Command: reload
		Description: reloads the entire plugin
		*/
        else if (args.getString(0).equalsIgnoreCase("reload")) {

            if (!sender.hasPermission(PermissionsHub.COMMAND_PIRATESHUB_RELOAD.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            long start = System.currentTimeMillis();
            plugin.reload();
            Messages.CONFIG_RELOAD.send(sender,"%time%", String.valueOf(System.currentTimeMillis() - start));
            int inventories = plugin.getInventoryManager().getInventories().size();
            if (inventories > 0) {
                sender.sendMessage(TextUtil.color("&8- &7Loaded &a" + inventories + "&7 custom menus."));
            }
        }

		/*
		Command: scoreboard
		Description: toggles the scoreboard on/off
		*/
        else if (args.getString(0).equalsIgnoreCase("scoreboard")) {

            if (!(sender instanceof Player)) throw new CommandException("Console cannot toggle the scoreboard");

            if (!sender.hasPermission(PermissionsHub.COMMAND_SCOREBOARD_TOGGLE.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            if (!plugin.getModuleManager().isEnabled(ModuleType.SCOREBOARD)) {
                sender.sendMessage(TextUtil.color("&cThe scoreboard module is not enabled in the configuration."));
                return;
            }

            Player player = (Player) sender;
            ScoreboardManager scoreboardManager = ((ScoreboardManager) plugin.getModuleManager().getModule(ModuleType.SCOREBOARD));

            if (scoreboardManager.hasScore(player.getUniqueId())) {
                scoreboardManager.removeScoreboard(player);
                Messages.SCOREBOARD_TOGGLE.send(player, "%value%", "disabled");
            } else {
                scoreboardManager.createScoreboard(player);
                Messages.SCOREBOARD_TOGGLE.send(player, "%value%", "enabled");
            }
        }

		/*
		Command: info
		Description: displays useful information about the configuration
		*/
        else if (args.getString(0).equalsIgnoreCase("info")) {

            if (!sender.hasPermission(PermissionsHub.COMMAND_PIRATESHUB_HELP.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            sender.sendMessage("");
            sender.sendMessage(TextUtil.color("&d&lInformação do Plugin"));
            sender.sendMessage("");

            Location location = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY)).getLocation();
            sender.sendMessage(TextUtil.color("&7Spawn setado &8- " + (location != null ? "&aSim" : "&cNão &7&o(/setlobby)")));

            sender.sendMessage("");

            ModuleManager moduleManager = plugin.getModuleManager();
            sender.sendMessage(TextUtil.color("&7Mundos desabilitados (" + moduleManager.getDisabledWorlds().size() + ") &8- &a" + (String.join(", ", moduleManager.getDisabledWorlds()))));

            InventoryManager inventoryManager = plugin.getInventoryManager();
            sender.sendMessage(TextUtil.color("&7Menus (" + inventoryManager.getInventories().size() + ")" + " &8- &a" + (String.join(", ", inventoryManager.getInventories().keySet()))));

            HotbarManager hotbarManager = ((HotbarManager) plugin.getModuleManager().getModule(ModuleType.HOTBAR_ITEMS));
            sender.sendMessage(TextUtil.color("&7Itens da hotbar (" + hotbarManager.getHotbarItems().size() + ")" + " &8- &a" + (hotbarManager.getHotbarItems().stream().map(HotbarItem::getKey).collect(Collectors.joining(", ")))));

            CommandManager commandManager = plugin.getCommandManager();
            sender.sendMessage(TextUtil.color("&7Comandos (" + commandManager.getCustomCommands().size() + ")" + " &8- &a" + (commandManager.getCustomCommands().stream().map(command -> command.getAliases().get(0)).collect(Collectors.joining(", ")))));

            sender.sendMessage("");

            sender.sendMessage(TextUtil.color("&7PlaceholderAPI Hook: " + (plugin.getHookManager().isHookEnabled("PLACEHOLDER_API") ? "&aSim" : "&cNão")));
            sender.sendMessage(TextUtil.color("&7HeadDatabase Hook: " + (plugin.getHookManager().isHookEnabled("HEAD_DATABASE") ? "&aSim" : "&cNão")));

            sender.sendMessage("");
        }

		/*
		Command: open
		Description: opens a custom menu
		*/
        else if (args.getString(0).equalsIgnoreCase("open")) {
            if (!(sender instanceof Player)) throw new CommandException("Console cannot open menus");

            if (!sender.hasPermission(PermissionsHub.COMMAND_OPEN_MENUS.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            if (args.argsLength() == 1) {
                sender.sendMessage(TextUtil.color("&cUse: /pirateshub open <menu>"));
                return;
            }

            AbstractInventory inventory = plugin.getInventoryManager().getInventory(args.getString(1));
            if (inventory == null) {
                sender.sendMessage(TextUtil.color("&c" + args.getString(1) + " não é um menu válido."));
                return;
            }
            inventory.openInventory((Player) sender);
        }

        /*
         * Holograms
         */
        if (args.getString(0).equalsIgnoreCase("hologram") || args.getString(0).equalsIgnoreCase("holo")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("Você não pode usar esse comando.");
                return;
            }

            if (!sender.hasPermission(PermissionsHub.COMMAND_HOLOGRAMS.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            if (args.argsLength() == 1) {
                sender.sendMessage("");
                sender.sendMessage(TextUtil.color("&d&lPiratesHub Hologramas"));
                sender.sendMessage("");
                sender.sendMessage(TextUtil.color(" &d/" + args.getCommand() + " hologram list"));
                sender.sendMessage(TextUtil.color("   &7&oLista de todos os hologramas"));
                sender.sendMessage(TextUtil.color(" &d/" + args.getCommand() + " hologram create <id>"));
                sender.sendMessage(TextUtil.color("   &7&oCria um novo holograma"));
                sender.sendMessage(TextUtil.color(" &d/" + args.getCommand() + " hologram remove <id>"));
                sender.sendMessage(TextUtil.color("   &7&oDeleta um holograma existente"));
                sender.sendMessage(TextUtil.color(" &d/" + args.getCommand() + " hologram move <id>"));
                sender.sendMessage(TextUtil.color("   &7&oMove o holograma para si"));
                sender.sendMessage(TextUtil.color(""));
                sender.sendMessage(TextUtil.color(" &d/" + args.getCommand() + " hologram setline <id> <line> <text>"));
                sender.sendMessage(TextUtil.color("   &7&oSeta a linha do holograma"));
                sender.sendMessage(TextUtil.color(" &d/" + args.getCommand() + " hologram addline <id> <text>"));
                sender.sendMessage(TextUtil.color("   &7&oAdiciona uma nova linha ao holograma"));
                sender.sendMessage(TextUtil.color(" &d/" + args.getCommand() + " hologram removeline <id> <line>"));
                sender.sendMessage(TextUtil.color("   &7&oRemove a linha do holograma"));
                sender.sendMessage("");
                return;
            }

            Player player = (Player) sender;

            if (args.argsLength() >= 2) {
                if (args.getString(1).equalsIgnoreCase("list")) {

                    if (plugin.getHologramManager().getHolograms().isEmpty()) {
                        Messages.HOLOGRAMS_EMPTY.send(player);
                        return;
                    }

                    sender.sendMessage("");
                    sender.sendMessage(TextUtil.color("&d&lLista de Hologramas"));
                    for (Hologram entry : plugin.getHologramManager().getHolograms()) {
                        sender.sendMessage(TextUtil.color("&8- &7" + entry.getName()));
                    }
                    sender.sendMessage("");
                }


                if (args.getString(1).equalsIgnoreCase("create")) {
                    if (args.argsLength() == 2) {
                        sender.sendMessage(TextUtil.color("&cUse: /pirateshub hologram create <id>"));
                        return;
                    }

                    if (plugin.getHologramManager().hasHologram(args.getString(2))) {
                        Messages.HOLOGRAMS_ALREADY_EXISTS.send(player, "%name%", args.getString(2));
                        return;
                    }

                    Hologram holo = plugin.getHologramManager().createHologram(args.getString(2), player.getLocation());
                    List<String> defaultMsg = new ArrayList<String>();
                    defaultMsg.add("&7Criado um novo holograma chamado &b" + args.getString(2));
                    defaultMsg.add("&7Use &b/pirateshub holo &7para customizar");
                    holo.setLines(defaultMsg);
                    Messages.HOLOGRAMS_SPAWNED.send(player, "%name%", args.getString(2));
                    return;
                }


                if (args.getString(1).equalsIgnoreCase("remove") || args.getString(1).equalsIgnoreCase("delete")) {
                    if (args.argsLength() == 2) {
                        sender.sendMessage(TextUtil.color("&cUse: /pirateshub hologram remove <id>"));
                        return;
                    }

                    if (!plugin.getHologramManager().hasHologram(args.getString(2))) {
                        Messages.HOLOGRAMS_INVALID_HOLOGRAM.send(player, "%name%", args.getString(2));
                        return;
                    }

                    plugin.getHologramManager().deleteHologram(args.getString(2));
                    Messages.HOLOGRAMS_DESPAWNED.send(player, "%name%", args.getString(2));
                    return;
                }

                if (args.getString(1).equalsIgnoreCase("setline")) {
                    if (args.argsLength() < 5) {
                        sender.sendMessage(TextUtil.color("&cUse: /pirateshub hologram setline <id> <line> <text>"));
                        return;
                    }

                    if (!plugin.getHologramManager().hasHologram(args.getString(2))) {
                        Messages.HOLOGRAMS_INVALID_HOLOGRAM.send(player, "%name%", args.getString(2));
                        return;
                    }

                    Hologram holo = plugin.getHologramManager().getHologram(args.getString(2));
                    int line = Integer.parseInt(args.getString(3));
                    String text = TextUtil.joinString(5, args.getOriginalArgs());

                    if (!holo.hasLine(line)) {
                        Messages.HOLOGRAMS_INVALID_LINE.send(player, "%line%", String.valueOf(line));
                        return;
                    }
                    holo.setLine(line, text);
                    Messages.HOLOGRAMS_LINE_SET.send(player, "%line%", String.valueOf(line));
                    return;
                }

                if (args.getString(1).equalsIgnoreCase("addline")) {
                    if (args.argsLength() <= 3) {
                        sender.sendMessage(TextUtil.color("&cUse: /pirateshub hologram addline <id> <text>"));
                        return;
                    }

                    if (!plugin.getHologramManager().hasHologram(args.getString(2))) {
                        Messages.HOLOGRAMS_INVALID_HOLOGRAM.send(player, "%name%", args.getString(2));
                        return;
                    }

                    Hologram holo = plugin.getHologramManager().getHologram(args.getString(2));
                    String text = TextUtil.joinString(4, args.getOriginalArgs());

                    holo.addLine(text);
                    Messages.HOLOGRAMS_ADDED_LINE.send(player, "%name%", args.getString(2));
                }

                if (args.getString(1).equalsIgnoreCase("removeline")) {
                    if (args.argsLength() != 4) {
                        sender.sendMessage(TextUtil.color("&cUse: /pirateshub hologram removeline <id> <line>"));
                        return;
                    }

                    if (!plugin.getHologramManager().hasHologram(args.getString(2))) {
                        Messages.HOLOGRAMS_INVALID_HOLOGRAM.send(player, "%name%", args.getString(2));
                        return;
                    }

                    Hologram holo = plugin.getHologramManager().getHologram(args.getString(2));
                    int line = Integer.parseInt(args.getString(3));

                    if (!holo.hasLine(line)) {
                        Messages.HOLOGRAMS_INVALID_LINE.send(player, "%line%", String.valueOf(line));
                        return;
                    }

                    if (holo.removeLine(line) == null) {
                        plugin.getHologramManager().deleteHologram(args.getString(2));
                        Messages.HOLOGRAMS_REMOVED_LINE.send(player, "%name%", args.getString(2));
                    }

                    return;
                }

                if (args.getString(1).equalsIgnoreCase("move")) {
                    if (args.argsLength() == 2) {
                        sender.sendMessage(TextUtil.color("&cUse: /pirateshub hologram move <id>"));
                        return;
                    }

                    if (!plugin.getHologramManager().hasHologram(args.getString(2))) {
                        Messages.HOLOGRAMS_INVALID_HOLOGRAM.send(player, "%name%", args.getString(2));
                        return;
                    }

                    Hologram holo = plugin.getHologramManager().getHologram(args.getString(2));

                    holo.setLocation(player.getLocation());
                    Messages.HOLOGRAMS_MOVED.send(player, "%name%", args.getString(2));
                }
                return;

            }

        }

    }
}
