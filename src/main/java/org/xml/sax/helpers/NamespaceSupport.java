/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax.helpers;

import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class NamespaceSupport {
    private static final Enumeration EMPTY_ENUMERATION = new Vector().elements();
    private Context[] contexts;
    private Context currentContext;
    private int contextPos;
    private boolean namespaceDeclUris;

    public NamespaceSupport() {
        this.reset();
    }

    public void reset() {
        this.contexts = new Context[32];
        this.namespaceDeclUris = false;
        this.contextPos = 0;
        this.contexts[this.contextPos] = this.currentContext = new Context(this);
        this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
    }

    public void pushContext() {
        int n2 = this.contexts.length;
        ++this.contextPos;
        if (this.contextPos >= n2) {
            Context[] arrcontext = new Context[n2 * 2];
            System.arraycopy(this.contexts, 0, arrcontext, 0, n2);
            n2 *= 2;
            this.contexts = arrcontext;
        }
        this.currentContext = this.contexts[this.contextPos];
        if (this.currentContext == null) {
            this.contexts[this.contextPos] = this.currentContext = new Context(this);
        }
        if (this.contextPos > 0) {
            this.currentContext.setParent(this.contexts[this.contextPos - 1]);
        }
    }

    public void popContext() {
        this.contexts[this.contextPos].clear();
        --this.contextPos;
        if (this.contextPos < 0) {
            throw new EmptyStackException();
        }
        this.currentContext = this.contexts[this.contextPos];
    }

    public boolean declarePrefix(String string, String string2) {
        if (string.equals("xml") || string.equals("xmlns")) {
            return false;
        }
        this.currentContext.declarePrefix(string, string2);
        return true;
    }

    public String[] processName(String string, String[] arrstring, boolean bl) {
        String[] arrstring2 = this.currentContext.processName(string, bl);
        if (arrstring2 == null) {
            return null;
        }
        arrstring[0] = arrstring2[0];
        arrstring[1] = arrstring2[1];
        arrstring[2] = arrstring2[2];
        return arrstring;
    }

    public String getURI(String string) {
        return this.currentContext.getURI(string);
    }

    public Enumeration getDeclaredPrefixes() {
        return this.currentContext.getDeclaredPrefixes();
    }

    public void setNamespaceDeclUris(boolean bl) {
        if (this.contextPos != 0) {
            throw new IllegalStateException();
        }
        if (bl == this.namespaceDeclUris) {
            return;
        }
        this.namespaceDeclUris = bl;
        if (bl) {
            this.currentContext.declarePrefix("xmlns", "http://www.w3.org/xmlns/2000/");
        } else {
            this.contexts[this.contextPos] = this.currentContext = new Context(this);
            this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
        }
    }

    static boolean access$000(NamespaceSupport namespaceSupport) {
        return namespaceSupport.namespaceDeclUris;
    }

    static Enumeration access$100() {
        return EMPTY_ENUMERATION;
    }

    final class Context {
        Hashtable prefixTable;
        Hashtable uriTable;
        Hashtable elementNameTable;
        Hashtable attributeNameTable;
        String defaultNS;
        private Vector declarations;
        private boolean declSeen;
        private Context parent;
        private final NamespaceSupport this$0;

        Context(NamespaceSupport namespaceSupport) {
            this.this$0 = namespaceSupport;
            this.defaultNS = null;
            this.declarations = null;
            this.declSeen = false;
            this.parent = null;
            this.copyTables();
        }

        void setParent(Context context) {
            this.parent = context;
            this.declarations = null;
            this.prefixTable = context.prefixTable;
            this.uriTable = context.uriTable;
            this.elementNameTable = context.elementNameTable;
            this.attributeNameTable = context.attributeNameTable;
            this.defaultNS = context.defaultNS;
            this.declSeen = false;
        }

        void clear() {
            this.parent = null;
            this.prefixTable = null;
            this.uriTable = null;
            this.elementNameTable = null;
            this.attributeNameTable = null;
            this.defaultNS = null;
        }

        void declarePrefix(String string, String string2) {
            if (!this.declSeen) {
                this.copyTables();
            }
            if (this.declarations == null) {
                this.declarations = new Vector();
            }
            string = string.intern();
            string2 = string2.intern();
            if ("".equals(string)) {
                this.defaultNS = "".equals(string2) ? null : string2;
            } else {
                this.prefixTable.put(string, string2);
                this.uriTable.put(string2, string);
            }
            this.declarations.addElement(string);
        }

        String[] processName(String string, boolean bl) {
            Hashtable hashtable = bl ? this.attributeNameTable : this.elementNameTable;
            String[] arrstring = (String[])hashtable.get(string);
            if (arrstring != null) {
                return arrstring;
            }
            arrstring = new String[3];
            arrstring[2] = string.intern();
            int n2 = string.indexOf(58);
            if (n2 == -1) {
                arrstring[0] = bl ? (string == "xmlns" && NamespaceSupport.access$000(this.this$0) ? "http://www.w3.org/xmlns/2000/" : "") : (this.defaultNS == null ? "" : this.defaultNS);
                arrstring[1] = arrstring[2];
            } else {
                String string2 = string.substring(0, n2);
                String string3 = string.substring(n2 + 1);
                String string4 = "".equals(string2) ? this.defaultNS : (String)this.prefixTable.get(string2);
                if (string4 == null || !bl && "xmlns".equals(string2)) {
                    return null;
                }
                arrstring[0] = string4;
                arrstring[1] = string3.intern();
            }
            hashtable.put(arrstring[2], arrstring);
            return arrstring;
        }

        String getURI(String string) {
            if ("".equals(string)) {
                return this.defaultNS;
            }
            if (this.prefixTable == null) {
                return null;
            }
            return (String)this.prefixTable.get(string);
        }

        Enumeration getDeclaredPrefixes() {
            if (this.declarations == null) {
                return NamespaceSupport.access$100();
            }
            return this.declarations.elements();
        }

        private void copyTables() {
            this.prefixTable = this.prefixTable != null ? (Hashtable)this.prefixTable.clone() : new Hashtable();
            this.uriTable = this.uriTable != null ? (Hashtable)this.uriTable.clone() : new Hashtable();
            this.elementNameTable = new Hashtable();
            this.attributeNameTable = new Hashtable();
            this.declSeen = true;
        }
    }

}

