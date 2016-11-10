/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public final class ArrayTypeAdapter<E>
extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Type type = typeToken.getType();
            if (!(type instanceof GenericArrayType || type instanceof Class && ((Class)type).isArray())) {
                return null;
            }
            Type type2 = $Gson$Types.getArrayComponentType(type);
            TypeAdapter typeAdapter = gson.getAdapter(TypeToken.get(type2));
            return new ArrayTypeAdapter(gson, typeAdapter, $Gson$Types.getRawType(type2));
        }
    };
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;

    public ArrayTypeAdapter(Gson gson, TypeAdapter<E> typeAdapter, Class<E> class_) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(gson, typeAdapter, class_);
        this.componentType = class_;
    }

    @Override
    public Object read(JsonReader jsonReader) throws IOException {
        Object object;
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        ArrayList<E> arrayList = new ArrayList<E>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            object = this.componentTypeAdapter.read(jsonReader);
            arrayList.add(object);
        }
        jsonReader.endArray();
        object = Array.newInstance(this.componentType, arrayList.size());
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            Array.set(object, i2, arrayList.get(i2));
        }
        return object;
    }

    @Override
    public void write(JsonWriter jsonWriter, Object object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginArray();
        int n2 = Array.getLength(object);
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object2 = Array.get(object, i2);
            this.componentTypeAdapter.write(jsonWriter, (Object)object2);
        }
        jsonWriter.endArray();
    }

}

