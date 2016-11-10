/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;
import java.util.Collection;

public final class ProvisionException
extends RuntimeException {
    private final ImmutableSet<Message> messages;
    private static final long serialVersionUID = 0;

    public ProvisionException(Iterable<Message> iterable) {
        this.messages = ImmutableSet.copyOf(iterable);
        Preconditions.checkArgument(!this.messages.isEmpty());
        this.initCause(Errors.getOnlyCause(this.messages));
    }

    public ProvisionException(String string, Throwable throwable) {
        super(throwable);
        this.messages = ImmutableSet.of(new Message(string, throwable));
    }

    public ProvisionException(String string) {
        this.messages = ImmutableSet.of(new Message(string));
    }

    public Collection<Message> getErrorMessages() {
        return this.messages;
    }

    @Override
    public String getMessage() {
        return Errors.format("Unable to provision, see the following errors", this.messages);
    }
}

