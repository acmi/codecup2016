/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

final class SecuritySupport {
    static ClassLoader getContextClassLoader() {
        return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                ClassLoader classLoader = null;
                try {
                    classLoader = Thread.currentThread().getContextClassLoader();
                }
                catch (SecurityException securityException) {
                    // empty catch block
                }
                return classLoader;
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                ClassLoader classLoader = null;
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                }
                catch (SecurityException securityException) {
                    // empty catch block
                }
                return classLoader;
            }
        });
    }

    static ClassLoader getParentClassLoader(ClassLoader classLoader) {
        return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction(classLoader){
            private final ClassLoader val$cl;

            public Object run() {
                ClassLoader classLoader = null;
                try {
                    classLoader = this.val$cl.getParent();
                }
                catch (SecurityException securityException) {
                    // empty catch block
                }
                return classLoader == this.val$cl ? null : classLoader;
            }
        });
    }

    static String getSystemProperty(String string) {
        return (String)AccessController.doPrivileged(new PrivilegedAction(string){
            private final String val$propName;

            public Object run() {
                return System.getProperty(this.val$propName);
            }
        });
    }

    static InputStream getResourceAsStream(ClassLoader classLoader, String string) {
        return (InputStream)AccessController.doPrivileged(new PrivilegedAction(classLoader, string){
            private final ClassLoader val$cl;
            private final String val$name;

            public Object run() {
                InputStream inputStream = this.val$cl == null ? ClassLoader.getSystemResourceAsStream(this.val$name) : this.val$cl.getResourceAsStream(this.val$name);
                return inputStream;
            }
        });
    }

}

