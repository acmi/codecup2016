/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm;

public interface DTMAxisIterator
extends Cloneable {
    public int next();

    public DTMAxisIterator reset();

    public int getLast();

    public int getPosition();

    public void setMark();

    public void gotoMark();

    public DTMAxisIterator setStartNode(int var1);

    public int getStartNode();

    public boolean isReverse();

    public DTMAxisIterator cloneIterator();

    public void setRestartable(boolean var1);

    public int getNodeByPosition(int var1);
}

