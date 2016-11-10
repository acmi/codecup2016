/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;

final class SortSettings {
    private AbstractTranslet _translet;
    private int[] _sortOrders;
    private int[] _types;
    private Locale[] _locales;
    private Collator[] _collators;
    private String[] _caseOrders;

    SortSettings(AbstractTranslet abstractTranslet, int[] arrn, int[] arrn2, Locale[] arrlocale, Collator[] arrcollator, String[] arrstring) {
        this._translet = abstractTranslet;
        this._sortOrders = arrn;
        this._types = arrn2;
        this._locales = arrlocale;
        this._collators = arrcollator;
        this._caseOrders = arrstring;
    }

    AbstractTranslet getTranslet() {
        return this._translet;
    }

    int[] getSortOrders() {
        return this._sortOrders;
    }

    int[] getTypes() {
        return this._types;
    }

    Locale[] getLocales() {
        return this._locales;
    }

    Collator[] getCollators() {
        return this._collators;
    }

    String[] getCaseOrders() {
        return this._caseOrders;
    }
}

