/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.apache.xalan.serialize.DOMSerializer;
import org.apache.xalan.serialize.Serializer;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

public abstract class SerializerFactory {
    private SerializerFactory() {
    }

    public static Serializer getSerializer(Properties properties) {
        org.apache.xml.serializer.Serializer serializer = org.apache.xml.serializer.SerializerFactory.getSerializer(properties);
        SerializerWrapper serializerWrapper = new SerializerWrapper(serializer);
        return serializerWrapper;
    }

    private static class DOMSerializerWrapper
    implements DOMSerializer {
        private final org.apache.xml.serializer.DOMSerializer m_dom;

        DOMSerializerWrapper(org.apache.xml.serializer.DOMSerializer dOMSerializer) {
            this.m_dom = dOMSerializer;
        }

        public void serialize(Node node) throws IOException {
            this.m_dom.serialize(node);
        }
    }

    private static class SerializerWrapper
    implements Serializer {
        private final org.apache.xml.serializer.Serializer m_serializer;
        private DOMSerializer m_old_DOMSerializer;

        SerializerWrapper(org.apache.xml.serializer.Serializer serializer) {
            this.m_serializer = serializer;
        }

        public void setOutputStream(OutputStream outputStream) {
            this.m_serializer.setOutputStream(outputStream);
        }

        public OutputStream getOutputStream() {
            return this.m_serializer.getOutputStream();
        }

        public void setWriter(Writer writer) {
            this.m_serializer.setWriter(writer);
        }

        public Writer getWriter() {
            return this.m_serializer.getWriter();
        }

        public void setOutputFormat(Properties properties) {
            this.m_serializer.setOutputFormat(properties);
        }

        public Properties getOutputFormat() {
            return this.m_serializer.getOutputFormat();
        }

        public ContentHandler asContentHandler() throws IOException {
            return this.m_serializer.asContentHandler();
        }

        public DOMSerializer asDOMSerializer() throws IOException {
            if (this.m_old_DOMSerializer == null) {
                this.m_old_DOMSerializer = new DOMSerializerWrapper(this.m_serializer.asDOMSerializer());
            }
            return this.m_old_DOMSerializer;
        }

        public boolean reset() {
            return this.m_serializer.reset();
        }
    }

}

