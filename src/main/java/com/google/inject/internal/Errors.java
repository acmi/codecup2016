/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Equivalence;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.inject.ConfigurationException;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.Exceptions;
import com.google.inject.internal.InternalFlags;
import com.google.inject.internal.util.Classes;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.internal.util.StackTraceElements;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.ElementSource;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Message;
import com.google.inject.spi.ScopeBinding;
import com.google.inject.spi.TypeConverterBinding;
import com.google.inject.spi.TypeListener;
import com.google.inject.spi.TypeListenerBinding;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Provider;

public final class Errors
implements Serializable {
    private static final Logger logger = Logger.getLogger(Guice.class.getName());
    private static final Set<Dependency<?>> warnedDependencies = Collections.newSetFromMap(new ConcurrentHashMap());
    private final Errors root;
    private final Errors parent;
    private final Object source;
    private List<Message> errors;
    private static final String CONSTRUCTOR_RULES = "Classes must have either one (and only one) constructor annotated with @Inject or a zero-argument constructor that is not private.";
    private static final Collection<Converter<?>> converters = ImmutableList.of(new Converter<Class>(Class.class){

        @Override
        public String toString(Class class_) {
            return class_.getName();
        }
    }, new Converter<Member>(Member.class){

        @Override
        public String toString(Member member) {
            return Classes.toString(member);
        }
    }, new Converter<Key>(Key.class){

        @Override
        public String toString(Key key) {
            if (key.getAnnotationType() != null) {
                return key.getTypeLiteral() + " annotated with " + (key.getAnnotation() != null ? key.getAnnotation() : key.getAnnotationType());
            }
            return key.getTypeLiteral().toString();
        }
    });

    public Errors() {
        this.root = this;
        this.parent = null;
        this.source = SourceProvider.UNKNOWN_SOURCE;
    }

    public Errors(Object object) {
        this.root = this;
        this.parent = null;
        this.source = object;
    }

    private Errors(Errors errors, Object object) {
        this.root = errors.root;
        this.parent = errors;
        this.source = object;
    }

    public Errors withSource(Object object) {
        return object == this.source || object == SourceProvider.UNKNOWN_SOURCE ? this : new Errors(this, object);
    }

    public Errors missingImplementation(Key key) {
        return this.addMessage("No implementation for %s was bound.", key);
    }

    public Errors jitDisabled(Key key) {
        return this.addMessage("Explicit bindings are required and %s is not explicitly bound.", key);
    }

    public Errors jitDisabledInParent(Key<?> key) {
        return this.addMessage("Explicit bindings are required and %s would be bound in a parent injector.%nPlease add an explicit binding for it, either in the child or the parent.", key);
    }

    public Errors atInjectRequired(Class class_) {
        return this.addMessage("Explicit @Inject annotations are required on constructors, but %s has no constructors annotated with @Inject.", class_);
    }

    public Errors converterReturnedNull(String string, Object object, TypeLiteral<?> typeLiteral, TypeConverterBinding typeConverterBinding) {
        return this.addMessage("Received null converting '%s' (bound at %s) to %s%n using %s.", string, Errors.convert(object), typeLiteral, typeConverterBinding);
    }

    public Errors conversionTypeError(String string, Object object, TypeLiteral<?> typeLiteral, TypeConverterBinding typeConverterBinding, Object object2) {
        return this.addMessage("Type mismatch converting '%s' (bound at %s) to %s%n using %s.%n Converter returned %s.", string, Errors.convert(object), typeLiteral, typeConverterBinding, object2);
    }

    public Errors conversionError(String string, Object object, TypeLiteral<?> typeLiteral, TypeConverterBinding typeConverterBinding, RuntimeException runtimeException) {
        return this.errorInUserCode(runtimeException, "Error converting '%s' (bound at %s) to %s%n using %s.%n Reason: %s", string, Errors.convert(object), typeLiteral, typeConverterBinding, runtimeException);
    }

    public Errors ambiguousTypeConversion(String string, Object object, TypeLiteral<?> typeLiteral, TypeConverterBinding typeConverterBinding, TypeConverterBinding typeConverterBinding2) {
        return this.addMessage("Multiple converters can convert '%s' (bound at %s) to %s:%n %s and%n %s.%n Please adjust your type converter configuration to avoid overlapping matches.", string, Errors.convert(object), typeLiteral, typeConverterBinding, typeConverterBinding2);
    }

    public Errors bindingToProvider() {
        return this.addMessage("Binding to Provider is not allowed.", new Object[0]);
    }

    public Errors subtypeNotProvided(Class<? extends Provider<?>> class_, Class<?> class_2) {
        return this.addMessage("%s doesn't provide instances of %s.", class_, class_2);
    }

    public Errors notASubtype(Class<?> class_, Class<?> class_2) {
        return this.addMessage("%s doesn't extend %s.", class_, class_2);
    }

    public Errors recursiveImplementationType() {
        return this.addMessage("@ImplementedBy points to the same class it annotates.", new Object[0]);
    }

    public Errors recursiveProviderType() {
        return this.addMessage("@ProvidedBy points to the same class it annotates.", new Object[0]);
    }

    public Errors missingRuntimeRetention(Class<? extends Annotation> class_) {
        return this.addMessage(Errors.format("Please annotate %s with @Retention(RUNTIME).", class_), new Object[0]);
    }

    public Errors missingScopeAnnotation(Class<? extends Annotation> class_) {
        return this.addMessage(Errors.format("Please annotate %s with @ScopeAnnotation.", class_), new Object[0]);
    }

    public Errors optionalConstructor(Constructor constructor) {
        return this.addMessage("%s is annotated @Inject(optional=true), but constructors cannot be optional.", constructor);
    }

    public Errors cannotBindToGuiceType(String string) {
        return this.addMessage("Binding to core guice framework type is not allowed: %s.", string);
    }

    public Errors scopeNotFound(Class<? extends Annotation> class_) {
        return this.addMessage("No scope is bound to %s.", class_);
    }

    public Errors scopeAnnotationOnAbstractType(Class<? extends Annotation> class_, Class<?> class_2, Object object) {
        return this.addMessage("%s is annotated with %s, but scope annotations are not supported for abstract types.%n Bound at %s.", class_2, class_, Errors.convert(object));
    }

    public Errors misplacedBindingAnnotation(Member member, Annotation annotation) {
        return this.addMessage("%s is annotated with %s, but binding annotations should be applied to its parameters instead.", member, annotation);
    }

    public Errors missingConstructor(Class<?> class_) {
        return this.addMessage("Could not find a suitable constructor in %s. Classes must have either one (and only one) constructor annotated with @Inject or a zero-argument constructor that is not private.", class_);
    }

    public Errors tooManyConstructors(Class<?> class_) {
        return this.addMessage("%s has more than one constructor annotated with @Inject. Classes must have either one (and only one) constructor annotated with @Inject or a zero-argument constructor that is not private.", class_);
    }

    public Errors constructorNotDefinedByType(Constructor<?> constructor, TypeLiteral<?> typeLiteral) {
        return this.addMessage("%s does not define %s", typeLiteral, constructor);
    }

    public Errors duplicateScopes(ScopeBinding scopeBinding, Class<? extends Annotation> class_, Scope scope) {
        return this.addMessage("Scope %s is already bound to %s at %s.%n Cannot bind %s.", scopeBinding.getScope(), class_, scopeBinding.getSource(), scope);
    }

    public Errors voidProviderMethod() {
        return this.addMessage("Provider methods must return a value. Do not return void.", new Object[0]);
    }

    public Errors missingConstantValues() {
        return this.addMessage("Missing constant value. Please call to(...).", new Object[0]);
    }

    public Errors cannotInjectInnerClass(Class<?> class_) {
        return this.addMessage("Injecting into inner classes is not supported.  Please use a 'static' class (top-level or nested) instead of %s.", class_);
    }

    public Errors duplicateBindingAnnotations(Member member, Class<? extends Annotation> class_, Class<? extends Annotation> class_2) {
        return this.addMessage("%s has more than one annotation annotated with @BindingAnnotation: %s and %s", member, class_, class_2);
    }

    public Errors staticInjectionOnInterface(Class<?> class_) {
        return this.addMessage("%s is an interface, but interfaces have no static injection points.", class_);
    }

    public Errors cannotInjectFinalField(Field field) {
        return this.addMessage("Injected field %s cannot be final.", field);
    }

    public Errors cannotInjectAbstractMethod(Method method) {
        return this.addMessage("Injected method %s cannot be abstract.", method);
    }

    public Errors cannotInjectNonVoidMethod(Method method) {
        return this.addMessage("Injected method %s must return void.", method);
    }

    public Errors cannotInjectMethodWithTypeParameters(Method method) {
        return this.addMessage("Injected method %s cannot declare type parameters of its own.", method);
    }

    public Errors duplicateScopeAnnotations(Class<? extends Annotation> class_, Class<? extends Annotation> class_2) {
        return this.addMessage("More than one scope annotation was found: %s and %s.", class_, class_2);
    }

    public Errors recursiveBinding() {
        return this.addMessage("Binding points to itself.", new Object[0]);
    }

    public Errors bindingAlreadySet(Key<?> key, Object object) {
        return this.addMessage("A binding to %s was already configured at %s.", key, Errors.convert(object));
    }

    public Errors jitBindingAlreadySet(Key<?> key) {
        return this.addMessage("A just-in-time binding to %s was already configured on a parent injector.", key);
    }

    public Errors childBindingAlreadySet(Key<?> key, Set<Object> set) {
        Formatter formatter = new Formatter();
        for (Object object : set) {
            if (object == null) {
                formatter.format("%n    (bound by a just-in-time binding)", new Object[0]);
                continue;
            }
            formatter.format("%n    bound at %s", object);
        }
        Errors errors = this.addMessage("Unable to create binding for %s. It was already configured on one or more child injectors or private modules%s%n  If it was in a PrivateModule, did you forget to expose the binding?", key, formatter.out());
        return errors;
    }

    public Errors errorCheckingDuplicateBinding(Key<?> key, Object object, Throwable throwable) {
        return this.addMessage("A binding to %s was already configured at %s and an error was thrown while checking duplicate bindings.  Error: %s", key, Errors.convert(object), throwable);
    }

    public Errors errorInjectingMethod(Throwable throwable) {
        return this.errorInUserCode(throwable, "Error injecting method, %s", throwable);
    }

    public Errors errorNotifyingTypeListener(TypeListenerBinding typeListenerBinding, TypeLiteral<?> typeLiteral, Throwable throwable) {
        return this.errorInUserCode(throwable, "Error notifying TypeListener %s (bound at %s) of %s.%n Reason: %s", typeListenerBinding.getListener(), Errors.convert(typeListenerBinding.getSource()), typeLiteral, throwable);
    }

    public Errors errorInjectingConstructor(Throwable throwable) {
        return this.errorInUserCode(throwable, "Error injecting constructor, %s", throwable);
    }

    public Errors errorInProvider(Throwable throwable) {
        Throwable throwable2 = this.unwrap(throwable);
        return this.errorInUserCode(throwable2, "Error in custom provider, %s", throwable2);
    }

    public Errors errorInUserInjector(MembersInjector<?> membersInjector, TypeLiteral<?> typeLiteral, RuntimeException runtimeException) {
        return this.errorInUserCode(runtimeException, "Error injecting %s using %s.%n Reason: %s", typeLiteral, membersInjector, runtimeException);
    }

    public Errors errorNotifyingInjectionListener(InjectionListener<?> injectionListener, TypeLiteral<?> typeLiteral, RuntimeException runtimeException) {
        return this.errorInUserCode(runtimeException, "Error notifying InjectionListener %s of %s.%n Reason: %s", injectionListener, typeLiteral, runtimeException);
    }

    public Errors exposedButNotBound(Key<?> key) {
        return this.addMessage("Could not expose() %s, it must be explicitly bound.", key);
    }

    public Errors keyNotFullySpecified(TypeLiteral<?> typeLiteral) {
        return this.addMessage("%s cannot be used as a key; It is not fully specified.", typeLiteral);
    }

    public Errors errorEnhancingClass(Class<?> class_, Throwable throwable) {
        return this.errorInUserCode(throwable, "Unable to method intercept: %s", class_);
    }

    public static Collection<Message> getMessagesFromThrowable(Throwable throwable) {
        if (throwable instanceof ProvisionException) {
            return ((ProvisionException)throwable).getErrorMessages();
        }
        if (throwable instanceof ConfigurationException) {
            return ((ConfigurationException)throwable).getErrorMessages();
        }
        if (throwable instanceof CreationException) {
            return ((CreationException)throwable).getErrorMessages();
        }
        return ImmutableSet.of();
    }

    public /* varargs */ Errors errorInUserCode(Throwable throwable, String string, Object ... arrobject) {
        Collection<Message> collection = Errors.getMessagesFromThrowable(throwable);
        if (!collection.isEmpty()) {
            return this.merge(collection);
        }
        return this.addMessage(throwable, string, arrobject);
    }

    private Throwable unwrap(Throwable throwable) {
        if (throwable instanceof Exceptions.UnhandledCheckedUserException) {
            return throwable.getCause();
        }
        return throwable;
    }

    public Errors cannotInjectRawProvider() {
        return this.addMessage("Cannot inject a Provider that has no type parameter", new Object[0]);
    }

    public Errors cannotInjectRawMembersInjector() {
        return this.addMessage("Cannot inject a MembersInjector that has no type parameter", new Object[0]);
    }

    public Errors cannotInjectTypeLiteralOf(Type type) {
        return this.addMessage("Cannot inject a TypeLiteral of %s", type);
    }

    public Errors cannotInjectRawTypeLiteral() {
        return this.addMessage("Cannot inject a TypeLiteral that has no type parameter", new Object[0]);
    }

    public Errors cannotProxyClass(Class<?> class_) {
        return this.addMessage("Tried proxying %s to support a circular dependency, but it is not an interface.", class_);
    }

    public Errors circularDependenciesDisabled(Class<?> class_) {
        return this.addMessage("Found a circular dependency involving %s, and circular dependencies are disabled.", class_);
    }

    public void throwCreationExceptionIfErrorsExist() {
        if (!this.hasErrors()) {
            return;
        }
        throw new CreationException(this.getMessages());
    }

    public void throwConfigurationExceptionIfErrorsExist() {
        if (!this.hasErrors()) {
            return;
        }
        throw new ConfigurationException(this.getMessages());
    }

    public void throwProvisionExceptionIfErrorsExist() {
        if (!this.hasErrors()) {
            return;
        }
        throw new ProvisionException(this.getMessages());
    }

    private Message merge(Message message) {
        ArrayList<Object> arrayList = Lists.newArrayList();
        arrayList.addAll(this.getSources());
        arrayList.addAll(message.getSources());
        return new Message(arrayList, message.getMessage(), message.getCause());
    }

    public Errors merge(Collection<Message> collection) {
        for (Message message : collection) {
            this.addMessage(this.merge(message));
        }
        return this;
    }

    public Errors merge(Errors errors) {
        if (errors.root == this.root || errors.root.errors == null) {
            return this;
        }
        this.merge(errors.root.errors);
        return this;
    }

    public List<Object> getSources() {
        ArrayList<Object> arrayList = Lists.newArrayList();
        Errors errors = this;
        while (errors != null) {
            if (errors.source != SourceProvider.UNKNOWN_SOURCE) {
                arrayList.add(0, errors.source);
            }
            errors = errors.parent;
        }
        return arrayList;
    }

    public void throwIfNewErrors(int n2) throws ErrorsException {
        if (this.size() == n2) {
            return;
        }
        throw this.toException();
    }

    public ErrorsException toException() {
        return new ErrorsException(this);
    }

    public boolean hasErrors() {
        return this.root.errors != null;
    }

    public /* varargs */ Errors addMessage(String string, Object ... arrobject) {
        return this.addMessage(null, string, arrobject);
    }

    private /* varargs */ Errors addMessage(Throwable throwable, String string, Object ... arrobject) {
        String string2 = Errors.format(string, arrobject);
        this.addMessage(new Message(this.getSources(), string2, throwable));
        return this;
    }

    public Errors addMessage(Message message) {
        if (this.root.errors == null) {
            this.root.errors = Lists.newArrayList();
        }
        this.root.errors.add(message);
        return this;
    }

    public static /* varargs */ String format(String string, Object ... arrobject) {
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            arrobject[i2] = Errors.convert(arrobject[i2]);
        }
        return String.format(string, arrobject);
    }

    public List<Message> getMessages() {
        if (this.root.errors == null) {
            return ImmutableList.of();
        }
        return new Ordering<Message>(){

            @Override
            public int compare(Message message, Message message2) {
                return message.getSource().compareTo(message2.getSource());
            }
        }.sortedCopy(this.root.errors);
    }

    public static String format(String string, Collection<Message> collection) {
        Formatter formatter = new Formatter().format(string, new Object[0]).format(":%n%n", new Object[0]);
        int n2 = 1;
        boolean bl = Errors.getOnlyCause(collection) == null;
        HashMap<Equivalence.Wrapper<Throwable>, Integer> hashMap = Maps.newHashMap();
        for (Message message : collection) {
            Equivalence.Wrapper<Throwable> wrapper;
            int n3 = n2++;
            formatter.format("%s) %s%n", n3, message.getMessage());
            List<Object> list = message.getSources();
            for (int i2 = list.size() - 1; i2 >= 0; --i2) {
                wrapper = list.get(i2);
                Errors.formatSource(formatter, wrapper);
            }
            Throwable throwable = message.getCause();
            if (bl && throwable != null) {
                wrapper = ThrowableEquivalence.INSTANCE.wrap(throwable);
                if (!hashMap.containsKey(wrapper)) {
                    hashMap.put(wrapper, n3);
                    formatter.format("Caused by: %s", Throwables.getStackTraceAsString(throwable));
                } else {
                    int n4 = (Integer)hashMap.get(wrapper);
                    formatter.format("Caused by: %s (same stack trace as error #%s)", throwable.getClass().getName(), n4);
                }
            }
            formatter.format("%n", new Object[0]);
        }
        if (collection.size() == 1) {
            formatter.format("1 error", new Object[0]);
        } else {
            formatter.format("%s errors", collection.size());
        }
        return formatter.toString();
    }

    public <T> T checkForNull(T t2, Object object, Dependency<?> dependency) throws ErrorsException {
        Object object2;
        if (t2 != null || dependency.isNullable()) {
            return t2;
        }
        if (dependency.getInjectionPoint().getMember() instanceof Method && (object2 = (Method)dependency.getInjectionPoint().getMember()).isAnnotationPresent(Provides.class)) {
            switch (InternalFlags.getNullableProvidesOption()) {
                case ERROR: {
                    break;
                }
                case IGNORE: {
                    return t2;
                }
                case WARN: {
                    if (!warnedDependencies.add(dependency)) {
                        return t2;
                    }
                    logger.log(Level.WARNING, "Guice injected null into {0} (a {1}), please mark it @Nullable. Use -Dguice_check_nullable_provides_params=ERROR to turn this into an error.", new Object[]{Errors.formatParameter(dependency), Errors.convert(dependency.getKey())});
                    return null;
                }
            }
        }
        object2 = dependency.getParameterIndex() != -1 ? Errors.formatParameter(dependency) : StackTraceElements.forMember(dependency.getInjectionPoint().getMember());
        this.addMessage("null returned by binding at %s%n but %s is not @Nullable", object, object2);
        throw this.toException();
    }

    public static Throwable getOnlyCause(Collection<Message> collection) {
        Throwable throwable = null;
        for (Message message : collection) {
            Throwable throwable2 = message.getCause();
            if (throwable2 == null) continue;
            if (throwable != null && !ThrowableEquivalence.INSTANCE.equivalent(throwable, throwable2)) {
                return null;
            }
            throwable = throwable2;
        }
        return throwable;
    }

    public int size() {
        return this.root.errors == null ? 0 : this.root.errors.size();
    }

    public static Object convert(Object object) {
        ElementSource elementSource = null;
        if (object instanceof ElementSource) {
            elementSource = (ElementSource)object;
            object = elementSource.getDeclaringSource();
        }
        return Errors.convert(object, elementSource);
    }

    public static Object convert(Object object, ElementSource elementSource) {
        for (Converter converter : converters) {
            if (!converter.appliesTo(object)) continue;
            return Errors.appendModules(converter.convert(object), elementSource);
        }
        return Errors.appendModules(object, elementSource);
    }

    private static Object appendModules(Object object, ElementSource elementSource) {
        String string = Errors.moduleSourceString(elementSource);
        if (string.length() == 0) {
            return object;
        }
        return object + string;
    }

    private static String moduleSourceString(ElementSource elementSource) {
        if (elementSource == null) {
            return "";
        }
        ArrayList<String> arrayList = Lists.newArrayList(elementSource.getModuleClassNames());
        while (elementSource.getOriginalElementSource() != null) {
            elementSource = elementSource.getOriginalElementSource();
            arrayList.addAll(0, elementSource.getModuleClassNames());
        }
        if (arrayList.size() <= 1) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(" (via modules: ");
        for (int i2 = arrayList.size() - 1; i2 >= 0; --i2) {
            stringBuilder.append(arrayList.get(i2));
            if (i2 == 0) continue;
            stringBuilder.append(" -> ");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static void formatSource(Formatter formatter, Object object) {
        ElementSource elementSource = null;
        if (object instanceof ElementSource) {
            elementSource = (ElementSource)object;
            object = elementSource.getDeclaringSource();
        }
        Errors.formatSource(formatter, object, elementSource);
    }

    public static void formatSource(Formatter formatter, Object object, ElementSource elementSource) {
        String string = Errors.moduleSourceString(elementSource);
        if (object instanceof Dependency) {
            Dependency dependency = (Dependency)object;
            InjectionPoint injectionPoint = dependency.getInjectionPoint();
            if (injectionPoint != null) {
                Errors.formatInjectionPoint(formatter, dependency, injectionPoint, elementSource);
            } else {
                Errors.formatSource(formatter, dependency.getKey(), elementSource);
            }
        } else if (object instanceof InjectionPoint) {
            Errors.formatInjectionPoint(formatter, null, (InjectionPoint)object, elementSource);
        } else if (object instanceof Class) {
            formatter.format("  at %s%s%n", StackTraceElements.forType((Class)object), string);
        } else if (object instanceof Member) {
            formatter.format("  at %s%s%n", StackTraceElements.forMember((Member)object), string);
        } else if (object instanceof TypeLiteral) {
            formatter.format("  while locating %s%s%n", object, string);
        } else if (object instanceof Key) {
            Key key = (Key)object;
            formatter.format("  while locating %s%n", Errors.convert(key, elementSource));
        } else if (object instanceof Thread) {
            formatter.format("  in thread %s%n", object);
        } else {
            formatter.format("  at %s%s%n", object, string);
        }
    }

    public static void formatInjectionPoint(Formatter formatter, Dependency<?> dependency, InjectionPoint injectionPoint, ElementSource elementSource) {
        Member member = injectionPoint.getMember();
        Class<? extends Member> class_ = Classes.memberType(member);
        if (class_ == Field.class) {
            dependency = injectionPoint.getDependencies().get(0);
            formatter.format("  while locating %s%n", Errors.convert(dependency.getKey(), elementSource));
            formatter.format("    for field at %s%n", StackTraceElements.forMember(member));
        } else if (dependency != null) {
            formatter.format("  while locating %s%n", Errors.convert(dependency.getKey(), elementSource));
            formatter.format("    for %s%n", Errors.formatParameter(dependency));
        } else {
            Errors.formatSource(formatter, injectionPoint.getMember());
        }
    }

    private static String formatParameter(Dependency<?> dependency) {
        int n2 = dependency.getParameterIndex() + 1;
        return String.format("the %s%s parameter of %s", n2, Errors.getOrdinalSuffix(n2), StackTraceElements.forMember(dependency.getInjectionPoint().getMember()));
    }

    private static String getOrdinalSuffix(int n2) {
        Preconditions.checkArgument(n2 >= 0);
        if (n2 / 10 % 10 == 1) {
            return "th";
        }
        switch (n2 % 10) {
            case 1: {
                return "st";
            }
            case 2: {
                return "nd";
            }
            case 3: {
                return "rd";
            }
        }
        return "th";
    }

    static class ThrowableEquivalence
    extends Equivalence<Throwable> {
        static final ThrowableEquivalence INSTANCE = new ThrowableEquivalence();

        ThrowableEquivalence() {
        }

        @Override
        protected boolean doEquivalent(Throwable throwable, Throwable throwable2) {
            return throwable.getClass().equals(throwable2.getClass()) && Objects.equal(throwable.getMessage(), throwable2.getMessage()) && Arrays.equals(throwable.getStackTrace(), throwable2.getStackTrace()) && this.equivalent(throwable.getCause(), throwable2.getCause());
        }

        @Override
        protected int doHash(Throwable throwable) {
            return Objects.hashCode(throwable.getClass().hashCode(), throwable.getMessage(), this.hash(throwable.getCause()));
        }
    }

    private static abstract class Converter<T> {
        final Class<T> type;

        Converter(Class<T> class_) {
            this.type = class_;
        }

        boolean appliesTo(Object object) {
            return object != null && this.type.isAssignableFrom(object.getClass());
        }

        String convert(Object object) {
            return this.toString(this.type.cast(object));
        }

        abstract String toString(T var1);
    }

}

