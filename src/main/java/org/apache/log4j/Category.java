/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Appender;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

public class Category
implements AppenderAttachable {
    protected String name;
    protected volatile Level level;
    protected volatile Category parent;
    private static final String FQCN;
    protected ResourceBundle resourceBundle;
    protected LoggerRepository repository;
    AppenderAttachableImpl aai;
    protected boolean additive = true;
    static Class class$org$apache$log4j$Category;

    protected Category(String string) {
        this.name = string;
    }

    public synchronized void addAppender(Appender appender) {
        if (this.aai == null) {
            this.aai = new AppenderAttachableImpl();
        }
        this.aai.addAppender(appender);
        this.repository.fireAddAppenderEvent(this, appender);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void callAppenders(LoggingEvent loggingEvent) {
        int n2 = 0;
        Category category = this;
        while (category != null) {
            Category category2 = category;
            synchronized (category2) {
                if (category.aai != null) {
                    n2 += category.aai.appendLoopOnAppenders(loggingEvent);
                }
                if (!category.additive) {
                    break;
                }
            }
            category = category.parent;
        }
        if (n2 == 0) {
            this.repository.emitNoAppenderWarning(this);
        }
    }

    synchronized void closeNestedAppenders() {
        Enumeration enumeration = this.getAllAppenders();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Appender appender = (Appender)enumeration.nextElement();
                if (!(appender instanceof AppenderAttachable)) continue;
                appender.close();
            }
        }
    }

    public void debug(Object object) {
        if (this.repository.isDisabled(10000)) {
            return;
        }
        if (Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(FQCN, Level.DEBUG, object, null);
        }
    }

    public void error(Object object) {
        if (this.repository.isDisabled(40000)) {
            return;
        }
        if (Level.ERROR.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(FQCN, Level.ERROR, object, null);
        }
    }

    public void error(Object object, Throwable throwable) {
        if (this.repository.isDisabled(40000)) {
            return;
        }
        if (Level.ERROR.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(FQCN, Level.ERROR, object, throwable);
        }
    }

    public void fatal(Object object, Throwable throwable) {
        if (this.repository.isDisabled(50000)) {
            return;
        }
        if (Level.FATAL.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(FQCN, Level.FATAL, object, throwable);
        }
    }

    protected void forcedLog(String string, Priority priority, Object object, Throwable throwable) {
        this.callAppenders(new LoggingEvent(string, this, priority, object, throwable));
    }

    public synchronized Enumeration getAllAppenders() {
        if (this.aai == null) {
            return NullEnumeration.getInstance();
        }
        return this.aai.getAllAppenders();
    }

    public Level getEffectiveLevel() {
        Category category = this;
        while (category != null) {
            if (category.level != null) {
                return category.level;
            }
            category = category.parent;
        }
        return null;
    }

    public final String getName() {
        return this.name;
    }

    public final Level getLevel() {
        return this.level;
    }

    public void info(Object object) {
        if (this.repository.isDisabled(20000)) {
            return;
        }
        if (Level.INFO.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(FQCN, Level.INFO, object, null);
        }
    }

    public void info(Object object, Throwable throwable) {
        if (this.repository.isDisabled(20000)) {
            return;
        }
        if (Level.INFO.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(FQCN, Level.INFO, object, throwable);
        }
    }

    public boolean isDebugEnabled() {
        if (this.repository.isDisabled(10000)) {
            return false;
        }
        return Level.DEBUG.isGreaterOrEqual(this.getEffectiveLevel());
    }

    public boolean isEnabledFor(Priority priority) {
        if (this.repository.isDisabled(priority.level)) {
            return false;
        }
        return priority.isGreaterOrEqual(this.getEffectiveLevel());
    }

    public void log(String string, Priority priority, Object object, Throwable throwable) {
        if (this.repository.isDisabled(priority.level)) {
            return;
        }
        if (priority.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(string, priority, object, throwable);
        }
    }

    private void fireRemoveAppenderEvent(Appender appender) {
        if (appender != null) {
            if (this.repository instanceof Hierarchy) {
                ((Hierarchy)this.repository).fireRemoveAppenderEvent(this, appender);
            } else if (this.repository instanceof HierarchyEventListener) {
                ((HierarchyEventListener)((Object)this.repository)).removeAppenderEvent(this, appender);
            }
        }
    }

    public synchronized void removeAllAppenders() {
        if (this.aai != null) {
            Vector vector = new Vector();
            Enumeration enumeration = this.aai.getAllAppenders();
            while (enumeration != null && enumeration.hasMoreElements()) {
                vector.add(enumeration.nextElement());
            }
            this.aai.removeAllAppenders();
            enumeration = vector.elements();
            while (enumeration.hasMoreElements()) {
                this.fireRemoveAppenderEvent((Appender)enumeration.nextElement());
            }
            this.aai = null;
        }
    }

    public void setAdditivity(boolean bl) {
        this.additive = bl;
    }

    final void setHierarchy(LoggerRepository loggerRepository) {
        this.repository = loggerRepository;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void warn(Object object) {
        if (this.repository.isDisabled(30000)) {
            return;
        }
        if (Level.WARN.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(FQCN, Level.WARN, object, null);
        }
    }

    public void warn(Object object, Throwable throwable) {
        if (this.repository.isDisabled(30000)) {
            return;
        }
        if (Level.WARN.isGreaterOrEqual(this.getEffectiveLevel())) {
            this.forcedLog(FQCN, Level.WARN, object, throwable);
        }
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError().initCause(classNotFoundException);
        }
    }

    static {
        Class class_ = class$org$apache$log4j$Category == null ? (Category.class$org$apache$log4j$Category = Category.class$("org.apache.log4j.Category")) : class$org$apache$log4j$Category;
        FQCN = class_.getName();
    }
}

