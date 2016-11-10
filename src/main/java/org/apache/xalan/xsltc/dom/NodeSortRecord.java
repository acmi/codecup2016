/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;
import org.apache.xalan.xsltc.CollatorFactory;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.ObjectFactory;
import org.apache.xalan.xsltc.dom.SortSettings;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.utils.StringComparable;

public abstract class NodeSortRecord {
    public static final int COMPARE_STRING = 0;
    public static final int COMPARE_NUMERIC = 1;
    public static final int COMPARE_ASCENDING = 0;
    public static final int COMPARE_DESCENDING = 1;
    private static final Collator DEFAULT_COLLATOR = Collator.getInstance();
    protected Collator _collator = DEFAULT_COLLATOR;
    protected Collator[] _collators;
    protected Locale _locale;
    protected CollatorFactory _collatorFactory;
    protected SortSettings _settings;
    private DOM _dom = null;
    private int _node;
    private int _last = 0;
    private int _scanned = 0;
    private Object[] _values;

    public NodeSortRecord(int n2) {
        this._node = n2;
    }

    public NodeSortRecord() {
        this(0);
    }

    public final void initialize(int n2, int n3, DOM dOM, SortSettings sortSettings) throws TransletException {
        this._dom = dOM;
        this._node = n2;
        this._last = n3;
        this._settings = sortSettings;
        int n4 = sortSettings.getSortOrders().length;
        this._values = new Object[n4];
        String string = System.getProperty("org.apache.xalan.xsltc.COLLATOR_FACTORY");
        if (string != null) {
            Locale[] arrlocale;
            try {
                arrlocale = ObjectFactory.findProviderClass(string, ObjectFactory.findClassLoader(), true);
                this._collatorFactory = (CollatorFactory)arrlocale;
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new TransletException(classNotFoundException);
            }
            arrlocale = sortSettings.getLocales();
            this._collators = new Collator[n4];
            for (int i2 = 0; i2 < n4; ++i2) {
                this._collators[i2] = this._collatorFactory.getCollator(arrlocale[i2]);
            }
            this._collator = this._collators[0];
        } else {
            this._collators = sortSettings.getCollators();
            this._collator = this._collators[0];
        }
    }

    public final int getNode() {
        return this._node;
    }

    public final int compareDocOrder(NodeSortRecord nodeSortRecord) {
        return this._node - nodeSortRecord._node;
    }

    private final Comparable stringValue(int n2) {
        if (this._scanned <= n2) {
            AbstractTranslet abstractTranslet = this._settings.getTranslet();
            Locale[] arrlocale = this._settings.getLocales();
            String[] arrstring = this._settings.getCaseOrders();
            String string = this.extractValueFromDOM(this._dom, this._node, n2, abstractTranslet, this._last);
            Comparable comparable = StringComparable.getComparator(string, arrlocale[n2], this._collators[n2], arrstring[n2]);
            this._values[this._scanned++] = comparable;
            return comparable;
        }
        return (Comparable)this._values[n2];
    }

    private final Double numericValue(int n2) {
        if (this._scanned <= n2) {
            Double d2;
            AbstractTranslet abstractTranslet = this._settings.getTranslet();
            String string = this.extractValueFromDOM(this._dom, this._node, n2, abstractTranslet, this._last);
            try {
                d2 = new Double(string);
            }
            catch (NumberFormatException numberFormatException) {
                d2 = new Double(Double.NEGATIVE_INFINITY);
            }
            this._values[this._scanned++] = d2;
            return d2;
        }
        return (Double)this._values[n2];
    }

    public int compareTo(NodeSortRecord nodeSortRecord) {
        int[] arrn = this._settings.getSortOrders();
        int n2 = this._settings.getSortOrders().length;
        int[] arrn2 = this._settings.getTypes();
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            Double d3;
            if (arrn2[i2] == 1) {
                d3 = this.numericValue(i2);
                Double d2 = nodeSortRecord.numericValue(i2);
                n3 = d3.compareTo(d2);
            } else {
                d3 = this.stringValue(i2);
                Comparable comparable = nodeSortRecord.stringValue(i2);
                n3 = d3.compareTo(comparable);
            }
            if (n3 == 0) continue;
            return arrn[i2] == 1 ? 0 - n3 : n3;
        }
        return this._node - nodeSortRecord._node;
    }

    public Collator[] getCollator() {
        return this._collators;
    }

    public abstract String extractValueFromDOM(DOM var1, int var2, int var3, AbstractTranslet var4, int var5);
}

