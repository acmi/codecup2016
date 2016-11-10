/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.processor;

import java.util.HashMap;
import org.apache.xalan.processor.ProcessorAttributeSet;
import org.apache.xalan.processor.ProcessorCharacters;
import org.apache.xalan.processor.ProcessorDecimalFormat;
import org.apache.xalan.processor.ProcessorExsltFuncResult;
import org.apache.xalan.processor.ProcessorExsltFunction;
import org.apache.xalan.processor.ProcessorGlobalParamDecl;
import org.apache.xalan.processor.ProcessorGlobalVariableDecl;
import org.apache.xalan.processor.ProcessorImport;
import org.apache.xalan.processor.ProcessorInclude;
import org.apache.xalan.processor.ProcessorKey;
import org.apache.xalan.processor.ProcessorLRE;
import org.apache.xalan.processor.ProcessorNamespaceAlias;
import org.apache.xalan.processor.ProcessorOutputElem;
import org.apache.xalan.processor.ProcessorPreserveSpace;
import org.apache.xalan.processor.ProcessorStripSpace;
import org.apache.xalan.processor.ProcessorStylesheetDoc;
import org.apache.xalan.processor.ProcessorStylesheetElement;
import org.apache.xalan.processor.ProcessorTemplate;
import org.apache.xalan.processor.ProcessorTemplateElem;
import org.apache.xalan.processor.ProcessorText;
import org.apache.xalan.processor.ProcessorUnknown;
import org.apache.xalan.processor.XSLTAttributeDef;
import org.apache.xalan.processor.XSLTElementDef;
import org.apache.xalan.processor.XSLTElementProcessor;
import org.apache.xml.utils.QName;

public class XSLTSchema
extends XSLTElementDef {
    private HashMap m_availElems = new HashMap();
    static Class class$org$apache$xalan$templates$ElemTextLiteral;
    static Class class$org$apache$xalan$templates$ElemLiteralResult;
    static Class class$org$apache$xalan$templates$ElemUnknown;
    static Class class$org$apache$xalan$templates$ElemValueOf;
    static Class class$org$apache$xalan$templates$ElemCopyOf;
    static Class class$org$apache$xalan$templates$ElemNumber;
    static Class class$org$apache$xalan$templates$ElemSort;
    static Class class$org$apache$xalan$templates$ElemWithParam;
    static Class class$org$apache$xalan$templates$ElemApplyTemplates;
    static Class class$org$apache$xalan$templates$ElemApplyImport;
    static Class class$org$apache$xalan$templates$ElemForEach;
    static Class class$org$apache$xalan$templates$ElemIf;
    static Class class$org$apache$xalan$templates$ElemWhen;
    static Class class$org$apache$xalan$templates$ElemOtherwise;
    static Class class$org$apache$xalan$templates$ElemChoose;
    static Class class$org$apache$xalan$templates$ElemAttribute;
    static Class class$org$apache$xalan$templates$ElemCallTemplate;
    static Class class$org$apache$xalan$templates$ElemVariable;
    static Class class$org$apache$xalan$templates$ElemParam;
    static Class class$org$apache$xalan$templates$ElemText;
    static Class class$org$apache$xalan$templates$ElemPI;
    static Class class$org$apache$xalan$templates$ElemElement;
    static Class class$org$apache$xalan$templates$ElemComment;
    static Class class$org$apache$xalan$templates$ElemCopy;
    static Class class$org$apache$xalan$templates$ElemMessage;
    static Class class$org$apache$xalan$templates$ElemFallback;
    static Class class$org$apache$xalan$templates$ElemExsltFunction;
    static Class class$org$apache$xalan$templates$ElemExsltFuncResult;
    static Class class$org$apache$xalan$templates$ElemTemplate;
    static Class class$org$apache$xalan$templates$ElemExtensionScript;
    static Class class$org$apache$xalan$templates$ElemExtensionDecl;

    XSLTSchema() {
        this.build();
    }

    void build() {
        XSLTAttributeDef xSLTAttributeDef = new XSLTAttributeDef(null, "href", 2, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef2 = new XSLTAttributeDef(null, "elements", 12, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef3 = new XSLTAttributeDef(null, "method", 9, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef4 = new XSLTAttributeDef(null, "version", 13, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef5 = new XSLTAttributeDef(null, "encoding", 1, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef6 = new XSLTAttributeDef(null, "omit-xml-declaration", 8, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef7 = new XSLTAttributeDef(null, "standalone", 8, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef8 = new XSLTAttributeDef(null, "doctype-public", 1, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef9 = new XSLTAttributeDef(null, "doctype-system", 1, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef10 = new XSLTAttributeDef(null, "cdata-section-elements", 19, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef11 = new XSLTAttributeDef(null, "indent", 8, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef12 = new XSLTAttributeDef(null, "media-type", 1, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef13 = new XSLTAttributeDef(null, "name", 9, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef14 = new XSLTAttributeDef(null, "name", 18, true, true, 2);
        XSLTAttributeDef xSLTAttributeDef15 = new XSLTAttributeDef(null, "name", 17, true, true, 2);
        XSLTAttributeDef xSLTAttributeDef16 = new XSLTAttributeDef(null, "name", 9, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef17 = new XSLTAttributeDef(null, "use", 5, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef18 = new XSLTAttributeDef(null, "namespace", 2, false, true, 2);
        XSLTAttributeDef xSLTAttributeDef19 = new XSLTAttributeDef(null, "decimal-separator", 6, false, 1, ".");
        XSLTAttributeDef xSLTAttributeDef20 = new XSLTAttributeDef(null, "infinity", 1, false, 1, "Infinity");
        XSLTAttributeDef xSLTAttributeDef21 = new XSLTAttributeDef(null, "minus-sign", 6, false, 1, "-");
        XSLTAttributeDef xSLTAttributeDef22 = new XSLTAttributeDef(null, "NaN", 1, false, 1, "NaN");
        XSLTAttributeDef xSLTAttributeDef23 = new XSLTAttributeDef(null, "percent", 6, false, 1, "%");
        XSLTAttributeDef xSLTAttributeDef24 = new XSLTAttributeDef(null, "per-mille", 6, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef25 = new XSLTAttributeDef(null, "zero-digit", 6, false, 1, "0");
        XSLTAttributeDef xSLTAttributeDef26 = new XSLTAttributeDef(null, "digit", 6, false, 1, "#");
        XSLTAttributeDef xSLTAttributeDef27 = new XSLTAttributeDef(null, "pattern-separator", 6, false, 1, ";");
        XSLTAttributeDef xSLTAttributeDef28 = new XSLTAttributeDef(null, "grouping-separator", 6, false, 1, ",");
        XSLTAttributeDef xSLTAttributeDef29 = new XSLTAttributeDef(null, "use-attribute-sets", 10, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef30 = new XSLTAttributeDef(null, "test", 5, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef31 = new XSLTAttributeDef(null, "select", 5, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef32 = new XSLTAttributeDef(null, "select", 5, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef33 = new XSLTAttributeDef(null, "select", 5, false, 1, "node()");
        XSLTAttributeDef xSLTAttributeDef34 = new XSLTAttributeDef(null, "select", 5, false, 1, ".");
        XSLTAttributeDef xSLTAttributeDef35 = new XSLTAttributeDef(null, "match", 4, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef36 = new XSLTAttributeDef(null, "match", 4, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef37 = new XSLTAttributeDef(null, "priority", 7, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef38 = new XSLTAttributeDef(null, "mode", 9, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef39 = new XSLTAttributeDef("http://www.w3.org/XML/1998/namespace", "space", false, false, false, 2, "default", 2, "preserve", 1);
        XSLTAttributeDef xSLTAttributeDef40 = new XSLTAttributeDef("http://www.w3.org/XML/1998/namespace", "space", 2, false, true, 1);
        XSLTAttributeDef xSLTAttributeDef41 = new XSLTAttributeDef(null, "stylesheet-prefix", 1, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef42 = new XSLTAttributeDef(null, "result-prefix", 1, true, false, 1);
        XSLTAttributeDef xSLTAttributeDef43 = new XSLTAttributeDef(null, "disable-output-escaping", 8, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef44 = new XSLTAttributeDef(null, "level", false, false, false, 1, "single", 1, "multiple", 2, "any", 3);
        xSLTAttributeDef44.setDefault("single");
        XSLTAttributeDef xSLTAttributeDef45 = new XSLTAttributeDef(null, "count", 4, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef46 = new XSLTAttributeDef(null, "from", 4, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef47 = new XSLTAttributeDef(null, "value", 5, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef48 = new XSLTAttributeDef(null, "format", 1, false, true, 1);
        xSLTAttributeDef48.setDefault("1");
        XSLTAttributeDef xSLTAttributeDef49 = new XSLTAttributeDef(null, "lang", 13, false, true, 1);
        XSLTAttributeDef xSLTAttributeDef50 = new XSLTAttributeDef(null, "letter-value", false, true, false, 1, "alphabetic", 1, "traditional", 2);
        XSLTAttributeDef xSLTAttributeDef51 = new XSLTAttributeDef(null, "grouping-separator", 6, false, true, 1);
        XSLTAttributeDef xSLTAttributeDef52 = new XSLTAttributeDef(null, "grouping-size", 7, false, true, 1);
        XSLTAttributeDef xSLTAttributeDef53 = new XSLTAttributeDef(null, "data-type", false, true, true, 1, "text", 1, "number", 1);
        xSLTAttributeDef53.setDefault("text");
        XSLTAttributeDef xSLTAttributeDef54 = new XSLTAttributeDef(null, "order", false, true, false, 1, "ascending", 1, "descending", 2);
        xSLTAttributeDef54.setDefault("ascending");
        XSLTAttributeDef xSLTAttributeDef55 = new XSLTAttributeDef(null, "case-order", false, true, false, 1, "upper-first", 1, "lower-first", 2);
        XSLTAttributeDef xSLTAttributeDef56 = new XSLTAttributeDef(null, "terminate", 8, false, false, 1);
        xSLTAttributeDef56.setDefault("no");
        XSLTAttributeDef xSLTAttributeDef57 = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "exclude-result-prefixes", 20, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef58 = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "extension-element-prefixes", 15, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef59 = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "use-attribute-sets", 10, false, false, 1);
        XSLTAttributeDef xSLTAttributeDef60 = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "version", 13, false, false, 1);
        Class class_ = class$org$apache$xalan$templates$ElemTextLiteral == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemTextLiteral = XSLTSchema.class$("org.apache.xalan.templates.ElemTextLiteral")) : class$org$apache$xalan$templates$ElemTextLiteral;
        XSLTElementDef xSLTElementDef = new XSLTElementDef(this, null, "text()", null, null, null, new ProcessorCharacters(), class_);
        xSLTElementDef.setType(2);
        XSLTElementDef xSLTElementDef2 = new XSLTElementDef(this, null, "text()", null, null, null, null, class$org$apache$xalan$templates$ElemTextLiteral == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemTextLiteral = XSLTSchema.class$("org.apache.xalan.templates.ElemTextLiteral")) : class$org$apache$xalan$templates$ElemTextLiteral);
        xSLTElementDef.setType(2);
        XSLTAttributeDef xSLTAttributeDef61 = new XSLTAttributeDef(null, "*", 3, false, true, 2);
        XSLTAttributeDef xSLTAttributeDef62 = new XSLTAttributeDef("http://www.w3.org/1999/XSL/Transform", "*", 1, false, false, 2);
        XSLTElementDef[] arrxSLTElementDef = new XSLTElementDef[23];
        XSLTElementDef[] arrxSLTElementDef2 = new XSLTElementDef[24];
        XSLTElementDef[] arrxSLTElementDef3 = new XSLTElementDef[24];
        XSLTElementDef[] arrxSLTElementDef4 = new XSLTElementDef[24];
        XSLTElementDef[] arrxSLTElementDef5 = new XSLTElementDef[15];
        Class class_2 = class$org$apache$xalan$templates$ElemLiteralResult == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemLiteralResult = XSLTSchema.class$("org.apache.xalan.templates.ElemLiteralResult")) : class$org$apache$xalan$templates$ElemLiteralResult;
        XSLTElementDef xSLTElementDef3 = new XSLTElementDef(this, null, "*", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef40, xSLTAttributeDef57, xSLTAttributeDef58, xSLTAttributeDef59, xSLTAttributeDef60, xSLTAttributeDef62, xSLTAttributeDef61}, (XSLTElementProcessor)new ProcessorLRE(), class_2, 20, true);
        Class class_3 = class$org$apache$xalan$templates$ElemUnknown == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemUnknown = XSLTSchema.class$("org.apache.xalan.templates.ElemUnknown")) : class$org$apache$xalan$templates$ElemUnknown;
        XSLTElementDef xSLTElementDef4 = new XSLTElementDef(this, "*", "unknown", null, arrxSLTElementDef2, new XSLTAttributeDef[]{xSLTAttributeDef57, xSLTAttributeDef58, xSLTAttributeDef59, xSLTAttributeDef60, xSLTAttributeDef62, xSLTAttributeDef61}, (XSLTElementProcessor)new ProcessorUnknown(), class_3, 20, true);
        Class class_4 = class$org$apache$xalan$templates$ElemValueOf == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemValueOf = XSLTSchema.class$("org.apache.xalan.templates.ElemValueOf")) : class$org$apache$xalan$templates$ElemValueOf;
        XSLTElementDef xSLTElementDef5 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "value-of", null, null, new XSLTAttributeDef[]{xSLTAttributeDef31, xSLTAttributeDef43}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_4, 20, true);
        Class class_5 = class$org$apache$xalan$templates$ElemCopyOf == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemCopyOf = XSLTSchema.class$("org.apache.xalan.templates.ElemCopyOf")) : class$org$apache$xalan$templates$ElemCopyOf;
        XSLTElementDef xSLTElementDef6 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "copy-of", null, null, new XSLTAttributeDef[]{xSLTAttributeDef31}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_5, 20, true);
        Class class_6 = class$org$apache$xalan$templates$ElemNumber == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemNumber = XSLTSchema.class$("org.apache.xalan.templates.ElemNumber")) : class$org$apache$xalan$templates$ElemNumber;
        XSLTElementDef xSLTElementDef7 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "number", null, null, new XSLTAttributeDef[]{xSLTAttributeDef44, xSLTAttributeDef45, xSLTAttributeDef46, xSLTAttributeDef47, xSLTAttributeDef48, xSLTAttributeDef49, xSLTAttributeDef50, xSLTAttributeDef51, xSLTAttributeDef52}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_6, 20, true);
        Class class_7 = class$org$apache$xalan$templates$ElemSort == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemSort = XSLTSchema.class$("org.apache.xalan.templates.ElemSort")) : class$org$apache$xalan$templates$ElemSort;
        XSLTElementDef xSLTElementDef8 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "sort", null, null, new XSLTAttributeDef[]{xSLTAttributeDef34, xSLTAttributeDef49, xSLTAttributeDef53, xSLTAttributeDef54, xSLTAttributeDef55}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_7, 19, true);
        Class class_8 = class$org$apache$xalan$templates$ElemWithParam == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemWithParam = XSLTSchema.class$("org.apache.xalan.templates.ElemWithParam")) : class$org$apache$xalan$templates$ElemWithParam;
        XSLTElementDef xSLTElementDef9 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "with-param", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef13, xSLTAttributeDef32}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_8, 19, true);
        Class class_9 = class$org$apache$xalan$templates$ElemApplyTemplates == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemApplyTemplates = XSLTSchema.class$("org.apache.xalan.templates.ElemApplyTemplates")) : class$org$apache$xalan$templates$ElemApplyTemplates;
        XSLTElementDef xSLTElementDef10 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "apply-templates", null, new XSLTElementDef[]{xSLTElementDef8, xSLTElementDef9}, new XSLTAttributeDef[]{xSLTAttributeDef33, xSLTAttributeDef38}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_9, 20, true);
        Class class_10 = class$org$apache$xalan$templates$ElemApplyImport == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemApplyImport = XSLTSchema.class$("org.apache.xalan.templates.ElemApplyImport")) : class$org$apache$xalan$templates$ElemApplyImport;
        XSLTElementDef xSLTElementDef11 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "apply-imports", null, null, new XSLTAttributeDef[0], new ProcessorTemplateElem(), class_10);
        Class class_11 = class$org$apache$xalan$templates$ElemForEach == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemForEach = XSLTSchema.class$("org.apache.xalan.templates.ElemForEach")) : class$org$apache$xalan$templates$ElemForEach;
        XSLTElementDef xSLTElementDef12 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "for-each", null, arrxSLTElementDef3, new XSLTAttributeDef[]{xSLTAttributeDef31, xSLTAttributeDef39}, new ProcessorTemplateElem(), class_11, true, false, true, 20, true);
        Class class_12 = class$org$apache$xalan$templates$ElemIf == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemIf = XSLTSchema.class$("org.apache.xalan.templates.ElemIf")) : class$org$apache$xalan$templates$ElemIf;
        XSLTElementDef xSLTElementDef13 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "if", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef30, xSLTAttributeDef39}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_12, 20, true);
        Class class_13 = class$org$apache$xalan$templates$ElemWhen == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemWhen = XSLTSchema.class$("org.apache.xalan.templates.ElemWhen")) : class$org$apache$xalan$templates$ElemWhen;
        XSLTElementDef xSLTElementDef14 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "when", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef30, xSLTAttributeDef39}, new ProcessorTemplateElem(), class_13, false, true, 1, true);
        Class class_14 = class$org$apache$xalan$templates$ElemOtherwise == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemOtherwise = XSLTSchema.class$("org.apache.xalan.templates.ElemOtherwise")) : class$org$apache$xalan$templates$ElemOtherwise;
        XSLTElementDef xSLTElementDef15 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "otherwise", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef39}, new ProcessorTemplateElem(), class_14, false, false, 2, false);
        Class class_15 = class$org$apache$xalan$templates$ElemChoose == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemChoose = XSLTSchema.class$("org.apache.xalan.templates.ElemChoose")) : class$org$apache$xalan$templates$ElemChoose;
        XSLTElementDef xSLTElementDef16 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "choose", null, new XSLTElementDef[]{xSLTElementDef14, xSLTElementDef15}, new XSLTAttributeDef[]{xSLTAttributeDef39}, new ProcessorTemplateElem(), class_15, true, false, true, 20, true);
        Class class_16 = class$org$apache$xalan$templates$ElemAttribute == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemAttribute = XSLTSchema.class$("org.apache.xalan.templates.ElemAttribute")) : class$org$apache$xalan$templates$ElemAttribute;
        XSLTElementDef xSLTElementDef17 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "attribute", null, arrxSLTElementDef5, new XSLTAttributeDef[]{xSLTAttributeDef14, xSLTAttributeDef18, xSLTAttributeDef39}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_16, 20, true);
        Class class_17 = class$org$apache$xalan$templates$ElemCallTemplate == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemCallTemplate = XSLTSchema.class$("org.apache.xalan.templates.ElemCallTemplate")) : class$org$apache$xalan$templates$ElemCallTemplate;
        XSLTElementDef xSLTElementDef18 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "call-template", null, new XSLTElementDef[]{xSLTElementDef9}, new XSLTAttributeDef[]{xSLTAttributeDef13}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_17, 20, true);
        Class class_18 = class$org$apache$xalan$templates$ElemVariable == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemVariable = XSLTSchema.class$("org.apache.xalan.templates.ElemVariable")) : class$org$apache$xalan$templates$ElemVariable;
        XSLTElementDef xSLTElementDef19 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "variable", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef13, xSLTAttributeDef32}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_18, 20, true);
        Class class_19 = class$org$apache$xalan$templates$ElemParam == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemParam = XSLTSchema.class$("org.apache.xalan.templates.ElemParam")) : class$org$apache$xalan$templates$ElemParam;
        XSLTElementDef xSLTElementDef20 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "param", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef13, xSLTAttributeDef32}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_19, 19, true);
        Class class_20 = class$org$apache$xalan$templates$ElemText == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemText = XSLTSchema.class$("org.apache.xalan.templates.ElemText")) : class$org$apache$xalan$templates$ElemText;
        XSLTElementDef xSLTElementDef21 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "text", null, new XSLTElementDef[]{xSLTElementDef}, new XSLTAttributeDef[]{xSLTAttributeDef43}, (XSLTElementProcessor)new ProcessorText(), class_20, 20, true);
        Class class_21 = class$org$apache$xalan$templates$ElemPI == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemPI = XSLTSchema.class$("org.apache.xalan.templates.ElemPI")) : class$org$apache$xalan$templates$ElemPI;
        XSLTElementDef xSLTElementDef22 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "processing-instruction", null, arrxSLTElementDef5, new XSLTAttributeDef[]{xSLTAttributeDef15, xSLTAttributeDef39}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_21, 20, true);
        Class class_22 = class$org$apache$xalan$templates$ElemElement == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemElement = XSLTSchema.class$("org.apache.xalan.templates.ElemElement")) : class$org$apache$xalan$templates$ElemElement;
        XSLTElementDef xSLTElementDef23 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "element", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef14, xSLTAttributeDef18, xSLTAttributeDef29, xSLTAttributeDef39}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_22, 20, true);
        Class class_23 = class$org$apache$xalan$templates$ElemComment == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemComment = XSLTSchema.class$("org.apache.xalan.templates.ElemComment")) : class$org$apache$xalan$templates$ElemComment;
        XSLTElementDef xSLTElementDef24 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "comment", null, arrxSLTElementDef5, new XSLTAttributeDef[]{xSLTAttributeDef39}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_23, 20, true);
        Class class_24 = class$org$apache$xalan$templates$ElemCopy == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemCopy = XSLTSchema.class$("org.apache.xalan.templates.ElemCopy")) : class$org$apache$xalan$templates$ElemCopy;
        XSLTElementDef xSLTElementDef25 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "copy", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef39, xSLTAttributeDef29}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_24, 20, true);
        Class class_25 = class$org$apache$xalan$templates$ElemMessage == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemMessage = XSLTSchema.class$("org.apache.xalan.templates.ElemMessage")) : class$org$apache$xalan$templates$ElemMessage;
        XSLTElementDef xSLTElementDef26 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "message", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef56}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_25, 20, true);
        Class class_26 = class$org$apache$xalan$templates$ElemFallback == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemFallback = XSLTSchema.class$("org.apache.xalan.templates.ElemFallback")) : class$org$apache$xalan$templates$ElemFallback;
        XSLTElementDef xSLTElementDef27 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "fallback", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef39}, (XSLTElementProcessor)new ProcessorTemplateElem(), class_26, 20, true);
        Class class_27 = class$org$apache$xalan$templates$ElemExsltFunction == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemExsltFunction = XSLTSchema.class$("org.apache.xalan.templates.ElemExsltFunction")) : class$org$apache$xalan$templates$ElemExsltFunction;
        XSLTElementDef xSLTElementDef28 = new XSLTElementDef(this, "http://exslt.org/functions", "function", null, arrxSLTElementDef4, new XSLTAttributeDef[]{xSLTAttributeDef13}, new ProcessorExsltFunction(), class_27);
        Class class_28 = class$org$apache$xalan$templates$ElemExsltFuncResult == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemExsltFuncResult = XSLTSchema.class$("org.apache.xalan.templates.ElemExsltFuncResult")) : class$org$apache$xalan$templates$ElemExsltFuncResult;
        XSLTElementDef xSLTElementDef29 = new XSLTElementDef(this, "http://exslt.org/functions", "result", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef32}, new ProcessorExsltFuncResult(), class_28);
        int n2 = 0;
        arrxSLTElementDef[n2++] = xSLTElementDef;
        arrxSLTElementDef[n2++] = xSLTElementDef10;
        arrxSLTElementDef[n2++] = xSLTElementDef18;
        arrxSLTElementDef[n2++] = xSLTElementDef11;
        arrxSLTElementDef[n2++] = xSLTElementDef12;
        arrxSLTElementDef[n2++] = xSLTElementDef5;
        arrxSLTElementDef[n2++] = xSLTElementDef6;
        arrxSLTElementDef[n2++] = xSLTElementDef7;
        arrxSLTElementDef[n2++] = xSLTElementDef16;
        arrxSLTElementDef[n2++] = xSLTElementDef13;
        arrxSLTElementDef[n2++] = xSLTElementDef21;
        arrxSLTElementDef[n2++] = xSLTElementDef25;
        arrxSLTElementDef[n2++] = xSLTElementDef19;
        arrxSLTElementDef[n2++] = xSLTElementDef26;
        arrxSLTElementDef[n2++] = xSLTElementDef27;
        arrxSLTElementDef[n2++] = xSLTElementDef22;
        arrxSLTElementDef[n2++] = xSLTElementDef24;
        arrxSLTElementDef[n2++] = xSLTElementDef23;
        arrxSLTElementDef[n2++] = xSLTElementDef17;
        arrxSLTElementDef[n2++] = xSLTElementDef3;
        arrxSLTElementDef[n2++] = xSLTElementDef4;
        arrxSLTElementDef[n2++] = xSLTElementDef28;
        arrxSLTElementDef[n2++] = xSLTElementDef29;
        System.arraycopy(arrxSLTElementDef, 0, arrxSLTElementDef2, 0, n2);
        System.arraycopy(arrxSLTElementDef, 0, arrxSLTElementDef3, 0, n2);
        System.arraycopy(arrxSLTElementDef, 0, arrxSLTElementDef4, 0, n2);
        arrxSLTElementDef2[n2] = xSLTElementDef20;
        arrxSLTElementDef3[n2] = xSLTElementDef8;
        arrxSLTElementDef4[n2] = xSLTElementDef20;
        n2 = 0;
        arrxSLTElementDef5[n2++] = xSLTElementDef;
        arrxSLTElementDef5[n2++] = xSLTElementDef10;
        arrxSLTElementDef5[n2++] = xSLTElementDef18;
        arrxSLTElementDef5[n2++] = xSLTElementDef11;
        arrxSLTElementDef5[n2++] = xSLTElementDef12;
        arrxSLTElementDef5[n2++] = xSLTElementDef5;
        arrxSLTElementDef5[n2++] = xSLTElementDef6;
        arrxSLTElementDef5[n2++] = xSLTElementDef7;
        arrxSLTElementDef5[n2++] = xSLTElementDef16;
        arrxSLTElementDef5[n2++] = xSLTElementDef13;
        arrxSLTElementDef5[n2++] = xSLTElementDef21;
        arrxSLTElementDef5[n2++] = xSLTElementDef25;
        arrxSLTElementDef5[n2++] = xSLTElementDef19;
        arrxSLTElementDef5[n2++] = xSLTElementDef26;
        arrxSLTElementDef5[n2++] = xSLTElementDef27;
        XSLTElementDef xSLTElementDef30 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "import", null, null, new XSLTAttributeDef[]{xSLTAttributeDef}, (XSLTElementProcessor)new ProcessorImport(), null, 1, true);
        XSLTElementDef xSLTElementDef31 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "include", null, null, new XSLTAttributeDef[]{xSLTAttributeDef}, (XSLTElementProcessor)new ProcessorInclude(), null, 20, true);
        XSLTAttributeDef[] arrxSLTAttributeDef = new XSLTAttributeDef[]{new XSLTAttributeDef(null, "lang", 13, true, false, 2), new XSLTAttributeDef(null, "src", 2, false, false, 2)};
        XSLTAttributeDef[] arrxSLTAttributeDef2 = new XSLTAttributeDef[]{new XSLTAttributeDef(null, "prefix", 13, true, false, 2), new XSLTAttributeDef(null, "elements", 14, false, false, 2), new XSLTAttributeDef(null, "functions", 14, false, false, 2)};
        XSLTElementDef[] arrxSLTElementDef6 = new XSLTElementDef[17];
        arrxSLTElementDef6[0] = xSLTElementDef31;
        arrxSLTElementDef6[1] = xSLTElementDef30;
        arrxSLTElementDef6[2] = xSLTElementDef2;
        arrxSLTElementDef6[3] = xSLTElementDef4;
        arrxSLTElementDef6[4] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "strip-space", null, null, new XSLTAttributeDef[]{xSLTAttributeDef2}, (XSLTElementProcessor)new ProcessorStripSpace(), null, 20, true);
        arrxSLTElementDef6[5] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "preserve-space", null, null, new XSLTAttributeDef[]{xSLTAttributeDef2}, (XSLTElementProcessor)new ProcessorPreserveSpace(), null, 20, true);
        arrxSLTElementDef6[6] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "output", null, null, new XSLTAttributeDef[]{xSLTAttributeDef3, xSLTAttributeDef4, xSLTAttributeDef5, xSLTAttributeDef6, xSLTAttributeDef7, xSLTAttributeDef8, xSLTAttributeDef9, xSLTAttributeDef10, xSLTAttributeDef11, xSLTAttributeDef12, XSLTAttributeDef.m_foreignAttr}, (XSLTElementProcessor)new ProcessorOutputElem(), null, 20, true);
        arrxSLTElementDef6[7] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "key", null, null, new XSLTAttributeDef[]{xSLTAttributeDef13, xSLTAttributeDef35, xSLTAttributeDef17}, (XSLTElementProcessor)new ProcessorKey(), null, 20, true);
        arrxSLTElementDef6[8] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "decimal-format", null, null, new XSLTAttributeDef[]{xSLTAttributeDef16, xSLTAttributeDef19, xSLTAttributeDef28, xSLTAttributeDef20, xSLTAttributeDef21, xSLTAttributeDef22, xSLTAttributeDef23, xSLTAttributeDef24, xSLTAttributeDef25, xSLTAttributeDef26, xSLTAttributeDef27}, (XSLTElementProcessor)new ProcessorDecimalFormat(), null, 20, true);
        arrxSLTElementDef6[9] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "attribute-set", null, new XSLTElementDef[]{xSLTElementDef17}, new XSLTAttributeDef[]{xSLTAttributeDef13, xSLTAttributeDef29}, (XSLTElementProcessor)new ProcessorAttributeSet(), null, 20, true);
        arrxSLTElementDef6[10] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "variable", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef13, xSLTAttributeDef32}, (XSLTElementProcessor)new ProcessorGlobalVariableDecl(), class$org$apache$xalan$templates$ElemVariable == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemVariable = XSLTSchema.class$("org.apache.xalan.templates.ElemVariable")) : class$org$apache$xalan$templates$ElemVariable, 20, true);
        arrxSLTElementDef6[11] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "param", null, arrxSLTElementDef, new XSLTAttributeDef[]{xSLTAttributeDef13, xSLTAttributeDef32}, (XSLTElementProcessor)new ProcessorGlobalParamDecl(), class$org$apache$xalan$templates$ElemParam == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemParam = XSLTSchema.class$("org.apache.xalan.templates.ElemParam")) : class$org$apache$xalan$templates$ElemParam, 20, true);
        Class class_29 = class$org$apache$xalan$templates$ElemTemplate == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemTemplate = XSLTSchema.class$("org.apache.xalan.templates.ElemTemplate")) : class$org$apache$xalan$templates$ElemTemplate;
        arrxSLTElementDef6[12] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "template", null, arrxSLTElementDef2, new XSLTAttributeDef[]{xSLTAttributeDef36, xSLTAttributeDef16, xSLTAttributeDef37, xSLTAttributeDef38, xSLTAttributeDef39}, new ProcessorTemplate(), class_29, true, 20, true);
        arrxSLTElementDef6[13] = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "namespace-alias", null, null, new XSLTAttributeDef[]{xSLTAttributeDef41, xSLTAttributeDef42}, (XSLTElementProcessor)new ProcessorNamespaceAlias(), null, 20, true);
        XSLTElementDef[] arrxSLTElementDef7 = new XSLTElementDef[1];
        Class class_30 = class$org$apache$xalan$templates$ElemExtensionScript == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemExtensionScript = XSLTSchema.class$("org.apache.xalan.templates.ElemExtensionScript")) : class$org$apache$xalan$templates$ElemExtensionScript;
        arrxSLTElementDef7[0] = new XSLTElementDef(this, "http://xml.apache.org/xalan", "script", null, new XSLTElementDef[]{xSLTElementDef}, arrxSLTAttributeDef, (XSLTElementProcessor)new ProcessorLRE(), class_30, 20, true);
        Class class_31 = class$org$apache$xalan$templates$ElemExtensionDecl == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemExtensionDecl = XSLTSchema.class$("org.apache.xalan.templates.ElemExtensionDecl")) : class$org$apache$xalan$templates$ElemExtensionDecl;
        arrxSLTElementDef6[14] = new XSLTElementDef(this, "http://xml.apache.org/xalan", "component", null, arrxSLTElementDef7, arrxSLTAttributeDef2, new ProcessorLRE(), class_31);
        XSLTElementDef[] arrxSLTElementDef8 = new XSLTElementDef[1];
        arrxSLTElementDef8[0] = new XSLTElementDef(this, "http://xml.apache.org/xslt", "script", null, new XSLTElementDef[]{xSLTElementDef}, arrxSLTAttributeDef, (XSLTElementProcessor)new ProcessorLRE(), class$org$apache$xalan$templates$ElemExtensionScript == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemExtensionScript = XSLTSchema.class$("org.apache.xalan.templates.ElemExtensionScript")) : class$org$apache$xalan$templates$ElemExtensionScript, 20, true);
        arrxSLTElementDef6[15] = new XSLTElementDef(this, "http://xml.apache.org/xslt", "component", null, arrxSLTElementDef8, arrxSLTAttributeDef2, new ProcessorLRE(), class$org$apache$xalan$templates$ElemExtensionDecl == null ? (XSLTSchema.class$org$apache$xalan$templates$ElemExtensionDecl = XSLTSchema.class$("org.apache.xalan.templates.ElemExtensionDecl")) : class$org$apache$xalan$templates$ElemExtensionDecl);
        arrxSLTElementDef6[16] = xSLTElementDef28;
        XSLTElementDef[] arrxSLTElementDef9 = arrxSLTElementDef6;
        XSLTAttributeDef xSLTAttributeDef63 = new XSLTAttributeDef(null, "exclude-result-prefixes", 20, false, false, 2);
        XSLTAttributeDef xSLTAttributeDef64 = new XSLTAttributeDef(null, "extension-element-prefixes", 15, false, false, 2);
        XSLTAttributeDef xSLTAttributeDef65 = new XSLTAttributeDef(null, "id", 1, false, false, 2);
        XSLTAttributeDef xSLTAttributeDef66 = new XSLTAttributeDef(null, "version", 13, true, false, 2);
        XSLTElementDef xSLTElementDef32 = new XSLTElementDef(this, "http://www.w3.org/1999/XSL/Transform", "stylesheet", "transform", arrxSLTElementDef9, new XSLTAttributeDef[]{xSLTAttributeDef64, xSLTAttributeDef63, xSLTAttributeDef65, xSLTAttributeDef66, xSLTAttributeDef39}, new ProcessorStylesheetElement(), null, true, -1, false);
        xSLTElementDef30.setElements(new XSLTElementDef[]{xSLTElementDef32, xSLTElementDef3, xSLTElementDef4});
        xSLTElementDef31.setElements(new XSLTElementDef[]{xSLTElementDef32, xSLTElementDef3, xSLTElementDef4});
        this.build(null, null, null, new XSLTElementDef[]{xSLTElementDef32, xSLTElementDef2, xSLTElementDef3, xSLTElementDef4}, null, new ProcessorStylesheetDoc(), null);
    }

    public HashMap getElemsAvailable() {
        return this.m_availElems;
    }

    void addAvailableElement(QName qName) {
        this.m_availElems.put(qName, qName);
    }

    public boolean elementAvailable(QName qName) {
        return this.m_availElems.containsKey(qName);
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

