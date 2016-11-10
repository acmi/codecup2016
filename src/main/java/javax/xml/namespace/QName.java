/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.namespace;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class QName
implements Serializable {
    private static final long serialVersionUID;
    private final String namespaceURI;
    private final String localPart;
    private String prefix;
    private transient String qNameAsString;

    public QName(String string, String string2) {
        this(string, string2, "");
    }

    public QName(String string, String string2, String string3) {
        this.namespaceURI = string == null ? "" : string;
        if (string2 == null) {
            throw new IllegalArgumentException("local part cannot be \"null\" when creating a QName");
        }
        this.localPart = string2;
        if (string3 == null) {
            throw new IllegalArgumentException("prefix cannot be \"null\" when creating a QName");
        }
        this.prefix = string3;
    }

    public QName(String string) {
        this("", string, "");
    }

    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    public String getLocalPart() {
        return this.localPart;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof QName) {
            QName qName = (QName)object;
            return this.localPart.equals(qName.localPart) && this.namespaceURI.equals(qName.namespaceURI);
        }
        return false;
    }

    public final int hashCode() {
        return this.namespaceURI.hashCode() ^ this.localPart.hashCode();
    }

    public String toString() {
        String string = this.qNameAsString;
        if (string == null) {
            int n2 = this.namespaceURI.length();
            if (n2 == 0) {
                string = this.localPart;
            } else {
                StringBuffer stringBuffer = new StringBuffer(n2 + this.localPart.length() + 2);
                stringBuffer.append('{');
                stringBuffer.append(this.namespaceURI);
                stringBuffer.append('}');
                stringBuffer.append(this.localPart);
                string = stringBuffer.toString();
            }
            this.qNameAsString = string;
        }
        return string;
    }

    static {
        String string = null;
        try {
            string = (String)AccessController.doPrivileged(new PrivilegedAction(){

                public Object run() {
                    return System.getProperty("org.apache.xml.namespace.QName.useCompatibleSerialVersionUID");
                }
            });
        }
        catch (Exception exception) {
            // empty catch block
        }
        serialVersionUID = !"1.0".equals(string) ? -9120448754896609940L : 4418622981026545151L;
    }

}

