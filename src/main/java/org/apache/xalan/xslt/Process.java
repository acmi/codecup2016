/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xslt;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ListResourceBundle;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.Version;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.trace.PrintTraceListener;
import org.apache.xalan.trace.TraceListener;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.xslt.ObjectFactory;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.WrappedRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Process {
    protected static void printArgOptions(ResourceBundle resourceBundle) {
        System.out.println(resourceBundle.getString("xslProc_option"));
        System.out.println("\n\t\t\t" + resourceBundle.getString("xslProc_common_options") + "\n");
        System.out.println(resourceBundle.getString("optionXSLTC"));
        System.out.println(resourceBundle.getString("optionIN"));
        System.out.println(resourceBundle.getString("optionXSL"));
        System.out.println(resourceBundle.getString("optionOUT"));
        System.out.println(resourceBundle.getString("optionV"));
        System.out.println(resourceBundle.getString("optionEDUMP"));
        System.out.println(resourceBundle.getString("optionXML"));
        System.out.println(resourceBundle.getString("optionTEXT"));
        System.out.println(resourceBundle.getString("optionHTML"));
        System.out.println(resourceBundle.getString("optionPARAM"));
        System.out.println(resourceBundle.getString("optionMEDIA"));
        System.out.println(resourceBundle.getString("optionFLAVOR"));
        System.out.println(resourceBundle.getString("optionDIAG"));
        System.out.println(resourceBundle.getString("optionURIRESOLVER"));
        System.out.println(resourceBundle.getString("optionENTITYRESOLVER"));
        Process.waitForReturnKey(resourceBundle);
        System.out.println(resourceBundle.getString("optionCONTENTHANDLER"));
        System.out.println(resourceBundle.getString("optionSECUREPROCESSING"));
        System.out.println("\n\t\t\t" + resourceBundle.getString("xslProc_xalan_options") + "\n");
        System.out.println(resourceBundle.getString("optionQC"));
        System.out.println(resourceBundle.getString("optionTT"));
        System.out.println(resourceBundle.getString("optionTG"));
        System.out.println(resourceBundle.getString("optionTS"));
        System.out.println(resourceBundle.getString("optionTTC"));
        System.out.println(resourceBundle.getString("optionTCLASS"));
        System.out.println(resourceBundle.getString("optionLINENUMBERS"));
        System.out.println(resourceBundle.getString("optionINCREMENTAL"));
        System.out.println(resourceBundle.getString("optionNOOPTIMIMIZE"));
        System.out.println(resourceBundle.getString("optionRL"));
        System.out.println("\n\t\t\t" + resourceBundle.getString("xslProc_xsltc_options") + "\n");
        System.out.println(resourceBundle.getString("optionXO"));
        Process.waitForReturnKey(resourceBundle);
        System.out.println(resourceBundle.getString("optionXD"));
        System.out.println(resourceBundle.getString("optionXJ"));
        System.out.println(resourceBundle.getString("optionXP"));
        System.out.println(resourceBundle.getString("optionXN"));
        System.out.println(resourceBundle.getString("optionXX"));
        System.out.println(resourceBundle.getString("optionXT"));
    }

    public static void main(String[] arrstring) {
        PrintWriter printWriter;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        String string = null;
        boolean bl4 = false;
        PrintWriter printWriter2 = printWriter = new PrintWriter(System.err, true);
        ListResourceBundle listResourceBundle = XSLMessages.loadResourceBundle("org.apache.xalan.res.XSLTErrorResources");
        String string2 = "s2s";
        if (arrstring.length < 1) {
            Process.printArgOptions(listResourceBundle);
        } else {
            TransformerFactory transformerFactory;
            Object object;
            Object object2;
            boolean bl5 = false;
            for (int i2 = 0; i2 < arrstring.length; ++i2) {
                if (!"-XSLTC".equalsIgnoreCase(arrstring[i2])) continue;
                bl5 = true;
            }
            if (bl5) {
                String string3 = "javax.xml.transform.TransformerFactory";
                String string4 = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
                object = System.getProperties();
                object.put(string3, string4);
                System.setProperties((Properties)object);
            }
            try {
                transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setErrorListener(new DefaultErrorHandler(false));
            }
            catch (TransformerFactoryConfigurationError transformerFactoryConfigurationError) {
                transformerFactoryConfigurationError.printStackTrace(printWriter2);
                string = XSLMessages.createMessage("ER_NOT_SUCCESSFUL", null);
                printWriter.println(string);
                transformerFactory = null;
                Process.doExit(string);
            }
            boolean bl6 = false;
            boolean bl7 = false;
            object = null;
            String string5 = null;
            String string6 = null;
            String string7 = null;
            Object var18_22 = null;
            PrintTraceListener printTraceListener = null;
            String string8 = null;
            String string9 = null;
            Vector<Object> vector = new Vector<Object>();
            boolean bl8 = false;
            URIResolver uRIResolver = null;
            EntityResolver entityResolver = null;
            ContentHandler contentHandler = null;
            int n2 = -1;
            for (int i3 = 0; i3 < arrstring.length; ++i3) {
                if ("-XSLTC".equalsIgnoreCase(arrstring[i3])) continue;
                if ("-TT".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        if (null == printTraceListener) {
                            printTraceListener = new PrintTraceListener(printWriter);
                        }
                        printTraceListener.m_traceTemplates = true;
                        continue;
                    }
                    Process.printInvalidXSLTCOption("-TT");
                    continue;
                }
                if ("-TG".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        if (null == printTraceListener) {
                            printTraceListener = new PrintTraceListener(printWriter);
                        }
                        printTraceListener.m_traceGeneration = true;
                        continue;
                    }
                    Process.printInvalidXSLTCOption("-TG");
                    continue;
                }
                if ("-TS".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        if (null == printTraceListener) {
                            printTraceListener = new PrintTraceListener(printWriter);
                        }
                        printTraceListener.m_traceSelection = true;
                        continue;
                    }
                    Process.printInvalidXSLTCOption("-TS");
                    continue;
                }
                if ("-TTC".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        if (null == printTraceListener) {
                            printTraceListener = new PrintTraceListener(printWriter);
                        }
                        printTraceListener.m_traceElements = true;
                        continue;
                    }
                    Process.printInvalidXSLTCOption("-TTC");
                    continue;
                }
                if ("-INDENT".equalsIgnoreCase(arrstring[i3])) {
                    int n3;
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        n3 = Integer.parseInt(arrstring[++i3]);
                        continue;
                    }
                    n3 = 0;
                    continue;
                }
                if ("-IN".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        object = arrstring[++i3];
                        continue;
                    }
                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-IN"}));
                    continue;
                }
                if ("-MEDIA".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 1 < arrstring.length) {
                        string9 = arrstring[++i3];
                        continue;
                    }
                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-MEDIA"}));
                    continue;
                }
                if ("-OUT".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        string5 = arrstring[++i3];
                        continue;
                    }
                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-OUT"}));
                    continue;
                }
                if ("-XSL".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        string7 = arrstring[++i3];
                        continue;
                    }
                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XSL"}));
                    continue;
                }
                if ("-FLAVOR".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 1 < arrstring.length) {
                        string2 = arrstring[++i3];
                        continue;
                    }
                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-FLAVOR"}));
                    continue;
                }
                if ("-PARAM".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 2 < arrstring.length) {
                        String string10 = arrstring[++i3];
                        vector.addElement(string10);
                        object2 = arrstring[++i3];
                        vector.addElement(object2);
                        continue;
                    }
                    System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-PARAM"}));
                    continue;
                }
                if ("-E".equalsIgnoreCase(arrstring[i3])) continue;
                if ("-V".equalsIgnoreCase(arrstring[i3])) {
                    printWriter.println(listResourceBundle.getString("version") + Version.getVersion() + ", " + listResourceBundle.getString("version2"));
                    continue;
                }
                if ("-QC".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        bl8 = true;
                        continue;
                    }
                    Process.printInvalidXSLTCOption("-QC");
                    continue;
                }
                if ("-Q".equalsIgnoreCase(arrstring[i3])) {
                    bl2 = true;
                    continue;
                }
                if ("-DIAG".equalsIgnoreCase(arrstring[i3])) {
                    bl3 = true;
                    continue;
                }
                if ("-XML".equalsIgnoreCase(arrstring[i3])) {
                    string8 = "xml";
                    continue;
                }
                if ("-TEXT".equalsIgnoreCase(arrstring[i3])) {
                    string8 = "text";
                    continue;
                }
                if ("-HTML".equalsIgnoreCase(arrstring[i3])) {
                    string8 = "html";
                    continue;
                }
                if ("-EDUMP".equalsIgnoreCase(arrstring[i3])) {
                    bl = true;
                    if (i3 + 1 >= arrstring.length || arrstring[i3 + 1].charAt(0) == '-') continue;
                    string6 = arrstring[++i3];
                    continue;
                }
                if ("-URIRESOLVER".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 1 < arrstring.length) {
                        try {
                            uRIResolver = (URIResolver)ObjectFactory.newInstance(arrstring[++i3], ObjectFactory.findClassLoader(), true);
                            transformerFactory.setURIResolver(uRIResolver);
                        }
                        catch (ObjectFactory.ConfigurationError configurationError) {
                            string = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-URIResolver"});
                            System.err.println(string);
                            Process.doExit(string);
                        }
                        continue;
                    }
                    string = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-URIResolver"});
                    System.err.println(string);
                    Process.doExit(string);
                    continue;
                }
                if ("-ENTITYRESOLVER".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 1 < arrstring.length) {
                        try {
                            entityResolver = (EntityResolver)ObjectFactory.newInstance(arrstring[++i3], ObjectFactory.findClassLoader(), true);
                        }
                        catch (ObjectFactory.ConfigurationError configurationError) {
                            string = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-EntityResolver"});
                            System.err.println(string);
                            Process.doExit(string);
                        }
                        continue;
                    }
                    string = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-EntityResolver"});
                    System.err.println(string);
                    Process.doExit(string);
                    continue;
                }
                if ("-CONTENTHANDLER".equalsIgnoreCase(arrstring[i3])) {
                    if (i3 + 1 < arrstring.length) {
                        try {
                            contentHandler = (ContentHandler)ObjectFactory.newInstance(arrstring[++i3], ObjectFactory.findClassLoader(), true);
                        }
                        catch (ObjectFactory.ConfigurationError configurationError) {
                            string = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-ContentHandler"});
                            System.err.println(string);
                            Process.doExit(string);
                        }
                        continue;
                    }
                    string = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-ContentHandler"});
                    System.err.println(string);
                    Process.doExit(string);
                    continue;
                }
                if ("-L".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        transformerFactory.setAttribute("http://xml.apache.org/xalan/properties/source-location", Boolean.TRUE);
                        continue;
                    }
                    Process.printInvalidXSLTCOption("-L");
                    continue;
                }
                if ("-INCREMENTAL".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        transformerFactory.setAttribute("http://xml.apache.org/xalan/features/incremental", Boolean.TRUE);
                        continue;
                    }
                    Process.printInvalidXSLTCOption("-INCREMENTAL");
                    continue;
                }
                if ("-NOOPTIMIZE".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        transformerFactory.setAttribute("http://xml.apache.org/xalan/features/optimize", Boolean.FALSE);
                        continue;
                    }
                    Process.printInvalidXSLTCOption("-NOOPTIMIZE");
                    continue;
                }
                if ("-RL".equalsIgnoreCase(arrstring[i3])) {
                    if (!bl5) {
                        if (i3 + 1 < arrstring.length) {
                            n2 = Integer.parseInt(arrstring[++i3]);
                            continue;
                        }
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-rl"}));
                        continue;
                    }
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        ++i3;
                    }
                    Process.printInvalidXSLTCOption("-RL");
                    continue;
                }
                if ("-XO".equalsIgnoreCase(arrstring[i3])) {
                    if (bl5) {
                        if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                            transformerFactory.setAttribute("generate-translet", "true");
                            transformerFactory.setAttribute("translet-name", arrstring[++i3]);
                            continue;
                        }
                        transformerFactory.setAttribute("generate-translet", "true");
                        continue;
                    }
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        ++i3;
                    }
                    Process.printInvalidXalanOption("-XO");
                    continue;
                }
                if ("-XD".equalsIgnoreCase(arrstring[i3])) {
                    if (bl5) {
                        if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                            transformerFactory.setAttribute("destination-directory", arrstring[++i3]);
                            continue;
                        }
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XD"}));
                        continue;
                    }
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        ++i3;
                    }
                    Process.printInvalidXalanOption("-XD");
                    continue;
                }
                if ("-XJ".equalsIgnoreCase(arrstring[i3])) {
                    if (bl5) {
                        if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                            transformerFactory.setAttribute("generate-translet", "true");
                            transformerFactory.setAttribute("jar-name", arrstring[++i3]);
                            continue;
                        }
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XJ"}));
                        continue;
                    }
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        ++i3;
                    }
                    Process.printInvalidXalanOption("-XJ");
                    continue;
                }
                if ("-XP".equalsIgnoreCase(arrstring[i3])) {
                    if (bl5) {
                        if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                            transformerFactory.setAttribute("package-name", arrstring[++i3]);
                            continue;
                        }
                        System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XP"}));
                        continue;
                    }
                    if (i3 + 1 < arrstring.length && arrstring[i3 + 1].charAt(0) != '-') {
                        ++i3;
                    }
                    Process.printInvalidXalanOption("-XP");
                    continue;
                }
                if ("-XN".equalsIgnoreCase(arrstring[i3])) {
                    if (bl5) {
                        transformerFactory.setAttribute("enable-inlining", "true");
                        continue;
                    }
                    Process.printInvalidXalanOption("-XN");
                    continue;
                }
                if ("-XX".equalsIgnoreCase(arrstring[i3])) {
                    if (bl5) {
                        transformerFactory.setAttribute("debug", "true");
                        continue;
                    }
                    Process.printInvalidXalanOption("-XX");
                    continue;
                }
                if ("-XT".equalsIgnoreCase(arrstring[i3])) {
                    if (bl5) {
                        transformerFactory.setAttribute("auto-translet", "true");
                        continue;
                    }
                    Process.printInvalidXalanOption("-XT");
                    continue;
                }
                if ("-SECURE".equalsIgnoreCase(arrstring[i3])) {
                    bl4 = true;
                    try {
                        transformerFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                    }
                    catch (TransformerConfigurationException transformerConfigurationException) {}
                    continue;
                }
                System.err.println(XSLMessages.createMessage("ER_INVALID_OPTION", new Object[]{arrstring[i3]}));
            }
            if (object == null && string7 == null) {
                string = listResourceBundle.getString("xslProc_no_input");
                System.err.println(string);
                Process.doExit(string);
            }
            try {
                Object object3;
                Object object4;
                Object object5;
                Object object6;
                long l2 = System.currentTimeMillis();
                if (null != string6) {
                    printWriter2 = new PrintWriter(new FileWriter(string6));
                }
                object2 = null;
                if (null != string7) {
                    if (string2.equals("d2d")) {
                        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                        documentBuilderFactory.setNamespaceAware(true);
                        if (bl4) {
                            try {
                                documentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                            }
                            catch (ParserConfigurationException parserConfigurationException) {
                                // empty catch block
                            }
                        }
                        object6 = documentBuilderFactory.newDocumentBuilder();
                        object4 = object6.parse(new InputSource(string7));
                        object2 = transformerFactory.newTemplates(new DOMSource((Node)object4, string7));
                    } else {
                        object2 = transformerFactory.newTemplates(new StreamSource(string7));
                    }
                }
                if (null != string5) {
                    object6 = new StreamResult(new FileOutputStream(string5));
                    object6.setSystemId(string5);
                } else {
                    object6 = new StreamResult(System.out);
                }
                object4 = (SAXTransformerFactory)transformerFactory;
                if (!bl5 && bl7) {
                    object4.setAttribute("http://xml.apache.org/xalan/properties/source-location", Boolean.TRUE);
                }
                if (null == object2) {
                    object3 = object4.getAssociatedStylesheet(new StreamSource((String)object), string9, null, null);
                    if (null != object3) {
                        object2 = transformerFactory.newTemplates((Source)object3);
                    } else {
                        if (null != string9) {
                            throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEET_IN_MEDIA", new Object[]{object, string9}));
                        }
                        throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEET_PI", new Object[]{object}));
                    }
                }
                if (null != object2) {
                    int n4;
                    object3 = string2.equals("th") ? null : object2.newTransformer();
                    object3.setErrorListener(new DefaultErrorHandler(false));
                    if (null != string8) {
                        object3.setOutputProperty("method", string8);
                    }
                    if (object3 instanceof TransformerImpl) {
                        TransformerImpl transformerImpl = (TransformerImpl)object3;
                        TraceManager traceManager = transformerImpl.getTraceManager();
                        if (null != printTraceListener) {
                            traceManager.addTraceListener(printTraceListener);
                        }
                        transformerImpl.setQuietConflictWarnings(bl8);
                        if (bl7) {
                            transformerImpl.setProperty("http://xml.apache.org/xalan/properties/source-location", Boolean.TRUE);
                        }
                        if (n2 > 0) {
                            transformerImpl.setRecursionLimit(n2);
                        }
                    }
                    int n5 = vector.size();
                    for (n4 = 0; n4 < n5; n4 += 2) {
                        object3.setParameter((String)vector.elementAt(n4), (String)vector.elementAt(n4 + 1));
                    }
                    if (uRIResolver != null) {
                        object3.setURIResolver(uRIResolver);
                    }
                    if (null != object) {
                        if (string2.equals("d2d")) {
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                            documentBuilderFactory.setCoalescing(true);
                            documentBuilderFactory.setNamespaceAware(true);
                            if (bl4) {
                                try {
                                    documentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                                }
                                catch (ParserConfigurationException parserConfigurationException) {
                                    // empty catch block
                                }
                            }
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            if (entityResolver != null) {
                                documentBuilder.setEntityResolver(entityResolver);
                            }
                            object5 = documentBuilder.parse(new InputSource((String)object));
                            Document document = documentBuilder.newDocument();
                            DocumentFragment documentFragment = document.createDocumentFragment();
                            object3.transform(new DOMSource((Node)object5, (String)object), new DOMResult(documentFragment));
                            Transformer transformer = object4.newTransformer();
                            transformer.setErrorListener(new DefaultErrorHandler(false));
                            Properties properties = object2.getOutputProperties();
                            transformer.setOutputProperties(properties);
                            if (contentHandler != null) {
                                SAXResult sAXResult = new SAXResult(contentHandler);
                                transformer.transform(new DOMSource(documentFragment), sAXResult);
                            } else {
                                transformer.transform(new DOMSource(documentFragment), (Result)object6);
                            }
                        } else if (string2.equals("th")) {
                            for (n4 = 0; n4 < 1; ++n4) {
                                XMLReader xMLReader = null;
                                try {
                                    object5 = SAXParserFactory.newInstance();
                                    object5.setNamespaceAware(true);
                                    if (bl4) {
                                        try {
                                            object5.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                                        }
                                        catch (SAXException sAXException) {
                                            // empty catch block
                                        }
                                    }
                                    SAXParser sAXParser = object5.newSAXParser();
                                    xMLReader = sAXParser.getXMLReader();
                                }
                                catch (ParserConfigurationException parserConfigurationException) {
                                    throw new SAXException(parserConfigurationException);
                                }
                                catch (FactoryConfigurationError factoryConfigurationError) {
                                    throw new SAXException(factoryConfigurationError.toString());
                                }
                                catch (NoSuchMethodError noSuchMethodError) {
                                }
                                catch (AbstractMethodError abstractMethodError) {
                                    // empty catch block
                                }
                                if (null == xMLReader) {
                                    xMLReader = XMLReaderFactory.createXMLReader();
                                }
                                if (!bl5) {
                                    object4.setAttribute("http://xml.apache.org/xalan/features/incremental", Boolean.TRUE);
                                }
                                object5 = object4.newTransformerHandler((Templates)object2);
                                xMLReader.setContentHandler((ContentHandler)object5);
                                xMLReader.setDTDHandler((DTDHandler)object5);
                                if (object5 instanceof ErrorHandler) {
                                    xMLReader.setErrorHandler((ErrorHandler)object5);
                                }
                                try {
                                    xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", object5);
                                }
                                catch (SAXNotRecognizedException sAXNotRecognizedException) {
                                }
                                catch (SAXNotSupportedException sAXNotSupportedException) {
                                    // empty catch block
                                }
                                try {
                                    xMLReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                                }
                                catch (SAXException sAXException) {
                                    // empty catch block
                                }
                                object5.setResult((Result)object6);
                                xMLReader.parse(new InputSource((String)object));
                            }
                        } else if (entityResolver != null) {
                            Object object7;
                            XMLReader xMLReader = null;
                            try {
                                object7 = SAXParserFactory.newInstance();
                                object7.setNamespaceAware(true);
                                if (bl4) {
                                    try {
                                        object7.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                                    }
                                    catch (SAXException sAXException) {
                                        // empty catch block
                                    }
                                }
                                object5 = object7.newSAXParser();
                                xMLReader = object5.getXMLReader();
                            }
                            catch (ParserConfigurationException parserConfigurationException) {
                                throw new SAXException(parserConfigurationException);
                            }
                            catch (FactoryConfigurationError factoryConfigurationError) {
                                throw new SAXException(factoryConfigurationError.toString());
                            }
                            catch (NoSuchMethodError noSuchMethodError) {
                            }
                            catch (AbstractMethodError abstractMethodError) {
                                // empty catch block
                            }
                            if (null == xMLReader) {
                                xMLReader = XMLReaderFactory.createXMLReader();
                            }
                            xMLReader.setEntityResolver(entityResolver);
                            if (contentHandler != null) {
                                object7 = new SAXResult(contentHandler);
                                object3.transform(new SAXSource(xMLReader, new InputSource((String)object)), (Result)object7);
                            } else {
                                object3.transform(new SAXSource(xMLReader, new InputSource((String)object)), (Result)object6);
                            }
                        } else if (contentHandler != null) {
                            SAXResult sAXResult = new SAXResult(contentHandler);
                            object3.transform(new StreamSource((String)object), sAXResult);
                        } else {
                            object3.transform(new StreamSource((String)object), (Result)object6);
                        }
                    } else {
                        StringReader stringReader = new StringReader("<?xml version=\"1.0\"?> <doc/>");
                        object3.transform(new StreamSource(stringReader), (Result)object6);
                    }
                } else {
                    string = XSLMessages.createMessage("ER_NOT_SUCCESSFUL", null);
                    printWriter.println(string);
                    Process.doExit(string);
                }
                if (null != string5 && object6 != null) {
                    object3 = object6.getOutputStream();
                    Writer writer = object6.getWriter();
                    try {
                        if (object3 != null) {
                            object3.close();
                        }
                        if (writer != null) {
                            writer.close();
                        }
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                long l3 = System.currentTimeMillis();
                long l4 = l3 - l2;
                if (bl3) {
                    object5 = new Object[]{object, string7, new Long(l4)};
                    string = XSLMessages.createMessage("diagTiming", (Object[])object5);
                    printWriter.println('\n');
                    printWriter.println(string);
                }
            }
            catch (Throwable throwable) {
                Exception exception;
                while (exception instanceof WrappedRuntimeException) {
                    exception = ((WrappedRuntimeException)exception).getException();
                }
                if (exception instanceof NullPointerException || exception instanceof ClassCastException) {
                    bl = true;
                }
                printWriter.println();
                if (bl) {
                    exception.printStackTrace(printWriter2);
                } else {
                    DefaultErrorHandler.printLocation(printWriter, exception);
                    printWriter.println(XSLMessages.createMessage("ER_XSLT_ERROR", null) + " (" + exception.getClass().getName() + "): " + exception.getMessage());
                }
                if (null != string6) {
                    printWriter2.close();
                }
                Process.doExit(exception.getMessage());
            }
            if (null != string6) {
                printWriter2.close();
            }
            if (null != printWriter) {
                // empty if block
            }
        }
    }

    static void doExit(String string) {
        throw new RuntimeException(string);
    }

    private static void waitForReturnKey(ResourceBundle resourceBundle) {
        System.out.println(resourceBundle.getString("xslProc_return_to_continue"));
        try {
            while (System.in.read() != 10) {
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private static void printInvalidXSLTCOption(String string) {
        System.err.println(XSLMessages.createMessage("xslProc_invalid_xsltc_option", new Object[]{string}));
    }

    private static void printInvalidXalanOption(String string) {
        System.err.println(XSLMessages.createMessage("xslProc_invalid_xalan_option", new Object[]{string}));
    }
}

