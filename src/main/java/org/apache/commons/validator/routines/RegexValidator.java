/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.validator.routines;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator
implements Serializable {
    private final Pattern[] patterns;

    public RegexValidator(String string) {
        this(string, true);
    }

    public RegexValidator(String string, boolean bl) {
        this(new String[]{string}, bl);
    }

    public RegexValidator(String[] arrstring, boolean bl) {
        if (arrstring == null || arrstring.length == 0) {
            throw new IllegalArgumentException("Regular expressions are missing");
        }
        this.patterns = new Pattern[arrstring.length];
        int n2 = bl ? 0 : 2;
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            if (arrstring[i2] == null || arrstring[i2].length() == 0) {
                throw new IllegalArgumentException("Regular expression[" + i2 + "] is missing");
            }
            this.patterns[i2] = Pattern.compile(arrstring[i2], n2);
        }
    }

    public boolean isValid(String string) {
        if (string == null) {
            return false;
        }
        for (int i2 = 0; i2 < this.patterns.length; ++i2) {
            if (!this.patterns[i2].matcher(string).matches()) continue;
            return true;
        }
        return false;
    }

    public String[] match(String string) {
        if (string == null) {
            return null;
        }
        for (int i2 = 0; i2 < this.patterns.length; ++i2) {
            Matcher matcher = this.patterns[i2].matcher(string);
            if (!matcher.matches()) continue;
            int n2 = matcher.groupCount();
            String[] arrstring = new String[n2];
            for (int i3 = 0; i3 < n2; ++i3) {
                arrstring[i3] = matcher.group(i3 + 1);
            }
            return arrstring;
        }
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RegexValidator{");
        for (int i2 = 0; i2 < this.patterns.length; ++i2) {
            if (i2 > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(this.patterns[i2].pattern());
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

