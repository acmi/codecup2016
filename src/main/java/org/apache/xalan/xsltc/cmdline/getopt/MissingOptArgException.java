/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.cmdline.getopt;

import org.apache.xalan.xsltc.cmdline.getopt.GetOptsException;

class MissingOptArgException
extends GetOptsException {
    static final long serialVersionUID = -1972471465394544822L;

    public MissingOptArgException(String string) {
        super(string);
    }
}

