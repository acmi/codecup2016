/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.io.PrintStream;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.trax.ObjectFactory;
import org.apache.xalan.xsltc.trax.TrAXFilter;
import org.apache.xalan.xsltc.trax.TransformerFactoryImpl;
import org.xml.sax.XMLFilter;

public class SmartTransformerFactoryImpl
extends SAXTransformerFactory {
    private static final String CLASS_NAME = "SmartTransformerFactoryImpl";
    private SAXTransformerFactory _xsltcFactory = null;
    private SAXTransformerFactory _xalanFactory = null;
    private SAXTransformerFactory _currFactory = null;
    private ErrorListener _errorlistener = null;
    private URIResolver _uriresolver = null;
    private boolean featureSecureProcessing = false;

    private void createXSLTCTransformerFactory() {
        this._currFactory = this._xsltcFactory = new TransformerFactoryImpl();
    }

    private void createXalanTransformerFactory() {
        String string = "org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl could not create an org.apache.xalan.processor.TransformerFactoryImpl.";
        try {
            Class class_ = ObjectFactory.findProviderClass("org.apache.xalan.processor.TransformerFactoryImpl", ObjectFactory.findClassLoader(), true);
            this._xalanFactory = (SAXTransformerFactory)class_.newInstance();
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.err.println("org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl could not create an org.apache.xalan.processor.TransformerFactoryImpl.");
        }
        catch (InstantiationException instantiationException) {
            System.err.println("org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl could not create an org.apache.xalan.processor.TransformerFactoryImpl.");
        }
        catch (IllegalAccessException illegalAccessException) {
            System.err.println("org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl could not create an org.apache.xalan.processor.TransformerFactoryImpl.");
        }
        this._currFactory = this._xalanFactory;
    }

    public void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException {
        this._errorlistener = errorListener;
    }

    public ErrorListener getErrorListener() {
        return this._errorlistener;
    }

    public Object getAttribute(String string) throws IllegalArgumentException {
        if (string.equals("translet-name") || string.equals("debug")) {
            if (this._xsltcFactory == null) {
                this.createXSLTCTransformerFactory();
            }
            return this._xsltcFactory.getAttribute(string);
        }
        if (this._xalanFactory == null) {
            this.createXalanTransformerFactory();
        }
        return this._xalanFactory.getAttribute(string);
    }

    public void setAttribute(String string, Object object) throws IllegalArgumentException {
        if (string.equals("translet-name") || string.equals("debug")) {
            if (this._xsltcFactory == null) {
                this.createXSLTCTransformerFactory();
            }
            this._xsltcFactory.setAttribute(string, object);
        } else {
            if (this._xalanFactory == null) {
                this.createXalanTransformerFactory();
            }
            this._xalanFactory.setAttribute(string, object);
        }
    }

    public void setFeature(String string, boolean bl) throws TransformerConfigurationException {
        if (string == null) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_SET_FEATURE_NULL_NAME");
            throw new NullPointerException(errorMsg.toString());
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            this.featureSecureProcessing = bl;
            return;
        }
        ErrorMsg errorMsg = new ErrorMsg("JAXP_UNSUPPORTED_FEATURE", string);
        throw new TransformerConfigurationException(errorMsg.toString());
    }

    public boolean getFeature(String string) {
        String[] arrstring = new String[]{"http://javax.xml.transform.dom.DOMSource/feature", "http://javax.xml.transform.dom.DOMResult/feature", "http://javax.xml.transform.sax.SAXSource/feature", "http://javax.xml.transform.sax.SAXResult/feature", "http://javax.xml.transform.stream.StreamSource/feature", "http://javax.xml.transform.stream.StreamResult/feature"};
        if (string == null) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_GET_FEATURE_NULL_NAME");
            throw new NullPointerException(errorMsg.toString());
        }
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            if (!string.equals(arrstring[i2])) continue;
            return true;
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this.featureSecureProcessing;
        }
        return false;
    }

    public URIResolver getURIResolver() {
        return this._uriresolver;
    }

    public void setURIResolver(URIResolver uRIResolver) {
        this._uriresolver = uRIResolver;
    }

    public Source getAssociatedStylesheet(Source source, String string, String string2, String string3) throws TransformerConfigurationException {
        if (this._currFactory == null) {
            this.createXSLTCTransformerFactory();
        }
        return this._currFactory.getAssociatedStylesheet(source, string, string2, string3);
    }

    public Transformer newTransformer() throws TransformerConfigurationException {
        if (this._xalanFactory == null) {
            this.createXalanTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xalanFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xalanFactory.setURIResolver(this._uriresolver);
        }
        this._currFactory = this._xalanFactory;
        return this._currFactory.newTransformer();
    }

    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        if (this._xalanFactory == null) {
            this.createXalanTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xalanFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xalanFactory.setURIResolver(this._uriresolver);
        }
        this._currFactory = this._xalanFactory;
        return this._currFactory.newTransformer(source);
    }

    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            this.createXSLTCTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xsltcFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xsltcFactory.setURIResolver(this._uriresolver);
        }
        this._currFactory = this._xsltcFactory;
        return this._currFactory.newTemplates(source);
    }

    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            this.createXSLTCTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xsltcFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xsltcFactory.setURIResolver(this._uriresolver);
        }
        return this._xsltcFactory.newTemplatesHandler();
    }

    public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
        if (this._xalanFactory == null) {
            this.createXalanTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xalanFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xalanFactory.setURIResolver(this._uriresolver);
        }
        return this._xalanFactory.newTransformerHandler();
    }

    public TransformerHandler newTransformerHandler(Source source) throws TransformerConfigurationException {
        if (this._xalanFactory == null) {
            this.createXalanTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xalanFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xalanFactory.setURIResolver(this._uriresolver);
        }
        return this._xalanFactory.newTransformerHandler(source);
    }

    public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            this.createXSLTCTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xsltcFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xsltcFactory.setURIResolver(this._uriresolver);
        }
        return this._xsltcFactory.newTransformerHandler(templates);
    }

    public XMLFilter newXMLFilter(Source source) throws TransformerConfigurationException {
        Templates templates;
        if (this._xsltcFactory == null) {
            this.createXSLTCTransformerFactory();
        }
        if (this._errorlistener != null) {
            this._xsltcFactory.setErrorListener(this._errorlistener);
        }
        if (this._uriresolver != null) {
            this._xsltcFactory.setURIResolver(this._uriresolver);
        }
        if ((templates = this._xsltcFactory.newTemplates(source)) == null) {
            return null;
        }
        return this.newXMLFilter(templates);
    }

    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
        try {
            return new TrAXFilter(templates);
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            ErrorListener errorListener;
            if (this._xsltcFactory == null) {
                this.createXSLTCTransformerFactory();
            }
            if ((errorListener = this._xsltcFactory.getErrorListener()) != null) {
                try {
                    errorListener.fatalError(transformerConfigurationException);
                    return null;
                }
                catch (TransformerException transformerException) {
                    new TransformerConfigurationException(transformerException);
                }
            }
            throw transformerConfigurationException;
        }
    }
}

