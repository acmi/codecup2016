/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public abstract class Hashtree2Node {
    public static void appendHashToNode(Hashtable hashtable, String string, Node node, Document document) {
        if (null == node || null == document || null == hashtable) {
            return;
        }
        String string2 = null;
        string2 = null == string || "".equals(string) ? "appendHashToNode" : string;
        try {
            Object object;
            Object object2;
            String string3;
            Element element = document.createElement(string2);
            node.appendChild(element);
            Enumeration enumeration = hashtable.keys();
            ArrayList<Object> arrayList = new ArrayList<Object>();
            while (enumeration.hasMoreElements()) {
                object = enumeration.nextElement();
                string3 = object.toString();
                object2 = hashtable.get(object);
                if (object2 instanceof Hashtable) {
                    arrayList.add(string3);
                    arrayList.add((Hashtable)object2);
                    continue;
                }
                try {
                    Element element2 = document.createElement("item");
                    element2.setAttribute("key", string3);
                    element2.appendChild(document.createTextNode((String)object2));
                    element.appendChild(element2);
                }
                catch (Exception exception) {
                    Element element3 = document.createElement("item");
                    element3.setAttribute("key", string3);
                    element3.appendChild(document.createTextNode("ERROR: Reading " + object + " threw: " + exception.toString()));
                    element.appendChild(element3);
                }
            }
            object = arrayList.iterator();
            while (object.hasNext()) {
                string3 = (String)object.next();
                object2 = (Hashtable)object.next();
                Hashtree2Node.appendHashToNode((Hashtable)object2, string3, element, document);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

