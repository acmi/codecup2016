/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.apache.xalan.xsltc.trax.ObjectFactory;
import org.apache.xalan.xsltc.trax.TransformerFactoryImpl;
import org.apache.xalan.xsltc.trax.TransformerImpl;

public final class TemplatesImpl
implements Serializable,
Templates {
    static final long serialVersionUID = 673094361519270707L;
    private static String ABSTRACT_TRANSLET = "org.apache.xalan.xsltc.runtime.AbstractTranslet";
    private String _name = null;
    private byte[][] _bytecodes = null;
    private Class[] _class = null;
    private int _transletIndex = -1;
    private Hashtable _auxClasses = null;
    private Properties _outputProperties;
    private int _indentNumber;
    private transient URIResolver _uriResolver = null;
    private transient ThreadLocal _sdom = new ThreadLocal();
    private transient TransformerFactoryImpl _tfactory = null;

    protected TemplatesImpl(byte[][] arrby, String string, Properties properties, int n2, TransformerFactoryImpl transformerFactoryImpl) {
        this._bytecodes = arrby;
        this._name = string;
        this._outputProperties = properties;
        this._indentNumber = n2;
        this._tfactory = transformerFactoryImpl;
    }

    protected TemplatesImpl(Class[] arrclass, String string, Properties properties, int n2, TransformerFactoryImpl transformerFactoryImpl) {
        this._class = arrclass;
        this._name = string;
        this._transletIndex = 0;
        this._outputProperties = properties;
        this._indentNumber = n2;
        this._tfactory = transformerFactoryImpl;
    }

    public TemplatesImpl() {
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (objectInputStream.readBoolean()) {
            this._uriResolver = (URIResolver)objectInputStream.readObject();
        }
        this._tfactory = new TransformerFactoryImpl();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        objectOutputStream.defaultWriteObject();
        if (this._uriResolver instanceof Serializable) {
            objectOutputStream.writeBoolean(true);
            objectOutputStream.writeObject((Serializable)((Object)this._uriResolver));
        } else {
            objectOutputStream.writeBoolean(false);
        }
    }

    public synchronized void setURIResolver(URIResolver uRIResolver) {
        this._uriResolver = uRIResolver;
    }

    protected synchronized void setTransletBytecodes(byte[][] arrby) {
        this._bytecodes = arrby;
    }

    public synchronized byte[][] getTransletBytecodes() {
        return this._bytecodes;
    }

    public synchronized Class[] getTransletClasses() {
        try {
            if (this._class == null) {
                this.defineTransletClasses();
            }
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            // empty catch block
        }
        return this._class;
    }

    public synchronized int getTransletIndex() {
        try {
            if (this._class == null) {
                this.defineTransletClasses();
            }
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            // empty catch block
        }
        return this._transletIndex;
    }

    protected synchronized void setTransletName(String string) {
        this._name = string;
    }

    protected synchronized String getTransletName() {
        return this._name;
    }

    private void defineTransletClasses() throws TransformerConfigurationException {
        if (this._bytecodes == null) {
            ErrorMsg errorMsg = new ErrorMsg("NO_TRANSLET_CLASS_ERR");
            throw new TransformerConfigurationException(errorMsg.toString());
        }
        TransletClassLoader transletClassLoader = (TransletClassLoader)AccessController.doPrivileged(new PrivilegedAction(this){
            private final TemplatesImpl this$0;

            public Object run() {
                return new TransletClassLoader(ObjectFactory.findClassLoader());
            }
        });
        try {
            int n2 = this._bytecodes.length;
            this._class = new Class[n2];
            if (n2 > 1) {
                this._auxClasses = new Hashtable();
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                this._class[i2] = transletClassLoader.defineClass(this._bytecodes[i2]);
                Class class_ = this._class[i2].getSuperclass();
                if (class_.getName().equals(ABSTRACT_TRANSLET)) {
                    this._transletIndex = i2;
                    continue;
                }
                this._auxClasses.put(this._class[i2].getName(), this._class[i2]);
            }
            if (this._transletIndex < 0) {
                ErrorMsg errorMsg = new ErrorMsg("NO_MAIN_TRANSLET_ERR", this._name);
                throw new TransformerConfigurationException(errorMsg.toString());
            }
        }
        catch (ClassFormatError classFormatError) {
            ErrorMsg errorMsg = new ErrorMsg("TRANSLET_CLASS_ERR", this._name);
            throw new TransformerConfigurationException(errorMsg.toString());
        }
        catch (LinkageError linkageError) {
            ErrorMsg errorMsg = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
            throw new TransformerConfigurationException(errorMsg.toString());
        }
    }

    private Translet getTransletInstance() throws TransformerConfigurationException {
        try {
            if (this._name == null) {
                return null;
            }
            if (this._class == null) {
                this.defineTransletClasses();
            }
            AbstractTranslet abstractTranslet = (AbstractTranslet)this._class[this._transletIndex].newInstance();
            abstractTranslet.postInitialization();
            abstractTranslet.setTemplates(this);
            if (this._auxClasses != null) {
                abstractTranslet.setAuxiliaryClasses(this._auxClasses);
            }
            return abstractTranslet;
        }
        catch (InstantiationException instantiationException) {
            ErrorMsg errorMsg = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
            throw new TransformerConfigurationException(errorMsg.toString());
        }
        catch (IllegalAccessException illegalAccessException) {
            ErrorMsg errorMsg = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
            throw new TransformerConfigurationException(errorMsg.toString());
        }
    }

    public synchronized Transformer newTransformer() throws TransformerConfigurationException {
        TransformerImpl transformerImpl = new TransformerImpl(this.getTransletInstance(), this._outputProperties, this._indentNumber, this._tfactory);
        if (this._uriResolver != null) {
            transformerImpl.setURIResolver(this._uriResolver);
        }
        if (this._tfactory.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
            transformerImpl.setSecureProcessing(true);
        }
        return transformerImpl;
    }

    public synchronized Properties getOutputProperties() {
        try {
            return this.newTransformer().getOutputProperties();
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            return null;
        }
    }

    public DOM getStylesheetDOM() {
        return (DOM)this._sdom.get();
    }

    public void setStylesheetDOM(DOM dOM) {
        this._sdom.set(dOM);
    }

    static final class TransletClassLoader
    extends ClassLoader {
        TransletClassLoader(ClassLoader classLoader) {
            super(classLoader);
        }

        Class defineClass(byte[] arrby) {
            return this.defineClass(null, arrby, 0, arrby.length);
        }
    }

}

