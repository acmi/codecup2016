/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.runtime;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.KeyIndex;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xalan.xsltc.runtime.MessageHandler;
import org.apache.xalan.xsltc.runtime.Parameter;
import org.apache.xalan.xsltc.runtime.StringValueHandler;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public abstract class AbstractTranslet
implements Translet {
    public String _version = "1.0";
    public String _method = null;
    public String _encoding = "UTF-8";
    public boolean _omitHeader = false;
    public String _standalone = null;
    public String _doctypePublic = null;
    public String _doctypeSystem = null;
    public boolean _indent = false;
    public String _mediaType = null;
    public Vector _cdata = null;
    public int _indentamount = -1;
    public static final int FIRST_TRANSLET_VERSION = 100;
    public static final int VER_SPLIT_NAMES_ARRAY = 101;
    public static final int CURRENT_TRANSLET_VERSION = 101;
    protected int transletVersion = 100;
    protected String[] namesArray;
    protected String[] urisArray;
    protected int[] typesArray;
    protected String[] namespaceArray;
    protected Templates _templates = null;
    protected boolean _hasIdCall = false;
    protected StringValueHandler stringValueHandler = new StringValueHandler();
    private static final String EMPTYSTRING = "";
    private static final String ID_INDEX_NAME = "##id";
    protected int pbase = 0;
    protected int pframe = 0;
    protected ArrayList paramsStack = new ArrayList();
    private MessageHandler _msgHandler = null;
    public Hashtable _formatSymbols = null;
    private Hashtable _keyIndexes = null;
    private KeyIndex _emptyKeyIndex = null;
    private int _indexSize = 0;
    private int _currentRootForKeys = 0;
    private DOMCache _domCache = null;
    private Hashtable _auxClasses = null;
    protected DOMImplementation _domImplementation = null;

    public void printInternalState() {
        System.out.println("-------------------------------------");
        System.out.println("AbstractTranslet this = " + this);
        System.out.println("pbase = " + this.pbase);
        System.out.println("vframe = " + this.pframe);
        System.out.println("paramsStack.size() = " + this.paramsStack.size());
        System.out.println("namesArray.size = " + this.namesArray.length);
        System.out.println("namespaceArray.size = " + this.namespaceArray.length);
        System.out.println("");
        System.out.println("Total memory = " + Runtime.getRuntime().totalMemory());
    }

    public final DOMAdapter makeDOMAdapter(DOM dOM) throws TransletException {
        this.setRootForKeys(dOM.getDocument());
        return new DOMAdapter(dOM, this.namesArray, this.urisArray, this.typesArray, this.namespaceArray);
    }

    public final void pushParamFrame() {
        this.paramsStack.add(this.pframe, new Integer(this.pbase));
        this.pbase = ++this.pframe;
    }

    public final void popParamFrame() {
        if (this.pbase > 0) {
            int n2 = (Integer)this.paramsStack.get(--this.pbase);
            for (int i2 = this.pframe - 1; i2 >= this.pbase; --i2) {
                this.paramsStack.remove(i2);
            }
            this.pframe = this.pbase;
            this.pbase = n2;
        }
    }

    public final Object addParameter(String string, Object object) {
        string = BasisLibrary.mapQNameToJavaName(string);
        return this.addParameter(string, object, false);
    }

    public final Object addParameter(String string, Object object, boolean bl) {
        for (int i2 = this.pframe - 1; i2 >= this.pbase; --i2) {
            Parameter parameter = (Parameter)this.paramsStack.get(i2);
            if (!parameter._name.equals(string)) continue;
            if (parameter._isDefault || !bl) {
                parameter._value = object;
                parameter._isDefault = bl;
                return object;
            }
            return parameter._value;
        }
        this.paramsStack.add(this.pframe++, new Parameter(string, object, bl));
        return object;
    }

    public void clearParameters() {
        this.pframe = 0;
        this.pbase = 0;
        this.paramsStack.clear();
    }

    public final Object getParameter(String string) {
        string = BasisLibrary.mapQNameToJavaName(string);
        for (int i2 = this.pframe - 1; i2 >= this.pbase; --i2) {
            Parameter parameter = (Parameter)this.paramsStack.get(i2);
            if (!parameter._name.equals(string)) continue;
            return parameter._value;
        }
        return null;
    }

    public final void setMessageHandler(MessageHandler messageHandler) {
        this._msgHandler = messageHandler;
    }

    public final void displayMessage(String string) {
        if (this._msgHandler == null) {
            System.err.println(string);
        } else {
            this._msgHandler.displayMessage(string);
        }
    }

    public void addDecimalFormat(String string, DecimalFormatSymbols decimalFormatSymbols) {
        if (this._formatSymbols == null) {
            this._formatSymbols = new Hashtable();
        }
        if (string == null) {
            string = "";
        }
        DecimalFormat decimalFormat = new DecimalFormat();
        if (decimalFormatSymbols != null) {
            decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        }
        this._formatSymbols.put(string, decimalFormat);
    }

    public final DecimalFormat getDecimalFormat(String string) {
        if (this._formatSymbols != null) {
            DecimalFormat decimalFormat;
            if (string == null) {
                string = "";
            }
            if ((decimalFormat = (DecimalFormat)this._formatSymbols.get(string)) == null) {
                decimalFormat = (DecimalFormat)this._formatSymbols.get("");
            }
            return decimalFormat;
        }
        return null;
    }

    public final void prepassDocument(DOM dOM) {
        this.setIndexSize(dOM.getSize());
        this.buildIDIndex(dOM);
    }

    private final void buildIDIndex(DOM dOM) {
        this.setRootForKeys(dOM.getDocument());
        if (dOM instanceof DOMEnhancedForDTM) {
            DOMEnhancedForDTM dOMEnhancedForDTM = (DOMEnhancedForDTM)dOM;
            if (dOMEnhancedForDTM.hasDOMSource()) {
                this.buildKeyIndex("##id", dOM);
                return;
            }
            Hashtable hashtable = dOMEnhancedForDTM.getElementsWithIDs();
            if (hashtable == null) {
                return;
            }
            Enumeration enumeration = hashtable.keys();
            boolean bl = false;
            while (enumeration.hasMoreElements()) {
                Object e2 = enumeration.nextElement();
                int n2 = dOM.getNodeHandle((Integer)hashtable.get(e2));
                this.buildKeyIndex("##id", n2, e2);
                bl = true;
            }
            if (bl) {
                this.setKeyIndexDom("##id", dOM);
            }
        }
    }

    public final void postInitialization() {
        if (this.transletVersion < 101) {
            int n2 = this.namesArray.length;
            String[] arrstring = new String[n2];
            String[] arrstring2 = new String[n2];
            int[] arrn = new int[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                String string = this.namesArray[i2];
                int n3 = string.lastIndexOf(58);
                int n4 = n3 + 1;
                if (n3 > -1) {
                    arrstring[i2] = string.substring(0, n3);
                }
                if (string.charAt(n4) == '@') {
                    ++n4;
                    arrn[i2] = 2;
                } else if (string.charAt(n4) == '?') {
                    ++n4;
                    arrn[i2] = 13;
                } else {
                    arrn[i2] = 1;
                }
                arrstring2[i2] = n4 == 0 ? string : string.substring(n4);
            }
            this.namesArray = arrstring2;
            this.urisArray = arrstring;
            this.typesArray = arrn;
        }
        if (this.transletVersion > 101) {
            BasisLibrary.runTimeError("UNKNOWN_TRANSLET_VERSION_ERR", this.getClass().getName());
        }
    }

    public void setIndexSize(int n2) {
        if (n2 > this._indexSize) {
            this._indexSize = n2;
        }
    }

    public KeyIndex createKeyIndex() {
        return new KeyIndex(this._indexSize);
    }

    public void buildKeyIndex(String string, int n2, Object object) {
        KeyIndex keyIndex;
        if (this._keyIndexes == null) {
            this._keyIndexes = new Hashtable();
        }
        if ((keyIndex = (KeyIndex)this._keyIndexes.get(string)) == null) {
            keyIndex = new KeyIndex(this._indexSize);
            this._keyIndexes.put(string, keyIndex);
        }
        keyIndex.add(object, n2, this._currentRootForKeys);
    }

    public void buildKeyIndex(String string, DOM dOM) {
        KeyIndex keyIndex;
        if (this._keyIndexes == null) {
            this._keyIndexes = new Hashtable();
        }
        if ((keyIndex = (KeyIndex)this._keyIndexes.get(string)) == null) {
            keyIndex = new KeyIndex(this._indexSize);
            this._keyIndexes.put(string, keyIndex);
        }
        keyIndex.setDom(dOM);
    }

    public KeyIndex getKeyIndex(String string) {
        if (this._keyIndexes == null) {
            KeyIndex keyIndex = this._emptyKeyIndex != null ? this._emptyKeyIndex : (this._emptyKeyIndex = new KeyIndex(1));
            return keyIndex;
        }
        KeyIndex keyIndex = (KeyIndex)this._keyIndexes.get(string);
        if (keyIndex == null) {
            KeyIndex keyIndex2 = this._emptyKeyIndex != null ? this._emptyKeyIndex : (this._emptyKeyIndex = new KeyIndex(1));
            return keyIndex2;
        }
        return keyIndex;
    }

    private void setRootForKeys(int n2) {
        this._currentRootForKeys = n2;
    }

    public void buildKeys(DOM dOM, DTMAxisIterator dTMAxisIterator, SerializationHandler serializationHandler, int n2) throws TransletException {
    }

    public void setKeyIndexDom(String string, DOM dOM) {
        this.getKeyIndex(string).setDom(dOM);
    }

    public void setDOMCache(DOMCache dOMCache) {
        this._domCache = dOMCache;
    }

    public DOMCache getDOMCache() {
        return this._domCache;
    }

    public SerializationHandler openOutputHandler(String string, boolean bl) throws TransletException {
        try {
            Object object;
            TransletOutputHandlerFactory transletOutputHandlerFactory = TransletOutputHandlerFactory.newInstance();
            String string2 = new File(string).getParent();
            if (null != string2 && string2.length() > 0) {
                object = new File(string2);
                object.mkdirs();
            }
            transletOutputHandlerFactory.setEncoding(this._encoding);
            transletOutputHandlerFactory.setOutputMethod(this._method);
            transletOutputHandlerFactory.setWriter(new FileWriter(string, bl));
            transletOutputHandlerFactory.setOutputType(0);
            object = transletOutputHandlerFactory.getSerializationHandler();
            this.transferOutputSettings((SerializationHandler)object);
            object.startDocument();
            return object;
        }
        catch (Exception exception) {
            throw new TransletException(exception);
        }
    }

    public SerializationHandler openOutputHandler(String string) throws TransletException {
        return this.openOutputHandler(string, false);
    }

    public void closeOutputHandler(SerializationHandler serializationHandler) {
        try {
            serializationHandler.endDocument();
            serializationHandler.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public abstract void transform(DOM var1, DTMAxisIterator var2, SerializationHandler var3) throws TransletException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void transform(DOM dOM, SerializationHandler serializationHandler) throws TransletException {
        try {
            this.transform(dOM, dOM.getIterator(), serializationHandler);
        }
        finally {
            this._keyIndexes = null;
        }
    }

    public final void characters(String string, SerializationHandler serializationHandler) throws TransletException {
        if (string != null) {
            try {
                serializationHandler.characters(string);
            }
            catch (Exception exception) {
                throw new TransletException(exception);
            }
        }
    }

    public void addCdataElement(String string) {
        int n2;
        if (this._cdata == null) {
            this._cdata = new Vector<E>();
        }
        if ((n2 = string.lastIndexOf(58)) > 0) {
            String string2 = string.substring(0, n2);
            String string3 = string.substring(n2 + 1);
            this._cdata.addElement(string2);
            this._cdata.addElement(string3);
        } else {
            this._cdata.addElement(null);
            this._cdata.addElement(string);
        }
    }

    protected void transferOutputSettings(SerializationHandler serializationHandler) {
        if (this._method != null) {
            if (this._method.equals("xml")) {
                if (this._standalone != null) {
                    serializationHandler.setStandalone(this._standalone);
                }
                if (this._omitHeader) {
                    serializationHandler.setOmitXMLDeclaration(true);
                }
                serializationHandler.setCdataSectionElements(this._cdata);
                if (this._version != null) {
                    serializationHandler.setVersion(this._version);
                }
                serializationHandler.setIndent(this._indent);
                serializationHandler.setIndentAmount(this._indentamount);
                if (this._doctypeSystem != null) {
                    serializationHandler.setDoctype(this._doctypeSystem, this._doctypePublic);
                }
            } else if (this._method.equals("html")) {
                serializationHandler.setIndent(this._indent);
                serializationHandler.setDoctype(this._doctypeSystem, this._doctypePublic);
                if (this._mediaType != null) {
                    serializationHandler.setMediaType(this._mediaType);
                }
            }
        } else {
            serializationHandler.setCdataSectionElements(this._cdata);
            if (this._version != null) {
                serializationHandler.setVersion(this._version);
            }
            if (this._standalone != null) {
                serializationHandler.setStandalone(this._standalone);
            }
            if (this._omitHeader) {
                serializationHandler.setOmitXMLDeclaration(true);
            }
            serializationHandler.setIndent(this._indent);
            serializationHandler.setDoctype(this._doctypeSystem, this._doctypePublic);
        }
    }

    public void addAuxiliaryClass(Class class_) {
        if (this._auxClasses == null) {
            this._auxClasses = new Hashtable();
        }
        this._auxClasses.put(class_.getName(), class_);
    }

    public void setAuxiliaryClasses(Hashtable hashtable) {
        this._auxClasses = hashtable;
    }

    public Class getAuxiliaryClass(String string) {
        if (this._auxClasses == null) {
            return null;
        }
        return (Class)this._auxClasses.get(string);
    }

    public String[] getNamesArray() {
        return this.namesArray;
    }

    public String[] getUrisArray() {
        return this.urisArray;
    }

    public int[] getTypesArray() {
        return this.typesArray;
    }

    public String[] getNamespaceArray() {
        return this.namespaceArray;
    }

    public boolean hasIdCall() {
        return this._hasIdCall;
    }

    public Templates getTemplates() {
        return this._templates;
    }

    public void setTemplates(Templates templates) {
        this._templates = templates;
    }

    public Document newDocument(String string, String string2) throws ParserConfigurationException {
        if (this._domImplementation == null) {
            this._domImplementation = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
        }
        return this._domImplementation.createDocument(string, string2, null);
    }
}

