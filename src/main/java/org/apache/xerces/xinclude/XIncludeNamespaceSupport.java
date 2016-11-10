/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xinclude;

import org.apache.xerces.xinclude.MultipleScopeNamespaceSupport;
import org.apache.xerces.xni.NamespaceContext;

public class XIncludeNamespaceSupport
extends MultipleScopeNamespaceSupport {
    private boolean[] fValidContext = new boolean[8];

    public XIncludeNamespaceSupport() {
    }

    public XIncludeNamespaceSupport(NamespaceContext namespaceContext) {
        super(namespaceContext);
    }

    public void pushContext() {
        super.pushContext();
        if (this.fCurrentContext + 1 == this.fValidContext.length) {
            boolean[] arrbl = new boolean[this.fValidContext.length * 2];
            System.arraycopy(this.fValidContext, 0, arrbl, 0, this.fValidContext.length);
            this.fValidContext = arrbl;
        }
        this.fValidContext[this.fCurrentContext] = true;
    }

    public void setContextInvalid() {
        this.fValidContext[this.fCurrentContext] = false;
    }

    public String getURIFromIncludeParent(String string) {
        int n2 = this.fCurrentContext - 1;
        while (n2 > 0 && !this.fValidContext[n2]) {
            --n2;
        }
        return this.getURI(string, n2);
    }
}

