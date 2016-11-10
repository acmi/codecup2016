/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.xs.models.XSCMBinOp;
import org.apache.xerces.impl.xs.models.XSCMLeaf;
import org.apache.xerces.impl.xs.models.XSCMRepeatingLeaf;
import org.apache.xerces.impl.xs.models.XSCMUniOp;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

public class CMNodeFactory {
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final boolean DEBUG = false;
    private static final int MULTIPLICITY = 1;
    private int nodeCount = 0;
    private int maxNodeLimit;
    private XMLErrorReporter fErrorReporter;
    private SecurityManager fSecurityManager = null;

    public void reset(XMLComponentManager xMLComponentManager) {
        this.fErrorReporter = (XMLErrorReporter)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        try {
            this.fSecurityManager = (SecurityManager)xMLComponentManager.getProperty("http://apache.org/xml/properties/security-manager");
            this.reset();
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            this.fSecurityManager = null;
        }
    }

    public void reset() {
        if (this.fSecurityManager != null) {
            this.maxNodeLimit = this.fSecurityManager.getMaxOccurNodeLimit() * 1;
        }
    }

    public CMNode getCMLeafNode(int n2, Object object, int n3, int n4) {
        this.nodeCountCheck();
        return new XSCMLeaf(n2, object, n3, n4);
    }

    public CMNode getCMRepeatingLeafNode(int n2, Object object, int n3, int n4, int n5, int n6) {
        this.nodeCountCheck();
        return new XSCMRepeatingLeaf(n2, object, n3, n4, n5, n6);
    }

    public CMNode getCMUniOpNode(int n2, CMNode cMNode) {
        this.nodeCountCheck();
        return new XSCMUniOp(n2, cMNode);
    }

    public CMNode getCMBinOpNode(int n2, CMNode cMNode, CMNode cMNode2) {
        this.nodeCountCheck();
        return new XSCMBinOp(n2, cMNode, cMNode2);
    }

    public void nodeCountCheck() {
        if (this.fSecurityManager != null && this.nodeCount++ > this.maxNodeLimit) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "maxOccurLimit", new Object[]{new Integer(this.maxNodeLimit)}, 2);
            this.nodeCount = 0;
        }
    }

    public void resetNodeCount() {
        this.nodeCount = 0;
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        if (string.startsWith("http://apache.org/xml/properties/")) {
            int n2 = string.length() - "http://apache.org/xml/properties/".length();
            if (n2 == "security-manager".length() && string.endsWith("security-manager")) {
                this.fSecurityManager = (SecurityManager)object;
                this.maxNodeLimit = this.fSecurityManager != null ? this.fSecurityManager.getMaxOccurNodeLimit() * 1 : 0;
                return;
            }
            if (n2 == "internal/error-reporter".length() && string.endsWith("internal/error-reporter")) {
                this.fErrorReporter = (XMLErrorReporter)object;
                return;
            }
        }
    }
}

