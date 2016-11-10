/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

class NameValue {
    String key;
    String value;

    public NameValue(String string, String string2) {
        this.key = string;
        this.value = string2;
    }

    public String toString() {
        return this.key + "=" + this.value;
    }
}

