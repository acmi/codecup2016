/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.ObjectFactory;
import org.apache.xml.serializer.SerializerBase;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.SystemIDResolver;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;

final class CharInfo {
    private HashMap m_charToString;
    public static final String HTML_ENTITIES_RESOURCE = SerializerBase.PKG_NAME + ".HTMLEntities";
    public static final String XML_ENTITIES_RESOURCE = SerializerBase.PKG_NAME + ".XMLEntities";
    boolean onlyQuotAmpLtGt;
    private final boolean[] shouldMapAttrChar_ASCII;
    private final boolean[] shouldMapTextChar_ASCII;
    private final int[] array_of_bits;
    private int firstWordNotUsed;
    private final CharKey m_charKey;
    private static Hashtable m_getCharInfoCache = new Hashtable();
    static Class class$org$apache$xml$serializer$CharInfo;

    private CharInfo() {
        this.array_of_bits = this.createEmptySetOfIntegers(65535);
        this.firstWordNotUsed = 0;
        this.shouldMapAttrChar_ASCII = new boolean[128];
        this.shouldMapTextChar_ASCII = new boolean[128];
        this.m_charKey = new CharKey();
        this.onlyQuotAmpLtGt = true;
    }

    private CharInfo(String string, String string2, boolean bl) {
        this();
        this.m_charToString = new HashMap();
        ResourceBundle resourceBundle = null;
        boolean bl2 = true;
        if (bl) {
            try {
                resourceBundle = PropertyResourceBundle.getBundle(string);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (resourceBundle != null) {
            Enumeration<String> enumeration = resourceBundle.getKeys();
            while (enumeration.hasMoreElements()) {
                String string3;
                int n2;
                String string4 = enumeration.nextElement();
                boolean bl3 = this.defineEntity(string4, (char)(n2 = Integer.parseInt(string3 = resourceBundle.getString(string4))));
                if (!bl3) continue;
                bl2 = false;
            }
        } else {
            InputStream inputStream = null;
            try {
                Object object;
                Object object2;
                if (bl) {
                    Class class_ = class$org$apache$xml$serializer$CharInfo == null ? (CharInfo.class$org$apache$xml$serializer$CharInfo = CharInfo.class$("org.apache.xml.serializer.CharInfo")) : class$org$apache$xml$serializer$CharInfo;
                    inputStream = class_.getResourceAsStream(string);
                } else {
                    object = ObjectFactory.findClassLoader();
                    inputStream = object == null ? ClassLoader.getSystemResourceAsStream(string) : object.getResourceAsStream(string);
                    if (inputStream == null) {
                        try {
                            object2 = new URL(string);
                            inputStream = object2.openStream();
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                }
                if (inputStream == null) {
                    throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_FIND", new Object[]{string, string}));
                }
                try {
                    object = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    object = new BufferedReader(new InputStreamReader(inputStream));
                }
                object2 = object.readLine();
                while (object2 != null) {
                    if (object2.length() == 0 || object2.charAt(0) == '#') {
                        object2 = object.readLine();
                        continue;
                    }
                    int n3 = object2.indexOf(32);
                    if (n3 > 1) {
                        String string5 = object2.substring(0, n3);
                        if (++n3 < object2.length()) {
                            int n4;
                            boolean bl4;
                            String string6 = object2.substring(n3);
                            n3 = string6.indexOf(32);
                            if (n3 > 0) {
                                string6 = string6.substring(0, n3);
                            }
                            if (bl4 = this.defineEntity(string5, (char)(n4 = Integer.parseInt(string6)))) {
                                bl2 = false;
                            }
                        }
                    }
                    object2 = object.readLine();
                }
                inputStream.close();
            }
            catch (Exception exception) {
                throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_LOAD", new Object[]{string, exception.toString(), string, exception.toString()}));
            }
            finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Exception exception) {}
                }
            }
        }
        this.onlyQuotAmpLtGt = bl2;
        if ("xml".equals(string2)) {
            this.shouldMapTextChar_ASCII[34] = false;
        }
        if ("html".equals(string2)) {
            this.shouldMapAttrChar_ASCII[60] = false;
            this.shouldMapTextChar_ASCII[34] = false;
        }
    }

    private boolean defineEntity(String string, char c2) {
        StringBuffer stringBuffer = new StringBuffer("&");
        stringBuffer.append(string);
        stringBuffer.append(';');
        String string2 = stringBuffer.toString();
        boolean bl = this.defineChar2StringMapping(string2, c2);
        return bl;
    }

    String getOutputStringForChar(char c2) {
        this.m_charKey.setChar(c2);
        return (String)this.m_charToString.get(this.m_charKey);
    }

    final boolean shouldMapAttrChar(int n2) {
        if (n2 < 128) {
            return this.shouldMapAttrChar_ASCII[n2];
        }
        return this.get(n2);
    }

    final boolean shouldMapTextChar(int n2) {
        if (n2 < 128) {
            return this.shouldMapTextChar_ASCII[n2];
        }
        return this.get(n2);
    }

    private static CharInfo getCharInfoBasedOnPrivilege(String string, String string2, boolean bl) {
        return (CharInfo)AccessController.doPrivileged(new PrivilegedAction(string, string2, bl){
            private final String val$entitiesFileName;
            private final String val$method;
            private final boolean val$internal;

            public Object run() {
                return new CharInfo(this.val$entitiesFileName, this.val$method, this.val$internal, null);
            }
        });
    }

    static CharInfo getCharInfo(String string, String string2) {
        CharInfo charInfo = (CharInfo)m_getCharInfoCache.get(string);
        if (charInfo != null) {
            return CharInfo.mutableCopyOf(charInfo);
        }
        try {
            charInfo = CharInfo.getCharInfoBasedOnPrivilege(string, string2, true);
            m_getCharInfoCache.put(string, charInfo);
            return CharInfo.mutableCopyOf(charInfo);
        }
        catch (Exception exception) {
            try {
                return CharInfo.getCharInfoBasedOnPrivilege(string, string2, false);
            }
            catch (Exception exception2) {
                if (string.indexOf(58) < 0) {
                    String string3 = SystemIDResolver.getAbsoluteURIFromRelative(string);
                } else {
                    try {
                        String string4 = SystemIDResolver.getAbsoluteURI(string, null);
                    }
                    catch (TransformerException transformerException) {
                        throw new WrappedRuntimeException(transformerException);
                    }
                }
                return CharInfo.getCharInfoBasedOnPrivilege(string, string2, false);
            }
        }
    }

    private static CharInfo mutableCopyOf(CharInfo charInfo) {
        CharInfo charInfo2 = new CharInfo();
        int n2 = charInfo.array_of_bits.length;
        System.arraycopy(charInfo.array_of_bits, 0, charInfo2.array_of_bits, 0, n2);
        charInfo2.firstWordNotUsed = charInfo.firstWordNotUsed;
        n2 = charInfo.shouldMapAttrChar_ASCII.length;
        System.arraycopy(charInfo.shouldMapAttrChar_ASCII, 0, charInfo2.shouldMapAttrChar_ASCII, 0, n2);
        n2 = charInfo.shouldMapTextChar_ASCII.length;
        System.arraycopy(charInfo.shouldMapTextChar_ASCII, 0, charInfo2.shouldMapTextChar_ASCII, 0, n2);
        charInfo2.m_charToString = (HashMap)charInfo.m_charToString.clone();
        charInfo2.onlyQuotAmpLtGt = charInfo.onlyQuotAmpLtGt;
        return charInfo2;
    }

    private static int arrayIndex(int n2) {
        return n2 >> 5;
    }

    private int[] createEmptySetOfIntegers(int n2) {
        this.firstWordNotUsed = 0;
        int[] arrn = new int[CharInfo.arrayIndex(n2 - 1) + 1];
        return arrn;
    }

    private final void set(int n2) {
        this.setASCIItextDirty(n2);
        this.setASCIIattrDirty(n2);
        int n3 = n2 >> 5;
        int n4 = n3 + 1;
        if (this.firstWordNotUsed < n4) {
            this.firstWordNotUsed = n4;
        }
        int[] arrn = this.array_of_bits;
        int n5 = n3;
        arrn[n5] = arrn[n5] | 1 << (n2 & 31);
    }

    private final boolean get(int n2) {
        boolean bl = false;
        int n3 = n2 >> 5;
        if (n3 < this.firstWordNotUsed) {
            bl = (this.array_of_bits[n3] & 1 << (n2 & 31)) != 0;
        }
        return bl;
    }

    private boolean extraEntity(String string, int n2) {
        boolean bl = false;
        if (n2 < 128) {
            switch (n2) {
                case 34: {
                    if (string.equals("&quot;")) break;
                    bl = true;
                    break;
                }
                case 38: {
                    if (string.equals("&amp;")) break;
                    bl = true;
                    break;
                }
                case 60: {
                    if (string.equals("&lt;")) break;
                    bl = true;
                    break;
                }
                case 62: {
                    if (string.equals("&gt;")) break;
                    bl = true;
                    break;
                }
                default: {
                    bl = true;
                }
            }
        }
        return bl;
    }

    private void setASCIItextDirty(int n2) {
        if (0 <= n2 && n2 < 128) {
            this.shouldMapTextChar_ASCII[n2] = true;
        }
    }

    private void setASCIIattrDirty(int n2) {
        if (0 <= n2 && n2 < 128) {
            this.shouldMapAttrChar_ASCII[n2] = true;
        }
    }

    boolean defineChar2StringMapping(String string, char c2) {
        CharKey charKey = new CharKey(c2);
        this.m_charToString.put(charKey, string);
        this.set(c2);
        boolean bl = this.extraEntity(string, c2);
        return bl;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    CharInfo(String string, String string2, boolean bl,  var4_4) {
        this(string, string2, bl);
    }

    private static class CharKey {
        private char m_char;

        public CharKey(char c2) {
            this.m_char = c2;
        }

        public CharKey() {
        }

        public final void setChar(char c2) {
            this.m_char = c2;
        }

        public final int hashCode() {
            return this.m_char;
        }

        public final boolean equals(Object object) {
            return ((CharKey)object).m_char == this.m_char;
        }
    }

}

