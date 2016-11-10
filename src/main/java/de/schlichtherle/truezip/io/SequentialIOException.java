/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.io;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Comparator;

public class SequentialIOException
extends IOException
implements Cloneable {
    private static int maxPrintExceptions = 3;
    static final Comparator<SequentialIOException> INDEX_COMP = new Comparator<SequentialIOException>(){

        @Override
        public int compare(SequentialIOException sequentialIOException, SequentialIOException sequentialIOException2) {
            return sequentialIOException.index - sequentialIOException2.index;
        }
    };
    static final Comparator<SequentialIOException> PRIORITY_COMP = new Comparator<SequentialIOException>(){

        @Override
        public int compare(SequentialIOException sequentialIOException, SequentialIOException sequentialIOException2) {
            int n2;
            int n3 = sequentialIOException.priority;
            return n3 < (n2 = sequentialIOException2.priority) ? -1 : (n3 > n2 ? 1 : SequentialIOException.INDEX_COMP.compare(sequentialIOException, sequentialIOException2));
        }
    };
    private SequentialIOException predecessor;
    private final int priority;
    private int index;
    int maxIndex;

    public SequentialIOException() {
        this(null, 0);
    }

    public SequentialIOException(String string, int n2) {
        super(string);
        this.predecessor = this;
        this.priority = n2;
    }

    public SequentialIOException clone() {
        try {
            return (SequentialIOException)Object.super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError(cloneNotSupportedException);
        }
    }

    @Override
    public SequentialIOException initCause(Throwable throwable) {
        super.initCause(throwable);
        return this;
    }

    public synchronized SequentialIOException initPredecessor(SequentialIOException sequentialIOException) {
        this.setPredecessor(sequentialIOException);
        sequentialIOException = this.getPredecessor();
        if (sequentialIOException != null) {
            this.index = this.maxIndex = sequentialIOException.maxIndex + 1;
        }
        return this;
    }

    private void setPredecessor(SequentialIOException sequentialIOException) {
        if (this.isInitPredecessor()) {
            if (this.predecessor == sequentialIOException) {
                return;
            }
            throw new IllegalStateException("Cannot overwrite predecessor!");
        }
        if (sequentialIOException == this) {
            throw new IllegalArgumentException("Cannot be predecessor of myself!");
        }
        if (null != sequentialIOException && !sequentialIOException.isInitPredecessor()) {
            throw new IllegalArgumentException("The predecessor's predecessor must be initialized in order to inhibit loops!");
        }
        this.predecessor = sequentialIOException;
    }

    public final synchronized SequentialIOException getPredecessor() {
        return this.isInitPredecessor() ? this.predecessor : null;
    }

    final boolean isInitPredecessor() {
        return this.predecessor != this;
    }

    public SequentialIOException sortPriority() {
        return this.sort(PRIORITY_COMP);
    }

    private SequentialIOException sort(Comparator<SequentialIOException> comparator) {
        SequentialIOException sequentialIOException = this.getPredecessor();
        if (null == sequentialIOException) {
            return this;
        }
        SequentialIOException sequentialIOException2 = sequentialIOException.sort(comparator);
        if (sequentialIOException2 == sequentialIOException && comparator.compare(this, sequentialIOException2) >= 0) {
            return this;
        }
        return sequentialIOException2.insert(this.clone(), comparator);
    }

    private SequentialIOException insert(SequentialIOException sequentialIOException, Comparator<SequentialIOException> comparator) {
        if (comparator.compare(sequentialIOException, this) >= 0) {
            sequentialIOException.predecessor = this;
            sequentialIOException.maxIndex = Math.max(sequentialIOException.index, this.maxIndex);
            return sequentialIOException;
        }
        SequentialIOException sequentialIOException2 = this.predecessor;
        assert (sequentialIOException2 != this);
        SequentialIOException sequentialIOException3 = this.clone();
        if (sequentialIOException2 != null) {
            sequentialIOException3.predecessor = sequentialIOException2.insert(sequentialIOException, comparator);
            sequentialIOException3.maxIndex = Math.max(sequentialIOException3.index, sequentialIOException3.predecessor.maxIndex);
        } else {
            sequentialIOException.predecessor = null;
            sequentialIOException3.predecessor = sequentialIOException;
            sequentialIOException3.maxIndex = sequentialIOException.maxIndex;
        }
        return sequentialIOException3;
    }

    @Override
    public void printStackTrace(PrintStream printStream) {
        this.printStackTrace(printStream, SequentialIOException.getMaxPrintExceptions());
    }

    public void printStackTrace(PrintStream printStream, int n2) {
        --n2;
        SequentialIOException sequentialIOException = this.getPredecessor();
        if (null != sequentialIOException) {
            if (n2 > 0) {
                sequentialIOException.printStackTrace(printStream, n2);
                printStream.println("\nFollowed, but not caused by:");
            } else {
                printStream.println("\nOmitting " + sequentialIOException.getNumExceptions() + " more exception(s) at the start of this list!");
            }
        }
        super.printStackTrace(printStream);
    }

    private int getNumExceptions() {
        SequentialIOException sequentialIOException = this.getPredecessor();
        return null != sequentialIOException ? sequentialIOException.getNumExceptions() + 1 : 1;
    }

    @Override
    public void printStackTrace(PrintWriter printWriter) {
        this.printStackTrace(printWriter, SequentialIOException.getMaxPrintExceptions());
    }

    public void printStackTrace(PrintWriter printWriter, int n2) {
        --n2;
        SequentialIOException sequentialIOException = this.getPredecessor();
        if (null != sequentialIOException) {
            if (0 < n2) {
                sequentialIOException.printStackTrace(printWriter, n2);
                printWriter.println("\nFollowed, but not caused by:");
            } else {
                printWriter.println("\nOmitting " + sequentialIOException.getNumExceptions() + " more exception(s) at the start of this list!");
            }
        }
        super.printStackTrace(printWriter);
    }

    public static int getMaxPrintExceptions() {
        return maxPrintExceptions;
    }

}

