/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xpointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeNamespaceSupport;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xpointer.ElementSchemePointer;
import org.apache.xerces.xpointer.ShortHandPointer;
import org.apache.xerces.xpointer.XPointerErrorHandler;
import org.apache.xerces.xpointer.XPointerMessageFormatter;
import org.apache.xerces.xpointer.XPointerPart;
import org.apache.xerces.xpointer.XPointerProcessor;

public final class XPointerHandler
extends XIncludeHandler
implements XPointerProcessor {
    protected ArrayList fXPointerParts = new ArrayList();
    protected XPointerPart fXPointerPart = null;
    protected boolean fFoundMatchingPtrPart = false;
    protected XMLErrorReporter fXPointerErrorReporter;
    protected XMLErrorHandler fErrorHandler;
    protected SymbolTable fSymbolTable = null;
    private final String ELEMENT_SCHEME_NAME = "element";
    protected boolean fIsXPointerResolved = false;
    protected boolean fFixupBase = false;
    protected boolean fFixupLang = false;

    public XPointerHandler() {
        this.fSymbolTable = new SymbolTable();
    }

    public XPointerHandler(SymbolTable symbolTable, XMLErrorHandler xMLErrorHandler, XMLErrorReporter xMLErrorReporter) {
        this.fSymbolTable = symbolTable;
        this.fErrorHandler = xMLErrorHandler;
        this.fXPointerErrorReporter = xMLErrorReporter;
    }

    public void setDocumentHandler(XMLDocumentHandler xMLDocumentHandler) {
        this.fDocumentHandler = xMLDocumentHandler;
    }

    public void parseXPointer(String string) throws XNIException {
        this.init();
        Tokens tokens = new Tokens(this, this.fSymbolTable, null);
        Scanner scanner = new Scanner(this, this.fSymbolTable){
            private final XPointerHandler this$0;

            protected void addToken(Tokens tokens, int n2) throws XNIException {
                if (n2 == 0 || n2 == 1 || n2 == 3 || n2 == 4 || n2 == 2) {
                    super.addToken(tokens, n2);
                    return;
                }
                XPointerHandler.access$300(this.this$0, "InvalidXPointerToken", new Object[]{Tokens.access$200(tokens, n2)});
            }
        };
        int n2 = string.length();
        boolean bl = Scanner.access$400(scanner, this.fSymbolTable, tokens, string, 0, n2);
        if (!bl) {
            this.reportError("InvalidXPointerExpression", new Object[]{string});
        }
        while (Tokens.access$500(tokens)) {
            int n3 = Tokens.access$600(tokens);
            switch (n3) {
                String string2;
                Object object;
                case 2: {
                    n3 = Tokens.access$600(tokens);
                    string2 = Tokens.access$200(tokens, n3);
                    if (string2 == null) {
                        this.reportError("InvalidXPointerExpression", new Object[]{string});
                    }
                    object = new ShortHandPointer(this.fSymbolTable);
                    object.setSchemeName(string2);
                    this.fXPointerParts.add(object);
                    break;
                }
                case 3: {
                    n3 = Tokens.access$600(tokens);
                    string2 = Tokens.access$200(tokens, n3);
                    n3 = Tokens.access$600(tokens);
                    object = Tokens.access$200(tokens, n3);
                    String string3 = string2 + (String)object;
                    int n4 = 0;
                    int n5 = 0;
                    n3 = Tokens.access$600(tokens);
                    String string4 = Tokens.access$200(tokens, n3);
                    if (string4 != "XPTRTOKEN_OPEN_PAREN") {
                        if (n3 == 2) {
                            this.reportError("MultipleShortHandPointers", new Object[]{string});
                        } else {
                            this.reportError("InvalidXPointerExpression", new Object[]{string});
                        }
                    }
                    ++n4;
                    String string5 = null;
                    while (Tokens.access$500(tokens)) {
                        n3 = Tokens.access$600(tokens);
                        string5 = Tokens.access$200(tokens, n3);
                        if (string5 != "XPTRTOKEN_OPEN_PAREN") break;
                        ++n4;
                    }
                    n3 = Tokens.access$600(tokens);
                    string5 = Tokens.access$200(tokens, n3);
                    n3 = Tokens.access$600(tokens);
                    String string6 = Tokens.access$200(tokens, n3);
                    if (string6 != "XPTRTOKEN_CLOSE_PAREN") {
                        this.reportError("SchemeDataNotFollowedByCloseParenthesis", new Object[]{string});
                    }
                    ++n5;
                    while (Tokens.access$500(tokens)) {
                        if (Tokens.access$200(tokens, Tokens.access$700(tokens)) != "XPTRTOKEN_OPEN_PAREN") break;
                        ++n5;
                    }
                    if (n4 != n5) {
                        this.reportError("UnbalancedParenthesisInXPointerExpression", new Object[]{string, new Integer(n4), new Integer(n5)});
                    }
                    if (string3.equals("element")) {
                        ElementSchemePointer elementSchemePointer = new ElementSchemePointer(this.fSymbolTable, this.fErrorReporter);
                        elementSchemePointer.setSchemeName(string3);
                        elementSchemePointer.setSchemeData(string5);
                        try {
                            elementSchemePointer.parseXPointer(string5);
                            this.fXPointerParts.add(elementSchemePointer);
                            break;
                        }
                        catch (XNIException xNIException) {
                            throw new XNIException(xNIException);
                        }
                    }
                    this.reportWarning("SchemeUnsupported", new Object[]{string3});
                    break;
                }
                default: {
                    this.reportError("InvalidXPointerExpression", new Object[]{string});
                }
            }
        }
    }

    public boolean resolveXPointer(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations, int n2) throws XNIException {
        boolean bl = false;
        if (!this.fFoundMatchingPtrPart) {
            int n3 = 0;
            while (n3 < this.fXPointerParts.size()) {
                this.fXPointerPart = (XPointerPart)this.fXPointerParts.get(n3);
                if (this.fXPointerPart.resolveXPointer(qName, xMLAttributes, augmentations, n2)) {
                    this.fFoundMatchingPtrPart = true;
                    bl = true;
                }
                ++n3;
            }
        } else if (this.fXPointerPart.resolveXPointer(qName, xMLAttributes, augmentations, n2)) {
            bl = true;
        }
        if (!this.fIsXPointerResolved) {
            this.fIsXPointerResolved = bl;
        }
        return bl;
    }

    public boolean isFragmentResolved() throws XNIException {
        boolean bl;
        boolean bl2 = bl = this.fXPointerPart != null ? this.fXPointerPart.isFragmentResolved() : false;
        if (!this.fIsXPointerResolved) {
            this.fIsXPointerResolved = bl;
        }
        return bl;
    }

    public boolean isChildFragmentResolved() throws XNIException {
        boolean bl = this.fXPointerPart != null ? this.fXPointerPart.isChildFragmentResolved() : false;
        return bl;
    }

    public boolean isXPointerResolved() throws XNIException {
        return this.fIsXPointerResolved;
    }

    public XPointerPart getXPointerPart() {
        return this.fXPointerPart;
    }

    private void reportError(String string, Object[] arrobject) throws XNIException {
        throw new XNIException(this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/XPTR").formatMessage(this.fErrorReporter.getLocale(), string, arrobject));
    }

    private void reportWarning(String string, Object[] arrobject) throws XNIException {
        this.fXPointerErrorReporter.reportError("http://www.w3.org/TR/XPTR", string, arrobject, 0);
    }

    protected void initErrorReporter() {
        if (this.fXPointerErrorReporter == null) {
            this.fXPointerErrorReporter = new XMLErrorReporter();
        }
        if (this.fErrorHandler == null) {
            this.fErrorHandler = new XPointerErrorHandler();
        }
        this.fXPointerErrorReporter.putMessageFormatter("http://www.w3.org/TR/XPTR", new XPointerMessageFormatter());
    }

    protected void init() {
        this.fXPointerParts.clear();
        this.fXPointerPart = null;
        this.fFoundMatchingPtrPart = false;
        this.fIsXPointerResolved = false;
        this.initErrorReporter();
    }

    public ArrayList getPointerParts() {
        return this.fXPointerParts;
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (!this.isChildFragmentResolved()) {
            return;
        }
        super.comment(xMLString, augmentations);
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (!this.isChildFragmentResolved()) {
            return;
        }
        super.processingInstruction(string, xMLString, augmentations);
    }

    public void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        if (!this.resolveXPointer(qName, xMLAttributes, augmentations, 0)) {
            if (this.fFixupBase) {
                this.processXMLBaseAttributes(xMLAttributes);
            }
            if (this.fFixupLang) {
                this.processXMLLangAttributes(xMLAttributes);
            }
            this.fNamespaceContext.setContextInvalid();
            return;
        }
        super.startElement(qName, xMLAttributes, augmentations);
    }

    public void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException {
        if (!this.resolveXPointer(qName, xMLAttributes, augmentations, 2)) {
            if (this.fFixupBase) {
                this.processXMLBaseAttributes(xMLAttributes);
            }
            if (this.fFixupLang) {
                this.processXMLLangAttributes(xMLAttributes);
            }
            this.fNamespaceContext.setContextInvalid();
            return;
        }
        super.emptyElement(qName, xMLAttributes, augmentations);
    }

    public void characters(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (!this.isChildFragmentResolved()) {
            return;
        }
        super.characters(xMLString, augmentations);
    }

    public void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException {
        if (!this.isChildFragmentResolved()) {
            return;
        }
        super.ignorableWhitespace(xMLString, augmentations);
    }

    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        if (!this.resolveXPointer(qName, null, augmentations, 1)) {
            return;
        }
        super.endElement(qName, augmentations);
    }

    public void startCDATA(Augmentations augmentations) throws XNIException {
        if (!this.isChildFragmentResolved()) {
            return;
        }
        super.startCDATA(augmentations);
    }

    public void endCDATA(Augmentations augmentations) throws XNIException {
        if (!this.isChildFragmentResolved()) {
            return;
        }
        super.endCDATA(augmentations);
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        if (string == "http://apache.org/xml/properties/internal/error-reporter") {
            this.fXPointerErrorReporter = object != null ? (XMLErrorReporter)object : null;
        }
        if (string == "http://apache.org/xml/properties/internal/error-handler") {
            this.fErrorHandler = object != null ? (XMLErrorHandler)object : null;
        }
        if (string == "http://apache.org/xml/features/xinclude/fixup-language") {
            this.fFixupLang = object != null ? (Boolean)object : false;
        }
        if (string == "http://apache.org/xml/features/xinclude/fixup-base-uris") {
            this.fFixupBase = object != null ? (Boolean)object : false;
        }
        if (string == "http://apache.org/xml/properties/internal/namespace-context") {
            this.fNamespaceContext = (XIncludeNamespaceSupport)object;
        }
        super.setProperty(string, object);
    }

    static void access$300(XPointerHandler xPointerHandler, String string, Object[] arrobject) throws XNIException {
        xPointerHandler.reportError(string, arrobject);
    }

    private class Scanner {
        private static final byte CHARTYPE_INVALID = 0;
        private static final byte CHARTYPE_OTHER = 1;
        private static final byte CHARTYPE_WHITESPACE = 2;
        private static final byte CHARTYPE_CARRET = 3;
        private static final byte CHARTYPE_OPEN_PAREN = 4;
        private static final byte CHARTYPE_CLOSE_PAREN = 5;
        private static final byte CHARTYPE_MINUS = 6;
        private static final byte CHARTYPE_PERIOD = 7;
        private static final byte CHARTYPE_SLASH = 8;
        private static final byte CHARTYPE_DIGIT = 9;
        private static final byte CHARTYPE_COLON = 10;
        private static final byte CHARTYPE_EQUAL = 11;
        private static final byte CHARTYPE_LETTER = 12;
        private static final byte CHARTYPE_UNDERSCORE = 13;
        private static final byte CHARTYPE_NONASCII = 14;
        private final byte[] fASCIICharMap;
        private SymbolTable fSymbolTable;
        private final XPointerHandler this$0;

        private Scanner(XPointerHandler xPointerHandler, SymbolTable symbolTable) {
            this.this$0 = xPointerHandler;
            this.fASCIICharMap = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 4, 5, 1, 1, 1, 6, 7, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 1, 1, 11, 1, 1, 1, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 1, 1, 1, 3, 13, 1, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 1, 1, 1, 1, 1};
            this.fSymbolTable = symbolTable;
        }

        private boolean scanExpr(SymbolTable symbolTable, Tokens tokens, String string, int n2, int n3) throws XNIException {
            int n4 = 0;
            int n5 = 0;
            boolean bl = false;
            String string2 = null;
            String string3 = null;
            String string4 = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (n2 != n3) {
                int n6 = string.charAt(n2);
                while (n6 == 32 || n6 == 10 || n6 == 9 || n6 == 13) {
                    if (++n2 == n3) break;
                    n6 = string.charAt(n2);
                }
                if (n2 == n3) break;
                int n7 = n6 >= 128 ? 14 : this.fASCIICharMap[n6];
                switch (n7) {
                    case 4: {
                        this.addToken(tokens, 0);
                        ++n4;
                        ++n2;
                        break;
                    }
                    case 5: {
                        this.addToken(tokens, 1);
                        ++n5;
                        ++n2;
                        break;
                    }
                    case 1: 
                    case 2: 
                    case 3: 
                    case 6: 
                    case 7: 
                    case 8: 
                    case 9: 
                    case 10: 
                    case 11: 
                    case 12: 
                    case 13: 
                    case 14: {
                        if (n4 == 0) {
                            int n8 = n2;
                            if ((n2 = this.scanNCName(string, n3, n2)) == n8) {
                                XPointerHandler.access$300(this.this$0, "InvalidShortHandPointer", new Object[]{string});
                                return false;
                            }
                            n6 = n2 < n3 ? (int)string.charAt(n2) : -1;
                            string2 = symbolTable.addSymbol(string.substring(n8, n2));
                            string3 = XMLSymbols.EMPTY_STRING;
                            if (n6 == 58) {
                                if (++n2 == n3) {
                                    return false;
                                }
                                n6 = string.charAt(n2);
                                string3 = string2;
                                n8 = n2;
                                if ((n2 = this.scanNCName(string, n3, n2)) == n8) {
                                    return false;
                                }
                                n6 = n2 < n3 ? (int)string.charAt(n2) : -1;
                                bl = true;
                                string2 = symbolTable.addSymbol(string.substring(n8, n2));
                            }
                            if (n2 != n3) {
                                this.addToken(tokens, 3);
                                Tokens.access$800(tokens, string3);
                                Tokens.access$800(tokens, string2);
                                bl = false;
                            } else if (n2 == n3) {
                                this.addToken(tokens, 2);
                                Tokens.access$800(tokens, string2);
                                bl = false;
                            }
                            n5 = 0;
                            break;
                        }
                        if (n4 > 0 && n5 == 0 && string2 != null) {
                            int n9 = n2;
                            if ((n2 = this.scanData(string, stringBuffer, n3, n2)) == n9) {
                                XPointerHandler.access$300(this.this$0, "InvalidSchemeDataInXPointer", new Object[]{string});
                                return false;
                            }
                            n6 = n2 < n3 ? (int)string.charAt(n2) : -1;
                            string4 = symbolTable.addSymbol(stringBuffer.toString());
                            this.addToken(tokens, 4);
                            Tokens.access$800(tokens, string4);
                            n4 = 0;
                            stringBuffer.delete(0, stringBuffer.length());
                            break;
                        }
                        return false;
                    }
                }
            }
            return true;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        private int scanNCName(String var1_1, int var2_2, int var3_3) {
            var4_4 = var1_1.charAt(var3_3);
            if (!(var4_4 >= '' ? XMLChar.isNameStart(var4_4) == false : (var5_5 = this.fASCIICharMap[var4_4]) != 12 && var5_5 != 13)) ** GOTO lbl7
            return var3_3;
lbl-1000: // 1 sources:
            {
                var4_4 = var1_1.charAt(var3_3);
                if (!(var4_4 >= '' ? XMLChar.isName(var4_4) == false : (var5_5 = this.fASCIICharMap[var4_4]) != 12 && var5_5 != 9 && var5_5 != 7 && var5_5 != 6 && var5_5 != 13)) continue;
                return var3_3;
lbl7: // 2 sources:
                ** while (++var3_3 < var2_2)
            }
lbl8: // 1 sources:
            return var3_3;
        }

        private int scanData(String string, StringBuffer stringBuffer, int n2, int n3) {
            while (n3 != n2) {
                int n4;
                char c2 = string.charAt(n3);
                int n5 = n4 = c2 >= '' ? 14 : this.fASCIICharMap[c2];
                if (n4 == 4) {
                    stringBuffer.append((int)c2);
                    ++n3;
                    n3 = this.scanData(string, stringBuffer, n2, n3);
                    if (n3 == n2) {
                        return n3;
                    }
                    c2 = string.charAt(n3);
                    int n6 = n4 = c2 >= '' ? 14 : this.fASCIICharMap[c2];
                    if (n4 != 5) {
                        return n2;
                    }
                    stringBuffer.append(c2);
                    ++n3;
                    continue;
                }
                if (n4 == 5) {
                    return n3;
                }
                if (n4 == 3) {
                    int n7 = n4 = (c2 = string.charAt(++n3)) >= '' ? 14 : this.fASCIICharMap[c2];
                    if (n4 != 3 && n4 != 4 && n4 != 5) break;
                    stringBuffer.append(c2);
                    ++n3;
                    continue;
                }
                stringBuffer.append(c2);
                ++n3;
            }
            return n3;
        }

        protected void addToken(Tokens tokens, int n2) throws XNIException {
            Tokens.access$900(tokens, n2);
        }

        Scanner(XPointerHandler xPointerHandler, SymbolTable symbolTable,  var3_3) {
            this(xPointerHandler, symbolTable);
        }

        static boolean access$400(Scanner scanner, SymbolTable symbolTable, Tokens tokens, String string, int n2, int n3) throws XNIException {
            return scanner.scanExpr(symbolTable, tokens, string, n2, n3);
        }
    }

    private final class Tokens {
        private static final int XPTRTOKEN_OPEN_PAREN = 0;
        private static final int XPTRTOKEN_CLOSE_PAREN = 1;
        private static final int XPTRTOKEN_SHORTHAND = 2;
        private static final int XPTRTOKEN_SCHEMENAME = 3;
        private static final int XPTRTOKEN_SCHEMEDATA = 4;
        private final String[] fgTokenNames;
        private static final int INITIAL_TOKEN_COUNT = 256;
        private int[] fTokens;
        private int fTokenCount;
        private int fCurrentTokenIndex;
        private SymbolTable fSymbolTable;
        private HashMap fTokenNames;
        private final XPointerHandler this$0;

        private Tokens(XPointerHandler xPointerHandler, SymbolTable symbolTable) {
            this.this$0 = xPointerHandler;
            this.fgTokenNames = new String[]{"XPTRTOKEN_OPEN_PAREN", "XPTRTOKEN_CLOSE_PAREN", "XPTRTOKEN_SHORTHAND", "XPTRTOKEN_SCHEMENAME", "XPTRTOKEN_SCHEMEDATA"};
            this.fTokens = new int[256];
            this.fTokenCount = 0;
            this.fTokenNames = new HashMap();
            this.fSymbolTable = symbolTable;
            this.fTokenNames.put(new Integer(0), "XPTRTOKEN_OPEN_PAREN");
            this.fTokenNames.put(new Integer(1), "XPTRTOKEN_CLOSE_PAREN");
            this.fTokenNames.put(new Integer(2), "XPTRTOKEN_SHORTHAND");
            this.fTokenNames.put(new Integer(3), "XPTRTOKEN_SCHEMENAME");
            this.fTokenNames.put(new Integer(4), "XPTRTOKEN_SCHEMEDATA");
        }

        private String getTokenString(int n2) {
            return (String)this.fTokenNames.get(new Integer(n2));
        }

        private void addToken(String string) {
            Integer n2 = (Integer)this.fTokenNames.get(string);
            if (n2 == null) {
                n2 = new Integer(this.fTokenNames.size());
                this.fTokenNames.put(n2, string);
            }
            this.addToken(n2);
        }

        private void addToken(int n2) {
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

        private void rewind() {
            this.fCurrentTokenIndex = 0;
        }

        private boolean hasMore() {
            return this.fCurrentTokenIndex < this.fTokenCount;
        }

        private int nextToken() throws XNIException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                XPointerHandler.access$300(this.this$0, "XPointerProcessingError", null);
            }
            return this.fTokens[this.fCurrentTokenIndex++];
        }

        private int peekToken() throws XNIException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                XPointerHandler.access$300(this.this$0, "XPointerProcessingError", null);
            }
            return this.fTokens[this.fCurrentTokenIndex];
        }

        private String nextTokenAsString() throws XNIException {
            String string = this.getTokenString(this.nextToken());
            if (string == null) {
                XPointerHandler.access$300(this.this$0, "XPointerProcessingError", null);
            }
            return string;
        }

        Tokens(XPointerHandler xPointerHandler, SymbolTable symbolTable,  var3_3) {
            this(xPointerHandler, symbolTable);
        }

        static String access$200(Tokens tokens, int n2) {
            return tokens.getTokenString(n2);
        }

        static boolean access$500(Tokens tokens) {
            return tokens.hasMore();
        }

        static int access$600(Tokens tokens) throws XNIException {
            return tokens.nextToken();
        }

        static int access$700(Tokens tokens) throws XNIException {
            return tokens.peekToken();
        }

        static void access$800(Tokens tokens, String string) {
            tokens.addToken(string);
        }

        static void access$900(Tokens tokens, int n2) {
            tokens.addToken(n2);
        }
    }

}

