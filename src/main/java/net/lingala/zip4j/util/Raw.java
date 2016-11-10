/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.util;

import java.io.DataInput;
import java.io.IOException;
import net.lingala.zip4j.exception.ZipException;

public class Raw {
    public static long readLongLittleEndian(byte[] arrby, int n2) {
        long l2 = 0;
        l2 |= (long)(arrby[n2 + 7] & 255);
        l2 <<= 8;
        l2 |= (long)(arrby[n2 + 6] & 255);
        l2 <<= 8;
        l2 |= (long)(arrby[n2 + 5] & 255);
        l2 <<= 8;
        l2 |= (long)(arrby[n2 + 4] & 255);
        l2 <<= 8;
        l2 |= (long)(arrby[n2 + 3] & 255);
        l2 <<= 8;
        l2 |= (long)(arrby[n2 + 2] & 255);
        l2 <<= 8;
        l2 |= (long)(arrby[n2 + 1] & 255);
        l2 <<= 8;
        return l2 |= (long)(arrby[n2] & 255);
    }

    public static int readLeInt(DataInput dataInput, byte[] arrby) throws ZipException {
        try {
            dataInput.readFully(arrby, 0, 4);
        }
        catch (IOException iOException) {
            throw new ZipException(iOException);
        }
        return arrby[0] & 255 | (arrby[1] & 255) << 8 | (arrby[2] & 255 | (arrby[3] & 255) << 8) << 16;
    }

    public static int readShortLittleEndian(byte[] arrby, int n2) {
        return arrby[n2] & 255 | (arrby[n2 + 1] & 255) << 8;
    }

    public static final short readShortBigEndian(byte[] arrby, int n2) {
        short s2 = 0;
        s2 = (short)(s2 | arrby[n2] & 255);
        s2 = (short)(s2 << 8);
        s2 = (short)(s2 | arrby[n2 + 1] & 255);
        return s2;
    }

    public static int readIntLittleEndian(byte[] arrby, int n2) {
        return arrby[n2] & 255 | (arrby[n2 + 1] & 255) << 8 | (arrby[n2 + 2] & 255 | (arrby[n2 + 3] & 255) << 8) << 16;
    }

    public static void prepareBuffAESIVBytes(byte[] arrby, int n2, int n3) {
        arrby[0] = (byte)n2;
        arrby[1] = (byte)(n2 >> 8);
        arrby[2] = (byte)(n2 >> 16);
        arrby[3] = (byte)(n2 >> 24);
        arrby[4] = 0;
        arrby[5] = 0;
        arrby[6] = 0;
        arrby[7] = 0;
        arrby[8] = 0;
        arrby[9] = 0;
        arrby[10] = 0;
        arrby[11] = 0;
        arrby[12] = 0;
        arrby[13] = 0;
        arrby[14] = 0;
        arrby[15] = 0;
    }

    public static byte[] convertCharArrayToByteArray(char[] arrc) {
        if (arrc == null) {
            throw new NullPointerException();
        }
        byte[] arrby = new byte[arrc.length];
        for (int i2 = 0; i2 < arrc.length; ++i2) {
            arrby[i2] = (byte)arrc[i2];
        }
        return arrby;
    }
}

