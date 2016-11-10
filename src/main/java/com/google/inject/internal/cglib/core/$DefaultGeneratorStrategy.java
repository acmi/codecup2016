/*
 * Decompiled with CFR 0_119.
 */
package com.google.inject.internal.cglib.core;

import com.google.inject.internal.asm.$ClassVisitor;
import com.google.inject.internal.asm.$ClassWriter;
import com.google.inject.internal.cglib.core.$ClassGenerator;
import com.google.inject.internal.cglib.core.$DebuggingClassWriter;
import com.google.inject.internal.cglib.core.$GeneratorStrategy;

public class $DefaultGeneratorStrategy
implements $GeneratorStrategy {
    public static final $DefaultGeneratorStrategy INSTANCE = new $DefaultGeneratorStrategy();

    public byte[] generate($ClassGenerator $ClassGenerator) throws Exception {
        $DebuggingClassWriter $DebuggingClassWriter = this.getClassVisitor();
        this.transform($ClassGenerator).generateClass($DebuggingClassWriter);
        return this.transform($DebuggingClassWriter.toByteArray());
    }

    protected $DebuggingClassWriter getClassVisitor() throws Exception {
        return new $DebuggingClassWriter(2);
    }

    protected final $ClassWriter getClassWriter() {
        throw new UnsupportedOperationException("You are calling getClassWriter, which no longer exists in this cglib version.");
    }

    protected byte[] transform(byte[] arrby) throws Exception {
        return arrby;
    }

    protected $ClassGenerator transform($ClassGenerator $ClassGenerator) throws Exception {
        return $ClassGenerator;
    }
}

