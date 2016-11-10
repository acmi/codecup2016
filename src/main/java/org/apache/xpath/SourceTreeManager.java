/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import java.io.IOException;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xpath.SourceTree;
import org.apache.xpath.XPathContext;

public class SourceTreeManager {
    private Vector m_sourceTree = new Vector();
    URIResolver m_uriResolver;

    public void reset() {
        this.m_sourceTree = new Vector();
    }

    public void setURIResolver(URIResolver uRIResolver) {
        this.m_uriResolver = uRIResolver;
    }

    public URIResolver getURIResolver() {
        return this.m_uriResolver;
    }

    public Source resolveURI(String string, String string2, SourceLocator sourceLocator) throws TransformerException, IOException {
        Source source = null;
        if (null != this.m_uriResolver) {
            source = this.m_uriResolver.resolve(string2, string);
        }
        if (null == source) {
            String string3 = SystemIDResolver.getAbsoluteURI(string2, string);
            source = new StreamSource(string3);
        }
        return source;
    }

    public void removeDocumentFromCache(int n2) {
        if (-1 == n2) {
            return;
        }
        for (int i2 = this.m_sourceTree.size() - 1; i2 >= 0; --i2) {
            SourceTree sourceTree = (SourceTree)this.m_sourceTree.elementAt(i2);
            if (sourceTree == null || sourceTree.m_root != n2) continue;
            this.m_sourceTree.removeElementAt(i2);
            return;
        }
    }

    public void putDocumentInCache(int n2, Source source) {
        int n3 = this.getNode(source);
        if (-1 != n3) {
            if (n3 != n2) {
                throw new RuntimeException("Programmer's Error!  putDocumentInCache found reparse of doc: " + source.getSystemId());
            }
            return;
        }
        if (null != source.getSystemId()) {
            this.m_sourceTree.addElement(new SourceTree(n2, source.getSystemId()));
        }
    }

    public int getNode(Source source) {
        String string = source.getSystemId();
        if (null == string) {
            return -1;
        }
        int n2 = this.m_sourceTree.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            SourceTree sourceTree = (SourceTree)this.m_sourceTree.elementAt(i2);
            if (!string.equals(sourceTree.m_url)) continue;
            return sourceTree.m_root;
        }
        return -1;
    }

    public int getSourceTree(Source source, SourceLocator sourceLocator, XPathContext xPathContext) throws TransformerException {
        int n2 = this.getNode(source);
        if (-1 != n2) {
            return n2;
        }
        n2 = this.parseToNode(source, sourceLocator, xPathContext);
        if (-1 != n2) {
            this.putDocumentInCache(n2, source);
        }
        return n2;
    }

    public int parseToNode(Source source, SourceLocator sourceLocator, XPathContext xPathContext) throws TransformerException {
        try {
            Object object = xPathContext.getOwnerObject();
            DTM dTM = null != object && object instanceof DTMWSFilter ? xPathContext.getDTM(source, false, (DTMWSFilter)object, false, true) : xPathContext.getDTM(source, false, null, false, true);
            return dTM.getDocument();
        }
        catch (Exception exception) {
            throw new TransformerException(exception.getMessage(), sourceLocator, exception);
        }
    }
}

