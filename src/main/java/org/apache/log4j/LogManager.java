/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.NOPLoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootLogger;

public class LogManager {
    private static Object guard = null;
    private static RepositorySelector repositorySelector;

    private static boolean isLikelySafeScenario(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        String string = stringWriter.toString();
        return string.indexOf("org.apache.catalina.loader.WebappClassLoader.stop") != -1;
    }

    public static LoggerRepository getLoggerRepository() {
        if (repositorySelector == null) {
            repositorySelector = new DefaultRepositorySelector(new NOPLoggerRepository());
            guard = null;
            IllegalStateException illegalStateException = new IllegalStateException("Class invariant violation");
            String string = "log4j called after unloading, see http://logging.apache.org/log4j/1.2/faq.html#unload.";
            if (LogManager.isLikelySafeScenario(illegalStateException)) {
                LogLog.debug(string, illegalStateException);
            } else {
                LogLog.error(string, illegalStateException);
            }
        }
        return repositorySelector.getLoggerRepository();
    }

    public static Logger getRootLogger() {
        return LogManager.getLoggerRepository().getRootLogger();
    }

    public static Logger getLogger(String string) {
        return LogManager.getLoggerRepository().getLogger(string);
    }

    static {
        Hierarchy hierarchy = new Hierarchy(new RootLogger(Level.DEBUG));
        repositorySelector = new DefaultRepositorySelector(hierarchy);
        String string = OptionConverter.getSystemProperty("log4j.defaultInitOverride", null);
        if (string == null || "false".equalsIgnoreCase(string)) {
            String string2 = OptionConverter.getSystemProperty("log4j.configuration", null);
            String string3 = OptionConverter.getSystemProperty("log4j.configuratorClass", null);
            URL uRL = null;
            if (string2 == null) {
                uRL = Loader.getResource("log4j.xml");
                if (uRL == null) {
                    uRL = Loader.getResource("log4j.properties");
                }
            } else {
                try {
                    uRL = new URL(string2);
                }
                catch (MalformedURLException malformedURLException) {
                    uRL = Loader.getResource(string2);
                }
            }
            if (uRL != null) {
                LogLog.debug("Using URL [" + uRL + "] for automatic log4j configuration.");
                try {
                    OptionConverter.selectAndConfigure(uRL, string3, LogManager.getLoggerRepository());
                }
                catch (NoClassDefFoundError noClassDefFoundError) {
                    LogLog.warn("Error during default initialization", noClassDefFoundError);
                }
            } else {
                LogLog.debug("Could not find resource: [" + string2 + "].");
            }
        } else {
            LogLog.debug("Default initialization of overridden by log4j.defaultInitOverrideproperty.");
        }
    }
}

