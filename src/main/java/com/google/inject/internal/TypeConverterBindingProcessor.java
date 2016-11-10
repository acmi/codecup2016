/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.TypeLiteral;
import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Errors;
import com.google.inject.internal.InjectorImpl;
import com.google.inject.internal.State;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeConverterBinding;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

final class TypeConverterBindingProcessor
extends AbstractProcessor {
    TypeConverterBindingProcessor(Errors errors) {
        super(errors);
    }

    static void prepareBuiltInConverters(InjectorImpl injectorImpl) {
        TypeConverterBindingProcessor.convertToPrimitiveType(injectorImpl, Integer.TYPE, Integer.class);
        TypeConverterBindingProcessor.convertToPrimitiveType(injectorImpl, Long.TYPE, Long.class);
        TypeConverterBindingProcessor.convertToPrimitiveType(injectorImpl, Boolean.TYPE, Boolean.class);
        TypeConverterBindingProcessor.convertToPrimitiveType(injectorImpl, Byte.TYPE, Byte.class);
        TypeConverterBindingProcessor.convertToPrimitiveType(injectorImpl, Short.TYPE, Short.class);
        TypeConverterBindingProcessor.convertToPrimitiveType(injectorImpl, Float.TYPE, Float.class);
        TypeConverterBindingProcessor.convertToPrimitiveType(injectorImpl, Double.TYPE, Double.class);
        TypeConverterBindingProcessor.convertToClass(injectorImpl, Character.class, new TypeConverter(){

            @Override
            public Object convert(String string, TypeLiteral<?> typeLiteral) {
                if ((string = string.trim()).length() != 1) {
                    throw new RuntimeException("Length != 1.");
                }
                return Character.valueOf(string.charAt(0));
            }

            public String toString() {
                return "TypeConverter<Character>";
            }
        });
        TypeConverterBindingProcessor.convertToClasses(injectorImpl, Matchers.subclassesOf(Enum.class), new TypeConverter(){

            @Override
            public Object convert(String string, TypeLiteral<?> typeLiteral) {
                return Enum.valueOf(typeLiteral.getRawType(), string);
            }

            public String toString() {
                return "TypeConverter<E extends Enum<E>>";
            }
        });
        TypeConverterBindingProcessor.internalConvertToTypes(injectorImpl, new AbstractMatcher<TypeLiteral<?>>(){

            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                return typeLiteral.getRawType() == Class.class;
            }

            public String toString() {
                return "Class<?>";
            }
        }, new TypeConverter(){

            @Override
            public Object convert(String string, TypeLiteral<?> typeLiteral) {
                try {
                    return Class.forName(string);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new RuntimeException(classNotFoundException.getMessage());
                }
            }

            public String toString() {
                return "TypeConverter<Class<?>>";
            }
        });
    }

    private static <T> void convertToPrimitiveType(InjectorImpl injectorImpl, Class<T> class_, final Class<T> class_2) {
        try {
            final Method method = class_2.getMethod("parse" + TypeConverterBindingProcessor.capitalize(class_.getName()), String.class);
            TypeConverter typeConverter = new TypeConverter(){

                @Override
                public Object convert(String string, TypeLiteral<?> typeLiteral) {
                    try {
                        return method.invoke(null, string);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        throw new AssertionError(illegalAccessException);
                    }
                    catch (InvocationTargetException invocationTargetException) {
                        throw new RuntimeException(invocationTargetException.getTargetException().getMessage());
                    }
                }

                public String toString() {
                    return "TypeConverter<" + class_2.getSimpleName() + ">";
                }
            };
            TypeConverterBindingProcessor.convertToClass(injectorImpl, class_2, typeConverter);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new AssertionError(noSuchMethodException);
        }
    }

    private static <T> void convertToClass(InjectorImpl injectorImpl, Class<T> class_, TypeConverter typeConverter) {
        TypeConverterBindingProcessor.convertToClasses(injectorImpl, Matchers.identicalTo(class_), typeConverter);
    }

    private static void convertToClasses(InjectorImpl injectorImpl, final Matcher<? super Class<?>> matcher, TypeConverter typeConverter) {
        TypeConverterBindingProcessor.internalConvertToTypes(injectorImpl, new AbstractMatcher<TypeLiteral<?>>(){

            @Override
            public boolean matches(TypeLiteral<?> typeLiteral) {
                Type type = typeLiteral.getType();
                if (!(type instanceof Class)) {
                    return false;
                }
                Class class_ = (Class)type;
                return matcher.matches(class_);
            }

            public String toString() {
                return matcher.toString();
            }
        }, typeConverter);
    }

    private static void internalConvertToTypes(InjectorImpl injectorImpl, Matcher<? super TypeLiteral<?>> matcher, TypeConverter typeConverter) {
        injectorImpl.state.addConverter(new TypeConverterBinding(SourceProvider.UNKNOWN_SOURCE, matcher, typeConverter));
    }

    @Override
    public Boolean visit(TypeConverterBinding typeConverterBinding) {
        this.injector.state.addConverter(new TypeConverterBinding(typeConverterBinding.getSource(), typeConverterBinding.getTypeMatcher(), typeConverterBinding.getTypeConverter()));
        return true;
    }

    private static String capitalize(String string) {
        char c2;
        if (string.length() == 0) {
            return string;
        }
        char c3 = string.charAt(0);
        return c3 == (c2 = Character.toUpperCase(c3)) ? string : "" + c2 + string.substring(1);
    }

}

