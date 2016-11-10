/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.compiler;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.StackInstruction;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.QName;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.TopLevelElement;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.utils.XML11Char;

final class Output
extends TopLevelElement {
    private String _version;
    private String _method;
    private String _encoding;
    private boolean _omitHeader = false;
    private String _standalone;
    private String _doctypePublic;
    private String _doctypeSystem;
    private String _cdata;
    private boolean _indent = false;
    private String _mediaType;
    private String _indentamount;
    private boolean _disabled = false;
    private static final String STRING_SIG = "Ljava/lang/String;";
    private static final String XML_VERSION = "1.0";
    private static final String HTML_VERSION = "4.0";

    Output() {
    }

    public void display(int n2) {
        this.indent(n2);
        Util.println("Output " + this._method);
    }

    public void disable() {
        this._disabled = true;
    }

    public boolean enabled() {
        return !this._disabled;
    }

    public String getCdata() {
        return this._cdata;
    }

    public String getOutputMethod() {
        return this._method;
    }

    private void transferAttribute(Output output, String string) {
        if (!this.hasAttribute(string) && output.hasAttribute(string)) {
            this.addAttribute(string, output.getAttribute(string));
        }
    }

    public void mergeOutput(Output output) {
        String string;
        this.transferAttribute(output, "version");
        this.transferAttribute(output, "method");
        this.transferAttribute(output, "encoding");
        this.transferAttribute(output, "doctype-system");
        this.transferAttribute(output, "doctype-public");
        this.transferAttribute(output, "media-type");
        this.transferAttribute(output, "indent");
        this.transferAttribute(output, "omit-xml-declaration");
        this.transferAttribute(output, "standalone");
        if (output.hasAttribute("cdata-section-elements")) {
            this.addAttribute("cdata-section-elements", output.getAttribute("cdata-section-elements") + ' ' + this.getAttribute("cdata-section-elements"));
        }
        if ((string = this.lookupPrefix("http://xml.apache.org/xalan")) != null) {
            this.transferAttribute(output, string + ':' + "indent-amount");
        }
        if ((string = this.lookupPrefix("http://xml.apache.org/xslt")) != null) {
            this.transferAttribute(output, string + ':' + "indent-amount");
        }
    }

    public void parseContents(Parser parser) {
        Object object;
        Properties properties = new Properties();
        parser.setOutput(this);
        if (this._disabled) {
            return;
        }
        String string2 = null;
        this._version = this.getAttribute("version");
        if (this._version.equals("")) {
            this._version = null;
        } else {
            properties.setProperty("version", this._version);
        }
        this._method = this.getAttribute("method");
        if (this._method.equals("")) {
            this._method = null;
        }
        if (this._method != null) {
            this._method = this._method.toLowerCase();
            if (this._method.equals("xml") || this._method.equals("html") || this._method.equals("text") || XML11Char.isXML11ValidQName(this._method) && this._method.indexOf(":") > 0) {
                properties.setProperty("method", this._method);
            } else {
                this.reportError(this, parser, "INVALID_METHOD_IN_OUTPUT", this._method);
            }
        }
        this._encoding = this.getAttribute("encoding");
        if (this._encoding.equals("")) {
            this._encoding = null;
        } else {
            try {
                String string = Encodings.convertMime2JavaEncoding(this._encoding);
                object = new OutputStreamWriter((OutputStream)System.out, string);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                object = new ErrorMsg("UNSUPPORTED_ENCODING", (Object)this._encoding, this);
                parser.reportError(4, (ErrorMsg)object);
            }
            properties.setProperty("encoding", this._encoding);
        }
        string2 = this.getAttribute("omit-xml-declaration");
        if (!string2.equals("")) {
            if (string2.equals("yes")) {
                this._omitHeader = true;
            }
            properties.setProperty("omit-xml-declaration", string2);
        }
        this._standalone = this.getAttribute("standalone");
        if (this._standalone.equals("")) {
            this._standalone = null;
        } else {
            properties.setProperty("standalone", this._standalone);
        }
        this._doctypeSystem = this.getAttribute("doctype-system");
        if (this._doctypeSystem.equals("")) {
            this._doctypeSystem = null;
        } else {
            properties.setProperty("doctype-system", this._doctypeSystem);
        }
        this._doctypePublic = this.getAttribute("doctype-public");
        if (this._doctypePublic.equals("")) {
            this._doctypePublic = null;
        } else {
            properties.setProperty("doctype-public", this._doctypePublic);
        }
        this._cdata = this.getAttribute("cdata-section-elements");
        if (this._cdata.equals("")) {
            this._cdata = null;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            object = new StringTokenizer(this._cdata);
            while (object.hasMoreTokens()) {
                String string = object.nextToken();
                if (!XML11Char.isXML11ValidQName(string)) {
                    ErrorMsg errorMsg = new ErrorMsg("INVALID_QNAME_ERR", (Object)string, this);
                    parser.reportError(3, errorMsg);
                }
                stringBuffer.append(parser.getQName(string).toString()).append(' ');
            }
            this._cdata = stringBuffer.toString();
            properties.setProperty("cdata-section-elements", this._cdata);
        }
        string2 = this.getAttribute("indent");
        if (!string2.equals("")) {
            if (string2.equals("yes")) {
                this._indent = true;
            }
            properties.setProperty("indent", string2);
        } else if (this._method != null && this._method.equals("html")) {
            this._indent = true;
        }
        this._indentamount = this.getAttribute(this.lookupPrefix("http://xml.apache.org/xalan"), "indent-amount");
        if (this._indentamount.equals("")) {
            this._indentamount = this.getAttribute(this.lookupPrefix("http://xml.apache.org/xslt"), "indent-amount");
        }
        if (!this._indentamount.equals("")) {
            properties.setProperty("indent_amount", this._indentamount);
        }
        this._mediaType = this.getAttribute("media-type");
        if (this._mediaType.equals("")) {
            this._mediaType = null;
        } else {
            properties.setProperty("media-type", this._mediaType);
        }
        if (this._method != null) {
            if (this._method.equals("html")) {
                if (this._version == null) {
                    this._version = "4.0";
                }
                if (this._mediaType == null) {
                    this._mediaType = "text/html";
                }
            } else if (this._method.equals("text") && this._mediaType == null) {
                this._mediaType = "text/plain";
            }
        }
        parser.getCurrentStylesheet().setOutputProperties(properties);
    }

    public void translate(ClassGenerator classGenerator, MethodGenerator methodGenerator) {
        if (this._disabled) {
            return;
        }
        ConstantPoolGen constantPoolGen = classGenerator.getConstantPool();
        InstructionList instructionList = methodGenerator.getInstructionList();
        int n2 = 0;
        instructionList.append(classGenerator.loadTranslet());
        if (this._version != null && !this._version.equals("1.0")) {
            n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_version", "Ljava/lang/String;");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, this._version));
            instructionList.append(new PUTFIELD(n2));
        }
        if (this._method != null) {
            n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_method", "Ljava/lang/String;");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, this._method));
            instructionList.append(new PUTFIELD(n2));
        }
        if (this._encoding != null) {
            n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_encoding", "Ljava/lang/String;");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, this._encoding));
            instructionList.append(new PUTFIELD(n2));
        }
        if (this._omitHeader) {
            n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_omitHeader", "Z");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, this._omitHeader));
            instructionList.append(new PUTFIELD(n2));
        }
        if (this._standalone != null) {
            n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_standalone", "Ljava/lang/String;");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, this._standalone));
            instructionList.append(new PUTFIELD(n2));
        }
        n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_doctypeSystem", "Ljava/lang/String;");
        instructionList.append(DUP);
        instructionList.append(new PUSH(constantPoolGen, this._doctypeSystem));
        instructionList.append(new PUTFIELD(n2));
        n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_doctypePublic", "Ljava/lang/String;");
        instructionList.append(DUP);
        instructionList.append(new PUSH(constantPoolGen, this._doctypePublic));
        instructionList.append(new PUTFIELD(n2));
        if (this._mediaType != null) {
            n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_mediaType", "Ljava/lang/String;");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, this._mediaType));
            instructionList.append(new PUTFIELD(n2));
        }
        if (this._indent) {
            n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_indent", "Z");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, this._indent));
            instructionList.append(new PUTFIELD(n2));
        }
        if (this._indentamount != null && !this._indentamount.equals("")) {
            n2 = constantPoolGen.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_indentamount", "I");
            instructionList.append(DUP);
            instructionList.append(new PUSH(constantPoolGen, Integer.parseInt(this._indentamount)));
            instructionList.append(new PUTFIELD(n2));
        }
        if (this._cdata != null) {
            int n3 = constantPoolGen.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addCdataElement", "(Ljava/lang/String;)V");
            StringTokenizer stringTokenizer = new StringTokenizer(this._cdata);
            while (stringTokenizer.hasMoreTokens()) {
                instructionList.append(DUP);
                instructionList.append(new PUSH(constantPoolGen, stringTokenizer.nextToken()));
                instructionList.append(new INVOKEVIRTUAL(n3));
            }
        }
        instructionList.append(POP);
    }
}

