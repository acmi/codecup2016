/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.xml.sax.ContentHandler;

public class SerializerSwitcher {
    public static void switchSerializerIfHTML(TransformerImpl transformerImpl, String string, String string2) throws TransformerException {
        if (null == transformerImpl) {
            return;
        }
        if ((null == string || string.length() == 0) && string2.equalsIgnoreCase("html")) {
            if (null != transformerImpl.getOutputPropertyNoDefault("method")) {
                return;
            }
            Properties properties = transformerImpl.getOutputFormat().getProperties();
            OutputProperties outputProperties = new OutputProperties("html");
            outputProperties.copyFrom(properties, true);
            Properties properties2 = outputProperties.getProperties();
            try {
                Serializer serializer = null;
                if (null != serializer) {
                    Object object;
                    Serializer serializer2 = SerializerFactory.getSerializer(properties2);
                    Writer writer = serializer.getWriter();
                    if (null != writer) {
                        serializer2.setWriter(writer);
                    } else {
                        object = serializer.getOutputStream();
                        if (null != object) {
                            serializer2.setOutputStream((OutputStream)object);
                        }
                    }
                    object = serializer2.asContentHandler();
                    transformerImpl.setContentHandler((ContentHandler)object);
                }
            }
            catch (IOException iOException) {
                throw new TransformerException(iOException);
            }
        }
    }

    private static String getOutputPropertyNoDefault(String string, Properties properties) throws IllegalArgumentException {
        String string2 = (String)properties.get(string);
        return string2;
    }

    public static Serializer switchSerializerIfHTML(String string, String string2, Properties properties, Serializer serializer) throws TransformerException {
        Serializer serializer2 = serializer;
        if ((null == string || string.length() == 0) && string2.equalsIgnoreCase("html")) {
            if (null != SerializerSwitcher.getOutputPropertyNoDefault("method", properties)) {
                return serializer2;
            }
            Properties properties2 = properties;
            OutputProperties outputProperties = new OutputProperties("html");
            outputProperties.copyFrom(properties2, true);
            Properties properties3 = outputProperties.getProperties();
            if (null != serializer) {
                Serializer serializer3 = SerializerFactory.getSerializer(properties3);
                Writer writer = serializer.getWriter();
                if (null != writer) {
                    serializer3.setWriter(writer);
                } else {
                    OutputStream outputStream = serializer3.getOutputStream();
                    if (null != outputStream) {
                        serializer3.setOutputStream(outputStream);
                    }
                }
                serializer2 = serializer3;
            }
        }
        return serializer2;
    }
}

