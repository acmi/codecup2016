/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal;

import com.google.inject.Guice;
import com.google.inject.internal.AbstractProcessor;
import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

final class MessageProcessor
extends AbstractProcessor {
    private static final Logger logger = Logger.getLogger(Guice.class.getName());

    MessageProcessor(Errors errors) {
        super(errors);
    }

    @Override
    public Boolean visit(Message message) {
        if (message.getCause() != null) {
            String string = MessageProcessor.getRootMessage(message.getCause());
            logger.log(Level.INFO, "An exception was caught and reported. Message: " + string, message.getCause());
        }
        this.errors.addMessage(message);
        return true;
    }

    public static String getRootMessage(Throwable throwable) {
        Throwable throwable2 = throwable.getCause();
        return throwable2 == null ? throwable.toString() : MessageProcessor.getRootMessage(throwable2);
    }
}

