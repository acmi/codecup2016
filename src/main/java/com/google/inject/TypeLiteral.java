/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Provider;
import com.google.inject.internal.MoreTypes;
import com.google.inject.util.Types;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

public class TypeLiteral<T> {
    final Class<? super T> rawType;
    final Type type;
    final int hashCode;

    protected TypeLiteral() {
        this.type = TypeLiteral.getSuperclassTypeParameter(this.getClass());
        this.rawType = MoreTypes.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    TypeLiteral(Type type) {
        this.type = MoreTypes.canonicalize(Preconditions.checkNotNull(type, "type"));
        this.rawType = MoreTypes.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    static Type getSuperclassTypeParameter(Class<?> class_) {
        Type type = class_.getGenericSuperclass();
        if (type instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType)type;
        return MoreTypes.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    static TypeLiteral<?> fromSuperclassTypeParameter(Class<?> class_) {
        return new TypeLiteral<T>(TypeLiteral.getSuperclassTypeParameter(class_));
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    final TypeLiteral<Provider<T>> providerType() {
        return TypeLiteral.get(Types.providerOf(this.getType()));
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object object) {
        return object instanceof TypeLiteral && MoreTypes.equals(this.type, ((TypeLiteral)object).type);
    }

    public final String toString() {
        return MoreTypes.typeToString(this.type);
    }

    public static TypeLiteral<?> get(Type type) {
        return new TypeLiteral<T>(type);
    }

    public static <T> TypeLiteral<T> get(Class<T> class_) {
        return new TypeLiteral<T>(class_);
    }

    private List<TypeLiteral<?>> resolveAll(Type[] arrtype) {
        TypeLiteral[] arrtypeLiteral = new TypeLiteral[arrtype.length];
        for (int i2 = 0; i2 < arrtype.length; ++i2) {
            arrtypeLiteral[i2] = this.resolve(arrtype[i2]);
        }
        return ImmutableList.copyOf(arrtypeLiteral);
    }

    TypeLiteral<?> resolve(Type type) {
        return TypeLiteral.get(this.resolveType(type));
    }

    Type resolveType(Type type) {
        while (type instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable)type;
            type = MoreTypes.resolveTypeVariable(this.type, this.rawType, typeVariable);
            if (type != typeVariable) continue;
            return type;
        }
        if (type instanceof GenericArrayType) {
            Type type2;
            GenericArrayType genericArrayType = (GenericArrayType)type;
            Type type3 = genericArrayType.getGenericComponentType();
            return type3 == (type2 = this.resolveType(type3)) ? genericArrayType : Types.arrayOf(type2);
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type type4 = parameterizedType.getOwnerType();
            Type type5 = this.resolveType(type4);
            boolean bl = type5 != type4;
            Type[] arrtype = parameterizedType.getActualTypeArguments();
            int n2 = arrtype.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Type type6 = this.resolveType(arrtype[i2]);
                if (type6 == arrtype[i2]) continue;
                if (!bl) {
                    arrtype = (Type[])arrtype.clone();
                    bl = true;
                }
                arrtype[i2] = type6;
            }
            return bl ? Types.newParameterizedTypeWithOwner(type5, parameterizedType.getRawType(), arrtype) : parameterizedType;
        }
        if (type instanceof WildcardType) {
            Type type7;
            WildcardType wildcardType = (WildcardType)type;
            Type[] arrtype = wildcardType.getLowerBounds();
            Type[] arrtype2 = wildcardType.getUpperBounds();
            if (arrtype.length == 1) {
                Type type8 = this.resolveType(arrtype[0]);
                if (type8 != arrtype[0]) {
                    return Types.supertypeOf(type8);
                }
            } else if (arrtype2.length == 1 && (type7 = this.resolveType(arrtype2[0])) != arrtype2[0]) {
                return Types.subtypeOf(type7);
            }
            return wildcardType;
        }
        return type;
    }

    public TypeLiteral<?> getSupertype(Class<?> class_) {
        Preconditions.checkArgument(class_.isAssignableFrom(this.rawType), "%s is not a supertype of %s", class_, this.type);
        return this.resolve(MoreTypes.getGenericSupertype(this.type, this.rawType, class_));
    }

    public TypeLiteral<?> getFieldType(Field field) {
        Preconditions.checkArgument(field.getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", field, this.type);
        return this.resolve(field.getGenericType());
    }

    public List<TypeLiteral<?>> getParameterTypes(Member member) {
        Type[] arrtype;
        if (member instanceof Method) {
            Method method = (Method)member;
            Preconditions.checkArgument(method.getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
            arrtype = method.getGenericParameterTypes();
        } else if (member instanceof Constructor) {
            Constructor constructor = (Constructor)member;
            Preconditions.checkArgument(constructor.getDeclaringClass().isAssignableFrom(this.rawType), "%s does not construct a supertype of %s", constructor, this.type);
            arrtype = constructor.getGenericParameterTypes();
        } else {
            throw new IllegalArgumentException("Not a method or a constructor: " + member);
        }
        return this.resolveAll(arrtype);
    }

    public List<TypeLiteral<?>> getExceptionTypes(Member member) {
        Type[] arrtype;
        if (member instanceof Method) {
            Method method = (Method)member;
            Preconditions.checkArgument(method.getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
            arrtype = method.getGenericExceptionTypes();
        } else if (member instanceof Constructor) {
            Constructor constructor = (Constructor)member;
            Preconditions.checkArgument(constructor.getDeclaringClass().isAssignableFrom(this.rawType), "%s does not construct a supertype of %s", constructor, this.type);
            arrtype = constructor.getGenericExceptionTypes();
        } else {
            throw new IllegalArgumentException("Not a method or a constructor: " + member);
        }
        return this.resolveAll(arrtype);
    }

    public TypeLiteral<?> getReturnType(Method method) {
        Preconditions.checkArgument(method.getDeclaringClass().isAssignableFrom(this.rawType), "%s is not defined by a supertype of %s", method, this.type);
        return this.resolve(method.getGenericReturnType());
    }
}

