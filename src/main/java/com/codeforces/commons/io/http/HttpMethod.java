/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io.http;

public final class HttpMethod
extends Enum<HttpMethod> {
    public static final /* enum */ HttpMethod GET = new HttpMethod();
    public static final /* enum */ HttpMethod POST = new HttpMethod();
    private static final /* synthetic */ HttpMethod[] $VALUES;

    public static HttpMethod[] values() {
        return (HttpMethod[])$VALUES.clone();
    }

    private HttpMethod() {
        super(string, n2);
    }

    static {
        $VALUES = new HttpMethod[]{GET, POST};
    }
}

