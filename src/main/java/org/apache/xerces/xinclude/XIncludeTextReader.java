/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xinclude;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.io.ASCIIReader;
import org.apache.xerces.impl.io.Latin1Reader;
import org.apache.xerces.impl.io.UTF16Reader;
import org.apache.xerces.impl.io.UTF8Reader;
import org.apache.xerces.util.EncodingMap;
import org.apache.xerces.util.HTTPInputSource;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XIncludeTextReader {
    private Reader fReader;
    private final XIncludeHandler fHandler;
    private XMLInputSource fSource;
    private XMLErrorReporter fErrorReporter;
    private XMLString fTempString = new XMLString();

    public XIncludeTextReader(XMLInputSource xMLInputSource, XIncludeHandler xIncludeHandler, int n2) throws IOException {
        this.fHandler = xIncludeHandler;
        this.fSource = xMLInputSource;
        this.fTempString = new XMLString(new char[n2 + 1], 0, 0);
    }

    public void setErrorReporter(XMLErrorReporter xMLErrorReporter) {
        this.fErrorReporter = xMLErrorReporter;
    }

    protected Reader getReader(XMLInputSource xMLInputSource) throws IOException {
        String string;
        Object object;
        Object object2;
        if (xMLInputSource.getCharacterStream() != null) {
            return xMLInputSource.getCharacterStream();
        }
        InputStream inputStream = null;
        String string2 = xMLInputSource.getEncoding();
        if (string2 == null) {
            string2 = "UTF-8";
        }
        if (xMLInputSource.getByteStream() != null) {
            inputStream = xMLInputSource.getByteStream();
            if (!(inputStream instanceof BufferedInputStream)) {
                inputStream = new BufferedInputStream(inputStream, this.fTempString.ch.length);
            }
        } else {
            Object object3;
            Object object4;
            string = XMLEntityManager.expandSystemId(xMLInputSource.getSystemId(), xMLInputSource.getBaseSystemId(), false);
            object2 = new URL(string);
            object = object2.openConnection();
            if (object instanceof HttpURLConnection && xMLInputSource instanceof HTTPInputSource) {
                object4 = (HttpURLConnection)object;
                HTTPInputSource hTTPInputSource = (HTTPInputSource)xMLInputSource;
                object3 = hTTPInputSource.getHTTPRequestProperties();
                while (object3.hasNext()) {
                    Map.Entry entry = (Map.Entry)object3.next();
                    object4.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
                }
                boolean bl = hTTPInputSource.getFollowHTTPRedirects();
                if (!bl) {
                    object4.setInstanceFollowRedirects(bl);
                }
            }
            inputStream = new BufferedInputStream(object.getInputStream());
            object4 = object.getContentType();
            int n2 = object4 != null ? object4.indexOf(59) : -1;
            object3 = null;
            String string3 = null;
            if (n2 != -1) {
                object3 = object4.substring(0, n2).trim();
                string3 = object4.substring(n2 + 1).trim();
                if (string3.startsWith("charset=")) {
                    if ((string3 = string3.substring(8).trim()).charAt(0) == '\"' && string3.charAt(string3.length() - 1) == '\"' || string3.charAt(0) == '\'' && string3.charAt(string3.length() - 1) == '\'') {
                        string3 = string3.substring(1, string3.length() - 1);
                    }
                } else {
                    string3 = null;
                }
            } else {
                object3 = object4.trim();
            }
            String string4 = null;
            if (object3.equals("text/xml")) {
                string4 = string3 != null ? string3 : "US-ASCII";
            } else if (object3.equals("application/xml")) {
                string4 = string3 != null ? string3 : this.getEncodingName(inputStream);
            } else if (object3.endsWith("+xml")) {
                string4 = this.getEncodingName(inputStream);
            }
            if (string4 != null) {
                string2 = string4;
            }
        }
        string2 = string2.toUpperCase(Locale.ENGLISH);
        if ((string2 = this.consumeBOM(inputStream, string2)).equals("UTF-8")) {
            return this.createUTF8Reader(inputStream);
        }
        if (string2.equals("UTF-16BE")) {
            return this.createUTF16Reader(inputStream, true);
        }
        if (string2.equals("UTF-16LE")) {
            return this.createUTF16Reader(inputStream, false);
        }
        string = EncodingMap.getIANA2JavaMapping(string2);
        if (string == null) {
            object2 = this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210");
            object = this.fErrorReporter.getLocale();
            throw new IOException(object2.formatMessage((Locale)object, "EncodingDeclInvalid", new Object[]{string2}));
        }
        if (string.equals("ASCII")) {
            return this.createASCIIReader(inputStream);
        }
        if (string.equals("ISO8859_1")) {
            return this.createLatin1Reader(inputStream);
        }
        return new InputStreamReader(inputStream, string);
    }

    private Reader createUTF8Reader(InputStream inputStream) {
        return new UTF8Reader(inputStream, this.fTempString.ch.length, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
    }

    private Reader createUTF16Reader(InputStream inputStream, boolean bl) {
        return new UTF16Reader(inputStream, this.fTempString.ch.length << 1, bl, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
    }

    private Reader createASCIIReader(InputStream inputStream) {
        return new ASCIIReader(inputStream, this.fTempString.ch.length, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
    }

    private Reader createLatin1Reader(InputStream inputStream) {
        return new Latin1Reader(inputStream, this.fTempString.ch.length);
    }

    protected String getEncodingName(InputStream inputStream) throws IOException {
        byte[] arrby = new byte[4];
        String string = null;
        inputStream.mark(4);
        int n2 = inputStream.read(arrby, 0, 4);
        inputStream.reset();
        if (n2 == 4) {
            string = this.getEncodingName(arrby);
        }
        return string;
    }

    protected String consumeBOM(InputStream inputStream, String string) throws IOException {
        byte[] arrby = new byte[3];
        int n2 = 0;
        inputStream.mark(3);
        if (string.equals("UTF-8")) {
            n2 = inputStream.read(arrby, 0, 3);
            if (n2 == 3) {
                int n3 = arrby[0] & 255;
                int n4 = arrby[1] & 255;
                int n5 = arrby[2] & 255;
                if (n3 != 239 || n4 != 187 || n5 != 191) {
                    inputStream.reset();
                }
            } else {
                inputStream.reset();
            }
        } else if (string.startsWith("UTF-16")) {
            n2 = inputStream.read(arrby, 0, 2);
            if (n2 == 2) {
                int n6 = arrby[0] & 255;
                int n7 = arrby[1] & 255;
                if (n6 == 254 && n7 == 255) {
                    return "UTF-16BE";
                }
                if (n6 == 255 && n7 == 254) {
                    return "UTF-16LE";
                }
            }
            inputStream.reset();
        }
        return string;
    }

    protected String getEncodingName(byte[] arrby) {
        int n2 = arrby[0] & 255;
        int n3 = arrby[1] & 255;
        if (n2 == 254 && n3 == 255) {
            return "UTF-16BE";
        }
        if (n2 == 255 && n3 == 254) {
            return "UTF-16LE";
        }
        int n4 = arrby[2] & 255;
        if (n2 == 239 && n3 == 187 && n4 == 191) {
            return "UTF-8";
        }
        int n5 = arrby[3] & 255;
        if (n2 == 0 && n3 == 0 && n4 == 0 && n5 == 60) {
            return "ISO-10646-UCS-4";
        }
        if (n2 == 60 && n3 == 0 && n4 == 0 && n5 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (n2 == 0 && n3 == 0 && n4 == 60 && n5 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (n2 == 0 && n3 == 60 && n4 == 0 && n5 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (n2 == 0 && n3 == 60 && n4 == 0 && n5 == 63) {
            return "UTF-16BE";
        }
        if (n2 == 60 && n3 == 0 && n4 == 63 && n5 == 0) {
            return "UTF-16LE";
        }
        if (n2 == 76 && n3 == 111 && n4 == 167 && n5 == 148) {
            return "CP037";
        }
        return null;
    }

    public void parse() throws IOException {
        this.fReader = this.getReader(this.fSource);
        this.fSource = null;
        int n2 = this.fReader.read(this.fTempString.ch, 0, this.fTempString.ch.length - 1);
        this.fHandler.fHasIncludeReportedContent = true;
        while (n2 != -1) {
            int n3 = 0;
            while (n3 < n2) {
                char c2 = this.fTempString.ch[n3];
                if (!this.isValid(c2)) {
                    if (XMLChar.isHighSurrogate(c2)) {
                        int n4;
                        if (++n3 < n2) {
                            n4 = this.fTempString.ch[n3];
                        } else {
                            n4 = this.fReader.read();
                            if (n4 != -1) {
                                this.fTempString.ch[n2++] = (char)n4;
                            }
                        }
                        if (XMLChar.isLowSurrogate(n4)) {
                            int n5 = XMLChar.supplemental(c2, (char)n4);
                            if (!this.isValid(n5)) {
                                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(n5, 16)}, 2);
                            }
                        } else {
                            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(n4, 16)}, 2);
                        }
                    } else {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(c2, 16)}, 2);
                    }
                }
                ++n3;
            }
            if (this.fHandler != null && n2 > 0) {
                this.fTempString.offset = 0;
                this.fTempString.length = n2;
                this.fHandler.characters(this.fTempString, this.fHandler.modifyAugmentations(null, true));
            }
            n2 = this.fReader.read(this.fTempString.ch, 0, this.fTempString.ch.length - 1);
        }
    }

    public void setInputSource(XMLInputSource xMLInputSource) {
        this.fSource = xMLInputSource;
    }

    public void close() throws IOException {
        if (this.fReader != null) {
            this.fReader.close();
            this.fReader = null;
        }
    }

    protected boolean isValid(int n2) {
        return XMLChar.isValid(n2);
    }

    protected void setBufferSize(int n2) {
        if (this.fTempString.ch.length != ++n2) {
            this.fTempString.ch = new char[n2];
        }
    }
}

