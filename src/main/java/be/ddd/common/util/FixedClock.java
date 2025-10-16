package be.ddd.common.util;

import java.time.LocalDateTime;

public class FixedClock extends CustomClock {

    private LocalDateTime fixedTime;

    public FixedClock(LocalDateTime fixedTime) {
        this.fixedTime = fixedTime;
    }

    @Override
    protected LocalDateTime timeNow() {
        return fixedTime;
    }
}
