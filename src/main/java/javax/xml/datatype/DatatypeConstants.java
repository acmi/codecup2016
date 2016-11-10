/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.datatype;

import javax.xml.namespace.QName;

public final class DatatypeConstants {
    public static final Field YEARS = new Field("YEARS", 0, null);
    public static final Field MONTHS = new Field("MONTHS", 1, null);
    public static final Field DAYS = new Field("DAYS", 2, null);
    public static final Field HOURS = new Field("HOURS", 3, null);
    public static final Field MINUTES = new Field("MINUTES", 4, null);
    public static final Field SECONDS = new Field("SECONDS", 5, null);
    public static final QName DATETIME = new QName("http://www.w3.org/2001/XMLSchema", "dateTime");
    public static final QName TIME = new QName("http://www.w3.org/2001/XMLSchema", "time");
    public static final QName DATE = new QName("http://www.w3.org/2001/XMLSchema", "date");
    public static final QName GYEARMONTH = new QName("http://www.w3.org/2001/XMLSchema", "gYearMonth");
    public static final QName GMONTHDAY = new QName("http://www.w3.org/2001/XMLSchema", "gMonthDay");
    public static final QName GYEAR = new QName("http://www.w3.org/2001/XMLSchema", "gYear");
    public static final QName GMONTH = new QName("http://www.w3.org/2001/XMLSchema", "gMonth");
    public static final QName GDAY = new QName("http://www.w3.org/2001/XMLSchema", "gDay");
    public static final QName DURATION = new QName("http://www.w3.org/2001/XMLSchema", "duration");
    public static final QName DURATION_DAYTIME = new QName("http://www.w3.org/2003/11/xpath-datatypes", "dayTimeDuration");
    public static final QName DURATION_YEARMONTH = new QName("http://www.w3.org/2003/11/xpath-datatypes", "yearMonthDuration");

    class 1 {
    }

    public static final class Field {
        private final String str;
        private final int id;

        private Field(String string, int n2) {
            this.str = string;
            this.id = n2;
        }

        public String toString() {
            return this.str;
        }

        Field(String string, int n2, 1 var3_3) {
            this(string, n2);
        }
    }

}

