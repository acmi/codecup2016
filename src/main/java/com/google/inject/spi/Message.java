/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.spi;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.internal.Errors;
import com.google.inject.internal.util.SourceProvider;
import com.google.inject.spi.Element;
import com.google.inject.spi.ElementVisitor;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;

public final class Message
implements Element,
Serializable {
    private final String message;
    private final Throwable cause;
    private final List<Object> sources;
    private static final long serialVersionUID = 0;

    public Message(List<Object> list, String string, Throwable throwable) {
        this.sources = ImmutableList.copyOf(list);
        this.message = Preconditions.checkNotNull(string, "message");
        this.cause = throwable;
    }

    public Message(String string, Throwable throwable) {
        this(ImmutableList.of(), string, throwable);
    }

    public Message(Object object, String string) {
        this(ImmutableList.of(object), string, null);
    }

    public Message(String string) {
        this(ImmutableList.of(), string, null);
    }

    @Override
    public String getSource() {
        return this.sources.isEmpty() ? SourceProvider.UNKNOWN_SOURCE.toString() : Errors.convert(this.sources.get(this.sources.size() - 1)).toString();
    }

    public List<Object> getSources() {
        return this.sources;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public <T> T acceptVisitor(ElementVisitor<T> elementVisitor) {
        return elementVisitor.visit(this);
    }

    public Throwable getCause() {
        return this.cause;
    }

    public String toString() {
        return this.message;
    }

    public int hashCode() {
        return Objects.hashCode(this.message, this.cause, this.sources);
    }

    public boolean equals(Object object) {
        if (!(object instanceof Message)) {
            return false;
        }
        Message message = (Message)object;
        return this.message.equals(message.message) && Objects.equal(this.cause, message.cause) && this.sources.equals(message.sources);
    }

    @Override
    public void applyTo(Binder binder) {
        binder.withSource(this.getSource()).addError(this);
    }

    private Object writeReplace() throws ObjectStreamException {
        Object[] arrobject = this.sources.toArray();
        for (int i2 = 0; i2 < arrobject.length; ++i2) {
            arrobject[i2] = Errors.convert(arrobject[i2]).toString();
        }
        return new Message(ImmutableList.copyOf(arrobject), this.message, this.cause);
    }
}

