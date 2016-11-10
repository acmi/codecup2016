/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.dtd;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.dtd.DTDDVFactoryImpl;
import org.apache.xerces.impl.dv.dtd.ListDatatypeValidator;
import org.apache.xerces.impl.dv.dtd.XML11IDDatatypeValidator;
import org.apache.xerces.impl.dv.dtd.XML11IDREFDatatypeValidator;
import org.apache.xerces.impl.dv.dtd.XML11NMTOKENDatatypeValidator;

public class XML11DTDDVFactoryImpl
extends DTDDVFactoryImpl {
    static final Hashtable fXML11BuiltInTypes = new Hashtable();

    public DatatypeValidator getBuiltInDV(String string) {
        if (fXML11BuiltInTypes.get(string) != null) {
            return (DatatypeValidator)fXML11BuiltInTypes.get(string);
        }
        return (DatatypeValidator)DTDDVFactoryImpl.fBuiltInTypes.get(string);
    }

    public Hashtable getBuiltInTypes() {
        Hashtable hashtable = (Hashtable)DTDDVFactoryImpl.fBuiltInTypes.clone();
        Iterator iterator = fXML11BuiltInTypes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            Object k2 = entry.getKey();
            Object v2 = entry.getValue();
            hashtable.put(k2, v2);
        }
        return hashtable;
    }

    static {
        fXML11BuiltInTypes.put("XML11ID", new XML11IDDatatypeValidator());
        DatatypeValidator datatypeValidator = new XML11IDREFDatatypeValidator();
        fXML11BuiltInTypes.put("XML11IDREF", datatypeValidator);
        fXML11BuiltInTypes.put("XML11IDREFS", new ListDatatypeValidator(datatypeValidator));
        datatypeValidator = new XML11NMTOKENDatatypeValidator();
        fXML11BuiltInTypes.put("XML11NMTOKEN", datatypeValidator);
        fXML11BuiltInTypes.put("XML11NMTOKENS", new ListDatatypeValidator(datatypeValidator));
    }
}

