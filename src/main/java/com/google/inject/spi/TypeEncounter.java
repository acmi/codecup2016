/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.Message;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;

public interface TypeEncounter<I> {
    public /* varargs */ void addError(String var1, Object ... var2);

    public void addError(Throwable var1);

    public void addError(Message var1);

    public <T> Provider<T> getProvider(Key<T> var1);

    public <T> Provider<T> getProvider(Class<T> var1);

    public <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> var1);

    public <T> MembersInjector<T> getMembersInjector(Class<T> var1);

    public void register(MembersInjector<? super I> var1);

    public void register(InjectionListener<? super I> var1);

    public /* varargs */ void bindInterceptor(Matcher<? super Method> var1, MethodInterceptor ... var2);
}

