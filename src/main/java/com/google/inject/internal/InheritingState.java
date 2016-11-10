/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.MethodAspect;
import com.google.inject.internal.State;
import com.google.inject.internal.WeakKeySet;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
import com.google.inject.spi.ProvisionListenerBinding;
import com.google.inject.spi.ScopeBinding;
import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.spi.TypeListenerBinding;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class InheritingState
implements State {
    private final State parent;
    private final Map<Key<?>, Binding<?>> explicitBindingsMutable = Maps.newLinkedHashMap();
    private final Map<Key<?>, Binding<?>> explicitBindings = Collections.unmodifiableMap(this.explicitBindingsMutable);
    private final Map<Class<? extends Annotation>, ScopeBinding> scopes = Maps.newHashMap();
    private final List<TypeConverterBinding> converters = Lists.newArrayList();
    private final List<MethodAspect> methodAspects = Lists.newArrayList();
    private final List<TypeListenerBinding> typeListenerBindings = Lists.newArrayList();
    private final List<ProvisionListenerBinding> provisionListenerBindings = Lists.newArrayList();
    private final List<ModuleAnnotatedMethodScannerBinding> scannerBindings = Lists.newArrayList();
    private final WeakKeySet blacklistedKeys;
    private final Object lock;

    InheritingState(State state) {
        this.parent = Preconditions.checkNotNull(state, "parent");
        this.lock = state == State.NONE ? this : state.lock();
        this.blacklistedKeys = new WeakKeySet(this.lock);
    }

    @Override
    public State parent() {
        return this.parent;
    }

    @Override
    public <T> BindingImpl<T> getExplicitBinding(Key<T> key) {
        Binding binding = this.explicitBindings.get(key);
        return binding != null ? (BindingImpl<T>)binding : this.parent.getExplicitBinding(key);
    }

    @Override
    public Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel() {
        return this.explicitBindings;
    }

    @Override
    public void putBinding(Key<?> key, BindingImpl<?> bindingImpl) {
        this.explicitBindingsMutable.put(key, bindingImpl);
    }

    @Override
    public ScopeBinding getScopeBinding(Class<? extends Annotation> class_) {
        ScopeBinding scopeBinding = this.scopes.get(class_);
        return scopeBinding != null ? scopeBinding : this.parent.getScopeBinding(class_);
    }

    @Override
    public void putScopeBinding(Class<? extends Annotation> class_, ScopeBinding scopeBinding) {
        this.scopes.put(class_, scopeBinding);
    }

    @Override
    public Iterable<TypeConverterBinding> getConvertersThisLevel() {
        return this.converters;
    }

    @Override
    public void addConverter(TypeConverterBinding typeConverterBinding) {
        this.converters.add(typeConverterBinding);
    }

    @Override
    public TypeConverterBinding getConverter(String string, TypeLiteral<?> typeLiteral, Errors errors, Object object) {
        TypeConverterBinding typeConverterBinding = null;
        for (State state = this; state != State.NONE; state = state.parent()) {
            for (TypeConverterBinding typeConverterBinding2 : state.getConvertersThisLevel()) {
                if (!typeConverterBinding2.getTypeMatcher().matches(typeLiteral)) continue;
                if (typeConverterBinding != null) {
                    errors.ambiguousTypeConversion(string, object, typeLiteral, typeConverterBinding, typeConverterBinding2);
                }
                typeConverterBinding = typeConverterBinding2;
            }
        }
        return typeConverterBinding;
    }

    @Override
    public void addMethodAspect(MethodAspect methodAspect) {
        this.methodAspects.add(methodAspect);
    }

    @Override
    public ImmutableList<MethodAspect> getMethodAspects() {
        return new ImmutableList.Builder().addAll(this.parent.getMethodAspects()).addAll(this.methodAspects).build();
    }

    @Override
    public void addTypeListener(TypeListenerBinding typeListenerBinding) {
        this.typeListenerBindings.add(typeListenerBinding);
    }

    @Override
    public List<TypeListenerBinding> getTypeListenerBindings() {
        List<TypeListenerBinding> list = this.parent.getTypeListenerBindings();
        ArrayList<TypeListenerBinding> arrayList = Lists.newArrayListWithCapacity(list.size() + this.typeListenerBindings.size());
        arrayList.addAll(list);
        arrayList.addAll(this.typeListenerBindings);
        return arrayList;
    }

    @Override
    public void addProvisionListener(ProvisionListenerBinding provisionListenerBinding) {
        this.provisionListenerBindings.add(provisionListenerBinding);
    }

    @Override
    public List<ProvisionListenerBinding> getProvisionListenerBindings() {
        List<ProvisionListenerBinding> list = this.parent.getProvisionListenerBindings();
        ArrayList<ProvisionListenerBinding> arrayList = Lists.newArrayListWithCapacity(list.size() + this.provisionListenerBindings.size());
        arrayList.addAll(list);
        arrayList.addAll(this.provisionListenerBindings);
        return arrayList;
    }

    @Override
    public void addScanner(ModuleAnnotatedMethodScannerBinding moduleAnnotatedMethodScannerBinding) {
        this.scannerBindings.add(moduleAnnotatedMethodScannerBinding);
    }

    @Override
    public List<ModuleAnnotatedMethodScannerBinding> getScannerBindings() {
        List<ModuleAnnotatedMethodScannerBinding> list = this.parent.getScannerBindings();
        ArrayList<ModuleAnnotatedMethodScannerBinding> arrayList = Lists.newArrayListWithCapacity(list.size() + this.scannerBindings.size());
        arrayList.addAll(list);
        arrayList.addAll(this.scannerBindings);
        return arrayList;
    }

    @Override
    public void blacklist(Key<?> key, State state, Object object) {
        this.parent.blacklist(key, state, object);
        this.blacklistedKeys.add(key, state, object);
    }

    @Override
    public boolean isBlacklisted(Key<?> key) {
        return this.blacklistedKeys.contains(key);
    }

    @Override
    public Set<Object> getSourcesForBlacklistedKey(Key<?> key) {
        return this.blacklistedKeys.getSources(key);
    }

    @Override
    public Object lock() {
        return this.lock;
    }

    @Override
    public Map<Class<? extends Annotation>, Scope> getScopes() {
        ImmutableMap.Builder<Class<? extends Annotation>, Scope> builder = ImmutableMap.builder();
        for (Map.Entry<Class<? extends Annotation>, ScopeBinding> entry : this.scopes.entrySet()) {
            builder.put(entry.getKey(), entry.getValue().getScope());
        }
        return builder.build();
    }
}

