/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLString;

public class XML11EntityScanner
extends XMLEntityScanner {
    public int peekChar() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        int n2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (this.fCurrentEntity.isExternal()) {
            return n2 != 13 && n2 != 133 && n2 != 8232 ? n2 : 10;
        }
        return n2;
    }

    public int scanChar() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        int n2 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
        boolean bl = false;
        if (n2 == 10 || (n2 == 13 || n2 == 133 || n2 == 8232) && (bl = this.fCurrentEntity.isExternal())) {
            char c2;
            ++this.fCurrentEntity.lineNumber;
            this.fCurrentEntity.columnNumber = 1;
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = (char)n2;
                this.load(1, false);
            }
            if (n2 == 13 && bl && (c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) != '\n' && c2 != '\u0085') {
                --this.fCurrentEntity.position;
            }
            n2 = 10;
        }
        ++this.fCurrentEntity.columnNumber;
        return n2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public String scanNmtoken() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        var1_1 = this.fCurrentEntity.position;
        do lbl-1000: // 5 sources:
        {
            if (!XML11Char.isXML11Name(var2_2 = this.fCurrentEntity.ch[this.fCurrentEntity.position])) ** GOTO lbl17
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl-1000
            var3_3 = this.fCurrentEntity.position - var1_1;
            if (var3_3 == this.fCurrentEntity.ch.length) {
                var4_5 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var1_1, var4_5, 0, var3_3);
                this.fCurrentEntity.ch = var4_5;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var3_3);
            }
            var1_1 = 0;
            if (!this.load(var3_3, false)) ** GOTO lbl-1000
            break;
lbl17: // 1 sources:
            if (!XML11Char.isXML11NameHighSurrogate(var2_2)) break;
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                var3_3 = this.fCurrentEntity.position - var1_1;
                if (var3_3 == this.fCurrentEntity.ch.length) {
                    var4_5 = new char[this.fCurrentEntity.ch.length << 1];
                    System.arraycopy(this.fCurrentEntity.ch, var1_1, var4_5, 0, var3_3);
                    this.fCurrentEntity.ch = var4_5;
                } else {
                    System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var3_3);
                }
                var1_1 = 0;
                if (this.load(var3_3, false)) {
                    --this.fCurrentEntity.startPosition;
                    --this.fCurrentEntity.position;
                    break;
                }
            }
            if (!XMLChar.isLowSurrogate(var3_3 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) || !XML11Char.isXML11Name(XMLChar.supplemental(var2_2, var3_3))) {
                --this.fCurrentEntity.position;
                break;
            }
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl-1000
            var4_6 = this.fCurrentEntity.position - var1_1;
            if (var4_6 == this.fCurrentEntity.ch.length) {
                var5_7 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var1_1, var5_7, 0, var4_6);
                this.fCurrentEntity.ch = var5_7;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var4_6);
            }
            var1_1 = 0;
        } while (!this.load(var4_6, false));
        var2_2 = this.fCurrentEntity.position - var1_1;
        this.fCurrentEntity.columnNumber += var2_2;
        var3_4 = null;
        if (var2_2 <= '\u0000') return var3_4;
        return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var1_1, var2_2);
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
        if (XML11Char.isXML11NameStart(var2_2 = this.fCurrentEntity.ch[var1_1 = this.fCurrentEntity.position++])) {
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var2_2;
                var1_1 = 0;
                if (this.load(1, false)) {
                    ++this.fCurrentEntity.columnNumber;
                    return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
                }
            }
        } else {
            if (XML11Char.isXML11NameHighSurrogate(var2_2) == false) return null;
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var2_2;
                var1_1 = 0;
                if (this.load(1, false)) {
                    --this.fCurrentEntity.position;
                    --this.fCurrentEntity.startPosition;
                    return null;
                }
            }
            if (!XMLChar.isLowSurrogate(var3_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) || !XML11Char.isXML11NameStart(XMLChar.supplemental(var2_2, var3_4))) {
                --this.fCurrentEntity.position;
                return null;
            }
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var2_2;
                this.fCurrentEntity.ch[1] = var3_4;
                var1_1 = 0;
                if (this.load(2, false)) {
                    this.fCurrentEntity.columnNumber += 2;
                    return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 2);
                }
            }
        }
        do lbl-1000: // 5 sources:
        {
            if (!XML11Char.isXML11Name(var2_2 = this.fCurrentEntity.ch[this.fCurrentEntity.position])) ** GOTO lbl42
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl-1000
            var3_4 = this.fCurrentEntity.position - var1_1;
            if (var3_4 == this.fCurrentEntity.ch.length) {
                var4_5 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var1_1, var4_5, 0, var3_4);
                this.fCurrentEntity.ch = var4_5;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var3_4);
            }
            var1_1 = 0;
            if (!this.load(var3_4, false)) ** GOTO lbl-1000
            break;
lbl42: // 1 sources:
            if (!XML11Char.isXML11NameHighSurrogate(var2_2)) break;
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                var3_4 = this.fCurrentEntity.position - var1_1;
                if (var3_4 == this.fCurrentEntity.ch.length) {
                    var4_5 = new char[this.fCurrentEntity.ch.length << 1];
                    System.arraycopy(this.fCurrentEntity.ch, var1_1, var4_5, 0, var3_4);
                    this.fCurrentEntity.ch = var4_5;
                } else {
                    System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var3_4);
                }
                var1_1 = 0;
                if (this.load(var3_4, false)) {
                    --this.fCurrentEntity.position;
                    --this.fCurrentEntity.startPosition;
                    break;
                }
            }
            if (!XMLChar.isLowSurrogate(var3_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) || !XML11Char.isXML11Name(XMLChar.supplemental(var2_2, var3_4))) {
                --this.fCurrentEntity.position;
                break;
            }
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl-1000
            var4_6 = this.fCurrentEntity.position - var1_1;
            if (var4_6 == this.fCurrentEntity.ch.length) {
                var5_7 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var1_1, var5_7, 0, var4_6);
                this.fCurrentEntity.ch = var5_7;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var4_6);
            }
            var1_1 = 0;
        } while (!this.load(var4_6, false));
        var3_4 = this.fCurrentEntity.position - var1_1;
        this.fCurrentEntity.columnNumber += var3_4;
        var4_5 = null;
        if (var3_4 <= '\u0000') return var4_5;
        return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var1_1, var3_4);
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
        if (XML11Char.isXML11NCNameStart(var2_2 = this.fCurrentEntity.ch[var1_1 = this.fCurrentEntity.position++])) {
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var2_2;
                var1_1 = 0;
                if (this.load(1, false)) {
                    ++this.fCurrentEntity.columnNumber;
                    return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
                }
            }
        } else {
            if (XML11Char.isXML11NameHighSurrogate(var2_2) == false) return null;
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var2_2;
                var1_1 = 0;
                if (this.load(1, false)) {
                    --this.fCurrentEntity.position;
                    --this.fCurrentEntity.startPosition;
                    return null;
                }
            }
            if (!XMLChar.isLowSurrogate(var3_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) || !XML11Char.isXML11NCNameStart(XMLChar.supplemental(var2_2, var3_4))) {
                --this.fCurrentEntity.position;
                return null;
            }
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var2_2;
                this.fCurrentEntity.ch[1] = var3_4;
                var1_1 = 0;
                if (this.load(2, false)) {
                    this.fCurrentEntity.columnNumber += 2;
                    return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 2);
                }
            }
        }
        do lbl-1000: // 5 sources:
        {
            if (!XML11Char.isXML11NCName(var2_2 = this.fCurrentEntity.ch[this.fCurrentEntity.position])) ** GOTO lbl42
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl-1000
            var3_4 = this.fCurrentEntity.position - var1_1;
            if (var3_4 == this.fCurrentEntity.ch.length) {
                var4_5 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var1_1, var4_5, 0, var3_4);
                this.fCurrentEntity.ch = var4_5;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var3_4);
            }
            var1_1 = 0;
            if (!this.load(var3_4, false)) ** GOTO lbl-1000
            break;
lbl42: // 1 sources:
            if (!XML11Char.isXML11NameHighSurrogate(var2_2)) break;
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                var3_4 = this.fCurrentEntity.position - var1_1;
                if (var3_4 == this.fCurrentEntity.ch.length) {
                    var4_5 = new char[this.fCurrentEntity.ch.length << 1];
                    System.arraycopy(this.fCurrentEntity.ch, var1_1, var4_5, 0, var3_4);
                    this.fCurrentEntity.ch = var4_5;
                } else {
                    System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var3_4);
                }
                var1_1 = 0;
                if (this.load(var3_4, false)) {
                    --this.fCurrentEntity.startPosition;
                    --this.fCurrentEntity.position;
                    break;
                }
            }
            if (!XMLChar.isLowSurrogate(var3_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) || !XML11Char.isXML11NCName(XMLChar.supplemental(var2_2, var3_4))) {
                --this.fCurrentEntity.position;
                break;
            }
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl-1000
            var4_6 = this.fCurrentEntity.position - var1_1;
            if (var4_6 == this.fCurrentEntity.ch.length) {
                var5_7 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var1_1, var5_7, 0, var4_6);
                this.fCurrentEntity.ch = var5_7;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var1_1, this.fCurrentEntity.ch, 0, var4_6);
            }
            var1_1 = 0;
        } while (!this.load(var4_6, false));
        var3_4 = this.fCurrentEntity.position - var1_1;
        this.fCurrentEntity.columnNumber += var3_4;
        var4_5 = null;
        if (var3_4 <= '\u0000') return var4_5;
        return this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var1_1, var3_4);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean scanQName(QName var1_1) throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        if (XML11Char.isXML11NCNameStart(var3_3 = this.fCurrentEntity.ch[var2_2 = this.fCurrentEntity.position++])) {
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var3_3;
                var2_2 = 0;
                if (this.load(1, false)) {
                    ++this.fCurrentEntity.columnNumber;
                    var4_4 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
                    var1_1.setValues(null, var4_4, var4_4, null);
                    return true;
                }
            }
        } else {
            if (XML11Char.isXML11NameHighSurrogate(var3_3) == false) return false;
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var3_3;
                var2_2 = 0;
                if (this.load(1, false)) {
                    --this.fCurrentEntity.startPosition;
                    --this.fCurrentEntity.position;
                    return false;
                }
            }
            if (!XMLChar.isLowSurrogate(var4_5 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) || !XML11Char.isXML11NCNameStart(XMLChar.supplemental(var3_3, (char)var4_5))) {
                --this.fCurrentEntity.position;
                return false;
            }
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = var3_3;
                this.fCurrentEntity.ch[1] = var4_5;
                var2_2 = 0;
                if (this.load(2, false)) {
                    this.fCurrentEntity.columnNumber += 2;
                    var5_6 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 2);
                    var1_1.setValues(null, var5_6, var5_6, null);
                    return true;
                }
            }
        }
        var4_5 = -1;
        var5_7 = false;
        do lbl-1000: // 5 sources:
        {
            if (!XML11Char.isXML11Name(var3_3 = this.fCurrentEntity.ch[this.fCurrentEntity.position])) ** GOTO lbl53
            if (var3_3 == ':') {
                if (var4_5 != -1) break;
                var4_5 = this.fCurrentEntity.position;
            }
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl-1000
            var6_8 = this.fCurrentEntity.position - var2_2;
            if (var6_8 == this.fCurrentEntity.ch.length) {
                var7_9 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var2_2, var7_9, 0, var6_8);
                this.fCurrentEntity.ch = var7_9;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var2_2, this.fCurrentEntity.ch, 0, var6_8);
            }
            if (var4_5 != -1) {
                var4_5 -= var2_2;
            }
            var2_2 = 0;
            if (!this.load(var6_8, false)) ** GOTO lbl-1000
            break;
lbl53: // 1 sources:
            if (!XML11Char.isXML11NameHighSurrogate(var3_3)) break;
            if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
                var6_8 = this.fCurrentEntity.position - var2_2;
                if (var6_8 == this.fCurrentEntity.ch.length) {
                    var7_9 = new char[this.fCurrentEntity.ch.length << 1];
                    System.arraycopy(this.fCurrentEntity.ch, var2_2, var7_9, 0, var6_8);
                    this.fCurrentEntity.ch = var7_9;
                } else {
                    System.arraycopy(this.fCurrentEntity.ch, var2_2, this.fCurrentEntity.ch, 0, var6_8);
                }
                if (var4_5 != -1) {
                    var4_5 -= var2_2;
                }
                var2_2 = 0;
                if (this.load(var6_8, false)) {
                    var5_7 = true;
                    --this.fCurrentEntity.startPosition;
                    --this.fCurrentEntity.position;
                    break;
                }
            }
            if (!XMLChar.isLowSurrogate(var6_8 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) || !XML11Char.isXML11Name(XMLChar.supplemental(var3_3, var6_8))) {
                var5_7 = true;
                --this.fCurrentEntity.position;
                break;
            }
            if (++this.fCurrentEntity.position != this.fCurrentEntity.count) ** GOTO lbl-1000
            var7_10 = this.fCurrentEntity.position - var2_2;
            if (var7_10 == this.fCurrentEntity.ch.length) {
                var8_11 = new char[this.fCurrentEntity.ch.length << 1];
                System.arraycopy(this.fCurrentEntity.ch, var2_2, var8_11, 0, var7_10);
                this.fCurrentEntity.ch = var8_11;
            } else {
                System.arraycopy(this.fCurrentEntity.ch, var2_2, this.fCurrentEntity.ch, 0, var7_10);
            }
            if (var4_5 != -1) {
                var4_5 -= var2_2;
            }
            var2_2 = 0;
        } while (!this.load(var7_10, false));
        var6_8 = this.fCurrentEntity.position - var2_2;
        this.fCurrentEntity.columnNumber += var6_8;
        if (var6_8 <= '\u0000') return false;
        var7_9 = null;
        var8_11 = null;
        var9_12 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var2_2, var6_8);
        if (var4_5 != -1) {
            var10_13 = var4_5 - var2_2;
            var7_9 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var2_2, var10_13);
            var11_14 = var6_8 - var10_13 - 1;
            var12_15 = var4_5 + 1;
            if (!(XML11Char.isXML11NCNameStart(this.fCurrentEntity.ch[var12_15]) || XML11Char.isXML11NameHighSurrogate(this.fCurrentEntity.ch[var12_15]) && !var5_7)) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IllegalQName", null, 2);
            }
            var8_11 = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, var4_5 + 1, var11_14);
        } else {
            var8_11 = var9_12;
        }
        var1_1.setValues((String)var7_9, (String)var8_11, (String)var9_12, null);
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public int scanContent(XMLString var1_1) throws IOException {
        block15 : {
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
            if (var3_3 == 10 || (var3_3 == 13 || var3_3 == 133 || var3_3 == 8232) && var5_5) {
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
                        if ((var6_6 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) == 10 || var6_6 == 133) {
                            ++this.fCurrentEntity.position;
                            ++var2_2;
                            continue;
                        }
                        ++var4_4;
                        continue;
                    }
                    if (var3_3 == 10 || (var3_3 == 133 || var3_3 == 8232) && var5_5) {
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
                if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                    var1_1.setValues(this.fCurrentEntity.ch, var2_2, var7_7);
                    return -1;
                }
            }
            if (!var5_5) ** GOTO lbl63
            while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
                if (XML11Char.isXML11Content(var3_3 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) && var3_3 != 133 && var3_3 != 8232) continue;
                --this.fCurrentEntity.position;
                break block15;
            }
            ** GOTO lbl64
lbl-1000: // 1 sources:
            {
                if (XML11Char.isXML11InternalEntityContent(var3_3 = this.fCurrentEntity.ch[this.fCurrentEntity.position++])) continue;
                --this.fCurrentEntity.position;
                break;
lbl63: // 2 sources:
                ** while (this.fCurrentEntity.position < this.fCurrentEntity.count)
            }
        }
        var6_6 = this.fCurrentEntity.position - var2_2;
        this.fCurrentEntity.columnNumber += var6_6 - var4_4;
        var1_1.setValues(this.fCurrentEntity.ch, var2_2, var6_6);
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            return -1;
        }
        var3_3 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (var3_3 != 13 && var3_3 != 133) {
            if (var3_3 != 8232) return var3_3;
        }
        if (var5_5 == false) return var3_3;
        return 10;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public int scanLiteral(int var1_1, XMLString var2_2) throws IOException {
        block13 : {
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.load(0, true);
            } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[this.fCurrentEntity.count - 1];
                this.load(1, false);
                this.fCurrentEntity.startPosition = 0;
                this.fCurrentEntity.position = 0;
            }
            var3_3 = this.fCurrentEntity.position;
            var4_4 = this.fCurrentEntity.ch[var3_3];
            var5_5 = 0;
            var6_6 = this.fCurrentEntity.isExternal();
            if (var4_4 == 10 || (var4_4 == 13 || var4_4 == 133 || var4_4 == 8232) && var6_6) {
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
                        if ((var7_7 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) == 10 || var7_7 == 133) {
                            ++this.fCurrentEntity.position;
                            ++var3_3;
                            continue;
                        }
                        ++var5_5;
                        continue;
                    }
                    if (var4_4 == 10 || (var4_4 == 133 || var4_4 == 8232) && var6_6) {
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
                if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                    var2_2.setValues(this.fCurrentEntity.ch, var3_3, var8_8);
                    return -1;
                }
            }
            if (!var6_6) ** GOTO lbl63
            while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
                if ((var4_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) != var1_1 && var4_4 != 37 && XML11Char.isXML11Content(var4_4) && var4_4 != 133 && var4_4 != 8232) continue;
                --this.fCurrentEntity.position;
                break block13;
            }
            ** GOTO lbl64
lbl-1000: // 1 sources:
            {
                if (((var4_4 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) != var1_1 || this.fCurrentEntity.literal) && var4_4 != 37 && XML11Char.isXML11InternalEntityContent(var4_4)) continue;
                --this.fCurrentEntity.position;
                break;
lbl63: // 2 sources:
                ** while (this.fCurrentEntity.position < this.fCurrentEntity.count)
            }
        }
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
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean scanData(String var1_1, XMLStringBuffer var2_2) throws IOException {
        var3_3 = false;
        var4_4 = var1_1.length();
        var5_5 = var1_1.charAt(0);
        var6_6 = this.fCurrentEntity.isExternal();
        do {
            block23 : {
                if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                    this.load(0, true);
                }
                var7_7 = false;
                while (this.fCurrentEntity.position >= this.fCurrentEntity.count - var4_4 && !var7_7) {
                    System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.position, this.fCurrentEntity.ch, 0, this.fCurrentEntity.count - this.fCurrentEntity.position);
                    var7_7 = this.load(this.fCurrentEntity.count - this.fCurrentEntity.position, false);
                    this.fCurrentEntity.position = 0;
                    this.fCurrentEntity.startPosition = 0;
                }
                if (this.fCurrentEntity.position >= this.fCurrentEntity.count - var4_4) {
                    var8_8 = this.fCurrentEntity.count - this.fCurrentEntity.position;
                    var2_2.append(this.fCurrentEntity.ch, this.fCurrentEntity.position, var8_8);
                    this.fCurrentEntity.columnNumber += this.fCurrentEntity.count;
                    this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                    this.fCurrentEntity.position = this.fCurrentEntity.count;
                    this.fCurrentEntity.startPosition = this.fCurrentEntity.count;
                    this.load(0, true);
                    return false;
                }
                var8_8 = this.fCurrentEntity.position;
                var9_9 = this.fCurrentEntity.ch[var8_8];
                var10_10 = 0;
                if (var9_9 == '\n' || (var9_9 == '\r' || var9_9 == '\u0085' || var9_9 == '\u2028') && var6_6) {
                    do {
                        if ((var9_9 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) == '\r' && var6_6) {
                            ++var10_10;
                            ++this.fCurrentEntity.lineNumber;
                            this.fCurrentEntity.columnNumber = 1;
                            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                                var8_8 = 0;
                                this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                                this.fCurrentEntity.position = var10_10;
                                this.fCurrentEntity.startPosition = var10_10;
                                if (this.load(var10_10, false)) break;
                            }
                            if ((var11_11 = this.fCurrentEntity.ch[this.fCurrentEntity.position]) == 10 || var11_11 == 133) {
                                ++this.fCurrentEntity.position;
                                ++var8_8;
                                continue;
                            }
                            ++var10_10;
                            continue;
                        }
                        if (var9_9 == '\n' || (var9_9 == '\u0085' || var9_9 == '\u2028') && var6_6) {
                            ++var10_10;
                            ++this.fCurrentEntity.lineNumber;
                            this.fCurrentEntity.columnNumber = 1;
                            if (this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
                            var8_8 = 0;
                            this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
                            this.fCurrentEntity.position = var10_10;
                            this.fCurrentEntity.startPosition = var10_10;
                            this.fCurrentEntity.count = var10_10;
                            if (!this.load(var10_10, false)) continue;
                            break;
                        }
                        --this.fCurrentEntity.position;
                        break;
                    } while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
                    var11_11 = var8_8;
                    while (var11_11 < this.fCurrentEntity.position) {
                        this.fCurrentEntity.ch[var11_11] = 10;
                        ++var11_11;
                    }
                    var12_12 = this.fCurrentEntity.position - var8_8;
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                        var2_2.append(this.fCurrentEntity.ch, var8_8, var12_12);
                        return true;
                    }
                }
                if (!var6_6) ** GOTO lbl122
                while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
                    if ((var9_9 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) != var5_5) ** GOTO lbl75
                    var11_11 = this.fCurrentEntity.position - 1;
                    var12_12 = 1;
                    ** GOTO lbl92
lbl75: // 1 sources:
                    if (var9_9 == '\n' || var9_9 == '\r' || var9_9 == '\u0085' || var9_9 == '\u2028') {
                        --this.fCurrentEntity.position;
                        break block23;
                    }
                    if (XML11Char.isXML11ValidLiteral(var9_9)) continue;
                    --this.fCurrentEntity.position;
                    var11_11 = this.fCurrentEntity.position - var8_8;
                    this.fCurrentEntity.columnNumber += var11_11 - var10_10;
                    var2_2.append(this.fCurrentEntity.ch, var8_8, var11_11);
                    return true;
lbl-1000: // 1 sources:
                    {
                        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                            this.fCurrentEntity.position -= var12_12;
                            break block23;
                        }
                        var9_9 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
                        if (var1_1.charAt(var12_12) != var9_9) {
                            --this.fCurrentEntity.position;
                            break;
                        }
                        ++var12_12;
lbl92: // 2 sources:
                        ** while (var12_12 < var4_4)
                    }
lbl93: // 2 sources:
                    if (this.fCurrentEntity.position != var11_11 + var4_4) continue;
                    var3_3 = true;
                    break block23;
                }
                ** GOTO lbl123
lbl-1000: // 1 sources:
                {
                    if ((var9_9 = this.fCurrentEntity.ch[this.fCurrentEntity.position++]) != var5_5) ** GOTO lbl101
                    var11_11 = this.fCurrentEntity.position - 1;
                    var12_12 = 1;
                    ** GOTO lbl118
lbl101: // 1 sources:
                    if (var9_9 == '\n') {
                        --this.fCurrentEntity.position;
                        break;
                    }
                    if (XML11Char.isXML11Valid(var9_9)) continue;
                    --this.fCurrentEntity.position;
                    var11_11 = this.fCurrentEntity.position - var8_8;
                    this.fCurrentEntity.columnNumber += var11_11 - var10_10;
                    var2_2.append(this.fCurrentEntity.ch, var8_8, var11_11);
                    return true;
lbl-1000: // 1 sources:
                    {
                        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                            this.fCurrentEntity.position -= var12_12;
                            break block6;
                        }
                        var9_9 = this.fCurrentEntity.ch[this.fCurrentEntity.position++];
                        if (var1_1.charAt(var12_12) != var9_9) {
                            --this.fCurrentEntity.position;
                            break;
                        }
                        ++var12_12;
lbl118: // 2 sources:
                        ** while (var12_12 < var4_4)
                    }
lbl119: // 2 sources:
                    if (this.fCurrentEntity.position != var11_11 + var4_4) continue;
                    var3_3 = true;
                    break;
lbl122: // 3 sources:
                    ** while (this.fCurrentEntity.position < this.fCurrentEntity.count)
                }
            }
            var11_11 = this.fCurrentEntity.position - var8_8;
            this.fCurrentEntity.columnNumber += var11_11 - var10_10;
            if (var3_3) {
                var11_11 -= var4_4;
            }
            var2_2.append(this.fCurrentEntity.ch, var8_8, var11_11);
        } while (!var3_3);
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
        if (n2 == 10 && (c2 == '\u2028' || c2 == '\u0085') && this.fCurrentEntity.isExternal()) {
            ++this.fCurrentEntity.position;
            ++this.fCurrentEntity.lineNumber;
            this.fCurrentEntity.columnNumber = 1;
            return true;
        }
        if (n2 == 10 && c2 == '\r' && this.fCurrentEntity.isExternal()) {
            char c3;
            if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
                this.fCurrentEntity.ch[0] = c2;
                this.load(1, false);
            }
            if ((c3 = this.fCurrentEntity.ch[++this.fCurrentEntity.position]) == '\n' || c3 == '\u0085') {
                ++this.fCurrentEntity.position;
            }
            ++this.fCurrentEntity.lineNumber;
            this.fCurrentEntity.columnNumber = 1;
            return true;
        }
        return false;
    }

    public boolean skipSpaces() throws IOException {
        if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
            this.load(0, true);
        }
        char c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
        if (this.fCurrentEntity.isExternal()) {
            if (XML11Char.isXML11Space(c2)) {
                do {
                    boolean bl = false;
                    if (c2 == '\n' || c2 == '\r' || c2 == '\u0085' || c2 == '\u2028') {
                        char c3;
                        ++this.fCurrentEntity.lineNumber;
                        this.fCurrentEntity.columnNumber = 1;
                        if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                            this.fCurrentEntity.ch[0] = c2;
                            bl = this.load(1, true);
                            if (!bl) {
                                this.fCurrentEntity.startPosition = 0;
                                this.fCurrentEntity.position = 0;
                            }
                        }
                        if (c2 == '\r' && (c3 = this.fCurrentEntity.ch[++this.fCurrentEntity.position]) != '\n' && c3 != '\u0085') {
                            --this.fCurrentEntity.position;
                        }
                    } else {
                        ++this.fCurrentEntity.columnNumber;
                    }
                    if (!bl) {
                        ++this.fCurrentEntity.position;
                    }
                    if (this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
                    this.load(0, true);
                } while (XML11Char.isXML11Space(c2 = this.fCurrentEntity.ch[this.fCurrentEntity.position]));
                return true;
            }
        } else if (XMLChar.isSpace(c2)) {
            do {
                boolean bl = false;
                if (c2 == '\n') {
                    ++this.fCurrentEntity.lineNumber;
                    this.fCurrentEntity.columnNumber = 1;
                    if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
                        this.fCurrentEntity.ch[0] = c2;
                        bl = this.load(1, true);
                        if (!bl) {
                            this.fCurrentEntity.startPosition = 0;
                            this.fCurrentEntity.position = 0;
                        }
                    }
                } else {
                    ++this.fCurrentEntity.columnNumber;
                }
                if (!bl) {
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
}

