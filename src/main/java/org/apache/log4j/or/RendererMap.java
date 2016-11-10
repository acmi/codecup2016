/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.or;

import java.util.Hashtable;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.or.DefaultRenderer;
import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.spi.RendererSupport;

public class RendererMap {
    Hashtable map = new Hashtable();
    static ObjectRenderer defaultRenderer = new DefaultRenderer();
    static Class class$org$apache$log4j$or$ObjectRenderer;

    public static void addRenderer(RendererSupport rendererSupport, String string, String string2) {
        LogLog.debug("Rendering class: [" + string2 + "], Rendered class: [" + string + "].");
        Class class_ = class$org$apache$log4j$or$ObjectRenderer == null ? (RendererMap.class$org$apache$log4j$or$ObjectRenderer = RendererMap.class$("org.apache.log4j.or.ObjectRenderer")) : class$org$apache$log4j$or$ObjectRenderer;
        ObjectRenderer objectRenderer = (ObjectRenderer)OptionConverter.instantiateByClassName(string2, class_, null);
        if (objectRenderer == null) {
            LogLog.error("Could not instantiate renderer [" + string2 + "].");
            return;
        }
        try {
            Class class_2 = Loader.loadClass(string);
            rendererSupport.setRenderer(class_2, objectRenderer);
        }
        catch (ClassNotFoundException classNotFoundException) {
            LogLog.error("Could not find class [" + string + "].", classNotFoundException);
        }
    }

    public void clear() {
        this.map.clear();
    }

    public void put(Class class_, ObjectRenderer objectRenderer) {
        this.map.put(class_, objectRenderer);
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError().initCause(classNotFoundException);
        }
    }
}

