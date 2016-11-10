/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;

public class XSNamedMapImpl
extends AbstractMap
implements XSNamedMap {
    public static final XSNamedMapImpl EMPTY_MAP = new XSNamedMapImpl(new XSObject[0], 0);
    final String[] fNamespaces;
    final int fNSNum;
    final SymbolHash[] fMaps;
    XSObject[] fArray = null;
    int fLength = -1;
    private Set fEntrySet = null;

    public XSNamedMapImpl(String string, SymbolHash symbolHash) {
        this.fNamespaces = new String[]{string};
        this.fMaps = new SymbolHash[]{symbolHash};
        this.fNSNum = 1;
    }

    public XSNamedMapImpl(String[] arrstring, SymbolHash[] arrsymbolHash, int n2) {
        this.fNamespaces = arrstring;
        this.fMaps = arrsymbolHash;
        this.fNSNum = n2;
    }

    public XSNamedMapImpl(XSObject[] arrxSObject, int n2) {
        if (n2 == 0) {
            this.fNamespaces = null;
            this.fMaps = null;
            this.fNSNum = 0;
            this.fArray = arrxSObject;
            this.fLength = 0;
            return;
        }
        this.fNamespaces = new String[]{arrxSObject[0].getNamespace()};
        this.fMaps = null;
        this.fNSNum = 1;
        this.fArray = arrxSObject;
        this.fLength = n2;
    }

    public synchronized int getLength() {
        if (this.fLength == -1) {
            this.fLength = 0;
            int n2 = 0;
            while (n2 < this.fNSNum) {
                this.fLength += this.fMaps[n2].getLength();
                ++n2;
            }
        }
        return this.fLength;
    }

    public XSObject itemByName(String string, String string2) {
        int n2 = 0;
        while (n2 < this.fNSNum) {
            if (XSNamedMapImpl.isEqual(string, this.fNamespaces[n2])) {
                if (this.fMaps != null) {
                    return (XSObject)this.fMaps[n2].get(string2);
                }
                int n3 = 0;
                while (n3 < this.fLength) {
                    XSObject xSObject = this.fArray[n3];
                    if (xSObject.getName().equals(string2)) {
                        return xSObject;
                    }
                    ++n3;
                }
                return null;
            }
            ++n2;
        }
        return null;
    }

    public synchronized XSObject item(int n2) {
        if (this.fArray == null) {
            this.getLength();
            this.fArray = new XSObject[this.fLength];
            int n3 = 0;
            int n4 = 0;
            while (n4 < this.fNSNum) {
                n3 += this.fMaps[n4].getValues(this.fArray, n3);
                ++n4;
            }
        }
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fArray[n2];
    }

    static boolean isEqual(String string, String string2) {
        return string != null ? string.equals(string2) : string2 == null;
    }

    public boolean containsKey(Object object) {
        return this.get(object) != null;
    }

    public Object get(Object object) {
        if (object instanceof QName) {
            QName qName = (QName)object;
            String string = qName.getNamespaceURI();
            if ("".equals(string)) {
                string = null;
            }
            String string2 = qName.getLocalPart();
            return this.itemByName(string, string2);
        }
        return null;
    }

    public int size() {
        return this.getLength();
    }

    public synchronized Set entrySet() {
        if (this.fEntrySet == null) {
            int n2 = this.getLength();
            XSNamedMapEntry[] arrxSNamedMapEntry = new XSNamedMapEntry[n2];
            int n3 = 0;
            while (n3 < n2) {
                XSObject xSObject = this.item(n3);
                arrxSNamedMapEntry[n3] = new XSNamedMapEntry(this, new QName(xSObject.getNamespace(), xSObject.getName()), xSObject);
                ++n3;
            }
            this.fEntrySet = new AbstractSet(this, n2, arrxSNamedMapEntry){
                private final int val$length;
                private final XSNamedMapEntry[] val$entries;
                private final XSNamedMapImpl this$0;

                public Iterator iterator() {
                    return new Iterator(this){
                        private int index;
                        private final  this$1;

                        public boolean hasNext() {
                            return this.index < .access$000(this.this$1);
                        }

                        public Object next() {
                            if (this.index < .access$000(this.this$1)) {
                                return .access$100(this.this$1)[this.index++];
                            }
                            throw new java.util.NoSuchElementException();
                        }

                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                public int size() {
                    return this.val$length;
                }

                static int access$000( var0) {
                    return var0.val$length;
                }

                static XSNamedMapEntry[] access$100( var0) {
                    return var0.val$entries;
                }
            };
        }
        return this.fEntrySet;
    }

    private final class XSNamedMapEntry
    implements Map.Entry {
        private final QName key;
        private final XSObject value;
        private final XSNamedMapImpl this$0;

        public XSNamedMapEntry(XSNamedMapImpl xSNamedMapImpl, QName qName, XSObject xSObject) {
            this.this$0 = xSNamedMapImpl;
            this.key = qName;
            this.value = xSObject;
        }

        public Object getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public Object setValue(Object object) {
            throw new UnsupportedOperationException();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public boolean equals(Object object) {
            boolean bl;
            if (!(object instanceof Map.Entry)) return false;
            Map.Entry entry = (Map.Entry)object;
            Object k2 = entry.getKey();
            Object v2 = entry.getValue();
            if (this.key == null) {
                if (k2 != null) return false;
                bl = true;
            } else {
                bl = this.key.equals(k2);
            }
            if (!bl) return false;
            if (this.value == null) {
                if (v2 != null) return false;
                return true;
            }
            boolean bl2 = this.value.equals(v2);
            if (!bl2) return false;
            return true;
        }

        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(String.valueOf(this.key));
            stringBuffer.append('=');
            stringBuffer.append(String.valueOf(this.value));
            return stringBuffer.toString();
        }
    }

}

