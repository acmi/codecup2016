/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.impl.xpath.XPathException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;

public class XPath {
    private static final boolean DEBUG_ALL = false;
    private static final boolean DEBUG_XPATH_PARSE = false;
    private static final boolean DEBUG_ANY = false;
    protected final String fExpression;
    protected final SymbolTable fSymbolTable;
    protected final LocationPath[] fLocationPaths;

    public XPath(String string, SymbolTable symbolTable, NamespaceContext namespaceContext) throws XPathException {
        this.fExpression = string;
        this.fSymbolTable = symbolTable;
        this.fLocationPaths = this.parseExpression(namespaceContext);
    }

    public LocationPath[] getLocationPaths() {
        LocationPath[] arrlocationPath = new LocationPath[this.fLocationPaths.length];
        int n2 = 0;
        while (n2 < this.fLocationPaths.length) {
            arrlocationPath[n2] = (LocationPath)this.fLocationPaths[n2].clone();
            ++n2;
        }
        return arrlocationPath;
    }

    public LocationPath getLocationPath() {
        return (LocationPath)this.fLocationPaths[0].clone();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        while (n2 < this.fLocationPaths.length) {
            if (n2 > 0) {
                stringBuffer.append("|");
            }
            stringBuffer.append(this.fLocationPaths[n2].toString());
            ++n2;
        }
        return stringBuffer.toString();
    }

    private static void check(boolean bl) throws XPathException {
        if (!bl) {
            throw new XPathException("c-general-xpath");
        }
    }

    private LocationPath buildLocationPath(Vector vector) throws XPathException {
        int n2 = vector.size();
        XPath.check(n2 != 0);
        Object[] arrobject = new Step[n2];
        vector.copyInto(arrobject);
        vector.removeAllElements();
        return new LocationPath((Step[])arrobject);
    }

    private LocationPath[] parseExpression(NamespaceContext namespaceContext) throws XPathException {
        Scanner scanner = new Scanner(this, this.fSymbolTable){
            private final XPath this$0;

            protected void addToken(Tokens tokens, int n2) throws XPathException {
                if (n2 == 6 || n2 == 11 || n2 == 21 || n2 == 4 || n2 == 9 || n2 == 10 || n2 == 22 || n2 == 23 || n2 == 36 || n2 == 35 || n2 == 8) {
                    super.addToken(tokens, n2);
                    return;
                }
                throw new XPathException("c-general-xpath");
            }
        };
        Tokens tokens = new Tokens(this.fSymbolTable);
        int n2 = this.fExpression.length();
        boolean bl = scanner.scanExpr(this.fSymbolTable, tokens, this.fExpression, 0, n2);
        if (!bl) {
            throw new XPathException("c-general-xpath");
        }
        Vector<Step> vector = new Vector<Step>();
        ArrayList<LocationPath> arrayList = new ArrayList<LocationPath>();
        boolean bl2 = true;
        block11 : while (tokens.hasMore()) {
            int n3 = tokens.nextToken();
            switch (n3) {
                case 23: {
                    XPath.check(!bl2);
                    arrayList.add(this.buildLocationPath(vector));
                    bl2 = true;
                    break;
                }
                case 6: {
                    XPath.check(bl2);
                    Step step = new Step(new Axis(2), this.parseNodeTest(tokens.nextToken(), tokens, namespaceContext));
                    vector.addElement(step);
                    bl2 = false;
                    break;
                }
                case 35: {
                    XPath.check(bl2);
                    if (tokens.nextToken() != 8) {
                        throw new XPathException("c-general-xpath");
                    }
                    Step step = new Step(new Axis(2), this.parseNodeTest(tokens.nextToken(), tokens, namespaceContext));
                    vector.addElement(step);
                    bl2 = false;
                    break;
                }
                case 9: 
                case 10: 
                case 11: {
                    XPath.check(bl2);
                    Step step = new Step(new Axis(1), this.parseNodeTest(n3, tokens, namespaceContext));
                    vector.addElement(step);
                    bl2 = false;
                    break;
                }
                case 36: {
                    XPath.check(bl2);
                    if (tokens.nextToken() != 8) {
                        throw new XPathException("c-general-xpath");
                    }
                    Step step = new Step(new Axis(1), this.parseNodeTest(tokens.nextToken(), tokens, namespaceContext));
                    vector.addElement(step);
                    bl2 = false;
                    break;
                }
                case 4: {
                    XPath.check(bl2);
                    bl2 = false;
                    if (vector.size() != 0) continue block11;
                    Axis axis = new Axis(3);
                    NodeTest nodeTest = new NodeTest(3);
                    Step step = new Step(axis, nodeTest);
                    vector.addElement(step);
                    if (!tokens.hasMore() || tokens.peekToken() != 22) continue block11;
                    tokens.nextToken();
                    Axis axis2 = new Axis(4);
                    nodeTest = new NodeTest(3);
                    step = new Step(axis2, nodeTest);
                    vector.addElement(step);
                    bl2 = true;
                    break;
                }
                case 22: {
                    throw new XPathException("c-general-xpath");
                }
                case 8: {
                    throw new XPathException("c-general-xpath");
                }
                case 21: {
                    XPath.check(!bl2);
                    bl2 = true;
                    break;
                }
                default: {
                    throw new InternalError();
                }
            }
        }
        XPath.check(!bl2);
        arrayList.add(this.buildLocationPath(vector));
        return arrayList.toArray(new LocationPath[arrayList.size()]);
    }

    private NodeTest parseNodeTest(int n2, Tokens tokens, NamespaceContext namespaceContext) throws XPathException {
        switch (n2) {
            case 9: {
                return new NodeTest(2);
            }
            case 10: 
            case 11: {
                String string = tokens.nextTokenAsString();
                String string2 = null;
                if (namespaceContext != null && string != XMLSymbols.EMPTY_STRING) {
                    string2 = namespaceContext.getURI(string);
                }
                if (string != XMLSymbols.EMPTY_STRING && namespaceContext != null && string2 == null) {
                    throw new XPathException("c-general-xpath-ns");
                }
                if (n2 == 10) {
                    return new NodeTest(string, string2);
                }
                String string3 = tokens.nextTokenAsString();
                String string4 = string != XMLSymbols.EMPTY_STRING ? this.fSymbolTable.addSymbol(string + ':' + string3) : string3;
                return new NodeTest(new QName(string, string3, string4, string2));
            }
        }
        throw new XPathException("c-general-xpath");
    }

    public static void main(String[] arrstring) throws Exception {
        int n2 = 0;
        while (n2 < arrstring.length) {
            String string = arrstring[n2];
            System.out.println("# XPath expression: \"" + string + '\"');
            try {
                SymbolTable symbolTable = new SymbolTable();
                XPath xPath = new XPath(string, symbolTable, null);
                System.out.println("expanded xpath: \"" + xPath.toString() + '\"');
            }
            catch (XPathException xPathException) {
                System.out.println("error: " + xPathException.getMessage());
            }
            ++n2;
        }
    }

    private static class Scanner {
        private static final byte CHARTYPE_INVALID = 0;
        private static final byte CHARTYPE_OTHER = 1;
        private static final byte CHARTYPE_WHITESPACE = 2;
        private static final byte CHARTYPE_EXCLAMATION = 3;
        private static final byte CHARTYPE_QUOTE = 4;
        private static final byte CHARTYPE_DOLLAR = 5;
        private static final byte CHARTYPE_OPEN_PAREN = 6;
        private static final byte CHARTYPE_CLOSE_PAREN = 7;
        private static final byte CHARTYPE_STAR = 8;
        private static final byte CHARTYPE_PLUS = 9;
        private static final byte CHARTYPE_COMMA = 10;
        private static final byte CHARTYPE_MINUS = 11;
        private static final byte CHARTYPE_PERIOD = 12;
        private static final byte CHARTYPE_SLASH = 13;
        private static final byte CHARTYPE_DIGIT = 14;
        private static final byte CHARTYPE_COLON = 15;
        private static final byte CHARTYPE_LESS = 16;
        private static final byte CHARTYPE_EQUAL = 17;
        private static final byte CHARTYPE_GREATER = 18;
        private static final byte CHARTYPE_ATSIGN = 19;
        private static final byte CHARTYPE_LETTER = 20;
        private static final byte CHARTYPE_OPEN_BRACKET = 21;
        private static final byte CHARTYPE_CLOSE_BRACKET = 22;
        private static final byte CHARTYPE_UNDERSCORE = 23;
        private static final byte CHARTYPE_UNION = 24;
        private static final byte CHARTYPE_NONASCII = 25;
        private static final byte[] fASCIICharMap = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 1, 5, 1, 1, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 1, 16, 17, 18, 1, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 1, 22, 1, 23, 1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 1, 24, 1, 1, 1};
        private SymbolTable fSymbolTable;
        private static final String fAndSymbol = "and".intern();
        private static final String fOrSymbol = "or".intern();
        private static final String fModSymbol = "mod".intern();
        private static final String fDivSymbol = "div".intern();
        private static final String fCommentSymbol = "comment".intern();
        private static final String fTextSymbol = "text".intern();
        private static final String fPISymbol = "processing-instruction".intern();
        private static final String fNodeSymbol = "node".intern();
        private static final String fAncestorSymbol = "ancestor".intern();
        private static final String fAncestorOrSelfSymbol = "ancestor-or-self".intern();
        private static final String fAttributeSymbol = "attribute".intern();
        private static final String fChildSymbol = "child".intern();
        private static final String fDescendantSymbol = "descendant".intern();
        private static final String fDescendantOrSelfSymbol = "descendant-or-self".intern();
        private static final String fFollowingSymbol = "following".intern();
        private static final String fFollowingSiblingSymbol = "following-sibling".intern();
        private static final String fNamespaceSymbol = "namespace".intern();
        private static final String fParentSymbol = "parent".intern();
        private static final String fPrecedingSymbol = "preceding".intern();
        private static final String fPrecedingSiblingSymbol = "preceding-sibling".intern();
        private static final String fSelfSymbol = "self".intern();

        public Scanner(SymbolTable symbolTable) {
            this.fSymbolTable = symbolTable;
        }

        public boolean scanExpr(SymbolTable symbolTable, Tokens tokens, String string, int n2, int n3) throws XPathException {
            boolean bl = false;
            block23 : while (n2 != n3) {
                int n4 = string.charAt(n2);
                while (n4 == 32 || n4 == 10 || n4 == 9 || n4 == 13) {
                    if (++n2 == n3) break;
                    n4 = string.charAt(n2);
                }
                if (n2 == n3) break;
                int n5 = n4 >= 128 ? 25 : fASCIICharMap[n4];
                switch (n5) {
                    int n6;
                    String string2;
                    String string3;
                    case 6: {
                        this.addToken(tokens, 0);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 7: {
                        this.addToken(tokens, 1);
                        bl = true;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 21: {
                        this.addToken(tokens, 2);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 22: {
                        this.addToken(tokens, 3);
                        bl = true;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 12: {
                        if (n2 + 1 == n3) {
                            this.addToken(tokens, 4);
                            bl = true;
                            ++n2;
                            continue block23;
                        }
                        n4 = string.charAt(n2 + 1);
                        if (n4 == 46) {
                            this.addToken(tokens, 5);
                            bl = true;
                            n2 += 2;
                        } else if (n4 >= 48 && n4 <= 57) {
                            this.addToken(tokens, 47);
                            bl = true;
                            n2 = this.scanNumber(tokens, string, n3, n2);
                        } else if (n4 == 47) {
                            this.addToken(tokens, 4);
                            bl = true;
                            ++n2;
                        } else {
                            if (n4 == 124) {
                                this.addToken(tokens, 4);
                                bl = true;
                                ++n2;
                                continue block23;
                            }
                            if (n4 == 32 || n4 == 10 || n4 == 9 || n4 == 13) {
                                while (++n2 != n3 && ((n4 = (int)string.charAt(n2)) == 32 || n4 == 10 || n4 == 9 || n4 == 13)) {
                                }
                                if (n2 == n3 || n4 == 124) {
                                    this.addToken(tokens, 4);
                                    bl = true;
                                    continue block23;
                                }
                                throw new XPathException("c-general-xpath");
                            }
                            throw new XPathException("c-general-xpath");
                        }
                        if (n2 != n3) continue block23;
                        continue block23;
                    }
                    case 19: {
                        this.addToken(tokens, 6);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 10: {
                        this.addToken(tokens, 7);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 15: {
                        if (++n2 == n3) {
                            return false;
                        }
                        n4 = string.charAt(n2);
                        if (n4 != 58) {
                            return false;
                        }
                        this.addToken(tokens, 8);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 13: {
                        if (++n2 == n3) {
                            this.addToken(tokens, 21);
                            bl = false;
                            continue block23;
                        }
                        n4 = string.charAt(n2);
                        if (n4 == 47) {
                            this.addToken(tokens, 22);
                            bl = false;
                            if (++n2 != n3) continue block23;
                            continue block23;
                        }
                        this.addToken(tokens, 21);
                        bl = false;
                        continue block23;
                    }
                    case 24: {
                        this.addToken(tokens, 23);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 9: {
                        this.addToken(tokens, 24);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 11: {
                        this.addToken(tokens, 25);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 17: {
                        this.addToken(tokens, 26);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 3: {
                        if (++n2 == n3) {
                            return false;
                        }
                        n4 = string.charAt(n2);
                        if (n4 != 61) {
                            return false;
                        }
                        this.addToken(tokens, 27);
                        bl = false;
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 16: {
                        if (++n2 == n3) {
                            this.addToken(tokens, 28);
                            bl = false;
                            continue block23;
                        }
                        n4 = string.charAt(n2);
                        if (n4 == 61) {
                            this.addToken(tokens, 29);
                            bl = false;
                            if (++n2 != n3) continue block23;
                            continue block23;
                        }
                        this.addToken(tokens, 28);
                        bl = false;
                        continue block23;
                    }
                    case 18: {
                        if (++n2 == n3) {
                            this.addToken(tokens, 30);
                            bl = false;
                            continue block23;
                        }
                        n4 = string.charAt(n2);
                        if (n4 == 61) {
                            this.addToken(tokens, 31);
                            bl = false;
                            if (++n2 != n3) continue block23;
                            continue block23;
                        }
                        this.addToken(tokens, 30);
                        bl = false;
                        continue block23;
                    }
                    case 4: {
                        int n7 = n4;
                        if (++n2 == n3) {
                            return false;
                        }
                        n4 = string.charAt(n2);
                        int n8 = n2;
                        while (n4 != n7) {
                            if (++n2 == n3) {
                                return false;
                            }
                            n4 = string.charAt(n2);
                        }
                        int n9 = n2 - n8;
                        this.addToken(tokens, 46);
                        bl = true;
                        tokens.addToken(symbolTable.addSymbol(string.substring(n8, n8 + n9)));
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 14: {
                        this.addToken(tokens, 47);
                        bl = true;
                        n2 = this.scanNumber(tokens, string, n3, n2);
                        continue block23;
                    }
                    case 5: {
                        if (++n2 == n3) {
                            return false;
                        }
                        n6 = n2;
                        if ((n2 = this.scanNCName(string, n3, n2)) == n6) {
                            return false;
                        }
                        n4 = n2 < n3 ? (int)string.charAt(n2) : -1;
                        string3 = symbolTable.addSymbol(string.substring(n6, n2));
                        if (n4 != 58) {
                            string2 = XMLSymbols.EMPTY_STRING;
                        } else {
                            string2 = string3;
                            if (++n2 == n3) {
                                return false;
                            }
                            n6 = n2;
                            if ((n2 = this.scanNCName(string, n3, n2)) == n6) {
                                return false;
                            }
                            n4 = n2 < n3 ? (int)string.charAt(n2) : -1;
                            string3 = symbolTable.addSymbol(string.substring(n6, n2));
                        }
                        this.addToken(tokens, 48);
                        bl = true;
                        tokens.addToken(string2);
                        tokens.addToken(string3);
                        continue block23;
                    }
                    case 8: {
                        if (bl) {
                            this.addToken(tokens, 20);
                            bl = false;
                        } else {
                            this.addToken(tokens, 9);
                            bl = true;
                        }
                        if (++n2 != n3) continue block23;
                        continue block23;
                    }
                    case 20: 
                    case 23: 
                    case 25: {
                        n6 = n2;
                        n2 = this.scanNCName(string, n3, n2);
                        if (n2 == n6) {
                            return false;
                        }
                        n4 = n2 < n3 ? (int)string.charAt(n2) : -1;
                        string3 = symbolTable.addSymbol(string.substring(n6, n2));
                        boolean bl2 = false;
                        boolean bl3 = false;
                        string2 = XMLSymbols.EMPTY_STRING;
                        if (n4 == 58) {
                            if (++n2 == n3) {
                                return false;
                            }
                            n4 = string.charAt(n2);
                            if (n4 == 42) {
                                if (++n2 < n3) {
                                    n4 = string.charAt(n2);
                                }
                                bl2 = true;
                            } else if (n4 == 58) {
                                if (++n2 < n3) {
                                    n4 = string.charAt(n2);
                                }
                                bl3 = true;
                            } else {
                                string2 = string3;
                                n6 = n2;
                                if ((n2 = this.scanNCName(string, n3, n2)) == n6) {
                                    return false;
                                }
                                n4 = n2 < n3 ? (int)string.charAt(n2) : -1;
                                string3 = symbolTable.addSymbol(string.substring(n6, n2));
                            }
                        }
                        while (n4 == 32 || n4 == 10 || n4 == 9 || n4 == 13) {
                            if (++n2 == n3) break;
                            n4 = string.charAt(n2);
                        }
                        if (bl) {
                            if (string3 == fAndSymbol) {
                                this.addToken(tokens, 16);
                                bl = false;
                            } else if (string3 == fOrSymbol) {
                                this.addToken(tokens, 17);
                                bl = false;
                            } else if (string3 == fModSymbol) {
                                this.addToken(tokens, 18);
                                bl = false;
                            } else if (string3 == fDivSymbol) {
                                this.addToken(tokens, 19);
                                bl = false;
                            } else {
                                return false;
                            }
                            if (bl2) {
                                return false;
                            }
                            if (!bl3) continue block23;
                            return false;
                        }
                        if (n4 == 40 && !bl2 && !bl3) {
                            if (string3 == fCommentSymbol) {
                                this.addToken(tokens, 12);
                            } else if (string3 == fTextSymbol) {
                                this.addToken(tokens, 13);
                            } else if (string3 == fPISymbol) {
                                this.addToken(tokens, 14);
                            } else if (string3 == fNodeSymbol) {
                                this.addToken(tokens, 15);
                            } else {
                                this.addToken(tokens, 32);
                                tokens.addToken(string2);
                                tokens.addToken(string3);
                            }
                            this.addToken(tokens, 0);
                            bl = false;
                            if (++n2 != n3) continue block23;
                            continue block23;
                        }
                        if (bl3 || n4 == 58 && n2 + 1 < n3 && string.charAt(n2 + 1) == ':') {
                            if (string3 == fAncestorSymbol) {
                                this.addToken(tokens, 33);
                            } else if (string3 == fAncestorOrSelfSymbol) {
                                this.addToken(tokens, 34);
                            } else if (string3 == fAttributeSymbol) {
                                this.addToken(tokens, 35);
                            } else if (string3 == fChildSymbol) {
                                this.addToken(tokens, 36);
                            } else if (string3 == fDescendantSymbol) {
                                this.addToken(tokens, 37);
                            } else if (string3 == fDescendantOrSelfSymbol) {
                                this.addToken(tokens, 38);
                            } else if (string3 == fFollowingSymbol) {
                                this.addToken(tokens, 39);
                            } else if (string3 == fFollowingSiblingSymbol) {
                                this.addToken(tokens, 40);
                            } else if (string3 == fNamespaceSymbol) {
                                this.addToken(tokens, 41);
                            } else if (string3 == fParentSymbol) {
                                this.addToken(tokens, 42);
                            } else if (string3 == fPrecedingSymbol) {
                                this.addToken(tokens, 43);
                            } else if (string3 == fPrecedingSiblingSymbol) {
                                this.addToken(tokens, 44);
                            } else if (string3 == fSelfSymbol) {
                                this.addToken(tokens, 45);
                            } else {
                                return false;
                            }
                            if (bl2) {
                                return false;
                            }
                            this.addToken(tokens, 8);
                            bl = false;
                            if (bl3) continue block23;
                            ++n2;
                            if (++n2 != n3) continue block23;
                            continue block23;
                        }
                        if (bl2) {
                            this.addToken(tokens, 10);
                            bl = true;
                            tokens.addToken(string3);
                            continue block23;
                        }
                        this.addToken(tokens, 11);
                        bl = true;
                        tokens.addToken(string2);
                        tokens.addToken(string3);
                        continue block23;
                    }
                }
                return false;
            }
            return true;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        int scanNCName(String var1_1, int var2_2, int var3_3) {
            var4_4 = var1_1.charAt(var3_3);
            if (!(var4_4 >= '' ? XMLChar.isNameStart(var4_4) == false : (var5_5 = Scanner.fASCIICharMap[var4_4]) != 20 && var5_5 != 23)) ** GOTO lbl7
            return var3_3;
lbl-1000: // 1 sources:
            {
                var4_4 = var1_1.charAt(var3_3);
                if (!(var4_4 >= '' ? XMLChar.isName(var4_4) == false : (var5_5 = Scanner.fASCIICharMap[var4_4]) != 20 && var5_5 != 14 && var5_5 != 12 && var5_5 != 11 && var5_5 != 23)) continue;
                return var3_3;
lbl7: // 2 sources:
                ** while (++var3_3 < var2_2)
            }
lbl8: // 1 sources:
            return var3_3;
        }

        private int scanNumber(Tokens tokens, String string, int n2, int n3) {
            char c2 = string.charAt(n3);
            int n4 = 0;
            int n5 = 0;
            while (c2 >= '0' && c2 <= '9') {
                n4 = n4 * 10 + (c2 - 48);
                if (++n3 == n2) break;
                c2 = string.charAt(n3);
            }
            if (c2 == '.' && ++n3 < n2) {
                c2 = string.charAt(n3);
                while (c2 >= '0' && c2 <= '9') {
                    n5 = n5 * 10 + (c2 - 48);
                    if (++n3 == n2) break;
                    c2 = string.charAt(n3);
                }
                if (n5 != 0) {
                    throw new RuntimeException("find a solution!");
                }
            }
            tokens.addToken(n4);
            tokens.addToken(n5);
            return n3;
        }

        protected void addToken(Tokens tokens, int n2) throws XPathException {
            tokens.addToken(n2);
        }
    }

    private static final class Tokens {
        static final boolean DUMP_TOKENS = false;
        public static final int EXPRTOKEN_OPEN_PAREN = 0;
        public static final int EXPRTOKEN_CLOSE_PAREN = 1;
        public static final int EXPRTOKEN_OPEN_BRACKET = 2;
        public static final int EXPRTOKEN_CLOSE_BRACKET = 3;
        public static final int EXPRTOKEN_PERIOD = 4;
        public static final int EXPRTOKEN_DOUBLE_PERIOD = 5;
        public static final int EXPRTOKEN_ATSIGN = 6;
        public static final int EXPRTOKEN_COMMA = 7;
        public static final int EXPRTOKEN_DOUBLE_COLON = 8;
        public static final int EXPRTOKEN_NAMETEST_ANY = 9;
        public static final int EXPRTOKEN_NAMETEST_NAMESPACE = 10;
        public static final int EXPRTOKEN_NAMETEST_QNAME = 11;
        public static final int EXPRTOKEN_NODETYPE_COMMENT = 12;
        public static final int EXPRTOKEN_NODETYPE_TEXT = 13;
        public static final int EXPRTOKEN_NODETYPE_PI = 14;
        public static final int EXPRTOKEN_NODETYPE_NODE = 15;
        public static final int EXPRTOKEN_OPERATOR_AND = 16;
        public static final int EXPRTOKEN_OPERATOR_OR = 17;
        public static final int EXPRTOKEN_OPERATOR_MOD = 18;
        public static final int EXPRTOKEN_OPERATOR_DIV = 19;
        public static final int EXPRTOKEN_OPERATOR_MULT = 20;
        public static final int EXPRTOKEN_OPERATOR_SLASH = 21;
        public static final int EXPRTOKEN_OPERATOR_DOUBLE_SLASH = 22;
        public static final int EXPRTOKEN_OPERATOR_UNION = 23;
        public static final int EXPRTOKEN_OPERATOR_PLUS = 24;
        public static final int EXPRTOKEN_OPERATOR_MINUS = 25;
        public static final int EXPRTOKEN_OPERATOR_EQUAL = 26;
        public static final int EXPRTOKEN_OPERATOR_NOT_EQUAL = 27;
        public static final int EXPRTOKEN_OPERATOR_LESS = 28;
        public static final int EXPRTOKEN_OPERATOR_LESS_EQUAL = 29;
        public static final int EXPRTOKEN_OPERATOR_GREATER = 30;
        public static final int EXPRTOKEN_OPERATOR_GREATER_EQUAL = 31;
        public static final int EXPRTOKEN_FUNCTION_NAME = 32;
        public static final int EXPRTOKEN_AXISNAME_ANCESTOR = 33;
        public static final int EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF = 34;
        public static final int EXPRTOKEN_AXISNAME_ATTRIBUTE = 35;
        public static final int EXPRTOKEN_AXISNAME_CHILD = 36;
        public static final int EXPRTOKEN_AXISNAME_DESCENDANT = 37;
        public static final int EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF = 38;
        public static final int EXPRTOKEN_AXISNAME_FOLLOWING = 39;
        public static final int EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING = 40;
        public static final int EXPRTOKEN_AXISNAME_NAMESPACE = 41;
        public static final int EXPRTOKEN_AXISNAME_PARENT = 42;
        public static final int EXPRTOKEN_AXISNAME_PRECEDING = 43;
        public static final int EXPRTOKEN_AXISNAME_PRECEDING_SIBLING = 44;
        public static final int EXPRTOKEN_AXISNAME_SELF = 45;
        public static final int EXPRTOKEN_LITERAL = 46;
        public static final int EXPRTOKEN_NUMBER = 47;
        public static final int EXPRTOKEN_VARIABLE_REFERENCE = 48;
        private static final String[] fgTokenNames = new String[]{"EXPRTOKEN_OPEN_PAREN", "EXPRTOKEN_CLOSE_PAREN", "EXPRTOKEN_OPEN_BRACKET", "EXPRTOKEN_CLOSE_BRACKET", "EXPRTOKEN_PERIOD", "EXPRTOKEN_DOUBLE_PERIOD", "EXPRTOKEN_ATSIGN", "EXPRTOKEN_COMMA", "EXPRTOKEN_DOUBLE_COLON", "EXPRTOKEN_NAMETEST_ANY", "EXPRTOKEN_NAMETEST_NAMESPACE", "EXPRTOKEN_NAMETEST_QNAME", "EXPRTOKEN_NODETYPE_COMMENT", "EXPRTOKEN_NODETYPE_TEXT", "EXPRTOKEN_NODETYPE_PI", "EXPRTOKEN_NODETYPE_NODE", "EXPRTOKEN_OPERATOR_AND", "EXPRTOKEN_OPERATOR_OR", "EXPRTOKEN_OPERATOR_MOD", "EXPRTOKEN_OPERATOR_DIV", "EXPRTOKEN_OPERATOR_MULT", "EXPRTOKEN_OPERATOR_SLASH", "EXPRTOKEN_OPERATOR_DOUBLE_SLASH", "EXPRTOKEN_OPERATOR_UNION", "EXPRTOKEN_OPERATOR_PLUS", "EXPRTOKEN_OPERATOR_MINUS", "EXPRTOKEN_OPERATOR_EQUAL", "EXPRTOKEN_OPERATOR_NOT_EQUAL", "EXPRTOKEN_OPERATOR_LESS", "EXPRTOKEN_OPERATOR_LESS_EQUAL", "EXPRTOKEN_OPERATOR_GREATER", "EXPRTOKEN_OPERATOR_GREATER_EQUAL", "EXPRTOKEN_FUNCTION_NAME", "EXPRTOKEN_AXISNAME_ANCESTOR", "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF", "EXPRTOKEN_AXISNAME_ATTRIBUTE", "EXPRTOKEN_AXISNAME_CHILD", "EXPRTOKEN_AXISNAME_DESCENDANT", "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF", "EXPRTOKEN_AXISNAME_FOLLOWING", "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING", "EXPRTOKEN_AXISNAME_NAMESPACE", "EXPRTOKEN_AXISNAME_PARENT", "EXPRTOKEN_AXISNAME_PRECEDING", "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING", "EXPRTOKEN_AXISNAME_SELF", "EXPRTOKEN_LITERAL", "EXPRTOKEN_NUMBER", "EXPRTOKEN_VARIABLE_REFERENCE"};
        private static final int INITIAL_TOKEN_COUNT = 256;
        private int[] fTokens = new int[256];
        private int fTokenCount = 0;
        private SymbolTable fSymbolTable;
        private Hashtable fSymbolMapping = new Hashtable();
        private Hashtable fTokenNames = new Hashtable();
        private int fCurrentTokenIndex;

        public Tokens(SymbolTable symbolTable) {
            this.fSymbolTable = symbolTable;
            String[] arrstring = new String[]{"ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", "namespace", "parent", "preceding", "preceding-sibling", "self"};
            int n2 = 0;
            while (n2 < arrstring.length) {
                this.fSymbolMapping.put(this.fSymbolTable.addSymbol(arrstring[n2]), new Integer(n2));
                ++n2;
            }
            this.fTokenNames.put(new Integer(0), "EXPRTOKEN_OPEN_PAREN");
            this.fTokenNames.put(new Integer(1), "EXPRTOKEN_CLOSE_PAREN");
            this.fTokenNames.put(new Integer(2), "EXPRTOKEN_OPEN_BRACKET");
            this.fTokenNames.put(new Integer(3), "EXPRTOKEN_CLOSE_BRACKET");
            this.fTokenNames.put(new Integer(4), "EXPRTOKEN_PERIOD");
            this.fTokenNames.put(new Integer(5), "EXPRTOKEN_DOUBLE_PERIOD");
            this.fTokenNames.put(new Integer(6), "EXPRTOKEN_ATSIGN");
            this.fTokenNames.put(new Integer(7), "EXPRTOKEN_COMMA");
            this.fTokenNames.put(new Integer(8), "EXPRTOKEN_DOUBLE_COLON");
            this.fTokenNames.put(new Integer(9), "EXPRTOKEN_NAMETEST_ANY");
            this.fTokenNames.put(new Integer(10), "EXPRTOKEN_NAMETEST_NAMESPACE");
            this.fTokenNames.put(new Integer(11), "EXPRTOKEN_NAMETEST_QNAME");
            this.fTokenNames.put(new Integer(12), "EXPRTOKEN_NODETYPE_COMMENT");
            this.fTokenNames.put(new Integer(13), "EXPRTOKEN_NODETYPE_TEXT");
            this.fTokenNames.put(new Integer(14), "EXPRTOKEN_NODETYPE_PI");
            this.fTokenNames.put(new Integer(15), "EXPRTOKEN_NODETYPE_NODE");
            this.fTokenNames.put(new Integer(16), "EXPRTOKEN_OPERATOR_AND");
            this.fTokenNames.put(new Integer(17), "EXPRTOKEN_OPERATOR_OR");
            this.fTokenNames.put(new Integer(18), "EXPRTOKEN_OPERATOR_MOD");
            this.fTokenNames.put(new Integer(19), "EXPRTOKEN_OPERATOR_DIV");
            this.fTokenNames.put(new Integer(20), "EXPRTOKEN_OPERATOR_MULT");
            this.fTokenNames.put(new Integer(21), "EXPRTOKEN_OPERATOR_SLASH");
            this.fTokenNames.put(new Integer(22), "EXPRTOKEN_OPERATOR_DOUBLE_SLASH");
            this.fTokenNames.put(new Integer(23), "EXPRTOKEN_OPERATOR_UNION");
            this.fTokenNames.put(new Integer(24), "EXPRTOKEN_OPERATOR_PLUS");
            this.fTokenNames.put(new Integer(25), "EXPRTOKEN_OPERATOR_MINUS");
            this.fTokenNames.put(new Integer(26), "EXPRTOKEN_OPERATOR_EQUAL");
            this.fTokenNames.put(new Integer(27), "EXPRTOKEN_OPERATOR_NOT_EQUAL");
            this.fTokenNames.put(new Integer(28), "EXPRTOKEN_OPERATOR_LESS");
            this.fTokenNames.put(new Integer(29), "EXPRTOKEN_OPERATOR_LESS_EQUAL");
            this.fTokenNames.put(new Integer(30), "EXPRTOKEN_OPERATOR_GREATER");
            this.fTokenNames.put(new Integer(31), "EXPRTOKEN_OPERATOR_GREATER_EQUAL");
            this.fTokenNames.put(new Integer(32), "EXPRTOKEN_FUNCTION_NAME");
            this.fTokenNames.put(new Integer(33), "EXPRTOKEN_AXISNAME_ANCESTOR");
            this.fTokenNames.put(new Integer(34), "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF");
            this.fTokenNames.put(new Integer(35), "EXPRTOKEN_AXISNAME_ATTRIBUTE");
            this.fTokenNames.put(new Integer(36), "EXPRTOKEN_AXISNAME_CHILD");
            this.fTokenNames.put(new Integer(37), "EXPRTOKEN_AXISNAME_DESCENDANT");
            this.fTokenNames.put(new Integer(38), "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF");
            this.fTokenNames.put(new Integer(39), "EXPRTOKEN_AXISNAME_FOLLOWING");
            this.fTokenNames.put(new Integer(40), "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING");
            this.fTokenNames.put(new Integer(41), "EXPRTOKEN_AXISNAME_NAMESPACE");
            this.fTokenNames.put(new Integer(42), "EXPRTOKEN_AXISNAME_PARENT");
            this.fTokenNames.put(new Integer(43), "EXPRTOKEN_AXISNAME_PRECEDING");
            this.fTokenNames.put(new Integer(44), "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING");
            this.fTokenNames.put(new Integer(45), "EXPRTOKEN_AXISNAME_SELF");
            this.fTokenNames.put(new Integer(46), "EXPRTOKEN_LITERAL");
            this.fTokenNames.put(new Integer(47), "EXPRTOKEN_NUMBER");
            this.fTokenNames.put(new Integer(48), "EXPRTOKEN_VARIABLE_REFERENCE");
        }

        public String getTokenString(int n2) {
            return (String)this.fTokenNames.get(new Integer(n2));
        }

        public void addToken(String string) {
            Integer n2 = (Integer)this.fTokenNames.get(string);
            if (n2 == null) {
                n2 = new Integer(this.fTokenNames.size());
                this.fTokenNames.put(n2, string);
            }
            this.addToken(n2);
        }

        public void addToken(int n2) {
            try {
                this.fTokens[this.fTokenCount] = n2;
            }
            catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                int[] arrn = this.fTokens;
                this.fTokens = new int[this.fTokenCount << 1];
                System.arraycopy(arrn, 0, this.fTokens, 0, this.fTokenCount);
                this.fTokens[this.fTokenCount] = n2;
            }
            ++this.fTokenCount;
        }

        public void rewind() {
            this.fCurrentTokenIndex = 0;
        }

        public boolean hasMore() {
            return this.fCurrentTokenIndex < this.fTokenCount;
        }

        public int nextToken() throws XPathException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                throw new XPathException("c-general-xpath");
            }
            return this.fTokens[this.fCurrentTokenIndex++];
        }

        public int peekToken() throws XPathException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                throw new XPathException("c-general-xpath");
            }
            return this.fTokens[this.fCurrentTokenIndex];
        }

        public String nextTokenAsString() throws XPathException {
            String string = this.getTokenString(this.nextToken());
            if (string == null) {
                throw new XPathException("c-general-xpath");
            }
            return string;
        }

        public void dumpTokens() {
            int n2 = 0;
            while (n2 < this.fTokenCount) {
                switch (this.fTokens[n2]) {
                    case 0: {
                        System.out.print("<OPEN_PAREN/>");
                        break;
                    }
                    case 1: {
                        System.out.print("<CLOSE_PAREN/>");
                        break;
                    }
                    case 2: {
                        System.out.print("<OPEN_BRACKET/>");
                        break;
                    }
                    case 3: {
                        System.out.print("<CLOSE_BRACKET/>");
                        break;
                    }
                    case 4: {
                        System.out.print("<PERIOD/>");
                        break;
                    }
                    case 5: {
                        System.out.print("<DOUBLE_PERIOD/>");
                        break;
                    }
                    case 6: {
                        System.out.print("<ATSIGN/>");
                        break;
                    }
                    case 7: {
                        System.out.print("<COMMA/>");
                        break;
                    }
                    case 8: {
                        System.out.print("<DOUBLE_COLON/>");
                        break;
                    }
                    case 9: {
                        System.out.print("<NAMETEST_ANY/>");
                        break;
                    }
                    case 10: {
                        System.out.print("<NAMETEST_NAMESPACE");
                        System.out.print(" prefix=\"" + this.getTokenString(this.fTokens[++n2]) + "\"");
                        System.out.print("/>");
                        break;
                    }
                    case 11: {
                        System.out.print("<NAMETEST_QNAME");
                        if (this.fTokens[++n2] != -1) {
                            System.out.print(" prefix=\"" + this.getTokenString(this.fTokens[n2]) + "\"");
                        }
                        System.out.print(" localpart=\"" + this.getTokenString(this.fTokens[++n2]) + "\"");
                        System.out.print("/>");
                        break;
                    }
                    case 12: {
                        System.out.print("<NODETYPE_COMMENT/>");
                        break;
                    }
                    case 13: {
                        System.out.print("<NODETYPE_TEXT/>");
                        break;
                    }
                    case 14: {
                        System.out.print("<NODETYPE_PI/>");
                        break;
                    }
                    case 15: {
                        System.out.print("<NODETYPE_NODE/>");
                        break;
                    }
                    case 16: {
                        System.out.print("<OPERATOR_AND/>");
                        break;
                    }
                    case 17: {
                        System.out.print("<OPERATOR_OR/>");
                        break;
                    }
                    case 18: {
                        System.out.print("<OPERATOR_MOD/>");
                        break;
                    }
                    case 19: {
                        System.out.print("<OPERATOR_DIV/>");
                        break;
                    }
                    case 20: {
                        System.out.print("<OPERATOR_MULT/>");
                        break;
                    }
                    case 21: {
                        System.out.print("<OPERATOR_SLASH/>");
                        if (n2 + 1 >= this.fTokenCount) break;
                        System.out.println();
                        System.out.print("  ");
                        break;
                    }
                    case 22: {
                        System.out.print("<OPERATOR_DOUBLE_SLASH/>");
                        break;
                    }
                    case 23: {
                        System.out.print("<OPERATOR_UNION/>");
                        break;
                    }
                    case 24: {
                        System.out.print("<OPERATOR_PLUS/>");
                        break;
                    }
                    case 25: {
                        System.out.print("<OPERATOR_MINUS/>");
                        break;
                    }
                    case 26: {
                        System.out.print("<OPERATOR_EQUAL/>");
                        break;
                    }
                    case 27: {
                        System.out.print("<OPERATOR_NOT_EQUAL/>");
                        break;
                    }
                    case 28: {
                        System.out.print("<OPERATOR_LESS/>");
                        break;
                    }
                    case 29: {
                        System.out.print("<OPERATOR_LESS_EQUAL/>");
                        break;
                    }
                    case 30: {
                        System.out.print("<OPERATOR_GREATER/>");
                        break;
                    }
                    case 31: {
                        System.out.print("<OPERATOR_GREATER_EQUAL/>");
                        break;
                    }
                    case 32: {
                        System.out.print("<FUNCTION_NAME");
                        if (this.fTokens[++n2] != -1) {
                            System.out.print(" prefix=\"" + this.getTokenString(this.fTokens[n2]) + "\"");
                        }
                        System.out.print(" localpart=\"" + this.getTokenString(this.fTokens[++n2]) + "\"");
                        System.out.print("/>");
                        break;
                    }
                    case 33: {
                        System.out.print("<AXISNAME_ANCESTOR/>");
                        break;
                    }
                    case 34: {
                        System.out.print("<AXISNAME_ANCESTOR_OR_SELF/>");
                        break;
                    }
                    case 35: {
                        System.out.print("<AXISNAME_ATTRIBUTE/>");
                        break;
                    }
                    case 36: {
                        System.out.print("<AXISNAME_CHILD/>");
                        break;
                    }
                    case 37: {
                        System.out.print("<AXISNAME_DESCENDANT/>");
                        break;
                    }
                    case 38: {
                        System.out.print("<AXISNAME_DESCENDANT_OR_SELF/>");
                        break;
                    }
                    case 39: {
                        System.out.print("<AXISNAME_FOLLOWING/>");
                        break;
                    }
                    case 40: {
                        System.out.print("<AXISNAME_FOLLOWING_SIBLING/>");
                        break;
                    }
                    case 41: {
                        System.out.print("<AXISNAME_NAMESPACE/>");
                        break;
                    }
                    case 42: {
                        System.out.print("<AXISNAME_PARENT/>");
                        break;
                    }
                    case 43: {
                        System.out.print("<AXISNAME_PRECEDING/>");
                        break;
                    }
                    case 44: {
                        System.out.print("<AXISNAME_PRECEDING_SIBLING/>");
                        break;
                    }
                    case 45: {
                        System.out.print("<AXISNAME_SELF/>");
                        break;
                    }
                    case 46: {
                        System.out.print("<LITERAL");
                        System.out.print(" value=\"" + this.getTokenString(this.fTokens[++n2]) + "\"");
                        System.out.print("/>");
                        break;
                    }
                    case 47: {
                        System.out.print("<NUMBER");
                        System.out.print(" whole=\"" + this.getTokenString(this.fTokens[++n2]) + "\"");
                        System.out.print(" part=\"" + this.getTokenString(this.fTokens[++n2]) + "\"");
                        System.out.print("/>");
                        break;
                    }
                    case 48: {
                        System.out.print("<VARIABLE_REFERENCE");
                        if (this.fTokens[++n2] != -1) {
                            System.out.print(" prefix=\"" + this.getTokenString(this.fTokens[n2]) + "\"");
                        }
                        System.out.print(" localpart=\"" + this.getTokenString(this.fTokens[++n2]) + "\"");
                        System.out.print("/>");
                        break;
                    }
                    default: {
                        System.out.println("<???/>");
                    }
                }
                ++n2;
            }
            System.out.println();
        }
    }

    public static class NodeTest
    implements Cloneable {
        public static final short QNAME = 1;
        public static final short WILDCARD = 2;
        public static final short NODE = 3;
        public static final short NAMESPACE = 4;
        public final short type;
        public final QName name = new QName();

        public NodeTest(short s2) {
            this.type = s2;
        }

        public NodeTest(QName qName) {
            this.type = 1;
            this.name.setValues(qName);
        }

        public NodeTest(String string, String string2) {
            this.type = 4;
            this.name.setValues(string, null, null, string2);
        }

        public NodeTest(NodeTest nodeTest) {
            this.type = nodeTest.type;
            this.name.setValues(nodeTest.name);
        }

        public String toString() {
            switch (this.type) {
                case 1: {
                    if (this.name.prefix.length() != 0) {
                        if (this.name.uri != null) {
                            return this.name.prefix + ':' + this.name.localpart;
                        }
                        return "{" + this.name.uri + '}' + this.name.prefix + ':' + this.name.localpart;
                    }
                    return this.name.localpart;
                }
                case 4: {
                    if (this.name.prefix.length() != 0) {
                        if (this.name.uri != null) {
                            return this.name.prefix + ":*";
                        }
                        return "{" + this.name.uri + '}' + this.name.prefix + ":*";
                    }
                    return "???:*";
                }
                case 2: {
                    return "*";
                }
                case 3: {
                    return "node()";
                }
            }
            return "???";
        }

        public Object clone() {
            return new NodeTest(this);
        }
    }

    public static class Axis
    implements Cloneable {
        public static final short CHILD = 1;
        public static final short ATTRIBUTE = 2;
        public static final short SELF = 3;
        public static final short DESCENDANT = 4;
        public final short type;

        public Axis(short s2) {
            this.type = s2;
        }

        protected Axis(Axis axis) {
            this.type = axis.type;
        }

        public String toString() {
            switch (this.type) {
                case 1: {
                    return "child";
                }
                case 2: {
                    return "attribute";
                }
                case 3: {
                    return "self";
                }
                case 4: {
                    return "descendant";
                }
            }
            return "???";
        }

        public Object clone() {
            return new Axis(this);
        }
    }

    public static class Step
    implements Cloneable {
        public final Axis axis;
        public final NodeTest nodeTest;

        public Step(Axis axis, NodeTest nodeTest) {
            this.axis = axis;
            this.nodeTest = nodeTest;
        }

        protected Step(Step step) {
            this.axis = (Axis)step.axis.clone();
            this.nodeTest = (NodeTest)step.nodeTest.clone();
        }

        public String toString() {
            if (this.axis.type == 3) {
                return ".";
            }
            if (this.axis.type == 2) {
                return "@" + this.nodeTest.toString();
            }
            if (this.axis.type == 1) {
                return this.nodeTest.toString();
            }
            if (this.axis.type == 4) {
                return "//";
            }
            return "??? (" + this.axis.type + ')';
        }

        public Object clone() {
            return new Step(this);
        }
    }

    public static class LocationPath
    implements Cloneable {
        public final Step[] steps;

        public LocationPath(Step[] arrstep) {
            this.steps = arrstep;
        }

        protected LocationPath(LocationPath locationPath) {
            this.steps = new Step[locationPath.steps.length];
            int n2 = 0;
            while (n2 < this.steps.length) {
                this.steps[n2] = (Step)locationPath.steps[n2].clone();
                ++n2;
            }
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            int n2 = 0;
            while (n2 < this.steps.length) {
                if (n2 > 0 && this.steps[n2 - 1].axis.type != 4 && this.steps[n2].axis.type != 4) {
                    stringBuffer.append('/');
                }
                stringBuffer.append(this.steps[n2].toString());
                ++n2;
            }
            return stringBuffer.toString();
        }

        public Object clone() {
            return new LocationPath(this);
        }
    }

}

