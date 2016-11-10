/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;
import org.apache.xalan.xsltc.CollatorFactory;

public class CollatorFactoryBase
implements CollatorFactory {
    public static final Locale DEFAULT_LOCALE = Locale.getDefault();
    public static final Collator DEFAULT_COLLATOR = Collator.getInstance();

    public Collator getCollator(String string, String string2) {
        return Collator.getInstance(new Locale(string, string2));
    }

    public Collator getCollator(Locale locale) {
        if (locale == DEFAULT_LOCALE) {
            return DEFAULT_COLLATOR;
        }
        return Collator.getInstance(locale);
    }
}

