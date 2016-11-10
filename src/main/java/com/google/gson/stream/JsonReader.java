/*
 * Decompiled with CFR 0_119.
 */
package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonReader
implements Closeable {
    private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_EOF = 17;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private final Reader in;
    private boolean lenient = false;
    private final char[] buffer = new char[1024];
    private int pos = 0;
    private int limit = 0;
    private int lineNumber = 0;
    private int lineStart = 0;
    int peeked = 0;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int[] stack = new int[32];
    private int stackSize = 0;
    private String[] pathNames;
    private int[] pathIndices;

    public JsonReader(Reader reader) {
        this.stack[this.stackSize++] = 6;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        if (reader == null) {
            throw new NullPointerException("in == null");
        }
        this.in = reader;
    }

    public final void setLenient(boolean bl) {
        this.lenient = bl;
    }

    public final boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 != 3) {
            throw new IllegalStateException("Expected BEGIN_ARRAY but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.push(1);
        this.pathIndices[this.stackSize - 1] = 0;
        this.peeked = 0;
    }

    public void endArray() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 4) {
            --this.stackSize;
        } else {
            throw new IllegalStateException("Expected END_ARRAY but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        int[] arrn = this.pathIndices;
        int n3 = this.stackSize - 1;
        arrn[n3] = arrn[n3] + 1;
        this.peeked = 0;
    }

    public void beginObject() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 != 1) {
            throw new IllegalStateException("Expected BEGIN_OBJECT but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.push(3);
        this.peeked = 0;
    }

    public void endObject() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 2) {
            --this.stackSize;
        } else {
            throw new IllegalStateException("Expected END_OBJECT but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.pathNames[this.stackSize] = null;
        int[] arrn = this.pathIndices;
        int n3 = this.stackSize - 1;
        arrn[n3] = arrn[n3] + 1;
        this.peeked = 0;
    }

    public boolean hasNext() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        return n2 != 2 && n2 != 4;
    }

    public JsonToken peek() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        switch (n2) {
            case 1: {
                return JsonToken.BEGIN_OBJECT;
            }
            case 2: {
                return JsonToken.END_OBJECT;
            }
            case 3: {
                return JsonToken.BEGIN_ARRAY;
            }
            case 4: {
                return JsonToken.END_ARRAY;
            }
            case 12: 
            case 13: 
            case 14: {
                return JsonToken.NAME;
            }
            case 5: 
            case 6: {
                return JsonToken.BOOLEAN;
            }
            case 7: {
                return JsonToken.NULL;
            }
            case 8: 
            case 9: 
            case 10: 
            case 11: {
                return JsonToken.STRING;
            }
            case 15: 
            case 16: {
                return JsonToken.NUMBER;
            }
            case 17: {
                return JsonToken.END_DOCUMENT;
            }
        }
        throw new AssertionError();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    int doPeek() throws IOException {
        var1_1 = this.stack[this.stackSize - 1];
        if (var1_1 != 1) ** GOTO lbl5
        this.stack[this.stackSize - 1] = 2;
        ** GOTO lbl76
lbl5: // 1 sources:
        if (var1_1 != 2) ** GOTO lbl17
        var2_2 = this.nextNonWhitespace(true);
        switch (var2_2) {
            case 93: {
                this.peeked = 4;
                return 4;
            }
            case 59: {
                this.checkLenient();
            }
            case 44: {
                ** GOTO lbl76
            }
            default: {
                throw this.syntaxError("Unterminated array");
            }
        }
lbl17: // 1 sources:
        if (var1_1 == 3 || var1_1 == 5) {
            this.stack[this.stackSize - 1] = 4;
            if (var1_1 == 5) {
                var2_3 = this.nextNonWhitespace(true);
                switch (var2_3) {
                    case 125: {
                        this.peeked = 2;
                        return 2;
                    }
                    case 59: {
                        this.checkLenient();
                    }
                    case 44: {
                        ** break;
                    }
                }
                throw this.syntaxError("Unterminated object");
            }
lbl30: // 3 sources:
            var2_3 = this.nextNonWhitespace(true);
            switch (var2_3) {
                case 34: {
                    this.peeked = 13;
                    return 13;
                }
                case 39: {
                    this.checkLenient();
                    this.peeked = 12;
                    return 12;
                }
                case 125: {
                    if (var1_1 == 5) throw this.syntaxError("Expected name");
                    this.peeked = 2;
                    return 2;
                }
            }
            this.checkLenient();
            --this.pos;
            if (this.isLiteral((char)var2_3) == false) throw this.syntaxError("Expected name");
            this.peeked = 14;
            return 14;
        }
        if (var1_1 != 4) ** GOTO lbl61
        this.stack[this.stackSize - 1] = 5;
        var2_2 = this.nextNonWhitespace(true);
        switch (var2_2) {
            case 58: {
                ** GOTO lbl76
            }
            case 61: {
                this.checkLenient();
                if ((this.pos < this.limit || this.fillBuffer(1)) && this.buffer[this.pos] == '>') {
                    ++this.pos;
                }
                ** GOTO lbl76
            }
            default: {
                throw this.syntaxError("Expected ':'");
            }
        }
lbl61: // 1 sources:
        if (var1_1 == 6) {
            if (this.lenient) {
                this.consumeNonExecutePrefix();
            }
            this.stack[this.stackSize - 1] = 7;
        } else if (var1_1 == 7) {
            var2_2 = this.nextNonWhitespace(false);
            if (var2_2 == -1) {
                this.peeked = 17;
                return 17;
            }
            this.checkLenient();
            --this.pos;
        } else if (var1_1 == 8) {
            throw new IllegalStateException("JsonReader is closed");
        }
lbl76: // 8 sources:
        var2_2 = this.nextNonWhitespace(true);
        switch (var2_2) {
            case 93: {
                if (var1_1 == 1) {
                    this.peeked = 4;
                    return 4;
                }
            }
            case 44: 
            case 59: {
                if (var1_1 != 1) {
                    if (var1_1 != 2) throw this.syntaxError("Unexpected value");
                }
                this.checkLenient();
                --this.pos;
                this.peeked = 7;
                return 7;
            }
            case 39: {
                this.checkLenient();
                this.peeked = 8;
                return 8;
            }
            case 34: {
                this.peeked = 9;
                return 9;
            }
            case 91: {
                this.peeked = 3;
                return 3;
            }
            case 123: {
                this.peeked = 1;
                return 1;
            }
        }
        --this.pos;
        var3_4 = this.peekKeyword();
        if (var3_4 != 0) {
            return var3_4;
        }
        var3_4 = this.peekNumber();
        if (var3_4 != 0) {
            return var3_4;
        }
        if (!this.isLiteral(this.buffer[this.pos])) {
            throw this.syntaxError("Expected value");
        }
        this.checkLenient();
        this.peeked = 10;
        return 10;
    }

    private int peekKeyword() throws IOException {
        String string;
        String string2;
        int n2;
        char c2 = this.buffer[this.pos];
        if (c2 == 't' || c2 == 'T') {
            string2 = "true";
            string = "TRUE";
            n2 = 5;
        } else if (c2 == 'f' || c2 == 'F') {
            string2 = "false";
            string = "FALSE";
            n2 = 6;
        } else if (c2 == 'n' || c2 == 'N') {
            string2 = "null";
            string = "NULL";
            n2 = 7;
        } else {
            return 0;
        }
        int n3 = string2.length();
        for (int i2 = 1; i2 < n3; ++i2) {
            if (this.pos + i2 >= this.limit && !this.fillBuffer(i2 + 1)) {
                return 0;
            }
            c2 = this.buffer[this.pos + i2];
            if (c2 == string2.charAt(i2) || c2 == string.charAt(i2)) continue;
            return 0;
        }
        if ((this.pos + n3 < this.limit || this.fillBuffer(n3 + 1)) && this.isLiteral(this.buffer[this.pos + n3])) {
            return 0;
        }
        this.pos += n3;
        this.peeked = n2;
        return this.peeked;
    }

    private int peekNumber() throws IOException {
        char[] arrc = this.buffer;
        int n2 = this.pos;
        int n3 = this.limit;
        long l2 = 0;
        boolean bl = false;
        boolean bl2 = true;
        int n4 = 0;
        int n5 = 0;
        block6 : do {
            if (n2 + n5 == n3) {
                if (n5 == arrc.length) {
                    return 0;
                }
                if (!this.fillBuffer(n5 + 1)) break;
                n2 = this.pos;
                n3 = this.limit;
            }
            char c2 = arrc[n2 + n5];
            switch (c2) {
                case '-': {
                    if (n4 == 0) {
                        bl = true;
                        n4 = 1;
                        break;
                    }
                    if (n4 == 5) {
                        n4 = 6;
                        break;
                    }
                    return 0;
                }
                case '+': {
                    if (n4 == 5) {
                        n4 = 6;
                        break;
                    }
                    return 0;
                }
                case 'E': 
                case 'e': {
                    if (n4 == 2 || n4 == 4) {
                        n4 = 5;
                        break;
                    }
                    return 0;
                }
                case '.': {
                    if (n4 == 2) {
                        n4 = 3;
                        break;
                    }
                    return 0;
                }
                default: {
                    if (c2 < '0' || c2 > '9') {
                        if (!this.isLiteral(c2)) break block6;
                        return 0;
                    }
                    if (n4 == 1 || n4 == 0) {
                        l2 = - c2 - 48;
                        n4 = 2;
                        break;
                    }
                    if (n4 == 2) {
                        if (l2 == 0) {
                            return 0;
                        }
                        long l3 = l2 * 10 - (long)(c2 - 48);
                        bl2 &= l2 > -922337203685477580L || l2 == -922337203685477580L && l3 < l2;
                        l2 = l3;
                        break;
                    }
                    if (n4 == 3) {
                        n4 = 4;
                        break;
                    }
                    if (n4 != 5 && n4 != 6) break;
                    n4 = 7;
                }
            }
            ++n5;
        } while (true);
        if (n4 == 2 && bl2 && (l2 != Long.MIN_VALUE || bl)) {
            this.peekedLong = bl ? l2 : - l2;
            this.pos += n5;
            this.peeked = 15;
            return 15;
        }
        if (n4 == 2 || n4 == 4 || n4 == 7) {
            this.peekedNumberLength = n5;
            this.peeked = 16;
            return 16;
        }
        return 0;
    }

    private boolean isLiteral(char c2) throws IOException {
        switch (c2) {
            case '#': 
            case '/': 
            case ';': 
            case '=': 
            case '\\': {
                this.checkLenient();
            }
            case '\t': 
            case '\n': 
            case '\f': 
            case '\r': 
            case ' ': 
            case ',': 
            case ':': 
            case '[': 
            case ']': 
            case '{': 
            case '}': {
                return false;
            }
        }
        return true;
    }

    public String nextName() throws IOException {
        String string;
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 14) {
            string = this.nextUnquotedValue();
        } else if (n2 == 12) {
            string = this.nextQuotedValue('\'');
        } else if (n2 == 13) {
            string = this.nextQuotedValue('\"');
        } else {
            throw new IllegalStateException("Expected a name but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = string;
        return string;
    }

    public String nextString() throws IOException {
        String string;
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 10) {
            string = this.nextUnquotedValue();
        } else if (n2 == 8) {
            string = this.nextQuotedValue('\'');
        } else if (n2 == 9) {
            string = this.nextQuotedValue('\"');
        } else if (n2 == 11) {
            string = this.peekedString;
            this.peekedString = null;
        } else if (n2 == 15) {
            string = Long.toString(this.peekedLong);
        } else if (n2 == 16) {
            string = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            throw new IllegalStateException("Expected a string but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n3 = this.stackSize - 1;
        arrn[n3] = arrn[n3] + 1;
        return string;
    }

    public boolean nextBoolean() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 5) {
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n3 = this.stackSize - 1;
            arrn[n3] = arrn[n3] + 1;
            return true;
        }
        if (n2 == 6) {
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n4 = this.stackSize - 1;
            arrn[n4] = arrn[n4] + 1;
            return false;
        }
        throw new IllegalStateException("Expected a boolean but was " + (Object)((Object)this.peek()) + this.locationString());
    }

    public void nextNull() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 != 7) {
            throw new IllegalStateException("Expected null but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n3 = this.stackSize - 1;
        arrn[n3] = arrn[n3] + 1;
    }

    public double nextDouble() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 15) {
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n3 = this.stackSize - 1;
            arrn[n3] = arrn[n3] + 1;
            return this.peekedLong;
        }
        if (n2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (n2 == 8 || n2 == 9) {
            this.peekedString = this.nextQuotedValue(n2 == 8 ? '\'' : '\"');
        } else if (n2 == 10) {
            this.peekedString = this.nextUnquotedValue();
        } else if (n2 != 11) {
            throw new IllegalStateException("Expected a double but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.peeked = 11;
        double d2 = Double.parseDouble(this.peekedString);
        if (!this.lenient && (Double.isNaN(d2) || Double.isInfinite(d2))) {
            throw new MalformedJsonException("JSON forbids NaN and infinities: " + d2 + this.locationString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n4 = this.stackSize - 1;
        arrn[n4] = arrn[n4] + 1;
        return d2;
    }

    public long nextLong() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 15) {
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n3 = this.stackSize - 1;
            arrn[n3] = arrn[n3] + 1;
            return this.peekedLong;
        }
        if (n2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (n2 == 8 || n2 == 9 || n2 == 10) {
            this.peekedString = n2 == 10 ? this.nextUnquotedValue() : this.nextQuotedValue(n2 == 8 ? '\'' : '\"');
            try {
                long l2 = Long.parseLong(this.peekedString);
                this.peeked = 0;
                int[] arrn = this.pathIndices;
                int n4 = this.stackSize - 1;
                arrn[n4] = arrn[n4] + 1;
                return l2;
            }
            catch (NumberFormatException numberFormatException) {}
        } else {
            throw new IllegalStateException("Expected a long but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.peeked = 11;
        double d2 = Double.parseDouble(this.peekedString);
        long l3 = (long)d2;
        if ((double)l3 != d2) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + this.locationString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n5 = this.stackSize - 1;
        arrn[n5] = arrn[n5] + 1;
        return l3;
    }

    private String nextQuotedValue(char c2) throws IOException {
        char[] arrc = this.buffer;
        StringBuilder stringBuilder = new StringBuilder();
        do {
            int n2 = this.pos;
            int n3 = this.limit;
            int n4 = n2;
            while (n2 < n3) {
                char c3;
                if ((c3 = arrc[n2++]) == c2) {
                    this.pos = n2;
                    stringBuilder.append(arrc, n4, n2 - n4 - 1);
                    return stringBuilder.toString();
                }
                if (c3 == '\\') {
                    this.pos = n2;
                    stringBuilder.append(arrc, n4, n2 - n4 - 1);
                    stringBuilder.append(this.readEscapeCharacter());
                    n2 = this.pos;
                    n3 = this.limit;
                    n4 = n2;
                    continue;
                }
                if (c3 != '\n') continue;
                ++this.lineNumber;
                this.lineStart = n2;
            }
            stringBuilder.append(arrc, n4, n2 - n4);
            this.pos = n2;
        } while (this.fillBuffer(1));
        throw this.syntaxError("Unterminated string");
    }

    private String nextUnquotedValue() throws IOException {
        String string;
        StringBuilder stringBuilder = null;
        int n2 = 0;
        block4 : do {
            if (this.pos + n2 < this.limit) {
                switch (this.buffer[this.pos + n2]) {
                    case '#': 
                    case '/': 
                    case ';': 
                    case '=': 
                    case '\\': {
                        this.checkLenient();
                    }
                    case '\t': 
                    case '\n': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                    case ',': 
                    case ':': 
                    case '[': 
                    case ']': 
                    case '{': 
                    case '}': {
                        break block4;
                    }
                }
                ++n2;
                continue;
            }
            if (n2 < this.buffer.length) {
                if (!this.fillBuffer(n2 + 1)) break;
                continue;
            }
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append(this.buffer, this.pos, n2);
            this.pos += n2;
            n2 = 0;
            if (!this.fillBuffer(1)) break;
        } while (true);
        if (stringBuilder == null) {
            string = new String(this.buffer, this.pos, n2);
        } else {
            stringBuilder.append(this.buffer, this.pos, n2);
            string = stringBuilder.toString();
        }
        this.pos += n2;
        return string;
    }

    private void skipQuotedValue(char c2) throws IOException {
        char[] arrc = this.buffer;
        do {
            int n2 = this.pos;
            int n3 = this.limit;
            while (n2 < n3) {
                char c3;
                if ((c3 = arrc[n2++]) == c2) {
                    this.pos = n2;
                    return;
                }
                if (c3 == '\\') {
                    this.pos = n2;
                    this.readEscapeCharacter();
                    n2 = this.pos;
                    n3 = this.limit;
                    continue;
                }
                if (c3 != '\n') continue;
                ++this.lineNumber;
                this.lineStart = n2;
            }
            this.pos = n2;
        } while (this.fillBuffer(1));
        throw this.syntaxError("Unterminated string");
    }

    private void skipUnquotedValue() throws IOException {
        do {
            int n2 = 0;
            while (this.pos + n2 < this.limit) {
                switch (this.buffer[this.pos + n2]) {
                    case '#': 
                    case '/': 
                    case ';': 
                    case '=': 
                    case '\\': {
                        this.checkLenient();
                    }
                    case '\t': 
                    case '\n': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                    case ',': 
                    case ':': 
                    case '[': 
                    case ']': 
                    case '{': 
                    case '}': {
                        this.pos += n2;
                        return;
                    }
                }
                ++n2;
            }
            this.pos += n2;
        } while (this.fillBuffer(1));
    }

    public int nextInt() throws IOException {
        int n2 = this.peeked;
        if (n2 == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 15) {
            int n3 = (int)this.peekedLong;
            if (this.peekedLong != (long)n3) {
                throw new NumberFormatException("Expected an int but was " + this.peekedLong + this.locationString());
            }
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n4 = this.stackSize - 1;
            arrn[n4] = arrn[n4] + 1;
            return n3;
        }
        if (n2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (n2 == 8 || n2 == 9 || n2 == 10) {
            this.peekedString = n2 == 10 ? this.nextUnquotedValue() : this.nextQuotedValue(n2 == 8 ? '\'' : '\"');
            try {
                int n5 = Integer.parseInt(this.peekedString);
                this.peeked = 0;
                int[] arrn = this.pathIndices;
                int n6 = this.stackSize - 1;
                arrn[n6] = arrn[n6] + 1;
                return n5;
            }
            catch (NumberFormatException numberFormatException) {}
        } else {
            throw new IllegalStateException("Expected an int but was " + (Object)((Object)this.peek()) + this.locationString());
        }
        this.peeked = 11;
        double d2 = Double.parseDouble(this.peekedString);
        int n7 = (int)d2;
        if ((double)n7 != d2) {
            throw new NumberFormatException("Expected an int but was " + this.peekedString + this.locationString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n8 = this.stackSize - 1;
        arrn[n8] = arrn[n8] + 1;
        return n7;
    }

    @Override
    public void close() throws IOException {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }

    public void skipValue() throws IOException {
        int n2 = 0;
        do {
            int n3;
            if ((n3 = this.peeked) == 0) {
                n3 = this.doPeek();
            }
            if (n3 == 3) {
                this.push(1);
                ++n2;
            } else if (n3 == 1) {
                this.push(3);
                ++n2;
            } else if (n3 == 4) {
                --this.stackSize;
                --n2;
            } else if (n3 == 2) {
                --this.stackSize;
                --n2;
            } else if (n3 == 14 || n3 == 10) {
                this.skipUnquotedValue();
            } else if (n3 == 8 || n3 == 12) {
                this.skipQuotedValue('\'');
            } else if (n3 == 9 || n3 == 13) {
                this.skipQuotedValue('\"');
            } else if (n3 == 16) {
                this.pos += this.peekedNumberLength;
            }
            this.peeked = 0;
        } while (n2 != 0);
        int[] arrn = this.pathIndices;
        int n4 = this.stackSize - 1;
        arrn[n4] = arrn[n4] + 1;
        this.pathNames[this.stackSize - 1] = "null";
    }

    private void push(int n2) {
        if (this.stackSize == this.stack.length) {
            int[] arrn = new int[this.stackSize * 2];
            int[] arrn2 = new int[this.stackSize * 2];
            String[] arrstring = new String[this.stackSize * 2];
            System.arraycopy(this.stack, 0, arrn, 0, this.stackSize);
            System.arraycopy(this.pathIndices, 0, arrn2, 0, this.stackSize);
            System.arraycopy(this.pathNames, 0, arrstring, 0, this.stackSize);
            this.stack = arrn;
            this.pathIndices = arrn2;
            this.pathNames = arrstring;
        }
        this.stack[this.stackSize++] = n2;
    }

    private boolean fillBuffer(int n2) throws IOException {
        int n3;
        char[] arrc = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(arrc, this.pos, arrc, 0, this.limit);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        while ((n3 = this.in.read(arrc, this.limit, arrc.length - this.limit)) != -1) {
            this.limit += n3;
            if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && arrc[0] == '\ufeff') {
                ++this.pos;
                ++this.lineStart;
                ++n2;
            }
            if (this.limit < n2) continue;
            return true;
        }
        return false;
    }

    private int nextNonWhitespace(boolean bl) throws IOException {
        block12 : {
            char c2;
            char[] arrc = this.buffer;
            int n2 = this.pos;
            int n3 = this.limit;
            block4 : do {
                if (n2 == n3) {
                    this.pos = n2;
                    if (!this.fillBuffer(1)) break block12;
                    n2 = this.pos;
                    n3 = this.limit;
                }
                if ((c2 = arrc[n2++]) == '\n') {
                    ++this.lineNumber;
                    this.lineStart = n2;
                    continue;
                }
                if (c2 == ' ' || c2 == '\r' || c2 == '\t') continue;
                if (c2 == '/') {
                    char c3;
                    this.pos = n2;
                    if (n2 == n3) {
                        --this.pos;
                        c3 = (char)this.fillBuffer(2) ? 1 : 0;
                        ++this.pos;
                        if (c3 == '\u0000') {
                            return c2;
                        }
                    }
                    this.checkLenient();
                    c3 = arrc[this.pos];
                    switch (c3) {
                        case '*': {
                            ++this.pos;
                            if (!this.skipTo("*/")) {
                                throw this.syntaxError("Unterminated comment");
                            }
                            n2 = this.pos + 2;
                            n3 = this.limit;
                            continue block4;
                        }
                        case '/': {
                            ++this.pos;
                            this.skipToEndOfLine();
                            n2 = this.pos;
                            n3 = this.limit;
                            continue block4;
                        }
                    }
                    return c2;
                }
                if (c2 != '#') break;
                this.pos = n2;
                this.checkLenient();
                this.skipToEndOfLine();
                n2 = this.pos;
                n3 = this.limit;
            } while (true);
            this.pos = n2;
            return c2;
        }
        if (bl) {
            throw new EOFException("End of input" + this.locationString());
        }
        return -1;
    }

    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw this.syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() throws IOException {
        while (this.pos < this.limit || this.fillBuffer(1)) {
            char c2;
            if ((c2 = this.buffer[this.pos++]) == '\n') {
                ++this.lineNumber;
                this.lineStart = this.pos;
                break;
            }
            if (c2 != '\r') continue;
            break;
        }
    }

    private boolean skipTo(String string) throws IOException {
        while (this.pos + string.length() <= this.limit || this.fillBuffer(string.length())) {
            block5 : {
                if (this.buffer[this.pos] == '\n') {
                    ++this.lineNumber;
                    this.lineStart = this.pos + 1;
                } else {
                    for (int i2 = 0; i2 < string.length(); ++i2) {
                        if (this.buffer[this.pos + i2] == string.charAt(i2)) {
                            continue;
                        }
                        break block5;
                    }
                    return true;
                }
            }
            ++this.pos;
        }
        return false;
    }

    public String toString() {
        return this.getClass().getSimpleName() + this.locationString();
    }

    private String locationString() {
        int n2 = this.lineNumber + 1;
        int n3 = this.pos - this.lineStart + 1;
        return " at line " + n2 + " column " + n3 + " path " + this.getPath();
    }

    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder().append('$');
        int n2 = this.stackSize;
        block4 : for (int i2 = 0; i2 < n2; ++i2) {
            switch (this.stack[i2]) {
                case 1: 
                case 2: {
                    stringBuilder.append('[').append(this.pathIndices[i2]).append(']');
                    continue block4;
                }
                case 3: 
                case 4: 
                case 5: {
                    stringBuilder.append('.');
                    if (this.pathNames[i2] == null) continue block4;
                    stringBuilder.append(this.pathNames[i2]);
                    break;
                }
            }
        }
        return stringBuilder.toString();
    }

    private char readEscapeCharacter() throws IOException {
        if (this.pos == this.limit && !this.fillBuffer(1)) {
            throw this.syntaxError("Unterminated escape sequence");
        }
        char c2 = this.buffer[this.pos++];
        switch (c2) {
            case 'u': {
                int n2;
                if (this.pos + 4 > this.limit && !this.fillBuffer(4)) {
                    throw this.syntaxError("Unterminated escape sequence");
                }
                char c3 = '\u0000';
                int n3 = n2 + 4;
                for (n2 = this.pos; n2 < n3; ++n2) {
                    char c4 = this.buffer[n2];
                    c3 = (char)(c3 << 4);
                    if (c4 >= '0' && c4 <= '9') {
                        c3 = (char)(c3 + (c4 - 48));
                        continue;
                    }
                    if (c4 >= 'a' && c4 <= 'f') {
                        c3 = (char)(c3 + (c4 - 97 + 10));
                        continue;
                    }
                    if (c4 >= 'A' && c4 <= 'F') {
                        c3 = (char)(c3 + (c4 - 65 + 10));
                        continue;
                    }
                    throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
                }
                this.pos += 4;
                return c3;
            }
            case 't': {
                return '\t';
            }
            case 'b': {
                return '\b';
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
            case '\n': {
                ++this.lineNumber;
                this.lineStart = this.pos;
            }
            case '\"': 
            case '\'': 
            case '/': 
            case '\\': {
                return c2;
            }
        }
        throw this.syntaxError("Invalid escape sequence");
    }

    private IOException syntaxError(String string) throws IOException {
        throw new MalformedJsonException(string + this.locationString());
    }

    private void consumeNonExecutePrefix() throws IOException {
        this.nextNonWhitespace(true);
        --this.pos;
        if (this.pos + NON_EXECUTE_PREFIX.length > this.limit && !this.fillBuffer(NON_EXECUTE_PREFIX.length)) {
            return;
        }
        for (int i2 = 0; i2 < NON_EXECUTE_PREFIX.length; ++i2) {
            if (this.buffer[this.pos + i2] == NON_EXECUTE_PREFIX[i2]) continue;
            return;
        }
        this.pos += NON_EXECUTE_PREFIX.length;
    }

    static {
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess(){

            @Override
            public void promoteNameToValue(JsonReader jsonReader) throws IOException {
                if (jsonReader instanceof JsonTreeReader) {
                    ((JsonTreeReader)jsonReader).promoteNameToValue();
                    return;
                }
                int n2 = jsonReader.peeked;
                if (n2 == 0) {
                    n2 = jsonReader.doPeek();
                }
                if (n2 == 13) {
                    jsonReader.peeked = 9;
                } else if (n2 == 12) {
                    jsonReader.peeked = 8;
                } else if (n2 == 14) {
                    jsonReader.peeked = 10;
                } else {
                    throw new IllegalStateException("Expected a name but was " + (Object)((Object)jsonReader.peek()) + jsonReader.locationString());
                }
            }
        };
    }

}

