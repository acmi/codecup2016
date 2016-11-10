/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.spi;

import java.io.Serializable;
import org.apache.log4j.Category;

public class ThrowableInformation
implements Serializable {
    private transient Throwable throwable;
    private transient Category category;

    public ThrowableInformation(Throwable throwable, Category category) {
        this.throwable = throwable;
        this.category = category;
    }
}

