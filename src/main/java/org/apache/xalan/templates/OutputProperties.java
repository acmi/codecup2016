/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.OutputPropertyUtils;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.QName;

public class OutputProperties
extends ElemTemplateElement
implements Cloneable {
    static final long serialVersionUID = -6975274363881785488L;
    private Properties m_properties = null;

    public OutputProperties() {
        this("xml");
    }

    public OutputProperties(Properties properties) {
        this.m_properties = new Properties(properties);
    }

    public OutputProperties(String string) {
        this.m_properties = new Properties(OutputPropertiesFactory.getDefaultMethodProperties(string));
    }

    public Object clone() {
        try {
            OutputProperties outputProperties = (OutputProperties)Object.super.clone();
            outputProperties.m_properties = (Properties)outputProperties.m_properties.clone();
            return outputProperties;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public void setProperty(QName qName, String string) {
        this.setProperty(qName.toNamespacedString(), string);
    }

    public void setProperty(String string, String string2) {
        if (string.equals("method")) {
            this.setMethodDefaults(string2);
        }
        if (string.startsWith("{http://xml.apache.org/xslt}")) {
            string = "{http://xml.apache.org/xalan}" + string.substring(OutputPropertiesFactory.S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL_LEN);
        }
        this.m_properties.put(string, string2);
    }

    public String getProperty(QName qName) {
        return this.m_properties.getProperty(qName.toNamespacedString());
    }

    public String getProperty(String string) {
        if (string.startsWith("{http://xml.apache.org/xslt}")) {
            string = "{http://xml.apache.org/xalan}" + string.substring(OutputPropertiesFactory.S_BUILTIN_OLD_EXTENSIONS_UNIVERSAL_LEN);
        }
        return this.m_properties.getProperty(string);
    }

    public void setBooleanProperty(QName qName, boolean bl) {
        this.m_properties.put(qName.toNamespacedString(), bl ? "yes" : "no");
    }

    public void setBooleanProperty(String string, boolean bl) {
        this.m_properties.put(string, bl ? "yes" : "no");
    }

    public boolean getBooleanProperty(QName qName) {
        return this.getBooleanProperty(qName.toNamespacedString());
    }

    public boolean getBooleanProperty(String string) {
        return OutputPropertyUtils.getBooleanProperty(string, this.m_properties);
    }

    public void setIntProperty(QName qName, int n2) {
        this.setIntProperty(qName.toNamespacedString(), n2);
    }

    public void setIntProperty(String string, int n2) {
        this.m_properties.put(string, Integer.toString(n2));
    }

    public int getIntProperty(QName qName) {
        return this.getIntProperty(qName.toNamespacedString());
    }

    public int getIntProperty(String string) {
        return OutputPropertyUtils.getIntProperty(string, this.m_properties);
    }

    public void setQNameProperty(QName qName, QName qName2) {
        this.setQNameProperty(qName.toNamespacedString(), qName2);
    }

    public void setMethodDefaults(String string) {
        String string2 = this.m_properties.getProperty("method");
        if (null == string2 || !string2.equals(string) || string2.equals("xml")) {
            Properties properties = this.m_properties;
            Properties properties2 = OutputPropertiesFactory.getDefaultMethodProperties(string);
            this.m_properties = new Properties(properties2);
            this.copyFrom(properties, false);
        }
    }

    public void setQNameProperty(String string, QName qName) {
        this.setProperty(string, qName.toNamespacedString());
    }

    public QName getQNameProperty(QName qName) {
        return this.getQNameProperty(qName.toNamespacedString());
    }

    public QName getQNameProperty(String string) {
        return OutputProperties.getQNameProperty(string, this.m_properties);
    }

    public static QName getQNameProperty(String string, Properties properties) {
        String string2 = properties.getProperty(string);
        if (null != string2) {
            return QName.getQNameFromString(string2);
        }
        return null;
    }

    public void setQNameProperties(QName qName, Vector vector) {
        this.setQNameProperties(qName.toNamespacedString(), vector);
    }

    public void setQNameProperties(String string, Vector vector) {
        int n2 = vector.size();
        FastStringBuffer fastStringBuffer = new FastStringBuffer(9, 9);
        for (int i2 = 0; i2 < n2; ++i2) {
            QName qName = (QName)vector.elementAt(i2);
            fastStringBuffer.append(qName.toNamespacedString());
            if (i2 >= n2 - 1) continue;
            fastStringBuffer.append(' ');
        }
        this.m_properties.put(string, fastStringBuffer.toString());
    }

    public Vector getQNameProperties(QName qName) {
        return this.getQNameProperties(qName.toNamespacedString());
    }

    public Vector getQNameProperties(String string) {
        return OutputProperties.getQNameProperties(string, this.m_properties);
    }

    public static Vector getQNameProperties(String string, Properties properties) {
        String string2 = properties.getProperty(string);
        if (null != string2) {
            Vector<QName> vector = new Vector<QName>();
            int n2 = string2.length();
            boolean bl = false;
            FastStringBuffer fastStringBuffer = new FastStringBuffer();
            for (int i2 = 0; i2 < n2; ++i2) {
                char c2 = string2.charAt(i2);
                if (Character.isWhitespace(c2)) {
                    if (!bl) {
                        if (fastStringBuffer.length() <= 0) continue;
                        QName qName = QName.getQNameFromString(fastStringBuffer.toString());
                        vector.addElement(qName);
                        fastStringBuffer.reset();
                        continue;
                    }
                } else if ('{' == c2) {
                    bl = true;
                } else if ('}' == c2) {
                    bl = false;
                }
                fastStringBuffer.append(c2);
            }
            if (fastStringBuffer.length() > 0) {
                QName qName = QName.getQNameFromString(fastStringBuffer.toString());
                vector.addElement(qName);
                fastStringBuffer.reset();
            }
            return vector;
        }
        return null;
    }

    public void recompose(StylesheetRoot stylesheetRoot) throws TransformerException {
        stylesheetRoot.recomposeOutput(this);
    }

    public void compose(StylesheetRoot stylesheetRoot) throws TransformerException {
        super.compose(stylesheetRoot);
    }

    public Properties getProperties() {
        return this.m_properties;
    }

    public void copyFrom(Properties properties) {
        this.copyFrom(properties, true);
    }

    public void copyFrom(Properties properties, boolean bl) {
        Enumeration enumeration = properties.keys();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            if (!OutputProperties.isLegalPropertyKey(string)) {
                throw new IllegalArgumentException(XSLMessages.createMessage("ER_OUTPUT_PROPERTY_NOT_RECOGNIZED", new Object[]{string}));
            }
            Object v2 = this.m_properties.get(string);
            if (null == v2) {
                String string2 = (String)properties.get(string);
                if (bl && string.equals("method")) {
                    this.setMethodDefaults(string2);
                }
                this.m_properties.put(string, string2);
                continue;
            }
            if (!string.equals("cdata-section-elements")) continue;
            this.m_properties.put(string, (String)v2 + " " + (String)properties.get(string));
        }
    }

    public void copyFrom(OutputProperties outputProperties) throws TransformerException {
        this.copyFrom(outputProperties.getProperties());
    }

    public static boolean isLegalPropertyKey(String string) {
        return string.equals("cdata-section-elements") || string.equals("doctype-public") || string.equals("doctype-system") || string.equals("encoding") || string.equals("indent") || string.equals("media-type") || string.equals("method") || string.equals("omit-xml-declaration") || string.equals("standalone") || string.equals("version") || string.length() > 0 && string.charAt(0) == '{' && string.lastIndexOf(123) == 0 && string.indexOf(125) > 0 && string.lastIndexOf(125) == string.indexOf(125);
    }

    public static Properties getDefaultMethodProperties(String string) {
        return OutputPropertiesFactory.getDefaultMethodProperties(string);
    }
}

