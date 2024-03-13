package com.x32pc.github.util;

import com.x32pc.github.PunishSystem32;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BlacklistYML {

    private final PunishSystem32 punishSystem32;

    public File blacklistFile ;
    public FileConfiguration blacklistConfig;

    public Set<String> blacklist;

    public void createBlacklistedYML() {
        blacklistFile = new File(PunishSystem32.getInstance().getDataFolder(), "blacklisted.yml");
        if (!blacklistFile.exists()) {
            blacklistFile.getParentFile().mkdirs();
            punishSystem32.saveResource("blacklisted.yml", false);
        }
    }
    public void saveBlacklistedYML() {
        blacklistConfig.set("blacklisted", new ArrayList<>(blacklist));
        try {
            blacklistConfig.save(blacklistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlacklistedYML() {
        if (!blacklistFile.exists()) {
            punishSystem32.saveResource("blacklisted.yml", false);
        }

        blacklistConfig = YamlConfiguration.loadConfiguration(blacklistFile);
        blacklist.clear();
        blacklist.addAll(blacklistConfig.getStringList("blacklisted"));
    }

    public FileConfiguration getBlacklistYML() {
        blacklistConfig = YamlConfiguration.loadConfiguration(blacklistFile);
        return blacklistConfig;
    }

    public boolean checkBlacklist(String player) {
        return punishSystem32.blacklistYML.getBlacklistYML().getStringList("blacklisted").contains(player.toLowerCase());
    }

    public void utilizeBlackList() {
        blacklist = new HashSet<>();
        loadBlacklistedYML();
    }

    public void setBlacklisted(String playerName) {
        utilizeBlackList();
        blacklist.add(playerName.toLowerCase());
        saveBlacklistedYML();
    }

    public void removeBlacklisted(String playerName) {
        utilizeBlackList();
        blacklist.remove(playerName.toLowerCase());
        saveBlacklistedYML();
    }

    public BlacklistYML(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
