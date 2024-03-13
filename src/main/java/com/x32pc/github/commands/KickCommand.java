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

public class KickCommand implements CommandExecutor {

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.kick")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if(args.length < 1) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        String target = args[0];
        String reason;
        if(args.length == 1){
            reason = "not specified";
        } else {
            reason = args[1];
        }


        if(Bukkit.getPlayer(target) == null) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.kick.not-online").replace("%target%", target)));
            } else {
                Bukkit.getPlayer(target).kickPlayer(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.kick.screen").replace("%executor%", sender.getName()).replace("%reason%", reason)));
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.kick.chat").replace("%target%", target).replace("%reason%", reason)));
                punishSystem32.historyManager.addHistory(Bukkit.getOfflinePlayer(target).getUniqueId().toString(), target, "KICK", reason, 0, sender.getName());
        }
        return true;
    }

    public KickCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
