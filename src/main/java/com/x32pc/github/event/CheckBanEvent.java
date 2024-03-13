package com.x32pc.github.event;

import com.x32pc.github.PunishSystem32;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class CheckBanEvent implements Listener {
    private final PunishSystem32 punishSystem32;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPreLogin(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        long time = getUnbanTime(p);
        Instant expirationInstant = Instant.now().plusMillis(time);
        Date expirationDate = Date.from(expirationInstant);

        if(event.getResult() == PlayerLoginEvent.Result.KICK_BANNED) {
            event.setKickMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.listToString.ListToStringFunction("messages.punishments.ban.screen").replace("%reason%", getBanReason(p)).replace("%time%", expirationDate.toString()).replace("%executor%", getWhoBanned(p))));
            }
        }

    public BanEntry getBanEntry(Player player) {
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        return banList.getBanEntry(player.getName());
    }

    public String getBanReason(Player player) {
        BanEntry banEntry = getBanEntry(player);
        if (banEntry != null) {
            return banEntry.getReason();
        }
        return null;
    }

    public long getUnbanTime(Player player) {
        BanEntry banEntry = getBanEntry(player);
        if (banEntry != null && banEntry.getExpiration() != null) {
            return banEntry.getExpiration().getTime();
        }
        return -1;
    }

    public String getWhoBanned(Player player) {
        BanEntry banEntry = getBanEntry(player);
        if (banEntry != null) {
            return banEntry.getSource();
        }
        return null;
    }

    public CheckBanEvent(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
