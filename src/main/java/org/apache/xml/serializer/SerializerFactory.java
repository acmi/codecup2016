/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.util.Hashtable;
import java.util.Properties;
import org.apache.xml.serializer.ObjectFactory;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerConstants;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;
import org.xml.sax.ContentHandler;

public final class SerializerFactory {
    private static Hashtable m_formats = new Hashtable();

    public static Serializer getSerializer(Properties properties) {
        Serializer serializer;
        block6 : {
            try {
                Object object;
                String string = properties.getProperty("method");
                if (string == null) {
                    String string2 = Utils.messages.createMessage("ER_FACTORY_PROPERTY_MISSING", new Object[]{"method"});
                    throw new IllegalArgumentException(string2);
                }
                String string3 = properties.getProperty("{http://xml.apache.org/xalan}content-handler");
                if (null == string3 && null == (string3 = (object = OutputPropertiesFactory.getDefaultMethodProperties(string)).getProperty("{http://xml.apache.org/xalan}content-handler"))) {
                    String string4 = Utils.messages.createMessage("ER_FACTORY_PROPERTY_MISSING", new Object[]{"{http://xml.apache.org/xalan}content-handler"});
                    throw new IllegalArgumentException(string4);
                }
                object = ObjectFactory.findClassLoader();
                Class class_ = ObjectFactory.findProviderClass(string3, (ClassLoader)object, true);
                Object t2 = class_.newInstance();
                if (t2 instanceof SerializationHandler) {
                    serializer = (Serializer)class_.newInstance();
                    serializer.setOutputFormat(properties);
                    break block6;
                }
                if (t2 instanceof ContentHandler) {
                    string3 = SerializerConstants.DEFAULT_SAX_SERIALIZER;
                    class_ = ObjectFactory.findProviderClass(string3, (ClassLoader)object, true);
                    SerializationHandler serializationHandler = (SerializationHandler)class_.newInstance();
                    serializationHandler.setContentHandler((ContentHandler)t2);
                    serializationHandler.setOutputFormat(properties);
                    serializer = serializationHandler;
                    break block6;
                }
                throw new Exception(Utils.messages.createMessage("ER_SERIALIZER_NOT_CONTENTHANDLER", new Object[]{string3}));
            }
            catch (Exception exception) {
                throw new WrappedRuntimeException(exception);
            }
        }
        return serializer;
    }
}

