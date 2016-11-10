/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.PrivateModule;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.AnnotatedElementBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.internal.AbstractBindingBuilder;
import com.google.inject.internal.BindingBuilder;
import com.google.inject.internal.ConstantBindingBuilderImpl;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ExposureBuilder;
import com.google.inject.internal.InternalFlags;
import com.google.inject.internal.MoreTypes;
import com.google.inject.internal.PrivateElementsImpl;
import com.google.inject.internal.ProviderMethodsModule;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.internal.util.StackTraceElements;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.DefaultBindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.DisableCircularProxiesOption;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementSource;
import com.google.inject.spi.InjectionRequest;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.InterceptorBinding;
import com.google.inject.spi.MembersInjectorLookup;
import com.google.inject.spi.Message;
import com.google.inject.spi.ModuleAnnotatedMethodScanner;
import com.google.inject.spi.ModuleAnnotatedMethodScannerBinding;
import com.google.inject.spi.ModuleSource;
import com.google.inject.spi.ProviderLookup;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.ProvisionListenerBinding;
import com.google.inject.spi.RequireAtInjectOnConstructorsOption;
import com.google.inject.spi.RequireExactBindingAnnotationsOption;
import com.google.inject.spi.RequireExplicitBindingsOption;
import com.google.inject.spi.ScopeBinding;
import com.google.inject.spi.StaticInjectionRequest;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.spi.TypeListener;
import com.google.inject.spi.TypeListenerBinding;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aopalliance.intercept.MethodInterceptor;

public final class Elements {
    private static final BindingTargetVisitor<Object, Object> GET_INSTANCE_VISITOR = new DefaultBindingTargetVisitor<Object, Object>(){

        @Override
        public Object visit(InstanceBinding<?> instanceBinding) {
            return instanceBinding.getInstance();
        }

        @Override
        protected Object visitOther(Binding<?> binding) {
            throw new IllegalArgumentException();
        }
    };

    public static /* varargs */ List<Element> getElements(Module ... arrmodule) {
        return Elements.getElements(Stage.DEVELOPMENT, Arrays.asList(arrmodule));
    }

    public static /* varargs */ List<Element> getElements(Stage stage, Module ... arrmodule) {
        return Elements.getElements(stage, Arrays.asList(arrmodule));
    }

    public static List<Element> getElements(Iterable<? extends Module> iterable) {
        return Elements.getElements(Stage.DEVELOPMENT, iterable);
    }

    public static List<Element> getElements(Stage stage, Iterable<? extends Module> iterable) {
        RecordingBinder recordingBinder = new RecordingBinder(stage);
        for (Module object : iterable) {
            recordingBinder.install(object);
        }
        recordingBinder.scanForAnnotatedMethods();
        for (RecordingBinder recordingBinder2 : recordingBinder.privateBinders) {
            recordingBinder2.scanForAnnotatedMethods();
        }
        StackTraceElements.clearCache();
        return Collections.unmodifiableList(recordingBinder.elements);
    }

    public static Module getModule(Iterable<? extends Element> iterable) {
        return new ElementsAsModule(iterable);
    }

    static <T> BindingTargetVisitor<T, T> getInstanceVisitor() {
        return GET_INSTANCE_VISITOR;
    }

    private static class RecordingBinder
    implements Binder,
    PrivateBinder {
        private final Stage stage;
        private final Map<Module, ModuleInfo> modules;
        private final List<Element> elements;
        private final Object source;
        private ModuleSource moduleSource = null;
        private final SourceProvider sourceProvider;
        private final Set<ModuleAnnotatedMethodScanner> scanners;
        private final RecordingBinder parent;
        private final PrivateElementsImpl privateElements;
        private final List<RecordingBinder> privateBinders;

        private RecordingBinder(Stage stage) {
            this.stage = stage;
            this.modules = Maps.newLinkedHashMap();
            this.scanners = Sets.newLinkedHashSet();
            this.elements = Lists.newArrayList();
            this.source = null;
            this.sourceProvider = SourceProvider.DEFAULT_INSTANCE.plusSkippedClasses(Elements.class, RecordingBinder.class, AbstractModule.class, ConstantBindingBuilderImpl.class, AbstractBindingBuilder.class, BindingBuilder.class);
            this.parent = null;
            this.privateElements = null;
            this.privateBinders = Lists.newArrayList();
        }

        private RecordingBinder(RecordingBinder recordingBinder, Object object, SourceProvider sourceProvider) {
            Preconditions.checkArgument(object == null ^ sourceProvider == null);
            this.stage = recordingBinder.stage;
            this.modules = recordingBinder.modules;
            this.elements = recordingBinder.elements;
            this.scanners = recordingBinder.scanners;
            this.source = object;
            this.moduleSource = recordingBinder.moduleSource;
            this.sourceProvider = sourceProvider;
            this.parent = recordingBinder.parent;
            this.privateElements = recordingBinder.privateElements;
            this.privateBinders = recordingBinder.privateBinders;
        }

        private RecordingBinder(RecordingBinder recordingBinder, PrivateElementsImpl privateElementsImpl) {
            this.stage = recordingBinder.stage;
            this.modules = Maps.newLinkedHashMap();
            this.scanners = Sets.newLinkedHashSet(recordingBinder.scanners);
            this.elements = privateElementsImpl.getElementsMutable();
            this.source = recordingBinder.source;
            this.moduleSource = recordingBinder.moduleSource;
            this.sourceProvider = recordingBinder.sourceProvider;
            this.parent = recordingBinder;
            this.privateElements = privateElementsImpl;
            this.privateBinders = recordingBinder.privateBinders;
        }

        @Override
        public /* varargs */ void bindInterceptor(Matcher<? super Class<?>> matcher, Matcher<? super Method> matcher2, MethodInterceptor ... arrmethodInterceptor) {
            this.elements.add(new InterceptorBinding(this.getElementSource(), matcher, matcher2, arrmethodInterceptor));
        }

        @Override
        public void bindScope(Class<? extends Annotation> class_, Scope scope) {
            this.elements.add(new ScopeBinding(this.getElementSource(), class_, scope));
        }

        @Override
        public void requestInjection(Object object) {
            this.requestInjection(TypeLiteral.get(object.getClass()), object);
        }

        @Override
        public <T> void requestInjection(TypeLiteral<T> typeLiteral, T t2) {
            this.elements.add(new InjectionRequest<T>(this.getElementSource(), MoreTypes.canonicalizeForKey(typeLiteral), t2));
        }

        @Override
        public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
            MembersInjectorLookup<T> membersInjectorLookup = new MembersInjectorLookup<T>(this.getElementSource(), MoreTypes.canonicalizeForKey(typeLiteral));
            this.elements.add(membersInjectorLookup);
            return membersInjectorLookup.getMembersInjector();
        }

        @Override
        public <T> MembersInjector<T> getMembersInjector(Class<T> class_) {
            return this.getMembersInjector(TypeLiteral.get(class_));
        }

        @Override
        public void bindListener(Matcher<? super TypeLiteral<?>> matcher, TypeListener typeListener) {
            this.elements.add(new TypeListenerBinding(this.getElementSource(), typeListener, matcher));
        }

        @Override
        public /* varargs */ void bindListener(Matcher<? super Binding<?>> matcher, ProvisionListener ... arrprovisionListener) {
            this.elements.add(new ProvisionListenerBinding(this.getElementSource(), matcher, arrprovisionListener));
        }

        @Override
        public /* varargs */ void requestStaticInjection(Class<?> ... arrclass) {
            for (Class class_ : arrclass) {
                this.elements.add(new StaticInjectionRequest(this.getElementSource(), class_));
            }
        }

        void scanForAnnotatedMethods() {
            for (ModuleAnnotatedMethodScanner moduleAnnotatedMethodScanner : this.scanners) {
                for (Map.Entry<Module, ModuleInfo> entry : Maps.newLinkedHashMap(this.modules).entrySet()) {
                    Module module = entry.getKey();
                    ModuleInfo moduleInfo = entry.getValue();
                    if (moduleInfo.skipScanning) continue;
                    this.moduleSource = entry.getValue().moduleSource;
                    try {
                        moduleInfo.binder.install(ProviderMethodsModule.forModule(module, moduleAnnotatedMethodScanner));
                    }
                    catch (RuntimeException runtimeException) {
                        Collection<Message> collection = Errors.getMessagesFromThrowable(runtimeException);
                        if (!collection.isEmpty()) {
                            this.elements.addAll(collection);
                            continue;
                        }
                        this.addError(runtimeException);
                    }
                }
            }
            this.moduleSource = null;
        }

        @Override
        public void install(Module module) {
            if (!this.modules.containsKey(module)) {
                RecordingBinder recordingBinder = this;
                boolean bl = false;
                if (module instanceof ProviderMethodsModule) {
                    Object object = ((ProviderMethodsModule)module).getDelegateModule();
                    if (this.moduleSource == null || !this.moduleSource.getModuleClassName().equals(object.getClass().getName())) {
                        this.moduleSource = this.getModuleSource(object);
                        bl = true;
                    }
                } else {
                    this.moduleSource = this.getModuleSource(module);
                    bl = true;
                }
                boolean bl2 = false;
                if (module instanceof PrivateModule) {
                    recordingBinder = (RecordingBinder)recordingBinder.newPrivateBinder();
                    recordingBinder.modules.put(module, new ModuleInfo(recordingBinder, this.moduleSource, false));
                    bl2 = true;
                }
                this.modules.put(module, new ModuleInfo(recordingBinder, this.moduleSource, bl2));
                try {
                    module.configure(recordingBinder);
                }
                catch (RuntimeException runtimeException) {
                    Collection<Message> collection = Errors.getMessagesFromThrowable(runtimeException);
                    if (!collection.isEmpty()) {
                        this.elements.addAll(collection);
                    }
                    this.addError(runtimeException);
                }
                recordingBinder.install(ProviderMethodsModule.forModule(module));
                if (bl) {
                    this.moduleSource = this.moduleSource.getParent();
                }
            }
        }

        @Override
        public Stage currentStage() {
            return this.stage;
        }

        @Override
        public /* varargs */ void addError(String string, Object ... arrobject) {
            this.elements.add(new Message(this.getElementSource(), Errors.format(string, arrobject)));
        }

        @Override
        public void addError(Throwable throwable) {
            String string = "An exception was caught and reported. Message: " + throwable.getMessage();
            this.elements.add(new Message(ImmutableList.of(this.getElementSource()), string, throwable));
        }

        @Override
        public void addError(Message message) {
            this.elements.add(message);
        }

        public <T> AnnotatedBindingBuilder<T> bind(Key<T> key) {
            BindingBuilder<T> bindingBuilder = new BindingBuilder<T>(this, this.elements, this.getElementSource(), MoreTypes.canonicalizeKey(key));
            return bindingBuilder;
        }

        @Override
        public <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
            return this.bind((Key)Key.get(typeLiteral));
        }

        @Override
        public <T> AnnotatedBindingBuilder<T> bind(Class<T> class_) {
            return this.bind((Key)Key.get(class_));
        }

        @Override
        public AnnotatedConstantBindingBuilder bindConstant() {
            return new ConstantBindingBuilderImpl(this, this.elements, this.getElementSource());
        }

        @Override
        public <T> Provider<T> getProvider(Key<T> key) {
            return this.getProvider(Dependency.get(key));
        }

        @Override
        public <T> Provider<T> getProvider(Dependency<T> dependency) {
            ProviderLookup<T> providerLookup = new ProviderLookup<T>((Object)this.getElementSource(), dependency);
            this.elements.add(providerLookup);
            return providerLookup.getProvider();
        }

        @Override
        public <T> Provider<T> getProvider(Class<T> class_) {
            return this.getProvider(Key.get(class_));
        }

        @Override
        public void convertToTypes(Matcher<? super TypeLiteral<?>> matcher, TypeConverter typeConverter) {
            this.elements.add(new TypeConverterBinding(this.getElementSource(), matcher, typeConverter));
        }

        @Override
        public RecordingBinder withSource(Object object) {
            return object == this.source ? this : new RecordingBinder(this, object, null);
        }

        @Override
        public /* varargs */ RecordingBinder skipSources(Class ... arrclass) {
            if (this.source != null) {
                return this;
            }
            SourceProvider sourceProvider = this.sourceProvider.plusSkippedClasses(arrclass);
            return new RecordingBinder(this, null, sourceProvider);
        }

        @Override
        public PrivateBinder newPrivateBinder() {
            PrivateElementsImpl privateElementsImpl = new PrivateElementsImpl(this.getElementSource());
            RecordingBinder recordingBinder = new RecordingBinder(this, privateElementsImpl);
            this.privateBinders.add(recordingBinder);
            this.elements.add(privateElementsImpl);
            return recordingBinder;
        }

        @Override
        public void disableCircularProxies() {
            this.elements.add(new DisableCircularProxiesOption(this.getElementSource()));
        }

        @Override
        public void requireExplicitBindings() {
            this.elements.add(new RequireExplicitBindingsOption(this.getElementSource()));
        }

        @Override
        public void requireAtInjectOnConstructors() {
            this.elements.add(new RequireAtInjectOnConstructorsOption(this.getElementSource()));
        }

        @Override
        public void requireExactBindingAnnotations() {
            this.elements.add(new RequireExactBindingAnnotationsOption(this.getElementSource()));
        }

        @Override
        public void scanModulesForAnnotatedMethods(ModuleAnnotatedMethodScanner moduleAnnotatedMethodScanner) {
            this.scanners.add(moduleAnnotatedMethodScanner);
            this.elements.add(new ModuleAnnotatedMethodScannerBinding(this.getElementSource(), moduleAnnotatedMethodScanner));
        }

        @Override
        public void expose(Key<?> key) {
            this.exposeInternal(key);
        }

        @Override
        public AnnotatedElementBuilder expose(Class<?> class_) {
            return this.exposeInternal(Key.get(class_));
        }

        @Override
        public AnnotatedElementBuilder expose(TypeLiteral<?> typeLiteral) {
            return this.exposeInternal(Key.get(typeLiteral));
        }

        private <T> AnnotatedElementBuilder exposeInternal(Key<T> key) {
            if (this.privateElements == null) {
                this.addError("Cannot expose %s on a standard binder. Exposed bindings are only applicable to private binders.", key);
                return new AnnotatedElementBuilder(){

                    @Override
                    public void annotatedWith(Class<? extends Annotation> class_) {
                    }

                    @Override
                    public void annotatedWith(Annotation annotation) {
                    }
                };
            }
            ExposureBuilder<T> exposureBuilder = new ExposureBuilder<T>(this, this.getElementSource(), MoreTypes.canonicalizeKey(key));
            this.privateElements.addExposureBuilder(exposureBuilder);
            return exposureBuilder;
        }

        private ModuleSource getModuleSource(Object object) {
            StackTraceElement[] arrstackTraceElement = InternalFlags.getIncludeStackTraceOption() == InternalFlags.IncludeStackTraceOption.COMPLETE ? this.getPartialCallStack(new Throwable().getStackTrace()) : new StackTraceElement[]{};
            if (this.moduleSource == null) {
                return new ModuleSource(object, arrstackTraceElement);
            }
            return this.moduleSource.createChild(object, arrstackTraceElement);
        }

        private ElementSource getElementSource() {
            InternalFlags.IncludeStackTraceOption includeStackTraceOption;
            StackTraceElement[] arrstackTraceElement = null;
            StackTraceElement[] arrstackTraceElement2 = new StackTraceElement[]{};
            ElementSource elementSource = null;
            Object object = this.source;
            if (object instanceof ElementSource) {
                elementSource = (ElementSource)object;
                object = elementSource.getDeclaringSource();
            }
            if ((includeStackTraceOption = InternalFlags.getIncludeStackTraceOption()) == InternalFlags.IncludeStackTraceOption.COMPLETE || includeStackTraceOption == InternalFlags.IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE && object == null) {
                arrstackTraceElement = new Throwable().getStackTrace();
            }
            if (includeStackTraceOption == InternalFlags.IncludeStackTraceOption.COMPLETE) {
                arrstackTraceElement2 = this.getPartialCallStack(arrstackTraceElement);
            }
            if (object == null) {
                object = includeStackTraceOption == InternalFlags.IncludeStackTraceOption.COMPLETE || includeStackTraceOption == InternalFlags.IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE ? this.sourceProvider.get(arrstackTraceElement) : this.sourceProvider.getFromClassNames(this.moduleSource.getModuleClassNames());
            }
            return new ElementSource(elementSource, object, this.moduleSource, arrstackTraceElement2);
        }

        private StackTraceElement[] getPartialCallStack(StackTraceElement[] arrstackTraceElement) {
            int n2 = 0;
            if (this.moduleSource != null) {
                n2 = this.moduleSource.getStackTraceSize();
            }
            int n3 = arrstackTraceElement.length - n2 - 1;
            StackTraceElement[] arrstackTraceElement2 = new StackTraceElement[n3];
            System.arraycopy(arrstackTraceElement, 1, arrstackTraceElement2, 0, n3);
            return arrstackTraceElement2;
        }

        public String toString() {
            return "Binder";
        }

    }

    private static class ModuleInfo {
        private final Binder binder;
        private final ModuleSource moduleSource;
        private final boolean skipScanning;

        private ModuleInfo(Binder binder, ModuleSource moduleSource, boolean bl) {
            this.binder = binder;
            this.moduleSource = moduleSource;
            this.skipScanning = bl;
        }
    }

    private static class ElementsAsModule
    implements Module {
        private final Iterable<? extends Element> elements;

        ElementsAsModule(Iterable<? extends Element> iterable) {
            this.elements = iterable;
        }

        @Override
        public void configure(Binder binder) {
            for (Element element : this.elements) {
                element.applyTo(binder);
            }
        }
    }

}

