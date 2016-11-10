/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import java.io.PrintStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBase;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.IncrementalSAXSource;
import org.apache.xml.dtm.ref.IncrementalSAXSource_Filter;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTM;
import org.apache.xml.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.dtm.ref.sax2dtm.SAX2RTFDTM;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLReaderManager;
import org.apache.xml.utils.XMLStringFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class DTMManagerDefault
extends DTMManager {
    private static final boolean DUMPTREE = false;
    private static final boolean DEBUG = false;
    protected DTM[] m_dtms = new DTM[256];
    int[] m_dtm_offsets = new int[256];
    protected XMLReaderManager m_readerManager = null;
    protected DefaultHandler m_defaultHandler = new DefaultHandler();
    private ExpandedNameTable m_expandedNameTable = new ExpandedNameTable();

    public synchronized void addDTM(DTM dTM, int n2) {
        this.addDTM(dTM, n2, 0);
    }

    public synchronized void addDTM(DTM dTM, int n2, int n3) {
        if (n2 >= 65536) {
            throw new DTMException(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
        }
        int n4 = this.m_dtms.length;
        if (n4 <= n2) {
            int n5 = Math.min(n2 + 256, 65536);
            DTM[] arrdTM = new DTM[n5];
            System.arraycopy(this.m_dtms, 0, arrdTM, 0, n4);
            this.m_dtms = arrdTM;
            int[] arrn = new int[n5];
            System.arraycopy(this.m_dtm_offsets, 0, arrn, 0, n4);
            this.m_dtm_offsets = arrn;
        }
        this.m_dtms[n2] = dTM;
        this.m_dtm_offsets[n2] = n3;
        dTM.documentRegistration();
    }

    public synchronized int getFirstFreeDTMID() {
        int n2 = this.m_dtms.length;
        for (int i2 = 1; i2 < n2; ++i2) {
            if (null != this.m_dtms[i2]) continue;
            return i2;
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public synchronized DTM getDTM(Source source, boolean bl, DTMWSFilter dTMWSFilter, boolean bl2, boolean bl3) {
        boolean bl4;
        XMLStringFactory xMLStringFactory = this.m_xsf;
        int n2 = this.getFirstFreeDTMID();
        int n3 = n2 << 16;
        if (null != source && source instanceof DOMSource) {
            DOM2DTM dOM2DTM = new DOM2DTM(this, (DOMSource)source, n3, dTMWSFilter, xMLStringFactory, bl3);
            this.addDTM(dOM2DTM, n2, 0);
            return dOM2DTM;
        }
        boolean bl5 = null != source ? source instanceof SAXSource : true;
        boolean bl6 = bl4 = null != source ? source instanceof StreamSource : false;
        if (bl5 || bl4) {
            XMLReader xMLReader = null;
            try {
                boolean bl7;
                Object object;
                InputSource inputSource;
                if (null == source) {
                    inputSource = null;
                } else {
                    xMLReader = this.getXMLReader(source);
                    inputSource = SAXSource.sourceToInputSource(source);
                    String string = inputSource.getSystemId();
                    if (null != string) {
                        try {
                            string = SystemIDResolver.getAbsoluteURI(string);
                        }
                        catch (Exception exception) {
                            System.err.println("Can not absolutize URL: " + string);
                        }
                        inputSource.setSystemId(string);
                    }
                }
                SAX2DTM sAX2DTM = source == null && bl && !bl2 && !bl3 ? new SAX2RTFDTM(this, source, n3, dTMWSFilter, xMLStringFactory, bl3) : new SAX2DTM(this, source, n3, dTMWSFilter, xMLStringFactory, bl3);
                this.addDTM(sAX2DTM, n2, 0);
                boolean bl8 = bl7 = null != xMLReader && xMLReader.getClass().getName().equals("org.apache.xerces.parsers.SAXParser");
                if (bl7) {
                    bl2 = true;
                }
                if (this.m_incremental && bl2) {
                    object = null;
                    if (bl7) {
                        try {
                            object = (IncrementalSAXSource)Class.forName("org.apache.xml.dtm.ref.IncrementalSAXSource_Xerces").newInstance();
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                            object = null;
                        }
                    }
                    if (object == null) {
                        if (null == xMLReader) {
                            object = new IncrementalSAXSource_Filter();
                        } else {
                            IncrementalSAXSource_Filter incrementalSAXSource_Filter = new IncrementalSAXSource_Filter();
                            incrementalSAXSource_Filter.setXMLReader(xMLReader);
                            object = incrementalSAXSource_Filter;
                        }
                    }
                    sAX2DTM.setIncrementalSAXSource((IncrementalSAXSource)object);
                    if (null == inputSource) {
                        SAX2DTM exception = sAX2DTM;
                        return exception;
                    }
                    if (null == xMLReader.getErrorHandler()) {
                        xMLReader.setErrorHandler(sAX2DTM);
                    }
                    xMLReader.setDTDHandler(sAX2DTM);
                    try {
                        object.startParse(inputSource);
                    }
                    catch (RuntimeException runtimeException) {
                        sAX2DTM.clearCoRoutine();
                        throw runtimeException;
                    }
                    catch (Exception exception) {
                        sAX2DTM.clearCoRoutine();
                        throw new WrappedRuntimeException(exception);
                    }
                }
                if (null == xMLReader) {
                    SAX2DTM sAX2DTM2 = sAX2DTM;
                    return sAX2DTM2;
                }
                xMLReader.setContentHandler(sAX2DTM);
                xMLReader.setDTDHandler(sAX2DTM);
                if (null == xMLReader.getErrorHandler()) {
                    xMLReader.setErrorHandler(sAX2DTM);
                }
                try {
                    xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", sAX2DTM);
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
                    sAX2DTM.clearCoRoutine();
                    throw runtimeException;
                }
                catch (Exception exception) {
                    sAX2DTM.clearCoRoutine();
                    throw new WrappedRuntimeException(exception);
                }
                object = sAX2DTM;
                return object;
            }
            finally {
                if (!(xMLReader == null || this.m_incremental && bl2)) {
                    xMLReader.setContentHandler(this.m_defaultHandler);
                    xMLReader.setDTDHandler(this.m_defaultHandler);
                    xMLReader.setErrorHandler(this.m_defaultHandler);
                    try {
                        xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", null);
                    }
                    catch (Exception exception) {}
                }
                this.releaseXMLReader(xMLReader);
            }
        }
        throw new DTMException(XMLMessages.createXMLMessage("ER_NOT_SUPPORTED", new Object[]{source}));
    }

    public synchronized int getDTMHandleFromNode(Node node) {
        int n2;
        Object object;
        if (null == node) {
            throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_NODE_NON_NULL", null));
        }
        if (node instanceof DTMNodeProxy) {
            return ((DTMNodeProxy)node).getDTMNodeNumber();
        }
        int n3 = this.m_dtms.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4;
            object = this.m_dtms[i2];
            if (null == object || !(object instanceof DOM2DTM) || (n4 = ((DOM2DTM)object).getHandleOfNode(node)) == -1) continue;
            return n4;
        }
        Object object2 = node;
        Object object3 = object = object2.getNodeType() == 2 ? ((Attr)object2).getOwnerElement() : object2.getParentNode();
        while (object != null) {
            object2 = object;
            object = object.getParentNode();
        }
        DOM2DTM dOM2DTM = (DOM2DTM)this.getDTM(new DOMSource((Node)object2), false, null, true, true);
        if (node instanceof DOM2DTMdefaultNamespaceDeclarationNode) {
            n2 = dOM2DTM.getHandleOfNode(((Attr)node).getOwnerElement());
            n2 = dOM2DTM.getAttributeNode(n2, node.getNamespaceURI(), node.getLocalName());
        } else {
            n2 = dOM2DTM.getHandleOfNode(node);
        }
        if (-1 == n2) {
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_COULD_NOT_RESOLVE_NODE", null));
        }
        return n2;
    }

    public synchronized XMLReader getXMLReader(Source source) {
        try {
            XMLReader xMLReader;
            XMLReader xMLReader2 = xMLReader = source instanceof SAXSource ? ((SAXSource)source).getXMLReader() : null;
            if (null == xMLReader) {
                if (this.m_readerManager == null) {
                    this.m_readerManager = XMLReaderManager.getInstance();
                }
                xMLReader = this.m_readerManager.getXMLReader();
            }
            return xMLReader;
        }
        catch (SAXException sAXException) {
            throw new DTMException(sAXException.getMessage(), sAXException);
        }
    }

    public synchronized void releaseXMLReader(XMLReader xMLReader) {
        if (this.m_readerManager != null) {
            this.m_readerManager.releaseXMLReader(xMLReader);
        }
    }

    public synchronized DTM getDTM(int n2) {
        try {
            return this.m_dtms[n2 >>> 16];
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            if (n2 == -1) {
                return null;
            }
            throw arrayIndexOutOfBoundsException;
        }
    }

    public synchronized int getDTMIdentity(DTM dTM) {
        if (dTM instanceof DTMDefaultBase) {
            DTMDefaultBase dTMDefaultBase = (DTMDefaultBase)dTM;
            if (dTMDefaultBase.getManager() == this) {
                return dTMDefaultBase.getDTMIDs().elementAt(0);
            }
            return -1;
        }
        int n2 = this.m_dtms.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            DTM dTM2 = this.m_dtms[i2];
            if (dTM2 != dTM || this.m_dtm_offsets[i2] != 0) continue;
            return i2 << 16;
        }
        return -1;
    }

    public synchronized boolean release(DTM dTM, boolean bl) {
        if (dTM instanceof SAX2DTM) {
            ((SAX2DTM)dTM).clearCoRoutine();
        }
        if (dTM instanceof DTMDefaultBase) {
            SuballocatedIntVector suballocatedIntVector = ((DTMDefaultBase)dTM).getDTMIDs();
            for (int i2 = suballocatedIntVector.size() - 1; i2 >= 0; --i2) {
                this.m_dtms[suballocatedIntVector.elementAt((int)i2) >>> 16] = null;
            }
        } else {
            int n2 = this.getDTMIdentity(dTM);
            if (n2 >= 0) {
                this.m_dtms[n2 >>> 16] = null;
            }
        }
        dTM.documentRelease();
        return true;
    }

    public synchronized DTM createDocumentFragment() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            DocumentFragment documentFragment = document.createDocumentFragment();
            return this.getDTM(new DOMSource(documentFragment), true, null, false, false);
        }
        catch (Exception exception) {
            throw new DTMException(exception);
        }
    }

    public synchronized DTMIterator createDTMIterator(int n2, DTMFilter dTMFilter, boolean bl) {
        return null;
    }

    public synchronized DTMIterator createDTMIterator(String string, PrefixResolver prefixResolver) {
        return null;
    }

    public synchronized DTMIterator createDTMIterator(int n2) {
        return null;
    }

    public synchronized DTMIterator createDTMIterator(Object object, int n2) {
        return null;
    }

    public ExpandedNameTable getExpandedNameTable(DTM dTM) {
        return this.m_expandedNameTable;
    }
}

