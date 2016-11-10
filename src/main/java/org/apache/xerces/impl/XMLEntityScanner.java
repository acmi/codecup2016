/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.io.UCSReader;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;

public class XMLEntityScanner
implements XMLLocator {
    private static final boolean DEBUG_ENCODINGS = false;
    private static final boolean DEBUG_BUFFER = false;
    private static final EOFException END_OF_DOCUMENT_ENTITY = new EOFException(){
        private static final long serialVersionUID = 980337771224675268L;

        public Throwable fillInStackTrace() {
            return this;
        }
    };
    private XMLEntityManager fEntityManager = null;
    protected XMLEntityManager.ScannedEntity fCurrentEntity = null;
    protected SymbolTable fSymbolTable = null;
    protected int fBufferSize = 2048;
    protected XMLErrorReporter fErrorReporter;

    public final String getBaseSystemId() {
        return this.fCurrentEntity != null && this.fCurrentEntity.entityLocation != null ? this.fCurrentEntity.entityLocation.getExpandedSystemId() : null;
    }

    public final void setEncoding(String string) throws IOException {
        if (!(this.fCurrentEntity.stream == null || this.fCurrentEntity.encoding != null && this.fCurrentEntity.encoding.equals(string))) {
            if (this.fCurrentEntity.encoding != null && this.fCurrentEntity.encoding.startsWith("UTF-16")) {
                String string2 = string.toUpperCase(Locale.ENGLISH);
                if (string2.equals("UTF-16")) {
                    return;
                }
                if (string2.equals("ISO-10646-UCS-4")) {
                    this.fCurrentEntity.reader = this.fCurrentEntity.encoding.equals("UTF-16BE") ? new UCSReader(this.fCurrentEntity.stream, 8) : new UCSReader(this.fCurrentEntity.stream, 4);
                    return;
                }
                if (string2.equals("ISO-10646-UCS-2")) {
                    this.fCurrentEntity.reader = this.fCurrentEntity.encoding.equals("UTF-16BE") ? new UCSReader(this.fCurrentEntity.stream, 2) : new UCSReader(this.fCurrentEntity.stream, 1);
                    return;
                }
            }
            this.fCurrentEntity.setReader(this.fCurrentEntity.stream, string, null);
            this.fCurrentEntity.encoding = string;
        }
    }

    public final void setXMLVersion(String string) {
        this.fCurrentEntity.xmlVersion = string;
    }

    public final boolean isExternal() {
        return this.fCurrentEntity.isExternal();
    }

    public int peekChar() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        int n2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (this.fCurrentEntity.isExternal()) {
            return n2 != 13 ? n2 : 10;
        }
        return n2;
    }

    public int scanChar() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        int n2 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
        boolean bl = false;
        if (n2 == 10 || n2 == 13 && (bl = this.fCurrentEntity.isExternal())) {
            ++this.fCurrentEntity.lineNumber;
            this.fCurrentEntity.columnNumber = 1;
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = n2;
                this.load(1, false);
            }
            if (n2 == 13 && bl) {
                if (this.fCurrentEntity.ch[this.fCurrentEntity.position++] != '\n') {
                    --this.fCurrentEntity.position;
                }
                n2 = 10;
            }
        }
        ++this.fCurrentEntity.columnNumber;
        return n2;
    }

    public String scanNmtoken() throws IOException {
        int n2;
        char[] arrc;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        int n3 = this.fCurrentEntity.position;
        while (XMLChar.isName(this.fCurrentEntity.ch[this.fCurrentEntity.position])) {
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
            n2 = this.fCurrentEntity.position - n3;
            if (n2 == this.fCurrentEntity.ch.length) {
                arrc = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, n3, arrc, 0, n2);
                this.fCurrentEntity.ch = arrc;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, n3, this.fCurrentEntity.ch, 0, n2);
            }
            n3 = 0;
            if (this.load(n2, false)) break;
        }
        n2 = this.fCurrentEntity.position - n3;
        this.fCurrentEntity.columnNumber += n2;
        arrc = null;
        if (n2 > 0) {
            arrc = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, n3, n2);
        }
        return arrc;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public String scanName() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        if (!XMLChar.isNameStart(this.fCurrentEntity.ch[var1_1 = this.fCurrentEntity.position++])) ** GOTO lbl21
        if (this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl20
        this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[var1_1];
        var1_1 = 0;
        if (!this.load(1, false)) ** GOTO lbl20
        ++this.fCurrentEntity.columnNumber;
        return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
lbl-1000: // 1 sources:
        {
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
            var2_3 = this.fCurrentEntity.position - var1_1;
            if (var2_3 == this.fCurrentEntity.ch.length) {
                var3_4 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var1_1, var3_4, 0, var2_3);
                this.fCurrentEntity.ch = var3_4;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var2_3);
            }
            var1_1 = 0;
            if (this.load(var2_3, false)) break;
lbl20: // 4 sources:
            ** while (XMLChar.isName((int)this.fCurrentEntity.ch[this.fCurrentEntity.position]))
        }
lbl21: // 3 sources:
        var2_3 = this.fCurrentEntity.position - var1_1;
        this.fCurrentEntity.columnNumber += var2_3;
        var3_4 = null;
        if (var2_3 <= 0) return var3_4;
        return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var1_1, var2_3);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public String scanNCName() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        if (!XMLChar.isNCNameStart(this.fCurrentEntity.ch[var1_1 = this.fCurrentEntity.position++])) ** GOTO lbl21
        if (this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl20
        this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[var1_1];
        var1_1 = 0;
        if (!this.load(1, false)) ** GOTO lbl20
        ++this.fCurrentEntity.columnNumber;
        return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
lbl-1000: // 1 sources:
        {
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
            var2_3 = this.fCurrentEntity.position - var1_1;
            if (var2_3 == this.fCurrentEntity.ch.length) {
                var3_4 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var1_1, var3_4, 0, var2_3);
                this.fCurrentEntity.ch = var3_4;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var2_3);
            }
            var1_1 = 0;
            if (this.load(var2_3, false)) break;
lbl20: // 4 sources:
            ** while (XMLChar.isNCName((int)this.fCurrentEntity.ch[this.fCurrentEntity.position]))
        }
lbl21: // 3 sources:
        var2_3 = this.fCurrentEntity.position - var1_1;
        this.fCurrentEntity.columnNumber += var2_3;
        var3_4 = null;
        if (var2_3 <= 0) return var3_4;
        return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var1_1, var2_3);
    }

    public boolean scanQName(QName qName) throws IOException {
        int n2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        if (XMLChar.isNCNameStart(this.fCurrentEntity.ch[n2 = this.fCurrentEntity.position++])) {
            Object object;
            int n3;
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[n2];
                n2 = 0;
                if (this.load(1, false)) {
                    ++this.fCurrentEntity.columnNumber;
                    String string = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
                    qName.setValues(null, string, string, null);
                    return true;
                }
            }
            int n4 = -1;
            while (XMLChar.isName(this.fCurrentEntity.ch[this.fCurrentEntity.position])) {
                n3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
                if (n3 == 58) {
                    if (n4 != -1) break;
                    n4 = this.fCurrentEntity.position;
                }
                if (++this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
                int n5 = this.fCurrentEntity.position - n2;
                if (n5 == this.fCurrentEntity.ch.length) {
                    object = new char[this.fCurrentEntity.ch.length << 1];
                    System.arraycopy(this.fCurrentEntity.ch, n2, object, 0, n5);
                    this.fCurrentEntity.ch = object;
                } else {
                    System.arraycopy(this.fCurrentEntity.ch, n2, this.fCurrentEntity.ch, 0, n5);
                }
                if (n4 != -1) {
                    n4 -= n2;
                }
                n2 = 0;
                if (this.load(n5, false)) break;
            }
            n3 = this.fCurrentEntity.position - n2;
            this.fCurrentEntity.columnNumber += n3;
            if (n3 > 0) {
                String string = null;
                object = null;
                char[] arrc = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, n2, n3);
                if (n4 != -1) {
                    int n6 = n4 - n2;
                    string = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, n2, n6);
                    int n7 = n3 - n6 - 1;
                    int n8 = n4 + 1;
                    if (!XMLChar.isNCNameStart(this.fCurrentEntity.ch[n8])) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IllegalQName", null, 2);
                    }
                    object = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, n8, n7);
                } else {
                    object = arrc;
                }
                qName.setValues(string, (String)object, (String)arrc, null);
                return true;
            }
        }
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public int scanContent(XMLString var1_1) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
            this.load(1, false);
            this.fCurrentEntity.position = 0;
            this.fCurrentEntity.startPosition = 0;
        }
        var2_2 = this.fCurrentEntity.position;
        var3_3 = this.fCurrentEntity.ch[var2_2];
        var4_4 = 0;
        var5_5 = this.fCurrentEntity.isExternal();
        if (var3_3 != 10 && (var3_3 != 13 || !var5_5)) ** GOTO lbl57
        do {
            if ((var3_3 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) == 13 && var5_5) {
                ++var4_4;
                ++this.fCurrentEntity.lineNumber;
                this.fCurrentEntity.columnNumber = 1;
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    var2_2 = 0;
                    this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                    this.fCurrentEntity.position = var4_4;
                    this.fCurrentEntity.startPosition = var4_4;
                    if (this.load(var4_4, false)) break;
                }
                if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
                    ++this.fCurrentEntity.position;
                    ++var2_2;
                    continue;
                }
                ++var4_4;
                continue;
            }
            if (var3_3 == 10) {
                ++var4_4;
                ++this.fCurrentEntity.lineNumber;
                this.fCurrentEntity.columnNumber = 1;
                if (this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
                var2_2 = 0;
                this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                this.fCurrentEntity.position = var4_4;
                this.fCurrentEntity.startPosition = var4_4;
                if (!this.load(var4_4, false)) continue;
                break;
            }
            --this.fCurrentEntity.position;
            break;
        } while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
        var6_6 = var2_2;
        while (var6_6 < this.fCurrentEntity.position) {
            this.fCurrentEntity.ch[var6_6] = 10;
            ++var6_6;
        }
        var7_7 = this.fCurrentEntity.position - var2_2;
        if (this.fCurrentEntity.position != this.fCurrentEntity.count - 1) ** GOTO lbl57
        var1_1.setValues(this.fCurrentEntity.ch, var2_2, var7_7);
        return -1;
lbl-1000: // 1 sources:
        {
            if (XMLChar.isContent(var3_3 = this.fCurrentEntity.ch[this.fCurrentEntity.position++])) continue;
            --this.fCurrentEntity.position;
            break;
lbl57: // 3 sources:
            ** while (this.fCurrentEntity.position < this.fCurrentEntity.count)
        }
lbl58: // 2 sources:
        var6_6 = this.fCurrentEntity.position - var2_2;
        this.fCurrentEntity.columnNumber += var6_6 - var4_4;
        var1_1.setValues(this.fCurrentEntity.ch, var2_2, var6_6);
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) return -1;
        var3_3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (var3_3 != 13) return var3_3;
        if (var5_5 == false) return var3_3;
        return 10;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public int scanLiteral(int var1_1, XMLString var2_2) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
            this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
            this.load(1, false);
            this.fCurrentEntity.position = 0;
            this.fCurrentEntity.startPosition = 0;
        }
        var3_3 = this.fCurrentEntity.position;
        var4_4 = this.fCurrentEntity.ch[var3_3];
        var5_5 = 0;
        var6_6 = this.fCurrentEntity.isExternal();
        if (var4_4 != 10 && (var4_4 != 13 || !var6_6)) ** GOTO lbl57
        do {
            if ((var4_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) == 13 && var6_6) {
                ++var5_5;
                ++this.fCurrentEntity.lineNumber;
                this.fCurrentEntity.columnNumber = 1;
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    var3_3 = 0;
                    this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                    this.fCurrentEntity.position = var5_5;
                    this.fCurrentEntity.startPosition = var5_5;
                    if (this.load(var5_5, false)) break;
                }
                if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
                    ++this.fCurrentEntity.position;
                    ++var3_3;
                    continue;
                }
                ++var5_5;
                continue;
            }
            if (var4_4 == 10) {
                ++var5_5;
                ++this.fCurrentEntity.lineNumber;
                this.fCurrentEntity.columnNumber = 1;
                if (this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
                var3_3 = 0;
                this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                this.fCurrentEntity.position = var5_5;
                this.fCurrentEntity.startPosition = var5_5;
                if (!this.load(var5_5, false)) continue;
                break;
            }
            --this.fCurrentEntity.position;
            break;
        } while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
        var7_7 = var3_3;
        while (var7_7 < this.fCurrentEntity.position) {
            this.fCurrentEntity.ch[var7_7] = 10;
            ++var7_7;
        }
        var8_8 = this.fCurrentEntity.position - var3_3;
        if (this.fCurrentEntity.position != this.fCurrentEntity.count - 1) ** GOTO lbl57
        var2_2.setValues(this.fCurrentEntity.ch, var3_3, var8_8);
        return -1;
lbl-1000: // 1 sources:
        {
            if (((var4_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) != var1_1 || this.fCurrentEntity.literal && !var6_6) && var4_4 != 37 && XMLChar.isContent(var4_4)) continue;
            --this.fCurrentEntity.position;
            break;
lbl57: // 3 sources:
            ** while (this.fCurrentEntity.position < this.fCurrentEntity.count)
        }
lbl58: // 2 sources:
        var7_7 = this.fCurrentEntity.position - var3_3;
        this.fCurrentEntity.columnNumber += var7_7 - var5_5;
        var2_2.setValues(this.fCurrentEntity.ch, var3_3, var7_7);
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) return -1;
        var4_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (var4_4 != var1_1) return var4_4;
        if (this.fCurrentEntity.literal == false) return var4_4;
        return -1;
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean scanData(String var1_1, XMLStringBuffer var2_2) throws IOException {
        var3_3 = false;
        var4_4 = var1_1.length();
        var5_5 = var1_1.charAt(0);
        var6_6 = this.fCurrentEntity.isExternal();
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        var7_7 = false;
        while (this.fCurrentEntity.position > this.fCurrentEntity.count - var4_4 && !var7_7) {
            System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.position, this.fCurrentEntity.ch, 0, this.fCurrentEntity.count - this.fCurrentEntity.position);
            var7_7 = this.load(this.fCurrentEntity.count - this.fCurrentEntity.position, false);
            this.fCurrentEntity.position = 0;
            this.fCurrentEntity.startPosition = 0;
        }
        if (this.fCurrentEntity.position > this.fCurrentEntity.count - var4_4) {
            var8_8 = this.fCurrentEntity.count - this.fCurrentEntity.position;
            var2_2.append(this.fCurrentEntity.ch, this.fCurrentEntity.position, var8_8);
            this.fCurrentEntity.columnNumber += this.fCurrentEntity.count;
            this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
            this.fCurrentEntity.position = this.fCurrentEntity.count;
            this.fCurrentEntity.startPosition = this.fCurrentEntity.count;
            this.load(0, true);
            return false;
        }
        var8_9 = this.fCurrentEntity.position;
        var9_10 = this.fCurrentEntity.ch[var8_9];
        var10_11 = 0;
        if (var9_10 != '\n' && (var9_10 != '\r' || !var6_6)) ** GOTO lbl93
        do {
            if ((var9_10 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) == '\r' && var6_6) {
                ++var10_11;
                ++this.fCurrentEntity.lineNumber;
                this.fCurrentEntity.columnNumber = 1;
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    var8_9 = 0;
                    this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                    this.fCurrentEntity.position = var10_11;
                    this.fCurrentEntity.startPosition = var10_11;
                    if (this.load(var10_11, false)) break;
                }
                if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
                    ++this.fCurrentEntity.position;
                    ++var8_9;
                    continue;
                }
                ++var10_11;
                continue;
            }
            if (var9_10 == '\n') {
                ++var10_11;
                ++this.fCurrentEntity.lineNumber;
                this.fCurrentEntity.columnNumber = 1;
                if (this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
                var8_9 = 0;
                this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                this.fCurrentEntity.position = var10_11;
                this.fCurrentEntity.startPosition = var10_11;
                this.fCurrentEntity.count = var10_11;
                if (!this.load(var10_11, false)) continue;
                break;
            }
            --this.fCurrentEntity.position;
            break;
        } while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
        var11_12 = var8_9;
        while (var11_12 < this.fCurrentEntity.position) {
            this.fCurrentEntity.ch[var11_12] = 10;
            ++var11_12;
        }
        var12_13 = this.fCurrentEntity.position - var8_9;
        if (this.fCurrentEntity.position != this.fCurrentEntity.count - 1) ** GOTO lbl93
        var2_2.append(this.fCurrentEntity.ch, var8_9, var12_13);
        return true;
lbl-1000: // 1 sources:
        {
            if ((var9_10 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) == var5_5) {
                var11_12 = this.fCurrentEntity.position - 1;
                var12_13 = 1;
                while (var12_13 < var4_4) {
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                        this.fCurrentEntity.position -= var12_13;
                        break block3;
                    }
                    var9_10 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
                    if (var1_1.charAt(var12_13) != var9_10) {
                        --this.fCurrentEntity.position;
                        break;
                    }
                    ++var12_13;
                }
                if (this.fCurrentEntity.position != var11_12 + var4_4) continue;
                var3_3 = true;
                break;
            }
            if (var9_10 == '\n' || var6_6 && var9_10 == '\r') {
                --this.fCurrentEntity.position;
                break;
            }
            if (!XMLChar.isInvalid(var9_10)) continue;
            --this.fCurrentEntity.position;
            var11_12 = this.fCurrentEntity.position - var8_9;
            this.fCurrentEntity.columnNumber += var11_12 - var10_11;
            var2_2.append(this.fCurrentEntity.ch, var8_9, var11_12);
            return true;
lbl93: // 4 sources:
            ** while (this.fCurrentEntity.position < this.fCurrentEntity.count)
        }
lbl94: // 4 sources:
        var11_12 = this.fCurrentEntity.position - var8_9;
        this.fCurrentEntity.columnNumber += var11_12 - var10_11;
        if (var3_3) {
            var11_12 -= var4_4;
        }
        var2_2.append(this.fCurrentEntity.ch, var8_9, var11_12);
        if (var3_3 != false) return false;
        return true;
    }

    public boolean skipChar(int n2) throws IOException {
        char c2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        if ((c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) == n2) {
            ++this.fCurrentEntity.position;
            if (n2 == 10) {
                ++this.fCurrentEntity.lineNumber;
                this.fCurrentEntity.columnNumber = 1;
            } else {
                ++this.fCurrentEntity.columnNumber;
            }
            return true;
        }
        if (n2 == 10 && c2 == '\r' && this.fCurrentEntity.isExternal()) {
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = c2;
                this.load(1, false);
            }
            ++this.fCurrentEntity.position;
            if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
                ++this.fCurrentEntity.position;
            }
            ++this.fCurrentEntity.lineNumber;
            this.fCurrentEntity.columnNumber = 1;
            return true;
        }
        return false;
    }

    public boolean skipSpaces() throws IOException {
        char c2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        if (XMLChar.isSpace(c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position])) {
            boolean bl = this.fCurrentEntity.isExternal();
            do {
                boolean bl2 = false;
                if (c2 == '\n' || bl && c2 == '\r') {
                    ++this.fCurrentEntity.lineNumber;
                    this.fCurrentEntity.columnNumber = 1;
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                        this.fCurrentEntity.ch[0] = c2;
                        bl2 = this.load(1, true);
                        if (!bl2) {
                            this.fCurrentEntity.position = 0;
                            this.fCurrentEntity.startPosition = 0;
                        }
                    }
                    if (c2 == '\r' && bl && this.fCurrentEntity.ch[++this.fCurrentEntity.position] != '\n') {
                        --this.fCurrentEntity.position;
                    }
                } else {
                    ++this.fCurrentEntity.columnNumber;
                }
                if (!bl2) {
                    ++this.fCurrentEntity.position;
                }
                if (this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
                this.load(0, true);
            } while (XMLChar.isSpace(c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position]));
            return true;
        }
        return false;
    }

    public final boolean skipDeclSpaces() throws IOException {
        char c2;
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        if (XMLChar.isSpace(c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position])) {
            boolean bl = this.fCurrentEntity.isExternal();
            do {
                boolean bl2 = false;
                if (c2 == '\n' || bl && c2 == '\r') {
                    ++this.fCurrentEntity.lineNumber;
                    this.fCurrentEntity.columnNumber = 1;
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                        this.fCurrentEntity.ch[0] = c2;
                        bl2 = this.load(1, true);
                        if (!bl2) {
                            this.fCurrentEntity.position = 0;
                            this.fCurrentEntity.startPosition = 0;
                        }
                    }
                    if (c2 == '\r' && bl && this.fCurrentEntity.ch[++this.fCurrentEntity.position] != '\n') {
                        --this.fCurrentEntity.position;
                    }
                } else {
                    ++this.fCurrentEntity.columnNumber;
                }
                if (!bl2) {
                    ++this.fCurrentEntity.position;
                }
                if (this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
                this.load(0, true);
            } while (XMLChar.isSpace(c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position]));
            return true;
        }
        return false;
    }

    public boolean skipString(String string) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2;
            if ((c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) != string.charAt(n3)) {
                this.fCurrentEntity.position -= n3 + 1;
                return false;
            }
            if (n3 < n2 - 1 && this.fCurrentEntity.position == this.fCurrentEntity.count) {
                System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.count - n3 - 1, this.fCurrentEntity.ch, 0, n3 + 1);
                if (this.load(n3 + 1, false)) {
                    this.fCurrentEntity.startPosition -= n3 + 1;
                    this.fCurrentEntity.position -= n3 + 1;
                    return false;
                }
            }
            ++n3;
        }
        this.fCurrentEntity.columnNumber += n2;
        return true;
    }

    public final String getPublicId() {
        return this.fCurrentEntity != null && this.fCurrentEntity.entityLocation != null ? this.fCurrentEntity.entityLocation.getPublicId() : null;
    }

    public final String getExpandedSystemId() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.entityLocation != null && this.fCurrentEntity.entityLocation.getExpandedSystemId() != null) {
                return this.fCurrentEntity.entityLocation.getExpandedSystemId();
            }
            return this.fCurrentEntity.getExpandedSystemId();
        }
        return null;
    }

    public final String getLiteralSystemId() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.entityLocation != null && this.fCurrentEntity.entityLocation.getLiteralSystemId() != null) {
                return this.fCurrentEntity.entityLocation.getLiteralSystemId();
            }
            return this.fCurrentEntity.getLiteralSystemId();
        }
        return null;
    }

    public final int getLineNumber() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.isExternal()) {
                return this.fCurrentEntity.lineNumber;
            }
            return this.fCurrentEntity.getLineNumber();
        }
        return -1;
    }

    public final int getColumnNumber() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.isExternal()) {
                return this.fCurrentEntity.columnNumber;
            }
            return this.fCurrentEntity.getColumnNumber();
        }
        return -1;
    }

    public final int getCharacterOffset() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.isExternal()) {
                return this.fCurrentEntity.baseCharOffset + (this.fCurrentEntity.position - this.fCurrentEntity.startPosition);
            }
            return this.fCurrentEntity.getCharacterOffset();
        }
        return -1;
    }

    public final String getEncoding() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.isExternal()) {
                return this.fCurrentEntity.encoding;
            }
            return this.fCurrentEntity.getEncoding();
        }
        return null;
    }

    public final String getXMLVersion() {
        if (this.fCurrentEntity != null) {
            if (this.fCurrentEntity.isExternal()) {
                return this.fCurrentEntity.xmlVersion;
            }
            return this.fCurrentEntity.getXMLVersion();
        }
        return null;
    }

    public final void setCurrentEntity(XMLEntityManager.ScannedEntity scannedEntity) {
        this.fCurrentEntity = scannedEntity;
    }

    public final void setBufferSize(int n2) {
        this.fBufferSize = n2;
    }

    public final void reset(SymbolTable symbolTable, XMLEntityManager xMLEntityManager, XMLErrorReporter xMLErrorReporter) {
        this.fCurrentEntity = null;
        this.fSymbolTable = symbolTable;
        this.fEntityManager = xMLEntityManager;
        this.fErrorReporter = xMLErrorReporter;
    }

    final boolean load(int n2, boolean bl) throws IOException {
        this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
        int n3 = this.fCurrentEntity.ch.length - n2;
        if (!this.fCurrentEntity.mayReadChunks && n3 > 64) {
            n3 = 64;
        }
        int n4 = this.fCurrentEntity.reader.read(this.fCurrentEntity.ch, n2, n3);
        boolean bl2 = false;
        if (n4 != -1) {
            if (n4 != 0) {
                this.fCurrentEntity.count = n4 + n2;
                this.fCurrentEntity.position = n2;
                this.fCurrentEntity.startPosition = n2;
            }
        } else {
            this.fCurrentEntity.count = n2;
            this.fCurrentEntity.position = n2;
            this.fCurrentEntity.startPosition = n2;
            bl2 = true;
            if (bl) {
                this.fEntityManager.endEntity();
                if (this.fCurrentEntity == null) {
                    throw END_OF_DOCUMENT_ENTITY;
                }
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    this.load(0, true);
                }
            }
        }
        return bl2;
    }

}

