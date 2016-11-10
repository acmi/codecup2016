/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import javax.xml.namespace.QName;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xs.datatypes.XSQName;

public class QNameDV
extends TypeValidator {
    private static final String EMPTY_STRING = "".intern();

    public short getAllowedFacets() {
        return 2079;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        String string2;
        String string3;
        int n2 = string.indexOf(":");
        if (n2 > 0) {
            string2 = validationContext.getSymbol(string.substring(0, n2));
            string3 = string.substring(n2 + 1);
        } else {
            string2 = EMPTY_STRING;
            string3 = string;
        }
        if (string2.length() > 0 && !XMLChar.isValidNCName(string2)) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "QName"});
        }
        if (!XMLChar.isValidNCName(string3)) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{string, "QName"});
        }
        String string4 = validationContext.getURI(string2);
        if (string2.length() > 0 && string4 == null) {
            throw new InvalidDatatypeValueException("UndeclaredPrefix", new Object[]{string, string2});
        }
        return new XQName(string2, validationContext.getSymbol(string3), validationContext.getSymbol(string), string4);
    }

    public int getDataLength(Object object) {
        return ((XQName)object).rawname.length();
    }

    private static final class XQName
    extends org.apache.xerces.xni.QName
    implements XSQName {
        public XQName(String string, String string2, String string3, String string4) {
            this.setValues(string, string2, string3, string4);
        }

        public boolean equals(Object object) {
            if (object instanceof org.apache.xerces.xni.QName) {
                org.apache.xerces.xni.QName qName = (org.apache.xerces.xni.QName)object;
                return this.uri == qName.uri && this.localpart == qName.localpart;
            }
            return false;
        }

        public String toString() {
            return this.rawname;
        }

        public QName getJAXPQName() {
            return new QName(this.uri, this.localpart, this.prefix);
        }

        public org.apache.xerces.xni.QName getXNIQName() {
            return this;
        }
    }

}

