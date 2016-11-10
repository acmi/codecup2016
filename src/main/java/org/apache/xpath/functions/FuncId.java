/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.StringTokenizer;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

public class FuncId
extends FunctionOneArg {
    private StringVector getNodesByID(XPathContext xPathContext, int n2, String string, StringVector stringVector, NodeSetDTM nodeSetDTM, boolean bl) {
        if (null != string) {
            String string2 = null;
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            boolean bl2 = stringTokenizer.hasMoreTokens();
            DTM dTM = xPathContext.getDTM(n2);
            while (bl2) {
                string2 = stringTokenizer.nextToken();
                bl2 = stringTokenizer.hasMoreTokens();
                if (null != stringVector && stringVector.contains(string2)) {
                    string2 = null;
                    continue;
                }
                int n3 = dTM.getElementById(string2);
                if (-1 != n3) {
                    nodeSetDTM.addNodeInDocOrder(n3, xPathContext);
                }
                if (null == string2 || !bl2 && !bl) continue;
                if (null == stringVector) {
                    stringVector = new StringVector();
                }
                stringVector.addElement(string2);
            }
        }
        return stringVector;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        int n2 = xPathContext.getCurrentNode();
        DTM dTM = xPathContext.getDTM(n2);
        int n3 = dTM.getDocument();
        if (-1 == n3) {
            this.error(xPathContext, "ER_CONTEXT_HAS_NO_OWNERDOC", null);
        }
        XObject xObject = this.m_arg0.execute(xPathContext);
        int n4 = xObject.getType();
        XNodeSet xNodeSet = new XNodeSet(xPathContext.getDTMManager());
        NodeSetDTM nodeSetDTM = xNodeSet.mutableNodeset();
        if (4 == n4) {
            DTMIterator dTMIterator = xObject.iter();
            StringVector stringVector = null;
            int n5 = dTMIterator.nextNode();
            while (-1 != n5) {
                DTM dTM2 = dTMIterator.getDTM(n5);
                String string = dTM2.getStringValue(n5).toString();
                n5 = dTMIterator.nextNode();
                stringVector = this.getNodesByID(xPathContext, n3, string, stringVector, nodeSetDTM, -1 != n5);
            }
        } else {
            if (-1 == n4) {
                return xNodeSet;
            }
            String string = xObject.str();
            this.getNodesByID(xPathContext, n3, string, null, nodeSetDTM, false);
        }
        return xNodeSet;
    }
}

