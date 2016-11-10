/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.events.XMLEvent;

public interface StartElement
extends XMLEvent {
    public Iterator getAttributes();

    public QName getName();

    public NamespaceContext getNamespaceContext();

    public Iterator getNamespaces();
}

