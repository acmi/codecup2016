/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.MoreTypes;
import com.google.inject.internal.Nullability;
import com.google.inject.internal.util.Classes;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Message;
import com.google.inject.spi.Toolable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class InjectionPoint {
    private static final Logger logger = Logger.getLogger(InjectionPoint.class.getName());
    private final boolean optional;
    private final Member member;
    private final TypeLiteral<?> declaringType;
    private final ImmutableList<Dependency<?>> dependencies;

    InjectionPoint(TypeLiteral<?> typeLiteral, Method method, boolean bl) {
        this.member = method;
        this.declaringType = typeLiteral;
        this.optional = bl;
        this.dependencies = this.forMember(method, typeLiteral, method.getParameterAnnotations());
    }

    InjectionPoint(TypeLiteral<?> typeLiteral, Constructor<?> constructor) {
        this.member = constructor;
        this.declaringType = typeLiteral;
        this.optional = false;
        this.dependencies = this.forMember(constructor, typeLiteral, constructor.getParameterAnnotations());
    }

    InjectionPoint(TypeLiteral<?> typeLiteral, Field field, boolean bl) {
        this.member = field;
        this.declaringType = typeLiteral;
        this.optional = bl;
        Annotation[] arrannotation = field.getAnnotations();
        Errors errors = new Errors(field);
        Key key = null;
        try {
            key = Annotations.getKey(typeLiteral.getFieldType(field), field, arrannotation, errors);
        }
        catch (ConfigurationException configurationException) {
            errors.merge(configurationException.getErrorMessages());
        }
        catch (ErrorsException errorsException) {
            errors.merge(errorsException.getErrors());
        }
        errors.throwConfigurationExceptionIfErrorsExist();
        this.dependencies = ImmutableList.of(this.newDependency(key, Nullability.allowsNull(arrannotation), -1));
    }

    private ImmutableList<Dependency<?>> forMember(Member member, TypeLiteral<?> typeLiteral, Annotation[][] arrannotation) {
        Errors errors = new Errors(member);
        Iterator iterator = Arrays.asList(arrannotation).iterator();
        ArrayList arrayList = Lists.newArrayList();
        int n2 = 0;
        for (TypeLiteral typeLiteral2 : typeLiteral.getParameterTypes(member)) {
            try {
                Annotation[] arrannotation2 = (Annotation[])iterator.next();
                Key key = Annotations.getKey(typeLiteral2, member, arrannotation2, errors);
                arrayList.add(this.newDependency(key, Nullability.allowsNull(arrannotation2), n2));
                ++n2;
            }
            catch (ConfigurationException configurationException) {
                errors.merge(configurationException.getErrorMessages());
            }
            catch (ErrorsException errorsException) {
                errors.merge(errorsException.getErrors());
            }
        }
        errors.throwConfigurationExceptionIfErrorsExist();
        return ImmutableList.copyOf(arrayList);
    }

    private <T> Dependency<T> newDependency(Key<T> key, boolean bl, int n2) {
        return new Dependency<T>(this, key, bl, n2);
    }

    public Member getMember() {
        return this.member;
    }

    public List<Dependency<?>> getDependencies() {
        return this.dependencies;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public boolean isToolable() {
        return ((AnnotatedElement)((Object)this.member)).isAnnotationPresent(Toolable.class);
    }

    public TypeLiteral<?> getDeclaringType() {
        return this.declaringType;
    }

    public boolean equals(Object object) {
        return object instanceof InjectionPoint && this.member.equals(((InjectionPoint)object).member) && this.declaringType.equals(((InjectionPoint)object).declaringType);
    }

    public int hashCode() {
        return this.member.hashCode() ^ this.declaringType.hashCode();
    }

    public String toString() {
        return Classes.toString(this.member);
    }

    public static <T> InjectionPoint forConstructor(Constructor<T> constructor) {
        return new InjectionPoint(TypeLiteral.get(constructor.getDeclaringClass()), constructor);
    }

    public static <T> InjectionPoint forConstructor(Constructor<T> constructor, TypeLiteral<? extends T> typeLiteral) {
        if (typeLiteral.getRawType() != constructor.getDeclaringClass()) {
            new Errors(typeLiteral).constructorNotDefinedByType(constructor, typeLiteral).throwConfigurationExceptionIfErrorsExist();
        }
        return new InjectionPoint(typeLiteral, constructor);
    }

    public static InjectionPoint forConstructorOf(TypeLiteral<?> typeLiteral) {
        Class class_ = MoreTypes.getRawType(typeLiteral.getType());
        Errors errors = new Errors(class_);
        Constructor constructor = null;
        for (Constructor constructor2 : class_.getDeclaredConstructors()) {
            boolean bl;
            Inject inject = constructor2.getAnnotation(Inject.class);
            if (inject == null) {
                javax.inject.Inject inject2 = constructor2.getAnnotation(javax.inject.Inject.class);
                if (inject2 == null) continue;
                bl = false;
            } else {
                bl = inject.optional();
            }
            if (bl) {
                errors.optionalConstructor(constructor2);
            }
            if (constructor != null) {
                errors.tooManyConstructors(class_);
            }
            constructor = constructor2;
            InjectionPoint.checkForMisplacedBindingAnnotations(constructor, errors);
        }
        errors.throwConfigurationExceptionIfErrorsExist();
        if (constructor != null) {
            return new InjectionPoint(typeLiteral, constructor);
        }
        try {
            Constructor constructor3 = class_.getDeclaredConstructor(new Class[0]);
            if (Modifier.isPrivate(constructor3.getModifiers()) && !Modifier.isPrivate(class_.getModifiers())) {
                errors.missingConstructor(class_);
                throw new ConfigurationException(errors.getMessages());
            }
            InjectionPoint.checkForMisplacedBindingAnnotations(constructor3, errors);
            return new InjectionPoint(typeLiteral, constructor3);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            errors.missingConstructor(class_);
            throw new ConfigurationException(errors.getMessages());
        }
    }

    public static InjectionPoint forConstructorOf(Class<?> class_) {
        return InjectionPoint.forConstructorOf(TypeLiteral.get(class_));
    }

    public static <T> InjectionPoint forMethod(Method method, TypeLiteral<T> typeLiteral) {
        return new InjectionPoint(typeLiteral, method, false);
    }

    public static Set<InjectionPoint> forStaticMethodsAndFields(TypeLiteral<?> typeLiteral) {
        Set<InjectionPoint> set;
        Errors errors = new Errors();
        if (typeLiteral.getRawType().isInterface()) {
            errors.staticInjectionOnInterface(typeLiteral.getRawType());
            set = null;
        } else {
            set = InjectionPoint.getInjectionPoints(typeLiteral, true, errors);
        }
        if (errors.hasErrors()) {
            throw new ConfigurationException(errors.getMessages()).withPartialValue(set);
        }
        return set;
    }

    public static Set<InjectionPoint> forStaticMethodsAndFields(Class<?> class_) {
        return InjectionPoint.forStaticMethodsAndFields(TypeLiteral.get(class_));
    }

    public static Set<InjectionPoint> forInstanceMethodsAndFields(TypeLiteral<?> typeLiteral) {
        Errors errors = new Errors();
        Set<InjectionPoint> set = InjectionPoint.getInjectionPoints(typeLiteral, false, errors);
        if (errors.hasErrors()) {
            throw new ConfigurationException(errors.getMessages()).withPartialValue(set);
        }
        return set;
    }

    public static Set<InjectionPoint> forInstanceMethodsAndFields(Class<?> class_) {
        return InjectionPoint.forInstanceMethodsAndFields(TypeLiteral.get(class_));
    }

    private static boolean checkForMisplacedBindingAnnotations(Member member, Errors errors) {
        Annotation annotation = Annotations.findBindingAnnotation(errors, member, ((AnnotatedElement)((Object)member)).getAnnotations());
        if (annotation == null) {
            return false;
        }
        if (member instanceof Method) {
            try {
                if (member.getDeclaringClass().getDeclaredField(member.getName()) != null) {
                    return false;
                }
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
        }
        errors.misplacedBindingAnnotation(member, annotation);
        return true;
    }

    static Annotation getAtInject(AnnotatedElement annotatedElement) {
        Annotation annotation = annotatedElement.getAnnotation(javax.inject.Inject.class);
        return annotation == null ? annotatedElement.getAnnotation(Inject.class) : annotation;
    }

    private static Set<InjectionPoint> getInjectionPoints(TypeLiteral<?> typeLiteral, boolean bl, Errors errors) {
        Object object;
        InjectableMembers injectableMembers = new InjectableMembers();
        OverrideIndex overrideIndex = null;
        List list = InjectionPoint.hierarchyFor(typeLiteral);
        for (int i2 = n2 = list.size() - 1; i2 >= 0; --i2) {
            Annotation annotation;
            int n2;
            if (overrideIndex != null && i2 < n2) {
                overrideIndex.position = i2 == 0 ? Position.BOTTOM : Position.MIDDLE;
            }
            object = list.get(i2);
            for (Field field : object.getRawType().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) != bl || (annotation = InjectionPoint.getAtInject(field)) == null) continue;
                InjectableField injectableField = new InjectableField(object, field, annotation);
                if (injectableField.jsr330 && Modifier.isFinal(field.getModifiers())) {
                    errors.cannotInjectFinalField(field);
                }
                injectableMembers.add(injectableField);
            }
            for (Field field : object.getRawType().getDeclaredMethods()) {
                boolean bl2;
                if (!InjectionPoint.isEligibleForInjection((Method)((Object)field), bl)) continue;
                annotation = InjectionPoint.getAtInject(field);
                if (annotation != null) {
                    InjectableMethod injectableMethod = new InjectableMethod(object, (Method)((Object)field), annotation);
                    if (InjectionPoint.checkForMisplacedBindingAnnotations(field, errors) || !InjectionPoint.isValidMethod(injectableMethod, errors)) {
                        boolean bl3;
                        if (overrideIndex == null || !(bl3 = overrideIndex.removeIfOverriddenBy((Method)((Object)field), false, injectableMethod))) continue;
                        logger.log(Level.WARNING, "Method: {0} is not a valid injectable method (because it either has misplaced binding annotations or specifies type parameters) but is overriding a method that is valid. Because it is not valid, the method will not be injected. To fix this, make the method a valid injectable method.", field);
                        continue;
                    }
                    if (bl) {
                        injectableMembers.add(injectableMethod);
                        continue;
                    }
                    if (overrideIndex == null) {
                        overrideIndex = new OverrideIndex(injectableMembers);
                    } else {
                        overrideIndex.removeIfOverriddenBy((Method)((Object)field), true, injectableMethod);
                    }
                    overrideIndex.add(injectableMethod);
                    continue;
                }
                if (overrideIndex == null || !(bl2 = overrideIndex.removeIfOverriddenBy((Method)((Object)field), false, null))) continue;
                logger.log(Level.WARNING, "Method: {0} is not annotated with @Inject but is overriding a method that is annotated with @javax.inject.Inject.  Because it is not annotated with @Inject, the method will not be injected. To fix this, annotate the method with @Inject.", field);
            }
        }
        if (injectableMembers.isEmpty()) {
            return Collections.emptySet();
        }
        ImmutableSet.Builder builder = ImmutableSet.builder();
        object = injectableMembers.head;
        while (object != null) {
            block14 : {
                try {
                    builder.add(object.toInjectionPoint());
                }
                catch (ConfigurationException configurationException) {
                    if (object.optional) break block14;
                    errors.merge(configurationException.getErrorMessages());
                }
            }
            object = object.next;
        }
        return builder.build();
    }

    private static boolean isEligibleForInjection(Method method, boolean bl) {
        return Modifier.isStatic(method.getModifiers()) == bl && !method.isBridge() && !method.isSynthetic();
    }

    private static boolean isValidMethod(InjectableMethod injectableMethod, Errors errors) {
        boolean bl = true;
        if (injectableMethod.jsr330) {
            Method method = injectableMethod.method;
            if (Modifier.isAbstract(method.getModifiers())) {
                errors.cannotInjectAbstractMethod(method);
                bl = false;
            }
            if (method.getTypeParameters().length > 0) {
                errors.cannotInjectMethodWithTypeParameters(method);
                bl = false;
            }
        }
        return bl;
    }

    private static List<TypeLiteral<?>> hierarchyFor(TypeLiteral<?> typeLiteral) {
        ArrayList arrayList = new ArrayList();
        TypeLiteral typeLiteral2 = typeLiteral;
        while (typeLiteral2.getRawType() != Object.class) {
            arrayList.add(typeLiteral2);
            typeLiteral2 = typeLiteral2.getSupertype(typeLiteral2.getRawType().getSuperclass());
        }
        return arrayList;
    }

    private static boolean overrides(Method method, Method method2) {
        int n2 = method2.getModifiers();
        if (Modifier.isPublic(n2) || Modifier.isProtected(n2)) {
            return true;
        }
        if (Modifier.isPrivate(n2)) {
            return false;
        }
        return method.getDeclaringClass().getPackage().equals(method2.getDeclaringClass().getPackage());
    }

    static class Signature {
        final String name;
        final Class[] parameterTypes;
        final int hash;

        Signature(Method method) {
            this.name = method.getName();
            this.parameterTypes = method.getParameterTypes();
            int n2 = this.name.hashCode();
            n2 = n2 * 31 + this.parameterTypes.length;
            for (Class class_ : this.parameterTypes) {
                n2 = n2 * 31 + class_.hashCode();
            }
            this.hash = n2;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object object) {
            if (!(object instanceof Signature)) {
                return false;
            }
            Signature signature = (Signature)object;
            if (!this.name.equals(signature.name)) {
                return false;
            }
            if (this.parameterTypes.length != signature.parameterTypes.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.parameterTypes.length; ++i2) {
                if (this.parameterTypes[i2] == signature.parameterTypes[i2]) continue;
                return false;
            }
            return true;
        }
    }

    static class OverrideIndex {
        final InjectableMembers injectableMembers;
        Map<Signature, List<InjectableMethod>> bySignature;
        Position position = Position.TOP;
        Method lastMethod;
        Signature lastSignature;

        OverrideIndex(InjectableMembers injectableMembers) {
            this.injectableMembers = injectableMembers;
        }

        boolean removeIfOverriddenBy(Method method, boolean bl, InjectableMethod injectableMethod) {
            Object object;
            Object object2;
            if (this.position == Position.TOP) {
                return false;
            }
            if (this.bySignature == null) {
                this.bySignature = new HashMap<Signature, List<InjectableMethod>>();
                object = this.injectableMembers.head;
                while (object != null) {
                    if (object instanceof InjectableMethod && !(object2 = (InjectableMethod)object).isFinal()) {
                        ArrayList<Object> arrayList = new ArrayList<Object>();
                        arrayList.add(object2);
                        this.bySignature.put(new Signature(object2.method), arrayList);
                    }
                    object = object.next;
                }
            }
            this.lastMethod = method;
            this.lastSignature = new Signature(method);
            object = this.lastSignature;
            object2 = this.bySignature.get(object);
            boolean bl2 = false;
            if (object2 != null) {
                Iterator iterator = object2.iterator();
                while (iterator.hasNext()) {
                    boolean bl3;
                    InjectableMethod injectableMethod2 = (InjectableMethod)iterator.next();
                    if (!InjectionPoint.overrides(method, injectableMethod2.method)) continue;
                    boolean bl4 = bl3 = !injectableMethod2.jsr330 || injectableMethod2.overrodeGuiceInject;
                    if (injectableMethod != null) {
                        injectableMethod.overrodeGuiceInject = bl3;
                    }
                    if (!bl && bl3) continue;
                    bl2 = true;
                    iterator.remove();
                    this.injectableMembers.remove(injectableMethod2);
                }
            }
            return bl2;
        }

        void add(InjectableMethod injectableMethod) {
            this.injectableMembers.add(injectableMethod);
            if (this.position == Position.BOTTOM || injectableMethod.isFinal()) {
                return;
            }
            if (this.bySignature != null) {
                Signature signature = injectableMethod.method == this.lastMethod ? this.lastSignature : new Signature(injectableMethod.method);
                List<InjectableMethod> list = this.bySignature.get(signature);
                if (list == null) {
                    list = new ArrayList<InjectableMethod>();
                    this.bySignature.put(signature, list);
                }
                list.add(injectableMethod);
            }
        }
    }

    static enum Position {
        TOP,
        MIDDLE,
        BOTTOM;
        

        private Position() {
        }
    }

    static class InjectableMembers {
        InjectableMember head;
        InjectableMember tail;

        InjectableMembers() {
        }

        void add(InjectableMember injectableMember) {
            if (this.head == null) {
                this.head = this.tail = injectableMember;
            } else {
                injectableMember.previous = this.tail;
                this.tail.next = injectableMember;
                this.tail = injectableMember;
            }
        }

        void remove(InjectableMember injectableMember) {
            if (injectableMember.previous != null) {
                injectableMember.previous.next = injectableMember.next;
            }
            if (injectableMember.next != null) {
                injectableMember.next.previous = injectableMember.previous;
            }
            if (this.head == injectableMember) {
                this.head = injectableMember.next;
            }
            if (this.tail == injectableMember) {
                this.tail = injectableMember.previous;
            }
        }

        boolean isEmpty() {
            return this.head == null;
        }
    }

    static class InjectableMethod
    extends InjectableMember {
        final Method method;
        boolean overrodeGuiceInject;

        InjectableMethod(TypeLiteral<?> typeLiteral, Method method, Annotation annotation) {
            super(typeLiteral, annotation);
            this.method = method;
        }

        @Override
        InjectionPoint toInjectionPoint() {
            return new InjectionPoint(this.declaringType, this.method, this.optional);
        }

        public boolean isFinal() {
            return Modifier.isFinal(this.method.getModifiers());
        }
    }

    static class InjectableField
    extends InjectableMember {
        final Field field;

        InjectableField(TypeLiteral<?> typeLiteral, Field field, Annotation annotation) {
            super(typeLiteral, annotation);
            this.field = field;
        }

        @Override
        InjectionPoint toInjectionPoint() {
            return new InjectionPoint(this.declaringType, this.field, this.optional);
        }
    }

    static abstract class InjectableMember {
        final TypeLiteral<?> declaringType;
        final boolean optional;
        final boolean jsr330;
        InjectableMember previous;
        InjectableMember next;

        InjectableMember(TypeLiteral<?> typeLiteral, Annotation annotation) {
            this.declaringType = typeLiteral;
            if (annotation.annotationType() == javax.inject.Inject.class) {
                this.optional = false;
                this.jsr330 = true;
                return;
            }
            this.jsr330 = false;
            this.optional = ((Inject)annotation).optional();
        }

        abstract InjectionPoint toInjectionPoint();
    }

}

