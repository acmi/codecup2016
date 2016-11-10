/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.extensions.ExtensionHandlerJava;
import org.apache.xalan.extensions.MethodResolver;
import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.functions.FuncExtFunction;

public class ExtensionHandlerJavaClass
extends ExtensionHandlerJava {
    private Class m_classObj = null;
    private Object m_defaultInstance = null;
    static Class class$org$apache$xalan$extensions$XSLProcessorContext;
    static Class class$org$apache$xalan$templates$ElemExtensionCall;
    static Class class$org$apache$xalan$extensions$ExpressionContext;

    public ExtensionHandlerJavaClass(String string, String string2, String string3) {
        super(string, string2, string3);
        try {
            this.m_classObj = ExtensionHandlerJavaClass.getClassForName(string3);
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
    }

    public boolean isFunctionAvailable(String string) {
        Method[] arrmethod = this.m_classObj.getMethods();
        int n2 = arrmethod.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!arrmethod[i2].getName().equals(string)) continue;
            return true;
        }
        return false;
    }

    public boolean isElementAvailable(String string) {
        Method[] arrmethod = this.m_classObj.getMethods();
        int n2 = arrmethod.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Class<?>[] arrclass;
            if (!arrmethod[i2].getName().equals(string) || (arrclass = arrmethod[i2].getParameterTypes()).length != 2 || !arrclass[0].isAssignableFrom(class$org$apache$xalan$extensions$XSLProcessorContext == null ? ExtensionHandlerJavaClass.class$("org.apache.xalan.extensions.XSLProcessorContext") : class$org$apache$xalan$extensions$XSLProcessorContext) || !arrclass[1].isAssignableFrom(class$org$apache$xalan$templates$ElemExtensionCall == null ? ExtensionHandlerJavaClass.class$("org.apache.xalan.templates.ElemExtensionCall") : class$org$apache$xalan$templates$ElemExtensionCall)) continue;
            return true;
        }
        return false;
    }

    /*
     * Exception decompiling
     */
    public Object callFunction(String var1_1, Vector var2_2, Object var3_3, ExpressionContext var4_4) throws TransformerException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 5[TRYBLOCK]
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:397)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:449)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2877)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:825)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    public Object callFunction(FuncExtFunction funcExtFunction, Vector vector, ExpressionContext expressionContext) throws TransformerException {
        return this.callFunction(funcExtFunction.getFunctionName(), vector, funcExtFunction.getMethodKey(), expressionContext);
    }

    public void processElement(String string, ElemTemplateElement elemTemplateElement, TransformerImpl transformerImpl, Stylesheet stylesheet, Object object) throws TransformerException, IOException {
        XSLProcessorContext xSLProcessorContext;
        Object object2;
        block21 : {
            object2 = null;
            Method method = (Method)this.getFromCache(object, null, null);
            if (null == method) {
                try {
                    method = MethodResolver.getElementMethod(this.m_classObj, string);
                    if (null == this.m_defaultInstance && !Modifier.isStatic(method.getModifiers())) {
                        if (transformerImpl.getDebug()) {
                            transformerImpl.getTraceManager().fireExtensionEvent(new ExtensionEvent(transformerImpl, this.m_classObj));
                            try {
                                this.m_defaultInstance = this.m_classObj.newInstance();
                            }
                            catch (Exception exception) {
                                throw exception;
                            }
                            finally {
                                transformerImpl.getTraceManager().fireExtensionEndEvent(new ExtensionEvent(transformerImpl, this.m_classObj));
                            }
                        }
                        this.m_defaultInstance = this.m_classObj.newInstance();
                    }
                }
                catch (Exception exception) {
                    throw new TransformerException(exception.getMessage(), exception);
                }
                this.putToCache(object, null, null, method);
            }
            xSLProcessorContext = new XSLProcessorContext(transformerImpl, stylesheet);
            try {
                if (transformerImpl.getDebug()) {
                    transformerImpl.getTraceManager().fireExtensionEvent(method, this.m_defaultInstance, new Object[]{xSLProcessorContext, elemTemplateElement});
                    try {
                        object2 = method.invoke(this.m_defaultInstance, xSLProcessorContext, elemTemplateElement);
                    }
                    catch (Exception exception) {
                        try {
                            throw exception;
                        }
                        catch (Throwable throwable) {
                            transformerImpl.getTraceManager().fireExtensionEndEvent(method, this.m_defaultInstance, new Object[]{xSLProcessorContext, elemTemplateElement});
                            throw throwable;
                        }
                    }
                    transformerImpl.getTraceManager().fireExtensionEndEvent(method, this.m_defaultInstance, new Object[]{xSLProcessorContext, elemTemplateElement});
                    break block21;
                }
                object2 = method.invoke(this.m_defaultInstance, xSLProcessorContext, elemTemplateElement);
            }
            catch (InvocationTargetException invocationTargetException) {
                Throwable throwable = invocationTargetException.getTargetException();
                if (throwable instanceof TransformerException) {
                    throw (TransformerException)throwable;
                }
                if (throwable != null) {
                    throw new TransformerException(throwable.getMessage(), throwable);
                }
                throw new TransformerException(invocationTargetException.getMessage(), invocationTargetException);
            }
            catch (Exception exception) {
                throw new TransformerException(exception.getMessage(), exception);
            }
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
}

