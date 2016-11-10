/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.apache.xalan.xsltc.compiler.SecuritySupport;

final class ObjectFactory {
    private static final String DEFAULT_PROPERTIES_FILENAME = "xalan.properties";
    private static final String SERVICES_PATH = "META-INF/services/";
    private static final boolean DEBUG = false;
    private static Properties fXalanProperties = null;
    private static long fLastModified = -1;
    static Class class$org$apache$xalan$xsltc$compiler$ObjectFactory;

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
        if (var1_1 != null) ** GOTO lbl74
        var4_5 = null;
        var5_7 = false;
        try {
            var6_12 = SecuritySupport.getSystemProperty("java.home");
            var1_1 = (String)var6_12 + File.separator + "lib" + File.separator + "xalan.properties";
            var4_5 = new File(var1_1);
            var5_7 = SecuritySupport.getFileExists(var4_5);
        }
        catch (SecurityException var6_13) {
            ObjectFactory.fLastModified = -1;
            ObjectFactory.fXalanProperties = null;
        }
        v0 = ObjectFactory.class$org$apache$xalan$xsltc$compiler$ObjectFactory == null ? (ObjectFactory.class$org$apache$xalan$xsltc$compiler$ObjectFactory = ObjectFactory.class$("org.apache.xalan.xsltc.compiler.ObjectFactory")) : ObjectFactory.class$org$apache$xalan$xsltc$compiler$ObjectFactory;
        var6_12 = v0;
        // MONITORENTER : v0
        var7_14 = false;
        var8_15 = null;
        try {
            if (ObjectFactory.fLastModified < 0) ** GOTO lbl39
            if (!var5_7) ** GOTO lbl-1000
            v1 = ObjectFactory.fLastModified;
            ObjectFactory.fLastModified = SecuritySupport.getLastModified(var4_5);
            if (v1 < ObjectFactory.fLastModified) {
                var7_14 = true;
            } else if (!var5_7) {
                ObjectFactory.fLastModified = -1;
                ObjectFactory.fXalanProperties = null;
            }
            ** GOTO lbl42
lbl39: // 1 sources:
            if (var5_7) {
                var7_14 = true;
                ObjectFactory.fLastModified = SecuritySupport.getLastModified(var4_5);
            }
lbl42: // 5 sources:
            if (var7_14) {
                ObjectFactory.fXalanProperties = new Properties();
                var8_15 = SecuritySupport.getFileInputStream(var4_5);
                ObjectFactory.fXalanProperties.load(var8_15);
            }
            ** if (var8_15 == null) goto lbl-1000
        }
        catch (Exception var9_17) {
            try {
                ObjectFactory.fXalanProperties = null;
                ObjectFactory.fLastModified = -1;
                ** if (var8_15 == null) goto lbl-1000
            }
            catch (Throwable var10_19) {
                if (var8_15 == null) throw var10_19;
                try {
                    var8_15.close();
                    throw var10_19;
                }
                catch (IOException var11_20) {
                    // empty catch block
                }
                throw var10_19;
            }
lbl-1000: // 1 sources:
            {
                try {
                    var8_15.close();
                }
                catch (IOException var9_18) {}
            }
lbl-1000: // 2 sources:
            {
            }
        }
lbl-1000: // 1 sources:
        {
            try {
                var8_15.close();
            }
            catch (IOException var9_16) {}
        }
lbl-1000: // 2 sources:
        {
        }
        if (ObjectFactory.fXalanProperties != null) {
            var3_3 = ObjectFactory.fXalanProperties.getProperty(var0);
        }
        ** GOTO lbl101
lbl74: // 1 sources:
        var4_6 = null;
        var4_6 = SecuritySupport.getFileInputStream(new File(var1_1));
        var5_8 = new Properties();
        var5_8.load(var4_6);
        var3_3 = var5_8.getProperty(var0);
        ** if (var4_6 == null) goto lbl-1000
lbl-1000: // 1 sources:
        {
            try {
                var4_6.close();
            }
            catch (IOException var5_9) {}
        }
lbl-1000: // 2 sources:
        {
        }
        catch (Exception var5_10) {
            if (var4_6 != null) {
                try {
                    var4_6.close();
                }
                catch (IOException var5_11) {}
            }
            catch (Throwable var13_21) {
                if (var4_6 == null) throw var13_21;
                try {
                    var4_6.close();
                    throw var13_21;
                }
                catch (IOException var14_22) {
                    // empty catch block
                }
                throw var13_21;
            }
        }
lbl101: // 5 sources:
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
                Class class_ = class$org$apache$xalan$xsltc$compiler$ObjectFactory == null ? (ObjectFactory.class$org$apache$xalan$xsltc$compiler$ObjectFactory = ObjectFactory.class$("org.apache.xalan.xsltc.compiler.ObjectFactory")) : class$org$apache$xalan$xsltc$compiler$ObjectFactory;
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
                    Class class_2 = class$org$apache$xalan$xsltc$compiler$ObjectFactory == null ? (ObjectFactory.class$org$apache$xalan$xsltc$compiler$ObjectFactory = ObjectFactory.class$("org.apache.xalan.xsltc.compiler.ObjectFactory")) : class$org$apache$xalan$xsltc$compiler$ObjectFactory;
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
     */
    private static String findJarServiceProviderName(String string) {
        Object object;
        String string2 = "META-INF/services/" + string;
        InputStream inputStream = null;
        ClassLoader classLoader = ObjectFactory.findClassLoader();
        inputStream = SecuritySupport.getResourceAsStream(classLoader, string2);
        if (inputStream == null) {
            Class class_ = class$org$apache$xalan$xsltc$compiler$ObjectFactory == null ? (ObjectFactory.class$org$apache$xalan$xsltc$compiler$ObjectFactory = ObjectFactory.class$("org.apache.xalan.xsltc.compiler.ObjectFactory")) : class$org$apache$xalan$xsltc$compiler$ObjectFactory;
            object = class_.getClassLoader();
            if (classLoader != object) {
                classLoader = object;
                inputStream = SecuritySupport.getResourceAsStream(classLoader, string2);
            }
        }
        if (inputStream == null) {
            return null;
        }
        ObjectFactory.debugPrintln("found jar resource=" + string2 + " using ClassLoader: " + classLoader);
        try {
            object = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            object = new BufferedReader(new InputStreamReader(inputStream));
        }
        String string3 = null;
        try {
            string3 = object.readLine();
        }
        catch (IOException iOException) {
            String string4 = null;
            return string4;
        }
        finally {
            try {
                object.close();
            }
            catch (IOException iOException) {}
        }
        if (string3 != null && !"".equals(string3)) {
            ObjectFactory.debugPrintln("found in resource, value=" + string3);
            return string3;
        }
        return null;
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
        static final long serialVersionUID = 3326843611085065902L;
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

