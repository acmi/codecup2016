/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.objects;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.OneStepIterator;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNodeSetForDOM;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

public class XObjectFactory {
    public static XObject create(Object object) {
        XObject xObject = object instanceof XObject ? (XObject)object : (object instanceof String ? new XString((String)object) : (object instanceof Boolean ? new XBoolean((Boolean)object) : (object instanceof Double ? new XNumber((Double)object) : new XObject(object))));
        return xObject;
    }

    public static XObject create(Object object, XPathContext xPathContext) {
        XObject xObject;
        if (object instanceof XObject) {
            xObject = (XObject)object;
        } else if (object instanceof String) {
            xObject = new XString((String)object);
        } else if (object instanceof Boolean) {
            xObject = new XBoolean((Boolean)object);
        } else if (object instanceof Number) {
            xObject = new XNumber((Number)object);
        } else if (object instanceof DTM) {
            DTM dTM = (DTM)object;
            try {
                int n2 = dTM.getDocument();
                DTMAxisIterator dTMAxisIterator = dTM.getAxisIterator(13);
                dTMAxisIterator.setStartNode(n2);
                OneStepIterator oneStepIterator = new OneStepIterator(dTMAxisIterator, 13);
                oneStepIterator.setRoot(n2, xPathContext);
                xObject = new XNodeSet(oneStepIterator);
            }
            catch (Exception exception) {
                throw new WrappedRuntimeException(exception);
            }
        } else if (object instanceof DTMAxisIterator) {
            DTMAxisIterator dTMAxisIterator = (DTMAxisIterator)object;
            try {
                OneStepIterator oneStepIterator = new OneStepIterator(dTMAxisIterator, 13);
                oneStepIterator.setRoot(dTMAxisIterator.getStartNode(), xPathContext);
                xObject = new XNodeSet(oneStepIterator);
            }
            catch (Exception exception) {
                throw new WrappedRuntimeException(exception);
            }
        } else {
            xObject = object instanceof DTMIterator ? new XNodeSet((DTMIterator)object) : (object instanceof Node ? new XNodeSetForDOM((Node)object, (DTMManager)xPathContext) : (object instanceof NodeList ? new XNodeSetForDOM((NodeList)object, xPathContext) : (object instanceof NodeIterator ? new XNodeSetForDOM((NodeIterator)object, xPathContext) : new XObject(object))));
        }
        return xObject;
    }
}

