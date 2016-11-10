/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.util.Enumeration;
import java.util.Hashtable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class NamespaceMappings {
    private int count = 0;
    private Hashtable m_namespaces = new Hashtable();
    private Stack m_nodeStack;

    public NamespaceMappings() {
        this.m_nodeStack = new Stack(this);
        this.initNamespaces();
    }

    private void initNamespaces() {
        MappingRecord mappingRecord = new MappingRecord("", "", -1);
        Stack stack = this.createPrefixStack("");
        stack.push(mappingRecord);
        mappingRecord = new MappingRecord("xml", "http://www.w3.org/XML/1998/namespace", -1);
        stack = this.createPrefixStack("xml");
        stack.push(mappingRecord);
    }

    public String lookupNamespace(String string) {
        String string2 = null;
        Stack stack = this.getPrefixStack(string);
        if (stack != null && !stack.isEmpty()) {
            string2 = ((MappingRecord)stack.peek()).m_uri;
        }
        if (string2 == null) {
            string2 = "";
        }
        return string2;
    }

    MappingRecord getMappingFromPrefix(String string) {
        Stack stack = (Stack)this.m_namespaces.get(string);
        return stack != null && !stack.isEmpty() ? (MappingRecord)stack.peek() : null;
    }

    public String lookupPrefix(String string) {
        String string2 = null;
        Enumeration enumeration = this.m_namespaces.keys();
        while (enumeration.hasMoreElements()) {
            String string3 = (String)enumeration.nextElement();
            String string4 = this.lookupNamespace(string3);
            if (string4 == null || !string4.equals(string)) continue;
            string2 = string3;
            break;
        }
        return string2;
    }

    public boolean pushNamespace(String string, String string2, int n2) {
        MappingRecord mappingRecord;
        if (string.startsWith("xml")) {
            return false;
        }
        Stack stack = (Stack)this.m_namespaces.get(string);
        if (stack == null) {
            stack = new Stack(this);
            this.m_namespaces.put(string, stack);
        }
        if (!stack.empty()) {
            mappingRecord = (MappingRecord)stack.peek();
            if (string2.equals(mappingRecord.m_uri) || n2 == mappingRecord.m_declarationDepth) {
                return false;
            }
        }
        mappingRecord = new MappingRecord(string, string2, n2);
        stack.push(mappingRecord);
        this.m_nodeStack.push(mappingRecord);
        return true;
    }

    void popNamespaces(int n2, ContentHandler contentHandler) {
        do {
            String string;
            Stack stack;
            MappingRecord mappingRecord;
            if (this.m_nodeStack.isEmpty()) {
                return;
            }
            MappingRecord mappingRecord2 = (MappingRecord)this.m_nodeStack.peek();
            int n3 = mappingRecord2.m_declarationDepth;
            if (n2 < 1 || mappingRecord2.m_declarationDepth < n2) break;
            MappingRecord mappingRecord3 = (MappingRecord)this.m_nodeStack.pop();
            if (mappingRecord3 != (mappingRecord = (MappingRecord)(stack = this.getPrefixStack(string = mappingRecord2.m_prefix)).peek())) continue;
            stack.pop();
            if (contentHandler == null) continue;
            try {
                contentHandler.endPrefixMapping(string);
            }
            catch (SAXException sAXException) {}
        } while (true);
    }

    public String generateNextPrefix() {
        return "ns" + this.count++;
    }

    public Object clone() throws CloneNotSupportedException {
        NamespaceMappings namespaceMappings = new NamespaceMappings();
        namespaceMappings.m_nodeStack = (Stack)this.m_nodeStack.clone();
        namespaceMappings.count = this.count;
        namespaceMappings.m_namespaces = (Hashtable)this.m_namespaces.clone();
        namespaceMappings.count = this.count;
        return namespaceMappings;
    }

    final void reset() {
        this.count = 0;
        this.m_namespaces.clear();
        this.m_nodeStack.clear();
        this.initNamespaces();
    }

    private Stack getPrefixStack(String string) {
        Stack stack = (Stack)this.m_namespaces.get(string);
        return stack;
    }

    private Stack createPrefixStack(String string) {
        Stack stack = new Stack(this);
        this.m_namespaces.put(string, stack);
        return stack;
    }

    private class Stack {
        private int top;
        private int max;
        Object[] m_stack;
        private final NamespaceMappings this$0;

        public Object clone() throws CloneNotSupportedException {
            Stack stack = new Stack(this.this$0);
            stack.max = this.max;
            stack.top = this.top;
            stack.m_stack = new Object[stack.max];
            for (int i2 = 0; i2 <= this.top; ++i2) {
                stack.m_stack[i2] = this.m_stack[i2];
            }
            return stack;
        }

        public Stack(NamespaceMappings namespaceMappings) {
            this.this$0 = namespaceMappings;
            this.top = -1;
            this.max = 20;
            this.m_stack = new Object[this.max];
        }

        public Object push(Object object) {
            ++this.top;
            if (this.max <= this.top) {
                int n2 = 2 * this.max + 1;
                Object[] arrobject = new Object[n2];
                System.arraycopy(this.m_stack, 0, arrobject, 0, this.max);
                this.max = n2;
                this.m_stack = arrobject;
            }
            this.m_stack[this.top] = object;
            return object;
        }

        public Object pop() {
            Object object;
            if (0 <= this.top) {
                object = this.m_stack[this.top];
                --this.top;
            } else {
                object = null;
            }
            return object;
        }

        public Object peek() {
            Object object = 0 <= this.top ? this.m_stack[this.top] : null;
            return object;
        }

        public boolean isEmpty() {
            return this.top < 0;
        }

        public boolean empty() {
            return this.top < 0;
        }

        public void clear() {
            for (int i2 = 0; i2 <= this.top; ++i2) {
                this.m_stack[i2] = null;
            }
            this.top = -1;
        }
    }

    static class MappingRecord {
        final String m_prefix;
        final String m_uri;
        final int m_declarationDepth;

        MappingRecord(String string, String string2, int n2) {
            this.m_prefix = string;
            this.m_uri = string2 == null ? "" : string2;
            this.m_declarationDepth = n2;
        }
    }

}

