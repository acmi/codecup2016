/*
 * Decompiled with CFR 0_119.
 */
package org.w3c.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public interface ProcessingInstruction
extends Node {
    public String getTarget();

    public String getData();

    public void setData(String var1) throws DOMException;
}

