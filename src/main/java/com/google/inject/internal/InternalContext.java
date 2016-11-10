/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.internal.ConstructionContext;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.DependencyAndSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class InternalContext {
    private final InjectorImpl.InjectorOptions options;
    private Map<Object, ConstructionContext<?>> constructionContexts = Maps.newHashMap();
    private Dependency<?> dependency;
    private final DependencyStack state = new DependencyStack();

    InternalContext(InjectorImpl.InjectorOptions injectorOptions) {
        this.options = injectorOptions;
    }

    public InjectorImpl.InjectorOptions getInjectorOptions() {
        return this.options;
    }

    public <T> ConstructionContext<T> getConstructionContext(Object object) {
        ConstructionContext constructionContext = this.constructionContexts.get(object);
        if (constructionContext == null) {
            constructionContext = new ConstructionContext();
            this.constructionContexts.put(object, constructionContext);
        }
        return constructionContext;
    }

    public Dependency<?> getDependency() {
        return this.dependency;
    }

    public Dependency<?> pushDependency(Dependency<?> dependency, Object object) {
        Dependency dependency2 = this.dependency;
        this.dependency = dependency;
        this.state.add(dependency, object);
        return dependency2;
    }

    public void popStateAndSetDependency(Dependency<?> dependency) {
        this.state.pop();
        this.dependency = dependency;
    }

    public void pushState(Key<?> key, Object object) {
        this.state.add(key, object);
    }

    public void popState() {
        this.state.pop();
    }

    public List<DependencyAndSource> getDependencyChain() {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (int i2 = 0; i2 < this.state.size(); i2 += 2) {
            Object object = this.state.get(i2);
            Dependency dependency = object instanceof Key ? Dependency.get((Key)object) : (Dependency)object;
            builder.add(new DependencyAndSource(dependency, this.state.get(i2 + 1)));
        }
        return builder.build();
    }

    private static final class DependencyStack {
        private Object[] elements = new Object[16];
        private int size = 0;

        private DependencyStack() {
        }

        public void add(Object object, Object object2) {
            if (this.elements.length < this.size + 2) {
                this.elements = Arrays.copyOf(this.elements, this.elements.length * 3 / 2 + 2);
            }
            this.elements[this.size++] = object;
            this.elements[this.size++] = object2;
        }

        public void pop() {
            this.elements[--this.size] = null;
            this.elements[--this.size] = null;
        }

        public Object get(int n2) {
            return this.elements[n2];
        }

        public int size() {
            return this.size;
        }
    }

}

