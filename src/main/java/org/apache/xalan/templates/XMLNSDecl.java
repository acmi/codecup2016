/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.templates;

import java.io.Serializable;

public class XMLNSDecl
implements Serializable {
    static final long serialVersionUID = 6710237366877605097L;
    private String m_prefix;
    private String m_uri;
    private boolean m_isExcluded;

    public XMLNSDecl(String string, String string2, boolean bl) {
        this.m_prefix = string;
        this.m_uri = string2;
        this.m_isExcluded = bl;
    }

    public String getPrefix() {
        return this.m_prefix;
    }

    public String getURI() {
        return this.m_uri;
    }

    public boolean getIsExcluded() {
        return this.m_isExcluded;
    }
}

