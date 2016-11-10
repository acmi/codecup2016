/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.validation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.SecuritySupport;

final class SchemaFactoryFinder {
    private static boolean debug = false;
    private static Properties cacheProps = new Properties();
    private static boolean firstTime = true;
    private final ClassLoader classLoader;
    private static final Class SERVICE_CLASS;
    private static final String SERVICE_ID;
    static Class class$javax$xml$validation$SchemaFactory;

    private static void debugPrintln(String string) {
        if (debug) {
            System.err.println("JAXP: " + string);
        }
    }

    public SchemaFactoryFinder(ClassLoader classLoader) {
        this.classLoader = classLoader;
        if (debug) {
            this.debugDisplayClassLoader();
        }
    }

    private void debugDisplayClassLoader() {
        try {
            if (this.classLoader == SecuritySupport.getContextClassLoader()) {
                SchemaFactoryFinder.debugPrintln("using thread context class loader (" + this.classLoader + ") for search");
                return;
            }
        }
        catch (VirtualMachineError virtualMachineError) {
            throw virtualMachineError;
        }
        catch (ThreadDeath threadDeath) {
            throw threadDeath;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (this.classLoader == ClassLoader.getSystemClassLoader()) {
            SchemaFactoryFinder.debugPrintln("using system class loader (" + this.classLoader + ") for search");
            return;
        }
        SchemaFactoryFinder.debugPrintln("using class loader (" + this.classLoader + ") for search");
    }

    public SchemaFactory newFactory(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        SchemaFactory schemaFactory = this._newFactory(string);
        if (debug) {
            if (schemaFactory != null) {
                SchemaFactoryFinder.debugPrintln("factory '" + schemaFactory.getClass().getName() + "' was found for " + string);
            } else {
                SchemaFactoryFinder.debugPrintln("unable to find a factory for " + string);
            }
        }
        return schemaFactory;
    }

    private SchemaFactory _newFactory(String string) {
        SchemaFactory schemaFactory;
        Object object;
        block31 : {
            String string3;
            String string2;
            block30 : {
                string3 = SERVICE_CLASS.getName() + ":" + string;
                try {
                    if (debug) {
                        SchemaFactoryFinder.debugPrintln("Looking up system property '" + string3 + "'");
                    }
                    if ((string2 = SecuritySupport.getSystemProperty(string3)) != null && string2.length() > 0) {
                        if (debug) {
                            SchemaFactoryFinder.debugPrintln("The value is '" + string2 + "'");
                        }
                        if ((schemaFactory = this.createInstance(string2)) != null) {
                            return schemaFactory;
                        }
                    } else if (debug) {
                        SchemaFactoryFinder.debugPrintln("The property is undefined.");
                    }
                }
                catch (VirtualMachineError virtualMachineError) {
                    throw virtualMachineError;
                }
                catch (ThreadDeath threadDeath) {
                    throw threadDeath;
                }
                catch (Throwable throwable) {
                    if (!debug) break block30;
                    SchemaFactoryFinder.debugPrintln("failed to look up system property '" + string3 + "'");
                    throwable.printStackTrace();
                }
            }
            string2 = SecuritySupport.getSystemProperty("java.home");
            String string4 = string2 + File.separator + "lib" + File.separator + "jaxp.properties";
            String string5 = null;
            try {
                if (firstTime) {
                    object = cacheProps;
                    synchronized (object) {
                        if (firstTime) {
                            File file = new File(string4);
                            firstTime = false;
                            if (SecuritySupport.doesFileExist(file)) {
                                if (debug) {
                                    SchemaFactoryFinder.debugPrintln("Read properties file " + file);
                                }
                                cacheProps.load(SecuritySupport.getFileInputStream(file));
                            }
                        }
                    }
                }
                string5 = cacheProps.getProperty(string3);
                if (debug) {
                    SchemaFactoryFinder.debugPrintln("found " + string5 + " in $java.home/jaxp.properties");
                }
                if (string5 != null && (schemaFactory = this.createInstance(string5)) != null) {
                    return schemaFactory;
                }
            }
            catch (Exception exception) {
                if (!debug) break block31;
                exception.printStackTrace();
            }
        }
        object = this.createServiceFileIterator();
        while (object.hasNext()) {
            URL uRL = (URL)object.next();
            if (debug) {
                SchemaFactoryFinder.debugPrintln("looking into " + uRL);
            }
            try {
                schemaFactory = this.loadFromServicesFile(string, uRL.toExternalForm(), SecuritySupport.getURLInputStream(uRL));
                if (schemaFactory == null) continue;
                return schemaFactory;
            }
            catch (IOException iOException) {
                if (!debug) continue;
                SchemaFactoryFinder.debugPrintln("failed to read " + uRL);
                iOException.printStackTrace();
            }
        }
        if (string.equals("http://www.w3.org/2001/XMLSchema") || string.equals("http://www.w3.org/XML/XMLSchema/v1.0")) {
            if (debug) {
                SchemaFactoryFinder.debugPrintln("attempting to use the platform default XML Schema 1.0 validator");
            }
            return this.createInstance("org.apache.xerces.jaxp.validation.XMLSchemaFactory");
        }
        if (string.equals("http://www.w3.org/XML/XMLSchema/v1.1")) {
            if (debug) {
                SchemaFactoryFinder.debugPrintln("attempting to use the platform default XML Schema 1.1 validator");
            }
            return this.createInstance("org.apache.xerces.jaxp.validation.XMLSchema11Factory");
        }
        if (debug) {
            SchemaFactoryFinder.debugPrintln("all things were tried, but none was found. bailing out.");
        }
        return null;
    }

    SchemaFactory createInstance(String string) {
        block8 : {
            try {
                Object obj;
                if (debug) {
                    SchemaFactoryFinder.debugPrintln("instanciating " + string);
                }
                Class class_ = this.classLoader != null ? this.classLoader.loadClass(string) : Class.forName(string);
                if (debug) {
                    SchemaFactoryFinder.debugPrintln("loaded it from " + SchemaFactoryFinder.which(class_));
                }
                if ((obj = class_.newInstance()) instanceof SchemaFactory) {
                    return (SchemaFactory)obj;
                }
                if (debug) {
                    SchemaFactoryFinder.debugPrintln(string + " is not assignable to " + SERVICE_CLASS.getName());
                }
            }
            catch (VirtualMachineError virtualMachineError) {
                throw virtualMachineError;
            }
            catch (ThreadDeath threadDeath) {
                throw threadDeath;
            }
            catch (Throwable throwable) {
                SchemaFactoryFinder.debugPrintln("failed to instanciate " + string);
                if (!debug) break block8;
                throwable.printStackTrace();
            }
        }
        return null;
    }

    private Iterator createServiceFileIterator() {
        if (this.classLoader == null) {
            return new SingleIterator(this){
                static Class class$javax$xml$validation$SchemaFactoryFinder;
                private final SchemaFactoryFinder this$0;

                protected Object value() {
                    Class class_ = class$javax$xml$validation$SchemaFactoryFinder == null ? (.class$javax$xml$validation$SchemaFactoryFinder = .class$("javax.xml.validation.SchemaFactoryFinder")) : class$javax$xml$validation$SchemaFactoryFinder;
                    ClassLoader classLoader = class_.getClassLoader();
                    return SecuritySupport.getResourceAsURL(classLoader, SchemaFactoryFinder.access$100());
                }

                static Class class$(String string) {
                    try {
                        return Class.forName(string);
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        throw new NoClassDefFoundError(classNotFoundException.getMessage());
                    }
                }
            };
        }
        try {
            Enumeration enumeration = SecuritySupport.getResources(this.classLoader, SERVICE_ID);
            if (debug && !enumeration.hasMoreElements()) {
                SchemaFactoryFinder.debugPrintln("no " + SERVICE_ID + " file was found");
            }
            return new Iterator(this, enumeration){
                private final Enumeration val$e;
                private final SchemaFactoryFinder this$0;

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                public boolean hasNext() {
                    return this.val$e.hasMoreElements();
                }

                public Object next() {
                    return this.val$e.nextElement();
                }
            };
        }
        catch (IOException iOException) {
            if (debug) {
                SchemaFactoryFinder.debugPrintln("failed to enumerate resources " + SERVICE_ID);
                iOException.printStackTrace();
            }
            return new ArrayList().iterator();
        }
    }

    private SchemaFactory loadFromServicesFile(String string, String string2, InputStream inputStream) {
        BufferedReader bufferedReader;
        if (debug) {
            SchemaFactoryFinder.debugPrintln("Reading " + string2);
        }
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 80);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 80);
        }
        String string3 = null;
        SchemaFactory schemaFactory = null;
        do {
            try {
                string3 = bufferedReader.readLine();
            }
            catch (IOException iOException) {
                break;
            }
            if (string3 == null) break;
            int n2 = string3.indexOf(35);
            if (n2 != -1) {
                string3 = string3.substring(0, n2);
            }
            if ((string3 = string3.trim()).length() == 0) continue;
            try {
                SchemaFactory schemaFactory2 = this.createInstance(string3);
                if (!schemaFactory2.isSchemaLanguageSupported(string)) continue;
                schemaFactory = schemaFactory2;
                break;
            }
            catch (Exception exception) {
                continue;
            }
            break;
        } while (true);
        try {
            bufferedReader.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return schemaFactory;
    }

    private static String which(Class class_) {
        return SchemaFactoryFinder.which(class_.getName(), class_.getClassLoader());
    }

    private static String which(String string, ClassLoader classLoader) {
        URL uRL;
        String string2 = string.replace('.', '/') + ".class";
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        if ((uRL = SecuritySupport.getResourceAsURL(classLoader, string2)) != null) {
            return uRL.toString();
        }
        return null;
    }

    static String access$100() {
        return SERVICE_ID;
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
        Class class_ = class$javax$xml$validation$SchemaFactory == null ? (SchemaFactoryFinder.class$javax$xml$validation$SchemaFactory = SchemaFactoryFinder.class$("javax.xml.validation.SchemaFactory")) : class$javax$xml$validation$SchemaFactory;
        SERVICE_CLASS = class_;
        SERVICE_ID = "META-INF/services/" + SERVICE_CLASS.getName();
    }

    private static abstract class SingleIterator
    implements Iterator {
        private boolean seen = false;

        private SingleIterator() {
        }

        public final void remove() {
            throw new UnsupportedOperationException();
        }

        public final boolean hasNext() {
            return !this.seen;
        }

        public final Object next() {
            if (this.seen) {
                throw new NoSuchElementException();
            }
            this.seen = true;
            return this.value();
        }

        protected abstract Object value();

        SingleIterator( var1_1) {
            this();
        }
    }

}

