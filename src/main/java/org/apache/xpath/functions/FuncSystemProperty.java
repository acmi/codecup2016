/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.functions;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.FunctionOneArg;
import org.apache.xpath.functions.ObjectFactory;
import org.apache.xpath.functions.SecuritySupport;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncSystemProperty
extends FunctionOneArg {
    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public XObject execute(XPathContext var1_1) throws TransformerException {
        block15 : {
            var2_2 = this.m_arg0.execute(var1_1).str();
            var3_3 = var2_2.indexOf(58);
            var4_4 = null;
            var5_5 = "";
            var6_6 = new Properties();
            this.loadPropertyFile("org/apache/xalan/res/XSLTInfo.properties", var6_6);
            if (var3_3 <= 0) ** GOTO lbl29
            var7_7 = var3_3 >= 0 ? var2_2.substring(0, var3_3) : "";
            var8_10 = var1_1.getNamespaceContext().getNamespaceForPrefix(var7_7);
            v0 = var5_5 = var3_3 < 0 ? var2_2 : var2_2.substring(var3_3 + 1);
            if (var8_10.startsWith("http://www.w3.org/XSL/Transform") || var8_10.equals("http://www.w3.org/1999/XSL/Transform")) {
                var4_4 = var6_6.getProperty(var5_5);
                if (null == var4_4) {
                    this.warn(var1_1, "WG_PROPERTY_NOT_SUPPORTED", new Object[]{var2_2});
                    return XString.EMPTYSTRING;
                }
            } else {
                this.warn(var1_1, "WG_DONT_DO_ANYTHING_WITH_NS", new Object[]{var8_10, var2_2});
                try {
                    if (!var1_1.isSecureProcessing()) {
                        var4_4 = System.getProperty(var5_5);
                    } else {
                        this.warn(var1_1, "WG_SECURITY_EXCEPTION", new Object[]{var2_2});
                    }
                    if (null == var4_4) {
                        return XString.EMPTYSTRING;
                    }
                    break block15;
                }
                catch (SecurityException var9_11) {
                    this.warn(var1_1, "WG_SECURITY_EXCEPTION", new Object[]{var2_2});
                    return XString.EMPTYSTRING;
                }
lbl29: // 1 sources:
                try {
                    if (!var1_1.isSecureProcessing()) {
                        var4_4 = System.getProperty(var2_2);
                    } else {
                        this.warn(var1_1, "WG_SECURITY_EXCEPTION", new Object[]{var2_2});
                    }
                    if (null == var4_4) {
                        return XString.EMPTYSTRING;
                    }
                }
                catch (SecurityException var7_8) {
                    this.warn(var1_1, "WG_SECURITY_EXCEPTION", new Object[]{var2_2});
                    return XString.EMPTYSTRING;
                }
            }
        }
        if (var5_5.equals("version") == false) return new XString(var4_4);
        if (var4_4.length() <= 0) return new XString(var4_4);
        try {
            return new XString("1.0");
        }
        catch (Exception var7_9) {
            return new XString(var4_4);
        }
    }

    public void loadPropertyFile(String string, Properties properties) {
        try {
            InputStream inputStream = SecuritySupport.getResourceAsStream(ObjectFactory.findClassLoader(), string);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            properties.load(bufferedInputStream);
            bufferedInputStream.close();
        }
        catch (Exception exception) {
            throw new WrappedRuntimeException(exception);
        }
    }
}

