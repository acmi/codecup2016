/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.client;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.res.XSLMessages;

public class XSLTProcessorApplet
extends Applet {
    transient TransformerFactory m_tfactory = null;
    private String m_styleURL;
    private String m_documentURL;
    private final String PARAM_styleURL = "styleURL";
    private final String PARAM_documentURL = "documentURL";
    private String m_styleURLOfCached = null;
    private String m_documentURLOfCached = null;
    private URL m_codeBase = null;
    private String m_treeURL = null;
    private URL m_documentBase = null;
    private transient Thread m_callThread = null;
    private transient TrustedAgent m_trustedAgent = null;
    private transient Thread m_trustedWorker = null;
    private transient String m_htmlText = null;
    private transient String m_sourceText = null;
    private transient String m_nameOfIDAttrOfElemToModify = null;
    private transient String m_elemIdToModify = null;
    private transient String m_attrNameToSet = null;
    private transient String m_attrValueToSet = null;
    transient Hashtable m_parameters;
    private static final long serialVersionUID = 4618876841979251422L;

    public String getAppletInfo() {
        return "Name: XSLTProcessorApplet\r\nAuthor: Scott Boag";
    }

    public String[][] getParameterInfo() {
        String[][] arrstring = new String[][]{{"styleURL", "String", "URL to an XSL stylesheet"}, {"documentURL", "String", "URL to an XML document"}};
        return arrstring;
    }

    public void init() {
        String string = this.getParameter("styleURL");
        this.m_parameters = new Hashtable();
        if (string != null) {
            this.setStyleURL(string);
        }
        if ((string = this.getParameter("documentURL")) != null) {
            this.setDocumentURL(string);
        }
        this.m_codeBase = this.getCodeBase();
        this.m_documentBase = this.getDocumentBase();
        this.resize(320, 240);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void start() {
        this.m_trustedAgent = new TrustedAgent(this);
        Thread thread = Thread.currentThread();
        this.m_trustedWorker = new Thread(thread.getThreadGroup(), this.m_trustedAgent);
        this.m_trustedWorker.start();
        try {
            this.m_tfactory = TransformerFactory.newInstance();
            this.showStatus("Causing Transformer and Parser to Load and JIT...");
            StringReader stringReader = new StringReader("<?xml version='1.0'?><foo/>");
            StringReader stringReader2 = new StringReader("<?xml version='1.0'?><xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'><xsl:template match='foo'><out/></xsl:template></xsl:stylesheet>");
            PrintWriter printWriter = new PrintWriter(new StringWriter());
            TransformerFactory transformerFactory = this.m_tfactory;
            synchronized (transformerFactory) {
                Templates templates = this.m_tfactory.newTemplates(new StreamSource(stringReader2));
                Transformer transformer = templates.newTransformer();
                transformer.transform(new StreamSource(stringReader), new StreamResult(printWriter));
            }
            System.out.println("Primed the pump!");
            this.showStatus("Ready to go!");
        }
        catch (Exception exception) {
            this.showStatus("Could not prime the pump!");
            System.out.println("Could not prime the pump!");
            exception.printStackTrace();
        }
    }

    public void paint(Graphics graphics) {
    }

    public void stop() {
        if (null != this.m_trustedWorker) {
            this.m_trustedWorker.stop();
            this.m_trustedWorker = null;
        }
        this.m_styleURLOfCached = null;
        this.m_documentURLOfCached = null;
    }

    public void destroy() {
        if (null != this.m_trustedWorker) {
            this.m_trustedWorker.stop();
            this.m_trustedWorker = null;
        }
        this.m_styleURLOfCached = null;
        this.m_documentURLOfCached = null;
    }

    public void setStyleURL(String string) {
        this.m_styleURL = string;
    }

    public void setDocumentURL(String string) {
        this.m_documentURL = string;
    }

    public void freeCache() {
        this.m_styleURLOfCached = null;
        this.m_documentURLOfCached = null;
    }

    public void setStyleSheetAttribute(String string, String string2, String string3, String string4) {
        this.m_nameOfIDAttrOfElemToModify = string;
        this.m_elemIdToModify = string2;
        this.m_attrNameToSet = string3;
        this.m_attrValueToSet = string4;
    }

    public void setStylesheetParam(String string, String string2) {
        this.m_parameters.put(string, string2);
    }

    public String escapeString(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            if ('<' == c2) {
                stringBuffer.append("&lt;");
                continue;
            }
            if ('>' == c2) {
                stringBuffer.append("&gt;");
                continue;
            }
            if ('&' == c2) {
                stringBuffer.append("&amp;");
                continue;
            }
            if ('\ud800' <= c2 && c2 < '\udc00') {
                int n3;
                if (i2 + 1 >= n2) {
                    throw new RuntimeException(XSLMessages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(c2)}));
                }
                if (56320 > (n3 = string.charAt(++i2)) || n3 >= 57344) {
                    throw new RuntimeException(XSLMessages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(c2) + " " + Integer.toHexString(n3)}));
                }
                n3 = (c2 - 55296 << 10) + n3 - 56320 + 65536;
                stringBuffer.append("&#x");
                stringBuffer.append(Integer.toHexString(n3));
                stringBuffer.append(";");
                continue;
            }
            stringBuffer.append(c2);
        }
        return stringBuffer.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getHtmlText() {
        this.m_trustedAgent.m_getData = true;
        this.m_callThread = Thread.currentThread();
        try {
            Thread thread = this.m_callThread;
            synchronized (thread) {
                this.m_callThread.wait();
            }
        }
        catch (InterruptedException interruptedException) {
            System.out.println(interruptedException.getMessage());
        }
        return this.m_htmlText;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getTreeAsText(String string) throws IOException {
        this.m_treeURL = string;
        this.m_trustedAgent.m_getData = true;
        this.m_trustedAgent.m_getSource = true;
        this.m_callThread = Thread.currentThread();
        try {
            Thread thread = this.m_callThread;
            synchronized (thread) {
                this.m_callThread.wait();
            }
        }
        catch (InterruptedException interruptedException) {
            System.out.println(interruptedException.getMessage());
        }
        return this.m_sourceText;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String getSource() throws TransformerException {
        String string;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, false);
        string = "";
        try {
            URL uRL = new URL(this.m_documentBase, this.m_treeURL);
            TransformerFactory transformerFactory = this.m_tfactory;
            synchronized (transformerFactory) {
                Transformer transformer = this.m_tfactory.newTransformer();
                StreamSource streamSource = new StreamSource(uRL.toString());
                StreamResult streamResult = new StreamResult(printWriter);
                transformer.transform(streamSource, streamResult);
                string = stringWriter.toString();
            }
        }
        catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            throw new RuntimeException(malformedURLException.getMessage());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return string;
    }

    public String getSourceTreeAsText() throws Exception {
        return this.getTreeAsText(this.m_documentURL);
    }

    public String getStyleTreeAsText() throws Exception {
        return this.getTreeAsText(this.m_styleURL);
    }

    public String getResultTreeAsText() throws Exception {
        return this.escapeString(this.getHtmlText());
    }

    public String transformToHtml(String string, String string2) {
        if (null != string) {
            this.m_documentURL = string;
        }
        if (null != string2) {
            this.m_styleURL = string2;
        }
        return this.getHtmlText();
    }

    public String transformToHtml(String string) {
        if (null != string) {
            this.m_documentURL = string;
        }
        this.m_styleURL = null;
        return this.getHtmlText();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String processTransformation() throws TransformerException {
        String string = null;
        this.showStatus("Waiting for Transformer and Parser to finish loading and JITing...");
        TransformerFactory transformerFactory = this.m_tfactory;
        synchronized (transformerFactory) {
            URL uRL = null;
            URL uRL2 = null;
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter, false);
            StreamResult streamResult = new StreamResult(printWriter);
            this.showStatus("Begin Transformation...");
            try {
                uRL = new URL(this.m_codeBase, this.m_documentURL);
                StreamSource streamSource = new StreamSource(uRL.toString());
                uRL2 = new URL(this.m_codeBase, this.m_styleURL);
                StreamSource streamSource2 = new StreamSource(uRL2.toString());
                Transformer transformer = this.m_tfactory.newTransformer(streamSource2);
                Iterator iterator = this.m_parameters.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = iterator.next();
                    Object k2 = entry.getKey();
                    Object v2 = entry.getValue();
                    transformer.setParameter((String)k2, v2);
                }
                transformer.transform(streamSource, streamResult);
            }
            catch (TransformerConfigurationException transformerConfigurationException) {
                transformerConfigurationException.printStackTrace();
                throw new RuntimeException(transformerConfigurationException.getMessage());
            }
            catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
                throw new RuntimeException(malformedURLException.getMessage());
            }
            this.showStatus("Transformation Done!");
            string = stringWriter.toString();
        }
        return string;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.m_tfactory = TransformerFactory.newInstance();
    }

    static String access$002(XSLTProcessorApplet xSLTProcessorApplet, String string) {
        xSLTProcessorApplet.m_htmlText = string;
        return xSLTProcessorApplet.m_htmlText;
    }

    static String access$102(XSLTProcessorApplet xSLTProcessorApplet, String string) {
        xSLTProcessorApplet.m_sourceText = string;
        return xSLTProcessorApplet.m_sourceText;
    }

    static String access$200(XSLTProcessorApplet xSLTProcessorApplet) throws TransformerException {
        return xSLTProcessorApplet.getSource();
    }

    static String access$300(XSLTProcessorApplet xSLTProcessorApplet) throws TransformerException {
        return xSLTProcessorApplet.processTransformation();
    }

    static Thread access$400(XSLTProcessorApplet xSLTProcessorApplet) {
        return xSLTProcessorApplet.m_callThread;
    }

    class TrustedAgent
    implements Runnable {
        public boolean m_getData;
        public boolean m_getSource;
        private final XSLTProcessorApplet this$0;

        TrustedAgent(XSLTProcessorApplet xSLTProcessorApplet) {
            this.this$0 = xSLTProcessorApplet;
            this.m_getData = false;
            this.m_getSource = false;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void run() {
            do {
                Thread.yield();
                if (this.m_getData) {
                    try {
                        this.m_getData = false;
                        XSLTProcessorApplet.access$002(this.this$0, null);
                        XSLTProcessorApplet.access$102(this.this$0, null);
                        if (this.m_getSource) {
                            this.m_getSource = false;
                            XSLTProcessorApplet.access$102(this.this$0, XSLTProcessorApplet.access$200(this.this$0));
                        }
                        XSLTProcessorApplet.access$002(this.this$0, XSLTProcessorApplet.access$300(this.this$0));
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    finally {
                        Thread thread = XSLTProcessorApplet.access$400(this.this$0);
                        synchronized (thread) {
                            XSLTProcessorApplet.access$400(this.this$0).notify();
                        }
                    }
                }
                try {
                    Thread.sleep(50);
                    continue;
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                    continue;
                }
                break;
            } while (true);
        }
    }

}

