/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.io.FileNotFoundException;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.MultiDOM;
import org.apache.xalan.xsltc.dom.SingletonIterator;
import org.apache.xalan.xsltc.dom.UnionIterator;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.trax.TemplatesImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.EmptyIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.SystemIDResolver;

public final class LoadDocument {
    private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";

    public static DTMAxisIterator documentF(Object object, DTMAxisIterator dTMAxisIterator, String string, AbstractTranslet abstractTranslet, DOM dOM) throws TransletException {
        String string2 = null;
        int n2 = dTMAxisIterator.next();
        if (n2 == -1) {
            return EmptyIterator.getInstance();
        }
        string2 = dOM.getDocumentURI(n2);
        if (!SystemIDResolver.isAbsoluteURI(string2)) {
            string2 = SystemIDResolver.getAbsoluteURIFromRelative(string2);
        }
        try {
            if (object instanceof String) {
                if (((String)object).length() == 0) {
                    return LoadDocument.document(string, "", abstractTranslet, dOM);
                }
                return LoadDocument.document((String)object, string2, abstractTranslet, dOM);
            }
            if (object instanceof DTMAxisIterator) {
                return LoadDocument.document((DTMAxisIterator)object, string2, abstractTranslet, dOM);
            }
            String string3 = "document(" + object.toString() + ")";
            throw new IllegalArgumentException(string3);
        }
        catch (Exception exception) {
            throw new TransletException(exception);
        }
    }

    public static DTMAxisIterator documentF(Object object, String string, AbstractTranslet abstractTranslet, DOM dOM) throws TransletException {
        try {
            if (object instanceof String) {
                String string2;
                if (string == null) {
                    string = "";
                }
                String string3 = string;
                if (!SystemIDResolver.isAbsoluteURI(string)) {
                    string3 = SystemIDResolver.getAbsoluteURIFromRelative(string);
                }
                if ((string2 = (String)object).length() == 0) {
                    string2 = "";
                    TemplatesImpl templatesImpl = (TemplatesImpl)abstractTranslet.getTemplates();
                    DOM dOM2 = null;
                    if (templatesImpl != null) {
                        dOM2 = templatesImpl.getStylesheetDOM();
                    }
                    if (dOM2 != null) {
                        return LoadDocument.document(dOM2, abstractTranslet, dOM);
                    }
                    return LoadDocument.document(string2, string3, abstractTranslet, dOM, true);
                }
                return LoadDocument.document(string2, string3, abstractTranslet, dOM);
            }
            if (object instanceof DTMAxisIterator) {
                return LoadDocument.document((DTMAxisIterator)object, null, abstractTranslet, dOM);
            }
            String string4 = "document(" + object.toString() + ")";
            throw new IllegalArgumentException(string4);
        }
        catch (Exception exception) {
            throw new TransletException(exception);
        }
    }

    private static DTMAxisIterator document(String string, String string2, AbstractTranslet abstractTranslet, DOM dOM) throws Exception {
        return LoadDocument.document(string, string2, abstractTranslet, dOM, false);
    }

    private static DTMAxisIterator document(String string, String string2, AbstractTranslet abstractTranslet, DOM dOM, boolean bl) throws Exception {
        Object object;
        Object object2;
        DOM dOM2;
        String string3 = string;
        MultiDOM multiDOM = (MultiDOM)dOM;
        if (string2 != null && string2.length() != 0) {
            string = SystemIDResolver.getAbsoluteURI(string, string2);
        }
        if (string == null || string.length() == 0) {
            return EmptyIterator.getInstance();
        }
        int n2 = multiDOM.getDocumentMask(string);
        if (n2 != -1 && (object = ((DOMAdapter)multiDOM.getDOMAdapter(string)).getDOMImpl()) instanceof DOMEnhancedForDTM) {
            return new SingletonIterator(((DOMEnhancedForDTM)object).getDocument(), true);
        }
        object = abstractTranslet.getDOMCache();
        n2 = multiDOM.nextMask();
        if (object != null) {
            dOM2 = object.retrieveDocument(string2, string3, abstractTranslet);
            if (dOM2 == null) {
                FileNotFoundException fileNotFoundException = new FileNotFoundException(string3);
                throw new TransletException(fileNotFoundException);
            }
        } else {
            TemplatesImpl templatesImpl;
            object2 = (XSLTCDTMManager)multiDOM.getDTMManager();
            DOMEnhancedForDTM dOMEnhancedForDTM = (DOMEnhancedForDTM)((Object)object2.getDTM(new StreamSource(string), false, null, true, false, abstractTranslet.hasIdCall(), bl));
            dOM2 = dOMEnhancedForDTM;
            if (bl && (templatesImpl = (TemplatesImpl)abstractTranslet.getTemplates()) != null) {
                templatesImpl.setStylesheetDOM(dOMEnhancedForDTM);
            }
            abstractTranslet.prepassDocument(dOMEnhancedForDTM);
            dOMEnhancedForDTM.setDocumentURI(string);
        }
        object2 = abstractTranslet.makeDOMAdapter(dOM2);
        multiDOM.addDOMAdapter((DOMAdapter)object2);
        abstractTranslet.buildKeys((DOM)object2, null, null, dOM2.getDocument());
        return new SingletonIterator(dOM2.getDocument(), true);
    }

    private static DTMAxisIterator document(DTMAxisIterator dTMAxisIterator, String string, AbstractTranslet abstractTranslet, DOM dOM) throws Exception {
        UnionIterator unionIterator = new UnionIterator(dOM);
        int n2 = -1;
        while ((n2 = dTMAxisIterator.next()) != -1) {
            String string2 = dOM.getStringValueX(n2);
            if (string == null && !SystemIDResolver.isAbsoluteURI(string = dOM.getDocumentURI(n2))) {
                string = SystemIDResolver.getAbsoluteURIFromRelative(string);
            }
            unionIterator.addIterator(LoadDocument.document(string2, string, abstractTranslet, dOM));
        }
        return unionIterator;
    }

    private static DTMAxisIterator document(DOM dOM, AbstractTranslet abstractTranslet, DOM dOM2) throws Exception {
        DTMManager dTMManager = ((MultiDOM)dOM2).getDTMManager();
        if (dTMManager != null && dOM instanceof DTM) {
            ((DTM)((Object)dOM)).migrateTo(dTMManager);
        }
        abstractTranslet.prepassDocument(dOM);
        DOMAdapter dOMAdapter = abstractTranslet.makeDOMAdapter(dOM);
        ((MultiDOM)dOM2).addDOMAdapter(dOMAdapter);
        abstractTranslet.buildKeys(dOMAdapter, null, null, dOM.getDocument());
        return new SingletonIterator(dOM.getDocument(), true);
    }
}

