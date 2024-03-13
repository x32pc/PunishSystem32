package com.x32pc.github.util;

import com.x32pc.github.PunishSystem32;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTime {

    private final PunishSystem32 punishSystem32;

    public long parseTime(String timeString) {
        long milliseconds = 0;
        Pattern pattern = Pattern.compile("(\\d+)([smhdwy])");
        Matcher matcher = pattern.matcher(timeString);

        while (matcher.find()) {
            long value = Integer.parseInt(matcher.group(1));
            char unit = matcher.group(2).charAt(0);

            switch (unit) {
                case 's':
                    milliseconds += value * 1000;
                    break;
                case 'm':
                    milliseconds += value * 60 * 1000;
                    break;
                case 'h':
                    milliseconds += value * 60 * 60 * 1000;
                    break;
                case 'd':
                    milliseconds += value * 24 * 60 * 60 * 1000;
                    break;
                case 'w':
                    milliseconds += value * 7 * 24 * 60 * 60 * 1000;
                    break;
                case 'y':
                    milliseconds += value * 365 * 24 * 60 * 60 * 1000;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid time unit: " + unit);
            }
        }

        return milliseconds;
    }

    public ParseTime(PunishSystem32 main) {
        this.punishSystem32 = main;
    }
}
