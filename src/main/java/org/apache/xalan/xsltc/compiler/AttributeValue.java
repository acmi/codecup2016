/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.AttributeValueTemplate;
import org.apache.xalan.xsltc.compiler.Expression;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SimpleAttributeValue;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;

abstract class AttributeValue
extends Expression {
    AttributeValue() {
    }

    public static final AttributeValue create(SyntaxTreeNode syntaxTreeNode, String string, Parser parser) {
        SyntaxTreeNode syntaxTreeNode2;
        if (string.indexOf(123) != -1) {
            syntaxTreeNode2 = new AttributeValueTemplate(string, parser, syntaxTreeNode);
        } else if (string.indexOf(125) != -1) {
            syntaxTreeNode2 = new AttributeValueTemplate(string, parser, syntaxTreeNode);
        } else {
            syntaxTreeNode2 = new SimpleAttributeValue(string);
            syntaxTreeNode2.setParser(parser);
            syntaxTreeNode2.setParent(syntaxTreeNode);
        }
        return syntaxTreeNode2;
    }
}

