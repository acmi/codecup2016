/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.apache.xerces.parsers.SecuritySupport;

final class ObjectFactory {
    private static final String DEFAULT_PROPERTIES_FILENAME = "xerces.properties";
    private static final boolean DEBUG = ObjectFactory.isDebugEnabled();
    private static final int DEFAULT_LINE_LENGTH = 80;
    private static Properties fXercesProperties = null;
    private static long fLastModified = -1;
    static Class class$org$apache$xerces$parsers$ObjectFactory;

    ObjectFactory() {
    }

    static Object createObject(String string, String string2) throws ConfigurationError {
        return ObjectFactory.createObject(string, null, string2);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    static Object createObject(String var0, String var1_1, String var2_2) throws ConfigurationError {
        if (ObjectFactory.DEBUG) {
            ObjectFactory.debugPrintln("debug is on");
        }
        var3_3 = ObjectFactory.findClassLoader();
        try {
            var4_4 = SecuritySupport.getSystemProperty(var0);
            if (var4_4 != null && var4_4.length() > 0) {
                if (ObjectFactory.DEBUG == false) return ObjectFactory.newInstance(var4_4, var3_3, true);
                ObjectFactory.debugPrintln("found system property, value=" + var4_4);
                return ObjectFactory.newInstance(var4_4, var3_3, true);
            }
        }
        catch (SecurityException var4_5) {
            // empty catch block
        }
        var4_4 = null;
        if (var1_1 != null) ** GOTO lbl86
        var5_6 = null;
        var6_7 = false;
        try {
            var7_10 = SecuritySupport.getSystemProperty("java.home");
            var1_1 = (String)var7_10 + File.separator + "lib" + File.separator + "xerces.properties";
            var5_6 = new File(var1_1);
            var6_7 = SecuritySupport.getFileExists((File)var5_6);
        }
        catch (SecurityException var7_11) {
            ObjectFactory.fLastModified = -1;
            ObjectFactory.fXercesProperties = null;
        }
        v0 = ObjectFactory.class$org$apache$xerces$parsers$ObjectFactory == null ? (ObjectFactory.class$org$apache$xerces$parsers$ObjectFactory = ObjectFactory.class$("org.apache.xerces.parsers.ObjectFactory")) : ObjectFactory.class$org$apache$xerces$parsers$ObjectFactory;
        var7_10 = v0;
        // MONITORENTER : var7_10
        var8_12 = false;
        var9_13 = null;
        try {
            block36 : {
                try {
                    if (ObjectFactory.fLastModified < 0) ** GOTO lbl44
                    if (!var6_7) ** GOTO lbl-1000
                    v1 = ObjectFactory.fLastModified;
                    ObjectFactory.fLastModified = SecuritySupport.getLastModified((File)var5_6);
                    if (v1 < ObjectFactory.fLastModified) {
                        var8_12 = true;
                    } else if (!var6_7) {
                        ObjectFactory.fLastModified = -1;
                        ObjectFactory.fXercesProperties = null;
                    }
                    ** GOTO lbl47
lbl44: // 1 sources:
                    if (var6_7) {
                        var8_12 = true;
                        ObjectFactory.fLastModified = SecuritySupport.getLastModified((File)var5_6);
                    }
lbl47: // 5 sources:
                    if (!var8_12) break block36;
                    ObjectFactory.fXercesProperties = new Properties();
                    var9_13 = SecuritySupport.getFileInputStream((File)var5_6);
                    ObjectFactory.fXercesProperties.load(var9_13);
                }
                catch (Exception var10_20) {
                    ObjectFactory.fXercesProperties = null;
                    ObjectFactory.fLastModified = -1;
                    var12_15 = null;
                    if (var9_13 != null) {
                        try {}
                        catch (IOException var13_18) {}
                        var9_13.close();
                    }
                }
            }
            var12_14 = null;
            if (var9_13 != null) {
                try {
                    var9_13.close();
                }
                catch (IOException var13_17) {}
            }
        }
        catch (Throwable var11_21) {
            var12_16 = null;
            if (var9_13 == null) throw var11_21;
            ** try [egrp 5[TRYBLOCK] [4 : 336->344)] { 
lbl76: // 1 sources:
            var9_13.close();
            throw var11_21;
lbl78: // 1 sources:
            catch (IOException var13_19) {
                // empty catch block
            }
            throw var11_21;
        }
        // MONITOREXIT : var7_10
        if (ObjectFactory.fXercesProperties != null) {
            var4_4 = ObjectFactory.fXercesProperties.getProperty(var0);
        }
        ** GOTO lbl120
lbl86: // 1 sources:
        var5_6 = null;
        try {
            try {
                var5_6 = SecuritySupport.getFileInputStream(new File(var1_1));
                var6_8 = new Properties();
                var6_8.load((InputStream)var5_6);
                var4_4 = var6_8.getProperty(var0);
            }
            catch (Exception var6_9) {
                var16_23 = null;
                if (var5_6 != null) {
                    try {}
                    catch (IOException var17_26) {}
                    var5_6.close();
                }
            }
            var16_22 = null;
            if (var5_6 != null) {
                try {
                    var5_6.close();
                }
                catch (IOException var17_25) {}
            }
        }
        catch (Throwable var15_28) {
            var16_24 = null;
            if (var5_6 == null) throw var15_28;
            ** try [egrp 8[TRYBLOCK] [8 : 449->457)] { 
lbl115: // 1 sources:
            var5_6.close();
            throw var15_28;
lbl117: // 1 sources:
            catch (IOException var17_27) {
                // empty catch block
            }
            throw var15_28;
        }
        if (var4_4 != null) {
            if (ObjectFactory.DEBUG == false) return ObjectFactory.newInstance(var4_4, var3_3, true);
            ObjectFactory.debugPrintln("found in " + var1_1 + ", value=" + var4_4);
            return ObjectFactory.newInstance(var4_4, var3_3, true);
        }
        var5_6 = ObjectFactory.findJarServiceProvider(var0);
        if (var5_6 != null) {
            return var5_6;
        }
        if (var2_2 == null) {
            throw new ConfigurationError("Provider for " + var0 + " cannot be found", null);
        }
        if (ObjectFactory.DEBUG == false) return ObjectFactory.newInstance(var2_2, var3_3, true);
        ObjectFactory.debugPrintln("using fallback, value=" + var2_2);
        return ObjectFactory.newInstance(var2_2, var3_3, true);
    }

    private static boolean isDebugEnabled() {
        try {
            String string = SecuritySupport.getSystemProperty("xerces.debug");
            return string != null && !"false".equals(string);
        }
        catch (SecurityException securityException) {
            return false;
        }
    }

    private static void debugPrintln(String string) {
        if (DEBUG) {
            System.err.println("XERCES: " + string);
        }
    }

    static ClassLoader findClassLoader() throws ConfigurationError {
        ClassLoader classLoader;
        ClassLoader classLoader2 = SecuritySupport.getContextClassLoader();
        ClassLoader classLoader3 = classLoader = SecuritySupport.getSystemClassLoader();
        do {
            if (classLoader2 == classLoader3) {
                Class class_ = class$org$apache$xerces$parsers$ObjectFactory == null ? (ObjectFactory.class$org$apache$xerces$parsers$ObjectFactory = ObjectFactory.class$("org.apache.xerces.parsers.ObjectFactory")) : class$org$apache$xerces$parsers$ObjectFactory;
                ClassLoader classLoader4 = class_.getClassLoader();
                classLoader3 = classLoader;
                do {
                    if (classLoader4 == classLoader3) {
                        return classLoader;
                    }
                    if (classLoader3 == null) break;
                    classLoader3 = SecuritySupport.getParentClassLoader(classLoader3);
                } while (true);
                return classLoader4;
            }
            if (classLoader3 == null) break;
            classLoader3 = SecuritySupport.getParentClassLoader(classLoader3);
        } while (true);
        return classLoader2;
    }

    static Object newInstance(String string, ClassLoader classLoader, boolean bl) throws ConfigurationError {
        try {
            Class class_ = ObjectFactory.findProviderClass(string, classLoader, bl);
            Object t2 = class_.newInstance();
            if (DEBUG) {
                ObjectFactory.debugPrintln("created new instance of " + class_ + " using ClassLoader: " + classLoader);
            }
            return t2;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new ConfigurationError("Provider " + string + " not found", classNotFoundException);
        }
        catch (Exception exception) {
            throw new ConfigurationError("Provider " + string + " could not be instantiated: " + exception, exception);
        }
    }

    static Class findProviderClass(String string, ClassLoader classLoader, boolean bl) throws ClassNotFoundException, ConfigurationError {
        Class class_;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            int n2 = string.lastIndexOf(".");
            String string2 = string;
            if (n2 != -1) {
                string2 = string.substring(0, n2);
            }
            securityManager.checkPackageAccess(string2);
        }
        if (classLoader == null) {
            class_ = Class.forName(string);
        } else {
            try {
                class_ = classLoader.loadClass(string);
            }
            catch (ClassNotFoundException classNotFoundException) {
                if (bl) {
                    Class class_2 = class$org$apache$xerces$parsers$ObjectFactory == null ? (ObjectFactory.class$org$apache$xerces$parsers$ObjectFactory = ObjectFactory.class$("org.apache.xerces.parsers.ObjectFactory")) : class$org$apache$xerces$parsers$ObjectFactory;
                    ClassLoader classLoader2 = class_2.getClassLoader();
                    if (classLoader2 == null) {
                        class_ = Class.forName(string);
                    }
                    if (classLoader != classLoader2) {
                        classLoader = classLoader2;
                        class_ = classLoader.loadClass(string);
                    }
                    throw classNotFoundException;
                }
                throw classNotFoundException;
            }
        }
        return class_;
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
        var3_3 = ObjectFactory.findClassLoader();
        var2_2 = SecuritySupport.getResourceAsStream(var3_3, var1_1);
        if (var2_2 != null) return null;
        v0 = ObjectFactory.class$org$apache$xerces$parsers$ObjectFactory == null ? (ObjectFactory.class$org$apache$xerces$parsers$ObjectFactory = ObjectFactory.class$("org.apache.xerces.parsers.ObjectFactory")) : ObjectFactory.class$org$apache$xerces$parsers$ObjectFactory;
        var4_4 = v0.getClassLoader();
        if (var3_3 != var4_4) {
            var3_3 = var4_4;
            var2_2 = SecuritySupport.getResourceAsStream(var3_3, var1_1);
        }
        if (var2_2 == null) {
            return null;
        }
        if (ObjectFactory.DEBUG) {
            ObjectFactory.debugPrintln("found jar resource=" + var1_1 + " using ClassLoader: " + var3_3);
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
                ** try [egrp 3[TRYBLOCK] [3 : 202->210)] { 
lbl29: // 1 sources:
                var4_4.close();
                return var7_14;
lbl31: // 1 sources:
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
            if (ObjectFactory.DEBUG == false) return ObjectFactory.newInstance(var5_6, var3_3, false);
            ObjectFactory.debugPrintln("found in resource, value=" + var5_6);
            return ObjectFactory.newInstance(var5_6, var3_3, false);
        }
        catch (Throwable var8_15) {
            var9_9 = null;
            ** try [egrp 3[TRYBLOCK] [3 : 202->210)] { 
lbl49: // 1 sources:
            var4_4.close();
            throw var8_15;
lbl51: // 1 sources:
            catch (IOException var10_12) {
                // empty catch block
            }
            throw var8_15;
        }
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static final class ConfigurationError
    extends Error {
        static final long serialVersionUID = -7285495612271660427L;
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

