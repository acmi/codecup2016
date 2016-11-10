/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.Properties;
import org.apache.xpath.functions.SecuritySupport;

final class ObjectFactory {
    private static Properties fXalanProperties = null;
    private static long fLastModified = -1;
    static Class class$org$apache$xpath$functions$ObjectFactory;

    ObjectFactory() {
    }

    static ClassLoader findClassLoader() throws ConfigurationError {
        ClassLoader classLoader;
        ClassLoader classLoader2 = SecuritySupport.getContextClassLoader();
        ClassLoader classLoader3 = classLoader = SecuritySupport.getSystemClassLoader();
        do {
            if (classLoader2 == classLoader3) {
                Class class_ = class$org$apache$xpath$functions$ObjectFactory == null ? (ObjectFactory.class$org$apache$xpath$functions$ObjectFactory = ObjectFactory.class$("org.apache.xpath.functions.ObjectFactory")) : class$org$apache$xpath$functions$ObjectFactory;
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
    }

}

