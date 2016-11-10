/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.IOException;
import java.io.Serializable;
import org.apache.xml.res.XMLMessages;

public class URI
implements Serializable {
    private String m_scheme = null;
    private String m_userinfo = null;
    private String m_host = null;
    private int m_port = -1;
    private String m_path = null;
    private String m_queryString = null;
    private String m_fragment = null;
    private static boolean DEBUG = false;

    public URI() {
    }

    public URI(String string) throws MalformedURIException {
        this(null, string);
    }

    public URI(URI uRI, String string) throws MalformedURIException {
        this.initialize(uRI, string);
    }

    private void initialize(URI uRI) {
        this.m_scheme = uRI.getScheme();
        this.m_userinfo = uRI.getUserinfo();
        this.m_host = uRI.getHost();
        this.m_port = uRI.getPort();
        this.m_path = uRI.getPath();
        this.m_queryString = uRI.getQueryString();
        this.m_fragment = uRI.getFragment();
    }

    private void initialize(URI uRI, String string) throws MalformedURIException {
        if (uRI == null && (string == null || string.trim().length() == 0)) {
            throw new MalformedURIException(XMLMessages.createXMLMessage("ER_CANNOT_INIT_URI_EMPTY_PARMS", null));
        }
        if (string == null || string.trim().length() == 0) {
            this.initialize(uRI);
            return;
        }
        String string2 = string.trim();
        int n2 = string2.length();
        int n3 = 0;
        int n4 = string2.indexOf(58);
        if (n4 < 0) {
            if (uRI == null) {
                throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_SCHEME_IN_URI", new Object[]{string2}));
            }
        } else {
            this.initializeScheme(string2);
            string2 = string2.substring(n4 + 1);
            if (!(this.m_scheme == null || uRI == null || !string2.startsWith("/") && this.m_scheme.equals(uRI.m_scheme) && uRI.getSchemeSpecificPart().startsWith("/"))) {
                uRI = null;
            }
            n2 = string2.length();
        }
        if (string2.startsWith("//")) {
            int n5 = n3 += 2;
            char c2 = '\u0000';
            while (n3 < n2 && (c2 = string2.charAt(n3)) != '/' && c2 != '?' && c2 != '#') {
                ++n3;
            }
            if (n3 > n5) {
                this.initializeAuthority(string2.substring(n5, n3));
            } else {
                this.m_host = "";
            }
        }
        this.initializePath(string2.substring(n3));
        if (uRI != null) {
            int n6;
            if (this.m_path.length() == 0 && this.m_scheme == null && this.m_host == null) {
                this.m_scheme = uRI.getScheme();
                this.m_userinfo = uRI.getUserinfo();
                this.m_host = uRI.getHost();
                this.m_port = uRI.getPort();
                this.m_path = uRI.getPath();
                if (this.m_queryString == null) {
                    this.m_queryString = uRI.getQueryString();
                }
                return;
            }
            if (this.m_scheme == null) {
                this.m_scheme = uRI.getScheme();
            }
            if (this.m_host != null) {
                return;
            }
            this.m_userinfo = uRI.getUserinfo();
            this.m_host = uRI.getHost();
            this.m_port = uRI.getPort();
            if (this.m_path.length() > 0 && this.m_path.startsWith("/")) {
                return;
            }
            String string3 = new String();
            String string4 = uRI.getPath();
            if (string4 != null && (n6 = string4.lastIndexOf(47)) != -1) {
                string3 = string4.substring(0, n6 + 1);
            }
            string3 = string3.concat(this.m_path);
            n3 = -1;
            while ((n3 = string3.indexOf("/./")) != -1) {
                string3 = string3.substring(0, n3 + 1).concat(string3.substring(n3 + 3));
            }
            if (string3.endsWith("/.")) {
                string3 = string3.substring(0, string3.length() - 1);
            }
            n3 = -1;
            n6 = -1;
            String string5 = null;
            while ((n3 = string3.indexOf("/../")) > 0) {
                string5 = string3.substring(0, string3.indexOf("/../"));
                n6 = string5.lastIndexOf(47);
                if (n6 == -1 || string5.substring(n6++).equals("..")) continue;
                string3 = string3.substring(0, n6).concat(string3.substring(n3 + 4));
            }
            if (string3.endsWith("/..") && (n6 = (string5 = string3.substring(0, string3.length() - 3)).lastIndexOf(47)) != -1) {
                string3 = string3.substring(0, n6 + 1);
            }
            this.m_path = string3;
        }
    }

    private void initializeScheme(String string) throws MalformedURIException {
        int n2;
        int n3 = string.length();
        String string2 = null;
        char c2 = '\u0000';
        for (n2 = 0; n2 < n3 && (c2 = string.charAt(n2)) != ':' && c2 != '/' && c2 != '?' && c2 != '#'; ++n2) {
        }
        string2 = string.substring(0, n2);
        if (string2.length() == 0) {
            throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_SCHEME_INURI", null));
        }
        this.setScheme(string2);
    }

    private void initializeAuthority(String string) throws MalformedURIException {
        int n2;
        int n3 = 0;
        int n4 = string.length();
        char c2 = '\u0000';
        String string2 = null;
        if (string.indexOf(64, n3) != -1) {
            for (n2 = 0; n2 < n4 && (c2 = string.charAt(n2)) != '@'; ++n2) {
            }
            string2 = string.substring(n3, n2);
            ++n2;
        }
        String string3 = null;
        n3 = n2;
        while (n2 < n4 && (c2 = string.charAt(n2)) != ':') {
            ++n2;
        }
        string3 = string.substring(n3, n2);
        int n5 = -1;
        if (string3.length() > 0 && c2 == ':') {
            n3 = ++n2;
            while (n2 < n4) {
                ++n2;
            }
            String string4 = string.substring(n3, n2);
            if (string4.length() > 0) {
                for (int i2 = 0; i2 < string4.length(); ++i2) {
                    if (URI.isDigit(string4.charAt(i2))) continue;
                    throw new MalformedURIException(string4 + " is invalid. Port should only contain digits!");
                }
                try {
                    n5 = Integer.parseInt(string4);
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
        }
        this.setHost(string3);
        this.setPort(n5);
        this.setUserinfo(string2);
    }

    private void initializePath(String string) throws MalformedURIException {
        int n2;
        if (string == null) {
            throw new MalformedURIException("Cannot initialize path from null string!");
        }
        int n3 = 0;
        int n4 = string.length();
        char c2 = '\u0000';
        for (n2 = 0; n2 < n4 && (c2 = string.charAt(n2)) != '?' && c2 != '#'; ++n2) {
            if (c2 == '%') {
                if (n2 + 2 < n4 && URI.isHex(string.charAt(n2 + 1)) && URI.isHex(string.charAt(n2 + 2))) continue;
                throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", null));
            }
            if (URI.isReservedCharacter(c2) || URI.isUnreservedCharacter(c2) || '\\' == c2) continue;
            throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PATH_INVALID_CHAR", new Object[]{String.valueOf(c2)}));
        }
        this.m_path = string.substring(n3, n2);
        if (c2 == '?') {
            n3 = ++n2;
            while (n2 < n4 && (c2 = string.charAt(n2)) != '#') {
                if (c2 == '%') {
                    if (n2 + 2 >= n4 || !URI.isHex(string.charAt(n2 + 1)) || !URI.isHex(string.charAt(n2 + 2))) {
                        throw new MalformedURIException("Query string contains invalid escape sequence!");
                    }
                } else if (!URI.isReservedCharacter(c2) && !URI.isUnreservedCharacter(c2)) {
                    throw new MalformedURIException("Query string contains invalid character:" + c2);
                }
                ++n2;
            }
            this.m_queryString = string.substring(n3, n2);
        }
        if (c2 == '#') {
            n3 = ++n2;
            while (n2 < n4) {
                c2 = string.charAt(n2);
                if (c2 == '%') {
                    if (n2 + 2 >= n4 || !URI.isHex(string.charAt(n2 + 1)) || !URI.isHex(string.charAt(n2 + 2))) {
                        throw new MalformedURIException("Fragment contains invalid escape sequence!");
                    }
                } else if (!URI.isReservedCharacter(c2) && !URI.isUnreservedCharacter(c2)) {
                    throw new MalformedURIException("Fragment contains invalid character:" + c2);
                }
                ++n2;
            }
            this.m_fragment = string.substring(n3, n2);
        }
    }

    public String getScheme() {
        return this.m_scheme;
    }

    public String getSchemeSpecificPart() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.m_userinfo != null || this.m_host != null || this.m_port != -1) {
            stringBuffer.append("//");
        }
        if (this.m_userinfo != null) {
            stringBuffer.append(this.m_userinfo);
            stringBuffer.append('@');
        }
        if (this.m_host != null) {
            stringBuffer.append(this.m_host);
        }
        if (this.m_port != -1) {
            stringBuffer.append(':');
            stringBuffer.append(this.m_port);
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
            throw new MalformedURIException(XMLMessages.createXMLMessage("ER_SCHEME_FROM_NULL_STRING", null));
        }
        if (!URI.isConformantSchemeName(string)) {
            throw new MalformedURIException(XMLMessages.createXMLMessage("ER_SCHEME_NOT_CONFORMANT", null));
        }
        this.m_scheme = string.toLowerCase();
    }

    public void setUserinfo(String string) throws MalformedURIException {
        if (string == null) {
            this.m_userinfo = null;
        } else {
            if (this.m_host == null) {
                throw new MalformedURIException("Userinfo cannot be set when host is null!");
            }
            int n2 = string.length();
            char c2 = '\u0000';
            for (int i2 = 0; i2 < n2; ++i2) {
                c2 = string.charAt(i2);
                if (c2 == '%') {
                    if (i2 + 2 < n2 && URI.isHex(string.charAt(i2 + 1)) && URI.isHex(string.charAt(i2 + 2))) continue;
                    throw new MalformedURIException("Userinfo contains invalid escape sequence!");
                }
                if (URI.isUnreservedCharacter(c2) || ";:&=+$,".indexOf(c2) != -1) continue;
                throw new MalformedURIException("Userinfo contains invalid character:" + c2);
            }
        }
        this.m_userinfo = string;
    }

    public void setHost(String string) throws MalformedURIException {
        if (string == null || string.trim().length() == 0) {
            this.m_host = string;
            this.m_userinfo = null;
            this.m_port = -1;
        } else if (!URI.isWellFormedAddress(string)) {
            throw new MalformedURIException(XMLMessages.createXMLMessage("ER_HOST_ADDRESS_NOT_WELLFORMED", null));
        }
        this.m_host = string;
    }

    public void setPort(int n2) throws MalformedURIException {
        if (n2 >= 0 && n2 <= 65535) {
            if (this.m_host == null) {
                throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PORT_WHEN_HOST_NULL", null));
            }
        } else if (n2 != -1) {
            throw new MalformedURIException(XMLMessages.createXMLMessage("ER_INVALID_PORT", null));
        }
        this.m_port = n2;
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

    public static boolean isConformantSchemeName(String string) {
        if (string == null || string.trim().length() == 0) {
            return false;
        }
        if (!URI.isAlpha(string.charAt(0))) {
            return false;
        }
        for (int i2 = 1; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (URI.isAlphanum(c2) || "+-.".indexOf(c2) != -1) continue;
            return false;
        }
        return true;
    }

    public static boolean isWellFormedAddress(String string) {
        if (string == null) {
            return false;
        }
        String string2 = string.trim();
        int n2 = string2.length();
        if (n2 == 0 || n2 > 255) {
            return false;
        }
        if (string2.startsWith(".") || string2.startsWith("-")) {
            return false;
        }
        int n3 = string2.lastIndexOf(46);
        if (string2.endsWith(".")) {
            n3 = string2.substring(0, n3).lastIndexOf(46);
        }
        if (n3 + 1 < n2 && URI.isDigit(string.charAt(n3 + 1))) {
            int n4 = 0;
            for (int i2 = 0; i2 < n2; ++i2) {
                char c2 = string2.charAt(i2);
                if (c2 == '.') {
                    if (!URI.isDigit(string2.charAt(i2 - 1)) || i2 + 1 < n2 && !URI.isDigit(string2.charAt(i2 + 1))) {
                        return false;
                    }
                    ++n4;
                    continue;
                }
                if (URI.isDigit(c2)) continue;
                return false;
            }
            if (n4 != 3) {
                return false;
            }
        } else {
            for (int i3 = 0; i3 < n2; ++i3) {
                char c3 = string2.charAt(i3);
                if (c3 == '.') {
                    if (!URI.isAlphanum(string2.charAt(i3 - 1))) {
                        return false;
                    }
                    if (i3 + 1 >= n2 || URI.isAlphanum(string2.charAt(i3 + 1))) continue;
                    return false;
                }
                if (URI.isAlphanum(c3) || c3 == '-') continue;
                return false;
            }
        }
        return true;
    }

    private static boolean isDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    private static boolean isHex(char c2) {
        return URI.isDigit(c2) || c2 >= 'a' && c2 <= 'f' || c2 >= 'A' && c2 <= 'F';
    }

    private static boolean isAlpha(char c2) {
        return c2 >= 'a' && c2 <= 'z' || c2 >= 'A' && c2 <= 'Z';
    }

    private static boolean isAlphanum(char c2) {
        return URI.isAlpha(c2) || URI.isDigit(c2);
    }

    private static boolean isReservedCharacter(char c2) {
        return ";/?:@&=+$,".indexOf(c2) != -1;
    }

    private static boolean isUnreservedCharacter(char c2) {
        return URI.isAlphanum(c2) || "-_.!~*'() ".indexOf(c2) != -1;
    }

    public static class MalformedURIException
    extends IOException {
        public MalformedURIException() {
        }

        public MalformedURIException(String string) {
            super(string);
        }
    }

}

