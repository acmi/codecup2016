/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.XSLTC;
import org.xml.sax.InputSource;

public interface SourceLoader {
    public InputSource loadSource(String var1, String var2, XSLTC var3);
}

