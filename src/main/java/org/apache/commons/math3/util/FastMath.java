/*
 * Decompiled with CFR 0_119.
 */
package org.apache.commons.math3.util;

public class FastMath {
    private static final double LOG_MAX_VALUE = StrictMath.log(Double.MAX_VALUE);
    private static final double[][] LN_QUICK_COEF = new double[][]{{1.0, 5.669184079525E-24}, {-0.25, -0.25}, {0.3333333134651184, 1.986821492305628E-8}, {-0.25, -6.663542893624021E-14}, {0.19999998807907104, 1.1921056801463227E-8}, {-0.1666666567325592, -7.800414592973399E-9}, {0.1428571343421936, 5.650007086920087E-9}, {-0.12502530217170715, -7.44321345601866E-11}, {0.11113807559013367, 9.219544613762692E-9}};
    private static final double[][] LN_HI_PREC_COEF = new double[][]{{1.0, -6.032174644509064E-23}, {-0.25, -0.25}, {0.3333333134651184, 1.9868161777724352E-8}, {-0.2499999701976776, -2.957007209750105E-8}, {0.19999954104423523, 1.5830993332061267E-10}, {-0.16624879837036133, -2.6033824355191673E-8}};
    private static final double[] SINE_TABLE_A = new double[]{0.0, 0.1246747374534607, 0.24740394949913025, 0.366272509098053, 0.4794255495071411, 0.5850973129272461, 0.6816387176513672, 0.7675435543060303, 0.8414709568023682, 0.902267575263977, 0.9489846229553223, 0.9808930158615112, 0.9974949359893799, 0.9985313415527344};
    private static final double[] SINE_TABLE_B = new double[]{0.0, -4.068233003401932E-9, 9.755392680573412E-9, 1.9987994582857286E-8, -1.0902938113007961E-8, -3.9986783938944604E-8, 4.23719669792332E-8, -5.207000323380292E-8, 2.800552834259E-8, 1.883511811213715E-8, -3.5997360512765566E-9, 4.116164446561962E-8, 5.0614674548127384E-8, -1.0129027912496858E-9};
    private static final double[] COSINE_TABLE_A = new double[]{1.0, 0.9921976327896118, 0.9689123630523682, 0.9305076599121094, 0.8775825500488281, 0.8109631538391113, 0.7316888570785522, 0.6409968137741089, 0.5403022766113281, 0.4311765432357788, 0.3153223395347595, 0.19454771280288696, 0.07073719799518585, -0.05417713522911072};
    private static final double[] COSINE_TABLE_B = new double[]{0.0, 3.4439717236742845E-8, 5.865827662008209E-8, -3.7999795083850525E-8, 1.184154459111628E-8, -3.43338934259355E-8, 1.1795268640216787E-8, 4.438921624363781E-8, 2.925681159240093E-8, -2.6437112632041807E-8, 2.2860509143963117E-8, -4.813899778443457E-9, 3.6725170580355583E-9, 2.0217439756338078E-10};
    private static final double[] TANGENT_TABLE_A = new double[]{0.0, 0.1256551444530487, 0.25534194707870483, 0.3936265707015991, 0.5463024377822876, 0.7214844226837158, 0.9315965175628662, 1.1974215507507324, 1.5574076175689697, 2.092571258544922, 3.0095696449279785, 5.041914939880371, 14.101419448852539, -18.430862426757812};
    private static final double[] TANGENT_TABLE_B = new double[]{0.0, -7.877917738262007E-9, -2.5857668567479893E-8, 5.2240336371356666E-9, 5.206150291559893E-8, 1.8307188599677033E-8, -5.7618793749770706E-8, 7.848361555046424E-8, 1.0708593250394448E-7, 1.7827257129423813E-8, 2.893485277253286E-8, 3.1660099222737955E-7, 4.983191803254889E-7, -3.356118100840571E-7};
    private static final long[] RECIP_2PI = new long[]{2935890503282001226L, 9154082963658192752L, 3952090531849364496L, 9193070505571053912L, 7910884519577875640L, 113236205062349959L, 4577762542105553359L, -5034868814120038111L, 4208363204685324176L, 5648769086999809661L, 2819561105158720014L, -4035746434778044925L, -302932621132653753L, -2644281811660520851L, -3183605296591799669L, 6722166367014452318L, -3512299194304650054L, -7278142539171889152L};
    private static final long[] PI_O_4_BITS = new long[]{-3958705157555305932L, -4267615245585081135L};
    private static final double[] EIGHTHS = new double[]{0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0, 1.125, 1.25, 1.375, 1.5, 1.625};
    private static final double[] CBRTTWO = new double[]{0.6299605249474366, 0.7937005259840998, 1.0, 1.2599210498948732, 1.5874010519681994};

    public static double sqrt(double d2) {
        return Math.sqrt(d2);
    }

    private static double polySine(double d2) {
        double d3 = d2 * d2;
        double d4 = 2.7553817452272217E-6;
        d4 = d4 * d3 + -1.9841269659586505E-4;
        d4 = d4 * d3 + 0.008333333333329196;
        d4 = d4 * d3 + -0.16666666666666666;
        d4 = d4 * d3 * d2;
        return d4;
    }

    private static double polyCosine(double d2) {
        double d3 = d2 * d2;
        double d4 = 2.479773539153719E-5;
        d4 = d4 * d3 + -0.0013888888689039883;
        d4 = d4 * d3 + 0.041666666666621166;
        d4 = d4 * d3 + -0.49999999999999994;
        return d4 *= d3;
    }

    private static double sinQ(double d2, double d3) {
        int n2 = (int)(d2 * 8.0 + 0.5);
        double d4 = d2 - EIGHTHS[n2];
        double d5 = SINE_TABLE_A[n2];
        double d6 = SINE_TABLE_B[n2];
        double d7 = COSINE_TABLE_A[n2];
        double d8 = COSINE_TABLE_B[n2];
        double d9 = d4;
        double d10 = FastMath.polySine(d4);
        double d11 = 1.0;
        double d12 = FastMath.polyCosine(d4);
        double d13 = d9 * 1.073741824E9;
        double d14 = d9 + d13 - d13;
        d9 = d14;
        double d15 = 0.0;
        double d16 = 0.0;
        double d17 = d5;
        double d18 = d15 + d17;
        double d19 = - d18 - d15 - d17;
        d15 = d18;
        d16 += d19;
        d17 = d7 * d9;
        d18 = d15 + d17;
        d19 = - d18 - d15 - d17;
        d15 = d18;
        d16 += d19;
        d16 = d16 + d5 * d12 + d7 * (d10 += d9 - d14);
        d16 = d16 + d6 + d8 * d9 + d6 * d12 + d8 * d10;
        if (d3 != 0.0) {
            d17 = ((d7 + d8) * (1.0 + d12) - (d5 + d6) * (d9 + d10)) * d3;
            d18 = d15 + d17;
            d19 = - d18 - d15 - d17;
            d15 = d18;
            d16 += d19;
        }
        double d20 = d15 + d16;
        return d20;
    }

    private static double cosQ(double d2, double d3) {
        double d4 = 1.5707963267948966;
        double d5 = 6.123233995736766E-17;
        double d6 = 1.5707963267948966 - d2;
        double d7 = - d6 - 1.5707963267948966 + d2;
        return FastMath.sinQ(d6, d7 += 6.123233995736766E-17 - d3);
    }

    private static void reducePayneHanek(double d2, double[] arrd) {
        int n2;
        long l2;
        boolean bl;
        long l3;
        long l4;
        int n3;
        long l5 = Double.doubleToRawLongBits(d2);
        int n4 = (int)(l5 >> 52 & 2047) - 1023;
        l5 &= 0xFFFFFFFFFFFFFL;
        l5 |= 0x10000000000000L;
        l5 <<= 11;
        if ((n3 = n4 - ((n2 = ++n4 >> 6) << 6)) != 0) {
            l4 = n2 == 0 ? 0 : RECIP_2PI[n2 - 1] << n3;
            l4 |= RECIP_2PI[n2] >>> 64 - n3;
            l3 = RECIP_2PI[n2] << n3 | RECIP_2PI[n2 + 1] >>> 64 - n3;
            l2 = RECIP_2PI[n2 + 1] << n3 | RECIP_2PI[n2 + 2] >>> 64 - n3;
        } else {
            l4 = n2 == 0 ? 0 : RECIP_2PI[n2 - 1];
            l3 = RECIP_2PI[n2];
            l2 = RECIP_2PI[n2 + 1];
        }
        long l6 = l5 >>> 32;
        long l7 = l5 & 0xFFFFFFFFL;
        long l8 = l3 >>> 32;
        long l9 = l3 & 0xFFFFFFFFL;
        long l10 = l6 * l8;
        long l11 = l7 * l9;
        long l12 = l7 * l8;
        long l13 = l6 * l9;
        long l14 = l11 + (l13 << 32);
        long l15 = l10 + (l13 >>> 32);
        boolean bl2 = (l11 & Long.MIN_VALUE) != 0;
        boolean bl3 = (l13 & 0x80000000L) != 0;
        boolean bl4 = bl = (l14 & Long.MIN_VALUE) != 0;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l15;
        }
        bl2 = (l14 & Long.MIN_VALUE) != 0;
        bl3 = (l12 & 0x80000000L) != 0;
        l15 += l12 >>> 32;
        boolean bl5 = bl = ((l14 += l12 << 32) & Long.MIN_VALUE) != 0;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l15;
        }
        l8 = l2 >>> 32;
        l9 = l2 & 0xFFFFFFFFL;
        l10 = l6 * l8;
        l12 = l7 * l8;
        l13 = l6 * l9;
        bl2 = (l14 & Long.MIN_VALUE) != 0;
        bl3 = ((l10 += l12 + l13 >>> 32) & Long.MIN_VALUE) != 0;
        boolean bl6 = bl = ((l14 += l10) & Long.MIN_VALUE) != 0;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l15;
        }
        l8 = l4 >>> 32;
        l9 = l4 & 0xFFFFFFFFL;
        l11 = l7 * l9;
        l12 = l7 * l8;
        l13 = l6 * l9;
        int n5 = (int)((l15 += l11 + (l12 + l13 << 32)) >>> 62);
        l15 <<= 2;
        l6 = (l15 |= (l14 <<= 2) >>> 62) >>> 32;
        l7 = l15 & 0xFFFFFFFFL;
        l8 = PI_O_4_BITS[0] >>> 32;
        l9 = PI_O_4_BITS[0] & 0xFFFFFFFFL;
        l10 = l6 * l8;
        l11 = l7 * l9;
        l12 = l7 * l8;
        l13 = l6 * l9;
        long l16 = l11 + (l13 << 32);
        long l17 = l10 + (l13 >>> 32);
        bl2 = (l11 & Long.MIN_VALUE) != 0;
        bl3 = (l13 & 0x80000000L) != 0;
        boolean bl7 = bl = (l16 & Long.MIN_VALUE) != 0;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l17;
        }
        bl2 = (l16 & Long.MIN_VALUE) != 0;
        bl3 = (l12 & 0x80000000L) != 0;
        l17 += l12 >>> 32;
        boolean bl8 = bl = ((l16 += l12 << 32) & Long.MIN_VALUE) != 0;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l17;
        }
        l8 = PI_O_4_BITS[1] >>> 32;
        l9 = PI_O_4_BITS[1] & 0xFFFFFFFFL;
        l10 = l6 * l8;
        l12 = l7 * l8;
        l13 = l6 * l9;
        bl2 = (l16 & Long.MIN_VALUE) != 0;
        bl3 = ((l10 += l12 + l13 >>> 32) & Long.MIN_VALUE) != 0;
        boolean bl9 = bl = ((l16 += l10) & Long.MIN_VALUE) != 0;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l17;
        }
        l6 = l14 >>> 32;
        l7 = l14 & 0xFFFFFFFFL;
        l8 = PI_O_4_BITS[0] >>> 32;
        l9 = PI_O_4_BITS[0] & 0xFFFFFFFFL;
        l10 = l6 * l8;
        l12 = l7 * l8;
        l13 = l6 * l9;
        bl2 = (l16 & Long.MIN_VALUE) != 0;
        bl3 = ((l10 += l12 + l13 >>> 32) & Long.MIN_VALUE) != 0;
        boolean bl10 = bl = ((l16 += l10) & Long.MIN_VALUE) != 0;
        if (bl2 && bl3 || (bl2 || bl3) && !bl) {
            ++l17;
        }
        double d3 = (double)(l17 >>> 12) / 4.503599627370496E15;
        double d4 = (double)(((l17 & 4095) << 40) + (l16 >>> 24)) / 4.503599627370496E15 / 4.503599627370496E15;
        double d5 = d3 + d4;
        double d6 = - d5 - d3 - d4;
        arrd[0] = n5;
        arrd[1] = d5 * 2.0;
        arrd[2] = d6 * 2.0;
    }

    public static double sin(double d2) {
        boolean bl = false;
        int n2 = 0;
        double d3 = 0.0;
        double d4 = d2;
        if (d2 < 0.0) {
            bl = true;
            d4 = - d4;
        }
        if (d4 == 0.0) {
            long l2 = Double.doubleToRawLongBits(d2);
            if (l2 < 0) {
                return -0.0;
            }
            return 0.0;
        }
        if (d4 != d4 || d4 == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        if (d4 > 3294198.0) {
            double[] arrd = new double[3];
            FastMath.reducePayneHanek(d4, arrd);
            n2 = (int)arrd[0] & 3;
            d4 = arrd[1];
            d3 = arrd[2];
        } else if (d4 > 1.5707963267948966) {
            CodyWaite codyWaite = new CodyWaite(d4);
            n2 = codyWaite.getK() & 3;
            d4 = codyWaite.getRemA();
            d3 = codyWaite.getRemB();
        }
        if (bl) {
            n2 ^= 2;
        }
        switch (n2) {
            case 0: {
                return FastMath.sinQ(d4, d3);
            }
            case 1: {
                return FastMath.cosQ(d4, d3);
            }
            case 2: {
                return - FastMath.sinQ(d4, d3);
            }
            case 3: {
                return - FastMath.cosQ(d4, d3);
            }
        }
        return Double.NaN;
    }

    public static double cos(double d2) {
        int n2 = 0;
        double d3 = d2;
        if (d2 < 0.0) {
            d3 = - d3;
        }
        if (d3 != d3 || d3 == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double d4 = 0.0;
        if (d3 > 3294198.0) {
            double[] arrd = new double[3];
            FastMath.reducePayneHanek(d3, arrd);
            n2 = (int)arrd[0] & 3;
            d3 = arrd[1];
            d4 = arrd[2];
        } else if (d3 > 1.5707963267948966) {
            CodyWaite codyWaite = new CodyWaite(d3);
            n2 = codyWaite.getK() & 3;
            d3 = codyWaite.getRemA();
            d4 = codyWaite.getRemB();
        }
        switch (n2) {
            case 0: {
                return FastMath.cosQ(d3, d4);
            }
            case 1: {
                return - FastMath.sinQ(d3, d4);
            }
            case 2: {
                return - FastMath.cosQ(d3, d4);
            }
            case 3: {
                return FastMath.sinQ(d3, d4);
            }
        }
        return Double.NaN;
    }

    public static double abs(double d2) {
        return Double.longBitsToDouble(Long.MAX_VALUE & Double.doubleToRawLongBits(d2));
    }

    public static double scalb(double d2, int n2) {
        if (n2 > -1023 && n2 < 1024) {
            return d2 * Double.longBitsToDouble((long)(n2 + 1023) << 52);
        }
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 == 0.0) {
            return d2;
        }
        if (n2 < -2098) {
            return d2 > 0.0 ? 0.0 : -0.0;
        }
        if (n2 > 2097) {
            return d2 > 0.0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        long l2 = Double.doubleToRawLongBits(d2);
        long l3 = l2 & Long.MIN_VALUE;
        int n3 = (int)(l2 >>> 52) & 2047;
        long l4 = l2 & 0xFFFFFFFFFFFFFL;
        int n4 = n3 + n2;
        if (n2 < 0) {
            if (n4 > 0) {
                return Double.longBitsToDouble(l3 | (long)n4 << 52 | l4);
            }
            if (n4 > -53) {
                long l5 = (l4 |= 0x10000000000000L) & 1 << - n4;
                l4 >>>= 1 - n4;
                if (l5 != 0) {
                    ++l4;
                }
                return Double.longBitsToDouble(l3 | l4);
            }
            return l3 == 0 ? 0.0 : -0.0;
        }
        if (n3 == 0) {
            while (l4 >>> 52 != 1) {
                l4 <<= 1;
                --n4;
            }
            l4 &= 0xFFFFFFFFFFFFFL;
            if (++n4 < 2047) {
                return Double.longBitsToDouble(l3 | (long)n4 << 52 | l4);
            }
            return l3 == 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        if (n4 < 2047) {
            return Double.longBitsToDouble(l3 | (long)n4 << 52 | l4);
        }
        return l3 == 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }

    public static double floor(double d2) {
        if (d2 != d2) {
            return d2;
        }
        if (d2 >= 4.503599627370496E15 || d2 <= -4.503599627370496E15) {
            return d2;
        }
        long l2 = (long)d2;
        if (d2 < 0.0 && (double)l2 != d2) {
            --l2;
        }
        if (l2 == 0) {
            return d2 * (double)l2;
        }
        return l2;
    }

    public static double ceil(double d2) {
        if (d2 != d2) {
            return d2;
        }
        double d3 = FastMath.floor(d2);
        if (d3 == d2) {
            return d3;
        }
        if ((d3 += 1.0) == 0.0) {
            return d2 * d3;
        }
        return d3;
    }

    public static double hypot(double d2, double d3) {
        int n2;
        if (Double.isInfinite(d2) || Double.isInfinite(d3)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(d2) || Double.isNaN(d3)) {
            return Double.NaN;
        }
        int n3 = FastMath.getExponent(d2);
        if (n3 > (n2 = FastMath.getExponent(d3)) + 27) {
            return FastMath.abs(d2);
        }
        if (n2 > n3 + 27) {
            return FastMath.abs(d3);
        }
        int n4 = (n3 + n2) / 2;
        double d4 = FastMath.scalb(d2, - n4);
        double d5 = FastMath.scalb(d3, - n4);
        double d6 = FastMath.sqrt(d4 * d4 + d5 * d5);
        return FastMath.scalb(d6, n4);
    }

    public static int getExponent(double d2) {
        return (int)(Double.doubleToRawLongBits(d2) >>> 52 & 2047) - 1023;
    }

    private static class CodyWaite {
        private final int finalK;
        private final double finalRemA;
        private final double finalRemB;

        CodyWaite(double d2) {
            double d3;
            double d4;
            int n2 = (int)(d2 * 0.6366197723675814);
            do {
                double d5 = (double)(- n2) * 1.570796251296997;
                d4 = d2 + d5;
                d3 = - d4 - d2 - d5;
                d5 = (double)(- n2) * 7.549789948768648E-8;
                double d6 = d4;
                d4 = d5 + d6;
                d3 += - d4 - d6 - d5;
                d5 = (double)(- n2) * 6.123233995736766E-17;
                d6 = d4;
                d4 = d5 + d6;
                d3 += - d4 - d6 - d5;
                if (d4 > 0.0) break;
                --n2;
            } while (true);
            this.finalK = n2;
            this.finalRemA = d4;
            this.finalRemB = d3;
        }

        int getK() {
            return this.finalK;
        }

        double getRemA() {
            return this.finalRemA;
        }

        double getRemB() {
            return this.finalRemB;
        }
    }

}

