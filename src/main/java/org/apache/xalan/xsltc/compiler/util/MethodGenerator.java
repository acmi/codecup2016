/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DSTORE;
import org.apache.bcel.generic.FLOAD;
import org.apache.bcel.generic.FSTORE;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.IndexedInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionTargeter;
import org.apache.bcel.generic.LLOAD;
import org.apache.bcel.generic.LSTORE;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.TargetLostException;
import org.apache.bcel.generic.Type;
import org.apache.xalan.xsltc.compiler.Constants;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.InternalError;
import org.apache.xalan.xsltc.compiler.util.MarkerInstruction;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.OutlineableChunkEnd;
import org.apache.xalan.xsltc.compiler.util.OutlineableChunkStart;
import org.apache.xalan.xsltc.compiler.util.SlotAllocator;

public class MethodGenerator
extends MethodGen
implements Constants {
    protected static final int INVALID_INDEX = -1;
    private static final String START_ELEMENT_SIG = "(Ljava/lang/String;)V";
    private static final String END_ELEMENT_SIG = "(Ljava/lang/String;)V";
    private InstructionList _mapTypeSub;
    private static final int DOM_INDEX = 1;
    private static final int ITERATOR_INDEX = 2;
    private static final int HANDLER_INDEX = 3;
    private static final int MAX_METHOD_SIZE = 65535;
    private static final int MAX_BRANCH_TARGET_OFFSET = 32767;
    private static final int MIN_BRANCH_TARGET_OFFSET = -32768;
    private static final int TARGET_METHOD_SIZE = 60000;
    private static final int MINIMUM_OUTLINEABLE_CHUNK_SIZE = 1000;
    private Instruction _iloadCurrent;
    private Instruction _istoreCurrent;
    private final Instruction _astoreHandler = new ASTORE(3);
    private final Instruction _aloadHandler = new ALOAD(3);
    private final Instruction _astoreIterator = new ASTORE(2);
    private final Instruction _aloadIterator = new ALOAD(2);
    private final Instruction _aloadDom = new ALOAD(1);
    private final Instruction _astoreDom = new ASTORE(1);
    private final Instruction _startElement;
    private final Instruction _endElement;
    private final Instruction _startDocument;
    private final Instruction _endDocument;
    private final Instruction _attribute;
    private final Instruction _uniqueAttribute;
    private final Instruction _namespace;
    private final Instruction _setStartNode;
    private final Instruction _reset;
    private final Instruction _nextNode;
    private SlotAllocator _slotAllocator;
    private boolean _allocatorInit = false;
    private LocalVariableRegistry _localVariableRegistry;
    private Hashtable _preCompiled = new Hashtable();
    private int m_totalChunks = 0;
    private int m_openChunks = 0;

    public MethodGenerator(int n2, Type type, Type[] arrtype, String[] arrstring, String string, String string2, InstructionList instructionList, ConstantPoolGen constantPoolGen) {
        super(n2, type, arrtype, arrstring, string, string2, instructionList, constantPoolGen);
        int n3 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "startElement", "(Ljava/lang/String;)V");
        this._startElement = new INVOKEINTERFACE(n3, 2);
        int n4 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "endElement", "(Ljava/lang/String;)V");
        this._endElement = new INVOKEINTERFACE(n4, 2);
        int n5 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "addAttribute", "(Ljava/lang/String;Ljava/lang/String;)V");
        this._attribute = new INVOKEINTERFACE(n5, 3);
        int n6 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "addUniqueAttribute", "(Ljava/lang/String;Ljava/lang/String;I)V");
        this._uniqueAttribute = new INVOKEINTERFACE(n6, 4);
        int n7 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "namespaceAfterStartElement", "(Ljava/lang/String;Ljava/lang/String;)V");
        this._namespace = new INVOKEINTERFACE(n7, 3);
        int n8 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "startDocument", "()V");
        this._startDocument = new INVOKEINTERFACE(n8, 1);
        n8 = constantPoolGen.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "endDocument", "()V");
        this._endDocument = new INVOKEINTERFACE(n8, 1);
        n8 = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "setStartNode", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
        this._setStartNode = new INVOKEINTERFACE(n8, 2);
        n8 = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "reset", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
        this._reset = new INVOKEINTERFACE(n8, 1);
        n8 = constantPoolGen.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "next", "()I");
        this._nextNode = new INVOKEINTERFACE(n8, 1);
        this._slotAllocator = new SlotAllocator();
        this._slotAllocator.initialize(this.getLocalVariableRegistry().getLocals(false));
        this._allocatorInit = true;
    }

    public LocalVariableGen addLocalVariable(String string, Type type, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        LocalVariableGen localVariableGen;
        if (this._allocatorInit) {
            localVariableGen = this.addLocalVariable2(string, type, instructionHandle);
        } else {
            localVariableGen = super.addLocalVariable(string, type, instructionHandle, instructionHandle2);
            this.getLocalVariableRegistry().registerLocalVariable(localVariableGen);
        }
        return localVariableGen;
    }

    public LocalVariableGen addLocalVariable2(String string, Type type, InstructionHandle instructionHandle) {
        LocalVariableGen localVariableGen = super.addLocalVariable(string, type, this._slotAllocator.allocateSlot(type), instructionHandle, null);
        this.getLocalVariableRegistry().registerLocalVariable(localVariableGen);
        return localVariableGen;
    }

    private LocalVariableRegistry getLocalVariableRegistry() {
        if (this._localVariableRegistry == null) {
            this._localVariableRegistry = new LocalVariableRegistry(this);
        }
        return this._localVariableRegistry;
    }

    boolean offsetInLocalVariableGenRange(LocalVariableGen localVariableGen, int n2) {
        InstructionHandle instructionHandle = localVariableGen.getStart();
        InstructionHandle instructionHandle2 = localVariableGen.getEnd();
        if (instructionHandle == null) {
            instructionHandle = this.getInstructionList().getStart();
        }
        if (instructionHandle2 == null) {
            instructionHandle2 = this.getInstructionList().getEnd();
        }
        return instructionHandle.getPosition() <= n2 && instructionHandle2.getPosition() + instructionHandle2.getInstruction().getLength() >= n2;
    }

    public void removeLocalVariable(LocalVariableGen localVariableGen) {
        this._slotAllocator.releaseSlot(localVariableGen);
        this.getLocalVariableRegistry().removeByNameTracking(localVariableGen);
        super.removeLocalVariable(localVariableGen);
    }

    public Instruction loadDOM() {
        return this._aloadDom;
    }

    public Instruction storeDOM() {
        return this._astoreDom;
    }

    public Instruction storeHandler() {
        return this._astoreHandler;
    }

    public Instruction loadHandler() {
        return this._aloadHandler;
    }

    public Instruction storeIterator() {
        return this._astoreIterator;
    }

    public Instruction loadIterator() {
        return this._aloadIterator;
    }

    public final Instruction setStartNode() {
        return this._setStartNode;
    }

    public final Instruction reset() {
        return this._reset;
    }

    public final Instruction nextNode() {
        return this._nextNode;
    }

    public final Instruction startElement() {
        return this._startElement;
    }

    public final Instruction endElement() {
        return this._endElement;
    }

    public final Instruction startDocument() {
        return this._startDocument;
    }

    public final Instruction endDocument() {
        return this._endDocument;
    }

    public final Instruction attribute() {
        return this._attribute;
    }

    public final Instruction uniqueAttribute() {
        return this._uniqueAttribute;
    }

    public final Instruction namespace() {
        return this._namespace;
    }

    public Instruction loadCurrentNode() {
        if (this._iloadCurrent == null) {
            int n2 = this.getLocalIndex("current");
            this._iloadCurrent = n2 > 0 ? new ILOAD(n2) : new ICONST(0);
        }
        return this._iloadCurrent;
    }

    public Instruction storeCurrentNode() {
        Instruction instruction = this._istoreCurrent != null ? this._istoreCurrent : (this._istoreCurrent = new ISTORE(this.getLocalIndex("current")));
        return instruction;
    }

    public Instruction loadContextNode() {
        return this.loadCurrentNode();
    }

    public Instruction storeContextNode() {
        return this.storeCurrentNode();
    }

    public int getLocalIndex(String string) {
        return this.getLocalVariable(string).getIndex();
    }

    public LocalVariableGen getLocalVariable(String string) {
        return this.getLocalVariableRegistry().lookUpByName(string);
    }

    public void setMaxLocals() {
        int n2 = super.getMaxLocals();
        LocalVariableGen[] arrlocalVariableGen = super.getLocalVariables();
        if (arrlocalVariableGen != null && arrlocalVariableGen.length > n2) {
            n2 = arrlocalVariableGen.length;
        }
        if (n2 < 5) {
            n2 = 5;
        }
        super.setMaxLocals(n2);
    }

    public void addInstructionList(Pattern pattern, InstructionList instructionList) {
        this._preCompiled.put(pattern, instructionList);
    }

    public InstructionList getInstructionList(Pattern pattern) {
        return (InstructionList)this._preCompiled.get(pattern);
    }

    private ArrayList getCandidateChunks(ClassGenerator classGenerator, int n2) {
        InstructionHandle instructionHandle;
        Iterator iterator = this.getInstructionList().iterator();
        ArrayList<Object> arrayList = new ArrayList<Object>();
        ArrayList arrayList2 = new ArrayList();
        Stack<ArrayList> stack = new Stack<ArrayList>();
        boolean bl = false;
        boolean bl2 = true;
        if (this.m_openChunks != 0) {
            String string = new ErrorMsg("OUTLINE_ERR_UNBALANCED_MARKERS").toString();
            throw new InternalError(string);
        }
        do {
            InstructionHandle instructionHandle2;
            Instruction instruction;
            int n3;
            int n4;
            instructionHandle = iterator.hasNext() ? (InstructionHandle)iterator.next() : null;
            Instruction instruction2 = instruction = instructionHandle != null ? instructionHandle.getInstruction() : null;
            if (bl2) {
                bl = true;
                arrayList2.add((InstructionHandle)instructionHandle);
                bl2 = false;
            }
            if (instruction instanceof OutlineableChunkStart) {
                if (bl) {
                    stack.push(arrayList2);
                    arrayList2 = new ArrayList();
                }
                bl = true;
                arrayList2.add((InstructionHandle)instructionHandle);
                continue;
            }
            if (instructionHandle != null && !(instruction instanceof OutlineableChunkEnd)) continue;
            ArrayList arrayList3 = null;
            if (!bl) {
                arrayList3 = arrayList2;
                arrayList2 = (ArrayList)stack.pop();
            }
            if ((n4 = (n3 = instructionHandle != null ? instructionHandle.getPosition() : n2) - (instructionHandle2 = (InstructionHandle)arrayList2.get(arrayList2.size() - 1)).getPosition()) <= 60000) {
                arrayList2.add(instructionHandle);
            } else {
                int n5;
                if (!bl && (n5 = arrayList3.size() / 2) > 0) {
                    Object object;
                    reference var18_20;
                    Chunk[] arrchunk = new Chunk[n5];
                    for (int i2 = 0; i2 < n5; ++i2) {
                        var18_20 = (reference)((InstructionHandle)arrayList3.get(i2 * 2));
                        object = (InstructionHandle)arrayList3.get(i2 * 2 + 1);
                        arrchunk[i2] = new Chunk(var18_20, (InstructionHandle)object);
                    }
                    ArrayList arrayList4 = this.mergeAdjacentChunks(arrchunk);
                    for (var18_20 = false; var18_20 < arrayList4.size(); ++var18_20) {
                        object = (Chunk)arrayList4.get((int)var18_20);
                        int n6 = object.getChunkSize();
                        if (n6 < 1000 || n6 > 60000) continue;
                        arrayList.add(object);
                    }
                }
                arrayList2.remove(arrayList2.size() - 1);
            }
            boolean bl3 = bl = (arrayList2.size() & 1) == 1;
        } while (instructionHandle != null);
        return arrayList;
    }

    private ArrayList mergeAdjacentChunks(Chunk[] arrchunk) {
        int n2;
        int n3;
        int[] arrn = new int[arrchunk.length];
        int[] arrn2 = new int[arrchunk.length];
        boolean[] arrbl = new boolean[arrchunk.length];
        int n4 = 0;
        int n5 = 0;
        ArrayList<Chunk> arrayList = new ArrayList<Chunk>();
        int n6 = 0;
        for (n2 = 1; n2 < arrchunk.length; ++n2) {
            if (arrchunk[n2 - 1].isAdjacentTo(arrchunk[n2])) continue;
            n3 = n2 - n6;
            if (n4 < n3) {
                n4 = n3;
            }
            if (n3 > 1) {
                arrn2[n5] = n3;
                arrn[n5] = n6;
                ++n5;
            }
            n6 = n2;
        }
        if (arrchunk.length - n6 > 1) {
            n2 = arrchunk.length - n6;
            if (n4 < n2) {
                n4 = n2;
            }
            arrn2[n5] = arrchunk.length - n6;
            arrn[n5] = n6;
            ++n5;
        }
        for (n2 = n4; n2 > 1; --n2) {
            for (n3 = 0; n3 < n5; ++n3) {
                int n7 = arrn[n3];
                int n8 = n7 + arrn2[n3] - 1;
                boolean bl = false;
                int n9 = n7;
                while (n9 + n2 - 1 <= n8 && !bl) {
                    int n10;
                    int n11 = n9 + n2 - 1;
                    int n12 = 0;
                    for (n10 = n9; n10 <= n11; ++n10) {
                        n12 += arrchunk[n10].getChunkSize();
                    }
                    if (n12 <= 60000) {
                        bl = true;
                        for (n10 = n9; n10 <= n11; ++n10) {
                            arrbl[n10] = true;
                        }
                        arrayList.add(new Chunk(arrchunk[n9].getChunkStart(), arrchunk[n11].getChunkEnd()));
                        arrn2[n3] = arrn[n3] - n9;
                        n10 = n8 - n11;
                        if (n10 >= 2) {
                            arrn[n5] = n11 + 1;
                            arrn2[n5] = n10;
                            ++n5;
                        }
                    }
                    ++n9;
                }
            }
        }
        for (n2 = 0; n2 < arrchunk.length; ++n2) {
            if (arrbl[n2]) continue;
            arrayList.add(arrchunk[n2]);
        }
        return arrayList;
    }

    public Method[] outlineChunks(ClassGenerator classGenerator, int n2) {
        boolean bl;
        Method[] arrmethod;
        ArrayList<Method> arrayList = new ArrayList<Method>();
        int n3 = n2;
        int n4 = 0;
        String string = this.getName();
        if (string.equals("<init>")) {
            string = "$lt$init$gt$";
        } else if (string.equals("<clinit>")) {
            string = "$lt$clinit$gt$";
        }
        do {
            arrmethod = this.getCandidateChunks(classGenerator, n3);
            Collections.sort(arrmethod);
            bl = false;
            for (int i2 = arrmethod.size() - 1; i2 >= 0 && n3 > 60000; --i2) {
                Chunk chunk = (Chunk)arrmethod.get(i2);
                arrayList.add(this.outline(chunk.getChunkStart(), chunk.getChunkEnd(), string + "$outline$" + n4, classGenerator));
                ++n4;
                bl = true;
                InstructionList instructionList = this.getInstructionList();
                InstructionHandle instructionHandle = instructionList.getEnd();
                instructionList.setPositions();
                n3 = instructionHandle.getPosition() + instructionHandle.getInstruction().getLength();
            }
        } while (bl && n3 > 60000);
        if (n3 > 65535) {
            arrmethod = new ErrorMsg("OUTLINE_ERR_METHOD_TOO_BIG").toString();
            throw new InternalError((String)arrmethod);
        }
        arrmethod = new Method[arrayList.size() + 1];
        arrayList.toArray(arrmethod);
        arrmethod[arrayList.size()] = this.getThisMethod();
        return arrmethod;
    }

    private Method outline(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, String string, ClassGenerator classGenerator) {
        Iterator iterator;
        InstructionHandle instructionHandle3;
        boolean bl;
        InstructionTargeter[] arrinstructionTargeter;
        Object object;
        void var35_38;
        int n2;
        Object localVariableGen;
        Object object2;
        String[] targetLostException;
        Object object3;
        reference var44_61;
        Object object4;
        InstructionHandle instructionHandle4;
        if (this.getExceptionHandlers().length != 0) {
            String string2 = new ErrorMsg("OUTLINE_ERR_TRY_CATCH").toString();
            throw new InternalError(string2);
        }
        int n22 = instructionHandle.getPosition();
        int n3 = instructionHandle2.getPosition() + instructionHandle2.getInstruction().getLength();
        ConstantPoolGen constantPoolGen = this.getConstantPool();
        InstructionList instructionList = new InstructionList();
        XSLTC xSLTC = classGenerator.getParser().getXSLTC();
        String string3 = xSLTC.getHelperClassName();
        Type[] arrtype = new Type[]{new ObjectType(string3).toJCType()};
        String string4 = "copyLocals";
        String[] arrstring2 = new String[]{"copyLocals"};
        int n4 = 18;
        boolean bl2 = bl = (this.getAccessFlags() & 8) != 0;
        if (bl) {
            n4 |= 8;
        }
        MethodGenerator methodGenerator = new MethodGenerator(n4, Type.VOID, arrtype, arrstring2, string, this.getClassName(), instructionList, constantPoolGen);
        ClassGenerator classGenerator2 = new ClassGenerator(this, string3, "java.lang.Object", string3 + ".java", 49, null, classGenerator.getStylesheet()){
            private final MethodGenerator this$0;

            public boolean isExternal() {
                return true;
            }
        };
        ConstantPoolGen constantPoolGen2 = classGenerator2.getConstantPool();
        classGenerator2.addEmptyConstructor(1);
        int n5 = 0;
        InstructionHandle instructionHandle5 = instructionHandle2.getNext();
        InstructionList instructionList2 = new InstructionList();
        InstructionList instructionList3 = new InstructionList();
        InstructionList instructionList4 = new InstructionList();
        InstructionList instructionList5 = new InstructionList();
        InstructionHandle instructionHandle6 = instructionList2.append(new NEW(constantPoolGen.addClass(string3)));
        instructionList2.append(InstructionConstants.DUP);
        instructionList2.append(InstructionConstants.DUP);
        instructionList2.append(new INVOKESPECIAL(constantPoolGen.addMethodref(string3, "<init>", "()V")));
        if (bl) {
            instructionHandle4 = instructionList3.append(new INVOKESTATIC(constantPoolGen.addMethodref(classGenerator.getClassName(), string, methodGenerator.getSignature())));
        } else {
            instructionList3.append(InstructionConstants.THIS);
            instructionList3.append(InstructionConstants.SWAP);
            instructionHandle4 = instructionList3.append(new INVOKEVIRTUAL(constantPoolGen.addMethodref(classGenerator.getClassName(), string, methodGenerator.getSignature())));
        }
        boolean bl3 = false;
        InstructionHandle instructionHandle7 = null;
        InstructionHandle instructionHandle8 = null;
        HashMap<InstructionHandle, InstructionHandle> hashMap = new HashMap<InstructionHandle, InstructionHandle>();
        HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
        HashMap<Object, InstructionHandle> hashMap3 = new HashMap<Object, InstructionHandle>();
        HashMap<Object, InstructionHandle> hashMap4 = new HashMap<Object, InstructionHandle>();
        for (instructionHandle3 = instructionHandle; instructionHandle3 != instructionHandle5; instructionHandle3 = instructionHandle3.getNext()) {
            Instruction instruction = instructionHandle3.getInstruction();
            if (instruction instanceof MarkerInstruction) {
                if (!instructionHandle3.hasTargeters()) continue;
                if (instruction instanceof OutlineableChunkEnd) {
                    hashMap.put(instructionHandle3, instructionHandle8);
                    continue;
                }
                if (bl3) continue;
                bl3 = true;
                instructionHandle7 = instructionHandle3;
                continue;
            }
            object2 = instruction.copy();
            instructionHandle8 = object2 instanceof BranchInstruction ? instructionList.append((BranchInstruction)object2) : instructionList.append((Instruction)object2);
            if (object2 instanceof LocalVariableInstruction || object2 instanceof RET) {
                iterator = (IndexedInstruction)object2;
                localVariableGen = iterator.getIndex();
                targetLostException = this.getLocalVariableRegistry().lookupRegisteredLocalVariable((int)localVariableGen, instructionHandle3.getPosition());
                object3 = (LocalVariableGen)hashMap2.get(targetLostException);
                if (hashMap2.get(targetLostException) == null) {
                    n2 = this.offsetInLocalVariableGenRange((LocalVariableGen)targetLostException, n22 != 0 ? n22 - 1 : 0);
                    object = this.offsetInLocalVariableGenRange((LocalVariableGen)targetLostException, n3 + 1);
                    if (n2 != 0 || object != 0) {
                        InstructionHandle instructionHandle9;
                        arrinstructionTargeter = targetLostException.getName();
                        var44_61 = targetLostException.getType();
                        object3 = methodGenerator.addLocalVariable((String)arrinstructionTargeter, var44_61, null, null);
                        object4 = object3.getIndex();
                        String string2 = var44_61.getSignature();
                        hashMap2.put(targetLostException, object3);
                        String string5 = "field" + ++n5;
                        classGenerator2.addField(new Field(1, constantPoolGen2.addUtf8(string5), constantPoolGen2.addUtf8(string2), null, constantPoolGen2.getConstantPool()));
                        int n6 = constantPoolGen.addFieldref(string3, string5, string2);
                        if (n2 != 0) {
                            instructionList2.append(InstructionConstants.DUP);
                            instructionHandle9 = instructionList2.append(MethodGenerator.loadLocal((int)localVariableGen, var44_61));
                            instructionList2.append(new PUTFIELD(n6));
                            if (object == false) {
                                hashMap4.put(targetLostException, instructionHandle9);
                            }
                            instructionList4.append(InstructionConstants.ALOAD_1);
                            instructionList4.append(new GETFIELD(n6));
                            instructionList4.append(MethodGenerator.storeLocal((int)object4, var44_61));
                        }
                        if (object != false) {
                            instructionList5.append(InstructionConstants.ALOAD_1);
                            instructionList5.append(MethodGenerator.loadLocal((int)object4, var44_61));
                            instructionList5.append(new PUTFIELD(n6));
                            instructionList3.append(InstructionConstants.DUP);
                            instructionList3.append(new GETFIELD(n6));
                            instructionHandle9 = instructionList3.append(MethodGenerator.storeLocal((int)localVariableGen, var44_61));
                            if (n2 == 0) {
                                hashMap3.put(targetLostException, instructionHandle9);
                            }
                        }
                    }
                }
            }
            if (instructionHandle3.hasTargeters()) {
                hashMap.put(instructionHandle3, instructionHandle8);
            }
            if (!bl3) continue;
            do {
                hashMap.put(instructionHandle7, instructionHandle8);
            } while ((instructionHandle7 = instructionHandle7.getNext()) != instructionHandle3);
            bl3 = false;
        }
        instructionHandle3 = instructionHandle;
        InstructionHandle instructionHandle10 = instructionList.getStart();
        while (var35_38 != null) {
            object2 = instructionHandle3.getInstruction();
            iterator = var35_38.getInstruction();
            if (object2 instanceof BranchInstruction) {
                BranchInstruction entry = (BranchInstruction)((Object)iterator);
                targetLostException = (BranchInstruction)object2;
                object3 = targetLostException.getTarget();
                InstructionHandle instructionHandle11 = (InstructionHandle)hashMap.get(object3);
                entry.setTarget(instructionHandle11);
                if (targetLostException instanceof Select) {
                    void var44_63;
                    InstructionHandle[] arrinstructionHandle = ((Select)targetLostException).getTargets();
                    arrinstructionTargeter = ((Select)entry).getTargets();
                    boolean bl4 = false;
                    while (++var44_63 < arrinstructionHandle.length) {
                        arrinstructionTargeter[var44_63] = (InstructionHandle)hashMap.get(arrinstructionHandle[var44_63]);
                    }
                }
            } else if (object2 instanceof LocalVariableInstruction || object2 instanceof RET) {
                IndexedInstruction instructionList6 = (IndexedInstruction)((Object)iterator);
                int n7 = instructionList6.getIndex();
                object3 = this.getLocalVariableRegistry().lookupRegisteredLocalVariable(n7, instructionHandle3.getPosition());
                LocalVariableGen localVariableGen2 = (LocalVariableGen)hashMap2.get(object3);
                if (localVariableGen2 == null) {
                    arrinstructionTargeter = object3.getName();
                    Type type = object3.getType();
                    localVariableGen2 = methodGenerator.addLocalVariable((String)arrinstructionTargeter, type, null, null);
                    object = localVariableGen2.getIndex();
                    hashMap2.put(object3, localVariableGen2);
                    hashMap3.put((InstructionHandle[])object3, instructionHandle4);
                    hashMap4.put((InstructionHandle[])object3, instructionHandle4);
                } else {
                    object = localVariableGen2.getIndex();
                }
                instructionList6.setIndex((int)object);
            }
            if (instructionHandle3.hasTargeters()) {
                localVariableGen = instructionHandle3.getTargeters();
                for (int i2 = 0; i2 < localVariableGen.length; ++i2) {
                    Object v2;
                    object3 = localVariableGen[i2];
                    if (!(object3 instanceof LocalVariableGen) || ((LocalVariableGen)object3).getEnd() != instructionHandle3 || (v2 = hashMap2.get(object3)) == null) continue;
                    methodGenerator.removeLocalVariable((LocalVariableGen)v2);
                }
            }
            if (!(object2 instanceof MarkerInstruction)) {
                InstructionHandle instructionHandle12 = var35_38.getNext();
            }
            instructionHandle3 = instructionHandle3.getNext();
        }
        instructionList3.append(InstructionConstants.POP);
        object2 = hashMap3.entrySet().iterator();
        while (object2.hasNext()) {
            iterator = (Map.Entry)object2.next();
            LocalVariableGen localVariableGen3 = (LocalVariableGen)iterator.getKey();
            targetLostException = (InstructionHandle)iterator.getValue();
            localVariableGen3.setStart((InstructionHandle)targetLostException);
        }
        iterator = hashMap4.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            targetLostException = (LocalVariableGen)entry.getKey();
            object3 = (InstructionHandle)entry.getValue();
            targetLostException.setEnd((InstructionHandle)object3);
        }
        xSLTC.dumpClass(classGenerator2.getJavaClass());
        InstructionList instructionList6 = this.getInstructionList();
        instructionList6.insert(instructionHandle, instructionList2);
        instructionList6.insert(instructionHandle, instructionList3);
        instructionList.insert(instructionList4);
        instructionList.append(instructionList5);
        instructionList.append(InstructionConstants.RETURN);
        try {
            instructionList6.delete(instructionHandle, instructionHandle2);
        }
        catch (TargetLostException targetLostException2) {
            object3 = targetLostException2.getTargets();
            for (n2 = 0; n2 < object3.length; ++n2) {
                object = object3[n2];
                arrinstructionTargeter = object.getTargeters();
                for (var44_61 = (reference)false ? 1 : 0; var44_61 < arrinstructionTargeter.length; ++var44_61) {
                    if (arrinstructionTargeter[var44_61] instanceof LocalVariableGen) {
                        object4 = (LocalVariableGen)arrinstructionTargeter[var44_61];
                        if (object4.getStart() == object) {
                            object4.setStart(instructionHandle4);
                        }
                        if (object4.getEnd() != object) continue;
                        object4.setEnd(instructionHandle4);
                        continue;
                    }
                    arrinstructionTargeter[var44_61].updateTarget((InstructionHandle)object, instructionHandle6);
                }
            }
        }
        targetLostException = this.getExceptions();
        for (int i3 = 0; i3 < targetLostException.length; ++i3) {
            methodGenerator.addException(targetLostException[i3]);
        }
        return methodGenerator.getThisMethod();
    }

    private static Instruction loadLocal(int n2, Type type) {
        if (type == Type.BOOLEAN) {
            return new ILOAD(n2);
        }
        if (type == Type.INT) {
            return new ILOAD(n2);
        }
        if (type == Type.SHORT) {
            return new ILOAD(n2);
        }
        if (type == Type.LONG) {
            return new LLOAD(n2);
        }
        if (type == Type.BYTE) {
            return new ILOAD(n2);
        }
        if (type == Type.CHAR) {
            return new ILOAD(n2);
        }
        if (type == Type.FLOAT) {
            return new FLOAD(n2);
        }
        if (type == Type.DOUBLE) {
            return new DLOAD(n2);
        }
        return new ALOAD(n2);
    }

    private static Instruction storeLocal(int n2, Type type) {
        if (type == Type.BOOLEAN) {
            return new ISTORE(n2);
        }
        if (type == Type.INT) {
            return new ISTORE(n2);
        }
        if (type == Type.SHORT) {
            return new ISTORE(n2);
        }
        if (type == Type.LONG) {
            return new LSTORE(n2);
        }
        if (type == Type.BYTE) {
            return new ISTORE(n2);
        }
        if (type == Type.CHAR) {
            return new ISTORE(n2);
        }
        if (type == Type.FLOAT) {
            return new FSTORE(n2);
        }
        if (type == Type.DOUBLE) {
            return new DSTORE(n2);
        }
        return new ASTORE(n2);
    }

    public void markChunkStart() {
        this.getInstructionList().append(OutlineableChunkStart.OUTLINEABLECHUNKSTART);
        ++this.m_totalChunks;
        ++this.m_openChunks;
    }

    public void markChunkEnd() {
        this.getInstructionList().append(OutlineableChunkEnd.OUTLINEABLECHUNKEND);
        --this.m_openChunks;
        if (this.m_openChunks < 0) {
            String string = new ErrorMsg("OUTLINE_ERR_UNBALANCED_MARKERS").toString();
            throw new InternalError(string);
        }
    }

    Method[] getGeneratedMethods(ClassGenerator classGenerator) {
        boolean bl;
        InstructionList instructionList = this.getInstructionList();
        InstructionHandle instructionHandle = instructionList.getEnd();
        instructionList.setPositions();
        int n2 = instructionHandle.getPosition() + instructionHandle.getInstruction().getLength();
        if (n2 > 32767 && (bl = this.widenConditionalBranchTargetOffsets())) {
            instructionList.setPositions();
            instructionHandle = instructionList.getEnd();
            n2 = instructionHandle.getPosition() + instructionHandle.getInstruction().getLength();
        }
        Method[] arrmethod = n2 > 65535 ? this.outlineChunks(classGenerator, n2) : new Method[]{this.getThisMethod()};
        return arrmethod;
    }

    protected Method getThisMethod() {
        this.stripAttributes(true);
        this.setMaxLocals();
        this.setMaxStack();
        this.removeNOPs();
        return this.getMethod();
    }

    boolean widenConditionalBranchTargetOffsets() {
        InstructionHandle instructionHandle;
        Instruction instruction;
        boolean bl = false;
        int n2 = 0;
        InstructionList instructionList = this.getInstructionList();
        block7 : for (instructionHandle = instructionList.getStart(); instructionHandle != null; instructionHandle = instructionHandle.getNext()) {
            instruction = instructionHandle.getInstruction();
            switch (instruction.getOpcode()) {
                case 167: 
                case 168: {
                    n2 += 2;
                    continue block7;
                }
                case 170: 
                case 171: {
                    n2 += 3;
                    continue block7;
                }
                case 153: 
                case 154: 
                case 155: 
                case 156: 
                case 157: 
                case 158: 
                case 159: 
                case 160: 
                case 161: 
                case 162: 
                case 163: 
                case 164: 
                case 165: 
                case 166: 
                case 198: 
                case 199: {
                    n2 += 5;
                }
            }
        }
        for (instructionHandle = instructionList.getStart(); instructionHandle != null; instructionHandle = instructionHandle.getNext()) {
            instruction = instructionHandle.getInstruction();
            if (!(instruction instanceof IfInstruction)) continue;
            IfInstruction ifInstruction = (IfInstruction)instruction;
            BranchHandle branchHandle = (BranchHandle)instructionHandle;
            InstructionHandle instructionHandle2 = ifInstruction.getTarget();
            int n3 = instructionHandle2.getPosition() - branchHandle.getPosition();
            if (n3 - n2 >= -32768 && n3 + n2 <= 32767) continue;
            InstructionHandle instructionHandle3 = branchHandle.getNext();
            IfInstruction ifInstruction2 = ifInstruction.negate();
            BranchHandle branchHandle2 = instructionList.append((InstructionHandle)branchHandle, ifInstruction2);
            BranchHandle branchHandle3 = instructionList.append((InstructionHandle)branchHandle2, new GOTO(instructionHandle2));
            if (instructionHandle3 == null) {
                instructionHandle3 = instructionList.append((InstructionHandle)branchHandle3, NOP);
            }
            branchHandle2.updateTarget(instructionHandle2, instructionHandle3);
            if (branchHandle.hasTargeters()) {
                InstructionTargeter[] arrinstructionTargeter = branchHandle.getTargeters();
                for (int i2 = 0; i2 < arrinstructionTargeter.length; ++i2) {
                    InstructionTargeter instructionTargeter = arrinstructionTargeter[i2];
                    if (instructionTargeter instanceof LocalVariableGen) {
                        LocalVariableGen localVariableGen = (LocalVariableGen)instructionTargeter;
                        if (localVariableGen.getStart() == branchHandle) {
                            localVariableGen.setStart(branchHandle2);
                            continue;
                        }
                        if (localVariableGen.getEnd() != branchHandle) continue;
                        localVariableGen.setEnd(branchHandle3);
                        continue;
                    }
                    instructionTargeter.updateTarget(branchHandle, branchHandle2);
                }
            }
            try {
                instructionList.delete(branchHandle);
            }
            catch (TargetLostException targetLostException) {
                String string = new ErrorMsg("OUTLINE_ERR_DELETED_TARGET", targetLostException.getMessage()).toString();
                throw new InternalError(string);
            }
            instructionHandle = branchHandle3;
            bl = true;
        }
        return bl;
    }

    private static class Chunk
    implements Comparable {
        private InstructionHandle m_start;
        private InstructionHandle m_end;
        private int m_size;

        Chunk(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
            this.m_start = instructionHandle;
            this.m_end = instructionHandle2;
            this.m_size = instructionHandle2.getPosition() - instructionHandle.getPosition();
        }

        boolean isAdjacentTo(Chunk chunk) {
            return this.getChunkEnd().getNext() == chunk.getChunkStart();
        }

        InstructionHandle getChunkStart() {
            return this.m_start;
        }

        InstructionHandle getChunkEnd() {
            return this.m_end;
        }

        int getChunkSize() {
            return this.m_size;
        }

        public int compareTo(Object object) {
            return this.getChunkSize() - ((Chunk)object).getChunkSize();
        }
    }

    protected class LocalVariableRegistry {
        protected ArrayList _variables;
        protected HashMap _nameToLVGMap;
        private final MethodGenerator this$0;

        protected LocalVariableRegistry(MethodGenerator methodGenerator) {
            this.this$0 = methodGenerator;
            this._variables = new ArrayList();
            this._nameToLVGMap = new HashMap();
        }

        protected void registerLocalVariable(LocalVariableGen localVariableGen) {
            int n2;
            int n3 = localVariableGen.getIndex();
            if (n3 >= (n2 = this._variables.size())) {
                for (int i2 = n2; i2 < n3; ++i2) {
                    this._variables.add(null);
                }
                this._variables.add(localVariableGen);
            } else {
                Object e2 = this._variables.get(n3);
                if (e2 != null) {
                    if (e2 instanceof LocalVariableGen) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(e2);
                        arrayList.add((LocalVariableGen)localVariableGen);
                        this._variables.set(n3, arrayList);
                    } else {
                        ((ArrayList)e2).add(localVariableGen);
                    }
                } else {
                    this._variables.set(n3, localVariableGen);
                }
            }
            this.registerByName(localVariableGen);
        }

        protected LocalVariableGen lookupRegisteredLocalVariable(int n2, int n3) {
            Object var3_3;
            Object v0 = var3_3 = this._variables != null ? this._variables.get(n2) : null;
            if (var3_3 != null) {
                if (var3_3 instanceof LocalVariableGen) {
                    LocalVariableGen localVariableGen = var3_3;
                    if (this.this$0.offsetInLocalVariableGenRange(localVariableGen, n3)) {
                        return localVariableGen;
                    }
                } else {
                    ArrayList arrayList = var3_3;
                    int n4 = arrayList.size();
                    for (int i2 = 0; i2 < n4; ++i2) {
                        LocalVariableGen localVariableGen = (LocalVariableGen)arrayList.get(i2);
                        if (!this.this$0.offsetInLocalVariableGenRange(localVariableGen, n3)) continue;
                        return localVariableGen;
                    }
                }
            }
            return null;
        }

        protected void registerByName(LocalVariableGen localVariableGen) {
            Object v2 = this._nameToLVGMap.get(localVariableGen.getName());
            if (v2 == null) {
                this._nameToLVGMap.put(localVariableGen.getName(), localVariableGen);
            } else {
                ArrayList<LocalVariableGen> arrayList;
                if (v2 instanceof ArrayList) {
                    arrayList = (ArrayList<LocalVariableGen>)v2;
                    arrayList.add(localVariableGen);
                } else {
                    arrayList = new ArrayList<LocalVariableGen>();
                    arrayList.add((LocalVariableGen)v2);
                    arrayList.add(localVariableGen);
                }
                this._nameToLVGMap.put(localVariableGen.getName(), arrayList);
            }
        }

        protected void removeByNameTracking(LocalVariableGen localVariableGen) {
            Object v2 = this._nameToLVGMap.get(localVariableGen.getName());
            if (v2 instanceof ArrayList) {
                ArrayList arrayList = (ArrayList)v2;
                for (int i2 = 0; i2 < arrayList.size(); ++i2) {
                    if (arrayList.get(i2) != localVariableGen) continue;
                    arrayList.remove(i2);
                    break;
                }
            } else {
                this._nameToLVGMap.remove(localVariableGen);
            }
        }

        protected LocalVariableGen lookUpByName(String string) {
            LocalVariableGen localVariableGen = null;
            Object v2 = this._nameToLVGMap.get(string);
            if (v2 instanceof ArrayList) {
                ArrayList arrayList = (ArrayList)v2;
                for (int i2 = 0; i2 < arrayList.size() && (localVariableGen = (LocalVariableGen)arrayList.get(i2)).getName() != string; ++i2) {
                }
            } else {
                localVariableGen = (LocalVariableGen)v2;
            }
            return localVariableGen;
        }

        protected LocalVariableGen[] getLocals(boolean bl) {
            LocalVariableGen[] arrlocalVariableGen = null;
            ArrayList<Object> arrayList = new ArrayList<Object>();
            if (bl) {
                int n2 = arrayList.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    Object e2 = this._variables.get(i2);
                    if (e2 == null) continue;
                    if (e2 instanceof ArrayList) {
                        ArrayList arrayList2 = (ArrayList)e2;
                        for (int i3 = 0; i3 < arrayList2.size(); ++i3) {
                            arrayList.add(arrayList2.get(i2));
                        }
                        continue;
                    }
                    arrayList.add(e2);
                }
            } else {
                Iterator iterator = this._nameToLVGMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = iterator.next();
                    Object v2 = entry.getValue();
                    if (v2 == null) continue;
                    if (v2 instanceof ArrayList) {
                        ArrayList arrayList3 = (ArrayList)v2;
                        for (int i4 = 0; i4 < arrayList3.size(); ++i4) {
                            arrayList.add(arrayList3.get(i4));
                        }
                        continue;
                    }
                    arrayList.add(v2);
                }
            }
            arrlocalVariableGen = new LocalVariableGen[arrayList.size()];
            arrayList.toArray(arrlocalVariableGen);
            return arrlocalVariableGen;
        }
    }

}

