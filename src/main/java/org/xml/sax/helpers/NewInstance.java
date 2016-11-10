/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax.helpers;

import org.xml.sax.helpers.SecuritySupport;

class NewInstance {
    static Class class$org$xml$sax$helpers$NewInstance;

    NewInstance() {
    }

    static Object newInstance(ClassLoader classLoader, String string) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class class_;
        if (classLoader == null) {
            class_ = Class.forName(string);
        } else {
            try {
                class_ = classLoader.loadClass(string);
            }
            catch (ClassNotFoundException classNotFoundException) {
                Class class_2 = class$org$xml$sax$helpers$NewInstance == null ? (NewInstance.class$org$xml$sax$helpers$NewInstance = NewInstance.class$("org.xml.sax.helpers.NewInstance")) : class$org$xml$sax$helpers$NewInstance;
                classLoader = class_2.getClassLoader();
                class_ = classLoader != null ? classLoader.loadClass(string) : Class.forName(string);
            }
        }
        Object obj = class_.newInstance();
        return obj;
    }

    static ClassLoader getClassLoader() {
        ClassLoader classLoader = SecuritySupport.getContextClassLoader();
        if (classLoader == null) {
            Class class_ = class$org$xml$sax$helpers$NewInstance == null ? (NewInstance.class$org$xml$sax$helpers$NewInstance = NewInstance.class$("org.xml.sax.helpers.NewInstance")) : class$org$xml$sax$helpers$NewInstance;
            classLoader = class_.getClassLoader();
        }
        return classLoader;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

