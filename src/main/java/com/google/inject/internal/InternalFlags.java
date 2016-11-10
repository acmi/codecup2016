/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.logging.Logger;

public class InternalFlags {
    private static final Logger logger = Logger.getLogger(InternalFlags.class.getName());
    private static final IncludeStackTraceOption INCLUDE_STACK_TRACES = InternalFlags.parseIncludeStackTraceOption();
    private static final CustomClassLoadingOption CUSTOM_CLASS_LOADING = InternalFlags.parseCustomClassLoadingOption();
    private static final NullableProvidesOption NULLABLE_PROVIDES = InternalFlags.parseNullableProvidesOption(NullableProvidesOption.ERROR);

    public static IncludeStackTraceOption getIncludeStackTraceOption() {
        return INCLUDE_STACK_TRACES;
    }

    public static CustomClassLoadingOption getCustomClassLoadingOption() {
        return CUSTOM_CLASS_LOADING;
    }

    public static NullableProvidesOption getNullableProvidesOption() {
        return NULLABLE_PROVIDES;
    }

    private static IncludeStackTraceOption parseIncludeStackTraceOption() {
        return InternalFlags.getSystemOption("guice_include_stack_traces", IncludeStackTraceOption.ONLY_FOR_DECLARING_SOURCE);
    }

    private static CustomClassLoadingOption parseCustomClassLoadingOption() {
        return InternalFlags.getSystemOption("guice_custom_class_loading", CustomClassLoadingOption.BRIDGE, CustomClassLoadingOption.OFF);
    }

    private static NullableProvidesOption parseNullableProvidesOption(NullableProvidesOption nullableProvidesOption) {
        return InternalFlags.getSystemOption("guice_check_nullable_provides_params", nullableProvidesOption);
    }

    private static <T extends Enum<T>> T getSystemOption(String string, T t2) {
        return InternalFlags.getSystemOption(string, t2, t2);
    }

    private static <T extends Enum<T>> T getSystemOption(final String string, T object, T t2) {
        Class class_ = object.getDeclaringClass();
        String string2 = null;
        try {
            string2 = (String)AccessController.doPrivileged(new PrivilegedAction<String>(){

                @Override
                public String run() {
                    return System.getProperty(string);
                }
            });
            return (T)(string2 != null && string2.length() > 0 ? Enum.valueOf(class_, string2) : object);
        }
        catch (SecurityException securityException) {
            return t2;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            logger.warning(string2 + " is not a valid flag value for " + string + ". " + " Values must be one of " + Arrays.asList(class_.getEnumConstants()));
            return (T)object;
        }
    }

    public static enum NullableProvidesOption {
        IGNORE,
        WARN,
        ERROR;
        

        private NullableProvidesOption() {
        }
    }

    public static enum CustomClassLoadingOption {
        OFF,
        BRIDGE;
        

        private CustomClassLoadingOption() {
        }
    }

    public static enum IncludeStackTraceOption {
        OFF,
        ONLY_FOR_DECLARING_SOURCE,
        COMPLETE;
        

        private IncludeStackTraceOption() {
        }
    }

}

