package com.x32pc.github.event;

import com.x32pc.github.PunishSystem32;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Objects;

public class CheckBlacklistEvent implements Listener {
    private final PunishSystem32 punishSystem32;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();

        if (punishSystem32.blacklistYML.checkBlacklist(playerName)) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.blacklist.screen"))));
        }
    }

    public CheckBlacklistEvent(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
