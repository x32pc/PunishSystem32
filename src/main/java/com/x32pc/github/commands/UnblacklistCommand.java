package com.x32pc.github.commands;

import com.x32pc.github.PunishSystem32;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class UnblacklistCommand implements CommandExecutor {

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.unblacklist")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if (args.length < 1) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
            return true;
        }

        if(!punishSystem32.blacklistYML.checkBlacklist(args[0])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.blacklist.not-blacklisted")));
        } else {
            punishSystem32.blacklistYML.removeBlacklisted(args[0]);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.unblacklist.admin").replace("%target%", args[0])));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.unblacklist.chat").replace("%target%", args[0])));
            punishSystem32.historyManager.addHistory(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString(), args[0], "UNBLACKLIST", "N/A", -1, sender.getName());
        }
        return true;
    }


    public UnblacklistCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
