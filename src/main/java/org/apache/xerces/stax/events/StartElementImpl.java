/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import org.apache.xerces.stax.DefaultNamespaceContext;
import org.apache.xerces.stax.events.ElementImpl;

public final class StartElementImpl
extends ElementImpl
implements StartElement {
    private static final Comparator QNAME_COMPARATOR = new Comparator(){

        public int compare(Object object, Object object2) {
            if (object.equals(object2)) {
                return 0;
            }
            QName qName = (QName)object;
            QName qName2 = (QName)object2;
            return qName.toString().compareTo(qName2.toString());
        }
    };
    private final Map fAttributes;
    private final NamespaceContext fNamespaceContext;

    public StartElementImpl(QName qName, Iterator iterator, Iterator iterator2, NamespaceContext namespaceContext, Location location) {
        super(qName, true, iterator2, location);
        if (iterator != null && iterator.hasNext()) {
            this.fAttributes = new TreeMap(QNAME_COMPARATOR);
            do {
                Attribute attribute = (Attribute)iterator.next();
                this.fAttributes.put(attribute.getName(), attribute);
            } while (iterator.hasNext());
        } else {
            this.fAttributes = Collections.EMPTY_MAP;
        }
        this.fNamespaceContext = namespaceContext != null ? namespaceContext : DefaultNamespaceContext.getInstance();
    }

    public Iterator getAttributes() {
        return ElementImpl.createImmutableIterator(this.fAttributes.values().iterator());
    }

    public Attribute getAttributeByName(QName qName) {
        return (Attribute)this.fAttributes.get(qName);
    }

    public NamespaceContext getNamespaceContext() {
        return this.fNamespaceContext;
    }

    public String getNamespaceURI(String string) {
        return this.fNamespaceContext.getNamespaceURI(string);
    }

    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
        try {
            Object object;
            writer.write(60);
            QName qName = this.getName();
            String string = qName.getPrefix();
            if (string != null && string.length() > 0) {
                writer.write(string);
                writer.write(58);
            }
            writer.write(qName.getLocalPart());
            Iterator iterator = this.getNamespaces();
            while (iterator.hasNext()) {
                object = (Namespace)iterator.next();
                writer.write(32);
                object.writeAsEncodedUnicode(writer);
            }
            object = this.getAttributes();
            while (object.hasNext()) {
                Attribute attribute = (Attribute)object.next();
                writer.write(32);
                attribute.writeAsEncodedUnicode(writer);
            }
            writer.write(62);
        }
        catch (IOException iOException) {
            throw new XMLStreamException(iOException);
        }
    }

}

