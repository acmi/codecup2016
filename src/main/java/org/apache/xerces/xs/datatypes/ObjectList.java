/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.xs.datatypes;

import java.util.List;

public interface ObjectList
extends List {
    public int getLength();

    public boolean contains(Object var1);

    public Object item(int var1);
}

