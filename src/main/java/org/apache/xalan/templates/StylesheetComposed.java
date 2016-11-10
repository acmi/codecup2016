/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.DecimalFormatProperties;
import org.apache.xalan.templates.ElemAttributeSet;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xalan.templates.NamespaceAlias;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.WhiteSpaceInfo;

public class StylesheetComposed
extends Stylesheet {
    static final long serialVersionUID = -3444072247410233923L;
    private int m_importNumber = -1;
    private int m_importCountComposed;
    private int m_endImportCountComposed;
    private transient Vector m_includesComposed;

    public StylesheetComposed(Stylesheet stylesheet) {
        super(stylesheet);
    }

    public boolean isAggregatedType() {
        return true;
    }

    public void recompose(Vector vector) throws TransformerException {
        int n2 = this.getIncludeCountComposed();
        for (int i2 = -1; i2 < n2; ++i2) {
            int n3;
            Stylesheet stylesheet = this.getIncludeComposed(i2);
            int n4 = stylesheet.getOutputCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getOutput(n3));
            }
            n4 = stylesheet.getAttributeSetCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getAttributeSet(n3));
            }
            n4 = stylesheet.getDecimalFormatCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getDecimalFormat(n3));
            }
            n4 = stylesheet.getKeyCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getKey(n3));
            }
            n4 = stylesheet.getNamespaceAliasCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getNamespaceAlias(n3));
            }
            n4 = stylesheet.getTemplateCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getTemplate(n3));
            }
            n4 = stylesheet.getVariableOrParamCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getVariableOrParam(n3));
            }
            n4 = stylesheet.getStripSpaceCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getStripSpace(n3));
            }
            n4 = stylesheet.getPreserveSpaceCount();
            for (n3 = 0; n3 < n4; ++n3) {
                vector.addElement(stylesheet.getPreserveSpace(n3));
            }
        }
    }

    void recomposeImports() {
        this.m_importNumber = this.getStylesheetRoot().getImportNumber(this);
        StylesheetRoot stylesheetRoot = this.getStylesheetRoot();
        int n2 = stylesheetRoot.getGlobalImportCount();
        this.m_importCountComposed = n2 - this.m_importNumber - 1;
        int n3 = this.getImportCount();
        if (n3 > 0) {
            this.m_endImportCountComposed += n3;
            while (n3 > 0) {
                this.m_endImportCountComposed += this.getImport(--n3).getEndImportCountComposed();
            }
        }
        n3 = this.getIncludeCountComposed();
        while (n3 > 0) {
            int n4 = this.getIncludeComposed(--n3).getImportCount();
            this.m_endImportCountComposed += n4;
            while (n4 > 0) {
                this.m_endImportCountComposed += this.getIncludeComposed(n3).getImport(--n4).getEndImportCountComposed();
            }
        }
    }

    public StylesheetComposed getImportComposed(int n2) throws ArrayIndexOutOfBoundsException {
        StylesheetRoot stylesheetRoot = this.getStylesheetRoot();
        return stylesheetRoot.getGlobalImport(1 + this.m_importNumber + n2);
    }

    public int getImportCountComposed() {
        return this.m_importCountComposed;
    }

    public int getEndImportCountComposed() {
        return this.m_endImportCountComposed;
    }

    void recomposeIncludes(Stylesheet stylesheet) {
        int n2 = stylesheet.getIncludeCount();
        if (n2 > 0) {
            if (null == this.m_includesComposed) {
                this.m_includesComposed = new Vector();
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                Stylesheet stylesheet2 = stylesheet.getInclude(i2);
                this.m_includesComposed.addElement(stylesheet2);
                this.recomposeIncludes(stylesheet2);
            }
        }
    }

    public Stylesheet getIncludeComposed(int n2) throws ArrayIndexOutOfBoundsException {
        if (-1 == n2) {
            return this;
        }
        if (null == this.m_includesComposed) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (Stylesheet)this.m_includesComposed.elementAt(n2);
    }

    public int getIncludeCountComposed() {
        return null != this.m_includesComposed ? this.m_includesComposed.size() : 0;
    }

    public void recomposeTemplates(boolean bl) throws TransformerException {
    }
}

