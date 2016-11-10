/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath;

import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.compiler.XPathParser;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;

public class XPath
implements Serializable,
ExpressionOwner {
    private Expression m_mainExp;
    private transient FunctionTable m_funcTable = null;
    String m_patternString;

    private void initFunctionTable() {
        this.m_funcTable = new FunctionTable();
    }

    public Expression getExpression() {
        return this.m_mainExp;
    }

    public void fixupVariables(Vector vector, int n2) {
        this.m_mainExp.fixupVariables(vector, n2);
    }

    public void setExpression(Expression expression) {
        if (null != this.m_mainExp) {
            expression.exprSetParent(this.m_mainExp.exprGetParent());
        }
        this.m_mainExp = expression;
    }

    public SourceLocator getLocator() {
        return this.m_mainExp;
    }

    public String getPatternString() {
        return this.m_patternString;
    }

    public XPath(String string, SourceLocator sourceLocator, PrefixResolver prefixResolver, int n2, ErrorListener errorListener) throws TransformerException {
        this.initFunctionTable();
        if (null == errorListener) {
            errorListener = new DefaultErrorHandler();
        }
        this.m_patternString = string;
        XPathParser xPathParser = new XPathParser(errorListener, sourceLocator);
        Compiler compiler = new Compiler(errorListener, sourceLocator, this.m_funcTable);
        if (0 == n2) {
            xPathParser.initXPath(compiler, string, prefixResolver);
        } else if (1 == n2) {
            xPathParser.initMatchPattern(compiler, string, prefixResolver);
        } else {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_DEAL_XPATH_TYPE", new Object[]{Integer.toString(n2)}));
        }
        Expression expression = compiler.compile(0);
        this.setExpression(expression);
        if (null != sourceLocator && sourceLocator instanceof ExpressionNode) {
            expression.exprSetParent((ExpressionNode)sourceLocator);
        }
    }

    public XPath(String string, SourceLocator sourceLocator, PrefixResolver prefixResolver, int n2, ErrorListener errorListener, FunctionTable functionTable) throws TransformerException {
        this.m_funcTable = functionTable;
        if (null == errorListener) {
            errorListener = new DefaultErrorHandler();
        }
        this.m_patternString = string;
        XPathParser xPathParser = new XPathParser(errorListener, sourceLocator);
        Compiler compiler = new Compiler(errorListener, sourceLocator, this.m_funcTable);
        if (0 == n2) {
            xPathParser.initXPath(compiler, string, prefixResolver);
        } else if (1 == n2) {
            xPathParser.initMatchPattern(compiler, string, prefixResolver);
        } else {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_DEAL_XPATH_TYPE", new Object[]{Integer.toString(n2)}));
        }
        Expression expression = compiler.compile(0);
        this.setExpression(expression);
        if (null != sourceLocator && sourceLocator instanceof ExpressionNode) {
            expression.exprSetParent((ExpressionNode)sourceLocator);
        }
    }

    public XPath(String string, SourceLocator sourceLocator, PrefixResolver prefixResolver, int n2) throws TransformerException {
        this(string, sourceLocator, prefixResolver, n2, null);
    }

    public XPath(Expression expression) {
        this.setExpression(expression);
        this.initFunctionTable();
    }

    public XObject execute(XPathContext xPathContext, Node node, PrefixResolver prefixResolver) throws TransformerException {
        return this.execute(xPathContext, xPathContext.getDTMHandleFromNode(node), prefixResolver);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public XObject execute(XPathContext xPathContext, int n2, PrefixResolver prefixResolver) throws TransformerException {
        XObject xObject;
        block11 : {
            xPathContext.pushNamespaceContext(prefixResolver);
            xPathContext.pushCurrentNodeAndExpression(n2, n2);
            xObject = null;
            try {
                xObject = this.m_mainExp.execute(xPathContext);
            }
            catch (TransformerException transformerException) {
                transformerException.setLocator(this.getLocator());
                ErrorListener errorListener = xPathContext.getErrorListener();
                if (null != errorListener) {
                    errorListener.error(transformerException);
                    break block11;
                }
                throw transformerException;
            }
            catch (Exception exception) {
                Exception exception2;
                while (exception2 instanceof WrappedRuntimeException) {
                    exception2 = ((WrappedRuntimeException)exception2).getException();
                }
                String string = exception2.getMessage();
                if (string == null || string.length() == 0) {
                    string = XSLMessages.createXPATHMessage("ER_XPATH_ERROR", null);
                }
                TransformerException transformerException = new TransformerException(string, this.getLocator(), exception2);
                ErrorListener errorListener = xPathContext.getErrorListener();
                if (null != errorListener) {
                    errorListener.fatalError(transformerException);
                    break block11;
                }
                throw transformerException;
            }
            finally {
                xPathContext.popNamespaceContext();
                xPathContext.popCurrentNodeAndExpression();
            }
        }
        return xObject;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean bool(XPathContext xPathContext, int n2, PrefixResolver prefixResolver) throws TransformerException {
        block11 : {
            xPathContext.pushNamespaceContext(prefixResolver);
            xPathContext.pushCurrentNodeAndExpression(n2, n2);
            try {
                boolean bl = this.m_mainExp.bool(xPathContext);
                return bl;
            }
            catch (TransformerException transformerException) {
                transformerException.setLocator(this.getLocator());
                ErrorListener errorListener = xPathContext.getErrorListener();
                if (null != errorListener) {
                    errorListener.error(transformerException);
                    break block11;
                }
                throw transformerException;
            }
            catch (Exception exception) {
                Exception exception2;
                while (exception2 instanceof WrappedRuntimeException) {
                    exception2 = ((WrappedRuntimeException)exception2).getException();
                }
                String string = exception2.getMessage();
                if (string == null || string.length() == 0) {
                    string = XSLMessages.createXPATHMessage("ER_XPATH_ERROR", null);
                }
                TransformerException transformerException = new TransformerException(string, this.getLocator(), exception2);
                ErrorListener errorListener = xPathContext.getErrorListener();
                if (null != errorListener) {
                    errorListener.fatalError(transformerException);
                    break block11;
                }
                throw transformerException;
            }
            finally {
                xPathContext.popNamespaceContext();
                xPathContext.popCurrentNodeAndExpression();
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public double getMatchScore(XPathContext xPathContext, int n2) throws TransformerException {
        xPathContext.pushCurrentNode(n2);
        xPathContext.pushCurrentExpressionNode(n2);
        try {
            XObject xObject = this.m_mainExp.execute(xPathContext);
            double d2 = xObject.num();
            return d2;
        }
        finally {
            xPathContext.popCurrentNode();
            xPathContext.popCurrentExpressionNode();
        }
    }

    public void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor) {
        this.m_mainExp.callVisitors(this, xPathVisitor);
    }
}

