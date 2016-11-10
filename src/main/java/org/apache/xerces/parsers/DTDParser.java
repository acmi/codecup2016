/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.parsers;

import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.parsers.XMLGrammarParser;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDTDContentModelSource;
import org.apache.xerces.xni.parser.XMLDTDScanner;
import org.apache.xerces.xni.parser.XMLDTDSource;

public abstract class DTDParser
extends XMLGrammarParser
implements XMLDTDContentModelHandler,
XMLDTDHandler {
    protected XMLDTDScanner fDTDScanner;

    public DTDParser(SymbolTable symbolTable) {
        super(symbolTable);
    }

    public DTDGrammar getDTDGrammar() {
        return null;
    }

    public void startEntity(String string, String string2, String string3, String string4) throws XNIException {
    }

    public void textDecl(String string, String string2) throws XNIException {
    }

    public void startDTD(XMLLocator xMLLocator, Augmentations augmentations) throws XNIException {
    }

    public void comment(XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void processingInstruction(String string, XMLString xMLString, Augmentations augmentations) throws XNIException {
    }

    public void startExternalSubset(XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
    }

    public void endExternalSubset(Augmentations augmentations) throws XNIException {
    }

    public void elementDecl(String string, String string2, Augmentations augmentations) throws XNIException {
    }

    public void startAttlist(String string, Augmentations augmentations) throws XNIException {
    }

    public void attributeDecl(String string, String string2, String string3, String[] arrstring, String string4, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
    }

    public void endAttlist(Augmentations augmentations) throws XNIException {
    }

    public void internalEntityDecl(String string, XMLString xMLString, XMLString xMLString2, Augmentations augmentations) throws XNIException {
    }

    public void externalEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
    }

    public void unparsedEntityDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, String string2, Augmentations augmentations) throws XNIException {
    }

    public void notationDecl(String string, XMLResourceIdentifier xMLResourceIdentifier, Augmentations augmentations) throws XNIException {
    }

    public void startConditional(short s2, Augmentations augmentations) throws XNIException {
    }

    public void endConditional(Augmentations augmentations) throws XNIException {
    }

    public void endDTD(Augmentations augmentations) throws XNIException {
    }

    public void endEntity(String string, Augmentations augmentations) throws XNIException {
    }

    public void startContentModel(String string, short s2) throws XNIException {
    }

    public void mixedElement(String string) throws XNIException {
    }

    public void childrenStartGroup() throws XNIException {
    }

    public void childrenElement(String string) throws XNIException {
    }

    public void childrenSeparator(short s2) throws XNIException {
    }

    public void childrenOccurrence(short s2) throws XNIException {
    }

    public void childrenEndGroup() throws XNIException {
    }

    public void endContentModel() throws XNIException {
    }

    public abstract XMLDTDSource getDTDSource();

    public abstract void setDTDSource(XMLDTDSource var1);

    public abstract void ignoredCharacters(XMLString var1, Augmentations var2) throws XNIException;

    public abstract void endParameterEntity(String var1, Augmentations var2) throws XNIException;

    public abstract void textDecl(String var1, String var2, Augmentations var3) throws XNIException;

    public abstract void startParameterEntity(String var1, XMLResourceIdentifier var2, String var3, Augmentations var4) throws XNIException;

    public abstract XMLDTDContentModelSource getDTDContentModelSource();

    public abstract void setDTDContentModelSource(XMLDTDContentModelSource var1);

    public abstract void endContentModel(Augmentations var1) throws XNIException;

    public abstract void endGroup(Augmentations var1) throws XNIException;

    public abstract void occurrence(short var1, Augmentations var2) throws XNIException;

    public abstract void separator(short var1, Augmentations var2) throws XNIException;

    public abstract void element(String var1, Augmentations var2) throws XNIException;

    public abstract void pcdata(Augmentations var1) throws XNIException;

    public abstract void startGroup(Augmentations var1) throws XNIException;

    public abstract void empty(Augmentations var1) throws XNIException;

    public abstract void any(Augmentations var1) throws XNIException;

    public abstract void startContentModel(String var1, Augmentations var2) throws XNIException;
}

