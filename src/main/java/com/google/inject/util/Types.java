/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.util;

import com.google.inject.Provider;
import com.google.inject.internal.MoreTypes;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Types {
    private Types() {
    }

    public static /* varargs */ ParameterizedType newParameterizedType(Type type, Type ... arrtype) {
        return Types.newParameterizedTypeWithOwner(null, type, arrtype);
    }

    public static /* varargs */ ParameterizedType newParameterizedTypeWithOwner(Type type, Type type2, Type ... arrtype) {
        return new MoreTypes.ParameterizedTypeImpl(type, type2, arrtype);
    }

    public static GenericArrayType arrayOf(Type type) {
        return new MoreTypes.GenericArrayTypeImpl(type);
    }

    public static WildcardType subtypeOf(Type type) {
        return new MoreTypes.WildcardTypeImpl(new Type[]{type}, MoreTypes.EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type type) {
        return new MoreTypes.WildcardTypeImpl(new Type[]{Object.class}, new Type[]{type});
    }

    public static ParameterizedType listOf(Type type) {
        return Types.newParameterizedType(List.class, new Type[]{type});
    }

    public static ParameterizedType collectionOf(Type type) {
        return Types.newParameterizedType(Collection.class, new Type[]{type});
    }

    public static ParameterizedType setOf(Type type) {
        return Types.newParameterizedType(Set.class, new Type[]{type});
    }

    public static ParameterizedType mapOf(Type type, Type type2) {
        return Types.newParameterizedType(Map.class, new Type[]{type, type2});
    }

    public static ParameterizedType providerOf(Type type) {
        return Types.newParameterizedType(Provider.class, new Type[]{type});
    }

    public static Type javaxProviderOf(Type type) {
        return Types.newParameterizedType(javax.inject.Provider.class, new Type[]{type});
    }
}

