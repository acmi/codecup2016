/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;

public interface DTMIterator {
    public DTM getDTM(int var1);

    public DTMManager getDTMManager();

    public int getRoot();

    public void setRoot(int var1, Object var2);

    public void reset();

    public int getWhatToShow();

    public boolean getExpandEntityReferences();

    public int nextNode();

    public int previousNode();

    public void detach();

    public void allowDetachToRelease(boolean var1);

    public int getCurrentNode();

    public void setShouldCacheNodes(boolean var1);

    public int getCurrentPos();

    public void runTo(int var1);

    public void setCurrentPos(int var1);

    public int item(int var1);

    public void setItem(int var1, int var2);

    public int getLength();

    public DTMIterator cloneWithReset() throws CloneNotSupportedException;

    public Object clone() throws CloneNotSupportedException;

    public boolean isDocOrdered();

    public int getAxis();
}

