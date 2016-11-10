/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm.ref;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xml.dtm.ref.IncrementalSAXSource;
import org.apache.xml.dtm.ref.ObjectFactory;
import org.apache.xml.res.XMLMessages;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.ext.LexicalHandler;

public class IncrementalSAXSource_Xerces
implements IncrementalSAXSource {
    Method fParseSomeSetup = null;
    Method fParseSome = null;
    Object fPullParserConfig = null;
    Method fConfigSetInput = null;
    Method fConfigParse = null;
    Method fSetInputSource = null;
    Constructor fConfigInputSourceCtor = null;
    Method fConfigSetByteStream = null;
    Method fConfigSetCharStream = null;
    Method fConfigSetEncoding = null;
    Method fReset = null;
    SAXParser fIncrementalParser;
    private boolean fParseInProgress = false;
    private static final Object[] noparms = new Object[0];
    private static final Object[] parmsfalse = new Object[]{Boolean.FALSE};
    static Class class$org$apache$xerces$parsers$SAXParser;
    static Class class$java$lang$String;
    static Class class$java$io$InputStream;
    static Class class$java$io$Reader;
    static Class class$org$xml$sax$InputSource;

    public IncrementalSAXSource_Xerces() throws NoSuchMethodException {
        try {
            Class class_ = ObjectFactory.findProviderClass("org.apache.xerces.xni.parser.XMLParserConfiguration", ObjectFactory.findClassLoader(), true);
            Class[] arrclass = new Class[]{class_};
            Class class_2 = class$org$apache$xerces$parsers$SAXParser == null ? (IncrementalSAXSource_Xerces.class$org$apache$xerces$parsers$SAXParser = IncrementalSAXSource_Xerces.class$("org.apache.xerces.parsers.SAXParser")) : class$org$apache$xerces$parsers$SAXParser;
            Constructor constructor = class_2.getConstructor(arrclass);
            Class class_3 = ObjectFactory.findProviderClass("org.apache.xerces.parsers.StandardParserConfiguration", ObjectFactory.findClassLoader(), true);
            this.fPullParserConfig = class_3.newInstance();
            Object[] arrobject = new Object[]{this.fPullParserConfig};
            this.fIncrementalParser = (SAXParser)constructor.newInstance(arrobject);
            Class class_4 = ObjectFactory.findProviderClass("org.apache.xerces.xni.parser.XMLInputSource", ObjectFactory.findClassLoader(), true);
            Class[] arrclass2 = new Class[]{class_4};
            this.fConfigSetInput = class_3.getMethod("setInputSource", arrclass2);
            Class[] arrclass3 = new Class[3];
            Class class_5 = class$java$lang$String == null ? (IncrementalSAXSource_Xerces.class$java$lang$String = IncrementalSAXSource_Xerces.class$("java.lang.String")) : class$java$lang$String;
            arrclass3[0] = class_5;
            arrclass3[1] = class$java$lang$String == null ? (IncrementalSAXSource_Xerces.class$java$lang$String = IncrementalSAXSource_Xerces.class$("java.lang.String")) : class$java$lang$String;
            arrclass3[2] = class$java$lang$String == null ? (IncrementalSAXSource_Xerces.class$java$lang$String = IncrementalSAXSource_Xerces.class$("java.lang.String")) : class$java$lang$String;
            Class[] arrclass4 = arrclass3;
            this.fConfigInputSourceCtor = class_4.getConstructor(arrclass4);
            Class[] arrclass5 = new Class[1];
            Class class_6 = class$java$io$InputStream == null ? (IncrementalSAXSource_Xerces.class$java$io$InputStream = IncrementalSAXSource_Xerces.class$("java.io.InputStream")) : class$java$io$InputStream;
            arrclass5[0] = class_6;
            Class[] arrclass6 = arrclass5;
            this.fConfigSetByteStream = class_4.getMethod("setByteStream", arrclass6);
            Class[] arrclass7 = new Class[1];
            Class class_7 = class$java$io$Reader == null ? (IncrementalSAXSource_Xerces.class$java$io$Reader = IncrementalSAXSource_Xerces.class$("java.io.Reader")) : class$java$io$Reader;
            arrclass7[0] = class_7;
            Class[] arrclass8 = arrclass7;
            this.fConfigSetCharStream = class_4.getMethod("setCharacterStream", arrclass8);
            Class[] arrclass9 = new Class[1];
            arrclass9[0] = class$java$lang$String == null ? (IncrementalSAXSource_Xerces.class$java$lang$String = IncrementalSAXSource_Xerces.class$("java.lang.String")) : class$java$lang$String;
            Class[] arrclass10 = arrclass9;
            this.fConfigSetEncoding = class_4.getMethod("setEncoding", arrclass10);
            Class[] arrclass11 = new Class[]{Boolean.TYPE};
            this.fConfigParse = class_3.getMethod("parse", arrclass11);
            Class[] arrclass12 = new Class[]{};
            this.fReset = this.fIncrementalParser.getClass().getMethod("reset", arrclass12);
        }
        catch (Exception exception) {
            IncrementalSAXSource_Xerces incrementalSAXSource_Xerces = new IncrementalSAXSource_Xerces(new SAXParser());
            this.fParseSomeSetup = incrementalSAXSource_Xerces.fParseSomeSetup;
            this.fParseSome = incrementalSAXSource_Xerces.fParseSome;
            this.fIncrementalParser = incrementalSAXSource_Xerces.fIncrementalParser;
        }
    }

    public IncrementalSAXSource_Xerces(SAXParser sAXParser) throws NoSuchMethodException {
        this.fIncrementalParser = sAXParser;
        Class class_ = sAXParser.getClass();
        Class[] arrclass = new Class[1];
        Class class_2 = class$org$xml$sax$InputSource == null ? (IncrementalSAXSource_Xerces.class$org$xml$sax$InputSource = IncrementalSAXSource_Xerces.class$("org.xml.sax.InputSource")) : class$org$xml$sax$InputSource;
        arrclass[0] = class_2;
        Class[] arrclass2 = arrclass;
        this.fParseSomeSetup = class_.getMethod("parseSomeSetup", arrclass2);
        arrclass2 = new Class[]{};
        this.fParseSome = class_.getMethod("parseSome", arrclass2);
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.fIncrementalParser.setContentHandler(contentHandler);
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        try {
            this.fIncrementalParser.setProperty("http://xml.org/sax/properties/lexical-handler", lexicalHandler);
        }
        catch (SAXNotRecognizedException sAXNotRecognizedException) {
        }
        catch (SAXNotSupportedException sAXNotSupportedException) {
            // empty catch block
        }
    }

    public void setDTDHandler(DTDHandler dTDHandler) {
        this.fIncrementalParser.setDTDHandler(dTDHandler);
    }

    public void startParse(InputSource inputSource) throws SAXException {
        if (this.fIncrementalParser == null) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_NEEDS_SAXPARSER", null));
        }
        if (this.fParseInProgress) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_WHILE_PARSING", null));
        }
        boolean bl = false;
        try {
            bl = this.parseSomeSetup(inputSource);
        }
        catch (Exception exception) {
            throw new SAXException(exception);
        }
        if (!bl) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_COULD_NOT_INIT_PARSER", null));
        }
    }

    public Object deliverMoreNodes(boolean bl) {
        Serializable serializable2;
        Serializable serializable2;
        if (!bl) {
            this.fParseInProgress = false;
            return Boolean.FALSE;
        }
        try {
            boolean bl2 = this.parseSome();
            serializable2 = bl2 ? Boolean.TRUE : Boolean.FALSE;
        }
        catch (SAXException sAXException) {
            serializable2 = sAXException;
        }
        catch (IOException iOException) {
            serializable2 = iOException;
        }
        catch (Exception exception) {
            serializable2 = new SAXException(exception);
        }
        return serializable2;
    }

    private boolean parseSomeSetup(InputSource inputSource) throws SAXException, IOException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.fConfigSetInput != null) {
            Object[] arrobject = new Object[]{inputSource.getPublicId(), inputSource.getSystemId(), null};
            Object t2 = this.fConfigInputSourceCtor.newInstance(arrobject);
            Object[] arrobject2 = new Object[]{inputSource.getByteStream()};
            this.fConfigSetByteStream.invoke(t2, arrobject2);
            arrobject2[0] = inputSource.getCharacterStream();
            this.fConfigSetCharStream.invoke(t2, arrobject2);
            arrobject2[0] = inputSource.getEncoding();
            this.fConfigSetEncoding.invoke(t2, arrobject2);
            Object[] arrobject3 = new Object[]{};
            this.fReset.invoke(this.fIncrementalParser, arrobject3);
            arrobject2[0] = t2;
            this.fConfigSetInput.invoke(this.fPullParserConfig, arrobject2);
            return this.parseSome();
        }
        Object[] arrobject = new Object[]{inputSource};
        Object object = this.fParseSomeSetup.invoke(this.fIncrementalParser, arrobject);
        return (Boolean)object;
    }

    private boolean parseSome() throws SAXException, IOException, IllegalAccessException, InvocationTargetException {
        if (this.fConfigSetInput != null) {
            Boolean bl = (Boolean)this.fConfigParse.invoke(this.fPullParserConfig, parmsfalse);
            return bl;
        }
        Object object = this.fParseSome.invoke(this.fIncrementalParser, noparms);
        return (Boolean)object;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

