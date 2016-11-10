/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.dom;

import java.util.Vector;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom3.as.ASAttributeDeclaration;
import org.apache.xerces.dom3.as.ASContentModel;
import org.apache.xerces.dom3.as.ASElementDeclaration;
import org.apache.xerces.dom3.as.ASEntityDeclaration;
import org.apache.xerces.dom3.as.ASModel;
import org.apache.xerces.dom3.as.ASNamedObjectMap;
import org.apache.xerces.dom3.as.ASNotationDeclaration;
import org.apache.xerces.dom3.as.ASObject;
import org.apache.xerces.dom3.as.ASObjectList;
import org.apache.xerces.dom3.as.DOMASException;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.w3c.dom.DOMException;

public class ASModelImpl
implements ASModel {
    boolean fNamespaceAware = true;
    protected Vector fASModels = new Vector();
    protected SchemaGrammar fGrammar = null;

    public ASModelImpl() {
    }

    public ASModelImpl(boolean bl) {
        this.fNamespaceAware = bl;
    }

    public short getAsNodeType() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public ASModel getOwnerASModel() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void setOwnerASModel(ASModel aSModel) {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public String getNodeName() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void setNodeName(String string) {
        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string2);
    }

    public String getPrefix() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void setPrefix(String string) {
        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string2);
    }

    public String getLocalName() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void setLocalName(String string) {
        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string2);
    }

    public String getNamespaceURI() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void setNamespaceURI(String string) {
        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string2);
    }

    public ASObject cloneASObject(boolean bl) {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public boolean getIsNamespaceAware() {
        return this.fNamespaceAware;
    }

    public short getUsageLocation() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public String getAsLocation() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void setAsLocation(String string) {
        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string2);
    }

    public String getAsHint() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void setAsHint(String string) {
        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string2);
    }

    public boolean getContainer() {
        return this.fGrammar != null;
    }

    public ASNamedObjectMap getElementDeclarations() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public ASNamedObjectMap getAttributeDeclarations() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public ASNamedObjectMap getNotationDeclarations() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public ASNamedObjectMap getEntityDeclarations() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public ASNamedObjectMap getContentModelDeclarations() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void addASModel(ASModel aSModel) {
        this.fASModels.addElement(aSModel);
    }

    public ASObjectList getASModels() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void removeAS(ASModel aSModel) {
        this.fASModels.removeElement(aSModel);
    }

    public boolean validate() {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void importASObject(ASObject aSObject) {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public void insertASObject(ASObject aSObject) {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public ASElementDeclaration createASElementDeclaration(String string, String string2) throws DOMException {
        String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string3);
    }

    public ASAttributeDeclaration createASAttributeDeclaration(String string, String string2) throws DOMException {
        String string3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string3);
    }

    public ASNotationDeclaration createASNotationDeclaration(String string, String string2, String string3, String string4) throws DOMException {
        String string5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string5);
    }

    public ASEntityDeclaration createASEntityDeclaration(String string) throws DOMException {
        String string2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string2);
    }

    public ASContentModel createASContentModel(int n2, int n3, short s2) throws DOMASException {
        String string = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException(9, string);
    }

    public SchemaGrammar getGrammar() {
        return this.fGrammar;
    }

    public void setGrammar(SchemaGrammar schemaGrammar) {
        this.fGrammar = schemaGrammar;
    }

    public Vector getInternalASModels() {
        return this.fASModels;
    }
}

