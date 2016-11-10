/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.NodeSortRecord;
import org.apache.xalan.xsltc.dom.ObjectFactory;
import org.apache.xalan.xsltc.dom.SortSettings;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.utils.LocaleUtility;

public class NodeSortRecordFactory {
    private static int DESCENDING = "descending".length();
    private static int NUMBER = "number".length();
    private final DOM _dom;
    private final String _className;
    private Class _class;
    private SortSettings _sortSettings;
    protected Collator _collator;

    public NodeSortRecordFactory(DOM dOM, String string, Translet translet, String[] arrstring, String[] arrstring2) throws TransletException {
        this(dOM, string, translet, arrstring, arrstring2, null, null);
    }

    public NodeSortRecordFactory(DOM dOM, String string, Translet translet, String[] arrstring, String[] arrstring2, String[] arrstring3, String[] arrstring4) throws TransletException {
        try {
            int n2;
            this._dom = dOM;
            this._className = string;
            this._class = translet.getAuxiliaryClass(string);
            if (this._class == null) {
                this._class = ObjectFactory.findProviderClass(string, ObjectFactory.findClassLoader(), true);
            }
            int n3 = arrstring.length;
            int[] arrn = new int[n3];
            int[] arrn2 = new int[n3];
            for (int i2 = 0; i2 < n3; ++i2) {
                if (arrstring[i2].length() == DESCENDING) {
                    arrn[i2] = 1;
                }
                if (arrstring2[i2].length() != NUMBER) continue;
                arrn2[i2] = 1;
            }
            String[] arrstring5 = null;
            if (arrstring3 == null || arrstring4 == null) {
                n2 = arrstring.length;
                arrstring5 = new String[n2];
                for (int i3 = 0; i3 < n2; ++i3) {
                    arrstring5[i3] = "";
                }
            }
            if (arrstring3 == null) {
                arrstring3 = arrstring5;
            }
            if (arrstring4 == null) {
                arrstring4 = arrstring5;
            }
            n2 = arrstring3.length;
            Locale[] arrlocale = new Locale[n2];
            Collator[] arrcollator = new Collator[n2];
            for (int i4 = 0; i4 < n2; ++i4) {
                arrlocale[i4] = LocaleUtility.langToLocale(arrstring3[i4]);
                arrcollator[i4] = Collator.getInstance(arrlocale[i4]);
            }
            this._sortSettings = new SortSettings((AbstractTranslet)translet, arrn, arrn2, arrlocale, arrcollator, arrstring4);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new TransletException(classNotFoundException);
        }
    }

    public NodeSortRecord makeNodeSortRecord(int n2, int n3) throws ExceptionInInitializerError, LinkageError, IllegalAccessException, InstantiationException, SecurityException, TransletException {
        NodeSortRecord nodeSortRecord = (NodeSortRecord)this._class.newInstance();
        nodeSortRecord.initialize(n2, n3, this._dom, this._sortSettings);
        return nodeSortRecord;
    }

    public String getClassName() {
        return this._className;
    }

    private final void setLang(String[] arrstring) {
    }
}

