/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xs;

import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

public interface XSFacet
extends XSObject {
    public short getFacetKind();

    public String getLexicalFacetValue();

    public boolean getFixed();

    public XSAnnotation getAnnotation();

    public XSObjectList getAnnotations();
}

