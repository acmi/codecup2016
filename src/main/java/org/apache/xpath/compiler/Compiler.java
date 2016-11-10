/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.compiler;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.ObjectVector;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xpath.Expression;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.UnionPathIterator;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.compiler.OpMap;
import org.apache.xpath.functions.FuncExtFunction;
import org.apache.xpath.functions.FuncExtFunctionAvailable;
import org.apache.xpath.functions.Function;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XString;
import org.apache.xpath.operations.And;
import org.apache.xpath.operations.Bool;
import org.apache.xpath.operations.Div;
import org.apache.xpath.operations.Equals;
import org.apache.xpath.operations.Gt;
import org.apache.xpath.operations.Gte;
import org.apache.xpath.operations.Lt;
import org.apache.xpath.operations.Lte;
import org.apache.xpath.operations.Minus;
import org.apache.xpath.operations.Mod;
import org.apache.xpath.operations.Mult;
import org.apache.xpath.operations.Neg;
import org.apache.xpath.operations.NotEquals;
import org.apache.xpath.operations.Number;
import org.apache.xpath.operations.Operation;
import org.apache.xpath.operations.Or;
import org.apache.xpath.operations.Plus;
import org.apache.xpath.operations.String;
import org.apache.xpath.operations.UnaryOperation;
import org.apache.xpath.operations.Variable;
import org.apache.xpath.patterns.FunctionPattern;
import org.apache.xpath.patterns.StepPattern;
import org.apache.xpath.patterns.UnionPattern;

public class Compiler
extends OpMap {
    private int locPathDepth = -1;
    private static long s_nextMethodId = 0;
    private PrefixResolver m_currentPrefixResolver = null;
    ErrorListener m_errorHandler;
    SourceLocator m_locator;
    private FunctionTable m_functionTable;

    public Compiler(ErrorListener errorListener, SourceLocator sourceLocator, FunctionTable functionTable) {
        this.m_errorHandler = errorListener;
        this.m_locator = sourceLocator;
        this.m_functionTable = functionTable;
    }

    public Compiler() {
        this.m_errorHandler = null;
        this.m_locator = null;
    }

    public Expression compile(int n2) throws TransformerException {
        int n3 = this.getOp(n2);
        Expression expression = null;
        switch (n3) {
            case 1: {
                expression = this.compile(n2 + 2);
                break;
            }
            case 2: {
                expression = this.or(n2);
                break;
            }
            case 3: {
                expression = this.and(n2);
                break;
            }
            case 4: {
                expression = this.notequals(n2);
                break;
            }
            case 5: {
                expression = this.equals(n2);
                break;
            }
            case 6: {
                expression = this.lte(n2);
                break;
            }
            case 7: {
                expression = this.lt(n2);
                break;
            }
            case 8: {
                expression = this.gte(n2);
                break;
            }
            case 9: {
                expression = this.gt(n2);
                break;
            }
            case 10: {
                expression = this.plus(n2);
                break;
            }
            case 11: {
                expression = this.minus(n2);
                break;
            }
            case 12: {
                expression = this.mult(n2);
                break;
            }
            case 13: {
                expression = this.div(n2);
                break;
            }
            case 14: {
                expression = this.mod(n2);
                break;
            }
            case 16: {
                expression = this.neg(n2);
                break;
            }
            case 17: {
                expression = this.string(n2);
                break;
            }
            case 18: {
                expression = this.bool(n2);
                break;
            }
            case 19: {
                expression = this.number(n2);
                break;
            }
            case 20: {
                expression = this.union(n2);
                break;
            }
            case 21: {
                expression = this.literal(n2);
                break;
            }
            case 22: {
                expression = this.variable(n2);
                break;
            }
            case 23: {
                expression = this.group(n2);
                break;
            }
            case 27: {
                expression = this.numberlit(n2);
                break;
            }
            case 26: {
                expression = this.arg(n2);
                break;
            }
            case 24: {
                expression = this.compileExtension(n2);
                break;
            }
            case 25: {
                expression = this.compileFunction(n2);
                break;
            }
            case 28: {
                expression = this.locationPath(n2);
                break;
            }
            case 29: {
                expression = null;
                break;
            }
            case 30: {
                expression = this.matchPattern(n2 + 2);
                break;
            }
            case 31: {
                expression = this.locationPathPattern(n2);
                break;
            }
            case 15: {
                this.error("ER_UNKNOWN_OPCODE", new Object[]{"quo"});
                break;
            }
            default: {
                this.error("ER_UNKNOWN_OPCODE", new Object[]{Integer.toString(this.getOp(n2))});
            }
        }
        return expression;
    }

    private Expression compileOperation(Operation operation, int n2) throws TransformerException {
        int n3 = Compiler.getFirstChildPos(n2);
        int n4 = this.getNextOpPos(n3);
        operation.setLeftRight(this.compile(n3), this.compile(n4));
        return operation;
    }

    private Expression compileUnary(UnaryOperation unaryOperation, int n2) throws TransformerException {
        int n3 = Compiler.getFirstChildPos(n2);
        unaryOperation.setRight(this.compile(n3));
        return unaryOperation;
    }

    protected Expression or(int n2) throws TransformerException {
        return this.compileOperation(new Or(), n2);
    }

    protected Expression and(int n2) throws TransformerException {
        return this.compileOperation(new And(), n2);
    }

    protected Expression notequals(int n2) throws TransformerException {
        return this.compileOperation(new NotEquals(), n2);
    }

    protected Expression equals(int n2) throws TransformerException {
        return this.compileOperation(new Equals(), n2);
    }

    protected Expression lte(int n2) throws TransformerException {
        return this.compileOperation(new Lte(), n2);
    }

    protected Expression lt(int n2) throws TransformerException {
        return this.compileOperation(new Lt(), n2);
    }

    protected Expression gte(int n2) throws TransformerException {
        return this.compileOperation(new Gte(), n2);
    }

    protected Expression gt(int n2) throws TransformerException {
        return this.compileOperation(new Gt(), n2);
    }

    protected Expression plus(int n2) throws TransformerException {
        return this.compileOperation(new Plus(), n2);
    }

    protected Expression minus(int n2) throws TransformerException {
        return this.compileOperation(new Minus(), n2);
    }

    protected Expression mult(int n2) throws TransformerException {
        return this.compileOperation(new Mult(), n2);
    }

    protected Expression div(int n2) throws TransformerException {
        return this.compileOperation(new Div(), n2);
    }

    protected Expression mod(int n2) throws TransformerException {
        return this.compileOperation(new Mod(), n2);
    }

    protected Expression neg(int n2) throws TransformerException {
        return this.compileUnary(new Neg(), n2);
    }

    protected Expression string(int n2) throws TransformerException {
        return this.compileUnary(new String(), n2);
    }

    protected Expression bool(int n2) throws TransformerException {
        return this.compileUnary(new Bool(), n2);
    }

    protected Expression number(int n2) throws TransformerException {
        return this.compileUnary(new Number(), n2);
    }

    protected Expression literal(int n2) {
        n2 = Compiler.getFirstChildPos(n2);
        return (XString)this.getTokenQueue().elementAt(this.getOp(n2));
    }

    protected Expression numberlit(int n2) {
        n2 = Compiler.getFirstChildPos(n2);
        return (XNumber)this.getTokenQueue().elementAt(this.getOp(n2));
    }

    protected Expression variable(int n2) throws TransformerException {
        Variable variable = new Variable();
        int n3 = this.getOp(n2 = Compiler.getFirstChildPos(n2));
        java.lang.String string = -2 == n3 ? null : (java.lang.String)this.getTokenQueue().elementAt(n3);
        java.lang.String string2 = (java.lang.String)this.getTokenQueue().elementAt(this.getOp(n2 + 1));
        QName qName = new QName(string, string2);
        variable.setQName(qName);
        return variable;
    }

    protected Expression group(int n2) throws TransformerException {
        return this.compile(n2 + 2);
    }

    protected Expression arg(int n2) throws TransformerException {
        return this.compile(n2 + 2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected Expression union(int n2) throws TransformerException {
        ++this.locPathDepth;
        try {
            LocPathIterator locPathIterator = UnionPathIterator.createUnionIterator(this, n2);
            return locPathIterator;
        }
        finally {
            --this.locPathDepth;
        }
    }

    public int getLocationPathDepth() {
        return this.locPathDepth;
    }

    FunctionTable getFunctionTable() {
        return this.m_functionTable;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Expression locationPath(int n2) throws TransformerException {
        ++this.locPathDepth;
        try {
            DTMIterator dTMIterator = WalkerFactory.newDTMIterator(this, n2, this.locPathDepth == 0);
            Expression expression = (Expression)((Object)dTMIterator);
            return expression;
        }
        finally {
            --this.locPathDepth;
        }
    }

    public Expression predicate(int n2) throws TransformerException {
        return this.compile(n2 + 2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected Expression matchPattern(int n2) throws TransformerException {
        ++this.locPathDepth;
        try {
            int n3 = n2;
            int n4 = 0;
            while (this.getOp(n3) == 31) {
                n3 = this.getNextOpPos(n3);
                ++n4;
            }
            if (n4 == 1) {
                Expression expression = this.compile(n2);
                return expression;
            }
            UnionPattern unionPattern = new UnionPattern();
            StepPattern[] arrstepPattern = new StepPattern[n4];
            n4 = 0;
            while (this.getOp(n2) == 31) {
                n3 = this.getNextOpPos(n2);
                arrstepPattern[n4] = (StepPattern)this.compile(n2);
                n2 = n3;
                ++n4;
            }
            unionPattern.setPatterns(arrstepPattern);
            UnionPattern unionPattern2 = unionPattern;
            return unionPattern2;
        }
        finally {
            --this.locPathDepth;
        }
    }

    public Expression locationPathPattern(int n2) throws TransformerException {
        n2 = Compiler.getFirstChildPos(n2);
        return this.stepPattern(n2, 0, null);
    }

    public int getWhatToShow(int n2) {
        int n3 = this.getOp(n2);
        int n4 = this.getOp(n2 + 3);
        switch (n4) {
            case 1030: {
                return 128;
            }
            case 1031: {
                return 12;
            }
            case 1032: {
                return 64;
            }
            case 1033: {
                switch (n3) {
                    case 49: {
                        return 4096;
                    }
                    case 39: 
                    case 51: {
                        return 2;
                    }
                    case 38: 
                    case 42: 
                    case 48: {
                        return -1;
                    }
                }
                if (this.getOp(0) == 30) {
                    return -1283;
                }
                return -3;
            }
            case 35: {
                return 1280;
            }
            case 1034: {
                return 65536;
            }
            case 34: {
                switch (n3) {
                    case 49: {
                        return 4096;
                    }
                    case 39: 
                    case 51: {
                        return 2;
                    }
                    case 52: 
                    case 53: {
                        return 1;
                    }
                }
                return 1;
            }
        }
        return -1;
    }

    protected StepPattern stepPattern(int n2, int n3, StepPattern stepPattern) throws TransformerException {
        int n4;
        StepPattern stepPattern2;
        int n5 = n2;
        int n6 = this.getOp(n2);
        if (-1 == n6) {
            return null;
        }
        boolean bl = true;
        int n7 = this.getNextOpPos(n2);
        switch (n6) {
            case 25: {
                bl = false;
                n4 = this.getOp(n2 + 1);
                stepPattern2 = new FunctionPattern(this.compileFunction(n2), 10, 3);
                break;
            }
            case 50: {
                bl = false;
                n4 = this.getArgLengthOfStep(n2);
                n2 = Compiler.getFirstChildPosOfStep(n2);
                stepPattern2 = new StepPattern(1280, 10, 3);
                break;
            }
            case 51: {
                n4 = this.getArgLengthOfStep(n2);
                n2 = Compiler.getFirstChildPosOfStep(n2);
                stepPattern2 = new StepPattern(2, this.getStepNS(n5), this.getStepLocalName(n5), 10, 2);
                break;
            }
            case 52: {
                n4 = this.getArgLengthOfStep(n2);
                n2 = Compiler.getFirstChildPosOfStep(n2);
                int n8 = this.getWhatToShow(n5);
                if (1280 == n8) {
                    bl = false;
                }
                stepPattern2 = new StepPattern(this.getWhatToShow(n5), this.getStepNS(n5), this.getStepLocalName(n5), 0, 3);
                break;
            }
            case 53: {
                n4 = this.getArgLengthOfStep(n2);
                n2 = Compiler.getFirstChildPosOfStep(n2);
                stepPattern2 = new StepPattern(this.getWhatToShow(n5), this.getStepNS(n5), this.getStepLocalName(n5), 10, 3);
                break;
            }
            default: {
                this.error("ER_UNKNOWN_MATCH_OPERATION", null);
                return null;
            }
        }
        stepPattern2.setPredicates(this.getCompiledPredicates(n2 + n4));
        if (null != stepPattern) {
            stepPattern2.setRelativePathPattern(stepPattern);
        }
        StepPattern stepPattern3 = this.stepPattern(n7, n3 + 1, stepPattern2);
        return null != stepPattern3 ? stepPattern3 : stepPattern2;
    }

    public Expression[] getCompiledPredicates(int n2) throws TransformerException {
        int n3 = this.countPredicates(n2);
        if (n3 > 0) {
            Expression[] arrexpression = new Expression[n3];
            this.compilePredicates(n2, arrexpression);
            return arrexpression;
        }
        return null;
    }

    public int countPredicates(int n2) throws TransformerException {
        int n3 = 0;
        while (29 == this.getOp(n2)) {
            ++n3;
            n2 = this.getNextOpPos(n2);
        }
        return n3;
    }

    private void compilePredicates(int n2, Expression[] arrexpression) throws TransformerException {
        int n3 = 0;
        while (29 == this.getOp(n2)) {
            arrexpression[n3] = this.predicate(n2);
            n2 = this.getNextOpPos(n2);
            ++n3;
        }
    }

    Expression compileFunction(int n2) throws TransformerException {
        int n3 = n2 + this.getOp(n2 + 1) - 1;
        n2 = Compiler.getFirstChildPos(n2);
        int n4 = this.getOp(n2);
        ++n2;
        if (-1 != n4) {
            Function function = this.m_functionTable.getFunction(n4);
            if (function instanceof FuncExtFunctionAvailable) {
                ((FuncExtFunctionAvailable)function).setFunctionTable(this.m_functionTable);
            }
            function.postCompileStep(this);
            try {
                int n5 = 0;
                int n6 = n2;
                while (n6 < n3) {
                    function.setArg(this.compile(n6), n5);
                    n6 = this.getNextOpPos(n6);
                    ++n5;
                }
                function.checkNumberArgs(n5);
            }
            catch (WrongNumberArgsException wrongNumberArgsException) {
                java.lang.String string = this.m_functionTable.getFunctionName(n4);
                this.m_errorHandler.fatalError(new TransformerException(XSLMessages.createXPATHMessage("ER_ONLY_ALLOWS", new Object[]{string, wrongNumberArgsException.getMessage()}), this.m_locator));
            }
            return function;
        }
        this.error("ER_FUNCTION_TOKEN_NOT_FOUND", null);
        return null;
    }

    private synchronized long getNextMethodId() {
        if (s_nextMethodId == Long.MAX_VALUE) {
            s_nextMethodId = 0;
        }
        return s_nextMethodId++;
    }

    private Expression compileExtension(int n2) throws TransformerException {
        int n3 = n2 + this.getOp(n2 + 1) - 1;
        n2 = Compiler.getFirstChildPos(n2);
        java.lang.String string = (java.lang.String)this.getTokenQueue().elementAt(this.getOp(n2));
        java.lang.String string2 = (java.lang.String)this.getTokenQueue().elementAt(this.getOp(++n2));
        ++n2;
        FuncExtFunction funcExtFunction = new FuncExtFunction(string, string2, java.lang.String.valueOf(this.getNextMethodId()));
        try {
            int n4 = 0;
            while (n2 < n3) {
                int n5 = this.getNextOpPos(n2);
                funcExtFunction.setArg(this.compile(n2), n4);
                n2 = n5;
                ++n4;
            }
        }
        catch (WrongNumberArgsException wrongNumberArgsException) {
            // empty catch block
        }
        return funcExtFunction;
    }

    public void error(java.lang.String string, Object[] arrobject) throws TransformerException {
        java.lang.String string2 = XSLMessages.createXPATHMessage(string, arrobject);
        if (null == this.m_errorHandler) {
            throw new TransformerException(string2, (SAXSourceLocator)this.m_locator);
        }
        this.m_errorHandler.fatalError(new TransformerException(string2, this.m_locator));
    }

    public PrefixResolver getNamespaceContext() {
        return this.m_currentPrefixResolver;
    }
}

