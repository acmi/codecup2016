/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsCompositeDriver;
import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsModel;
import de.schlichtherle.truezip.fs.FsMountPoint;
import de.schlichtherle.truezip.fs.FsNeedsWriteLockException;
import de.schlichtherle.truezip.fs.FsSyncException;
import de.schlichtherle.truezip.fs.FsSyncOption;
import de.schlichtherle.truezip.fs.FsSyncShutdownHook;
import de.schlichtherle.truezip.util.BitField;
import de.schlichtherle.truezip.util.Link;
import de.schlichtherle.truezip.util.Links;
import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class FsDefaultManager
extends FsManager {
    private final Map<FsMountPoint, Link<FsController<?>>> controllers = new WeakHashMap();
    private final Link.Type optionalScheduleType;
    private final ReentrantReadWriteLock.ReadLock readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    public FsDefaultManager() {
        this(Link.Type.WEAK);
    }

    FsDefaultManager(Link.Type type) {
        assert (null != type);
        this.optionalScheduleType = type;
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.readLock = reentrantReadWriteLock.readLock();
        this.writeLock = reentrantReadWriteLock.writeLock();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public FsController<?> getController(FsMountPoint fsMountPoint, FsCompositeDriver fsCompositeDriver) {
        this.readLock.lock();
        try {
            FsController fsController = this.getController0(fsMountPoint, fsCompositeDriver);
            this.readLock.unlock();
            return fsController;
        }
        catch (Throwable throwable) {
            try {
                this.readLock.unlock();
                throw throwable;
            }
            catch (FsNeedsWriteLockException fsNeedsWriteLockException) {
                this.writeLock.lock();
                try {
                    FsController fsController = this.getController0(fsMountPoint, fsCompositeDriver);
                    return fsController;
                }
                finally {
                    this.writeLock.unlock();
                }
            }
        }
    }

    private FsController<?> getController0(FsMountPoint fsMountPoint, FsCompositeDriver fsCompositeDriver) {
        FsController fsController = Links.getTarget(this.controllers.get(fsMountPoint));
        if (null != fsController) {
            return fsController;
        }
        if (!this.writeLock.isHeldByCurrentThread()) {
            throw FsNeedsWriteLockException.get();
        }
        FsMountPoint fsMountPoint2 = fsMountPoint.getParent();
        FsController fsController2 = null == fsMountPoint2 ? null : this.getController0(fsMountPoint2, fsCompositeDriver);
        ManagedModel managedModel = new ManagedModel(fsMountPoint, null == fsController2 ? null : (FsModel)fsController2.getModel());
        fsController = fsCompositeDriver.newController(this, managedModel, fsController2);
        managedModel.init(fsController);
        return fsController;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getSize() {
        this.readLock.lock();
        try {
            int n2 = this.controllers.size();
            return n2;
        }
        finally {
            this.readLock.unlock();
        }
    }

    @Override
    public Iterator<FsController<?>> iterator() {
        return this.sortedControllers().iterator();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Set<FsController<?>> sortedControllers() {
        this.readLock.lock();
        try {
            TreeSet treeSet = new TreeSet(ReverseControllerComparator.INSTANCE);
            for (Link link : this.controllers.values()) {
                FsController fsController = Links.getTarget(link);
                if (null == fsController) continue;
                treeSet.add(fsController);
            }
            TreeSet treeSet2 = treeSet;
            return treeSet2;
        }
        finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void sync(BitField<FsSyncOption> bitField) throws FsSyncException {
        FsSyncShutdownHook.cancel();
        super.sync(bitField);
    }

    private static final class ReverseControllerComparator
    implements Comparator<FsController<?>> {
        static final ReverseControllerComparator INSTANCE = new ReverseControllerComparator();

        private ReverseControllerComparator() {
        }

        @Override
        public int compare(FsController<?> fsController, FsController<?> fsController2) {
            return fsController2.getModel().getMountPoint().toHierarchicalUri().compareTo(fsController.getModel().getMountPoint().toHierarchicalUri());
        }
    }

    private final class ManagedModel
    extends FsModel {
        FsController<?> controller;
        volatile boolean mounted;

        ManagedModel(FsMountPoint fsMountPoint, FsModel fsModel) {
            super(fsMountPoint, fsModel);
        }

        void init(FsController<? extends FsModel> fsController) {
            assert (null != fsController);
            assert (!this.mounted);
            this.controller = fsController;
            this.schedule(false);
        }

        @Override
        public boolean isMounted() {
            return this.mounted;
        }

        void schedule(boolean bl) {
            assert (FsDefaultManager.this.writeLock.isHeldByCurrentThread());
            Link.Type type = bl ? Link.Type.STRONG : FsDefaultManager.this.optionalScheduleType;
            FsDefaultManager.this.controllers.put(this.getMountPoint(), type.newLink(this.controller));
        }
    }

}

