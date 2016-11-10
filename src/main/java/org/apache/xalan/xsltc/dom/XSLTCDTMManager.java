/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.io.PrintStream;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.dom.DOMBuilder;
import org.apache.xalan.xsltc.dom.ObjectFactory;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.trax.DOM2SAX;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class XSLTCDTMManager
extends DTMManagerDefault {
    private static final String DEFAULT_CLASS_NAME = "org.apache.xalan.xsltc.dom.XSLTCDTMManager";
    private static final String DEFAULT_PROP_NAME = "org.apache.xalan.xsltc.dom.XSLTCDTMManager";
    private static final boolean DUMPTREE = false;
    private static final boolean DEBUG = false;
    static Class class$org$apache$xalan$xsltc$dom$XSLTCDTMManager;

    public static XSLTCDTMManager newInstance() {
        return new XSLTCDTMManager();
    }

    public static Class getDTMManagerClass() {
        Class class_ = ObjectFactory.lookUpFactoryClass("org.apache.xalan.xsltc.dom.XSLTCDTMManager", null, "org.apache.xalan.xsltc.dom.XSLTCDTMManager");
        Class class_2 = class_ != null ? class_ : (class$org$apache$xalan$xsltc$dom$XSLTCDTMManager == null ? (XSLTCDTMManager.class$org$apache$xalan$xsltc$dom$XSLTCDTMManager = XSLTCDTMManager.class$("org.apache.xalan.xsltc.dom.XSLTCDTMManager")) : class$org$apache$xalan$xsltc$dom$XSLTCDTMManager);
        return class_2;
    }

    public DTM getDTM(Source source, boolean bl, DTMWSFilter dTMWSFilter, boolean bl2, boolean bl3) {
        return this.getDTM(source, bl, dTMWSFilter, bl2, bl3, false, 0, true, false);
    }

    public DTM getDTM(Source source, boolean bl, DTMWSFilter dTMWSFilter, boolean bl2, boolean bl3, boolean bl4) {
        return this.getDTM(source, bl, dTMWSFilter, bl2, bl3, false, 0, bl4, false);
    }

    public DTM getDTM(Source source, boolean bl, DTMWSFilter dTMWSFilter, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        return this.getDTM(source, bl, dTMWSFilter, bl2, bl3, false, 0, bl4, bl5);
    }

    public DTM getDTM(Source source, boolean bl, DTMWSFilter dTMWSFilter, boolean bl2, boolean bl3, boolean bl4, int n2, boolean bl5) {
        return this.getDTM(source, bl, dTMWSFilter, bl2, bl3, bl4, n2, bl5, false);
    }

    public DTM getDTM(Source source, boolean bl, DTMWSFilter dTMWSFilter, boolean bl2, boolean bl3, boolean bl4, int n2, boolean bl5, boolean bl6) {
        boolean bl7;
        int n3 = this.getFirstFreeDTMID();
        int n4 = n3 << 16;
        if (null != source && source instanceof DOMSource) {
            DOMSource dOMSource = (DOMSource)source;
            Node node = dOMSource.getNode();
            DOM2SAX dOM2SAX = new DOM2SAX(node);
            SAXImpl sAXImpl = n2 <= 0 ? new SAXImpl(this, source, n4, dTMWSFilter, null, bl3, 512, bl5, bl6) : new SAXImpl(this, source, n4, dTMWSFilter, null, bl3, n2, bl5, bl6);
            sAXImpl.setDocumentURI(source.getSystemId());
            this.addDTM(sAXImpl, n3, 0);
            dOM2SAX.setContentHandler(sAXImpl);
            try {
                dOM2SAX.parse();
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
            catch (Exception exception) {
                throw new WrappedRuntimeException(exception);
            }
            return sAXImpl;
        }
        boolean bl8 = null != source ? source instanceof SAXSource : true;
        boolean bl9 = bl7 = null != source ? source instanceof StreamSource : false;
        if (bl8 || bl7) {
            Object object;
            InputSource inputSource;
            XMLReader xMLReader;
            if (null == source) {
                inputSource = null;
                xMLReader = null;
                bl4 = false;
            } else {
                xMLReader = this.getXMLReader(source);
                inputSource = SAXSource.sourceToInputSource(source);
                object = inputSource.getSystemId();
                if (null != object) {
                    try {
                        object = SystemIDResolver.getAbsoluteURI((String)object);
                    }
                    catch (Exception exception) {
                        System.err.println("Can not absolutize URL: " + (String)object);
                    }
                    inputSource.setSystemId((String)object);
                }
            }
            object = n2 <= 0 ? new SAXImpl(this, source, n4, dTMWSFilter, null, bl3, 512, bl5, bl6) : new SAXImpl(this, source, n4, dTMWSFilter, null, bl3, n2, bl5, bl6);
            this.addDTM((DTM)object, n3, 0);
            if (null == xMLReader) {
                return object;
            }
            xMLReader.setContentHandler(object.getBuilder());
            if (!bl4 || null == xMLReader.getDTDHandler()) {
                xMLReader.setDTDHandler((DTDHandler)object);
            }
            if (!bl4 || null == xMLReader.getErrorHandler()) {
                xMLReader.setErrorHandler((ErrorHandler)object);
            }
            try {
                xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", object);
            }
            catch (SAXNotRecognizedException sAXNotRecognizedException) {
            }
            catch (SAXNotSupportedException sAXNotSupportedException) {
                // empty catch block
            }
            try {
                xMLReader.parse(inputSource);
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
            catch (Exception exception) {
                throw new WrappedRuntimeException(exception);
            }
            finally {
                if (!bl4) {
                    this.releaseXMLReader(xMLReader);
                }
            }
            return object;
        }
        throw new DTMException(XMLMessages.createXMLMessage("ER_NOT_SUPPORTED", new Object[]{source}));
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

