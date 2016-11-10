/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import org.apache.xml.utils.XMLCharacterRecognizer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class FastStringBuffer {
    int m_chunkBits = 15;
    int m_maxChunkBits = 15;
    int m_rebundleBits = 2;
    int m_chunkSize;
    int m_chunkMask;
    char[][] m_array;
    int m_lastChunk = 0;
    int m_firstFree = 0;
    FastStringBuffer m_innerFSB = null;
    static final char[] SINGLE_SPACE = new char[]{' '};

    public FastStringBuffer(int n2, int n3, int n4) {
        n3 = n2;
        this.m_array = new char[16][];
        if (n2 > n3) {
            n2 = n3;
        }
        this.m_chunkBits = n2;
        this.m_maxChunkBits = n3;
        this.m_rebundleBits = n4;
        this.m_chunkSize = 1 << n2;
        this.m_chunkMask = this.m_chunkSize - 1;
        this.m_array[0] = new char[this.m_chunkSize];
    }

    public FastStringBuffer(int n2, int n3) {
        this(n2, n3, 2);
    }

    public FastStringBuffer(int n2) {
        this(n2, 15, 2);
    }

    public FastStringBuffer() {
        this(10, 15, 2);
    }

    public final int size() {
        return (this.m_lastChunk << this.m_chunkBits) + this.m_firstFree;
    }

    public final int length() {
        return (this.m_lastChunk << this.m_chunkBits) + this.m_firstFree;
    }

    public final void reset() {
        this.m_lastChunk = 0;
        this.m_firstFree = 0;
        FastStringBuffer fastStringBuffer = this;
        while (fastStringBuffer.m_innerFSB != null) {
            fastStringBuffer = fastStringBuffer.m_innerFSB;
        }
        this.m_chunkBits = fastStringBuffer.m_chunkBits;
        this.m_chunkSize = fastStringBuffer.m_chunkSize;
        this.m_chunkMask = fastStringBuffer.m_chunkMask;
        this.m_innerFSB = null;
        this.m_array = new char[16][0];
        this.m_array[0] = new char[this.m_chunkSize];
    }

    public final void setLength(int n2) {
        this.m_lastChunk = n2 >>> this.m_chunkBits;
        if (this.m_lastChunk == 0 && this.m_innerFSB != null) {
            this.m_innerFSB.setLength(n2, this);
        } else {
            this.m_firstFree = n2 & this.m_chunkMask;
            if (this.m_firstFree == 0 && this.m_lastChunk > 0) {
                --this.m_lastChunk;
                this.m_firstFree = this.m_chunkSize;
            }
        }
    }

    private final void setLength(int n2, FastStringBuffer fastStringBuffer) {
        this.m_lastChunk = n2 >>> this.m_chunkBits;
        if (this.m_lastChunk == 0 && this.m_innerFSB != null) {
            this.m_innerFSB.setLength(n2, fastStringBuffer);
        } else {
            fastStringBuffer.m_chunkBits = this.m_chunkBits;
            fastStringBuffer.m_maxChunkBits = this.m_maxChunkBits;
            fastStringBuffer.m_rebundleBits = this.m_rebundleBits;
            fastStringBuffer.m_chunkSize = this.m_chunkSize;
            fastStringBuffer.m_chunkMask = this.m_chunkMask;
            fastStringBuffer.m_array = this.m_array;
            fastStringBuffer.m_innerFSB = this.m_innerFSB;
            fastStringBuffer.m_lastChunk = this.m_lastChunk;
            fastStringBuffer.m_firstFree = n2 & this.m_chunkMask;
        }
    }

    public final String toString() {
        int n2 = (this.m_lastChunk << this.m_chunkBits) + this.m_firstFree;
        return this.getString(new StringBuffer(n2), 0, 0, n2).toString();
    }

    public final void append(char c2) {
        char[] arrc;
        if (this.m_firstFree < this.m_chunkSize) {
            arrc = this.m_array[this.m_lastChunk];
        } else {
            int n2 = this.m_array.length;
            if (this.m_lastChunk + 1 == n2) {
                char[][] arrarrc = new char[n2 + 16][];
                System.arraycopy(this.m_array, 0, arrarrc, 0, n2);
                this.m_array = arrarrc;
            }
            if ((arrc = this.m_array[++this.m_lastChunk]) == null) {
                if (this.m_lastChunk == 1 << this.m_rebundleBits && this.m_chunkBits < this.m_maxChunkBits) {
                    this.m_innerFSB = new FastStringBuffer(this);
                }
                this.m_array[this.m_lastChunk] = new char[this.m_chunkSize];
                arrc = this.m_array[this.m_lastChunk];
            }
            this.m_firstFree = 0;
        }
        arrc[this.m_firstFree++] = c2;
    }

    public final void append(String string) {
        if (string == null) {
            return;
        }
        int n2 = string.length();
        if (0 == n2) {
            return;
        }
        int n3 = 0;
        char[] arrc = this.m_array[this.m_lastChunk];
        int n4 = this.m_chunkSize - this.m_firstFree;
        while (n2 > 0) {
            if (n4 > n2) {
                n4 = n2;
            }
            string.getChars(n3, n3 + n4, this.m_array[this.m_lastChunk], this.m_firstFree);
            n3 += n4;
            if ((n2 -= n4) <= 0) continue;
            int n5 = this.m_array.length;
            if (this.m_lastChunk + 1 == n5) {
                char[][] arrarrc = new char[n5 + 16][];
                System.arraycopy(this.m_array, 0, arrarrc, 0, n5);
                this.m_array = arrarrc;
            }
            if ((arrc = this.m_array[++this.m_lastChunk]) == null) {
                if (this.m_lastChunk == 1 << this.m_rebundleBits && this.m_chunkBits < this.m_maxChunkBits) {
                    this.m_innerFSB = new FastStringBuffer(this);
                }
                this.m_array[this.m_lastChunk] = new char[this.m_chunkSize];
                arrc = this.m_array[this.m_lastChunk];
            }
            n4 = this.m_chunkSize;
            this.m_firstFree = 0;
        }
        this.m_firstFree += n4;
    }

    public final void append(char[] arrc, int n2, int n3) {
        int n4 = n3;
        if (0 == n4) {
            return;
        }
        int n5 = n2;
        char[] arrc2 = this.m_array[this.m_lastChunk];
        int n6 = this.m_chunkSize - this.m_firstFree;
        while (n4 > 0) {
            if (n6 > n4) {
                n6 = n4;
            }
            System.arraycopy(arrc, n5, this.m_array[this.m_lastChunk], this.m_firstFree, n6);
            n5 += n6;
            if ((n4 -= n6) <= 0) continue;
            int n7 = this.m_array.length;
            if (this.m_lastChunk + 1 == n7) {
                char[][] arrarrc = new char[n7 + 16][];
                System.arraycopy(this.m_array, 0, arrarrc, 0, n7);
                this.m_array = arrarrc;
            }
            if ((arrc2 = this.m_array[++this.m_lastChunk]) == null) {
                if (this.m_lastChunk == 1 << this.m_rebundleBits && this.m_chunkBits < this.m_maxChunkBits) {
                    this.m_innerFSB = new FastStringBuffer(this);
                }
                this.m_array[this.m_lastChunk] = new char[this.m_chunkSize];
                arrc2 = this.m_array[this.m_lastChunk];
            }
            n6 = this.m_chunkSize;
            this.m_firstFree = 0;
        }
        this.m_firstFree += n6;
    }

    public boolean isWhitespace(int n2, int n3) {
        int n4 = n2 >>> this.m_chunkBits;
        int n5 = n2 & this.m_chunkMask;
        int n6 = this.m_chunkSize - n5;
        while (n3 > 0) {
            int n7 = n3 <= n6 ? n3 : n6;
            boolean bl = n4 == 0 && this.m_innerFSB != null ? this.m_innerFSB.isWhitespace(n5, n7) : XMLCharacterRecognizer.isWhiteSpace(this.m_array[n4], n5, n7);
            if (!bl) {
                return false;
            }
            n3 -= n7;
            ++n4;
            n5 = 0;
            n6 = this.m_chunkSize;
        }
        return true;
    }

    public String getString(int n2, int n3) {
        int n4 = n2 & this.m_chunkMask;
        int n5 = n2 >>> this.m_chunkBits;
        if (n4 + n3 < this.m_chunkMask && this.m_innerFSB == null) {
            return this.getOneChunkString(n5, n4, n3);
        }
        return this.getString(new StringBuffer(n3), n5, n4, n3).toString();
    }

    protected String getOneChunkString(int n2, int n3, int n4) {
        return new String(this.m_array[n2], n3, n4);
    }

    StringBuffer getString(StringBuffer stringBuffer, int n2, int n3) {
        return this.getString(stringBuffer, n2 >>> this.m_chunkBits, n2 & this.m_chunkMask, n3);
    }

    StringBuffer getString(StringBuffer stringBuffer, int n2, int n3, int n4) {
        int n5 = (n2 << this.m_chunkBits) + n3 + n4;
        int n6 = n5 >>> this.m_chunkBits;
        int n7 = n5 & this.m_chunkMask;
        for (int i2 = n2; i2 < n6; ++i2) {
            if (i2 == 0 && this.m_innerFSB != null) {
                this.m_innerFSB.getString(stringBuffer, n3, this.m_chunkSize - n3);
            } else {
                stringBuffer.append(this.m_array[i2], n3, this.m_chunkSize - n3);
            }
            n3 = 0;
        }
        if (n6 == 0 && this.m_innerFSB != null) {
            this.m_innerFSB.getString(stringBuffer, n3, n7 - n3);
        } else if (n7 > n3) {
            stringBuffer.append(this.m_array[n6], n3, n7 - n3);
        }
        return stringBuffer;
    }

    public char charAt(int n2) {
        int n3 = n2 >>> this.m_chunkBits;
        if (n3 == 0 && this.m_innerFSB != null) {
            return this.m_innerFSB.charAt(n2 & this.m_chunkMask);
        }
        return this.m_array[n3][n2 & this.m_chunkMask];
    }

    public void sendSAXcharacters(ContentHandler contentHandler, int n2, int n3) throws SAXException {
        int n4 = n2 >>> this.m_chunkBits;
        int n5 = n2 & this.m_chunkMask;
        if (n5 + n3 < this.m_chunkMask && this.m_innerFSB == null) {
            contentHandler.characters(this.m_array[n4], n5, n3);
            return;
        }
        int n6 = n2 + n3;
        int n7 = n6 >>> this.m_chunkBits;
        int n8 = n6 & this.m_chunkMask;
        for (int i2 = n4; i2 < n7; ++i2) {
            if (i2 == 0 && this.m_innerFSB != null) {
                this.m_innerFSB.sendSAXcharacters(contentHandler, n5, this.m_chunkSize - n5);
            } else {
                contentHandler.characters(this.m_array[i2], n5, this.m_chunkSize - n5);
            }
            n5 = 0;
        }
        if (n7 == 0 && this.m_innerFSB != null) {
            this.m_innerFSB.sendSAXcharacters(contentHandler, n5, n8 - n5);
        } else if (n8 > n5) {
            contentHandler.characters(this.m_array[n7], n5, n8 - n5);
        }
    }

    public int sendNormalizedSAXcharacters(ContentHandler contentHandler, int n2, int n3) throws SAXException {
        int n4 = 1;
        int n5 = n2 + n3;
        int n6 = n2 >>> this.m_chunkBits;
        int n7 = n2 & this.m_chunkMask;
        int n8 = n5 >>> this.m_chunkBits;
        int n9 = n5 & this.m_chunkMask;
        for (int i2 = n6; i2 < n8; ++i2) {
            n4 = i2 == 0 && this.m_innerFSB != null ? this.m_innerFSB.sendNormalizedSAXcharacters(contentHandler, n7, this.m_chunkSize - n7) : FastStringBuffer.sendNormalizedSAXcharacters(this.m_array[i2], n7, this.m_chunkSize - n7, contentHandler, n4);
            n7 = 0;
        }
        if (n8 == 0 && this.m_innerFSB != null) {
            n4 = this.m_innerFSB.sendNormalizedSAXcharacters(contentHandler, n7, n9 - n7);
        } else if (n9 > n7) {
            n4 = FastStringBuffer.sendNormalizedSAXcharacters(this.m_array[n8], n7, n9 - n7, contentHandler, n4 | 2);
        }
        return n4;
    }

    static int sendNormalizedSAXcharacters(char[] arrc, int n2, int n3, ContentHandler contentHandler, int n4) throws SAXException {
        int n5;
        boolean bl = (n4 & 1) != 0;
        boolean bl2 = (n4 & 4) != 0;
        int n6 = n2 + n3;
        if (bl) {
            for (n5 = n2; n5 < n6 && XMLCharacterRecognizer.isWhiteSpace(arrc[n5]); ++n5) {
            }
            if (n5 == n6) {
                return n4;
            }
        }
        while (n5 < n6) {
            int n7 = n5;
            while (n5 < n6 && !XMLCharacterRecognizer.isWhiteSpace(arrc[n5])) {
                ++n5;
            }
            if (n7 != n5) {
                if (bl2) {
                    contentHandler.characters(SINGLE_SPACE, 0, 1);
                    bl2 = false;
                }
                contentHandler.characters(arrc, n7, n5 - n7);
            }
            int n8 = n5;
            while (n5 < n6 && XMLCharacterRecognizer.isWhiteSpace(arrc[n5])) {
                ++n5;
            }
            if (n8 == n5) continue;
            bl2 = true;
        }
        return (bl2 ? 4 : 0) | n4 & 2;
    }

    public static void sendNormalizedSAXcharacters(char[] arrc, int n2, int n3, ContentHandler contentHandler) throws SAXException {
        FastStringBuffer.sendNormalizedSAXcharacters(arrc, n2, n3, contentHandler, 3);
    }

    public void sendSAXComment(LexicalHandler lexicalHandler, int n2, int n3) throws SAXException {
        String string = this.getString(n2, n3);
        lexicalHandler.comment(string.toCharArray(), 0, n3);
    }

    private FastStringBuffer(FastStringBuffer fastStringBuffer) {
        this.m_chunkBits = fastStringBuffer.m_chunkBits;
        this.m_maxChunkBits = fastStringBuffer.m_maxChunkBits;
        this.m_rebundleBits = fastStringBuffer.m_rebundleBits;
        this.m_chunkSize = fastStringBuffer.m_chunkSize;
        this.m_chunkMask = fastStringBuffer.m_chunkMask;
        this.m_array = fastStringBuffer.m_array;
        this.m_innerFSB = fastStringBuffer.m_innerFSB;
        this.m_lastChunk = fastStringBuffer.m_lastChunk - 1;
        this.m_firstFree = fastStringBuffer.m_chunkSize;
        fastStringBuffer.m_array = new char[16][];
        fastStringBuffer.m_innerFSB = this;
        fastStringBuffer.m_lastChunk = 1;
        fastStringBuffer.m_firstFree = 0;
        fastStringBuffer.m_chunkBits += this.m_rebundleBits;
        fastStringBuffer.m_chunkSize = 1 << fastStringBuffer.m_chunkBits;
        fastStringBuffer.m_chunkMask = fastStringBuffer.m_chunkSize - 1;
    }
}

