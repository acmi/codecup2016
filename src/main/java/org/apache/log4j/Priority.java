/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import org.apache.log4j.Level;

public class Priority {
    transient int level;
    transient String levelStr;
    transient int syslogEquivalent;
    public static final Priority FATAL = new Level(50000, "FATAL", 0);
    public static final Priority ERROR = new Level(40000, "ERROR", 3);
    public static final Priority WARN = new Level(30000, "WARN", 4);
    public static final Priority INFO = new Level(20000, "INFO", 6);
    public static final Priority DEBUG = new Level(10000, "DEBUG", 7);

    protected Priority() {
        this.level = 10000;
        this.levelStr = "DEBUG";
        this.syslogEquivalent = 7;
    }

    protected Priority(int n2, String string, int n3) {
        this.level = n2;
        this.levelStr = string;
        this.syslogEquivalent = n3;
    }

    public boolean equals(Object object) {
        if (object instanceof Priority) {
            Priority priority = (Priority)object;
            return this.level == priority.level;
        }
        return false;
    }

    public boolean isGreaterOrEqual(Priority priority) {
        return this.level >= priority.level;
    }

    public final String toString() {
        return this.levelStr;
    }
}

