/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.exception.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.math3.exception.util.ArgUtils;
import org.apache.commons.math3.exception.util.Localizable;

public class ExceptionContext
implements Serializable {
    private Throwable throwable;
    private List<Localizable> msgPatterns;
    private List<Object[]> msgArguments;
    private Map<String, Object> context;

    public ExceptionContext(Throwable throwable) {
        this.throwable = throwable;
        this.msgPatterns = new ArrayList<Localizable>();
        this.msgArguments = new ArrayList<Object[]>();
        this.context = new HashMap<String, Object>();
    }

    public /* varargs */ void addMessage(Localizable localizable, Object ... arrobject) {
        this.msgPatterns.add(localizable);
        this.msgArguments.add(ArgUtils.flatten(arrobject));
    }

    public String getMessage() {
        return this.getMessage(Locale.US);
    }

    public String getLocalizedMessage() {
        return this.getMessage(Locale.getDefault());
    }

    public String getMessage(Locale locale) {
        return this.buildMessage(locale, ": ");
    }

    private String buildMessage(Locale locale, String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        int n3 = this.msgPatterns.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            Localizable localizable = this.msgPatterns.get(i2);
            Object[] arrobject = this.msgArguments.get(i2);
            MessageFormat messageFormat = new MessageFormat(localizable.getLocalizedString(locale), locale);
            stringBuilder.append(messageFormat.format(arrobject));
            if (++n2 >= n3) continue;
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}

