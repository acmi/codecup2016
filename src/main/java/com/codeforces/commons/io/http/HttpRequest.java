/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.io.http;

import com.codeforces.commons.io.CountingInputStream;
import com.codeforces.commons.io.IoUtil;
import com.codeforces.commons.io.http.HttpMethod;
import com.codeforces.commons.io.http.HttpResponse;
import com.codeforces.commons.io.http.HttpResponseChecker;
import com.codeforces.commons.math.NumberUtil;
import com.codeforces.commons.process.ThreadUtil;
import com.codeforces.commons.properties.internal.CommonsPropertiesUtil;
import com.codeforces.commons.text.StringUtil;
import com.codeforces.commons.text.UrlUtil;
import com.google.common.base.Preconditions;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

public final class HttpRequest {
    private static final Logger logger = Logger.getLogger(HttpRequest.class);
    private final String url;
    private final Map<String, List<String>> parametersByName = new LinkedHashMap<String, List<String>>(8);
    private byte[] binaryEntity;
    private boolean gzip;
    private final Map<String, List<String>> headersByName = new LinkedHashMap<String, List<String>>(8);
    private HttpMethod method = HttpMethod.GET;
    private int timeoutMillis = NumberUtil.toInt(600000);
    private int maxRetryCount = 1;
    private HttpResponseChecker responseChecker = httpResponse -> !httpResponse.hasIoException();
    private ThreadUtil.ExecutionStrategy retryStrategy = new ThreadUtil.ExecutionStrategy(250, ThreadUtil.ExecutionStrategy.Type.LINEAR);
    private long maxSizeBytes = 0x40000000;

    public static /* varargs */ HttpRequest create(String string, Object ... arrobject) {
        return new HttpRequest(string, arrobject);
    }

    private /* varargs */ HttpRequest(String string, Object ... arrobject) {
        this.url = string;
        this.appendParameters(arrobject);
    }

    public /* varargs */ HttpRequest appendParameters(Object ... arrobject) {
        if (this.hasBinaryEntity()) {
            throw new IllegalStateException("Can't send parameters and binary entity with a single request.");
        }
        String[] arrstring = HttpRequest.validateAndEncodeParameters(this.url, arrobject);
        HttpRequest.appendNamedItems(arrstring, this.parametersByName);
        return this;
    }

    public HttpRequest setBinaryEntity(byte[] arrby) {
        if (!this.parametersByName.isEmpty()) {
            throw new IllegalStateException("Can't send parameters and binary entity with a single request.");
        }
        this.binaryEntity = arrby;
        return this;
    }

    public boolean hasBinaryEntity() {
        return this.binaryEntity != null;
    }

    public HttpRequest setGzip(boolean bl) {
        this.gzip = bl;
        return this;
    }

    public HttpRequest setMethod(HttpMethod httpMethod) {
        Preconditions.checkNotNull(httpMethod, "Argument 'method' is null.");
        this.method = httpMethod;
        return this;
    }

    public HttpRequest setTimeoutMillis(int n2) {
        Preconditions.checkArgument(n2 > 0, "Argument 'timeoutMillis' is zero or negative.");
        this.timeoutMillis = n2;
        return this;
    }

    public HttpResponse executeAndReturnResponse() {
        return this.internalExecute(true);
    }

    private HttpResponse internalExecute(boolean bl) {
        String string = this.appendGetParametersToUrl(this.url);
        if (this.method == HttpMethod.GET && this.hasBinaryEntity()) {
            String string2 = "Can't write binary entity to '" + string + "' with GET method.";
            logger.warn(string2);
            return new HttpResponse(-1, null, null, new IOException(string2));
        }
        long l2 = System.currentTimeMillis();
        for (int i2 = 1; i2 < this.maxRetryCount; ++i2) {
            HttpResponse httpResponse = this.internalGetHttpResponse(bl, string, l2);
            if (this.responseChecker.check(httpResponse)) {
                return httpResponse;
            }
            ThreadUtil.sleep(this.retryStrategy.getDelayTimeMillis(i2));
        }
        return this.internalGetHttpResponse(bl, string, l2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private HttpResponse internalGetHttpResponse(boolean bl, String string, long l2) {
        HttpURLConnection httpURLConnection;
        try {
            httpURLConnection = this.newConnection(string, this.method == HttpMethod.POST && (!this.parametersByName.isEmpty() || this.hasBinaryEntity()));
        }
        catch (IOException iOException) {
            String string2 = "Can't create connection to '" + string + "'.";
            logger.warn(string2, iOException);
            return new HttpResponse(-1, null, null, new IOException(string2, iOException));
        }
        if (this.method == HttpMethod.POST) {
            if (!this.parametersByName.isEmpty()) {
                try {
                    this.writePostParameters(httpURLConnection, this.parametersByName);
                }
                catch (IOException iOException) {
                    String string3 = "Can't write POST parameters to '" + string + "'.";
                    logger.warn(string3, iOException);
                    return new HttpResponse(-1, null, null, new IOException(string3, iOException));
                }
            }
            if (this.hasBinaryEntity()) {
                try {
                    this.writeEntity(httpURLConnection, this.binaryEntity);
                }
                catch (IOException iOException) {
                    String string4 = "Can't write binary entity to '" + string + "'.";
                    logger.warn(string4, iOException);
                    return new HttpResponse(-1, null, null, new IOException(string4, iOException));
                }
            }
        }
        try {
            httpURLConnection.connect();
            int n2 = httpURLConnection.getResponseCode();
            byte[] arrby = this.getBytes(httpURLConnection, bl, l2);
            HttpResponse httpResponse = new HttpResponse(n2, arrby, httpURLConnection.getHeaderFields(), null);
            return httpResponse;
        }
        catch (IOException iOException) {
            String string5 = "Can't read response from '" + string + "'.";
            logger.warn(string5, iOException);
            HttpResponse httpResponse = new HttpResponse(-1, null, httpURLConnection.getHeaderFields(), new IOException(string5, iOException));
            return httpResponse;
        }
        finally {
            httpURLConnection.disconnect();
        }
    }

    private byte[] getBytes(HttpURLConnection httpURLConnection, boolean bl, long l2) throws IOException {
        byte[] arrby;
        if (bl) {
            InputStream inputStream;
            block11 : {
                try {
                    inputStream = httpURLConnection.getInputStream();
                }
                catch (IOException iOException) {
                    inputStream = httpURLConnection.getErrorStream();
                    if (inputStream != null) break block11;
                    throw iOException;
                }
            }
            if (inputStream == null) {
                arrby = null;
            } else {
                String string = httpURLConnection.getContentEncoding();
                if ("gzip".equalsIgnoreCase(string)) {
                    inputStream = new GZIPInputStream(inputStream, IoUtil.BUFFER_SIZE);
                } else if ("deflate".equalsIgnoreCase(string)) {
                    inputStream = new InflaterInputStream(inputStream, new Inflater(), IoUtil.BUFFER_SIZE);
                } else if ("zip".equalsIgnoreCase(string)) {
                    inputStream = new ZipInputStream(inputStream);
                }
                inputStream = new CountingInputStream(inputStream, (l3, l4) -> {
                    if (System.currentTimeMillis() - l2 > (long)this.timeoutMillis) {
                        throw new IOException("Can't read response within " + this.timeoutMillis + " ms.");
                    }
                }
                );
                arrby = IoUtil.toByteArray(inputStream, NumberUtil.toInt(this.maxSizeBytes), true);
            }
        } else {
            arrby = null;
        }
        return arrby;
    }

    private String appendGetParametersToUrl(String string) {
        if (this.method == HttpMethod.GET) {
            for (Map.Entry<String, List<String>> entry : this.parametersByName.entrySet()) {
                String string2 = entry.getKey();
                for (String string3 : entry.getValue()) {
                    string = UrlUtil.appendParameterToUrl(string, string2, string3);
                }
            }
        }
        return string;
    }

    private static /* varargs */ String[] validateAndEncodeParameters(String string, Object ... arrobject) {
        boolean bl;
        if (!UrlUtil.isValidUrl(string)) {
            throw new IllegalArgumentException("" + '\'' + string + "' is not a valid URL.");
        }
        try {
            bl = CommonsPropertiesUtil.getSecureHosts().contains(new URL(string).getHost());
        }
        catch (MalformedURLException malformedURLException) {
            throw new IllegalArgumentException("" + '\'' + string + "' is not a valid URL.", malformedURLException);
        }
        int n2 = arrobject.length;
        if (n2 == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (n2 % 2 != 0) {
            throw new IllegalArgumentException("Argument 'parameters' should contain even number of elements, i.e. should consist of key-value pairs.");
        }
        List<String> list = CommonsPropertiesUtil.getSecurePasswords();
        List<String> list2 = CommonsPropertiesUtil.getPrivateParameters();
        String[] arrstring = new String[n2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            Object object = arrobject[i2];
            Object object2 = arrobject[i2 + 1];
            if (!(object instanceof String) || StringUtil.isBlank((String)object)) {
                throw new IllegalArgumentException(String.format("Each parameter name should be non-blank string, but found: '%s'.", object));
            }
            if (object2 == null) {
                throw new IllegalArgumentException(String.format("Value of parameter '%s' is null.", object));
            }
            try {
                arrstring[i2] = URLEncoder.encode((String)object, "UTF-8");
                if (bl || !list2.contains(object) && !list.contains(object2.toString())) {
                    arrstring[i2 + 1] = URLEncoder.encode(object2.toString(), "UTF-8");
                    continue;
                }
                arrstring[i2 + 1] = "";
                continue;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new RuntimeException("UTF-8 is unsupported.", unsupportedEncodingException);
            }
        }
        return arrstring;
    }

    private HttpURLConnection newConnection(String string, boolean bl) throws IOException {
        URL uRL = new URL(string);
        Proxy proxy = HttpRequest.getProxy(uRL.getProtocol());
        HttpURLConnection httpURLConnection = (HttpURLConnection)(proxy == null ? uRL.openConnection() : uRL.openConnection(proxy));
        if (httpURLConnection instanceof HttpsURLConnection) {
            HttpRequest.bypassSecureHostSslCertificateCheck((HttpsURLConnection)httpURLConnection, uRL);
        }
        httpURLConnection.setReadTimeout(this.timeoutMillis);
        httpURLConnection.setConnectTimeout(this.timeoutMillis);
        httpURLConnection.setRequestMethod(this.method.name());
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(bl);
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setRequestProperty("Connection", "keep-alive");
        if (this.method == HttpMethod.POST) {
            if (this.hasBinaryEntity()) {
                httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
            } else if (!this.parametersByName.isEmpty()) {
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            if (this.gzip && (this.hasBinaryEntity() || !this.parametersByName.isEmpty())) {
                httpURLConnection.setRequestProperty("Content-Encoding", "gzip");
            }
        }
        for (Map.Entry<String, List<String>> entry : this.headersByName.entrySet()) {
            String string2 = entry.getKey();
            boolean bl2 = true;
            for (String string3 : entry.getValue()) {
                if (bl2) {
                    httpURLConnection.setRequestProperty(string2, string3);
                    bl2 = false;
                    continue;
                }
                httpURLConnection.addRequestProperty(string2, string3);
            }
        }
        return httpURLConnection;
    }

    private static void bypassSecureHostSslCertificateCheck(HttpsURLConnection httpsURLConnection, URL uRL) {
        SSLContext sSLContext;
        if (!CommonsPropertiesUtil.isBypassCertificateCheck() || !CommonsPropertiesUtil.getSecureHosts().contains(uRL.getHost())) {
            return;
        }
        X509TrustManager x509TrustManager = new X509TrustManager(){

            @Override
            public void checkClientTrusted(X509Certificate[] arrx509Certificate, String string) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arrx509Certificate, String string) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            sSLContext = SSLContext.getInstance("SSL");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.warn("Can't get instance of SSL context.", noSuchAlgorithmException);
            return;
        }
        try {
            sSLContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
        }
        catch (KeyManagementException keyManagementException) {
            logger.warn("Can't initialize SSL context.", keyManagementException);
            return;
        }
        httpsURLConnection.setSSLSocketFactory(sSLContext.getSocketFactory());
    }

    private static Proxy getProxy(String string) {
        int n2;
        if (!Boolean.parseBoolean(System.getProperty("proxySet"))) {
            return null;
        }
        if (!"http".equalsIgnoreCase(string) && !"https".equalsIgnoreCase(string)) {
            return null;
        }
        String string2 = System.getProperty(string + ".proxyHost");
        if (StringUtil.isBlank(string2)) {
            return null;
        }
        try {
            n2 = Integer.parseInt(System.getProperty(string + ".proxyPort"));
            if (n2 <= 0 || n2 > 65535) {
                return null;
            }
        }
        catch (NumberFormatException numberFormatException) {
            return null;
        }
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(string2, n2));
    }

    private void writePostParameters(HttpURLConnection httpURLConnection, Map<String, List<String>> map) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String string = entry.getKey();
            for (String string2 : entry.getValue()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append('&');
                }
                stringBuilder.append(string).append('=').append(string2);
            }
        }
        this.writeEntity(httpURLConnection, stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void writeEntity(HttpURLConnection httpURLConnection, byte[] arrby) throws IOException {
        OutputStream outputStream = this.gzip ? new GZIPOutputStream(httpURLConnection.getOutputStream(), IoUtil.BUFFER_SIZE) : new BufferedOutputStream(httpURLConnection.getOutputStream(), IoUtil.BUFFER_SIZE);
        long l2 = System.currentTimeMillis();
        try {
            outputStream.write(arrby);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException iOException) {
            try {
                IoUtil.closeQuietly((AutoCloseable)outputStream);
                throw iOException;
            }
            catch (Throwable throwable) {
                long l3 = System.currentTimeMillis() - l2;
                if (l3 > 100) {
                    logger.info(String.format("Writing of HTTP entity takes %d ms (size=%d, gzip=%s).", l3, arrby.length, this.gzip));
                }
                throw throwable;
            }
        }
        long l4 = System.currentTimeMillis() - l2;
        if (l4 > 100) {
            logger.info(String.format("Writing of HTTP entity takes %d ms (size=%d, gzip=%s).", l4, arrby.length, this.gzip));
        }
    }

    private static void appendNamedItems(String[] arrstring, Map<String, List<String>> map) {
        int n2 = arrstring.length;
        for (int i2 = 0; i2 < n2; i2 += 2) {
            String string = arrstring[i2];
            String string2 = arrstring[i2 + 1];
            List<String> list = map.get(string);
            if (list == null) {
                list = new ArrayList<String>(1);
                map.put(string, list);
            }
            list.add(string2);
        }
    }

    static <K, V> Map<K, List<V>> getDeepUnmodifiableMap(Map<K, List<V>> map) {
        LinkedHashMap<K, List<V>> linkedHashMap = new LinkedHashMap<K, List<V>>(map);
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            entry.setValue(Collections.unmodifiableList(new ArrayList(entry.getValue())));
        }
        return Collections.unmodifiableMap(linkedHashMap);
    }

}

