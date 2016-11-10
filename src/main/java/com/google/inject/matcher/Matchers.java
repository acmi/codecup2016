/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.matcher;

import com.google.common.base.Preconditions;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class Matchers {
    private static final Matcher<Object> ANY = new Any();

    private Matchers() {
    }

    public static Matcher<Object> any() {
        return ANY;
    }

    public static <T> Matcher<T> not(Matcher<? super T> matcher) {
        return new Not(matcher);
    }

    private static void checkForRuntimeRetention(Class<? extends Annotation> class_) {
        Retention retention = class_.getAnnotation(Retention.class);
        Preconditions.checkArgument(retention != null && retention.value() == RetentionPolicy.RUNTIME, "Annotation %s is missing RUNTIME retention", class_.getSimpleName());
    }

    public static Matcher<AnnotatedElement> annotatedWith(Class<? extends Annotation> class_) {
        return new AnnotatedWithType(class_);
    }

    public static Matcher<AnnotatedElement> annotatedWith(Annotation annotation) {
        return new AnnotatedWith(annotation);
    }

    public static Matcher<Class> subclassesOf(Class<?> class_) {
        return new SubclassesOf(class_);
    }

    public static Matcher<Object> only(Object object) {
        return new Only(object);
    }

    public static Matcher<Object> identicalTo(Object object) {
        return new IdenticalTo(object);
    }

    public static Matcher<Class> inPackage(Package package_) {
        return new InPackage(package_);
    }

    public static Matcher<Class> inSubpackage(String string) {
        return new InSubpackage(string);
    }

    public static Matcher<Method> returns(Matcher<? super Class<?>> matcher) {
        return new Returns(matcher);
    }

    private static class Returns
    extends AbstractMatcher<Method>
    implements Serializable {
        private final Matcher<? super Class<?>> returnType;
        private static final long serialVersionUID = 0;

        public Returns(Matcher<? super Class<?>> matcher) {
            this.returnType = Preconditions.checkNotNull(matcher, "return type matcher");
        }

        @Override
        public boolean matches(Method method) {
            return this.returnType.matches(method.getReturnType());
        }

        public boolean equals(Object object) {
            return object instanceof Returns && ((Returns)object).returnType.equals(this.returnType);
        }

        public int hashCode() {
            return 37 * this.returnType.hashCode();
        }

        public String toString() {
            return "returns(" + this.returnType + ")";
        }
    }

    private static class InSubpackage
    extends AbstractMatcher<Class>
    implements Serializable {
        private final String targetPackageName;
        private static final long serialVersionUID = 0;

        public InSubpackage(String string) {
            this.targetPackageName = string;
        }

        @Override
        public boolean matches(Class class_) {
            String string = class_.getPackage().getName();
            return string.equals(this.targetPackageName) || string.startsWith(this.targetPackageName + ".");
        }

        public boolean equals(Object object) {
            return object instanceof InSubpackage && ((InSubpackage)object).targetPackageName.equals(this.targetPackageName);
        }

        public int hashCode() {
            return 37 * this.targetPackageName.hashCode();
        }

        public String toString() {
            return "inSubpackage(" + this.targetPackageName + ")";
        }
    }

    private static class InPackage
    extends AbstractMatcher<Class>
    implements Serializable {
        private final transient Package targetPackage;
        private final String packageName;
        private static final long serialVersionUID = 0;

        public InPackage(Package package_) {
            this.targetPackage = Preconditions.checkNotNull(package_, "package");
            this.packageName = package_.getName();
        }

        @Override
        public boolean matches(Class class_) {
            return class_.getPackage().equals(this.targetPackage);
        }

        public boolean equals(Object object) {
            return object instanceof InPackage && ((InPackage)object).targetPackage.equals(this.targetPackage);
        }

        public int hashCode() {
            return 37 * this.targetPackage.hashCode();
        }

        public String toString() {
            return "inPackage(" + this.targetPackage.getName() + ")";
        }

        public Object readResolve() {
            return Matchers.inPackage(Package.getPackage(this.packageName));
        }
    }

    private static class IdenticalTo
    extends AbstractMatcher<Object>
    implements Serializable {
        private final Object value;
        private static final long serialVersionUID = 0;

        public IdenticalTo(Object object) {
            this.value = Preconditions.checkNotNull(object, "value");
        }

        @Override
        public boolean matches(Object object) {
            return this.value == object;
        }

        public boolean equals(Object object) {
            return object instanceof IdenticalTo && ((IdenticalTo)object).value == this.value;
        }

        public int hashCode() {
            return 37 * System.identityHashCode(this.value);
        }

        public String toString() {
            return "identicalTo(" + this.value + ")";
        }
    }

    private static class Only
    extends AbstractMatcher<Object>
    implements Serializable {
        private final Object value;
        private static final long serialVersionUID = 0;

        public Only(Object object) {
            this.value = Preconditions.checkNotNull(object, "value");
        }

        @Override
        public boolean matches(Object object) {
            return this.value.equals(object);
        }

        public boolean equals(Object object) {
            return object instanceof Only && ((Only)object).value.equals(this.value);
        }

        public int hashCode() {
            return 37 * this.value.hashCode();
        }

        public String toString() {
            return "only(" + this.value + ")";
        }
    }

    private static class SubclassesOf
    extends AbstractMatcher<Class>
    implements Serializable {
        private final Class<?> superclass;
        private static final long serialVersionUID = 0;

        public SubclassesOf(Class<?> class_) {
            this.superclass = Preconditions.checkNotNull(class_, "superclass");
        }

        @Override
        public boolean matches(Class class_) {
            return this.superclass.isAssignableFrom(class_);
        }

        public boolean equals(Object object) {
            return object instanceof SubclassesOf && ((SubclassesOf)object).superclass.equals(this.superclass);
        }

        public int hashCode() {
            return 37 * this.superclass.hashCode();
        }

        public String toString() {
            return "subclassesOf(" + this.superclass.getSimpleName() + ".class)";
        }
    }

    private static class AnnotatedWith
    extends AbstractMatcher<AnnotatedElement>
    implements Serializable {
        private final Annotation annotation;
        private static final long serialVersionUID = 0;

        public AnnotatedWith(Annotation annotation) {
            this.annotation = Preconditions.checkNotNull(annotation, "annotation");
            Matchers.checkForRuntimeRetention(annotation.annotationType());
        }

        @Override
        public boolean matches(AnnotatedElement annotatedElement) {
            Annotation annotation = annotatedElement.getAnnotation(this.annotation.annotationType());
            return annotation != null && this.annotation.equals(annotation);
        }

        public boolean equals(Object object) {
            return object instanceof AnnotatedWith && ((AnnotatedWith)object).annotation.equals(this.annotation);
        }

        public int hashCode() {
            return 37 * this.annotation.hashCode();
        }

        public String toString() {
            return "annotatedWith(" + this.annotation + ")";
        }
    }

    private static class AnnotatedWithType
    extends AbstractMatcher<AnnotatedElement>
    implements Serializable {
        private final Class<? extends Annotation> annotationType;
        private static final long serialVersionUID = 0;

        public AnnotatedWithType(Class<? extends Annotation> class_) {
            this.annotationType = Preconditions.checkNotNull(class_, "annotation type");
            Matchers.checkForRuntimeRetention(class_);
        }

        @Override
        public boolean matches(AnnotatedElement annotatedElement) {
            return annotatedElement.isAnnotationPresent(this.annotationType);
        }

        public boolean equals(Object object) {
            return object instanceof AnnotatedWithType && ((AnnotatedWithType)object).annotationType.equals(this.annotationType);
        }

        public int hashCode() {
            return 37 * this.annotationType.hashCode();
        }

        public String toString() {
            return "annotatedWith(" + this.annotationType.getSimpleName() + ".class)";
        }
    }

    private static class Not<T>
    extends AbstractMatcher<T>
    implements Serializable {
        final Matcher<? super T> delegate;
        private static final long serialVersionUID = 0;

        private Not(Matcher<? super T> matcher) {
            this.delegate = Preconditions.checkNotNull(matcher, "delegate");
        }

        @Override
        public boolean matches(T t2) {
            return !this.delegate.matches(t2);
        }

        public boolean equals(Object object) {
            return object instanceof Not && ((Not)object).delegate.equals(this.delegate);
        }

        public int hashCode() {
            return - this.delegate.hashCode();
        }

        public String toString() {
            return "not(" + this.delegate + ")";
        }
    }

    private static class Any
    extends AbstractMatcher<Object>
    implements Serializable {
        private static final long serialVersionUID = 0;

        private Any() {
        }

        @Override
        public boolean matches(Object object) {
            return true;
        }

        public String toString() {
            return "any()";
        }

        public Object readResolve() {
            return Matchers.any();
        }
    }

}

