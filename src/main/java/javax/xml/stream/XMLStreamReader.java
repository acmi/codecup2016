/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;

public interface XMLStreamReader {
    public int getAttributeCount();

    public String getAttributeLocalName(int var1);

    public String getAttributeNamespace(int var1);

    public String getAttributePrefix(int var1);

    public String getAttributeType(int var1);

    public String getAttributeValue(int var1);

    public String getCharacterEncodingScheme();

    public int getEventType();

    public String getLocalName();

    public Location getLocation();

    public NamespaceContext getNamespaceContext();

    public int getNamespaceCount();

    public String getNamespacePrefix(int var1);

    public String getNamespaceURI();

    public String getPIData();

    public String getPITarget();

    public String getPrefix();

    public Object getProperty(String var1) throws IllegalArgumentException;

    public String getText();

    public char[] getTextCharacters();

    public int getTextLength();

    public int getTextStart();

    public String getVersion();

    public boolean hasNext() throws XMLStreamException;

    public boolean isAttributeSpecified(int var1);

    public int next() throws XMLStreamException;

    public boolean standaloneSet();
}

