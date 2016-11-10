/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Hashtable;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.KeyManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.UnionPathIterator;
import org.apache.xpath.functions.Function2Args;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class FuncKey
extends Function2Args {
    static final long serialVersionUID = 9089293100115347340L;
    private static Boolean ISTRUE = new Boolean(true);

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        Hashtable<XMLString, Boolean> hashtable;
        TransformerImpl transformerImpl = (TransformerImpl)xPathContext.getOwnerObject();
        XNodeSet xNodeSet = null;
        int n2 = xPathContext.getCurrentNode();
        DTM dTM = xPathContext.getDTM(n2);
        int n3 = dTM.getDocumentRoot(n2);
        if (-1 == n3) {
            // empty if block
        }
        String string = this.getArg0().execute(xPathContext).str();
        QName qName = new QName(string, xPathContext.getNamespaceContext());
        XObject xObject = this.getArg1().execute(xPathContext);
        boolean bl = 4 == xObject.getType();
        KeyManager keyManager = transformerImpl.getKeyManager();
        if (bl) {
            hashtable = (XNodeSet)xObject;
            hashtable.setShouldCacheNodes(true);
            int n4 = hashtable.getLength();
            if (n4 <= 1) {
                bl = false;
            }
        }
        if (bl) {
            int n5;
            hashtable = null;
            DTMIterator dTMIterator = xObject.iter();
            UnionPathIterator unionPathIterator = new UnionPathIterator();
            unionPathIterator.exprSetParent(this);
            while (-1 != (n5 = dTMIterator.nextNode())) {
                dTM = xPathContext.getDTM(n5);
                XMLString xMLString = dTM.getStringValue(n5);
                if (null == xMLString) continue;
                if (null == hashtable) {
                    hashtable = new Hashtable<XMLString, Boolean>();
                }
                if (hashtable.get(xMLString) != null) continue;
                hashtable.put(xMLString, ISTRUE);
                XNodeSet xNodeSet2 = keyManager.getNodeSetDTMByKey(xPathContext, n3, qName, xMLString, xPathContext.getNamespaceContext());
                xNodeSet2.setRoot(xPathContext.getCurrentNode(), xPathContext);
                unionPathIterator.addIterator(xNodeSet2);
            }
            int n6 = xPathContext.getCurrentNode();
            unionPathIterator.setRoot(n6, xPathContext);
            xNodeSet = new XNodeSet(unionPathIterator);
        } else {
            hashtable = xObject.xstr();
            xNodeSet = keyManager.getNodeSetDTMByKey(xPathContext, n3, qName, (XMLString)((Object)hashtable), xPathContext.getNamespaceContext());
            xNodeSet.setRoot(xPathContext.getCurrentNode(), xPathContext);
        }
        return xNodeSet;
    }
}

