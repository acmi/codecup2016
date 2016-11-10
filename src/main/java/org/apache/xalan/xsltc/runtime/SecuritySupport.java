/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

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

    static FileInputStream getFileInputStream(File file) throws FileNotFoundException {
        try {
            return (FileInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction(file){
                private final File val$file;

                public Object run() throws FileNotFoundException {
                    return new FileInputStream(this.val$file);
                }
            });
        }
        catch (PrivilegedActionException privilegedActionException) {
            throw (FileNotFoundException)privilegedActionException.getException();
        }
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

    static boolean getFileExists(File file) {
        return (Boolean)AccessController.doPrivileged(new PrivilegedAction(file){
            private final File val$f;

            public Object run() {
                return this.val$f.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        });
    }

    static long getLastModified(File file) {
        return (Long)AccessController.doPrivileged(new PrivilegedAction(file){
            private final File val$f;

            public Object run() {
                return new Long(this.val$f.lastModified());
            }
        });
    }

    private SecuritySupport() {
    }

}

