/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import b.a.b;
import b.a.c;
import java.util.Stack;
import java.util.Vector;
import org.apache.xalan.xsltc.compiler.AbsoluteLocationPath;
import org.apache.xalan.xsltc.compiler.AbsolutePathPattern;
import org.apache.xalan.xsltc.compiler.AlternativePattern;
import org.apache.xalan.xsltc.compiler.AncestorPattern;
import org.apache.xalan.xsltc.compiler.BinOpExpr;
import org.apache.xalan.xsltc.compiler.BooleanCall;
import org.apache.xalan.xsltc.compiler.BooleanExpr;
import org.apache.xalan.xsltc.compiler.CastCall;
import org.apache.xalan.xsltc.compiler.CeilingCall;
import org.apache.xalan.xsltc.compiler.ConcatCall;
import org.apache.xalan.xsltc.compiler.ContainsCall;
import org.apache.xalan.xsltc.compiler.CurrentCall;
import org.apache.xalan.xsltc.compiler.DocumentCall;
import org.apache.xalan.xsltc.compiler.ElementAvailableCall;
import org.apache.xalan.xsltc.compiler.EqualityExpr;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.FilterExpr;
import org.apache.xalan.xsltc.compiler.FilterParentPath;
import org.apache.xalan.xsltc.compiler.FloorCall;
import org.apache.xalan.xsltc.compiler.FormatNumberCall;
import org.apache.xalan.xsltc.compiler.FunctionAvailableCall;
import org.apache.xalan.xsltc.compiler.FunctionCall;
import org.apache.xalan.xsltc.compiler.GenerateIdCall;
import org.apache.xalan.xsltc.compiler.IdKeyPattern;
import org.apache.xalan.xsltc.compiler.IdPattern;
import org.apache.xalan.xsltc.compiler.IntExpr;
import org.apache.xalan.xsltc.compiler.KeyCall;
import org.apache.xalan.xsltc.compiler.KeyPattern;
import org.apache.xalan.xsltc.compiler.LangCall;
import org.apache.xalan.xsltc.compiler.LastCall;
import org.apache.xalan.xsltc.compiler.LiteralExpr;
import org.apache.xalan.xsltc.compiler.LocalNameCall;
import org.apache.xalan.xsltc.compiler.LogicalExpr;
import org.apache.xalan.xsltc.compiler.NameCall;
import org.apache.xalan.xsltc.compiler.NamespaceUriCall;
import org.apache.xalan.xsltc.compiler.NotCall;
import org.apache.xalan.xsltc.compiler.NumberCall;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.ParameterRef;
import org.apache.xalan.xsltc.compiler.ParentLocationPath;
import org.apache.xalan.xsltc.compiler.ParentPattern;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.PositionCall;
import org.apache.xalan.xsltc.compiler.Predicate;
import org.apache.xalan.xsltc.compiler.ProcessingInstructionPattern;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.RealExpr;
import org.apache.xalan.xsltc.compiler.RelationalExpr;
import org.apache.xalan.xsltc.compiler.RelativeLocationPath;
import org.apache.xalan.xsltc.compiler.RelativePathPattern;
import org.apache.xalan.xsltc.compiler.RoundCall;
import org.apache.xalan.xsltc.compiler.StartsWithCall;
import org.apache.xalan.xsltc.compiler.Step;
import org.apache.xalan.xsltc.compiler.StepPattern;
import org.apache.xalan.xsltc.compiler.StringCall;
import org.apache.xalan.xsltc.compiler.StringLengthCall;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.UnaryOpExpr;
import org.apache.xalan.xsltc.compiler.UnionPathExpr;
import org.apache.xalan.xsltc.compiler.UnparsedEntityUriCall;
import org.apache.xalan.xsltc.compiler.UnresolvedRef;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.VariableRef;
import org.apache.xalan.xsltc.compiler.VariableRefBase;
import org.apache.xalan.xsltc.compiler.XPathParser;

class CUP$XPathParser$actions {
    private final XPathParser parser;

    CUP$XPathParser$actions(XPathParser xPathParser) {
        this.parser = xPathParser;
    }

    public final b CUP$XPathParser$do_action(int n2, c c2, Stack stack, int n3) throws Exception {
        switch (n2) {
            case 140: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("id");
                b b2 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b2;
            }
            case 139: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("self");
                b b3 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b3;
            }
            case 138: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("preceding-sibling");
                b b4 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b4;
            }
            case 137: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("preceding");
                b b5 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b5;
            }
            case 136: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("parent");
                b b6 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b6;
            }
            case 135: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("namespace");
                b b7 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b7;
            }
            case 134: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("following-sibling");
                b b8 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b8;
            }
            case 133: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("following");
                b b9 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b9;
            }
            case 132: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("decendant-or-self");
                b b10 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b10;
            }
            case 131: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("decendant");
                b b11 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b11;
            }
            case 130: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("child");
                b b12 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b12;
            }
            case 129: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("attribute");
                b b13 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b13;
            }
            case 128: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("ancestor-or-self");
                b b14 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b14;
            }
            case 127: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("child");
                b b15 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b15;
            }
            case 126: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("key");
                b b16 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b16;
            }
            case 125: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("mod");
                b b17 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b17;
            }
            case 124: {
                QName qName = null;
                qName = this.parser.getQNameIgnoreDefaultNs("div");
                b b18 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b18;
            }
            case 123: {
                QName qName = null;
                int n4 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n5 = ((b)stack.elementAt((int)(n3 - 0))).e;
                String string = (String)((b)stack.elementAt((int)(n3 - 0))).f;
                qName = this.parser.getQNameIgnoreDefaultNs(string);
                b b19 = new b(37, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName);
                return b19;
            }
            case 122: {
                QName qName;
                QName qName2 = null;
                int n6 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n7 = ((b)stack.elementAt((int)(n3 - 0))).e;
                qName2 = qName = (QName)((b)stack.elementAt((int)(n3 - 0))).f;
                b b20 = new b(26, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName2);
                return b20;
            }
            case 121: {
                Object var6_24 = null;
                var6_24 = null;
                b b21 = new b(26, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, var6_24);
                return b21;
            }
            case 120: {
                Integer n8 = null;
                n8 = new Integer(7);
                b b22 = new b(25, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n8);
                return b22;
            }
            case 119: {
                Step step = null;
                int n9 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n10 = ((b)stack.elementAt((int)(n3 - 1))).e;
                String string = (String)((b)stack.elementAt((int)(n3 - 1))).f;
                QName qName = this.parser.getQNameIgnoreDefaultNs("name");
                EqualityExpr equalityExpr = new EqualityExpr(0, new NameCall(qName), new LiteralExpr(string));
                Vector<Predicate> vector = new Vector<Predicate>();
                vector.addElement(new Predicate(equalityExpr));
                step = new Step(3, 7, vector);
                b b23 = new b(25, ((b)stack.elementAt((int)(n3 - 3))).d, ((b)stack.elementAt((int)(n3 - 0))).e, step);
                return b23;
            }
            case 118: {
                Integer n11 = null;
                n11 = new Integer(8);
                b b24 = new b(25, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n11);
                return b24;
            }
            case 117: {
                Integer n12 = null;
                n12 = new Integer(3);
                b b25 = new b(25, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n12);
                return b25;
            }
            case 116: {
                Integer n13 = null;
                n13 = new Integer(-1);
                b b26 = new b(25, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n13);
                return b26;
            }
            case 115: {
                Object object;
                Object object2 = null;
                int n14 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n15 = ((b)stack.elementAt((int)(n3 - 0))).e;
                object2 = object = ((b)stack.elementAt((int)(n3 - 0))).f;
                b b27 = new b(25, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, object2);
                return b27;
            }
            case 114: {
                Expression expression;
                Expression expression2 = null;
                int n16 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n17 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression2 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b28 = new b(3, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression2);
                return b28;
            }
            case 113: {
                QName qName;
                QName qName3 = null;
                int n18 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n19 = ((b)stack.elementAt((int)(n3 - 0))).e;
                qName3 = qName = (QName)((b)stack.elementAt((int)(n3 - 0))).f;
                b b29 = new b(39, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName3);
                return b29;
            }
            case 112: {
                QName qName;
                QName qName4 = null;
                int n20 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n21 = ((b)stack.elementAt((int)(n3 - 0))).e;
                qName4 = qName = (QName)((b)stack.elementAt((int)(n3 - 0))).f;
                b b30 = new b(38, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName4);
                return b30;
            }
            case 111: {
                Vector vector = null;
                int n22 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n23 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n24 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n25 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector2 = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                vector2.insertElementAt(expression, 0);
                vector = vector2;
                b b31 = new b(36, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, vector);
                return b31;
            }
            case 110: {
                Vector<Expression> vector = null;
                int n26 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n27 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                Vector<Expression> vector3 = new Vector<Expression>();
                vector3.addElement(expression);
                vector = vector3;
                b b32 = new b(36, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, vector);
                return b32;
            }
            case 109: {
                FunctionCall functionCall = null;
                int n28 = ((b)stack.elementAt((int)(n3 - 3))).d;
                int n29 = ((b)stack.elementAt((int)(n3 - 3))).e;
                QName qName = (QName)((b)stack.elementAt((int)(n3 - 3))).f;
                int n30 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n31 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Vector vector = (Vector)((b)stack.elementAt((int)(n3 - 1))).f;
                if (this.parser.getQNameIgnoreDefaultNs("concat").equals(qName)) {
                    functionCall = new ConcatCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("number").equals(qName)) {
                    functionCall = new NumberCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("document").equals(qName)) {
                    this.parser.setMultiDocument(true);
                    functionCall = new DocumentCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("string").equals(qName)) {
                    functionCall = new StringCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("boolean").equals(qName)) {
                    functionCall = new BooleanCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("name").equals(qName)) {
                    functionCall = new NameCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("generate-id").equals(qName)) {
                    functionCall = new GenerateIdCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("not").equals(qName)) {
                    functionCall = new NotCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("format-number").equals(qName)) {
                    functionCall = new FormatNumberCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("unparsed-entity-uri").equals(qName)) {
                    functionCall = new UnparsedEntityUriCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("key").equals(qName)) {
                    functionCall = new KeyCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("id").equals(qName)) {
                    functionCall = new KeyCall(qName, vector);
                    this.parser.setHasIdCall(true);
                } else if (this.parser.getQNameIgnoreDefaultNs("ceiling").equals(qName)) {
                    functionCall = new CeilingCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("round").equals(qName)) {
                    functionCall = new RoundCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("floor").equals(qName)) {
                    functionCall = new FloorCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("contains").equals(qName)) {
                    functionCall = new ContainsCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("string-length").equals(qName)) {
                    functionCall = new StringLengthCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("starts-with").equals(qName)) {
                    functionCall = new StartsWithCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("function-available").equals(qName)) {
                    functionCall = new FunctionAvailableCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("element-available").equals(qName)) {
                    functionCall = new ElementAvailableCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("local-name").equals(qName)) {
                    functionCall = new LocalNameCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("lang").equals(qName)) {
                    functionCall = new LangCall(qName, vector);
                } else if (this.parser.getQNameIgnoreDefaultNs("namespace-uri").equals(qName)) {
                    functionCall = new NamespaceUriCall(qName, vector);
                } else if (this.parser.getQName("http://xml.apache.org/xalan/xsltc", "xsltc", "cast").equals(qName)) {
                    functionCall = new CastCall(qName, vector);
                } else if (qName.getLocalPart().equals("nodeset") || qName.getLocalPart().equals("node-set")) {
                    this.parser.setCallsNodeset(true);
                    functionCall = new FunctionCall(qName, vector);
                } else {
                    functionCall = new FunctionCall(qName, vector);
                }
                b b33 = new b(16, ((b)stack.elementAt((int)(n3 - 3))).d, ((b)stack.elementAt((int)(n3 - 0))).e, functionCall);
                return b33;
            }
            case 108: {
                Expression expression = null;
                int n32 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n33 = ((b)stack.elementAt((int)(n3 - 2))).e;
                QName qName = (QName)((b)stack.elementAt((int)(n3 - 2))).f;
                expression = this.parser.getQNameIgnoreDefaultNs("current").equals(qName) ? new CurrentCall(qName) : (this.parser.getQNameIgnoreDefaultNs("number").equals(qName) ? new NumberCall(qName, XPathParser.EmptyArgs) : (this.parser.getQNameIgnoreDefaultNs("string").equals(qName) ? new StringCall(qName, XPathParser.EmptyArgs) : (this.parser.getQNameIgnoreDefaultNs("concat").equals(qName) ? new ConcatCall(qName, XPathParser.EmptyArgs) : (this.parser.getQNameIgnoreDefaultNs("true").equals(qName) ? new BooleanExpr(true) : (this.parser.getQNameIgnoreDefaultNs("false").equals(qName) ? new BooleanExpr(false) : (this.parser.getQNameIgnoreDefaultNs("name").equals(qName) ? new NameCall(qName) : (this.parser.getQNameIgnoreDefaultNs("generate-id").equals(qName) ? new GenerateIdCall(qName, XPathParser.EmptyArgs) : (this.parser.getQNameIgnoreDefaultNs("string-length").equals(qName) ? new StringLengthCall(qName, XPathParser.EmptyArgs) : (this.parser.getQNameIgnoreDefaultNs("position").equals(qName) ? new PositionCall(qName) : (this.parser.getQNameIgnoreDefaultNs("last").equals(qName) ? new LastCall(qName) : (this.parser.getQNameIgnoreDefaultNs("local-name").equals(qName) ? new LocalNameCall(qName) : (this.parser.getQNameIgnoreDefaultNs("namespace-uri").equals(qName) ? new NamespaceUriCall(qName) : new FunctionCall(qName, XPathParser.EmptyArgs)))))))))))));
                b b34 = new b(16, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression);
                return b34;
            }
            case 107: {
                VariableRefBase variableRefBase = null;
                int n34 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n35 = ((b)stack.elementAt((int)(n3 - 0))).e;
                QName qName = (QName)((b)stack.elementAt((int)(n3 - 0))).f;
                SyntaxTreeNode syntaxTreeNode = this.parser.lookupName(qName);
                if (syntaxTreeNode != null) {
                    variableRefBase = syntaxTreeNode instanceof Variable ? new VariableRef((Variable)syntaxTreeNode) : (syntaxTreeNode instanceof Param ? new ParameterRef((Param)syntaxTreeNode) : new UnresolvedRef(qName));
                }
                if (syntaxTreeNode == null) {
                    variableRefBase = new UnresolvedRef(qName);
                }
                b b35 = new b(15, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, variableRefBase);
                return b35;
            }
            case 106: {
                Expression expression;
                Expression expression3 = null;
                int n36 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n37 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression3 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b36 = new b(17, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression3);
                return b36;
            }
            case 105: {
                RealExpr realExpr = null;
                int n38 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n39 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Double d2 = (Double)((b)stack.elementAt((int)(n3 - 0))).f;
                realExpr = new RealExpr(d2);
                b b37 = new b(17, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, realExpr);
                return b37;
            }
            case 104: {
                Expression expression = null;
                int n40 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n41 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Long l2 = (Long)((b)stack.elementAt((int)(n3 - 0))).f;
                long l3 = l2;
                expression = l3 < Integer.MIN_VALUE || l3 > Integer.MAX_VALUE ? new RealExpr(l3) : (l2.doubleValue() == 0.0 ? new RealExpr(l2.doubleValue()) : (l2.intValue() == 0 ? new IntExpr(l2.intValue()) : (l2.doubleValue() == 0.0 ? new RealExpr(l2.doubleValue()) : new IntExpr(l2.intValue()))));
                b b38 = new b(17, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression);
                return b38;
            }
            case 103: {
                LiteralExpr literalExpr = null;
                int n42 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n43 = ((b)stack.elementAt((int)(n3 - 0))).e;
                String string = (String)((b)stack.elementAt((int)(n3 - 0))).f;
                String string2 = null;
                int n44 = string.lastIndexOf(58);
                if (n44 > 0) {
                    String string3 = string.substring(0, n44);
                    string2 = this.parser._symbolTable.lookupNamespace(string3);
                }
                literalExpr = string2 == null ? new LiteralExpr(string) : new LiteralExpr(string, string2);
                b b39 = new b(17, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, literalExpr);
                return b39;
            }
            case 102: {
                Expression expression;
                Expression expression4 = null;
                int n45 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n46 = ((b)stack.elementAt((int)(n3 - 1))).e;
                expression4 = expression = (Expression)((b)stack.elementAt((int)(n3 - 1))).f;
                b b40 = new b(17, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression4);
                return b40;
            }
            case 101: {
                Expression expression;
                Expression expression5 = null;
                int n47 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n48 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression5 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b41 = new b(17, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression5);
                return b41;
            }
            case 100: {
                FilterExpr filterExpr = null;
                int n49 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n50 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 1))).f;
                int n51 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n52 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                filterExpr = new FilterExpr(expression, vector);
                b b42 = new b(6, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, filterExpr);
                return b42;
            }
            case 99: {
                Expression expression;
                Expression expression6 = null;
                int n53 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n54 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression6 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b43 = new b(6, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression6);
                return b43;
            }
            case 98: {
                Step step = null;
                step = new Step(10, -1, null);
                b b44 = new b(20, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, step);
                return b44;
            }
            case 97: {
                Step step = null;
                step = new Step(13, -1, null);
                b b45 = new b(20, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, step);
                return b45;
            }
            case 96: {
                Integer n55 = null;
                n55 = new Integer(13);
                b b46 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n55);
                return b46;
            }
            case 95: {
                Integer n56 = null;
                n56 = new Integer(12);
                b b47 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n56);
                return b47;
            }
            case 94: {
                Integer n57 = null;
                n57 = new Integer(11);
                b b48 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n57);
                return b48;
            }
            case 93: {
                Integer n58 = null;
                n58 = new Integer(10);
                b b49 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n58);
                return b49;
            }
            case 92: {
                Integer n59 = null;
                n59 = new Integer(9);
                b b50 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n59);
                return b50;
            }
            case 91: {
                Integer n60 = null;
                n60 = new Integer(7);
                b b51 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n60);
                return b51;
            }
            case 90: {
                Integer n61 = null;
                n61 = new Integer(6);
                b b52 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n61);
                return b52;
            }
            case 89: {
                Integer n62 = null;
                n62 = new Integer(5);
                b b53 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n62);
                return b53;
            }
            case 88: {
                Integer n63 = null;
                n63 = new Integer(4);
                b b54 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n63);
                return b54;
            }
            case 87: {
                Integer n64 = null;
                n64 = new Integer(3);
                b b55 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n64);
                return b55;
            }
            case 86: {
                Integer n65 = null;
                n65 = new Integer(2);
                b b56 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n65);
                return b56;
            }
            case 85: {
                Integer n66 = null;
                n66 = new Integer(1);
                b b57 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n66);
                return b57;
            }
            case 84: {
                Integer n67 = null;
                n67 = new Integer(0);
                b b58 = new b(40, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n67);
                return b58;
            }
            case 83: {
                Integer n68 = null;
                n68 = new Integer(2);
                b b59 = new b(41, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n68);
                return b59;
            }
            case 82: {
                Integer n69;
                Integer n70 = null;
                int n71 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n72 = ((b)stack.elementAt((int)(n3 - 1))).e;
                n70 = n69 = (Integer)((b)stack.elementAt((int)(n3 - 1))).f;
                b b60 = new b(41, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n70);
                return b60;
            }
            case 81: {
                Expression expression;
                Expression expression7 = null;
                int n73 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n74 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression7 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b61 = new b(7, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression7);
                return b61;
            }
            case 80: {
                Step step = null;
                int n75 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n76 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Integer n77 = (Integer)((b)stack.elementAt((int)(n3 - 1))).f;
                int n78 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n79 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Object object = ((b)stack.elementAt((int)(n3 - 0))).f;
                step = new Step(n77, this.parser.findNodeType(n77, object), null);
                b b62 = new b(7, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, step);
                return b62;
            }
            case 79: {
                Step step = null;
                int n80 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n81 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Integer n82 = (Integer)((b)stack.elementAt((int)(n3 - 2))).f;
                int n83 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n84 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Object object = ((b)stack.elementAt((int)(n3 - 1))).f;
                int n85 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n86 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                step = new Step(n82, this.parser.findNodeType(n82, object), vector);
                b b63 = new b(7, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, step);
                return b63;
            }
            case 78: {
                Step step = null;
                int n87 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n88 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Object object = ((b)stack.elementAt((int)(n3 - 1))).f;
                int n89 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n90 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                if (object instanceof Step) {
                    Step step2 = (Step)object;
                    step2.addPredicates(vector);
                    step = (Step)object;
                } else {
                    step = new Step(3, this.parser.findNodeType(3, object), vector);
                }
                b b64 = new b(7, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, step);
                return b64;
            }
            case 77: {
                Step step = null;
                int n91 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n92 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Object object = ((b)stack.elementAt((int)(n3 - 0))).f;
                step = object instanceof Step ? (Step)object : new Step(3, this.parser.findNodeType(3, object), null);
                b b65 = new b(7, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, step);
                return b65;
            }
            case 76: {
                AbsoluteLocationPath absoluteLocationPath = null;
                int n93 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n94 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                int n95 = -1;
                if (expression instanceof Step && this.parser.isElementAxis(((Step)expression).getAxis())) {
                    n95 = 1;
                }
                Step step = new Step(5, n95, null);
                absoluteLocationPath = new AbsoluteLocationPath(this.parser.insertStep(step, (RelativeLocationPath)expression));
                b b66 = new b(24, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, absoluteLocationPath);
                return b66;
            }
            case 75: {
                RelativeLocationPath relativeLocationPath = null;
                int n96 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n97 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n98 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n99 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression8 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                Step step = (Step)expression8;
                int n100 = step.getAxis();
                int n101 = step.getNodeType();
                Vector vector = step.getPredicates();
                if (n100 == 3 && n101 != 2) {
                    if (vector == null) {
                        step.setAxis(4);
                        if (expression instanceof Step && ((Step)expression).isAbbreviatedDot()) {
                            relativeLocationPath = step;
                        } else {
                            RelativeLocationPath relativeLocationPath2 = (RelativeLocationPath)expression;
                            relativeLocationPath = new ParentLocationPath(relativeLocationPath2, step);
                        }
                    } else if (expression instanceof Step && ((Step)expression).isAbbreviatedDot()) {
                        Step step3 = new Step(5, 1, null);
                        relativeLocationPath = new ParentLocationPath(step3, step);
                    } else {
                        RelativeLocationPath relativeLocationPath3 = (RelativeLocationPath)expression;
                        Step step4 = new Step(5, 1, null);
                        ParentLocationPath parentLocationPath = new ParentLocationPath(step4, step);
                        relativeLocationPath = new ParentLocationPath(relativeLocationPath3, parentLocationPath);
                    }
                } else if (n100 == 2 || n101 == 2) {
                    RelativeLocationPath relativeLocationPath4 = (RelativeLocationPath)expression;
                    Step step5 = new Step(5, 1, null);
                    ParentLocationPath parentLocationPath = new ParentLocationPath(step5, step);
                    relativeLocationPath = new ParentLocationPath(relativeLocationPath4, parentLocationPath);
                } else {
                    RelativeLocationPath relativeLocationPath5 = (RelativeLocationPath)expression;
                    Step step6 = new Step(5, -1, null);
                    ParentLocationPath parentLocationPath = new ParentLocationPath(step6, step);
                    relativeLocationPath = new ParentLocationPath(relativeLocationPath5, parentLocationPath);
                }
                b b67 = new b(22, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, relativeLocationPath);
                return b67;
            }
            case 74: {
                Expression expression;
                Expression expression9 = null;
                int n102 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n103 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression9 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b68 = new b(23, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression9);
                return b68;
            }
            case 73: {
                AbsoluteLocationPath absoluteLocationPath = null;
                int n104 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n105 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                absoluteLocationPath = new AbsoluteLocationPath(expression);
                b b69 = new b(23, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, absoluteLocationPath);
                return b69;
            }
            case 72: {
                AbsoluteLocationPath absoluteLocationPath = null;
                absoluteLocationPath = new AbsoluteLocationPath();
                b b70 = new b(23, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, absoluteLocationPath);
                return b70;
            }
            case 71: {
                Expression expression;
                Expression expression10 = null;
                int n106 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n107 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression10 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b71 = new b(21, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression10);
                return b71;
            }
            case 70: {
                Expression expression = null;
                int n108 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n109 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression11 = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n110 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n111 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression12 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                expression = expression11 instanceof Step && ((Step)expression11).isAbbreviatedDot() ? expression12 : (((Step)expression12).isAbbreviatedDot() ? expression11 : new ParentLocationPath((RelativeLocationPath)expression11, expression12));
                b b72 = new b(21, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression);
                return b72;
            }
            case 69: {
                Expression expression;
                Expression expression13 = null;
                int n112 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n113 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression13 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b73 = new b(21, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression13);
                return b73;
            }
            case 68: {
                Expression expression;
                Expression expression14 = null;
                int n114 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n115 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression14 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b74 = new b(4, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression14);
                return b74;
            }
            case 67: {
                Expression expression;
                Expression expression15 = null;
                int n116 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n117 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression15 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b75 = new b(4, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression15);
                return b75;
            }
            case 66: {
                FilterParentPath filterParentPath = null;
                int n118 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n119 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n120 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n121 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression16 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                int n122 = -1;
                if (expression16 instanceof Step && this.parser.isElementAxis(((Step)expression16).getAxis())) {
                    n122 = 1;
                }
                Step step = new Step(5, n122, null);
                FilterParentPath filterParentPath2 = new FilterParentPath(expression, step);
                filterParentPath2 = new FilterParentPath(filterParentPath2, expression16);
                if (!(expression instanceof KeyCall)) {
                    filterParentPath2.setDescendantAxis();
                }
                filterParentPath = filterParentPath2;
                b b76 = new b(19, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, filterParentPath);
                return b76;
            }
            case 65: {
                FilterParentPath filterParentPath = null;
                int n123 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n124 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n125 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n126 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression17 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                filterParentPath = new FilterParentPath(expression, expression17);
                b b77 = new b(19, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, filterParentPath);
                return b77;
            }
            case 64: {
                Expression expression;
                Expression expression18 = null;
                int n127 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n128 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression18 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b78 = new b(19, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression18);
                return b78;
            }
            case 63: {
                Expression expression;
                Expression expression19 = null;
                int n129 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n130 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression19 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b79 = new b(19, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression19);
                return b79;
            }
            case 62: {
                UnionPathExpr unionPathExpr = null;
                int n131 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n132 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n133 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n134 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression20 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                unionPathExpr = new UnionPathExpr(expression, expression20);
                b b80 = new b(18, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, unionPathExpr);
                return b80;
            }
            case 61: {
                Expression expression;
                Expression expression21 = null;
                int n135 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n136 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression21 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b81 = new b(18, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression21);
                return b81;
            }
            case 60: {
                UnaryOpExpr unaryOpExpr = null;
                int n137 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n138 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                unaryOpExpr = new UnaryOpExpr(expression);
                b b82 = new b(14, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, unaryOpExpr);
                return b82;
            }
            case 59: {
                Expression expression;
                Expression expression22 = null;
                int n139 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n140 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression22 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b83 = new b(14, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression22);
                return b83;
            }
            case 58: {
                BinOpExpr binOpExpr = null;
                int n141 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n142 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n143 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n144 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression23 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                binOpExpr = new BinOpExpr(4, expression, expression23);
                b b84 = new b(13, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, binOpExpr);
                return b84;
            }
            case 57: {
                BinOpExpr binOpExpr = null;
                int n145 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n146 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n147 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n148 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression24 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                binOpExpr = new BinOpExpr(3, expression, expression24);
                b b85 = new b(13, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, binOpExpr);
                return b85;
            }
            case 56: {
                BinOpExpr binOpExpr = null;
                int n149 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n150 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n151 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n152 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression25 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                binOpExpr = new BinOpExpr(2, expression, expression25);
                b b86 = new b(13, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, binOpExpr);
                return b86;
            }
            case 55: {
                Expression expression;
                Expression expression26 = null;
                int n153 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n154 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression26 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b87 = new b(13, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression26);
                return b87;
            }
            case 54: {
                BinOpExpr binOpExpr = null;
                int n155 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n156 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n157 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n158 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression27 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                binOpExpr = new BinOpExpr(1, expression, expression27);
                b b88 = new b(12, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, binOpExpr);
                return b88;
            }
            case 53: {
                BinOpExpr binOpExpr = null;
                int n159 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n160 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n161 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n162 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression28 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                binOpExpr = new BinOpExpr(0, expression, expression28);
                b b89 = new b(12, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, binOpExpr);
                return b89;
            }
            case 52: {
                Expression expression;
                Expression expression29 = null;
                int n163 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n164 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression29 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b90 = new b(12, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression29);
                return b90;
            }
            case 51: {
                RelationalExpr relationalExpr = null;
                int n165 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n166 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n167 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n168 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression30 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                relationalExpr = new RelationalExpr(4, expression, expression30);
                b b91 = new b(11, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, relationalExpr);
                return b91;
            }
            case 50: {
                RelationalExpr relationalExpr = null;
                int n169 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n170 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n171 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n172 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression31 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                relationalExpr = new RelationalExpr(5, expression, expression31);
                b b92 = new b(11, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, relationalExpr);
                return b92;
            }
            case 49: {
                RelationalExpr relationalExpr = null;
                int n173 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n174 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n175 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n176 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression32 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                relationalExpr = new RelationalExpr(2, expression, expression32);
                b b93 = new b(11, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, relationalExpr);
                return b93;
            }
            case 48: {
                RelationalExpr relationalExpr = null;
                int n177 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n178 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n179 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n180 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression33 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                relationalExpr = new RelationalExpr(3, expression, expression33);
                b b94 = new b(11, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, relationalExpr);
                return b94;
            }
            case 47: {
                Expression expression;
                Expression expression34 = null;
                int n181 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n182 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression34 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b95 = new b(11, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression34);
                return b95;
            }
            case 46: {
                EqualityExpr equalityExpr = null;
                int n183 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n184 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n185 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n186 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression35 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                equalityExpr = new EqualityExpr(1, expression, expression35);
                b b96 = new b(10, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, equalityExpr);
                return b96;
            }
            case 45: {
                EqualityExpr equalityExpr = null;
                int n187 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n188 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n189 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n190 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression36 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                equalityExpr = new EqualityExpr(0, expression, expression36);
                b b97 = new b(10, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, equalityExpr);
                return b97;
            }
            case 44: {
                Expression expression;
                Expression expression37 = null;
                int n191 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n192 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression37 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b98 = new b(10, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression37);
                return b98;
            }
            case 43: {
                LogicalExpr logicalExpr = null;
                int n193 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n194 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n195 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n196 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression38 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                logicalExpr = new LogicalExpr(1, expression, expression38);
                b b99 = new b(9, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, logicalExpr);
                return b99;
            }
            case 42: {
                Expression expression;
                Expression expression39 = null;
                int n197 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n198 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression39 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b100 = new b(9, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression39);
                return b100;
            }
            case 41: {
                LogicalExpr logicalExpr = null;
                int n199 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n200 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 2))).f;
                int n201 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n202 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression40 = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                logicalExpr = new LogicalExpr(0, expression, expression40);
                b b101 = new b(8, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, logicalExpr);
                return b101;
            }
            case 40: {
                Expression expression;
                Expression expression41 = null;
                int n203 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n204 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression41 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b102 = new b(8, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression41);
                return b102;
            }
            case 39: {
                Expression expression;
                Expression expression42 = null;
                int n205 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n206 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression42 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b103 = new b(2, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression42);
                return b103;
            }
            case 38: {
                Predicate predicate = null;
                int n207 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n208 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 1))).f;
                predicate = new Predicate(expression);
                b b104 = new b(5, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, predicate);
                return b104;
            }
            case 37: {
                Vector vector = null;
                int n209 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n210 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 1))).f;
                int n211 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n212 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector4 = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                vector4.insertElementAt(expression, 0);
                vector = vector4;
                b b105 = new b(35, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, vector);
                return b105;
            }
            case 36: {
                Vector<Expression> vector = null;
                int n213 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n214 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Expression expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                Vector<Expression> vector5 = new Vector<Expression>();
                vector5.addElement(expression);
                vector = vector5;
                b b106 = new b(35, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, vector);
                return b106;
            }
            case 35: {
                Integer n215 = null;
                n215 = new Integer(2);
                b b107 = new b(42, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n215);
                return b107;
            }
            case 34: {
                Integer n216 = null;
                n216 = new Integer(3);
                b b108 = new b(42, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n216);
                return b108;
            }
            case 33: {
                Integer n217 = null;
                n217 = new Integer(2);
                b b109 = new b(42, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n217);
                return b109;
            }
            case 32: {
                QName qName;
                QName qName5 = null;
                int n218 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n219 = ((b)stack.elementAt((int)(n3 - 0))).e;
                qName5 = qName = (QName)((b)stack.elementAt((int)(n3 - 0))).f;
                b b110 = new b(34, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, qName5);
                return b110;
            }
            case 31: {
                Object var6_114 = null;
                var6_114 = null;
                b b111 = new b(34, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, var6_114);
                return b111;
            }
            case 30: {
                Integer n220 = null;
                n220 = new Integer(7);
                b b112 = new b(33, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n220);
                return b112;
            }
            case 29: {
                Integer n221 = null;
                n221 = new Integer(8);
                b b113 = new b(33, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n221);
                return b113;
            }
            case 28: {
                Integer n222 = null;
                n222 = new Integer(3);
                b b114 = new b(33, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n222);
                return b114;
            }
            case 27: {
                Integer n223 = null;
                n223 = new Integer(-1);
                b b115 = new b(33, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, n223);
                return b115;
            }
            case 26: {
                Object object;
                Object object3 = null;
                int n224 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n225 = ((b)stack.elementAt((int)(n3 - 0))).e;
                object3 = object = ((b)stack.elementAt((int)(n3 - 0))).f;
                b b116 = new b(33, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, object3);
                return b116;
            }
            case 25: {
                ProcessingInstructionPattern processingInstructionPattern = null;
                int n226 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n227 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Integer n228 = (Integer)((b)stack.elementAt((int)(n3 - 2))).f;
                int n229 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n230 = ((b)stack.elementAt((int)(n3 - 1))).e;
                StepPattern stepPattern = (StepPattern)((b)stack.elementAt((int)(n3 - 1))).f;
                int n231 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n232 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                processingInstructionPattern = (ProcessingInstructionPattern)stepPattern.setPredicates(vector);
                b b117 = new b(32, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, processingInstructionPattern);
                return b117;
            }
            case 24: {
                StepPattern stepPattern;
                StepPattern stepPattern2 = null;
                int n233 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n234 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Integer n235 = (Integer)((b)stack.elementAt((int)(n3 - 1))).f;
                int n236 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n237 = ((b)stack.elementAt((int)(n3 - 0))).e;
                stepPattern2 = stepPattern = (StepPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                b b118 = new b(32, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, stepPattern2);
                return b118;
            }
            case 23: {
                StepPattern stepPattern = null;
                int n238 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n239 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Integer n240 = (Integer)((b)stack.elementAt((int)(n3 - 2))).f;
                int n241 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n242 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Object object = ((b)stack.elementAt((int)(n3 - 1))).f;
                int n243 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n244 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                stepPattern = this.parser.createStepPattern(n240, object, vector);
                b b119 = new b(32, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, stepPattern);
                return b119;
            }
            case 22: {
                StepPattern stepPattern = null;
                int n245 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n246 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Integer n247 = (Integer)((b)stack.elementAt((int)(n3 - 1))).f;
                int n248 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n249 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Object object = ((b)stack.elementAt((int)(n3 - 0))).f;
                stepPattern = this.parser.createStepPattern(n247, object, null);
                b b120 = new b(32, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, stepPattern);
                return b120;
            }
            case 21: {
                ProcessingInstructionPattern processingInstructionPattern = null;
                int n250 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n251 = ((b)stack.elementAt((int)(n3 - 1))).e;
                StepPattern stepPattern = (StepPattern)((b)stack.elementAt((int)(n3 - 1))).f;
                int n252 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n253 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                processingInstructionPattern = (ProcessingInstructionPattern)stepPattern.setPredicates(vector);
                b b121 = new b(32, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, processingInstructionPattern);
                return b121;
            }
            case 20: {
                StepPattern stepPattern;
                StepPattern stepPattern3 = null;
                int n254 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n255 = ((b)stack.elementAt((int)(n3 - 0))).e;
                stepPattern3 = stepPattern = (StepPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                b b122 = new b(32, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, stepPattern3);
                return b122;
            }
            case 19: {
                StepPattern stepPattern = null;
                int n256 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n257 = ((b)stack.elementAt((int)(n3 - 1))).e;
                Object object = ((b)stack.elementAt((int)(n3 - 1))).f;
                int n258 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n259 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Vector vector = (Vector)((b)stack.elementAt((int)(n3 - 0))).f;
                stepPattern = this.parser.createStepPattern(3, object, vector);
                b b123 = new b(32, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, stepPattern);
                return b123;
            }
            case 18: {
                StepPattern stepPattern = null;
                int n260 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n261 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Object object = ((b)stack.elementAt((int)(n3 - 0))).f;
                stepPattern = this.parser.createStepPattern(3, object, null);
                b b124 = new b(32, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, stepPattern);
                return b124;
            }
            case 17: {
                AncestorPattern ancestorPattern = null;
                int n262 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n263 = ((b)stack.elementAt((int)(n3 - 2))).e;
                StepPattern stepPattern = (StepPattern)((b)stack.elementAt((int)(n3 - 2))).f;
                int n264 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n265 = ((b)stack.elementAt((int)(n3 - 0))).e;
                RelativePathPattern relativePathPattern = (RelativePathPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                ancestorPattern = new AncestorPattern(stepPattern, relativePathPattern);
                b b125 = new b(31, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, ancestorPattern);
                return b125;
            }
            case 16: {
                ParentPattern parentPattern = null;
                int n266 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n267 = ((b)stack.elementAt((int)(n3 - 2))).e;
                StepPattern stepPattern = (StepPattern)((b)stack.elementAt((int)(n3 - 2))).f;
                int n268 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n269 = ((b)stack.elementAt((int)(n3 - 0))).e;
                RelativePathPattern relativePathPattern = (RelativePathPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                parentPattern = new ParentPattern(stepPattern, relativePathPattern);
                b b126 = new b(31, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, parentPattern);
                return b126;
            }
            case 15: {
                StepPattern stepPattern;
                StepPattern stepPattern4 = null;
                int n270 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n271 = ((b)stack.elementAt((int)(n3 - 0))).e;
                stepPattern4 = stepPattern = (StepPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                b b127 = new b(31, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, stepPattern4);
                return b127;
            }
            case 14: {
                ProcessingInstructionPattern processingInstructionPattern = null;
                int n272 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n273 = ((b)stack.elementAt((int)(n3 - 1))).e;
                String string = (String)((b)stack.elementAt((int)(n3 - 1))).f;
                processingInstructionPattern = new ProcessingInstructionPattern(string);
                b b128 = new b(30, ((b)stack.elementAt((int)(n3 - 3))).d, ((b)stack.elementAt((int)(n3 - 0))).e, processingInstructionPattern);
                return b128;
            }
            case 13: {
                KeyPattern keyPattern = null;
                int n274 = ((b)stack.elementAt((int)(n3 - 3))).d;
                int n275 = ((b)stack.elementAt((int)(n3 - 3))).e;
                String string = (String)((b)stack.elementAt((int)(n3 - 3))).f;
                int n276 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n277 = ((b)stack.elementAt((int)(n3 - 1))).e;
                String string4 = (String)((b)stack.elementAt((int)(n3 - 1))).f;
                keyPattern = new KeyPattern(string, string4);
                b b129 = new b(27, ((b)stack.elementAt((int)(n3 - 5))).d, ((b)stack.elementAt((int)(n3 - 0))).e, keyPattern);
                return b129;
            }
            case 12: {
                IdPattern idPattern = null;
                int n278 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n279 = ((b)stack.elementAt((int)(n3 - 1))).e;
                String string = (String)((b)stack.elementAt((int)(n3 - 1))).f;
                idPattern = new IdPattern(string);
                this.parser.setHasIdCall(true);
                b b130 = new b(27, ((b)stack.elementAt((int)(n3 - 3))).d, ((b)stack.elementAt((int)(n3 - 0))).e, idPattern);
                return b130;
            }
            case 11: {
                RelativePathPattern relativePathPattern;
                RelativePathPattern relativePathPattern2 = null;
                int n280 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n281 = ((b)stack.elementAt((int)(n3 - 0))).e;
                relativePathPattern2 = relativePathPattern = (RelativePathPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                b b131 = new b(29, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, relativePathPattern2);
                return b131;
            }
            case 10: {
                AncestorPattern ancestorPattern = null;
                int n282 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n283 = ((b)stack.elementAt((int)(n3 - 0))).e;
                RelativePathPattern relativePathPattern = (RelativePathPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                ancestorPattern = new AncestorPattern(relativePathPattern);
                b b132 = new b(29, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, ancestorPattern);
                return b132;
            }
            case 9: {
                AncestorPattern ancestorPattern = null;
                int n284 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n285 = ((b)stack.elementAt((int)(n3 - 2))).e;
                IdKeyPattern idKeyPattern = (IdKeyPattern)((b)stack.elementAt((int)(n3 - 2))).f;
                int n286 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n287 = ((b)stack.elementAt((int)(n3 - 0))).e;
                RelativePathPattern relativePathPattern = (RelativePathPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                ancestorPattern = new AncestorPattern(idKeyPattern, relativePathPattern);
                b b133 = new b(29, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, ancestorPattern);
                return b133;
            }
            case 8: {
                ParentPattern parentPattern = null;
                int n288 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n289 = ((b)stack.elementAt((int)(n3 - 2))).e;
                IdKeyPattern idKeyPattern = (IdKeyPattern)((b)stack.elementAt((int)(n3 - 2))).f;
                int n290 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n291 = ((b)stack.elementAt((int)(n3 - 0))).e;
                RelativePathPattern relativePathPattern = (RelativePathPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                parentPattern = new ParentPattern(idKeyPattern, relativePathPattern);
                b b134 = new b(29, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, parentPattern);
                return b134;
            }
            case 7: {
                IdKeyPattern idKeyPattern;
                IdKeyPattern idKeyPattern2 = null;
                int n292 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n293 = ((b)stack.elementAt((int)(n3 - 0))).e;
                idKeyPattern2 = idKeyPattern = (IdKeyPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                b b135 = new b(29, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, idKeyPattern2);
                return b135;
            }
            case 6: {
                AbsolutePathPattern absolutePathPattern = null;
                int n294 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n295 = ((b)stack.elementAt((int)(n3 - 0))).e;
                RelativePathPattern relativePathPattern = (RelativePathPattern)((b)stack.elementAt((int)(n3 - 0))).f;
                absolutePathPattern = new AbsolutePathPattern(relativePathPattern);
                b b136 = new b(29, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, absolutePathPattern);
                return b136;
            }
            case 5: {
                AbsolutePathPattern absolutePathPattern = null;
                absolutePathPattern = new AbsolutePathPattern(null);
                b b137 = new b(29, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, absolutePathPattern);
                return b137;
            }
            case 4: {
                AlternativePattern alternativePattern = null;
                int n296 = ((b)stack.elementAt((int)(n3 - 2))).d;
                int n297 = ((b)stack.elementAt((int)(n3 - 2))).e;
                Pattern pattern = (Pattern)((b)stack.elementAt((int)(n3 - 2))).f;
                int n298 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n299 = ((b)stack.elementAt((int)(n3 - 0))).e;
                Pattern pattern2 = (Pattern)((b)stack.elementAt((int)(n3 - 0))).f;
                alternativePattern = new AlternativePattern(pattern, pattern2);
                b b138 = new b(28, ((b)stack.elementAt((int)(n3 - 2))).d, ((b)stack.elementAt((int)(n3 - 0))).e, alternativePattern);
                return b138;
            }
            case 3: {
                Pattern pattern;
                Pattern pattern3 = null;
                int n300 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n301 = ((b)stack.elementAt((int)(n3 - 0))).e;
                pattern3 = pattern = (Pattern)((b)stack.elementAt((int)(n3 - 0))).f;
                b b139 = new b(28, ((b)stack.elementAt((int)(n3 - 0))).d, ((b)stack.elementAt((int)(n3 - 0))).e, pattern3);
                return b139;
            }
            case 2: {
                Expression expression;
                Expression expression43 = null;
                int n302 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n303 = ((b)stack.elementAt((int)(n3 - 0))).e;
                expression43 = expression = (Expression)((b)stack.elementAt((int)(n3 - 0))).f;
                b b140 = new b(1, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, expression43);
                return b140;
            }
            case 1: {
                Pattern pattern;
                Pattern pattern4 = null;
                int n304 = ((b)stack.elementAt((int)(n3 - 0))).d;
                int n305 = ((b)stack.elementAt((int)(n3 - 0))).e;
                pattern4 = pattern = (Pattern)((b)stack.elementAt((int)(n3 - 0))).f;
                b b141 = new b(1, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, pattern4);
                return b141;
            }
            case 0: {
                SyntaxTreeNode syntaxTreeNode;
                SyntaxTreeNode syntaxTreeNode2 = null;
                int n306 = ((b)stack.elementAt((int)(n3 - 1))).d;
                int n307 = ((b)stack.elementAt((int)(n3 - 1))).e;
                syntaxTreeNode2 = syntaxTreeNode = (SyntaxTreeNode)((b)stack.elementAt((int)(n3 - 1))).f;
                b b142 = new b(0, ((b)stack.elementAt((int)(n3 - 1))).d, ((b)stack.elementAt((int)(n3 - 0))).e, syntaxTreeNode2);
                c2.done_parsing();
                return b142;
            }
        }
        throw new Exception("Invalid action number found in internal parse table");
    }
}

