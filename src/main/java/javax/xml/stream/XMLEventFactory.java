/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.FactoryFinder;
import javax.xml.stream.Location;
import javax.xml.stream.SecuritySupport;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

public abstract class XMLEventFactory {
    private static final String PROPERTY_NAME = "javax.xml.stream.XMLEventFactory";
    private static final String DEFAULT_FACTORY = "org.apache.xerces.stax.XMLEventFactoryImpl";

    protected XMLEventFactory() {
    }

    public static XMLEventFactory newInstance() throws FactoryConfigurationError {
        try {
            return (XMLEventFactory)FactoryFinder.find("javax.xml.stream.XMLEventFactory", "org.apache.xerces.stax.XMLEventFactoryImpl");
        }
        catch (FactoryFinder.ConfigurationError configurationError) {
            throw new FactoryConfigurationError(configurationError.getException(), configurationError.getMessage());
        }
    }

    public static XMLEventFactory newInstance(String string, ClassLoader classLoader) throws FactoryConfigurationError {
        if (classLoader == null) {
            classLoader = SecuritySupport.getContextClassLoader();
        }
        try {
            return (XMLEventFactory)FactoryFinder.find(string, classLoader, "org.apache.xerces.stax.XMLEventFactoryImpl");
        }
        catch (FactoryFinder.ConfigurationError configurationError) {
            throw new FactoryConfigurationError(configurationError.getException(), configurationError.getMessage());
        }
    }

    public abstract void setLocation(Location var1);

    public abstract Attribute createAttribute(QName var1, String var2);

    public abstract Attribute createAttribute(String var1, String var2);

    public abstract Attribute createAttribute(String var1, String var2, String var3, String var4);

    public abstract Namespace createNamespace(String var1);

    public abstract Namespace createNamespace(String var1, String var2);

    public abstract StartElement createStartElement(QName var1, Iterator var2, Iterator var3);

    public abstract StartElement createStartElement(String var1, String var2, String var3);

    public abstract StartElement createStartElement(String var1, String var2, String var3, Iterator var4, Iterator var5);

    public abstract StartElement createStartElement(String var1, String var2, String var3, Iterator var4, Iterator var5, NamespaceContext var6);

    public abstract EndElement createEndElement(QName var1, Iterator var2);

    public abstract EndElement createEndElement(String var1, String var2, String var3);

    public abstract EndElement createEndElement(String var1, String var2, String var3, Iterator var4);

    public abstract Characters createCharacters(String var1);

    public abstract Characters createCData(String var1);

    public abstract Characters createSpace(String var1);

    public abstract Characters createIgnorableSpace(String var1);

    public abstract StartDocument createStartDocument();

    public abstract StartDocument createStartDocument(String var1);

    public abstract StartDocument createStartDocument(String var1, String var2);

    public abstract StartDocument createStartDocument(String var1, String var2, boolean var3);

    public abstract EndDocument createEndDocument();

    public abstract EntityReference createEntityReference(String var1, EntityDeclaration var2);

    public abstract Comment createComment(String var1);

    public abstract ProcessingInstruction createProcessingInstruction(String var1, String var2);

    public abstract DTD createDTD(String var1);
}
