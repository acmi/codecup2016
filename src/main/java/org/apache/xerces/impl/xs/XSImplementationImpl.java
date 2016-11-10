/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.PSVIDOMImplementationImpl;
import org.apache.xerces.impl.xs.XSLoaderImpl;
import org.apache.xerces.impl.xs.util.LSInputListImpl;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.xs.LSInputList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSException;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.ls.LSInput;

public class XSImplementationImpl
extends PSVIDOMImplementationImpl
implements XSImplementation {
    static final XSImplementationImpl singleton = new XSImplementationImpl();

    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }

    public boolean hasFeature(String string, String string2) {
        return string.equalsIgnoreCase("XS-Loader") && (string2 == null || string2.equals("1.0")) || super.hasFeature(string, string2);
    }

    public XSLoader createXSLoader(StringList stringList) throws XSException {
        XSLoaderImpl xSLoaderImpl = new XSLoaderImpl();
        if (stringList == null) {
            return xSLoaderImpl;
        }
        int n2 = 0;
        while (n2 < stringList.getLength()) {
            if (!stringList.item(n2).equals("1.0")) {
                String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{stringList.item(n2)});
                throw new XSException(1, string);
            }
            ++n2;
        }
        return xSLoaderImpl;
    }

    public StringList createStringList(String[] arrstring) {
        int n2 = arrstring != null ? arrstring.length : 0;
        return n2 != 0 ? new StringListImpl((String[])arrstring.clone(), n2) : StringListImpl.EMPTY_LIST;
    }

    public LSInputList createLSInputList(LSInput[] arrlSInput) {
        int n2 = arrlSInput != null ? arrlSInput.length : 0;
        return n2 != 0 ? new LSInputListImpl((LSInput[])arrlSInput.clone(), n2) : LSInputListImpl.EMPTY_LIST;
    }

    public StringList getRecognizedVersions() {
        StringListImpl stringListImpl = new StringListImpl(new String[]{"1.0"}, 1);
        return stringListImpl;
    }
}

