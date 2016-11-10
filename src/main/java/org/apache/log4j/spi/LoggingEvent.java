/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.spi;

import java.io.Serializable;
import java.util.Hashtable;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.ThrowableInformation;

public class LoggingEvent
implements Serializable {
    private static long startTime = System.currentTimeMillis();
    public final transient String fqnOfCategoryClass;
    private transient Category logger;
    public final String categoryName;
    public transient Priority level;
    private boolean ndcLookupRequired = true;
    private boolean mdcCopyLookupRequired = true;
    private transient Object message;
    private ThrowableInformation throwableInfo;
    public final long timeStamp;
    static final Integer[] PARAM_ARRAY = new Integer[1];
    static final Class[] TO_LEVEL_PARAMS = new Class[]{Integer.TYPE};
    static final Hashtable methodCache = new Hashtable(3);

    public LoggingEvent(String string, Category category, Priority priority, Object object, Throwable throwable) {
        this.fqnOfCategoryClass = string;
        this.logger = category;
        this.categoryName = category.getName();
        this.level = priority;
        this.message = object;
        if (throwable != null) {
            this.throwableInfo = new ThrowableInformation(throwable, category);
        }
        this.timeStamp = System.currentTimeMillis();
    }
}

