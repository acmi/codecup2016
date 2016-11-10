/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.utils.SystemIDResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public final class DocumentCache
implements DOMCache {
    private int _size;
    private Hashtable _references;
    private String[] _URIs;
    private int _count;
    private int _current;
    private SAXParser _parser;
    private XMLReader _reader;
    private XSLTCDTMManager _dtmManager;
    private static final int REFRESH_INTERVAL = 1000;

    public DocumentCache(int n2) throws SAXException {
        this(n2, null);
        try {
            this._dtmManager = (XSLTCDTMManager)XSLTCDTMManager.getDTMManagerClass().newInstance();
        }
        catch (Exception exception) {
            throw new SAXException(exception);
        }
    }

    public DocumentCache(int n2, XSLTCDTMManager xSLTCDTMManager) throws SAXException {
        this._dtmManager = xSLTCDTMManager;
        this._count = 0;
        this._current = 0;
        this._size = n2;
        this._references = new Hashtable(this._size + 2);
        this._URIs = new String[this._size];
        try {
            SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
            try {
                sAXParserFactory.setFeature("http://xml.org/sax/features/namespaces", true);
            }
            catch (Exception exception) {
                sAXParserFactory.setNamespaceAware(true);
            }
            this._parser = sAXParserFactory.newSAXParser();
            this._reader = this._parser.getXMLReader();
        }
        catch (ParserConfigurationException parserConfigurationException) {
            BasisLibrary.runTimeError("NAMESPACES_SUPPORT_ERR");
        }
    }

    private final long getLastModified(String string) {
        try {
            URL uRL = new URL(string);
            URLConnection uRLConnection = uRL.openConnection();
            long l2 = uRLConnection.getLastModified();
            if (l2 == 0 && "file".equals(uRL.getProtocol())) {
                File file = new File(URLDecoder.decode(uRL.getFile()));
                l2 = file.lastModified();
            }
            return l2;
        }
        catch (Exception exception) {
            return System.currentTimeMillis();
        }
    }

    private CachedDocument lookupDocument(String string) {
        return (CachedDocument)this._references.get(string);
    }

    private synchronized void insertDocument(String string, CachedDocument cachedDocument) {
        if (this._count < this._size) {
            this._URIs[this._count++] = string;
            this._current = 0;
        } else {
            this._references.remove(this._URIs[this._current]);
            this._URIs[this._current] = string;
            if (++this._current >= this._size) {
                this._current = 0;
            }
        }
        this._references.put(string, cachedDocument);
    }

    private synchronized void replaceDocument(String string, CachedDocument cachedDocument) {
        CachedDocument cachedDocument2 = (CachedDocument)this._references.get(string);
        if (cachedDocument == null) {
            this.insertDocument(string, cachedDocument);
        } else {
            this._references.put(string, cachedDocument);
        }
    }

    public DOM retrieveDocument(String string, String string2, Translet translet) {
        CachedDocument cachedDocument;
        String string3 = string2;
        if (string != null && string.length() != 0) {
            try {
                string3 = SystemIDResolver.getAbsoluteURI(string3, string);
            }
            catch (TransformerException transformerException) {
                // empty catch block
            }
        }
        if ((cachedDocument = this.lookupDocument(string3)) == null) {
            cachedDocument = new CachedDocument(this, string3);
            if (cachedDocument == null) {
                return null;
            }
            cachedDocument.setLastModified(this.getLastModified(string3));
            this.insertDocument(string3, cachedDocument);
        } else {
            long l2 = System.currentTimeMillis();
            long l3 = cachedDocument.getLastChecked();
            cachedDocument.setLastChecked(l2);
            if (l2 > l3 + 1000) {
                cachedDocument.setLastChecked(l2);
                long l4 = this.getLastModified(string3);
                if (l4 > cachedDocument.getLastModified()) {
                    cachedDocument = new CachedDocument(this, string3);
                    if (cachedDocument == null) {
                        return null;
                    }
                    cachedDocument.setLastModified(this.getLastModified(string3));
                    this.replaceDocument(string3, cachedDocument);
                }
            }
        }
        DOM dOM = cachedDocument.getDocument();
        if (dOM == null) {
            return null;
        }
        cachedDocument.incAccessCount();
        AbstractTranslet abstractTranslet = (AbstractTranslet)translet;
        abstractTranslet.prepassDocument(dOM);
        return cachedDocument.getDocument();
    }

    public void getStatistics(PrintWriter printWriter) {
        printWriter.println("<h2>DOM cache statistics</h2><center><table border=\"2\"><tr><td><b>Document URI</b></td><td><center><b>Build time</b></center></td><td><center><b>Access count</b></center></td><td><center><b>Last accessed</b></center></td><td><center><b>Last modified</b></center></td></tr>");
        for (int i2 = 0; i2 < this._count; ++i2) {
            CachedDocument cachedDocument = (CachedDocument)this._references.get(this._URIs[i2]);
            printWriter.print("<tr><td><a href=\"" + this._URIs[i2] + "\">" + "<font size=-1>" + this._URIs[i2] + "</font></a></td>");
            printWriter.print("<td><center>" + cachedDocument.getLatency() + "ms</center></td>");
            printWriter.print("<td><center>" + cachedDocument.getAccessCount() + "</center></td>");
            printWriter.print("<td><center>" + new Date(cachedDocument.getLastReferenced()) + "</center></td>");
            printWriter.print("<td><center>" + new Date(cachedDocument.getLastModified()) + "</center></td>");
            printWriter.println("</tr>");
        }
        printWriter.println("</table></center>");
    }

    static XMLReader access$000(DocumentCache documentCache) {
        return documentCache._reader;
    }

    static XSLTCDTMManager access$100(DocumentCache documentCache) {
        return documentCache._dtmManager;
    }

    public final class CachedDocument {
        private long _firstReferenced;
        private long _lastReferenced;
        private long _accessCount;
        private long _lastModified;
        private long _lastChecked;
        private long _buildTime;
        private DOMEnhancedForDTM _dom;
        private final DocumentCache this$0;

        public CachedDocument(DocumentCache documentCache, String string) {
            long l2;
            this.this$0 = documentCache;
            this._dom = null;
            this._firstReferenced = l2 = System.currentTimeMillis();
            this._lastReferenced = l2;
            this._accessCount = 0;
            this.loadDocument(string);
            this._buildTime = System.currentTimeMillis() - l2;
        }

        public void loadDocument(String string) {
            try {
                long l2 = System.currentTimeMillis();
                this._dom = (DOMEnhancedForDTM)((Object)DocumentCache.access$100(this.this$0).getDTM(new SAXSource(DocumentCache.access$000(this.this$0), new InputSource(string)), false, null, true, false));
                this._dom.setDocumentURI(string);
                long l3 = System.currentTimeMillis() - l2;
                this._buildTime = this._buildTime > 0 ? this._buildTime + l3 >>> 1 : l3;
            }
            catch (Exception exception) {
                this._dom = null;
            }
        }

        public DOM getDocument() {
            return this._dom;
        }

        public long getFirstReferenced() {
            return this._firstReferenced;
        }

        public long getLastReferenced() {
            return this._lastReferenced;
        }

        public long getAccessCount() {
            return this._accessCount;
        }

        public void incAccessCount() {
            ++this._accessCount;
        }

        public long getLastModified() {
            return this._lastModified;
        }

        public void setLastModified(long l2) {
            this._lastModified = l2;
        }

        public long getLatency() {
            return this._buildTime;
        }

        public long getLastChecked() {
            return this._lastChecked;
        }

        public void setLastChecked(long l2) {
            this._lastChecked = l2;
        }

        public long getEstimatedSize() {
            if (this._dom != null) {
                return this._dom.getSize() << 5;
            }
            return 0;
        }
    }

}

