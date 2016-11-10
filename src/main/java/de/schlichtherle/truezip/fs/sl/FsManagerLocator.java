/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs.sl;

import de.schlichtherle.truezip.fs.FsDefaultManager;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsManagerProvider;
import de.schlichtherle.truezip.fs.spi.FsManagerDecorator;
import de.schlichtherle.truezip.fs.spi.FsManagerService;
import de.schlichtherle.truezip.util.ServiceLocator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FsManagerLocator
implements FsManagerProvider {
    public static final FsManagerLocator SINGLETON = new FsManagerLocator();

    private FsManagerLocator() {
    }

    @Override
    public FsManager get() {
        return Boot.manager;
    }

    private static final class DefaultManagerService
    extends FsManagerService {
        private DefaultManagerService() {
        }

        @Override
        public FsManager get() {
            return new FsDefaultManager();
        }
    }

    private static final class Boot {
        static final FsManager manager;

        private static FsManager create(ServiceLocator serviceLocator, Logger logger) {
            Object object;
            FsManagerService fsManagerService = serviceLocator.getService(FsManagerService.class, null);
            if (null == fsManagerService) {
                object = serviceLocator.getServices(FsManagerService.class);
                while (object.hasNext()) {
                    int n2;
                    FsManagerService fsManagerService2 = object.next();
                    logger.log(Level.CONFIG, "located", fsManagerService2);
                    if (null == fsManagerService) {
                        fsManagerService = fsManagerService2;
                        continue;
                    }
                    int n3 = fsManagerService.getPriority();
                    if (n3 < (n2 = fsManagerService2.getPriority())) {
                        fsManagerService = fsManagerService2;
                        continue;
                    }
                    if (n3 != n2) continue;
                    logger.log(Level.WARNING, "collision", new Object[]{n3, fsManagerService, fsManagerService2});
                }
            }
            if (null == fsManagerService) {
                fsManagerService = new DefaultManagerService();
            }
            logger.log(Level.CONFIG, "using", fsManagerService);
            object = fsManagerService.get();
            logger.log(Level.CONFIG, "result", object);
            return object;
        }

        private static FsManager decorate(FsManager fsManager, ServiceLocator serviceLocator, Logger logger) {
            ArrayList<FsManagerDecorator> arrayList = new ArrayList<FsManagerDecorator>();
            FsManagerDecorator[] arrfsManagerDecorator = serviceLocator.getServices(FsManagerDecorator.class);
            while (arrfsManagerDecorator.hasNext()) {
                arrayList.add(arrfsManagerDecorator.next());
            }
            arrfsManagerDecorator = arrayList.toArray(new FsManagerDecorator[arrayList.size()]);
            Arrays.sort(arrfsManagerDecorator, new Comparator<FsManagerDecorator>(){

                @Override
                public int compare(FsManagerDecorator fsManagerDecorator, FsManagerDecorator fsManagerDecorator2) {
                    return fsManagerDecorator.getPriority() - fsManagerDecorator2.getPriority();
                }
            });
            for (FsManagerDecorator fsManagerDecorator : arrfsManagerDecorator) {
                logger.log(Level.CONFIG, "decorating", fsManagerDecorator);
                fsManager = fsManagerDecorator.decorate(fsManager);
                logger.log(Level.CONFIG, "result", fsManager);
            }
            return fsManager;
        }

        static {
            Class<FsManagerLocator> class_ = FsManagerLocator.class;
            Logger logger = Logger.getLogger(class_.getName(), class_.getName());
            ServiceLocator serviceLocator = new ServiceLocator(class_.getClassLoader());
            manager = Boot.decorate(Boot.create(serviceLocator, logger), serviceLocator, logger);
        }

    }

}

