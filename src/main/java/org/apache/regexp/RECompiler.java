/*
 * Decompiled with CFR 0_119.
 */
package org.apache.regexp;

import java.util.Hashtable;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;

public class RECompiler {
    char[] instruction = new char[128];
    int lenInstruction = 0;
    String pattern;
    int len;
    int idx;
    int parens;
    static int brackets = 0;
    static int[] bracketStart = null;
    static int[] bracketEnd = null;
    static int[] bracketMin = null;
    static int[] bracketOpt = null;
    static Hashtable hashPOSIX = new Hashtable();

    static {
        hashPOSIX.put("alnum", new Character('w'));
        hashPOSIX.put("alpha", new Character('a'));
        hashPOSIX.put("blank", new Character('b'));
        hashPOSIX.put("cntrl", new Character('c'));
        hashPOSIX.put("digit", new Character('d'));
        hashPOSIX.put("graph", new Character('g'));
        hashPOSIX.put("lower", new Character('l'));
        hashPOSIX.put("print", new Character('p'));
        hashPOSIX.put("punct", new Character('!'));
        hashPOSIX.put("space", new Character('s'));
        hashPOSIX.put("upper", new Character('u'));
        hashPOSIX.put("xdigit", new Character('x'));
        hashPOSIX.put("javastart", new Character('j'));
        hashPOSIX.put("javapart", new Character('k'));
    }

    void allocBrackets() {
        if (bracketStart == null) {
            bracketStart = new int[10];
            bracketEnd = new int[10];
            bracketMin = new int[10];
            bracketOpt = new int[10];
            int n2 = 0;
            while (n2 < 10) {
                RECompiler.bracketOpt[n2] = -1;
                RECompiler.bracketMin[n2] = -1;
                RECompiler.bracketEnd[n2] = -1;
                RECompiler.bracketStart[n2] = -1;
                ++n2;
            }
        }
    }

    /*
     * Exception decompiling
     */
    int atom() throws RESyntaxException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:374)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:452)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2877)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:825)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    void bracket() throws RESyntaxException {
        if (this.idx >= this.len || this.pattern.charAt(this.idx++) != '{') {
            this.internalError();
        }
        if (this.idx >= this.len || !Character.isDigit(this.pattern.charAt(this.idx))) {
            this.syntaxError("Expected digit");
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (this.idx < this.len && Character.isDigit(this.pattern.charAt(this.idx))) {
            stringBuffer.append(this.pattern.charAt(this.idx++));
        }
        try {
            RECompiler.bracketMin[RECompiler.brackets] = Integer.parseInt(stringBuffer.toString());
        }
        catch (NumberFormatException numberFormatException) {
            this.syntaxError("Expected valid number");
        }
        if (this.idx >= this.len) {
            this.syntaxError("Expected comma or right bracket");
        }
        if (this.pattern.charAt(this.idx) == '}') {
            ++this.idx;
            RECompiler.bracketOpt[RECompiler.brackets] = 0;
            return;
        }
        if (this.idx >= this.len || this.pattern.charAt(this.idx++) != ',') {
            this.syntaxError("Expected comma");
        }
        if (this.idx >= this.len) {
            this.syntaxError("Expected comma or right bracket");
        }
        if (this.pattern.charAt(this.idx) == '}') {
            ++this.idx;
            RECompiler.bracketOpt[RECompiler.brackets] = -1;
            return;
        }
        if (this.idx >= this.len || !Character.isDigit(this.pattern.charAt(this.idx))) {
            this.syntaxError("Expected digit");
        }
        stringBuffer.setLength(0);
        while (this.idx < this.len && Character.isDigit(this.pattern.charAt(this.idx))) {
            stringBuffer.append(this.pattern.charAt(this.idx++));
        }
        try {
            RECompiler.bracketOpt[RECompiler.brackets] = Integer.parseInt(stringBuffer.toString()) - bracketMin[brackets];
        }
        catch (NumberFormatException numberFormatException) {
            this.syntaxError("Expected valid number");
        }
        if (bracketOpt[brackets] <= 0) {
            this.syntaxError("Bad range");
        }
        if (this.idx >= this.len || this.pattern.charAt(this.idx++) != '}') {
            this.syntaxError("Missing close brace");
        }
    }

    int branch(int[] arrn) throws RESyntaxException {
        int n2 = this.node('|', 0);
        int n3 = -1;
        int[] arrn2 = new int[1];
        boolean bl = true;
        while (this.idx < this.len && this.pattern.charAt(this.idx) != '|' && this.pattern.charAt(this.idx) != ')') {
            arrn2[0] = 0;
            int n4 = this.closure(arrn2);
            if (arrn2[0] == 0) {
                bl = false;
            }
            if (n3 != -1) {
                this.setNextOfEnd(n3, n4);
            }
            n3 = n4;
        }
        if (n3 == -1) {
            this.node('N', 0);
        }
        if (bl) {
            int[] arrn3 = arrn;
            arrn3[0] = arrn3[0] | 1;
        }
        return n2;
    }

    int characterClass() throws RESyntaxException {
        int n2;
        int n3;
        int n4;
        if (this.pattern.charAt(this.idx) != '[') {
            this.internalError();
        }
        if (this.idx + 1 >= this.len || this.pattern.charAt(++this.idx) == ']') {
            this.syntaxError("Empty or unterminated class");
        }
        if (this.idx < this.len && this.pattern.charAt(this.idx) == ':') {
            ++this.idx;
            n2 = this.idx;
            while (this.idx < this.len && this.pattern.charAt(this.idx) >= 'a' && this.pattern.charAt(this.idx) <= 'z') {
                ++this.idx;
            }
            if (this.idx + 1 < this.len && this.pattern.charAt(this.idx) == ':' && this.pattern.charAt(this.idx + 1) == ']') {
                String string = this.pattern.substring(n2, this.idx);
                Character c2 = (Character)hashPOSIX.get(string);
                if (c2 != null) {
                    this.idx += 2;
                    return this.node('P', c2.charValue());
                }
                this.syntaxError("Invalid POSIX character class '" + string + "'");
            }
            this.syntaxError("Invalid POSIX character class syntax");
        }
        n2 = this.node('[', 0);
        int n5 = n4 = 65535;
        int n6 = 0;
        boolean bl = true;
        boolean bl2 = false;
        int n7 = this.idx;
        int n8 = 0;
        RERange rERange = new RERange(this);
        block16 : while (this.idx < this.len && this.pattern.charAt(this.idx) != ']') {
            switch (this.pattern.charAt(this.idx)) {
                case '^': {
                    bl ^= true;
                    if (this.idx == n7) {
                        rERange.include(0, 65535, true);
                    }
                    ++this.idx;
                    continue block16;
                }
                case '\\': {
                    n3 = this.escape();
                    switch (n3) {
                        case 65534: 
                        case 65535: {
                            this.syntaxError("Bad character class");
                        }
                        case 65533: {
                            if (bl2) {
                                this.syntaxError("Bad character class");
                            }
                            switch (this.pattern.charAt(this.idx - 1)) {
                                case 'D': 
                                case 'S': 
                                case 'W': {
                                    this.syntaxError("Bad character class");
                                }
                                case 's': {
                                    rERange.include('\t', bl);
                                    rERange.include('\r', bl);
                                    rERange.include('\f', bl);
                                    rERange.include('\n', bl);
                                    rERange.include('\b', bl);
                                    rERange.include(' ', bl);
                                    break;
                                }
                                case 'w': {
                                    rERange.include(97, 122, bl);
                                    rERange.include(65, 90, bl);
                                    rERange.include('_', bl);
                                }
                                case 'd': {
                                    rERange.include(48, 57, bl);
                                    break;
                                }
                            }
                            n5 = n4;
                            continue block16;
                        }
                    }
                    n6 = n3;
                    break;
                }
                case '-': {
                    if (bl2) {
                        this.syntaxError("Bad class range");
                    }
                    bl2 = true;
                    int n9 = n8 = n5 == n4 ? 0 : n5;
                    if (this.idx + 1 >= this.len || this.pattern.charAt(++this.idx) != ']') continue block16;
                    n6 = 65535;
                    break;
                }
                default: {
                    n6 = this.pattern.charAt(this.idx++);
                }
            }
            if (bl2) {
                int n10 = n6;
                if (n8 >= n10) {
                    this.syntaxError("Bad character class");
                }
                rERange.include(n8, n10, bl);
                n5 = n4;
                bl2 = false;
                continue;
            }
            if (this.idx + 1 >= this.len || this.pattern.charAt(this.idx + 1) != '-') {
                rERange.include((char)n6, bl);
            }
            n5 = n6;
        }
        if (this.idx == this.len) {
            this.syntaxError("Unterminated character class");
        }
        ++this.idx;
        this.instruction[n2 + 1] = (char)rERange.num;
        n3 = 0;
        while (n3 < rERange.num) {
            this.emit((char)rERange.minRange[n3]);
            this.emit((char)rERange.maxRange[n3]);
            ++n3;
        }
        return n2;
    }

    /*
     * Exception decompiling
     */
    int closure(int[] var1_1) throws RESyntaxException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:486)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    public REProgram compile(String string) throws RESyntaxException {
        this.pattern = string;
        this.len = string.length();
        this.idx = 0;
        this.lenInstruction = 0;
        this.parens = 1;
        brackets = 0;
        int[] arrn = new int[]{2};
        this.expr(arrn);
        if (this.idx != this.len) {
            if (string.charAt(this.idx) == ')') {
                this.syntaxError("Unmatched close paren");
            }
            this.syntaxError("Unexpected input remains");
        }
        char[] arrc = new char[this.lenInstruction];
        System.arraycopy(this.instruction, 0, arrc, 0, this.lenInstruction);
        return new REProgram(arrc);
    }

    void emit(char c2) {
        this.ensure(1);
        this.instruction[this.lenInstruction++] = c2;
    }

    void ensure(int n2) {
        int n3 = this.instruction.length;
        if (this.lenInstruction + n2 >= n3) {
            while (this.lenInstruction + n2 >= n3) {
                n3 *= 2;
            }
            char[] arrc = new char[n3];
            System.arraycopy(this.instruction, 0, arrc, 0, this.lenInstruction);
            this.instruction = arrc;
        }
    }

    char escape() throws RESyntaxException {
        if (this.pattern.charAt(this.idx) != '\\') {
            this.internalError();
        }
        if (this.idx + 1 == this.len) {
            this.syntaxError("Escape terminates string");
        }
        this.idx += 2;
        char c2 = this.pattern.charAt(this.idx - 1);
        switch (c2) {
            case 'B': 
            case 'b': {
                return '\ufffe';
            }
            case 'D': 
            case 'S': 
            case 'W': 
            case 'd': 
            case 's': 
            case 'w': {
                return '\ufffd';
            }
            case 'u': 
            case 'x': {
                int n2 = c2 == 'u' ? 4 : 2;
                int n3 = 0;
                while (this.idx < this.len && n2-- > 0) {
                    char c3 = this.pattern.charAt(this.idx);
                    if (c3 >= '0' && c3 <= '9') {
                        n3 = (n3 << 4) + c3 - 48;
                    } else if ((c3 = Character.toLowerCase(c3)) >= 'a' && c3 <= 'f') {
                        n3 = (n3 << 4) + (c3 - 97) + 10;
                    } else {
                        this.syntaxError("Expected " + n2 + " hexadecimal digits after \\" + c2);
                    }
                    ++this.idx;
                }
                return (char)n3;
            }
            case 't': {
                return '\t';
            }
            case 'n': {
                return '\n';
            }
            case 'r': {
                return '\r';
            }
            case 'f': {
                return '\f';
            }
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': {
                if (this.idx < this.len && Character.isDigit(this.pattern.charAt(this.idx)) || c2 == '0') {
                    int n4 = c2 - 48;
                    if (this.idx < this.len && Character.isDigit(this.pattern.charAt(this.idx))) {
                        n4 = (n4 << 3) + (this.pattern.charAt(this.idx++) - 48);
                        if (this.idx < this.len && Character.isDigit(this.pattern.charAt(this.idx))) {
                            n4 = (n4 << 3) + (this.pattern.charAt(this.idx++) - 48);
                        }
                    }
                    return (char)n4;
                }
                return '\uffff';
            }
        }
        return c2;
    }

    int expr(int[] arrn) throws RESyntaxException {
        int n2;
        boolean bl = false;
        int n3 = -1;
        int n4 = this.parens;
        if ((arrn[0] & 2) == 0 && this.pattern.charAt(this.idx) == '(') {
            ++this.idx;
            bl = true;
            n3 = this.node('(', this.parens++);
        }
        int[] arrn2 = arrn;
        arrn2[0] = arrn2[0] & -3;
        int n5 = this.branch(arrn);
        if (n3 == -1) {
            n3 = n5;
        } else {
            this.setNextOfEnd(n3, n5);
        }
        while (this.idx < this.len && this.pattern.charAt(this.idx) == '|') {
            ++this.idx;
            n5 = this.branch(arrn);
            this.setNextOfEnd(n3, n5);
        }
        if (bl) {
            if (this.idx < this.len && this.pattern.charAt(this.idx) == ')') {
                ++this.idx;
            } else {
                this.syntaxError("Missing close paren");
            }
            n2 = this.node(')', n4);
        } else {
            n2 = this.node('E', 0);
        }
        this.setNextOfEnd(n3, n2);
        int n6 = -1;
        int n7 = n3;
        while (n6 != 0) {
            if (this.instruction[n7] == '|') {
                this.setNextOfEnd(n7 + 3, n2);
            }
            n6 = this.instruction[n7 + 2];
            n7 += n6;
        }
        return n3;
    }

    void internalError() throws Error {
        throw new Error("Internal error!");
    }

    int node(char c2, int n2) {
        this.ensure(3);
        this.instruction[this.lenInstruction] = c2;
        this.instruction[this.lenInstruction + 1] = (char)n2;
        this.instruction[this.lenInstruction + 2] = '\u0000';
        this.lenInstruction += 3;
        return this.lenInstruction - 3;
    }

    void nodeInsert(char c2, int n2, int n3) {
        this.ensure(3);
        System.arraycopy(this.instruction, n3, this.instruction, n3 + 3, this.lenInstruction - n3);
        this.instruction[n3] = c2;
        this.instruction[n3 + 1] = (char)n2;
        this.instruction[n3 + 2] = '\u0000';
        this.lenInstruction += 3;
    }

    void setNextOfEnd(int n2, int n3) {
        char c2;
        while ((c2 = this.instruction[n2 + 2]) != '\u0000') {
            n2 += c2;
        }
        this.instruction[n2 + 2] = (char)(n3 - n2);
    }

    void syntaxError(String string) throws RESyntaxException {
        throw new RESyntaxException(string);
    }

    int terminal(int[] arrn) throws RESyntaxException {
        switch (this.pattern.charAt(this.idx)) {
            case '$': 
            case '.': 
            case '^': {
                return this.node(this.pattern.charAt(this.idx++), 0);
            }
            case '[': {
                return this.characterClass();
            }
            case '(': {
                return this.expr(arrn);
            }
            case ')': {
                this.syntaxError("Unexpected close paren");
            }
            case '|': {
                this.internalError();
            }
            case ']': {
                this.syntaxError("Mismatched class");
            }
            case '\u0000': {
                this.syntaxError("Unexpected end of input");
            }
            case '*': 
            case '+': 
            case '?': 
            case '{': {
                this.syntaxError("Missing operand to closure");
            }
            case '\\': {
                int n2 = this.idx;
                switch (this.escape()) {
                    case '\ufffd': 
                    case '\ufffe': {
                        int[] arrn2 = arrn;
                        arrn2[0] = arrn2[0] & -2;
                        return this.node('\\', this.pattern.charAt(this.idx - 1));
                    }
                    case '\uffff': {
                        char c2 = (char)(this.pattern.charAt(this.idx - 1) - 48);
                        if (this.parens <= c2) {
                            this.syntaxError("Bad backreference");
                        }
                        int[] arrn3 = arrn;
                        arrn3[0] = arrn3[0] | 1;
                        return this.node('#', c2);
                    }
                }
                this.idx = n2;
                int[] arrn4 = arrn;
                arrn4[0] = arrn4[0] & -2;
                break;
            }
        }
        int[] arrn5 = arrn;
        arrn5[0] = arrn5[0] & -2;
        return this.atom();
    }

    class RERange {
        private final RECompiler this$0;
        int size;
        int[] minRange;
        int[] maxRange;
        int num;

        RERange(RECompiler rECompiler) {
            this.this$0 = rECompiler;
            this.size = 16;
            this.minRange = new int[this.size];
            this.maxRange = new int[this.size];
            this.num = 0;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        void delete(int var1_1) {
            if (this.num == 0) return;
            if (var1_1 < this.num) ** GOTO lbl7
            return;
lbl-1000: // 1 sources:
            {
                if (var1_1 - 1 < 0) continue;
                this.minRange[var1_1 - 1] = this.minRange[var1_1];
                this.maxRange[var1_1 - 1] = this.maxRange[var1_1];
lbl7: // 3 sources:
                ** while (var1_1++ < this.num)
            }
lbl8: // 1 sources:
            --this.num;
        }

        void include(char c2, boolean bl) {
            this.include(c2, c2, bl);
        }

        void include(int n2, int n3, boolean bl) {
            if (bl) {
                this.merge(n2, n3);
            } else {
                this.remove(n2, n3);
            }
        }

        void merge(int n2, int n3) {
            int n4 = 0;
            while (n4 < this.num) {
                if (n2 >= this.minRange[n4] && n3 <= this.maxRange[n4]) {
                    return;
                }
                if (n2 <= this.minRange[n4] && n3 >= this.maxRange[n4]) {
                    this.delete(n4);
                    this.merge(n2, n3);
                    return;
                }
                if (n2 >= this.minRange[n4] && n2 <= this.maxRange[n4]) {
                    this.delete(n4);
                    n2 = this.minRange[n4];
                    this.merge(n2, n3);
                    return;
                }
                if (n3 >= this.minRange[n4] && n3 <= this.maxRange[n4]) {
                    this.delete(n4);
                    n3 = this.maxRange[n4];
                    this.merge(n2, n3);
                    return;
                }
                ++n4;
            }
            if (this.num >= this.size) {
                this.size *= 2;
                int[] arrn = new int[this.size];
                int[] arrn2 = new int[this.size];
                System.arraycopy(this.minRange, 0, arrn, 0, this.num);
                System.arraycopy(this.maxRange, 0, arrn2, 0, this.num);
                this.minRange = arrn;
                this.maxRange = arrn2;
            }
            this.minRange[this.num] = n2;
            this.maxRange[this.num] = n3;
            ++this.num;
        }

        void remove(int n2, int n3) {
            int n4 = 0;
            while (n4 < this.num) {
                if (this.minRange[n4] >= n2 && this.maxRange[n4] <= n3) {
                    this.delete(n4);
                    --n4;
                    return;
                }
                if (n2 >= this.minRange[n4] && n3 <= this.maxRange[n4]) {
                    int n5 = this.minRange[n4];
                    int n6 = this.maxRange[n4];
                    this.delete(n4);
                    if (n5 < n2 - 1) {
                        this.merge(n5, n2 - 1);
                    }
                    if (n3 + 1 < n6) {
                        this.merge(n3 + 1, n6);
                    }
                    return;
                }
                if (this.minRange[n4] >= n2 && this.minRange[n4] <= n3) {
                    this.minRange[n4] = n3 + 1;
                    return;
                }
                if (this.maxRange[n4] >= n2 && this.maxRange[n4] <= n3) {
                    this.maxRange[n4] = n2 - 1;
                    return;
                }
                ++n4;
            }
        }
    }

}

