/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;
import com.google.inject.util.Types;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.inject.Provider;

public class MoreTypes {
    public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
    private static final Map<TypeLiteral<?>, TypeLiteral<?>> PRIMITIVE_TO_WRAPPER = new ImmutableMap.Builder<TypeLiteral<Boolean>, TypeLiteral<Boolean>>().put(TypeLiteral.get(Boolean.TYPE), TypeLiteral.get(Boolean.class)).put(TypeLiteral.get(Byte.TYPE), TypeLiteral.get(Byte.class)).put(TypeLiteral.get(Short.TYPE), TypeLiteral.get(Short.class)).put(TypeLiteral.get(Integer.TYPE), TypeLiteral.get(Integer.class)).put(TypeLiteral.get(Long.TYPE), TypeLiteral.get(Long.class)).put(TypeLiteral.get(Float.TYPE), TypeLiteral.get(Float.class)).put(TypeLiteral.get(Double.TYPE), TypeLiteral.get(Double.class)).put(TypeLiteral.get(Character.TYPE), TypeLiteral.get(Character.class)).put(TypeLiteral.get(Void.TYPE), TypeLiteral.get(Void.class)).build();

    private MoreTypes() {
    }

    public static <T> Key<T> canonicalizeKey(Key<T> key) {
        if (key.getClass() == Key.class) {
            return key;
        }
        if (key.getAnnotation() != null) {
            return Key.get(key.getTypeLiteral(), key.getAnnotation());
        }
        if (key.getAnnotationType() != null) {
            return Key.get(key.getTypeLiteral(), key.getAnnotationType());
        }
        return Key.get(key.getTypeLiteral());
    }

    public static <T> TypeLiteral<T> canonicalizeForKey(TypeLiteral<T> typeLiteral) {
        Type type = typeLiteral.getType();
        if (!MoreTypes.isFullySpecified(type)) {
            Errors errors = new Errors().keyNotFullySpecified(typeLiteral);
            throw new ConfigurationException(errors.getMessages());
        }
        if (typeLiteral.getRawType() == Provider.class) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            TypeLiteral typeLiteral2 = TypeLiteral.get(Types.providerOf(parameterizedType.getActualTypeArguments()[0]));
            return typeLiteral2;
        }
        TypeLiteral typeLiteral3 = PRIMITIVE_TO_WRAPPER.get(typeLiteral);
        if (typeLiteral3 != null) {
            return typeLiteral3;
        }
        if (typeLiteral.getClass() == TypeLiteral.class) {
            return typeLiteral;
        }
        TypeLiteral typeLiteral4 = TypeLiteral.get(typeLiteral.getType());
        return typeLiteral4;
    }

    private static boolean isFullySpecified(Type type) {
        if (type instanceof Class) {
            return true;
        }
        if (type instanceof CompositeType) {
            return ((CompositeType)((Object)type)).isFullySpecified();
        }
        if (type instanceof TypeVariable) {
            return false;
        }
        return ((CompositeType)((Object)MoreTypes.canonicalize(type))).isFullySpecified();
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Type type2 = (Class)type;
            return type2.isArray() ? new GenericArrayTypeImpl(MoreTypes.canonicalize(type2.getComponentType())) : type2;
        }
        if (type instanceof CompositeType) {
            return type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            return new ParameterizedTypeImpl(parameterizedType.getOwnerType(), parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType)type;
            return new GenericArrayTypeImpl(genericArrayType.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)type;
            return new WildcardTypeImpl(wildcardType.getUpperBounds(), wildcardType.getLowerBounds());
        }
        return type;
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class)type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type type2 = parameterizedType.getRawType();
            Preconditions.checkArgument(type2 instanceof Class, "Expected a Class, but <%s> is of type %s", type, type.getClass().getName());
            return (Class)type2;
        }
        if (type instanceof GenericArrayType) {
            Type type3 = ((GenericArrayType)type).getGenericComponentType();
            return Array.newInstance(MoreTypes.getRawType(type3), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }

    public static boolean equals(Type type, Type type2) {
        if (type == type2) {
            return true;
        }
        if (type instanceof Class) {
            return type.equals(type2);
        }
        if (type instanceof ParameterizedType) {
            if (!(type2 instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType parameterizedType = (ParameterizedType)type;
            ParameterizedType parameterizedType2 = (ParameterizedType)type2;
            return Objects.equal(parameterizedType.getOwnerType(), parameterizedType2.getOwnerType()) && parameterizedType.getRawType().equals(parameterizedType2.getRawType()) && Arrays.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            if (!(type2 instanceof GenericArrayType)) {
                return false;
            }
            GenericArrayType genericArrayType = (GenericArrayType)type;
            GenericArrayType genericArrayType2 = (GenericArrayType)type2;
            return MoreTypes.equals(genericArrayType.getGenericComponentType(), genericArrayType2.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            if (!(type2 instanceof WildcardType)) {
                return false;
            }
            WildcardType wildcardType = (WildcardType)type;
            WildcardType wildcardType2 = (WildcardType)type2;
            return Arrays.equals(wildcardType.getUpperBounds(), wildcardType2.getUpperBounds()) && Arrays.equals(wildcardType.getLowerBounds(), wildcardType2.getLowerBounds());
        }
        if (type instanceof TypeVariable) {
            if (!(type2 instanceof TypeVariable)) {
                return false;
            }
            TypeVariable typeVariable = (TypeVariable)type;
            TypeVariable typeVariable2 = (TypeVariable)type2;
            return typeVariable.getGenericDeclaration().equals(typeVariable2.getGenericDeclaration()) && typeVariable.getName().equals(typeVariable2.getName());
        }
        return false;
    }

    private static int hashCodeOrZero(Object object) {
        return object != null ? object.hashCode() : 0;
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class)type).getName() : type.toString();
    }

    public static Type getGenericSupertype(Type type, Class<?> object, Class<?> class_) {
        Class class_2;
        if (class_ == object) {
            return type;
        }
        if (class_.isInterface()) {
            class_2 = object.getInterfaces();
            int n2 = class_2.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                if (class_2[i2] == class_) {
                    return object.getGenericInterfaces()[i2];
                }
                if (!class_.isAssignableFrom(class_2[i2])) continue;
                return MoreTypes.getGenericSupertype(object.getGenericInterfaces()[i2], class_2[i2], class_);
            }
        }
        if (!object.isInterface()) {
            while (object != Object.class) {
                class_2 = object.getSuperclass();
                if (class_2 == class_) {
                    return object.getGenericSuperclass();
                }
                if (class_.isAssignableFrom(class_2)) {
                    return MoreTypes.getGenericSupertype(object.getGenericSuperclass(), class_2, class_);
                }
                object = class_2;
            }
        }
        return class_;
    }

    public static Type resolveTypeVariable(Type type, Class<?> class_, TypeVariable typeVariable) {
        Class class_2 = MoreTypes.declaringClassOf(typeVariable);
        if (class_2 == null) {
            return typeVariable;
        }
        Type type2 = MoreTypes.getGenericSupertype(type, class_, class_2);
        if (type2 instanceof ParameterizedType) {
            int n2 = MoreTypes.indexOf(class_2.getTypeParameters(), typeVariable);
            return ((ParameterizedType)type2).getActualTypeArguments()[n2];
        }
        return typeVariable;
    }

    private static int indexOf(Object[] arrobject, Object object) {
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            if (!object.equals(arrobject[i2])) continue;
            return i2;
        }
        throw new NoSuchElementException();
    }

    private static Class<?> declaringClassOf(TypeVariable typeVariable) {
        Object d2 = typeVariable.getGenericDeclaration();
        return d2 instanceof Class ? (Class)d2 : null;
    }

    private static void checkNotPrimitive(Type type, String string) {
        Preconditions.checkArgument(!(type instanceof Class) || !((Class)type).isPrimitive(), "Primitive types are not allowed in %s: %s", string, type);
    }

    private static interface CompositeType {
        public boolean isFullySpecified();
    }

    public static class WildcardTypeImpl
    implements CompositeType,
    Serializable,
    WildcardType {
        private final Type upperBound;
        private final Type lowerBound;
        private static final long serialVersionUID = 0;

        public WildcardTypeImpl(Type[] arrtype, Type[] arrtype2) {
            Preconditions.checkArgument(arrtype2.length <= 1, "Must have at most one lower bound.");
            Preconditions.checkArgument(arrtype.length == 1, "Must have exactly one upper bound.");
            if (arrtype2.length == 1) {
                Preconditions.checkNotNull(arrtype2[0], "lowerBound");
                MoreTypes.checkNotPrimitive(arrtype2[0], "wildcard bounds");
                Preconditions.checkArgument(arrtype[0] == Object.class, "bounded both ways");
                this.lowerBound = MoreTypes.canonicalize(arrtype2[0]);
                this.upperBound = Object.class;
            } else {
                Preconditions.checkNotNull(arrtype[0], "upperBound");
                MoreTypes.checkNotPrimitive(arrtype[0], "wildcard bounds");
                this.lowerBound = null;
                this.upperBound = MoreTypes.canonicalize(arrtype[0]);
            }
        }

        @Override
        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        @Override
        public Type[] getLowerBounds() {
            Type[] arrtype;
            if (this.lowerBound != null) {
                Type[] arrtype2 = new Type[1];
                arrtype = arrtype2;
                arrtype2[0] = this.lowerBound;
            } else {
                arrtype = MoreTypes.EMPTY_TYPE_ARRAY;
            }
            return arrtype;
        }

        @Override
        public boolean isFullySpecified() {
            return MoreTypes.isFullySpecified(this.upperBound) && (this.lowerBound == null || MoreTypes.isFullySpecified(this.lowerBound));
        }

        public boolean equals(Object object) {
            return object instanceof WildcardType && MoreTypes.equals(this, (WildcardType)object);
        }

        public int hashCode() {
            return (this.lowerBound != null ? 31 + this.lowerBound.hashCode() : 1) ^ 31 + this.upperBound.hashCode();
        }

        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + MoreTypes.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + MoreTypes.typeToString(this.upperBound);
        }
    }

    public static class GenericArrayTypeImpl
    implements CompositeType,
    Serializable,
    GenericArrayType {
        private final Type componentType;
        private static final long serialVersionUID = 0;

        public GenericArrayTypeImpl(Type type) {
            this.componentType = MoreTypes.canonicalize(type);
        }

        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }

        @Override
        public boolean isFullySpecified() {
            return MoreTypes.isFullySpecified(this.componentType);
        }

        public boolean equals(Object object) {
            return object instanceof GenericArrayType && MoreTypes.equals(this, (GenericArrayType)object);
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return MoreTypes.typeToString(this.componentType) + "[]";
        }
    }

    public static class ParameterizedTypeImpl
    implements CompositeType,
    Serializable,
    ParameterizedType {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;
        private static final long serialVersionUID = 0;

        public /* varargs */ ParameterizedTypeImpl(Type type, Type type2, Type ... arrtype) {
            ParameterizedTypeImpl.ensureOwnerType(type, type2);
            this.ownerType = type == null ? null : MoreTypes.canonicalize(type);
            this.rawType = MoreTypes.canonicalize(type2);
            this.typeArguments = (Type[])arrtype.clone();
            for (int i2 = 0; i2 < this.typeArguments.length; ++i2) {
                Preconditions.checkNotNull(this.typeArguments[i2], "type parameter");
                MoreTypes.checkNotPrimitive(this.typeArguments[i2], "type parameters");
                this.typeArguments[i2] = MoreTypes.canonicalize(this.typeArguments[i2]);
            }
        }

        @Override
        public Type[] getActualTypeArguments() {
            return (Type[])this.typeArguments.clone();
        }

        @Override
        public Type getRawType() {
            return this.rawType;
        }

        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }

        @Override
        public boolean isFullySpecified() {
            if (this.ownerType != null && !MoreTypes.isFullySpecified(this.ownerType)) {
                return false;
            }
            if (!MoreTypes.isFullySpecified(this.rawType)) {
                return false;
            }
            for (Type type : this.typeArguments) {
                if (MoreTypes.isFullySpecified(type)) continue;
                return false;
            }
            return true;
        }

        public boolean equals(Object object) {
            return object instanceof ParameterizedType && MoreTypes.equals(this, (ParameterizedType)object);
        }

        public int hashCode() {
            return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ MoreTypes.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(30 * (this.typeArguments.length + 1));
            stringBuilder.append(MoreTypes.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append("<").append(MoreTypes.typeToString(this.typeArguments[0]));
            for (int i2 = 1; i2 < this.typeArguments.length; ++i2) {
                stringBuilder.append(", ").append(MoreTypes.typeToString(this.typeArguments[i2]));
            }
            return stringBuilder.append(">").toString();
        }

        private static void ensureOwnerType(Type type, Type type2) {
            if (type2 instanceof Class) {
                Class class_ = (Class)type2;
                Preconditions.checkArgument(type != null || class_.getEnclosingClass() == null, "No owner type for enclosed %s", type2);
                Preconditions.checkArgument(type == null || class_.getEnclosingClass() != null, "Owner type for unenclosed %s", type2);
            }
        }
    }

}

