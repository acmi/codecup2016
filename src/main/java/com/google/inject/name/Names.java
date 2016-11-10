/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.name;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.NamedImpl;
import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Names {
    private Names() {
    }

    public static Named named(String string) {
        return new NamedImpl(string);
    }

    public static void bindProperties(Binder binder, Map<String, String> map) {
        binder = binder.skipSources(Names.class);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String string = entry.getKey();
            String string2 = entry.getValue();
            binder.bind(Key.get(String.class, (Annotation)new NamedImpl(string))).toInstance(string2);
        }
    }

    public static void bindProperties(Binder binder, Properties properties) {
        binder = binder.skipSources(Names.class);
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            String string2 = properties.getProperty(string);
            binder.bind(Key.get(String.class, (Annotation)new NamedImpl(string))).toInstance(string2);
        }
    }
}

