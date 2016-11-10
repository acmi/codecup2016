/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xinclude;

import java.util.Enumeration;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;

public class MultipleScopeNamespaceSupport
extends NamespaceSupport {
    protected int[] fScope = new int[8];
    protected int fCurrentScope = 0;

    public MultipleScopeNamespaceSupport() {
        this.fScope[0] = 0;
    }

    public MultipleScopeNamespaceSupport(NamespaceContext namespaceContext) {
        super(namespaceContext);
        this.fScope[0] = 0;
    }

    public Enumeration getAllPrefixes() {
        Object object;
        int n2 = 0;
        if (this.fPrefixes.length < this.fNamespace.length / 2) {
            object = new String[this.fNamespaceSize];
            this.fPrefixes = object;
        }
        object = null;
        boolean bl = true;
        int n3 = this.fContext[this.fScope[this.fCurrentScope]];
        while (n3 <= this.fNamespaceSize - 2) {
            object = this.fNamespace[n3];
            int n4 = 0;
            while (n4 < n2) {
                if (this.fPrefixes[n4] == object) {
                    bl = false;
                    break;
                }
                ++n4;
            }
            if (bl) {
                this.fPrefixes[n2++] = object;
            }
            bl = true;
            n3 += 2;
        }
        return new NamespaceSupport.Prefixes(this, this.fPrefixes, n2);
    }

    public int getScopeForContext(int n2) {
        int n3 = this.fCurrentScope;
        while (n2 < this.fScope[n3]) {
            --n3;
        }
        return n3;
    }

    public String getPrefix(String string) {
        return this.getPrefix(string, this.fNamespaceSize, this.fContext[this.fScope[this.fCurrentScope]]);
    }

    public String getURI(String string) {
        return this.getURI(string, this.fNamespaceSize, this.fContext[this.fScope[this.fCurrentScope]]);
    }

    public String getPrefix(String string, int n2) {
        return this.getPrefix(string, this.fContext[n2 + 1], this.fContext[this.fScope[this.getScopeForContext(n2)]]);
    }

    public String getURI(String string, int n2) {
        return this.getURI(string, this.fContext[n2 + 1], this.fContext[this.fScope[this.getScopeForContext(n2)]]);
    }

    public String getPrefix(String string, int n2, int n3) {
        if (string == NamespaceContext.XML_URI) {
            return XMLSymbols.PREFIX_XML;
        }
        if (string == NamespaceContext.XMLNS_URI) {
            return XMLSymbols.PREFIX_XMLNS;
        }
        int n4 = n2;
        while (n4 > n3) {
            if (this.fNamespace[n4 - 1] == string && this.getURI(this.fNamespace[n4 - 2]) == string) {
                return this.fNamespace[n4 - 2];
            }
            n4 -= 2;
        }
        return null;
    }

    public String getURI(String string, int n2, int n3) {
        if (string == XMLSymbols.PREFIX_XML) {
            return NamespaceContext.XML_URI;
        }
        if (string == XMLSymbols.PREFIX_XMLNS) {
            return NamespaceContext.XMLNS_URI;
        }
        int n4 = n2;
        while (n4 > n3) {
            if (this.fNamespace[n4 - 2] == string) {
                return this.fNamespace[n4 - 1];
            }
            n4 -= 2;
        }
        return null;
    }

    public void reset() {
        this.fCurrentContext = this.fScope[this.fCurrentScope];
        this.fNamespaceSize = this.fContext[this.fCurrentContext];
    }

    public void pushScope() {
        if (this.fCurrentScope + 1 == this.fScope.length) {
            int[] arrn = new int[this.fScope.length * 2];
            System.arraycopy(this.fScope, 0, arrn, 0, this.fScope.length);
            this.fScope = arrn;
        }
        this.pushContext();
        this.fScope[++this.fCurrentScope] = this.fCurrentContext;
    }

    public void popScope() {
        this.fCurrentContext = this.fScope[this.fCurrentScope--];
        this.popContext();
    }
}

