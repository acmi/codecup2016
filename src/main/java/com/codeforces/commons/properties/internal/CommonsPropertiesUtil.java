/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.properties.internal;

import com.codeforces.commons.properties.PropertiesUtil;
import java.util.List;

public class CommonsPropertiesUtil {
    private static final String[] RESOURCE_NAMES = new String[]{"/com/codeforces/commons/properties/commons.properties", "/com/codeforces/commons/properties/commons_default.properties"};

    public static String getProperty(String string, String string2) {
        return PropertiesUtil.getPropertyQuietly(string, string2, RESOURCE_NAMES);
    }

    public static List<String> getListProperty(String string, String string2) {
        return PropertiesUtil.getListPropertyQuietly(string, string2, RESOURCE_NAMES);
    }

    public static List<String> getSecurePasswords() {
        return SECURE_PASSWORDS;
    }

    public static List<String> getSecureHosts() {
        return SECURE_HOSTS;
    }

    public static boolean isBypassCertificateCheck() {
        return BYPASS_CERTIFICATE_CHECK;
    }

    public static List<String> getPrivateParameters() {
        return PRIVATE_PARAMETERS;
    }

    private static final class PropertyValuesHolder {
        private static final String TEMP_DIR_NAME = CommonsPropertiesUtil.getProperty("temp-dir.name", "temp");
        private static final List<String> SECURE_PASSWORDS = CommonsPropertiesUtil.getListProperty("security.secure-passwords", "");
        private static final List<String> SECURE_HOSTS = CommonsPropertiesUtil.getListProperty("security.secure-hosts", "");
        private static final boolean BYPASS_CERTIFICATE_CHECK = Boolean.parseBoolean(CommonsPropertiesUtil.getProperty("security.secure-hosts.bypass-certificate-check", "false"));
        private static final List<String> PRIVATE_PARAMETERS = CommonsPropertiesUtil.getListProperty("security.private-parameters", "");
        private static final String SUBSCRIPTION_TOKEN = CommonsPropertiesUtil.getProperty("security.subscription-token", "secret");
    }

}

