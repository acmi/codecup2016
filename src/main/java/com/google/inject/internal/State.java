/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.MethodAspect;
import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
import com.google.inject.spi.ProvisionListenerBinding;
import com.google.inject.spi.ScopeBinding;
import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.spi.TypeListenerBinding;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

interface State {
    public static final State NONE = new State(){

        @Override
        public State parent() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> BindingImpl<T> getExplicitBinding(Key<T> key) {
            return null;
        }

        @Override
        public Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putBinding(Key<?> key, BindingImpl<?> bindingImpl) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ScopeBinding getScopeBinding(Class<? extends Annotation> class_) {
            return null;
        }

        @Override
        public void putScopeBinding(Class<? extends Annotation> class_, ScopeBinding scopeBinding) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addConverter(TypeConverterBinding typeConverterBinding) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeConverterBinding getConverter(String string, TypeLiteral<?> typeLiteral, Errors errors, Object object) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<TypeConverterBinding> getConvertersThisLevel() {
            return ImmutableSet.of();
        }

        @Override
        public void addMethodAspect(MethodAspect methodAspect) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ImmutableList<MethodAspect> getMethodAspects() {
            return ImmutableList.of();
        }

        @Override
        public void addTypeListener(TypeListenerBinding typeListenerBinding) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<TypeListenerBinding> getTypeListenerBindings() {
            return ImmutableList.of();
        }

        @Override
        public void addProvisionListener(ProvisionListenerBinding provisionListenerBinding) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<ProvisionListenerBinding> getProvisionListenerBindings() {
            return ImmutableList.of();
        }

        @Override
        public void addScanner(ModuleAnnotatedMethodScannerBinding moduleAnnotatedMethodScannerBinding) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<ModuleAnnotatedMethodScannerBinding> getScannerBindings() {
            return ImmutableList.of();
        }

        @Override
        public void blacklist(Key<?> key, State state, Object object) {
        }

        @Override
        public boolean isBlacklisted(Key<?> key) {
            return true;
        }

        @Override
        public Set<Object> getSourcesForBlacklistedKey(Key<?> key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object lock() {
            throw new UnsupportedOperationException();
        }

        public Object singletonCreationLock() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<Class<? extends Annotation>, Scope> getScopes() {
            return ImmutableMap.of();
        }
    };

    public State parent();

    public <T> BindingImpl<T> getExplicitBinding(Key<T> var1);

    public Map<Key<?>, Binding<?>> getExplicitBindingsThisLevel();

    public void putBinding(Key<?> var1, BindingImpl<?> var2);

    public ScopeBinding getScopeBinding(Class<? extends Annotation> var1);

    public void putScopeBinding(Class<? extends Annotation> var1, ScopeBinding var2);

    public void addConverter(TypeConverterBinding var1);

    public TypeConverterBinding getConverter(String var1, TypeLiteral<?> var2, Errors var3, Object var4);

    public Iterable<TypeConverterBinding> getConvertersThisLevel();

    public void addMethodAspect(MethodAspect var1);

    public ImmutableList<MethodAspect> getMethodAspects();

    public void addTypeListener(TypeListenerBinding var1);

    public List<TypeListenerBinding> getTypeListenerBindings();

    public void addProvisionListener(ProvisionListenerBinding var1);

    public List<ProvisionListenerBinding> getProvisionListenerBindings();

    public void addScanner(ModuleAnnotatedMethodScannerBinding var1);

    public List<ModuleAnnotatedMethodScannerBinding> getScannerBindings();

    public void blacklist(Key<?> var1, State var2, Object var3);

    public boolean isBlacklisted(Key<?> var1);

    public Set<Object> getSourcesForBlacklistedKey(Key<?> var1);

    public Object lock();

    public Map<Class<? extends Annotation>, Scope> getScopes();

}

