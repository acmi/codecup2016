/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.ConstructionContext;
import com.google.inject.internal.CycleDetectingLock;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.InternalContext;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.DependencyAndSource;
import com.google.inject.spi.Message;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class SingletonScope
implements Scope {
    private static final Object NULL = new Object();
    static final ThreadLocal<WeakReference<InjectorImpl>> currentInjector = new ThreadLocal();
    private static final ConcurrentMap<Thread, InternalContext> internalContextsMap = Maps.newConcurrentMap();
    private static final CycleDetectingLock.CycleDetectingLockFactory<Key<?>> cycleDetectingLockFactory = new CycleDetectingLock.CycleDetectingLockFactory();

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
        return new Provider<T>(){
            volatile Object instance;
            final ConstructionContext<T> constructionContext;
            final CycleDetectingLock<Key<?>> creationLock;
            final InjectorImpl injector;

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public T get() {
                var1_1 = this.instance;
                if (var1_1 == null) {
                    block31 : {
                        var2_2 = Thread.currentThread();
                        var3_4 = this.injector.getLocalContext();
                        var4_5 = (InternalContext)SingletonScope.access$100().get(var2_2);
                        SingletonScope.access$100().put(var2_2, var3_4);
                        try {
                            var5_6 = this.creationLock.lockOrDetectPotentialLocksCycle();
                            if (var5_6.isEmpty()) {
                                try {
                                    if (this.instance == null) {
                                        var6_7 /* !! */  = provider.get();
                                        v0 /* !! */  = var7_9 = var6_7 /* !! */  == null ? SingletonScope.access$200() : var6_7 /* !! */ ;
                                        if (this.instance == null) {
                                            if (Scopes.isCircularProxy(var6_7 /* !! */ )) {
                                                var8_11 /* !! */  = var6_7 /* !! */ ;
                                                return (T)var8_11 /* !! */ ;
                                            }
                                            var8_12 = this.constructionContext;
                                            synchronized (var8_12) {
                                                this.instance = var7_9;
                                                this.constructionContext.setProxyDelegates(var6_7 /* !! */ );
                                                ** GOTO lbl52
                                            }
                                        } else {
                                            Preconditions.checkState(this.instance == var7_9, "Singleton is called recursively returning different results");
                                        }
                                    }
                                    break block31;
                                }
                                catch (RuntimeException var6_8) {
                                    var7_9 = this.constructionContext;
                                    synchronized (var7_9) {
                                        this.constructionContext.finishConstruction();
                                        throw var6_8;
                                    }
                                }
                                finally {
                                    this.creationLock.unlock();
                                }
                            }
                            var6_7 /* !! */  = this.constructionContext;
                            synchronized (var6_7 /* !! */ ) {
                                if (this.instance == null) {
                                    var7_10 = Preconditions.checkNotNull(var3_4.getDependency(), "internalContext.getDependency()");
                                    var8_13 = var7_10.getKey().getTypeLiteral().getRawType();
                                    try {
                                        var10_18 = var9_16 = this.constructionContext.createProxy(new Errors(), var3_4.getInjectorOptions(), var8_13);
                                    }
                                    catch (ErrorsException var9_17) {
                                        var10_19 = Iterables.getOnlyElement(var9_17.getErrors().getMessages());
                                        var11_15 = this.createCycleDependenciesMessage((Map<Thread, InternalContext>)ImmutableMap.copyOf(SingletonScope.access$100()), var5_6, var10_19);
                                        throw new ProvisionException(ImmutableList.of(var11_15, var10_19));
                                    }
                                    return (T)var10_18;
                                }
                            }
                        }
                        finally {
                            if (var4_5 != null) {
                                SingletonScope.access$100().put(var2_2, var4_5);
                            } else {
                                SingletonScope.access$100().remove(var2_2);
                            }
                        }
                    }
                    var5_6 = this.instance;
                    Preconditions.checkState(var5_6 != null, "Internal error: Singleton is not initialized contrary to our expectations");
                    var6_7 /* !! */  = var5_6;
                    if (var5_6 == SingletonScope.access$200()) {
                        v1 = null;
                        return (T)v1;
                    }
                    v1 = var6_7 /* !! */ ;
                    return (T)v1;
                }
                var2_3 = var1_1;
                if (var1_1 == SingletonScope.access$200()) {
                    v2 = null;
                    return (T)v2;
                }
                v2 = var2_3;
                return (T)v2;
            }

            private Message createCycleDependenciesMessage(Map<Thread, InternalContext> map, ListMultimap<Long, Key<?>> listMultimap, Message message) {
                ArrayList<Object> arrayList = Lists.newArrayList();
                arrayList.add(Thread.currentThread());
                HashMap<Long, Thread> hashMap = Maps.newHashMap();
                for (Thread thread : map.keySet()) {
                    hashMap.put(thread.getId(), thread);
                }
                Iterator iterator = listMultimap.keySet().iterator();
                while (iterator.hasNext()) {
                    Object object2;
                    long l2 = (Long)iterator.next();
                    Thread thread = (Thread)hashMap.get(l2);
                    List list = Collections.unmodifiableList(listMultimap.get(l2));
                    if (thread == null) continue;
                    List<DependencyAndSource> list2 = null;
                    boolean bl = false;
                    InternalContext internalContext = map.get(thread);
                    if (internalContext != null) {
                        list2 = internalContext.getDependencyChain();
                        object2 = Lists.newLinkedList(list);
                        for (DependencyAndSource dependencyAndSource : list2) {
                            Object object = dependencyAndSource.getDependency();
                            if (object == null || !object.getKey().equals(object2.get(0))) continue;
                            object2.remove(0);
                            if (!object2.isEmpty()) continue;
                            bl = true;
                            break;
                        }
                    }
                    if (bl) {
                        object2 = list.get(0);
                        boolean bl2 = false;
                        for (Object object : list2) {
                            Dependency dependency = object.getDependency();
                            if (dependency == null) continue;
                            if (bl2) {
                                arrayList.add(dependency);
                                arrayList.add(object.getBindingSource());
                                continue;
                            }
                            if (!dependency.getKey().equals(object2)) continue;
                            bl2 = true;
                            arrayList.add(object.getBindingSource());
                        }
                    } else {
                        arrayList.addAll(list);
                    }
                    arrayList.add(thread);
                }
                return new Message(arrayList, String.format("Encountered circular dependency spanning several threads. %s", message.getMessage()), null);
            }

            public String toString() {
                return String.format("%s[%s]", provider, Scopes.SINGLETON);
            }
        };
    }

    @Override
    public String toString() {
        return "Scopes.SINGLETON";
    }

    static /* synthetic */ ConcurrentMap access$100() {
        return internalContextsMap;
    }

    static /* synthetic */ Object access$200() {
        return NULL;
    }

}

