/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XSMessageFormatter;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

final class SchemaValidatorConfiguration
implements XMLComponentManager {
    private static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    private static final String VALIDATION = "http://xml.org/sax/features/validation";
    private static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
    private static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    private final XMLComponentManager fParentComponentManager;
    private final XMLGrammarPool fGrammarPool;
    private final boolean fUseGrammarPoolOnly;
    private final ValidationManager fValidationManager;

    public SchemaValidatorConfiguration(XMLComponentManager xMLComponentManager, XSGrammarPoolContainer xSGrammarPoolContainer, ValidationManager validationManager) {
        this.fParentComponentManager = xMLComponentManager;
        this.fGrammarPool = xSGrammarPoolContainer.getGrammarPool();
        this.fUseGrammarPoolOnly = xSGrammarPoolContainer.isFullyComposed();
        this.fValidationManager = validationManager;
        try {
            XMLErrorReporter xMLErrorReporter = (XMLErrorReporter)this.fParentComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
            if (xMLErrorReporter != null) {
                xMLErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
            }
        }
        catch (XMLConfigurationException xMLConfigurationException) {
            // empty catch block
        }
    }

    public boolean getFeature(String string) throws XMLConfigurationException {
        if ("http://apache.org/xml/features/internal/parser-settings".equals(string)) {
            return this.fParentComponentManager.getFeature(string);
        }
        if ("http://xml.org/sax/features/validation".equals(string) || "http://apache.org/xml/features/validation/schema".equals(string)) {
            return true;
        }
        if ("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only".equals(string)) {
            return this.fUseGrammarPoolOnly;
        }
        return this.fParentComponentManager.getFeature(string);
    }

    public Object getProperty(String string) throws XMLConfigurationException {
        if ("http://apache.org/xml/properties/internal/grammar-pool".equals(string)) {
            return this.fGrammarPool;
        }
        if ("http://apache.org/xml/properties/internal/validation-manager".equals(string)) {
            return this.fValidationManager;
        }
        return this.fParentComponentManager.getProperty(string);
    }
}

