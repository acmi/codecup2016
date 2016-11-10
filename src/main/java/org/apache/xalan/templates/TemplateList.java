/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.StylesheetComposed;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.TemplateSubPatternAssociation;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;
import org.apache.xpath.patterns.StepPattern;
import org.apache.xpath.patterns.UnionPattern;

public class TemplateList
implements Serializable {
    static final long serialVersionUID = 5803675288911728791L;
    static final boolean DEBUG = false;
    private Hashtable m_namedTemplates = new Hashtable(89);
    private Hashtable m_patternTable = new Hashtable(89);
    private TemplateSubPatternAssociation m_wildCardPatterns = null;
    private TemplateSubPatternAssociation m_textPatterns = null;
    private TemplateSubPatternAssociation m_docPatterns = null;
    private TemplateSubPatternAssociation m_commentPatterns = null;

    public void setTemplate(ElemTemplate elemTemplate) {
        XPath xPath = elemTemplate.getMatch();
        if (null == elemTemplate.getName() && null == xPath) {
            elemTemplate.error("ER_NEED_NAME_OR_MATCH_ATTRIB", new Object[]{"xsl:template"});
        }
        if (null != elemTemplate.getName()) {
            ElemTemplate expression = (ElemTemplate)this.m_namedTemplates.get(elemTemplate.getName());
            if (null == expression) {
                this.m_namedTemplates.put(elemTemplate.getName(), elemTemplate);
            } else {
                int unionPattern = expression.getStylesheetComposed().getImportCountComposed();
                int arrstepPattern = elemTemplate.getStylesheetComposed().getImportCountComposed();
                if (arrstepPattern > unionPattern) {
                    this.m_namedTemplates.put(elemTemplate.getName(), elemTemplate);
                } else if (arrstepPattern == unionPattern) {
                    elemTemplate.error("ER_DUPLICATE_NAMED_TEMPLATE", new Object[]{elemTemplate.getName()});
                }
            }
        }
        if (null != xPath) {
            Expression expression = xPath.getExpression();
            if (expression instanceof StepPattern) {
                this.insertPatternInTable((StepPattern)expression, elemTemplate);
            } else if (expression instanceof UnionPattern) {
                UnionPattern unionPattern = (UnionPattern)expression;
                StepPattern[] arrstepPattern = unionPattern.getPatterns();
                int n2 = arrstepPattern.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    this.insertPatternInTable(arrstepPattern[i2], elemTemplate);
                }
            }
        }
    }

    void dumpAssociationTables() {
        TemplateSubPatternAssociation templateSubPatternAssociation;
        Enumeration enumeration = this.m_patternTable.elements();
        while (enumeration.hasMoreElements()) {
            for (templateSubPatternAssociation = (TemplateSubPatternAssociation)enumeration.nextElement(); null != templateSubPatternAssociation; templateSubPatternAssociation = templateSubPatternAssociation.getNext()) {
                System.out.print("(" + templateSubPatternAssociation.getTargetString() + ", " + templateSubPatternAssociation.getPattern() + ")");
            }
            System.out.println("\n.....");
        }
        System.out.print("wild card list: ");
        for (templateSubPatternAssociation = this.m_wildCardPatterns; null != templateSubPatternAssociation; templateSubPatternAssociation = templateSubPatternAssociation.getNext()) {
            System.out.print("(" + templateSubPatternAssociation.getTargetString() + ", " + templateSubPatternAssociation.getPattern() + ")");
        }
        System.out.println("\n.....");
    }

    public void compose(StylesheetRoot stylesheetRoot) {
        if (null != this.m_wildCardPatterns) {
            Enumeration enumeration = this.m_patternTable.elements();
            while (enumeration.hasMoreElements()) {
                TemplateSubPatternAssociation templateSubPatternAssociation = (TemplateSubPatternAssociation)enumeration.nextElement();
                for (TemplateSubPatternAssociation templateSubPatternAssociation2 = this.m_wildCardPatterns; null != templateSubPatternAssociation2; templateSubPatternAssociation2 = templateSubPatternAssociation2.getNext()) {
                    try {
                        templateSubPatternAssociation = this.insertAssociationIntoList(templateSubPatternAssociation, (TemplateSubPatternAssociation)templateSubPatternAssociation2.clone(), true);
                        continue;
                    }
                    catch (CloneNotSupportedException cloneNotSupportedException) {
                        // empty catch block
                    }
                }
            }
        }
    }

    private TemplateSubPatternAssociation insertAssociationIntoList(TemplateSubPatternAssociation templateSubPatternAssociation, TemplateSubPatternAssociation templateSubPatternAssociation2, boolean bl) {
        TemplateSubPatternAssociation templateSubPatternAssociation3;
        boolean bl2;
        double d2;
        double d3 = this.getPriorityOrScore(templateSubPatternAssociation2);
        int n2 = templateSubPatternAssociation2.getImportLevel();
        int n3 = templateSubPatternAssociation2.getDocOrderPos();
        TemplateSubPatternAssociation templateSubPatternAssociation4 = templateSubPatternAssociation;
        while (null != (templateSubPatternAssociation3 = templateSubPatternAssociation4.getNext())) {
            d2 = this.getPriorityOrScore(templateSubPatternAssociation3);
            if (n2 > templateSubPatternAssociation3.getImportLevel()) break;
            if (n2 < templateSubPatternAssociation3.getImportLevel()) {
                templateSubPatternAssociation4 = templateSubPatternAssociation3;
                continue;
            }
            if (d3 > d2) break;
            if (d3 < d2) {
                templateSubPatternAssociation4 = templateSubPatternAssociation3;
                continue;
            }
            if (n3 >= templateSubPatternAssociation3.getDocOrderPos()) break;
            templateSubPatternAssociation4 = templateSubPatternAssociation3;
        }
        if (null == templateSubPatternAssociation3 || templateSubPatternAssociation4 == templateSubPatternAssociation) {
            d2 = this.getPriorityOrScore(templateSubPatternAssociation4);
            bl2 = n2 > templateSubPatternAssociation4.getImportLevel() ? true : (n2 < templateSubPatternAssociation4.getImportLevel() ? false : (d3 > d2 ? true : (d3 < d2 ? false : n3 >= templateSubPatternAssociation4.getDocOrderPos())));
        } else {
            bl2 = false;
        }
        if (bl) {
            if (bl2) {
                templateSubPatternAssociation2.setNext(templateSubPatternAssociation4);
                String string = templateSubPatternAssociation4.getTargetString();
                templateSubPatternAssociation2.setTargetString(string);
                this.putHead(string, templateSubPatternAssociation2);
                return templateSubPatternAssociation2;
            }
            templateSubPatternAssociation2.setNext(templateSubPatternAssociation3);
            templateSubPatternAssociation4.setNext(templateSubPatternAssociation2);
            return templateSubPatternAssociation;
        }
        if (bl2) {
            templateSubPatternAssociation2.setNext(templateSubPatternAssociation4);
            if (templateSubPatternAssociation4.isWild() || templateSubPatternAssociation2.isWild()) {
                this.m_wildCardPatterns = templateSubPatternAssociation2;
            } else {
                this.putHead(templateSubPatternAssociation2.getTargetString(), templateSubPatternAssociation2);
            }
            return templateSubPatternAssociation2;
        }
        templateSubPatternAssociation2.setNext(templateSubPatternAssociation3);
        templateSubPatternAssociation4.setNext(templateSubPatternAssociation2);
        return templateSubPatternAssociation;
    }

    private void insertPatternInTable(StepPattern stepPattern, ElemTemplate elemTemplate) {
        String string = stepPattern.getTargetString();
        if (null != string) {
            TemplateSubPatternAssociation templateSubPatternAssociation;
            String string2 = elemTemplate.getMatch().getPatternString();
            TemplateSubPatternAssociation templateSubPatternAssociation2 = new TemplateSubPatternAssociation(elemTemplate, stepPattern, string2);
            boolean bl = templateSubPatternAssociation2.isWild();
            TemplateSubPatternAssociation templateSubPatternAssociation3 = templateSubPatternAssociation = bl ? this.m_wildCardPatterns : this.getHead(string);
            if (null == templateSubPatternAssociation) {
                if (bl) {
                    this.m_wildCardPatterns = templateSubPatternAssociation2;
                } else {
                    this.putHead(string, templateSubPatternAssociation2);
                }
            } else {
                this.insertAssociationIntoList(templateSubPatternAssociation, templateSubPatternAssociation2, false);
            }
        }
    }

    private double getPriorityOrScore(TemplateSubPatternAssociation templateSubPatternAssociation) {
        StepPattern stepPattern;
        double d2 = templateSubPatternAssociation.getTemplate().getPriority();
        if (d2 == Double.NEGATIVE_INFINITY && (stepPattern = templateSubPatternAssociation.getStepPattern()) instanceof NodeTest) {
            return ((NodeTest)stepPattern).getDefaultScore();
        }
        return d2;
    }

    public ElemTemplate getTemplate(QName qName) {
        return (ElemTemplate)this.m_namedTemplates.get(qName);
    }

    public TemplateSubPatternAssociation getHead(XPathContext xPathContext, int n2, DTM dTM) {
        TemplateSubPatternAssociation templateSubPatternAssociation;
        short s2 = dTM.getNodeType(n2);
        switch (s2) {
            case 1: 
            case 2: {
                templateSubPatternAssociation = (TemplateSubPatternAssociation)this.m_patternTable.get(dTM.getLocalName(n2));
                break;
            }
            case 3: 
            case 4: {
                templateSubPatternAssociation = this.m_textPatterns;
                break;
            }
            case 5: 
            case 6: {
                templateSubPatternAssociation = (TemplateSubPatternAssociation)this.m_patternTable.get(dTM.getNodeName(n2));
                break;
            }
            case 7: {
                templateSubPatternAssociation = (TemplateSubPatternAssociation)this.m_patternTable.get(dTM.getLocalName(n2));
                break;
            }
            case 8: {
                templateSubPatternAssociation = this.m_commentPatterns;
                break;
            }
            case 9: 
            case 11: {
                templateSubPatternAssociation = this.m_docPatterns;
                break;
            }
            default: {
                templateSubPatternAssociation = (TemplateSubPatternAssociation)this.m_patternTable.get(dTM.getNodeName(n2));
            }
        }
        return null == templateSubPatternAssociation ? this.m_wildCardPatterns : templateSubPatternAssociation;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public ElemTemplate getTemplateFast(XPathContext var1_1, int var2_2, int var3_3, QName var4_4, int var5_5, boolean var6_6, DTM var7_7) throws TransformerException {
        switch (var7_7.getNodeType(var2_2)) {
            case 1: 
            case 2: {
                var8_8 = (TemplateSubPatternAssociation)this.m_patternTable.get(var7_7.getLocalNameFromExpandedNameID(var3_3));
                ** break;
            }
            case 3: 
            case 4: {
                var8_8 = this.m_textPatterns;
                ** break;
            }
            case 5: 
            case 6: {
                var8_8 = (TemplateSubPatternAssociation)this.m_patternTable.get(var7_7.getNodeName(var2_2));
                ** break;
            }
            case 7: {
                var8_8 = (TemplateSubPatternAssociation)this.m_patternTable.get(var7_7.getLocalName(var2_2));
                ** break;
            }
            case 8: {
                var8_8 = this.m_commentPatterns;
                ** break;
            }
            case 9: 
            case 11: {
                var8_8 = this.m_docPatterns;
                ** break;
            }
        }
        var8_8 = (TemplateSubPatternAssociation)this.m_patternTable.get(var7_7.getNodeName(var2_2));
lbl21: // 7 sources:
        if (null == var8_8 && null == (var8_8 = this.m_wildCardPatterns)) {
            return null;
        }
        var1_1.pushNamespaceContextNull();
        try {
            do {
                if (var5_5 > -1 && var8_8.getImportLevel() > var5_5) continue;
                var9_9 = var8_8.getTemplate();
                var1_1.setNamespaceContext(var9_9);
                if (var8_8.m_stepPattern.execute(var1_1, var2_2, var7_7, var3_3) == NodeTest.SCORE_NONE || !var8_8.matchMode(var4_4)) continue;
                if (var6_6) {
                    this.checkConflicts(var8_8, var1_1, var2_2, var4_4);
                }
                var10_10 = var9_9;
                return var10_10;
            } while (null != (var8_8 = var8_8.getNext()));
            return null;
        }
        finally {
            var1_1.popNamespaceContext();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ElemTemplate getTemplate(XPathContext xPathContext, int n2, QName qName, boolean bl, DTM dTM) throws TransformerException {
        TemplateSubPatternAssociation templateSubPatternAssociation = this.getHead(xPathContext, n2, dTM);
        if (null == templateSubPatternAssociation) return null;
        xPathContext.pushNamespaceContextNull();
        xPathContext.pushCurrentNodeAndExpression(n2, n2);
        try {
            do {
                ElemTemplate elemTemplate = templateSubPatternAssociation.getTemplate();
                xPathContext.setNamespaceContext(elemTemplate);
                if (templateSubPatternAssociation.m_stepPattern.execute(xPathContext, n2) == NodeTest.SCORE_NONE || !templateSubPatternAssociation.matchMode(qName)) continue;
                if (bl) {
                    this.checkConflicts(templateSubPatternAssociation, xPathContext, n2, qName);
                }
                ElemTemplate elemTemplate2 = elemTemplate;
                return elemTemplate2;
            } while (null != (templateSubPatternAssociation = templateSubPatternAssociation.getNext()));
            return null;
        }
        finally {
            xPathContext.popCurrentNodeAndExpression();
            xPathContext.popNamespaceContext();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ElemTemplate getTemplate(XPathContext xPathContext, int n2, QName qName, int n3, int n4, boolean bl, DTM dTM) throws TransformerException {
        TemplateSubPatternAssociation templateSubPatternAssociation = this.getHead(xPathContext, n2, dTM);
        if (null == templateSubPatternAssociation) return null;
        xPathContext.pushNamespaceContextNull();
        xPathContext.pushCurrentNodeAndExpression(n2, n2);
        try {
            do {
                ElemTemplate elemTemplate;
                if (n3 > -1 && templateSubPatternAssociation.getImportLevel() > n3) continue;
                if (templateSubPatternAssociation.getImportLevel() <= n3 - n4) {
                    elemTemplate = null;
                    return elemTemplate;
                }
                elemTemplate = templateSubPatternAssociation.getTemplate();
                xPathContext.setNamespaceContext(elemTemplate);
                if (templateSubPatternAssociation.m_stepPattern.execute(xPathContext, n2) == NodeTest.SCORE_NONE || !templateSubPatternAssociation.matchMode(qName)) continue;
                if (bl) {
                    this.checkConflicts(templateSubPatternAssociation, xPathContext, n2, qName);
                }
                ElemTemplate elemTemplate2 = elemTemplate;
                return elemTemplate2;
            } while (null != (templateSubPatternAssociation = templateSubPatternAssociation.getNext()));
            return null;
        }
        finally {
            xPathContext.popCurrentNodeAndExpression();
            xPathContext.popNamespaceContext();
        }
    }

    public TemplateWalker getWalker() {
        return new TemplateWalker(this, null);
    }

    private void checkConflicts(TemplateSubPatternAssociation templateSubPatternAssociation, XPathContext xPathContext, int n2, QName qName) {
    }

    private void addObjectIfNotFound(Object object, Vector vector) {
        int n2 = vector.size();
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (vector.elementAt(i2) != object) continue;
            bl = false;
            break;
        }
        if (bl) {
            vector.addElement(object);
        }
    }

    private Hashtable getNamedTemplates() {
        return this.m_namedTemplates;
    }

    private void setNamedTemplates(Hashtable hashtable) {
        this.m_namedTemplates = hashtable;
    }

    private TemplateSubPatternAssociation getHead(String string) {
        return (TemplateSubPatternAssociation)this.m_patternTable.get(string);
    }

    private void putHead(String string, TemplateSubPatternAssociation templateSubPatternAssociation) {
        if (string.equals("#text")) {
            this.m_textPatterns = templateSubPatternAssociation;
        } else if (string.equals("/")) {
            this.m_docPatterns = templateSubPatternAssociation;
        } else if (string.equals("#comment")) {
            this.m_commentPatterns = templateSubPatternAssociation;
        }
        this.m_patternTable.put(string, templateSubPatternAssociation);
    }

    static Hashtable access$100(TemplateList templateList) {
        return templateList.m_patternTable;
    }

    static Hashtable access$200(TemplateList templateList) {
        return templateList.m_namedTemplates;
    }

    static class 1 {
    }

    public class TemplateWalker {
        private Enumeration hashIterator;
        private boolean inPatterns;
        private TemplateSubPatternAssociation curPattern;
        private Hashtable m_compilerCache;
        private final TemplateList this$0;

        private TemplateWalker(TemplateList templateList) {
            this.this$0 = templateList;
            this.m_compilerCache = new Hashtable();
            this.hashIterator = TemplateList.access$100(templateList).elements();
            this.inPatterns = true;
            this.curPattern = null;
        }

        public ElemTemplate next() {
            ElemTemplate elemTemplate;
            ElemTemplateElement elemTemplateElement = null;
            do {
                if (this.inPatterns) {
                    if (null != this.curPattern) {
                        this.curPattern = this.curPattern.getNext();
                    }
                    if (null != this.curPattern) {
                        elemTemplateElement = this.curPattern.getTemplate();
                    } else if (this.hashIterator.hasMoreElements()) {
                        this.curPattern = (TemplateSubPatternAssociation)this.hashIterator.nextElement();
                        elemTemplateElement = this.curPattern.getTemplate();
                    } else {
                        this.inPatterns = false;
                        this.hashIterator = TemplateList.access$200(this.this$0).elements();
                    }
                }
                if (this.inPatterns) continue;
                if (this.hashIterator.hasMoreElements()) {
                    elemTemplateElement = (ElemTemplate)this.hashIterator.nextElement();
                    continue;
                }
                return null;
            } while (null != (elemTemplate = (ElemTemplate)this.m_compilerCache.get(new Integer(elemTemplateElement.getUid()))));
            this.m_compilerCache.put(new Integer(elemTemplateElement.getUid()), elemTemplateElement);
            return elemTemplateElement;
        }

        TemplateWalker(TemplateList templateList, 1 var2_2) {
            this(templateList);
        }
    }

}

