/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.xml.res.XMLMessages;

public class ObjectPool
implements Serializable {
    private final Class objectType;
    private final ArrayList freeStack;

    public ObjectPool(Class class_) {
        this.objectType = class_;
        this.freeStack = new ArrayList();
    }

    public ObjectPool() {
        this.objectType = null;
        this.freeStack = new ArrayList();
    }

    public synchronized Object getInstance() {
        if (this.freeStack.isEmpty()) {
            try {
                return this.objectType.newInstance();
            }
            catch (InstantiationException instantiationException) {
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_EXCEPTION_CREATING_POOL", null));
        }
        Object e2 = this.freeStack.remove(this.freeStack.size() - 1);
        return e2;
    }

    public synchronized void freeInstance(Object object) {
        this.freeStack.add(object);
    }
}

