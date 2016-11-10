/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer.utils;

import org.apache.xml.serializer.utils.Messages;

public final class Utils {
    public static final Messages messages;
    static Class class$org$apache$xml$serializer$utils$SerializerMessages;

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static {
        Class class_ = class$org$apache$xml$serializer$utils$SerializerMessages == null ? (Utils.class$org$apache$xml$serializer$utils$SerializerMessages = Utils.class$("org.apache.xml.serializer.utils.SerializerMessages")) : class$org$apache$xml$serializer$utils$SerializerMessages;
        messages = new Messages(class_.getName());
    }
}

