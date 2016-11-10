/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.Instruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Attribute
extends Instruction {
    private QName _name;

    Attribute() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Attribute " + this._name);
        this.displayContents(n2 + 4);
    }

    public void parseContents(Parser parser) {
        this._name = parser.getQName(this.getAttribute("name"));
        this.parseChildren(parser);
    }
}

