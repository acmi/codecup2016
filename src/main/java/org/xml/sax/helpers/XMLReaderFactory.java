/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.NewInstance;
import org.xml.sax.helpers.ParserAdapter;
import org.xml.sax.helpers.ParserFactory;
import org.xml.sax.helpers.SecuritySupport;

public final class XMLReaderFactory {
    static Class class$org$xml$sax$helpers$XMLReaderFactory;

    private XMLReaderFactory() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static XMLReader createXMLReader() throws SAXException {
        var0 = null;
        var1_1 = NewInstance.getClassLoader();
        try {
            var0 = SecuritySupport.getSystemProperty("org.xml.sax.driver");
        }
        catch (Exception var2_2) {
            // empty catch block
        }
        if (var0 == null || var0.length() == 0) {
            var2_3 = "META-INF/services/org.xml.sax.driver";
            var3_5 = null;
            var0 = null;
            var4_6 = SecuritySupport.getContextClassLoader();
            if (var4_6 != null) {
                var3_5 = SecuritySupport.getResourceAsStream(var4_6, var2_3);
                if (var3_5 == null) {
                    v0 = XMLReaderFactory.class$org$xml$sax$helpers$XMLReaderFactory == null ? (XMLReaderFactory.class$org$xml$sax$helpers$XMLReaderFactory = XMLReaderFactory.class$("org.xml.sax.helpers.XMLReaderFactory")) : XMLReaderFactory.class$org$xml$sax$helpers$XMLReaderFactory;
                    var4_6 = v0.getClassLoader();
                    var3_5 = SecuritySupport.getResourceAsStream(var4_6, var2_3);
                }
            } else {
                v1 = XMLReaderFactory.class$org$xml$sax$helpers$XMLReaderFactory == null ? (XMLReaderFactory.class$org$xml$sax$helpers$XMLReaderFactory = XMLReaderFactory.class$("org.xml.sax.helpers.XMLReaderFactory")) : XMLReaderFactory.class$org$xml$sax$helpers$XMLReaderFactory;
                var4_6 = v1.getClassLoader();
                var3_5 = SecuritySupport.getResourceAsStream(var4_6, var2_3);
            }
            if (var3_5 != null) {
                try {
                    var5_7 = new BufferedReader(new InputStreamReader(var3_5, "UTF-8"), 80);
                }
                catch (UnsupportedEncodingException var6_8) {
                    var5_7 = new BufferedReader(new InputStreamReader(var3_5), 80);
                }
                try {
                    try {
                        var0 = var5_7.readLine();
                    }
                    catch (Exception var6_9) {
                        var8_11 = null;
                        try {}
                        catch (IOException var9_14) {}
                        var5_7.close();
                    }
                    var8_10 = null;
                    var5_7.close();
                    catch (IOException var9_13) {}
                }
                catch (Throwable var7_16) {
                    var8_12 = null;
                    ** try [egrp 4[TRYBLOCK] [4 : 203->211)] { 
lbl51: // 1 sources:
                    var5_7.close();
                    throw var7_16;
lbl53: // 1 sources:
                    catch (IOException var9_15) {
                        // empty catch block
                    }
                    throw var7_16;
                }
            }
        }
        if (var0 == null) {
            var0 = "org.apache.xerces.parsers.SAXParser";
        }
        if (var0 != null) {
            return XMLReaderFactory.loadClass(var1_1, var0);
        }
        try {
            return new ParserAdapter(ParserFactory.makeParser());
        }
        catch (Exception var2_4) {
            throw new SAXException("Can't create default XMLReader; is system property org.xml.sax.driver set?");
        }
    }

    private static XMLReader loadClass(ClassLoader classLoader, String string) throws SAXException {
        try {
            return (XMLReader)NewInstance.newInstance(classLoader, string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new SAXException("SAX2 driver class " + string + " not found", classNotFoundException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new SAXException("SAX2 driver class " + string + " found but cannot be loaded", illegalAccessException);
        }
        catch (InstantiationException instantiationException) {
            throw new SAXException("SAX2 driver class " + string + " loaded but cannot be instantiated (no empty public constructor?)", instantiationException);
        }
        catch (ClassCastException classCastException) {
            throw new SAXException("SAX2 driver class " + string + " does not implement XMLReader", classCastException);
        }
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

