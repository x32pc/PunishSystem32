package com.x32pc.github;

import com.x32pc.github.commands.*;
import com.x32pc.github.event.CheckBanEvent;
import com.x32pc.github.event.CheckBlacklistEvent;
import com.x32pc.github.event.CheckMuteEvent;
import com.x32pc.github.manager.HistoryManager;
import com.x32pc.github.manager.SQLiteManager;
import com.x32pc.github.util.BlacklistYML;
import com.x32pc.github.util.FormatDuration;
import com.x32pc.github.util.ListToString;
import com.x32pc.github.util.ParseTime;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class PunishSystem32 extends JavaPlugin {

    public BanCommand banCommand;
    public ParseTime parseTime;
    public TempbanCommand tempbanCommand;
    public BlacklistYML blacklistYML;
    public BlacklistCommand blacklistCommand;
    public CheckBlacklistEvent checkBlacklistEvent;
    public UnblacklistCommand unblacklistCommand;
    public TempmuteCommand tempmuteCommand;
    public MuteCommand muteCommand;
    public UnmuteCommand unmuteCommand;
    public CheckMuteEvent checkMuteEvent;
    public SQLiteManager sqLiteManager;
    public ListToString listToString;
    public FormatDuration formatDuration;
    public HistoryManager historyManager;
    public CheckBanEvent checkBanEvent;

    @Override
    public void onEnable() {
        registerFiles();
        registerCommands();
        registerEvents();
        saveDefaultConfig();
        blacklistYML.createBlacklistedYML();
        blacklistYML.utilizeBlackList();

        sqLiteManager = new SQLiteManager(getDataFolder() + File.separator + "database.db", this);
        historyManager = new HistoryManager(getDataFolder() + File.separator + "database.db", this);
        if (sqLiteManager.getConnection() == null || historyManager.getConnection() == null) {
            getLogger().severe("Failed to initialize SQLiteManager or historyManager.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.sqLiteManager.closeConnection();
        this.historyManager.closeConnection();
    }

    public void registerFiles() {
        banCommand = new BanCommand(this);
        parseTime = new ParseTime(this);
        tempbanCommand = new TempbanCommand(this);
        blacklistYML = new BlacklistYML(this);
        blacklistCommand = new BlacklistCommand(this);
        checkBlacklistEvent = new CheckBlacklistEvent(this);
        unblacklistCommand = new UnblacklistCommand(this);
        tempmuteCommand = new TempmuteCommand(this);
        muteCommand = new MuteCommand(this);
        unmuteCommand = new UnmuteCommand(this);
        checkMuteEvent = new CheckMuteEvent(this);
        listToString = new ListToString(this);
        formatDuration = new FormatDuration(this);
        checkBanEvent = new CheckBanEvent(this);
    }

    public void registerCommands() {
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("tempban").setExecutor(new TempbanCommand(this));
        getCommand("blacklist").setExecutor(new BlacklistCommand(this));
        getCommand("unblacklist").setExecutor(new UnblacklistCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("tempmute").setExecutor(new TempmuteCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
        getCommand("history").setExecutor(new HistoryCommand(this));
        getCommand("kick").setExecutor(new KickCommand(this));
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new CheckBlacklistEvent(this), this);
        getServer().getPluginManager().registerEvents(new CheckMuteEvent(this), this);
        getServer().getPluginManager().registerEvents(new CheckBanEvent(this), this);
    }

    public static PunishSystem32 getInstance() {
        return getPlugin(PunishSystem32.class);
    }
}
