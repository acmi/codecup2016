/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.helpers;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;

public class OptionConverter {
    static String DELIM_START = "${";
    static char DELIM_STOP = 125;
    static int DELIM_START_LEN = 2;
    static int DELIM_STOP_LEN = 1;
    static Class class$java$lang$String;
    static Class class$org$apache$log4j$Level;
    static Class class$org$apache$log4j$spi$Configurator;

    public static String getSystemProperty(String string, String string2) {
        try {
            return System.getProperty(string, string2);
        }
        catch (Throwable throwable) {
            LogLog.debug("Was not allowed to read system property \"" + string + "\".");
            return string2;
        }
    }

    public static Object instantiateByKey(Properties properties, String string, Class class_, Object object) {
        String string2 = OptionConverter.findAndSubst(string, properties);
        if (string2 == null) {
            LogLog.error("Could not find value for key " + string);
            return object;
        }
        return OptionConverter.instantiateByClassName(string2.trim(), class_, object);
    }

    public static boolean toBoolean(String string, boolean bl) {
        if (string == null) {
            return bl;
        }
        String string2 = string.trim();
        if ("true".equalsIgnoreCase(string2)) {
            return true;
        }
        if ("false".equalsIgnoreCase(string2)) {
            return false;
        }
        return bl;
    }

    public static Level toLevel(String string, Level level) {
        if (string == null) {
            return level;
        }
        int n2 = (string = string.trim()).indexOf(35);
        if (n2 == -1) {
            if ("NULL".equalsIgnoreCase(string)) {
                return null;
            }
            return Level.toLevel(string, level);
        }
        Level level2 = level;
        String string2 = string.substring(n2 + 1);
        String string3 = string.substring(0, n2);
        if ("NULL".equalsIgnoreCase(string3)) {
            return null;
        }
        LogLog.debug("toLevel:class=[" + string2 + "]" + ":pri=[" + string3 + "]");
        try {
            Class class_ = Loader.loadClass(string2);
            Class[] arrclass = new Class[2];
            Class class_2 = class$java$lang$String == null ? (OptionConverter.class$java$lang$String = OptionConverter.class$("java.lang.String")) : class$java$lang$String;
            arrclass[0] = class_2;
            Class class_3 = class$org$apache$log4j$Level == null ? (OptionConverter.class$org$apache$log4j$Level = OptionConverter.class$("org.apache.log4j.Level")) : class$org$apache$log4j$Level;
            arrclass[1] = class_3;
            Class[] arrclass2 = arrclass;
            Method method = class_.getMethod("toLevel", arrclass2);
            Object[] arrobject = new Object[]{string3, level};
            Object object = method.invoke(null, arrobject);
            level2 = (Level)object;
        }
        catch (ClassNotFoundException classNotFoundException) {
            LogLog.warn("custom level class [" + string2 + "] not found.");
        }
        catch (NoSuchMethodException noSuchMethodException) {
            LogLog.warn("custom level class [" + string2 + "]" + " does not have a class function toLevel(String, Level)", noSuchMethodException);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getTargetException() instanceof InterruptedException || invocationTargetException.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LogLog.warn("custom level class [" + string2 + "]" + " could not be instantiated", invocationTargetException);
        }
        catch (ClassCastException classCastException) {
            LogLog.warn("class [" + string2 + "] is not a subclass of org.apache.log4j.Level", classCastException);
        }
        catch (IllegalAccessException illegalAccessException) {
            LogLog.warn("class [" + string2 + "] cannot be instantiated due to access restrictions", illegalAccessException);
        }
        catch (RuntimeException runtimeException) {
            LogLog.warn("class [" + string2 + "], level [" + string3 + "] conversion failed.", runtimeException);
        }
        return level2;
    }

    public static String findAndSubst(String string, Properties properties) {
        String string2 = properties.getProperty(string);
        if (string2 == null) {
            return null;
        }
        try {
            return OptionConverter.substVars(string2, properties);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            LogLog.error("Bad option value [" + string2 + "].", illegalArgumentException);
            return string2;
        }
    }

    public static Object instantiateByClassName(String string, Class class_, Object object) {
        if (string != null) {
            try {
                Class class_2 = Loader.loadClass(string);
                if (!class_.isAssignableFrom(class_2)) {
                    LogLog.error("A \"" + string + "\" object is not assignable to a \"" + class_.getName() + "\" variable.");
                    LogLog.error("The class \"" + class_.getName() + "\" was loaded by ");
                    LogLog.error("[" + class_.getClassLoader() + "] whereas object of type ");
                    LogLog.error("\"" + class_2.getName() + "\" was loaded by [" + class_2.getClassLoader() + "].");
                    return object;
                }
                return class_2.newInstance();
            }
            catch (ClassNotFoundException classNotFoundException) {
                LogLog.error("Could not instantiate class [" + string + "].", classNotFoundException);
            }
            catch (IllegalAccessException illegalAccessException) {
                LogLog.error("Could not instantiate class [" + string + "].", illegalAccessException);
            }
            catch (InstantiationException instantiationException) {
                LogLog.error("Could not instantiate class [" + string + "].", instantiationException);
            }
            catch (RuntimeException runtimeException) {
                LogLog.error("Could not instantiate class [" + string + "].", runtimeException);
            }
        }
        return object;
    }

    public static String substVars(String string, Properties properties) throws IllegalArgumentException {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        do {
            int n3;
            if ((n3 = string.indexOf(DELIM_START, n2)) == -1) {
                if (n2 == 0) {
                    return string;
                }
                stringBuffer.append(string.substring(n2, string.length()));
                return stringBuffer.toString();
            }
            stringBuffer.append(string.substring(n2, n3));
            int n4 = string.indexOf(DELIM_STOP, n3);
            if (n4 == -1) {
                throw new IllegalArgumentException("" + '\"' + string + "\" has no closing brace. Opening brace at position " + n3 + '.');
            }
            String string2 = string.substring(n3 += DELIM_START_LEN, n4);
            String string3 = OptionConverter.getSystemProperty(string2, null);
            if (string3 == null && properties != null) {
                string3 = properties.getProperty(string2);
            }
            if (string3 != null) {
                String string4 = OptionConverter.substVars(string3, properties);
                stringBuffer.append(string4);
            }
            n2 = n4 + DELIM_STOP_LEN;
        } while (true);
    }

    public static void selectAndConfigure(URL uRL, String string, LoggerRepository loggerRepository) {
        Configurator configurator = null;
        String string2 = uRL.getFile();
        if (string == null && string2 != null && string2.endsWith(".xml")) {
            string = "org.apache.log4j.xml.DOMConfigurator";
        }
        if (string != null) {
            LogLog.debug("Preferred configurator class: " + string);
            Class class_ = class$org$apache$log4j$spi$Configurator == null ? (OptionConverter.class$org$apache$log4j$spi$Configurator = OptionConverter.class$("org.apache.log4j.spi.Configurator")) : class$org$apache$log4j$spi$Configurator;
            configurator = (Configurator)OptionConverter.instantiateByClassName(string, class_, null);
            if (configurator == null) {
                LogLog.error("Could not instantiate configurator [" + string + "].");
                return;
            }
        } else {
            configurator = new PropertyConfigurator();
        }
        configurator.doConfigure(uRL, loggerRepository);
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

