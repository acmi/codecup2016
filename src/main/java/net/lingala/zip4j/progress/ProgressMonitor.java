/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.progress;

import net.lingala.zip4j.exception.ZipException;

public class ProgressMonitor {
    private int state;
    private long totalWork;
    private long workCompleted;
    private int percentDone;
    private int currentOperation;
    private String fileName;
    private int result;
    private Throwable exception;
    private boolean cancelAllTasks;
    private boolean pause;

    public ProgressMonitor() {
        this.reset();
        this.percentDone = 0;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int n2) {
        this.state = n2;
    }

    public void setTotalWork(long l2) {
        this.totalWork = l2;
    }

    public void updateWorkCompleted(long l2) {
        this.workCompleted += l2;
        if (this.totalWork > 0) {
            this.percentDone = (int)(this.workCompleted * 100 / this.totalWork);
            if (this.percentDone > 100) {
                this.percentDone = 100;
            }
        }
        while (this.pause) {
            try {
                Thread.sleep(150);
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    public void setPercentDone(int n2) {
        this.percentDone = n2;
    }

    public void setResult(int n2) {
        this.result = n2;
    }

    public void setFileName(String string) {
        this.fileName = string;
    }

    public void setCurrentOperation(int n2) {
        this.currentOperation = n2;
    }

    public void endProgressMonitorSuccess() throws ZipException {
        this.reset();
        this.result = 0;
    }

    public void endProgressMonitorError(Throwable throwable) throws ZipException {
        this.reset();
        this.result = 2;
        this.exception = throwable;
    }

    public void reset() {
        this.currentOperation = -1;
        this.state = 0;
        this.fileName = null;
        this.totalWork = 0;
        this.workCompleted = 0;
        this.percentDone = 0;
    }

    public boolean isCancelAllTasks() {
        return this.cancelAllTasks;
    }
}

