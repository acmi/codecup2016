/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.dtm;

public final class Axis {
    private static final boolean[] isReverse = new boolean[]{true, true, false, false, false, false, false, false, false, false, false, true, true, false};
    private static final String[] names = new String[]{"ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", "namespace-decls", "namespace", "parent", "preceding", "preceding-sibling", "self", "all-from-node", "preceding-and-ancestor", "all", "descendants-from-root", "descendants-or-self-from-root", "root", "filtered-list"};

    public static boolean isReverse(int n2) {
        return isReverse[n2];
    }

    public static String getNames(int n2) {
        return names[n2];
    }

    public static int getNamesLength() {
        return names.length;
    }
}

