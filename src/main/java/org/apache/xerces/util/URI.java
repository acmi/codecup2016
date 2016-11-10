/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

public class URI
implements Serializable {
    static final long serialVersionUID = 1601921774685357214L;
    private static final byte[] fgLookupTable = new byte[128];
    private static final int RESERVED_CHARACTERS = 1;
    private static final int MARK_CHARACTERS = 2;
    private static final int SCHEME_CHARACTERS = 4;
    private static final int USERINFO_CHARACTERS = 8;
    private static final int ASCII_ALPHA_CHARACTERS = 16;
    private static final int ASCII_DIGIT_CHARACTERS = 32;
    private static final int ASCII_HEX_CHARACTERS = 64;
    private static final int PATH_CHARACTERS = 128;
    private static final int MASK_ALPHA_NUMERIC = 48;
    private static final int MASK_UNRESERVED_MASK = 50;
    private static final int MASK_URI_CHARACTER = 51;
    private static final int MASK_SCHEME_CHARACTER = 52;
    private static final int MASK_USERINFO_CHARACTER = 58;
    private static final int MASK_PATH_CHARACTER = 178;
    private String m_scheme = null;
    private String m_userinfo = null;
    private String m_host = null;
    private int m_port = -1;
    private String m_regAuthority = null;
    private String m_path = null;
    private String m_queryString = null;
    private String m_fragment = null;
    private static boolean DEBUG;

    public URI() {
    }

    public URI(URI uRI) {
        this.initialize(uRI);
    }

    public URI(String string) throws MalformedURIException {
        this((URI)null, string);
    }

    public URI(String string, boolean bl) throws MalformedURIException {
        this(null, string, bl);
    }

    public URI(URI uRI, String string) throws MalformedURIException {
        this.initialize(uRI, string);
    }

    public URI(URI uRI, String string, boolean bl) throws MalformedURIException {
        this.initialize(uRI, string, bl);
    }

    public URI(String string, String string2) throws MalformedURIException {
        if (string == null || string.trim().length() == 0) {
            throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
        }
        if (string2 == null || string2.trim().length() == 0) {
            throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
        }
        this.setScheme(string);
        this.setPath(string2);
    }

    public URI(String string, String string2, String string3, String string4, String string5) throws MalformedURIException {
        this(string, null, string2, -1, string3, string4, string5);
    }

    public URI(String string, String string2, String string3, int n2, String string4, String string5, String string6) throws MalformedURIException {
        if (string == null || string.trim().length() == 0) {
            throw new MalformedURIException("Scheme is required!");
        }
        if (string3 == null) {
            if (string2 != null) {
                throw new MalformedURIException("Userinfo may not be specified if host is not specified!");
            }
            if (n2 != -1) {
                throw new MalformedURIException("Port may not be specified if host is not specified!");
            }
        }
        if (string4 != null) {
            if (string4.indexOf(63) != -1 && string5 != null) {
                throw new MalformedURIException("Query string cannot be specified in path and query string!");
            }
            if (string4.indexOf(35) != -1 && string6 != null) {
                throw new MalformedURIException("Fragment cannot be specified in both the path and fragment!");
            }
        }
        this.setScheme(string);
        this.setHost(string3);
        this.setPort(n2);
        this.setUserinfo(string2);
        this.setPath(string4);
        this.setQueryString(string5);
        this.setFragment(string6);
    }

    private void initialize(URI uRI) {
        this.m_scheme = uRI.getScheme();
        this.m_userinfo = uRI.getUserinfo();
        this.m_host = uRI.getHost();
        this.m_port = uRI.getPort();
        this.m_regAuthority = uRI.getRegBasedAuthority();
        this.m_path = uRI.getPath();
        this.m_queryString = uRI.getQueryString();
        this.m_fragment = uRI.getFragment();
    }

    private void initialize(URI uRI, String string, boolean bl) throws MalformedURIException {
        int n2;
        int n3;
        int n4;
        String string2 = string;
        int n5 = n3 = string2 != null ? string2.length() : 0;
        if (uRI == null && n3 == 0) {
            if (bl) {
                this.m_path = "";
                return;
            }
            throw new MalformedURIException("Cannot initialize URI with empty parameters.");
        }
        if (n3 == 0) {
            this.initialize(uRI);
            return;
        }
        int n6 = 0;
        int n7 = string2.indexOf(58);
        if (n7 != -1) {
            n4 = n7 - 1;
            n2 = string2.lastIndexOf(47, n4);
            int n8 = string2.lastIndexOf(63, n4);
            int n9 = string2.lastIndexOf(35, n4);
            if (n7 == 0 || n2 != -1 || n8 != -1 || n9 != -1) {
                if (n7 == 0 || uRI == null && n9 != 0 && !bl) {
                    throw new MalformedURIException("No scheme found in URI.");
                }
            } else {
                this.initializeScheme(string2);
                n6 = this.m_scheme.length() + 1;
                if (n7 == n3 - 1 || string2.charAt(n7 + 1) == '#') {
                    throw new MalformedURIException("Scheme specific part cannot be empty.");
                }
            }
        } else if (uRI == null && string2.indexOf(35) != 0 && !bl) {
            throw new MalformedURIException("No scheme found in URI.");
        }
        if (n6 + 1 < n3 && string2.charAt(n6) == '/' && string2.charAt(n6 + 1) == '/') {
            n4 = n6 += 2;
            n2 = 0;
            while (n6 < n3) {
                n2 = string2.charAt(n6);
                if (n2 == 47 || n2 == 63 || n2 == 35) break;
                ++n6;
            }
            if (n6 > n4) {
                if (!this.initializeAuthority(string2.substring(n4, n6))) {
                    n6 = n4 - 2;
                }
            } else {
                this.m_host = "";
            }
        }
        this.initializePath(string2, n6);
        if (uRI != null) {
            this.absolutize(uRI);
        }
    }

    private void initialize(URI uRI, String string) throws MalformedURIException {
        int n2;
        int n3;
        int n4;
        String string2 = string;
        int n5 = n2 = string2 != null ? string2.length() : 0;
        if (uRI == null && n2 == 0) {
            throw new MalformedURIException("Cannot initialize URI with empty parameters.");
        }
        if (n2 == 0) {
            this.initialize(uRI);
            return;
        }
        int n6 = 0;
        int n7 = string2.indexOf(58);
        if (n7 != -1) {
            n3 = n7 - 1;
            n4 = string2.lastIndexOf(47, n3);
            int n8 = string2.lastIndexOf(63, n3);
            int n9 = string2.lastIndexOf(35, n3);
            if (n7 == 0 || n4 != -1 || n8 != -1 || n9 != -1) {
                if (n7 == 0 || uRI == null && n9 != 0) {
                    throw new MalformedURIException("No scheme found in URI.");
                }
            } else {
                this.initializeScheme(string2);
                n6 = this.m_scheme.length() + 1;
                if (n7 == n2 - 1 || string2.charAt(n7 + 1) == '#') {
                    throw new MalformedURIException("Scheme specific part cannot be empty.");
                }
            }
        } else if (uRI == null && string2.indexOf(35) != 0) {
            throw new MalformedURIException("No scheme found in URI.");
        }
        if (n6 + 1 < n2 && string2.charAt(n6) == '/' && string2.charAt(n6 + 1) == '/') {
            n3 = n6 += 2;
            n4 = 0;
            while (n6 < n2) {
                n4 = string2.charAt(n6);
                if (n4 == 47 || n4 == 63 || n4 == 35) break;
                ++n6;
            }
            if (n6 > n3) {
                if (!this.initializeAuthority(string2.substring(n3, n6))) {
                    n6 = n3 - 2;
                }
            } else {
                this.m_host = "";
            }
        }
        this.initializePath(string2, n6);
        if (uRI != null) {
            this.absolutize(uRI);
        }
    }

    public void absolutize(URI uRI) {
        int n2;
        if (this.m_path.length() == 0 && this.m_scheme == null && this.m_host == null && this.m_regAuthority == null) {
            this.m_scheme = uRI.getScheme();
            this.m_userinfo = uRI.getUserinfo();
            this.m_host = uRI.getHost();
            this.m_port = uRI.getPort();
            this.m_regAuthority = uRI.getRegBasedAuthority();
            this.m_path = uRI.getPath();
            if (this.m_queryString == null) {
                this.m_queryString = uRI.getQueryString();
                if (this.m_fragment == null) {
                    this.m_fragment = uRI.getFragment();
                }
            }
            return;
        }
        if (this.m_scheme != null) {
            return;
        }
        this.m_scheme = uRI.getScheme();
        if (this.m_host != null || this.m_regAuthority != null) {
            return;
        }
        this.m_userinfo = uRI.getUserinfo();
        this.m_host = uRI.getHost();
        this.m_port = uRI.getPort();
        this.m_regAuthority = uRI.getRegBasedAuthority();
        if (this.m_path.length() > 0 && this.m_path.startsWith("/")) {
            return;
        }
        String string = "";
        String string2 = uRI.getPath();
        if (string2 != null && string2.length() > 0) {
            n2 = string2.lastIndexOf(47);
            if (n2 != -1) {
                string = string2.substring(0, n2 + 1);
            }
        } else if (this.m_path.length() > 0) {
            string = "/";
        }
        string = string.concat(this.m_path);
        n2 = -1;
        while ((n2 = string.indexOf("/./")) != -1) {
            string = string.substring(0, n2 + 1).concat(string.substring(n2 + 3));
        }
        if (string.endsWith("/.")) {
            string = string.substring(0, string.length() - 1);
        }
        n2 = 1;
        int n3 = -1;
        String string3 = null;
        while ((n2 = string.indexOf("/../", n2)) > 0) {
            string3 = string.substring(0, string.indexOf("/../"));
            n3 = string3.lastIndexOf(47);
            if (n3 != -1) {
                if (!string3.substring(n3).equals("..")) {
                    string = string.substring(0, n3 + 1).concat(string.substring(n2 + 4));
                    n2 = n3;
                    continue;
                }
                n2 += 4;
                continue;
            }
            n2 += 4;
        }
        if (string.endsWith("/..") && (n3 = (string3 = string.substring(0, string.length() - 3)).lastIndexOf(47)) != -1) {
            string = string.substring(0, n3 + 1);
        }
        this.m_path = string;
    }

    private void initializeScheme(String string) throws MalformedURIException {
        int n2 = string.length();
        int n3 = 0;
        String string2 = null;
        char c2 = '\u0000';
        while (n3 < n2) {
            c2 = string.charAt(n3);
            if (c2 == ':' || c2 == '/' || c2 == '?' || c2 == '#') break;
            ++n3;
        }
        if ((string2 = string.substring(0, n3)).length() == 0) {
            throw new MalformedURIException("No scheme found in URI.");
        }
        this.setScheme(string2);
    }

    private boolean initializeAuthority(String string) {
        int n2;
        int n3 = 0;
        int n4 = 0;
        int n5 = string.length();
        char c2 = '\u0000';
        String string2 = null;
        if (string.indexOf(64, n4) != -1) {
            while (n3 < n5) {
                c2 = string.charAt(n3);
                if (c2 == '@') break;
                ++n3;
            }
            string2 = string.substring(n4, n3);
            ++n3;
        }
        String string3 = null;
        n4 = n3;
        boolean bl = false;
        if (n3 < n5) {
            if (string.charAt(n4) == '[') {
                n2 = string.indexOf(93, n4);
                int n6 = n3 = n2 != -1 ? n2 : n5;
                if (n3 + 1 < n5 && string.charAt(n3 + 1) == ':') {
                    ++n3;
                    bl = true;
                } else {
                    n3 = n5;
                }
            } else {
                n2 = string.lastIndexOf(58, n5);
                n3 = n2 > n4 ? n2 : n5;
                bl = n3 != n5;
            }
        }
        string3 = string.substring(n4, n3);
        n2 = -1;
        if (string3.length() > 0 && bl) {
            n4 = ++n3;
            while (n3 < n5) {
                ++n3;
            }
            String string4 = string.substring(n4, n3);
            if (string4.length() > 0) {
                try {
                    n2 = Integer.parseInt(string4);
                    if (n2 == -1) {
                        --n2;
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    n2 = -2;
                }
            }
        }
        if (this.isValidServerBasedAuthority(string3, n2, string2)) {
            this.m_host = string3;
            this.m_port = n2;
            this.m_userinfo = string2;
            return true;
        }
        if (this.isValidRegistryBasedAuthority(string)) {
            this.m_regAuthority = string;
            return true;
        }
        return false;
    }

    private boolean isValidServerBasedAuthority(String string, int n2, String string2) {
        if (!URI.isWellFormedAddress(string)) {
            return false;
        }
        if (n2 < -1 || n2 > 65535) {
            return false;
        }
        if (string2 != null) {
            int n3 = 0;
            int n4 = string2.length();
            char c2 = '\u0000';
            while (n3 < n4) {
                c2 = string2.charAt(n3);
                if (c2 == '%') {
                    if (n3 + 2 >= n4 || !URI.isHex(string2.charAt(n3 + 1)) || !URI.isHex(string2.charAt(n3 + 2))) {
                        return false;
                    }
                    n3 += 2;
                } else if (!URI.isUserinfoCharacter(c2)) {
                    return false;
                }
                ++n3;
            }
        }
        return true;
    }

    private boolean isValidRegistryBasedAuthority(String string) {
        int n2 = 0;
        int n3 = string.length();
        while (n2 < n3) {
            char c2 = string.charAt(n2);
            if (c2 == '%') {
                if (n2 + 2 >= n3 || !URI.isHex(string.charAt(n2 + 1)) || !URI.isHex(string.charAt(n2 + 2))) {
                    return false;
                }
                n2 += 2;
            } else if (!URI.isPathCharacter(c2)) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void initializePath(String var1_1, int var2_2) throws MalformedURIException {
        block22 : {
            if (var1_1 == null) {
                throw new MalformedURIException("Cannot initialize path from null string!");
            }
            var3_3 = var2_2;
            var4_4 = var2_2;
            var5_5 = var1_1.length();
            var6_6 = '\u0000';
            if (var4_4 >= var5_5) ** GOTO lbl37
            if (this.getScheme() != null && var1_1.charAt(var4_4) != '/') ** GOTO lbl36
            while (var3_3 < var5_5) {
                var6_6 = var1_1.charAt(var3_3);
                if (var6_6 == '%') {
                    if (var3_3 + 2 >= var5_5) throw new MalformedURIException("Path contains invalid escape sequence!");
                    if (URI.isHex(var1_1.charAt(var3_3 + 1)) == false) throw new MalformedURIException("Path contains invalid escape sequence!");
                    if (!URI.isHex(var1_1.charAt(var3_3 + 2))) {
                        throw new MalformedURIException("Path contains invalid escape sequence!");
                    }
                    var3_3 += 2;
                } else if (!URI.isPathCharacter(var6_6)) {
                    if (var6_6 == 63) break block22;
                    if (var6_6 != 35) throw new MalformedURIException("Path contains invalid character: " + var6_6);
                    break block22;
                }
                ++var3_3;
            }
            ** GOTO lbl37
            while ((var6_6 = (char)var1_1.charAt(var3_3)) != '?' && var6_6 != '#') {
                if (var6_6 == '%') {
                    if (var3_3 + 2 >= var5_5) throw new MalformedURIException("Opaque part contains invalid escape sequence!");
                    if (URI.isHex(var1_1.charAt(var3_3 + 1)) == false) throw new MalformedURIException("Opaque part contains invalid escape sequence!");
                    if (!URI.isHex(var1_1.charAt(var3_3 + 2))) {
                        throw new MalformedURIException("Opaque part contains invalid escape sequence!");
                    }
                    var3_3 += 2;
                } else if (!URI.isURICharacter(var6_6)) {
                    throw new MalformedURIException("Opaque part contains invalid character: " + var6_6);
                }
                ++var3_3;
lbl36: // 2 sources:
                if (var3_3 < var5_5) continue;
            }
        }
        this.m_path = var1_1.substring(var4_4, var3_3);
        if (var6_6 == '?') {
            var4_4 = ++var3_3;
            while (var3_3 < var5_5) {
                var6_6 = var1_1.charAt(var3_3);
                if (var6_6 == '#') break;
                if (var6_6 == '%') {
                    if (var3_3 + 2 >= var5_5) throw new MalformedURIException("Query string contains invalid escape sequence!");
                    if (URI.isHex(var1_1.charAt(var3_3 + 1)) == false) throw new MalformedURIException("Query string contains invalid escape sequence!");
                    if (!URI.isHex(var1_1.charAt(var3_3 + 2))) {
                        throw new MalformedURIException("Query string contains invalid escape sequence!");
                    }
                    var3_3 += 2;
                } else if (!URI.isURICharacter(var6_6)) {
                    throw new MalformedURIException("Query string contains invalid character: " + var6_6);
                }
                ++var3_3;
            }
            this.m_queryString = var1_1.substring(var4_4, var3_3);
        }
        if (var6_6 != 35) return;
        var4_4 = ++var3_3;
        while (var3_3 < var5_5) {
            var6_6 = var1_1.charAt(var3_3);
            if (var6_6 == '%') {
                if (var3_3 + 2 >= var5_5) throw new MalformedURIException("Fragment contains invalid escape sequence!");
                if (URI.isHex(var1_1.charAt(var3_3 + 1)) == false) throw new MalformedURIException("Fragment contains invalid escape sequence!");
                if (!URI.isHex(var1_1.charAt(var3_3 + 2))) {
                    throw new MalformedURIException("Fragment contains invalid escape sequence!");
                }
                var3_3 += 2;
            } else if (!URI.isURICharacter(var6_6)) {
                throw new MalformedURIException("Fragment contains invalid character: " + var6_6);
            }
            ++var3_3;
        }
        this.m_fragment = var1_1.substring(var4_4, var3_3);
    }

    public String getScheme() {
        return this.m_scheme;
    }

    public String getSchemeSpecificPart() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.m_host != null || this.m_regAuthority != null) {
            stringBuffer.append("//");
            if (this.m_host != null) {
                if (this.m_userinfo != null) {
                    stringBuffer.append(this.m_userinfo);
                    stringBuffer.append('@');
                }
                stringBuffer.append(this.m_host);
                if (this.m_port != -1) {
                    stringBuffer.append(':');
                    stringBuffer.append(this.m_port);
                }
            } else {
                stringBuffer.append(this.m_regAuthority);
            }
        }
        if (this.m_path != null) {
            stringBuffer.append(this.m_path);
        }
        if (this.m_queryString != null) {
            stringBuffer.append('?');
            stringBuffer.append(this.m_queryString);
        }
        if (this.m_fragment != null) {
            stringBuffer.append('#');
            stringBuffer.append(this.m_fragment);
        }
        return stringBuffer.toString();
    }

    public String getUserinfo() {
        return this.m_userinfo;
    }

    public String getHost() {
        return this.m_host;
    }

    public int getPort() {
        return this.m_port;
    }

    public String getRegBasedAuthority() {
        return this.m_regAuthority;
    }

    public String getAuthority() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.m_host != null || this.m_regAuthority != null) {
            stringBuffer.append("//");
            if (this.m_host != null) {
                if (this.m_userinfo != null) {
                    stringBuffer.append(this.m_userinfo);
                    stringBuffer.append('@');
                }
                stringBuffer.append(this.m_host);
                if (this.m_port != -1) {
                    stringBuffer.append(':');
                    stringBuffer.append(this.m_port);
                }
            } else {
                stringBuffer.append(this.m_regAuthority);
            }
        }
        return stringBuffer.toString();
    }

    public String getPath(boolean bl, boolean bl2) {
        StringBuffer stringBuffer = new StringBuffer(this.m_path);
        if (bl && this.m_queryString != null) {
            stringBuffer.append('?');
            stringBuffer.append(this.m_queryString);
        }
        if (bl2 && this.m_fragment != null) {
            stringBuffer.append('#');
            stringBuffer.append(this.m_fragment);
        }
        return stringBuffer.toString();
    }

    public String getPath() {
        return this.m_path;
    }

    public String getQueryString() {
        return this.m_queryString;
    }

    public String getFragment() {
        return this.m_fragment;
    }

    public void setScheme(String string) throws MalformedURIException {
        if (string == null) {
            throw new MalformedURIException("Cannot set scheme from null string!");
        }
        if (!URI.isConformantSchemeName(string)) {
            throw new MalformedURIException("The scheme is not conformant.");
        }
        this.m_scheme = string.toLowerCase(Locale.ENGLISH);
    }

    public void setUserinfo(String string) throws MalformedURIException {
        if (string == null) {
            this.m_userinfo = null;
            return;
        }
        if (this.m_host == null) {
            throw new MalformedURIException("Userinfo cannot be set when host is null!");
        }
        int n2 = 0;
        int n3 = string.length();
        char c2 = '\u0000';
        while (n2 < n3) {
            c2 = string.charAt(n2);
            if (c2 == '%') {
                if (n2 + 2 >= n3 || !URI.isHex(string.charAt(n2 + 1)) || !URI.isHex(string.charAt(n2 + 2))) {
                    throw new MalformedURIException("Userinfo contains invalid escape sequence!");
                }
            } else if (!URI.isUserinfoCharacter(c2)) {
                throw new MalformedURIException("Userinfo contains invalid character:" + c2);
            }
            ++n2;
        }
        this.m_userinfo = string;
    }

    public void setHost(String string) throws MalformedURIException {
        if (string == null || string.length() == 0) {
            if (string != null) {
                this.m_regAuthority = null;
            }
            this.m_host = string;
            this.m_userinfo = null;
            this.m_port = -1;
            return;
        }
        if (!URI.isWellFormedAddress(string)) {
            throw new MalformedURIException("Host is not a well formed address!");
        }
        this.m_host = string;
        this.m_regAuthority = null;
    }

    public void setPort(int n2) throws MalformedURIException {
        if (n2 >= 0 && n2 <= 65535) {
            if (this.m_host == null) {
                throw new MalformedURIException("Port cannot be set when host is null!");
            }
        } else if (n2 != -1) {
            throw new MalformedURIException("Invalid port number!");
        }
        this.m_port = n2;
    }

    public void setRegBasedAuthority(String string) throws MalformedURIException {
        if (string == null) {
            this.m_regAuthority = null;
            return;
        }
        if (string.length() < 1 || !this.isValidRegistryBasedAuthority(string) || string.indexOf(47) != -1) {
            throw new MalformedURIException("Registry based authority is not well formed.");
        }
        this.m_regAuthority = string;
        this.m_host = null;
        this.m_userinfo = null;
        this.m_port = -1;
    }

    public void setPath(String string) throws MalformedURIException {
        if (string == null) {
            this.m_path = null;
            this.m_queryString = null;
            this.m_fragment = null;
        } else {
            this.initializePath(string, 0);
        }
    }

    public void appendPath(String string) throws MalformedURIException {
        if (string == null || string.trim().length() == 0) {
            return;
        }
        if (!URI.isURIString(string)) {
            throw new MalformedURIException("Path contains invalid character!");
        }
        this.m_path = this.m_path == null || this.m_path.trim().length() == 0 ? (string.startsWith("/") ? string : "/" + string) : (this.m_path.endsWith("/") ? (string.startsWith("/") ? this.m_path.concat(string.substring(1)) : this.m_path.concat(string)) : (string.startsWith("/") ? this.m_path.concat(string) : this.m_path.concat("/" + string)));
    }

    public void setQueryString(String string) throws MalformedURIException {
        if (string == null) {
            this.m_queryString = null;
        } else {
            if (!this.isGenericURI()) {
                throw new MalformedURIException("Query string can only be set for a generic URI!");
            }
            if (this.getPath() == null) {
                throw new MalformedURIException("Query string cannot be set when path is null!");
            }
            if (!URI.isURIString(string)) {
                throw new MalformedURIException("Query string contains invalid character!");
            }
            this.m_queryString = string;
        }
    }

    public void setFragment(String string) throws MalformedURIException {
        if (string == null) {
            this.m_fragment = null;
        } else {
            if (!this.isGenericURI()) {
                throw new MalformedURIException("Fragment can only be set for a generic URI!");
            }
            if (this.getPath() == null) {
                throw new MalformedURIException("Fragment cannot be set when path is null!");
            }
            if (!URI.isURIString(string)) {
                throw new MalformedURIException("Fragment contains invalid character!");
            }
            this.m_fragment = string;
        }
    }

    public boolean equals(Object object) {
        if (object instanceof URI) {
            URI uRI = (URI)object;
            if ((this.m_scheme == null && uRI.m_scheme == null || this.m_scheme != null && uRI.m_scheme != null && this.m_scheme.equals(uRI.m_scheme)) && (this.m_userinfo == null && uRI.m_userinfo == null || this.m_userinfo != null && uRI.m_userinfo != null && this.m_userinfo.equals(uRI.m_userinfo)) && (this.m_host == null && uRI.m_host == null || this.m_host != null && uRI.m_host != null && this.m_host.equals(uRI.m_host)) && this.m_port == uRI.m_port && (this.m_path == null && uRI.m_path == null || this.m_path != null && uRI.m_path != null && this.m_path.equals(uRI.m_path)) && (this.m_queryString == null && uRI.m_queryString == null || this.m_queryString != null && uRI.m_queryString != null && this.m_queryString.equals(uRI.m_queryString)) && (this.m_fragment == null && uRI.m_fragment == null || this.m_fragment != null && uRI.m_fragment != null && this.m_fragment.equals(uRI.m_fragment))) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.m_scheme != null) {
            stringBuffer.append(this.m_scheme);
            stringBuffer.append(':');
        }
        stringBuffer.append(this.getSchemeSpecificPart());
        return stringBuffer.toString();
    }

    public boolean isGenericURI() {
        return this.m_host != null;
    }

    public boolean isAbsoluteURI() {
        return this.m_scheme != null;
    }

    public static boolean isConformantSchemeName(String string) {
        if (string == null || string.trim().length() == 0) {
            return false;
        }
        if (!URI.isAlpha(string.charAt(0))) {
            return false;
        }
        int n2 = string.length();
        int n3 = 1;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (!URI.isSchemeCharacter(c2)) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    public static boolean isWellFormedAddress(String string) {
        if (string == null) {
            return false;
        }
        int n2 = string.length();
        if (n2 == 0) {
            return false;
        }
        if (string.startsWith("[")) {
            return URI.isWellFormedIPv6Reference(string);
        }
        if (string.startsWith(".") || string.startsWith("-") || string.endsWith("-")) {
            return false;
        }
        int n3 = string.lastIndexOf(46);
        if (string.endsWith(".")) {
            n3 = string.substring(0, n3).lastIndexOf(46);
        }
        if (n3 + 1 < n2 && URI.isDigit(string.charAt(n3 + 1))) {
            return URI.isWellFormedIPv4Address(string);
        }
        if (n2 > 255) {
            return false;
        }
        int n4 = 0;
        int n5 = 0;
        while (n5 < n2) {
            char c2 = string.charAt(n5);
            if (c2 == '.') {
                if (!URI.isAlphanum(string.charAt(n5 - 1))) {
                    return false;
                }
                if (n5 + 1 < n2 && !URI.isAlphanum(string.charAt(n5 + 1))) {
                    return false;
                }
                n4 = 0;
            } else {
                if (!URI.isAlphanum(c2) && c2 != '-') {
                    return false;
                }
                if (++n4 > 63) {
                    return false;
                }
            }
            ++n5;
        }
        return true;
    }

    public static boolean isWellFormedIPv4Address(String string) {
        int n2 = string.length();
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        while (n5 < n2) {
            char c2 = string.charAt(n5);
            if (c2 == '.') {
                if (n5 > 0 && !URI.isDigit(string.charAt(n5 - 1)) || n5 + 1 < n2 && !URI.isDigit(string.charAt(n5 + 1))) {
                    return false;
                }
                n4 = 0;
                if (++n3 > 3) {
                    return false;
                }
            } else {
                if (!URI.isDigit(c2)) {
                    return false;
                }
                if (++n4 > 3) {
                    return false;
                }
                if (n4 == 3) {
                    char c3 = string.charAt(n5 - 2);
                    char c4 = string.charAt(n5 - 1);
                    if (c3 >= '2' && (c3 != '2' || c4 >= '5' && (c4 != '5' || c2 > '5'))) {
                        return false;
                    }
                }
            }
            ++n5;
        }
        return n3 == 3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isWellFormedIPv6Reference(String string) {
        int n2 = string.length();
        int n3 = 1;
        int n4 = n2 - 1;
        if (n2 <= 2) return false;
        if (string.charAt(0) != '[') return false;
        if (string.charAt(n4) != ']') {
            return false;
        }
        int[] arrn = new int[1];
        if ((n3 = URI.scanHexSequence(string, n3, n4, arrn)) == -1) {
            return false;
        }
        if (n3 == n4) {
            if (arrn[0] != 8) return false;
            return true;
        }
        if (n3 + 1 >= n4) return false;
        if (string.charAt(n3) != ':') return false;
        if (string.charAt(n3 + 1) == ':') {
            arrn[0] = arrn[0] + 1;
            if (arrn[0] > 8) {
                return false;
            }
            if ((n3 += 2) == n4) {
                return true;
            }
        } else {
            if (arrn[0] != 6) return false;
            if (!URI.isWellFormedIPv4Address(string.substring(n3 + 1, n4))) return false;
            return true;
        }
        int n5 = arrn[0];
        if ((n3 = URI.scanHexSequence(string, n3, n4, arrn)) == n4) return true;
        if (n3 == -1) return false;
        if (!URI.isWellFormedIPv4Address(string.substring(arrn[0] > n5 ? n3 + 1 : n3, n4))) return false;
        return true;
    }

    private static int scanHexSequence(String string, int n2, int n3, int[] arrn) {
        int n4 = 0;
        int n5 = n2;
        while (n2 < n3) {
            char c2 = string.charAt(n2);
            if (c2 == ':') {
                if (n4 > 0 && (arrn[0] = arrn[0] + 1) > 8) {
                    return -1;
                }
                if (n4 == 0 || n2 + 1 < n3 && string.charAt(n2 + 1) == ':') {
                    return n2;
                }
                n4 = 0;
            } else {
                if (!URI.isHex(c2)) {
                    if (c2 == '.' && n4 < 4 && n4 > 0 && arrn[0] <= 6) {
                        int n6 = n2 - n4 - 1;
                        return n6 >= n5 ? n6 : n6 + 1;
                    }
                    return -1;
                }
                if (++n4 > 4) {
                    return -1;
                }
            }
            ++n2;
        }
        return n4 > 0 && (arrn[0] = arrn[0] + 1) <= 8 ? n3 : -1;
    }

    private static boolean isDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    private static boolean isHex(char c2) {
        return c2 <= 'f' && (fgLookupTable[c2] & 64) != 0;
    }

    private static boolean isAlpha(char c2) {
        return c2 >= 'a' && c2 <= 'z' || c2 >= 'A' && c2 <= 'Z';
    }

    private static boolean isAlphanum(char c2) {
        return c2 <= 'z' && (fgLookupTable[c2] & 48) != 0;
    }

    private static boolean isReservedCharacter(char c2) {
        return c2 <= ']' && (fgLookupTable[c2] & 1) != 0;
    }

    private static boolean isUnreservedCharacter(char c2) {
        return c2 <= '~' && (fgLookupTable[c2] & 50) != 0;
    }

    private static boolean isURICharacter(char c2) {
        return c2 <= '~' && (fgLookupTable[c2] & 51) != 0;
    }

    private static boolean isSchemeCharacter(char c2) {
        return c2 <= 'z' && (fgLookupTable[c2] & 52) != 0;
    }

    private static boolean isUserinfoCharacter(char c2) {
        return c2 <= 'z' && (fgLookupTable[c2] & 58) != 0;
    }

    private static boolean isPathCharacter(char c2) {
        return c2 <= '~' && (fgLookupTable[c2] & 178) != 0;
    }

    private static boolean isURIString(String string) {
        if (string == null) {
            return false;
        }
        int n2 = string.length();
        char c2 = '\u0000';
        int n3 = 0;
        while (n3 < n2) {
            c2 = string.charAt(n3);
            if (c2 == '%') {
                if (n3 + 2 >= n2 || !URI.isHex(string.charAt(n3 + 1)) || !URI.isHex(string.charAt(n3 + 2))) {
                    return false;
                }
                n3 += 2;
            } else if (!URI.isURICharacter(c2)) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    static {
        int n2 = 48;
        while (n2 <= 57) {
            byte[] arrby = fgLookupTable;
            int n3 = n2++;
            arrby[n3] = (byte)(arrby[n3] | 96);
        }
        int n4 = 65;
        while (n4 <= 70) {
            byte[] arrby = fgLookupTable;
            int n5 = n4;
            arrby[n5] = (byte)(arrby[n5] | 80);
            byte[] arrby2 = fgLookupTable;
            int n6 = n4 + 32;
            arrby2[n6] = (byte)(arrby2[n6] | 80);
            ++n4;
        }
        int n7 = 71;
        while (n7 <= 90) {
            byte[] arrby = fgLookupTable;
            int n8 = n7;
            arrby[n8] = (byte)(arrby[n8] | 16);
            byte[] arrby3 = fgLookupTable;
            int n9 = n7 + 32;
            arrby3[n9] = (byte)(arrby3[n9] | 16);
            ++n7;
        }
        byte[] arrby = fgLookupTable;
        arrby[59] = (byte)(arrby[59] | 1);
        byte[] arrby4 = fgLookupTable;
        arrby4[47] = (byte)(arrby4[47] | 1);
        byte[] arrby5 = fgLookupTable;
        arrby5[63] = (byte)(arrby5[63] | 1);
        byte[] arrby6 = fgLookupTable;
        arrby6[58] = (byte)(arrby6[58] | 1);
        byte[] arrby7 = fgLookupTable;
        arrby7[64] = (byte)(arrby7[64] | 1);
        byte[] arrby8 = fgLookupTable;
        arrby8[38] = (byte)(arrby8[38] | 1);
        byte[] arrby9 = fgLookupTable;
        arrby9[61] = (byte)(arrby9[61] | 1);
        byte[] arrby10 = fgLookupTable;
        arrby10[43] = (byte)(arrby10[43] | 1);
        byte[] arrby11 = fgLookupTable;
        arrby11[36] = (byte)(arrby11[36] | 1);
        byte[] arrby12 = fgLookupTable;
        arrby12[44] = (byte)(arrby12[44] | 1);
        byte[] arrby13 = fgLookupTable;
        arrby13[91] = (byte)(arrby13[91] | 1);
        byte[] arrby14 = fgLookupTable;
        arrby14[93] = (byte)(arrby14[93] | 1);
        byte[] arrby15 = fgLookupTable;
        arrby15[45] = (byte)(arrby15[45] | 2);
        byte[] arrby16 = fgLookupTable;
        arrby16[95] = (byte)(arrby16[95] | 2);
        byte[] arrby17 = fgLookupTable;
        arrby17[46] = (byte)(arrby17[46] | 2);
        byte[] arrby18 = fgLookupTable;
        arrby18[33] = (byte)(arrby18[33] | 2);
        byte[] arrby19 = fgLookupTable;
        arrby19[126] = (byte)(arrby19[126] | 2);
        byte[] arrby20 = fgLookupTable;
        arrby20[42] = (byte)(arrby20[42] | 2);
        byte[] arrby21 = fgLookupTable;
        arrby21[39] = (byte)(arrby21[39] | 2);
        byte[] arrby22 = fgLookupTable;
        arrby22[40] = (byte)(arrby22[40] | 2);
        byte[] arrby23 = fgLookupTable;
        arrby23[41] = (byte)(arrby23[41] | 2);
        byte[] arrby24 = fgLookupTable;
        arrby24[43] = (byte)(arrby24[43] | 4);
        byte[] arrby25 = fgLookupTable;
        arrby25[45] = (byte)(arrby25[45] | 4);
        byte[] arrby26 = fgLookupTable;
        arrby26[46] = (byte)(arrby26[46] | 4);
        byte[] arrby27 = fgLookupTable;
        arrby27[59] = (byte)(arrby27[59] | 8);
        byte[] arrby28 = fgLookupTable;
        arrby28[58] = (byte)(arrby28[58] | 8);
        byte[] arrby29 = fgLookupTable;
        arrby29[38] = (byte)(arrby29[38] | 8);
        byte[] arrby30 = fgLookupTable;
        arrby30[61] = (byte)(arrby30[61] | 8);
        byte[] arrby31 = fgLookupTable;
        arrby31[43] = (byte)(arrby31[43] | 8);
        byte[] arrby32 = fgLookupTable;
        arrby32[36] = (byte)(arrby32[36] | 8);
        byte[] arrby33 = fgLookupTable;
        arrby33[44] = (byte)(arrby33[44] | 8);
        byte[] arrby34 = fgLookupTable;
        arrby34[59] = (byte)(arrby34[59] | 128);
        byte[] arrby35 = fgLookupTable;
        arrby35[47] = (byte)(arrby35[47] | 128);
        byte[] arrby36 = fgLookupTable;
        arrby36[58] = (byte)(arrby36[58] | 128);
        byte[] arrby37 = fgLookupTable;
        arrby37[64] = (byte)(arrby37[64] | 128);
        byte[] arrby38 = fgLookupTable;
        arrby38[38] = (byte)(arrby38[38] | 128);
        byte[] arrby39 = fgLookupTable;
        arrby39[61] = (byte)(arrby39[61] | 128);
        byte[] arrby40 = fgLookupTable;
        arrby40[43] = (byte)(arrby40[43] | 128);
        byte[] arrby41 = fgLookupTable;
        arrby41[36] = (byte)(arrby41[36] | 128);
        byte[] arrby42 = fgLookupTable;
        arrby42[44] = (byte)(arrby42[44] | 128);
        DEBUG = false;
    }

    public static class MalformedURIException
    extends IOException {
        static final long serialVersionUID = -6695054834342951930L;

        public MalformedURIException() {
        }

        public MalformedURIException(String string) {
            super(string);
        }
    }

}

