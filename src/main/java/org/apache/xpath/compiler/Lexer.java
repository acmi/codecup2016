/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.compiler;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.ObjectVector;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.Keywords;
import org.apache.xpath.compiler.OpMapVector;
import org.apache.xpath.compiler.XPathParser;

class Lexer {
    private Compiler m_compiler;
    PrefixResolver m_namespaceContext;
    XPathParser m_processor;
    private int[] m_patternMap = new int[100];
    private int m_patternMapSize;

    Lexer(Compiler compiler, PrefixResolver prefixResolver, XPathParser xPathParser) {
        this.m_compiler = compiler;
        this.m_namespaceContext = prefixResolver;
        this.m_processor = xPathParser;
    }

    void tokenize(String string) throws TransformerException {
        this.tokenize(string, null);
    }

    void tokenize(String string, Vector vector) throws TransformerException {
        this.m_compiler.m_currentPattern = string;
        this.m_patternMapSize = 0;
        int n2 = (string.length() < 500 ? string.length() : 500) * 5;
        this.m_compiler.m_opMap = new OpMapVector(n2, 2500, 1);
        int n3 = string.length();
        int n4 = -1;
        int n5 = -1;
        boolean bl = true;
        boolean bl2 = false;
        boolean bl3 = false;
        int n6 = 0;
        block9 : for (int i2 = 0; i2 < n3; ++i2) {
            char c2 = string.charAt(i2);
            switch (c2) {
                case '\"': {
                    if (n4 != -1) {
                        bl3 = false;
                        bl = this.mapPatternElemPos(n6, bl, bl2);
                        bl2 = false;
                        if (-1 != n5) {
                            n5 = this.mapNSTokens(string, n4, n5, i2);
                        } else {
                            this.addToTokenQueue(string.substring(n4, i2));
                        }
                    }
                    n4 = i2++;
                    while (i2 < n3 && (c2 = string.charAt(i2)) != '\"') {
                        ++i2;
                    }
                    if (c2 == '\"' && i2 < n3) {
                        this.addToTokenQueue(string.substring(n4, i2 + 1));
                        n4 = -1;
                        continue block9;
                    }
                    this.m_processor.error("ER_EXPECTED_DOUBLE_QUOTE", null);
                    continue block9;
                }
                case '\'': {
                    if (n4 != -1) {
                        bl3 = false;
                        bl = this.mapPatternElemPos(n6, bl, bl2);
                        bl2 = false;
                        if (-1 != n5) {
                            n5 = this.mapNSTokens(string, n4, n5, i2);
                        } else {
                            this.addToTokenQueue(string.substring(n4, i2));
                        }
                    }
                    n4 = i2++;
                    while (i2 < n3 && (c2 = string.charAt(i2)) != '\'') {
                        ++i2;
                    }
                    if (c2 == '\'' && i2 < n3) {
                        this.addToTokenQueue(string.substring(n4, i2 + 1));
                        n4 = -1;
                        continue block9;
                    }
                    this.m_processor.error("ER_EXPECTED_SINGLE_QUOTE", null);
                    continue block9;
                }
                case '\t': 
                case '\n': 
                case '\r': 
                case ' ': {
                    if (n4 == -1) continue block9;
                    bl3 = false;
                    bl = this.mapPatternElemPos(n6, bl, bl2);
                    bl2 = false;
                    if (-1 != n5) {
                        n5 = this.mapNSTokens(string, n4, n5, i2);
                    } else {
                        this.addToTokenQueue(string.substring(n4, i2));
                    }
                    n4 = -1;
                    continue block9;
                }
                case '@': {
                    bl2 = true;
                }
                case '-': {
                    if ('-' == c2) {
                        if (!bl3 && n4 != -1) continue block9;
                        bl3 = false;
                    }
                }
                case '!': 
                case '$': 
                case '(': 
                case ')': 
                case '*': 
                case '+': 
                case ',': 
                case '/': 
                case '<': 
                case '=': 
                case '>': 
                case '[': 
                case '\\': 
                case ']': 
                case '^': 
                case '|': {
                    if (n4 != -1) {
                        bl3 = false;
                        bl = this.mapPatternElemPos(n6, bl, bl2);
                        bl2 = false;
                        if (-1 != n5) {
                            n5 = this.mapNSTokens(string, n4, n5, i2);
                        } else {
                            this.addToTokenQueue(string.substring(n4, i2));
                        }
                        n4 = -1;
                    } else if ('/' == c2 && bl) {
                        bl = this.mapPatternElemPos(n6, bl, bl2);
                    } else if ('*' == c2) {
                        bl = this.mapPatternElemPos(n6, bl, bl2);
                        bl2 = false;
                    }
                    if (0 == n6 && '|' == c2) {
                        if (null != vector) {
                            this.recordTokenString(vector);
                        }
                        bl = true;
                    }
                    if (')' == c2 || ']' == c2) {
                        --n6;
                    } else if ('(' == c2 || '[' == c2) {
                        ++n6;
                    }
                    this.addToTokenQueue(string.substring(i2, i2 + 1));
                    continue block9;
                }
                case ':': {
                    if (i2 > 0) {
                        if (n5 == i2 - 1) {
                            if (n4 != -1 && n4 < i2 - 1) {
                                this.addToTokenQueue(string.substring(n4, i2 - 1));
                            }
                            bl3 = false;
                            bl2 = false;
                            n4 = -1;
                            n5 = -1;
                            this.addToTokenQueue(string.substring(i2 - 1, i2 + 1));
                            continue block9;
                        }
                        n5 = i2;
                    }
                }
                default: {
                    if (-1 == n4) {
                        n4 = i2;
                        bl3 = Character.isDigit(c2);
                        continue block9;
                    }
                    if (!bl3) continue block9;
                    bl3 = Character.isDigit(c2);
                }
            }
        }
        if (n4 != -1) {
            bl3 = false;
            bl = this.mapPatternElemPos(n6, bl, bl2);
            if (-1 != n5 || this.m_namespaceContext != null && this.m_namespaceContext.handlesNullPrefixes()) {
                n5 = this.mapNSTokens(string, n4, n5, n3);
            } else {
                this.addToTokenQueue(string.substring(n4, n3));
            }
        }
        if (0 == this.m_compiler.getTokenQueueSize()) {
            this.m_processor.error("ER_EMPTY_EXPRESSION", null);
        } else if (null != vector) {
            this.recordTokenString(vector);
        }
        this.m_processor.m_queueMark = 0;
    }

    private boolean mapPatternElemPos(int n2, boolean bl, boolean bl2) {
        if (0 == n2) {
            if (this.m_patternMapSize >= this.m_patternMap.length) {
                int[] arrn = this.m_patternMap;
                int n3 = this.m_patternMap.length;
                this.m_patternMap = new int[this.m_patternMapSize + 100];
                System.arraycopy(arrn, 0, this.m_patternMap, 0, n3);
            }
            if (!bl) {
                int[] arrn = this.m_patternMap;
                int n4 = this.m_patternMapSize - 1;
                arrn[n4] = arrn[n4] - 10000;
            }
            this.m_patternMap[this.m_patternMapSize] = this.m_compiler.getTokenQueueSize() - (bl2 ? 1 : 0) + 10000;
            ++this.m_patternMapSize;
            bl = false;
        }
        return bl;
    }

    private int getTokenQueuePosFromMap(int n2) {
        int n3 = this.m_patternMap[n2];
        return n3 >= 10000 ? n3 - 10000 : n3;
    }

    private final void resetTokenMark(int n2) {
        int n3 = this.m_compiler.getTokenQueueSize();
        int n4 = n2 > 0 ? (n2 <= n3 ? n2 - 1 : n2) : (this.m_processor.m_queueMark = 0);
        if (this.m_processor.m_queueMark < n3) {
            this.m_processor.m_token = (String)this.m_compiler.getTokenQueue().elementAt(this.m_processor.m_queueMark++);
            this.m_processor.m_tokenChar = this.m_processor.m_token.charAt(0);
        } else {
            this.m_processor.m_token = null;
            this.m_processor.m_tokenChar = '\u0000';
        }
    }

    final int getKeywordToken(String string) {
        int n2;
        try {
            Integer n3 = (Integer)Keywords.getKeyWord(string);
            n2 = null != n3 ? n3 : 0;
        }
        catch (NullPointerException nullPointerException) {
            n2 = 0;
        }
        catch (ClassCastException classCastException) {
            n2 = 0;
        }
        return n2;
    }

    private void recordTokenString(Vector vector) {
        int n2 = this.getTokenQueuePosFromMap(this.m_patternMapSize - 1);
        this.resetTokenMark(n2 + 1);
        if (this.m_processor.lookahead('(', 1)) {
            int n3 = this.getKeywordToken(this.m_processor.m_token);
            switch (n3) {
                case 1030: {
                    vector.addElement("#comment");
                    break;
                }
                case 1031: {
                    vector.addElement("#text");
                    break;
                }
                case 1033: {
                    vector.addElement("*");
                    break;
                }
                case 35: {
                    vector.addElement("/");
                    break;
                }
                case 36: {
                    vector.addElement("*");
                    break;
                }
                case 1032: {
                    vector.addElement("*");
                    break;
                }
                default: {
                    vector.addElement("*");
                    break;
                }
            }
        } else {
            if (this.m_processor.tokenIs('@')) {
                this.resetTokenMark(++n2 + 1);
            }
            if (this.m_processor.lookahead(':', 1)) {
                n2 += 2;
            }
            vector.addElement(this.m_compiler.getTokenQueue().elementAt(n2));
        }
    }

    private final void addToTokenQueue(String string) {
        this.m_compiler.getTokenQueue().addElement(string);
    }

    private int mapNSTokens(String string, int n2, int n3, int n4) throws TransformerException {
        String string2;
        String string3;
        block9 : {
            string3 = "";
            if (n2 >= 0 && n3 >= 0) {
                string3 = string.substring(n2, n3);
            }
            if (null != this.m_namespaceContext && !string3.equals("*") && !string3.equals("xmlns")) {
                try {
                    if (string3.length() > 0) {
                        string2 = this.m_namespaceContext.getNamespaceForPrefix(string3);
                        break block9;
                    }
                    string2 = this.m_namespaceContext.getNamespaceForPrefix(string3);
                }
                catch (ClassCastException classCastException) {
                    string2 = this.m_namespaceContext.getNamespaceForPrefix(string3);
                }
            } else {
                string2 = string3;
            }
        }
        if (null != string2 && string2.length() > 0) {
            this.addToTokenQueue(string2);
            this.addToTokenQueue(":");
            String string4 = string.substring(n3 + 1, n4);
            if (string4.length() > 0) {
                this.addToTokenQueue(string4);
            }
        } else {
            this.m_processor.errorForDOM3("ER_PREFIX_MUST_RESOLVE", new String[]{string3});
        }
        return -1;
    }
}

