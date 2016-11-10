/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.stream.events;

import java.util.List;
import javax.xml.stream.events.XMLEvent;

public interface DTD
extends XMLEvent {
    public String getDocumentTypeDeclaration();

    public List getEntities();
}

