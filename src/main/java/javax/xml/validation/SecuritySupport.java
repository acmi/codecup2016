/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;

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

    static InputStream getURLInputStream(URL uRL) throws IOException {
        try {
            return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction(uRL){
                private final URL val$url;

                public Object run() throws IOException {
                    return this.val$url.openStream();
                }
            });
        }
        catch (PrivilegedActionException privilegedActionException) {
            throw (IOException)privilegedActionException.getException();
        }
    }

    static URL getResourceAsURL(ClassLoader classLoader, String string) {
        return (URL)AccessController.doPrivileged(new PrivilegedAction(classLoader, string){
            private final ClassLoader val$cl;
            private final String val$name;

            public Object run() {
                URL uRL = this.val$cl == null ? ClassLoader.getSystemResource(this.val$name) : this.val$cl.getResource(this.val$name);
                return uRL;
            }
        });
    }

    static Enumeration getResources(ClassLoader classLoader, String string) throws IOException {
        try {
            return (Enumeration)AccessController.doPrivileged(new PrivilegedExceptionAction(classLoader, string){
                private final ClassLoader val$cl;
                private final String val$name;

                public Object run() throws IOException {
                    Enumeration<URL> enumeration = this.val$cl == null ? ClassLoader.getSystemResources(this.val$name) : this.val$cl.getResources(this.val$name);
                    return enumeration;
                }
            });
        }
        catch (PrivilegedActionException privilegedActionException) {
            throw (IOException)privilegedActionException.getException();
        }
    }

    static boolean doesFileExist(File file) {
        return (Boolean)AccessController.doPrivileged(new PrivilegedAction(file){
            private final File val$f;

            public Object run() {
                return this.val$f.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        });
    }

}

