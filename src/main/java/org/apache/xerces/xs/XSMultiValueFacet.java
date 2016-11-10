/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xs;

import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

public interface XSMultiValueFacet
extends XSObject {
    public short getFacetKind();

    public StringList getLexicalFacetValues();

    public XSObjectList getAnnotations();
}

