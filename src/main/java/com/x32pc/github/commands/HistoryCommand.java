package com.x32pc.github.commands;

import com.x32pc.github.PunishSystem32;
import com.x32pc.github.manager.HistoryManager;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryCommand implements CommandExecutor {

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.history")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if(args.length < 1) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        String target = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();

        sender.sendMessage("" + ChatColor.WHITE + ChatColor.BOLD + "History of punishments for player " + args[0] + ":");
        sender.sendMessage("" + ChatColor.GRAY + ChatColor.ITALIC + " ( Hover for more info )");
        getPlayerHistory(target, sender);

        return true;
    }


    public void getPlayerHistory(String playerUUID, CommandSender sender) {
        int rowCount = punishSystem32.getConfig().getInt("history.amount");
        String query = "SELECT * FROM history WHERE player_uuid = ? ORDER BY id DESC LIMIT ?";

        try (PreparedStatement pstmt = punishSystem32.historyManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, playerUUID);
            pstmt.setInt(2, rowCount);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                String playerName = resultSet.getString("player_name");
                String punishment = resultSet.getString("punishment");
                String reason = resultSet.getString("reason");
                long time = resultSet.getLong("time");
                String executor = resultSet.getString("executor");
                String entry = getString(punishment, playerName, executor);
                TextComponent subComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&',entry));
                subComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(createHover(time, reason))));
                sender.spigot().sendMessage(subComponent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String createHover(long time, String reason) {
        String t;
        if (time > 0) {
            t = punishSystem32.formatDuration.formatDuration(time);
        } else {
            t = "N/A";
        }
        return ChatColor.translateAlternateColorCodes('&', "&9Reason: &f" + reason + " &f, &9Punished for: &f" + t);
    }

    private static String getString(String punishment, String playerName, String executor) {
        String p = "";
        switch(punishment) {
            case "BAN":
                p = "&cbanned";
                break;
            case "MUTE":
                p = "&cmuted";
                break;
            case "KICK":
                p = "&ckicked";
                break;
            case "BLACKLIST":
                p = "&cblacklisted";
                break;
            case "UNMUTE":
                p = "&cunmuted";
                break;
            case "UNBLACKLIST":
                p = "&cunblacklisted";
                break;
            case "UNBAN":
                p = "&cunbanned";
                break;
            default:
                p = "&cpunished";
        }
        String entry = "&c# &f" + playerName + " was " + p + "&f by &9" + executor + "&f.";
        return entry;
    }

    public HistoryCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
