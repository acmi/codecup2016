/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import org.apache.bcel.classfile.JavaClass;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public final class XSLTC {
    private Parser _parser;
    private XMLReader _reader = null;
    private SourceLoader _loader = null;
    private Stylesheet _stylesheet;
    private int _modeSerial = 1;
    private int _stylesheetSerial = 1;
    private int _stepPatternSerial = 1;
    private int _helperClassSerial = 0;
    private int _attributeSetSerial = 0;
    private int[] _numberFieldIndexes;
    private int _nextGType;
    private Vector _namesIndex;
    private Hashtable _elements;
    private Hashtable _attributes;
    private int _nextNSType;
    private Vector _namespaceIndex;
    private Hashtable _namespaces;
    private Hashtable _namespacePrefixes;
    private Vector m_characterData;
    public static final int FILE_OUTPUT = 0;
    public static final int JAR_OUTPUT = 1;
    public static final int BYTEARRAY_OUTPUT = 2;
    public static final int CLASSLOADER_OUTPUT = 3;
    public static final int BYTEARRAY_AND_FILE_OUTPUT = 4;
    public static final int BYTEARRAY_AND_JAR_OUTPUT = 5;
    private boolean _debug = false;
    private String _jarFileName = null;
    private String _className = null;
    private String _packageName = null;
    private File _destDir = null;
    private int _outputType = 0;
    private Vector _classes;
    private Vector _bcelClasses;
    private boolean _callsNodeset = false;
    private boolean _multiDocument = false;
    private boolean _hasIdCall = false;
    private Vector _stylesheetNSAncestorPointers;
    private Vector _prefixURIPairs;
    private Vector _prefixURIPairsIdx;
    private boolean _templateInlining = false;
    private boolean _isSecureProcessing = false;

    public XSLTC() {
        this._parser = new Parser(this);
    }

    public void setSecureProcessing(boolean bl) {
        this._isSecureProcessing = bl;
    }

    public boolean isSecureProcessing() {
        return this._isSecureProcessing;
    }

    public Parser getParser() {
        return this._parser;
    }

    public void setOutputType(int n2) {
        this._outputType = n2;
    }

    public Properties getOutputProperties() {
        return this._parser.getOutputProperties();
    }

    public void init() {
        this.reset();
        this._reader = null;
        this._classes = new Vector();
        this._bcelClasses = new Vector();
    }

    private void reset() {
        this._nextGType = 14;
        this._elements = new Hashtable();
        this._attributes = new Hashtable();
        this._namespaces = new Hashtable();
        this._namespaces.put("", new Integer(this._nextNSType));
        this._namesIndex = new Vector(128);
        this._namespaceIndex = new Vector(32);
        this._namespacePrefixes = new Hashtable();
        this._stylesheet = null;
        this._parser.init();
        this._modeSerial = 1;
        this._stylesheetSerial = 1;
        this._stepPatternSerial = 1;
        this._helperClassSerial = 0;
        this._attributeSetSerial = 0;
        this._multiDocument = false;
        this._hasIdCall = false;
        this._stylesheetNSAncestorPointers = null;
        this._prefixURIPairs = null;
        this._prefixURIPairsIdx = null;
        this._numberFieldIndexes = new int[]{-1, -1, -1};
    }

    public void setSourceLoader(SourceLoader sourceLoader) {
        this._loader = sourceLoader;
    }

    public void setTemplateInlining(boolean bl) {
        this._templateInlining = bl;
    }

    public boolean getTemplateInlining() {
        return this._templateInlining;
    }

    public void setPIParameters(String string, String string2, String string3) {
        this._parser.setPIParameters(string, string2, string3);
    }

    public boolean compile(URL uRL) {
        try {
            InputStream inputStream = uRL.openStream();
            InputSource inputSource = new InputSource(inputStream);
            inputSource.setSystemId(uRL.toString());
            return this.compile(inputSource, this._className);
        }
        catch (IOException iOException) {
            this._parser.reportError(2, new ErrorMsg(iOException));
            return false;
        }
    }

    public boolean compile(URL uRL, String string) {
        try {
            InputStream inputStream = uRL.openStream();
            InputSource inputSource = new InputSource(inputStream);
            inputSource.setSystemId(uRL.toString());
            return this.compile(inputSource, string);
        }
        catch (IOException iOException) {
            this._parser.reportError(2, new ErrorMsg(iOException));
            return false;
        }
    }

    public boolean compile(InputStream inputStream, String string) {
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setSystemId(string);
        return this.compile(inputSource, string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean compile(InputSource inputSource, String string) {
        block18 : {
            try {
                this.reset();
                String string2 = null;
                if (inputSource != null) {
                    string2 = inputSource.getSystemId();
                }
                if (this._className == null) {
                    if (string != null) {
                        this.setClassName(string);
                    } else if (string2 != null && string2.length() != 0) {
                        this.setClassName(Util.baseName(string2));
                    }
                    if (this._className == null || this._className.length() == 0) {
                        this.setClassName("GregorSamsa");
                    }
                }
                SyntaxTreeNode syntaxTreeNode = null;
                syntaxTreeNode = this._reader == null ? this._parser.parse(inputSource) : this._parser.parse(this._reader, inputSource);
                if (!this._parser.errorsFound() && syntaxTreeNode != null) {
                    this._stylesheet = this._parser.makeStylesheet(syntaxTreeNode);
                    this._stylesheet.setSourceLoader(this._loader);
                    this._stylesheet.setSystemId(string2);
                    this._stylesheet.setParentStylesheet(null);
                    this._stylesheet.setTemplateInlining(this._templateInlining);
                    this._parser.setCurrentStylesheet(this._stylesheet);
                    this._parser.createAST(this._stylesheet);
                }
                if (this._parser.errorsFound() || this._stylesheet == null) break block18;
                this._stylesheet.setCallsNodeset(this._callsNodeset);
                this._stylesheet.setMultiDocument(this._multiDocument);
                this._stylesheet.setHasIdCall(this._hasIdCall);
                Class class_ = this.getClass();
                synchronized (class_) {
                    this._stylesheet.translate();
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                this._parser.reportError(2, new ErrorMsg(exception));
            }
            catch (Error error) {
                if (this._debug) {
                    error.printStackTrace();
                }
                this._parser.reportError(2, new ErrorMsg(error));
            }
            finally {
                this._reader = null;
            }
        }
        return !this._parser.errorsFound();
    }

    public boolean compile(Vector vector) {
        int n2 = vector.size();
        if (n2 == 0) {
            return true;
        }
        if (n2 == 1) {
            Object e2 = vector.firstElement();
            if (e2 instanceof URL) {
                return this.compile((URL)e2);
            }
            return false;
        }
        Enumeration enumeration = vector.elements();
        while (enumeration.hasMoreElements()) {
            this._className = null;
            Object e3 = enumeration.nextElement();
            if (!(e3 instanceof URL) || this.compile((URL)e3)) continue;
            return false;
        }
        return true;
    }

    public byte[][] getBytecodes() {
        int n2 = this._classes.size();
        byte[][] arrby = new byte[n2][1];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrby[i2] = (byte[])this._classes.elementAt(i2);
        }
        return arrby;
    }

    public byte[][] compile(String string, InputSource inputSource, int n2) {
        this._outputType = n2;
        if (this.compile(inputSource, string)) {
            return this.getBytecodes();
        }
        return null;
    }

    public byte[][] compile(String string, InputSource inputSource) {
        return this.compile(string, inputSource, 2);
    }

    public void setXMLReader(XMLReader xMLReader) {
        this._reader = xMLReader;
    }

    public XMLReader getXMLReader() {
        return this._reader;
    }

    public Vector getErrors() {
        return this._parser.getErrors();
    }

    public Vector getWarnings() {
        return this._parser.getWarnings();
    }

    public void printErrors() {
        this._parser.printErrors();
    }

    public void printWarnings() {
        this._parser.printWarnings();
    }

    protected void setMultiDocument(boolean bl) {
        this._multiDocument = bl;
    }

    public boolean isMultiDocument() {
        return this._multiDocument;
    }

    protected void setCallsNodeset(boolean bl) {
        if (bl) {
            this.setMultiDocument(bl);
        }
        this._callsNodeset = bl;
    }

    public boolean callsNodeset() {
        return this._callsNodeset;
    }

    protected void setHasIdCall(boolean bl) {
        this._hasIdCall = bl;
    }

    public boolean hasIdCall() {
        return this._hasIdCall;
    }

    public void setClassName(String string) {
        String string2 = Util.baseName(string);
        String string3 = Util.noExtName(string2);
        String string4 = Util.toJavaName(string3);
        this._className = this._packageName == null ? string4 : this._packageName + '.' + string4;
    }

    public String getClassName() {
        return this._className;
    }

    private String classFileName(String string) {
        return string.replace('.', File.separatorChar) + ".class";
    }

    private File getOutputFile(String string) {
        if (this._destDir != null) {
            return new File(this._destDir, this.classFileName(string));
        }
        return new File(this.classFileName(string));
    }

    public boolean setDestDirectory(String string) {
        File file = new File(string);
        if (file.exists() || file.mkdirs()) {
            this._destDir = file;
            return true;
        }
        this._destDir = null;
        return false;
    }

    public void setPackageName(String string) {
        this._packageName = string;
        if (this._className != null) {
            this.setClassName(this._className);
        }
    }

    public void setJarFileName(String string) {
        String string2 = ".jar";
        this._jarFileName = string.endsWith(".jar") ? string : string + ".jar";
        this._outputType = 1;
    }

    public String getJarFileName() {
        return this._jarFileName;
    }

    public void setStylesheet(Stylesheet stylesheet) {
        if (this._stylesheet == null) {
            this._stylesheet = stylesheet;
        }
    }

    public Stylesheet getStylesheet() {
        return this._stylesheet;
    }

    public int registerAttribute(QName qName) {
        Integer n2 = (Integer)this._attributes.get(qName.toString());
        if (n2 == null) {
            n2 = new Integer(this._nextGType++);
            this._attributes.put(qName.toString(), n2);
            String string = qName.getNamespace();
            String string2 = "@" + qName.getLocalPart();
            if (string != null && string.length() != 0) {
                this._namesIndex.addElement(string + ":" + string2);
            } else {
                this._namesIndex.addElement(string2);
            }
            if (qName.getLocalPart().equals("*")) {
                this.registerNamespace(qName.getNamespace());
            }
        }
        return n2;
    }

    public int registerElement(QName qName) {
        Integer n2 = (Integer)this._elements.get(qName.toString());
        if (n2 == null) {
            n2 = new Integer(this._nextGType++);
            this._elements.put(qName.toString(), n2);
            this._namesIndex.addElement(qName.toString());
        }
        if (qName.getLocalPart().equals("*")) {
            this.registerNamespace(qName.getNamespace());
        }
        return n2;
    }

    public int registerNamespacePrefix(QName qName) {
        Integer n2 = (Integer)this._namespacePrefixes.get(qName.toString());
        if (n2 == null) {
            n2 = new Integer(this._nextGType++);
            this._namespacePrefixes.put(qName.toString(), n2);
            String string = qName.getNamespace();
            if (string != null && string.length() != 0) {
                this._namesIndex.addElement("?");
            } else {
                this._namesIndex.addElement("?" + qName.getLocalPart());
            }
        }
        return n2;
    }

    public int registerNamespacePrefix(String string) {
        Integer n2 = (Integer)this._namespacePrefixes.get(string);
        if (n2 == null) {
            n2 = new Integer(this._nextGType++);
            this._namespacePrefixes.put(string, n2);
            this._namesIndex.addElement("?" + string);
        }
        return n2;
    }

    public int registerNamespace(String string) {
        Integer n2 = (Integer)this._namespaces.get(string);
        if (n2 == null) {
            n2 = new Integer(this._nextNSType++);
            this._namespaces.put(string, n2);
            this._namespaceIndex.addElement(string);
        }
        return n2;
    }

    public int registerStylesheetPrefixMappingForRuntime(Hashtable hashtable, int n2) {
        if (this._stylesheetNSAncestorPointers == null) {
            this._stylesheetNSAncestorPointers = new Vector();
        }
        if (this._prefixURIPairs == null) {
            this._prefixURIPairs = new Vector();
        }
        if (this._prefixURIPairsIdx == null) {
            this._prefixURIPairsIdx = new Vector();
        }
        int n3 = this._stylesheetNSAncestorPointers.size();
        this._stylesheetNSAncestorPointers.add(new Integer(n2));
        Iterator iterator = hashtable.entrySet().iterator();
        int n4 = this._prefixURIPairs.size();
        this._prefixURIPairsIdx.add(new Integer(n4));
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            String string = (String)entry.getKey();
            String string2 = (String)entry.getValue();
            this._prefixURIPairs.add(string);
            this._prefixURIPairs.add(string2);
        }
        return n3;
    }

    public Vector getNSAncestorPointers() {
        return this._stylesheetNSAncestorPointers;
    }

    public Vector getPrefixURIPairs() {
        return this._prefixURIPairs;
    }

    public Vector getPrefixURIPairsIdx() {
        return this._prefixURIPairsIdx;
    }

    public int nextModeSerial() {
        return this._modeSerial++;
    }

    public int nextStylesheetSerial() {
        return this._stylesheetSerial++;
    }

    public int nextStepPatternSerial() {
        return this._stepPatternSerial++;
    }

    public int[] getNumberFieldIndexes() {
        return this._numberFieldIndexes;
    }

    public int nextHelperClassSerial() {
        return this._helperClassSerial++;
    }

    public int nextAttributeSetSerial() {
        return this._attributeSetSerial++;
    }

    public Vector getNamesIndex() {
        return this._namesIndex;
    }

    public Vector getNamespaceIndex() {
        return this._namespaceIndex;
    }

    public String getHelperClassName() {
        return this.getClassName() + '$' + this._helperClassSerial++;
    }

    public void dumpClass(JavaClass javaClass) {
        File file;
        String string;
        Object object;
        if (!(this._outputType != 0 && this._outputType != 4 || (string = (object = this.getOutputFile(javaClass.getClassName())).getParent()) == null || (file = new File(string)).exists())) {
            file.mkdirs();
        }
        try {
            switch (this._outputType) {
                case 0: {
                    javaClass.dump(new BufferedOutputStream(new FileOutputStream(this.getOutputFile(javaClass.getClassName()))));
                    break;
                }
                case 1: {
                    this._bcelClasses.addElement(javaClass);
                    break;
                }
                case 2: 
                case 3: 
                case 4: 
                case 5: {
                    object = new ByteArrayOutputStream(2048);
                    javaClass.dump((OutputStream)object);
                    this._classes.addElement(object.toByteArray());
                    if (this._outputType == 4) {
                        javaClass.dump(new BufferedOutputStream(new FileOutputStream(this.getOutputFile(javaClass.getClassName()))));
                        break;
                    }
                    if (this._outputType != 5) break;
                    this._bcelClasses.addElement(javaClass);
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String entryName(File file) throws IOException {
        return file.getName().replace(File.separatorChar, '/');
    }

    public void outputToJar() throws IOException {
        Object object2;
        Object object;
        Manifest manifest = new Manifest();
        Attributes attributes2 = manifest.getMainAttributes();
        attributes2.put(Attributes.Name.MANIFEST_VERSION, "1.2");
        Map<String, Attributes> map = manifest.getEntries();
        Enumeration enumeration = this._bcelClasses.elements();
        String string = new Date().toString();
        Attributes.Name name = new Attributes.Name("Date");
        while (enumeration.hasMoreElements()) {
            object = (JavaClass)enumeration.nextElement();
            object2 = object.getClassName().replace('.', '/');
            Attributes attributes = new Attributes();
            attributes.put(name, string);
            map.put((String)object2 + ".class", attributes);
        }
        object = new File(this._destDir, this._jarFileName);
        object2 = new JarOutputStream(new FileOutputStream((File)object), manifest);
        enumeration = this._bcelClasses.elements();
        while (enumeration.hasMoreElements()) {
            JavaClass javaClass = (JavaClass)enumeration.nextElement();
            String string2 = javaClass.getClassName().replace('.', '/');
            object2.putNextEntry(new JarEntry(string2 + ".class"));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
            javaClass.dump(byteArrayOutputStream);
            byteArrayOutputStream.writeTo((OutputStream)object2);
        }
        object2.close();
    }

    public void setDebug(boolean bl) {
        this._debug = bl;
    }

    public boolean debug() {
        return this._debug;
    }

    public String getCharacterData(int n2) {
        return ((StringBuffer)this.m_characterData.elementAt(n2)).toString();
    }

    public int getCharacterDataCount() {
        return this.m_characterData != null ? this.m_characterData.size() : 0;
    }

    public int addCharacterData(String string) {
        StringBuffer stringBuffer;
        if (this.m_characterData == null) {
            this.m_characterData = new Vector();
            stringBuffer = new StringBuffer();
            this.m_characterData.addElement(stringBuffer);
        } else {
            stringBuffer = (StringBuffer)this.m_characterData.elementAt(this.m_characterData.size() - 1);
        }
        if (string.length() + stringBuffer.length() > 21845) {
            stringBuffer = new StringBuffer();
            this.m_characterData.addElement(stringBuffer);
        }
        int n2 = stringBuffer.length();
        stringBuffer.append(string);
        return n2;
    }
}

