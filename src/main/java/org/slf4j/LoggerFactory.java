/*
 * Decompiled with CFR 0_119.
 */
package org.slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.event.LoggingEvent;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.helpers.SubstituteLoggerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticLoggerBinder;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class LoggerFactory {
    static volatile int INITIALIZATION_STATE = 0;
    static SubstituteLoggerFactory SUBST_FACTORY = new SubstituteLoggerFactory();
    static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
    static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
    private static final String[] API_COMPATIBILITY_LIST = new String[]{"1.6", "1.7"};
    private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";

    private LoggerFactory() {
    }

    private static final void performInitialization() {
        LoggerFactory.bind();
        if (INITIALIZATION_STATE == 3) {
            LoggerFactory.versionSanityCheck();
        }
    }

    private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String string) {
        if (string == null) {
            return false;
        }
        if (string.contains("org/slf4j/impl/StaticLoggerBinder")) {
            return true;
        }
        if (string.contains("org.slf4j.impl.StaticLoggerBinder")) {
            return true;
        }
        return false;
    }

    private static final void bind() {
        try {
            Set<URL> set = null;
            if (!LoggerFactory.isAndroid()) {
                set = LoggerFactory.findPossibleStaticLoggerBinderPathSet();
                LoggerFactory.reportMultipleBindingAmbiguity(set);
            }
            StaticLoggerBinder.getSingleton();
            INITIALIZATION_STATE = 3;
            LoggerFactory.reportActualBinding(set);
            LoggerFactory.fixSubstituteLoggers();
            LoggerFactory.replayEvents();
            SUBST_FACTORY.clear();
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            String string = noClassDefFoundError.getMessage();
            if (LoggerFactory.messageContainsOrgSlf4jImplStaticLoggerBinder(string)) {
                INITIALIZATION_STATE = 4;
                Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
                Util.report("Defaulting to no-operation (NOP) logger implementation");
                Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
            }
            LoggerFactory.failedBinding(noClassDefFoundError);
            throw noClassDefFoundError;
        }
        catch (NoSuchMethodError noSuchMethodError) {
            String string = noSuchMethodError.getMessage();
            if (string != null && string.contains("org.slf4j.impl.StaticLoggerBinder.getSingleton()")) {
                INITIALIZATION_STATE = 2;
                Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
                Util.report("Your binding is version 1.5.5 or earlier.");
                Util.report("Upgrade your binding to version 1.6.x.");
            }
            throw noSuchMethodError;
        }
        catch (Exception exception) {
            LoggerFactory.failedBinding(exception);
            throw new IllegalStateException("Unexpected initialization failure", exception);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void fixSubstituteLoggers() {
        SubstituteLoggerFactory substituteLoggerFactory = SUBST_FACTORY;
        synchronized (substituteLoggerFactory) {
            SUBST_FACTORY.postInitialization();
            for (SubstituteLogger substituteLogger : SUBST_FACTORY.getLoggers()) {
                Logger logger = LoggerFactory.getLogger(substituteLogger.getName());
                substituteLogger.setDelegate(logger);
            }
        }
    }

    static void failedBinding(Throwable throwable) {
        INITIALIZATION_STATE = 2;
        Util.report("Failed to instantiate SLF4J LoggerFactory", throwable);
    }

    private static void replayEvents() {
        int n2;
        LinkedBlockingQueue<SubstituteLoggingEvent> linkedBlockingQueue = SUBST_FACTORY.getEventQueue();
        int n3 = linkedBlockingQueue.size();
        int n4 = 0;
        int n5 = 128;
        ArrayList arrayList = new ArrayList(128);
        while ((n2 = linkedBlockingQueue.drainTo(arrayList, 128)) != 0) {
            for (SubstituteLoggingEvent substituteLoggingEvent : arrayList) {
                LoggerFactory.replaySingleEvent(substituteLoggingEvent);
                if (n4++ != 0) continue;
                LoggerFactory.emitReplayOrSubstituionWarning(substituteLoggingEvent, n3);
            }
            arrayList.clear();
        }
    }

    private static void emitReplayOrSubstituionWarning(SubstituteLoggingEvent substituteLoggingEvent, int n2) {
        if (substituteLoggingEvent.getLogger().isDelegateEventAware()) {
            LoggerFactory.emitReplayWarning(n2);
        } else if (!substituteLoggingEvent.getLogger().isDelegateNOP()) {
            LoggerFactory.emitSubstitutionWarning();
        }
    }

    private static void replaySingleEvent(SubstituteLoggingEvent substituteLoggingEvent) {
        if (substituteLoggingEvent == null) {
            return;
        }
        SubstituteLogger substituteLogger = substituteLoggingEvent.getLogger();
        String string = substituteLogger.getName();
        if (substituteLogger.isDelegateNull()) {
            throw new IllegalStateException("Delegate logger cannot be null at this state.");
        }
        if (!substituteLogger.isDelegateNOP()) {
            if (substituteLogger.isDelegateEventAware()) {
                substituteLogger.log(substituteLoggingEvent);
            } else {
                Util.report(string);
            }
        }
    }

    private static void emitSubstitutionWarning() {
        Util.report("The following set of substitute loggers may have been accessed");
        Util.report("during the initialization phase. Logging calls during this");
        Util.report("phase were not honored. However, subsequent logging calls to these");
        Util.report("loggers will work as normally expected.");
        Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
    }

    private static void emitReplayWarning(int n2) {
        Util.report("A number (" + n2 + ") of logging calls during the initialization phase have been intercepted and are");
        Util.report("now being replayed. These are subject to the filtering rules of the underlying logging system.");
        Util.report("See also http://www.slf4j.org/codes.html#replay");
    }

    private static final void versionSanityCheck() {
        try {
            String string = StaticLoggerBinder.REQUESTED_API_VERSION;
            boolean bl = false;
            for (String string2 : API_COMPATIBILITY_LIST) {
                if (!string.startsWith(string2)) continue;
                bl = true;
            }
            if (!bl) {
                Util.report("The requested version " + string + " by your slf4j binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
                Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
            }
        }
        catch (NoSuchFieldError noSuchFieldError) {
        }
        catch (Throwable throwable) {
            Util.report("Unexpected problem occured during version sanity check", throwable);
        }
    }

    static Set<URL> findPossibleStaticLoggerBinderPathSet() {
        LinkedHashSet<URL> linkedHashSet = new LinkedHashSet<URL>();
        try {
            ClassLoader classLoader = LoggerFactory.class.getClassLoader();
            Enumeration<URL> enumeration = classLoader == null ? ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH) : classLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            while (enumeration.hasMoreElements()) {
                URL uRL = enumeration.nextElement();
                linkedHashSet.add(uRL);
            }
        }
        catch (IOException iOException) {
            Util.report("Error getting resources from path", iOException);
        }
        return linkedHashSet;
    }

    private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> set) {
        return set.size() > 1;
    }

    private static void reportMultipleBindingAmbiguity(Set<URL> set) {
        if (LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(set)) {
            Util.report("Class path contains multiple SLF4J bindings.");
            for (URL uRL : set) {
                Util.report("Found binding in [" + uRL + "]");
            }
            Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
        }
    }

    private static boolean isAndroid() {
        String string = Util.safeGetSystemProperty("java.vendor.url");
        if (string == null) {
            return false;
        }
        return string.toLowerCase().contains("android");
    }

    private static void reportActualBinding(Set<URL> set) {
        if (set != null && LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(set)) {
            Util.report("Actual binding is of type [" + StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr() + "]");
        }
    }

    public static Logger getLogger(String string) {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        return iLoggerFactory.getLogger(string);
    }

    public static Logger getLogger(Class<?> class_) {
        Class class_2;
        Logger logger = LoggerFactory.getLogger(class_.getName());
        if (DETECT_LOGGER_NAME_MISMATCH && (class_2 = Util.getCallingClass()) != null && LoggerFactory.nonMatchingClasses(class_, class_2)) {
            Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", logger.getName(), class_2.getName()));
            Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
        }
        return logger;
    }

    private static boolean nonMatchingClasses(Class<?> class_, Class<?> class_2) {
        return !class_2.isAssignableFrom(class_);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static ILoggerFactory getILoggerFactory() {
        if (INITIALIZATION_STATE == 0) {
            Class<LoggerFactory> class_ = LoggerFactory.class;
            // MONITORENTER : org.slf4j.LoggerFactory.class
            if (INITIALIZATION_STATE == 0) {
                INITIALIZATION_STATE = 1;
                LoggerFactory.performInitialization();
            }
            // MONITOREXIT : class_
        }
        switch (INITIALIZATION_STATE) {
            case 3: {
                return StaticLoggerBinder.getSingleton().getLoggerFactory();
            }
            case 4: {
                return NOP_FALLBACK_FACTORY;
            }
            case 2: {
                throw new IllegalStateException("org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
            }
            case 1: {
                return SUBST_FACTORY;
            }
        }
        throw new IllegalStateException("Unreachable code");
    }
}

