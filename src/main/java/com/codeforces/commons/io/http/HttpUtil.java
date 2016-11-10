/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io.http;

import com.codeforces.commons.io.http.HttpMethod;
import com.codeforces.commons.io.http.HttpRequest;
import com.codeforces.commons.io.http.HttpResponse;

public final class HttpUtil {
    public static /* varargs */ HttpRequest newRequest(String string, Object ... arrobject) {
        return HttpRequest.create(string, arrobject);
    }

    public static /* varargs */ HttpResponse executePostRequestAndReturnResponse(int n2, String string, Object ... arrobject) {
        return HttpUtil.newRequest(string, arrobject).setTimeoutMillis(n2).setMethod(HttpMethod.POST).executeAndReturnResponse();
    }
}

