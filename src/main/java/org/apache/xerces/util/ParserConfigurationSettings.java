/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;

public class ParserConfigurationSettings
implements XMLComponentManager {
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected ArrayList fRecognizedProperties = new ArrayList();
    protected HashMap fProperties = new HashMap();
    protected ArrayList fRecognizedFeatures = new ArrayList();
    protected HashMap fFeatures = new HashMap();
    protected XMLComponentManager fParentSettings;

    public ParserConfigurationSettings() {
        this(null);
    }

    public ParserConfigurationSettings(XMLComponentManager xMLComponentManager) {
        this.fParentSettings = xMLComponentManager;
    }

    public void addRecognizedFeatures(String[] arrstring) {
        int n2 = arrstring != null ? arrstring.length : 0;
        int n3 = 0;
        while (n3 < n2) {
            String string = arrstring[n3];
            if (!this.fRecognizedFeatures.contains(string)) {
                this.fRecognizedFeatures.add(string);
            }
            ++n3;
        }
    }

    public void setFeature(String string, boolean bl) throws XMLConfigurationException {
        this.checkFeature(string);
        this.fFeatures.put(string, bl ? Boolean.TRUE : Boolean.FALSE);
    }

    public void addRecognizedProperties(String[] arrstring) {
        int n2 = arrstring != null ? arrstring.length : 0;
        int n3 = 0;
        while (n3 < n2) {
            String string = arrstring[n3];
            if (!this.fRecognizedProperties.contains(string)) {
                this.fRecognizedProperties.add(string);
            }
            ++n3;
        }
    }

    public void setProperty(String string, Object object) throws XMLConfigurationException {
        this.checkProperty(string);
        this.fProperties.put(string, object);
    }

    public boolean getFeature(String string) throws XMLConfigurationException {
        Boolean bl = (Boolean)this.fFeatures.get(string);
        if (bl == null) {
            this.checkFeature(string);
            return false;
        }
        return bl;
    }

    public Object getProperty(String string) throws XMLConfigurationException {
        Object v2 = this.fProperties.get(string);
        if (v2 == null) {
            this.checkProperty(string);
        }
        return v2;
    }

    protected void checkFeature(String string) throws XMLConfigurationException {
        if (!this.fRecognizedFeatures.contains(string)) {
            if (this.fParentSettings != null) {
                this.fParentSettings.getFeature(string);
            } else {
                short s2 = 0;
                throw new XMLConfigurationException(s2, string);
            }
        }
    }

    protected void checkProperty(String string) throws XMLConfigurationException {
        if (!this.fRecognizedProperties.contains(string)) {
            if (this.fParentSettings != null) {
                this.fParentSettings.getProperty(string);
            } else {
                short s2 = 0;
                throw new XMLConfigurationException(s2, string);
            }
        }
    }
}

