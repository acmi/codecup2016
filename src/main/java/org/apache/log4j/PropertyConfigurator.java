/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.log4j.Appender;
import org.apache.log4j.DefaultCategoryFactory;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.NameValue;
import org.apache.log4j.SortedKeyEnumeration;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.spi.ThrowableRendererSupport;

public class PropertyConfigurator
implements Configurator {
    protected Hashtable registry = new Hashtable(11);
    private LoggerRepository repository;
    protected LoggerFactory loggerFactory = new DefaultCategoryFactory();
    static Class class$org$apache$log4j$spi$LoggerFactory;
    static Class class$org$apache$log4j$spi$ThrowableRenderer;
    static Class class$org$apache$log4j$Appender;
    static Class class$org$apache$log4j$Layout;
    static Class class$org$apache$log4j$spi$ErrorHandler;
    static Class class$org$apache$log4j$spi$Filter;

    public void doConfigure(Properties properties, LoggerRepository loggerRepository) {
        String string;
        String string2;
        this.repository = loggerRepository;
        String string3 = properties.getProperty("log4j.debug");
        if (string3 == null && (string3 = properties.getProperty("log4j.configDebug")) != null) {
            LogLog.warn("[log4j.configDebug] is deprecated. Use [log4j.debug] instead.");
        }
        if (string3 != null) {
            LogLog.setInternalDebugging(OptionConverter.toBoolean(string3, true));
        }
        if ((string = properties.getProperty("log4j.reset")) != null && OptionConverter.toBoolean(string, false)) {
            loggerRepository.resetConfiguration();
        }
        if ((string2 = OptionConverter.findAndSubst("log4j.threshold", properties)) != null) {
            loggerRepository.setThreshold(OptionConverter.toLevel(string2, Level.ALL));
            LogLog.debug("Hierarchy threshold set to [" + loggerRepository.getThreshold() + "].");
        }
        this.configureRootCategory(properties, loggerRepository);
        this.configureLoggerFactory(properties);
        this.parseCatsAndRenderers(properties, loggerRepository);
        LogLog.debug("Finished configuring.");
        this.registry.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void doConfigure(URL uRL, LoggerRepository loggerRepository) {
        Properties properties = new Properties();
        LogLog.debug("Reading configuration from URL " + uRL);
        InputStream inputStream = null;
        URLConnection uRLConnection = null;
        try {
            uRLConnection = uRL.openConnection();
            uRLConnection.setUseCaches(false);
            inputStream = uRLConnection.getInputStream();
            properties.load(inputStream);
        }
        catch (Exception exception) {
            if (exception instanceof InterruptedIOException || exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not read configuration file from URL [" + uRL + "].", exception);
            LogLog.error("Ignoring configuration file [" + uRL + "].");
            return;
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (InterruptedIOException interruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                catch (IOException iOException) {
                }
                catch (RuntimeException runtimeException) {}
            }
        }
        this.doConfigure(properties, loggerRepository);
    }

    protected void configureLoggerFactory(Properties properties) {
        String string = OptionConverter.findAndSubst("log4j.loggerFactory", properties);
        if (string != null) {
            LogLog.debug("Setting category factory to [" + string + "].");
            Class class_ = class$org$apache$log4j$spi$LoggerFactory == null ? (PropertyConfigurator.class$org$apache$log4j$spi$LoggerFactory = PropertyConfigurator.class$("org.apache.log4j.spi.LoggerFactory")) : class$org$apache$log4j$spi$LoggerFactory;
            this.loggerFactory = (LoggerFactory)OptionConverter.instantiateByClassName(string, class_, this.loggerFactory);
            PropertySetter.setProperties(this.loggerFactory, properties, "log4j.factory.");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void configureRootCategory(Properties properties, LoggerRepository loggerRepository) {
        String string = "log4j.rootLogger";
        String string2 = OptionConverter.findAndSubst("log4j.rootLogger", properties);
        if (string2 == null) {
            string2 = OptionConverter.findAndSubst("log4j.rootCategory", properties);
            string = "log4j.rootCategory";
        }
        if (string2 == null) {
            LogLog.debug("Could not find root logger information. Is this OK?");
        } else {
            Logger logger;
            Logger logger2 = logger = loggerRepository.getRootLogger();
            synchronized (logger2) {
                this.parseCategory(properties, logger, string, "root", string2);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void parseCatsAndRenderers(Properties properties, LoggerRepository loggerRepository) {
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            Object object;
            Object object2;
            String string = (String)enumeration.nextElement();
            if (string.startsWith("log4j.category.") || string.startsWith("log4j.logger.")) {
                Logger logger;
                object2 = null;
                if (string.startsWith("log4j.category.")) {
                    object2 = string.substring("log4j.category.".length());
                } else if (string.startsWith("log4j.logger.")) {
                    object2 = string.substring("log4j.logger.".length());
                }
                object = OptionConverter.findAndSubst(string, properties);
                Logger logger2 = logger = loggerRepository.getLogger((String)object2, this.loggerFactory);
                synchronized (logger2) {
                    this.parseCategory(properties, logger, string, (String)object2, (String)object);
                    this.parseAdditivityForLogger(properties, logger, (String)object2);
                    continue;
                }
            }
            if (string.startsWith("log4j.renderer.")) {
                object2 = string.substring("log4j.renderer.".length());
                object = OptionConverter.findAndSubst(string, properties);
                if (!(loggerRepository instanceof RendererSupport)) continue;
                RendererMap.addRenderer((RendererSupport)((Object)loggerRepository), (String)object2, (String)object);
                continue;
            }
            if (!string.equals("log4j.throwableRenderer") || !(loggerRepository instanceof ThrowableRendererSupport)) continue;
            object2 = (ThrowableRenderer)OptionConverter.instantiateByKey(properties, "log4j.throwableRenderer", class$org$apache$log4j$spi$ThrowableRenderer == null ? PropertyConfigurator.class$("org.apache.log4j.spi.ThrowableRenderer") : class$org$apache$log4j$spi$ThrowableRenderer, null);
            if (object2 == null) {
                LogLog.error("Could not instantiate throwableRenderer.");
                continue;
            }
            object = new PropertySetter(object2);
            object.setProperties(properties, "log4j.throwableRenderer.");
            ((ThrowableRendererSupport)((Object)loggerRepository)).setThrowableRenderer((ThrowableRenderer)object2);
        }
    }

    void parseAdditivityForLogger(Properties properties, Logger logger, String string) {
        String string2 = OptionConverter.findAndSubst("log4j.additivity." + string, properties);
        LogLog.debug("Handling log4j.additivity." + string + "=[" + string2 + "]");
        if (string2 != null && !string2.equals("")) {
            boolean bl = OptionConverter.toBoolean(string2, true);
            LogLog.debug("Setting additivity for \"" + string + "\" to " + bl);
            logger.setAdditivity(bl);
        }
    }

    void parseCategory(Properties properties, Logger logger, String string, String string2, String string3) {
        Object object;
        LogLog.debug("Parsing for [" + string2 + "] with value=[" + string3 + "].");
        StringTokenizer stringTokenizer = new StringTokenizer(string3, ",");
        if (!string3.startsWith(",") && !string3.equals("")) {
            if (!stringTokenizer.hasMoreTokens()) {
                return;
            }
            object = stringTokenizer.nextToken();
            LogLog.debug("Level token is [" + (String)object + "].");
            if ("inherited".equalsIgnoreCase((String)object) || "null".equalsIgnoreCase((String)object)) {
                if (string2.equals("root")) {
                    LogLog.warn("The root logger cannot be set to null.");
                } else {
                    logger.setLevel(null);
                }
            } else {
                logger.setLevel(OptionConverter.toLevel((String)object, Level.DEBUG));
            }
            LogLog.debug("Category " + string2 + " set to " + logger.getLevel());
        }
        logger.removeAllAppenders();
        while (stringTokenizer.hasMoreTokens()) {
            String string4 = stringTokenizer.nextToken().trim();
            if (string4 == null || string4.equals(",")) continue;
            LogLog.debug("Parsing appender named \"" + string4 + "\".");
            object = this.parseAppender(properties, string4);
            if (object == null) continue;
            logger.addAppender((Appender)object);
        }
    }

    Appender parseAppender(Properties properties, String string) {
        Appender appender = this.registryGet(string);
        if (appender != null) {
            LogLog.debug("Appender \"" + string + "\" was already parsed.");
            return appender;
        }
        String string2 = "log4j.appender." + string;
        String string3 = string2 + ".layout";
        Class class_ = class$org$apache$log4j$Appender == null ? (PropertyConfigurator.class$org$apache$log4j$Appender = PropertyConfigurator.class$("org.apache.log4j.Appender")) : class$org$apache$log4j$Appender;
        appender = (Appender)OptionConverter.instantiateByKey(properties, string2, class_, null);
        if (appender == null) {
            LogLog.error("Could not instantiate appender named \"" + string + "\".");
            return null;
        }
        appender.setName(string);
        if (appender instanceof OptionHandler) {
            Object object;
            String string4;
            if (appender.requiresLayout()) {
                Class class_2 = class$org$apache$log4j$Layout == null ? (PropertyConfigurator.class$org$apache$log4j$Layout = PropertyConfigurator.class$("org.apache.log4j.Layout")) : class$org$apache$log4j$Layout;
                object = (Layout)OptionConverter.instantiateByKey(properties, string3, class_2, null);
                if (object != null) {
                    appender.setLayout((Layout)object);
                    LogLog.debug("Parsing layout options for \"" + string + "\".");
                    PropertySetter.setProperties(object, properties, string3 + ".");
                    LogLog.debug("End of parsing for \"" + string + "\".");
                }
            }
            if ((string4 = OptionConverter.findAndSubst((String)(object = string2 + ".errorhandler"), properties)) != null) {
                Class class_3 = class$org$apache$log4j$spi$ErrorHandler == null ? (PropertyConfigurator.class$org$apache$log4j$spi$ErrorHandler = PropertyConfigurator.class$("org.apache.log4j.spi.ErrorHandler")) : class$org$apache$log4j$spi$ErrorHandler;
                ErrorHandler errorHandler = (ErrorHandler)OptionConverter.instantiateByKey(properties, (String)object, class_3, null);
                if (errorHandler != null) {
                    appender.setErrorHandler(errorHandler);
                    LogLog.debug("Parsing errorhandler options for \"" + string + "\".");
                    this.parseErrorHandler(errorHandler, (String)object, properties, this.repository);
                    Properties properties2 = new Properties();
                    String[] arrstring = new String[]{(String)object + "." + "root-ref", (String)object + "." + "logger-ref", (String)object + "." + "appender-ref"};
                    Iterator iterator = properties.entrySet().iterator();
                    while (iterator.hasNext()) {
                        int n2;
                        Map.Entry entry = iterator.next();
                        for (n2 = 0; n2 < arrstring.length && !arrstring[n2].equals(entry.getKey()); ++n2) {
                        }
                        if (n2 != arrstring.length) continue;
                        properties2.put(entry.getKey(), entry.getValue());
                    }
                    PropertySetter.setProperties(errorHandler, properties2, (String)object + ".");
                    LogLog.debug("End of errorhandler parsing for \"" + string + "\".");
                }
            }
            PropertySetter.setProperties(appender, properties, string2 + ".");
            LogLog.debug("Parsed \"" + string + "\" options.");
        }
        this.parseAppenderFilters(properties, string, appender);
        this.registryPut(appender);
        return appender;
    }

    private void parseErrorHandler(ErrorHandler errorHandler, String string, Properties properties, LoggerRepository loggerRepository) {
        String string2;
        Object object;
        Appender appender;
        boolean bl = OptionConverter.toBoolean(OptionConverter.findAndSubst(string + "root-ref", properties), false);
        if (bl) {
            errorHandler.setLogger(loggerRepository.getRootLogger());
        }
        if ((string2 = OptionConverter.findAndSubst(string + "logger-ref", properties)) != null) {
            object = this.loggerFactory == null ? loggerRepository.getLogger(string2) : loggerRepository.getLogger(string2, this.loggerFactory);
            errorHandler.setLogger((Logger)object);
        }
        if ((object = OptionConverter.findAndSubst(string + "appender-ref", properties)) != null && (appender = this.parseAppender(properties, (String)object)) != null) {
            errorHandler.setBackupAppender(appender);
        }
    }

    void parseAppenderFilters(Properties properties, String string, Appender appender) {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        String string2 = "log4j.appender." + string + ".filter.";
        int n2 = string2.length();
        Hashtable hashtable = new Hashtable();
        Enumeration enumeration = properties.keys();
        String string3 = "";
        while (enumeration.hasMoreElements()) {
            object4 = (String)enumeration.nextElement();
            if (!object4.startsWith(string2)) continue;
            int n3 = object4.indexOf(46, n2);
            object3 = object4;
            if (n3 != -1) {
                object3 = object4.substring(0, n3);
                string3 = object4.substring(n3 + 1);
            }
            if ((object = (Vector<NameValue>)hashtable.get(object3)) == null) {
                object = new Vector<NameValue>();
                hashtable.put(object3, object);
            }
            if (n3 == -1) continue;
            object2 = OptionConverter.findAndSubst((String)object4, properties);
            object.add((NameValue)new NameValue(string3, (String)object2));
        }
        object4 = new SortedKeyEnumeration(hashtable);
        while (object4.hasMoreElements()) {
            String string4 = (String)object4.nextElement();
            object3 = properties.getProperty(string4);
            if (object3 != null) {
                LogLog.debug("Filter key: [" + string4 + "] class: [" + properties.getProperty(string4) + "] props: " + hashtable.get(string4));
                object = (Filter)OptionConverter.instantiateByClassName((String)object3, class$org$apache$log4j$spi$Filter == null ? PropertyConfigurator.class$("org.apache.log4j.spi.Filter") : class$org$apache$log4j$spi$Filter, null);
                if (object == null) continue;
                object2 = new PropertySetter(object);
                Vector vector = (Vector)hashtable.get(string4);
                Enumeration enumeration2 = vector.elements();
                while (enumeration2.hasMoreElements()) {
                    NameValue nameValue = (NameValue)enumeration2.nextElement();
                    object2.setProperty(nameValue.key, nameValue.value);
                }
                object2.activate();
                LogLog.debug("Adding filter of type [" + object.getClass() + "] to appender named [" + appender.getName() + "].");
                appender.addFilter((Filter)object);
                continue;
            }
            LogLog.warn("Missing class definition for filter: [" + string4 + "]");
        }
    }

    void registryPut(Appender appender) {
        this.registry.put(appender.getName(), appender);
    }

    Appender registryGet(String string) {
        return (Appender)this.registry.get(string);
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError().initCause(classNotFoundException);
        }
    }
}

