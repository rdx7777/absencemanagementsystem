package io.github.rdx7777.absencemanagementsystem.generators;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private static AtomicLong atomicLong = new AtomicLong(0);

    public static Long getNextId() {
        return atomicLong.incrementAndGet();
    }
}
