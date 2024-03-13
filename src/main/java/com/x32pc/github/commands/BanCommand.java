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

public class BanCommand implements CommandExecutor {

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.ban")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if(args.length < 2) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        String target = args[0];
        String reason = args[1];

        Bukkit.getBanList(BanList.Type.NAME).addBan(target, reason, null, sender.getName());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.ban.admin").replace("%target%", target).replace("%reason%", reason))));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.ban.chat").replace("%target%", target).replace("%reason%", reason))));

        punishSystem32.historyManager.addHistory(Bukkit.getOfflinePlayer(target).getUniqueId().toString(), target, "BAN", reason, -1, sender.getName());

        if(Bukkit.getPlayer(target) != null) {
            Bukkit.getPlayer(target).kickPlayer(ChatColor.translateAlternateColorCodes('&', punishSystem32.listToString.ListToStringFunction("messages.punishments.ban.screen").replace("%reason%", punishSystem32.checkBanEvent.getBanReason((Player) Bukkit.getOfflinePlayer(target))).replace("%time%", "never").replace("%executor%", sender.getName())));
        }
        return true;
    }

    public BanCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
