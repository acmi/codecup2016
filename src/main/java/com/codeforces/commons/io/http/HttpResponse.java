/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io.http;

import com.codeforces.commons.io.http.HttpRequest;
import com.codeforces.commons.text.StringUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public final class HttpResponse {
    private final int code;
    private final byte[] bytes;
    private final Map<String, List<String>> headersByName;
    private final IOException ioException;

    HttpResponse(int n2, byte[] arrby, Map<String, List<String>> map, IOException iOException) {
        if (n2 == -1 == (iOException == null)) {
            throw new IllegalArgumentException("Argument 'ioException' should be set if and only if argument 'code' is -1.");
        }
        this.code = n2;
        this.bytes = arrby;
        this.headersByName = map == null || map.isEmpty() ? null : HttpRequest.getDeepUnmodifiableMap(map);
        this.ioException = iOException;
    }

    public int getCode() {
        return this.code;
    }

    public IOException getIoException() {
        return this.ioException;
    }

    public boolean hasIoException() {
        return this.ioException != null;
    }

    public String getUtf8String() {
        return this.bytes == null ? null : new String(this.bytes, StandardCharsets.UTF_8);
    }

    public String toString() {
        Object[] arrobject = new Object[3];
        arrobject[0] = this.code;
        arrobject[1] = this.bytes == null ? "null" : Integer.toString(this.bytes.length);
        arrobject[2] = StringUtil.shrinkTo(this.getUtf8String(), 50);
        return String.format("Response {code=%d, size=%s, s='%s'}", arrobject);
    }
}

