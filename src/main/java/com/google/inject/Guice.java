/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.internal.InternalInjectorCreator;
import java.util.Arrays;

public final class Guice {
    private Guice() {
    }

    public static /* varargs */ Injector createInjector(Module ... arrmodule) {
        return Guice.createInjector(Arrays.asList(arrmodule));
    }

    public static Injector createInjector(Iterable<? extends Module> iterable) {
        return Guice.createInjector(Stage.DEVELOPMENT, iterable);
    }

    public static /* varargs */ Injector createInjector(Stage stage, Module ... arrmodule) {
        return Guice.createInjector(stage, Arrays.asList(arrmodule));
    }

    public static Injector createInjector(Stage stage, Iterable<? extends Module> iterable) {
        return new InternalInjectorCreator().stage(stage).addModules(iterable).build();
    }
}

