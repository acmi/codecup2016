/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSNotationDecl;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSNamedMap4Types;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNamespaceItemList;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;

public final class XSModelImpl
extends AbstractList
implements XSModel,
XSNamespaceItemList {
    private static final short MAX_COMP_IDX = 16;
    private static final boolean[] GLOBAL_COMP = new boolean[]{false, true, true, true, false, true, true, false, false, false, false, true, false, false, false, true, true};
    private final int fGrammarCount;
    private final String[] fNamespaces;
    private final SchemaGrammar[] fGrammarList;
    private final SymbolHash fGrammarMap;
    private final SymbolHash fSubGroupMap;
    private final XSNamedMap[] fGlobalComponents;
    private final XSNamedMap[][] fNSComponents;
    private final StringList fNamespacesList;
    private XSObjectList fAnnotations = null;
    private final boolean fHasIDC;

    public XSModelImpl(SchemaGrammar[] arrschemaGrammar) {
        this(arrschemaGrammar, 1);
    }

    public XSModelImpl(SchemaGrammar[] arrschemaGrammar, short s2) {
        SchemaGrammar schemaGrammar;
        Object object;
        int n2 = arrschemaGrammar.length;
        int n3 = Math.max(n2 + 1, 5);
        String[] arrstring = new String[n3];
        SchemaGrammar[] arrschemaGrammar2 = new SchemaGrammar[n3];
        boolean bl = false;
        int n4 = 0;
        while (n4 < n2) {
            schemaGrammar = arrschemaGrammar[n4];
            object = schemaGrammar.getTargetNamespace();
            arrstring[n4] = object;
            arrschemaGrammar2[n4] = schemaGrammar;
            if (object == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
                bl = true;
            }
            ++n4;
        }
        if (!bl) {
            arrstring[n2] = SchemaSymbols.URI_SCHEMAFORSCHEMA;
            arrschemaGrammar2[n2++] = SchemaGrammar.getS4SGrammar(s2);
        }
        int n5 = 0;
        while (n5 < n2) {
            schemaGrammar = arrschemaGrammar2[n5];
            Vector vector = schemaGrammar.getImportedGrammars();
            int n6 = vector == null ? -1 : vector.size() - 1;
            while (n6 >= 0) {
                object = (SchemaGrammar)vector.elementAt(n6);
                int n7 = 0;
                while (n7 < n2) {
                    if (object == arrschemaGrammar2[n7]) break;
                    ++n7;
                }
                if (n7 == n2) {
                    if (n2 == arrschemaGrammar2.length) {
                        String[] arrstring2 = new String[n2 * 2];
                        System.arraycopy(arrstring, 0, arrstring2, 0, n2);
                        arrstring = arrstring2;
                        SchemaGrammar[] arrschemaGrammar3 = new SchemaGrammar[n2 * 2];
                        System.arraycopy(arrschemaGrammar2, 0, arrschemaGrammar3, 0, n2);
                        arrschemaGrammar2 = arrschemaGrammar3;
                    }
                    arrstring[n2] = object.getTargetNamespace();
                    arrschemaGrammar2[n2] = object;
                    ++n2;
                }
                --n6;
            }
            ++n5;
        }
        this.fNamespaces = arrstring;
        this.fGrammarList = arrschemaGrammar2;
        boolean bl2 = false;
        this.fGrammarMap = new SymbolHash(n2 * 2);
        n5 = 0;
        while (n5 < n2) {
            this.fGrammarMap.put(XSModelImpl.null2EmptyString(this.fNamespaces[n5]), this.fGrammarList[n5]);
            if (this.fGrammarList[n5].hasIDConstraints()) {
                bl2 = true;
            }
            ++n5;
        }
        this.fHasIDC = bl2;
        this.fGrammarCount = n2;
        this.fGlobalComponents = new XSNamedMap[17];
        this.fNSComponents = new XSNamedMap[n2][17];
        this.fNamespacesList = new StringListImpl(this.fNamespaces, this.fGrammarCount);
        this.fSubGroupMap = this.buildSubGroups();
    }

    private SymbolHash buildSubGroups_Org() {
        SubstitutionGroupHandler substitutionGroupHandler = new SubstitutionGroupHandler(null);
        int n2 = 0;
        while (n2 < this.fGrammarCount) {
            substitutionGroupHandler.addSubstitutionGroup(this.fGrammarList[n2].getSubstitutionGroups());
            ++n2;
        }
        XSNamedMap xSNamedMap = this.getComponents(2);
        int n3 = xSNamedMap.getLength();
        SymbolHash symbolHash = new SymbolHash(n3 * 2);
        int n4 = 0;
        while (n4 < n3) {
            XSElementDecl xSElementDecl;
            XSObject[] arrxSObject = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl = (XSElementDecl)xSNamedMap.item(n4));
            symbolHash.put(xSElementDecl, arrxSObject.length > 0 ? new XSObjectListImpl(arrxSObject, arrxSObject.length) : XSObjectListImpl.EMPTY_LIST);
            ++n4;
        }
        return symbolHash;
    }

    private SymbolHash buildSubGroups() {
        SubstitutionGroupHandler substitutionGroupHandler = new SubstitutionGroupHandler(null);
        int n2 = 0;
        while (n2 < this.fGrammarCount) {
            substitutionGroupHandler.addSubstitutionGroup(this.fGrammarList[n2].getSubstitutionGroups());
            ++n2;
        }
        XSObjectListImpl xSObjectListImpl = this.getGlobalElements();
        int n3 = xSObjectListImpl.getLength();
        SymbolHash symbolHash = new SymbolHash(n3 * 2);
        int n4 = 0;
        while (n4 < n3) {
            XSElementDecl xSElementDecl;
            XSObject[] arrxSObject = substitutionGroupHandler.getSubstitutionGroup(xSElementDecl = (XSElementDecl)xSObjectListImpl.item(n4));
            symbolHash.put(xSElementDecl, arrxSObject.length > 0 ? new XSObjectListImpl(arrxSObject, arrxSObject.length) : XSObjectListImpl.EMPTY_LIST);
            ++n4;
        }
        return symbolHash;
    }

    private XSObjectListImpl getGlobalElements() {
        SymbolHash[] arrsymbolHash = new SymbolHash[this.fGrammarCount];
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.fGrammarCount) {
            arrsymbolHash[n3] = this.fGrammarList[n3].fAllGlobalElemDecls;
            n2 += arrsymbolHash[n3].getLength();
            ++n3;
        }
        if (n2 == 0) {
            return XSObjectListImpl.EMPTY_LIST;
        }
        Object[] arrobject = new XSObject[n2];
        int n4 = 0;
        int n5 = 0;
        while (n5 < this.fGrammarCount) {
            arrsymbolHash[n5].getValues(arrobject, n4);
            n4 += arrsymbolHash[n5].getLength();
            ++n5;
        }
        return new XSObjectListImpl((XSObject[])arrobject, n2);
    }

    public StringList getNamespaces() {
        return this.fNamespacesList;
    }

    public XSNamespaceItemList getNamespaceItems() {
        return this;
    }

    public synchronized XSNamedMap getComponents(short s2) {
        if (s2 <= 0 || s2 > 16 || !GLOBAL_COMP[s2]) {
            return XSNamedMapImpl.EMPTY_MAP;
        }
        SymbolHash[] arrsymbolHash = new SymbolHash[this.fGrammarCount];
        if (this.fGlobalComponents[s2] == null) {
            int n2 = 0;
            while (n2 < this.fGrammarCount) {
                switch (s2) {
                    case 3: 
                    case 15: 
                    case 16: {
                        arrsymbolHash[n2] = this.fGrammarList[n2].fGlobalTypeDecls;
                        break;
                    }
                    case 1: {
                        arrsymbolHash[n2] = this.fGrammarList[n2].fGlobalAttrDecls;
                        break;
                    }
                    case 2: {
                        arrsymbolHash[n2] = this.fGrammarList[n2].fGlobalElemDecls;
                        break;
                    }
                    case 5: {
                        arrsymbolHash[n2] = this.fGrammarList[n2].fGlobalAttrGrpDecls;
                        break;
                    }
                    case 6: {
                        arrsymbolHash[n2] = this.fGrammarList[n2].fGlobalGroupDecls;
                        break;
                    }
                    case 11: {
                        arrsymbolHash[n2] = this.fGrammarList[n2].fGlobalNotationDecls;
                    }
                }
                ++n2;
            }
            this.fGlobalComponents[s2] = s2 == 15 || s2 == 16 ? new XSNamedMap4Types(this.fNamespaces, arrsymbolHash, this.fGrammarCount, s2) : new XSNamedMapImpl(this.fNamespaces, arrsymbolHash, this.fGrammarCount);
        }
        return this.fGlobalComponents[s2];
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public synchronized XSNamedMap getComponentsByNamespace(short var1_1, String var2_2) {
        block14 : {
            if (var1_1 <= 0) return XSNamedMapImpl.EMPTY_MAP;
            if (var1_1 > 16) return XSNamedMapImpl.EMPTY_MAP;
            if (!XSModelImpl.GLOBAL_COMP[var1_1]) {
                return XSNamedMapImpl.EMPTY_MAP;
            }
            var3_3 = 0;
            if (var2_2 == null) ** GOTO lbl15
            while (var3_3 < this.fGrammarCount) {
                if (!var2_2.equals(this.fNamespaces[var3_3])) {
                    ++var3_3;
                    continue;
                }
                break block14;
            }
            ** GOTO lbl16
            while (this.fNamespaces[var3_3] != null) {
                ++var3_3;
lbl15: // 2 sources:
                if (var3_3 < this.fGrammarCount) continue;
            }
        }
        if (var3_3 == this.fGrammarCount) {
            return XSNamedMapImpl.EMPTY_MAP;
        }
        if (this.fNSComponents[var3_3][var1_1] != null) return this.fNSComponents[var3_3][var1_1];
        var4_4 = null;
        switch (var1_1) {
            case 3: 
            case 15: 
            case 16: {
                var4_4 = this.fGrammarList[var3_3].fGlobalTypeDecls;
                break;
            }
            case 1: {
                var4_4 = this.fGrammarList[var3_3].fGlobalAttrDecls;
                break;
            }
            case 2: {
                var4_4 = this.fGrammarList[var3_3].fGlobalElemDecls;
                break;
            }
            case 5: {
                var4_4 = this.fGrammarList[var3_3].fGlobalAttrGrpDecls;
                break;
            }
            case 6: {
                var4_4 = this.fGrammarList[var3_3].fGlobalGroupDecls;
                break;
            }
            case 11: {
                var4_4 = this.fGrammarList[var3_3].fGlobalNotationDecls;
            }
        }
        if (var1_1 != 15 && var1_1 != 16) {
            this.fNSComponents[var3_3][var1_1] = new XSNamedMapImpl(var2_2, var4_4);
            return this.fNSComponents[var3_3][var1_1];
        }
        this.fNSComponents[var3_3][var1_1] = new XSNamedMap4Types(var2_2, var4_4, var1_1);
        return this.fNSComponents[var3_3][var1_1];
    }

    public XSTypeDefinition getTypeDefinition(String string, String string2) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return (XSTypeDefinition)schemaGrammar.fGlobalTypeDecls.get(string);
    }

    public XSTypeDefinition getTypeDefinition(String string, String string2, String string3) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return schemaGrammar.getGlobalTypeDecl(string, string3);
    }

    public XSAttributeDeclaration getAttributeDeclaration(String string, String string2) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return (XSAttributeDeclaration)schemaGrammar.fGlobalAttrDecls.get(string);
    }

    public XSAttributeDeclaration getAttributeDeclaration(String string, String string2, String string3) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return schemaGrammar.getGlobalAttributeDecl(string, string3);
    }

    public XSElementDeclaration getElementDeclaration(String string, String string2) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return (XSElementDeclaration)schemaGrammar.fGlobalElemDecls.get(string);
    }

    public XSElementDeclaration getElementDeclaration(String string, String string2, String string3) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return schemaGrammar.getGlobalElementDecl(string, string3);
    }

    public XSAttributeGroupDefinition getAttributeGroup(String string, String string2) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return (XSAttributeGroupDefinition)schemaGrammar.fGlobalAttrGrpDecls.get(string);
    }

    public XSAttributeGroupDefinition getAttributeGroup(String string, String string2, String string3) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return schemaGrammar.getGlobalAttributeGroupDecl(string, string3);
    }

    public XSModelGroupDefinition getModelGroupDefinition(String string, String string2) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return (XSModelGroupDefinition)schemaGrammar.fGlobalGroupDecls.get(string);
    }

    public XSModelGroupDefinition getModelGroupDefinition(String string, String string2, String string3) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return schemaGrammar.getGlobalGroupDecl(string, string3);
    }

    public XSNotationDeclaration getNotationDeclaration(String string, String string2) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return (XSNotationDeclaration)schemaGrammar.fGlobalNotationDecls.get(string);
    }

    public XSNotationDeclaration getNotationDeclaration(String string, String string2, String string3) {
        SchemaGrammar schemaGrammar = (SchemaGrammar)this.fGrammarMap.get(XSModelImpl.null2EmptyString(string2));
        if (schemaGrammar == null) {
            return null;
        }
        return schemaGrammar.getGlobalNotationDecl(string, string3);
    }

    public synchronized XSObjectList getAnnotations() {
        if (this.fAnnotations != null) {
            return this.fAnnotations;
        }
        int n2 = 0;
        int n3 = 0;
        while (n3 < this.fGrammarCount) {
            n2 += this.fGrammarList[n3].fNumAnnotations;
            ++n3;
        }
        if (n2 == 0) {
            this.fAnnotations = XSObjectListImpl.EMPTY_LIST;
            return this.fAnnotations;
        }
        XSObject[] arrxSObject = new XSAnnotationImpl[n2];
        int n4 = 0;
        int n5 = 0;
        while (n5 < this.fGrammarCount) {
            SchemaGrammar schemaGrammar = this.fGrammarList[n5];
            if (schemaGrammar.fNumAnnotations > 0) {
                System.arraycopy(schemaGrammar.fAnnotations, 0, arrxSObject, n4, schemaGrammar.fNumAnnotations);
                n4 += schemaGrammar.fNumAnnotations;
            }
            ++n5;
        }
        this.fAnnotations = new XSObjectListImpl(arrxSObject, arrxSObject.length);
        return this.fAnnotations;
    }

    private static final String null2EmptyString(String string) {
        return string == null ? XMLSymbols.EMPTY_STRING : string;
    }

    public boolean hasIDConstraints() {
        return this.fHasIDC;
    }

    public XSObjectList getSubstitutionGroup(XSElementDeclaration xSElementDeclaration) {
        return (XSObjectList)this.fSubGroupMap.get(xSElementDeclaration);
    }

    public int getLength() {
        return this.fGrammarCount;
    }

    public XSNamespaceItem item(int n2) {
        if (n2 < 0 || n2 >= this.fGrammarCount) {
            return null;
        }
        return this.fGrammarList[n2];
    }

    public Object get(int n2) {
        if (n2 >= 0 && n2 < this.fGrammarCount) {
            return this.fGrammarList[n2];
        }
        throw new IndexOutOfBoundsException("Index: " + n2);
    }

    public int size() {
        return this.getLength();
    }

    public Iterator iterator() {
        return this.listIterator0(0);
    }

    public ListIterator listIterator() {
        return this.listIterator0(0);
    }

    public ListIterator listIterator(int n2) {
        if (n2 >= 0 && n2 < this.fGrammarCount) {
            return this.listIterator0(n2);
        }
        throw new IndexOutOfBoundsException("Index: " + n2);
    }

    private ListIterator listIterator0(int n2) {
        return new XSNamespaceItemListIterator(this, n2);
    }

    public Object[] toArray() {
        Object[] arrobject = new Object[this.fGrammarCount];
        this.toArray0(arrobject);
        return arrobject;
    }

    public Object[] toArray(Object[] arrobject) {
        if (arrobject.length < this.fGrammarCount) {
            Class class_ = arrobject.getClass();
            Class class_2 = class_.getComponentType();
            arrobject = (Object[])Array.newInstance(class_2, this.fGrammarCount);
        }
        this.toArray0(arrobject);
        if (arrobject.length > this.fGrammarCount) {
            arrobject[this.fGrammarCount] = null;
        }
        return arrobject;
    }

    private void toArray0(Object[] arrobject) {
        if (this.fGrammarCount > 0) {
            System.arraycopy(this.fGrammarList, 0, arrobject, 0, this.fGrammarCount);
        }
    }

    static int access$000(XSModelImpl xSModelImpl) {
        return xSModelImpl.fGrammarCount;
    }

    static SchemaGrammar[] access$100(XSModelImpl xSModelImpl) {
        return xSModelImpl.fGrammarList;
    }

    private final class XSNamespaceItemListIterator
    implements ListIterator {
        private int index;
        private final XSModelImpl this$0;

        public XSNamespaceItemListIterator(XSModelImpl xSModelImpl, int n2) {
            this.this$0 = xSModelImpl;
            this.index = n2;
        }

        public boolean hasNext() {
            return this.index < XSModelImpl.access$000(this.this$0);
        }

        public Object next() {
            if (this.index < XSModelImpl.access$000(this.this$0)) {
                return XSModelImpl.access$100(this.this$0)[this.index++];
            }
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return this.index > 0;
        }

        public Object previous() {
            if (this.index > 0) {
                return XSModelImpl.access$100(this.this$0)[--this.index];
            }
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return this.index;
        }

        public int previousIndex() {
            return this.index - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(Object object) {
            throw new UnsupportedOperationException();
        }

        public void add(Object object) {
            throw new UnsupportedOperationException();
        }
    }

}

