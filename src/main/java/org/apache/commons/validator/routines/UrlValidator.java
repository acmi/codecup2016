/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.validator.routines;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.RegexValidator;

public class UrlValidator
implements Serializable {
    private static final Pattern URL_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
    private static final Pattern SCHEME_PATTERN = Pattern.compile("^\\p{Alpha}[\\p{Alnum}\\+\\-\\.]*");
    private static final Pattern AUTHORITY_PATTERN = Pattern.compile("(?:\\[([0-9a-fA-F:]+)\\]|(?:(?:[a-zA-Z0-9%-._~!$&'()*+,;=]+:[a-zA-Z0-9%-._~!$&'()*+,;=]*@)?([\\p{Alnum}\\-\\.]*)))(:\\d*)?(.*)?");
    private static final Pattern PATH_PATTERN = Pattern.compile("^(/[-\\w:@&?=+,.!/~*'%$_;\\(\\)]*)?$");
    private static final Pattern QUERY_PATTERN = Pattern.compile("^(.*)$");
    private final long options;
    private final Set<String> allowedSchemes;
    private final RegexValidator authorityValidator;
    private static final String[] DEFAULT_SCHEMES = new String[]{"http", "https", "ftp"};
    private static final UrlValidator DEFAULT_URL_VALIDATOR = new UrlValidator();

    public UrlValidator() {
        this(null);
    }

    public UrlValidator(String[] arrstring) {
        this(arrstring, 0);
    }

    public UrlValidator(String[] arrstring, long l2) {
        this(arrstring, null, l2);
    }

    public UrlValidator(String[] arrstring, RegexValidator regexValidator, long l2) {
        this.options = l2;
        if (this.isOn(1)) {
            this.allowedSchemes = Collections.emptySet();
        } else {
            if (arrstring == null) {
                arrstring = DEFAULT_SCHEMES;
            }
            this.allowedSchemes = new HashSet<String>(arrstring.length);
            for (int i2 = 0; i2 < arrstring.length; ++i2) {
                this.allowedSchemes.add(arrstring[i2].toLowerCase(Locale.ENGLISH));
            }
        }
        this.authorityValidator = regexValidator;
    }

    public boolean isValid(String string) {
        if (string == null) {
            return false;
        }
        Matcher matcher = URL_PATTERN.matcher(string);
        if (!matcher.matches()) {
            return false;
        }
        String string2 = matcher.group(2);
        if (!this.isValidScheme(string2)) {
            return false;
        }
        String string3 = matcher.group(4);
        if ("file".equals(string2) ? !"".equals(string3) && string3.contains(":") : !this.isValidAuthority(string3)) {
            return false;
        }
        if (!this.isValidPath(matcher.group(5))) {
            return false;
        }
        if (!this.isValidQuery(matcher.group(7))) {
            return false;
        }
        if (!this.isValidFragment(matcher.group(9))) {
            return false;
        }
        return true;
    }

    protected boolean isValidScheme(String string) {
        if (string == null) {
            return false;
        }
        if (!SCHEME_PATTERN.matcher(string).matches()) {
            return false;
        }
        if (this.isOff(1) && !this.allowedSchemes.contains(string.toLowerCase(Locale.ENGLISH))) {
            return false;
        }
        return true;
    }

    protected boolean isValidAuthority(String string) {
        Object object;
        if (string == null) {
            return false;
        }
        if (this.authorityValidator != null && this.authorityValidator.isValid(string)) {
            return true;
        }
        String string2 = DomainValidator.unicodeToASCII(string);
        Matcher matcher = AUTHORITY_PATTERN.matcher(string2);
        if (!matcher.matches()) {
            return false;
        }
        String string3 = matcher.group(1);
        if (string3 != null) {
            object = InetAddressValidator.getInstance();
            if (!object.isValidInet6Address(string3)) {
                return false;
            }
        } else {
            InetAddressValidator inetAddressValidator;
            object = matcher.group(2);
            DomainValidator domainValidator = DomainValidator.getInstance(this.isOn(8));
            if (!domainValidator.isValid((String)object) && !(inetAddressValidator = InetAddressValidator.getInstance()).isValidInet4Address((String)object)) {
                return false;
            }
        }
        if ((object = matcher.group(4)) != null && object.trim().length() > 0) {
            return false;
        }
        return true;
    }

    protected boolean isValidPath(String string) {
        if (string == null) {
            return false;
        }
        if (!PATH_PATTERN.matcher(string).matches()) {
            return false;
        }
        try {
            URI uRI = new URI(null, null, string, null);
            String string2 = uRI.normalize().getPath();
            if (string2.startsWith("/../") || string2.equals("/..")) {
                return false;
            }
        }
        catch (URISyntaxException uRISyntaxException) {
            return false;
        }
        int n2 = this.countToken("//", string);
        if (this.isOff(2) && n2 > 0) {
            return false;
        }
        return true;
    }

    protected boolean isValidQuery(String string) {
        if (string == null) {
            return true;
        }
        return QUERY_PATTERN.matcher(string).matches();
    }

    protected boolean isValidFragment(String string) {
        if (string == null) {
            return true;
        }
        return this.isOff(4);
    }

    protected int countToken(String string, String string2) {
        int n2 = 0;
        int n3 = 0;
        while (n2 != -1) {
            if ((n2 = string2.indexOf(string, n2)) <= -1) continue;
            ++n2;
            ++n3;
        }
        return n3;
    }

    private boolean isOn(long l2) {
        return (this.options & l2) > 0;
    }

    private boolean isOff(long l2) {
        return (this.options & l2) == 0;
    }
}

