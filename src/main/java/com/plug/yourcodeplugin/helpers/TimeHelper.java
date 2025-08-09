package com.plug.yourcodeplugin.helpers;

import java.sql.Timestamp;

public class TimeHelper {
    public enum TimeUnit{
        HOURS,
        MINUTES,
        SECONDS;
    }

    public static Long timestampToLong(Timestamp timestamp) {
        return timestamp.getTime();
    }

    public static Timestamp longToTimestamp(Long timestamp) {
        return new Timestamp(timestamp);
    }

    public static Long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(getCurrentTimeMillis());
    }

    public static Long convertMillis(Long millis, TimeUnit timeUnit) {
        if (millis == null || timeUnit == null) {
            throw new IllegalArgumentException("Null millis input");
        }

        switch (timeUnit) {
            case HOURS:
                return millis / (1000 * 60 * 60); // ms → hours
            case MINUTES:
                return millis / (1000 * 60); // ms → minutes
            case SECONDS:
                return millis / 1000; // ms → seconds
            default:
                throw new IllegalArgumentException("Unsupported TimeUnit: " + timeUnit);
        }
    }
}
