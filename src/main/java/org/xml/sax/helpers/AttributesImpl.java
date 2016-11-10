/*
 * Decompiled with CFR 0_119.
 */
package org.xml.sax.helpers;

import org.xml.sax.Attributes;

public class AttributesImpl
implements Attributes {
    int length;
    String[] data;

    public AttributesImpl() {
        this.length = 0;
        this.data = null;
    }

    public AttributesImpl(Attributes attributes) {
        this.setAttributes(attributes);
    }

    public int getLength() {
        return this.length;
    }

    public String getURI(int n2) {
        if (n2 >= 0 && n2 < this.length) {
            return this.data[n2 * 5];
        }
        return null;
    }

    public String getLocalName(int n2) {
        if (n2 >= 0 && n2 < this.length) {
            return this.data[n2 * 5 + 1];
        }
        return null;
    }

    public String getQName(int n2) {
        if (n2 >= 0 && n2 < this.length) {
            return this.data[n2 * 5 + 2];
        }
        return null;
    }

    public String getType(int n2) {
        if (n2 >= 0 && n2 < this.length) {
            return this.data[n2 * 5 + 3];
        }
        return null;
    }

    public String getValue(int n2) {
        if (n2 >= 0 && n2 < this.length) {
            return this.data[n2 * 5 + 4];
        }
        return null;
    }

    public int getIndex(String string, String string2) {
        int n2 = this.length * 5;
        int n3 = 0;
        while (n3 < n2) {
            if (this.data[n3].equals(string) && this.data[n3 + 1].equals(string2)) {
                return n3 / 5;
            }
            n3 += 5;
        }
        return -1;
    }

    public int getIndex(String string) {
        int n2 = this.length * 5;
        int n3 = 0;
        while (n3 < n2) {
            if (this.data[n3 + 2].equals(string)) {
                return n3 / 5;
            }
            n3 += 5;
        }
        return -1;
    }

    public String getValue(String string) {
        int n2 = this.length * 5;
        int n3 = 0;
        while (n3 < n2) {
            if (this.data[n3 + 2].equals(string)) {
                return this.data[n3 + 4];
            }
            n3 += 5;
        }
        return null;
    }

    public void clear() {
        if (this.data != null) {
            int n2 = 0;
            while (n2 < this.length * 5) {
                this.data[n2] = null;
                ++n2;
            }
        }
        this.length = 0;
    }

    public void setAttributes(Attributes attributes) {
        this.clear();
        this.length = attributes.getLength();
        if (this.length > 0) {
            this.data = new String[this.length * 5];
            int n2 = 0;
            while (n2 < this.length) {
                this.data[n2 * 5] = attributes.getURI(n2);
                this.data[n2 * 5 + 1] = attributes.getLocalName(n2);
                this.data[n2 * 5 + 2] = attributes.getQName(n2);
                this.data[n2 * 5 + 3] = attributes.getType(n2);
                this.data[n2 * 5 + 4] = attributes.getValue(n2);
                ++n2;
            }
        }
    }

    public void addAttribute(String string, String string2, String string3, String string4, String string5) {
        this.ensureCapacity(this.length + 1);
        this.data[this.length * 5] = string;
        this.data[this.length * 5 + 1] = string2;
        this.data[this.length * 5 + 2] = string3;
        this.data[this.length * 5 + 3] = string4;
        this.data[this.length * 5 + 4] = string5;
        ++this.length;
    }

    public void setAttribute(int n2, String string, String string2, String string3, String string4, String string5) {
        if (n2 >= 0 && n2 < this.length) {
            this.data[n2 * 5] = string;
            this.data[n2 * 5 + 1] = string2;
            this.data[n2 * 5 + 2] = string3;
            this.data[n2 * 5 + 3] = string4;
            this.data[n2 * 5 + 4] = string5;
        } else {
            this.badIndex(n2);
        }
    }

    public void removeAttribute(int n2) {
        if (n2 >= 0 && n2 < this.length) {
            if (n2 < this.length - 1) {
                System.arraycopy(this.data, (n2 + 1) * 5, this.data, n2 * 5, (this.length - n2 - 1) * 5);
            }
            n2 = (this.length - 1) * 5;
            this.data[n2++] = null;
            this.data[n2++] = null;
            this.data[n2++] = null;
            this.data[n2++] = null;
            this.data[n2] = null;
            --this.length;
        } else {
            this.badIndex(n2);
        }
    }

    public void setValue(int n2, String string) {
        if (n2 >= 0 && n2 < this.length) {
            this.data[n2 * 5 + 4] = string;
        } else {
            this.badIndex(n2);
        }
    }

    private void ensureCapacity(int n2) {
        int n3;
        if (n2 <= 0) {
            return;
        }
        if (this.data == null || this.data.length == 0) {
            n3 = 25;
        } else {
            if (this.data.length >= n2 * 5) {
                return;
            }
            n3 = this.data.length;
        }
        while (n3 < n2 * 5) {
            n3 *= 2;
        }
        String[] arrstring = new String[n3];
        if (this.length > 0) {
            System.arraycopy(this.data, 0, arrstring, 0, this.length * 5);
        }
        this.data = arrstring;
    }

    private void badIndex(int n2) throws ArrayIndexOutOfBoundsException {
        String string = "Attempt to modify attribute at illegal index: " + n2;
        throw new ArrayIndexOutOfBoundsException(string);
    }
}

