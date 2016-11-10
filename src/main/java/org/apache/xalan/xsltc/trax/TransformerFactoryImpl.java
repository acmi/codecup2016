/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.trax.DOM2SAX;
import org.apache.xalan.xsltc.trax.ObjectFactory;
import org.apache.xalan.xsltc.trax.TemplatesHandlerImpl;
import org.apache.xalan.xsltc.trax.TemplatesImpl;
import org.apache.xalan.xsltc.trax.TrAXFilter;
import org.apache.xalan.xsltc.trax.TransformerHandlerImpl;
import org.apache.xalan.xsltc.trax.TransformerImpl;
import org.apache.xalan.xsltc.trax.Util;
import org.apache.xml.utils.StopParseException;
import org.apache.xml.utils.StylesheetPIHandler;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class TransformerFactoryImpl
extends SAXTransformerFactory
implements ErrorListener,
SourceLoader {
    public static final String TRANSLET_NAME = "translet-name";
    public static final String DESTINATION_DIRECTORY = "destination-directory";
    public static final String PACKAGE_NAME = "package-name";
    public static final String JAR_NAME = "jar-name";
    public static final String GENERATE_TRANSLET = "generate-translet";
    public static final String AUTO_TRANSLET = "auto-translet";
    public static final String USE_CLASSPATH = "use-classpath";
    public static final String DEBUG = "debug";
    public static final String ENABLE_INLINING = "enable-inlining";
    public static final String INDENT_NUMBER = "indent-number";
    private ErrorListener _errorListener;
    private URIResolver _uriResolver;
    protected static final String DEFAULT_TRANSLET_NAME = "GregorSamsa";
    private String _transletName;
    private String _destinationDirectory;
    private String _packageName;
    private String _jarFileName;
    private Hashtable _piParams;
    private boolean _debug;
    private boolean _enableInlining;
    private boolean _generateTranslet;
    private boolean _autoTranslet;
    private boolean _useClasspath;
    private int _indentNumber;
    private Class m_DTMManagerClass;
    private boolean _isSecureProcessing;

    public TransformerFactoryImpl() {
        this._errorListener = this;
        this._uriResolver = null;
        this._transletName = "GregorSamsa";
        this._destinationDirectory = null;
        this._packageName = null;
        this._jarFileName = null;
        this._piParams = null;
        this._debug = false;
        this._enableInlining = false;
        this._generateTranslet = false;
        this._autoTranslet = false;
        this._useClasspath = false;
        this._indentNumber = -1;
        this._isSecureProcessing = false;
        this.m_DTMManagerClass = XSLTCDTMManager.getDTMManagerClass();
    }

    public void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException {
        if (errorListener == null) {
            ErrorMsg errorMsg = new ErrorMsg("ERROR_LISTENER_NULL_ERR", "TransformerFactory");
            throw new IllegalArgumentException(errorMsg.toString());
        }
        this._errorListener = errorListener;
    }

    public ErrorListener getErrorListener() {
        return this._errorListener;
    }

    public Object getAttribute(String string) throws IllegalArgumentException {
        if (string.equals("translet-name")) {
            return this._transletName;
        }
        if (string.equals("generate-translet")) {
            return this._generateTranslet ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equals("auto-translet")) {
            return this._autoTranslet ? Boolean.TRUE : Boolean.FALSE;
        }
        if (string.equals("enable-inlining")) {
            if (this._enableInlining) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        ErrorMsg errorMsg = new ErrorMsg("JAXP_INVALID_ATTR_ERR", string);
        throw new IllegalArgumentException(errorMsg.toString());
    }

    public void setAttribute(String string, Object object) throws IllegalArgumentException {
        if (string.equals("translet-name") && object instanceof String) {
            this._transletName = (String)object;
            return;
        }
        if (string.equals("destination-directory") && object instanceof String) {
            this._destinationDirectory = (String)object;
            return;
        }
        if (string.equals("package-name") && object instanceof String) {
            this._packageName = (String)object;
            return;
        }
        if (string.equals("jar-name") && object instanceof String) {
            this._jarFileName = (String)object;
            return;
        }
        if (string.equals("generate-translet")) {
            if (object instanceof Boolean) {
                this._generateTranslet = (Boolean)object;
                return;
            }
            if (object instanceof String) {
                this._generateTranslet = ((String)object).equalsIgnoreCase("true");
                return;
            }
        } else if (string.equals("auto-translet")) {
            if (object instanceof Boolean) {
                this._autoTranslet = (Boolean)object;
                return;
            }
            if (object instanceof String) {
                this._autoTranslet = ((String)object).equalsIgnoreCase("true");
                return;
            }
        } else if (string.equals("use-classpath")) {
            if (object instanceof Boolean) {
                this._useClasspath = (Boolean)object;
                return;
            }
            if (object instanceof String) {
                this._useClasspath = ((String)object).equalsIgnoreCase("true");
                return;
            }
        } else if (string.equals("debug")) {
            if (object instanceof Boolean) {
                this._debug = (Boolean)object;
                return;
            }
            if (object instanceof String) {
                this._debug = ((String)object).equalsIgnoreCase("true");
                return;
            }
        } else if (string.equals("enable-inlining")) {
            if (object instanceof Boolean) {
                this._enableInlining = (Boolean)object;
                return;
            }
            if (object instanceof String) {
                this._enableInlining = ((String)object).equalsIgnoreCase("true");
                return;
            }
        } else if (string.equals("indent-number")) {
            if (object instanceof String) {
                try {
                    this._indentNumber = Integer.parseInt((String)object);
                    return;
                }
                catch (NumberFormatException numberFormatException) {}
            } else if (object instanceof Integer) {
                this._indentNumber = (Integer)object;
                return;
            }
        }
        ErrorMsg errorMsg = new ErrorMsg("JAXP_INVALID_ATTR_ERR", string);
        throw new IllegalArgumentException(errorMsg.toString());
    }

    public void setFeature(String string, boolean bl) throws TransformerConfigurationException {
        if (string == null) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_SET_FEATURE_NULL_NAME");
            throw new NullPointerException(errorMsg.toString());
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            this._isSecureProcessing = bl;
            return;
        }
        ErrorMsg errorMsg = new ErrorMsg("JAXP_UNSUPPORTED_FEATURE", string);
        throw new TransformerConfigurationException(errorMsg.toString());
    }

    public boolean getFeature(String string) {
        String[] arrstring = new String[]{"http://javax.xml.transform.dom.DOMSource/feature", "http://javax.xml.transform.dom.DOMResult/feature", "http://javax.xml.transform.sax.SAXSource/feature", "http://javax.xml.transform.sax.SAXResult/feature", "http://javax.xml.transform.stream.StreamSource/feature", "http://javax.xml.transform.stream.StreamResult/feature", "http://javax.xml.transform.sax.SAXTransformerFactory/feature", "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter"};
        if (string == null) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_GET_FEATURE_NULL_NAME");
            throw new NullPointerException(errorMsg.toString());
        }
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            if (!string.equals(arrstring[i2])) continue;
            return true;
        }
        if (string.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            return this._isSecureProcessing;
        }
        return false;
    }

    public URIResolver getURIResolver() {
        return this._uriResolver;
    }

    public void setURIResolver(URIResolver uRIResolver) {
        this._uriResolver = uRIResolver;
    }

    public Source getAssociatedStylesheet(Source source, String string, String string2, String string3) throws TransformerConfigurationException {
        XMLReader xMLReader = null;
        InputSource inputSource = null;
        StylesheetPIHandler stylesheetPIHandler = new StylesheetPIHandler(null, string, string2, string3);
        try {
            if (source instanceof DOMSource) {
                DOMSource dOMSource = (DOMSource)source;
                String string4 = dOMSource.getSystemId();
                Node node = dOMSource.getNode();
                DOM2SAX dOM2SAX = new DOM2SAX(node);
                stylesheetPIHandler.setBaseId(string4);
                dOM2SAX.setContentHandler(stylesheetPIHandler);
                dOM2SAX.parse();
            } else {
                SAXParser sAXParser;
                inputSource = SAXSource.sourceToInputSource(source);
                String string5 = inputSource.getSystemId();
                SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
                sAXParserFactory.setNamespaceAware(true);
                if (this._isSecureProcessing) {
                    try {
                        sAXParserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                    }
                    catch (SAXException sAXException) {
                        // empty catch block
                    }
                }
                if ((xMLReader = (sAXParser = sAXParserFactory.newSAXParser()).getXMLReader()) == null) {
                    xMLReader = XMLReaderFactory.createXMLReader();
                }
                stylesheetPIHandler.setBaseId(string5);
                xMLReader.setContentHandler(stylesheetPIHandler);
                xMLReader.parse(inputSource);
            }
            if (this._uriResolver != null) {
                stylesheetPIHandler.setURIResolver(this._uriResolver);
            }
        }
        catch (StopParseException stopParseException) {
        }
        catch (ParserConfigurationException parserConfigurationException) {
            throw new TransformerConfigurationException("getAssociatedStylesheets failed", parserConfigurationException);
        }
        catch (SAXException sAXException) {
            throw new TransformerConfigurationException("getAssociatedStylesheets failed", sAXException);
        }
        catch (IOException iOException) {
            throw new TransformerConfigurationException("getAssociatedStylesheets failed", iOException);
        }
        return stylesheetPIHandler.getAssociatedStylesheet();
    }

    public Transformer newTransformer() throws TransformerConfigurationException {
        TransformerImpl transformerImpl = new TransformerImpl(new Properties(), this._indentNumber, this);
        if (this._uriResolver != null) {
            transformerImpl.setURIResolver(this._uriResolver);
        }
        if (this._isSecureProcessing) {
            transformerImpl.setSecureProcessing(true);
        }
        return transformerImpl;
    }

    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        Templates templates = this.newTemplates(source);
        Transformer transformer = templates.newTransformer();
        if (this._uriResolver != null) {
            transformer.setURIResolver(this._uriResolver);
        }
        return transformer;
    }

    private void passWarningsToListener(Vector vector) throws TransformerException {
        if (this._errorListener == null || vector == null) {
            return;
        }
        int n2 = vector.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            ErrorMsg errorMsg = (ErrorMsg)vector.elementAt(i2);
            if (errorMsg.isWarningError()) {
                this._errorListener.error(new TransformerConfigurationException(errorMsg.toString()));
                continue;
            }
            this._errorListener.warning(new TransformerConfigurationException(errorMsg.toString()));
        }
    }

    private void passErrorsToListener(Vector vector) {
        try {
            if (this._errorListener == null || vector == null) {
                return;
            }
            int n2 = vector.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                String string = vector.elementAt(i2).toString();
                this._errorListener.error(new TransformerException(string));
            }
        }
        catch (TransformerException transformerException) {
            // empty catch block
        }
    }

    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        Object object;
        Object object2;
        String string;
        byte[][] arrby;
        Object object3;
        if (this._useClasspath) {
            String string2 = this.getTransletBaseName(source);
            if (this._packageName != null) {
                string2 = this._packageName + "." + string2;
            }
            try {
                Class class_ = ObjectFactory.findProviderClass(string2, ObjectFactory.findClassLoader(), true);
                this.resetTransientAttributes();
                return new TemplatesImpl(new Class[]{class_}, string2, null, this._indentNumber, this);
            }
            catch (ClassNotFoundException classNotFoundException) {
                ErrorMsg errorMsg = new ErrorMsg("CLASS_NOT_FOUND_ERR", string2);
                throw new TransformerConfigurationException(errorMsg.toString());
            }
            catch (Exception exception) {
                ErrorMsg errorMsg = new ErrorMsg(new ErrorMsg("RUNTIME_ERROR_KEY") + exception.getMessage());
                throw new TransformerConfigurationException(errorMsg.toString());
            }
        }
        if (this._autoTranslet) {
            arrby = null;
            object3 = this.getTransletBaseName(source);
            if (this._packageName != null) {
                object3 = this._packageName + "." + (String)object3;
            }
            arrby = this._jarFileName != null ? this.getBytecodesFromJar(source, (String)object3) : this.getBytecodesFromClasses(source, (String)object3);
            if (arrby != null) {
                if (this._debug) {
                    if (this._jarFileName != null) {
                        System.err.println(new ErrorMsg("TRANSFORM_WITH_JAR_STR", object3, (Object)this._jarFileName));
                    } else {
                        System.err.println(new ErrorMsg("TRANSFORM_WITH_TRANSLET_STR", object3));
                    }
                }
                this.resetTransientAttributes();
                return new TemplatesImpl(arrby, (String)object3, null, this._indentNumber, this);
            }
        }
        arrby = new XSLTC();
        if (this._debug) {
            arrby.setDebug(true);
        }
        if (this._enableInlining) {
            arrby.setTemplateInlining(true);
        } else {
            arrby.setTemplateInlining(false);
        }
        if (this._isSecureProcessing) {
            arrby.setSecureProcessing(true);
        }
        arrby.init();
        if (this._uriResolver != null) {
            arrby.setSourceLoader(this);
        }
        if (this._piParams != null && this._piParams.get(source) != null && (object3 = (PIParamWrapper)this._piParams.get(source)) != null) {
            arrby.setPIParameters(object3._media, object3._title, object3._charset);
        }
        int n2 = 2;
        if (this._generateTranslet || this._autoTranslet) {
            arrby.setClassName(this.getTransletBaseName(source));
            if (this._destinationDirectory != null) {
                arrby.setDestDirectory(this._destinationDirectory);
            } else {
                object = this.getStylesheetFileName(source);
                if (object != null && (string = (object2 = new File((String)object)).getParent()) != null) {
                    arrby.setDestDirectory(string);
                }
            }
            if (this._packageName != null) {
                arrby.setPackageName(this._packageName);
            }
            if (this._jarFileName != null) {
                arrby.setJarFileName(this._jarFileName);
                n2 = 5;
            } else {
                n2 = 4;
            }
        }
        object = Util.getInputSource((XSLTC)arrby, source);
        object2 = arrby.compile(null, (InputSource)object, n2);
        string = arrby.getClassName();
        if ((this._generateTranslet || this._autoTranslet) && object2 != null && this._jarFileName != null) {
            try {
                arrby.outputToJar();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        this.resetTransientAttributes();
        if (this._errorListener != this) {
            try {
                this.passWarningsToListener(arrby.getWarnings());
            }
            catch (TransformerException transformerException) {
                throw new TransformerConfigurationException(transformerException);
            }
        } else {
            arrby.printWarnings();
        }
        if (object2 == null) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_COMPILE_ERR");
            TransformerConfigurationException transformerConfigurationException = new TransformerConfigurationException(errorMsg.toString());
            if (this._errorListener != null) {
                this.passErrorsToListener(arrby.getErrors());
                try {
                    this._errorListener.fatalError(transformerConfigurationException);
                }
                catch (TransformerException transformerException) {}
            } else {
                arrby.printErrors();
            }
            throw transformerConfigurationException;
        }
        return new TemplatesImpl((byte[][])object2, string, arrby.getOutputProperties(), this._indentNumber, this);
    }

    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
        TemplatesHandlerImpl templatesHandlerImpl = new TemplatesHandlerImpl(this._indentNumber, this);
        if (this._uriResolver != null) {
            templatesHandlerImpl.setURIResolver(this._uriResolver);
        }
        return templatesHandlerImpl;
    }

    public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
        Transformer transformer = this.newTransformer();
        if (this._uriResolver != null) {
            transformer.setURIResolver(this._uriResolver);
        }
        return new TransformerHandlerImpl((TransformerImpl)transformer);
    }

    public TransformerHandler newTransformerHandler(Source source) throws TransformerConfigurationException {
        Transformer transformer = this.newTransformer(source);
        if (this._uriResolver != null) {
            transformer.setURIResolver(this._uriResolver);
        }
        return new TransformerHandlerImpl((TransformerImpl)transformer);
    }

    public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
        Transformer transformer = templates.newTransformer();
        TransformerImpl transformerImpl = (TransformerImpl)transformer;
        return new TransformerHandlerImpl(transformerImpl);
    }

    public XMLFilter newXMLFilter(Source source) throws TransformerConfigurationException {
        Templates templates = this.newTemplates(source);
        if (templates == null) {
            return null;
        }
        return this.newXMLFilter(templates);
    }

    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
        try {
            return new TrAXFilter(templates);
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            if (this._errorListener != null) {
                try {
                    this._errorListener.fatalError(transformerConfigurationException);
                    return null;
                }
                catch (TransformerException transformerException) {
                    new TransformerConfigurationException(transformerException);
                }
            }
            throw transformerConfigurationException;
        }
    }

    public void error(TransformerException transformerException) throws TransformerException {
        Throwable throwable = transformerException.getException();
        if (throwable != null) {
            System.err.println(new ErrorMsg("ERROR_PLUS_WRAPPED_MSG", (Object)transformerException.getMessageAndLocation(), (Object)throwable.getMessage()));
        } else {
            System.err.println(new ErrorMsg("ERROR_MSG", transformerException.getMessageAndLocation()));
        }
        throw transformerException;
    }

    public void fatalError(TransformerException transformerException) throws TransformerException {
        Throwable throwable = transformerException.getException();
        if (throwable != null) {
            System.err.println(new ErrorMsg("FATAL_ERR_PLUS_WRAPPED_MSG", (Object)transformerException.getMessageAndLocation(), (Object)throwable.getMessage()));
        } else {
            System.err.println(new ErrorMsg("FATAL_ERR_MSG", transformerException.getMessageAndLocation()));
        }
        throw transformerException;
    }

    public void warning(TransformerException transformerException) throws TransformerException {
        Throwable throwable = transformerException.getException();
        if (throwable != null) {
            System.err.println(new ErrorMsg("WARNING_PLUS_WRAPPED_MSG", (Object)transformerException.getMessageAndLocation(), (Object)throwable.getMessage()));
        } else {
            System.err.println(new ErrorMsg("WARNING_MSG", transformerException.getMessageAndLocation()));
        }
    }

    public InputSource loadSource(String string, String string2, XSLTC xSLTC) {
        try {
            Source source;
            if (this._uriResolver != null && (source = this._uriResolver.resolve(string, string2)) != null) {
                return Util.getInputSource(xSLTC, source);
            }
        }
        catch (TransformerException transformerException) {
            // empty catch block
        }
        return null;
    }

    private void resetTransientAttributes() {
        this._transletName = "GregorSamsa";
        this._destinationDirectory = null;
        this._packageName = null;
        this._jarFileName = null;
    }

    private byte[][] getBytecodesFromClasses(Source source, String string) {
        Object object;
        int n2;
        int n3;
        int n4;
        Object object2;
        byte[][] arrby;
        if (string == null) {
            return null;
        }
        String string2 = this.getStylesheetFileName(source);
        File file = null;
        if (string2 != null) {
            file = new File(string2);
        }
        String string3 = (n4 = string.lastIndexOf(46)) > 0 ? string.substring(n4 + 1) : string;
        String string4 = string.replace('.', '/');
        string4 = this._destinationDirectory != null ? this._destinationDirectory + "/" + string4 + ".class" : (file != null && file.getParent() != null ? file.getParent() + "/" + string4 + ".class" : string4 + ".class");
        File file2 = new File(string4);
        if (!file2.exists()) {
            return null;
        }
        if (file != null && file.exists()) {
            long l2 = file.lastModified();
            long l3 = file2.lastModified();
            if (l3 < l2) {
                return null;
            }
        }
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        int n5 = (int)file2.length();
        if (n5 > 0) {
            object2 = null;
            try {
                object2 = new FileInputStream(file2);
            }
            catch (FileNotFoundException fileNotFoundException) {
                return null;
            }
            object = new byte[n5];
            try {
                this.readFromInputStream((byte[])object, (InputStream)object2, n5);
                object2.close();
            }
            catch (IOException iOException) {
                return null;
            }
        }
        return null;
        arrayList.add((byte[])object);
        object2 = file2.getParent();
        if (object2 == null) {
            object2 = System.getProperty("user.dir");
        }
        object = new File((String)object2);
        String string5 = string3 + "$";
        File[] arrfile = object.listFiles(new FilenameFilter(this, string5){
            private final String val$transletAuxPrefix;
            private final TransformerFactoryImpl this$0;

            public boolean accept(File file, String string) {
                return string.endsWith(".class") && string.startsWith(this.val$transletAuxPrefix);
            }
        });
        for (n3 = 0; n3 < arrfile.length; ++n3) {
            arrby = arrfile[n3];
            n2 = (int)arrby.length();
            if (n2 <= 0) continue;
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream((File)arrby);
            }
            catch (FileNotFoundException fileNotFoundException) {
                continue;
            }
            byte[] arrby2 = new byte[n2];
            try {
                this.readFromInputStream(arrby2, fileInputStream, n2);
                fileInputStream.close();
            }
            catch (IOException iOException) {
                continue;
            }
            arrayList.add(arrby2);
        }
        n3 = arrayList.size();
        if (n3 > 0) {
            arrby = new byte[n3][1];
            for (n2 = 0; n2 < n3; ++n2) {
                arrby[n2] = (byte[])arrayList.get(n2);
            }
            return arrby;
        }
        return null;
    }

    private byte[][] getBytecodesFromJar(Source source, String string) {
        byte[][] arrby;
        String string2 = this.getStylesheetFileName(source);
        File file = null;
        if (string2 != null) {
            file = new File(string2);
        }
        String string3 = null;
        string3 = this._destinationDirectory != null ? this._destinationDirectory + "/" + this._jarFileName : (file != null && file.getParent() != null ? file.getParent() + "/" + this._jarFileName : this._jarFileName);
        File file2 = new File(string3);
        if (!file2.exists()) {
            return null;
        }
        if (file != null && file.exists()) {
            long l2 = file.lastModified();
            long l3 = file2.lastModified();
            if (l3 < l2) {
                return null;
            }
        }
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file2);
        }
        catch (IOException iOException) {
            return null;
        }
        String string4 = string.replace('.', '/');
        String string5 = string4 + "$";
        String string6 = string4 + ".class";
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = enumeration.nextElement();
            arrby = zipEntry.getName();
            if (zipEntry.getSize() <= 0 || !arrby.equals(string6) && (!arrby.endsWith(".class") || !arrby.startsWith(string5))) continue;
            try {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                int n2 = (int)zipEntry.getSize();
                byte[] arrby2 = new byte[n2];
                this.readFromInputStream(arrby2, inputStream, n2);
                inputStream.close();
                arrayList.add(arrby2);
                continue;
            }
            catch (IOException iOException) {
                return null;
            }
        }
        int n3 = arrayList.size();
        if (n3 > 0) {
            arrby = new byte[n3][1];
            for (int i2 = 0; i2 < n3; ++i2) {
                arrby[i2] = (byte[])arrayList.get(i2);
            }
            return arrby;
        }
        return null;
    }

    private void readFromInputStream(byte[] arrby, InputStream inputStream, int n2) throws IOException {
        int n3 = 0;
        int n4 = 0;
        for (int i2 = n2; i2 > 0 && (n3 = inputStream.read(arrby, n4, i2)) > 0; i2 -= n3) {
            n4 += n3;
        }
    }

    private String getTransletBaseName(Source source) {
        String string;
        String string2 = null;
        if (!this._transletName.equals("GregorSamsa")) {
            return this._transletName;
        }
        String string3 = source.getSystemId();
        if (string3 != null && (string = Util.baseName(string3)) != null) {
            string = Util.noExtName(string);
            string2 = Util.toJavaName(string);
        }
        return string2 != null ? string2 : "GregorSamsa";
    }

    private String getStylesheetFileName(Source source) {
        String string = source.getSystemId();
        if (string != null) {
            File file = new File(string);
            if (file.exists()) {
                return string;
            }
            URL uRL = null;
            try {
                uRL = new URL(string);
            }
            catch (MalformedURLException malformedURLException) {
                return null;
            }
            if ("file".equals(uRL.getProtocol())) {
                return uRL.getFile();
            }
            return null;
        }
        return null;
    }

    protected Class getDTMManagerClass() {
        return this.m_DTMManagerClass;
    }

    private static class PIParamWrapper {
        public String _media = null;
        public String _title = null;
        public String _charset = null;

        public PIParamWrapper(String string, String string2, String string3) {
            this._media = string;
            this._title = string2;
            this._charset = string3;
        }
    }

}

