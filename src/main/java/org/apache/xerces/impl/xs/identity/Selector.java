/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xpath.XPath;
import org.apache.xerces.impl.xpath.XPathException;
import org.apache.xerces.impl.xs.identity.Field;
import org.apache.xerces.impl.xs.identity.FieldActivator;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.XPathMatcher;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSTypeDefinition;

public class Selector {
    protected final XPath fXPath;
    protected final IdentityConstraint fIdentityConstraint;
    protected IdentityConstraint fIDConstraint;

    public Selector(XPath xPath, IdentityConstraint identityConstraint) {
        this.fXPath = xPath;
        this.fIdentityConstraint = identityConstraint;
    }

    public org.apache.xerces.impl.xpath.XPath getXPath() {
        return this.fXPath;
    }

    public IdentityConstraint getIDConstraint() {
        return this.fIdentityConstraint;
    }

    public XPathMatcher createMatcher(FieldActivator fieldActivator, int n2) {
        return new Matcher(this, this.fXPath, fieldActivator, n2);
    }

    public String toString() {
        return this.fXPath.toString();
    }

    public class Matcher
    extends XPathMatcher {
        protected final FieldActivator fFieldActivator;
        protected final int fInitialDepth;
        protected int fElementDepth;
        protected int fMatchedDepth;
        private final Selector this$0;

        public Matcher(Selector selector, XPath xPath, FieldActivator fieldActivator, int n2) {
            super(xPath);
            this.this$0 = selector;
            this.fFieldActivator = fieldActivator;
            this.fInitialDepth = n2;
        }

        public void startDocumentFragment() {
            super.startDocumentFragment();
            this.fElementDepth = 0;
            this.fMatchedDepth = -1;
        }

        public void startElement(QName qName, XMLAttributes xMLAttributes) {
            super.startElement(qName, xMLAttributes);
            ++this.fElementDepth;
            if (this.isMatched()) {
                this.fMatchedDepth = this.fElementDepth;
                this.fFieldActivator.startValueScopeFor(this.this$0.fIdentityConstraint, this.fInitialDepth);
                int n2 = this.this$0.fIdentityConstraint.getFieldCount();
                int n3 = 0;
                while (n3 < n2) {
                    Field field = this.this$0.fIdentityConstraint.getFieldAt(n3);
                    XPathMatcher xPathMatcher = this.fFieldActivator.activateField(field, this.fInitialDepth);
                    xPathMatcher.startElement(qName, xMLAttributes);
                    ++n3;
                }
            }
        }

        public void endElement(QName qName, XSTypeDefinition xSTypeDefinition, boolean bl, Object object, short s2, ShortList shortList) {
            super.endElement(qName, xSTypeDefinition, bl, object, s2, shortList);
            if (this.fElementDepth-- == this.fMatchedDepth) {
                this.fMatchedDepth = -1;
                this.fFieldActivator.endValueScopeFor(this.this$0.fIdentityConstraint, this.fInitialDepth);
            }
        }

        public IdentityConstraint getIdentityConstraint() {
            return this.this$0.fIdentityConstraint;
        }

        public int getInitialDepth() {
            return this.fInitialDepth;
        }
    }

    public static class XPath
    extends org.apache.xerces.impl.xpath.XPath {
        public XPath(String string, SymbolTable symbolTable, NamespaceContext namespaceContext) throws XPathException {
            super(XPath.normalize(string), symbolTable, namespaceContext);
            int n2 = 0;
            while (n2 < this.fLocationPaths.length) {
                XPath.Axis axis = this.fLocationPaths[n2].steps[this.fLocationPaths[n2].steps.length - 1].axis;
                if (axis.type == 2) {
                    throw new XPathException("c-selector-xpath");
                }
                ++n2;
            }
        }

        private static String normalize(String string) {
            StringBuffer stringBuffer = new StringBuffer(string.length() + 5);
            int n2 = -1;
            do {
                if (!XMLChar.trim(string).startsWith("/") && !XMLChar.trim(string).startsWith(".")) {
                    stringBuffer.append("./");
                }
                if ((n2 = string.indexOf(124)) == -1) break;
                stringBuffer.append(string.substring(0, n2 + 1));
                string = string.substring(n2 + 1, string.length());
            } while (true);
            stringBuffer.append(string);
            return stringBuffer.toString();
        }
    }

}

