/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public abstract class TypeAdapter<T> {
    public abstract void write(JsonWriter var1, T var2) throws IOException;

    public final void toJson(Writer writer, T t2) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(writer);
        this.write(jsonWriter, t2);
    }

    public final TypeAdapter<T> nullSafe() {
        return new TypeAdapter<T>(){

            @Override
            public void write(JsonWriter jsonWriter, T t2) throws IOException {
                if (t2 == null) {
                    jsonWriter.nullValue();
                } else {
                    TypeAdapter.this.write(jsonWriter, t2);
                }
            }

            @Override
            public T read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return TypeAdapter.this.read(jsonReader);
            }
        };
    }

    public final String toJson(T t2) {
        StringWriter stringWriter = new StringWriter();
        try {
            this.toJson(stringWriter, t2);
        }
        catch (IOException iOException) {
            throw new AssertionError(iOException);
        }
        return stringWriter.toString();
    }

    public final JsonElement toJsonTree(T t2) {
        try {
            JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
            this.write(jsonTreeWriter, t2);
            return jsonTreeWriter.get();
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
    }

    public abstract T read(JsonReader var1) throws IOException;

    public final T fromJson(Reader reader) throws IOException {
        JsonReader jsonReader = new JsonReader(reader);
        return this.read(jsonReader);
    }

    public final T fromJson(String string) throws IOException {
        return this.fromJson(new StringReader(string));
    }

    public final T fromJsonTree(JsonElement jsonElement) {
        try {
            JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
            return this.read(jsonTreeReader);
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
    }

}

