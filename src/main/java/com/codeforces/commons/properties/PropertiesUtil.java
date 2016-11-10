/*
 * Decompiled with CFR 0_119.
 */
package com.codeforces.commons.properties;

import com.codeforces.commons.resource.CantReadResourceException;
import com.codeforces.commons.text.Patterns;
import com.codeforces.commons.text.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class PropertiesUtil {
    private static final Logger logger = Logger.getLogger(PropertiesUtil.class);
    private static final ConcurrentMap<String, Properties> propertiesByResourceName = new ConcurrentHashMap<String, Properties>();

    private PropertiesUtil() {
        throw new UnsupportedOperationException();
    }

    public static /* varargs */ String getProperty(boolean bl, String string, String string2, String ... arrstring) throws CantReadResourceException {
        for (String string3 : arrstring) {
            Properties properties;
            try {
                properties = PropertiesUtil.ensurePropertiesByResourceName(string3);
            }
            catch (IOException iOException) {
                String string4 = String.format("Can't read properties from resource '%s'.", string3);
                if (bl) {
                    logger.error(string4, iOException);
                    throw new CantReadResourceException(string4, iOException);
                }
                logger.warn(string4, iOException);
                continue;
            }
            String string5 = properties.getProperty(string);
            if (string5 == null) continue;
            return string5;
        }
        return string2;
    }

    public static /* varargs */ String getPropertyQuietly(String string, String string2, String ... arrstring) {
        return PropertiesUtil.getProperty(false, string, string2, arrstring);
    }

    public static /* varargs */ List<String> getListProperty(boolean bl, String string, String string2, String ... arrstring) {
        String string3 = PropertiesUtil.getProperty(bl, string, string2, arrstring);
        if (StringUtil.isBlank(string3)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(Patterns.SEMICOLON_PATTERN.split(string3)));
    }

    public static /* varargs */ List<String> getListPropertyQuietly(String string, String string2, String ... arrstring) {
        return PropertiesUtil.getListProperty(false, string, string2, arrstring);
    }

    private static Properties ensurePropertiesByResourceName(String string) throws IOException {
        Properties properties = propertiesByResourceName.get(string);
        if (properties == null) {
            block14 : {
                properties = new Properties();
                InputStream inputStream = PropertiesUtil.class.getResourceAsStream(string);
                Throwable throwable = null;
                try {
                    if (inputStream != null) {
                        properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        break block14;
                    }
                    logger.warn(String.format("Can't find resource file '%s'.", string));
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                    throw throwable2;
                }
                finally {
                    if (inputStream != null) {
                        if (throwable != null) {
                            try {
                                inputStream.close();
                            }
                            catch (Throwable throwable3) {
                                throwable.addSuppressed(throwable3);
                            }
                        } else {
                            inputStream.close();
                        }
                    }
                }
            }
            propertiesByResourceName.putIfAbsent(string, properties);
            properties = propertiesByResourceName.get(string);
        }
        return properties;
    }
}

