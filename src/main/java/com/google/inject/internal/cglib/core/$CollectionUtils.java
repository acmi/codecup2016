/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.cglib.core.$Predicate;
import com.google.inject.internal.cglib.core.$Transformer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class $CollectionUtils {
    private $CollectionUtils() {
    }

    public static Map bucket(Collection collection, $Transformer $Transformer) {
        HashMap hashMap = new HashMap();
        for (Object e2 : collection) {
            Object object = $Transformer.transform(e2);
            LinkedList linkedList = (LinkedList)hashMap.get(object);
            if (linkedList == null) {
                linkedList = new LinkedList();
                hashMap.put(object, linkedList);
            }
            linkedList.add(e2);
        }
        return hashMap;
    }

    public static void reverse(Map map, Map map2) {
        for (Object k2 : map.keySet()) {
            map2.put(map.get(k2), k2);
        }
    }

    public static Collection filter(Collection collection, $Predicate $Predicate) {
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            if ($Predicate.evaluate(iterator.next())) continue;
            iterator.remove();
        }
        return collection;
    }

    public static List transform(Collection collection, $Transformer $Transformer) {
        ArrayList<Object> arrayList = new ArrayList<Object>(collection.size());
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            arrayList.add($Transformer.transform(iterator.next()));
        }
        return arrayList;
    }

    public static Map getIndexMap(List list) {
        HashMap hashMap = new HashMap();
        int n2 = 0;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            hashMap.put(iterator.next(), new Integer(n2++));
        }
        return hashMap;
    }
}

