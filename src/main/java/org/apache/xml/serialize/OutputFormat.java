/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.UnsupportedEncodingException;
import org.apache.xml.serialize.EncodingInfo;
import org.apache.xml.serialize.Encodings;

public class OutputFormat {
    private String _method;
    private String _version;
    private int _indent = 0;
    private String _encoding = "UTF-8";
    private EncodingInfo _encodingInfo = null;
    private boolean _allowJavaNames = false;
    private String _doctypeSystem;
    private String _doctypePublic;
    private boolean _omitXmlDeclaration = false;
    private boolean _omitDoctype = false;
    private boolean _omitComments = false;
    private boolean _standalone = false;
    private String[] _cdataElements;
    private String[] _nonEscapingElements;
    private String _lineSeparator = "\n";
    private int _lineWidth = 72;
    private boolean _preserve = false;
    private boolean _preserveEmptyAttributes = false;

    public OutputFormat() {
    }

    public OutputFormat(String string, String string2, boolean bl) {
        this.setMethod(string);
        this.setEncoding(string2);
        this.setIndenting(bl);
    }

    public void setMethod(String string) {
        this._method = string;
    }

    public String getVersion() {
        return this._version;
    }

    public void setVersion(String string) {
        this._version = string;
    }

    public int getIndent() {
        return this._indent;
    }

    public boolean getIndenting() {
        return this._indent > 0;
    }

    public void setIndenting(boolean bl) {
        if (bl) {
            this._indent = 4;
            this._lineWidth = 72;
        } else {
            this._indent = 0;
            this._lineWidth = 0;
        }
    }

    public String getEncoding() {
        return this._encoding;
    }

    public void setEncoding(String string) {
        this._encoding = string;
        this._encodingInfo = null;
    }

    public EncodingInfo getEncodingInfo() throws UnsupportedEncodingException {
        if (this._encodingInfo == null) {
            this._encodingInfo = Encodings.getEncodingInfo(this._encoding, this._allowJavaNames);
        }
        return this._encodingInfo;
    }

    public String getDoctypePublic() {
        return this._doctypePublic;
    }

    public String getDoctypeSystem() {
        return this._doctypeSystem;
    }

    public boolean getOmitComments() {
        return this._omitComments;
    }

    public void setOmitComments(boolean bl) {
        this._omitComments = bl;
    }

    public boolean getOmitDocumentType() {
        return this._omitDoctype;
    }

    public boolean getOmitXMLDeclaration() {
        return this._omitXmlDeclaration;
    }

    public void setOmitXMLDeclaration(boolean bl) {
        this._omitXmlDeclaration = bl;
    }

    public boolean getStandalone() {
        return this._standalone;
    }

    public boolean isCDataElement(String string) {
        if (this._cdataElements == null) {
            return false;
        }
        int n2 = 0;
        while (n2 < this._cdataElements.length) {
            if (this._cdataElements[n2].equals(string)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public boolean isNonEscapingElement(String string) {
        if (this._nonEscapingElements == null) {
            return false;
        }
        int n2 = 0;
        while (n2 < this._nonEscapingElements.length) {
            if (this._nonEscapingElements[n2].equals(string)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public String getLineSeparator() {
        return this._lineSeparator;
    }

    public void setLineSeparator(String string) {
        this._lineSeparator = string == null ? "\n" : string;
    }

    public boolean getPreserveSpace() {
        return this._preserve;
    }

    public int getLineWidth() {
        return this._lineWidth;
    }

    public boolean getPreserveEmptyAttributes() {
        return this._preserveEmptyAttributes;
    }
}

