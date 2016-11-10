/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xs;

import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSException;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

public interface XSAttributeUse
extends XSObject {
    public boolean getRequired();

    public XSAttributeDeclaration getAttrDeclaration();

    public short getConstraintType();

    public String getConstraintValue();

    public Object getActualVC() throws XSException;

    public short getActualVCType() throws XSException;

    public ShortList getItemValueTypes() throws XSException;

    public XSObjectList getAnnotations();
}

