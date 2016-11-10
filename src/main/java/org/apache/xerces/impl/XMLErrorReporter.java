/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.util.ErrorHandlerProxy;
import org.apache.xerces.util.MessageFormatter;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;
import org.xml.sax.ErrorHandler;

public class XMLErrorReporter
implements XMLComponent {
    public static final short SEVERITY_WARNING = 0;
    public static final short SEVERITY_ERROR = 1;
    public static final short SEVERITY_FATAL_ERROR = 2;
    protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
    protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
    private static final String[] RECOGNIZED_FEATURES = new String[]{"http://apache.org/xml/features/continue-after-fatal-error"};
    private static final Boolean[] FEATURE_DEFAULTS = new Boolean[]{null};
    private static final String[] RECOGNIZED_PROPERTIES = new String[]{"http://apache.org/xml/properties/internal/error-handler"};
    private static final Object[] PROPERTY_DEFAULTS = new Object[]{null};
    protected Locale fLocale;
    protected Hashtable fMessageFormatters = new Hashtable();
    protected XMLErrorHandler fErrorHandler;
    protected XMLLocator fLocator;
    protected boolean fContinueAfterFatalError;
    protected XMLErrorHandler fDefaultErrorHandler;
    private ErrorHandler fSaxProxy = null;

    public void setLocale(Locale locale) {
        this.fLocale = locale;
    }

    public Locale getLocale() {
        return this.fLocale;
    }

    public void setDocumentLocator(XMLLocator xMLLocator) {
        this.fLocator = xMLLocator;
    }

    public void putMessageFormatter(String string, MessageFormatter messageFormatter) {
        this.fMessageFormatters.put(string, messageFormatter);
    }

    public MessageFormatter getMessageFormatter(String string) {
        return (MessageFormatter)this.fMessageFormatters.get(string);
    }

    public MessageFormatter removeMessageFormatter(String string) {
        return (MessageFormatter)this.fMessageFormatters.remove(string);
    }

    public String reportError(String string, String string2, Object[] arrobject, short s2) throws XNIException {
        return this.reportError(this.fLocator, string, string2, arrobject, s2);
    }

    public String reportError(String string, String string2, Object[] arrobject, short s2, Exception exception) throws XNIException {
        return this.reportError(this.fLocator, string, string2, arrobject, s2, exception);
    }

    public String reportError(XMLLocator xMLLocator, String string, String string2, Object[] arrobject, short s2) throws XNIException {
        return this.reportError(xMLLocator, string, string2, arrobject, s2, null);
    }

    public String reportError(XMLLocator xMLLocator, String string, String string2, Object[] arrobject, short s2, Exception exception) throws XNIException {
        String string3;
        StringBuffer stringBuffer;
        MessageFormatter messageFormatter = this.getMessageFormatter(string);
        if (messageFormatter != null) {
            string3 = messageFormatter.formatMessage(this.fLocale, string2, arrobject);
        } else {
            int n2;
            stringBuffer = new StringBuffer();
            stringBuffer.append(string);
            stringBuffer.append('#');
            stringBuffer.append(string2);
            int n3 = n2 = arrobject != null ? arrobject.length : 0;
            if (n2 > 0) {
                stringBuffer.append('?');
                int n4 = 0;
                while (n4 < n2) {
                    stringBuffer.append(arrobject[n4]);
                    if (n4 < n2 - 1) {
                        stringBuffer.append('&');
                    }
                    ++n4;
                }
            }
            string3 = stringBuffer.toString();
        }
        stringBuffer = exception != null ? new XMLParseException(xMLLocator, string3, exception) : new XMLParseException(xMLLocator, string3);
        XMLErrorHandler xMLErrorHandler = this.fErrorHandler;
        if (xMLErrorHandler == null) {
            if (this.fDefaultErrorHandler == null) {
                this.fDefaultErrorHandler = new DefaultErrorHandler();
            }
            xMLErrorHandler = this.fDefaultErrorHandler;
        }
        switch (s2) {
            case 0: {
                xMLErrorHandler.warning(string, string2, (XMLParseException)((Object)stringBuffer));
                break;
            }
            case 1: {
                xMLErrorHandler.error(string, string2, (XMLParseException)((Object)stringBuffer));
                break;
            }
            case 2: {
                xMLErrorHandler.fatalError(string, string2, (XMLParseException)((Object)stringBuffer));
                if (this.fContinueAfterFatalError) break;
                throw stringBuffer;
            }
        }
        return string3;
    }

    public void reset(XMLComponentManager xMLComponentManager) throws XNIException {
        try {
            this.fContinueAfterFatalError = xMLComponentManager.getFeature("http://apache.org/xml/features/continue-after-fatal-error");
        }
        catch (XNIException xNIException) {
            this.fContinueAfterFatalError = false;
        }
        this.fErrorHandler = (XMLErrorHandler)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-handler");
    }

    public String[] getRecognizedFeatures() {
        return (String[])RECOGNIZED_FEATURES.clone();
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
        int n2;
        if (string.startsWith("http://apache.org/xml/features/") && (n2 = string.length() - "http://apache.org/xml/features/".length()) == "continue-after-fatal-error".length() && string.endsWith("continue-after-fatal-error")) {
            this.fContinueAfterFatalError = bl;
        }
    }

    public boolean getFeature(String string) throws XMLConfigurationException {
        int n2;
        if (string.startsWith("http://apache.org/xml/features/") && (n2 = string.length() - "http://apache.org/xml/features/".length()) == "continue-after-fatal-error".length() && string.endsWith("continue-after-fatal-error")) {
            return this.fContinueAfterFatalError;
        }
        return false;
    }

    public String[] getRecognizedProperties() {
        return (String[])RECOGNIZED_PROPERTIES.clone();
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        int n2;
        if (string.startsWith("http://apache.org/xml/properties/") && (n2 = string.length() - "http://apache.org/xml/properties/".length()) == "internal/error-handler".length() && string.endsWith("internal/error-handler")) {
            this.fErrorHandler = (XMLErrorHandler)object;
        }
    }

    public Boolean getFeatureDefault(String string) {
        int n2 = 0;
        while (n2 < RECOGNIZED_FEATURES.length) {
            if (RECOGNIZED_FEATURES[n2].equals(string)) {
                return FEATURE_DEFAULTS[n2];
            }
            ++n2;
        }
        return null;
    }

    public Object getPropertyDefault(String string) {
        int n2 = 0;
        while (n2 < RECOGNIZED_PROPERTIES.length) {
            if (RECOGNIZED_PROPERTIES[n2].equals(string)) {
                return PROPERTY_DEFAULTS[n2];
            }
            ++n2;
        }
        return null;
    }

    public XMLErrorHandler getErrorHandler() {
        return this.fErrorHandler;
    }

    public ErrorHandler getSAXErrorHandler() {
        if (this.fSaxProxy == null) {
            this.fSaxProxy = new ErrorHandlerProxy(this){
                private final XMLErrorReporter this$0;

                protected XMLErrorHandler getErrorHandler() {
                    return this.this$0.fErrorHandler;
                }
            };
        }
        return this.fSaxProxy;
    }

}

