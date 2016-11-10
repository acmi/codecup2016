/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.datatype;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;
import javax.xml.datatype.SecuritySupport;

final class FactoryFinder {
    private static boolean debug = false;
    private static Properties cacheProps = new Properties();
    private static boolean firstTime = true;
    static Class class$javax$xml$datatype$FactoryFinder;

    private FactoryFinder() {
    }

    private static void debugPrintln(String string) {
        if (debug) {
            System.err.println("javax.xml.datatype.FactoryFinder:" + string);
        }
    }

    private static ClassLoader findClassLoader() throws ConfigurationError {
        ClassLoader classLoader = SecuritySupport.getContextClassLoader();
        if (debug) {
            FactoryFinder.debugPrintln("Using context class loader: " + classLoader);
        }
        if (classLoader == null) {
            Class class_ = class$javax$xml$datatype$FactoryFinder == null ? (FactoryFinder.class$javax$xml$datatype$FactoryFinder = FactoryFinder.class$("javax.xml.datatype.FactoryFinder")) : class$javax$xml$datatype$FactoryFinder;
            classLoader = class_.getClassLoader();
            if (debug) {
                FactoryFinder.debugPrintln("Using the class loader of FactoryFinder: " + classLoader);
            }
        }
        return classLoader;
    }

    static Object newInstance(String string, ClassLoader classLoader) throws ConfigurationError {
        try {
            Class class_ = classLoader == null ? Class.forName(string) : classLoader.loadClass(string);
            if (debug) {
                FactoryFinder.debugPrintln("Loaded " + string + " from " + FactoryFinder.which(class_));
            }
            return class_.newInstance();
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new ConfigurationError("Provider " + string + " not found", classNotFoundException);
        }
        catch (Exception exception) {
            throw new ConfigurationError("Provider " + string + " could not be instantiated: " + exception, exception);
        }
    }

    static Object find(String string, String string2) throws ConfigurationError {
        ClassLoader classLoader;
        Object object;
        block18 : {
            classLoader = FactoryFinder.findClassLoader();
            try {
                object = SecuritySupport.getSystemProperty(string);
                if (object != null && object.length() > 0) {
                    if (debug) {
                        FactoryFinder.debugPrintln("found " + (String)object + " in the system property " + string);
                    }
                    return FactoryFinder.newInstance((String)object, classLoader);
                }
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
            try {
                object = SecuritySupport.getSystemProperty("java.home");
                String string3 = (String)object + File.separator + "lib" + File.separator + "jaxp.properties";
                String string4 = null;
                if (firstTime) {
                    Properties properties = cacheProps;
                    synchronized (properties) {
                        if (firstTime) {
                            File file = new File(string3);
                            firstTime = false;
                            if (SecuritySupport.doesFileExist(file)) {
                                if (debug) {
                                    FactoryFinder.debugPrintln("Read properties file " + file);
                                }
                                cacheProps.load(SecuritySupport.getFileInputStream(file));
                            }
                        }
                    }
                }
                string4 = cacheProps.getProperty(string);
                if (debug) {
                    FactoryFinder.debugPrintln("found " + string4 + " in $java.home/jaxp.properties");
                }
                if (string4 != null) {
                    return FactoryFinder.newInstance(string4, classLoader);
                }
            }
            catch (Exception exception) {
                if (!debug) break block18;
                exception.printStackTrace();
            }
        }
        if ((object = FactoryFinder.findJarServiceProvider(string)) != null) {
            return object;
        }
        if (string2 == null) {
            throw new ConfigurationError("Provider for " + string + " cannot be found", null);
        }
        if (debug) {
            FactoryFinder.debugPrintln("loaded from fallback value: " + string2);
        }
        return FactoryFinder.newInstance(string2, classLoader);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static Object findJarServiceProvider(String var0) throws ConfigurationError {
        var1_1 = "META-INF/services/" + var0;
        var2_2 = null;
        var3_3 = SecuritySupport.getContextClassLoader();
        if (var3_3 != null) {
            var2_2 = SecuritySupport.getResourceAsStream(var3_3, var1_1);
            if (var2_2 == null) {
                v0 = FactoryFinder.class$javax$xml$datatype$FactoryFinder == null ? (FactoryFinder.class$javax$xml$datatype$FactoryFinder = FactoryFinder.class$("javax.xml.datatype.FactoryFinder")) : FactoryFinder.class$javax$xml$datatype$FactoryFinder;
                var3_3 = v0.getClassLoader();
                var2_2 = SecuritySupport.getResourceAsStream(var3_3, var1_1);
            }
        } else {
            v1 = FactoryFinder.class$javax$xml$datatype$FactoryFinder == null ? (FactoryFinder.class$javax$xml$datatype$FactoryFinder = FactoryFinder.class$("javax.xml.datatype.FactoryFinder")) : FactoryFinder.class$javax$xml$datatype$FactoryFinder;
            var3_3 = v1.getClassLoader();
            var2_2 = SecuritySupport.getResourceAsStream(var3_3, var1_1);
        }
        if (var2_2 == null) {
            return null;
        }
        if (FactoryFinder.debug) {
            FactoryFinder.debugPrintln("found jar resource=" + var1_1 + " using ClassLoader: " + var3_3);
        }
        try {
            var4_4 = new BufferedReader(new InputStreamReader(var2_2, "UTF-8"), 80);
        }
        catch (UnsupportedEncodingException var5_5) {
            var4_4 = new BufferedReader(new InputStreamReader(var2_2), 80);
        }
        var5_6 = null;
        try {
            try {
                var5_6 = var4_4.readLine();
            }
            catch (IOException var6_13) {
                var7_14 = null;
                var9_8 = null;
                ** try [egrp 3[TRYBLOCK] [3 : 230->238)] { 
lbl32: // 1 sources:
                var4_4.close();
                return var7_14;
lbl34: // 1 sources:
                catch (IOException var10_11) {
                    // empty catch block
                }
                return var7_14;
            }
            var9_7 = null;
            var4_4.close();
            catch (IOException var10_10) {}
            if (var5_6 == null) return null;
            if ("".equals(var5_6) != false) return null;
            if (FactoryFinder.debug == false) return FactoryFinder.newInstance(var5_6, var3_3);
            FactoryFinder.debugPrintln("found in resource, value=" + var5_6);
            return FactoryFinder.newInstance(var5_6, var3_3);
        }
        catch (Throwable var8_15) {
            var9_9 = null;
            ** try [egrp 3[TRYBLOCK] [3 : 230->238)] { 
lbl52: // 1 sources:
            var4_4.close();
            throw var8_15;
lbl54: // 1 sources:
            catch (IOException var10_12) {
                // empty catch block
            }
            throw var8_15;
        }
    }

    private static String which(Class class_) {
        block5 : {
            try {
                String string = class_.getName().replace('.', '/') + ".class";
                ClassLoader classLoader = class_.getClassLoader();
                URL uRL = classLoader != null ? classLoader.getResource(string) : ClassLoader.getSystemResource(string);
                if (uRL != null) {
                    return uRL.toString();
                }
            }
            catch (VirtualMachineError virtualMachineError) {
                throw virtualMachineError;
            }
            catch (ThreadDeath threadDeath) {
                throw threadDeath;
            }
            catch (Throwable throwable) {
                if (!debug) break block5;
                throwable.printStackTrace();
            }
        }
        return "unknown location";
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static {
        try {
            String string = SecuritySupport.getSystemProperty("jaxp.debug");
            debug = string != null && !"false".equals(string);
        }
        catch (Exception exception) {
            debug = false;
        }
    }

    static class ConfigurationError
    extends Error {
        private Exception exception;

        ConfigurationError(String string, Exception exception) {
            super(string);
            this.exception = exception;
        }

        Exception getException() {
            return this.exception;
        }
    }

}

