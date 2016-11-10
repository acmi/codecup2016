/*
 * Decompiled with CFR 0_119.
 */
package org.apache.regexp;

public class REProgram {
    char[] instruction;
    int lenInstruction;
    char[] prefix;
    int flags;

    public REProgram(char[] arrc) {
        this(arrc, arrc.length);
    }

    public REProgram(char[] arrc, int n2) {
        this.setInstructions(arrc, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setInstructions(char[] arrc, int n2) {
        int n3;
        this.instruction = arrc;
        this.lenInstruction = n2;
        this.flags = 0;
        this.prefix = null;
        if (arrc == null) return;
        if (n2 == 0) return;
        if (n2 >= 3 && arrc[0] == '|' && arrc[n3 = arrc[2]] == 'E' && n2 >= 6 && arrc[3] == 'A') {
            char c2 = arrc[4];
            this.prefix = new char[c2];
            System.arraycopy(arrc, 6, this.prefix, 0, c2);
        }
        n3 = 0;
        while (n3 < n2) {
            switch (arrc[n3]) {
                case '[': {
                    n3 += arrc[n3 + 1] * 2;
                    break;
                }
                case 'A': {
                    n3 += arrc[n3 + 1];
                    break;
                }
                case '#': {
                    this.flags |= 1;
                    return;
                }
            }
            n3 += 3;
        }
    }
}

