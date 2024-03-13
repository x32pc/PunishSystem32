package com.x32pc.github.commands;

import com.x32pc.github.PunishSystem32;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TempmuteCommand implements CommandExecutor {

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.tempmute")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if(args.length < 2) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }


        if(Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.mute.not-online").replace("%target%", args[0]))));
            return true;
        }
        String target = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
        String reason = args[2];
        long time = punishSystem32.parseTime.parseTime(args[1]);

        if(punishSystem32.sqLiteManager.isPlayerMuted(target)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.mute.already").replace("%target%", args[0]).replace("%reason%", reason))));
        } else {
            punishSystem32.sqLiteManager.addMute(target, time, reason, false);
            if(Bukkit.getPlayer(args[0]) != null) {
                Bukkit.getPlayer(args[0]).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.mute.target-chat").replace("%target%", args[0]).replace("%reason%", reason).replace("%time%", punishSystem32.formatDuration.formatDuration(time)))));
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.tempmute.chat").replace("%target%", args[0]).replace("%reason%", reason).replace("%executor%", sender.getName())));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.tempmute.admin").replace("%target%", args[0]).replace("%reason%", reason)));
            punishSystem32.historyManager.addHistory(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString(), args[0], "MUTE", args[2], time, sender.getName());
        }
            return true;
    }

    public TempmuteCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
