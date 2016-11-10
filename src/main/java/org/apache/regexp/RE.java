/*
 * Decompiled with CFR 0_119.
 */
package org.apache.regexp;

import org.apache.regexp.CharacterIterator;
import org.apache.regexp.RECompiler;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;
import org.apache.regexp.StringCharacterIterator;

public class RE {
    static final String NEWLINE = System.getProperty("line.separator");
    REProgram program;
    CharacterIterator search;
    int matchFlags;
    int parenCount;
    int start0;
    int end0;
    int start1;
    int end1;
    int start2;
    int end2;
    int[] startn;
    int[] endn;
    int[] startBackref;
    int[] endBackref;

    public RE() {
        this((REProgram)null, 0);
    }

    public RE(String string) throws RESyntaxException {
        this(string, 0);
    }

    public RE(String string, int n2) throws RESyntaxException {
        this(new RECompiler().compile(string));
        this.setMatchFlags(n2);
    }

    public RE(REProgram rEProgram) {
        this(rEProgram, 0);
    }

    public RE(REProgram rEProgram, int n2) {
        this.setProgram(rEProgram);
        this.setMatchFlags(n2);
    }

    private final void allocParens() {
        this.startn = new int[16];
        this.endn = new int[16];
        int n2 = 0;
        while (n2 < 16) {
            this.startn[n2] = -1;
            this.endn[n2] = -1;
            ++n2;
        }
    }

    public final int getParenEnd(int n2) {
        if (n2 < this.parenCount) {
            switch (n2) {
                case 0: {
                    return this.end0;
                }
                case 1: {
                    return this.end1;
                }
                case 2: {
                    return this.end2;
                }
            }
            if (this.endn == null) {
                this.allocParens();
            }
            return this.endn[n2];
        }
        return -1;
    }

    public final int getParenLength(int n2) {
        if (n2 < this.parenCount) {
            return this.getParenEnd(n2) - this.getParenStart(n2);
        }
        return -1;
    }

    public final int getParenStart(int n2) {
        if (n2 < this.parenCount) {
            switch (n2) {
                case 0: {
                    return this.start0;
                }
                case 1: {
                    return this.start1;
                }
                case 2: {
                    return this.start2;
                }
            }
            if (this.startn == null) {
                this.allocParens();
            }
            return this.startn[n2];
        }
        return -1;
    }

    protected void internalError(String string) throws Error {
        throw new Error("RE internal error: " + string);
    }

    private boolean isNewline(int n2) {
        if (n2 < NEWLINE.length() - 1) {
            return false;
        }
        if (this.search.charAt(n2) == '\n') {
            return true;
        }
        int n3 = NEWLINE.length() - 1;
        while (n3 >= 0) {
            if (NEWLINE.charAt(n3) != this.search.charAt(n2)) {
                return false;
            }
            --n3;
            --n2;
        }
        return true;
    }

    public boolean match(String string, int n2) {
        return this.match(new StringCharacterIterator(string), n2);
    }

    public boolean match(CharacterIterator characterIterator, int n2) {
        if (this.program == null) {
            this.internalError("No RE program to run!");
        }
        this.search = characterIterator;
        if (this.program.prefix == null) {
            while (!characterIterator.isEnd(n2 - 1)) {
                if (this.matchAt(n2)) {
                    return true;
                }
                ++n2;
            }
            return false;
        }
        boolean bl = (this.matchFlags & 1) != 0;
        char[] arrc = this.program.prefix;
        while (!characterIterator.isEnd(n2 + arrc.length - 1)) {
            boolean bl2 = false;
            if (bl) {
                bl2 = Character.toLowerCase(characterIterator.charAt(n2)) == Character.toLowerCase(arrc[0]);
            } else {
                boolean bl3 = bl2 = characterIterator.charAt(n2) == arrc[0];
            }
            if (bl2) {
                int n3 = n2++;
                int n4 = 1;
                while (n4 < arrc.length) {
                    if (bl) {
                        bl2 = Character.toLowerCase(characterIterator.charAt(n2++)) == Character.toLowerCase(arrc[n4++]);
                    } else {
                        boolean bl4 = bl2 = characterIterator.charAt(n2++) == arrc[n4++];
                    }
                    if (!bl2) break;
                }
                if (n4 == arrc.length && this.matchAt(n3)) {
                    return true;
                }
                n2 = n3;
            }
            ++n2;
        }
        return false;
    }

    protected boolean matchAt(int n2) {
        int n3;
        this.start0 = -1;
        this.end0 = -1;
        this.start1 = -1;
        this.end1 = -1;
        this.start2 = -1;
        this.end2 = -1;
        this.startn = null;
        this.endn = null;
        this.parenCount = 1;
        this.setParenStart(0, n2);
        if ((this.program.flags & 1) != 0) {
            this.startBackref = new int[16];
            this.endBackref = new int[16];
        }
        if ((n3 = this.matchNodes(0, 65536, n2)) != -1) {
            this.setParenEnd(0, n3);
            return true;
        }
        this.parenCount = 0;
        return false;
    }

    /*
     * Exception decompiling
     */
    protected int matchNodes(int var1_1, int var2_2, int var3_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
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

    public void setMatchFlags(int n2) {
        this.matchFlags = n2;
    }

    protected final void setParenEnd(int n2, int n3) {
        if (n2 < this.parenCount) {
            switch (n2) {
                case 0: {
                    this.end0 = n3;
                    break;
                }
                case 1: {
                    this.end1 = n3;
                    break;
                }
                case 2: {
                    this.end2 = n3;
                    break;
                }
                default: {
                    if (this.endn == null) {
                        this.allocParens();
                    }
                    this.endn[n2] = n3;
                    break;
                }
            }
        }
    }

    protected final void setParenStart(int n2, int n3) {
        if (n2 < this.parenCount) {
            switch (n2) {
                case 0: {
                    this.start0 = n3;
                    break;
                }
                case 1: {
                    this.start1 = n3;
                    break;
                }
                case 2: {
                    this.start2 = n3;
                    break;
                }
                default: {
                    if (this.startn == null) {
                        this.allocParens();
                    }
                    this.startn[n2] = n3;
                    break;
                }
            }
        }
    }

    public void setProgram(REProgram rEProgram) {
        this.program = rEProgram;
    }
}

