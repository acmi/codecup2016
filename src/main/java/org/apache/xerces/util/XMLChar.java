/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.util.Arrays;

public class XMLChar {
    private static final byte[] CHARS = new byte[65536];
    public static final int MASK_VALID = 1;
    public static final int MASK_SPACE = 2;
    public static final int MASK_NAME_START = 4;
    public static final int MASK_NAME = 8;
    public static final int MASK_PUBID = 16;
    public static final int MASK_CONTENT = 32;
    public static final int MASK_NCNAME_START = 64;
    public static final int MASK_NCNAME = 128;

    public static boolean isSupplemental(int n2) {
        return n2 >= 65536 && n2 <= 1114111;
    }

    public static int supplemental(char c2, char c3) {
        return (c2 - 55296) * 1024 + (c3 - 56320) + 65536;
    }

    public static char highSurrogate(int n2) {
        return (char)((n2 - 65536 >> 10) + 55296);
    }

    public static char lowSurrogate(int n2) {
        return (char)((n2 - 65536 & 1023) + 56320);
    }

    public static boolean isHighSurrogate(int n2) {
        return 55296 <= n2 && n2 <= 56319;
    }

    public static boolean isLowSurrogate(int n2) {
        return 56320 <= n2 && n2 <= 57343;
    }

    public static boolean isValid(int n2) {
        return n2 < 65536 && (CHARS[n2] & 1) != 0 || 65536 <= n2 && n2 <= 1114111;
    }

    public static boolean isInvalid(int n2) {
        return !XMLChar.isValid(n2);
    }

    public static boolean isContent(int n2) {
        return n2 < 65536 && (CHARS[n2] & 32) != 0 || 65536 <= n2 && n2 <= 1114111;
    }

    public static boolean isMarkup(int n2) {
        return n2 == 60 || n2 == 38 || n2 == 37;
    }

    public static boolean isSpace(int n2) {
        return n2 <= 32 && (CHARS[n2] & 2) != 0;
    }

    public static boolean isNameStart(int n2) {
        return n2 < 65536 && (CHARS[n2] & 4) != 0;
    }

    public static boolean isName(int n2) {
        return n2 < 65536 && (CHARS[n2] & 8) != 0;
    }

    public static boolean isNCNameStart(int n2) {
        return n2 < 65536 && (CHARS[n2] & 64) != 0;
    }

    public static boolean isNCName(int n2) {
        return n2 < 65536 && (CHARS[n2] & 128) != 0;
    }

    public static boolean isPubid(int n2) {
        return n2 < 65536 && (CHARS[n2] & 16) != 0;
    }

    public static boolean isValidName(String string) {
        int n2 = string.length();
        if (n2 == 0) {
            return false;
        }
        char c2 = string.charAt(0);
        if (!XMLChar.isNameStart(c2)) {
            return false;
        }
        int n3 = 1;
        while (n3 < n2) {
            c2 = string.charAt(n3);
            if (!XMLChar.isName(c2)) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    public static boolean isValidNCName(String string) {
        int n2 = string.length();
        if (n2 == 0) {
            return false;
        }
        char c2 = string.charAt(0);
        if (!XMLChar.isNCNameStart(c2)) {
            return false;
        }
        int n3 = 1;
        while (n3 < n2) {
            c2 = string.charAt(n3);
            if (!XMLChar.isNCName(c2)) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    public static boolean isValidNmtoken(String string) {
        int n2 = string.length();
        if (n2 == 0) {
            return false;
        }
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (!XMLChar.isName(c2)) {
                return false;
            }
            ++n3;
        }
        return true;
    }

    public static boolean isValidIANAEncoding(String string) {
        int n2;
        char c2;
        if (string != null && (n2 = string.length()) > 0 && ((c2 = string.charAt(0)) >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z')) {
            int n3 = 1;
            while (n3 < n2) {
                c2 = string.charAt(n3);
                if (!(c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z' || c2 >= '0' && c2 <= '9' || c2 == '.' || c2 == '_' || c2 == '-')) {
                    return false;
                }
                ++n3;
            }
            return true;
        }
        return false;
    }

    public static boolean isValidJavaEncoding(String string) {
        int n2;
        if (string != null && (n2 = string.length()) > 0) {
            int n3 = 1;
            while (n3 < n2) {
                char c2 = string.charAt(n3);
                if (!(c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z' || c2 >= '0' && c2 <= '9' || c2 == '.' || c2 == '_' || c2 == '-')) {
                    return false;
                }
                ++n3;
            }
            return true;
        }
        return false;
    }

    public static String trim(String string) {
        int n2 = string.length() - 1;
        int n3 = 0;
        while (n3 <= n2) {
            if (!XMLChar.isSpace(string.charAt(n3))) break;
            ++n3;
        }
        int n4 = n2;
        while (n4 >= n3) {
            if (!XMLChar.isSpace(string.charAt(n4))) break;
            --n4;
        }
        if (n3 == 0 && n4 == n2) {
            return string;
        }
        if (n3 > n2) {
            return "";
        }
        return string.substring(n3, n4 + 1);
    }

    static {
        XMLChar.CHARS[9] = 35;
        XMLChar.CHARS[10] = 19;
        XMLChar.CHARS[13] = 19;
        XMLChar.CHARS[32] = 51;
        XMLChar.CHARS[33] = 49;
        XMLChar.CHARS[34] = 33;
        Arrays.fill(CHARS, 35, 38, 49);
        XMLChar.CHARS[38] = 1;
        Arrays.fill(CHARS, 39, 45, 49);
        Arrays.fill(CHARS, 45, 47, -71);
        XMLChar.CHARS[47] = 49;
        Arrays.fill(CHARS, 48, 58, -71);
        XMLChar.CHARS[58] = 61;
        XMLChar.CHARS[59] = 49;
        XMLChar.CHARS[60] = 1;
        XMLChar.CHARS[61] = 49;
        XMLChar.CHARS[62] = 33;
        Arrays.fill(CHARS, 63, 65, 49);
        Arrays.fill(CHARS, 65, 91, -3);
        Arrays.fill(CHARS, 91, 93, 33);
        XMLChar.CHARS[93] = 1;
        XMLChar.CHARS[94] = 33;
        XMLChar.CHARS[95] = -3;
        XMLChar.CHARS[96] = 33;
        Arrays.fill(CHARS, 97, 123, -3);
        Arrays.fill(CHARS, 123, 183, 33);
        XMLChar.CHARS[183] = -87;
        Arrays.fill(CHARS, 184, 192, 33);
        Arrays.fill(CHARS, 192, 215, -19);
        XMLChar.CHARS[215] = 33;
        Arrays.fill(CHARS, 216, 247, -19);
        XMLChar.CHARS[247] = 33;
        Arrays.fill(CHARS, 248, 306, -19);
        Arrays.fill(CHARS, 306, 308, 33);
        Arrays.fill(CHARS, 308, 319, -19);
        Arrays.fill(CHARS, 319, 321, 33);
        Arrays.fill(CHARS, 321, 329, -19);
        XMLChar.CHARS[329] = 33;
        Arrays.fill(CHARS, 330, 383, -19);
        XMLChar.CHARS[383] = 33;
        Arrays.fill(CHARS, 384, 452, -19);
        Arrays.fill(CHARS, 452, 461, 33);
        Arrays.fill(CHARS, 461, 497, -19);
        Arrays.fill(CHARS, 497, 500, 33);
        Arrays.fill(CHARS, 500, 502, -19);
        Arrays.fill(CHARS, 502, 506, 33);
        Arrays.fill(CHARS, 506, 536, -19);
        Arrays.fill(CHARS, 536, 592, 33);
        Arrays.fill(CHARS, 592, 681, -19);
        Arrays.fill(CHARS, 681, 699, 33);
        Arrays.fill(CHARS, 699, 706, -19);
        Arrays.fill(CHARS, 706, 720, 33);
        Arrays.fill(CHARS, 720, 722, -87);
        Arrays.fill(CHARS, 722, 768, 33);
        Arrays.fill(CHARS, 768, 838, -87);
        Arrays.fill(CHARS, 838, 864, 33);
        Arrays.fill(CHARS, 864, 866, -87);
        Arrays.fill(CHARS, 866, 902, 33);
        XMLChar.CHARS[902] = -19;
        XMLChar.CHARS[903] = -87;
        Arrays.fill(CHARS, 904, 907, -19);
        XMLChar.CHARS[907] = 33;
        XMLChar.CHARS[908] = -19;
        XMLChar.CHARS[909] = 33;
        Arrays.fill(CHARS, 910, 930, -19);
        XMLChar.CHARS[930] = 33;
        Arrays.fill(CHARS, 931, 975, -19);
        XMLChar.CHARS[975] = 33;
        Arrays.fill(CHARS, 976, 983, -19);
        Arrays.fill(CHARS, 983, 986, 33);
        XMLChar.CHARS[986] = -19;
        XMLChar.CHARS[987] = 33;
        XMLChar.CHARS[988] = -19;
        XMLChar.CHARS[989] = 33;
        XMLChar.CHARS[990] = -19;
        XMLChar.CHARS[991] = 33;
        XMLChar.CHARS[992] = -19;
        XMLChar.CHARS[993] = 33;
        Arrays.fill(CHARS, 994, 1012, -19);
        Arrays.fill(CHARS, 1012, 1025, 33);
        Arrays.fill(CHARS, 1025, 1037, -19);
        XMLChar.CHARS[1037] = 33;
        Arrays.fill(CHARS, 1038, 1104, -19);
        XMLChar.CHARS[1104] = 33;
        Arrays.fill(CHARS, 1105, 1117, -19);
        XMLChar.CHARS[1117] = 33;
        Arrays.fill(CHARS, 1118, 1154, -19);
        XMLChar.CHARS[1154] = 33;
        Arrays.fill(CHARS, 1155, 1159, -87);
        Arrays.fill(CHARS, 1159, 1168, 33);
        Arrays.fill(CHARS, 1168, 1221, -19);
        Arrays.fill(CHARS, 1221, 1223, 33);
        Arrays.fill(CHARS, 1223, 1225, -19);
        Arrays.fill(CHARS, 1225, 1227, 33);
        Arrays.fill(CHARS, 1227, 1229, -19);
        Arrays.fill(CHARS, 1229, 1232, 33);
        Arrays.fill(CHARS, 1232, 1260, -19);
        Arrays.fill(CHARS, 1260, 1262, 33);
        Arrays.fill(CHARS, 1262, 1270, -19);
        Arrays.fill(CHARS, 1270, 1272, 33);
        Arrays.fill(CHARS, 1272, 1274, -19);
        Arrays.fill(CHARS, 1274, 1329, 33);
        Arrays.fill(CHARS, 1329, 1367, -19);
        Arrays.fill(CHARS, 1367, 1369, 33);
        XMLChar.CHARS[1369] = -19;
        Arrays.fill(CHARS, 1370, 1377, 33);
        Arrays.fill(CHARS, 1377, 1415, -19);
        Arrays.fill(CHARS, 1415, 1425, 33);
        Arrays.fill(CHARS, 1425, 1442, -87);
        XMLChar.CHARS[1442] = 33;
        Arrays.fill(CHARS, 1443, 1466, -87);
        XMLChar.CHARS[1466] = 33;
        Arrays.fill(CHARS, 1467, 1470, -87);
        XMLChar.CHARS[1470] = 33;
        XMLChar.CHARS[1471] = -87;
        XMLChar.CHARS[1472] = 33;
        Arrays.fill(CHARS, 1473, 1475, -87);
        XMLChar.CHARS[1475] = 33;
        XMLChar.CHARS[1476] = -87;
        Arrays.fill(CHARS, 1477, 1488, 33);
        Arrays.fill(CHARS, 1488, 1515, -19);
        Arrays.fill(CHARS, 1515, 1520, 33);
        Arrays.fill(CHARS, 1520, 1523, -19);
        Arrays.fill(CHARS, 1523, 1569, 33);
        Arrays.fill(CHARS, 1569, 1595, -19);
        Arrays.fill(CHARS, 1595, 1600, 33);
        XMLChar.CHARS[1600] = -87;
        Arrays.fill(CHARS, 1601, 1611, -19);
        Arrays.fill(CHARS, 1611, 1619, -87);
        Arrays.fill(CHARS, 1619, 1632, 33);
        Arrays.fill(CHARS, 1632, 1642, -87);
        Arrays.fill(CHARS, 1642, 1648, 33);
        XMLChar.CHARS[1648] = -87;
        Arrays.fill(CHARS, 1649, 1720, -19);
        Arrays.fill(CHARS, 1720, 1722, 33);
        Arrays.fill(CHARS, 1722, 1727, -19);
        XMLChar.CHARS[1727] = 33;
        Arrays.fill(CHARS, 1728, 1743, -19);
        XMLChar.CHARS[1743] = 33;
        Arrays.fill(CHARS, 1744, 1748, -19);
        XMLChar.CHARS[1748] = 33;
        XMLChar.CHARS[1749] = -19;
        Arrays.fill(CHARS, 1750, 1765, -87);
        Arrays.fill(CHARS, 1765, 1767, -19);
        Arrays.fill(CHARS, 1767, 1769, -87);
        XMLChar.CHARS[1769] = 33;
        Arrays.fill(CHARS, 1770, 1774, -87);
        Arrays.fill(CHARS, 1774, 1776, 33);
        Arrays.fill(CHARS, 1776, 1786, -87);
        Arrays.fill(CHARS, 1786, 2305, 33);
        Arrays.fill(CHARS, 2305, 2308, -87);
        XMLChar.CHARS[2308] = 33;
        Arrays.fill(CHARS, 2309, 2362, -19);
        Arrays.fill(CHARS, 2362, 2364, 33);
        XMLChar.CHARS[2364] = -87;
        XMLChar.CHARS[2365] = -19;
        Arrays.fill(CHARS, 2366, 2382, -87);
        Arrays.fill(CHARS, 2382, 2385, 33);
        Arrays.fill(CHARS, 2385, 2389, -87);
        Arrays.fill(CHARS, 2389, 2392, 33);
        Arrays.fill(CHARS, 2392, 2402, -19);
        Arrays.fill(CHARS, 2402, 2404, -87);
        Arrays.fill(CHARS, 2404, 2406, 33);
        Arrays.fill(CHARS, 2406, 2416, -87);
        Arrays.fill(CHARS, 2416, 2433, 33);
        Arrays.fill(CHARS, 2433, 2436, -87);
        XMLChar.CHARS[2436] = 33;
        Arrays.fill(CHARS, 2437, 2445, -19);
        Arrays.fill(CHARS, 2445, 2447, 33);
        Arrays.fill(CHARS, 2447, 2449, -19);
        Arrays.fill(CHARS, 2449, 2451, 33);
        Arrays.fill(CHARS, 2451, 2473, -19);
        XMLChar.CHARS[2473] = 33;
        Arrays.fill(CHARS, 2474, 2481, -19);
        XMLChar.CHARS[2481] = 33;
        XMLChar.CHARS[2482] = -19;
        Arrays.fill(CHARS, 2483, 2486, 33);
        Arrays.fill(CHARS, 2486, 2490, -19);
        Arrays.fill(CHARS, 2490, 2492, 33);
        XMLChar.CHARS[2492] = -87;
        XMLChar.CHARS[2493] = 33;
        Arrays.fill(CHARS, 2494, 2501, -87);
        Arrays.fill(CHARS, 2501, 2503, 33);
        Arrays.fill(CHARS, 2503, 2505, -87);
        Arrays.fill(CHARS, 2505, 2507, 33);
        Arrays.fill(CHARS, 2507, 2510, -87);
        Arrays.fill(CHARS, 2510, 2519, 33);
        XMLChar.CHARS[2519] = -87;
        Arrays.fill(CHARS, 2520, 2524, 33);
        Arrays.fill(CHARS, 2524, 2526, -19);
        XMLChar.CHARS[2526] = 33;
        Arrays.fill(CHARS, 2527, 2530, -19);
        Arrays.fill(CHARS, 2530, 2532, -87);
        Arrays.fill(CHARS, 2532, 2534, 33);
        Arrays.fill(CHARS, 2534, 2544, -87);
        Arrays.fill(CHARS, 2544, 2546, -19);
        Arrays.fill(CHARS, 2546, 2562, 33);
        XMLChar.CHARS[2562] = -87;
        Arrays.fill(CHARS, 2563, 2565, 33);
        Arrays.fill(CHARS, 2565, 2571, -19);
        Arrays.fill(CHARS, 2571, 2575, 33);
        Arrays.fill(CHARS, 2575, 2577, -19);
        Arrays.fill(CHARS, 2577, 2579, 33);
        Arrays.fill(CHARS, 2579, 2601, -19);
        XMLChar.CHARS[2601] = 33;
        Arrays.fill(CHARS, 2602, 2609, -19);
        XMLChar.CHARS[2609] = 33;
        Arrays.fill(CHARS, 2610, 2612, -19);
        XMLChar.CHARS[2612] = 33;
        Arrays.fill(CHARS, 2613, 2615, -19);
        XMLChar.CHARS[2615] = 33;
        Arrays.fill(CHARS, 2616, 2618, -19);
        Arrays.fill(CHARS, 2618, 2620, 33);
        XMLChar.CHARS[2620] = -87;
        XMLChar.CHARS[2621] = 33;
        Arrays.fill(CHARS, 2622, 2627, -87);
        Arrays.fill(CHARS, 2627, 2631, 33);
        Arrays.fill(CHARS, 2631, 2633, -87);
        Arrays.fill(CHARS, 2633, 2635, 33);
        Arrays.fill(CHARS, 2635, 2638, -87);
        Arrays.fill(CHARS, 2638, 2649, 33);
        Arrays.fill(CHARS, 2649, 2653, -19);
        XMLChar.CHARS[2653] = 33;
        XMLChar.CHARS[2654] = -19;
        Arrays.fill(CHARS, 2655, 2662, 33);
        Arrays.fill(CHARS, 2662, 2674, -87);
        Arrays.fill(CHARS, 2674, 2677, -19);
        Arrays.fill(CHARS, 2677, 2689, 33);
        Arrays.fill(CHARS, 2689, 2692, -87);
        XMLChar.CHARS[2692] = 33;
        Arrays.fill(CHARS, 2693, 2700, -19);
        XMLChar.CHARS[2700] = 33;
        XMLChar.CHARS[2701] = -19;
        XMLChar.CHARS[2702] = 33;
        Arrays.fill(CHARS, 2703, 2706, -19);
        XMLChar.CHARS[2706] = 33;
        Arrays.fill(CHARS, 2707, 2729, -19);
        XMLChar.CHARS[2729] = 33;
        Arrays.fill(CHARS, 2730, 2737, -19);
        XMLChar.CHARS[2737] = 33;
        Arrays.fill(CHARS, 2738, 2740, -19);
        XMLChar.CHARS[2740] = 33;
        Arrays.fill(CHARS, 2741, 2746, -19);
        Arrays.fill(CHARS, 2746, 2748, 33);
        XMLChar.CHARS[2748] = -87;
        XMLChar.CHARS[2749] = -19;
        Arrays.fill(CHARS, 2750, 2758, -87);
        XMLChar.CHARS[2758] = 33;
        Arrays.fill(CHARS, 2759, 2762, -87);
        XMLChar.CHARS[2762] = 33;
        Arrays.fill(CHARS, 2763, 2766, -87);
        Arrays.fill(CHARS, 2766, 2784, 33);
        XMLChar.CHARS[2784] = -19;
        Arrays.fill(CHARS, 2785, 2790, 33);
        Arrays.fill(CHARS, 2790, 2800, -87);
        Arrays.fill(CHARS, 2800, 2817, 33);
        Arrays.fill(CHARS, 2817, 2820, -87);
        XMLChar.CHARS[2820] = 33;
        Arrays.fill(CHARS, 2821, 2829, -19);
        Arrays.fill(CHARS, 2829, 2831, 33);
        Arrays.fill(CHARS, 2831, 2833, -19);
        Arrays.fill(CHARS, 2833, 2835, 33);
        Arrays.fill(CHARS, 2835, 2857, -19);
        XMLChar.CHARS[2857] = 33;
        Arrays.fill(CHARS, 2858, 2865, -19);
        XMLChar.CHARS[2865] = 33;
        Arrays.fill(CHARS, 2866, 2868, -19);
        Arrays.fill(CHARS, 2868, 2870, 33);
        Arrays.fill(CHARS, 2870, 2874, -19);
        Arrays.fill(CHARS, 2874, 2876, 33);
        XMLChar.CHARS[2876] = -87;
        XMLChar.CHARS[2877] = -19;
        Arrays.fill(CHARS, 2878, 2884, -87);
        Arrays.fill(CHARS, 2884, 2887, 33);
        Arrays.fill(CHARS, 2887, 2889, -87);
        Arrays.fill(CHARS, 2889, 2891, 33);
        Arrays.fill(CHARS, 2891, 2894, -87);
        Arrays.fill(CHARS, 2894, 2902, 33);
        Arrays.fill(CHARS, 2902, 2904, -87);
        Arrays.fill(CHARS, 2904, 2908, 33);
        Arrays.fill(CHARS, 2908, 2910, -19);
        XMLChar.CHARS[2910] = 33;
        Arrays.fill(CHARS, 2911, 2914, -19);
        Arrays.fill(CHARS, 2914, 2918, 33);
        Arrays.fill(CHARS, 2918, 2928, -87);
        Arrays.fill(CHARS, 2928, 2946, 33);
        Arrays.fill(CHARS, 2946, 2948, -87);
        XMLChar.CHARS[2948] = 33;
        Arrays.fill(CHARS, 2949, 2955, -19);
        Arrays.fill(CHARS, 2955, 2958, 33);
        Arrays.fill(CHARS, 2958, 2961, -19);
        XMLChar.CHARS[2961] = 33;
        Arrays.fill(CHARS, 2962, 2966, -19);
        Arrays.fill(CHARS, 2966, 2969, 33);
        Arrays.fill(CHARS, 2969, 2971, -19);
        XMLChar.CHARS[2971] = 33;
        XMLChar.CHARS[2972] = -19;
        XMLChar.CHARS[2973] = 33;
        Arrays.fill(CHARS, 2974, 2976, -19);
        Arrays.fill(CHARS, 2976, 2979, 33);
        Arrays.fill(CHARS, 2979, 2981, -19);
        Arrays.fill(CHARS, 2981, 2984, 33);
        Arrays.fill(CHARS, 2984, 2987, -19);
        Arrays.fill(CHARS, 2987, 2990, 33);
        Arrays.fill(CHARS, 2990, 2998, -19);
        XMLChar.CHARS[2998] = 33;
        Arrays.fill(CHARS, 2999, 3002, -19);
        Arrays.fill(CHARS, 3002, 3006, 33);
        Arrays.fill(CHARS, 3006, 3011, -87);
        Arrays.fill(CHARS, 3011, 3014, 33);
        Arrays.fill(CHARS, 3014, 3017, -87);
        XMLChar.CHARS[3017] = 33;
        Arrays.fill(CHARS, 3018, 3022, -87);
        Arrays.fill(CHARS, 3022, 3031, 33);
        XMLChar.CHARS[3031] = -87;
        Arrays.fill(CHARS, 3032, 3047, 33);
        Arrays.fill(CHARS, 3047, 3056, -87);
        Arrays.fill(CHARS, 3056, 3073, 33);
        Arrays.fill(CHARS, 3073, 3076, -87);
        XMLChar.CHARS[3076] = 33;
        Arrays.fill(CHARS, 3077, 3085, -19);
        XMLChar.CHARS[3085] = 33;
        Arrays.fill(CHARS, 3086, 3089, -19);
        XMLChar.CHARS[3089] = 33;
        Arrays.fill(CHARS, 3090, 3113, -19);
        XMLChar.CHARS[3113] = 33;
        Arrays.fill(CHARS, 3114, 3124, -19);
        XMLChar.CHARS[3124] = 33;
        Arrays.fill(CHARS, 3125, 3130, -19);
        Arrays.fill(CHARS, 3130, 3134, 33);
        Arrays.fill(CHARS, 3134, 3141, -87);
        XMLChar.CHARS[3141] = 33;
        Arrays.fill(CHARS, 3142, 3145, -87);
        XMLChar.CHARS[3145] = 33;
        Arrays.fill(CHARS, 3146, 3150, -87);
        Arrays.fill(CHARS, 3150, 3157, 33);
        Arrays.fill(CHARS, 3157, 3159, -87);
        Arrays.fill(CHARS, 3159, 3168, 33);
        Arrays.fill(CHARS, 3168, 3170, -19);
        Arrays.fill(CHARS, 3170, 3174, 33);
        Arrays.fill(CHARS, 3174, 3184, -87);
        Arrays.fill(CHARS, 3184, 3202, 33);
        Arrays.fill(CHARS, 3202, 3204, -87);
        XMLChar.CHARS[3204] = 33;
        Arrays.fill(CHARS, 3205, 3213, -19);
        XMLChar.CHARS[3213] = 33;
        Arrays.fill(CHARS, 3214, 3217, -19);
        XMLChar.CHARS[3217] = 33;
        Arrays.fill(CHARS, 3218, 3241, -19);
        XMLChar.CHARS[3241] = 33;
        Arrays.fill(CHARS, 3242, 3252, -19);
        XMLChar.CHARS[3252] = 33;
        Arrays.fill(CHARS, 3253, 3258, -19);
        Arrays.fill(CHARS, 3258, 3262, 33);
        Arrays.fill(CHARS, 3262, 3269, -87);
        XMLChar.CHARS[3269] = 33;
        Arrays.fill(CHARS, 3270, 3273, -87);
        XMLChar.CHARS[3273] = 33;
        Arrays.fill(CHARS, 3274, 3278, -87);
        Arrays.fill(CHARS, 3278, 3285, 33);
        Arrays.fill(CHARS, 3285, 3287, -87);
        Arrays.fill(CHARS, 3287, 3294, 33);
        XMLChar.CHARS[3294] = -19;
        XMLChar.CHARS[3295] = 33;
        Arrays.fill(CHARS, 3296, 3298, -19);
        Arrays.fill(CHARS, 3298, 3302, 33);
        Arrays.fill(CHARS, 3302, 3312, -87);
        Arrays.fill(CHARS, 3312, 3330, 33);
        Arrays.fill(CHARS, 3330, 3332, -87);
        XMLChar.CHARS[3332] = 33;
        Arrays.fill(CHARS, 3333, 3341, -19);
        XMLChar.CHARS[3341] = 33;
        Arrays.fill(CHARS, 3342, 3345, -19);
        XMLChar.CHARS[3345] = 33;
        Arrays.fill(CHARS, 3346, 3369, -19);
        XMLChar.CHARS[3369] = 33;
        Arrays.fill(CHARS, 3370, 3386, -19);
        Arrays.fill(CHARS, 3386, 3390, 33);
        Arrays.fill(CHARS, 3390, 3396, -87);
        Arrays.fill(CHARS, 3396, 3398, 33);
        Arrays.fill(CHARS, 3398, 3401, -87);
        XMLChar.CHARS[3401] = 33;
        Arrays.fill(CHARS, 3402, 3406, -87);
        Arrays.fill(CHARS, 3406, 3415, 33);
        XMLChar.CHARS[3415] = -87;
        Arrays.fill(CHARS, 3416, 3424, 33);
        Arrays.fill(CHARS, 3424, 3426, -19);
        Arrays.fill(CHARS, 3426, 3430, 33);
        Arrays.fill(CHARS, 3430, 3440, -87);
        Arrays.fill(CHARS, 3440, 3585, 33);
        Arrays.fill(CHARS, 3585, 3631, -19);
        XMLChar.CHARS[3631] = 33;
        XMLChar.CHARS[3632] = -19;
        XMLChar.CHARS[3633] = -87;
        Arrays.fill(CHARS, 3634, 3636, -19);
        Arrays.fill(CHARS, 3636, 3643, -87);
        Arrays.fill(CHARS, 3643, 3648, 33);
        Arrays.fill(CHARS, 3648, 3654, -19);
        Arrays.fill(CHARS, 3654, 3663, -87);
        XMLChar.CHARS[3663] = 33;
        Arrays.fill(CHARS, 3664, 3674, -87);
        Arrays.fill(CHARS, 3674, 3713, 33);
        Arrays.fill(CHARS, 3713, 3715, -19);
        XMLChar.CHARS[3715] = 33;
        XMLChar.CHARS[3716] = -19;
        Arrays.fill(CHARS, 3717, 3719, 33);
        Arrays.fill(CHARS, 3719, 3721, -19);
        XMLChar.CHARS[3721] = 33;
        XMLChar.CHARS[3722] = -19;
        Arrays.fill(CHARS, 3723, 3725, 33);
        XMLChar.CHARS[3725] = -19;
        Arrays.fill(CHARS, 3726, 3732, 33);
        Arrays.fill(CHARS, 3732, 3736, -19);
        XMLChar.CHARS[3736] = 33;
        Arrays.fill(CHARS, 3737, 3744, -19);
        XMLChar.CHARS[3744] = 33;
        Arrays.fill(CHARS, 3745, 3748, -19);
        XMLChar.CHARS[3748] = 33;
        XMLChar.CHARS[3749] = -19;
        XMLChar.CHARS[3750] = 33;
        XMLChar.CHARS[3751] = -19;
        Arrays.fill(CHARS, 3752, 3754, 33);
        Arrays.fill(CHARS, 3754, 3756, -19);
        XMLChar.CHARS[3756] = 33;
        Arrays.fill(CHARS, 3757, 3759, -19);
        XMLChar.CHARS[3759] = 33;
        XMLChar.CHARS[3760] = -19;
        XMLChar.CHARS[3761] = -87;
        Arrays.fill(CHARS, 3762, 3764, -19);
        Arrays.fill(CHARS, 3764, 3770, -87);
        XMLChar.CHARS[3770] = 33;
        Arrays.fill(CHARS, 3771, 3773, -87);
        XMLChar.CHARS[3773] = -19;
        Arrays.fill(CHARS, 3774, 3776, 33);
        Arrays.fill(CHARS, 3776, 3781, -19);
        XMLChar.CHARS[3781] = 33;
        XMLChar.CHARS[3782] = -87;
        XMLChar.CHARS[3783] = 33;
        Arrays.fill(CHARS, 3784, 3790, -87);
        Arrays.fill(CHARS, 3790, 3792, 33);
        Arrays.fill(CHARS, 3792, 3802, -87);
        Arrays.fill(CHARS, 3802, 3864, 33);
        Arrays.fill(CHARS, 3864, 3866, -87);
        Arrays.fill(CHARS, 3866, 3872, 33);
        Arrays.fill(CHARS, 3872, 3882, -87);
        Arrays.fill(CHARS, 3882, 3893, 33);
        XMLChar.CHARS[3893] = -87;
        XMLChar.CHARS[3894] = 33;
        XMLChar.CHARS[3895] = -87;
        XMLChar.CHARS[3896] = 33;
        XMLChar.CHARS[3897] = -87;
        Arrays.fill(CHARS, 3898, 3902, 33);
        Arrays.fill(CHARS, 3902, 3904, -87);
        Arrays.fill(CHARS, 3904, 3912, -19);
        XMLChar.CHARS[3912] = 33;
        Arrays.fill(CHARS, 3913, 3946, -19);
        Arrays.fill(CHARS, 3946, 3953, 33);
        Arrays.fill(CHARS, 3953, 3973, -87);
        XMLChar.CHARS[3973] = 33;
        Arrays.fill(CHARS, 3974, 3980, -87);
        Arrays.fill(CHARS, 3980, 3984, 33);
        Arrays.fill(CHARS, 3984, 3990, -87);
        XMLChar.CHARS[3990] = 33;
        XMLChar.CHARS[3991] = -87;
        XMLChar.CHARS[3992] = 33;
        Arrays.fill(CHARS, 3993, 4014, -87);
        Arrays.fill(CHARS, 4014, 4017, 33);
        Arrays.fill(CHARS, 4017, 4024, -87);
        XMLChar.CHARS[4024] = 33;
        XMLChar.CHARS[4025] = -87;
        Arrays.fill(CHARS, 4026, 4256, 33);
        Arrays.fill(CHARS, 4256, 4294, -19);
        Arrays.fill(CHARS, 4294, 4304, 33);
        Arrays.fill(CHARS, 4304, 4343, -19);
        Arrays.fill(CHARS, 4343, 4352, 33);
        XMLChar.CHARS[4352] = -19;
        XMLChar.CHARS[4353] = 33;
        Arrays.fill(CHARS, 4354, 4356, -19);
        XMLChar.CHARS[4356] = 33;
        Arrays.fill(CHARS, 4357, 4360, -19);
        XMLChar.CHARS[4360] = 33;
        XMLChar.CHARS[4361] = -19;
        XMLChar.CHARS[4362] = 33;
        Arrays.fill(CHARS, 4363, 4365, -19);
        XMLChar.CHARS[4365] = 33;
        Arrays.fill(CHARS, 4366, 4371, -19);
        Arrays.fill(CHARS, 4371, 4412, 33);
        XMLChar.CHARS[4412] = -19;
        XMLChar.CHARS[4413] = 33;
        XMLChar.CHARS[4414] = -19;
        XMLChar.CHARS[4415] = 33;
        XMLChar.CHARS[4416] = -19;
        Arrays.fill(CHARS, 4417, 4428, 33);
        XMLChar.CHARS[4428] = -19;
        XMLChar.CHARS[4429] = 33;
        XMLChar.CHARS[4430] = -19;
        XMLChar.CHARS[4431] = 33;
        XMLChar.CHARS[4432] = -19;
        Arrays.fill(CHARS, 4433, 4436, 33);
        Arrays.fill(CHARS, 4436, 4438, -19);
        Arrays.fill(CHARS, 4438, 4441, 33);
        XMLChar.CHARS[4441] = -19;
        Arrays.fill(CHARS, 4442, 4447, 33);
        Arrays.fill(CHARS, 4447, 4450, -19);
        XMLChar.CHARS[4450] = 33;
        XMLChar.CHARS[4451] = -19;
        XMLChar.CHARS[4452] = 33;
        XMLChar.CHARS[4453] = -19;
        XMLChar.CHARS[4454] = 33;
        XMLChar.CHARS[4455] = -19;
        XMLChar.CHARS[4456] = 33;
        XMLChar.CHARS[4457] = -19;
        Arrays.fill(CHARS, 4458, 4461, 33);
        Arrays.fill(CHARS, 4461, 4463, -19);
        Arrays.fill(CHARS, 4463, 4466, 33);
        Arrays.fill(CHARS, 4466, 4468, -19);
        XMLChar.CHARS[4468] = 33;
        XMLChar.CHARS[4469] = -19;
        Arrays.fill(CHARS, 4470, 4510, 33);
        XMLChar.CHARS[4510] = -19;
        Arrays.fill(CHARS, 4511, 4520, 33);
        XMLChar.CHARS[4520] = -19;
        Arrays.fill(CHARS, 4521, 4523, 33);
        XMLChar.CHARS[4523] = -19;
        Arrays.fill(CHARS, 4524, 4526, 33);
        Arrays.fill(CHARS, 4526, 4528, -19);
        Arrays.fill(CHARS, 4528, 4535, 33);
        Arrays.fill(CHARS, 4535, 4537, -19);
        XMLChar.CHARS[4537] = 33;
        XMLChar.CHARS[4538] = -19;
        XMLChar.CHARS[4539] = 33;
        Arrays.fill(CHARS, 4540, 4547, -19);
        Arrays.fill(CHARS, 4547, 4587, 33);
        XMLChar.CHARS[4587] = -19;
        Arrays.fill(CHARS, 4588, 4592, 33);
        XMLChar.CHARS[4592] = -19;
        Arrays.fill(CHARS, 4593, 4601, 33);
        XMLChar.CHARS[4601] = -19;
        Arrays.fill(CHARS, 4602, 7680, 33);
        Arrays.fill(CHARS, 7680, 7836, -19);
        Arrays.fill(CHARS, 7836, 7840, 33);
        Arrays.fill(CHARS, 7840, 7930, -19);
        Arrays.fill(CHARS, 7930, 7936, 33);
        Arrays.fill(CHARS, 7936, 7958, -19);
        Arrays.fill(CHARS, 7958, 7960, 33);
        Arrays.fill(CHARS, 7960, 7966, -19);
        Arrays.fill(CHARS, 7966, 7968, 33);
        Arrays.fill(CHARS, 7968, 8006, -19);
        Arrays.fill(CHARS, 8006, 8008, 33);
        Arrays.fill(CHARS, 8008, 8014, -19);
        Arrays.fill(CHARS, 8014, 8016, 33);
        Arrays.fill(CHARS, 8016, 8024, -19);
        XMLChar.CHARS[8024] = 33;
        XMLChar.CHARS[8025] = -19;
        XMLChar.CHARS[8026] = 33;
        XMLChar.CHARS[8027] = -19;
        XMLChar.CHARS[8028] = 33;
        XMLChar.CHARS[8029] = -19;
        XMLChar.CHARS[8030] = 33;
        Arrays.fill(CHARS, 8031, 8062, -19);
        Arrays.fill(CHARS, 8062, 8064, 33);
        Arrays.fill(CHARS, 8064, 8117, -19);
        XMLChar.CHARS[8117] = 33;
        Arrays.fill(CHARS, 8118, 8125, -19);
        XMLChar.CHARS[8125] = 33;
        XMLChar.CHARS[8126] = -19;
        Arrays.fill(CHARS, 8127, 8130, 33);
        Arrays.fill(CHARS, 8130, 8133, -19);
        XMLChar.CHARS[8133] = 33;
        Arrays.fill(CHARS, 8134, 8141, -19);
        Arrays.fill(CHARS, 8141, 8144, 33);
        Arrays.fill(CHARS, 8144, 8148, -19);
        Arrays.fill(CHARS, 8148, 8150, 33);
        Arrays.fill(CHARS, 8150, 8156, -19);
        Arrays.fill(CHARS, 8156, 8160, 33);
        Arrays.fill(CHARS, 8160, 8173, -19);
        Arrays.fill(CHARS, 8173, 8178, 33);
        Arrays.fill(CHARS, 8178, 8181, -19);
        XMLChar.CHARS[8181] = 33;
        Arrays.fill(CHARS, 8182, 8189, -19);
        Arrays.fill(CHARS, 8189, 8400, 33);
        Arrays.fill(CHARS, 8400, 8413, -87);
        Arrays.fill(CHARS, 8413, 8417, 33);
        XMLChar.CHARS[8417] = -87;
        Arrays.fill(CHARS, 8418, 8486, 33);
        XMLChar.CHARS[8486] = -19;
        Arrays.fill(CHARS, 8487, 8490, 33);
        Arrays.fill(CHARS, 8490, 8492, -19);
        Arrays.fill(CHARS, 8492, 8494, 33);
        XMLChar.CHARS[8494] = -19;
        Arrays.fill(CHARS, 8495, 8576, 33);
        Arrays.fill(CHARS, 8576, 8579, -19);
        Arrays.fill(CHARS, 8579, 12293, 33);
        XMLChar.CHARS[12293] = -87;
        XMLChar.CHARS[12294] = 33;
        XMLChar.CHARS[12295] = -19;
        Arrays.fill(CHARS, 12296, 12321, 33);
        Arrays.fill(CHARS, 12321, 12330, -19);
        Arrays.fill(CHARS, 12330, 12336, -87);
        XMLChar.CHARS[12336] = 33;
        Arrays.fill(CHARS, 12337, 12342, -87);
        Arrays.fill(CHARS, 12342, 12353, 33);
        Arrays.fill(CHARS, 12353, 12437, -19);
        Arrays.fill(CHARS, 12437, 12441, 33);
        Arrays.fill(CHARS, 12441, 12443, -87);
        Arrays.fill(CHARS, 12443, 12445, 33);
        Arrays.fill(CHARS, 12445, 12447, -87);
        Arrays.fill(CHARS, 12447, 12449, 33);
        Arrays.fill(CHARS, 12449, 12539, -19);
        XMLChar.CHARS[12539] = 33;
        Arrays.fill(CHARS, 12540, 12543, -87);
        Arrays.fill(CHARS, 12543, 12549, 33);
        Arrays.fill(CHARS, 12549, 12589, -19);
        Arrays.fill(CHARS, 12589, 19968, 33);
        Arrays.fill(CHARS, 19968, 40870, -19);
        Arrays.fill(CHARS, 40870, 44032, 33);
        Arrays.fill(CHARS, 44032, 55204, -19);
        Arrays.fill(CHARS, 55204, 55296, 33);
        Arrays.fill(CHARS, 57344, 65534, 33);
    }
}
