/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import java.util.concurrent.ConcurrentMap;

public abstract class FailableCache<K, V> {
    private final LoadingCache<K, Object> delegate;

    public FailableCache() {
        this.delegate = CacheBuilder.newBuilder().build(new CacheLoader<K, Object>(){

            @Override
            public Object load(K k2) {
                Errors errors = new Errors();
                Errors errors2 = null;
                try {
                    errors2 = (Errors)FailableCache.this.create(k2, errors);
                }
                catch (ErrorsException errorsException) {
                    errors.merge(errorsException.getErrors());
                }
                return errors.hasErrors() ? errors : errors2;
            }
        });
    }

    protected abstract V create(K var1, Errors var2) throws ErrorsException;

    public V get(K k2, Errors errors) throws ErrorsException {
        Object object = this.delegate.getUnchecked(k2);
        if (object instanceof Errors) {
            errors.merge((Errors)object);
            throw errors.toException();
        }
        Object object2 = object;
        return (V)object2;
    }

    boolean remove(K k2) {
        return this.delegate.asMap().remove(k2) != null;
    }

}

