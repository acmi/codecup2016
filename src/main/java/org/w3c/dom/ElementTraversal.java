/*
 * Decompiled with CFR 0_119.
 */
package org.w3c.dom;

import org.w3c.dom.Element;

public interface ElementTraversal {
    public Element getFirstElementChild();

    public Element getLastElementChild();

    public Element getPreviousElementSibling();

    public Element getNextElementSibling();

    public int getChildElementCount();
}
