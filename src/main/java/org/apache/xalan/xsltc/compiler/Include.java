/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import org.apache.xalan.xsltc.compiler.Param;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SymbolTable;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.Variable;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xml.utils.SystemIDResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

final class Include
extends TopLevelElement {
    private Stylesheet _included = null;

    Include() {
    }

    public Stylesheet getIncludedStylesheet() {
        return this._included;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void parseContents(Parser parser) {
        XSLTC xSLTC = parser.getXSLTC();
        Stylesheet stylesheet = parser.getCurrentStylesheet();
        String string = this.getAttribute("href");
        try {
            if (stylesheet.checkForLoop(string)) {
                ErrorMsg errorMsg = new ErrorMsg("CIRCULAR_INCLUDE_ERR", (Object)string, this);
                parser.reportError(2, errorMsg);
                return;
            }
            InputSource inputSource = null;
            XMLReader xMLReader = null;
            String string2 = stylesheet.getSystemId();
            SourceLoader sourceLoader = stylesheet.getSourceLoader();
            if (sourceLoader != null && (inputSource = sourceLoader.loadSource(string, string2, xSLTC)) != null) {
                string = inputSource.getSystemId();
                xMLReader = xSLTC.getXMLReader();
            }
            if (inputSource == null) {
                string = SystemIDResolver.getAbsoluteURI(string, string2);
                inputSource = new InputSource(string);
            }
            if (inputSource == null) {
                ErrorMsg errorMsg = new ErrorMsg("FILE_NOT_FOUND_ERR", (Object)string, this);
                parser.reportError(2, errorMsg);
                return;
            }
            SyntaxTreeNode syntaxTreeNode = xMLReader != null ? parser.parse(xMLReader, inputSource) : parser.parse(inputSource);
            if (syntaxTreeNode == null) {
                return;
            }
            this._included = parser.makeStylesheet(syntaxTreeNode);
            if (this._included == null) {
                return;
            }
            this._included.setSourceLoader(sourceLoader);
            this._included.setSystemId(string);
            this._included.setParentStylesheet(stylesheet);
            this._included.setIncludingStylesheet(stylesheet);
            this._included.setTemplateInlining(stylesheet.getTemplateInlining());
            int n2 = stylesheet.getImportPrecedence();
            this._included.setImportPrecedence(n2);
            parser.setCurrentStylesheet(this._included);
            this._included.parseContents(parser);
            Enumeration enumeration = this._included.elements();
            Stylesheet stylesheet2 = parser.getTopLevelStylesheet();
            while (enumeration.hasMoreElements()) {
                Object e2 = enumeration.nextElement();
                if (!(e2 instanceof TopLevelElement)) continue;
                if (e2 instanceof Variable) {
                    stylesheet2.addVariable((Variable)e2);
                    continue;
                }
                if (e2 instanceof Param) {
                    stylesheet2.addParam((Param)e2);
                    continue;
                }
                stylesheet2.addElement((TopLevelElement)e2);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            parser.setCurrentStylesheet(stylesheet);
        }
    }

    public Type typeCheck(SymbolTable symbolTable) throws TypeCheckError {
        return Type.Void;
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
    }
}

