package com.crownixed.icon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CrownIconPlugin extends JavaPlugin {

    private final Map<String, String> iconMap = new LinkedHashMap<>();
    private final Map<String, String> aliases = new HashMap<>();
    private CrownIconExpansion crownExpansion;
    private LemonIconAliasExpansion lemonExpansion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerAliases();
        loadIconsFromConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.crownExpansion = new CrownIconExpansion(this);
            this.crownExpansion.register();

            this.lemonExpansion = new LemonIconAliasExpansion(this);
            this.lemonExpansion.register();

            getLogger().info("CrownIcon enabled! Hooked into PlaceholderAPI (%crownicon:<item>%). Author: *crownixed៚");
        } else {
            getLogger().warning("PlaceholderAPI not found! Install PlaceholderAPI to use %crownicon:<item>% placeholders.");
        }
    }

    @Override
    public void onDisable() {
        if (this.crownExpansion != null) {
            this.crownExpansion.unregister();
        }
        if (this.lemonExpansion != null) {
            this.lemonExpansion.unregister();
        }
    }

    private void registerAliases() {
        aliases.clear();
        aliases.put("armorstand", "armor_stand");
        aliases.put("netheritespear", "netherite_spear");
        aliases.put("netheritesword", "netherite_sword");
        aliases.put("netheritepickaxe", "netherite_pickaxe");
        aliases.put("grassblock", "grass_block");
        aliases.put("cherrysapling", "cherry_sapling");
        aliases.put("flintandsteel", "flint_and_steel");
        aliases.put("nametag", "name_tag");
        aliases.put("beenest", "bee_nest");
        aliases.put("heavycore", "heavy_core");
        aliases.put("netheriteupgrade", "netherite_upgrade");
        aliases.put("blockofnetherite", "block_of_netherite");
        aliases.put("netherite_block", "block_of_netherite");
        aliases.put("netheriteblock", "block_of_netherite");
        aliases.put("torch_flower", "torchflower");
        aliases.put("pinkpetals", "pink_petals");
        aliases.put("honeycombblock", "honeycomb_block");
        aliases.put("caulk_shrieker", "sculk_shrieker");
        aliases.put("caulkshrieker", "sculk_shrieker");
        aliases.put("sculkshrieker", "sculk_shrieker");
    }

    public void loadIconsFromConfig() {
        iconMap.clear();
        ConfigurationSection section = getConfig().getConfigurationSection("icons");
        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {
            String rawValue = section.getString(key, "");
            String translated = ChatColor.translateAlternateColorCodes('&', rawValue);
            iconMap.put(key.toLowerCase(), translated);
        }
    }

    public String getIcon(String rawKey) {
        if (rawKey == null || rawKey.isEmpty()) {
            return "";
        }
        String normalized = rawKey.toLowerCase().trim();
        if (aliases.containsKey(normalized)) {
            normalized = aliases.get(normalized);
        }
        return iconMap.getOrDefault(normalized, "");
    }

    public Map<String, String> getAllIcons() {
        return iconMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("crownicon")) {
            return false;
        }

        if (!sender.hasPermission("crownicon.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            loadIconsFromConfig();
            sender.sendMessage(ChatColor.GREEN + "CrownIcon reloaded (" + iconMap.size() + " icons) by *crownixed៚.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(ChatColor.GOLD + "=== CrownIcon Placeholders (Author: *crownixed៚) ===");
            for (Map.Entry<String, String> entry : iconMap.entrySet()) {
                sender.sendMessage(ChatColor.AQUA + "%crownicon:" + entry.getKey() + "% "
                        + ChatColor.GRAY + "-> " + ChatColor.WHITE + entry.getValue());
            }
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Usage: /crownicon <list|reload>");
        return true;
    }
}
