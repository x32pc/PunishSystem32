package com.x32pc.github.event;

import com.x32pc.github.PunishSystem32;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckMuteEvent implements Listener {

    private final PunishSystem32 punishSystem32;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String reason = punishSystem32.sqLiteManager.getMuteReason(event.getPlayer().getUniqueId().toString());
        String uuid = event.getPlayer().getUniqueId().toString();
        if(punishSystem32.sqLiteManager.isMuteExpired(uuid)) {
            punishSystem32.sqLiteManager.removeMute(uuid);
        } else {
            if(punishSystem32.sqLiteManager.isPlayerMuted(uuid)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.mute.target-chat").replace("%reason%", reason).replace("%time%", getTimeUntilUnmute(uuid))));
            }
        }

        punishSystem32.getLogger().info(String.valueOf(punishSystem32.sqLiteManager.isMuteExpired(uuid)));
        punishSystem32.getLogger().info(String.valueOf(punishSystem32.sqLiteManager.isPermanentMute(uuid)));
        punishSystem32.getLogger().info(String.valueOf(punishSystem32.sqLiteManager.isPlayerMuted(uuid)));
    }

    public String getTimeUntilUnmute(String playerUUID) {
        long currentTime = System.currentTimeMillis();
        long unmuteDate = punishSystem32.sqLiteManager.getUnmuteTime(playerUUID);

        if(unmuteDate == -1) {
            return "never";
        }

        long remainingTimeMillis = unmuteDate - currentTime;

        long seconds = remainingTimeMillis / 1000;
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        long months = days / 30;
        days = days % 30;

        StringBuilder formattedTime = new StringBuilder();

        if (months > 0) {
            formattedTime.append(months).append(" months ");
        }
        if (days > 0) {
            formattedTime.append(days).append(" days ");
        }
        if (hours > 0) {
            formattedTime.append(hours).append(" hours ");
        }
        if (minutes > 0) {
            formattedTime.append(minutes).append(" minutes ");
        }
        if (seconds > 0) {
            formattedTime.append(seconds).append(" seconds ");
        }

        return formattedTime.toString().trim();
    }

    public CheckMuteEvent(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
