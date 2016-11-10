/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a.a;

import com.a.b.a.a.e.a.a.b;
import com.codeforces.commons.text.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class g {
    private static final Pattern a = Pattern.compile(";");
    private static final ConcurrentMap<String, b> b = new ConcurrentHashMap<String, b>();

    private g() {
        throw new UnsupportedOperationException();
    }

    public static b a(String string) {
        if (StringUtil.isNotBlank(string)) {
            if (string.endsWith(".zip")) {
                string = string.substring(0, string.length() - ".zip".length());
            }
            for (Map.Entry<String, b> entry : b.entrySet()) {
                if (!string.endsWith("" + '.' + entry.getKey()) || entry.getValue() == null) continue;
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException(String.format("Can't find run script for '%s'.", string));
    }

    private static void a() {
        String[] arrstring;
        String string = "/remote-process.properties";
        Properties properties = new Properties();
        try {
            properties.load(g.class.getResourceAsStream(string));
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException(String.format("Can't read property file '%s'.", string), iOException);
        }
        for (String string2 : arrstring = a.split(properties.getProperty("remote-process.supported-postfixes"))) {
            String string3 = "remote-process.postfix-command-line." + string2;
            String string4 = properties.getProperty(string3);
            if (StringUtils.isBlank(string4)) {
                throw new IllegalArgumentException(String.format("Expected property '%s' in '%s'.", string3, string));
            }
            String string5 = string4.trim();
            String string6 = "remote-process.resources-to-copy." + string2;
            String string7 = properties.getProperty(string6);
            ArrayList<String> arrayList = new ArrayList<String>();
            if (!StringUtils.isBlank(string7)) {
                for (String string8 : a.split(string7)) {
                    arrayList.add(string8.trim());
                }
            }
            String string8 = "remote-process.resources-to-filter." + string2;
            String string9 = properties.getProperty(string8);
            HashSet<String> hashSet = new HashSet<String>();
            if (!StringUtils.isBlank(string9)) {
                for (String string10 : a.split(string9)) {
                    hashSet.add(string10.trim());
                }
            }
            b.put(string2, new b(string5, arrayList, hashSet));
        }
    }

    static {
        g.a();
    }
}

