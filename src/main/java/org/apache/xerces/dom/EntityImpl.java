/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.ParentNode;
import org.w3c.dom.Document;
import org.w3c.dom.Entity;
import org.w3c.dom.Node;

public class EntityImpl
extends ParentNode
implements Entity {
    static final long serialVersionUID = -3575760943444303423L;
    protected String name;
    protected String publicId;
    protected String systemId;
    protected String encoding;
    protected String inputEncoding;
    protected String version;
    protected String notationName;
    protected String baseURI;

    public EntityImpl(CoreDocumentImpl coreDocumentImpl, String string) {
        super(coreDocumentImpl);
        this.name = string;
        this.isReadOnly(true);
    }

    public short getNodeType() {
        return 6;
    }

    public String getNodeName() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.name;
    }

    public Node cloneNode(boolean bl) {
        EntityImpl entityImpl = (EntityImpl)super.cloneNode(bl);
        entityImpl.setReadOnly(true, bl);
        return entityImpl;
    }

    public String getPublicId() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.publicId;
    }

    public String getSystemId() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.systemId;
    }

    public String getXmlVersion() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.version;
    }

    public String getXmlEncoding() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.encoding;
    }

    public String getNotationName() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.notationName;
    }

    public void setPublicId(String string) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.publicId = string;
    }

    public void setXmlEncoding(String string) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.encoding = string;
    }

    public String getInputEncoding() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.inputEncoding;
    }

    public void setInputEncoding(String string) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.inputEncoding = string;
    }

    public void setXmlVersion(String string) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.version = string;
    }

    public void setSystemId(String string) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.systemId = string;
    }

    public void setNotationName(String string) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.notationName = string;
    }

    public String getBaseURI() {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        return this.baseURI != null ? this.baseURI : ((CoreDocumentImpl)this.getOwnerDocument()).getBaseURI();
    }

    public void setBaseURI(String string) {
        if (this.needsSyncData()) {
            this.synchronizeData();
        }
        this.baseURI = string;
    }
}

