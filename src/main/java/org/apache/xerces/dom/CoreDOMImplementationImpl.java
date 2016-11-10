/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.lang.ref.SoftReference;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DOMOutputImpl;
import org.apache.xerces.dom.DocumentTypeImpl;
import org.apache.xerces.dom.ObjectFactory;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.parsers.DOMParserImpl;
import org.apache.xerces.util.XMLChar;
import org.apache.xml.serialize.DOMSerializerImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

public class CoreDOMImplementationImpl
implements DOMImplementation,
DOMImplementationLS {
    private static final int SIZE = 2;
    private SoftReference[] schemaValidators = new SoftReference[2];
    private SoftReference[] xml10DTDValidators = new SoftReference[2];
    private SoftReference[] xml11DTDValidators = new SoftReference[2];
    private int freeSchemaValidatorIndex = -1;
    private int freeXML10DTDValidatorIndex = -1;
    private int freeXML11DTDValidatorIndex = -1;
    private int schemaValidatorsCurrentSize = 2;
    private int xml10DTDValidatorsCurrentSize = 2;
    private int xml11DTDValidatorsCurrentSize = 2;
    private SoftReference[] xml10DTDLoaders = new SoftReference[2];
    private SoftReference[] xml11DTDLoaders = new SoftReference[2];
    private int freeXML10DTDLoaderIndex = -1;
    private int freeXML11DTDLoaderIndex = -1;
    private int xml10DTDLoaderCurrentSize = 2;
    private int xml11DTDLoaderCurrentSize = 2;
    private int docAndDoctypeCounter = 0;
    static final CoreDOMImplementationImpl singleton = new CoreDOMImplementationImpl();

    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }

    public boolean hasFeature(String string, String string2) {
        boolean bl;
        boolean bl2 = bl = string2 == null || string2.length() == 0;
        if (string.equalsIgnoreCase("+XPath") && (bl || string2.equals("3.0"))) {
            try {
                Class class_ = ObjectFactory.findProviderClass("org.apache.xpath.domapi.XPathEvaluatorImpl", ObjectFactory.findClassLoader(), true);
                Class<?>[] arrclass = class_.getInterfaces();
                int n2 = 0;
                while (n2 < arrclass.length) {
                    if (arrclass[n2].getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
                        return true;
                    }
                    ++n2;
                }
            }
            catch (Exception exception) {
                return false;
            }
            return true;
        }
        if (string.startsWith("+")) {
            string = string.substring(1);
        }
        return string.equalsIgnoreCase("Core") && (bl || string2.equals("1.0") || string2.equals("2.0") || string2.equals("3.0")) || string.equalsIgnoreCase("XML") && (bl || string2.equals("1.0") || string2.equals("2.0") || string2.equals("3.0")) || string.equalsIgnoreCase("XMLVersion") && (bl || string2.equals("1.0") || string2.equals("1.1")) || string.equalsIgnoreCase("LS") && (bl || string2.equals("3.0")) || string.equalsIgnoreCase("ElementTraversal") && (bl || string2.equals("1.0"));
    }

    public DocumentType createDocumentType(String string, String string2, String string3) {
        this.checkQName(string);
        return new DocumentTypeImpl(null, string, string2, string3);
    }

    final void checkQName(String string) {
        int n2;
        int n3 = string.indexOf(58);
        int n4 = string.lastIndexOf(58);
        int n5 = string.length();
        if (n3 == 0 || n3 == n5 - 1 || n4 != n3) {
            String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
            throw new DOMException(14, string2);
        }
        int n6 = 0;
        if (n3 > 0) {
            if (!XMLChar.isNCNameStart(string.charAt(n6))) {
                String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
                throw new DOMException(5, string3);
            }
            n2 = 1;
            while (n2 < n3) {
                if (!XMLChar.isNCName(string.charAt(n2))) {
                    String string4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
                    throw new DOMException(5, string4);
                }
                ++n2;
            }
            n6 = n3 + 1;
        }
        if (!XMLChar.isNCNameStart(string.charAt(n6))) {
            String string5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
            throw new DOMException(5, string5);
        }
        n2 = n6 + 1;
        while (n2 < n5) {
            if (!XMLChar.isNCName(string.charAt(n2))) {
                String string6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
                throw new DOMException(5, string6);
            }
            ++n2;
        }
    }

    public Document createDocument(String string, String string2, DocumentType documentType) throws DOMException {
        if (documentType != null && documentType.getOwnerDocument() != null) {
            String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
            throw new DOMException(4, string3);
        }
        CoreDocumentImpl coreDocumentImpl = this.createDocument(documentType);
        if (string2 != null || string != null) {
            Element element = coreDocumentImpl.createElementNS(string, string2);
            coreDocumentImpl.appendChild(element);
        }
        return coreDocumentImpl;
    }

    protected CoreDocumentImpl createDocument(DocumentType documentType) {
        return new CoreDocumentImpl(documentType);
    }

    public Object getFeature(String string, String string2) {
        if (singleton.hasFeature(string, string2)) {
            if (string.equalsIgnoreCase("+XPath")) {
                try {
                    Class class_ = ObjectFactory.findProviderClass("org.apache.xpath.domapi.XPathEvaluatorImpl", ObjectFactory.findClassLoader(), true);
                    Class<?>[] arrclass = class_.getInterfaces();
                    int n2 = 0;
                    while (n2 < arrclass.length) {
                        if (arrclass[n2].getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
                            return class_.newInstance();
                        }
                        ++n2;
                    }
                }
                catch (Exception exception) {
                    return null;
                }
            } else {
                return singleton;
            }
        }
        return null;
    }

    public LSParser createLSParser(short s2, String string) throws DOMException {
        if (s2 != 1 || string != null && !"http://www.w3.org/2001/XMLSchema".equals(string) && !"http://www.w3.org/TR/REC-xml".equals(string)) {
            String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
            throw new DOMException(9, string2);
        }
        if (string != null && string.equals("http://www.w3.org/TR/REC-xml")) {
            return new DOMParserImpl("org.apache.xerces.parsers.DTDConfiguration", string);
        }
        return new DOMParserImpl("org.apache.xerces.parsers.XIncludeAwareParserConfiguration", string);
    }

    public LSSerializer createLSSerializer() {
        try {
            Class class_ = ObjectFactory.findProviderClass("org.apache.xml.serializer.dom3.LSSerializerImpl", ObjectFactory.findClassLoader(), true);
            return (LSSerializer)class_.newInstance();
        }
        catch (Exception exception) {
            return new DOMSerializerImpl();
        }
    }

    public LSInput createLSInput() {
        return new DOMInputImpl();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    synchronized RevalidationHandler getValidator(String var1_1, String var2_2) {
        if (var1_1 == "http://www.w3.org/2001/XMLSchema") ** GOTO lbl13
        if (var1_1 != "http://www.w3.org/TR/REC-xml") return null;
        if (!"1.1".equals(var2_2)) ** GOTO lbl33
        ** GOTO lbl23
lbl-1000: // 1 sources:
        {
            var3_3 = this.schemaValidators[this.freeSchemaValidatorIndex];
            var4_6 = (RevalidationHandlerHolder)var3_3.get();
            if (var4_6 != null && var4_6.handler != null) {
                var5_9 = var4_6.handler;
                var4_6.handler = null;
                --this.freeSchemaValidatorIndex;
                return var5_9;
            }
            this.schemaValidators[this.freeSchemaValidatorIndex--] = null;
lbl13: // 2 sources:
            ** while (this.freeSchemaValidatorIndex >= 0)
        }
lbl14: // 1 sources:
        return (RevalidationHandler)ObjectFactory.newInstance("org.apache.xerces.impl.xs.XMLSchemaValidator", ObjectFactory.findClassLoader(), true);
lbl-1000: // 1 sources:
        {
            var3_4 = this.xml11DTDValidators[this.freeXML11DTDValidatorIndex];
            var4_7 = (RevalidationHandlerHolder)var3_4.get();
            if (var4_7 != null && var4_7.handler != null) {
                var5_10 = var4_7.handler;
                var4_7.handler = null;
                --this.freeXML11DTDValidatorIndex;
                return var5_10;
            }
            this.xml11DTDValidators[this.freeXML11DTDValidatorIndex--] = null;
lbl23: // 2 sources:
            ** while (this.freeXML11DTDValidatorIndex >= 0)
        }
lbl24: // 1 sources:
        return (RevalidationHandler)ObjectFactory.newInstance("org.apache.xerces.impl.dtd.XML11DTDValidator", ObjectFactory.findClassLoader(), true);
lbl-1000: // 1 sources:
        {
            var3_5 = this.xml10DTDValidators[this.freeXML10DTDValidatorIndex];
            var4_8 = (RevalidationHandlerHolder)var3_5.get();
            if (var4_8 != null && var4_8.handler != null) {
                var5_11 = var4_8.handler;
                var4_8.handler = null;
                --this.freeXML10DTDValidatorIndex;
                return var5_11;
            }
            this.xml10DTDValidators[this.freeXML10DTDValidatorIndex--] = null;
lbl33: // 2 sources:
            ** while (this.freeXML10DTDValidatorIndex >= 0)
        }
lbl34: // 1 sources:
        return (RevalidationHandler)ObjectFactory.newInstance("org.apache.xerces.impl.dtd.XMLDTDValidator", ObjectFactory.findClassLoader(), true);
    }

    synchronized void releaseValidator(String string, String string2, RevalidationHandler revalidationHandler) {
        if (string == "http://www.w3.org/2001/XMLSchema") {
            SoftReference[] arrsoftReference;
            RevalidationHandlerHolder revalidationHandlerHolder;
            ++this.freeSchemaValidatorIndex;
            if (this.schemaValidators.length == this.freeSchemaValidatorIndex) {
                this.schemaValidatorsCurrentSize += 2;
                arrsoftReference = new SoftReference[this.schemaValidatorsCurrentSize];
                System.arraycopy(this.schemaValidators, 0, arrsoftReference, 0, this.schemaValidators.length);
                this.schemaValidators = arrsoftReference;
            }
            if ((arrsoftReference = this.schemaValidators[this.freeSchemaValidatorIndex]) != null && (revalidationHandlerHolder = (RevalidationHandlerHolder)arrsoftReference.get()) != null) {
                revalidationHandlerHolder.handler = revalidationHandler;
                return;
            }
            this.schemaValidators[this.freeSchemaValidatorIndex] = new SoftReference<RevalidationHandlerHolder>(new RevalidationHandlerHolder(revalidationHandler));
        } else if (string == "http://www.w3.org/TR/REC-xml") {
            if ("1.1".equals(string2)) {
                SoftReference[] arrsoftReference;
                RevalidationHandlerHolder revalidationHandlerHolder;
                ++this.freeXML11DTDValidatorIndex;
                if (this.xml11DTDValidators.length == this.freeXML11DTDValidatorIndex) {
                    this.xml11DTDValidatorsCurrentSize += 2;
                    arrsoftReference = new SoftReference[this.xml11DTDValidatorsCurrentSize];
                    System.arraycopy(this.xml11DTDValidators, 0, arrsoftReference, 0, this.xml11DTDValidators.length);
                    this.xml11DTDValidators = arrsoftReference;
                }
                if ((arrsoftReference = this.xml11DTDValidators[this.freeXML11DTDValidatorIndex]) != null && (revalidationHandlerHolder = (RevalidationHandlerHolder)arrsoftReference.get()) != null) {
                    revalidationHandlerHolder.handler = revalidationHandler;
                    return;
                }
                this.xml11DTDValidators[this.freeXML11DTDValidatorIndex] = new SoftReference<RevalidationHandlerHolder>(new RevalidationHandlerHolder(revalidationHandler));
            } else {
                SoftReference[] arrsoftReference;
                RevalidationHandlerHolder revalidationHandlerHolder;
                ++this.freeXML10DTDValidatorIndex;
                if (this.xml10DTDValidators.length == this.freeXML10DTDValidatorIndex) {
                    this.xml10DTDValidatorsCurrentSize += 2;
                    arrsoftReference = new SoftReference[this.xml10DTDValidatorsCurrentSize];
                    System.arraycopy(this.xml10DTDValidators, 0, arrsoftReference, 0, this.xml10DTDValidators.length);
                    this.xml10DTDValidators = arrsoftReference;
                }
                if ((arrsoftReference = this.xml10DTDValidators[this.freeXML10DTDValidatorIndex]) != null && (revalidationHandlerHolder = (RevalidationHandlerHolder)arrsoftReference.get()) != null) {
                    revalidationHandlerHolder.handler = revalidationHandler;
                    return;
                }
                this.xml10DTDValidators[this.freeXML10DTDValidatorIndex] = new SoftReference<RevalidationHandlerHolder>(new RevalidationHandlerHolder(revalidationHandler));
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    final synchronized XMLDTDLoader getDTDLoader(String var1_1) {
        if (!"1.1".equals(var1_1)) ** GOTO lbl21
        while (this.freeXML11DTDLoaderIndex >= 0) {
            var2_2 = this.xml11DTDLoaders[this.freeXML11DTDLoaderIndex];
            var3_4 = (XMLDTDLoaderHolder)var2_2.get();
            if (var3_4 != null && var3_4.loader != null) {
                var4_6 = var3_4.loader;
                var3_4.loader = null;
                --this.freeXML11DTDLoaderIndex;
                return var4_6;
            }
            this.xml11DTDLoaders[this.freeXML11DTDLoaderIndex--] = null;
        }
        return (XMLDTDLoader)ObjectFactory.newInstance("org.apache.xerces.impl.dtd.XML11DTDProcessor", ObjectFactory.findClassLoader(), true);
lbl-1000: // 1 sources:
        {
            var2_3 = this.xml10DTDLoaders[this.freeXML10DTDLoaderIndex];
            var3_5 = (XMLDTDLoaderHolder)var2_3.get();
            if (var3_5 != null && var3_5.loader != null) {
                var4_7 = var3_5.loader;
                var3_5.loader = null;
                --this.freeXML10DTDLoaderIndex;
                return var4_7;
            }
            this.xml10DTDLoaders[this.freeXML10DTDLoaderIndex--] = null;
lbl21: // 2 sources:
            ** while (this.freeXML10DTDLoaderIndex >= 0)
        }
lbl22: // 1 sources:
        return new XMLDTDLoader();
    }

    final synchronized void releaseDTDLoader(String string, XMLDTDLoader xMLDTDLoader) {
        if ("1.1".equals(string)) {
            SoftReference[] arrsoftReference;
            XMLDTDLoaderHolder xMLDTDLoaderHolder;
            ++this.freeXML11DTDLoaderIndex;
            if (this.xml11DTDLoaders.length == this.freeXML11DTDLoaderIndex) {
                this.xml11DTDLoaderCurrentSize += 2;
                arrsoftReference = new SoftReference[this.xml11DTDLoaderCurrentSize];
                System.arraycopy(this.xml11DTDLoaders, 0, arrsoftReference, 0, this.xml11DTDLoaders.length);
                this.xml11DTDLoaders = arrsoftReference;
            }
            if ((arrsoftReference = this.xml11DTDLoaders[this.freeXML11DTDLoaderIndex]) != null && (xMLDTDLoaderHolder = (XMLDTDLoaderHolder)arrsoftReference.get()) != null) {
                xMLDTDLoaderHolder.loader = xMLDTDLoader;
                return;
            }
            this.xml11DTDLoaders[this.freeXML11DTDLoaderIndex] = new SoftReference<XMLDTDLoaderHolder>(new XMLDTDLoaderHolder(xMLDTDLoader));
        } else {
            SoftReference[] arrsoftReference;
            XMLDTDLoaderHolder xMLDTDLoaderHolder;
            ++this.freeXML10DTDLoaderIndex;
            if (this.xml10DTDLoaders.length == this.freeXML10DTDLoaderIndex) {
                this.xml10DTDLoaderCurrentSize += 2;
                arrsoftReference = new SoftReference[this.xml10DTDLoaderCurrentSize];
                System.arraycopy(this.xml10DTDLoaders, 0, arrsoftReference, 0, this.xml10DTDLoaders.length);
                this.xml10DTDLoaders = arrsoftReference;
            }
            if ((arrsoftReference = this.xml10DTDLoaders[this.freeXML10DTDLoaderIndex]) != null && (xMLDTDLoaderHolder = (XMLDTDLoaderHolder)arrsoftReference.get()) != null) {
                xMLDTDLoaderHolder.loader = xMLDTDLoader;
                return;
            }
            this.xml10DTDLoaders[this.freeXML10DTDLoaderIndex] = new SoftReference<XMLDTDLoaderHolder>(new XMLDTDLoaderHolder(xMLDTDLoader));
        }
    }

    protected synchronized int assignDocumentNumber() {
        return ++this.docAndDoctypeCounter;
    }

    protected synchronized int assignDocTypeNumber() {
        return ++this.docAndDoctypeCounter;
    }

    public LSOutput createLSOutput() {
        return new DOMOutputImpl();
    }

    static final class XMLDTDLoaderHolder {
        XMLDTDLoader loader;

        XMLDTDLoaderHolder(XMLDTDLoader xMLDTDLoader) {
            this.loader = xMLDTDLoader;
        }
    }

    static final class RevalidationHandlerHolder {
        RevalidationHandler handler;

        RevalidationHandlerHolder(RevalidationHandler revalidationHandler) {
            this.handler = revalidationHandler;
        }
    }

}

