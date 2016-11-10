/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serializer;

import java.util.Vector;

interface XSLOutputAttributes {
    public String getDoctypePublic();

    public String getDoctypeSystem();

    public String getEncoding();

    public String getMediaType();

    public boolean getOmitXMLDeclaration();

    public String getStandalone();

    public String getVersion();

    public void setCdataSectionElements(Vector var1);

    public void setDoctype(String var1, String var2);

    public void setDoctypePublic(String var1);

    public void setDoctypeSystem(String var1);

    public void setEncoding(String var1);

    public void setIndent(boolean var1);

    public void setMediaType(String var1);

    public void setOmitXMLDeclaration(boolean var1);

    public void setStandalone(String var1);

    public void setVersion(String var1);
}

