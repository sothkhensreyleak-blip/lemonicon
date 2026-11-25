package com.crownixed.icon;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LemonIconAliasExpansion extends PlaceholderExpansion {

    private final CrownIconPlugin plugin;

    public LemonIconAliasExpansion(CrownIconPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "lemonicon";
    }

    @Override
    public @NotNull String getAuthor() {
        return "*crownixed៚";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("author")) {
            return "*crownixed៚";
        }
        return plugin.getIcon(params);
    }
}
