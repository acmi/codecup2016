/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.CategoryKey;
import org.apache.log4j.DefaultCategoryFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.ProvisionNode;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.spi.ThrowableRendererSupport;

public class Hierarchy
implements LoggerRepository,
RendererSupport,
ThrowableRendererSupport {
    private LoggerFactory defaultFactory;
    private Vector listeners = new Vector(1);
    Hashtable ht = new Hashtable();
    Logger root;
    RendererMap rendererMap;
    int thresholdInt;
    Level threshold;
    boolean emittedNoAppenderWarning = false;
    boolean emittedNoResourceBundleWarning = false;
    private ThrowableRenderer throwableRenderer = null;

    public Hierarchy(Logger logger) {
        this.root = logger;
        this.setThreshold(Level.ALL);
        this.root.setHierarchy(this);
        this.rendererMap = new RendererMap();
        this.defaultFactory = new DefaultCategoryFactory();
    }

    public void emitNoAppenderWarning(Category category) {
        if (!this.emittedNoAppenderWarning) {
            LogLog.warn("No appenders could be found for logger (" + category.getName() + ").");
            LogLog.warn("Please initialize the log4j system properly.");
            LogLog.warn("See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.");
            this.emittedNoAppenderWarning = true;
        }
    }

    public void setThreshold(Level level) {
        if (level != null) {
            this.thresholdInt = level.level;
            this.threshold = level;
        }
    }

    public void fireAddAppenderEvent(Category category, Appender appender) {
        if (this.listeners != null) {
            int n2 = this.listeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                HierarchyEventListener hierarchyEventListener = (HierarchyEventListener)this.listeners.elementAt(i2);
                hierarchyEventListener.addAppenderEvent(category, appender);
            }
        }
    }

    void fireRemoveAppenderEvent(Category category, Appender appender) {
        if (this.listeners != null) {
            int n2 = this.listeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                HierarchyEventListener hierarchyEventListener = (HierarchyEventListener)this.listeners.elementAt(i2);
                hierarchyEventListener.removeAppenderEvent(category, appender);
            }
        }
    }

    public Level getThreshold() {
        return this.threshold;
    }

    public Logger getLogger(String string) {
        return this.getLogger(string, this.defaultFactory);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Logger getLogger(String string, LoggerFactory loggerFactory) {
        CategoryKey categoryKey = new CategoryKey(string);
        Hashtable hashtable = this.ht;
        synchronized (hashtable) {
            Object v2 = this.ht.get(categoryKey);
            if (v2 == null) {
                Logger logger = loggerFactory.makeNewLoggerInstance(string);
                logger.setHierarchy(this);
                this.ht.put(categoryKey, logger);
                this.updateParents(logger);
                return logger;
            }
            if (v2 instanceof Logger) {
                return (Logger)v2;
            }
            if (v2 instanceof ProvisionNode) {
                Logger logger = loggerFactory.makeNewLoggerInstance(string);
                logger.setHierarchy(this);
                this.ht.put(categoryKey, logger);
                this.updateChildren((ProvisionNode)v2, logger);
                this.updateParents(logger);
                return logger;
            }
            return null;
        }
    }

    public Enumeration getCurrentLoggers() {
        Vector vector = new Vector(this.ht.size());
        Enumeration enumeration = this.ht.elements();
        while (enumeration.hasMoreElements()) {
            Object v2 = enumeration.nextElement();
            if (!(v2 instanceof Logger)) continue;
            vector.addElement(v2);
        }
        return vector.elements();
    }

    public Logger getRootLogger() {
        return this.root;
    }

    public boolean isDisabled(int n2) {
        return this.thresholdInt > n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void resetConfiguration() {
        this.getRootLogger().setLevel(Level.DEBUG);
        this.root.setResourceBundle(null);
        this.setThreshold(Level.ALL);
        Hashtable hashtable = this.ht;
        synchronized (hashtable) {
            this.shutdown();
            Enumeration enumeration = this.getCurrentLoggers();
            while (enumeration.hasMoreElements()) {
                Logger logger = (Logger)enumeration.nextElement();
                logger.setLevel(null);
                logger.setAdditivity(true);
                logger.setResourceBundle(null);
            }
        }
        this.rendererMap.clear();
        this.throwableRenderer = null;
    }

    public void setRenderer(Class class_, ObjectRenderer objectRenderer) {
        this.rendererMap.put(class_, objectRenderer);
    }

    public void setThrowableRenderer(ThrowableRenderer throwableRenderer) {
        this.throwableRenderer = throwableRenderer;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void shutdown() {
        Logger logger = this.getRootLogger();
        logger.closeNestedAppenders();
        Hashtable hashtable = this.ht;
        synchronized (hashtable) {
            Logger logger2;
            Enumeration enumeration = this.getCurrentLoggers();
            while (enumeration.hasMoreElements()) {
                logger2 = (Logger)enumeration.nextElement();
                logger2.closeNestedAppenders();
            }
            logger.removeAllAppenders();
            enumeration = this.getCurrentLoggers();
            while (enumeration.hasMoreElements()) {
                logger2 = (Logger)enumeration.nextElement();
                logger2.removeAllAppenders();
            }
        }
    }

    private final void updateParents(Logger logger) {
        String string = logger.name;
        int n2 = string.length();
        boolean bl = false;
        int n3 = string.lastIndexOf(46, n2 - 1);
        while (n3 >= 0) {
            String string2 = string.substring(0, n3);
            CategoryKey categoryKey = new CategoryKey(string2);
            Object v2 = this.ht.get(categoryKey);
            if (v2 == null) {
                ProvisionNode provisionNode = new ProvisionNode(logger);
                this.ht.put(categoryKey, provisionNode);
            } else {
                if (v2 instanceof Category) {
                    bl = true;
                    logger.parent = (Category)v2;
                    break;
                }
                if (v2 instanceof ProvisionNode) {
                    ((ProvisionNode)v2).addElement(logger);
                } else {
                    IllegalStateException illegalStateException = new IllegalStateException("unexpected object type " + v2.getClass() + " in ht.");
                    illegalStateException.printStackTrace();
                }
            }
            n3 = string.lastIndexOf(46, n3 - 1);
        }
        if (!bl) {
            logger.parent = this.root;
        }
    }

    private final void updateChildren(ProvisionNode provisionNode, Logger logger) {
        int n2 = provisionNode.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Logger logger2 = (Logger)provisionNode.elementAt(i2);
            if (logger2.parent.name.startsWith(logger.name)) continue;
            logger.parent = logger2.parent;
            logger2.parent = logger;
        }
    }
}

