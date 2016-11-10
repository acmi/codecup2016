/*
 * Decompiled with CFR 0_119.
 */
package com.google.common.cache;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public abstract class CacheLoader<K, V> {
    protected CacheLoader() {
    }

    public abstract V load(K var1) throws Exception;

    public ListenableFuture<V> reload(K k2, V v2) throws Exception {
        Preconditions.checkNotNull(k2);
        Preconditions.checkNotNull(v2);
        return Futures.immediateFuture(this.load(k2));
    }

    public Map<K, V> loadAll(Iterable<? extends K> iterable) throws Exception {
        throw new UnsupportedLoadingOperationException();
    }

    public static <K, V> CacheLoader<K, V> from(Function<K, V> function) {
        return new FunctionToCacheLoader<K, V>(function);
    }

    public static <V> CacheLoader<Object, V> from(Supplier<V> supplier) {
        return new SupplierToCacheLoader<V>(supplier);
    }

    public static <K, V> CacheLoader<K, V> asyncReloading(final CacheLoader<K, V> cacheLoader, final Executor executor) {
        Preconditions.checkNotNull(cacheLoader);
        Preconditions.checkNotNull(executor);
        return new CacheLoader<K, V>(){

            @Override
            public V load(K k2) throws Exception {
                return cacheLoader.load(k2);
            }

            @Override
            public ListenableFuture<V> reload(final K k2, final V v2) throws Exception {
                ListenableFutureTask listenableFutureTask = ListenableFutureTask.create(new Callable<V>(){

                    @Override
                    public V call() throws Exception {
                        return (V)cacheLoader.reload(k2, v2).get();
                    }
                });
                executor.execute(listenableFutureTask);
                return listenableFutureTask;
            }

            @Override
            public Map<K, V> loadAll(Iterable<? extends K> iterable) throws Exception {
                return cacheLoader.loadAll(iterable);
            }

        };
    }

    public static final class InvalidCacheLoadException
    extends RuntimeException {
        public InvalidCacheLoadException(String string) {
            super(string);
        }
    }

    public static final class UnsupportedLoadingOperationException
    extends UnsupportedOperationException {
        UnsupportedLoadingOperationException() {
        }
    }

    private static final class SupplierToCacheLoader<V>
    extends CacheLoader<Object, V>
    implements Serializable {
        private final Supplier<V> computingSupplier;

        public SupplierToCacheLoader(Supplier<V> supplier) {
            this.computingSupplier = Preconditions.checkNotNull(supplier);
        }

        @Override
        public V load(Object object) {
            Preconditions.checkNotNull(object);
            return this.computingSupplier.get();
        }
    }

    private static final class FunctionToCacheLoader<K, V>
    extends CacheLoader<K, V>
    implements Serializable {
        private final Function<K, V> computingFunction;

        public FunctionToCacheLoader(Function<K, V> function) {
            this.computingFunction = Preconditions.checkNotNull(function);
        }

        @Override
        public V load(K k2) {
            return this.computingFunction.apply(Preconditions.checkNotNull(k2));
        }
    }

}

