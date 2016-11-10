/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.cmdline;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.cmdline.ObjectFactory;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Parameter;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public final class Transform {
    private SerializationHandler _handler;
    private String _fileName;
    private String _className;
    private String _jarFileSrc;
    private boolean _isJarFileSpecified = false;
    private Vector _params = null;
    private boolean _uri;
    private boolean _debug;
    private int _iterations;

    public Transform(String string, String string2, boolean bl, boolean bl2, int n2) {
        this._fileName = string2;
        this._className = string;
        this._uri = bl;
        this._debug = bl2;
        this._iterations = n2;
    }

    public String getFileName() {
        return this._fileName;
    }

    public String getClassName() {
        return this._className;
    }

    public void setParameters(Vector vector) {
        this._params = vector;
    }

    private void setJarFileInputSrc(boolean bl, String string) {
        this._isJarFileSpecified = bl;
        this._jarFileSrc = string;
    }

    private void doTransform() {
        try {
            Class class_ = ObjectFactory.findProviderClass(this._className, ObjectFactory.findClassLoader(), true);
            AbstractTranslet abstractTranslet = (AbstractTranslet)class_.newInstance();
            abstractTranslet.postInitialization();
            SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
            try {
                sAXParserFactory.setFeature("http://xml.org/sax/features/namespaces", true);
            }
            catch (Exception exception) {
                sAXParserFactory.setNamespaceAware(true);
            }
            SAXParser sAXParser = sAXParserFactory.newSAXParser();
            XMLReader xMLReader = sAXParser.getXMLReader();
            XSLTCDTMManager xSLTCDTMManager = (XSLTCDTMManager)XSLTCDTMManager.getDTMManagerClass().newInstance();
            DOMWSFilter dOMWSFilter = abstractTranslet != null && abstractTranslet instanceof StripFilter ? new DOMWSFilter(abstractTranslet) : null;
            DOMEnhancedForDTM dOMEnhancedForDTM = (DOMEnhancedForDTM)((Object)xSLTCDTMManager.getDTM(new SAXSource(xMLReader, new InputSource(this._fileName)), false, dOMWSFilter, true, false, abstractTranslet.hasIdCall()));
            dOMEnhancedForDTM.setDocumentURI(this._fileName);
            abstractTranslet.prepassDocument(dOMEnhancedForDTM);
            int n2 = this._params.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Parameter parameter = (Parameter)this._params.elementAt(i2);
                abstractTranslet.addParameter(parameter._name, parameter._value);
            }
            TransletOutputHandlerFactory transletOutputHandlerFactory = TransletOutputHandlerFactory.newInstance();
            transletOutputHandlerFactory.setOutputType(0);
            transletOutputHandlerFactory.setEncoding(abstractTranslet._encoding);
            transletOutputHandlerFactory.setOutputMethod(abstractTranslet._method);
            if (this._iterations == -1) {
                abstractTranslet.transform(dOMEnhancedForDTM, transletOutputHandlerFactory.getSerializationHandler());
            } else if (this._iterations > 0) {
                long l2 = System.currentTimeMillis();
                for (int i3 = 0; i3 < this._iterations; ++i3) {
                    abstractTranslet.transform(dOMEnhancedForDTM, transletOutputHandlerFactory.getSerializationHandler());
                }
                l2 = System.currentTimeMillis() - l2;
                System.err.println("\n<!--");
                System.err.println("  transform  = " + (double)l2 / (double)this._iterations + " ms");
                System.err.println("  throughput = " + 1000.0 / ((double)l2 / (double)this._iterations) + " tps");
                System.err.println("-->");
            }
        }
        catch (TransletException transletException) {
            if (this._debug) {
                transletException.printStackTrace();
            }
            System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + transletException.getMessage());
        }
        catch (RuntimeException runtimeException) {
            if (this._debug) {
                runtimeException.printStackTrace();
            }
            System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + runtimeException.getMessage());
        }
        catch (FileNotFoundException fileNotFoundException) {
            if (this._debug) {
                fileNotFoundException.printStackTrace();
            }
            ErrorMsg errorMsg = new ErrorMsg("FILE_NOT_FOUND_ERR", this._fileName);
            System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + errorMsg.toString());
        }
        catch (MalformedURLException malformedURLException) {
            if (this._debug) {
                malformedURLException.printStackTrace();
            }
            ErrorMsg errorMsg = new ErrorMsg("INVALID_URI_ERR", this._fileName);
            System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + errorMsg.toString());
        }
        catch (ClassNotFoundException classNotFoundException) {
            if (this._debug) {
                classNotFoundException.printStackTrace();
            }
            ErrorMsg errorMsg = new ErrorMsg("CLASS_NOT_FOUND_ERR", this._className);
            System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + errorMsg.toString());
        }
        catch (UnknownHostException unknownHostException) {
            if (this._debug) {
                unknownHostException.printStackTrace();
            }
            ErrorMsg errorMsg = new ErrorMsg("INVALID_URI_ERR", this._fileName);
            System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + errorMsg.toString());
        }
        catch (SAXException sAXException) {
            Exception exception = sAXException.getException();
            if (this._debug) {
                if (exception != null) {
                    exception.printStackTrace();
                }
                sAXException.printStackTrace();
            }
            System.err.print(new ErrorMsg("RUNTIME_ERROR_KEY"));
            if (exception != null) {
                System.err.println(exception.getMessage());
            } else {
                System.err.println(sAXException.getMessage());
            }
        }
        catch (Exception exception) {
            if (this._debug) {
                exception.printStackTrace();
            }
            System.err.println(new ErrorMsg("RUNTIME_ERROR_KEY") + exception.getMessage());
        }
    }

    public static void printUsage() {
        System.err.println(new ErrorMsg("TRANSFORM_USAGE_STR"));
    }

    public static void main(String[] arrstring) {
        try {
            if (arrstring.length > 0) {
                int n2;
                int n3 = -1;
                boolean bl = false;
                boolean bl2 = false;
                boolean bl3 = false;
                String string = null;
                for (n2 = 0; n2 < arrstring.length && arrstring[n2].charAt(0) == '-'; ++n2) {
                    if (arrstring[n2].equals("-u")) {
                        bl = true;
                        continue;
                    }
                    if (arrstring[n2].equals("-x")) {
                        bl2 = true;
                        continue;
                    }
                    if (arrstring[n2].equals("-j")) {
                        bl3 = true;
                        string = arrstring[++n2];
                        continue;
                    }
                    if (arrstring[n2].equals("-n")) {
                        try {
                            n3 = Integer.parseInt(arrstring[++n2]);
                        }
                        catch (NumberFormatException numberFormatException) {}
                        continue;
                    }
                    Transform.printUsage();
                }
                if (arrstring.length - n2 < 2) {
                    Transform.printUsage();
                }
                Transform transform = new Transform(arrstring[n2 + 1], arrstring[n2], bl, bl2, n3);
                transform.setJarFileInputSrc(bl3, string);
                Vector<Parameter> vector = new Vector<Parameter>();
                n2 += 2;
                while (n2 < arrstring.length) {
                    int n4 = arrstring[n2].indexOf(61);
                    if (n4 > 0) {
                        String string2 = arrstring[n2].substring(0, n4);
                        String string3 = arrstring[n2].substring(n4 + 1);
                        vector.addElement(new Parameter(string2, string3));
                    } else {
                        Transform.printUsage();
                    }
                    ++n2;
                }
                if (n2 == arrstring.length) {
                    transform.setParameters(vector);
                    transform.doTransform();
                }
            } else {
                Transform.printUsage();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

