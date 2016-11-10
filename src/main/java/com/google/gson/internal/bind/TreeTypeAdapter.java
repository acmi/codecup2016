/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public final class TreeTypeAdapter<T>
extends TypeAdapter<T> {
    private final JsonSerializer<T> serializer;
    private final JsonDeserializer<T> deserializer;
    private final Gson gson;
    private final TypeToken<T> typeToken;
    private final TypeAdapterFactory skipPast;
    private final TreeTypeAdapter<T> context;
    private TypeAdapter<T> delegate;

    public TreeTypeAdapter(JsonSerializer<T> jsonSerializer, JsonDeserializer<T> jsonDeserializer, Gson gson, TypeToken<T> typeToken, TypeAdapterFactory typeAdapterFactory) {
        this.context = new GsonContextImpl();
        this.serializer = jsonSerializer;
        this.deserializer = jsonDeserializer;
        this.gson = gson;
        this.typeToken = typeToken;
        this.skipPast = typeAdapterFactory;
    }

    @Override
    public T read(JsonReader jsonReader) throws IOException {
        if (this.deserializer == null) {
            return this.delegate().read(jsonReader);
        }
        JsonElement jsonElement = Streams.parse(jsonReader);
        if (jsonElement.isJsonNull()) {
            return null;
        }
        return this.deserializer.deserialize(jsonElement, this.typeToken.getType(), (JsonDeserializationContext)((Object)this.context));
    }

    @Override
    public void write(JsonWriter jsonWriter, T t2) throws IOException {
        if (this.serializer == null) {
            this.delegate().write(jsonWriter, t2);
            return;
        }
        if (t2 == null) {
            jsonWriter.nullValue();
            return;
        }
        JsonElement jsonElement = this.serializer.serialize(t2, this.typeToken.getType(), (JsonSerializationContext)((Object)this.context));
        Streams.write(jsonElement, jsonWriter);
    }

    private TypeAdapter<T> delegate() {
        TypeAdapter<T> typeAdapter = this.delegate;
        TypeAdapter<T> typeAdapter2 = typeAdapter != null ? typeAdapter : (this.delegate = this.gson.getDelegateAdapter(this.skipPast, this.typeToken));
        return typeAdapter2;
    }

    public static TypeAdapterFactory newFactory(TypeToken<?> typeToken, Object object) {
        return new SingleTypeFactory(object, typeToken, false, null);
    }

    public static TypeAdapterFactory newFactoryWithMatchRawType(TypeToken<?> typeToken, Object object) {
        boolean bl = typeToken.getType() == typeToken.getRawType();
        return new SingleTypeFactory(object, typeToken, bl, null);
    }

    public static TypeAdapterFactory newTypeHierarchyFactory(Class<?> class_, Object object) {
        return new SingleTypeFactory(object, null, false, class_);
    }

    private final class GsonContextImpl
    implements JsonDeserializationContext,
    JsonSerializationContext {
        private GsonContextImpl() {
        }

        @Override
        public JsonElement serialize(Object object) {
            return TreeTypeAdapter.this.gson.toJsonTree(object);
        }

        @Override
        public JsonElement serialize(Object object, Type type) {
            return TreeTypeAdapter.this.gson.toJsonTree(object, type);
        }

        public <R> R deserialize(JsonElement jsonElement, Type type) throws JsonParseException {
            return (R)TreeTypeAdapter.this.gson.fromJson(jsonElement, type);
        }
    }

    private static final class SingleTypeFactory
    implements TypeAdapterFactory {
        private final TypeToken<?> exactType;
        private final boolean matchRawType;
        private final Class<?> hierarchyType;
        private final JsonSerializer<?> serializer;
        private final JsonDeserializer<?> deserializer;

        SingleTypeFactory(Object object, TypeToken<?> typeToken, boolean bl, Class<?> class_) {
            this.serializer = object instanceof JsonSerializer ? (JsonSerializer)object : null;
            this.deserializer = object instanceof JsonDeserializer ? (JsonDeserializer)object : null;
            $Gson$Preconditions.checkArgument(this.serializer != null || this.deserializer != null);
            this.exactType = typeToken;
            this.matchRawType = bl;
            this.hierarchyType = class_;
        }

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            boolean bl = this.exactType != null ? this.exactType.equals(typeToken) || this.matchRawType && this.exactType.getType() == typeToken.getRawType() : this.hierarchyType.isAssignableFrom(typeToken.getRawType());
            return bl ? new TreeTypeAdapter(this.serializer, this.deserializer, gson, typeToken, this) : null;
        }
    }

}

