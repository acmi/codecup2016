/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import de.schlichtherle.truezip.util.Link;

public class Links {
    public static <T> T getTarget(Link<T> link) {
        return null == link ? null : (T)link.getTarget();
    }
}

