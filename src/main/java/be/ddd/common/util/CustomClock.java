package be.ddd.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class CustomClock {

    private static final CustomClock DEFAULT = new CustomClock();
    private static CustomClock instance = DEFAULT;

    public static void reset() {
        instance = DEFAULT;
    }

    public static LocalDateTime now() {
        return instance.timeNow();
    }

    protected void setInstance(CustomClock customClock) {
        CustomClock.instance = customClock;
    }

    protected LocalDateTime timeNow() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
