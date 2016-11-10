/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSGroupDecl;
import org.apache.xerces.impl.xs.XSNotationDecl;
import org.apache.xerces.impl.xs.util.XSGrammarPool;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.LSInputList;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.ls.LSInput;

public final class XSLoaderImpl
implements XSLoader,
DOMConfiguration {
    private final XSGrammarPool fGrammarPool = new XSGrammarMerger();
    private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader();

    public XSLoaderImpl() {
        this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
    }

    public DOMConfiguration getConfig() {
        return this;
    }

    public XSModel loadURIList(StringList stringList) {
        int n2 = stringList.getLength();
        try {
            this.fGrammarPool.clear();
            int n3 = 0;
            while (n3 < n2) {
                this.fSchemaLoader.loadGrammar(new XMLInputSource(null, stringList.item(n3), null));
                ++n3;
            }
            return this.fGrammarPool.toXSModel();
        }
        catch (Exception exception) {
            this.fSchemaLoader.reportDOMFatalError(exception);
            return null;
        }
    }

    public XSModel loadInputList(LSInputList lSInputList) {
        int n2 = lSInputList.getLength();
        try {
            this.fGrammarPool.clear();
            int n3 = 0;
            while (n3 < n2) {
                this.fSchemaLoader.loadGrammar(this.fSchemaLoader.dom2xmlInputSource(lSInputList.item(n3)));
                ++n3;
            }
            return this.fGrammarPool.toXSModel();
        }
        catch (Exception exception) {
            this.fSchemaLoader.reportDOMFatalError(exception);
            return null;
        }
    }

    public XSModel loadURI(String string) {
        try {
            this.fGrammarPool.clear();
            return ((XSGrammar)this.fSchemaLoader.loadGrammar(new XMLInputSource(null, string, null))).toXSModel();
        }
        catch (Exception exception) {
            this.fSchemaLoader.reportDOMFatalError(exception);
            return null;
        }
    }

    public XSModel load(LSInput lSInput) {
        try {
            this.fGrammarPool.clear();
            return ((XSGrammar)this.fSchemaLoader.loadGrammar(this.fSchemaLoader.dom2xmlInputSource(lSInput))).toXSModel();
        }
        catch (Exception exception) {
            this.fSchemaLoader.reportDOMFatalError(exception);
            return null;
        }
    }

    public void setParameter(String string, Object object) throws DOMException {
        this.fSchemaLoader.setParameter(string, object);
    }

    public Object getParameter(String string) throws DOMException {
        return this.fSchemaLoader.getParameter(string);
    }

    public boolean canSetParameter(String string, Object object) {
        return this.fSchemaLoader.canSetParameter(string, object);
    }

    public DOMStringList getParameterNames() {
        return this.fSchemaLoader.getParameterNames();
    }

    private static final class XSGrammarMerger
    extends XSGrammarPool {
        public void putGrammar(Grammar grammar) {
            SchemaGrammar schemaGrammar = this.toSchemaGrammar(super.getGrammar(grammar.getGrammarDescription()));
            if (schemaGrammar != null) {
                SchemaGrammar schemaGrammar2 = this.toSchemaGrammar(grammar);
                if (schemaGrammar2 != null) {
                    this.mergeSchemaGrammars(schemaGrammar, schemaGrammar2);
                }
            } else {
                super.putGrammar(grammar);
            }
        }

        private SchemaGrammar toSchemaGrammar(Grammar grammar) {
            return grammar instanceof SchemaGrammar ? (SchemaGrammar)grammar : null;
        }

        private void mergeSchemaGrammars(SchemaGrammar schemaGrammar, SchemaGrammar schemaGrammar2) {
            Object object;
            XSNamedMap xSNamedMap = schemaGrammar2.getComponents(2);
            int n2 = xSNamedMap.getLength();
            int n3 = 0;
            while (n3 < n2) {
                XSElementDecl xSElementDecl = (XSElementDecl)xSNamedMap.item(n3);
                if (schemaGrammar.getGlobalElementDecl(xSElementDecl.getName()) == null) {
                    schemaGrammar.addGlobalElementDecl(xSElementDecl);
                }
                ++n3;
            }
            xSNamedMap = schemaGrammar2.getComponents(1);
            n2 = xSNamedMap.getLength();
            int n4 = 0;
            while (n4 < n2) {
                XSAttributeDecl xSAttributeDecl = (XSAttributeDecl)xSNamedMap.item(n4);
                if (schemaGrammar.getGlobalAttributeDecl(xSAttributeDecl.getName()) == null) {
                    schemaGrammar.addGlobalAttributeDecl(xSAttributeDecl);
                }
                ++n4;
            }
            xSNamedMap = schemaGrammar2.getComponents(3);
            n2 = xSNamedMap.getLength();
            int n5 = 0;
            while (n5 < n2) {
                XSTypeDefinition xSTypeDefinition = (XSTypeDefinition)xSNamedMap.item(n5);
                if (schemaGrammar.getGlobalTypeDecl(xSTypeDefinition.getName()) == null) {
                    schemaGrammar.addGlobalTypeDecl(xSTypeDefinition);
                }
                ++n5;
            }
            xSNamedMap = schemaGrammar2.getComponents(5);
            n2 = xSNamedMap.getLength();
            int n6 = 0;
            while (n6 < n2) {
                XSAttributeGroupDecl xSAttributeGroupDecl = (XSAttributeGroupDecl)xSNamedMap.item(n6);
                if (schemaGrammar.getGlobalAttributeGroupDecl(xSAttributeGroupDecl.getName()) == null) {
                    schemaGrammar.addGlobalAttributeGroupDecl(xSAttributeGroupDecl);
                }
                ++n6;
            }
            xSNamedMap = schemaGrammar2.getComponents(7);
            n2 = xSNamedMap.getLength();
            int n7 = 0;
            while (n7 < n2) {
                XSGroupDecl xSGroupDecl = (XSGroupDecl)xSNamedMap.item(n7);
                if (schemaGrammar.getGlobalGroupDecl(xSGroupDecl.getName()) == null) {
                    schemaGrammar.addGlobalGroupDecl(xSGroupDecl);
                }
                ++n7;
            }
            xSNamedMap = schemaGrammar2.getComponents(11);
            n2 = xSNamedMap.getLength();
            int n8 = 0;
            while (n8 < n2) {
                object = (XSNotationDecl)xSNamedMap.item(n8);
                if (schemaGrammar.getGlobalNotationDecl(object.getName()) == null) {
                    schemaGrammar.addGlobalNotationDecl((XSNotationDecl)object);
                }
                ++n8;
            }
            object = schemaGrammar2.getAnnotations();
            n2 = object.getLength();
            int n9 = 0;
            while (n9 < n2) {
                schemaGrammar.addAnnotation((XSAnnotationImpl)object.item(n9));
                ++n9;
            }
        }

        public boolean containsGrammar(XMLGrammarDescription xMLGrammarDescription) {
            return false;
        }

        public Grammar getGrammar(XMLGrammarDescription xMLGrammarDescription) {
            return null;
        }

        public Grammar retrieveGrammar(XMLGrammarDescription xMLGrammarDescription) {
            return null;
        }

        public Grammar[] retrieveInitialGrammarSet(String string) {
            return new Grammar[0];
        }
    }

}

