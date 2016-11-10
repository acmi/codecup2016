/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xpointer;

import java.util.HashMap;
import java.util.Locale;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xpointer.ShortHandPointer;
import org.apache.xerces.xpointer.XPointerErrorHandler;
import org.apache.xerces.xpointer.XPointerMessageFormatter;
import org.apache.xerces.xpointer.XPointerPart;

final class ElementSchemePointer
implements XPointerPart {
    private String fSchemeName;
    private String fSchemeData;
    private String fShortHandPointerName;
    private boolean fIsResolveElement = false;
    private boolean fIsElementFound = false;
    private boolean fWasOnlyEmptyElementFound = false;
    boolean fIsShortHand = false;
    int fFoundDepth = 0;
    private int[] fChildSequence;
    private int fCurrentChildPosition = 1;
    private int fCurrentChildDepth = 0;
    private int[] fCurrentChildSequence;
    private boolean fIsFragmentResolved = false;
    private ShortHandPointer fShortHandPointer;
    protected XMLErrorReporter fErrorReporter;
    protected XMLErrorHandler fErrorHandler;
    private SymbolTable fSymbolTable;

    public ElementSchemePointer() {
    }

    public ElementSchemePointer(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public ElementSchemePointer(SymbolTable symbolTable, XMLErrorReporter xMLErrorReporter) {
        this.fSymbolTable = symbolTable;
        this.fErrorReporter = xMLErrorReporter;
    }

    public void parseXPointer(String string) throws XNIException {
        this.init();
        Tokens tokens = new Tokens(this, this.fSymbolTable, null);
        Scanner scanner = new Scanner(this, this.fSymbolTable){
            private final ElementSchemePointer this$0;

            protected void addToken(Tokens tokens, int n2) throws XNIException {
                if (n2 == 1 || n2 == 0) {
                    super.addToken(tokens, n2);
                    return;
                }
                this.this$0.reportError("InvalidElementSchemeToken", new Object[]{Tokens.access$200(tokens, n2)});
            }
        };
        int n2 = string.length();
        boolean bl = Scanner.access$300(scanner, this.fSymbolTable, tokens, string, 0, n2);
        if (!bl) {
            this.reportError("InvalidElementSchemeXPointer", new Object[]{string});
        }
        int[] arrn = new int[Tokens.access$400(tokens) / 2 + 1];
        int n3 = 0;
        while (Tokens.access$500(tokens)) {
            int n4 = Tokens.access$600(tokens);
            switch (n4) {
                case 0: {
                    n4 = Tokens.access$600(tokens);
                    this.fShortHandPointerName = Tokens.access$200(tokens, n4);
                    this.fShortHandPointer = new ShortHandPointer(this.fSymbolTable);
                    this.fShortHandPointer.setSchemeName(this.fShortHandPointerName);
                    break;
                }
                case 1: {
                    arrn[n3] = Tokens.access$600(tokens);
                    ++n3;
                    break;
                }
                default: {
                    this.reportError("InvalidElementSchemeXPointer", new Object[]{string});
                }
            }
        }
        this.fChildSequence = new int[n3];
        this.fCurrentChildSequence = new int[n3];
        System.arraycopy(arrn, 0, this.fChildSequence, 0, n3);
    }

    public String getSchemeName() {
        return this.fSchemeName;
    }

    public String getSchemeData() {
        return this.fSchemeData;
    }

    public void setSchemeName(String string) {
        this.fSchemeName = string;
    }

    public void setSchemeData(String string) {
        this.fSchemeData = string;
    }

    public boolean resolveXPointer(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations, int n2) throws XNIException {
        boolean bl = false;
        if (this.fShortHandPointerName != null) {
            bl = this.fShortHandPointer.resolveXPointer(qName, xMLAttributes, augmentations, n2);
            if (bl) {
                this.fIsResolveElement = true;
                this.fIsShortHand = true;
            } else {
                this.fIsResolveElement = false;
            }
        } else {
            this.fIsResolveElement = true;
        }
        this.fIsFragmentResolved = this.fChildSequence.length > 0 ? this.matchChildSequence(qName, n2) : (bl && this.fChildSequence.length <= 0 ? bl : false);
        return this.fIsFragmentResolved;
    }

    protected boolean matchChildSequence(QName qName, int n2) throws XNIException {
        if (this.fCurrentChildDepth >= this.fCurrentChildSequence.length) {
            int[] arrn = new int[this.fCurrentChildSequence.length];
            System.arraycopy(this.fCurrentChildSequence, 0, arrn, 0, this.fCurrentChildSequence.length);
            this.fCurrentChildSequence = new int[this.fCurrentChildDepth * 2];
            System.arraycopy(arrn, 0, this.fCurrentChildSequence, 0, arrn.length);
        }
        if (this.fIsResolveElement) {
            if (n2 == 0) {
                this.fCurrentChildSequence[this.fCurrentChildDepth] = this.fCurrentChildPosition;
                ++this.fCurrentChildDepth;
                this.fCurrentChildPosition = 1;
                if (this.fCurrentChildDepth <= this.fFoundDepth || this.fFoundDepth == 0) {
                    if (this.checkMatch()) {
                        this.fIsElementFound = true;
                        this.fFoundDepth = this.fCurrentChildDepth;
                    } else {
                        this.fIsElementFound = false;
                        this.fFoundDepth = 0;
                    }
                }
            } else if (n2 == 1) {
                if (this.fCurrentChildDepth == this.fFoundDepth) {
                    this.fIsElementFound = true;
                } else if (this.fCurrentChildDepth < this.fFoundDepth && this.fFoundDepth != 0 || this.fCurrentChildDepth > this.fFoundDepth && this.fFoundDepth == 0) {
                    this.fIsElementFound = false;
                }
                this.fCurrentChildSequence[this.fCurrentChildDepth] = 0;
                --this.fCurrentChildDepth;
                this.fCurrentChildPosition = this.fCurrentChildSequence[this.fCurrentChildDepth] + 1;
            } else if (n2 == 2) {
                this.fCurrentChildSequence[this.fCurrentChildDepth] = this.fCurrentChildPosition++;
                if (this.checkMatch()) {
                    this.fWasOnlyEmptyElementFound = !this.fIsElementFound;
                    this.fIsElementFound = true;
                } else {
                    this.fIsElementFound = false;
                    this.fWasOnlyEmptyElementFound = false;
                }
            }
        }
        return this.fIsElementFound;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean checkMatch() {
        if (!this.fIsShortHand) {
            if (this.fChildSequence.length > this.fCurrentChildDepth + 1) return false;
            int n2 = 0;
            while (n2 < this.fChildSequence.length) {
                if (this.fChildSequence[n2] != this.fCurrentChildSequence[n2]) {
                    return false;
                }
                ++n2;
            }
            return true;
        } else {
            if (this.fChildSequence.length > this.fCurrentChildDepth + 1) return false;
            int n3 = 0;
            while (n3 < this.fChildSequence.length) {
                if (this.fCurrentChildSequence.length < n3 + 2) {
                    return false;
                }
                if (this.fChildSequence[n3] != this.fCurrentChildSequence[n3 + 1]) {
                    return false;
                }
                ++n3;
            }
        }
        return true;
    }

    public boolean isFragmentResolved() throws XNIException {
        return this.fIsFragmentResolved;
    }

    public boolean isChildFragmentResolved() {
        if (this.fIsShortHand && this.fShortHandPointer != null && this.fChildSequence.length <= 0) {
            return this.fShortHandPointer.isChildFragmentResolved();
        }
        return this.fWasOnlyEmptyElementFound ? !this.fWasOnlyEmptyElementFound : this.fIsFragmentResolved && this.fCurrentChildDepth >= this.fFoundDepth;
    }

    protected void reportError(String string, Object[] arrobject) throws XNIException {
        throw new XNIException(this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/XPTR").formatMessage(this.fErrorReporter.getLocale(), string, arrobject));
    }

    protected void initErrorReporter() {
        if (this.fErrorReporter == null) {
            this.fErrorReporter = new XMLErrorReporter();
        }
        if (this.fErrorHandler == null) {
            this.fErrorHandler = new XPointerErrorHandler();
        }
        this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/XPTR", new XPointerMessageFormatter());
    }

    protected void init() {
        this.fSchemeName = null;
        this.fSchemeData = null;
        this.fShortHandPointerName = null;
        this.fIsResolveElement = false;
        this.fIsElementFound = false;
        this.fWasOnlyEmptyElementFound = false;
        this.fFoundDepth = 0;
        this.fCurrentChildPosition = 1;
        this.fCurrentChildDepth = 0;
        this.fIsFragmentResolved = false;
        this.fShortHandPointer = null;
        this.initErrorReporter();
    }

    private class Scanner {
        private static final byte CHARTYPE_INVALID = 0;
        private static final byte CHARTYPE_OTHER = 1;
        private static final byte CHARTYPE_MINUS = 2;
        private static final byte CHARTYPE_PERIOD = 3;
        private static final byte CHARTYPE_SLASH = 4;
        private static final byte CHARTYPE_DIGIT = 5;
        private static final byte CHARTYPE_LETTER = 6;
        private static final byte CHARTYPE_UNDERSCORE = 7;
        private static final byte CHARTYPE_NONASCII = 8;
        private final byte[] fASCIICharMap;
        private SymbolTable fSymbolTable;
        private final ElementSchemePointer this$0;

        private Scanner(ElementSchemePointer elementSchemePointer, SymbolTable symbolTable) {
            this.this$0 = elementSchemePointer;
            this.fASCIICharMap = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 7, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 1};
            this.fSymbolTable = symbolTable;
        }

        private boolean scanExpr(SymbolTable symbolTable, Tokens tokens, String string, int n2, int n3) throws XNIException {
            String string2 = null;
            while (n2 != n3) {
                int n4 = string.charAt(n2);
                int n5 = n4 >= 128 ? 8 : this.fASCIICharMap[n4];
                switch (n5) {
                    case 4: {
                        if (++n2 == n3) {
                            return false;
                        }
                        this.addToken(tokens, 1);
                        n4 = string.charAt(n2);
                        int n6 = 0;
                        while (n4 >= 48 && n4 <= 57) {
                            n6 = n6 * 10 + (n4 - 48);
                            if (++n2 == n3) break;
                            n4 = string.charAt(n2);
                        }
                        if (n6 == 0) {
                            this.this$0.reportError("InvalidChildSequenceCharacter", new Object[]{new Character((char)n4)});
                            return false;
                        }
                        Tokens.access$700(tokens, n6);
                        break;
                    }
                    case 1: 
                    case 2: 
                    case 3: 
                    case 5: 
                    case 6: 
                    case 7: 
                    case 8: {
                        int n7 = n2;
                        n2 = this.scanNCName(string, n3, n2);
                        if (n2 == n7) {
                            this.this$0.reportError("InvalidNCNameInElementSchemeData", new Object[]{string});
                            return false;
                        }
                        n4 = n2 < n3 ? (int)string.charAt(n2) : -1;
                        string2 = symbolTable.addSymbol(string.substring(n7, n2));
                        this.addToken(tokens, 0);
                        Tokens.access$800(tokens, string2);
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
            if (!(var4_4 >= '' ? XMLChar.isNameStart(var4_4) == false : (var5_5 = this.fASCIICharMap[var4_4]) != 6 && var5_5 != 7)) ** GOTO lbl7
            return var3_3;
lbl-1000: // 1 sources:
            {
                var4_4 = var1_1.charAt(var3_3);
                if (!(var4_4 >= '' ? XMLChar.isName(var4_4) == false : (var5_5 = this.fASCIICharMap[var4_4]) != 6 && var5_5 != 5 && var5_5 != 3 && var5_5 != 2 && var5_5 != 7)) continue;
                return var3_3;
lbl7: // 2 sources:
                ** while (++var3_3 < var2_2)
            }
lbl8: // 1 sources:
            return var3_3;
        }

        protected void addToken(Tokens tokens, int n2) throws XNIException {
            Tokens.access$700(tokens, n2);
        }

        Scanner(ElementSchemePointer elementSchemePointer, SymbolTable symbolTable,  var3_3) {
            this(elementSchemePointer, symbolTable);
        }

        static boolean access$300(Scanner scanner, SymbolTable symbolTable, Tokens tokens, String string, int n2, int n3) throws XNIException {
            return scanner.scanExpr(symbolTable, tokens, string, n2, n3);
        }
    }

    private final class Tokens {
        private static final int XPTRTOKEN_ELEM_NCNAME = 0;
        private static final int XPTRTOKEN_ELEM_CHILD = 1;
        private final String[] fgTokenNames;
        private static final int INITIAL_TOKEN_COUNT = 256;
        private int[] fTokens;
        private int fTokenCount;
        private int fCurrentTokenIndex;
        private SymbolTable fSymbolTable;
        private HashMap fTokenNames;
        private final ElementSchemePointer this$0;

        private Tokens(ElementSchemePointer elementSchemePointer, SymbolTable symbolTable) {
            this.this$0 = elementSchemePointer;
            this.fgTokenNames = new String[]{"XPTRTOKEN_ELEM_NCNAME", "XPTRTOKEN_ELEM_CHILD"};
            this.fTokens = new int[256];
            this.fTokenCount = 0;
            this.fTokenNames = new HashMap();
            this.fSymbolTable = symbolTable;
            this.fTokenNames.put(new Integer(0), "XPTRTOKEN_ELEM_NCNAME");
            this.fTokenNames.put(new Integer(1), "XPTRTOKEN_ELEM_CHILD");
        }

        private String getTokenString(int n2) {
            return (String)this.fTokenNames.get(new Integer(n2));
        }

        private Integer getToken(int n2) {
            return (Integer)this.fTokenNames.get(new Integer(n2));
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
                this.this$0.reportError("XPointerElementSchemeProcessingError", null);
            }
            return this.fTokens[this.fCurrentTokenIndex++];
        }

        private int peekToken() throws XNIException {
            if (this.fCurrentTokenIndex == this.fTokenCount) {
                this.this$0.reportError("XPointerElementSchemeProcessingError", null);
            }
            return this.fTokens[this.fCurrentTokenIndex];
        }

        private String nextTokenAsString() throws XNIException {
            String string = this.getTokenString(this.nextToken());
            if (string == null) {
                this.this$0.reportError("XPointerElementSchemeProcessingError", null);
            }
            return string;
        }

        private int getTokenCount() {
            return this.fTokenCount;
        }

        Tokens(ElementSchemePointer elementSchemePointer, SymbolTable symbolTable,  var3_3) {
            this(elementSchemePointer, symbolTable);
        }

        static String access$200(Tokens tokens, int n2) {
            return tokens.getTokenString(n2);
        }

        static int access$400(Tokens tokens) {
            return tokens.getTokenCount();
        }

        static boolean access$500(Tokens tokens) {
            return tokens.hasMore();
        }

        static int access$600(Tokens tokens) throws XNIException {
            return tokens.nextToken();
        }

        static void access$700(Tokens tokens, int n2) {
            tokens.addToken(n2);
        }

        static void access$800(Tokens tokens, String string) {
            tokens.addToken(string);
        }
    }

}

