/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.fs;

import de.schlichtherle.truezip.fs.FsController;
import de.schlichtherle.truezip.fs.FsDecoratingManager;
import de.schlichtherle.truezip.fs.FsManager;
import de.schlichtherle.truezip.fs.FsMountPoint;
import de.schlichtherle.truezip.util.FilteringIterator;
import java.net.URI;
import java.util.Iterator;

public final class FsFilteringManager
extends FsDecoratingManager<FsManager> {
    private final URI prefix;

    public FsFilteringManager(FsManager fsManager, FsMountPoint fsMountPoint) {
        super(fsManager);
        this.prefix = fsMountPoint.toHierarchicalUri();
    }

    @Override
    public int getSize() {
        int n2 = 0;
        for (FsController fsController : this) {
            ++n2;
        }
        return n2;
    }

    @Override
    public Iterator<FsController<?>> iterator() {
        return new FilteredControllerIterator();
    }

    private final class FilteredControllerIterator
    extends FilteringIterator<FsController<?>> {
        final String ps;
        final String pp;
        final int ppl;
        final boolean pps;

        FilteredControllerIterator() {
            super(FsFilteringManager.this.delegate.iterator());
            this.ps = FsFilteringManager.this.prefix.getScheme();
            this.pp = FsFilteringManager.this.prefix.getPath();
            this.ppl = this.pp.length();
            this.pps = '/' == this.pp.charAt(this.ppl - 1);
        }

        @Override
        protected boolean accept(FsController<?> fsController) {
            String string;
            assert (null != fsController);
            URI uRI = fsController.getModel().getMountPoint().toHierarchicalUri();
            return uRI.getScheme().equals(this.ps) && (string = uRI.getPath()).startsWith(this.pp) && (this.pps || string.length() == this.ppl || '/' == string.charAt(this.ppl));
        }
    }

}

