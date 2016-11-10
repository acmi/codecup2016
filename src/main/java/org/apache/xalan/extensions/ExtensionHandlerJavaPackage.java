/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.extensions.ExtensionHandlerJava;
import org.apache.xalan.extensions.MethodResolver;
import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.objects.XObject;

public class ExtensionHandlerJavaPackage
extends ExtensionHandlerJava {
    static Class class$org$apache$xalan$extensions$XSLProcessorContext;
    static Class class$org$apache$xalan$templates$ElemExtensionCall;

    public ExtensionHandlerJavaPackage(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public boolean isFunctionAvailable(String string) {
        try {
            String string2 = this.m_className + string;
            int n2 = string2.lastIndexOf(46);
            if (n2 >= 0) {
                Class class_ = ExtensionHandlerJavaPackage.getClassForName(string2.substring(0, n2));
                Method[] arrmethod = class_.getMethods();
                int n3 = arrmethod.length;
                string = string2.substring(n2 + 1);
                for (int i2 = 0; i2 < n3; ++i2) {
                    if (!arrmethod[i2].getName().equals(string)) continue;
                    return true;
                }
            }
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        return false;
    }

    public boolean isElementAvailable(String string) {
        try {
            String string2 = this.m_className + string;
            int n2 = string2.lastIndexOf(46);
            if (n2 >= 0) {
                Class class_ = ExtensionHandlerJavaPackage.getClassForName(string2.substring(0, n2));
                Method[] arrmethod = class_.getMethods();
                int n3 = arrmethod.length;
                string = string2.substring(n2 + 1);
                for (int i2 = 0; i2 < n3; ++i2) {
                    Class<?>[] arrclass;
                    if (!arrmethod[i2].getName().equals(string) || (arrclass = arrmethod[i2].getParameterTypes()).length != 2 || !arrclass[0].isAssignableFrom(class$org$apache$xalan$extensions$XSLProcessorContext == null ? ExtensionHandlerJavaPackage.class$("org.apache.xalan.extensions.XSLProcessorContext") : class$org$apache$xalan$extensions$XSLProcessorContext) || !arrclass[1].isAssignableFrom(class$org$apache$xalan$templates$ElemExtensionCall == null ? ExtensionHandlerJavaPackage.class$("org.apache.xalan.templates.ElemExtensionCall") : class$org$apache$xalan$templates$ElemExtensionCall)) continue;
                    return true;
                }
            }
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        return false;
    }

    public Object callFunction(String string, Vector vector, Object object, ExpressionContext expressionContext) throws TransformerException {
        int n2 = string.lastIndexOf(46);
        try {
            TransformerImpl transformerImpl;
            Method method;
            TransformerImpl transformerImpl2 = transformerImpl = expressionContext != null ? (TransformerImpl)expressionContext.getXPathContext().getOwnerObject() : null;
            if (string.endsWith(".new")) {
                Class class_;
                Constructor constructor;
                Object[] arrobject = new Object[vector.size()];
                Object[][] arrobject2 = new Object[1][];
                for (int i2 = 0; i2 < arrobject.length; ++i2) {
                    arrobject[i2] = vector.get(i2);
                }
                Constructor constructor2 = constructor = object != null ? (Constructor)this.getFromCache(object, null, arrobject) : null;
                if (constructor != null) {
                    try {
                        Class[] arrclass = constructor.getParameterTypes();
                        MethodResolver.convertParams(arrobject, arrobject2, arrclass, expressionContext);
                        return constructor.newInstance(arrobject2[0]);
                    }
                    catch (InvocationTargetException invocationTargetException) {
                        throw invocationTargetException;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                String string2 = this.m_className + string.substring(0, n2);
                try {
                    class_ = ExtensionHandlerJavaPackage.getClassForName(string2);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new TransformerException(classNotFoundException);
                }
                constructor = MethodResolver.getConstructor(class_, arrobject, arrobject2, expressionContext);
                if (object != null) {
                    this.putToCache(object, null, arrobject, constructor);
                }
                if (transformerImpl != null && transformerImpl.getDebug()) {
                    Object t2;
                    transformerImpl.getTraceManager().fireExtensionEvent(new ExtensionEvent(transformerImpl, constructor, arrobject2[0]));
                    try {
                        t2 = constructor.newInstance(arrobject2[0]);
                    }
                    catch (Exception exception) {
                        throw exception;
                    }
                    finally {
                        transformerImpl.getTraceManager().fireExtensionEndEvent(new ExtensionEvent(transformerImpl, constructor, arrobject2[0]));
                    }
                    return t2;
                }
                return constructor.newInstance(arrobject2[0]);
            }
            if (-1 != n2) {
                Class class_;
                Method method2;
                Object[] arrobject = new Object[vector.size()];
                Object[][] arrobject3 = new Object[1][];
                for (int i3 = 0; i3 < arrobject.length; ++i3) {
                    arrobject[i3] = vector.get(i3);
                }
                Method method3 = method2 = object != null ? (Method)this.getFromCache(object, null, arrobject) : null;
                if (method2 != null && !transformerImpl.getDebug()) {
                    try {
                        Class[] arrclass = method2.getParameterTypes();
                        MethodResolver.convertParams(arrobject, arrobject3, arrclass, expressionContext);
                        return method2.invoke(null, arrobject3[0]);
                    }
                    catch (InvocationTargetException invocationTargetException) {
                        throw invocationTargetException;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                String string3 = this.m_className + string.substring(0, n2);
                String string4 = string.substring(n2 + 1);
                try {
                    class_ = ExtensionHandlerJavaPackage.getClassForName(string3);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new TransformerException(classNotFoundException);
                }
                method2 = MethodResolver.getMethod(class_, string4, arrobject, arrobject3, expressionContext, 1);
                if (object != null) {
                    this.putToCache(object, null, arrobject, method2);
                }
                if (transformerImpl != null && transformerImpl.getDebug()) {
                    Object object2;
                    transformerImpl.getTraceManager().fireExtensionEvent(method2, null, arrobject3[0]);
                    try {
                        object2 = method2.invoke(null, arrobject3[0]);
                    }
                    catch (Exception exception) {
                        throw exception;
                    }
                    finally {
                        transformerImpl.getTraceManager().fireExtensionEndEvent(method2, null, arrobject3[0]);
                    }
                    return object2;
                }
                return method2.invoke(null, arrobject3[0]);
            }
            if (vector.size() < 1) {
                throw new TransformerException(XSLMessages.createMessage("ER_INSTANCE_MTHD_CALL_REQUIRES", new Object[]{string}));
            }
            Object object3 = vector.get(0);
            if (object3 instanceof XObject) {
                object3 = ((XObject)object3).object();
            }
            Object[] arrobject = new Object[vector.size() - 1];
            Object[][] arrobject4 = new Object[1][];
            for (int i4 = 0; i4 < arrobject.length; ++i4) {
                arrobject[i4] = vector.get(i4 + 1);
            }
            Method method4 = method = object != null ? (Method)this.getFromCache(object, object3, arrobject) : null;
            if (method != null) {
                try {
                    Class[] arrclass = method.getParameterTypes();
                    MethodResolver.convertParams(arrobject, arrobject4, arrclass, expressionContext);
                    return method.invoke(object3, arrobject4[0]);
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw invocationTargetException;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            Class class_ = object3.getClass();
            method = MethodResolver.getMethod(class_, string, arrobject, arrobject4, expressionContext, 2);
            if (object != null) {
                this.putToCache(object, object3, arrobject, method);
            }
            if (transformerImpl != null && transformerImpl.getDebug()) {
                Object object4;
                transformerImpl.getTraceManager().fireExtensionEvent(method, object3, arrobject4[0]);
                try {
                    object4 = method.invoke(object3, arrobject4[0]);
                }
                catch (Exception exception) {
                    throw exception;
                }
                finally {
                    transformerImpl.getTraceManager().fireExtensionEndEvent(method, object3, arrobject4[0]);
                }
                return object4;
            }
            return method.invoke(object3, arrobject4[0]);
        }
        catch (InvocationTargetException invocationTargetException) {
            Throwable throwable = invocationTargetException;
            Throwable throwable2 = invocationTargetException.getTargetException();
            if (throwable2 instanceof TransformerException) {
                throw (TransformerException)throwable2;
            }
            if (throwable2 != null) {
                throwable = throwable2;
            }
            throw new TransformerException(throwable);
        }
        catch (Exception exception) {
            throw new TransformerException(exception);
        }
    }

    public Object callFunction(FuncExtFunction funcExtFunction, Vector vector, ExpressionContext expressionContext) throws TransformerException {
        return this.callFunction(funcExtFunction.getFunctionName(), vector, funcExtFunction.getMethodKey(), expressionContext);
    }

    public void processElement(String string, ElemTemplateElement elemTemplateElement, TransformerImpl transformerImpl, Stylesheet stylesheet, Object object) throws TransformerException, IOException {
        Object object2;
        Object object3;
        block18 : {
            object3 = null;
            Method method = (Method)this.getFromCache(object, null, null);
            if (null == method) {
                try {
                    Class class_;
                    object2 = this.m_className + string;
                    int n2 = object2.lastIndexOf(46);
                    if (n2 < 0) {
                        throw new TransformerException(XSLMessages.createMessage("ER_INVALID_ELEMENT_NAME", new Object[]{object2}));
                    }
                    try {
                        class_ = ExtensionHandlerJavaPackage.getClassForName(object2.substring(0, n2));
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        throw new TransformerException(classNotFoundException);
                    }
                    string = object2.substring(n2 + 1);
                    method = MethodResolver.getElementMethod(class_, string);
                    if (!Modifier.isStatic(method.getModifiers())) {
                        throw new TransformerException(XSLMessages.createMessage("ER_ELEMENT_NAME_METHOD_STATIC", new Object[]{object2}));
                    }
                }
                catch (Exception exception) {
                    throw new TransformerException(exception);
                }
                this.putToCache(object, null, null, method);
            }
            object2 = new XSLProcessorContext(transformerImpl, stylesheet);
            try {
                if (transformerImpl.getDebug()) {
                    transformerImpl.getTraceManager().fireExtensionEvent(method, null, new Object[]{object2, elemTemplateElement});
                    try {
                        object3 = method.invoke(null, object2, elemTemplateElement);
                    }
                    catch (Exception exception) {
                        try {
                            throw exception;
                        }
                        catch (Throwable throwable) {
                            transformerImpl.getTraceManager().fireExtensionEndEvent(method, null, new Object[]{object2, elemTemplateElement});
                            throw throwable;
                        }
                    }
                    transformerImpl.getTraceManager().fireExtensionEndEvent(method, null, new Object[]{object2, elemTemplateElement});
                    break block18;
                }
                object3 = method.invoke(null, object2, elemTemplateElement);
            }
            catch (InvocationTargetException invocationTargetException) {
                Throwable throwable = invocationTargetException;
                Throwable throwable2 = invocationTargetException.getTargetException();
                if (throwable2 instanceof TransformerException) {
                    throw (TransformerException)throwable2;
                }
                if (throwable2 != null) {
                    throwable = throwable2;
                }
                throw new TransformerException(throwable);
            }
            catch (Exception exception) {
                throw new TransformerException(exception);
            }
        }
        if (object3 != null) {
            object2.outputToResultTree(stylesheet, object3);
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
}

