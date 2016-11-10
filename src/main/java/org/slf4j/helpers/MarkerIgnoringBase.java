/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j.helpers;

import org.slf4j.Logger;
import org.slf4j.helpers.NamedLoggerBase;

public abstract class MarkerIgnoringBase
extends NamedLoggerBase
implements Logger {
    public String toString() {
        return this.getClass().getName() + "(" + this.getName() + ")";
    }
}

