/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.xerces.dom.AttrNSImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

public class PSVIAttrNSImpl
extends AttrNSImpl
implements AttributePSVI {
    static final long serialVersionUID = -3241738699421018889L;
    protected XSAttributeDeclaration fDeclaration = null;
    protected XSTypeDefinition fTypeDecl = null;
    protected boolean fSpecified = true;
    protected String fNormalizedValue = null;
    protected Object fActualValue = null;
    protected short fActualValueType = 45;
    protected ShortList fItemValueTypes = null;
    protected XSSimpleTypeDefinition fMemberType = null;
    protected short fValidationAttempted = 0;
    protected short fValidity = 0;
    protected StringList fErrorCodes = null;
    protected StringList fErrorMessages = null;
    protected String fValidationContext = null;

    public PSVIAttrNSImpl(CoreDocumentImpl coreDocumentImpl, String string, String string2, String string3) {
        super(coreDocumentImpl, string, string2, string3);
    }

    public PSVIAttrNSImpl(CoreDocumentImpl coreDocumentImpl, String string, String string2) {
        super(coreDocumentImpl, string, string2);
    }

    public String getSchemaDefault() {
        return this.fDeclaration == null ? null : this.fDeclaration.getConstraintValue();
    }

    public String getSchemaNormalizedValue() {
        return this.fNormalizedValue;
    }

    public boolean getIsSchemaSpecified() {
        return this.fSpecified;
    }

    public short getValidationAttempted() {
        return this.fValidationAttempted;
    }

    public short getValidity() {
        return this.fValidity;
    }

    public StringList getErrorCodes() {
        if (this.fErrorCodes != null) {
            return this.fErrorCodes;
        }
        return StringListImpl.EMPTY_LIST;
    }

    public StringList getErrorMessages() {
        if (this.fErrorMessages != null) {
            return this.fErrorMessages;
        }
        return StringListImpl.EMPTY_LIST;
    }

    public String getValidationContext() {
        return this.fValidationContext;
    }

    public XSTypeDefinition getTypeDefinition() {
        return this.fTypeDecl;
    }

    public XSSimpleTypeDefinition getMemberTypeDefinition() {
        return this.fMemberType;
    }

    public XSAttributeDeclaration getAttributeDeclaration() {
        return this.fDeclaration;
    }

    public void setPSVI(AttributePSVI attributePSVI) {
        this.fDeclaration = attributePSVI.getAttributeDeclaration();
        this.fValidationContext = attributePSVI.getValidationContext();
        this.fValidity = attributePSVI.getValidity();
        this.fValidationAttempted = attributePSVI.getValidationAttempted();
        this.fErrorCodes = attributePSVI.getErrorCodes();
        this.fErrorMessages = attributePSVI.getErrorMessages();
        this.fNormalizedValue = attributePSVI.getSchemaNormalizedValue();
        this.fActualValue = attributePSVI.getActualNormalizedValue();
        this.fActualValueType = attributePSVI.getActualNormalizedValueType();
        this.fItemValueTypes = attributePSVI.getItemValueTypes();
        this.fTypeDecl = attributePSVI.getTypeDefinition();
        this.fMemberType = attributePSVI.getMemberTypeDefinition();
        this.fSpecified = attributePSVI.getIsSchemaSpecified();
    }

    public Object getActualNormalizedValue() {
        return this.fActualValue;
    }

    public short getActualNormalizedValueType() {
        return this.fActualValueType;
    }

    public ShortList getItemValueTypes() {
        return this.fItemValueTypes;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        throw new NotSerializableException(this.getClass().getName());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        throw new NotSerializableException(this.getClass().getName());
    }
}

