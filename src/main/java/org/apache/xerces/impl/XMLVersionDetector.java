/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.impl;

import java.io.CharConversionException;
import java.io.EOFException;
import java.io.IOException;
import org.apache.xerces.impl.XMLEntityHandler;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.impl.XMLEntityScanner;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLInputSource;

public class XMLVersionDetector {
    private static final char[] XML11_VERSION = new char[]{'1', '.', '1'};
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    protected static final String fVersionSymbol = "version".intern();
    protected static final String fXMLSymbol = "[xml]".intern();
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEntityManager fEntityManager;
    protected String fEncoding = null;
    private final char[] fExpectedVersionString = new char[]{'<', '?', 'x', 'm', 'l', ' ', 'v', 'e', 'r', 's', 'i', 'o', 'n', '=', ' ', ' ', ' ', ' ', ' '};

    public void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException {
        this.fSymbolTable = (SymbolTable)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fEntityManager = (XMLEntityManager)xMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager");
        int n2 = 14;
        while (n2 < this.fExpectedVersionString.length) {
            this.fExpectedVersionString[n2] = 32;
            ++n2;
        }
    }

    public void startDocumentParsing(XMLEntityHandler xMLEntityHandler, short s2) {
        if (s2 == 1) {
            this.fEntityManager.setScannerVersion(1);
        } else {
            this.fEntityManager.setScannerVersion(2);
        }
        this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
        this.fEntityManager.setEntityHandler(xMLEntityHandler);
        xMLEntityHandler.startEntity(fXMLSymbol, this.fEntityManager.getCurrentResourceIdentifier(), this.fEncoding, null);
    }

    public short determineDocVersion(XMLInputSource xMLInputSource) throws IOException {
        this.fEncoding = this.fEntityManager.setupCurrentEntity(fXMLSymbol, xMLInputSource, false, true);
        this.fEntityManager.setScannerVersion(1);
        XMLEntityScanner xMLEntityScanner = this.fEntityManager.getEntityScanner();
        try {
            if (!xMLEntityScanner.skipString("<?xml")) {
                return 1;
            }
            if (!xMLEntityScanner.skipDeclSpaces()) {
                this.fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 5);
                return 1;
            }
            if (!xMLEntityScanner.skipString("version")) {
                this.fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 6);
                return 1;
            }
            xMLEntityScanner.skipDeclSpaces();
            if (xMLEntityScanner.peekChar() != 61) {
                this.fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 13);
                return 1;
            }
            xMLEntityScanner.scanChar();
            xMLEntityScanner.skipDeclSpaces();
            int n2 = xMLEntityScanner.scanChar();
            this.fExpectedVersionString[14] = (char)n2;
            int n3 = 0;
            while (n3 < XML11_VERSION.length) {
                this.fExpectedVersionString[15 + n3] = (char)xMLEntityScanner.scanChar();
                ++n3;
            }
            this.fExpectedVersionString[18] = (char)xMLEntityScanner.scanChar();
            this.fixupCurrentEntity(this.fEntityManager, this.fExpectedVersionString, 19);
            int n4 = 0;
            while (n4 < XML11_VERSION.length) {
                if (this.fExpectedVersionString[15 + n4] != XML11_VERSION[n4]) break;
                ++n4;
            }
            return n4 == XML11_VERSION.length ? 2 : 1;
        }
        catch (MalformedByteSequenceException malformedByteSequenceException) {
            this.fErrorReporter.reportError(malformedByteSequenceException.getDomain(), malformedByteSequenceException.getKey(), malformedByteSequenceException.getArguments(), 2, malformedByteSequenceException);
            return -1;
        }
        catch (CharConversionException charConversionException) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, 2, charConversionException);
            return -1;
        }
        catch (EOFException eOFException) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "PrematureEOF", null, 2);
            return -1;
        }
    }

    private void fixupCurrentEntity(XMLEntityManager xMLEntityManager, char[] arrc, int n2) {
        XMLEntityManager.ScannedEntity scannedEntity = xMLEntityManager.getCurrentEntity();
        if (scannedEntity.count - scannedEntity.position + n2 > scannedEntity.ch.length) {
            char[] arrc2 = scannedEntity.ch;
            scannedEntity.ch = new char[n2 + scannedEntity.count - scannedEntity.position + 1];
            System.arraycopy(arrc2, 0, scannedEntity.ch, 0, arrc2.length);
        }
        if (scannedEntity.position < n2) {
            System.arraycopy(scannedEntity.ch, scannedEntity.position, scannedEntity.ch, n2, scannedEntity.count - scannedEntity.position);
            scannedEntity.count += n2 - scannedEntity.position;
        } else {
            int n3 = n2;
            while (n3 < scannedEntity.position) {
                scannedEntity.ch[n3] = 32;
                ++n3;
            }
        }
        System.arraycopy(arrc, 0, scannedEntity.ch, 0, n2);
        scannedEntity.position = 0;
        scannedEntity.baseCharOffset = 0;
        scannedEntity.startPosition = 0;
        scannedEntity.lineNumber = 1;
        scannedEntity.columnNumber = 1;
    }
}

