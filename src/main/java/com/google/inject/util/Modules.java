/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.util;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.internal.Errors;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.spi.Elements;
import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
import com.google.inject.spi.PrivateElements;
import com.google.inject.spi.ScopeBinding;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Modules {
    public static final Module EMPTY_MODULE = new EmptyModule();

    private Modules() {
    }

    public static /* varargs */ OverriddenModuleBuilder override(Module ... arrmodule) {
        return new RealOverriddenModuleBuilder(Arrays.asList(arrmodule));
    }

    public static OverriddenModuleBuilder override(Iterable<? extends Module> iterable) {
        return new RealOverriddenModuleBuilder(iterable);
    }

    public static /* varargs */ Module combine(Module ... arrmodule) {
        return Modules.combine(ImmutableSet.copyOf(arrmodule));
    }

    public static Module combine(Iterable<? extends Module> iterable) {
        return new CombinedModule(iterable);
    }

    private static Module extractScanners(Iterable<Element> iterable) {
        final ArrayList arrayList = Lists.newArrayList();
        DefaultElementVisitor<Void> defaultElementVisitor = new DefaultElementVisitor<Void>(){

            @Override
            public Void visit(ModuleAnnotatedMethodScannerBinding moduleAnnotatedMethodScannerBinding) {
                arrayList.add(moduleAnnotatedMethodScannerBinding);
                return null;
            }
        };
        for (Element element : iterable) {
            element.acceptVisitor(defaultElementVisitor);
        }
        return new AbstractModule(){

            @Override
            protected void configure() {
                for (ModuleAnnotatedMethodScannerBinding moduleAnnotatedMethodScannerBinding : arrayList) {
                    moduleAnnotatedMethodScannerBinding.applyTo(this.binder());
                }
            }
        };
    }

    private static class ModuleWriter
    extends DefaultElementVisitor<Void> {
        protected final Binder binder;

        ModuleWriter(Binder binder) {
            this.binder = binder.skipSources(this.getClass());
        }

        @Override
        protected Void visitOther(Element element) {
            element.applyTo(this.binder);
            return null;
        }

        void writeAll(Iterable<? extends Element> iterable) {
            for (Element element : iterable) {
                element.acceptVisitor(this);
            }
        }
    }

    static class OverrideModule
    extends AbstractModule {
        private final ImmutableSet<Module> overrides;
        private final ImmutableSet<Module> baseModules;

        OverrideModule(Iterable<? extends Module> iterable, ImmutableSet<Module> immutableSet) {
            this.overrides = ImmutableSet.copyOf(iterable);
            this.baseModules = immutableSet;
        }

        @Override
        public void configure() {
            Object object3;
            Object object;
            Object object4;
            Object object5 = this.binder();
            List<Element> list = Elements.getElements(this.currentStage(), this.baseModules);
            if (list.size() == 1 && (object4 = Iterables.getOnlyElement(list)) instanceof PrivateElements) {
                object = (PrivateElements)object4;
                object3 = object5.newPrivateBinder().withSource(object.getSource());
                for (Key object22 : object.getExposedKeys()) {
                    object3.withSource(object.getExposedSource(object22)).expose(object22);
                }
                object5 = object3;
                list = object.getElements();
            }
            object4 = object5.skipSources(this.getClass());
            object = new LinkedHashSet<Element>(list);
            object3 = Modules.extractScanners((Iterable)object);
            List<Element> list2 = Elements.getElements(this.currentStage(), ImmutableList.builder().addAll(this.overrides).add(object3).build());
            final HashSet hashSet = Sets.newHashSet();
            final HashMap hashMap = Maps.newHashMap();
            new ModuleWriter((Binder)object4){

                @Override
                public <T> Void visit(Binding<T> binding) {
                    hashSet.add(binding.getKey());
                    return (Void)super.visit(binding);
                }

                @Override
                public Void visit(ScopeBinding scopeBinding) {
                    hashMap.put(scopeBinding.getAnnotationType(), scopeBinding);
                    return (Void)super.visit(scopeBinding);
                }

                @Override
                public Void visit(PrivateElements privateElements) {
                    hashSet.addAll(privateElements.getExposedKeys());
                    return (Void)super.visit(privateElements);
                }
            }.writeAll(list2);
            final HashMap hashMap2 = Maps.newHashMap();
            final ArrayList arrayList = Lists.newArrayList();
            new ModuleWriter((Binder)object4){

                @Override
                public <T> Void visit(Binding<T> binding) {
                    if (!hashSet.remove(binding.getKey())) {
                        super.visit(binding);
                        Scope scope = OverrideModule.this.getScopeInstanceOrNull(binding);
                        if (scope != null) {
                            ArrayList<Object> arrayList2 = (ArrayList<Object>)hashMap2.get(scope);
                            if (arrayList2 == null) {
                                arrayList2 = Lists.newArrayList();
                                hashMap2.put(scope, arrayList2);
                            }
                            arrayList2.add(binding.getSource());
                        }
                    }
                    return null;
                }

                void rewrite(Binder binder, PrivateElements privateElements, Set<Key<?>> set) {
                    PrivateBinder privateBinder = binder.withSource(privateElements.getSource()).newPrivateBinder();
                    HashSet hashSet2 = Sets.newHashSet();
                    for (Key object : privateElements.getExposedKeys()) {
                        if (set.remove(object)) {
                            hashSet2.add(object);
                            continue;
                        }
                        privateBinder.withSource(privateElements.getExposedSource(object)).expose(object);
                    }
                    for (Element element : privateElements.getElements()) {
                        if (element instanceof Binding && hashSet2.remove(((Binding)element).getKey())) continue;
                        if (element instanceof PrivateElements) {
                            this.rewrite(privateBinder, (PrivateElements)element, hashSet2);
                            continue;
                        }
                        element.applyTo(privateBinder);
                    }
                }

                @Override
                public Void visit(PrivateElements privateElements) {
                    this.rewrite(this.binder, privateElements, hashSet);
                    return null;
                }

                @Override
                public Void visit(ScopeBinding scopeBinding) {
                    arrayList.add(scopeBinding);
                    return null;
                }
            }.writeAll((Iterable<? extends Element>)object);
            new ModuleWriter((Binder)object4){

                @Override
                public Void visit(ScopeBinding scopeBinding) {
                    ScopeBinding scopeBinding2 = (ScopeBinding)hashMap.remove(scopeBinding.getAnnotationType());
                    if (scopeBinding2 == null) {
                        super.visit(scopeBinding);
                    } else {
                        List list = (List)hashMap2.get(scopeBinding.getScope());
                        if (list != null) {
                            StringBuilder stringBuilder = new StringBuilder("The scope for @%s is bound directly and cannot be overridden.");
                            stringBuilder.append("%n     original binding at " + Errors.convert(scopeBinding.getSource()));
                            for (Object e2 : list) {
                                stringBuilder.append("%n     bound directly at " + Errors.convert(e2) + "");
                            }
                            this.binder.withSource(scopeBinding2.getSource()).addError(stringBuilder.toString(), scopeBinding.getAnnotationType().getSimpleName());
                        }
                    }
                    return null;
                }
            }.writeAll(arrayList);
        }

        private Scope getScopeInstanceOrNull(Binding<?> binding) {
            return (Scope)binding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Scope>(){

                @Override
                public Scope visitScope(Scope scope) {
                    return scope;
                }
            });
        }

    }

    private static final class RealOverriddenModuleBuilder
    implements OverriddenModuleBuilder {
        private final ImmutableSet<Module> baseModules;

        private RealOverriddenModuleBuilder(Iterable<? extends Module> iterable) {
            this.baseModules = ImmutableSet.copyOf(iterable);
        }

        @Override
        public /* varargs */ Module with(Module ... arrmodule) {
            return this.with(Arrays.asList(arrmodule));
        }

        @Override
        public Module with(Iterable<? extends Module> iterable) {
            return new OverrideModule(iterable, this.baseModules);
        }
    }

    public static interface OverriddenModuleBuilder {
        public /* varargs */ Module with(Module ... var1);

        public Module with(Iterable<? extends Module> var1);
    }

    private static class CombinedModule
    implements Module {
        final Set<Module> modulesSet;

        CombinedModule(Iterable<? extends Module> iterable) {
            this.modulesSet = ImmutableSet.copyOf(iterable);
        }

        @Override
        public void configure(Binder binder) {
            binder = binder.skipSources(this.getClass());
            for (Module module : this.modulesSet) {
                binder.install(module);
            }
        }
    }

    private static class EmptyModule
    implements Module {
        private EmptyModule() {
        }

        @Override
        public void configure(Binder binder) {
        }
    }

}

