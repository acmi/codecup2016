/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.io.PrintStream;
import java.util.Vector;

public class ObjectArray {
    private int m_minArraySize = 10;
    private Vector m_Arrays = new Vector(200);
    private _ObjectArray m_currentArray;
    private int m_nextSlot;

    public ObjectArray() {
        this.init(10);
    }

    public ObjectArray(int n2) {
        this.init(n2);
    }

    private void init(int n2) {
        this.m_minArraySize = n2;
        this.m_currentArray = new _ObjectArray(this, this.m_minArraySize);
    }

    public Object getAt(int n2) {
        int n3 = n2 / this.m_minArraySize;
        int n4 = n2 - n3 * this.m_minArraySize;
        if (n3 < this.m_Arrays.size()) {
            _ObjectArray _ObjectArray2 = (_ObjectArray)this.m_Arrays.elementAt(n3);
            return _ObjectArray2.objects[n4];
        }
        return this.m_currentArray.objects[n4];
    }

    public void setAt(int n2, Object object) {
        int n3 = n2 / this.m_minArraySize;
        int n4 = n2 - n3 * this.m_minArraySize;
        if (n3 < this.m_Arrays.size()) {
            _ObjectArray _ObjectArray2 = (_ObjectArray)this.m_Arrays.elementAt(n3);
            _ObjectArray2.objects[n4] = object;
        } else {
            this.m_currentArray.objects[n4] = object;
        }
    }

    public int append(Object object) {
        if (this.m_nextSlot >= this.m_minArraySize) {
            this.m_Arrays.addElement(this.m_currentArray);
            this.m_nextSlot = 0;
            this.m_currentArray = new _ObjectArray(this, this.m_minArraySize);
        }
        this.m_currentArray.objects[this.m_nextSlot] = object;
        int n2 = this.m_Arrays.size() * this.m_minArraySize + this.m_nextSlot;
        ++this.m_nextSlot;
        return n2;
    }

    public static void main(String[] arrstring) {
        int n2;
        String[] arrstring2 = new String[]{"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Twenty-One", "Twenty-Two", "Twenty-Three", "Twenty-Four", "Twenty-Five", "Twenty-Six", "Twenty-Seven", "Twenty-Eight", "Twenty-Nine", "Thirty", "Thirty-One", "Thirty-Two", "Thirty-Three", "Thirty-Four", "Thirty-Five", "Thirty-Six", "Thirty-Seven", "Thirty-Eight", "Thirty-Nine"};
        ObjectArray objectArray = new ObjectArray();
        for (n2 = 0; n2 < arrstring2.length; ++n2) {
            System.out.print(" - " + objectArray.append(arrstring2[n2]));
        }
        System.out.println("\n");
        for (n2 = 0; n2 < arrstring2.length; ++n2) {
            String string = (String)objectArray.getAt(n2);
            System.out.println(string);
        }
        System.out.println((String)objectArray.getAt(5));
        System.out.println((String)objectArray.getAt(10));
        System.out.println((String)objectArray.getAt(20));
        System.out.println((String)objectArray.getAt(2));
        System.out.println((String)objectArray.getAt(15));
        System.out.println((String)objectArray.getAt(30));
        System.out.println((String)objectArray.getAt(6));
        System.out.println((String)objectArray.getAt(8));
        System.out.println((String)objectArray.getAt(40));
    }

    class _ObjectArray {
        public Object[] objects;
        private final ObjectArray this$0;

        public _ObjectArray(ObjectArray objectArray, int n2) {
            this.this$0 = objectArray;
            this.objects = new Object[n2];
        }
    }

}

