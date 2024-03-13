package com.x32pc.github.util;

import com.x32pc.github.PunishSystem32;

import java.util.concurrent.TimeUnit;

public class FormatDuration {

    private final PunishSystem32 punishSystem32;

    public String formatDuration(long millis) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long days = TimeUnit.MILLISECONDS.toDays(millis);

        if (days > 0) {
            return days + (days == 1 ? " day" : " days");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour" : " hours");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minute" : " minutes");
        } else {
            return seconds + (seconds == 1 ? " second" : " seconds");
        }
    }

    public FormatDuration(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
