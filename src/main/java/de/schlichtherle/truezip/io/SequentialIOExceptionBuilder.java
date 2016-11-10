/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.io;

import de.schlichtherle.truezip.io.SequentialIOException;
import de.schlichtherle.truezip.util.AbstractExceptionBuilder;
import java.lang.reflect.Constructor;

public class SequentialIOExceptionBuilder<C extends Exception, X extends SequentialIOException>
extends AbstractExceptionBuilder<C, X> {
    private final Class<X> assemblyClass;
    private volatile Constructor<X> assemblyConstructor;

    public SequentialIOExceptionBuilder(Class<C> class_, Class<X> class_2) {
        this.assemblyClass = class_2;
        try {
            if (!class_2.isAssignableFrom(class_)) {
                this.wrap(null);
            }
        }
        catch (IllegalStateException illegalStateException) {
            throw new IllegalArgumentException(illegalStateException.getCause());
        }
    }

    private Class<X> assemblyClass() {
        return this.assemblyClass;
    }

    private Constructor<X> assemblyConstructor() {
        Constructor<X> constructor = this.assemblyConstructor;
        Constructor<X> constructor2 = null != constructor ? constructor : (this.assemblyConstructor = this.newAssemblyConstructor());
        return constructor2;
    }

    private Constructor<X> newAssemblyConstructor() {
        try {
            return this.assemblyClass().getConstructor(String.class);
        }
        catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Override
    protected final X update(C c2, X x2) {
        SequentialIOException sequentialIOException = null;
        if (this.assemblyClass().isInstance(c2) && (sequentialIOException = (SequentialIOException)c2).isInitPredecessor()) {
            if (null == x2) {
                return (X)sequentialIOException;
            }
            sequentialIOException = null;
        }
        if (null == sequentialIOException) {
            sequentialIOException = this.wrap(c2);
        }
        sequentialIOException.initPredecessor((SequentialIOException)x2);
        return (X)sequentialIOException;
    }

    private X wrap(C c2) {
        X x2 = this.newAssembly(SequentialIOExceptionBuilder.toString(c2));
        x2.initCause((Throwable)c2);
        return x2;
    }

    private static String toString(Object object) {
        return null == object ? "" : object.toString();
    }

    private X newAssembly(String string) {
        Constructor<X> constructor = this.assemblyConstructor();
        try {
            try {
                return (X)((SequentialIOException)constructor.newInstance(string));
            }
            catch (IllegalAccessException illegalAccessException) {
                constructor.setAccessible(true);
                return (X)((SequentialIOException)constructor.newInstance(string));
            }
        }
        catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Override
    protected final X post(X x2) {
        return (X)x2.sortPriority();
    }
}

