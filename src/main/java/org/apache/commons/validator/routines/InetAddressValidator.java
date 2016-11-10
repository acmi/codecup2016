/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.validator.routines;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.validator.routines.RegexValidator;

public class InetAddressValidator
implements Serializable {
    private static final InetAddressValidator VALIDATOR = new InetAddressValidator();
    private final RegexValidator ipv4Validator = new RegexValidator("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");

    public static InetAddressValidator getInstance() {
        return VALIDATOR;
    }

    public boolean isValidInet4Address(String string) {
        String[] arrstring = this.ipv4Validator.match(string);
        if (arrstring == null) {
            return false;
        }
        for (String string2 : arrstring) {
            if (string2 == null || string2.length() == 0) {
                return false;
            }
            int n2 = 0;
            try {
                n2 = Integer.parseInt(string2);
            }
            catch (NumberFormatException numberFormatException) {
                return false;
            }
            if (n2 > 255) {
                return false;
            }
            if (string2.length() <= 1 || !string2.startsWith("0")) continue;
            return false;
        }
        return true;
    }

    public boolean isValidInet6Address(String string) {
        boolean bl = string.contains("::");
        if (bl && string.indexOf("::") != string.lastIndexOf("::")) {
            return false;
        }
        if (string.startsWith(":") && !string.startsWith("::") || string.endsWith(":") && !string.endsWith("::")) {
            return false;
        }
        String[] arrstring = string.split(":");
        if (bl) {
            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arrstring));
            if (string.endsWith("::")) {
                arrayList.add("");
            } else if (string.startsWith("::") && !arrayList.isEmpty()) {
                arrayList.remove(0);
            }
            arrstring = arrayList.toArray(new String[arrayList.size()]);
        }
        if (arrstring.length > 8) {
            return false;
        }
        int n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            String string2 = arrstring[i2];
            if (string2.length() == 0) {
                if (++n3 > 1) {
                    return false;
                }
            } else {
                n3 = 0;
                if (string2.contains(".")) {
                    if (!string.endsWith(string2)) {
                        return false;
                    }
                    if (i2 > arrstring.length - 1 || i2 > 6) {
                        return false;
                    }
                    if (!this.isValidInet4Address(string2)) {
                        return false;
                    }
                    n2 += 2;
                    continue;
                }
                if (string2.length() > 4) {
                    return false;
                }
                int n4 = 0;
                try {
                    n4 = Integer.valueOf(string2, 16);
                }
                catch (NumberFormatException numberFormatException) {
                    return false;
                }
                if (n4 < 0 || n4 > 65535) {
                    return false;
                }
            }
            ++n2;
        }
        if (n2 < 8 && !bl) {
            return false;
        }
        return true;
    }
}

