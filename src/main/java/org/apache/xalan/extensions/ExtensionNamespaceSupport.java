/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.extensions;

import java.lang.reflect.Constructor;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExtensionHandler;

public class ExtensionNamespaceSupport {
    String m_namespace = null;
    String m_handlerClass = null;
    Class[] m_sig = null;
    Object[] m_args = null;

    public ExtensionNamespaceSupport(String string, String string2, Object[] arrobject) {
        this.m_namespace = string;
        this.m_handlerClass = string2;
        this.m_args = arrobject;
        this.m_sig = new Class[this.m_args.length];
        for (int i2 = 0; i2 < this.m_args.length; ++i2) {
            if (this.m_args[i2] == null) {
                this.m_sig = null;
                break;
            }
            this.m_sig[i2] = this.m_args[i2].getClass();
        }
    }

    public String getNamespace() {
        return this.m_namespace;
    }

    public ExtensionHandler launch() throws TransformerException {
        ExtensionHandler extensionHandler;
        block6 : {
            extensionHandler = null;
            try {
                Class class_ = ExtensionHandler.getClassForName(this.m_handlerClass);
                Constructor constructor = null;
                if (this.m_sig != null) {
                    constructor = class_.getConstructor(this.m_sig);
                } else {
                    Constructor<?>[] arrconstructor = class_.getConstructors();
                    for (int i2 = 0; i2 < arrconstructor.length; ++i2) {
                        if (arrconstructor[i2].getParameterTypes().length != this.m_args.length) continue;
                        constructor = arrconstructor[i2];
                        break;
                    }
                }
                if (constructor != null) {
                    extensionHandler = (ExtensionHandler)constructor.newInstance(this.m_args);
                    break block6;
                }
                throw new TransformerException("ExtensionHandler constructor not found");
            }
            catch (Exception exception) {
                throw new TransformerException(exception);
            }
        }
        return extensionHandler;
    }
}

