/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;

public class XMLAttributesImpl
implements XMLAttributes {
    protected static final int TABLE_SIZE = 101;
    protected static final int SIZE_LIMIT = 20;
    protected boolean fNamespaces = true;
    protected int fLargeCount = 1;
    protected int fLength;
    protected Attribute[] fAttributes = new Attribute[4];
    protected Attribute[] fAttributeTableView;
    protected int[] fAttributeTableViewChainState;
    protected int fTableViewBuckets;
    protected boolean fIsTableViewConsistent;

    public XMLAttributesImpl() {
        this(101);
    }

    public XMLAttributesImpl(int n2) {
        this.fTableViewBuckets = n2;
        int n3 = 0;
        while (n3 < this.fAttributes.length) {
            this.fAttributes[n3] = new Attribute();
            ++n3;
        }
    }

    public void setNamespaces(boolean bl) {
        this.fNamespaces = bl;
    }

    public int addAttribute(QName qName, String string, String string2) {
        Attribute[] arrattribute;
        int n2;
        if (this.fLength < 20) {
            int n3 = n2 = qName.uri != null && qName.uri.length() != 0 ? this.getIndexFast(qName.uri, qName.localpart) : this.getIndexFast(qName.rawname);
            if (n2 == -1) {
                n2 = this.fLength;
                if (this.fLength++ == this.fAttributes.length) {
                    arrattribute = new Attribute[this.fAttributes.length + 4];
                    System.arraycopy(this.fAttributes, 0, arrattribute, 0, this.fAttributes.length);
                    int n4 = this.fAttributes.length;
                    while (n4 < arrattribute.length) {
                        arrattribute[n4] = new Attribute();
                        ++n4;
                    }
                    this.fAttributes = arrattribute;
                }
            }
        } else if (qName.uri == null || qName.uri.length() == 0 || (n2 = this.getIndexFast(qName.uri, qName.localpart)) == -1) {
            int n5;
            if (!this.fIsTableViewConsistent || this.fLength == 20) {
                this.prepareAndPopulateTableView();
                this.fIsTableViewConsistent = true;
            }
            if (this.fAttributeTableViewChainState[n5 = this.getTableViewBucket(qName.rawname)] != this.fLargeCount) {
                n2 = this.fLength;
                if (this.fLength++ == this.fAttributes.length) {
                    Attribute[] arrattribute2 = new Attribute[this.fAttributes.length << 1];
                    System.arraycopy(this.fAttributes, 0, arrattribute2, 0, this.fAttributes.length);
                    int n6 = this.fAttributes.length;
                    while (n6 < arrattribute2.length) {
                        arrattribute2[n6] = new Attribute();
                        ++n6;
                    }
                    this.fAttributes = arrattribute2;
                }
                this.fAttributeTableViewChainState[n5] = this.fLargeCount;
                this.fAttributes[n2].next = null;
                this.fAttributeTableView[n5] = this.fAttributes[n2];
            } else {
                Attribute attribute = this.fAttributeTableView[n5];
                while (attribute != null) {
                    if (attribute.name.rawname == qName.rawname) break;
                    attribute = attribute.next;
                }
                if (attribute == null) {
                    n2 = this.fLength;
                    if (this.fLength++ == this.fAttributes.length) {
                        Attribute[] arrattribute3 = new Attribute[this.fAttributes.length << 1];
                        System.arraycopy(this.fAttributes, 0, arrattribute3, 0, this.fAttributes.length);
                        int n7 = this.fAttributes.length;
                        while (n7 < arrattribute3.length) {
                            arrattribute3[n7] = new Attribute();
                            ++n7;
                        }
                        this.fAttributes = arrattribute3;
                    }
                    this.fAttributes[n2].next = this.fAttributeTableView[n5];
                    this.fAttributeTableView[n5] = this.fAttributes[n2];
                } else {
                    n2 = this.getIndexFast(qName.rawname);
                }
            }
        }
        arrattribute = this.fAttributes[n2];
        arrattribute.name.setValues(qName);
        arrattribute.type = string;
        arrattribute.value = string2;
        arrattribute.nonNormalizedValue = string2;
        arrattribute.specified = false;
        arrattribute.augs.removeAllItems();
        return n2;
    }

    public void removeAllAttributes() {
        this.fLength = 0;
    }

    public void removeAttributeAt(int n2) {
        this.fIsTableViewConsistent = false;
        if (n2 < this.fLength - 1) {
            Attribute attribute = this.fAttributes[n2];
            System.arraycopy(this.fAttributes, n2 + 1, this.fAttributes, n2, this.fLength - n2 - 1);
            this.fAttributes[this.fLength - 1] = attribute;
        }
        --this.fLength;
    }

    public void setName(int n2, QName qName) {
        this.fAttributes[n2].name.setValues(qName);
    }

    public void getName(int n2, QName qName) {
        qName.setValues(this.fAttributes[n2].name);
    }

    public void setType(int n2, String string) {
        this.fAttributes[n2].type = string;
    }

    public void setValue(int n2, String string) {
        Attribute attribute = this.fAttributes[n2];
        attribute.value = string;
        attribute.nonNormalizedValue = string;
    }

    public void setNonNormalizedValue(int n2, String string) {
        if (string == null) {
            string = this.fAttributes[n2].value;
        }
        this.fAttributes[n2].nonNormalizedValue = string;
    }

    public String getNonNormalizedValue(int n2) {
        String string = this.fAttributes[n2].nonNormalizedValue;
        return string;
    }

    public void setSpecified(int n2, boolean bl) {
        this.fAttributes[n2].specified = bl;
    }

    public boolean isSpecified(int n2) {
        return this.fAttributes[n2].specified;
    }

    public int getLength() {
        return this.fLength;
    }

    public String getType(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.getReportableType(this.fAttributes[n2].type);
    }

    public String getType(String string) {
        int n2 = this.getIndex(string);
        return n2 != -1 ? this.getReportableType(this.fAttributes[n2].type) : null;
    }

    public String getValue(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fAttributes[n2].value;
    }

    public String getValue(String string) {
        int n2 = this.getIndex(string);
        return n2 != -1 ? this.fAttributes[n2].value : null;
    }

    public String getName(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fAttributes[n2].name.rawname;
    }

    public int getIndex(String string) {
        int n2 = 0;
        while (n2 < this.fLength) {
            Attribute attribute = this.fAttributes[n2];
            if (attribute.name.rawname != null && attribute.name.rawname.equals(string)) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public int getIndex(String string, String string2) {
        int n2 = 0;
        while (n2 < this.fLength) {
            Attribute attribute = this.fAttributes[n2];
            if (attribute.name.localpart != null && attribute.name.localpart.equals(string2) && (string == attribute.name.uri || string != null && attribute.name.uri != null && attribute.name.uri.equals(string))) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public String getLocalName(int n2) {
        if (!this.fNamespaces) {
            return "";
        }
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fAttributes[n2].name.localpart;
    }

    public String getQName(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        String string = this.fAttributes[n2].name.rawname;
        return string != null ? string : "";
    }

    public String getType(String string, String string2) {
        if (!this.fNamespaces) {
            return null;
        }
        int n2 = this.getIndex(string, string2);
        return n2 != -1 ? this.getReportableType(this.fAttributes[n2].type) : null;
    }

    public String getPrefix(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        String string = this.fAttributes[n2].name.prefix;
        return string != null ? string : "";
    }

    public String getURI(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        String string = this.fAttributes[n2].name.uri;
        return string;
    }

    public String getValue(String string, String string2) {
        int n2 = this.getIndex(string, string2);
        return n2 != -1 ? this.getValue(n2) : null;
    }

    public Augmentations getAugmentations(String string, String string2) {
        int n2 = this.getIndex(string, string2);
        return n2 != -1 ? this.fAttributes[n2].augs : null;
    }

    public Augmentations getAugmentations(String string) {
        int n2 = this.getIndex(string);
        return n2 != -1 ? this.fAttributes[n2].augs : null;
    }

    public Augmentations getAugmentations(int n2) {
        if (n2 < 0 || n2 >= this.fLength) {
            return null;
        }
        return this.fAttributes[n2].augs;
    }

    public void setAugmentations(int n2, Augmentations augmentations) {
        this.fAttributes[n2].augs = augmentations;
    }

    public void setURI(int n2, String string) {
        this.fAttributes[n2].name.uri = string;
    }

    public int getIndexFast(String string) {
        int n2 = 0;
        while (n2 < this.fLength) {
            Attribute attribute = this.fAttributes[n2];
            if (attribute.name.rawname == string) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    public void addAttributeNS(QName qName, String string, String string2) {
        Attribute[] arrattribute;
        int n2 = this.fLength;
        if (this.fLength++ == this.fAttributes.length) {
            arrattribute = this.fLength < 20 ? new Attribute[this.fAttributes.length + 4] : new Attribute[this.fAttributes.length << 1];
            System.arraycopy(this.fAttributes, 0, arrattribute, 0, this.fAttributes.length);
            int n3 = this.fAttributes.length;
            while (n3 < arrattribute.length) {
                arrattribute[n3] = new Attribute();
                ++n3;
            }
            this.fAttributes = arrattribute;
        }
        arrattribute = this.fAttributes[n2];
        arrattribute.name.setValues(qName);
        arrattribute.type = string;
        arrattribute.value = string2;
        arrattribute.nonNormalizedValue = string2;
        arrattribute.specified = false;
        arrattribute.augs.removeAllItems();
    }

    public QName checkDuplicatesNS() {
        if (this.fLength <= 20) {
            int n2 = 0;
            while (n2 < this.fLength - 1) {
                Attribute attribute = this.fAttributes[n2];
                int n3 = n2 + 1;
                while (n3 < this.fLength) {
                    Attribute attribute2 = this.fAttributes[n3];
                    if (attribute.name.localpart == attribute2.name.localpart && attribute.name.uri == attribute2.name.uri) {
                        return attribute2.name;
                    }
                    ++n3;
                }
                ++n2;
            }
        } else {
            this.fIsTableViewConsistent = false;
            this.prepareTableView();
            int n4 = this.fLength - 1;
            while (n4 >= 0) {
                Attribute attribute = this.fAttributes[n4];
                int n5 = this.getTableViewBucket(attribute.name.localpart, attribute.name.uri);
                if (this.fAttributeTableViewChainState[n5] != this.fLargeCount) {
                    this.fAttributeTableViewChainState[n5] = this.fLargeCount;
                    attribute.next = null;
                    this.fAttributeTableView[n5] = attribute;
                } else {
                    Attribute attribute3 = this.fAttributeTableView[n5];
                    while (attribute3 != null) {
                        if (attribute3.name.localpart == attribute.name.localpart && attribute3.name.uri == attribute.name.uri) {
                            return attribute.name;
                        }
                        attribute3 = attribute3.next;
                    }
                    attribute.next = this.fAttributeTableView[n5];
                    this.fAttributeTableView[n5] = attribute;
                }
                --n4;
            }
        }
        return null;
    }

    public int getIndexFast(String string, String string2) {
        int n2 = 0;
        while (n2 < this.fLength) {
            Attribute attribute = this.fAttributes[n2];
            if (attribute.name.localpart == string2 && attribute.name.uri == string) {
                return n2;
            }
            ++n2;
        }
        return -1;
    }

    private String getReportableType(String string) {
        if (string.charAt(0) == '(') {
            return "NMTOKEN";
        }
        return string;
    }

    protected int getTableViewBucket(String string) {
        return (string.hashCode() & Integer.MAX_VALUE) % this.fTableViewBuckets;
    }

    protected int getTableViewBucket(String string, String string2) {
        if (string2 == null) {
            return (string.hashCode() & Integer.MAX_VALUE) % this.fTableViewBuckets;
        }
        return (string.hashCode() + string2.hashCode() & Integer.MAX_VALUE) % this.fTableViewBuckets;
    }

    protected void cleanTableView() {
        if (++this.fLargeCount < 0) {
            if (this.fAttributeTableViewChainState != null) {
                int n2 = this.fTableViewBuckets - 1;
                while (n2 >= 0) {
                    this.fAttributeTableViewChainState[n2] = 0;
                    --n2;
                }
            }
            this.fLargeCount = 1;
        }
    }

    protected void prepareTableView() {
        if (this.fAttributeTableView == null) {
            this.fAttributeTableView = new Attribute[this.fTableViewBuckets];
            this.fAttributeTableViewChainState = new int[this.fTableViewBuckets];
        } else {
            this.cleanTableView();
        }
    }

    protected void prepareAndPopulateTableView() {
        this.prepareTableView();
        int n2 = 0;
        while (n2 < this.fLength) {
            Attribute attribute = this.fAttributes[n2];
            int n3 = this.getTableViewBucket(attribute.name.rawname);
            if (this.fAttributeTableViewChainState[n3] != this.fLargeCount) {
                this.fAttributeTableViewChainState[n3] = this.fLargeCount;
                attribute.next = null;
                this.fAttributeTableView[n3] = attribute;
            } else {
                attribute.next = this.fAttributeTableView[n3];
                this.fAttributeTableView[n3] = attribute;
            }
            ++n2;
        }
    }

    static class Attribute {
        public final QName name = new QName();
        public String type;
        public String value;
        public String nonNormalizedValue;
        public boolean specified;
        public Augmentations augs = new AugmentationsImpl();
        public Attribute next;

        Attribute() {
        }
    }

}

