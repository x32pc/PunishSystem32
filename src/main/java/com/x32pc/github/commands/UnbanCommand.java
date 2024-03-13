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

public class UnbanCommand implements CommandExecutor {

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

        String target = args[0];

        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        if (banList.isBanned(target)) {
            banList.pardon(target);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.unban.unbanned").replace("%target%", target)));
            punishSystem32.historyManager.addHistory(Bukkit.getOfflinePlayer(target).getUniqueId().toString(), args[0], "UNBAN", "N/A", 0, sender.getName());
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.unban.not-banned").replace("%target%", target)));
        }
        return true;
    }

    public UnbanCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
