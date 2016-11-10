/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.DecimalFormatProperties;
import org.apache.xalan.templates.ElemAttributeSet;
import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xalan.templates.NamespaceAlias;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.WhiteSpaceInfo;
import org.apache.xalan.templates.XSLTVisitor;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.StringVector;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.WrappedRuntimeException;

public class Stylesheet
extends ElemTemplateElement
implements Serializable {
    static final long serialVersionUID = 2085337282743043776L;
    public static final String STYLESHEET_EXT = ".lxc";
    private String m_XmlnsXsl;
    private StringVector m_ExtensionElementURIs;
    private StringVector m_ExcludeResultPrefixs;
    private String m_Id;
    private String m_Version;
    private boolean m_isCompatibleMode = false;
    private Vector m_imports;
    private Vector m_includes;
    Stack m_DecimalFormatDeclarations;
    private Vector m_whitespaceStrippingElements;
    private Vector m_whitespacePreservingElements;
    private Vector m_output;
    private Vector m_keyDeclarations;
    private Vector m_attributeSets;
    private Vector m_topLevelVariables;
    private Vector m_templates;
    private Vector m_prefix_aliases;
    private Hashtable m_NonXslTopLevel;
    private String m_href = null;
    private String m_publicId;
    private String m_systemId;
    private StylesheetRoot m_stylesheetRoot;
    private Stylesheet m_stylesheetParent;

    public Stylesheet(Stylesheet stylesheet) {
        if (null != stylesheet) {
            this.m_stylesheetParent = stylesheet;
            this.m_stylesheetRoot = stylesheet.getStylesheetRoot();
        }
    }

    public Stylesheet getStylesheet() {
        return this;
    }

    public boolean isAggregatedType() {
        return false;
    }

    public boolean isRoot() {
        return false;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, TransformerException {
        try {
            objectInputStream.defaultReadObject();
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new TransformerException(classNotFoundException);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    public void setXmlnsXsl(String string) {
        this.m_XmlnsXsl = string;
    }

    public String getXmlnsXsl() {
        return this.m_XmlnsXsl;
    }

    public void setExtensionElementPrefixes(StringVector stringVector) {
        this.m_ExtensionElementURIs = stringVector;
    }

    public String getExtensionElementPrefix(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_ExtensionElementURIs) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.m_ExtensionElementURIs.elementAt(n2);
    }

    public int getExtensionElementPrefixCount() {
        return null != this.m_ExtensionElementURIs ? this.m_ExtensionElementURIs.size() : 0;
    }

    public boolean containsExtensionElementURI(String string) {
        if (null == this.m_ExtensionElementURIs) {
            return false;
        }
        return this.m_ExtensionElementURIs.contains(string);
    }

    public void setExcludeResultPrefixes(StringVector stringVector) {
        this.m_ExcludeResultPrefixs = stringVector;
    }

    public String getExcludeResultPrefix(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_ExcludeResultPrefixs) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.m_ExcludeResultPrefixs.elementAt(n2);
    }

    public int getExcludeResultPrefixCount() {
        return null != this.m_ExcludeResultPrefixs ? this.m_ExcludeResultPrefixs.size() : 0;
    }

    public boolean containsExcludeResultPrefix(String string, String string2) {
        if (null == this.m_ExcludeResultPrefixs || string2 == null) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_ExcludeResultPrefixs.size(); ++i2) {
            if (!string2.equals(this.getNamespaceForPrefix(this.m_ExcludeResultPrefixs.elementAt(i2)))) continue;
            return true;
        }
        return false;
    }

    public void setId(String string) {
        this.m_Id = string;
    }

    public String getId() {
        return this.m_Id;
    }

    public void setVersion(String string) {
        this.m_Version = string;
        this.m_isCompatibleMode = Double.valueOf(string) > 1.0;
    }

    public boolean getCompatibleMode() {
        return this.m_isCompatibleMode;
    }

    public String getVersion() {
        return this.m_Version;
    }

    public void setImport(StylesheetComposed stylesheetComposed) {
        if (null == this.m_imports) {
            this.m_imports = new Vector();
        }
        this.m_imports.addElement(stylesheetComposed);
    }

    public StylesheetComposed getImport(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_imports) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (StylesheetComposed)this.m_imports.elementAt(n2);
    }

    public int getImportCount() {
        return null != this.m_imports ? this.m_imports.size() : 0;
    }

    public void setInclude(Stylesheet stylesheet) {
        if (null == this.m_includes) {
            this.m_includes = new Vector();
        }
        this.m_includes.addElement(stylesheet);
    }

    public Stylesheet getInclude(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_includes) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (Stylesheet)this.m_includes.elementAt(n2);
    }

    public int getIncludeCount() {
        return null != this.m_includes ? this.m_includes.size() : 0;
    }

    public void setDecimalFormat(DecimalFormatProperties decimalFormatProperties) {
        if (null == this.m_DecimalFormatDeclarations) {
            this.m_DecimalFormatDeclarations = new Stack();
        }
        this.m_DecimalFormatDeclarations.push(decimalFormatProperties);
    }

    public DecimalFormatProperties getDecimalFormat(QName qName) {
        if (null == this.m_DecimalFormatDeclarations) {
            return null;
        }
        int n2 = this.getDecimalFormatCount();
        for (int i2 = n2 - 1; i2 >= 0; ++i2) {
            DecimalFormatProperties decimalFormatProperties = this.getDecimalFormat(i2);
            if (!decimalFormatProperties.getName().equals(qName)) continue;
            return decimalFormatProperties;
        }
        return null;
    }

    public DecimalFormatProperties getDecimalFormat(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_DecimalFormatDeclarations) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (DecimalFormatProperties)this.m_DecimalFormatDeclarations.elementAt(n2);
    }

    public int getDecimalFormatCount() {
        return null != this.m_DecimalFormatDeclarations ? this.m_DecimalFormatDeclarations.size() : 0;
    }

    public void setStripSpaces(WhiteSpaceInfo whiteSpaceInfo) {
        if (null == this.m_whitespaceStrippingElements) {
            this.m_whitespaceStrippingElements = new Vector();
        }
        this.m_whitespaceStrippingElements.addElement(whiteSpaceInfo);
    }

    public WhiteSpaceInfo getStripSpace(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_whitespaceStrippingElements) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (WhiteSpaceInfo)this.m_whitespaceStrippingElements.elementAt(n2);
    }

    public int getStripSpaceCount() {
        return null != this.m_whitespaceStrippingElements ? this.m_whitespaceStrippingElements.size() : 0;
    }

    public void setPreserveSpaces(WhiteSpaceInfo whiteSpaceInfo) {
        if (null == this.m_whitespacePreservingElements) {
            this.m_whitespacePreservingElements = new Vector();
        }
        this.m_whitespacePreservingElements.addElement(whiteSpaceInfo);
    }

    public WhiteSpaceInfo getPreserveSpace(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_whitespacePreservingElements) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (WhiteSpaceInfo)this.m_whitespacePreservingElements.elementAt(n2);
    }

    public int getPreserveSpaceCount() {
        return null != this.m_whitespacePreservingElements ? this.m_whitespacePreservingElements.size() : 0;
    }

    public void setOutput(OutputProperties outputProperties) {
        if (null == this.m_output) {
            this.m_output = new Vector();
        }
        this.m_output.addElement(outputProperties);
    }

    public OutputProperties getOutput(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_output) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (OutputProperties)this.m_output.elementAt(n2);
    }

    public int getOutputCount() {
        return null != this.m_output ? this.m_output.size() : 0;
    }

    public void setKey(KeyDeclaration keyDeclaration) {
        if (null == this.m_keyDeclarations) {
            this.m_keyDeclarations = new Vector();
        }
        this.m_keyDeclarations.addElement(keyDeclaration);
    }

    public KeyDeclaration getKey(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_keyDeclarations) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (KeyDeclaration)this.m_keyDeclarations.elementAt(n2);
    }

    public int getKeyCount() {
        return null != this.m_keyDeclarations ? this.m_keyDeclarations.size() : 0;
    }

    public void setAttributeSet(ElemAttributeSet elemAttributeSet) {
        if (null == this.m_attributeSets) {
            this.m_attributeSets = new Vector();
        }
        this.m_attributeSets.addElement(elemAttributeSet);
    }

    public ElemAttributeSet getAttributeSet(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_attributeSets) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (ElemAttributeSet)this.m_attributeSets.elementAt(n2);
    }

    public int getAttributeSetCount() {
        return null != this.m_attributeSets ? this.m_attributeSets.size() : 0;
    }

    public void setVariable(ElemVariable elemVariable) {
        if (null == this.m_topLevelVariables) {
            this.m_topLevelVariables = new Vector();
        }
        this.m_topLevelVariables.addElement(elemVariable);
    }

    public ElemVariable getVariableOrParam(QName qName) {
        if (null != this.m_topLevelVariables) {
            int n2 = this.getVariableOrParamCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                ElemVariable elemVariable = this.getVariableOrParam(i2);
                if (!elemVariable.getName().equals(qName)) continue;
                return elemVariable;
            }
        }
        return null;
    }

    public ElemVariable getVariable(QName qName) {
        if (null != this.m_topLevelVariables) {
            int n2 = this.getVariableOrParamCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                ElemVariable elemVariable = this.getVariableOrParam(i2);
                if (elemVariable.getXSLToken() != 73 || !elemVariable.getName().equals(qName)) continue;
                return elemVariable;
            }
        }
        return null;
    }

    public ElemVariable getVariableOrParam(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_topLevelVariables) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (ElemVariable)this.m_topLevelVariables.elementAt(n2);
    }

    public int getVariableOrParamCount() {
        return null != this.m_topLevelVariables ? this.m_topLevelVariables.size() : 0;
    }

    public void setParam(ElemParam elemParam) {
        this.setVariable(elemParam);
    }

    public ElemParam getParam(QName qName) {
        if (null != this.m_topLevelVariables) {
            int n2 = this.getVariableOrParamCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                ElemVariable elemVariable = this.getVariableOrParam(i2);
                if (elemVariable.getXSLToken() != 41 || !elemVariable.getName().equals(qName)) continue;
                return (ElemParam)elemVariable;
            }
        }
        return null;
    }

    public void setTemplate(ElemTemplate elemTemplate) {
        if (null == this.m_templates) {
            this.m_templates = new Vector();
        }
        this.m_templates.addElement(elemTemplate);
        elemTemplate.setStylesheet(this);
    }

    public ElemTemplate getTemplate(int n2) throws TransformerException {
        if (null == this.m_templates) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (ElemTemplate)this.m_templates.elementAt(n2);
    }

    public int getTemplateCount() {
        return null != this.m_templates ? this.m_templates.size() : 0;
    }

    public void setNamespaceAlias(NamespaceAlias namespaceAlias) {
        if (this.m_prefix_aliases == null) {
            this.m_prefix_aliases = new Vector();
        }
        this.m_prefix_aliases.addElement(namespaceAlias);
    }

    public NamespaceAlias getNamespaceAlias(int n2) throws ArrayIndexOutOfBoundsException {
        if (null == this.m_prefix_aliases) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (NamespaceAlias)this.m_prefix_aliases.elementAt(n2);
    }

    public int getNamespaceAliasCount() {
        return null != this.m_prefix_aliases ? this.m_prefix_aliases.size() : 0;
    }

    public void setNonXslTopLevel(QName qName, Object object) {
        if (null == this.m_NonXslTopLevel) {
            this.m_NonXslTopLevel = new Hashtable();
        }
        this.m_NonXslTopLevel.put(qName, object);
    }

    public Object getNonXslTopLevel(QName qName) {
        return null != this.m_NonXslTopLevel ? this.m_NonXslTopLevel.get(qName) : null;
    }

    public String getHref() {
        return this.m_href;
    }

    public void setHref(String string) {
        this.m_href = string;
    }

    public void setLocaterInfo(SourceLocator sourceLocator) {
        if (null != sourceLocator) {
            this.m_publicId = sourceLocator.getPublicId();
            this.m_systemId = sourceLocator.getSystemId();
            if (null != this.m_systemId) {
                try {
                    this.m_href = SystemIDResolver.getAbsoluteURI(this.m_systemId, null);
                }
                catch (TransformerException transformerException) {
                    // empty catch block
                }
            }
            super.setLocaterInfo(sourceLocator);
        }
    }

    public StylesheetRoot getStylesheetRoot() {
        return this.m_stylesheetRoot;
    }

    public void setStylesheetRoot(StylesheetRoot stylesheetRoot) {
        this.m_stylesheetRoot = stylesheetRoot;
    }

    public Stylesheet getStylesheetParent() {
        return this.m_stylesheetParent;
    }

    public void setStylesheetParent(Stylesheet stylesheet) {
        this.m_stylesheetParent = stylesheet;
    }

    public StylesheetComposed getStylesheetComposed() {
        Stylesheet stylesheet = this;
        while (!stylesheet.isAggregatedType()) {
            stylesheet = stylesheet.getStylesheetParent();
        }
        return (StylesheetComposed)stylesheet;
    }

    public short getNodeType() {
        return 9;
    }

    public int getXSLToken() {
        return 25;
    }

    public String getNodeName() {
        return "stylesheet";
    }

    public void replaceTemplate(ElemTemplate elemTemplate, int n2) throws TransformerException {
        if (null == this.m_templates) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.replaceChild(elemTemplate, (ElemTemplateElement)this.m_templates.elementAt(n2));
        this.m_templates.setElementAt(elemTemplate, n2);
        elemTemplate.setStylesheet(this);
    }

    protected void callChildVisitors(XSLTVisitor xSLTVisitor, boolean bl) {
        ElemTemplateElement elemTemplateElement;
        int n2;
        int n3 = this.getImportCount();
        for (n2 = 0; n2 < n3; ++n2) {
            this.getImport(n2).callVisitors(xSLTVisitor);
        }
        n3 = this.getIncludeCount();
        for (n2 = 0; n2 < n3; ++n2) {
            this.getInclude(n2).callVisitors(xSLTVisitor);
        }
        n3 = this.getOutputCount();
        for (n2 = 0; n2 < n3; ++n2) {
            xSLTVisitor.visitTopLevelInstruction(this.getOutput(n2));
        }
        n3 = this.getAttributeSetCount();
        for (n2 = 0; n2 < n3; ++n2) {
            elemTemplateElement = this.getAttributeSet(n2);
            if (!xSLTVisitor.visitTopLevelInstruction(elemTemplateElement)) continue;
            elemTemplateElement.callChildVisitors(xSLTVisitor);
        }
        n3 = this.getDecimalFormatCount();
        for (n2 = 0; n2 < n3; ++n2) {
            xSLTVisitor.visitTopLevelInstruction(this.getDecimalFormat(n2));
        }
        n3 = this.getKeyCount();
        for (n2 = 0; n2 < n3; ++n2) {
            xSLTVisitor.visitTopLevelInstruction(this.getKey(n2));
        }
        n3 = this.getNamespaceAliasCount();
        for (n2 = 0; n2 < n3; ++n2) {
            xSLTVisitor.visitTopLevelInstruction(this.getNamespaceAlias(n2));
        }
        n3 = this.getTemplateCount();
        for (n2 = 0; n2 < n3; ++n2) {
            try {
                elemTemplateElement = this.getTemplate(n2);
                if (!xSLTVisitor.visitTopLevelInstruction(elemTemplateElement)) continue;
                elemTemplateElement.callChildVisitors(xSLTVisitor);
                continue;
            }
            catch (TransformerException transformerException) {
                throw new WrappedRuntimeException(transformerException);
            }
        }
        n3 = this.getVariableOrParamCount();
        for (n2 = 0; n2 < n3; ++n2) {
            elemTemplateElement = this.getVariableOrParam(n2);
            if (!xSLTVisitor.visitTopLevelVariableOrParamDecl(elemTemplateElement)) continue;
            elemTemplateElement.callChildVisitors(xSLTVisitor);
        }
        n3 = this.getStripSpaceCount();
        for (n2 = 0; n2 < n3; ++n2) {
            xSLTVisitor.visitTopLevelInstruction(this.getStripSpace(n2));
        }
        n3 = this.getPreserveSpaceCount();
        for (n2 = 0; n2 < n3; ++n2) {
            xSLTVisitor.visitTopLevelInstruction(this.getPreserveSpace(n2));
        }
        if (null != this.m_NonXslTopLevel) {
            Enumeration enumeration = this.m_NonXslTopLevel.elements();
            while (enumeration.hasMoreElements()) {
                elemTemplateElement = (ElemTemplateElement)enumeration.nextElement();
                if (!xSLTVisitor.visitTopLevelInstruction(elemTemplateElement)) continue;
                elemTemplateElement.callChildVisitors(xSLTVisitor);
            }
        }
    }

    protected boolean accept(XSLTVisitor xSLTVisitor) {
        return xSLTVisitor.visitStylesheet(this);
    }
}

