/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.extensions.ExtensionHandler;
import org.apache.xalan.extensions.ObjectFactory;
import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathProcessorException;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;

public class ExtensionHandlerGeneral
extends ExtensionHandler {
    private String m_scriptSrc;
    private String m_scriptSrcURL;
    private Hashtable m_functions = new Hashtable();
    private Hashtable m_elements = new Hashtable();
    private Object m_engine;
    private Method m_engineCall = null;
    private static String BSF_MANAGER;
    private static final String DEFAULT_BSF_MANAGER = "org.apache.bsf.BSFManager";
    private static final String propName = "org.apache.xalan.extensions.bsf.BSFManager";
    private static final Integer ZEROINT;
    static Class class$java$lang$String;
    static Class class$java$lang$Object;
    static Class array$Ljava$lang$Object;

    public ExtensionHandlerGeneral(String string, StringVector stringVector, StringVector stringVector2, String string2, String string3, String string4, String string5) throws TransformerException {
        byte[] arrby;
        int n2;
        Object object;
        Object object2;
        super(string, string2);
        if (stringVector != null) {
            object2 = new Object();
            object = stringVector.size();
            for (n2 = 0; n2 < object; ++n2) {
                arrby = stringVector.elementAt(n2);
                this.m_elements.put(arrby, object2);
            }
        }
        if (stringVector2 != null) {
            object2 = new Object();
            object = stringVector2.size();
            for (n2 = 0; n2 < object; ++n2) {
                arrby = stringVector2.elementAt(n2);
                this.m_functions.put(arrby, object2);
            }
        }
        this.m_scriptSrcURL = string3;
        this.m_scriptSrc = string4;
        if (this.m_scriptSrcURL != null) {
            object2 = null;
            try {
                object2 = new URL(this.m_scriptSrcURL);
            }
            catch (MalformedURLException malformedURLException) {
                n2 = this.m_scriptSrcURL.indexOf(58);
                int n3 = this.m_scriptSrcURL.indexOf(47);
                if (n2 != -1 && n3 != -1 && n2 < n3) {
                    object2 = null;
                    throw new TransformerException(XSLMessages.createMessage("ER_COULD_NOT_FIND_EXTERN_SCRIPT", new Object[]{this.m_scriptSrcURL}), malformedURLException);
                }
                try {
                    object2 = new URL(new URL(SystemIDResolver.getAbsoluteURI(string5)), this.m_scriptSrcURL);
                }
                catch (MalformedURLException malformedURLException2) {
                    throw new TransformerException(XSLMessages.createMessage("ER_COULD_NOT_FIND_EXTERN_SCRIPT", new Object[]{this.m_scriptSrcURL}), malformedURLException2);
                }
            }
            if (object2 != null) {
                try {
                    URLConnection uRLConnection = object2.openConnection();
                    InputStream inputStream = uRLConnection.getInputStream();
                    arrby = new byte[uRLConnection.getContentLength()];
                    inputStream.read(arrby);
                    this.m_scriptSrc = new String(arrby);
                }
                catch (IOException iOException) {
                    throw new TransformerException(XSLMessages.createMessage("ER_COULD_NOT_FIND_EXTERN_SCRIPT", new Object[]{this.m_scriptSrcURL}), iOException);
                }
            }
        }
        object2 = null;
        try {
            object2 = ObjectFactory.newInstance(BSF_MANAGER, ObjectFactory.findClassLoader(), true);
        }
        catch (ObjectFactory.ConfigurationError configurationError) {
            configurationError.printStackTrace();
        }
        if (object2 == null) {
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_INIT_BSFMGR", null));
        }
        try {
            Class[] arrclass = new Class[1];
            Class class_ = class$java$lang$String == null ? (ExtensionHandlerGeneral.class$java$lang$String = ExtensionHandlerGeneral.class$("java.lang.String")) : class$java$lang$String;
            arrclass[0] = class_;
            object = object2.getClass().getMethod("loadScriptingEngine", arrclass);
            this.m_engine = object.invoke(object2, string2);
            Class[] arrclass2 = new Class[4];
            arrclass2[0] = class$java$lang$String == null ? (ExtensionHandlerGeneral.class$java$lang$String = ExtensionHandlerGeneral.class$("java.lang.String")) : class$java$lang$String;
            arrclass2[1] = Integer.TYPE;
            arrclass2[2] = Integer.TYPE;
            Class class_2 = class$java$lang$Object == null ? (ExtensionHandlerGeneral.class$java$lang$Object = ExtensionHandlerGeneral.class$("java.lang.Object")) : class$java$lang$Object;
            arrclass2[3] = class_2;
            Method method = this.m_engine.getClass().getMethod("exec", arrclass2);
            method.invoke(this.m_engine, "XalanScript", ZEROINT, ZEROINT, this.m_scriptSrc);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_CMPL_EXTENSN", null), exception);
        }
    }

    public boolean isFunctionAvailable(String string) {
        return this.m_functions.get(string) != null;
    }

    public boolean isElementAvailable(String string) {
        return this.m_elements.get(string) != null;
    }

    public Object callFunction(String string, Vector vector, Object object, ExpressionContext expressionContext) throws TransformerException {
        try {
            Object[] arrobject = new Object[vector.size()];
            for (int i2 = 0; i2 < arrobject.length; ++i2) {
                Object object2 = vector.get(i2);
                arrobject[i2] = object2 instanceof XObject ? ((XObject)object2).object() : object2;
                object2 = arrobject[i2];
                if (null == object2 || !(object2 instanceof DTMIterator)) continue;
                arrobject[i2] = new DTMNodeList((DTMIterator)object2);
            }
            if (this.m_engineCall == null) {
                Class[] arrclass = new Class[3];
                Class class_ = class$java$lang$Object == null ? (ExtensionHandlerGeneral.class$java$lang$Object = ExtensionHandlerGeneral.class$("java.lang.Object")) : class$java$lang$Object;
                arrclass[0] = class_;
                Class class_2 = class$java$lang$String == null ? (ExtensionHandlerGeneral.class$java$lang$String = ExtensionHandlerGeneral.class$("java.lang.String")) : class$java$lang$String;
                arrclass[1] = class_2;
                Class class_3 = array$Ljava$lang$Object == null ? (ExtensionHandlerGeneral.array$Ljava$lang$Object = ExtensionHandlerGeneral.class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object;
                arrclass[2] = class_3;
                this.m_engineCall = this.m_engine.getClass().getMethod("call", arrclass);
            }
            return this.m_engineCall.invoke(this.m_engine, null, string, arrobject);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            String string2 = exception.getMessage();
            if (null != string2) {
                if (string2.startsWith("Stopping after fatal error:")) {
                    string2 = string2.substring("Stopping after fatal error:".length());
                }
                throw new TransformerException(exception);
            }
            throw new TransformerException(XSLMessages.createMessage("ER_CANNOT_CREATE_EXTENSN", new Object[]{string, exception}));
        }
    }

    public Object callFunction(FuncExtFunction funcExtFunction, Vector vector, ExpressionContext expressionContext) throws TransformerException {
        return this.callFunction(funcExtFunction.getFunctionName(), vector, funcExtFunction.getMethodKey(), expressionContext);
    }

    public void processElement(String string, ElemTemplateElement elemTemplateElement, TransformerImpl transformerImpl, Stylesheet stylesheet, Object object) throws TransformerException, IOException {
        Object object2 = null;
        XSLProcessorContext xSLProcessorContext = new XSLProcessorContext(transformerImpl, stylesheet);
        try {
            Vector<Object> vector = new Vector<Object>(2);
            vector.add(xSLProcessorContext);
            vector.add(elemTemplateElement);
            object2 = this.callFunction(string, vector, object, transformerImpl.getXPathContext().getExpressionContext());
        }
        catch (XPathProcessorException xPathProcessorException) {
            throw new TransformerException(xPathProcessorException.getMessage(), xPathProcessorException);
        }
        if (object2 != null) {
            xSLProcessorContext.outputToResultTree(stylesheet, object2);
        }
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
        ZEROINT = new Integer(0);
        BSF_MANAGER = ObjectFactory.lookUpFactoryClassName("org.apache.xalan.extensions.bsf.BSFManager", null, null);
        if (BSF_MANAGER == null) {
            BSF_MANAGER = "org.apache.bsf.BSFManager";
        }
    }
}

