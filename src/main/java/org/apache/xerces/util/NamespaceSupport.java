/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;

public class NamespaceSupport
implements NamespaceContext {
    protected String[] fNamespace = new String[32];
    protected int fNamespaceSize;
    protected int[] fContext = new int[8];
    protected int fCurrentContext;
    protected String[] fPrefixes = new String[16];

    public NamespaceSupport() {
    }

    public NamespaceSupport(NamespaceContext namespaceContext) {
        this.pushContext();
        Enumeration enumeration = namespaceContext.getAllPrefixes();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            String string2 = namespaceContext.getURI(string);
            this.declarePrefix(string, string2);
        }
    }

    public void reset() {
        this.fNamespaceSize = 0;
        this.fCurrentContext = 0;
        this.fContext[this.fCurrentContext] = this.fNamespaceSize;
        this.fNamespace[this.fNamespaceSize++] = XMLSymbols.PREFIX_XML;
        this.fNamespace[this.fNamespaceSize++] = NamespaceContext.XML_URI;
        this.fNamespace[this.fNamespaceSize++] = XMLSymbols.PREFIX_XMLNS;
        this.fNamespace[this.fNamespaceSize++] = NamespaceContext.XMLNS_URI;
        ++this.fCurrentContext;
    }

    public void pushContext() {
        if (this.fCurrentContext + 1 == this.fContext.length) {
            int[] arrn = new int[this.fContext.length * 2];
            System.arraycopy(this.fContext, 0, arrn, 0, this.fContext.length);
            this.fContext = arrn;
        }
        this.fContext[++this.fCurrentContext] = this.fNamespaceSize;
    }

    public void popContext() {
        this.fNamespaceSize = this.fContext[this.fCurrentContext--];
    }

    public boolean declarePrefix(String string, String string2) {
        if (string == XMLSymbols.PREFIX_XML || string == XMLSymbols.PREFIX_XMLNS) {
            return false;
        }
        int n2 = this.fNamespaceSize;
        while (n2 > this.fContext[this.fCurrentContext]) {
            if (this.fNamespace[n2 - 2] == string) {
                this.fNamespace[n2 - 1] = string2;
                return true;
            }
            n2 -= 2;
        }
        if (this.fNamespaceSize == this.fNamespace.length) {
            String[] arrstring = new String[this.fNamespaceSize * 2];
            System.arraycopy(this.fNamespace, 0, arrstring, 0, this.fNamespaceSize);
            this.fNamespace = arrstring;
        }
        this.fNamespace[this.fNamespaceSize++] = string;
        this.fNamespace[this.fNamespaceSize++] = string2;
        return true;
    }

    public String getURI(String string) {
        int n2 = this.fNamespaceSize;
        while (n2 > 0) {
            if (this.fNamespace[n2 - 2] == string) {
                return this.fNamespace[n2 - 1];
            }
            n2 -= 2;
        }
        return null;
    }

    public String getPrefix(String string) {
        int n2 = this.fNamespaceSize;
        while (n2 > 0) {
            if (this.fNamespace[n2 - 1] == string && this.getURI(this.fNamespace[n2 - 2]) == string) {
                return this.fNamespace[n2 - 2];
            }
            n2 -= 2;
        }
        return null;
    }

    public int getDeclaredPrefixCount() {
        return (this.fNamespaceSize - this.fContext[this.fCurrentContext]) / 2;
    }

    public String getDeclaredPrefixAt(int n2) {
        return this.fNamespace[this.fContext[this.fCurrentContext] + n2 * 2];
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
        int n3 = 2;
        while (n3 < this.fNamespaceSize - 2) {
            object = this.fNamespace[n3 + 2];
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
        return new Prefixes(this, this.fPrefixes, n2);
    }

    public boolean containsPrefix(String string) {
        int n2 = this.fNamespaceSize;
        while (n2 > 0) {
            if (this.fNamespace[n2 - 2] == string) {
                return true;
            }
            n2 -= 2;
        }
        return false;
    }

    protected final class Prefixes
    implements Enumeration {
        private String[] prefixes;
        private int counter;
        private int size;
        private final NamespaceSupport this$0;

        public Prefixes(NamespaceSupport namespaceSupport, String[] arrstring, int n2) {
            this.this$0 = namespaceSupport;
            this.counter = 0;
            this.size = 0;
            this.prefixes = arrstring;
            this.size = n2;
        }

        public boolean hasMoreElements() {
            return this.counter < this.size;
        }

        public Object nextElement() {
            if (this.counter < this.size) {
                return this.this$0.fPrefixes[this.counter++];
            }
            throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            int n2 = 0;
            while (n2 < this.size) {
                stringBuffer.append(this.prefixes[n2]);
                stringBuffer.append(" ");
                ++n2;
            }
            return stringBuffer.toString();
        }
    }

}

