/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream;

import javax.xml.stream.XMLStreamException;

public interface XMLStreamWriter {
    public void flush() throws XMLStreamException;

    public void writeAttribute(String var1, String var2) throws XMLStreamException;

    public void writeAttribute(String var1, String var2, String var3) throws XMLStreamException;

    public void writeAttribute(String var1, String var2, String var3, String var4) throws XMLStreamException;

    public void writeCData(String var1) throws XMLStreamException;

    public void writeCharacters(char[] var1, int var2, int var3) throws XMLStreamException;

    public void writeCharacters(String var1) throws XMLStreamException;

    public void writeComment(String var1) throws XMLStreamException;

    public void writeDefaultNamespace(String var1) throws XMLStreamException;

    public void writeDTD(String var1) throws XMLStreamException;

    public void writeEndDocument() throws XMLStreamException;

    public void writeEndElement() throws XMLStreamException;

    public void writeEntityRef(String var1) throws XMLStreamException;

    public void writeNamespace(String var1, String var2) throws XMLStreamException;

    public void writeProcessingInstruction(String var1) throws XMLStreamException;

    public void writeProcessingInstruction(String var1, String var2) throws XMLStreamException;

    public void writeStartDocument(String var1, String var2) throws XMLStreamException;

    public void writeStartElement(String var1) throws XMLStreamException;

    public void writeStartElement(String var1, String var2) throws XMLStreamException;

    public void writeStartElement(String var1, String var2, String var3) throws XMLStreamException;
}

