/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.SWITCH;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.generic.Type;
import org.apache.bcel.util.InstructionFinder;
import org.apache.xalan.xsltc.compiler.AlternativePattern;
import org.apache.xalan.xsltc.compiler.Constants;
import org.apache.xalan.xsltc.compiler.IdKeyPattern;
import org.apache.xalan.xsltc.compiler.LocationPathPattern;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.StepPattern;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.TestSeq;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NamedMethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Mode
implements Constants {
    private final QName _name;
    private final Stylesheet _stylesheet;
    private final String _methodName;
    private Vector _templates;
    private Vector _childNodeGroup = null;
    private TestSeq _childNodeTestSeq = null;
    private Vector _attribNodeGroup = null;
    private TestSeq _attribNodeTestSeq = null;
    private Vector _idxGroup = null;
    private TestSeq _idxTestSeq = null;
    private Vector[] _patternGroups;
    private TestSeq[] _testSeq;
    private Hashtable _neededTemplates = new Hashtable();
    private Hashtable _namedTemplates = new Hashtable();
    private Hashtable _templateIHs = new Hashtable();
    private Hashtable _templateILs = new Hashtable();
    private LocationPathPattern _rootPattern = null;
    private Hashtable _importLevels = null;
    private Hashtable _keys = null;
    private int _currentIndex;

    public Mode(QName qName, Stylesheet stylesheet, String string) {
        this._name = qName;
        this._stylesheet = stylesheet;
        this._methodName = "applyTemplates" + string;
        this._templates = new Vector();
        this._patternGroups = new Vector[32];
    }

    public String functionName() {
        return this._methodName;
    }

    public String functionName(int n2, int n3) {
        if (this._importLevels == null) {
            this._importLevels = new Hashtable();
        }
        this._importLevels.put(new Integer(n3), new Integer(n2));
        return this._methodName + '_' + n3;
    }

    private String getClassName() {
        return this._stylesheet.getClassName();
    }

    public Stylesheet getStylesheet() {
        return this._stylesheet;
    }

    public void addTemplate(Template template) {
        this._templates.addElement(template);
    }

    private Vector quicksort(Vector vector, int n2, int n3) {
        if (n2 < n3) {
            int n4 = this.partition(vector, n2, n3);
            this.quicksort(vector, n2, n4);
            this.quicksort(vector, n4 + 1, n3);
        }
        return vector;
    }

    private int partition(Vector vector, int n2, int n3) {
        Template template = (Template)vector.elementAt(n2);
        int n4 = n2 - 1;
        int n5 = n3 + 1;
        do {
            if (template.compareTo((Template)vector.elementAt(--n5)) > 0) {
                continue;
            }
            while (template.compareTo((Template)vector.elementAt(++n4)) < 0) {
            }
            if (n4 >= n5) break;
            vector.set(n5, vector.set(n4, vector.elementAt(n5)));
        } while (true);
        return n5;
    }

    public void processPatterns(Hashtable hashtable) {
        this._keys = hashtable;
        this._templates = this.quicksort(this._templates, 0, this._templates.size() - 1);
        Enumeration enumeration = this._templates.elements();
        while (enumeration.hasMoreElements()) {
            Pattern pattern;
            Template template = (Template)enumeration.nextElement();
            if (template.isNamed() && !template.disabled()) {
                this._namedTemplates.put(template, this);
            }
            if ((pattern = template.getPattern()) == null) continue;
            this.flattenAlternative(pattern, template, hashtable);
        }
        this.prepareTestSequences();
    }

    private void flattenAlternative(Pattern pattern, Template template, Hashtable hashtable) {
        if (pattern instanceof IdKeyPattern) {
            IdKeyPattern idKeyPattern = (IdKeyPattern)pattern;
            idKeyPattern.setTemplate(template);
            if (this._idxGroup == null) {
                this._idxGroup = new Vector();
            }
            this._idxGroup.add(pattern);
        } else if (pattern instanceof AlternativePattern) {
            AlternativePattern alternativePattern = (AlternativePattern)pattern;
            this.flattenAlternative(alternativePattern.getLeft(), template, hashtable);
            this.flattenAlternative(alternativePattern.getRight(), template, hashtable);
        } else if (pattern instanceof LocationPathPattern) {
            LocationPathPattern locationPathPattern = (LocationPathPattern)pattern;
            locationPathPattern.setTemplate(template);
            this.addPatternToGroup(locationPathPattern);
        }
    }

    private void addPatternToGroup(LocationPathPattern locationPathPattern) {
        if (locationPathPattern instanceof IdKeyPattern) {
            this.addPattern(-1, locationPathPattern);
        } else {
            StepPattern stepPattern = locationPathPattern.getKernelPattern();
            if (stepPattern != null) {
                this.addPattern(stepPattern.getNodeType(), locationPathPattern);
            } else if (this._rootPattern == null || locationPathPattern.noSmallerThan(this._rootPattern)) {
                this._rootPattern = locationPathPattern;
            }
        }
    }

    private void addPattern(int n2, LocationPathPattern locationPathPattern) {
        Object object;
        int n3 = this._patternGroups.length;
        if (n2 >= n3) {
            object = new Vector[n2 * 2];
            System.arraycopy(this._patternGroups, 0, object, 0, n3);
            this._patternGroups = object;
        }
        if (n2 == -1) {
            if (locationPathPattern.getAxis() == 2) {
                Vector vector = this._attribNodeGroup == null ? (this._attribNodeGroup = new Vector(2)) : this._attribNodeGroup;
                object = vector;
            } else {
                Vector vector = this._childNodeGroup == null ? (this._childNodeGroup = new Vector(2)) : this._childNodeGroup;
                object = vector;
            }
        } else {
            Object object2 = object = this._patternGroups[n2] == null ? new Vector(2) : this._patternGroups[n2];
        }
        if (object.size() == 0) {
            object.addElement(locationPathPattern);
        } else {
            boolean bl = false;
            for (int i2 = 0; i2 < object.size(); ++i2) {
                LocationPathPattern locationPathPattern2 = (LocationPathPattern)object.elementAt(i2);
                if (!locationPathPattern.noSmallerThan(locationPathPattern2)) continue;
                bl = true;
                object.insertElementAt(locationPathPattern, i2);
                break;
            }
            if (!bl) {
                object.addElement(locationPathPattern);
            }
        }
    }

    private void completeTestSequences(int n2, Vector vector) {
        if (vector != null) {
            if (this._patternGroups[n2] == null) {
                this._patternGroups[n2] = vector;
            } else {
                int n3 = vector.size();
                for (int i2 = 0; i2 < n3; ++i2) {
                    this.addPattern(n2, (LocationPathPattern)vector.elementAt(i2));
                }
            }
        }
    }

    private void prepareTestSequences() {
        int n2;
        Object object;
        int n3;
        Vector vector = this._patternGroups[1];
        Vector vector2 = this._patternGroups[2];
        this.completeTestSequences(3, this._childNodeGroup);
        this.completeTestSequences(1, this._childNodeGroup);
        this.completeTestSequences(7, this._childNodeGroup);
        this.completeTestSequences(8, this._childNodeGroup);
        this.completeTestSequences(2, this._attribNodeGroup);
        Vector vector3 = this._stylesheet.getXSLTC().getNamesIndex();
        if (vector != null || vector2 != null || this._childNodeGroup != null || this._attribNodeGroup != null) {
            n2 = this._patternGroups.length;
            for (n3 = 14; n3 < n2; ++n3) {
                if (this._patternGroups[n3] == null) continue;
                object = (String)vector3.elementAt(n3 - 14);
                if (Mode.isAttributeName((String)object)) {
                    this.completeTestSequences(n3, vector2);
                    this.completeTestSequences(n3, this._attribNodeGroup);
                    continue;
                }
                this.completeTestSequences(n3, vector);
                this.completeTestSequences(n3, this._childNodeGroup);
            }
        }
        this._testSeq = new TestSeq[14 + vector3.size()];
        n2 = this._patternGroups.length;
        for (n3 = 0; n3 < n2; ++n3) {
            object = this._patternGroups[n3];
            if (object == null) continue;
            TestSeq testSeq = new TestSeq((Vector)object, n3, this);
            testSeq.reduce();
            this._testSeq[n3] = testSeq;
            testSeq.findTemplates(this._neededTemplates);
        }
        if (this._childNodeGroup != null && this._childNodeGroup.size() > 0) {
            this._childNodeTestSeq = new TestSeq(this._childNodeGroup, -1, this);
            this._childNodeTestSeq.reduce();
            this._childNodeTestSeq.findTemplates(this._neededTemplates);
        }
        if (this._idxGroup != null && this._idxGroup.size() > 0) {
            this._idxTestSeq = new TestSeq(this._idxGroup, this);
            this._idxTestSeq.reduce();
            this._idxTestSeq.findTemplates(this._neededTemplates);
        }
        if (this._rootPattern != null) {
            this._neededTemplates.put(this._rootPattern.getTemplate(), this);
        }
    }

    private void compileNamedTemplate(Template template, ClassGenerator classGenerator) {
        Type[] arrtype;
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = new InstructionList();
        String string = Util.escape(template.getName().toString());
        int n2 = 0;
        if (template.isSimpleNamedTemplate()) {
            arrtype = template.getParameters();
            n2 = arrtype.size();
        }
        arrtype = new Type[4 + n2];
        String[] arrstring = new String[4 + n2];
        arrtype[0] = Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;");
        arrtype[1] = Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;");
        arrtype[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
        arrtype[3] = Type.INT;
        arrstring[0] = "document";
        arrstring[1] = "iterator";
        arrstring[2] = "handler";
        arrstring[3] = "node";
        for (int i2 = 4; i2 < 4 + n2; ++i2) {
            arrtype[i2] = Util.getJCRefType("Ljava/lang/Object;");
            arrstring[i2] = "param" + String.valueOf(i2 - 4);
        }
        NamedMethodGenerator namedMethodGenerator = new NamedMethodGenerator(1, Type.VOID, arrtype, arrstring, string, this.getClassName(), instructionList, constantPoolGen);
        instructionList.append(template.compile(classGenerator, namedMethodGenerator));
        instructionList.append(RETURN);
        classGenerator.addMethod(namedMethodGenerator);
    }

    private void compileTemplates(ClassGenerator classGenerator, MethodGenerator methodGenerator, InstructionHandle instructionHandle) {
        Template template;
        Enumeration enumeration = this._namedTemplates.keys();
        while (enumeration.hasMoreElements()) {
            template = (Template)enumeration.nextElement();
            this.compileNamedTemplate(template, classGenerator);
        }
        enumeration = this._neededTemplates.keys();
        while (enumeration.hasMoreElements()) {
            template = (Template)enumeration.nextElement();
            if (template.hasContents()) {
                InstructionList instructionList = template.compile(classGenerator, methodGenerator);
                instructionList.append(new GOTO_W(instructionHandle));
                this._templateILs.put(template, instructionList);
                this._templateIHs.put(template, instructionList.getStart());
                continue;
            }
            this._templateIHs.put(template, instructionHandle);
        }
    }

    private void appendTemplateCode(InstructionList instructionList) {
        Enumeration enumeration = this._neededTemplates.keys();
        while (enumeration.hasMoreElements()) {
            Object v2 = this._templateILs.get(enumeration.nextElement());
            if (v2 == null) continue;
            instructionList.append((InstructionList)v2);
        }
    }

    private void appendTestSequences(InstructionList instructionList) {
        int n2 = this._testSeq.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            InstructionList instructionList2;
            TestSeq testSeq = this._testSeq[i2];
            if (testSeq == null || (instructionList2 = testSeq.getInstructionList()) == null) continue;
            instructionList.append(instructionList2);
        }
    }

    public static void compileGetChildren(ClassGenerator classGenerator, MethodGenerator methodGenerator, int n2) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n3 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getChildren", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new ILOAD(n2));
        instructionList.append(new INVOKEINTERFACE(n3, 2));
    }

    private InstructionList compileDefaultRecursion(ClassGenerator classGenerator, MethodGenerator methodGenerator, InstructionHandle instructionHandle) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = new InstructionList();
        String string = classGenerator.getApplyTemplatesSig();
        int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getChildren", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
        int n3 = constantPoolGen.addMethodref(this.getClassName(), this.functionName(), string);
        instructionList.append(classGenerator.loadTranslet());
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new ILOAD(this._currentIndex));
        instructionList.append(new INVOKEINTERFACE(n2, 2));
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(new INVOKEVIRTUAL(n3));
        instructionList.append(new GOTO_W(instructionHandle));
        return instructionList;
    }

    private InstructionList compileDefaultText(ClassGenerator classGenerator, MethodGenerator methodGenerator, InstructionHandle instructionHandle) {
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = new InstructionList();
        int n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "characters", CHARACTERS_SIG);
        instructionList.append(methodGenerator.loadDOM());
        instructionList.append(new ILOAD(this._currentIndex));
        instructionList.append(methodGenerator.loadHandler());
        instructionList.append(new INVOKEINTERFACE(n2, 3));
        instructionList.append(new GOTO_W(instructionHandle));
        return instructionList;
    }

    private InstructionList compileNamespaces(ClassGenerator classGenerator, MethodGenerator methodGenerator, boolean[] arrbl, boolean[] arrbl2, boolean bl, InstructionHandle instructionHandle) {
        XSLTC xSLTC = classGenerator.getParser().getXSLTC();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        Vector vector = xSLTC.getNamespaceIndex();
        Vector vector2 = xSLTC.getNamesIndex();
        int n2 = vector.size() + 1;
        int n3 = vector2.size();
        InstructionList instructionList = new InstructionList();
        int[] arrn = new int[n2];
        InstructionHandle[] arrinstructionHandle = new InstructionHandle[arrn.length];
        if (n2 > 0) {
            boolean bl2 = false;
            int n4 = 0;
            while (n4 < n2) {
                arrinstructionHandle[n4] = instructionHandle;
                arrn[n4] = n4++;
            }
            for (n4 = 14; n4 < 14 + n3; ++n4) {
                if (!arrbl[n4] || arrbl2[n4] != bl) continue;
                String string = (String)vector2.elementAt(n4 - 14);
                String string2 = string.substring(0, string.lastIndexOf(58));
                int n5 = xSLTC.registerNamespace(string2);
                if (n4 >= this._testSeq.length || this._testSeq[n4] == null) continue;
                arrinstructionHandle[n5] = this._testSeq[n4].compile(classGenerator, methodGenerator, instructionHandle);
                bl2 = true;
            }
            if (!bl2) {
                return null;
            }
            n4 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNamespaceType", "(I)I");
            instructionList.append(methodGenerator.loadDOM());
            instructionList.append(new ILOAD(this._currentIndex));
            instructionList.append(new INVOKEINTERFACE(n4, 2));
            instructionList.append(new SWITCH(arrn, arrinstructionHandle, instructionHandle));
            return instructionList;
        }
        return null;
    }

    public void compileApplyTemplates(ClassGenerator classGenerator) {
        int n2;
        Object object;
        Object object2;
        int n3;
        Object object3;
        XSLTC xSLTC = classGenerator.getParser().getXSLTC();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        Vector vector = xSLTC.getNamesIndex();
        Type[] arrtype = new Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), Util.getJCRefType(TRANSLET_OUTPUT_SIG)};
        String[] arrstring = new String[]{"document", "iterator", "handler"};
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator = new MethodGenerator(17, Type.VOID, arrtype, arrstring, this.functionName(), this.getClassName(), instructionList, classGenerator.getConstantPool());
        methodGenerator.addException("org.apache.xalan.xsltc.TransletException");
        instructionList.append(NOP);
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable2("current", Type.INT, null);
        this._currentIndex = localVariableGen.getIndex();
        InstructionList instructionList2 = new InstructionList();
        instructionList2.append(NOP);
        InstructionList instructionList3 = new InstructionList();
        instructionList3.append(methodGenerator.loadIterator());
        instructionList3.append(methodGenerator.nextNode());
        instructionList3.append(DUP);
        instructionList3.append(new ISTORE(this._currentIndex));
        BranchHandle branchHandle = instructionList3.append(new IFLT(null));
        BranchHandle branchHandle2 = instructionList3.append(new GOTO_W(null));
        branchHandle.setTarget(instructionList3.append(RETURN));
        InstructionHandle instructionHandle = instructionList3.getStart();
        localVariableGen.setStart(instructionList.append(new GOTO_W(instructionHandle)));
        localVariableGen.setEnd(branchHandle2);
        InstructionList instructionList4 = this.compileDefaultRecursion(classGenerator, methodGenerator, instructionHandle);
        InstructionHandle instructionHandle2 = instructionList4.getStart();
        InstructionList instructionList5 = this.compileDefaultText(classGenerator, methodGenerator, instructionHandle);
        InstructionHandle instructionHandle3 = instructionList5.getStart();
        int[] arrn = new int[14 + vector.size()];
        int n4 = 0;
        while (n4 < arrn.length) {
            arrn[n4] = n4++;
        }
        boolean[] arrbl = new boolean[arrn.length];
        boolean[] arrbl2 = new boolean[arrn.length];
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            object3 = (String)vector.elementAt(i2);
            arrbl[i2 + 14] = Mode.isAttributeName((String)object3);
            arrbl2[i2 + 14] = Mode.isNamespaceName((String)object3);
        }
        this.compileTemplates(classGenerator, methodGenerator, instructionHandle);
        TestSeq testSeq = this._testSeq[1];
        object3 = instructionHandle2;
        if (testSeq != null) {
            object3 = testSeq.compile(classGenerator, methodGenerator, instructionHandle2);
        }
        TestSeq testSeq2 = this._testSeq[2];
        InstructionHandle instructionHandle4 = instructionHandle3;
        if (testSeq2 != null) {
            instructionHandle4 = testSeq2.compile(classGenerator, methodGenerator, instructionHandle4);
        }
        InstructionList instructionList6 = null;
        if (this._idxTestSeq != null) {
            branchHandle2.setTarget(this._idxTestSeq.compile(classGenerator, methodGenerator, instructionList2.getStart()));
            instructionList6 = this._idxTestSeq.getInstructionList();
        } else {
            branchHandle2.setTarget(instructionList2.getStart());
        }
        if (this._childNodeTestSeq != null) {
            double d2 = this._childNodeTestSeq.getPriority();
            int n5 = this._childNodeTestSeq.getPosition();
            double d3 = -1.7976931348623157E308;
            n3 = Integer.MIN_VALUE;
            if (testSeq != null) {
                d3 = testSeq.getPriority();
                n3 = testSeq.getPosition();
            }
            if (d3 == Double.NaN || d3 < d2 || d3 == d2 && n3 < n5) {
                object3 = this._childNodeTestSeq.compile(classGenerator, methodGenerator, instructionHandle);
            }
            object2 = this._testSeq[3];
            double d4 = -1.7976931348623157E308;
            int n6 = Integer.MIN_VALUE;
            if (object2 != null) {
                d4 = object2.getPriority();
                n6 = object2.getPosition();
            }
            if (Double.isNaN(d4) || d4 < d2 || d4 == d2 && n6 < n5) {
                instructionHandle3 = this._childNodeTestSeq.compile(classGenerator, methodGenerator, instructionHandle);
                this._testSeq[3] = this._childNodeTestSeq;
            }
        }
        Object object4 = object3;
        InstructionList instructionList7 = this.compileNamespaces(classGenerator, methodGenerator, arrbl2, arrbl, false, (InstructionHandle)object3);
        if (instructionList7 != null) {
            object4 = instructionList7.getStart();
        }
        InstructionHandle instructionHandle5 = instructionHandle4;
        InstructionList instructionList8 = this.compileNamespaces(classGenerator, methodGenerator, arrbl2, arrbl, true, instructionHandle4);
        if (instructionList8 != null) {
            instructionHandle5 = instructionList8.getStart();
        }
        InstructionHandle[] arrinstructionHandle = new InstructionHandle[arrn.length];
        for (n3 = 14; n3 < arrinstructionHandle.length; ++n3) {
            object2 = this._testSeq[n3];
            if (arrbl2[n3]) {
                if (arrbl[n3]) {
                    arrinstructionHandle[n3] = instructionHandle5;
                    continue;
                }
                arrinstructionHandle[n3] = object4;
                continue;
            }
            if (object2 != null) {
                if (arrbl[n3]) {
                    arrinstructionHandle[n3] = object2.compile(classGenerator, methodGenerator, instructionHandle5);
                    continue;
                }
                arrinstructionHandle[n3] = object2.compile(classGenerator, methodGenerator, (InstructionHandle)object4);
                continue;
            }
            arrinstructionHandle[n3] = instructionHandle;
        }
        arrinstructionHandle[0] = this._rootPattern != null ? this.getTemplateInstructionHandle(this._rootPattern.getTemplate()) : instructionHandle2;
        arrinstructionHandle[9] = this._rootPattern != null ? this.getTemplateInstructionHandle(this._rootPattern.getTemplate()) : instructionHandle2;
        arrinstructionHandle[3] = this._testSeq[3] != null ? this._testSeq[3].compile(classGenerator, methodGenerator, instructionHandle3) : instructionHandle3;
        arrinstructionHandle[13] = instructionHandle;
        arrinstructionHandle[1] = object4;
        arrinstructionHandle[2] = instructionHandle5;
        Object object5 = instructionHandle;
        if (this._childNodeTestSeq != null) {
            object5 = object3;
        }
        arrinstructionHandle[7] = this._testSeq[7] != null ? this._testSeq[7].compile(classGenerator, methodGenerator, (InstructionHandle)object5) : object5;
        object2 = instructionHandle;
        if (this._childNodeTestSeq != null) {
            object2 = object3;
        }
        arrinstructionHandle[8] = this._testSeq[8] != null ? this._testSeq[8].compile(classGenerator, methodGenerator, (InstructionHandle)object2) : object2;
        arrinstructionHandle[4] = instructionHandle;
        arrinstructionHandle[11] = instructionHandle;
        arrinstructionHandle[10] = instructionHandle;
        arrinstructionHandle[6] = instructionHandle;
        arrinstructionHandle[5] = instructionHandle;
        arrinstructionHandle[12] = instructionHandle;
        for (n2 = 14; n2 < arrinstructionHandle.length; ++n2) {
            object = this._testSeq[n2];
            if (object == null || arrbl2[n2]) {
                if (arrbl[n2]) {
                    arrinstructionHandle[n2] = instructionHandle5;
                    continue;
                }
                arrinstructionHandle[n2] = object4;
                continue;
            }
            arrinstructionHandle[n2] = arrbl[n2] ? object.compile(classGenerator, methodGenerator, instructionHandle5) : object.compile(classGenerator, methodGenerator, (InstructionHandle)object4);
        }
        if (instructionList6 != null) {
            instructionList2.insert(instructionList6);
        }
        n2 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
        instructionList2.append(methodGenerator.loadDOM());
        instructionList2.append(new ILOAD(this._currentIndex));
        instructionList2.append(new INVOKEINTERFACE(n2, 2));
        object = instructionList2.append(new SWITCH(arrn, arrinstructionHandle, instructionHandle));
        this.appendTestSequences(instructionList2);
        this.appendTemplateCode(instructionList2);
        if (instructionList7 != null) {
            instructionList2.append(instructionList7);
        }
        if (instructionList8 != null) {
            instructionList2.append(instructionList8);
        }
        instructionList2.append(instructionList4);
        instructionList2.append(instructionList5);
        instructionList.append(instructionList2);
        instructionList.append(instructionList3);
        this.peepHoleOptimization(methodGenerator);
        classGenerator.addMethod(methodGenerator);
        if (this._importLevels != null) {
            Enumeration enumeration = this._importLevels.keys();
            while (enumeration.hasMoreElements()) {
                Integer n7 = (Integer)enumeration.nextElement();
                Integer n8 = (Integer)this._importLevels.get(n7);
                this.compileApplyImports(classGenerator, n8, n7);
            }
        }
    }

    private void compileTemplateCalls(ClassGenerator classGenerator, MethodGenerator methodGenerator, InstructionHandle instructionHandle, int n2, int n3) {
        Enumeration enumeration = this._neededTemplates.keys();
        while (enumeration.hasMoreElements()) {
            Template template = (Template)enumeration.nextElement();
            int n4 = template.getImportPrecedence();
            if (n4 < n2 || n4 >= n3) continue;
            if (template.hasContents()) {
                InstructionList instructionList = template.compile(classGenerator, methodGenerator);
                instructionList.append(new GOTO_W(instructionHandle));
                this._templateILs.put(template, instructionList);
                this._templateIHs.put(template, instructionList.getStart());
                continue;
            }
            this._templateIHs.put(template, instructionHandle);
        }
    }

    public void compileApplyImports(ClassGenerator classGenerator, int n2, int n3) {
        int n4;
        Object object;
        Type[] arrtype;
        int n5;
        Object object2;
        Object object3;
        XSLTC xSLTC = classGenerator.getParser().getXSLTC();
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        Vector vector = xSLTC.getNamesIndex();
        this._namedTemplates = new Hashtable();
        this._neededTemplates = new Hashtable();
        this._templateIHs = new Hashtable();
        this._templateILs = new Hashtable();
        this._patternGroups = new Vector[32];
        this._rootPattern = null;
        Vector vector2 = this._templates;
        this._templates = new Vector();
        Enumeration enumeration = vector2.elements();
        while (enumeration.hasMoreElements()) {
            arrtype = (Type[])enumeration.nextElement();
            int n6 = arrtype.getImportPrecedence();
            if (n6 < n2 || n6 >= n3) continue;
            this.addTemplate((Template)arrtype);
        }
        this.processPatterns(this._keys);
        arrtype = new Type[]{Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), Util.getJCRefType(TRANSLET_OUTPUT_SIG), Type.INT};
        String[] arrstring = new String[]{"document", "iterator", "handler", "node"};
        InstructionList instructionList = new InstructionList();
        MethodGenerator methodGenerator = new MethodGenerator(17, Type.VOID, arrtype, arrstring, this.functionName() + '_' + n3, this.getClassName(), instructionList, classGenerator.getConstantPool());
        methodGenerator.addException("org.apache.xalan.xsltc.TransletException");
        LocalVariableGen localVariableGen = methodGenerator.addLocalVariable2("current", Type.INT, null);
        this._currentIndex = localVariableGen.getIndex();
        instructionList.append(new ILOAD(methodGenerator.getLocalIndex("node")));
        localVariableGen.setStart(instructionList.append(new ISTORE(this._currentIndex)));
        InstructionList instructionList2 = new InstructionList();
        instructionList2.append(NOP);
        InstructionList instructionList3 = new InstructionList();
        instructionList3.append(RETURN);
        InstructionHandle instructionHandle = instructionList3.getStart();
        InstructionList instructionList4 = this.compileDefaultRecursion(classGenerator, methodGenerator, instructionHandle);
        InstructionHandle instructionHandle2 = instructionList4.getStart();
        InstructionList instructionList5 = this.compileDefaultText(classGenerator, methodGenerator, instructionHandle);
        InstructionHandle instructionHandle3 = instructionList5.getStart();
        int[] arrn = new int[14 + vector.size()];
        int n7 = 0;
        while (n7 < arrn.length) {
            arrn[n7] = n7++;
        }
        boolean[] arrbl = new boolean[arrn.length];
        boolean[] arrbl2 = new boolean[arrn.length];
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            object3 = (String)vector.elementAt(i2);
            arrbl[i2 + 14] = Mode.isAttributeName((String)object3);
            arrbl2[i2 + 14] = Mode.isNamespaceName((String)object3);
        }
        this.compileTemplateCalls(classGenerator, methodGenerator, instructionHandle, n2, n3);
        TestSeq testSeq = this._testSeq[1];
        object3 = instructionHandle2;
        if (testSeq != null) {
            object3 = testSeq.compile(classGenerator, methodGenerator, instructionHandle);
        }
        TestSeq testSeq2 = this._testSeq[2];
        InstructionHandle instructionHandle4 = instructionHandle;
        if (testSeq2 != null) {
            instructionHandle4 = testSeq2.compile(classGenerator, methodGenerator, instructionHandle4);
        }
        InstructionList instructionList6 = null;
        if (this._idxTestSeq != null) {
            instructionList6 = this._idxTestSeq.getInstructionList();
        }
        if (this._childNodeTestSeq != null) {
            double d2 = this._childNodeTestSeq.getPriority();
            int n8 = this._childNodeTestSeq.getPosition();
            double d3 = -1.7976931348623157E308;
            n4 = Integer.MIN_VALUE;
            if (testSeq != null) {
                d3 = testSeq.getPriority();
                n4 = testSeq.getPosition();
            }
            if (d3 == Double.NaN || d3 < d2 || d3 == d2 && n4 < n8) {
                object3 = this._childNodeTestSeq.compile(classGenerator, methodGenerator, instructionHandle);
            }
            object = this._testSeq[3];
            double d4 = -1.7976931348623157E308;
            int n9 = Integer.MIN_VALUE;
            if (object != null) {
                d4 = object.getPriority();
                n9 = object.getPosition();
            }
            if (Double.isNaN(d4) || d4 < d2 || d4 == d2 && n9 < n8) {
                instructionHandle3 = this._childNodeTestSeq.compile(classGenerator, methodGenerator, instructionHandle);
                this._testSeq[3] = this._childNodeTestSeq;
            }
        }
        Object object4 = object3;
        InstructionList instructionList7 = this.compileNamespaces(classGenerator, methodGenerator, arrbl2, arrbl, false, (InstructionHandle)object3);
        if (instructionList7 != null) {
            object4 = instructionList7.getStart();
        }
        InstructionList instructionList8 = this.compileNamespaces(classGenerator, methodGenerator, arrbl2, arrbl, true, instructionHandle4);
        InstructionHandle instructionHandle5 = instructionHandle4;
        if (instructionList8 != null) {
            instructionHandle5 = instructionList8.getStart();
        }
        InstructionHandle[] arrinstructionHandle = new InstructionHandle[arrn.length];
        for (n4 = 14; n4 < arrinstructionHandle.length; ++n4) {
            object = this._testSeq[n4];
            if (arrbl2[n4]) {
                if (arrbl[n4]) {
                    arrinstructionHandle[n4] = instructionHandle5;
                    continue;
                }
                arrinstructionHandle[n4] = object4;
                continue;
            }
            if (object != null) {
                if (arrbl[n4]) {
                    arrinstructionHandle[n4] = object.compile(classGenerator, methodGenerator, instructionHandle5);
                    continue;
                }
                arrinstructionHandle[n4] = object.compile(classGenerator, methodGenerator, (InstructionHandle)object4);
                continue;
            }
            arrinstructionHandle[n4] = instructionHandle;
        }
        arrinstructionHandle[0] = this._rootPattern != null ? this.getTemplateInstructionHandle(this._rootPattern.getTemplate()) : instructionHandle2;
        arrinstructionHandle[9] = this._rootPattern != null ? this.getTemplateInstructionHandle(this._rootPattern.getTemplate()) : instructionHandle2;
        arrinstructionHandle[3] = this._testSeq[3] != null ? this._testSeq[3].compile(classGenerator, methodGenerator, instructionHandle3) : instructionHandle3;
        arrinstructionHandle[13] = instructionHandle;
        arrinstructionHandle[1] = object4;
        arrinstructionHandle[2] = instructionHandle5;
        Object object5 = instructionHandle;
        if (this._childNodeTestSeq != null) {
            object5 = object3;
        }
        arrinstructionHandle[7] = this._testSeq[7] != null ? this._testSeq[7].compile(classGenerator, methodGenerator, (InstructionHandle)object5) : object5;
        object = instructionHandle;
        if (this._childNodeTestSeq != null) {
            object = object3;
        }
        arrinstructionHandle[8] = this._testSeq[8] != null ? this._testSeq[8].compile(classGenerator, methodGenerator, (InstructionHandle)object) : object;
        arrinstructionHandle[4] = instructionHandle;
        arrinstructionHandle[11] = instructionHandle;
        arrinstructionHandle[10] = instructionHandle;
        arrinstructionHandle[6] = instructionHandle;
        arrinstructionHandle[5] = instructionHandle;
        arrinstructionHandle[12] = instructionHandle;
        for (n5 = 14; n5 < arrinstructionHandle.length; ++n5) {
            object2 = this._testSeq[n5];
            if (object2 == null || arrbl2[n5]) {
                if (arrbl[n5]) {
                    arrinstructionHandle[n5] = instructionHandle5;
                    continue;
                }
                arrinstructionHandle[n5] = object4;
                continue;
            }
            arrinstructionHandle[n5] = arrbl[n5] ? object2.compile(classGenerator, methodGenerator, instructionHandle5) : object2.compile(classGenerator, methodGenerator, (InstructionHandle)object4);
        }
        if (instructionList6 != null) {
            instructionList2.insert(instructionList6);
        }
        n5 = constantPoolGen.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
        instructionList2.append(methodGenerator.loadDOM());
        instructionList2.append(new ILOAD(this._currentIndex));
        instructionList2.append(new INVOKEINTERFACE(n5, 2));
        object2 = instructionList2.append(new SWITCH(arrn, arrinstructionHandle, instructionHandle));
        this.appendTestSequences(instructionList2);
        this.appendTemplateCode(instructionList2);
        if (instructionList7 != null) {
            instructionList2.append(instructionList7);
        }
        if (instructionList8 != null) {
            instructionList2.append(instructionList8);
        }
        instructionList2.append(instructionList4);
        instructionList2.append(instructionList5);
        instructionList.append(instructionList2);
        localVariableGen.setEnd(instructionList2.getEnd());
        instructionList.append(instructionList3);
        this.peepHoleOptimization(methodGenerator);
        classGenerator.addMethod(methodGenerator);
        this._templates = vector2;
    }

    private void peepHoleOptimization(MethodGenerator methodGenerator) {
        LocalVariableInstruction localVariableInstruction;
        InstructionHandle[] arrinstructionHandle;
        LocalVariableInstruction localVariableInstruction2;
        InstructionList instructionList = methodGenerator.getInstructionList();
        InstructionFinder instructionFinder = new InstructionFinder(instructionList);
        String string = "LoadInstruction POP";
        Iterator iterator = instructionFinder.search(string);
        while (iterator.hasNext()) {
            arrinstructionHandle = (InstructionHandle[])iterator.next();
            try {
                if (arrinstructionHandle[0].hasTargeters() || arrinstructionHandle[1].hasTargeters()) continue;
                instructionList.delete(arrinstructionHandle[0], arrinstructionHandle[1]);
            }
            catch (TargetLostException targetLostException) {}
        }
        string = "ILOAD ILOAD SWAP ISTORE";
        iterator = instructionFinder.search(string);
        while (iterator.hasNext()) {
            arrinstructionHandle = (InstructionHandle[])iterator.next();
            try {
                localVariableInstruction2 = (ILOAD)arrinstructionHandle[0].getInstruction();
                localVariableInstruction = (ILOAD)arrinstructionHandle[1].getInstruction();
                ISTORE iSTORE = (ISTORE)arrinstructionHandle[3].getInstruction();
                if (arrinstructionHandle[1].hasTargeters() || arrinstructionHandle[2].hasTargeters() || arrinstructionHandle[3].hasTargeters() || localVariableInstruction2.getIndex() != localVariableInstruction.getIndex() || localVariableInstruction.getIndex() != iSTORE.getIndex()) continue;
                instructionList.delete(arrinstructionHandle[1], arrinstructionHandle[3]);
            }
            catch (TargetLostException targetLostException) {}
        }
        string = "LoadInstruction LoadInstruction SWAP";
        iterator = instructionFinder.search(string);
        while (iterator.hasNext()) {
            arrinstructionHandle = (InstructionHandle[])iterator.next();
            try {
                if (arrinstructionHandle[0].hasTargeters() || arrinstructionHandle[1].hasTargeters() || arrinstructionHandle[2].hasTargeters()) continue;
                localVariableInstruction2 = arrinstructionHandle[1].getInstruction();
                instructionList.insert(arrinstructionHandle[0], localVariableInstruction2);
                instructionList.delete(arrinstructionHandle[1], arrinstructionHandle[2]);
            }
            catch (TargetLostException targetLostException) {}
        }
        string = "ALOAD ALOAD";
        iterator = instructionFinder.search(string);
        while (iterator.hasNext()) {
            arrinstructionHandle = (InstructionHandle[])iterator.next();
            try {
                if (arrinstructionHandle[1].hasTargeters()) continue;
                localVariableInstruction2 = (ALOAD)arrinstructionHandle[0].getInstruction();
                localVariableInstruction = (ALOAD)arrinstructionHandle[1].getInstruction();
                if (localVariableInstruction2.getIndex() != localVariableInstruction.getIndex()) continue;
                instructionList.insert(arrinstructionHandle[1], new DUP());
                instructionList.delete(arrinstructionHandle[1]);
            }
            catch (TargetLostException targetLostException) {}
        }
    }

    public InstructionHandle getTemplateInstructionHandle(Template template) {
        return (InstructionHandle)this._templateIHs.get(template);
    }

    private static boolean isAttributeName(String string) {
        int n2 = string.lastIndexOf(58) + 1;
        return string.charAt(n2) == '@';
    }

    private static boolean isNamespaceName(String string) {
        int n2 = string.lastIndexOf(58);
        return n2 > -1 && string.charAt(string.length() - 1) == '*';
    }
}

