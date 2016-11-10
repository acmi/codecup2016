/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import de.schlichtherle.truezip.util.JointIterator;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public final class ServiceLocator {
    private final ClassLoader l1;

    public ServiceLocator() {
        this(null);
    }

    public ServiceLocator(ClassLoader classLoader) {
        this.l1 = null != classLoader ? classLoader : ClassLoader.getSystemClassLoader();
    }

    public <S> Iterator<S> getServices(Class<S> class_) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return this.l1 == classLoader ? ServiceLoader.load(class_, this.l1).iterator() : new JointIterator<S>(ServiceLoader.load(class_, this.l1).iterator(), ServiceLoader.load(class_, classLoader).iterator());
    }

    public <S> S getService(Class<S> class_, Class<? extends S> class_2) {
        String string = System.getProperty(class_.getName(), null == class_2 ? null : class_2.getName());
        if (null == string) {
            return null;
        }
        try {
            return class_2.cast(this.getClass(string).newInstance());
        }
        catch (ClassCastException classCastException) {
            throw new ServiceConfigurationError(classCastException.toString(), classCastException);
        }
        catch (InstantiationException instantiationException) {
            throw new ServiceConfigurationError(instantiationException.toString(), instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new ServiceConfigurationError(illegalAccessException.toString(), illegalAccessException);
        }
    }

    public Class<?> getClass(String string) {
        try {
            return this.l1.loadClass(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (this.l1 == classLoader) {
                    throw classNotFoundException;
                }
                return classLoader.loadClass(string);
            }
            catch (ClassNotFoundException classNotFoundException2) {
                throw new ServiceConfigurationError(classNotFoundException2.toString(), classNotFoundException2);
            }
        }
    }

    static {
        Logger.getLogger(ServiceLocator.class.getName(), ServiceLocator.class.getName()).config("banner");
    }
}

