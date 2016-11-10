/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.dv.xs;

import java.util.AbstractList;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.xs.datatypes.ObjectList;

public class ListDV
extends TypeValidator {
    public short getAllowedFacets() {
        return 2079;
    }

    public Object getActualValue(String string, ValidationContext validationContext) throws InvalidDatatypeValueException {
        return string;
    }

    public int getDataLength(Object object) {
        return ((ListData)object).getLength();
    }

    static final class ListData
    extends AbstractList
    implements ObjectList {
        final Object[] data;
        private String canonical;

        public ListData(Object[] arrobject) {
            this.data = arrobject;
        }

        public synchronized String toString() {
            if (this.canonical == null) {
                int n2 = this.data.length;
                StringBuffer stringBuffer = new StringBuffer();
                if (n2 > 0) {
                    stringBuffer.append(this.data[0].toString());
                }
                int n3 = 1;
                while (n3 < n2) {
                    stringBuffer.append(' ');
                    stringBuffer.append(this.data[n3].toString());
                    ++n3;
                }
                this.canonical = stringBuffer.toString();
            }
            return this.canonical;
        }

        public int getLength() {
            return this.data.length;
        }

        public boolean equals(Object object) {
            if (!(object instanceof ListData)) {
                return false;
            }
            int n2 = this.data.length;
            Object[] arrobject = ((ListData)object).data;
            if (n2 != arrobject.length) {
                return false;
            }
            int n3 = 0;
            while (n3 < n2) {
                if (!this.data[n3].equals(arrobject[n3])) {
                    return false;
                }
                ++n3;
            }
            return true;
        }

        public int hashCode() {
            int n2 = 0;
            int n3 = 0;
            while (n3 < this.data.length) {
                n2 ^= this.data[n3].hashCode();
                ++n3;
            }
            return n2;
        }

        public boolean contains(Object object) {
            int n2 = 0;
            while (n2 < this.data.length) {
                if (object == this.data[n2]) {
                    return true;
                }
                ++n2;
            }
            return false;
        }

        public Object item(int n2) {
            if (n2 < 0 || n2 >= this.data.length) {
                return null;
            }
            return this.data[n2];
        }

        public Object get(int n2) {
            if (n2 >= 0 && n2 < this.data.length) {
                return this.data[n2];
            }
            throw new IndexOutOfBoundsException("Index: " + n2);
        }

        public int size() {
            return this.getLength();
        }
    }

}

