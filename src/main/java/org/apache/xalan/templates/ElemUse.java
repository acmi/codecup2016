/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.util.ArrayList;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.templates.ElemAttributeSet;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;

public class ElemUse
extends ElemTemplateElement {
    static final long serialVersionUID = 5830057200289299736L;
    private QName[] m_attributeSetsNames = null;

    public void setUseAttributeSets(Vector vector) {
        int n2 = vector.size();
        this.m_attributeSetsNames = new QName[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.m_attributeSetsNames[i2] = (QName)vector.elementAt(i2);
        }
    }

    public void setUseAttributeSets(QName[] arrqName) {
        this.m_attributeSetsNames = arrqName;
    }

    public QName[] getUseAttributeSets() {
        return this.m_attributeSetsNames;
    }

    public void applyAttrSets(TransformerImpl transformerImpl, StylesheetRoot stylesheetRoot) throws TransformerException {
        this.applyAttrSets(transformerImpl, stylesheetRoot, this.m_attributeSetsNames);
    }

    private void applyAttrSets(TransformerImpl transformerImpl, StylesheetRoot stylesheetRoot, QName[] arrqName) throws TransformerException {
        if (null != arrqName) {
            int n2 = arrqName.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                QName qName = arrqName[i2];
                ArrayList arrayList = stylesheetRoot.getAttributeSetComposed(qName);
                if (null != arrayList) {
                    int n3 = arrayList.size();
                    for (int i3 = n3 - 1; i3 >= 0; --i3) {
                        ElemAttributeSet elemAttributeSet = (ElemAttributeSet)arrayList.get(i3);
                        elemAttributeSet.execute(transformerImpl);
                    }
                    continue;
                }
                throw new TransformerException(XSLMessages.createMessage("ER_NO_ATTRIB_SET", new Object[]{qName}), this);
            }
        }
    }

    public void execute(TransformerImpl transformerImpl) throws TransformerException {
        if (null != this.m_attributeSetsNames) {
            this.applyAttrSets(transformerImpl, this.getStylesheetRoot(), this.m_attributeSetsNames);
        }
    }
}

