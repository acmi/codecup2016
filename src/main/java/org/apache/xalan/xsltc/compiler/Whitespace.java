/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Whitespace
extends TopLevelElement {
    public static final int USE_PREDICATE = 0;
    public static final int STRIP_SPACE = 1;
    public static final int PRESERVE_SPACE = 2;
    public static final int RULE_NONE = 0;
    public static final int RULE_ELEMENT = 1;
    public static final int RULE_NAMESPACE = 2;
    public static final int RULE_ALL = 3;
    private String _elementList;
    private int _action;
    private int _importPrecedence;

    Whitespace() {
    }

    public void parseContents(Parser parser) {
        this._action = this._qname.getLocalPart().endsWith("strip-space") ? 1 : 2;
        this._importPrecedence = parser.getCurrentImportPrecedence();
        this._elementList = this.getAttribute("elements");
        if (this._elementList == null || this._elementList.length() == 0) {
            this.reportError(this, parser, "REQUIRED_ATTR_ERR", "elements");
            return;
        }
        SymbolTable symbolTable = parser.getSymbolTable();
        StringTokenizer stringTokenizer = new StringTokenizer(this._elementList);
        StringBuffer stringBuffer = new StringBuffer("");
        while (stringTokenizer.hasMoreElements()) {
            String string = stringTokenizer.nextToken();
            int n2 = string.indexOf(58);
            if (n2 != -1) {
                String string2 = this.lookupNamespace(string.substring(0, n2));
                if (string2 != null) {
                    stringBuffer.append(string2 + ":" + string.substring(n2 + 1, string.length()));
                } else {
                    stringBuffer.append(string);
                }
            } else {
                stringBuffer.append(string);
            }
            if (!stringTokenizer.hasMoreElements()) continue;
            stringBuffer.append(" ");
        }
        this._elementList = stringBuffer.toString();
    }

    public Vector getRules() {
        Vector<WhitespaceRule> vector = new Vector<WhitespaceRule>();
        StringTokenizer stringTokenizer = new StringTokenizer(this._elementList);
        while (stringTokenizer.hasMoreElements()) {
            vector.add(new WhitespaceRule(this._action, stringTokenizer.nextToken(), this._importPrecedence));
        }
        return vector;
    }

    private static WhitespaceRule findContradictingRule(Vector vector, WhitespaceRule whitespaceRule) {
        block5 : for (int i2 = 0; i2 < vector.size(); ++i2) {
            WhitespaceRule whitespaceRule2 = (WhitespaceRule)vector.elementAt(i2);
            if (whitespaceRule2 == whitespaceRule) {
                return null;
            }
            switch (whitespaceRule2.getStrength()) {
                case 3: {
                    return whitespaceRule2;
                }
                case 1: {
                    if (!whitespaceRule.getElement().equals(whitespaceRule2.getElement())) continue block5;
                }
                case 2: {
                    if (!whitespaceRule.getNamespace().equals(whitespaceRule2.getNamespace())) continue block5;
                    return whitespaceRule2;
                }
            }
        }
        return null;
    }

    private static int prioritizeRules(Vector vector) {
        int n2;
        WhitespaceRule whitespaceRule;
        int n3 = 2;
        Whitespace.quicksort(vector, 0, vector.size() - 1);
        boolean bl = false;
        for (n2 = 0; n2 < vector.size(); ++n2) {
            whitespaceRule = (WhitespaceRule)vector.elementAt(n2);
            if (whitespaceRule.getAction() != 1) continue;
            bl = true;
        }
        if (!bl) {
            vector.removeAllElements();
            return 2;
        }
        n2 = 0;
        while (n2 < vector.size()) {
            whitespaceRule = (WhitespaceRule)vector.elementAt(n2);
            if (Whitespace.findContradictingRule(vector, whitespaceRule) != null) {
                vector.remove(n2);
                continue;
            }
            if (whitespaceRule.getStrength() == 3) {
                n3 = whitespaceRule.getAction();
                for (int i2 = n2; i2 < vector.size(); ++i2) {
                    vector.removeElementAt(i2);
                }
            }
            ++n2;
        }
        if (vector.size() == 0) {
            return n3;
        }
        while ((whitespaceRule = (WhitespaceRule)vector.lastElement()).getAction() == n3) {
            vector.removeElementAt(vector.size() - 1);
            if (vector.size() > 0) continue;
        }
        return n3;
    }

    public static void compileStripSpace(BranchHandle[] arrbranchHandle, int n2, InstructionList instructionList) {
        InstructionHandle instructionHandle = instructionList.append(ICONST_1);
        instructionList.append(IRETURN);
        for (int i2 = 0; i2 < n2; ++i2) {
            arrbranchHandle[i2].setTarget(instructionHandle);
        }
    }

    public static void compilePreserveSpace(BranchHandle[] arrbranchHandle, int n2, InstructionList instructionList) {
        InstructionHandle instructionHandle = instructionList.append(ICONST_0);
        instructionList.append(IRETURN);
        for (int i2 = 0; i2 < n2; ++i2) {
            arrbranchHandle[i2].setTarget(instructionHandle);
        }
    }

    private static void compilePredicate(Vector vector, int n2, ClassGenerator classGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = new InstructionList();
        XSLTC xSLTC = classGenerator.getParser().getXSLTC();
        MethodGenerator methodGenerator = new MethodGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT}, new String[]{"dom", "node", "type"}, "stripSpace", classGenerator.getClassName(), instructionList, constantPoolGen);
        classGenerator.addInterface("org/apache/xalan/xsltc/StripFilter");
        int n3 = methodGenerator.getLocalIndex("dom");
        int n4 = methodGenerator.getLocalIndex("node");
        int n5 = methodGenerator.getLocalIndex("type");
        BranchHandle[] arrbranchHandle = new BranchHandle[vector.size()];
        BranchHandle[] arrbranchHandle2 = new BranchHandle[vector.size()];
        int n6 = 0;
        int n7 = 0;
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            WhitespaceRule whitespaceRule = (WhitespaceRule)vector.elementAt(i2);
            int n8 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNamespaceName", "(I)Ljava/lang/String;");
            int n9 = constantPoolGen.addMethodref("java/lang/String", "compareTo", "(Ljava/lang/String;)I");
            if (whitespaceRule.getStrength() == 2) {
                instructionList.append(new ALOAD(n3));
                instructionList.append(new ILOAD(n4));
                instructionList.append(new INVOKEINTERFACE(n8, 2));
                instructionList.append(new PUSH(constantPoolGen, whitespaceRule.getNamespace()));
                instructionList.append(new INVOKEVIRTUAL(n9));
                instructionList.append(ICONST_0);
                if (whitespaceRule.getAction() == 1) {
                    arrbranchHandle[n6++] = instructionList.append(new IF_ICMPEQ(null));
                    continue;
                }
                arrbranchHandle2[n7++] = instructionList.append(new IF_ICMPEQ(null));
                continue;
            }
            if (whitespaceRule.getStrength() != 1) continue;
            Parser parser = classGenerator.getParser();
            QName qName = whitespaceRule.getNamespace() != "" ? parser.getQName(whitespaceRule.getNamespace(), null, whitespaceRule.getElement()) : parser.getQName(whitespaceRule.getElement());
            int n10 = xSLTC.registerElement(qName);
            instructionList.append(new ILOAD(n5));
            instructionList.append(new PUSH(constantPoolGen, n10));
            if (whitespaceRule.getAction() == 1) {
                arrbranchHandle[n6++] = instructionList.append(new IF_ICMPEQ(null));
                continue;
            }
            arrbranchHandle2[n7++] = instructionList.append(new IF_ICMPEQ(null));
        }
        if (n2 == 1) {
            Whitespace.compileStripSpace(arrbranchHandle, n6, instructionList);
            Whitespace.compilePreserveSpace(arrbranchHandle2, n7, instructionList);
        } else {
            Whitespace.compilePreserveSpace(arrbranchHandle2, n7, instructionList);
            Whitespace.compileStripSpace(arrbranchHandle, n6, instructionList);
        }
        classGenerator.addMethod(methodGenerator);
    }

    private static void compileDefault(int n2, ClassGenerator classGenerator) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = new InstructionList();
        XSLTC xSLTC = classGenerator.getParser().getXSLTC();
        MethodGenerator methodGenerator = new MethodGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT}, new String[]{"dom", "node", "type"}, "stripSpace", classGenerator.getClassName(), instructionList, constantPoolGen);
        classGenerator.addInterface("org/apache/xalan/xsltc/StripFilter");
        if (n2 == 1) {
            instructionList.append(ICONST_1);
        } else {
            instructionList.append(ICONST_0);
        }
        instructionList.append(IRETURN);
        classGenerator.addMethod(methodGenerator);
    }

    public static int translateRules(Vector vector, ClassGenerator classGenerator) {
        int n2 = Whitespace.prioritizeRules(vector);
        if (vector.size() == 0) {
            Whitespace.compileDefault(n2, classGenerator);
            return n2;
        }
        Whitespace.compilePredicate(vector, n2, classGenerator);
        return 0;
    }

    private static void quicksort(Vector vector, int n2, int n3) {
        while (n2 < n3) {
            int n4 = Whitespace.partition(vector, n2, n3);
            Whitespace.quicksort(vector, n2, n4);
            n2 = n4 + 1;
        }
    }

    private static int partition(Vector vector, int n2, int n3) {
        WhitespaceRule whitespaceRule = (WhitespaceRule)vector.elementAt(n2 + n3 >>> 1);
        int n4 = n2 - 1;
        int n5 = n3 + 1;
        do {
            if (whitespaceRule.compareTo((WhitespaceRule)vector.elementAt(--n5)) < 0) {
                continue;
            }
            while (whitespaceRule.compareTo((WhitespaceRule)vector.elementAt(++n4)) > 0) {
            }
            if (n4 >= n5) break;
            WhitespaceRule whitespaceRule2 = (WhitespaceRule)vector.elementAt(n4);
            vector.setElementAt(vector.elementAt(n5), n4);
            vector.setElementAt(whitespaceRule2, n5);
        } while (true);
        return n5;
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
    }

    private static final class WhitespaceRule {
        private final int _action;
        private String _namespace;
        private String _element;
        private int _type;
        private int _priority;

        public WhitespaceRule(int n2, String string, int n3) {
            this._action = n2;
            int n4 = string.lastIndexOf(58);
            if (n4 >= 0) {
                this._namespace = string.substring(0, n4);
                this._element = string.substring(n4 + 1, string.length());
            } else {
                this._namespace = "";
                this._element = string;
            }
            this._priority = n3 << 2;
            if (this._element.equals("*")) {
                if (this._namespace == "") {
                    this._type = 3;
                    this._priority += 2;
                } else {
                    this._type = 2;
                    ++this._priority;
                }
            } else {
                this._type = 1;
            }
        }

        public int compareTo(WhitespaceRule whitespaceRule) {
            return this._priority < whitespaceRule._priority ? -1 : (this._priority > whitespaceRule._priority ? 1 : 0);
        }

        public int getAction() {
            return this._action;
        }

        public int getStrength() {
            return this._type;
        }

        public int getPriority() {
            return this._priority;
        }

        public String getElement() {
            return this._element;
        }

        public String getNamespace() {
            return this._namespace;
        }
    }

}

