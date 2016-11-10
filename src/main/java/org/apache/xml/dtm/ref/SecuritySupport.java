/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

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

}

