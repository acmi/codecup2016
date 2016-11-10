/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.compiler;

import java.util.Hashtable;

public class Keywords {
    private static Hashtable m_keywords = new Hashtable();
    private static Hashtable m_axisnames = new Hashtable();
    private static Hashtable m_nodetests = new Hashtable();
    private static Hashtable m_nodetypes = new Hashtable();

    static Object getAxisName(String string) {
        return m_axisnames.get(string);
    }

    static Object lookupNodeTest(String string) {
        return m_nodetests.get(string);
    }

    static Object getKeyWord(String string) {
        return m_keywords.get(string);
    }

    static Object getNodeType(String string) {
        return m_nodetypes.get(string);
    }

    static {
        m_axisnames.put("ancestor", new Integer(37));
        m_axisnames.put("ancestor-or-self", new Integer(38));
        m_axisnames.put("attribute", new Integer(39));
        m_axisnames.put("child", new Integer(40));
        m_axisnames.put("descendant", new Integer(41));
        m_axisnames.put("descendant-or-self", new Integer(42));
        m_axisnames.put("following", new Integer(43));
        m_axisnames.put("following-sibling", new Integer(44));
        m_axisnames.put("parent", new Integer(45));
        m_axisnames.put("preceding", new Integer(46));
        m_axisnames.put("preceding-sibling", new Integer(47));
        m_axisnames.put("self", new Integer(48));
        m_axisnames.put("namespace", new Integer(49));
        m_nodetypes.put("comment", new Integer(1030));
        m_nodetypes.put("text", new Integer(1031));
        m_nodetypes.put("processing-instruction", new Integer(1032));
        m_nodetypes.put("node", new Integer(1033));
        m_nodetypes.put("*", new Integer(36));
        m_keywords.put(".", new Integer(48));
        m_keywords.put("id", new Integer(4));
        m_keywords.put("key", new Integer(5));
        m_nodetests.put("comment", new Integer(1030));
        m_nodetests.put("text", new Integer(1031));
        m_nodetests.put("processing-instruction", new Integer(1032));
        m_nodetests.put("node", new Integer(1033));
    }
}

