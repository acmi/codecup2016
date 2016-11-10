/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.serializer.SerializerBase;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;

public final class OutputPropertiesFactory {
    public static final int S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL_LEN = "{http://xml.apache.org/xslt}".length();
    private static final int S_XSLT_PREFIX_LEN = "xslt.output.".length();
    private static final int S_XALAN_PREFIX_LEN = "org.apache.xslt.".length();
    private static Integer m_synch_object = new Integer(1);
    private static final String PROP_DIR = SerializerBase.PKG_PATH + '/';
    private static Properties m_xml_properties = null;
    private static Properties m_html_properties = null;
    private static Properties m_text_properties = null;
    private static Properties m_unknown_properties = null;
    private static final Class ACCESS_CONTROLLER_CLASS = OutputPropertiesFactory.findAccessControllerClass();
    static Class class$org$apache$xml$serializer$OutputPropertiesFactory;

    private static Class findAccessControllerClass() {
        try {
            return Class.forName("java.security.AccessController");
        }
        catch (Exception exception) {
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final Properties getDefaultMethodProperties(String string) {
        String string2 = null;
        Properties properties = null;
        try {
            Object object = m_synch_object;
            synchronized (object) {
                if (null == m_xml_properties) {
                    string2 = "output_xml.properties";
                    m_xml_properties = OutputPropertiesFactory.loadPropertiesFile(string2, null);
                }
            }
            if (string.equals("xml")) {
                properties = m_xml_properties;
            } else if (string.equals("html")) {
                if (null == m_html_properties) {
                    string2 = "output_html.properties";
                    m_html_properties = OutputPropertiesFactory.loadPropertiesFile(string2, m_xml_properties);
                }
                properties = m_html_properties;
            } else if (string.equals("text")) {
                if (null == m_text_properties && null == (OutputPropertiesFactory.m_text_properties = OutputPropertiesFactory.loadPropertiesFile(string2 = "output_text.properties", m_xml_properties)).getProperty("encoding")) {
                    object = Encodings.getMimeEncoding(null);
                    m_text_properties.put("encoding", object);
                }
                properties = m_text_properties;
            } else if (string.equals("")) {
                if (null == m_unknown_properties) {
                    string2 = "output_unknown.properties";
                    m_unknown_properties = OutputPropertiesFactory.loadPropertiesFile(string2, m_xml_properties);
                }
                properties = m_unknown_properties;
            } else {
                properties = m_xml_properties;
            }
        }
        catch (IOException iOException) {
            throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_METHOD_PROPERTY", new Object[]{string2, string}), iOException);
        }
        return new Properties(properties);
    }

    private static Properties loadPropertiesFile(String string, Properties properties) throws IOException {
        Properties properties2 = new Properties(properties);
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            if (ACCESS_CONTROLLER_CLASS != null) {
                inputStream = (InputStream)AccessController.doPrivileged(new PrivilegedAction(string){
                    private final String val$resourceName;

                    public Object run() {
                        Class class_ = OutputPropertiesFactory.class$org$apache$xml$serializer$OutputPropertiesFactory == null ? (OutputPropertiesFactory.class$org$apache$xml$serializer$OutputPropertiesFactory = OutputPropertiesFactory.class$("org.apache.xml.serializer.OutputPropertiesFactory")) : OutputPropertiesFactory.class$org$apache$xml$serializer$OutputPropertiesFactory;
                        return class_.getResourceAsStream(this.val$resourceName);
                    }
                });
            } else {
                Class class_ = class$org$apache$xml$serializer$OutputPropertiesFactory == null ? (OutputPropertiesFactory.class$org$apache$xml$serializer$OutputPropertiesFactory = OutputPropertiesFactory.class$("org.apache.xml.serializer.OutputPropertiesFactory")) : class$org$apache$xml$serializer$OutputPropertiesFactory;
                inputStream = class_.getResourceAsStream(string);
            }
            bufferedInputStream = new BufferedInputStream(inputStream);
            properties2.load(bufferedInputStream);
        }
        catch (IOException iOException) {
            if (properties == null) {
                throw iOException;
            }
            throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_RESOURCE", new Object[]{string}), iOException);
        }
        catch (SecurityException securityException) {
            if (properties == null) {
                throw securityException;
            }
            throw new WrappedRuntimeException(Utils.messages.createMessage("ER_COULD_NOT_LOAD_RESOURCE", new Object[]{string}), securityException);
        }
        finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Enumeration enumeration = ((Properties)properties2.clone()).keys();
        while (enumeration.hasMoreElements()) {
            String string2 = (String)enumeration.nextElement();
            String string3 = null;
            try {
                string3 = System.getProperty(string2);
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
            if (string3 == null) {
                string3 = (String)properties2.get(string2);
            }
            String string4 = OutputPropertiesFactory.fixupPropertyString(string2, true);
            String string5 = null;
            try {
                string5 = System.getProperty(string4);
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
            string5 = string5 == null ? OutputPropertiesFactory.fixupPropertyString(string3, false) : OutputPropertiesFactory.fixupPropertyString(string5, false);
            if (string2 == string4 && string3 == string5) continue;
            properties2.remove(string2);
            properties2.put(string4, string5);
        }
        return properties2;
    }

    private static String fixupPropertyString(String string, boolean bl) {
        int n2;
        if (bl && string.startsWith("xslt.output.")) {
            string = string.substring(S_XSLT_PREFIX_LEN);
        }
        if (string.startsWith("org.apache.xslt.")) {
            string = "{http://xml.apache.org/xalan}" + string.substring(S_XALAN_PREFIX_LEN);
        }
        if ((n2 = string.indexOf("\\u003a")) > 0) {
            String string2 = string.substring(n2 + 6);
            string = string.substring(0, n2) + ":" + string2;
        }
        return string;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

}

