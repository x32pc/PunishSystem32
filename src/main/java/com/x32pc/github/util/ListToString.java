package com.x32pc.github.util;

import com.x32pc.github.PunishSystem32;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ListToString {

    private final PunishSystem32 punishSystem32;

    public String ListToStringFunction(String configKey) {

        FileConfiguration config = punishSystem32.getConfig();

        List<String> list = config.getStringList(configKey);
        StringBuilder result = new StringBuilder();
        for (String item : list) {
            result.append(item).append("\n");
        }

        return result.toString();
    }

    public ListToString(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
