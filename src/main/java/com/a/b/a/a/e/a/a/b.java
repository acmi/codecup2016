/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.e.a.a;

import com.codeforces.commons.text.StringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class b {
    private static final Pattern a = Pattern.compile("(%[a-zA-Z_1-90]+%)");
    private final String b;
    private final List<String> c;
    private final Set<String> d;

    public b(String string, List<String> list, Set<String> set) {
        this.b = string;
        this.c = new ArrayList<String>(list);
        this.d = new HashSet<String>(set);
    }

    public String a(String string, Map<String, String> map) {
        String string2 = this.b;
        Matcher matcher = a.matcher(this.b);
        while (matcher.find()) {
            boolean bl = false;
            int n2 = matcher.groupCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                String string3 = matcher.group(i2);
                String string4 = System.getenv(string3.substring(1, string3.length() - 1));
                if (string4 == null) continue;
                string2 = StringUtil.replace(string2, string3, string4);
                bl = true;
            }
            if (bl) continue;
            break;
        }
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                string2 = StringUtil.replace(string2, "${" + entry.getKey() + '}', entry.getValue());
            }
        }
        return String.format(string2, string);
    }

    public List<String> a() {
        return Collections.unmodifiableList(this.c);
    }

    public boolean a(String string) {
        return this.d.contains(string);
    }
}

