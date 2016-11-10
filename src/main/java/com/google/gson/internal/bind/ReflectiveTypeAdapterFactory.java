/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingStrategy, Excluder excluder, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingStrategy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterAnnotationTypeAdapterFactory;
    }

    public boolean excludeField(Field field, boolean bl) {
        return ReflectiveTypeAdapterFactory.excludeField(field, bl, this.excluder);
    }

    static boolean excludeField(Field field, boolean bl, Excluder excluder) {
        return !excluder.excludeClass(field.getType(), bl) && !excluder.excludeField(field, bl);
    }

    private List<String> getFieldNames(Field field) {
        SerializedName serializedName = field.getAnnotation(SerializedName.class);
        if (serializedName == null) {
            String string = this.fieldNamingPolicy.translateName(field);
            return Collections.singletonList(string);
        }
        String string = serializedName.value();
        String[] arrstring = serializedName.alternate();
        if (arrstring.length == 0) {
            return Collections.singletonList(string);
        }
        ArrayList<String> arrayList = new ArrayList<String>(arrstring.length + 1);
        arrayList.add(string);
        for (String string2 : arrstring) {
            arrayList.add(string2);
        }
        return arrayList;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<T> class_ = typeToken.getRawType();
        if (!Object.class.isAssignableFrom(class_)) {
            return null;
        }
        ObjectConstructor<T> objectConstructor = this.constructorConstructor.get(typeToken);
        return new Adapter<T>(objectConstructor, this.getBoundFields(gson, typeToken, class_));
    }

    private BoundField createBoundField(final Gson gson, final Field field, String string, final TypeToken<?> typeToken, boolean bl, boolean bl2) {
        boolean bl3;
        final boolean bl4 = Primitives.isPrimitive(typeToken.getRawType());
        JsonAdapter jsonAdapter = field.getAnnotation(JsonAdapter.class);
        TypeAdapter typeAdapter = null;
        if (jsonAdapter != null) {
            typeAdapter = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
        }
        boolean bl5 = bl3 = typeAdapter != null;
        if (typeAdapter == null) {
            typeAdapter = gson.getAdapter(typeToken);
        }
        final TypeAdapter typeAdapter2 = typeAdapter;
        return new BoundField(string, bl, bl2){

            @Override
            void write(JsonWriter jsonWriter, Object object) throws IOException, IllegalAccessException {
                Object object2 = field.get(object);
                TypeAdapter typeAdapter = bl3 ? typeAdapter2 : new TypeAdapterRuntimeTypeWrapper(gson, typeAdapter2, typeToken.getType());
                typeAdapter.write(jsonWriter, object2);
            }

            @Override
            void read(JsonReader jsonReader, Object object) throws IOException, IllegalAccessException {
                Object t2 = typeAdapter2.read(jsonReader);
                if (t2 != null || !bl4) {
                    field.set(object, t2);
                }
            }

            @Override
            public boolean writeField(Object object) throws IOException, IllegalAccessException {
                if (!this.serialized) {
                    return false;
                }
                Object object2 = field.get(object);
                return object2 != object;
            }
        };
    }

    private Map<String, BoundField> getBoundFields(Gson gson, TypeToken<?> typeToken, Class<?> class_) {
        LinkedHashMap<String, BoundField> linkedHashMap = new LinkedHashMap<String, BoundField>();
        if (class_.isInterface()) {
            return linkedHashMap;
        }
        Type type = typeToken.getType();
        while (class_ != Object.class) {
            Field[] arrfield;
            for (Field field : arrfield = class_.getDeclaredFields()) {
                boolean bl = this.excludeField(field, true);
                boolean bl2 = this.excludeField(field, false);
                if (!bl && !bl2) continue;
                field.setAccessible(true);
                Type type2 = $Gson$Types.resolve(typeToken.getType(), class_, field.getGenericType());
                List<String> list = this.getFieldNames(field);
                BoundField boundField = null;
                for (int i2 = 0; i2 < list.size(); ++i2) {
                    String string = list.get(i2);
                    if (i2 != 0) {
                        bl = false;
                    }
                    BoundField boundField2 = this.createBoundField(gson, field, string, TypeToken.get(type2), bl, bl2);
                    BoundField boundField3 = linkedHashMap.put(string, boundField2);
                    if (boundField != null) continue;
                    boundField = boundField3;
                }
                if (boundField == null) continue;
                throw new IllegalArgumentException(type + " declares multiple JSON fields named " + boundField.name);
            }
            typeToken = TypeToken.get($Gson$Types.resolve(typeToken.getType(), class_, class_.getGenericSuperclass()));
            class_ = typeToken.getRawType();
        }
        return linkedHashMap;
    }

    public static final class Adapter<T>
    extends TypeAdapter<T> {
        private final ObjectConstructor<T> constructor;
        private final Map<String, BoundField> boundFields;

        Adapter(ObjectConstructor<T> objectConstructor, Map<String, BoundField> map) {
            this.constructor = objectConstructor;
            this.boundFields = map;
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            T t2 = this.constructor.construct();
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String string = jsonReader.nextName();
                    BoundField boundField = this.boundFields.get(string);
                    if (boundField == null || !boundField.deserialized) {
                        jsonReader.skipValue();
                        continue;
                    }
                    boundField.read(jsonReader, t2);
                }
            }
            catch (IllegalStateException illegalStateException) {
                throw new JsonSyntaxException(illegalStateException);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError(illegalAccessException);
            }
            jsonReader.endObject();
            return t2;
        }

        @Override
        public void write(JsonWriter jsonWriter, T t2) throws IOException {
            if (t2 == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    if (!boundField.writeField(t2)) continue;
                    jsonWriter.name(boundField.name);
                    boundField.write(jsonWriter, t2);
                }
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError(illegalAccessException);
            }
            jsonWriter.endObject();
        }
    }

    static abstract class BoundField {
        final String name;
        final boolean serialized;
        final boolean deserialized;

        protected BoundField(String string, boolean bl, boolean bl2) {
            this.name = string;
            this.serialized = bl;
            this.deserialized = bl2;
        }

        abstract boolean writeField(Object var1) throws IOException, IllegalAccessException;

        abstract void write(JsonWriter var1, Object var2) throws IOException, IllegalAccessException;

        abstract void read(JsonReader var1, Object var2) throws IOException, IllegalAccessException;
    }

}

