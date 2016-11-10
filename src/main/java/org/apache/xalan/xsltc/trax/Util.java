/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.trax;

import java.io.InputStream;
import java.io.Reader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.trax.DOM2SAX;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public final class Util {
    public static String baseName(String string) {
        return org.apache.xalan.xsltc.compiler.util.Util.baseName(string);
    }

    public static String noExtName(String string) {
        return org.apache.xalan.xsltc.compiler.util.Util.noExtName(string);
    }

    public static String toJavaName(String string) {
        return org.apache.xalan.xsltc.compiler.util.Util.toJavaName(string);
    }

    public static InputSource getInputSource(XSLTC xSLTC, Source source) throws TransformerConfigurationException {
        InputSource inputSource = null;
        String string = source.getSystemId();
        try {
            if (source instanceof SAXSource) {
                SAXSource sAXSource = (SAXSource)source;
                inputSource = sAXSource.getInputSource();
                try {
                    XMLReader xMLReader = sAXSource.getXMLReader();
                    if (xMLReader == null) {
                        try {
                            xMLReader = XMLReaderFactory.createXMLReader();
                        }
                        catch (Exception exception) {
                            try {
                                SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
                                sAXParserFactory.setNamespaceAware(true);
                                if (xSLTC.isSecureProcessing()) {
                                    try {
                                        sAXParserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                                    }
                                    catch (SAXException sAXException) {
                                        // empty catch block
                                    }
                                }
                                xMLReader = sAXParserFactory.newSAXParser().getXMLReader();
                            }
                            catch (ParserConfigurationException parserConfigurationException) {
                                throw new TransformerConfigurationException("ParserConfigurationException", parserConfigurationException);
                            }
                        }
                    }
                    xMLReader.setFeature("http://xml.org/sax/features/namespaces", true);
                    xMLReader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                    xSLTC.setXMLReader(xMLReader);
                }
                catch (SAXNotRecognizedException sAXNotRecognizedException) {
                    throw new TransformerConfigurationException("SAXNotRecognizedException ", sAXNotRecognizedException);
                }
                catch (SAXNotSupportedException sAXNotSupportedException) {
                    throw new TransformerConfigurationException("SAXNotSupportedException ", sAXNotSupportedException);
                }
                catch (SAXException sAXException) {
                    throw new TransformerConfigurationException("SAXException ", sAXException);
                }
            }
            if (source instanceof DOMSource) {
                DOMSource dOMSource = (DOMSource)source;
                Document document = (Document)dOMSource.getNode();
                DOM2SAX dOM2SAX = new DOM2SAX(document);
                xSLTC.setXMLReader(dOM2SAX);
                inputSource = SAXSource.sourceToInputSource(source);
                if (inputSource == null) {
                    inputSource = new InputSource(dOMSource.getSystemId());
                }
            } else if (source instanceof StreamSource) {
                StreamSource streamSource = (StreamSource)source;
                InputStream inputStream = streamSource.getInputStream();
                Reader reader = streamSource.getReader();
                xSLTC.setXMLReader(null);
                inputSource = inputStream != null ? new InputSource(inputStream) : (reader != null ? new InputSource(reader) : new InputSource(string));
            } else {
                ErrorMsg errorMsg = new ErrorMsg("JAXP_UNKNOWN_SOURCE_ERR");
                throw new TransformerConfigurationException(errorMsg.toString());
            }
            inputSource.setSystemId(string);
        }
        catch (NullPointerException nullPointerException) {
            ErrorMsg errorMsg = new ErrorMsg("JAXP_NO_SOURCE_ERR", "TransformerFactory.newTemplates()");
            throw new TransformerConfigurationException(errorMsg.toString());
        }
        catch (SecurityException securityException) {
            ErrorMsg errorMsg = new ErrorMsg("FILE_ACCESS_ERR", string);
            throw new TransformerConfigurationException(errorMsg.toString());
        }
        return inputSource;
    }
}

