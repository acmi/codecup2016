/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xs;

import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSException;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;

public interface XSAttributeDeclaration
extends XSObject {
    public XSSimpleTypeDefinition getTypeDefinition();

    public short getScope();

    public XSComplexTypeDefinition getEnclosingCTDefinition();

    public short getConstraintType();

    public String getConstraintValue();

    public Object getActualVC() throws XSException;

    public short getActualVCType() throws XSException;

    public ShortList getItemValueTypes() throws XSException;

    public XSAnnotation getAnnotation();

    public XSObjectList getAnnotations();
}

