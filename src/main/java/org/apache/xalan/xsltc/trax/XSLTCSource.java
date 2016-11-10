/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.xml.sax.SAXException;

public final class XSLTCSource
implements Source {
    private String _systemId = null;
    private Source _source = null;
    private ThreadLocal _dom = new ThreadLocal();

    public XSLTCSource(String string) {
        this._systemId = string;
    }

    public XSLTCSource(Source source) {
        this._source = source;
    }

    public void setSystemId(String string) {
        this._systemId = string;
        if (this._source != null) {
            this._source.setSystemId(string);
        }
    }

    public String getSystemId() {
        if (this._source != null) {
            return this._source.getSystemId();
        }
        return this._systemId;
    }

    protected DOM getDOM(XSLTCDTMManager xSLTCDTMManager, AbstractTranslet abstractTranslet) throws SAXException {
        SAXImpl sAXImpl = (SAXImpl)this._dom.get();
        if (sAXImpl != null) {
            if (xSLTCDTMManager != null) {
                sAXImpl.migrateTo(xSLTCDTMManager);
            }
        } else {
            boolean bl;
            Source source = this._source;
            if (source == null) {
                if (this._systemId != null && this._systemId.length() > 0) {
                    source = new StreamSource(this._systemId);
                } else {
                    ErrorMsg errorMsg = new ErrorMsg("XSLTC_SOURCE_ERR");
                    throw new SAXException(errorMsg.toString());
                }
            }
            DOMWSFilter dOMWSFilter = null;
            if (abstractTranslet != null && abstractTranslet instanceof StripFilter) {
                dOMWSFilter = new DOMWSFilter(abstractTranslet);
            }
            boolean bl2 = bl = abstractTranslet != null ? abstractTranslet.hasIdCall() : false;
            if (xSLTCDTMManager == null) {
                xSLTCDTMManager = XSLTCDTMManager.newInstance();
            }
            sAXImpl = (SAXImpl)xSLTCDTMManager.getDTM(source, true, dOMWSFilter, false, false, bl);
            String string = this.getSystemId();
            if (string != null) {
                sAXImpl.setDocumentURI(string);
            }
            this._dom.set(sAXImpl);
        }
        return sAXImpl;
    }
}

