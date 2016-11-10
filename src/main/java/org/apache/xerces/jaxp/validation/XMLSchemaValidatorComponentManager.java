/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.jaxp.validation.DraconianErrorHandler;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.util.DOMEntityResolverWrapper;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.apache.xerces.util.SecurityManager;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;

final class XMLSchemaValidatorComponentManager
extends ParserConfigurationSettings
implements XMLComponentManager {
    private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    private static final String VALIDATION = "http://xml.org/sax/features/validation";
    private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
    private static final String IGNORE_XSI_TYPE = "http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl";
    private static final String ID_IDREF_CHECKING = "http://apache.org/xml/features/validation/id-idref-checking";
    private static final String UNPARSED_ENTITY_CHECKING = "http://apache.org/xml/features/validation/unparsed-entity-checking";
    private static final String IDENTITY_CONSTRAINT_CHECKING = "http://apache.org/xml/features/validation/identity-constraint-checking";
    private static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
    private static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
    private static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
    private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    private static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    private static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private static final String LOCALE = "http://apache.org/xml/properties/locale";
    private boolean fConfigUpdated = true;
    private boolean fUseGrammarPoolOnly;
    private final HashMap fComponents = new HashMap();
    private final XMLEntityManager fEntityManager = new XMLEntityManager();
    private final XMLErrorReporter fErrorReporter;
    private final NamespaceContext fNamespaceContext;
    private final XMLSchemaValidator fSchemaValidator;
    private final ValidationManager fValidationManager;
    private final HashMap fInitFeatures = new HashMap();
    private final HashMap fInitProperties = new HashMap();
    private final SecurityManager fInitSecurityManager;
    private ErrorHandler fErrorHandler = null;
    private LSResourceResolver fResourceResolver = null;
    private Locale fLocale = null;

    public XMLSchemaValidatorComponentManager(XSGrammarPoolContainer xSGrammarPoolContainer) {
        this.fComponents.put("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
        this.fErrorReporter = new XMLErrorReporter();
        this.fComponents.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        this.fNamespaceContext = new NamespaceSupport();
        this.fComponents.put("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
        this.fSchemaValidator = new XMLSchemaValidator();
        this.fComponents.put("http://apache.org/xml/properties/internal/validator/schema", this.fSchemaValidator);
        this.fValidationManager = new ValidationManager();
        this.fComponents.put("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
        this.fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", null);
        this.fComponents.put("http://apache.org/xml/properties/internal/error-handler", null);
        this.fComponents.put("http://apache.org/xml/properties/security-manager", null);
        this.fComponents.put("http://apache.org/xml/properties/internal/symbol-table", new SymbolTable());
        this.fComponents.put("http://apache.org/xml/properties/internal/grammar-pool", xSGrammarPoolContainer.getGrammarPool());
        this.fUseGrammarPoolOnly = xSGrammarPoolContainer.isFullyComposed();
        this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
        String[] arrstring = new String[]{"http://apache.org/xml/features/disallow-doctype-decl", "http://apache.org/xml/features/validation/schema/normalized-value", "http://apache.org/xml/features/validation/schema/element-default", "http://apache.org/xml/features/validation/schema/augment-psvi"};
        this.addRecognizedFeatures(arrstring);
        this.fFeatures.put("http://apache.org/xml/features/disallow-doctype-decl", Boolean.FALSE);
        this.fFeatures.put("http://apache.org/xml/features/validation/schema/normalized-value", Boolean.FALSE);
        this.fFeatures.put("http://apache.org/xml/features/validation/schema/element-default", Boolean.FALSE);
        this.fFeatures.put("http://apache.org/xml/features/validation/schema/augment-psvi", Boolean.TRUE);
        this.addRecognizedParamsAndSetDefaults(this.fEntityManager, xSGrammarPoolContainer);
        this.addRecognizedParamsAndSetDefaults(this.fErrorReporter, xSGrammarPoolContainer);
        this.addRecognizedParamsAndSetDefaults(this.fSchemaValidator, xSGrammarPoolContainer);
        Boolean bl = xSGrammarPoolContainer.getFeature("http://javax.xml.XMLConstants/feature/secure-processing");
        this.fInitSecurityManager = Boolean.TRUE.equals(bl) ? new SecurityManager() : null;
        this.fComponents.put("http://apache.org/xml/properties/security-manager", this.fInitSecurityManager);
        this.fFeatures.put("http://apache.org/xml/features/validation/schema/ignore-xsi-type-until-elemdecl", Boolean.FALSE);
        this.fFeatures.put("http://apache.org/xml/features/validation/id-idref-checking", Boolean.TRUE);
        this.fFeatures.put("http://apache.org/xml/features/validation/identity-constraint-checking", Boolean.TRUE);
        this.fFeatures.put("http://apache.org/xml/features/validation/unparsed-entity-checking", Boolean.TRUE);
    }

    public boolean getFeature(String string) throws XMLConfigurationException {
        if ("http://apache.org/xml/features/internal/parser-settings".equals(string)) {
            return this.fConfigUpdated;
        }
        if ("http://xml.org/sax/features/validation".equals(string) || "http://apache.org/xml/features/validation/schema".equals(string)) {
            return true;
        }
        if ("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(string)) {
            return this.fUseGrammarPoolOnly;
        }
        if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(string)) {
            return this.getProperty("http://apache.org/xml/properties/security-manager") != null;
        }
        return super.getFeature(string);
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
        if ("http://apache.org/xml/features/internal/parser-settings".equals(string)) {
            throw new XMLConfigurationException(1, string);
        }
        if (!bl && ("http://xml.org/sax/features/validation".equals(string) || "http://apache.org/xml/features/validation/schema".equals(string))) {
            throw new XMLConfigurationException(1, string);
        }
        if ("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(string) && bl != this.fUseGrammarPoolOnly) {
            throw new XMLConfigurationException(1, string);
        }
        if ("http://javax.xml.XMLConstants/feature/secure-processing".equals(string)) {
            this.setProperty("http://apache.org/xml/properties/security-manager", bl ? new SecurityManager() : null);
            return;
        }
        this.fConfigUpdated = true;
        this.fEntityManager.setFeature(string, bl);
        this.fErrorReporter.setFeature(string, bl);
        this.fSchemaValidator.setFeature(string, bl);
        if (!this.fInitFeatures.containsKey(string)) {
            boolean bl2 = super.getFeature(string);
            this.fInitFeatures.put(string, bl2 ? Boolean.TRUE : Boolean.FALSE);
        }
        super.setFeature(string, bl);
    }

    public Object getProperty(String string) throws XMLConfigurationException {
        if ("http://apache.org/xml/properties/locale".equals(string)) {
            return this.getLocale();
        }
        Object v2 = this.fComponents.get(string);
        if (v2 != null) {
            return v2;
        }
        if (this.fComponents.containsKey(string)) {
            return null;
        }
        return super.getProperty(string);
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        if ("http://apache.org/xml/properties/internal/entity-manager".equals(string) || "http://apache.org/xml/properties/internal/error-reporter".equals(string) || "http://apache.org/xml/properties/internal/namespace-context".equals(string) || "http://apache.org/xml/properties/internal/validator/schema".equals(string) || "http://apache.org/xml/properties/internal/symbol-table".equals(string) || "http://apache.org/xml/properties/internal/validation-manager".equals(string) || "http://apache.org/xml/properties/internal/grammar-pool".equals(string)) {
            throw new XMLConfigurationException(1, string);
        }
        this.fConfigUpdated = true;
        this.fEntityManager.setProperty(string, object);
        this.fErrorReporter.setProperty(string, object);
        this.fSchemaValidator.setProperty(string, object);
        if ("http://apache.org/xml/properties/internal/entity-resolver".equals(string) || "http://apache.org/xml/properties/internal/error-handler".equals(string) || "http://apache.org/xml/properties/security-manager".equals(string)) {
            this.fComponents.put(string, object);
            return;
        }
        if ("http://apache.org/xml/properties/locale".equals(string)) {
            this.setLocale((Locale)object);
            this.fComponents.put(string, object);
            return;
        }
        if (!this.fInitProperties.containsKey(string)) {
            this.fInitProperties.put(string, super.getProperty(string));
        }
        super.setProperty(string, object);
    }

    public void addRecognizedParamsAndSetDefaults(XMLComponent xMLComponent, XSGrammarPoolContainer xSGrammarPoolContainer) {
        String[] arrstring = xMLComponent.getRecognizedFeatures();
        this.addRecognizedFeatures(arrstring);
        String[] arrstring2 = xMLComponent.getRecognizedProperties();
        this.addRecognizedProperties(arrstring2);
        this.setFeatureDefaults(xMLComponent, arrstring, xSGrammarPoolContainer);
        this.setPropertyDefaults(xMLComponent, arrstring2);
    }

    public void reset() throws XNIException {
        this.fNamespaceContext.reset();
        this.fValidationManager.reset();
        this.fEntityManager.reset(this);
        this.fErrorReporter.reset(this);
        this.fSchemaValidator.reset(this);
        this.fConfigUpdated = false;
    }

    void setErrorHandler(ErrorHandler errorHandler) {
        this.fErrorHandler = errorHandler;
        this.setProperty("http://apache.org/xml/properties/internal/error-handler", errorHandler != null ? new ErrorHandlerWrapper(errorHandler) : new ErrorHandlerWrapper(DraconianErrorHandler.getInstance()));
    }

    ErrorHandler getErrorHandler() {
        return this.fErrorHandler;
    }

    void setResourceResolver(LSResourceResolver lSResourceResolver) {
        this.fResourceResolver = lSResourceResolver;
        this.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DOMEntityResolverWrapper(lSResourceResolver));
    }

    LSResourceResolver getResourceResolver() {
        return this.fResourceResolver;
    }

    void setLocale(Locale locale) {
        this.fLocale = locale;
        this.fErrorReporter.setLocale(locale);
    }

    Locale getLocale() {
        return this.fLocale;
    }

    void restoreInitialState() {
        String string;
        Iterator iterator;
        Map.Entry entry;
        this.fConfigUpdated = true;
        this.fComponents.put("http://apache.org/xml/properties/internal/entity-resolver", null);
        this.fComponents.put("http://apache.org/xml/properties/internal/error-handler", null);
        this.fComponents.put("http://apache.org/xml/properties/security-manager", this.fInitSecurityManager);
        this.setLocale(null);
        this.fComponents.put("http://apache.org/xml/properties/locale", null);
        if (!this.fInitFeatures.isEmpty()) {
            iterator = this.fInitFeatures.entrySet().iterator();
            while (iterator.hasNext()) {
                entry = iterator.next();
                string = (String)entry.getKey();
                boolean bl = (Boolean)entry.getValue();
                super.setFeature(string, bl);
            }
            this.fInitFeatures.clear();
        }
        if (!this.fInitProperties.isEmpty()) {
            iterator = this.fInitProperties.entrySet().iterator();
            while (iterator.hasNext()) {
                entry = iterator.next();
                string = (String)entry.getKey();
                Object v2 = entry.getValue();
                super.setProperty(string, v2);
            }
            this.fInitProperties.clear();
        }
    }

    private void setFeatureDefaults(XMLComponent xMLComponent, String[] arrstring, XSGrammarPoolContainer xSGrammarPoolContainer) {
        if (arrstring != null) {
            int n2 = 0;
            while (n2 < arrstring.length) {
                String string = arrstring[n2];
                Boolean bl = xSGrammarPoolContainer.getFeature(string);
                if (bl == null) {
                    bl = xMLComponent.getFeatureDefault(string);
                }
                if (bl != null && !this.fFeatures.containsKey(string)) {
                    this.fFeatures.put(string, bl);
                    this.fConfigUpdated = true;
                }
                ++n2;
            }
        }
    }

    private void setPropertyDefaults(XMLComponent xMLComponent, String[] arrstring) {
        if (arrstring != null) {
            int n2 = 0;
            while (n2 < arrstring.length) {
                String string = arrstring[n2];
                Object object = xMLComponent.getPropertyDefault(string);
                if (object != null && !this.fProperties.containsKey(string)) {
                    this.fProperties.put(string, object);
                    this.fConfigUpdated = true;
                }
                ++n2;
            }
        }
    }
}

