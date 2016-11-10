/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import org.apache.xml.serialize.HTMLSerializer;
import org.apache.xml.serialize.OutputFormat;

public class XHTMLSerializer
extends HTMLSerializer {
    public XHTMLSerializer() {
        super(true, new OutputFormat("xhtml", null, false));
    }

    public XHTMLSerializer(OutputFormat outputFormat) {
        super(true, outputFormat != null ? outputFormat : new OutputFormat("xhtml", null, false));
    }
}

