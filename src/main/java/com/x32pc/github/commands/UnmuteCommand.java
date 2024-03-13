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
public class UnmuteCommand implements CommandExecutor {

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.unban")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if(args.length < 1) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        String target = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();

        if(punishSystem32.sqLiteManager.isPlayerMuted(target)) {
            punishSystem32.sqLiteManager.removeMute(target);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.unmute.admin").replace("%target%", args[0])));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.unmute.chat").replace("%target%", args[0])));
            punishSystem32.historyManager.addHistory(Objects.requireNonNull(Bukkit.getOfflinePlayer(args[0])).getUniqueId().toString(), args[0], "UNMUTE", "N/A", -1, sender.getName());
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.unmute.negative").replace("%target%", args[0])));
        }
        return true;
    }

    public UnmuteCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
