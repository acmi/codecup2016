/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.AVTPart;
import org.apache.xalan.templates.AVTPartSimple;
import org.apache.xalan.templates.AVTPartXPath;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.XSLTVisitable;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;

public class AVT
implements Serializable,
XSLTVisitable {
    static final long serialVersionUID = 5167607155517042691L;
    private static final boolean USE_OBJECT_POOL = false;
    private static final int INIT_BUFFER_CHUNK_BITS = 8;
    private String m_simpleString;
    private Vector m_parts;
    private String m_rawName;
    private String m_name;
    private String m_uri;

    public String getRawName() {
        return this.m_rawName;
    }

    public void setRawName(String string) {
        this.m_rawName = string;
    }

    public String getName() {
        return this.m_name;
    }

    public void setName(String string) {
        this.m_name = string;
    }

    public String getURI() {
        return this.m_uri;
    }

    public void setURI(String string) {
        this.m_uri = string;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public AVT(StylesheetHandler var1_1, String var2_2, String var3_3, String var4_4, String var5_5, ElemTemplateElement var6_6) throws TransformerException {
        super();
        this.m_simpleString = null;
        this.m_parts = null;
        this.m_uri = var2_2;
        this.m_name = var3_3;
        this.m_rawName = var4_4;
        var7_7 = new StringTokenizer(var5_5, "{}\"'", true);
        var8_8 = var7_7.countTokens();
        if (var8_8 < 2) {
            this.m_simpleString = var5_5;
        } else {
            var9_9 = null;
            var10_10 = null;
            var9_9 = new FastStringBuffer(6);
            var10_10 = new FastStringBuffer(6);
            try {
                this.m_parts = new Vector<E>(var8_8 + 1);
                var11_11 = null;
                var12_12 = null;
                var13_13 = null;
                while (var7_7.hasMoreTokens()) {
                    if (var12_12 != null) {
                        var11_11 = var12_12;
                        var12_12 = null;
                    } else {
                        var11_11 = var7_7.nextToken();
                    }
                    if (var11_11.length() == 1) {
                        switch (var11_11.charAt(0)) {
                            case '\"': 
                            case '\'': {
                                var9_9.append(var11_11);
                                ** break;
                            }
                            case '{': {
                                try {
                                    var12_12 = var7_7.nextToken();
                                    if (var12_12.equals("{")) {
                                        var9_9.append(var12_12);
                                        var12_12 = null;
                                        ** break;
                                    }
                                    if (var9_9.length() > 0) {
                                        this.m_parts.addElement(new AVTPartSimple(var9_9.toString()));
                                        var9_9.setLength(0);
                                    }
                                    var10_10.setLength(0);
                                    block20 : while (null != var12_12) {
                                        if (var12_12.length() == 1) {
                                            switch (var12_12.charAt(0)) {
                                                case '\"': 
                                                case '\'': {
                                                    var10_10.append(var12_12);
                                                    var14_14 = var12_12;
                                                    var12_12 = var7_7.nextToken();
                                                    while (!var12_12.equals(var14_14)) {
                                                        var10_10.append(var12_12);
                                                        var12_12 = var7_7.nextToken();
                                                    }
                                                    var10_10.append(var12_12);
                                                    var12_12 = var7_7.nextToken();
                                                    continue block20;
                                                }
                                                case '{': {
                                                    var13_13 = XSLMessages.createMessage("ER_NO_CURLYBRACE", null);
                                                    var12_12 = null;
                                                    continue block20;
                                                }
                                                case '}': {
                                                    var9_9.setLength(0);
                                                    var14_14 = var1_1.createXPath(var10_10.toString(), var6_6);
                                                    this.m_parts.addElement(new AVTPartXPath((XPath)var14_14));
                                                    var12_12 = null;
                                                    continue block20;
                                                }
                                            }
                                            var10_10.append(var12_12);
                                            var12_12 = var7_7.nextToken();
                                            continue;
                                        }
                                        var10_10.append(var12_12);
                                        var12_12 = var7_7.nextToken();
                                    }
                                    if (var13_13 != null) break;
                                }
                                catch (NoSuchElementException var14_15) {
                                    var13_13 = XSLMessages.createMessage("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{var3_3, var5_5});
                                }
                                break;
                            }
                            case '}': {
                                var12_12 = var7_7.nextToken();
                                if (var12_12.equals("}")) {
                                    var9_9.append(var12_12);
                                    var12_12 = null;
                                    ** break;
                                }
                                try {
                                    var1_1.warn("WG_FOUND_CURLYBRACE", null);
                                }
                                catch (SAXException var14_16) {
                                    throw new TransformerException(var14_16);
                                }
                                var9_9.append("}");
                                ** break;
                            }
                            default: {
                                var9_9.append(var11_11);
                                ** break;
lbl94: // 5 sources:
                                break;
                            }
                        }
                    } else {
                        var9_9.append(var11_11);
                    }
                    if (null == var13_13) continue;
                    try {
                        var1_1.warn("WG_ATTR_TEMPLATE", new Object[]{var13_13});
                        break;
                    }
                    catch (SAXException var14_17) {
                        throw new TransformerException(var14_17);
                    }
                }
                if (var9_9.length() > 0) {
                    this.m_parts.addElement(new AVTPartSimple(var9_9.toString()));
                    var9_9.setLength(0);
                }
            }
            finally {
                var9_9 = null;
                var10_10 = null;
            }
        }
        if (null != this.m_parts) return;
        if (null != this.m_simpleString) return;
        this.m_simpleString = "";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getSimpleString() {
        if (null != this.m_simpleString) {
            return this.m_simpleString;
        }
        if (null != this.m_parts) {
            FastStringBuffer fastStringBuffer = this.getBuffer();
            String string = null;
            int n2 = this.m_parts.size();
            try {
                for (int i2 = 0; i2 < n2; ++i2) {
                    AVTPart aVTPart = (AVTPart)this.m_parts.elementAt(i2);
                    fastStringBuffer.append(aVTPart.getSimpleString());
                }
                string = fastStringBuffer.toString();
            }
            finally {
                fastStringBuffer.setLength(0);
            }
            return string;
        }
        return "";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String evaluate(XPathContext xPathContext, int n2, PrefixResolver prefixResolver) throws TransformerException {
        if (null != this.m_simpleString) {
            return this.m_simpleString;
        }
        if (null != this.m_parts) {
            FastStringBuffer fastStringBuffer = this.getBuffer();
            String string = null;
            int n3 = this.m_parts.size();
            try {
                for (int i2 = 0; i2 < n3; ++i2) {
                    AVTPart aVTPart = (AVTPart)this.m_parts.elementAt(i2);
                    aVTPart.evaluate(xPathContext, fastStringBuffer, n2, prefixResolver);
                }
                string = fastStringBuffer.toString();
            }
            finally {
                fastStringBuffer.setLength(0);
            }
            return string;
        }
        return "";
    }

    public boolean isContextInsensitive() {
        return null != this.m_simpleString;
    }

    public boolean canTraverseOutsideSubtree() {
        if (null != this.m_parts) {
            int n2 = this.m_parts.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                AVTPart aVTPart = (AVTPart)this.m_parts.elementAt(i2);
                if (!aVTPart.canTraverseOutsideSubtree()) continue;
                return true;
            }
        }
        return false;
    }

    public void fixupVariables(Vector vector, int n2) {
        if (null != this.m_parts) {
            int n3 = this.m_parts.size();
            for (int i2 = 0; i2 < n3; ++i2) {
                AVTPart aVTPart = (AVTPart)this.m_parts.elementAt(i2);
                aVTPart.fixupVariables(vector, n2);
            }
        }
    }

    public void callVisitors(XSLTVisitor xSLTVisitor) {
        if (xSLTVisitor.visitAVT(this) && null != this.m_parts) {
            int n2 = this.m_parts.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                AVTPart aVTPart = (AVTPart)this.m_parts.elementAt(i2);
                aVTPart.callVisitors(xSLTVisitor);
            }
        }
    }

    public boolean isSimple() {
        return this.m_simpleString != null;
    }

    private final FastStringBuffer getBuffer() {
        return new FastStringBuffer(8);
    }
}

