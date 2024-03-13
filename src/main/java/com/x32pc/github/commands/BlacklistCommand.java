package com.x32pc.github.commands;

import com.x32pc.github.PunishSystem32;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class BlacklistCommand implements CommandExecutor {

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.blacklist")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if (args.length < 1) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
            return true;
        }

        String target = args[0];

        if(punishSystem32.blacklistYML.checkBlacklist(target)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.blacklist.already"))));
            return true;
        } else {
            punishSystem32.blacklistYML.setBlacklisted(target);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.blacklist.admin").replace("%target%", target))));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.blacklist.chat").replace("%target%", target))));
            punishSystem32.historyManager.addHistory(Bukkit.getOfflinePlayer(target).getUniqueId().toString(), target, "BLACKLIST", "/", -1, sender.getName());
        }

        if(Bukkit.getPlayer(target) != null) {
            Bukkit.getPlayer(target).kickPlayer(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.blacklist.screen")));
        }
        return true;
    }


    public BlacklistCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
