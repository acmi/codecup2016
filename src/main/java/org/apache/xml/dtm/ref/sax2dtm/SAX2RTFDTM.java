/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref.sax2dtm;

import java.util.Vector;
import javax.xml.transform.Source;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.IntVector;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLStringFactory;
import org.xml.sax.SAXException;

public class SAX2RTFDTM
extends SAX2DTM {
    private int m_currentDocumentNode = -1;
    IntStack mark_size = new IntStack();
    IntStack mark_data_size = new IntStack();
    IntStack mark_char_size = new IntStack();
    IntStack mark_doq_size = new IntStack();
    IntStack mark_nsdeclset_size = new IntStack();
    IntStack mark_nsdeclelem_size = new IntStack();
    int m_emptyNodeCount;
    int m_emptyNSDeclSetCount;
    int m_emptyNSDeclSetElemsCount;
    int m_emptyDataCount;
    int m_emptyCharsCount;
    int m_emptyDataQNCount;

    public SAX2RTFDTM(DTMManager dTMManager, Source source, int n2, DTMWSFilter dTMWSFilter, XMLStringFactory xMLStringFactory, boolean bl) {
        super(dTMManager, source, n2, dTMWSFilter, xMLStringFactory, bl);
        this.m_useSourceLocationProperty = false;
        this.m_sourceSystemId = this.m_useSourceLocationProperty ? new StringVector() : null;
        this.m_sourceLine = this.m_useSourceLocationProperty ? new IntVector() : null;
        this.m_sourceColumn = this.m_useSourceLocationProperty ? new IntVector() : null;
        this.m_emptyNodeCount = this.m_size;
        this.m_emptyNSDeclSetCount = this.m_namespaceDeclSets == null ? 0 : this.m_namespaceDeclSets.size();
        this.m_emptyNSDeclSetElemsCount = this.m_namespaceDeclSetElements == null ? 0 : this.m_namespaceDeclSetElements.size();
        this.m_emptyDataCount = this.m_data.size();
        this.m_emptyCharsCount = this.m_chars.size();
        this.m_emptyDataQNCount = this.m_dataOrQName.size();
    }

    public int getDocument() {
        return this.makeNodeHandle(this.m_currentDocumentNode);
    }

    public int getDocumentRoot(int n2) {
        int n3 = this.makeNodeIdentity(n2);
        while (n3 != -1) {
            if (this._type(n3) == 9) {
                return this.makeNodeHandle(n3);
            }
            n3 = this._parent(n3);
        }
        return -1;
    }

    public void startDocument() throws SAXException {
        this.m_endDocumentOccured = false;
        this.m_prefixMappings = new Vector();
        this.m_contextIndexes = new IntStack();
        this.m_parents = new IntStack();
        this.m_currentDocumentNode = this.m_size;
        super.startDocument();
    }

    public void endDocument() throws SAXException {
        this.charactersFlush();
        this.m_nextsib.setElementAt(-1, this.m_currentDocumentNode);
        if (this.m_firstch.elementAt(this.m_currentDocumentNode) == -2) {
            this.m_firstch.setElementAt(-1, this.m_currentDocumentNode);
        }
        if (-1 != this.m_previous) {
            this.m_nextsib.setElementAt(-1, this.m_previous);
        }
        this.m_parents = null;
        this.m_prefixMappings = null;
        this.m_contextIndexes = null;
        this.m_currentDocumentNode = -1;
        this.m_endDocumentOccured = true;
    }

    public void pushRewindMark() {
        if (this.m_indexing || this.m_elemIndexes != null) {
            throw new NullPointerException("Coding error; Don't try to mark/rewind an indexed DTM");
        }
        this.mark_size.push(this.m_size);
        this.mark_nsdeclset_size.push(this.m_namespaceDeclSets == null ? 0 : this.m_namespaceDeclSets.size());
        this.mark_nsdeclelem_size.push(this.m_namespaceDeclSetElements == null ? 0 : this.m_namespaceDeclSetElements.size());
        this.mark_data_size.push(this.m_data.size());
        this.mark_char_size.push(this.m_chars.size());
        this.mark_doq_size.push(this.m_dataOrQName.size());
    }

    public boolean popRewindMark() {
        int n2;
        int n3;
        boolean bl = this.mark_size.empty();
        this.m_size = bl ? this.m_emptyNodeCount : this.mark_size.pop();
        this.m_exptype.setSize(this.m_size);
        this.m_firstch.setSize(this.m_size);
        this.m_nextsib.setSize(this.m_size);
        this.m_prevsib.setSize(this.m_size);
        this.m_parent.setSize(this.m_size);
        this.m_elemIndexes = null;
        int n4 = n2 = bl ? this.m_emptyNSDeclSetCount : this.mark_nsdeclset_size.pop();
        if (this.m_namespaceDeclSets != null) {
            this.m_namespaceDeclSets.setSize(n2);
        }
        int n5 = n3 = bl ? this.m_emptyNSDeclSetElemsCount : this.mark_nsdeclelem_size.pop();
        if (this.m_namespaceDeclSetElements != null) {
            this.m_namespaceDeclSetElements.setSize(n3);
        }
        this.m_data.setSize(bl ? this.m_emptyDataCount : this.mark_data_size.pop());
        this.m_chars.setLength(bl ? this.m_emptyCharsCount : this.mark_char_size.pop());
        this.m_dataOrQName.setSize(bl ? this.m_emptyDataQNCount : this.mark_doq_size.pop());
        return this.m_size == 0;
    }

    public boolean isTreeIncomplete() {
        return !this.m_endDocumentOccured;
    }
}

