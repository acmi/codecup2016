/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.PrivateBinder;
import com.google.inject.internal.ExposureBuilder;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.PrivateElements;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PrivateElementsImpl
implements PrivateElements {
    private final Object source;
    private List<Element> elementsMutable = Lists.newArrayList();
    private List<ExposureBuilder<?>> exposureBuilders = Lists.newArrayList();
    private ImmutableList<Element> elements;
    private ImmutableMap<Key<?>, Object> exposedKeysToSources;
    private Injector injector;

    public PrivateElementsImpl(Object object) {
        this.source = Preconditions.checkNotNull(object, "source");
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    @Override
    public List<Element> getElements() {
        if (this.elements == null) {
            this.elements = ImmutableList.copyOf(this.elementsMutable);
            this.elementsMutable = null;
        }
        return this.elements;
    }

    @Override
    public Injector getInjector() {
        return this.injector;
    }

    public void initInjector(Injector injector) {
        Preconditions.checkState(this.injector == null, "injector already initialized");
        this.injector = Preconditions.checkNotNull(injector, "injector");
    }

    @Override
    public Set<Key<?>> getExposedKeys() {
        if (this.exposedKeysToSources == null) {
            LinkedHashMap linkedHashMap = Maps.newLinkedHashMap();
            for (ExposureBuilder exposureBuilder : this.exposureBuilders) {
                linkedHashMap.put(exposureBuilder.getKey(), exposureBuilder.getSource());
            }
            this.exposedKeysToSources = ImmutableMap.copyOf(linkedHashMap);
            this.exposureBuilders = null;
        }
        return this.exposedKeysToSources.keySet();
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    public List<Element> getElementsMutable() {
        return this.elementsMutable;
    }

    public void addExposureBuilder(ExposureBuilder<?> exposureBuilder) {
        this.exposureBuilders.add(exposureBuilder);
    }

    @Override
    public void applyTo(Binder binder) {
        PrivateBinder privateBinder = binder.withSource(this.source).newPrivateBinder();
        for (Element object : this.getElements()) {
            object.applyTo(privateBinder);
        }
        this.getExposedKeys();
        for (Map.Entry entry : this.exposedKeysToSources.entrySet()) {
            privateBinder.withSource(entry.getValue()).expose((Key)entry.getKey());
        }
    }

    @Override
    public Object getExposedSource(Key<?> key) {
        this.getExposedKeys();
        Object object = this.exposedKeysToSources.get(key);
        Preconditions.checkArgument(object != null, "%s not exposed by %s.", key, this);
        return object;
    }

    public String toString() {
        return MoreObjects.toStringHelper(PrivateElements.class).add("exposedKeys", this.getExposedKeys()).add("source", this.getSource()).toString();
    }
}

