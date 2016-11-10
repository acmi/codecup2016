/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.axes;

import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.axes.AttributeIterator;
import org.apache.xpath.axes.AxesWalker;
import org.apache.xpath.axes.ChildIterator;
import org.apache.xpath.axes.ChildTestIterator;
import org.apache.xpath.axes.DescendantIterator;
import org.apache.xpath.axes.FilterExprWalker;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.OneStepIterator;
import org.apache.xpath.axes.OneStepIteratorForward;
import org.apache.xpath.axes.ReverseAxesWalker;
import org.apache.xpath.axes.SelfIteratorNoPredicate;
import org.apache.xpath.axes.WalkingIterator;
import org.apache.xpath.axes.WalkingIteratorSorted;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;

public class WalkerFactory {
    static AxesWalker loadWalkers(WalkingIterator walkingIterator, Compiler compiler, int n2, int n3) throws TransformerException {
        int n4;
        AxesWalker axesWalker = null;
        AxesWalker axesWalker2 = null;
        int n5 = WalkerFactory.analyze(compiler, n2, n3);
        while (-1 != (n4 = compiler.getOp(n2))) {
            AxesWalker axesWalker3 = WalkerFactory.createDefaultWalker(compiler, n2, walkingIterator, n5);
            axesWalker3.init(compiler, n2, n4);
            axesWalker3.exprSetParent(walkingIterator);
            if (null == axesWalker) {
                axesWalker = axesWalker3;
            } else {
                axesWalker2.setNextWalker(axesWalker3);
                axesWalker3.setPrevWalker(axesWalker2);
            }
            axesWalker2 = axesWalker3;
            if ((n2 = compiler.getNextStepPos(n2)) >= 0) continue;
        }
        return axesWalker;
    }

    public static boolean isSet(int n2, int n3) {
        return 0 != (n2 & n3);
    }

    public static DTMIterator newDTMIterator(Compiler compiler, int n2, boolean bl) throws TransformerException {
        int n3 = OpMap.getFirstChildPos(n2);
        int n4 = WalkerFactory.analyze(compiler, n3, 0);
        boolean bl2 = WalkerFactory.isOneStep(n4);
        LocPathIterator locPathIterator = bl2 && WalkerFactory.walksSelfOnly(n4) && WalkerFactory.isWild(n4) && !WalkerFactory.hasPredicate(n4) ? new SelfIteratorNoPredicate(compiler, n2, n4) : (WalkerFactory.walksChildrenOnly(n4) && bl2 ? (WalkerFactory.isWild(n4) && !WalkerFactory.hasPredicate(n4) ? new ChildIterator(compiler, n2, n4) : new ChildTestIterator(compiler, n2, n4)) : (bl2 && WalkerFactory.walksAttributes(n4) ? new AttributeIterator(compiler, n2, n4) : (bl2 && !WalkerFactory.walksFilteredList(n4) ? (!WalkerFactory.walksNamespaces(n4) && (WalkerFactory.walksInDocOrder(n4) || WalkerFactory.isSet(n4, 4194304)) ? new OneStepIteratorForward(compiler, n2, n4) : new OneStepIterator(compiler, n2, n4)) : (WalkerFactory.isOptimizableForDescendantIterator(compiler, n3, 0) ? new DescendantIterator(compiler, n2, n4) : (WalkerFactory.isNaturalDocOrder(compiler, n3, 0, n4) ? new WalkingIterator(compiler, n2, n4, true) : new WalkingIteratorSorted(compiler, n2, n4, true))))));
        if (locPathIterator instanceof LocPathIterator) {
            ((LocPathIterator)locPathIterator).setIsTopLevel(bl);
        }
        return locPathIterator;
    }

    public static int getAxisFromStep(Compiler compiler, int n2) throws TransformerException {
        int n3 = compiler.getOp(n2);
        switch (n3) {
            case 43: {
                return 6;
            }
            case 44: {
                return 7;
            }
            case 46: {
                return 11;
            }
            case 47: {
                return 12;
            }
            case 45: {
                return 10;
            }
            case 49: {
                return 9;
            }
            case 37: {
                return 0;
            }
            case 38: {
                return 1;
            }
            case 39: {
                return 2;
            }
            case 50: {
                return 19;
            }
            case 40: {
                return 3;
            }
            case 42: {
                return 5;
            }
            case 41: {
                return 4;
            }
            case 48: {
                return 13;
            }
            case 22: 
            case 23: 
            case 24: 
            case 25: {
                return 20;
            }
        }
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(n3)}));
    }

    public static int getAnalysisBitFromAxes(int n2) {
        switch (n2) {
            case 0: {
                return 8192;
            }
            case 1: {
                return 16384;
            }
            case 2: {
                return 32768;
            }
            case 3: {
                return 65536;
            }
            case 4: {
                return 131072;
            }
            case 5: {
                return 262144;
            }
            case 6: {
                return 524288;
            }
            case 7: {
                return 1048576;
            }
            case 8: 
            case 9: {
                return 2097152;
            }
            case 10: {
                return 4194304;
            }
            case 11: {
                return 8388608;
            }
            case 12: {
                return 16777216;
            }
            case 13: {
                return 33554432;
            }
            case 14: {
                return 262144;
            }
            case 16: 
            case 17: 
            case 18: {
                return 536870912;
            }
            case 19: {
                return 134217728;
            }
            case 20: {
                return 67108864;
            }
        }
        return 67108864;
    }

    static boolean functionProximateOrContainsProximate(Compiler compiler, int n2) {
        int n3 = n2 + compiler.getOp(n2 + 1) - 1;
        n2 = OpMap.getFirstChildPos(n2);
        int n4 = compiler.getOp(n2);
        switch (n4) {
            case 1: 
            case 2: {
                return true;
            }
        }
        int n5 = 0;
        int n6 = ++n2;
        while (n6 < n3) {
            int n7 = n6 + 2;
            int n8 = compiler.getOp(n7);
            boolean bl = WalkerFactory.isProximateInnerExpr(compiler, n7);
            if (bl) {
                return true;
            }
            n6 = compiler.getNextOpPos(n6);
            ++n5;
        }
        return false;
    }

    static boolean isProximateInnerExpr(Compiler compiler, int n2) {
        int n3 = compiler.getOp(n2);
        int n4 = n2 + 2;
        switch (n3) {
            case 26: {
                if (!WalkerFactory.isProximateInnerExpr(compiler, n4)) break;
                return true;
            }
            case 21: 
            case 22: 
            case 27: 
            case 28: {
                break;
            }
            case 25: {
                boolean bl = WalkerFactory.functionProximateOrContainsProximate(compiler, n2);
                if (!bl) break;
                return true;
            }
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: {
                int n5 = OpMap.getFirstChildPos(n3);
                int n6 = compiler.getNextOpPos(n5);
                boolean bl = WalkerFactory.isProximateInnerExpr(compiler, n5);
                if (bl) {
                    return true;
                }
                bl = WalkerFactory.isProximateInnerExpr(compiler, n6);
                if (!bl) break;
                return true;
            }
            default: {
                return true;
            }
        }
        return false;
    }

    public static boolean mightBeProximate(Compiler compiler, int n2, int n3) throws TransformerException {
        boolean bl = false;
        switch (n3) {
            int n4;
            case 22: 
            case 23: 
            case 24: 
            case 25: {
                n4 = compiler.getArgLength(n2);
                break;
            }
            default: {
                n4 = compiler.getArgLengthOfStep(n2);
            }
        }
        int n5 = compiler.getFirstPredicateOpPos(n2);
        int n6 = 0;
        while (29 == compiler.getOp(n5)) {
            ++n6;
            int n7 = n5 + 2;
            int n8 = compiler.getOp(n7);
            switch (n8) {
                boolean bl2;
                case 22: {
                    return true;
                }
                case 28: {
                    break;
                }
                case 19: 
                case 27: {
                    return true;
                }
                case 25: {
                    bl2 = WalkerFactory.functionProximateOrContainsProximate(compiler, n7);
                    if (!bl2) break;
                    return true;
                }
                case 5: 
                case 6: 
                case 7: 
                case 8: 
                case 9: {
                    int n9 = OpMap.getFirstChildPos(n7);
                    int n10 = compiler.getNextOpPos(n9);
                    bl2 = WalkerFactory.isProximateInnerExpr(compiler, n9);
                    if (bl2) {
                        return true;
                    }
                    bl2 = WalkerFactory.isProximateInnerExpr(compiler, n10);
                    if (!bl2) break;
                    return true;
                }
                default: {
                    return true;
                }
            }
            n5 = compiler.getNextOpPos(n5);
        }
        return bl;
    }

    private static boolean isOptimizableForDescendantIterator(Compiler compiler, int n2, int n3) throws TransformerException {
        int n4;
        int n5 = 0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        int n6 = 1033;
        while (-1 != (n4 = compiler.getOp(n2))) {
            if (n6 != 1033 && n6 != 35) {
                return false;
            }
            if (++n5 > 3) {
                return false;
            }
            boolean bl4 = WalkerFactory.mightBeProximate(compiler, n2, n4);
            if (bl4) {
                return false;
            }
            switch (n4) {
                case 22: 
                case 23: 
                case 24: 
                case 25: 
                case 37: 
                case 38: 
                case 39: 
                case 43: 
                case 44: 
                case 45: 
                case 46: 
                case 47: 
                case 49: 
                case 51: 
                case 52: 
                case 53: {
                    return false;
                }
                case 50: {
                    if (1 == n5) break;
                    return false;
                }
                case 40: {
                    if (bl3 || bl && bl2) break;
                    return false;
                }
                case 42: {
                    bl3 = true;
                }
                case 41: {
                    if (3 == n5) {
                        return false;
                    }
                    bl = true;
                    break;
                }
                case 48: {
                    if (1 != n5) {
                        return false;
                    }
                    bl2 = true;
                    break;
                }
                default: {
                    throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(n4)}));
                }
            }
            n6 = compiler.getStepTestType(n2);
            int n7 = compiler.getNextStepPos(n2);
            if (n7 < 0) break;
            if (-1 != compiler.getOp(n7) && compiler.countPredicates(n2) > 0) {
                return false;
            }
            n2 = n7;
        }
        return true;
    }

    private static int analyze(Compiler compiler, int n2, int n3) throws TransformerException {
        int n4;
        int n5 = 0;
        int n6 = 0;
        while (-1 != (n4 = compiler.getOp(n2))) {
            ++n5;
            boolean bl = WalkerFactory.analyzePredicate(compiler, n2, n4);
            if (bl) {
                n6 |= 4096;
            }
            switch (n4) {
                case 22: 
                case 23: 
                case 24: 
                case 25: {
                    n6 |= 67108864;
                    break;
                }
                case 50: {
                    n6 |= 134217728;
                    break;
                }
                case 37: {
                    n6 |= 8192;
                    break;
                }
                case 38: {
                    n6 |= 16384;
                    break;
                }
                case 39: {
                    n6 |= 32768;
                    break;
                }
                case 49: {
                    n6 |= 2097152;
                    break;
                }
                case 40: {
                    n6 |= 65536;
                    break;
                }
                case 41: {
                    n6 |= 131072;
                    break;
                }
                case 42: {
                    if (2 == n5 && 134217728 == n6) {
                        n6 |= 536870912;
                    }
                    n6 |= 262144;
                    break;
                }
                case 43: {
                    n6 |= 524288;
                    break;
                }
                case 44: {
                    n6 |= 1048576;
                    break;
                }
                case 46: {
                    n6 |= 8388608;
                    break;
                }
                case 47: {
                    n6 |= 16777216;
                    break;
                }
                case 45: {
                    n6 |= 4194304;
                    break;
                }
                case 48: {
                    n6 |= 33554432;
                    break;
                }
                case 51: {
                    n6 |= -2147450880;
                    break;
                }
                case 52: {
                    n6 |= -2147475456;
                    break;
                }
                case 53: {
                    n6 |= -2143289344;
                    break;
                }
                default: {
                    throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(n4)}));
                }
            }
            if (1033 == compiler.getOp(n2 + 3)) {
                n6 |= 1073741824;
            }
            if ((n2 = compiler.getNextStepPos(n2)) >= 0) continue;
            break;
        }
        return n6 |= n5 & 255;
    }

    static boolean analyzePredicate(Compiler compiler, int n2, int n3) throws TransformerException {
        switch (n3) {
            case 22: 
            case 23: 
            case 24: 
            case 25: {
                int n4 = compiler.getArgLength(n2);
                break;
            }
            default: {
                int n5 = compiler.getArgLengthOfStep(n2);
            }
        }
        int n6 = compiler.getFirstPredicateOpPos(n2);
        int n7 = compiler.countPredicates(n6);
        return n7 > 0;
    }

    private static AxesWalker createDefaultWalker(Compiler compiler, int n2, WalkingIterator walkingIterator, int n3) {
        AxesWalker axesWalker = null;
        int n4 = compiler.getOp(n2);
        boolean bl = false;
        int n5 = n3 & 255;
        boolean bl2 = true;
        switch (n4) {
            case 22: 
            case 23: 
            case 24: 
            case 25: {
                bl2 = false;
                axesWalker = new FilterExprWalker(walkingIterator);
                bl = true;
                break;
            }
            case 50: {
                axesWalker = new AxesWalker(walkingIterator, 19);
                break;
            }
            case 37: {
                bl2 = false;
                axesWalker = new ReverseAxesWalker(walkingIterator, 0);
                break;
            }
            case 38: {
                bl2 = false;
                axesWalker = new ReverseAxesWalker(walkingIterator, 1);
                break;
            }
            case 39: {
                axesWalker = new AxesWalker(walkingIterator, 2);
                break;
            }
            case 49: {
                axesWalker = new AxesWalker(walkingIterator, 9);
                break;
            }
            case 40: {
                axesWalker = new AxesWalker(walkingIterator, 3);
                break;
            }
            case 41: {
                bl2 = false;
                axesWalker = new AxesWalker(walkingIterator, 4);
                break;
            }
            case 42: {
                bl2 = false;
                axesWalker = new AxesWalker(walkingIterator, 5);
                break;
            }
            case 43: {
                bl2 = false;
                axesWalker = new AxesWalker(walkingIterator, 6);
                break;
            }
            case 44: {
                bl2 = false;
                axesWalker = new AxesWalker(walkingIterator, 7);
                break;
            }
            case 46: {
                bl2 = false;
                axesWalker = new ReverseAxesWalker(walkingIterator, 11);
                break;
            }
            case 47: {
                bl2 = false;
                axesWalker = new ReverseAxesWalker(walkingIterator, 12);
                break;
            }
            case 45: {
                bl2 = false;
                axesWalker = new ReverseAxesWalker(walkingIterator, 10);
                break;
            }
            case 48: {
                axesWalker = new AxesWalker(walkingIterator, 13);
                break;
            }
            default: {
                throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(n4)}));
            }
        }
        if (bl) {
            axesWalker.initNodeTest(-1);
        } else {
            int n6 = compiler.getWhatToShow(n2);
            if (0 == (n6 & 4163) || n6 == -1) {
                axesWalker.initNodeTest(n6);
            } else {
                axesWalker.initNodeTest(n6, compiler.getStepNS(n2), compiler.getStepLocalName(n2));
            }
        }
        return axesWalker;
    }

    public static boolean hasPredicate(int n2) {
        return 0 != (n2 & 4096);
    }

    public static boolean isWild(int n2) {
        return 0 != (n2 & 1073741824);
    }

    public static boolean walksAttributes(int n2) {
        return 0 != (n2 & 32768);
    }

    public static boolean walksNamespaces(int n2) {
        return 0 != (n2 & 2097152);
    }

    public static boolean walksChildren(int n2) {
        return 0 != (n2 & 65536);
    }

    public static boolean walksDescendants(int n2) {
        return WalkerFactory.isSet(n2, 393216);
    }

    public static boolean walksSubtree(int n2) {
        return WalkerFactory.isSet(n2, 458752);
    }

    public static boolean walksSubtreeOnlyMaybeAbsolute(int n2) {
        return WalkerFactory.walksSubtree(n2) && !WalkerFactory.walksExtraNodes(n2) && !WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2);
    }

    public static boolean walksFilteredList(int n2) {
        return WalkerFactory.isSet(n2, 67108864);
    }

    public static boolean walksInDocOrder(int n2) {
        return (WalkerFactory.walksSubtreeOnlyMaybeAbsolute(n2) || WalkerFactory.walksExtraNodesOnly(n2) || WalkerFactory.walksFollowingOnlyMaybeAbsolute(n2)) && !WalkerFactory.isSet(n2, 67108864);
    }

    public static boolean walksFollowingOnlyMaybeAbsolute(int n2) {
        return WalkerFactory.isSet(n2, 35127296) && !WalkerFactory.walksSubtree(n2) && !WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2);
    }

    public static boolean walksUp(int n2) {
        return WalkerFactory.isSet(n2, 4218880);
    }

    public static boolean walksSideways(int n2) {
        return WalkerFactory.isSet(n2, 26738688);
    }

    public static boolean walksExtraNodes(int n2) {
        return WalkerFactory.isSet(n2, 2129920);
    }

    public static boolean walksExtraNodesOnly(int n2) {
        return WalkerFactory.walksExtraNodes(n2) && !WalkerFactory.isSet(n2, 33554432) && !WalkerFactory.walksSubtree(n2) && !WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2) && !WalkerFactory.isAbsolute(n2);
    }

    public static boolean isAbsolute(int n2) {
        return WalkerFactory.isSet(n2, 201326592);
    }

    public static boolean walksChildrenOnly(int n2) {
        return WalkerFactory.walksChildren(n2) && !WalkerFactory.isSet(n2, 33554432) && !WalkerFactory.walksExtraNodes(n2) && !WalkerFactory.walksDescendants(n2) && !WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2) && (!WalkerFactory.isAbsolute(n2) || WalkerFactory.isSet(n2, 134217728));
    }

    public static boolean walksChildrenAndExtraAndSelfOnly(int n2) {
        return WalkerFactory.walksChildren(n2) && !WalkerFactory.walksDescendants(n2) && !WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2) && (!WalkerFactory.isAbsolute(n2) || WalkerFactory.isSet(n2, 134217728));
    }

    public static boolean walksDescendantsAndExtraAndSelfOnly(int n2) {
        return !WalkerFactory.walksChildren(n2) && WalkerFactory.walksDescendants(n2) && !WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2) && (!WalkerFactory.isAbsolute(n2) || WalkerFactory.isSet(n2, 134217728));
    }

    public static boolean walksSelfOnly(int n2) {
        return WalkerFactory.isSet(n2, 33554432) && !WalkerFactory.walksSubtree(n2) && !WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2) && !WalkerFactory.isAbsolute(n2);
    }

    public static boolean walksUpOnly(int n2) {
        return !WalkerFactory.walksSubtree(n2) && WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2) && !WalkerFactory.isAbsolute(n2);
    }

    public static boolean walksDownOnly(int n2) {
        return WalkerFactory.walksSubtree(n2) && !WalkerFactory.walksUp(n2) && !WalkerFactory.walksSideways(n2) && !WalkerFactory.isAbsolute(n2);
    }

    public static boolean canSkipSubtrees(int n2) {
        return WalkerFactory.isSet(n2, 65536) | WalkerFactory.walksSideways(n2);
    }

    public static boolean canCrissCross(int n2) {
        if (WalkerFactory.walksSelfOnly(n2)) {
            return false;
        }
        if (WalkerFactory.walksDownOnly(n2) && !WalkerFactory.canSkipSubtrees(n2)) {
            return false;
        }
        if (WalkerFactory.walksChildrenAndExtraAndSelfOnly(n2)) {
            return false;
        }
        if (WalkerFactory.walksDescendantsAndExtraAndSelfOnly(n2)) {
            return false;
        }
        if (WalkerFactory.walksUpOnly(n2)) {
            return false;
        }
        if (WalkerFactory.walksExtraNodesOnly(n2)) {
            return false;
        }
        if (WalkerFactory.walksSubtree(n2) && (WalkerFactory.walksSideways(n2) || WalkerFactory.walksUp(n2) || WalkerFactory.canSkipSubtrees(n2))) {
            return true;
        }
        return false;
    }

    public static boolean isNaturalDocOrder(int n2) {
        if (WalkerFactory.canCrissCross(n2) || WalkerFactory.isSet(n2, 2097152) || WalkerFactory.walksFilteredList(n2)) {
            return false;
        }
        if (WalkerFactory.walksInDocOrder(n2)) {
            return true;
        }
        return false;
    }

    private static boolean isNaturalDocOrder(Compiler compiler, int n2, int n3, int n4) throws TransformerException {
        int n5;
        if (WalkerFactory.canCrissCross(n4)) {
            return false;
        }
        if (WalkerFactory.isSet(n4, 2097152)) {
            return false;
        }
        if (WalkerFactory.isSet(n4, 1572864) && WalkerFactory.isSet(n4, 25165824)) {
            return false;
        }
        int n6 = 0;
        boolean bl = false;
        int n7 = 0;
        while (-1 != (n5 = compiler.getOp(n2))) {
            int n8;
            ++n6;
            switch (n5) {
                case 39: 
                case 51: {
                    if (bl) {
                        return false;
                    }
                    String string = compiler.getStepLocalName(n2);
                    if (!string.equals("*")) break;
                    bl = true;
                    break;
                }
                case 22: 
                case 23: 
                case 24: 
                case 25: 
                case 37: 
                case 38: 
                case 41: 
                case 42: 
                case 43: 
                case 44: 
                case 45: 
                case 46: 
                case 47: 
                case 49: 
                case 52: 
                case 53: {
                    if (n7 > 0) {
                        return false;
                    }
                    ++n7;
                }
                case 40: 
                case 48: 
                case 50: {
                    if (!bl) break;
                    return false;
                }
                default: {
                    throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[]{Integer.toString(n5)}));
                }
            }
            if ((n8 = compiler.getNextStepPos(n2)) < 0) break;
            n2 = n8;
        }
        return true;
    }

    public static boolean isOneStep(int n2) {
        return (n2 & 255) == 1;
    }
}

