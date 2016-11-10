/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xslt;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xalan.xslt.ObjectFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class EnvironmentCheck {
    public static final String ERROR = "ERROR.";
    public static final String WARNING = "WARNING.";
    public static final String ERROR_FOUND = "At least one error was found!";
    public static final String VERSION = "version.";
    public static final String FOUNDCLASSES = "foundclasses.";
    public static final String CLASS_PRESENT = "present-unknown-version";
    public static final String CLASS_NOTPRESENT = "not-present";
    public String[] jarNames = new String[]{"xalan.jar", "xalansamples.jar", "xalanj1compat.jar", "xalanservlet.jar", "serializer.jar", "xerces.jar", "xercesImpl.jar", "testxsl.jar", "crimson.jar", "lotusxsl.jar", "jaxp.jar", "parser.jar", "dom.jar", "sax.jar", "xml.jar", "xml-apis.jar", "xsltc.jar"};
    private static Hashtable jarVersions = new Hashtable();
    protected PrintWriter outWriter = new PrintWriter(System.out, true);
    static Class class$java$lang$String;
    static Class class$org$xml$sax$Attributes;

    public static void main(String[] arrstring) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            if (!"-out".equalsIgnoreCase(arrstring[i2])) continue;
            if (++i2 < arrstring.length) {
                try {
                    printWriter = new PrintWriter(new FileWriter(arrstring[i2], true));
                }
                catch (Exception exception) {
                    System.err.println("# WARNING: -out " + arrstring[i2] + " threw " + exception.toString());
                }
                continue;
            }
            System.err.println("# WARNING: -out argument should have a filename, output sent to console");
        }
        EnvironmentCheck environmentCheck = new EnvironmentCheck();
        environmentCheck.checkEnvironment(printWriter);
    }

    public boolean checkEnvironment(PrintWriter printWriter) {
        boolean bl;
        Hashtable hashtable;
        if (null != printWriter) {
            this.outWriter = printWriter;
        }
        if (bl = this.writeEnvironmentReport(hashtable = this.getEnvironmentHash())) {
            this.logMsg("# WARNING: Potential problems found in your environment!");
            this.logMsg("#    Check any 'ERROR' items above against the Xalan FAQs");
            this.logMsg("#    to correct potential problems with your classes/jars");
            this.logMsg("#    http://xml.apache.org/xalan-j/faq.html");
            if (null != this.outWriter) {
                this.outWriter.flush();
            }
            return false;
        }
        this.logMsg("# YAHOO! Your environment seems to be OK.");
        if (null != this.outWriter) {
            this.outWriter.flush();
        }
        return true;
    }

    public Hashtable getEnvironmentHash() {
        Hashtable hashtable = new Hashtable();
        this.checkJAXPVersion(hashtable);
        this.checkProcessorVersion(hashtable);
        this.checkParserVersion(hashtable);
        this.checkAntVersion(hashtable);
        this.checkDOMVersion(hashtable);
        this.checkSAXVersion(hashtable);
        this.checkSystemProperties(hashtable);
        return hashtable;
    }

    protected boolean writeEnvironmentReport(Hashtable hashtable) {
        if (null == hashtable) {
            this.logMsg("# ERROR: writeEnvironmentReport called with null Hashtable");
            return false;
        }
        boolean bl = false;
        this.logMsg("#---- BEGIN writeEnvironmentReport($Revision: 468646 $): Useful stuff found: ----");
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            Object k2 = enumeration.nextElement();
            String string = (String)k2;
            try {
                if (string.startsWith("foundclasses.")) {
                    Vector vector = (Vector)hashtable.get(string);
                    bl |= this.logFoundJars(vector, string);
                    continue;
                }
                if (string.startsWith("ERROR.")) {
                    bl = true;
                }
                this.logMsg(string + "=" + hashtable.get(string));
            }
            catch (Exception exception) {
                this.logMsg("Reading-" + k2 + "= threw: " + exception.toString());
            }
        }
        this.logMsg("#----- END writeEnvironmentReport: Useful properties found: -----");
        return bl;
    }

    protected boolean logFoundJars(Vector vector, String string) {
        if (null == vector || vector.size() < 1) {
            return false;
        }
        boolean bl = false;
        this.logMsg("#---- BEGIN Listing XML-related jars in: " + string + " ----");
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            Hashtable hashtable = (Hashtable)vector.elementAt(i2);
            Enumeration enumeration = hashtable.keys();
            while (enumeration.hasMoreElements()) {
                Object k2 = enumeration.nextElement();
                String string2 = (String)k2;
                try {
                    if (string2.startsWith("ERROR.")) {
                        bl = true;
                    }
                    this.logMsg(string2 + "=" + hashtable.get(string2));
                }
                catch (Exception exception) {
                    bl = true;
                    this.logMsg("Reading-" + k2 + "= threw: " + exception.toString());
                }
            }
        }
        this.logMsg("#----- END Listing XML-related jars in: " + string + " -----");
        return bl;
    }

    public void appendEnvironmentReport(Node node, Document document, Hashtable hashtable) {
        if (null == node || null == document) {
            return;
        }
        try {
            Element element = document.createElement("EnvironmentCheck");
            element.setAttribute("version", "$Revision: 468646 $");
            node.appendChild(element);
            if (null == hashtable) {
                Element element2 = document.createElement("status");
                element2.setAttribute("result", "ERROR");
                element2.appendChild(document.createTextNode("appendEnvironmentReport called with null Hashtable!"));
                element.appendChild(element2);
                return;
            }
            boolean bl = false;
            Element element3 = document.createElement("environment");
            element.appendChild(element3);
            Object object = hashtable.keys();
            while (object.hasMoreElements()) {
                Object k2 = object.nextElement();
                String string = (String)k2;
                try {
                    Object object2;
                    if (string.startsWith("foundclasses.")) {
                        object2 = (Vector)hashtable.get(string);
                        bl |= this.appendFoundJars(element3, document, (Vector)object2, string);
                        continue;
                    }
                    if (string.startsWith("ERROR.")) {
                        bl = true;
                    }
                    object2 = document.createElement("item");
                    object2.setAttribute("key", string);
                    object2.appendChild(document.createTextNode((String)hashtable.get(string)));
                    element3.appendChild((Node)object2);
                }
                catch (Exception exception) {
                    bl = true;
                    Element element4 = document.createElement("item");
                    element4.setAttribute("key", string);
                    element4.appendChild(document.createTextNode("ERROR. Reading " + k2 + " threw: " + exception.toString()));
                    element3.appendChild(element4);
                }
            }
            object = document.createElement("status");
            object.setAttribute("result", bl ? "ERROR" : "OK");
            element.appendChild((Node)object);
        }
        catch (Exception exception) {
            System.err.println("appendEnvironmentReport threw: " + exception.toString());
            exception.printStackTrace();
        }
    }

    protected boolean appendFoundJars(Node node, Document document, Vector vector, String string) {
        if (null == vector || vector.size() < 1) {
            return false;
        }
        boolean bl = false;
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            Hashtable hashtable = (Hashtable)vector.elementAt(i2);
            Enumeration enumeration = hashtable.keys();
            while (enumeration.hasMoreElements()) {
                Element element;
                Object k2 = enumeration.nextElement();
                try {
                    String string2 = (String)k2;
                    if (string2.startsWith("ERROR.")) {
                        bl = true;
                    }
                    element = document.createElement("foundJar");
                    element.setAttribute("name", string2.substring(0, string2.indexOf("-")));
                    element.setAttribute("desc", string2.substring(string2.indexOf("-") + 1));
                    element.appendChild(document.createTextNode((String)hashtable.get(string2)));
                    node.appendChild(element);
                }
                catch (Exception exception) {
                    bl = true;
                    element = document.createElement("foundJar");
                    element.appendChild(document.createTextNode("ERROR. Reading " + k2 + " threw: " + exception.toString()));
                    node.appendChild(element);
                }
            }
        }
        return bl;
    }

    protected void checkSystemProperties(Hashtable hashtable) {
        String string;
        if (null == hashtable) {
            hashtable = new Hashtable<String, Object>();
        }
        try {
            string = System.getProperty("java.version");
            hashtable.put("java.version", string);
        }
        catch (SecurityException securityException) {
            hashtable.put("java.version", "WARNING: SecurityException thrown accessing system version properties");
        }
        try {
            String string2;
            string = System.getProperty("java.class.path");
            hashtable.put("java.class.path", string);
            Vector vector = this.checkPathForJars(string, this.jarNames);
            if (null != vector) {
                hashtable.put("foundclasses.java.class.path", vector);
            }
            if (null != (string2 = System.getProperty("sun.boot.class.path"))) {
                hashtable.put("sun.boot.class.path", string2);
                vector = this.checkPathForJars(string2, this.jarNames);
                if (null != vector) {
                    hashtable.put("foundclasses.sun.boot.class.path", vector);
                }
            }
            if (null != (string2 = System.getProperty("java.ext.dirs"))) {
                hashtable.put("java.ext.dirs", string2);
                vector = this.checkPathForJars(string2, this.jarNames);
                if (null != vector) {
                    hashtable.put("foundclasses.java.ext.dirs", vector);
                }
            }
        }
        catch (SecurityException securityException) {
            hashtable.put("java.class.path", "WARNING: SecurityException thrown accessing system classpath properties");
        }
    }

    protected Vector checkPathForJars(String string, String[] arrstring) {
        if (null == string || null == arrstring || 0 == string.length() || 0 == arrstring.length) {
            return null;
        }
        Vector vector = new Vector();
        StringTokenizer stringTokenizer = new StringTokenizer(string, File.pathSeparator);
        while (stringTokenizer.hasMoreTokens()) {
            String string2 = stringTokenizer.nextToken();
            for (int i2 = 0; i2 < arrstring.length; ++i2) {
                Hashtable<String, String> hashtable;
                if (string2.indexOf(arrstring[i2]) <= -1) continue;
                File file = new File(string2);
                if (file.exists()) {
                    try {
                        hashtable = new Hashtable(2);
                        hashtable.put(arrstring[i2] + "-path", file.getAbsolutePath());
                        if (!"xalan.jar".equalsIgnoreCase(arrstring[i2])) {
                            hashtable.put(arrstring[i2] + "-apparent.version", this.getApparentVersion(arrstring[i2], file.length()));
                        }
                        vector.addElement(hashtable);
                    }
                    catch (Exception exception) {}
                    continue;
                }
                hashtable = new Hashtable<String, String>(2);
                hashtable.put(arrstring[i2] + "-path", "WARNING. Classpath entry: " + string2 + " does not exist");
                hashtable.put(arrstring[i2] + "-apparent.version", "not-present");
                vector.addElement(hashtable);
            }
        }
        return vector;
    }

    protected String getApparentVersion(String string, long l2) {
        String string2 = (String)jarVersions.get(new Long(l2));
        if (null != string2 && string2.startsWith(string)) {
            return string2;
        }
        if ("xerces.jar".equalsIgnoreCase(string) || "xercesImpl.jar".equalsIgnoreCase(string)) {
            return string + " " + "WARNING." + "present-unknown-version";
        }
        return string + " " + "present-unknown-version";
    }

    protected void checkJAXPVersion(Hashtable hashtable) {
        if (null == hashtable) {
            hashtable = new Hashtable<String, String>();
        }
        Class[] arrclass = new Class[]{};
        Class class_ = null;
        try {
            String string = "javax.xml.parsers.DocumentBuilder";
            String string2 = "getDOMImplementation";
            class_ = ObjectFactory.findProviderClass("javax.xml.parsers.DocumentBuilder", ObjectFactory.findClassLoader(), true);
            Method method = class_.getMethod("getDOMImplementation", arrclass);
            hashtable.put("version.JAXP", "1.1 or higher");
        }
        catch (Exception exception) {
            if (null != class_) {
                hashtable.put("ERROR.version.JAXP", "1.0.1");
                hashtable.put("ERROR.", "At least one error was found!");
            }
            hashtable.put("ERROR.version.JAXP", "not-present");
            hashtable.put("ERROR.", "At least one error was found!");
        }
    }

    protected void checkProcessorVersion(Hashtable hashtable) {
        Field field;
        String string;
        Class[] arrclass;
        Object object;
        if (null == hashtable) {
            hashtable = new Hashtable<String, String>();
        }
        try {
            string = "org.apache.xalan.xslt.XSLProcessorVersion";
            object = ObjectFactory.findProviderClass("org.apache.xalan.xslt.XSLProcessorVersion", ObjectFactory.findClassLoader(), true);
            arrclass = new Class[]();
            field = object.getField("PRODUCT");
            arrclass.append(field.get(null));
            arrclass.append(';');
            field = object.getField("LANGUAGE");
            arrclass.append(field.get(null));
            arrclass.append(';');
            field = object.getField("S_VERSION");
            arrclass.append(field.get(null));
            arrclass.append(';');
            hashtable.put("version.xalan1", arrclass.toString());
        }
        catch (Exception exception) {
            hashtable.put("version.xalan1", "not-present");
        }
        try {
            string = "org.apache.xalan.processor.XSLProcessorVersion";
            object = ObjectFactory.findProviderClass("org.apache.xalan.processor.XSLProcessorVersion", ObjectFactory.findClassLoader(), true);
            arrclass = new StringBuffer();
            field = object.getField("S_VERSION");
            arrclass.append(field.get(null));
            hashtable.put("version.xalan2x", arrclass.toString());
        }
        catch (Exception exception) {
            hashtable.put("version.xalan2x", "not-present");
        }
        try {
            string = "org.apache.xalan.Version";
            object = "getVersion";
            arrclass = new Class[]{};
            field = ObjectFactory.findProviderClass("org.apache.xalan.Version", ObjectFactory.findClassLoader(), true);
            Method method = field.getMethod("getVersion", arrclass);
            Object object2 = method.invoke(null, new Object[0]);
            hashtable.put("version.xalan2_2", (String)object2);
        }
        catch (Exception exception) {
            hashtable.put("version.xalan2_2", "not-present");
        }
    }

    protected void checkParserVersion(Hashtable hashtable) {
        Field field;
        String string;
        Class class_;
        String string2;
        if (null == hashtable) {
            hashtable = new Hashtable<String, String>();
        }
        try {
            string = "org.apache.xerces.framework.Version";
            class_ = ObjectFactory.findProviderClass("org.apache.xerces.framework.Version", ObjectFactory.findClassLoader(), true);
            field = class_.getField("fVersion");
            string2 = (String)field.get(null);
            hashtable.put("version.xerces1", string2);
        }
        catch (Exception exception) {
            hashtable.put("version.xerces1", "not-present");
        }
        try {
            string = "org.apache.xerces.impl.Version";
            class_ = ObjectFactory.findProviderClass("org.apache.xerces.impl.Version", ObjectFactory.findClassLoader(), true);
            field = class_.getField("fVersion");
            string2 = (String)field.get(null);
            hashtable.put("version.xerces2", string2);
        }
        catch (Exception exception) {
            hashtable.put("version.xerces2", "not-present");
        }
        try {
            string = "org.apache.crimson.parser.Parser2";
            class_ = ObjectFactory.findProviderClass("org.apache.crimson.parser.Parser2", ObjectFactory.findClassLoader(), true);
            hashtable.put("version.crimson", "present-unknown-version");
        }
        catch (Exception exception) {
            hashtable.put("version.crimson", "not-present");
        }
    }

    protected void checkAntVersion(Hashtable hashtable) {
        if (null == hashtable) {
            hashtable = new Hashtable<String, String>();
        }
        try {
            String string = "org.apache.tools.ant.Main";
            String string2 = "getAntVersion";
            Class[] arrclass = new Class[]{};
            Class class_ = ObjectFactory.findProviderClass("org.apache.tools.ant.Main", ObjectFactory.findClassLoader(), true);
            Method method = class_.getMethod("getAntVersion", arrclass);
            Object object = method.invoke(null, new Object[0]);
            hashtable.put("version.ant", (String)object);
        }
        catch (Exception exception) {
            hashtable.put("version.ant", "not-present");
        }
    }

    protected void checkDOMVersion(Hashtable hashtable) {
        if (null == hashtable) {
            hashtable = new Hashtable<String, String>();
        }
        String string = "org.w3c.dom.Document";
        String string2 = "createElementNS";
        String string3 = "org.w3c.dom.Node";
        String string4 = "supported";
        String string5 = "org.w3c.dom.Node";
        String string6 = "isSupported";
        Class[] arrclass = new Class[2];
        Class class_ = class$java$lang$String == null ? (EnvironmentCheck.class$java$lang$String = EnvironmentCheck.class$("java.lang.String")) : class$java$lang$String;
        arrclass[0] = class_;
        arrclass[1] = class$java$lang$String == null ? (EnvironmentCheck.class$java$lang$String = EnvironmentCheck.class$("java.lang.String")) : class$java$lang$String;
        Class[] arrclass2 = arrclass;
        try {
            Class class_2 = ObjectFactory.findProviderClass("org.w3c.dom.Document", ObjectFactory.findClassLoader(), true);
            Method method = class_2.getMethod("createElementNS", arrclass2);
            hashtable.put("version.DOM", "2.0");
            try {
                class_2 = ObjectFactory.findProviderClass("org.w3c.dom.Node", ObjectFactory.findClassLoader(), true);
                method = class_2.getMethod("supported", arrclass2);
                hashtable.put("ERROR.version.DOM.draftlevel", "2.0wd");
                hashtable.put("ERROR.", "At least one error was found!");
            }
            catch (Exception exception) {
                try {
                    class_2 = ObjectFactory.findProviderClass("org.w3c.dom.Node", ObjectFactory.findClassLoader(), true);
                    method = class_2.getMethod("isSupported", arrclass2);
                    hashtable.put("version.DOM.draftlevel", "2.0fd");
                }
                catch (Exception exception2) {
                    hashtable.put("ERROR.version.DOM.draftlevel", "2.0unknown");
                    hashtable.put("ERROR.", "At least one error was found!");
                }
            }
        }
        catch (Exception exception) {
            hashtable.put("ERROR.version.DOM", "ERROR attempting to load DOM level 2 class: " + exception.toString());
            hashtable.put("ERROR.", "At least one error was found!");
        }
    }

    protected void checkSAXVersion(Hashtable hashtable) {
        if (null == hashtable) {
            hashtable = new Hashtable<String, String>();
        }
        String string = "org.xml.sax.Parser";
        String string2 = "parse";
        String string3 = "org.xml.sax.XMLReader";
        String string4 = "parse";
        String string5 = "org.xml.sax.helpers.AttributesImpl";
        String string6 = "setAttributes";
        Class[] arrclass = new Class[1];
        Class class_ = class$java$lang$String == null ? (EnvironmentCheck.class$java$lang$String = EnvironmentCheck.class$("java.lang.String")) : class$java$lang$String;
        arrclass[0] = class_;
        Class[] arrclass2 = arrclass;
        Class[] arrclass3 = new Class[1];
        Class class_2 = class$org$xml$sax$Attributes == null ? (EnvironmentCheck.class$org$xml$sax$Attributes = EnvironmentCheck.class$("org.xml.sax.Attributes")) : class$org$xml$sax$Attributes;
        arrclass3[0] = class_2;
        Class[] arrclass4 = arrclass3;
        try {
            Class class_3 = ObjectFactory.findProviderClass("org.xml.sax.helpers.AttributesImpl", ObjectFactory.findClassLoader(), true);
            Method method = class_3.getMethod("setAttributes", arrclass4);
            hashtable.put("version.SAX", "2.0");
        }
        catch (Exception exception) {
            hashtable.put("ERROR.version.SAX", "ERROR attempting to load SAX version 2 class: " + exception.toString());
            hashtable.put("ERROR.", "At least one error was found!");
            try {
                Class class_4 = ObjectFactory.findProviderClass("org.xml.sax.XMLReader", ObjectFactory.findClassLoader(), true);
                Method method = class_4.getMethod("parse", arrclass2);
                hashtable.put("version.SAX-backlevel", "2.0beta2-or-earlier");
            }
            catch (Exception exception2) {
                hashtable.put("ERROR.version.SAX", "ERROR attempting to load SAX version 2 class: " + exception.toString());
                hashtable.put("ERROR.", "At least one error was found!");
                try {
                    Class class_5 = ObjectFactory.findProviderClass("org.xml.sax.Parser", ObjectFactory.findClassLoader(), true);
                    Method method = class_5.getMethod("parse", arrclass2);
                    hashtable.put("version.SAX-backlevel", "1.0");
                }
                catch (Exception exception3) {
                    hashtable.put("ERROR.version.SAX-backlevel", "ERROR attempting to load SAX version 1 class: " + exception3.toString());
                }
            }
        }
    }

    protected void logMsg(String string) {
        this.outWriter.println(string);
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
        jarVersions.put(new Long(857192), "xalan.jar from xalan-j_1_1");
        jarVersions.put(new Long(440237), "xalan.jar from xalan-j_1_2");
        jarVersions.put(new Long(436094), "xalan.jar from xalan-j_1_2_1");
        jarVersions.put(new Long(426249), "xalan.jar from xalan-j_1_2_2");
        jarVersions.put(new Long(702536), "xalan.jar from xalan-j_2_0_0");
        jarVersions.put(new Long(720930), "xalan.jar from xalan-j_2_0_1");
        jarVersions.put(new Long(732330), "xalan.jar from xalan-j_2_1_0");
        jarVersions.put(new Long(872241), "xalan.jar from xalan-j_2_2_D10");
        jarVersions.put(new Long(882739), "xalan.jar from xalan-j_2_2_D11");
        jarVersions.put(new Long(923866), "xalan.jar from xalan-j_2_2_0");
        jarVersions.put(new Long(905872), "xalan.jar from xalan-j_2_3_D1");
        jarVersions.put(new Long(906122), "xalan.jar from xalan-j_2_3_0");
        jarVersions.put(new Long(906248), "xalan.jar from xalan-j_2_3_1");
        jarVersions.put(new Long(983377), "xalan.jar from xalan-j_2_4_D1");
        jarVersions.put(new Long(997276), "xalan.jar from xalan-j_2_4_0");
        jarVersions.put(new Long(1031036), "xalan.jar from xalan-j_2_4_1");
        jarVersions.put(new Long(596540), "xsltc.jar from xalan-j_2_2_0");
        jarVersions.put(new Long(590247), "xsltc.jar from xalan-j_2_3_D1");
        jarVersions.put(new Long(589914), "xsltc.jar from xalan-j_2_3_0");
        jarVersions.put(new Long(589915), "xsltc.jar from xalan-j_2_3_1");
        jarVersions.put(new Long(1306667), "xsltc.jar from xalan-j_2_4_D1");
        jarVersions.put(new Long(1328227), "xsltc.jar from xalan-j_2_4_0");
        jarVersions.put(new Long(1344009), "xsltc.jar from xalan-j_2_4_1");
        jarVersions.put(new Long(1348361), "xsltc.jar from xalan-j_2_5_D1");
        jarVersions.put(new Long(1268634), "xsltc.jar-bundled from xalan-j_2_3_0");
        jarVersions.put(new Long(100196), "xml-apis.jar from xalan-j_2_2_0 or xalan-j_2_3_D1");
        jarVersions.put(new Long(108484), "xml-apis.jar from xalan-j_2_3_0, or xalan-j_2_3_1 from xml-commons-1.0.b2");
        jarVersions.put(new Long(109049), "xml-apis.jar from xalan-j_2_4_0 from xml-commons RIVERCOURT1 branch");
        jarVersions.put(new Long(113749), "xml-apis.jar from xalan-j_2_4_1 from factoryfinder-build of xml-commons RIVERCOURT1");
        jarVersions.put(new Long(124704), "xml-apis.jar from tck-jaxp-1_2_0 branch of xml-commons");
        jarVersions.put(new Long(124724), "xml-apis.jar from tck-jaxp-1_2_0 branch of xml-commons, tag: xml-commons-external_1_2_01");
        jarVersions.put(new Long(194205), "xml-apis.jar from head branch of xml-commons, tag: xml-commons-external_1_3_02");
        jarVersions.put(new Long(424490), "xalan.jar from Xerces Tools releases - ERROR:DO NOT USE!");
        jarVersions.put(new Long(1591855), "xerces.jar from xalan-j_1_1 from xerces-1...");
        jarVersions.put(new Long(1498679), "xerces.jar from xalan-j_1_2 from xerces-1_2_0.bin");
        jarVersions.put(new Long(1484896), "xerces.jar from xalan-j_1_2_1 from xerces-1_2_1.bin");
        jarVersions.put(new Long(804460), "xerces.jar from xalan-j_1_2_2 from xerces-1_2_2.bin");
        jarVersions.put(new Long(1499244), "xerces.jar from xalan-j_2_0_0 from xerces-1_2_3.bin");
        jarVersions.put(new Long(1605266), "xerces.jar from xalan-j_2_0_1 from xerces-1_3_0.bin");
        jarVersions.put(new Long(904030), "xerces.jar from xalan-j_2_1_0 from xerces-1_4.bin");
        jarVersions.put(new Long(904030), "xerces.jar from xerces-1_4_0.bin");
        jarVersions.put(new Long(1802885), "xerces.jar from xerces-1_4_2.bin");
        jarVersions.put(new Long(1734594), "xerces.jar from Xerces-J-bin.2.0.0.beta3");
        jarVersions.put(new Long(1808883), "xerces.jar from xalan-j_2_2_D10,D11,D12 or xerces-1_4_3.bin");
        jarVersions.put(new Long(1812019), "xerces.jar from xalan-j_2_2_0");
        jarVersions.put(new Long(1720292), "xercesImpl.jar from xalan-j_2_3_D1");
        jarVersions.put(new Long(1730053), "xercesImpl.jar from xalan-j_2_3_0 or xalan-j_2_3_1 from xerces-2_0_0");
        jarVersions.put(new Long(1728861), "xercesImpl.jar from xalan-j_2_4_D1 from xerces-2_0_1");
        jarVersions.put(new Long(972027), "xercesImpl.jar from xalan-j_2_4_0 from xerces-2_1");
        jarVersions.put(new Long(831587), "xercesImpl.jar from xalan-j_2_4_1 from xerces-2_2");
        jarVersions.put(new Long(891817), "xercesImpl.jar from xalan-j_2_5_D1 from xerces-2_3");
        jarVersions.put(new Long(895924), "xercesImpl.jar from xerces-2_4");
        jarVersions.put(new Long(1010806), "xercesImpl.jar from Xerces-J-bin.2.6.2");
        jarVersions.put(new Long(1203860), "xercesImpl.jar from Xerces-J-bin.2.7.1");
        jarVersions.put(new Long(37485), "xalanj1compat.jar from xalan-j_2_0_0");
        jarVersions.put(new Long(38100), "xalanj1compat.jar from xalan-j_2_0_1");
        jarVersions.put(new Long(18779), "xalanservlet.jar from xalan-j_2_0_0");
        jarVersions.put(new Long(21453), "xalanservlet.jar from xalan-j_2_0_1");
        jarVersions.put(new Long(24826), "xalanservlet.jar from xalan-j_2_3_1 or xalan-j_2_4_1");
        jarVersions.put(new Long(24831), "xalanservlet.jar from xalan-j_2_4_1");
        jarVersions.put(new Long(5618), "jaxp.jar from jaxp1.0.1");
        jarVersions.put(new Long(136133), "parser.jar from jaxp1.0.1");
        jarVersions.put(new Long(28404), "jaxp.jar from jaxp-1.1");
        jarVersions.put(new Long(187162), "crimson.jar from jaxp-1.1");
        jarVersions.put(new Long(801714), "xalan.jar from jaxp-1.1");
        jarVersions.put(new Long(196399), "crimson.jar from crimson-1.1.1");
        jarVersions.put(new Long(33323), "jaxp.jar from crimson-1.1.1 or jakarta-ant-1.4.1b1");
        jarVersions.put(new Long(152717), "crimson.jar from crimson-1.1.2beta2");
        jarVersions.put(new Long(88143), "xml-apis.jar from crimson-1.1.2beta2");
        jarVersions.put(new Long(206384), "crimson.jar from crimson-1.1.3 or jakarta-ant-1.4.1b1");
        jarVersions.put(new Long(136198), "parser.jar from jakarta-ant-1.3 or 1.2");
        jarVersions.put(new Long(5537), "jaxp.jar from jakarta-ant-1.3 or 1.2");
    }
}

