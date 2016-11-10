/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.text.Collator;
import java.util.Locale;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.MsgMgr;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;

class NodeSortKey {
    XPath m_selectPat;
    boolean m_treatAsNumbers;
    boolean m_descending;
    boolean m_caseOrderUpper;
    Collator m_col;
    Locale m_locale;
    PrefixResolver m_namespaceContext;
    TransformerImpl m_processor;

    NodeSortKey(TransformerImpl transformerImpl, XPath xPath, boolean bl, boolean bl2, String string, boolean bl3, PrefixResolver prefixResolver) throws TransformerException {
        this.m_processor = transformerImpl;
        this.m_namespaceContext = prefixResolver;
        this.m_selectPat = xPath;
        this.m_treatAsNumbers = bl;
        this.m_descending = bl2;
        this.m_caseOrderUpper = bl3;
        if (null != string && !this.m_treatAsNumbers) {
            this.m_locale = new Locale(string.toLowerCase(), Locale.getDefault().getCountry());
            if (null == this.m_locale) {
                this.m_locale = Locale.getDefault();
            }
        } else {
            this.m_locale = Locale.getDefault();
        }
        this.m_col = Collator.getInstance(this.m_locale);
        if (null == this.m_col) {
            this.m_processor.getMsgMgr().warn(null, "WG_CANNOT_FIND_COLLATOR", new Object[]{string});
            this.m_col = Collator.getInstance();
        }
    }
}

