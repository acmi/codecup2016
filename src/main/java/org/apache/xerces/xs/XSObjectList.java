/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xs;

import java.util.List;
import org.apache.xerces.xs.XSObject;

public interface XSObjectList
extends List {
    public int getLength();

    public XSObject item(int var1);
}
