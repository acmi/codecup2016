/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSWildcard;

public class XSWildcardDecl
implements XSWildcard {
    public static final String ABSENT = null;
    public short fType = 1;
    public short fProcessContents = 1;
    public String[] fNamespaceList;
    public XSObjectList fAnnotations = null;
    private String fDescription = null;

    public boolean allowNamespace(String string) {
        int n2;
        int n3;
        if (this.fType == 1) {
            return true;
        }
        if (this.fType == 2) {
            n2 = 0;
            n3 = this.fNamespaceList.length;
            int n4 = 0;
            while (n4 < n3 && n2 == 0) {
                if (string == this.fNamespaceList[n4]) {
                    n2 = 1;
                }
                ++n4;
            }
            if (n2 == 0) {
                return true;
            }
        }
        if (this.fType == 3) {
            n2 = this.fNamespaceList.length;
            n3 = 0;
            while (n3 < n2) {
                if (string == this.fNamespaceList[n3]) {
                    return true;
                }
                ++n3;
            }
        }
        return false;
    }

    public boolean isSubsetOf(XSWildcardDecl xSWildcardDecl) {
        if (xSWildcardDecl == null) {
            return false;
        }
        if (xSWildcardDecl.fType == 1) {
            return true;
        }
        if (this.fType == 2 && xSWildcardDecl.fType == 2 && this.fNamespaceList[0] == xSWildcardDecl.fNamespaceList[0]) {
            return true;
        }
        if (this.fType == 3) {
            if (xSWildcardDecl.fType == 3 && this.subset2sets(this.fNamespaceList, xSWildcardDecl.fNamespaceList)) {
                return true;
            }
            if (xSWildcardDecl.fType == 2 && !this.elementInSet(xSWildcardDecl.fNamespaceList[0], this.fNamespaceList) && !this.elementInSet(ABSENT, this.fNamespaceList)) {
                return true;
            }
        }
        return false;
    }

    public boolean weakerProcessContents(XSWildcardDecl xSWildcardDecl) {
        return this.fProcessContents == 3 && xSWildcardDecl.fProcessContents == 1 || this.fProcessContents == 2 && xSWildcardDecl.fProcessContents != 2;
    }

    public XSWildcardDecl performUnionWith(XSWildcardDecl xSWildcardDecl, short s2) {
        if (xSWildcardDecl == null) {
            return null;
        }
        XSWildcardDecl xSWildcardDecl2 = new XSWildcardDecl();
        xSWildcardDecl2.fProcessContents = s2;
        if (this.areSame(xSWildcardDecl)) {
            xSWildcardDecl2.fType = this.fType;
            xSWildcardDecl2.fNamespaceList = this.fNamespaceList;
        } else if (this.fType == 1 || xSWildcardDecl.fType == 1) {
            xSWildcardDecl2.fType = 1;
        } else if (this.fType == 3 && xSWildcardDecl.fType == 3) {
            xSWildcardDecl2.fType = 3;
            xSWildcardDecl2.fNamespaceList = this.union2sets(this.fNamespaceList, xSWildcardDecl.fNamespaceList);
        } else if (this.fType == 2 && xSWildcardDecl.fType == 2) {
            xSWildcardDecl2.fType = 2;
            xSWildcardDecl2.fNamespaceList = new String[2];
            xSWildcardDecl2.fNamespaceList[0] = ABSENT;
            xSWildcardDecl2.fNamespaceList[1] = ABSENT;
        } else if (this.fType == 2 && xSWildcardDecl.fType == 3 || this.fType == 3 && xSWildcardDecl.fType == 2) {
            String[] arrstring = null;
            String[] arrstring2 = null;
            if (this.fType == 2) {
                arrstring = this.fNamespaceList;
                arrstring2 = xSWildcardDecl.fNamespaceList;
            } else {
                arrstring = xSWildcardDecl.fNamespaceList;
                arrstring2 = this.fNamespaceList;
            }
            boolean bl = this.elementInSet(ABSENT, arrstring2);
            if (arrstring[0] != ABSENT) {
                boolean bl2 = this.elementInSet(arrstring[0], arrstring2);
                if (bl2 && bl) {
                    xSWildcardDecl2.fType = 1;
                } else if (bl2 && !bl) {
                    xSWildcardDecl2.fType = 2;
                    xSWildcardDecl2.fNamespaceList = new String[2];
                    xSWildcardDecl2.fNamespaceList[0] = ABSENT;
                    xSWildcardDecl2.fNamespaceList[1] = ABSENT;
                } else {
                    if (!bl2 && bl) {
                        return null;
                    }
                    xSWildcardDecl2.fType = 2;
                    xSWildcardDecl2.fNamespaceList = arrstring;
                }
            } else if (bl) {
                xSWildcardDecl2.fType = 1;
            } else {
                xSWildcardDecl2.fType = 2;
                xSWildcardDecl2.fNamespaceList = arrstring;
            }
        }
        return xSWildcardDecl2;
    }

    public XSWildcardDecl performIntersectionWith(XSWildcardDecl xSWildcardDecl, short s2) {
        if (xSWildcardDecl == null) {
            return null;
        }
        XSWildcardDecl xSWildcardDecl2 = new XSWildcardDecl();
        xSWildcardDecl2.fProcessContents = s2;
        if (this.areSame(xSWildcardDecl)) {
            xSWildcardDecl2.fType = this.fType;
            xSWildcardDecl2.fNamespaceList = this.fNamespaceList;
        } else if (this.fType == 1 || xSWildcardDecl.fType == 1) {
            XSWildcardDecl xSWildcardDecl3 = this;
            if (this.fType == 1) {
                xSWildcardDecl3 = xSWildcardDecl;
            }
            xSWildcardDecl2.fType = xSWildcardDecl3.fType;
            xSWildcardDecl2.fNamespaceList = xSWildcardDecl3.fNamespaceList;
        } else if (this.fType == 2 && xSWildcardDecl.fType == 3 || this.fType == 3 && xSWildcardDecl.fType == 2) {
            String[] arrstring = null;
            String[] arrstring2 = null;
            if (this.fType == 2) {
                arrstring2 = this.fNamespaceList;
                arrstring = xSWildcardDecl.fNamespaceList;
            } else {
                arrstring2 = xSWildcardDecl.fNamespaceList;
                arrstring = this.fNamespaceList;
            }
            int n2 = arrstring.length;
            String[] arrstring3 = new String[n2];
            int n3 = 0;
            int n4 = 0;
            while (n4 < n2) {
                if (arrstring[n4] != arrstring2[0] && arrstring[n4] != ABSENT) {
                    arrstring3[n3++] = arrstring[n4];
                }
                ++n4;
            }
            xSWildcardDecl2.fType = 3;
            xSWildcardDecl2.fNamespaceList = new String[n3];
            System.arraycopy(arrstring3, 0, xSWildcardDecl2.fNamespaceList, 0, n3);
        } else if (this.fType == 3 && xSWildcardDecl.fType == 3) {
            xSWildcardDecl2.fType = 3;
            xSWildcardDecl2.fNamespaceList = this.intersect2sets(this.fNamespaceList, xSWildcardDecl.fNamespaceList);
        } else if (this.fType == 2 && xSWildcardDecl.fType == 2) {
            if (this.fNamespaceList[0] != ABSENT && xSWildcardDecl.fNamespaceList[0] != ABSENT) {
                return null;
            }
            XSWildcardDecl xSWildcardDecl4 = this;
            if (this.fNamespaceList[0] == ABSENT) {
                xSWildcardDecl4 = xSWildcardDecl;
            }
            xSWildcardDecl2.fType = xSWildcardDecl4.fType;
            xSWildcardDecl2.fNamespaceList = xSWildcardDecl4.fNamespaceList;
        }
        return xSWildcardDecl2;
    }

    private boolean areSame(XSWildcardDecl xSWildcardDecl) {
        if (this.fType == xSWildcardDecl.fType) {
            if (this.fType == 1) {
                return true;
            }
            if (this.fType == 2) {
                return this.fNamespaceList[0] == xSWildcardDecl.fNamespaceList[0];
            }
            if (this.fNamespaceList.length == xSWildcardDecl.fNamespaceList.length) {
                int n2 = 0;
                while (n2 < this.fNamespaceList.length) {
                    if (!this.elementInSet(this.fNamespaceList[n2], xSWildcardDecl.fNamespaceList)) {
                        return false;
                    }
                    ++n2;
                }
                return true;
            }
        }
        return false;
    }

    String[] intersect2sets(String[] arrstring, String[] arrstring2) {
        String[] arrstring3 = new String[java.lang.Math.min(arrstring.length, arrstring2.length)];
        int n2 = 0;
        int n3 = 0;
        while (n3 < arrstring.length) {
            if (this.elementInSet(arrstring[n3], arrstring2)) {
                arrstring3[n2++] = arrstring[n3];
            }
            ++n3;
        }
        String[] arrstring4 = new String[n2];
        System.arraycopy(arrstring3, 0, arrstring4, 0, n2);
        return arrstring4;
    }

    String[] union2sets(String[] arrstring, String[] arrstring2) {
        String[] arrstring3 = new String[arrstring.length];
        int n2 = 0;
        int n3 = 0;
        while (n3 < arrstring.length) {
            if (!this.elementInSet(arrstring[n3], arrstring2)) {
                arrstring3[n2++] = arrstring[n3];
            }
            ++n3;
        }
        String[] arrstring4 = new String[n2 + arrstring2.length];
        System.arraycopy(arrstring3, 0, arrstring4, 0, n2);
        System.arraycopy(arrstring2, 0, arrstring4, n2, arrstring2.length);
        return arrstring4;
    }

    boolean subset2sets(String[] arrstring, String[] arrstring2) {
        int n2 = 0;
        while (n2 < arrstring.length) {
            if (!this.elementInSet(arrstring[n2], arrstring2)) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    boolean elementInSet(String string, String[] arrstring) {
        boolean bl = false;
        int n2 = 0;
        while (n2 < arrstring.length && !bl) {
            if (string == arrstring[n2]) {
                bl = true;
            }
            ++n2;
        }
        return bl;
    }

    public String toString() {
        if (this.fDescription == null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("WC[");
            switch (this.fType) {
                case 1: {
                    stringBuffer.append("##any");
                    break;
                }
                case 2: {
                    stringBuffer.append("##other");
                    stringBuffer.append(":\"");
                    if (this.fNamespaceList[0] != null) {
                        stringBuffer.append(this.fNamespaceList[0]);
                    }
                    stringBuffer.append("\"");
                    break;
                }
                case 3: {
                    if (this.fNamespaceList.length == 0) break;
                    stringBuffer.append("\"");
                    if (this.fNamespaceList[0] != null) {
                        stringBuffer.append(this.fNamespaceList[0]);
                    }
                    stringBuffer.append("\"");
                    int n2 = 1;
                    while (n2 < this.fNamespaceList.length) {
                        stringBuffer.append(",\"");
                        if (this.fNamespaceList[n2] != null) {
                            stringBuffer.append(this.fNamespaceList[n2]);
                        }
                        stringBuffer.append("\"");
                        ++n2;
                    }
                    break block0;
                }
            }
            stringBuffer.append("]");
            this.fDescription = stringBuffer.toString();
        }
        return this.fDescription;
    }

    public short getType() {
        return 9;
    }

    public String getName() {
        return null;
    }

    public String getNamespace() {
        return null;
    }

    public short getConstraintType() {
        return this.fType;
    }

    public StringList getNsConstraintList() {
        return new StringListImpl(this.fNamespaceList, this.fNamespaceList == null ? 0 : this.fNamespaceList.length);
    }

    public short getProcessContents() {
        return this.fProcessContents;
    }

    public String getProcessContentsAsString() {
        switch (this.fProcessContents) {
            case 2: {
                return "skip";
            }
            case 3: {
                return "lax";
            }
            case 1: {
                return "strict";
            }
        }
        return "invalid value";
    }

    public XSAnnotation getAnnotation() {
        return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
    }

    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    public XSNamespaceItem getNamespaceItem() {
        return null;
    }
}

