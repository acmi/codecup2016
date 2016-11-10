/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.dom.DOMMessageFormatter;

public final class HTMLdtd {
    private static Hashtable _byChar;
    private static Hashtable _byName;
    private static Hashtable _boolAttrs;
    private static Hashtable _elemDefs;
    static Class class$org$apache$xml$serialize$HTMLdtd;

    public static boolean isEmptyTag(String string) {
        return HTMLdtd.isElement(string, 17);
    }

    public static boolean isPreserveSpace(String string) {
        return HTMLdtd.isElement(string, 4);
    }

    public static boolean isOnlyOpening(String string) {
        return HTMLdtd.isElement(string, 1);
    }

    public static boolean isURI(String string, String string2) {
        return string2.equalsIgnoreCase("href") || string2.equalsIgnoreCase("src");
    }

    public static boolean isBoolean(String string, String string2) {
        String[] arrstring = (String[])_boolAttrs.get(string.toUpperCase(Locale.ENGLISH));
        if (arrstring == null) {
            return false;
        }
        int n2 = 0;
        while (n2 < arrstring.length) {
            if (arrstring[n2].equalsIgnoreCase(string2)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public static String fromChar(int n2) {
        if (n2 > 65535) {
            return null;
        }
        HTMLdtd.initialize();
        String string = (String)_byChar.get(new Integer(n2));
        return string;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static void initialize() {
        inputStream = null;
        bufferedReader = null;
        if (HTMLdtd._byName != null) {
            return;
        }
        try {
            try {
                HTMLdtd._byName = new Hashtable<K, V>();
                HTMLdtd._byChar = new Hashtable<K, V>();
                v0 = HTMLdtd.class$org$apache$xml$serialize$HTMLdtd == null ? (HTMLdtd.class$org$apache$xml$serialize$HTMLdtd = HTMLdtd.class$("org.apache.xml.serialize.HTMLdtd")) : HTMLdtd.class$org$apache$xml$serialize$HTMLdtd;
                inputStream = v0.getResourceAsStream("HTMLEntities.res");
                if (inputStream == null) {
                    throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResourceNotFound", new Object[]{"HTMLEntities.res"}));
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ASCII"));
                string = bufferedReader.readLine();
                do {
                    if (string == null) {
                        inputStream.close();
                        ** break;
                    }
                    if (string.length() == 0 || string.charAt(0) == '#') {
                        string = bufferedReader.readLine();
                        continue;
                    }
                    n2 = string.indexOf(32);
                    if (n2 > 1) {
                        string2 = string.substring(0, n2);
                        if (++n2 < string.length()) {
                            string3 = string.substring(n2);
                            n2 = string3.indexOf(32);
                            if (n2 > 0) {
                                string3 = string3.substring(0, n2);
                            }
                            n3 = Integer.parseInt(string3);
                            HTMLdtd.defineEntity(string2, (char)n3);
                        }
                    }
                    string = bufferedReader.readLine();
                } while (true);
            }
            catch (Exception exception) {
                throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResourceNotLoaded", new Object[]{"HTMLEntities.res", exception.toString()}));
            }
lbl36: // 1 sources:
            var9_7 = null;
            if (inputStream == null) return;
            try {
                inputStream.close();
                return;
            }
            catch (Exception exception) {
                return;
            }
        }
        catch (Throwable throwable) {
            var9_8 = null;
            if (inputStream == null) throw throwable;
            ** try [egrp 2[TRYBLOCK] [2 : 286->293)] { 
lbl48: // 1 sources:
            inputStream.close();
            throw throwable;
lbl50: // 1 sources:
            catch (Exception exception) {
                // empty catch block
            }
            throw throwable;
        }
    }

    private static void defineEntity(String string, char c2) {
        if (_byName.get(string) == null) {
            _byName.put(string, new Integer(c2));
            _byChar.put(new Integer(c2), string);
        }
    }

    private static void defineElement(String string, int n2) {
        _elemDefs.put(string, new Integer(n2));
    }

    private static void defineBoolean(String string, String string2) {
        HTMLdtd.defineBoolean(string, new String[]{string2});
    }

    private static void defineBoolean(String string, String[] arrstring) {
        _boolAttrs.put(string, arrstring);
    }

    private static boolean isElement(String string, int n2) {
        Integer n3 = (Integer)_elemDefs.get(string.toUpperCase(Locale.ENGLISH));
        if (n3 == null) {
            return false;
        }
        return (n3 & n2) == n2;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static {
        _elemDefs = new Hashtable();
        HTMLdtd.defineElement("ADDRESS", 64);
        HTMLdtd.defineElement("AREA", 17);
        HTMLdtd.defineElement("BASE", 49);
        HTMLdtd.defineElement("BASEFONT", 17);
        HTMLdtd.defineElement("BLOCKQUOTE", 64);
        HTMLdtd.defineElement("BODY", 8);
        HTMLdtd.defineElement("BR", 17);
        HTMLdtd.defineElement("COL", 17);
        HTMLdtd.defineElement("COLGROUP", 522);
        HTMLdtd.defineElement("DD", 137);
        HTMLdtd.defineElement("DIV", 64);
        HTMLdtd.defineElement("DL", 66);
        HTMLdtd.defineElement("DT", 137);
        HTMLdtd.defineElement("FIELDSET", 64);
        HTMLdtd.defineElement("FORM", 64);
        HTMLdtd.defineElement("FRAME", 25);
        HTMLdtd.defineElement("H1", 64);
        HTMLdtd.defineElement("H2", 64);
        HTMLdtd.defineElement("H3", 64);
        HTMLdtd.defineElement("H4", 64);
        HTMLdtd.defineElement("H5", 64);
        HTMLdtd.defineElement("H6", 64);
        HTMLdtd.defineElement("HEAD", 10);
        HTMLdtd.defineElement("HR", 81);
        HTMLdtd.defineElement("HTML", 10);
        HTMLdtd.defineElement("IMG", 17);
        HTMLdtd.defineElement("INPUT", 17);
        HTMLdtd.defineElement("ISINDEX", 49);
        HTMLdtd.defineElement("LI", 265);
        HTMLdtd.defineElement("LINK", 49);
        HTMLdtd.defineElement("MAP", 32);
        HTMLdtd.defineElement("META", 49);
        HTMLdtd.defineElement("OL", 66);
        HTMLdtd.defineElement("OPTGROUP", 2);
        HTMLdtd.defineElement("OPTION", 265);
        HTMLdtd.defineElement("P", 328);
        HTMLdtd.defineElement("PARAM", 17);
        HTMLdtd.defineElement("PRE", 68);
        HTMLdtd.defineElement("SCRIPT", 36);
        HTMLdtd.defineElement("NOSCRIPT", 36);
        HTMLdtd.defineElement("SELECT", 2);
        HTMLdtd.defineElement("STYLE", 36);
        HTMLdtd.defineElement("TABLE", 66);
        HTMLdtd.defineElement("TBODY", 522);
        HTMLdtd.defineElement("TD", 16392);
        HTMLdtd.defineElement("TEXTAREA", 4);
        HTMLdtd.defineElement("TFOOT", 522);
        HTMLdtd.defineElement("TH", 16392);
        HTMLdtd.defineElement("THEAD", 522);
        HTMLdtd.defineElement("TITLE", 32);
        HTMLdtd.defineElement("TR", 522);
        HTMLdtd.defineElement("UL", 66);
        _boolAttrs = new Hashtable();
        HTMLdtd.defineBoolean("AREA", "href");
        HTMLdtd.defineBoolean("BUTTON", "disabled");
        HTMLdtd.defineBoolean("DIR", "compact");
        HTMLdtd.defineBoolean("DL", "compact");
        HTMLdtd.defineBoolean("FRAME", "noresize");
        HTMLdtd.defineBoolean("HR", "noshade");
        HTMLdtd.defineBoolean("IMAGE", "ismap");
        HTMLdtd.defineBoolean("INPUT", new String[]{"defaultchecked", "checked", "readonly", "disabled"});
        HTMLdtd.defineBoolean("LINK", "link");
        HTMLdtd.defineBoolean("MENU", "compact");
        HTMLdtd.defineBoolean("OBJECT", "declare");
        HTMLdtd.defineBoolean("OL", "compact");
        HTMLdtd.defineBoolean("OPTGROUP", "disabled");
        HTMLdtd.defineBoolean("OPTION", new String[]{"default-selected", "selected", "disabled"});
        HTMLdtd.defineBoolean("SCRIPT", "defer");
        HTMLdtd.defineBoolean("SELECT", new String[]{"multiple", "disabled"});
        HTMLdtd.defineBoolean("STYLE", "disabled");
        HTMLdtd.defineBoolean("TD", "nowrap");
        HTMLdtd.defineBoolean("TH", "nowrap");
        HTMLdtd.defineBoolean("TEXTAREA", new String[]{"disabled", "readonly"});
        HTMLdtd.defineBoolean("UL", "compact");
        HTMLdtd.initialize();
    }
}

