/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.compiler;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.ObjectVector;
import org.apache.xpath.compiler.OpMapVector;

public class OpMap {
    protected String m_currentPattern;
    ObjectVector m_tokenQueue = new ObjectVector(500, 500);
    OpMapVector m_opMap = null;

    public String toString() {
        return this.m_currentPattern;
    }

    public ObjectVector getTokenQueue() {
        return this.m_tokenQueue;
    }

    public int getTokenQueueSize() {
        return this.m_tokenQueue.size();
    }

    void shrink() {
        int n2 = this.m_opMap.elementAt(1);
        this.m_opMap.setToSize(n2 + 4);
        this.m_opMap.setElementAt(0, n2);
        this.m_opMap.setElementAt(0, n2 + 1);
        this.m_opMap.setElementAt(0, n2 + 2);
        n2 = this.m_tokenQueue.size();
        this.m_tokenQueue.setToSize(n2 + 4);
        this.m_tokenQueue.setElementAt(null, n2);
        this.m_tokenQueue.setElementAt(null, n2 + 1);
        this.m_tokenQueue.setElementAt(null, n2 + 2);
    }

    public int getOp(int n2) {
        return this.m_opMap.elementAt(n2);
    }

    public void setOp(int n2, int n3) {
        this.m_opMap.setElementAt(n3, n2);
    }

    public int getNextOpPos(int n2) {
        return n2 + this.m_opMap.elementAt(n2 + 1);
    }

    public int getNextStepPos(int n2) {
        int n3 = this.getOp(n2);
        if (n3 >= 37 && n3 <= 53) {
            return this.getNextOpPos(n2);
        }
        if (n3 >= 22 && n3 <= 25) {
            int n4 = this.getNextOpPos(n2);
            while (29 == this.getOp(n4)) {
                n4 = this.getNextOpPos(n4);
            }
            n3 = this.getOp(n4);
            if (n3 < 37 || n3 > 53) {
                return -1;
            }
            return n4;
        }
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_UNKNOWN_STEP", new Object[]{String.valueOf(n3)}));
    }

    public int getFirstPredicateOpPos(int n2) throws TransformerException {
        int n3 = this.m_opMap.elementAt(n2);
        if (n3 >= 37 && n3 <= 53) {
            return n2 + this.m_opMap.elementAt(n2 + 2);
        }
        if (n3 >= 22 && n3 <= 25) {
            return n2 + this.m_opMap.elementAt(n2 + 1);
        }
        if (-2 == n3) {
            return -2;
        }
        this.error("ER_UNKNOWN_OPCODE", new Object[]{String.valueOf(n3)});
        return -1;
    }

    public void error(String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createXPATHMessage(string, arrobject);
        throw new TransformerException(string2);
    }

    public static int getFirstChildPos(int n2) {
        return n2 + 2;
    }

    public int getArgLength(int n2) {
        return this.m_opMap.elementAt(n2 + 1);
    }

    public int getArgLengthOfStep(int n2) {
        return this.m_opMap.elementAt(n2 + 1 + 1) - 3;
    }

    public static int getFirstChildPosOfStep(int n2) {
        return n2 + 3;
    }

    public int getStepTestType(int n2) {
        return this.m_opMap.elementAt(n2 + 3);
    }

    public String getStepNS(int n2) {
        int n3 = this.getArgLengthOfStep(n2);
        if (n3 == 3) {
            int n4 = this.m_opMap.elementAt(n2 + 4);
            if (n4 >= 0) {
                return (String)this.m_tokenQueue.elementAt(n4);
            }
            if (-3 == n4) {
                return "*";
            }
            return null;
        }
        return null;
    }

    public String getStepLocalName(int n2) {
        int n3;
        int n4 = this.getArgLengthOfStep(n2);
        switch (n4) {
            case 0: {
                n3 = -2;
                break;
            }
            case 1: {
                n3 = -3;
                break;
            }
            case 2: {
                n3 = this.m_opMap.elementAt(n2 + 4);
                break;
            }
            case 3: {
                n3 = this.m_opMap.elementAt(n2 + 5);
                break;
            }
            default: {
                n3 = -2;
            }
        }
        if (n3 >= 0) {
            return this.m_tokenQueue.elementAt(n3).toString();
        }
        if (-3 == n3) {
            return "*";
        }
        return null;
    }
}

