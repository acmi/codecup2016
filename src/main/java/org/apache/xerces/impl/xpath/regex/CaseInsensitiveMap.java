/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xpath.regex;

final class CaseInsensitiveMap {
    private static int CHUNK_SHIFT = 10;
    private static int CHUNK_SIZE = 1 << CHUNK_SHIFT;
    private static int CHUNK_MASK = CHUNK_SIZE - 1;
    private static int INITIAL_CHUNK_COUNT = 64;
    private static int[][][] caseInsensitiveMap;
    private static int LOWER_CASE_MATCH;
    private static int UPPER_CASE_MATCH;

    CaseInsensitiveMap() {
    }

    public static int[] get(int n2) {
        return n2 < 65536 ? CaseInsensitiveMap.getMapping(n2) : null;
    }

    private static int[] getMapping(int n2) {
        int n3 = n2 >>> CHUNK_SHIFT;
        int n4 = n2 & CHUNK_MASK;
        return caseInsensitiveMap[n3][n4];
    }

    private static void buildCaseInsensitiveMap() {
        caseInsensitiveMap = new int[INITIAL_CHUNK_COUNT][CHUNK_SIZE][];
        int n2 = 0;
        while (n2 < 65536) {
            char c2;
            int n3 = Character.toLowerCase((char)n2);
            if (n3 != (c2 = Character.toUpperCase((char)n2)) || n3 != n2) {
                int[] arrn;
                int[] arrn2 = new int[2];
                int n4 = 0;
                if (n3 != n2) {
                    arrn2[n4++] = n3;
                    arrn2[n4++] = LOWER_CASE_MATCH;
                    arrn = CaseInsensitiveMap.getMapping(n3);
                    if (arrn != null) {
                        arrn2 = CaseInsensitiveMap.updateMap(n2, arrn2, n3, arrn, LOWER_CASE_MATCH);
                    }
                }
                if (c2 != n2) {
                    if (n4 == arrn2.length) {
                        arrn2 = CaseInsensitiveMap.expandMap(arrn2, 2);
                    }
                    arrn2[n4++] = c2;
                    arrn2[n4++] = UPPER_CASE_MATCH;
                    arrn = CaseInsensitiveMap.getMapping(c2);
                    if (arrn != null) {
                        arrn2 = CaseInsensitiveMap.updateMap(n2, arrn2, c2, arrn, UPPER_CASE_MATCH);
                    }
                }
                CaseInsensitiveMap.set(n2, arrn2);
            }
            ++n2;
        }
    }

    private static int[] expandMap(int[] arrn, int n2) {
        int n3 = arrn.length;
        int[] arrn2 = new int[n3 + n2];
        System.arraycopy(arrn, 0, arrn2, 0, n3);
        return arrn2;
    }

    private static void set(int n2, int[] arrn) {
        int n3 = n2 >>> CHUNK_SHIFT;
        int n4 = n2 & CHUNK_MASK;
        CaseInsensitiveMap.caseInsensitiveMap[n3][n4] = arrn;
    }

    private static int[] updateMap(int n2, int[] arrn, int n3, int[] arrn2, int n4) {
        int n5 = 0;
        while (n5 < arrn2.length) {
            int n6 = arrn2[n5];
            int[] arrn3 = CaseInsensitiveMap.getMapping(n6);
            if (arrn3 != null && CaseInsensitiveMap.contains(arrn3, n3, n4)) {
                if (!CaseInsensitiveMap.contains(arrn3, n2)) {
                    arrn3 = CaseInsensitiveMap.expandAndAdd(arrn3, n2, n4);
                    CaseInsensitiveMap.set(n6, arrn3);
                }
                if (!CaseInsensitiveMap.contains(arrn, n6)) {
                    arrn = CaseInsensitiveMap.expandAndAdd(arrn, n6, n4);
                }
            }
            n5 += 2;
        }
        if (!CaseInsensitiveMap.contains(arrn2, n2)) {
            arrn2 = CaseInsensitiveMap.expandAndAdd(arrn2, n2, n4);
            CaseInsensitiveMap.set(n3, arrn2);
        }
        return arrn;
    }

    private static boolean contains(int[] arrn, int n2) {
        int n3 = 0;
        while (n3 < arrn.length) {
            if (arrn[n3] == n2) {
                return true;
            }
            n3 += 2;
        }
        return false;
    }

    private static boolean contains(int[] arrn, int n2, int n3) {
        int n4 = 0;
        while (n4 < arrn.length) {
            if (arrn[n4] == n2 && arrn[n4 + 1] == n3) {
                return true;
            }
            n4 += 2;
        }
        return false;
    }

    private static int[] expandAndAdd(int[] arrn, int n2, int n3) {
        int n4 = arrn.length;
        int[] arrn2 = new int[n4 + 2];
        System.arraycopy(arrn, 0, arrn2, 0, n4);
        arrn2[n4] = n2;
        arrn2[n4 + 1] = n3;
        return arrn2;
    }

    static {
        LOWER_CASE_MATCH = 1;
        UPPER_CASE_MATCH = 2;
        CaseInsensitiveMap.buildCaseInsensitiveMap();
    }
}

