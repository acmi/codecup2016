/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.functions.Function;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XNull;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;

public class FuncExtFunction
extends Function {
    String m_namespace;
    String m_extensionName;
    Object m_methodKey;
    Vector m_argVec = new Vector();

    public void fixupVariables(Vector vector, int n2) {
        if (null != this.m_argVec) {
            int n3 = this.m_argVec.size();
            for (int i2 = 0; i2 < n3; ++i2) {
                Expression expression = (Expression)this.m_argVec.elementAt(i2);
                expression.fixupVariables(vector, n2);
            }
        }
    }

    public String getNamespace() {
        return this.m_namespace;
    }

    public String getFunctionName() {
        return this.m_extensionName;
    }

    public Object getMethodKey() {
        return this.m_methodKey;
    }

    public FuncExtFunction(String string, String string2, Object object) {
        this.m_namespace = string;
        this.m_extensionName = string2;
        this.m_methodKey = object;
    }

    public XObject execute(XPathContext xPathContext) throws TransformerException {
        Object object;
        if (xPathContext.isSecureProcessing()) {
            throw new TransformerException(XPATHMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[]{this.toString()}));
        }
        Vector<XObject> vector = new Vector<XObject>();
        int n2 = this.m_argVec.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            object = (Expression)this.m_argVec.elementAt(i2);
            XObject xObject = object.execute(xPathContext);
            xObject.allowDetachToRelease(false);
            vector.addElement(xObject);
        }
        ExtensionsProvider extensionsProvider = (ExtensionsProvider)xPathContext.getOwnerObject();
        object = extensionsProvider.extFunction(this, vector);
        XObject xObject = null != object ? XObject.create(object, xPathContext) : new XNull();
        return xObject;
    }

    public void setArg(Expression expression, int n2) throws WrongNumberArgsException {
        this.m_argVec.addElement(expression);
        expression.exprSetParent(this);
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
    }

    public void callArgVisitors(XPathVisitor xPathVisitor) {
        for (int i2 = 0; i2 < this.m_argVec.size(); ++i2) {
            Expression expression = (Expression)this.m_argVec.elementAt(i2);
            expression.callVisitors(new ArgExtOwner(this, expression), xPathVisitor);
        }
    }

    public void exprSetParent(ExpressionNode expressionNode) {
        super.exprSetParent(expressionNode);
        int n2 = this.m_argVec.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Expression expression = (Expression)this.m_argVec.elementAt(i2);
            expression.exprSetParent(expressionNode);
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        String string = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{"Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called."});
        throw new RuntimeException(string);
    }

    public String toString() {
        if (this.m_namespace != null && this.m_namespace.length() > 0) {
            return "{" + this.m_namespace + "}" + this.m_extensionName;
        }
        return this.m_extensionName;
    }

    class ArgExtOwner
    implements ExpressionOwner {
        Expression m_exp;
        private final FuncExtFunction this$0;

        ArgExtOwner(FuncExtFunction funcExtFunction, Expression expression) {
            this.this$0 = funcExtFunction;
            this.m_exp = expression;
        }

        public Expression getExpression() {
            return this.m_exp;
        }

        public void setExpression(Expression expression) {
            expression.exprSetParent(this.this$0);
            this.m_exp = expression;
        }
    }

}

