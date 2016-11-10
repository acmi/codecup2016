/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xpath.XPath;
import org.apache.xerces.impl.xpath.XPathException;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.identity.ValueStore;
import org.apache.xerces.impl.xs.identity.XPathMatcher;
import org.apache.xerces.impl.xs.util.ShortListImpl;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class Field {
    protected final XPath fXPath;
    protected final IdentityConstraint fIdentityConstraint;

    public Field(XPath xPath, IdentityConstraint identityConstraint) {
        this.fXPath = xPath;
        this.fIdentityConstraint = identityConstraint;
    }

    public org.apache.xerces.impl.xpath.XPath getXPath() {
        return this.fXPath;
    }

    public IdentityConstraint getIdentityConstraint() {
        return this.fIdentityConstraint;
    }

    public XPathMatcher createMatcher(ValueStore valueStore) {
        return new Matcher(this, this.fXPath, valueStore);
    }

    public String toString() {
        return this.fXPath.toString();
    }

    protected class Matcher
    extends XPathMatcher {
        protected final ValueStore fStore;
        protected boolean fMayMatch;
        private final Field this$0;

        public Matcher(Field field, XPath xPath, ValueStore valueStore) {
            super(xPath);
            this.this$0 = field;
            this.fMayMatch = true;
            this.fStore = valueStore;
        }

        protected void matched(Object object, short s2, ShortList shortList, boolean bl) {
            super.matched(object, s2, shortList, bl);
            if (bl && this.this$0.fIdentityConstraint.getCategory() == 1) {
                String string = "KeyMatchesNillable";
                this.fStore.reportError(string, new Object[]{this.this$0.fIdentityConstraint.getElementName(), this.this$0.fIdentityConstraint.getIdentityConstraintName()});
            }
            this.fStore.addValue(this.this$0, this.fMayMatch, object, this.convertToPrimitiveKind(s2), this.convertToPrimitiveKind(shortList));
            this.fMayMatch = false;
        }

        private short convertToPrimitiveKind(short s2) {
            if (s2 <= 20) {
                return s2;
            }
            if (s2 <= 29) {
                return 2;
            }
            if (s2 <= 42) {
                return 4;
            }
            return s2;
        }

        private ShortList convertToPrimitiveKind(ShortList shortList) {
            if (shortList != null) {
                int n2 = shortList.getLength();
                int n3 = 0;
                while (n3 < n2) {
                    short s2 = shortList.item(n3);
                    if (s2 != this.convertToPrimitiveKind(s2)) break;
                    ++n3;
                }
                if (n3 != n2) {
                    short[] arrs = new short[n2];
                    int n4 = 0;
                    while (n4 < n3) {
                        arrs[n4] = shortList.item(n4);
                        ++n4;
                    }
                    while (n3 < n2) {
                        arrs[n3] = this.convertToPrimitiveKind(shortList.item(n3));
                        ++n3;
                    }
                    return new ShortListImpl(arrs, arrs.length);
                }
            }
            return shortList;
        }

        protected void handleContent(XSTypeDefinition xSTypeDefinition, boolean bl, Object object, short s2, ShortList shortList) {
            if (xSTypeDefinition == null || xSTypeDefinition.getTypeCategory() == 15 && ((XSComplexTypeDefinition)xSTypeDefinition).getContentType() != 1) {
                this.fStore.reportError("cvc-id.3", new Object[]{this.this$0.fIdentityConstraint.getName(), this.this$0.fIdentityConstraint.getElementName()});
            }
            this.fMatchedString = object;
            this.matched(this.fMatchedString, s2, shortList, bl);
        }
    }

    public static class XPath
    extends org.apache.xerces.impl.xpath.XPath {
        public XPath(String string, SymbolTable symbolTable, NamespaceContext namespaceContext) throws XPathException {
            super(XPath.fixupXPath(string), symbolTable, namespaceContext);
            int n2 = 0;
            while (n2 < this.fLocationPaths.length) {
                int n3 = 0;
                while (n3 < this.fLocationPaths[n2].steps.length) {
                    XPath.Axis axis = this.fLocationPaths[n2].steps[n3].axis;
                    if (axis.type == 2 && n3 < this.fLocationPaths[n2].steps.length - 1) {
                        throw new XPathException("c-fields-xpaths");
                    }
                    ++n3;
                }
                ++n2;
            }
        }

        private static String fixupXPath(String string) {
            int n2 = string.length();
            int n3 = 0;
            boolean bl = true;
            while (n3 < n2) {
                char c2 = string.charAt(n3);
                if (bl) {
                    if (!XMLChar.isSpace(c2)) {
                        if (c2 == '.' || c2 == '/') {
                            bl = false;
                        } else if (c2 != '|') {
                            return XPath.fixupXPath2(string, n3, n2);
                        }
                    }
                } else if (c2 == '|') {
                    bl = true;
                }
                ++n3;
            }
            return string;
        }

        private static String fixupXPath2(String string, int n2, int n3) {
            StringBuffer stringBuffer = new StringBuffer(n3 + 2);
            int n4 = 0;
            while (n4 < n2) {
                stringBuffer.append(string.charAt(n4));
                ++n4;
            }
            stringBuffer.append("./");
            boolean bl = false;
            while (n2 < n3) {
                char c2 = string.charAt(n2);
                if (bl) {
                    if (!XMLChar.isSpace(c2)) {
                        if (c2 == '.' || c2 == '/') {
                            bl = false;
                        } else if (c2 != '|') {
                            stringBuffer.append("./");
                            bl = false;
                        }
                    }
                } else if (c2 == '|') {
                    bl = true;
                }
                stringBuffer.append(c2);
                ++n2;
            }
            return stringBuffer.toString();
        }
    }

}

