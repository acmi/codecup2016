/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;

public final class JsonAdapterAnnotationTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<T> class_ = typeToken.getRawType();
        JsonAdapter jsonAdapter = class_.getAnnotation(JsonAdapter.class);
        if (jsonAdapter == null) {
            return null;
        }
        return this.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
    }

    TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> typeToken, JsonAdapter jsonAdapter) {
        TypeAdapter typeAdapter2;
        TypeAdapter typeAdapter2;
        Object obj = constructorConstructor.get(TypeToken.get(jsonAdapter.value())).construct();
        if (obj instanceof TypeAdapter) {
            typeAdapter2 = (TypeAdapter)obj;
        } else if (obj instanceof TypeAdapterFactory) {
            typeAdapter2 = ((TypeAdapterFactory)obj).create(gson, typeToken);
        } else if (obj instanceof JsonSerializer || obj instanceof JsonDeserializer) {
            JsonSerializer jsonSerializer = obj instanceof JsonSerializer ? (JsonSerializer)obj : null;
            JsonDeserializer jsonDeserializer = obj instanceof JsonDeserializer ? (JsonDeserializer)obj : null;
            typeAdapter2 = new TreeTypeAdapter(jsonSerializer, jsonDeserializer, gson, typeToken, null);
        } else {
            throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer reference.");
        }
        if (typeAdapter2 != null) {
            typeAdapter2 = typeAdapter2.nullSafe();
        }
        return typeAdapter2;
    }
}

