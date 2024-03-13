package com.x32pc.github.commands;

import com.x32pc.github.PunishSystem32;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TempbanCommand implements CommandExecutor{

    private final PunishSystem32 punishSystem32;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("punishsystem32.tempban")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if(args.length < 3) {
            for (String message : punishSystem32.getConfig().getStringList("messages.help"))
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        String target = args[0];
        long time = punishSystem32.parseTime.parseTime(args[1]);
        punishSystem32.getLogger().info(String.valueOf(time));
        String reason = args[2];

        Instant expirationInstant = Instant.now().plusMillis(time);
        Date expirationDate = Date.from(expirationInstant);

        Bukkit.getBanList(BanList.Type.NAME).addBan(target, reason, expirationDate, sender.getName());
        if(Bukkit.getPlayer(target) != null) {
            Bukkit.getPlayer(target).kickPlayer(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.tempban.screen").replace("%executor%", sender.getName()).replace("%reason%", reason).replace("%time%", expirationDate.toString())));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.tempban.admin").replace("%target%", target).replace("%reason%", reason)));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', punishSystem32.getConfig().getString("messages.punishments.tempban.chat").replace("%target%", target).replace("%reason%", reason)));
        punishSystem32.historyManager.addHistory(Objects.requireNonNull(Bukkit.getOfflinePlayer(target)).getUniqueId().toString(), target, "BAN", reason, time, sender.getName());
        return true;
    }

    public TempbanCommand(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
