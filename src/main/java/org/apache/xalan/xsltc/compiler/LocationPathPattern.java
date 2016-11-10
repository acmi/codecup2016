/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.Pattern;
import org.apache.xalan.xsltc.compiler.StepPattern;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.Template;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

public abstract class LocationPathPattern
extends Pattern {
    private Template _template;
    private int _importPrecedence;
    private double _priority = Double.NaN;
    private int _position = 0;

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
    }

    public void setTemplate(Template template) {
        this._template = template;
        this._priority = template.getPriority();
        this._importPrecedence = template.getImportPrecedence();
        this._position = template.getPosition();
    }

    public Template getTemplate() {
        return this._template;
    }

    public final double getPriority() {
        return Double.isNaN(this._priority) ? this.getDefaultPriority() : this._priority;
    }

    public double getDefaultPriority() {
        return 0.5;
    }

    public boolean noSmallerThan(LocationPathPattern locationPathPattern) {
        if (this._importPrecedence > locationPathPattern._importPrecedence) {
            return true;
        }
        if (this._importPrecedence == locationPathPattern._importPrecedence) {
            if (this._priority > locationPathPattern._priority) {
                return true;
            }
            if (this._priority == locationPathPattern._priority && this._position > locationPathPattern._position) {
                return true;
            }
        }
        return false;
    }

    public abstract StepPattern getKernelPattern();

    public abstract void reduceKernelPattern();

    public abstract boolean isWildcard();

    public int getAxis() {
        StepPattern stepPattern = this.getKernelPattern();
        return stepPattern != null ? stepPattern.getAxis() : 3;
    }

    public String toString() {
        return "root()";
    }
}

