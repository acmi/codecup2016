/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.Serializable;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExtensionNamespacesManager;
import org.apache.xalan.processor.XSLTSchema;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.DecimalFormatProperties;
import org.apache.xalan.templates.ElemApplyTemplates;
import org.apache.xalan.templates.ElemAttributeSet;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemValueOf;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xalan.templates.NamespaceAlias;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.TemplateList;
import org.apache.xalan.templates.WhiteSpaceInfo;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;

public class StylesheetRoot
extends StylesheetComposed
implements Serializable,
Templates {
    static final long serialVersionUID = 3875353123529147855L;
    private boolean m_optimizer = true;
    private boolean m_incremental = false;
    private boolean m_source_location = false;
    private boolean m_isSecureProcessing = false;
    private HashMap m_availElems;
    private transient ExtensionNamespacesManager m_extNsMgr = null;
    private StylesheetComposed[] m_globalImportList;
    private OutputProperties m_outputProperties;
    private boolean m_outputMethodSet = false;
    private HashMap m_attrSets;
    private Hashtable m_decimalFormatSymbols;
    private Vector m_keyDecls;
    private Hashtable m_namespaceAliasComposed;
    private TemplateList m_templateList;
    private Vector m_variables;
    private TemplateList m_whiteSpaceInfoList;
    private ElemTemplate m_defaultTextRule;
    private ElemTemplate m_defaultRule;
    private ElemTemplate m_defaultRootRule;
    private ElemTemplate m_startRule;
    XPath m_selectDefault;
    private transient ComposeState m_composeState;
    private String m_extensionHandlerClass = "org.apache.xalan.extensions.ExtensionHandlerExsltFunction";

    public StylesheetRoot(ErrorListener errorListener) throws TransformerConfigurationException {
        super(null);
        this.setStylesheetRoot(this);
        try {
            this.m_selectDefault = new XPath("node()", this, this, 0, errorListener);
            this.initDefaultRule(errorListener);
        }
        catch (TransformerException transformerException) {
            throw new TransformerConfigurationException(XSLMessages.createMessage("ER_CANNOT_INIT_DEFAULT_TEMPLATES", null), transformerException);
        }
    }

    public StylesheetRoot(XSLTSchema xSLTSchema, ErrorListener errorListener) throws TransformerConfigurationException {
        this(errorListener);
        this.m_availElems = xSLTSchema.getElemsAvailable();
    }

    public boolean isRoot() {
        return true;
    }

    public void setSecureProcessing(boolean bl) {
        this.m_isSecureProcessing = bl;
    }

    public boolean isSecureProcessing() {
        return this.m_isSecureProcessing;
    }

    public HashMap getAvailableElements() {
        return this.m_availElems;
    }

    public ExtensionNamespacesManager getExtensionNamespacesManager() {
        if (this.m_extNsMgr == null) {
            this.m_extNsMgr = new ExtensionNamespacesManager();
        }
        return this.m_extNsMgr;
    }

    public Vector getExtensions() {
        return this.m_extNsMgr != null ? this.m_extNsMgr.getExtensions() : null;
    }

    public Transformer newTransformer() {
        return new TransformerImpl(this);
    }

    public Properties getDefaultOutputProps() {
        return this.m_outputProperties.getProperties();
    }

    public Properties getOutputProperties() {
        return (Properties)this.getDefaultOutputProps().clone();
    }

    public void recompose() throws TransformerException {
        int n2;
        Vector vector = new Vector();
        if (null == this.m_globalImportList) {
            Vector vector2 = new Vector();
            this.addImports(this, true, vector2);
            this.m_globalImportList = new StylesheetComposed[vector2.size()];
            int n3 = vector2.size() - 1;
            for (n2 = 0; n2 < vector2.size(); ++n2) {
                this.m_globalImportList[n3] = (StylesheetComposed)vector2.elementAt(n2);
                this.m_globalImportList[n3].recomposeIncludes(this.m_globalImportList[n3]);
                this.m_globalImportList[n3--].recomposeImports();
            }
        }
        int n4 = this.getGlobalImportCount();
        for (n2 = 0; n2 < n4; ++n2) {
            StylesheetComposed stylesheetComposed = this.getGlobalImport(n2);
            stylesheetComposed.recompose(vector);
        }
        this.QuickSort2(vector, 0, vector.size() - 1);
        this.m_outputProperties = new OutputProperties("");
        this.m_attrSets = new HashMap();
        this.m_decimalFormatSymbols = new Hashtable();
        this.m_keyDecls = new Vector();
        this.m_namespaceAliasComposed = new Hashtable();
        this.m_templateList = new TemplateList();
        this.m_variables = new Vector();
        for (n2 = vector.size() - 1; n2 >= 0; --n2) {
            ((ElemTemplateElement)vector.elementAt(n2)).recompose(this);
        }
        this.initComposeState();
        this.m_templateList.compose(this);
        this.m_outputProperties.compose(this);
        this.m_outputProperties.endCompose(this);
        n4 = this.getGlobalImportCount();
        for (n2 = 0; n2 < n4; ++n2) {
            StylesheetComposed stylesheetComposed = this.getGlobalImport(n2);
            int n5 = stylesheetComposed.getIncludeCountComposed();
            for (int i2 = -1; i2 < n5; ++i2) {
                Stylesheet stylesheet = stylesheetComposed.getIncludeComposed(i2);
                this.composeTemplates(stylesheet);
            }
        }
        if (this.m_extNsMgr != null) {
            this.m_extNsMgr.registerUnregisteredNamespaces();
        }
        this.clearComposeState();
    }

    void composeTemplates(ElemTemplateElement elemTemplateElement) throws TransformerException {
        elemTemplateElement.compose(this);
        for (ElemTemplateElement elemTemplateElement2 = elemTemplateElement.getFirstChildElem(); elemTemplateElement2 != null; elemTemplateElement2 = elemTemplateElement2.getNextSiblingElem()) {
            this.composeTemplates(elemTemplateElement2);
        }
        elemTemplateElement.endCompose(this);
    }

    protected void addImports(Stylesheet stylesheet, boolean bl, Vector vector) {
        int n2;
        int n3 = stylesheet.getImportCount();
        if (n3 > 0) {
            for (n2 = 0; n2 < n3; ++n2) {
                StylesheetComposed stylesheetComposed = stylesheet.getImport(n2);
                this.addImports(stylesheetComposed, true, vector);
            }
        }
        if ((n3 = stylesheet.getIncludeCount()) > 0) {
            for (n2 = 0; n2 < n3; ++n2) {
                Stylesheet stylesheet2 = stylesheet.getInclude(n2);
                this.addImports(stylesheet2, false, vector);
            }
        }
        if (bl) {
            vector.addElement(stylesheet);
        }
    }

    public StylesheetComposed getGlobalImport(int n2) {
        return this.m_globalImportList[n2];
    }

    public int getGlobalImportCount() {
        return this.m_globalImportList != null ? this.m_globalImportList.length : 1;
    }

    public int getImportNumber(StylesheetComposed stylesheetComposed) {
        if (this == stylesheetComposed) {
            return 0;
        }
        int n2 = this.getGlobalImportCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stylesheetComposed != this.getGlobalImport(i2)) continue;
            return i2;
        }
        return -1;
    }

    void recomposeOutput(OutputProperties outputProperties) throws TransformerException {
        this.m_outputProperties.copyFrom(outputProperties);
    }

    public OutputProperties getOutputComposed() {
        return this.m_outputProperties;
    }

    public boolean isOutputMethodSet() {
        return this.m_outputMethodSet;
    }

    void recomposeAttributeSets(ElemAttributeSet elemAttributeSet) {
        ArrayList<ElemAttributeSet> arrayList = (ArrayList<ElemAttributeSet>)this.m_attrSets.get(elemAttributeSet.getName());
        if (null == arrayList) {
            arrayList = new ArrayList<ElemAttributeSet>();
            this.m_attrSets.put(elemAttributeSet.getName(), arrayList);
        }
        arrayList.add(elemAttributeSet);
    }

    public ArrayList getAttributeSetComposed(QName qName) throws ArrayIndexOutOfBoundsException {
        return (ArrayList)this.m_attrSets.get(qName);
    }

    void recomposeDecimalFormats(DecimalFormatProperties decimalFormatProperties) {
        DecimalFormatSymbols decimalFormatSymbols = (DecimalFormatSymbols)this.m_decimalFormatSymbols.get(decimalFormatProperties.getName());
        if (null == decimalFormatSymbols) {
            this.m_decimalFormatSymbols.put(decimalFormatProperties.getName(), decimalFormatProperties.getDecimalFormatSymbols());
        } else if (!decimalFormatProperties.getDecimalFormatSymbols().equals(decimalFormatSymbols)) {
            String string = decimalFormatProperties.getName().equals(new QName("")) ? XSLMessages.createWarning("WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED", new Object[0]) : XSLMessages.createWarning("WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE", new Object[]{decimalFormatProperties.getName()});
            this.error(string);
        }
    }

    public DecimalFormatSymbols getDecimalFormatComposed(QName qName) {
        return (DecimalFormatSymbols)this.m_decimalFormatSymbols.get(qName);
    }

    void recomposeKeys(KeyDeclaration keyDeclaration) {
        this.m_keyDecls.addElement(keyDeclaration);
    }

    public Vector getKeysComposed() {
        return this.m_keyDecls;
    }

    void recomposeNamespaceAliases(NamespaceAlias namespaceAlias) {
        this.m_namespaceAliasComposed.put(namespaceAlias.getStylesheetNamespace(), namespaceAlias);
    }

    public NamespaceAlias getNamespaceAliasComposed(String string) {
        return null == this.m_namespaceAliasComposed ? null : this.m_namespaceAliasComposed.get(string);
    }

    void recomposeTemplates(ElemTemplate elemTemplate) {
        this.m_templateList.setTemplate(elemTemplate);
    }

    public final TemplateList getTemplateListComposed() {
        return this.m_templateList;
    }

    public final void setTemplateListComposed(TemplateList templateList) {
        this.m_templateList = templateList;
    }

    public ElemTemplate getTemplateComposed(XPathContext xPathContext, int n2, QName qName, boolean bl, DTM dTM) throws TransformerException {
        return this.m_templateList.getTemplate(xPathContext, n2, qName, bl, dTM);
    }

    public ElemTemplate getTemplateComposed(XPathContext xPathContext, int n2, QName qName, int n3, int n4, boolean bl, DTM dTM) throws TransformerException {
        return this.m_templateList.getTemplate(xPathContext, n2, qName, n3, n4, bl, dTM);
    }

    public ElemTemplate getTemplateComposed(QName qName) {
        return this.m_templateList.getTemplate(qName);
    }

    void recomposeVariables(ElemVariable elemVariable) {
        if (this.getVariableOrParamComposed(elemVariable.getName()) == null) {
            elemVariable.setIsTopLevel(true);
            elemVariable.setIndex(this.m_variables.size());
            this.m_variables.addElement(elemVariable);
        }
    }

    public ElemVariable getVariableOrParamComposed(QName qName) {
        if (null != this.m_variables) {
            int n2 = this.m_variables.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                ElemVariable elemVariable = (ElemVariable)this.m_variables.elementAt(i2);
                if (!elemVariable.getName().equals(qName)) continue;
                return elemVariable;
            }
        }
        return null;
    }

    public Vector getVariablesAndParamsComposed() {
        return this.m_variables;
    }

    void recomposeWhiteSpaceInfo(WhiteSpaceInfo whiteSpaceInfo) {
        if (null == this.m_whiteSpaceInfoList) {
            this.m_whiteSpaceInfoList = new TemplateList();
        }
        this.m_whiteSpaceInfoList.setTemplate(whiteSpaceInfo);
    }

    public boolean shouldCheckWhitespace() {
        return null != this.m_whiteSpaceInfoList;
    }

    public WhiteSpaceInfo getWhiteSpaceInfo(XPathContext xPathContext, int n2, DTM dTM) throws TransformerException {
        if (null != this.m_whiteSpaceInfoList) {
            return (WhiteSpaceInfo)this.m_whiteSpaceInfoList.getTemplate(xPathContext, n2, null, false, dTM);
        }
        return null;
    }

    public boolean shouldStripWhiteSpace(XPathContext xPathContext, int n2) throws TransformerException {
        if (null != this.m_whiteSpaceInfoList) {
            while (-1 != n2) {
                DTM dTM = xPathContext.getDTM(n2);
                WhiteSpaceInfo whiteSpaceInfo = (WhiteSpaceInfo)this.m_whiteSpaceInfoList.getTemplate(xPathContext, n2, null, false, dTM);
                if (null != whiteSpaceInfo) {
                    return whiteSpaceInfo.getShouldStripSpace();
                }
                int n3 = dTM.getParent(n2);
                if (-1 != n3 && 1 == dTM.getNodeType(n3)) {
                    n2 = n3;
                    continue;
                }
                n2 = -1;
            }
        }
        return false;
    }

    public boolean canStripWhiteSpace() {
        return null != this.m_whiteSpaceInfoList;
    }

    public final ElemTemplate getDefaultTextRule() {
        return this.m_defaultTextRule;
    }

    public final ElemTemplate getDefaultRule() {
        return this.m_defaultRule;
    }

    public final ElemTemplate getDefaultRootRule() {
        return this.m_defaultRootRule;
    }

    public final ElemTemplate getStartRule() {
        return this.m_startRule;
    }

    private void initDefaultRule(ErrorListener errorListener) throws TransformerException {
        this.m_defaultRule = new ElemTemplate();
        this.m_defaultRule.setStylesheet(this);
        XPath xPath = new XPath("*", this, this, 1, errorListener);
        this.m_defaultRule.setMatch(xPath);
        ElemApplyTemplates elemApplyTemplates = new ElemApplyTemplates();
        elemApplyTemplates.setIsDefaultTemplate(true);
        elemApplyTemplates.setSelect(this.m_selectDefault);
        this.m_defaultRule.appendChild(elemApplyTemplates);
        this.m_startRule = this.m_defaultRule;
        this.m_defaultTextRule = new ElemTemplate();
        this.m_defaultTextRule.setStylesheet(this);
        xPath = new XPath("text() | @*", this, this, 1, errorListener);
        this.m_defaultTextRule.setMatch(xPath);
        ElemValueOf elemValueOf = new ElemValueOf();
        this.m_defaultTextRule.appendChild(elemValueOf);
        XPath xPath2 = new XPath(".", this, this, 0, errorListener);
        elemValueOf.setSelect(xPath2);
        this.m_defaultRootRule = new ElemTemplate();
        this.m_defaultRootRule.setStylesheet(this);
        xPath = new XPath("/", this, this, 1, errorListener);
        this.m_defaultRootRule.setMatch(xPath);
        elemApplyTemplates = new ElemApplyTemplates();
        elemApplyTemplates.setIsDefaultTemplate(true);
        this.m_defaultRootRule.appendChild(elemApplyTemplates);
        elemApplyTemplates.setSelect(this.m_selectDefault);
    }

    private void QuickSort2(Vector vector, int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        if (n3 > n2) {
            ElemTemplateElement elemTemplateElement = (ElemTemplateElement)vector.elementAt((n2 + n3) / 2);
            while (n4 <= n5) {
                while (n4 < n3 && ((ElemTemplateElement)vector.elementAt(n4)).compareTo(elemTemplateElement) < 0) {
                    ++n4;
                }
                while (n5 > n2 && ((ElemTemplateElement)vector.elementAt(n5)).compareTo(elemTemplateElement) > 0) {
                    --n5;
                }
                if (n4 > n5) continue;
                ElemTemplateElement elemTemplateElement2 = (ElemTemplateElement)vector.elementAt(n4);
                vector.setElementAt(vector.elementAt(n5), n4);
                vector.setElementAt(elemTemplateElement2, n5);
                ++n4;
                --n5;
            }
            if (n2 < n5) {
                this.QuickSort2(vector, n2, n5);
            }
            if (n4 < n3) {
                this.QuickSort2(vector, n4, n3);
            }
        }
    }

    void initComposeState() {
        this.m_composeState = new ComposeState(this);
    }

    ComposeState getComposeState() {
        return this.m_composeState;
    }

    private void clearComposeState() {
        this.m_composeState = null;
    }

    public String setExtensionHandlerClass(String string) {
        String string2 = this.m_extensionHandlerClass;
        this.m_extensionHandlerClass = string;
        return string2;
    }

    public String getExtensionHandlerClass() {
        return this.m_extensionHandlerClass;
    }

    public boolean getOptimizer() {
        return this.m_optimizer;
    }

    public void setOptimizer(boolean bl) {
        this.m_optimizer = bl;
    }

    public boolean getIncremental() {
        return this.m_incremental;
    }

    public boolean getSource_location() {
        return this.m_source_location;
    }

    public void setIncremental(boolean bl) {
        this.m_incremental = bl;
    }

    public void setSource_location(boolean bl) {
        this.m_source_location = bl;
    }

    static Vector access$000(StylesheetRoot stylesheetRoot) {
        return stylesheetRoot.m_variables;
    }

    class ComposeState {
        private ExpandedNameTable m_ent;
        private Vector m_variableNames;
        IntStack m_marks;
        private int m_maxStackFrameSize;
        private final StylesheetRoot this$0;

        ComposeState(StylesheetRoot stylesheetRoot) {
            this.this$0 = stylesheetRoot;
            this.m_ent = new ExpandedNameTable();
            this.m_variableNames = new Vector();
            this.m_marks = new IntStack();
            int n2 = StylesheetRoot.access$000(stylesheetRoot).size();
            for (int i2 = 0; i2 < n2; ++i2) {
                ElemVariable elemVariable = (ElemVariable)StylesheetRoot.access$000(stylesheetRoot).elementAt(i2);
                this.m_variableNames.addElement(elemVariable.getName());
            }
        }

        public int getQNameID(QName qName) {
            return this.m_ent.getExpandedTypeID(qName.getNamespace(), qName.getLocalName(), 1);
        }

        int addVariableName(QName qName) {
            int n2 = this.m_variableNames.size();
            this.m_variableNames.addElement(qName);
            int n3 = this.m_variableNames.size() - this.getGlobalsSize();
            if (n3 > this.m_maxStackFrameSize) {
                ++this.m_maxStackFrameSize;
            }
            return n2;
        }

        void resetStackFrameSize() {
            this.m_maxStackFrameSize = 0;
        }

        int getFrameSize() {
            return this.m_maxStackFrameSize;
        }

        int getCurrentStackFrameSize() {
            return this.m_variableNames.size();
        }

        void setCurrentStackFrameSize(int n2) {
            this.m_variableNames.setSize(n2);
        }

        int getGlobalsSize() {
            return StylesheetRoot.access$000(this.this$0).size();
        }

        void pushStackMark() {
            this.m_marks.push(this.getCurrentStackFrameSize());
        }

        void popStackMark() {
            int n2 = this.m_marks.pop();
            this.setCurrentStackFrameSize(n2);
        }

        Vector getVariableNames() {
            return this.m_variableNames;
        }
    }

}

