/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.xalan.processor.XSLTAttributeDef;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xalan.processor.XSLTSchema;
import org.apache.xml.utils.QName;

public class XSLTElementDef {
    static final int T_ELEMENT = 1;
    static final int T_PCDATA = 2;
    static final int T_ANY = 3;
    private int m_type = 1;
    private String m_namespace;
    private String m_name;
    private String m_nameAlias;
    private XSLTElementDef[] m_elements;
    private XSLTAttributeDef[] m_attributes;
    private XSLTElementProcessor m_elementProcessor;
    private Class m_classObject;
    private boolean m_has_required = false;
    private boolean m_required = false;
    Hashtable m_requiredFound;
    boolean m_isOrdered = false;
    private int m_order = -1;
    private int m_lastOrder = -1;
    private boolean m_multiAllowed = true;

    XSLTElementDef() {
    }

    XSLTElementDef(XSLTSchema xSLTSchema, String string, String string2, String string3, XSLTElementDef[] arrxSLTElementDef, XSLTAttributeDef[] arrxSLTAttributeDef, XSLTElementProcessor xSLTElementProcessor, Class class_) {
        this.build(string, string2, string3, arrxSLTElementDef, arrxSLTAttributeDef, xSLTElementProcessor, class_);
        if (null != string && (string.equals("http://www.w3.org/1999/XSL/Transform") || string.equals("http://xml.apache.org/xalan") || string.equals("http://xml.apache.org/xslt"))) {
            xSLTSchema.addAvailableElement(new QName(string, string2));
            if (null != string3) {
                xSLTSchema.addAvailableElement(new QName(string, string3));
            }
        }
    }

    XSLTElementDef(XSLTSchema xSLTSchema, String string, String string2, String string3, XSLTElementDef[] arrxSLTElementDef, XSLTAttributeDef[] arrxSLTAttributeDef, XSLTElementProcessor xSLTElementProcessor, Class class_, boolean bl) {
        this.m_has_required = bl;
        this.build(string, string2, string3, arrxSLTElementDef, arrxSLTAttributeDef, xSLTElementProcessor, class_);
        if (null != string && (string.equals("http://www.w3.org/1999/XSL/Transform") || string.equals("http://xml.apache.org/xalan") || string.equals("http://xml.apache.org/xslt"))) {
            xSLTSchema.addAvailableElement(new QName(string, string2));
            if (null != string3) {
                xSLTSchema.addAvailableElement(new QName(string, string3));
            }
        }
    }

    XSLTElementDef(XSLTSchema xSLTSchema, String string, String string2, String string3, XSLTElementDef[] arrxSLTElementDef, XSLTAttributeDef[] arrxSLTAttributeDef, XSLTElementProcessor xSLTElementProcessor, Class class_, boolean bl, boolean bl2) {
        this(xSLTSchema, string, string2, string3, arrxSLTElementDef, arrxSLTAttributeDef, xSLTElementProcessor, class_, bl);
        this.m_required = bl2;
    }

    XSLTElementDef(XSLTSchema xSLTSchema, String string, String string2, String string3, XSLTElementDef[] arrxSLTElementDef, XSLTAttributeDef[] arrxSLTAttributeDef, XSLTElementProcessor xSLTElementProcessor, Class class_, boolean bl, boolean bl2, int n2, boolean bl3) {
        this(xSLTSchema, string, string2, string3, arrxSLTElementDef, arrxSLTAttributeDef, xSLTElementProcessor, class_, bl, bl2);
        this.m_order = n2;
        this.m_multiAllowed = bl3;
    }

    XSLTElementDef(XSLTSchema xSLTSchema, String string, String string2, String string3, XSLTElementDef[] arrxSLTElementDef, XSLTAttributeDef[] arrxSLTAttributeDef, XSLTElementProcessor xSLTElementProcessor, Class class_, boolean bl, boolean bl2, boolean bl3, int n2, boolean bl4) {
        this(xSLTSchema, string, string2, string3, arrxSLTElementDef, arrxSLTAttributeDef, xSLTElementProcessor, class_, bl, bl2);
        this.m_order = n2;
        this.m_multiAllowed = bl4;
        this.m_isOrdered = bl3;
    }

    XSLTElementDef(XSLTSchema xSLTSchema, String string, String string2, String string3, XSLTElementDef[] arrxSLTElementDef, XSLTAttributeDef[] arrxSLTAttributeDef, XSLTElementProcessor xSLTElementProcessor, Class class_, boolean bl, int n2, boolean bl2) {
        this(xSLTSchema, string, string2, string3, arrxSLTElementDef, arrxSLTAttributeDef, xSLTElementProcessor, class_, n2, bl2);
        this.m_isOrdered = bl;
    }

    XSLTElementDef(XSLTSchema xSLTSchema, String string, String string2, String string3, XSLTElementDef[] arrxSLTElementDef, XSLTAttributeDef[] arrxSLTAttributeDef, XSLTElementProcessor xSLTElementProcessor, Class class_, int n2, boolean bl) {
        this(xSLTSchema, string, string2, string3, arrxSLTElementDef, arrxSLTAttributeDef, xSLTElementProcessor, class_);
        this.m_order = n2;
        this.m_multiAllowed = bl;
    }

    XSLTElementDef(Class class_, XSLTElementProcessor xSLTElementProcessor, int n2) {
        this.m_classObject = class_;
        this.m_type = n2;
        this.setElementProcessor(xSLTElementProcessor);
    }

    void build(String string, String string2, String string3, XSLTElementDef[] arrxSLTElementDef, XSLTAttributeDef[] arrxSLTAttributeDef, XSLTElementProcessor xSLTElementProcessor, Class class_) {
        this.m_namespace = string;
        this.m_name = string2;
        this.m_nameAlias = string3;
        this.m_elements = arrxSLTElementDef;
        this.m_attributes = arrxSLTAttributeDef;
        this.setElementProcessor(xSLTElementProcessor);
        this.m_classObject = class_;
        if (this.hasRequired() && this.m_elements != null) {
            int n2 = this.m_elements.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                XSLTElementDef xSLTElementDef = this.m_elements[i2];
                if (xSLTElementDef == null || !xSLTElementDef.getRequired()) continue;
                if (this.m_requiredFound == null) {
                    this.m_requiredFound = new Hashtable();
                }
                this.m_requiredFound.put(xSLTElementDef.getName(), "xsl:" + xSLTElementDef.getName());
            }
        }
    }

    private static boolean equalsMayBeNull(Object object, Object object2) {
        return object2 == object || null != object && null != object2 && object2.equals(object);
    }

    private static boolean equalsMayBeNullOrZeroLen(String string, String string2) {
        int n2;
        int n3 = string == null ? 0 : string.length();
        int n4 = n2 = string2 == null ? 0 : string2.length();
        return n3 != n2 ? false : (n3 == 0 ? true : string.equals(string2));
    }

    int getType() {
        return this.m_type;
    }

    void setType(int n2) {
        this.m_type = n2;
    }

    String getNamespace() {
        return this.m_namespace;
    }

    String getName() {
        return this.m_name;
    }

    String getNameAlias() {
        return this.m_nameAlias;
    }

    public XSLTElementDef[] getElements() {
        return this.m_elements;
    }

    void setElements(XSLTElementDef[] arrxSLTElementDef) {
        this.m_elements = arrxSLTElementDef;
    }

    private boolean QNameEquals(String string, String string2) {
        return XSLTElementDef.equalsMayBeNullOrZeroLen(this.m_namespace, string) && (XSLTElementDef.equalsMayBeNullOrZeroLen(this.m_name, string2) || XSLTElementDef.equalsMayBeNullOrZeroLen(this.m_nameAlias, string2));
    }

    XSLTElementProcessor getProcessorFor(String string, String string2) {
        int n2;
        XSLTElementProcessor xSLTElementProcessor = null;
        if (null == this.m_elements) {
            return null;
        }
        int n3 = this.m_elements.length;
        int n4 = -1;
        boolean bl = true;
        for (n2 = 0; n2 < n3; ++n2) {
            XSLTElementDef xSLTElementDef = this.m_elements[n2];
            if (xSLTElementDef.m_name.equals("*")) {
                if (XSLTElementDef.equalsMayBeNullOrZeroLen(string, "http://www.w3.org/1999/XSL/Transform")) continue;
                xSLTElementProcessor = xSLTElementDef.m_elementProcessor;
                n4 = xSLTElementDef.getOrder();
                bl = xSLTElementDef.getMultiAllowed();
                continue;
            }
            if (!xSLTElementDef.QNameEquals(string, string2)) continue;
            if (xSLTElementDef.getRequired()) {
                this.setRequiredFound(xSLTElementDef.getName(), true);
            }
            n4 = xSLTElementDef.getOrder();
            bl = xSLTElementDef.getMultiAllowed();
            xSLTElementProcessor = xSLTElementDef.m_elementProcessor;
            break;
        }
        if (xSLTElementProcessor != null && this.isOrdered()) {
            n2 = this.getLastOrder();
            if (n4 > n2) {
                this.setLastOrder(n4);
            } else {
                if (n4 == n2 && !bl) {
                    return null;
                }
                if (n4 < n2 && n4 > 0) {
                    return null;
                }
            }
        }
        return xSLTElementProcessor;
    }

    XSLTElementProcessor getProcessorForUnknown(String string, String string2) {
        if (null == this.m_elements) {
            return null;
        }
        int n2 = this.m_elements.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            XSLTElementDef xSLTElementDef = this.m_elements[i2];
            if (!xSLTElementDef.m_name.equals("unknown") || string.length() <= 0) continue;
            return xSLTElementDef.m_elementProcessor;
        }
        return null;
    }

    XSLTAttributeDef[] getAttributes() {
        return this.m_attributes;
    }

    XSLTAttributeDef getAttributeDef(String string, String string2) {
        XSLTAttributeDef xSLTAttributeDef = null;
        XSLTAttributeDef[] arrxSLTAttributeDef = this.getAttributes();
        int n2 = arrxSLTAttributeDef.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            XSLTAttributeDef xSLTAttributeDef2 = arrxSLTAttributeDef[i2];
            String string3 = xSLTAttributeDef2.getNamespace();
            String string4 = xSLTAttributeDef2.getName();
            if (string4.equals("*") && (XSLTElementDef.equalsMayBeNullOrZeroLen(string, string3) || string3 != null && string3.equals("*") && string != null && string.length() > 0)) {
                return xSLTAttributeDef2;
            }
            if (string4.equals("*") && string3 == null) {
                xSLTAttributeDef = xSLTAttributeDef2;
                continue;
            }
            if (!XSLTElementDef.equalsMayBeNullOrZeroLen(string, string3) || !string2.equals(string4)) continue;
            return xSLTAttributeDef2;
        }
        if (null == xSLTAttributeDef && string.length() > 0 && !XSLTElementDef.equalsMayBeNullOrZeroLen(string, "http://www.w3.org/1999/XSL/Transform")) {
            return XSLTAttributeDef.m_foreignAttr;
        }
        return xSLTAttributeDef;
    }

    public XSLTElementProcessor getElementProcessor() {
        return this.m_elementProcessor;
    }

    public void setElementProcessor(XSLTElementProcessor xSLTElementProcessor) {
        if (xSLTElementProcessor != null) {
            this.m_elementProcessor = xSLTElementProcessor;
            this.m_elementProcessor.setElemDef(this);
        }
    }

    Class getClassObject() {
        return this.m_classObject;
    }

    boolean hasRequired() {
        return this.m_has_required;
    }

    boolean getRequired() {
        return this.m_required;
    }

    void setRequiredFound(String string, boolean bl) {
        if (this.m_requiredFound.get(string) != null) {
            this.m_requiredFound.remove(string);
        }
    }

    boolean getRequiredFound() {
        if (this.m_requiredFound == null) {
            return true;
        }
        return this.m_requiredFound.isEmpty();
    }

    String getRequiredElem() {
        if (this.m_requiredFound == null) {
            return null;
        }
        Enumeration enumeration = this.m_requiredFound.elements();
        String string = "";
        boolean bl = true;
        while (enumeration.hasMoreElements()) {
            if (bl) {
                bl = false;
            } else {
                string = string + ", ";
            }
            string = string + (String)enumeration.nextElement();
        }
        return string;
    }

    boolean isOrdered() {
        return this.m_isOrdered;
    }

    int getOrder() {
        return this.m_order;
    }

    int getLastOrder() {
        return this.m_lastOrder;
    }

    void setLastOrder(int n2) {
        this.m_lastOrder = n2;
    }

    boolean getMultiAllowed() {
        return this.m_multiAllowed;
    }
}

