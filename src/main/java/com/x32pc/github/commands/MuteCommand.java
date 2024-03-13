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

public class MuteCommand implements CommandExecutor {

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.mute")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if(args.length < 2) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        String reason = args[1];
        String target = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
        String target_name = args[0];

        if(punishSystem32.sqLiteManager.isPlayerMuted(target)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.mute.already").replace("%target%", target_name).replace("%reason%", reason)));
        } else {
            punishSystem32.sqLiteManager.addMute(target, punishSystem32.parseTime.parseTime(args[1]), reason, true);
            if(Bukkit.getPlayer(target_name) != null) {
                Bukkit.getPlayer(target_name).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(punishSystem32.getConfig().getString("messages.punishments.mute.target-chat").replace("%target%", target_name).replace("%reason%", reason).replace("%time%", "never"))));
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.mute.chat").replace("%target%", target_name).replace("%reason%", reason).replace("%executor%", sender.getName())));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.mute.admin").replace("%target%", target_name).replace("%reason%", reason)));
            punishSystem32.historyManager.addHistory(Bukkit.getOfflinePlayer(target_name).getUniqueId().toString(), target_name, "MUTE", reason, -1, sender.getName());
        }
        return true;
    }

    public MuteCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
