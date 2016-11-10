/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import java.lang.ref.SoftReference;
import java.util.Vector;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSModelImpl;
import org.apache.xerces.impl.xs.XSNotationDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.util.ObjectListImpl;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSNamedMap4Types;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XML11Configuration;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSWildcard;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.xml.sax.SAXException;

public class SchemaGrammar
implements XSGrammar,
XSNamespaceItem {
    String fTargetNamespace;
    SymbolHash fGlobalAttrDecls;
    SymbolHash fGlobalAttrGrpDecls;
    SymbolHash fGlobalElemDecls;
    SymbolHash fGlobalGroupDecls;
    SymbolHash fGlobalNotationDecls;
    SymbolHash fGlobalIDConstraintDecls;
    SymbolHash fGlobalTypeDecls;
    SymbolHash fGlobalAttrDeclsExt;
    SymbolHash fGlobalAttrGrpDeclsExt;
    SymbolHash fGlobalElemDeclsExt;
    SymbolHash fGlobalGroupDeclsExt;
    SymbolHash fGlobalNotationDeclsExt;
    SymbolHash fGlobalIDConstraintDeclsExt;
    SymbolHash fGlobalTypeDeclsExt;
    SymbolHash fAllGlobalElemDecls;
    XSDDescription fGrammarDescription = null;
    XSAnnotationImpl[] fAnnotations = null;
    int fNumAnnotations;
    private SymbolTable fSymbolTable = null;
    private SoftReference fSAXParser = null;
    private SoftReference fDOMParser = null;
    private boolean fIsImmutable = false;
    private static final int BASICSET_COUNT = 29;
    private static final int FULLSET_COUNT = 46;
    private static final int GRAMMAR_XS = 1;
    private static final int GRAMMAR_XSI = 2;
    Vector fImported = null;
    private static final int INITIAL_SIZE = 16;
    private static final int INC_SIZE = 16;
    private int fCTCount = 0;
    private XSComplexTypeDecl[] fComplexTypeDecls = new XSComplexTypeDecl[16];
    private SimpleLocator[] fCTLocators = new SimpleLocator[16];
    private static final int REDEFINED_GROUP_INIT_SIZE = 2;
    private int fRGCount = 0;
    private XSGroupDecl[] fRedefinedGroupDecls = new XSGroupDecl[2];
    private SimpleLocator[] fRGLocators = new SimpleLocator[1];
    boolean fFullChecked = false;
    private int fSubGroupCount = 0;
    private XSElementDecl[] fSubGroups = new XSElementDecl[16];
    public static final XSComplexTypeDecl fAnyType = new XSAnyType();
    public static final BuiltinSchemaGrammar SG_SchemaNS = new BuiltinSchemaGrammar(1, 1);
    private static final BuiltinSchemaGrammar SG_SchemaNSExtended = new BuiltinSchemaGrammar(1, 2);
    public static final XSSimpleType fAnySimpleType = (XSSimpleType)SG_SchemaNS.getGlobalTypeDecl("anySimpleType");
    public static final BuiltinSchemaGrammar SG_XSI = new BuiltinSchemaGrammar(2, 1);
    private static final short MAX_COMP_IDX = 16;
    private static final boolean[] GLOBAL_COMP = new boolean[]{false, true, true, true, false, true, true, false, false, false, false, true, false, false, false, true, true};
    private XSNamedMap[] fComponents = null;
    private ObjectList[] fComponentsExt = null;
    private Vector fDocuments = null;
    private Vector fLocations = null;

    protected SchemaGrammar() {
    }

    public SchemaGrammar(String string, XSDDescription xSDDescription, SymbolTable symbolTable) {
        this.fTargetNamespace = string;
        this.fGrammarDescription = xSDDescription;
        this.fSymbolTable = symbolTable;
        this.fGlobalAttrDecls = new SymbolHash();
        this.fGlobalAttrGrpDecls = new SymbolHash();
        this.fGlobalElemDecls = new SymbolHash();
        this.fGlobalGroupDecls = new SymbolHash();
        this.fGlobalNotationDecls = new SymbolHash();
        this.fGlobalIDConstraintDecls = new SymbolHash();
        this.fGlobalAttrDeclsExt = new SymbolHash();
        this.fGlobalAttrGrpDeclsExt = new SymbolHash();
        this.fGlobalElemDeclsExt = new SymbolHash();
        this.fGlobalGroupDeclsExt = new SymbolHash();
        this.fGlobalNotationDeclsExt = new SymbolHash();
        this.fGlobalIDConstraintDeclsExt = new SymbolHash();
        this.fGlobalTypeDeclsExt = new SymbolHash();
        this.fAllGlobalElemDecls = new SymbolHash();
        this.fGlobalTypeDecls = this.fTargetNamespace == SchemaSymbols.URI_SCHEMAFORSCHEMA ? SchemaGrammar.SG_SchemaNS.fGlobalTypeDecls.makeClone() : new SymbolHash();
    }

    public SchemaGrammar(SchemaGrammar schemaGrammar) {
        int n2;
        this.fTargetNamespace = schemaGrammar.fTargetNamespace;
        this.fGrammarDescription = schemaGrammar.fGrammarDescription.makeClone();
        this.fSymbolTable = schemaGrammar.fSymbolTable;
        this.fGlobalAttrDecls = schemaGrammar.fGlobalAttrDecls.makeClone();
        this.fGlobalAttrGrpDecls = schemaGrammar.fGlobalAttrGrpDecls.makeClone();
        this.fGlobalElemDecls = schemaGrammar.fGlobalElemDecls.makeClone();
        this.fGlobalGroupDecls = schemaGrammar.fGlobalGroupDecls.makeClone();
        this.fGlobalNotationDecls = schemaGrammar.fGlobalNotationDecls.makeClone();
        this.fGlobalIDConstraintDecls = schemaGrammar.fGlobalIDConstraintDecls.makeClone();
        this.fGlobalTypeDecls = schemaGrammar.fGlobalTypeDecls.makeClone();
        this.fGlobalAttrDeclsExt = schemaGrammar.fGlobalAttrDeclsExt.makeClone();
        this.fGlobalAttrGrpDeclsExt = schemaGrammar.fGlobalAttrGrpDeclsExt.makeClone();
        this.fGlobalElemDeclsExt = schemaGrammar.fGlobalElemDeclsExt.makeClone();
        this.fGlobalGroupDeclsExt = schemaGrammar.fGlobalGroupDeclsExt.makeClone();
        this.fGlobalNotationDeclsExt = schemaGrammar.fGlobalNotationDeclsExt.makeClone();
        this.fGlobalIDConstraintDeclsExt = schemaGrammar.fGlobalIDConstraintDeclsExt.makeClone();
        this.fGlobalTypeDeclsExt = schemaGrammar.fGlobalTypeDeclsExt.makeClone();
        this.fAllGlobalElemDecls = schemaGrammar.fAllGlobalElemDecls.makeClone();
        this.fNumAnnotations = schemaGrammar.fNumAnnotations;
        if (this.fNumAnnotations > 0) {
            this.fAnnotations = new XSAnnotationImpl[schemaGrammar.fAnnotations.length];
            System.arraycopy(schemaGrammar.fAnnotations, 0, this.fAnnotations, 0, this.fNumAnnotations);
        }
        this.fSubGroupCount = schemaGrammar.fSubGroupCount;
        if (this.fSubGroupCount > 0) {
            this.fSubGroups = new XSElementDecl[schemaGrammar.fSubGroups.length];
            System.arraycopy(schemaGrammar.fSubGroups, 0, this.fSubGroups, 0, this.fSubGroupCount);
        }
        this.fCTCount = schemaGrammar.fCTCount;
        if (this.fCTCount > 0) {
            this.fComplexTypeDecls = new XSComplexTypeDecl[schemaGrammar.fComplexTypeDecls.length];
            this.fCTLocators = new SimpleLocator[schemaGrammar.fCTLocators.length];
            System.arraycopy(schemaGrammar.fComplexTypeDecls, 0, this.fComplexTypeDecls, 0, this.fCTCount);
            System.arraycopy(schemaGrammar.fCTLocators, 0, this.fCTLocators, 0, this.fCTCount);
        }
        this.fRGCount = schemaGrammar.fRGCount;
        if (this.fRGCount > 0) {
            this.fRedefinedGroupDecls = new XSGroupDecl[schemaGrammar.fRedefinedGroupDecls.length];
            this.fRGLocators = new SimpleLocator[schemaGrammar.fRGLocators.length];
            System.arraycopy(schemaGrammar.fRedefinedGroupDecls, 0, this.fRedefinedGroupDecls, 0, this.fRGCount);
            System.arraycopy(schemaGrammar.fRGLocators, 0, this.fRGLocators, 0, this.fRGCount);
        }
        if (schemaGrammar.fImported != null) {
            this.fImported = new Vector();
            n2 = 0;
            while (n2 < schemaGrammar.fImported.size()) {
                this.fImported.add(schemaGrammar.fImported.elementAt(n2));
                ++n2;
            }
        }
        if (schemaGrammar.fLocations != null) {
            n2 = 0;
            while (n2 < schemaGrammar.fLocations.size()) {
                this.addDocument(null, (String)schemaGrammar.fLocations.elementAt(n2));
                ++n2;
            }
        }
    }

    public XMLGrammarDescription getGrammarDescription() {
        return this.fGrammarDescription;
    }

    public boolean isNamespaceAware() {
        return true;
    }

    public void setImportedGrammars(Vector vector) {
        this.fImported = vector;
    }

    public Vector getImportedGrammars() {
        return this.fImported;
    }

    public final String getTargetNamespace() {
        return this.fTargetNamespace;
    }

    public void addGlobalAttributeDecl(XSAttributeDecl xSAttributeDecl) {
        this.fGlobalAttrDecls.put(xSAttributeDecl.fName, xSAttributeDecl);
        xSAttributeDecl.setNamespaceItem(this);
    }

    public void addGlobalAttributeDecl(XSAttributeDecl xSAttributeDecl, String string) {
        this.fGlobalAttrDeclsExt.put((string != null ? string : "") + "," + xSAttributeDecl.fName, xSAttributeDecl);
        if (xSAttributeDecl.getNamespaceItem() == null) {
            xSAttributeDecl.setNamespaceItem(this);
        }
    }

    public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl xSAttributeGroupDecl) {
        this.fGlobalAttrGrpDecls.put(xSAttributeGroupDecl.fName, xSAttributeGroupDecl);
        xSAttributeGroupDecl.setNamespaceItem(this);
    }

    public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl xSAttributeGroupDecl, String string) {
        this.fGlobalAttrGrpDeclsExt.put((string != null ? string : "") + "," + xSAttributeGroupDecl.fName, xSAttributeGroupDecl);
        if (xSAttributeGroupDecl.getNamespaceItem() == null) {
            xSAttributeGroupDecl.setNamespaceItem(this);
        }
    }

    public void addGlobalElementDeclAll(XSElementDecl xSElementDecl) {
        if (this.fAllGlobalElemDecls.get(xSElementDecl) == null) {
            this.fAllGlobalElemDecls.put(xSElementDecl, xSElementDecl);
            if (xSElementDecl.fSubGroup != null) {
                if (this.fSubGroupCount == this.fSubGroups.length) {
                    this.fSubGroups = SchemaGrammar.resize(this.fSubGroups, this.fSubGroupCount + 16);
                }
                this.fSubGroups[this.fSubGroupCount++] = xSElementDecl;
            }
        }
    }

    public void addGlobalElementDecl(XSElementDecl xSElementDecl) {
        this.fGlobalElemDecls.put(xSElementDecl.fName, xSElementDecl);
        xSElementDecl.setNamespaceItem(this);
    }

    public void addGlobalElementDecl(XSElementDecl xSElementDecl, String string) {
        this.fGlobalElemDeclsExt.put((string != null ? string : "") + "," + xSElementDecl.fName, xSElementDecl);
        if (xSElementDecl.getNamespaceItem() == null) {
            xSElementDecl.setNamespaceItem(this);
        }
    }

    public void addGlobalGroupDecl(XSGroupDecl xSGroupDecl) {
        this.fGlobalGroupDecls.put(xSGroupDecl.fName, xSGroupDecl);
        xSGroupDecl.setNamespaceItem(this);
    }

    public void addGlobalGroupDecl(XSGroupDecl xSGroupDecl, String string) {
        this.fGlobalGroupDeclsExt.put((string != null ? string : "") + "," + xSGroupDecl.fName, xSGroupDecl);
        if (xSGroupDecl.getNamespaceItem() == null) {
            xSGroupDecl.setNamespaceItem(this);
        }
    }

    public void addGlobalNotationDecl(XSNotationDecl xSNotationDecl) {
        this.fGlobalNotationDecls.put(xSNotationDecl.fName, xSNotationDecl);
        xSNotationDecl.setNamespaceItem(this);
    }

    public void addGlobalNotationDecl(XSNotationDecl xSNotationDecl, String string) {
        this.fGlobalNotationDeclsExt.put((string != null ? string : "") + "," + xSNotationDecl.fName, xSNotationDecl);
        if (xSNotationDecl.getNamespaceItem() == null) {
            xSNotationDecl.setNamespaceItem(this);
        }
    }

    public void addGlobalTypeDecl(XSTypeDefinition xSTypeDefinition) {
        this.fGlobalTypeDecls.put(xSTypeDefinition.getName(), xSTypeDefinition);
        if (xSTypeDefinition instanceof XSComplexTypeDecl) {
            ((XSComplexTypeDecl)xSTypeDefinition).setNamespaceItem(this);
        } else if (xSTypeDefinition instanceof XSSimpleTypeDecl) {
            ((XSSimpleTypeDecl)xSTypeDefinition).setNamespaceItem(this);
        }
    }

    public void addGlobalTypeDecl(XSTypeDefinition xSTypeDefinition, String string) {
        this.fGlobalTypeDeclsExt.put((string != null ? string : "") + "," + xSTypeDefinition.getName(), xSTypeDefinition);
        if (xSTypeDefinition.getNamespaceItem() == null) {
            if (xSTypeDefinition instanceof XSComplexTypeDecl) {
                ((XSComplexTypeDecl)xSTypeDefinition).setNamespaceItem(this);
            } else if (xSTypeDefinition instanceof XSSimpleTypeDecl) {
                ((XSSimpleTypeDecl)xSTypeDefinition).setNamespaceItem(this);
            }
        }
    }

    public void addGlobalComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl) {
        this.fGlobalTypeDecls.put(xSComplexTypeDecl.getName(), xSComplexTypeDecl);
        xSComplexTypeDecl.setNamespaceItem(this);
    }

    public void addGlobalComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl, String string) {
        this.fGlobalTypeDeclsExt.put((string != null ? string : "") + "," + xSComplexTypeDecl.getName(), xSComplexTypeDecl);
        if (xSComplexTypeDecl.getNamespaceItem() == null) {
            xSComplexTypeDecl.setNamespaceItem(this);
        }
    }

    public void addGlobalSimpleTypeDecl(XSSimpleType xSSimpleType) {
        this.fGlobalTypeDecls.put(xSSimpleType.getName(), xSSimpleType);
        if (xSSimpleType instanceof XSSimpleTypeDecl) {
            ((XSSimpleTypeDecl)xSSimpleType).setNamespaceItem(this);
        }
    }

    public void addGlobalSimpleTypeDecl(XSSimpleType xSSimpleType, String string) {
        this.fGlobalTypeDeclsExt.put((string != null ? string : "") + "," + xSSimpleType.getName(), xSSimpleType);
        if (xSSimpleType.getNamespaceItem() == null && xSSimpleType instanceof XSSimpleTypeDecl) {
            ((XSSimpleTypeDecl)xSSimpleType).setNamespaceItem(this);
        }
    }

    public final void addIDConstraintDecl(XSElementDecl xSElementDecl, IdentityConstraint identityConstraint) {
        xSElementDecl.addIDConstraint(identityConstraint);
        this.fGlobalIDConstraintDecls.put(identityConstraint.getIdentityConstraintName(), identityConstraint);
    }

    public final void addIDConstraintDecl(XSElementDecl xSElementDecl, IdentityConstraint identityConstraint, String string) {
        this.fGlobalIDConstraintDeclsExt.put((string != null ? string : "") + "," + identityConstraint.getIdentityConstraintName(), identityConstraint);
    }

    public final XSAttributeDecl getGlobalAttributeDecl(String string) {
        return (XSAttributeDecl)this.fGlobalAttrDecls.get(string);
    }

    public final XSAttributeDecl getGlobalAttributeDecl(String string, String string2) {
        return (XSAttributeDecl)this.fGlobalAttrDeclsExt.get((string2 != null ? string2 : "") + "," + string);
    }

    public final XSAttributeGroupDecl getGlobalAttributeGroupDecl(String string) {
        return (XSAttributeGroupDecl)this.fGlobalAttrGrpDecls.get(string);
    }

    public final XSAttributeGroupDecl getGlobalAttributeGroupDecl(String string, String string2) {
        return (XSAttributeGroupDecl)this.fGlobalAttrGrpDeclsExt.get((string2 != null ? string2 : "") + "," + string);
    }

    public final XSElementDecl getGlobalElementDecl(String string) {
        return (XSElementDecl)this.fGlobalElemDecls.get(string);
    }

    public final XSElementDecl getGlobalElementDecl(String string, String string2) {
        return (XSElementDecl)this.fGlobalElemDeclsExt.get((string2 != null ? string2 : "") + "," + string);
    }

    public final XSGroupDecl getGlobalGroupDecl(String string) {
        return (XSGroupDecl)this.fGlobalGroupDecls.get(string);
    }

    public final XSGroupDecl getGlobalGroupDecl(String string, String string2) {
        return (XSGroupDecl)this.fGlobalGroupDeclsExt.get((string2 != null ? string2 : "") + "," + string);
    }

    public final XSNotationDecl getGlobalNotationDecl(String string) {
        return (XSNotationDecl)this.fGlobalNotationDecls.get(string);
    }

    public final XSNotationDecl getGlobalNotationDecl(String string, String string2) {
        return (XSNotationDecl)this.fGlobalNotationDeclsExt.get((string2 != null ? string2 : "") + "," + string);
    }

    public final XSTypeDefinition getGlobalTypeDecl(String string) {
        return (XSTypeDefinition)this.fGlobalTypeDecls.get(string);
    }

    public final XSTypeDefinition getGlobalTypeDecl(String string, String string2) {
        return (XSTypeDefinition)this.fGlobalTypeDeclsExt.get((string2 != null ? string2 : "") + "," + string);
    }

    public final IdentityConstraint getIDConstraintDecl(String string) {
        return (IdentityConstraint)this.fGlobalIDConstraintDecls.get(string);
    }

    public final IdentityConstraint getIDConstraintDecl(String string, String string2) {
        return (IdentityConstraint)this.fGlobalIDConstraintDeclsExt.get((string2 != null ? string2 : "") + "," + string);
    }

    public final boolean hasIDConstraints() {
        return this.fGlobalIDConstraintDecls.getLength() > 0;
    }

    public void addComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl, SimpleLocator simpleLocator) {
        if (this.fCTCount == this.fComplexTypeDecls.length) {
            this.fComplexTypeDecls = SchemaGrammar.resize(this.fComplexTypeDecls, this.fCTCount + 16);
            this.fCTLocators = SchemaGrammar.resize(this.fCTLocators, this.fCTCount + 16);
        }
        this.fCTLocators[this.fCTCount] = simpleLocator;
        this.fComplexTypeDecls[this.fCTCount++] = xSComplexTypeDecl;
    }

    public void addRedefinedGroupDecl(XSGroupDecl xSGroupDecl, XSGroupDecl xSGroupDecl2, SimpleLocator simpleLocator) {
        if (this.fRGCount == this.fRedefinedGroupDecls.length) {
            this.fRedefinedGroupDecls = SchemaGrammar.resize(this.fRedefinedGroupDecls, this.fRGCount << 1);
            this.fRGLocators = SchemaGrammar.resize(this.fRGLocators, this.fRGCount);
        }
        this.fRGLocators[this.fRGCount / 2] = simpleLocator;
        this.fRedefinedGroupDecls[this.fRGCount++] = xSGroupDecl;
        this.fRedefinedGroupDecls[this.fRGCount++] = xSGroupDecl2;
    }

    final XSComplexTypeDecl[] getUncheckedComplexTypeDecls() {
        if (this.fCTCount < this.fComplexTypeDecls.length) {
            this.fComplexTypeDecls = SchemaGrammar.resize(this.fComplexTypeDecls, this.fCTCount);
            this.fCTLocators = SchemaGrammar.resize(this.fCTLocators, this.fCTCount);
        }
        return this.fComplexTypeDecls;
    }

    final SimpleLocator[] getUncheckedCTLocators() {
        if (this.fCTCount < this.fCTLocators.length) {
            this.fComplexTypeDecls = SchemaGrammar.resize(this.fComplexTypeDecls, this.fCTCount);
            this.fCTLocators = SchemaGrammar.resize(this.fCTLocators, this.fCTCount);
        }
        return this.fCTLocators;
    }

    final XSGroupDecl[] getRedefinedGroupDecls() {
        if (this.fRGCount < this.fRedefinedGroupDecls.length) {
            this.fRedefinedGroupDecls = SchemaGrammar.resize(this.fRedefinedGroupDecls, this.fRGCount);
            this.fRGLocators = SchemaGrammar.resize(this.fRGLocators, this.fRGCount / 2);
        }
        return this.fRedefinedGroupDecls;
    }

    final SimpleLocator[] getRGLocators() {
        if (this.fRGCount < this.fRedefinedGroupDecls.length) {
            this.fRedefinedGroupDecls = SchemaGrammar.resize(this.fRedefinedGroupDecls, this.fRGCount);
            this.fRGLocators = SchemaGrammar.resize(this.fRGLocators, this.fRGCount / 2);
        }
        return this.fRGLocators;
    }

    final void setUncheckedTypeNum(int n2) {
        this.fCTCount = n2;
        this.fComplexTypeDecls = SchemaGrammar.resize(this.fComplexTypeDecls, this.fCTCount);
        this.fCTLocators = SchemaGrammar.resize(this.fCTLocators, this.fCTCount);
    }

    final XSElementDecl[] getSubstitutionGroups() {
        if (this.fSubGroupCount < this.fSubGroups.length) {
            this.fSubGroups = SchemaGrammar.resize(this.fSubGroups, this.fSubGroupCount);
        }
        return this.fSubGroups;
    }

    public static SchemaGrammar getS4SGrammar(short s2) {
        if (s2 == 1) {
            return SG_SchemaNS;
        }
        return SG_SchemaNSExtended;
    }

    static final XSComplexTypeDecl[] resize(XSComplexTypeDecl[] arrxSComplexTypeDecl, int n2) {
        XSComplexTypeDecl[] arrxSComplexTypeDecl2 = new XSComplexTypeDecl[n2];
        System.arraycopy(arrxSComplexTypeDecl, 0, arrxSComplexTypeDecl2, 0, Math.min(arrxSComplexTypeDecl.length, n2));
        return arrxSComplexTypeDecl2;
    }

    static final XSGroupDecl[] resize(XSGroupDecl[] arrxSGroupDecl, int n2) {
        XSGroupDecl[] arrxSGroupDecl2 = new XSGroupDecl[n2];
        System.arraycopy(arrxSGroupDecl, 0, arrxSGroupDecl2, 0, Math.min(arrxSGroupDecl.length, n2));
        return arrxSGroupDecl2;
    }

    static final XSElementDecl[] resize(XSElementDecl[] arrxSElementDecl, int n2) {
        XSElementDecl[] arrxSElementDecl2 = new XSElementDecl[n2];
        System.arraycopy(arrxSElementDecl, 0, arrxSElementDecl2, 0, Math.min(arrxSElementDecl.length, n2));
        return arrxSElementDecl2;
    }

    static final SimpleLocator[] resize(SimpleLocator[] arrsimpleLocator, int n2) {
        SimpleLocator[] arrsimpleLocator2 = new SimpleLocator[n2];
        System.arraycopy(arrsimpleLocator, 0, arrsimpleLocator2, 0, Math.min(arrsimpleLocator.length, n2));
        return arrsimpleLocator2;
    }

    public synchronized void addDocument(Object object, String string) {
        if (this.fDocuments == null) {
            this.fDocuments = new Vector();
            this.fLocations = new Vector();
        }
        this.fDocuments.addElement(object);
        this.fLocations.addElement(string);
    }

    public synchronized void removeDocument(int n2) {
        if (this.fDocuments != null && n2 >= 0 && n2 < this.fDocuments.size()) {
            this.fDocuments.removeElementAt(n2);
            this.fLocations.removeElementAt(n2);
        }
    }

    public String getSchemaNamespace() {
        return this.fTargetNamespace;
    }

    synchronized DOMParser getDOMParser() {
        Object object;
        if (this.fDOMParser != null && (object = (DOMParser)this.fDOMParser.get()) != null) {
            return object;
        }
        object = new XML11Configuration(this.fSymbolTable);
        object.setFeature("http://xml.org/sax/features/namespaces", true);
        object.setFeature("http://xml.org/sax/features/validation", false);
        DOMParser dOMParser = new DOMParser((XMLParserConfiguration)object);
        try {
            dOMParser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        }
        catch (SAXException sAXException) {
            // empty catch block
        }
        this.fDOMParser = new SoftReference<DOMParser>(dOMParser);
        return dOMParser;
    }

    synchronized SAXParser getSAXParser() {
        Object object;
        if (this.fSAXParser != null && (object = (SAXParser)this.fSAXParser.get()) != null) {
            return object;
        }
        object = new XML11Configuration(this.fSymbolTable);
        object.setFeature("http://xml.org/sax/features/namespaces", true);
        object.setFeature("http://xml.org/sax/features/validation", false);
        SAXParser sAXParser = new SAXParser((XMLParserConfiguration)object);
        this.fSAXParser = new SoftReference<SAXParser>(sAXParser);
        return sAXParser;
    }

    public synchronized XSNamedMap getComponents(short s2) {
        if (s2 <= 0 || s2 > 16 || !GLOBAL_COMP[s2]) {
            return XSNamedMapImpl.EMPTY_MAP;
        }
        if (this.fComponents == null) {
            this.fComponents = new XSNamedMap[17];
        }
        if (this.fComponents[s2] == null) {
            SymbolHash symbolHash = null;
            switch (s2) {
                case 3: 
                case 15: 
                case 16: {
                    symbolHash = this.fGlobalTypeDecls;
                    break;
                }
                case 1: {
                    symbolHash = this.fGlobalAttrDecls;
                    break;
                }
                case 2: {
                    symbolHash = this.fGlobalElemDecls;
                    break;
                }
                case 5: {
                    symbolHash = this.fGlobalAttrGrpDecls;
                    break;
                }
                case 6: {
                    symbolHash = this.fGlobalGroupDecls;
                    break;
                }
                case 11: {
                    symbolHash = this.fGlobalNotationDecls;
                }
            }
            this.fComponents[s2] = s2 == 15 || s2 == 16 ? new XSNamedMap4Types(this.fTargetNamespace, symbolHash, s2) : new XSNamedMapImpl(this.fTargetNamespace, symbolHash);
        }
        return this.fComponents[s2];
    }

    public synchronized ObjectList getComponentsExt(short s2) {
        if (s2 <= 0 || s2 > 16 || !GLOBAL_COMP[s2]) {
            return ObjectListImpl.EMPTY_LIST;
        }
        if (this.fComponentsExt == null) {
            this.fComponentsExt = new ObjectList[17];
        }
        if (this.fComponentsExt[s2] == null) {
            SymbolHash symbolHash = null;
            switch (s2) {
                case 3: 
                case 15: 
                case 16: {
                    symbolHash = this.fGlobalTypeDeclsExt;
                    break;
                }
                case 1: {
                    symbolHash = this.fGlobalAttrDeclsExt;
                    break;
                }
                case 2: {
                    symbolHash = this.fGlobalElemDeclsExt;
                    break;
                }
                case 5: {
                    symbolHash = this.fGlobalAttrGrpDeclsExt;
                    break;
                }
                case 6: {
                    symbolHash = this.fGlobalGroupDeclsExt;
                    break;
                }
                case 11: {
                    symbolHash = this.fGlobalNotationDeclsExt;
                }
            }
            Object[] arrobject = symbolHash.getEntries();
            this.fComponentsExt[s2] = new ObjectListImpl(arrobject, arrobject.length);
        }
        return this.fComponentsExt[s2];
    }

    public synchronized void resetComponents() {
        this.fComponents = null;
        this.fComponentsExt = null;
    }

    public XSTypeDefinition getTypeDefinition(String string) {
        return this.getGlobalTypeDecl(string);
    }

    public XSAttributeDeclaration getAttributeDeclaration(String string) {
        return this.getGlobalAttributeDecl(string);
    }

    public XSElementDeclaration getElementDeclaration(String string) {
        return this.getGlobalElementDecl(string);
    }

    public XSAttributeGroupDefinition getAttributeGroup(String string) {
        return this.getGlobalAttributeGroupDecl(string);
    }

    public XSModelGroupDefinition getModelGroupDefinition(String string) {
        return this.getGlobalGroupDecl(string);
    }

    public XSNotationDeclaration getNotationDeclaration(String string) {
        return this.getGlobalNotationDecl(string);
    }

    public StringList getDocumentLocations() {
        return new StringListImpl(this.fLocations);
    }

    public XSModel toXSModel() {
        return new XSModelImpl(new SchemaGrammar[]{this});
    }

    public XSModel toXSModel(XSGrammar[] arrxSGrammar) {
        if (arrxSGrammar == null || arrxSGrammar.length == 0) {
            return this.toXSModel();
        }
        int n2 = arrxSGrammar.length;
        boolean bl = false;
        int n3 = 0;
        while (n3 < n2) {
            if (arrxSGrammar[n3] == this) {
                bl = true;
                break;
            }
            ++n3;
        }
        SchemaGrammar[] arrschemaGrammar = new SchemaGrammar[bl ? n2 : n2 + 1];
        int n4 = 0;
        while (n4 < n2) {
            arrschemaGrammar[n4] = (SchemaGrammar)arrxSGrammar[n4];
            ++n4;
        }
        if (!bl) {
            arrschemaGrammar[n2] = this;
        }
        return new XSModelImpl(arrschemaGrammar);
    }

    public XSObjectList getAnnotations() {
        if (this.fNumAnnotations == 0) {
            return XSObjectListImpl.EMPTY_LIST;
        }
        return new XSObjectListImpl(this.fAnnotations, this.fNumAnnotations);
    }

    public void addAnnotation(XSAnnotationImpl xSAnnotationImpl) {
        if (xSAnnotationImpl == null) {
            return;
        }
        if (this.fAnnotations == null) {
            this.fAnnotations = new XSAnnotationImpl[2];
        } else if (this.fNumAnnotations == this.fAnnotations.length) {
            XSAnnotationImpl[] arrxSAnnotationImpl = new XSAnnotationImpl[this.fNumAnnotations << 1];
            System.arraycopy(this.fAnnotations, 0, arrxSAnnotationImpl, 0, this.fNumAnnotations);
            this.fAnnotations = arrxSAnnotationImpl;
        }
        this.fAnnotations[this.fNumAnnotations++] = xSAnnotationImpl;
    }

    public void setImmutable(boolean bl) {
        this.fIsImmutable = bl;
    }

    public boolean isImmutable() {
        return this.fIsImmutable;
    }

    private static class BuiltinAttrDecl
    extends XSAttributeDecl {
        public BuiltinAttrDecl(String string, String string2, XSSimpleType xSSimpleType, short s2) {
            this.fName = string;
            this.fTargetNamespace = string2;
            this.fType = xSSimpleType;
            this.fScope = s2;
        }

        public void setValues(String string, String string2, XSSimpleType xSSimpleType, short s2, short s3, ValidatedInfo validatedInfo, XSComplexTypeDecl xSComplexTypeDecl) {
        }

        public void reset() {
        }

        public XSAnnotation getAnnotation() {
            return null;
        }

        public XSNamespaceItem getNamespaceItem() {
            return SchemaGrammar.SG_XSI;
        }
    }

    private static class XSAnyType
    extends XSComplexTypeDecl {
        public XSAnyType() {
            this.fName = "anyType";
            this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
            this.fBaseType = this;
            this.fDerivedBy = 2;
            this.fContentType = 3;
            this.fParticle = null;
            this.fAttrGrp = null;
        }

        public void setValues(String string, String string2, XSTypeDefinition xSTypeDefinition, short s2, short s3, short s4, short s5, boolean bl, XSAttributeGroupDecl xSAttributeGroupDecl, XSSimpleType xSSimpleType, XSParticleDecl xSParticleDecl) {
        }

        public void setName(String string) {
        }

        public void setIsAbstractType() {
        }

        public void setContainsTypeID() {
        }

        public void setIsAnonymous() {
        }

        public void reset() {
        }

        public XSObjectList getAttributeUses() {
            return XSObjectListImpl.EMPTY_LIST;
        }

        public XSAttributeGroupDecl getAttrGrp() {
            XSWildcardDecl xSWildcardDecl = new XSWildcardDecl();
            xSWildcardDecl.fProcessContents = 3;
            XSAttributeGroupDecl xSAttributeGroupDecl = new XSAttributeGroupDecl();
            xSAttributeGroupDecl.fAttributeWC = xSWildcardDecl;
            return xSAttributeGroupDecl;
        }

        public XSWildcard getAttributeWildcard() {
            XSWildcardDecl xSWildcardDecl = new XSWildcardDecl();
            xSWildcardDecl.fProcessContents = 3;
            return xSWildcardDecl;
        }

        public XSParticle getParticle() {
            XSWildcardDecl xSWildcardDecl = new XSWildcardDecl();
            xSWildcardDecl.fProcessContents = 3;
            XSParticleDecl xSParticleDecl = new XSParticleDecl();
            xSParticleDecl.fMinOccurs = 0;
            xSParticleDecl.fMaxOccurs = -1;
            xSParticleDecl.fType = 2;
            xSParticleDecl.fValue = xSWildcardDecl;
            XSModelGroupImpl xSModelGroupImpl = new XSModelGroupImpl();
            xSModelGroupImpl.fCompositor = 102;
            xSModelGroupImpl.fParticleCount = 1;
            xSModelGroupImpl.fParticles = new XSParticleDecl[1];
            xSModelGroupImpl.fParticles[0] = xSParticleDecl;
            XSParticleDecl xSParticleDecl2 = new XSParticleDecl();
            xSParticleDecl2.fType = 3;
            xSParticleDecl2.fValue = xSModelGroupImpl;
            return xSParticleDecl2;
        }

        public XSObjectList getAnnotations() {
            return XSObjectListImpl.EMPTY_LIST;
        }

        public XSNamespaceItem getNamespaceItem() {
            return SchemaGrammar.SG_SchemaNS;
        }
    }

    public static final class Schema4Annotations
    extends SchemaGrammar {
        public static final Schema4Annotations INSTANCE = new Schema4Annotations();

        private Schema4Annotations() {
            this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
            this.fGrammarDescription = new XSDDescription();
            this.fGrammarDescription.fContextType = 3;
            this.fGrammarDescription.setNamespace(SchemaSymbols.URI_SCHEMAFORSCHEMA);
            this.fGlobalAttrDecls = new SymbolHash(1);
            this.fGlobalAttrGrpDecls = new SymbolHash(1);
            this.fGlobalElemDecls = new SymbolHash(6);
            this.fGlobalGroupDecls = new SymbolHash(1);
            this.fGlobalNotationDecls = new SymbolHash(1);
            this.fGlobalIDConstraintDecls = new SymbolHash(1);
            this.fGlobalAttrDeclsExt = new SymbolHash(1);
            this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
            this.fGlobalElemDeclsExt = new SymbolHash(6);
            this.fGlobalGroupDeclsExt = new SymbolHash(1);
            this.fGlobalNotationDeclsExt = new SymbolHash(1);
            this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
            this.fGlobalTypeDeclsExt = new SymbolHash(1);
            this.fAllGlobalElemDecls = new SymbolHash(6);
            this.fGlobalTypeDecls = SchemaGrammar.SG_SchemaNS.fGlobalTypeDecls;
            XSElementDecl xSElementDecl = this.createAnnotationElementDecl(SchemaSymbols.ELT_ANNOTATION);
            XSElementDecl xSElementDecl2 = this.createAnnotationElementDecl(SchemaSymbols.ELT_DOCUMENTATION);
            XSElementDecl xSElementDecl3 = this.createAnnotationElementDecl(SchemaSymbols.ELT_APPINFO);
            this.fGlobalElemDecls.put(xSElementDecl.fName, xSElementDecl);
            this.fGlobalElemDecls.put(xSElementDecl2.fName, xSElementDecl2);
            this.fGlobalElemDecls.put(xSElementDecl3.fName, xSElementDecl3);
            this.fGlobalElemDeclsExt.put("," + xSElementDecl.fName, xSElementDecl);
            this.fGlobalElemDeclsExt.put("," + xSElementDecl2.fName, xSElementDecl2);
            this.fGlobalElemDeclsExt.put("," + xSElementDecl3.fName, xSElementDecl3);
            this.fAllGlobalElemDecls.put(xSElementDecl, xSElementDecl);
            this.fAllGlobalElemDecls.put(xSElementDecl2, xSElementDecl2);
            this.fAllGlobalElemDecls.put(xSElementDecl3, xSElementDecl3);
            XSComplexTypeDecl xSComplexTypeDecl = new XSComplexTypeDecl();
            XSComplexTypeDecl xSComplexTypeDecl2 = new XSComplexTypeDecl();
            XSComplexTypeDecl xSComplexTypeDecl3 = new XSComplexTypeDecl();
            xSElementDecl.fType = xSComplexTypeDecl;
            xSElementDecl2.fType = xSComplexTypeDecl2;
            xSElementDecl3.fType = xSComplexTypeDecl3;
            XSAttributeGroupDecl xSAttributeGroupDecl = new XSAttributeGroupDecl();
            XSAttributeGroupDecl xSAttributeGroupDecl2 = new XSAttributeGroupDecl();
            XSAttributeGroupDecl xSAttributeGroupDecl3 = new XSAttributeGroupDecl();
            XSObject xSObject = new XSAttributeUseImpl();
            xSObject.fAttrDecl = new XSAttributeDecl();
            xSObject.fAttrDecl.setValues(SchemaSymbols.ATT_ID, null, (XSSimpleType)this.fGlobalTypeDecls.get("ID"), 0, 2, null, xSComplexTypeDecl, null);
            xSObject.fUse = 0;
            xSObject.fConstraintType = 0;
            XSObject xSObject2 = new XSAttributeUseImpl();
            xSObject2.fAttrDecl = new XSAttributeDecl();
            xSObject2.fAttrDecl.setValues(SchemaSymbols.ATT_SOURCE, null, (XSSimpleType)this.fGlobalTypeDecls.get("anyURI"), 0, 2, null, xSComplexTypeDecl2, null);
            xSObject2.fUse = 0;
            xSObject2.fConstraintType = 0;
            XSAttributeUseImpl xSAttributeUseImpl = new XSAttributeUseImpl();
            xSAttributeUseImpl.fAttrDecl = new XSAttributeDecl();
            xSAttributeUseImpl.fAttrDecl.setValues("lang".intern(), NamespaceContext.XML_URI, (XSSimpleType)this.fGlobalTypeDecls.get("language"), 0, 2, null, xSComplexTypeDecl2, null);
            xSAttributeUseImpl.fUse = 0;
            xSAttributeUseImpl.fConstraintType = 0;
            XSAttributeUseImpl xSAttributeUseImpl2 = new XSAttributeUseImpl();
            xSAttributeUseImpl2.fAttrDecl = new XSAttributeDecl();
            xSAttributeUseImpl2.fAttrDecl.setValues(SchemaSymbols.ATT_SOURCE, null, (XSSimpleType)this.fGlobalTypeDecls.get("anyURI"), 0, 2, null, xSComplexTypeDecl3, null);
            xSAttributeUseImpl2.fUse = 0;
            xSAttributeUseImpl2.fConstraintType = 0;
            XSWildcardDecl xSWildcardDecl = new XSWildcardDecl();
            xSWildcardDecl.fNamespaceList = new String[]{this.fTargetNamespace, null};
            xSWildcardDecl.fType = 2;
            xSWildcardDecl.fProcessContents = 3;
            xSAttributeGroupDecl.addAttributeUse((XSAttributeUseImpl)xSObject);
            xSAttributeGroupDecl.fAttributeWC = xSWildcardDecl;
            xSAttributeGroupDecl2.addAttributeUse((XSAttributeUseImpl)xSObject2);
            xSAttributeGroupDecl2.addAttributeUse(xSAttributeUseImpl);
            xSAttributeGroupDecl2.fAttributeWC = xSWildcardDecl;
            xSAttributeGroupDecl3.addAttributeUse(xSAttributeUseImpl2);
            xSAttributeGroupDecl3.fAttributeWC = xSWildcardDecl;
            xSObject = this.createUnboundedModelGroupParticle();
            xSObject2 = new XSModelGroupImpl();
            xSObject2.fCompositor = 101;
            xSObject2.fParticleCount = 2;
            xSObject2.fParticles = new XSParticleDecl[2];
            xSObject2.fParticles[0] = this.createChoiceElementParticle(xSElementDecl3);
            xSObject2.fParticles[1] = this.createChoiceElementParticle(xSElementDecl2);
            xSObject.fValue = xSObject2;
            xSObject2 = this.createUnboundedAnyWildcardSequenceParticle();
            xSComplexTypeDecl.setValues("#AnonType_" + SchemaSymbols.ELT_ANNOTATION, this.fTargetNamespace, SchemaGrammar.fAnyType, 2, 0, 3, 2, false, xSAttributeGroupDecl, null, (XSParticleDecl)xSObject, new XSObjectListImpl(null, 0));
            xSComplexTypeDecl.setName("#AnonType_" + SchemaSymbols.ELT_ANNOTATION);
            xSComplexTypeDecl.setIsAnonymous();
            xSComplexTypeDecl2.setValues("#AnonType_" + SchemaSymbols.ELT_DOCUMENTATION, this.fTargetNamespace, SchemaGrammar.fAnyType, 2, 0, 3, 3, false, xSAttributeGroupDecl2, null, (XSParticleDecl)xSObject2, new XSObjectListImpl(null, 0));
            xSComplexTypeDecl2.setName("#AnonType_" + SchemaSymbols.ELT_DOCUMENTATION);
            xSComplexTypeDecl2.setIsAnonymous();
            xSComplexTypeDecl3.setValues("#AnonType_" + SchemaSymbols.ELT_APPINFO, this.fTargetNamespace, SchemaGrammar.fAnyType, 2, 0, 3, 3, false, xSAttributeGroupDecl3, null, (XSParticleDecl)xSObject2, new XSObjectListImpl(null, 0));
            xSComplexTypeDecl3.setName("#AnonType_" + SchemaSymbols.ELT_APPINFO);
            xSComplexTypeDecl3.setIsAnonymous();
        }

        public XMLGrammarDescription getGrammarDescription() {
            return this.fGrammarDescription.makeClone();
        }

        public void setImportedGrammars(Vector vector) {
        }

        public void addGlobalAttributeDecl(XSAttributeDecl xSAttributeDecl) {
        }

        public void addGlobalAttributeDecl(XSAttributeGroupDecl xSAttributeGroupDecl, String string) {
        }

        public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl xSAttributeGroupDecl) {
        }

        public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl xSAttributeGroupDecl, String string) {
        }

        public void addGlobalElementDecl(XSElementDecl xSElementDecl) {
        }

        public void addGlobalElementDecl(XSElementDecl xSElementDecl, String string) {
        }

        public void addGlobalElementDeclAll(XSElementDecl xSElementDecl) {
        }

        public void addGlobalGroupDecl(XSGroupDecl xSGroupDecl) {
        }

        public void addGlobalGroupDecl(XSGroupDecl xSGroupDecl, String string) {
        }

        public void addGlobalNotationDecl(XSNotationDecl xSNotationDecl) {
        }

        public void addGlobalNotationDecl(XSNotationDecl xSNotationDecl, String string) {
        }

        public void addGlobalTypeDecl(XSTypeDefinition xSTypeDefinition) {
        }

        public void addGlobalTypeDecl(XSTypeDefinition xSTypeDefinition, String string) {
        }

        public void addGlobalComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl) {
        }

        public void addGlobalComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl, String string) {
        }

        public void addGlobalSimpleTypeDecl(XSSimpleType xSSimpleType) {
        }

        public void addGlobalSimpleTypeDecl(XSSimpleType xSSimpleType, String string) {
        }

        public void addComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl, SimpleLocator simpleLocator) {
        }

        public void addRedefinedGroupDecl(XSGroupDecl xSGroupDecl, XSGroupDecl xSGroupDecl2, SimpleLocator simpleLocator) {
        }

        public synchronized void addDocument(Object object, String string) {
        }

        synchronized DOMParser getDOMParser() {
            return null;
        }

        synchronized SAXParser getSAXParser() {
            return null;
        }

        private XSElementDecl createAnnotationElementDecl(String string) {
            XSElementDecl xSElementDecl = new XSElementDecl();
            xSElementDecl.fName = string;
            xSElementDecl.fTargetNamespace = this.fTargetNamespace;
            xSElementDecl.setIsGlobal();
            xSElementDecl.fBlock = 7;
            xSElementDecl.setConstraintType(0);
            return xSElementDecl;
        }

        private XSParticleDecl createUnboundedModelGroupParticle() {
            XSParticleDecl xSParticleDecl = new XSParticleDecl();
            xSParticleDecl.fMinOccurs = 0;
            xSParticleDecl.fMaxOccurs = -1;
            xSParticleDecl.fType = 3;
            return xSParticleDecl;
        }

        private XSParticleDecl createChoiceElementParticle(XSElementDecl xSElementDecl) {
            XSParticleDecl xSParticleDecl = new XSParticleDecl();
            xSParticleDecl.fMinOccurs = 1;
            xSParticleDecl.fMaxOccurs = 1;
            xSParticleDecl.fType = 1;
            xSParticleDecl.fValue = xSElementDecl;
            return xSParticleDecl;
        }

        private XSParticleDecl createUnboundedAnyWildcardSequenceParticle() {
            XSParticleDecl xSParticleDecl = this.createUnboundedModelGroupParticle();
            XSModelGroupImpl xSModelGroupImpl = new XSModelGroupImpl();
            xSModelGroupImpl.fCompositor = 102;
            xSModelGroupImpl.fParticleCount = 1;
            xSModelGroupImpl.fParticles = new XSParticleDecl[1];
            xSModelGroupImpl.fParticles[0] = this.createAnyLaxWildcardParticle();
            xSParticleDecl.fValue = xSModelGroupImpl;
            return xSParticleDecl;
        }

        private XSParticleDecl createAnyLaxWildcardParticle() {
            XSParticleDecl xSParticleDecl = new XSParticleDecl();
            xSParticleDecl.fMinOccurs = 1;
            xSParticleDecl.fMaxOccurs = 1;
            xSParticleDecl.fType = 2;
            XSWildcardDecl xSWildcardDecl = new XSWildcardDecl();
            xSWildcardDecl.fNamespaceList = null;
            xSWildcardDecl.fType = 1;
            xSWildcardDecl.fProcessContents = 3;
            xSParticleDecl.fValue = xSWildcardDecl;
            return xSParticleDecl;
        }
    }

    public static class BuiltinSchemaGrammar
    extends SchemaGrammar {
        private static final String EXTENDED_SCHEMA_FACTORY_CLASS = "org.apache.xerces.impl.dv.xs.ExtendedSchemaDVFactoryImpl";

        public BuiltinSchemaGrammar(int n2, short s2) {
            SchemaDVFactory schemaDVFactory = s2 == 1 ? SchemaDVFactory.getInstance() : SchemaDVFactory.getInstance("org.apache.xerces.impl.dv.xs.ExtendedSchemaDVFactoryImpl");
            if (n2 == 1) {
                this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
                this.fGrammarDescription = new XSDDescription();
                this.fGrammarDescription.fContextType = 3;
                this.fGrammarDescription.setNamespace(SchemaSymbols.URI_SCHEMAFORSCHEMA);
                this.fGlobalAttrDecls = new SymbolHash(1);
                this.fGlobalAttrGrpDecls = new SymbolHash(1);
                this.fGlobalElemDecls = new SymbolHash(1);
                this.fGlobalGroupDecls = new SymbolHash(1);
                this.fGlobalNotationDecls = new SymbolHash(1);
                this.fGlobalIDConstraintDecls = new SymbolHash(1);
                this.fGlobalAttrDeclsExt = new SymbolHash(1);
                this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
                this.fGlobalElemDeclsExt = new SymbolHash(1);
                this.fGlobalGroupDeclsExt = new SymbolHash(1);
                this.fGlobalNotationDeclsExt = new SymbolHash(1);
                this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
                this.fGlobalTypeDeclsExt = new SymbolHash(1);
                this.fAllGlobalElemDecls = new SymbolHash(1);
                this.fGlobalTypeDecls = schemaDVFactory.getBuiltInTypes();
                int n3 = this.fGlobalTypeDecls.getLength();
                Object[] arrobject = new XSTypeDefinition[n3];
                this.fGlobalTypeDecls.getValues(arrobject, 0);
                int n4 = 0;
                while (n4 < n3) {
                    Object object = arrobject[n4];
                    if (object instanceof XSSimpleTypeDecl) {
                        ((XSSimpleTypeDecl)object).setNamespaceItem(this);
                    }
                    ++n4;
                }
                this.fGlobalTypeDecls.put(SchemaGrammar.fAnyType.getName(), SchemaGrammar.fAnyType);
            } else if (n2 == 2) {
                this.fTargetNamespace = SchemaSymbols.URI_XSI;
                this.fGrammarDescription = new XSDDescription();
                this.fGrammarDescription.fContextType = 3;
                this.fGrammarDescription.setNamespace(SchemaSymbols.URI_XSI);
                this.fGlobalAttrGrpDecls = new SymbolHash(1);
                this.fGlobalElemDecls = new SymbolHash(1);
                this.fGlobalGroupDecls = new SymbolHash(1);
                this.fGlobalNotationDecls = new SymbolHash(1);
                this.fGlobalIDConstraintDecls = new SymbolHash(1);
                this.fGlobalTypeDecls = new SymbolHash(1);
                this.fGlobalAttrDeclsExt = new SymbolHash(1);
                this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
                this.fGlobalElemDeclsExt = new SymbolHash(1);
                this.fGlobalGroupDeclsExt = new SymbolHash(1);
                this.fGlobalNotationDeclsExt = new SymbolHash(1);
                this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
                this.fGlobalTypeDeclsExt = new SymbolHash(1);
                this.fAllGlobalElemDecls = new SymbolHash(1);
                this.fGlobalAttrDecls = new SymbolHash(8);
                String string = null;
                String string2 = null;
                XSSimpleType xSSimpleType = null;
                short s3 = 1;
                string = SchemaSymbols.XSI_TYPE;
                string2 = SchemaSymbols.URI_XSI;
                xSSimpleType = schemaDVFactory.getBuiltInType("QName");
                this.fGlobalAttrDecls.put(string, new BuiltinAttrDecl(string, string2, xSSimpleType, s3));
                string = SchemaSymbols.XSI_NIL;
                string2 = SchemaSymbols.URI_XSI;
                xSSimpleType = schemaDVFactory.getBuiltInType("boolean");
                this.fGlobalAttrDecls.put(string, new BuiltinAttrDecl(string, string2, xSSimpleType, s3));
                XSSimpleType xSSimpleType2 = schemaDVFactory.getBuiltInType("anyURI");
                string = SchemaSymbols.XSI_SCHEMALOCATION;
                string2 = SchemaSymbols.URI_XSI;
                xSSimpleType = schemaDVFactory.createTypeList("#AnonType_schemaLocation", SchemaSymbols.URI_XSI, 0, xSSimpleType2, null);
                if (xSSimpleType instanceof XSSimpleTypeDecl) {
                    ((XSSimpleTypeDecl)xSSimpleType).setAnonymous(true);
                }
                this.fGlobalAttrDecls.put(string, new BuiltinAttrDecl(string, string2, xSSimpleType, s3));
                string = SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION;
                string2 = SchemaSymbols.URI_XSI;
                xSSimpleType = xSSimpleType2;
                this.fGlobalAttrDecls.put(string, new BuiltinAttrDecl(string, string2, xSSimpleType, s3));
            }
        }

        public XMLGrammarDescription getGrammarDescription() {
            return this.fGrammarDescription.makeClone();
        }

        public void setImportedGrammars(Vector vector) {
        }

        public void addGlobalAttributeDecl(XSAttributeDecl xSAttributeDecl) {
        }

        public void addGlobalAttributeDecl(XSAttributeDecl xSAttributeDecl, String string) {
        }

        public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl xSAttributeGroupDecl) {
        }

        public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl xSAttributeGroupDecl, String string) {
        }

        public void addGlobalElementDecl(XSElementDecl xSElementDecl) {
        }

        public void addGlobalElementDecl(XSElementDecl xSElementDecl, String string) {
        }

        public void addGlobalElementDeclAll(XSElementDecl xSElementDecl) {
        }

        public void addGlobalGroupDecl(XSGroupDecl xSGroupDecl) {
        }

        public void addGlobalGroupDecl(XSGroupDecl xSGroupDecl, String string) {
        }

        public void addGlobalNotationDecl(XSNotationDecl xSNotationDecl) {
        }

        public void addGlobalNotationDecl(XSNotationDecl xSNotationDecl, String string) {
        }

        public void addGlobalTypeDecl(XSTypeDefinition xSTypeDefinition) {
        }

        public void addGlobalTypeDecl(XSTypeDefinition xSTypeDefinition, String string) {
        }

        public void addGlobalComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl) {
        }

        public void addGlobalComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl, String string) {
        }

        public void addGlobalSimpleTypeDecl(XSSimpleType xSSimpleType) {
        }

        public void addGlobalSimpleTypeDecl(XSSimpleType xSSimpleType, String string) {
        }

        public void addComplexTypeDecl(XSComplexTypeDecl xSComplexTypeDecl, SimpleLocator simpleLocator) {
        }

        public void addRedefinedGroupDecl(XSGroupDecl xSGroupDecl, XSGroupDecl xSGroupDecl2, SimpleLocator simpleLocator) {
        }

        public synchronized void addDocument(Object object, String string) {
        }

        synchronized DOMParser getDOMParser() {
            return null;
        }

        synchronized SAXParser getSAXParser() {
            return null;
        }
    }

}

