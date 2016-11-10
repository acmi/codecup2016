/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs.sl;

import de.schlichtherle.truezip.fs.FsDriver;
import de.schlichtherle.truezip.fs.FsDriverProvider;
import de.schlichtherle.truezip.fs.FsScheme;
import de.schlichtherle.truezip.fs.spi.FsDriverService;
import de.schlichtherle.truezip.util.HashMaps;
import de.schlichtherle.truezip.util.ServiceLocator;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FsDriverLocator
implements FsDriverProvider {
    public static final FsDriverLocator SINGLETON = new FsDriverLocator();

    private FsDriverLocator() {
    }

    @Override
    public Map<FsScheme, FsDriver> get() {
        return Boot.DRIVERS;
    }

    private static final class Boot {
        static final Map<FsScheme, FsDriver> DRIVERS;

        static {
            FsScheme fsScheme;
            FsDriver fsDriver;
            Object object;
            Logger logger = Logger.getLogger(FsDriverLocator.class.getName(), FsDriverLocator.class.getName());
            Iterator<FsDriverService> iterator = new ServiceLocator(FsDriverLocator.class.getClassLoader()).getServices(FsDriverService.class);
            TreeMap<FsScheme, FsDriver> treeMap = new TreeMap<FsScheme, FsDriver>();
            if (!iterator.hasNext()) {
                logger.log(Level.WARNING, "null", FsDriverService.class);
            }
            while (iterator.hasNext()) {
                object = iterator.next();
                logger.log(Level.CONFIG, "located", object);
                for (Map.Entry entry : object.get().entrySet()) {
                    FsDriver fsDriver2;
                    fsScheme = (FsScheme)entry.getKey();
                    fsDriver = (FsDriver)entry.getValue();
                    if (null == fsScheme || null == fsDriver || null == (fsDriver2 = treeMap.put(fsScheme, fsDriver)) || fsDriver2.getPriority() <= fsDriver.getPriority()) continue;
                    treeMap.put(fsScheme, fsDriver2);
                }
            }
            object = new LinkedHashMap(HashMaps.initialCapacity(treeMap.size()));
            for (Map.Entry entry : treeMap.entrySet()) {
                fsScheme = (FsScheme)entry.getKey();
                fsDriver = (FsDriver)entry.getValue();
                logger.log(Level.CONFIG, "mapping", new Object[]{fsScheme, fsDriver});
                object.put(fsScheme, fsDriver);
            }
            DRIVERS = Collections.unmodifiableMap(object);
        }
    }

}

