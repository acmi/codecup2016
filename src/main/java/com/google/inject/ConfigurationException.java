/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;
import java.util.Collection;

public final class ConfigurationException
extends RuntimeException {
    private final ImmutableSet<Message> messages;
    private Object partialValue = null;
    private static final long serialVersionUID = 0;

    public ConfigurationException(Iterable<Message> iterable) {
        this.messages = ImmutableSet.copyOf(iterable);
        this.initCause(Errors.getOnlyCause(this.messages));
    }

    public ConfigurationException withPartialValue(Object object) {
        Preconditions.checkState(this.partialValue == null, "Can't clobber existing partial value %s with %s", this.partialValue, object);
        ConfigurationException configurationException = new ConfigurationException(this.messages);
        configurationException.partialValue = object;
        return configurationException;
    }

    public Collection<Message> getErrorMessages() {
        return this.messages;
    }

    public <E> E getPartialValue() {
        return (E)this.partialValue;
    }

    @Override
    public String getMessage() {
        return Errors.format("Guice configuration errors", this.messages);
    }
}

