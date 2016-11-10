/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.inject.internal.asm.$AnnotationVisitor;
import com.google.inject.internal.asm.$ClassReader;
import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$FieldVisitor;
import com.google.inject.internal.asm.$Label;
import com.google.inject.internal.asm.$MethodVisitor;
import com.google.inject.internal.asm.$Type;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

final class LineNumbers {
    private final Class type;
    private final Map<String, Integer> lines = Maps.newHashMap();
    private String source;
    private int firstLine = Integer.MAX_VALUE;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public LineNumbers(Class class_) throws IOException {
        InputStream inputStream;
        this.type = class_;
        if (!class_.isArray() && (inputStream = class_.getResourceAsStream("/" + class_.getName().replace('.', '/') + ".class")) != null) {
            try {
                new $ClassReader(inputStream).accept(new LineNumberReader(), 4);
            }
            finally {
                try {
                    inputStream.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    public String getSource() {
        return this.source;
    }

    public Integer getLineNumber(Member member) {
        Preconditions.checkArgument(this.type == member.getDeclaringClass(), "Member %s belongs to %s, not %s", member, member.getDeclaringClass(), this.type);
        return this.lines.get(this.memberKey(member));
    }

    public int getFirstLine() {
        return this.firstLine == Integer.MAX_VALUE ? 1 : this.firstLine;
    }

    private String memberKey(Member member) {
        Preconditions.checkNotNull(member, "member");
        if (member instanceof Field) {
            return member.getName();
        }
        if (member instanceof Method) {
            return member.getName() + $Type.getMethodDescriptor((Method)member);
        }
        if (member instanceof Constructor) {
            StringBuilder stringBuilder = new StringBuilder().append("<init>(");
            for (Class class_ : ((Constructor)member).getParameterTypes()) {
                stringBuilder.append($Type.getDescriptor(class_));
            }
            return stringBuilder.append(")V").toString();
        }
        throw new IllegalArgumentException("Unsupported implementation class for Member, " + member.getClass());
    }

    private class LineNumberReader
    extends $ClassVisitor {
        private int line;
        private String pendingMethod;
        private String name;

        LineNumberReader() {
            super(327680);
            this.line = -1;
        }

        @Override
        public void visit(int n2, int n3, String string, String string2, String string3, String[] arrstring) {
            this.name = string;
        }

        @Override
        public $MethodVisitor visitMethod(int n2, String string, String string2, String string3, String[] arrstring) {
            if ((n2 & 2) != 0) {
                return null;
            }
            this.pendingMethod = string + string2;
            this.line = -1;
            return new LineNumberMethodVisitor();
        }

        @Override
        public void visitSource(String string, String string2) {
            LineNumbers.this.source = string;
        }

        public void visitLineNumber(int n2, $Label $Label) {
            if (n2 < LineNumbers.this.firstLine) {
                LineNumbers.this.firstLine = n2;
            }
            this.line = n2;
            if (this.pendingMethod != null) {
                LineNumbers.this.lines.put(this.pendingMethod, n2);
                this.pendingMethod = null;
            }
        }

        @Override
        public $FieldVisitor visitField(int n2, String string, String string2, String string3, Object object) {
            return null;
        }

        @Override
        public $AnnotationVisitor visitAnnotation(String string, boolean bl) {
            return new LineNumberAnnotationVisitor();
        }

        public $AnnotationVisitor visitParameterAnnotation(int n2, String string, boolean bl) {
            return new LineNumberAnnotationVisitor();
        }

        class LineNumberAnnotationVisitor
        extends $AnnotationVisitor {
            LineNumberAnnotationVisitor() {
                super(327680);
            }

            @Override
            public $AnnotationVisitor visitAnnotation(String string, String string2) {
                return this;
            }

            @Override
            public $AnnotationVisitor visitArray(String string) {
                return this;
            }

            public void visitLocalVariable(String string, String string2, String string3, $Label $Label, $Label $Label2, int n2) {
            }
        }

        class LineNumberMethodVisitor
        extends $MethodVisitor {
            LineNumberMethodVisitor() {
                super(327680);
            }

            @Override
            public $AnnotationVisitor visitAnnotation(String string, boolean bl) {
                return new LineNumberAnnotationVisitor();
            }

            @Override
            public $AnnotationVisitor visitAnnotationDefault() {
                return new LineNumberAnnotationVisitor();
            }

            @Override
            public void visitFieldInsn(int n2, String string, String string2, String string3) {
                if (n2 == 181 && LineNumberReader.this.name.equals(string) && !LineNumbers.this.lines.containsKey(string2) && LineNumberReader.this.line != -1) {
                    LineNumbers.this.lines.put(string2, LineNumberReader.this.line);
                }
            }

            @Override
            public void visitLineNumber(int n2, $Label $Label) {
                LineNumberReader.this.visitLineNumber(n2, $Label);
            }
        }

    }

}

