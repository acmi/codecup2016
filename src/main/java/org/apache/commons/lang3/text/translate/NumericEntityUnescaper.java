/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class NumericEntityUnescaper
extends CharSequenceTranslator {
    private final EnumSet<OPTION> options;

    public /* varargs */ NumericEntityUnescaper(OPTION ... arroPTION) {
        this.options = arroPTION.length > 0 ? EnumSet.copyOf(Arrays.asList(arroPTION)) : EnumSet.copyOf(Arrays.asList(new OPTION[]{OPTION.semiColonRequired}));
    }

    public boolean isSet(OPTION oPTION) {
        return this.options == null ? false : this.options.contains((Object)oPTION);
    }

    @Override
    public int translate(CharSequence charSequence, int n2, Writer writer) throws IOException {
        int n3 = charSequence.length();
        if (charSequence.charAt(n2) == '&' && n2 < n3 - 2 && charSequence.charAt(n2 + 1) == '#') {
            boolean bl;
            int n4;
            int n5;
            int n6 = n2 + 2;
            boolean bl2 = false;
            char c2 = charSequence.charAt(n6);
            if (c2 == 'x' || c2 == 'X') {
                bl2 = true;
                if (++n6 == n3) {
                    return 0;
                }
            }
            for (n5 = n6; n5 < n3 && (charSequence.charAt(n5) >= '0' && charSequence.charAt(n5) <= '9' || charSequence.charAt(n5) >= 'a' && charSequence.charAt(n5) <= 'f' || charSequence.charAt(n5) >= 'A' && charSequence.charAt(n5) <= 'F'); ++n5) {
            }
            boolean bl3 = bl = n5 != n3 && charSequence.charAt(n5) == ';';
            if (!bl) {
                if (this.isSet(OPTION.semiColonRequired)) {
                    return 0;
                }
                if (this.isSet(OPTION.errorIfNoSemiColon)) {
                    throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
                }
            }
            try {
                n4 = bl2 ? Integer.parseInt(charSequence.subSequence(n6, n5).toString(), 16) : Integer.parseInt(charSequence.subSequence(n6, n5).toString(), 10);
            }
            catch (NumberFormatException numberFormatException) {
                return 0;
            }
            if (n4 > 65535) {
                char[] arrc = Character.toChars(n4);
                writer.write(arrc[0]);
                writer.write(arrc[1]);
            } else {
                writer.write(n4);
            }
            return 2 + n5 - n6 + (bl2 ? 1 : 0) + (bl ? 1 : 0);
        }
        return 0;
    }

    public static final class OPTION
    extends Enum<OPTION> {
        public static final /* enum */ OPTION semiColonRequired = new OPTION();
        public static final /* enum */ OPTION semiColonOptional = new OPTION();
        public static final /* enum */ OPTION errorIfNoSemiColon = new OPTION();
        private static final /* synthetic */ OPTION[] $VALUES;

        public static OPTION[] values() {
            return (OPTION[])$VALUES.clone();
        }

        private OPTION() {
            super(string, n2);
        }

        static {
            $VALUES = new OPTION[]{semiColonRequired, semiColonOptional, errorIfNoSemiColon};
        }
    }

}

