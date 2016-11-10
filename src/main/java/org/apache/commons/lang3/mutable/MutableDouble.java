/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.mutable.Mutable;

public class MutableDouble
extends Number
implements Comparable<MutableDouble>,
Mutable<Number> {
    private double value;

    @Override
    public void setValue(double d2) {
        this.value = d2;
    }

    @Override
    public void setValue(Number number) {
        this.value = number.doubleValue();
    }

    @Override
    public int intValue() {
        return (int)this.value;
    }

    @Override
    public long longValue() {
        return (long)this.value;
    }

    @Override
    public float floatValue() {
        return (float)this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    public boolean equals(Object object) {
        return object instanceof MutableDouble && Double.doubleToLongBits(((MutableDouble)object).value) == Double.doubleToLongBits(this.value);
    }

    public int hashCode() {
        long l2 = Double.doubleToLongBits(this.value);
        return (int)(l2 ^ l2 >>> 32);
    }

    @Override
    public int compareTo(MutableDouble mutableDouble) {
        return Double.compare(this.value, mutableDouble.value);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}

