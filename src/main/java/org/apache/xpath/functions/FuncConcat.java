/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionMultiArgs;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncConcat
extends FunctionMultiArgs {
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.m_arg0.execute(xPathContext).str());
        stringBuffer.append(this.m_arg1.execute(xPathContext).str());
        if (null != this.m_arg2) {
            stringBuffer.append(this.m_arg2.execute(xPathContext).str());
        }
        if (null != this.m_args) {
            for (int i2 = 0; i2 < this.m_args.length; ++i2) {
                stringBuffer.append(this.m_args[i2].execute(xPathContext).str());
            }
        }
        return new XString(stringBuffer.toString());
    }

    public void checkNumberArgs(int n2) throws WrongNumberArgsException {
        if (n2 < 2) {
            this.reportWrongNumberArgs();
        }
    }

    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("gtone", null));
    }
}

