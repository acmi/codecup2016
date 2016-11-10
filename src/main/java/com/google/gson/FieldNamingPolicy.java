/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson;

import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
import java.util.Locale;

public enum FieldNamingPolicy implements FieldNamingStrategy
{
    IDENTITY{

        @Override
        public String translateName(Field field) {
            return field.getName();
        }
    }
    ,
    UPPER_CAMEL_CASE{

        @Override
        public String translateName(Field field) {
            return .upperCaseFirstLetter(field.getName());
        }
    }
    ,
    UPPER_CAMEL_CASE_WITH_SPACES{

        @Override
        public String translateName(Field field) {
            return .upperCaseFirstLetter(.separateCamelCase(field.getName(), " "));
        }
    }
    ,
    LOWER_CASE_WITH_UNDERSCORES{

        @Override
        public String translateName(Field field) {
            return .separateCamelCase(field.getName(), "_").toLowerCase(Locale.ENGLISH);
        }
    }
    ,
    LOWER_CASE_WITH_DASHES{

        @Override
        public String translateName(Field field) {
            return .separateCamelCase(field.getName(), "-").toLowerCase(Locale.ENGLISH);
        }
    };
    

    private FieldNamingPolicy() {
    }

    static String separateCamelCase(String string, String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (Character.isUpperCase(c2) && stringBuilder.length() != 0) {
                stringBuilder.append(string2);
            }
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    static String upperCaseFirstLetter(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        char c2 = string.charAt(n2);
        while (n2 < string.length() - 1 && !Character.isLetter(c2)) {
            stringBuilder.append(c2);
            c2 = string.charAt(++n2);
        }
        if (n2 == string.length()) {
            return stringBuilder.toString();
        }
        if (!Character.isUpperCase(c2)) {
            String string2 = FieldNamingPolicy.modifyString(Character.toUpperCase(c2), string, ++n2);
            return stringBuilder.append(string2).toString();
        }
        return string;
    }

    private static String modifyString(char c2, String string, int n2) {
        return n2 < string.length() ? "" + c2 + string.substring(n2) : String.valueOf(c2);
    }

}

