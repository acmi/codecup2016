/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.MoreTypes;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class Key<T> {
    private final AnnotationStrategy annotationStrategy;
    private final TypeLiteral<T> typeLiteral;
    private final int hashCode;
    private String toString;

    protected Key(Class<? extends Annotation> class_) {
        this.annotationStrategy = Key.strategyFor(class_);
        this.typeLiteral = MoreTypes.canonicalizeForKey(TypeLiteral.fromSuperclassTypeParameter(this.getClass()));
        this.hashCode = this.computeHashCode();
    }

    protected Key(Annotation annotation) {
        this.annotationStrategy = Key.strategyFor(annotation);
        this.typeLiteral = MoreTypes.canonicalizeForKey(TypeLiteral.fromSuperclassTypeParameter(this.getClass()));
        this.hashCode = this.computeHashCode();
    }

    protected Key() {
        this.annotationStrategy = NullAnnotationStrategy.INSTANCE;
        this.typeLiteral = MoreTypes.canonicalizeForKey(TypeLiteral.fromSuperclassTypeParameter(this.getClass()));
        this.hashCode = this.computeHashCode();
    }

    private Key(Type type, AnnotationStrategy annotationStrategy) {
        this.annotationStrategy = annotationStrategy;
        this.typeLiteral = MoreTypes.canonicalizeForKey(TypeLiteral.get(type));
        this.hashCode = this.computeHashCode();
    }

    private Key(TypeLiteral<T> typeLiteral, AnnotationStrategy annotationStrategy) {
        this.annotationStrategy = annotationStrategy;
        this.typeLiteral = MoreTypes.canonicalizeForKey(typeLiteral);
        this.hashCode = this.computeHashCode();
    }

    private int computeHashCode() {
        return this.typeLiteral.hashCode() * 31 + this.annotationStrategy.hashCode();
    }

    public final TypeLiteral<T> getTypeLiteral() {
        return this.typeLiteral;
    }

    public final Class<? extends Annotation> getAnnotationType() {
        return this.annotationStrategy.getAnnotationType();
    }

    public final Annotation getAnnotation() {
        return this.annotationStrategy.getAnnotation();
    }

    boolean hasAnnotationType() {
        return this.annotationStrategy.getAnnotationType() != null;
    }

    String getAnnotationName() {
        Annotation annotation = this.annotationStrategy.getAnnotation();
        if (annotation != null) {
            return annotation.toString();
        }
        return this.annotationStrategy.getAnnotationType().toString();
    }

    Class<? super T> getRawType() {
        return this.typeLiteral.getRawType();
    }

    Key<Provider<T>> providerKey() {
        return this.ofType(this.typeLiteral.providerType());
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Key)) {
            return false;
        }
        Key key = (Key)object;
        return this.annotationStrategy.equals(key.annotationStrategy) && this.typeLiteral.equals(key.typeLiteral);
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final String toString() {
        String string = this.toString;
        if (string == null) {
            this.toString = string = "Key[type=" + this.typeLiteral + ", annotation=" + this.annotationStrategy + "]";
        }
        return string;
    }

    static <T> Key<T> get(Class<T> class_, AnnotationStrategy annotationStrategy) {
        return new Key<T>(class_, annotationStrategy);
    }

    public static <T> Key<T> get(Class<T> class_) {
        return new Key<T>(class_, (AnnotationStrategy)NullAnnotationStrategy.INSTANCE);
    }

    public static <T> Key<T> get(Class<T> class_, Class<? extends Annotation> class_2) {
        return new Key<T>(class_, Key.strategyFor(class_2));
    }

    public static <T> Key<T> get(Class<T> class_, Annotation annotation) {
        return new Key<T>(class_, Key.strategyFor(annotation));
    }

    public static Key<?> get(Type type) {
        return new Key<T>(type, (AnnotationStrategy)NullAnnotationStrategy.INSTANCE);
    }

    public static Key<?> get(Type type, Class<? extends Annotation> class_) {
        return new Key<T>(type, Key.strategyFor(class_));
    }

    public static Key<?> get(Type type, Annotation annotation) {
        return new Key<T>(type, Key.strategyFor(annotation));
    }

    public static <T> Key<T> get(TypeLiteral<T> typeLiteral) {
        return new Key<T>(typeLiteral, (AnnotationStrategy)NullAnnotationStrategy.INSTANCE);
    }

    public static <T> Key<T> get(TypeLiteral<T> typeLiteral, Class<? extends Annotation> class_) {
        return new Key<T>(typeLiteral, Key.strategyFor(class_));
    }

    public static <T> Key<T> get(TypeLiteral<T> typeLiteral, Annotation annotation) {
        return new Key<T>(typeLiteral, Key.strategyFor(annotation));
    }

    public <T> Key<T> ofType(Class<T> class_) {
        return new Key<T>(class_, this.annotationStrategy);
    }

    public Key<?> ofType(Type type) {
        return new Key<T>(type, this.annotationStrategy);
    }

    public <T> Key<T> ofType(TypeLiteral<T> typeLiteral) {
        return new Key<T>(typeLiteral, this.annotationStrategy);
    }

    public boolean hasAttributes() {
        return this.annotationStrategy.hasAttributes();
    }

    public Key<T> withoutAttributes() {
        return new Key<T>(this.typeLiteral, this.annotationStrategy.withoutAttributes());
    }

    static AnnotationStrategy strategyFor(Annotation annotation) {
        Preconditions.checkNotNull(annotation, "annotation");
        Class<? extends Annotation> class_ = annotation.annotationType();
        Key.ensureRetainedAtRuntime(class_);
        Key.ensureIsBindingAnnotation(class_);
        if (Annotations.isMarker(class_)) {
            return new AnnotationTypeStrategy(class_, annotation);
        }
        return new AnnotationInstanceStrategy(Annotations.canonicalizeIfNamed(annotation));
    }

    static AnnotationStrategy strategyFor(Class<? extends Annotation> class_) {
        if (Annotations.isAllDefaultMethods(class_ = Annotations.canonicalizeIfNamed(class_))) {
            return Key.strategyFor(Annotations.generateAnnotation(class_));
        }
        Preconditions.checkNotNull(class_, "annotation type");
        Key.ensureRetainedAtRuntime(class_);
        Key.ensureIsBindingAnnotation(class_);
        return new AnnotationTypeStrategy(class_, null);
    }

    private static void ensureRetainedAtRuntime(Class<? extends Annotation> class_) {
        Preconditions.checkArgument(Annotations.isRetainedAtRuntime(class_), "%s is not retained at runtime. Please annotate it with @Retention(RUNTIME).", class_.getName());
    }

    private static void ensureIsBindingAnnotation(Class<? extends Annotation> class_) {
        Preconditions.checkArgument(Annotations.isBindingAnnotation(class_), "%s is not a binding annotation. Please annotate it with @BindingAnnotation.", class_.getName());
    }

    static class AnnotationTypeStrategy
    implements AnnotationStrategy {
        final Class<? extends Annotation> annotationType;
        final Annotation annotation;

        AnnotationTypeStrategy(Class<? extends Annotation> class_, Annotation annotation) {
            this.annotationType = Preconditions.checkNotNull(class_, "annotation type");
            this.annotation = annotation;
        }

        @Override
        public boolean hasAttributes() {
            return false;
        }

        @Override
        public AnnotationStrategy withoutAttributes() {
            throw new UnsupportedOperationException("Key already has no attributes.");
        }

        @Override
        public Annotation getAnnotation() {
            return this.annotation;
        }

        @Override
        public Class<? extends Annotation> getAnnotationType() {
            return this.annotationType;
        }

        public boolean equals(Object object) {
            if (!(object instanceof AnnotationTypeStrategy)) {
                return false;
            }
            AnnotationTypeStrategy annotationTypeStrategy = (AnnotationTypeStrategy)object;
            return this.annotationType.equals(annotationTypeStrategy.annotationType);
        }

        public int hashCode() {
            return this.annotationType.hashCode();
        }

        public String toString() {
            return "@" + this.annotationType.getName();
        }
    }

    static class AnnotationInstanceStrategy
    implements AnnotationStrategy {
        final Annotation annotation;

        AnnotationInstanceStrategy(Annotation annotation) {
            this.annotation = Preconditions.checkNotNull(annotation, "annotation");
        }

        @Override
        public boolean hasAttributes() {
            return true;
        }

        @Override
        public AnnotationStrategy withoutAttributes() {
            return new AnnotationTypeStrategy(this.getAnnotationType(), this.annotation);
        }

        @Override
        public Annotation getAnnotation() {
            return this.annotation;
        }

        @Override
        public Class<? extends Annotation> getAnnotationType() {
            return this.annotation.annotationType();
        }

        public boolean equals(Object object) {
            if (!(object instanceof AnnotationInstanceStrategy)) {
                return false;
            }
            AnnotationInstanceStrategy annotationInstanceStrategy = (AnnotationInstanceStrategy)object;
            return this.annotation.equals(annotationInstanceStrategy.annotation);
        }

        public int hashCode() {
            return this.annotation.hashCode();
        }

        public String toString() {
            return this.annotation.toString();
        }
    }

    static enum NullAnnotationStrategy implements AnnotationStrategy
    {
        INSTANCE;
        

        private NullAnnotationStrategy() {
        }

        @Override
        public boolean hasAttributes() {
            return false;
        }

        @Override
        public AnnotationStrategy withoutAttributes() {
            throw new UnsupportedOperationException("Key already has no attributes.");
        }

        @Override
        public Annotation getAnnotation() {
            return null;
        }

        @Override
        public Class<? extends Annotation> getAnnotationType() {
            return null;
        }

        public String toString() {
            return "[none]";
        }
    }

    static interface AnnotationStrategy {
        public Annotation getAnnotation();

        public Class<? extends Annotation> getAnnotationType();

        public boolean hasAttributes();

        public AnnotationStrategy withoutAttributes();
    }

}

