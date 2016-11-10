/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.exception.util;

import java.util.ArrayList;

public class ArgUtils {
    public static Object[] flatten(Object[] arrobject) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if (arrobject != null) {
            for (Object object : arrobject) {
                if (object instanceof Object[]) {
                    for (Object object2 : ArgUtils.flatten((Object[])object)) {
                        arrayList.add(object2);
                    }
                    continue;
                }
                arrayList.add(object);
            }
        }
        return arrayList.toArray();
    }
}

