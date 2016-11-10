/*
 * Decompiled with CFR 0_119.
 */
package net.lingala.zip4j.exception;

public class ZipException
extends Exception {
    private int code = -1;

    public ZipException() {
    }

    public ZipException(String string) {
        super(string);
    }

    public ZipException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public ZipException(String string, int n2) {
        super(string);
        this.code = n2;
    }

    public ZipException(String string, Throwable throwable, int n2) {
        super(string, throwable);
        this.code = n2;
    }

    public ZipException(Throwable throwable) {
        super(throwable);
    }
}

