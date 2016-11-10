/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.transformer;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.transformer.KeyTable;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;

public class KeyManager {
    private transient Vector m_key_tables = null;

    public XNodeSet getNodeSetDTMByKey(XPathContext xPathContext, int n2, QName qName, XMLString xMLString, PrefixResolver prefixResolver) throws TransformerException {
        XNodeSet xNodeSet = null;
        ElemTemplateElement elemTemplateElement = (ElemTemplateElement)prefixResolver;
        if (null != elemTemplateElement && null != elemTemplateElement.getStylesheetRoot().getKeysComposed()) {
            boolean bl = false;
            if (null == this.m_key_tables) {
                this.m_key_tables = new Vector(4);
            } else {
                int n3 = this.m_key_tables.size();
                for (int i2 = 0; i2 < n3; ++i2) {
                    KeyTable keyTable = (KeyTable)this.m_key_tables.elementAt(i2);
                    if (!keyTable.getKeyTableName().equals(qName) || n2 != keyTable.getDocKey() || (xNodeSet = keyTable.getNodeSetDTMByKey(qName, xMLString)) == null) continue;
                    bl = true;
                    break;
                }
            }
            if (null == xNodeSet && !bl) {
                KeyTable keyTable = new KeyTable(n2, prefixResolver, qName, elemTemplateElement.getStylesheetRoot().getKeysComposed(), xPathContext);
                this.m_key_tables.addElement(keyTable);
                if (n2 == keyTable.getDocKey()) {
                    bl = true;
                    xNodeSet = keyTable.getNodeSetDTMByKey(qName, xMLString);
                }
            }
        }
        return xNodeSet;
    }
}

