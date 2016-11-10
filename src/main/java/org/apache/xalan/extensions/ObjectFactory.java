/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.apache.xalan.extensions.SecuritySupport;

final class ObjectFactory {
    private static final String DEFAULT_PROPERTIES_FILENAME = "xalan.properties";
    private static final String SERVICES_PATH = "META-INF/services/";
    private static final boolean DEBUG = false;
    private static Properties fXalanProperties = null;
    private static long fLastModified = -1;
    static Class class$org$apache$xalan$extensions$ObjectFactory;

    ObjectFactory() {
    }

    static Object createObject(String string, String string2) throws ConfigurationError {
        return ObjectFactory.createObject(string, null, string2);
    }

    static Object createObject(String string, String string2, String string3) throws ConfigurationError {
        Class class_ = ObjectFactory.lookUpFactoryClass(string, string2, string3);
        if (class_ == null) {
            throw new ConfigurationError("Provider for " + string + " cannot be found", null);
        }
        try {
            Object t2 = class_.newInstance();
            ObjectFactory.debugPrintln("created new instance of factory " + string);
            return t2;
        }
        catch (Exception exception) {
            throw new ConfigurationError("Provider for factory " + string + " could not be instantiated: " + exception, exception);
        }
    }

    static Class lookUpFactoryClass(String string) throws ConfigurationError {
        return ObjectFactory.lookUpFactoryClass(string, null, null);
    }

    static Class lookUpFactoryClass(String string, String string2, String string3) throws ConfigurationError {
        String string4 = ObjectFactory.lookUpFactoryClassName(string, string2, string3);
        ClassLoader classLoader = ObjectFactory.findClassLoader();
        if (string4 == null) {
            string4 = string3;
        }
        try {
            Class class_ = ObjectFactory.findProviderClass(string4, classLoader, true);
            ObjectFactory.debugPrintln("created new instance of " + class_ + " using ClassLoader: " + classLoader);
            return class_;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new ConfigurationError("Provider " + string4 + " not found", classNotFoundException);
        }
        catch (Exception exception) {
            throw new ConfigurationError("Provider " + string4 + " could not be instantiated: " + exception, exception);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    static String lookUpFactoryClassName(String var0, String var1_1, String var2_2) {
        try {
            var3_3 = SecuritySupport.getSystemProperty(var0);
            if (var3_3 != null) {
                ObjectFactory.debugPrintln("found system property, value=" + var3_3);
                return var3_3;
            }
        }
        catch (SecurityException var3_4) {
            // empty catch block
        }
        var3_3 = null;
        if (var1_1 != null) ** GOTO lbl82
        var4_5 = null;
        var5_7 = false;
        try {
            var6_10 = SecuritySupport.getSystemProperty("java.home");
            var1_1 = (String)var6_10 + File.separator + "lib" + File.separator + "xalan.properties";
            var4_5 = new File(var1_1);
            var5_7 = SecuritySupport.getFileExists(var4_5);
        }
        catch (SecurityException var6_11) {
            ObjectFactory.fLastModified = -1;
            ObjectFactory.fXalanProperties = null;
        }
        v0 = ObjectFactory.class$org$apache$xalan$extensions$ObjectFactory == null ? (ObjectFactory.class$org$apache$xalan$extensions$ObjectFactory = ObjectFactory.class$("org.apache.xalan.extensions.ObjectFactory")) : ObjectFactory.class$org$apache$xalan$extensions$ObjectFactory;
        var6_10 = v0;
        // MONITORENTER : v0
        var7_12 = false;
        var8_13 = null;
        try {
            block32 : {
                try {
                    if (ObjectFactory.fLastModified < 0) ** GOTO lbl40
                    if (!var5_7) ** GOTO lbl-1000
                    v1 = ObjectFactory.fLastModified;
                    ObjectFactory.fLastModified = SecuritySupport.getLastModified(var4_5);
                    if (v1 < ObjectFactory.fLastModified) {
                        var7_12 = true;
                    } else if (!var5_7) {
                        ObjectFactory.fLastModified = -1;
                        ObjectFactory.fXalanProperties = null;
                    }
                    ** GOTO lbl43
lbl40: // 1 sources:
                    if (var5_7) {
                        var7_12 = true;
                        ObjectFactory.fLastModified = SecuritySupport.getLastModified(var4_5);
                    }
lbl43: // 5 sources:
                    if (!var7_12) break block32;
                    ObjectFactory.fXalanProperties = new Properties();
                    var8_13 = SecuritySupport.getFileInputStream(var4_5);
                    ObjectFactory.fXalanProperties.load(var8_13);
                }
                catch (Exception var9_20) {
                    ObjectFactory.fXalanProperties = null;
                    ObjectFactory.fLastModified = -1;
                    var11_15 = null;
                    if (var8_13 != null) {
                        try {}
                        catch (IOException var12_18) {}
                        var8_13.close();
                    }
                }
            }
            var11_14 = null;
            if (var8_13 != null) {
                try {
                    var8_13.close();
                }
                catch (IOException var12_17) {}
            }
        }
        catch (Throwable var10_21) {
            var11_16 = null;
            if (var8_13 == null) throw var10_21;
            ** try [egrp 5[TRYBLOCK] [6 : 295->303)] { 
lbl72: // 1 sources:
            var8_13.close();
            throw var10_21;
lbl74: // 1 sources:
            catch (IOException var12_19) {
                // empty catch block
            }
            throw var10_21;
        }
        // MONITOREXIT : var6_10
        if (ObjectFactory.fXalanProperties != null) {
            var3_3 = ObjectFactory.fXalanProperties.getProperty(var0);
        }
        ** GOTO lbl116
lbl82: // 1 sources:
        var4_6 = null;
        try {
            try {
                var4_6 = SecuritySupport.getFileInputStream(new File(var1_1));
                var5_8 = new Properties();
                var5_8.load(var4_6);
                var3_3 = var5_8.getProperty(var0);
            }
            catch (Exception var5_9) {
                var15_23 = null;
                if (var4_6 != null) {
                    try {}
                    catch (IOException var16_26) {}
                    var4_6.close();
                }
            }
            var15_22 = null;
            if (var4_6 != null) {
                try {
                    var4_6.close();
                }
                catch (IOException var16_25) {}
            }
        }
        catch (Throwable var14_28) {
            var15_24 = null;
            if (var4_6 == null) throw var14_28;
            ** try [egrp 8[TRYBLOCK] [13 : 406->414)] { 
lbl111: // 1 sources:
            var4_6.close();
            throw var14_28;
lbl113: // 1 sources:
            catch (IOException var16_27) {
                // empty catch block
            }
            throw var14_28;
        }
        if (var3_3 == null) return ObjectFactory.findJarServiceProviderName(var0);
        ObjectFactory.debugPrintln("found in " + var1_1 + ", value=" + var3_3);
        return var3_3;
    }

    private static void debugPrintln(String string) {
    }

    static ClassLoader findClassLoader() throws ConfigurationError {
        ClassLoader classLoader;
        ClassLoader classLoader2 = SecuritySupport.getContextClassLoader();
        ClassLoader classLoader3 = classLoader = SecuritySupport.getSystemClassLoader();
        do {
            if (classLoader2 == classLoader3) {
                Class class_ = class$org$apache$xalan$extensions$ObjectFactory == null ? (ObjectFactory.class$org$apache$xalan$extensions$ObjectFactory = ObjectFactory.class$("org.apache.xalan.extensions.ObjectFactory")) : class$org$apache$xalan$extensions$ObjectFactory;
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
            ObjectFactory.debugPrintln("created new instance of " + class_ + " using ClassLoader: " + classLoader);
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
            int n2 = string.lastIndexOf(46);
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
                    Class class_2 = class$org$apache$xalan$extensions$ObjectFactory == null ? (ObjectFactory.class$org$apache$xalan$extensions$ObjectFactory = ObjectFactory.class$("org.apache.xalan.extensions.ObjectFactory")) : class$org$apache$xalan$extensions$ObjectFactory;
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
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static String findJarServiceProviderName(String var0) {
        var1_1 = "META-INF/services/" + var0;
        var2_2 = null;
        var3_3 = ObjectFactory.findClassLoader();
        var2_2 = SecuritySupport.getResourceAsStream(var3_3, var1_1);
        if (var2_2 != null) return null;
        v0 = ObjectFactory.class$org$apache$xalan$extensions$ObjectFactory == null ? (ObjectFactory.class$org$apache$xalan$extensions$ObjectFactory = ObjectFactory.class$("org.apache.xalan.extensions.ObjectFactory")) : ObjectFactory.class$org$apache$xalan$extensions$ObjectFactory;
        var4_4 = v0.getClassLoader();
        if (var3_3 != var4_4) {
            var3_3 = var4_4;
            var2_2 = SecuritySupport.getResourceAsStream(var3_3, var1_1);
        }
        if (var2_2 == null) {
            return null;
        }
        ObjectFactory.debugPrintln("found jar resource=" + var1_1 + " using ClassLoader: " + var3_3);
        try {
            var4_4 = new BufferedReader(new InputStreamReader(var2_2, "UTF-8"));
        }
        catch (UnsupportedEncodingException var5_5) {
            var4_4 = new BufferedReader(new InputStreamReader(var2_2));
        }
        var5_6 = null;
        try {
            try {
                var5_6 = var4_4.readLine();
            }
            catch (IOException var6_13) {
                var7_14 = null;
                var9_8 = null;
                ** try [egrp 3[TRYBLOCK] [5 : 192->200)] { 
lbl28: // 1 sources:
                var4_4.close();
                return var7_14;
lbl30: // 1 sources:
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
            ObjectFactory.debugPrintln("found in resource, value=" + var5_6);
            return var5_6;
        }
        catch (Throwable var8_15) {
            var9_9 = null;
            ** try [egrp 3[TRYBLOCK] [5 : 192->200)] { 
lbl47: // 1 sources:
            var4_4.close();
            throw var8_15;
lbl49: // 1 sources:
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

    static class ConfigurationError
    extends Error {
        static final long serialVersionUID = 8564305128443551853L;
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

