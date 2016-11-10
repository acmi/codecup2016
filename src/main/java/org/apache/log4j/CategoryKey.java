/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j;

class CategoryKey {
    String name;
    int hashCache;
    static Class class$org$apache$log4j$CategoryKey;

    CategoryKey(String string) {
        this.name = string;
        this.hashCache = string.hashCode();
    }

    public final int hashCode() {
        return this.hashCache;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null) {
            Class class_ = class$org$apache$log4j$CategoryKey == null ? (CategoryKey.class$org$apache$log4j$CategoryKey = CategoryKey.class$("org.apache.log4j.CategoryKey")) : class$org$apache$log4j$CategoryKey;
            if (class_ == object.getClass()) {
                return this.name.equals(((CategoryKey)object).name);
            }
        }
        return false;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError().initCause(classNotFoundException);
        }
    }
}

