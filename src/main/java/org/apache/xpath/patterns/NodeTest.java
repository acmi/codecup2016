/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.patterns;

import java.io.PrintStream;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class NodeTest
extends Expression {
    static final long serialVersionUID = -5736721866747906182L;
    public static final String WILD = "*";
    public static final String SUPPORTS_PRE_STRIPPING = "http://xml.apache.org/xpath/features/whitespace-pre-stripping";
    protected int m_whatToShow;
    public static final int SHOW_BYFUNCTION = 65536;
    String m_namespace;
    protected String m_name;
    XNumber m_score;
    public static final XNumber SCORE_NODETEST = new XNumber(-0.5);
    public static final XNumber SCORE_NSWILD = new XNumber(-0.25);
    public static final XNumber SCORE_QNAME = new XNumber(0.0);
    public static final XNumber SCORE_OTHER = new XNumber(0.5);
    public static final XNumber SCORE_NONE = new XNumber(Double.NEGATIVE_INFINITY);
    private boolean m_isTotallyWild;

    public int getWhatToShow() {
        return this.m_whatToShow;
    }

    public void setWhatToShow(int n2) {
        this.m_whatToShow = n2;
    }

    public String getNamespace() {
        return this.m_namespace;
    }

    public void setNamespace(String string) {
        this.m_namespace = string;
    }

    public String getLocalName() {
        return null == this.m_name ? "" : this.m_name;
    }

    public void setLocalName(String string) {
        this.m_name = string;
    }

    public NodeTest(int n2, String string, String string2) {
        this.initNodeTest(n2, string, string2);
    }

    public NodeTest(int n2) {
        this.initNodeTest(n2);
    }

    public boolean deepEquals(Expression expression) {
        if (!this.isSameClass(expression)) {
            return false;
        }
        NodeTest nodeTest = (NodeTest)expression;
        if (null != nodeTest.m_name) {
            if (null == this.m_name) {
                return false;
            }
            if (!nodeTest.m_name.equals(this.m_name)) {
                return false;
            }
        } else if (null != this.m_name) {
            return false;
        }
        if (null != nodeTest.m_namespace) {
            if (null == this.m_namespace) {
                return false;
            }
            if (!nodeTest.m_namespace.equals(this.m_namespace)) {
                return false;
            }
        } else if (null != this.m_namespace) {
            return false;
        }
        if (this.m_whatToShow != nodeTest.m_whatToShow) {
            return false;
        }
        if (this.m_isTotallyWild != nodeTest.m_isTotallyWild) {
            return false;
        }
        return true;
    }

    public NodeTest() {
    }

    public void initNodeTest(int n2) {
        this.m_whatToShow = n2;
        this.calcScore();
    }

    public void initNodeTest(int n2, String string, String string2) {
        this.m_whatToShow = n2;
        this.m_namespace = string;
        this.m_name = string2;
        this.calcScore();
    }

    public XNumber getStaticScore() {
        return this.m_score;
    }

    public void setStaticScore(XNumber xNumber) {
        this.m_score = xNumber;
    }

    protected void calcScore() {
        this.m_score = this.m_namespace == null && this.m_name == null ? SCORE_NODETEST : ((this.m_namespace == "*" || this.m_namespace == null) && this.m_name == "*" ? SCORE_NODETEST : (this.m_namespace != "*" && this.m_name == "*" ? SCORE_NSWILD : SCORE_QNAME));
        this.m_isTotallyWild = this.m_namespace == null && this.m_name == "*";
    }

    public double getDefaultScore() {
        return this.m_score.num();
    }

    public static int getNodeTypeTest(int n2) {
        if (0 != (n2 & 1)) {
            return 1;
        }
        if (0 != (n2 & 2)) {
            return 2;
        }
        if (0 != (n2 & 4)) {
            return 3;
        }
        if (0 != (n2 & 256)) {
            return 9;
        }
        if (0 != (n2 & 1024)) {
            return 11;
        }
        if (0 != (n2 & 4096)) {
            return 13;
        }
        if (0 != (n2 & 128)) {
            return 8;
        }
        if (0 != (n2 & 64)) {
            return 7;
        }
        if (0 != (n2 & 512)) {
            return 10;
        }
        if (0 != (n2 & 32)) {
            return 6;
        }
        if (0 != (n2 & 16)) {
            return 5;
        }
        if (0 != (n2 & 2048)) {
            return 12;
        }
        if (0 != (n2 & 8)) {
            return 4;
        }
        return 0;
    }

    public static void debugWhatToShow(int n2) {
        Vector<String> vector = new Vector<String>();
        if (0 != (n2 & 2)) {
            vector.addElement("SHOW_ATTRIBUTE");
        }
        if (0 != (n2 & 4096)) {
            vector.addElement("SHOW_NAMESPACE");
        }
        if (0 != (n2 & 8)) {
            vector.addElement("SHOW_CDATA_SECTION");
        }
        if (0 != (n2 & 128)) {
            vector.addElement("SHOW_COMMENT");
        }
        if (0 != (n2 & 256)) {
            vector.addElement("SHOW_DOCUMENT");
        }
        if (0 != (n2 & 1024)) {
            vector.addElement("SHOW_DOCUMENT_FRAGMENT");
        }
        if (0 != (n2 & 512)) {
            vector.addElement("SHOW_DOCUMENT_TYPE");
        }
        if (0 != (n2 & 1)) {
            vector.addElement("SHOW_ELEMENT");
        }
        if (0 != (n2 & 32)) {
            vector.addElement("SHOW_ENTITY");
        }
        if (0 != (n2 & 16)) {
            vector.addElement("SHOW_ENTITY_REFERENCE");
        }
        if (0 != (n2 & 2048)) {
            vector.addElement("SHOW_NOTATION");
        }
        if (0 != (n2 & 64)) {
            vector.addElement("SHOW_PROCESSING_INSTRUCTION");
        }
        if (0 != (n2 & 4)) {
            vector.addElement("SHOW_TEXT");
        }
        int n3 = vector.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            if (i2 > 0) {
                System.out.print(" | ");
            }
            System.out.print(vector.elementAt(i2));
        }
        if (0 == n3) {
            System.out.print("empty whatToShow: " + n2);
        }
        System.out.println();
    }

    private static final boolean subPartMatch(String string, String string2) {
        return string == string2 || null != string && (string2 == "*" || string.equals(string2));
    }

    private static final boolean subPartMatchNS(String string, String string2) {
        return string == string2 || null != string && (string.length() > 0 ? string2 == "*" || string.equals(string2) : null == string2);
    }

    public XObject execute(XPathContext xPathContext, int n2) throws TransformerException {
        DTM dTM = xPathContext.getDTM(n2);
        short s2 = dTM.getNodeType(n2);
        if (this.m_whatToShow == -1) {
            return this.m_score;
        }
        int n3 = this.m_whatToShow & 1 << s2 - 1;
        switch (n3) {
            case 256: 
            case 1024: {
                return SCORE_OTHER;
            }
            case 128: {
                return this.m_score;
            }
            case 4: 
            case 8: {
                return this.m_score;
            }
            case 64: {
                return NodeTest.subPartMatch(dTM.getNodeName(n2), this.m_name) ? this.m_score : SCORE_NONE;
            }
            case 4096: {
                String string = dTM.getLocalName(n2);
                return NodeTest.subPartMatch(string, this.m_name) ? this.m_score : SCORE_NONE;
            }
            case 1: 
            case 2: {
                return this.m_isTotallyWild || NodeTest.subPartMatchNS(dTM.getNamespaceURI(n2), this.m_namespace) && NodeTest.subPartMatch(dTM.getLocalName(n2), this.m_name) ? this.m_score : SCORE_NONE;
            }
        }
        return SCORE_NONE;
    }

    public XObject execute(XPathContext xPathContext, int n2, DTM dTM, int n3) throws TransformerException {
        if (this.m_whatToShow == -1) {
            return this.m_score;
        }
        int n4 = this.m_whatToShow & 1 << dTM.getNodeType(n2) - 1;
        switch (n4) {
            case 256: 
            case 1024: {
                return SCORE_OTHER;
            }
            case 128: {
                return this.m_score;
            }
            case 4: 
            case 8: {
                return this.m_score;
            }
            case 64: {
                return NodeTest.subPartMatch(dTM.getNodeName(n2), this.m_name) ? this.m_score : SCORE_NONE;
            }
            case 4096: {
                String string = dTM.getLocalName(n2);
                return NodeTest.subPartMatch(string, this.m_name) ? this.m_score : SCORE_NONE;
            }
            case 1: 
            case 2: {
                return this.m_isTotallyWild || NodeTest.subPartMatchNS(dTM.getNamespaceURI(n2), this.m_namespace) && NodeTest.subPartMatch(dTM.getLocalName(n2), this.m_name) ? this.m_score : SCORE_NONE;
            }
        }
        return SCORE_NONE;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        return this.execute(xPathContext, xPathContext.getCurrentNode());
    }

    public void fixupVariables(Vector vector, int n2) {
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        this.assertion(false, "callVisitors should not be called for this object!!!");
    }
}

