/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class JsonTreeWriter
extends JsonWriter {
    private static final Writer UNWRITABLE_WRITER = new Writer(){

        @Override
        public void write(char[] arrc, int n2, int n3) {
            throw new AssertionError();
        }

        @Override
        public void flush() throws IOException {
            throw new AssertionError();
        }

        @Override
        public void close() throws IOException {
            throw new AssertionError();
        }
    };
    private static final JsonPrimitive SENTINEL_CLOSED = new JsonPrimitive("closed");
    private final List<JsonElement> stack = new ArrayList<JsonElement>();
    private String pendingName;
    private JsonElement product = JsonNull.INSTANCE;

    public JsonTreeWriter() {
        super(UNWRITABLE_WRITER);
    }

    public JsonElement get() {
        if (!this.stack.isEmpty()) {
            throw new IllegalStateException("Expected one JSON element but was " + this.stack);
        }
        return this.product;
    }

    private JsonElement peek() {
        return this.stack.get(this.stack.size() - 1);
    }

    private void put(JsonElement jsonElement) {
        if (this.pendingName != null) {
            if (!jsonElement.isJsonNull() || this.getSerializeNulls()) {
                JsonObject jsonObject = (JsonObject)this.peek();
                jsonObject.add(this.pendingName, jsonElement);
            }
            this.pendingName = null;
        } else if (this.stack.isEmpty()) {
            this.product = jsonElement;
        } else {
            JsonElement jsonElement2 = this.peek();
            if (jsonElement2 instanceof JsonArray) {
                ((JsonArray)jsonElement2).add(jsonElement);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public JsonWriter beginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        this.put(jsonArray);
        this.stack.add(jsonArray);
        return this;
    }

    @Override
    public JsonWriter endArray() throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        JsonElement jsonElement = this.peek();
        if (jsonElement instanceof JsonArray) {
            this.stack.remove(this.stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }

    @Override
    public JsonWriter beginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        this.put(jsonObject);
        this.stack.add(jsonObject);
        return this;
    }

    @Override
    public JsonWriter endObject() throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        JsonElement jsonElement = this.peek();
        if (jsonElement instanceof JsonObject) {
            this.stack.remove(this.stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }

    @Override
    public JsonWriter name(String string) throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        JsonElement jsonElement = this.peek();
        if (jsonElement instanceof JsonObject) {
            this.pendingName = string;
            return this;
        }
        throw new IllegalStateException();
    }

    @Override
    public JsonWriter value(String string) throws IOException {
        if (string == null) {
            return this.nullValue();
        }
        this.put(new JsonPrimitive(string));
        return this;
    }

    @Override
    public JsonWriter nullValue() throws IOException {
        this.put(JsonNull.INSTANCE);
        return this;
    }

    @Override
    public JsonWriter value(boolean bl) throws IOException {
        this.put(new JsonPrimitive(bl));
        return this;
    }

    @Override
    public JsonWriter value(Boolean bl) throws IOException {
        if (bl == null) {
            return this.nullValue();
        }
        this.put(new JsonPrimitive(bl));
        return this;
    }

    @Override
    public JsonWriter value(double d2) throws IOException {
        if (!this.isLenient() && (Double.isNaN(d2) || Double.isInfinite(d2))) {
            throw new IllegalArgumentException("JSON forbids NaN and infinities: " + d2);
        }
        this.put(new JsonPrimitive(d2));
        return this;
    }

    @Override
    public JsonWriter value(long l2) throws IOException {
        this.put(new JsonPrimitive(l2));
        return this;
    }

    @Override
    public JsonWriter value(Number number) throws IOException {
        double d2;
        if (number == null) {
            return this.nullValue();
        }
        if (!this.isLenient() && (Double.isNaN(d2 = number.doubleValue()) || Double.isInfinite(d2))) {
            throw new IllegalArgumentException("JSON forbids NaN and infinities: " + number);
        }
        this.put(new JsonPrimitive(number));
        return this;
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
        if (!this.stack.isEmpty()) {
            throw new IOException("Incomplete document");
        }
        this.stack.add(SENTINEL_CLOSED);
    }

}

