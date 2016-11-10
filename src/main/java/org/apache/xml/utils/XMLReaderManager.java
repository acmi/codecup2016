/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.Hashtable;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XMLReaderManager {
    private static final XMLReaderManager m_singletonManager = new XMLReaderManager();
    private static SAXParserFactory m_parserFactory;
    private ThreadLocal m_readers;
    private Hashtable m_inUse;

    private XMLReaderManager() {
    }

    public static XMLReaderManager getInstance() {
        return m_singletonManager;
    }

    public synchronized XMLReader getXMLReader() throws SAXException {
        XMLReader xMLReader;
        boolean bl;
        if (this.m_readers == null) {
            this.m_readers = new ThreadLocal();
        }
        if (this.m_inUse == null) {
            this.m_inUse = new Hashtable();
        }
        boolean bl2 = bl = (xMLReader = (XMLReader)this.m_readers.get()) != null;
        if (!bl || this.m_inUse.get(xMLReader) == Boolean.TRUE) {
            try {
                try {
                    xMLReader = XMLReaderFactory.createXMLReader();
                }
                catch (Exception exception) {
                    if (m_parserFactory == null) {
                        m_parserFactory = SAXParserFactory.newInstance();
                        m_parserFactory.setNamespaceAware(true);
                    }
                    xMLReader = m_parserFactory.newSAXParser().getXMLReader();
                }
                try {
                    xMLReader.setFeature("http://xml.org/sax/features/namespaces", true);
                    xMLReader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                }
                catch (SAXException sAXException) {}
            }
            catch (ParserConfigurationException parserConfigurationException) {
                throw new SAXException(parserConfigurationException);
            }
            catch (FactoryConfigurationError factoryConfigurationError) {
                throw new SAXException(factoryConfigurationError.toString());
            }
            catch (NoSuchMethodError noSuchMethodError) {
            }
            catch (AbstractMethodError abstractMethodError) {
                // empty catch block
            }
            if (!bl) {
                this.m_readers.set(xMLReader);
                this.m_inUse.put(xMLReader, Boolean.TRUE);
            }
        } else {
            this.m_inUse.put(xMLReader, Boolean.TRUE);
        }
        return xMLReader;
    }

    public synchronized void releaseXMLReader(XMLReader xMLReader) {
        if (this.m_readers.get() == xMLReader && xMLReader != null) {
            this.m_inUse.remove(xMLReader);
        }
    }
}

