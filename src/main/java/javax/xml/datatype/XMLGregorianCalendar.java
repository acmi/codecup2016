/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;

public abstract class XMLGregorianCalendar
implements Cloneable {
    public abstract void clear();

    public abstract void reset();

    public abstract void setYear(BigInteger var1);

    public abstract void setYear(int var1);

    public abstract void setMonth(int var1);

    public abstract void setDay(int var1);

    public abstract void setTimezone(int var1);

    public void setTime(int n2, int n3, int n4) {
        this.setTime(n2, n3, n4, null);
    }

    public abstract void setHour(int var1);

    public abstract void setMinute(int var1);

    public abstract void setSecond(int var1);

    public abstract void setMillisecond(int var1);

    public abstract void setFractionalSecond(BigDecimal var1);

    public void setTime(int n2, int n3, int n4, BigDecimal bigDecimal) {
        this.setHour(n2);
        this.setMinute(n3);
        this.setSecond(n4);
        this.setFractionalSecond(bigDecimal);
    }

    public void setTime(int n2, int n3, int n4, int n5) {
        this.setHour(n2);
        this.setMinute(n3);
        this.setSecond(n4);
        this.setMillisecond(n5);
    }

    public abstract BigInteger getEon();

    public abstract int getYear();

    public abstract BigInteger getEonAndYear();

    public abstract int getMonth();

    public abstract int getDay();

    public abstract int getTimezone();

    public abstract int getHour();

    public abstract int getMinute();

    public abstract int getSecond();

    public int getMillisecond() {
        BigDecimal bigDecimal = this.getFractionalSecond();
        if (bigDecimal == null) {
            return Integer.MIN_VALUE;
        }
        return this.getFractionalSecond().movePointRight(3).intValue();
    }

    public abstract BigDecimal getFractionalSecond();

    public abstract int compare(XMLGregorianCalendar var1);

    public abstract XMLGregorianCalendar normalize();

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof XMLGregorianCalendar) {
            return this.compare((XMLGregorianCalendar)object) == 0;
        }
        return false;
    }

    public int hashCode() {
        int n2 = this.getTimezone();
        if (n2 == Integer.MIN_VALUE) {
            n2 = 0;
        }
        XMLGregorianCalendar xMLGregorianCalendar = this;
        if (n2 != 0) {
            xMLGregorianCalendar = this.normalize();
        }
        return xMLGregorianCalendar.getYear() + xMLGregorianCalendar.getMonth() + xMLGregorianCalendar.getDay() + xMLGregorianCalendar.getHour() + xMLGregorianCalendar.getMinute() + xMLGregorianCalendar.getSecond();
    }

    public abstract String toXMLFormat();

    public abstract QName getXMLSchemaType();

    public String toString() {
        return this.toXMLFormat();
    }

    public abstract boolean isValid();

    public abstract void add(Duration var1);

    public abstract GregorianCalendar toGregorianCalendar();

    public abstract GregorianCalendar toGregorianCalendar(TimeZone var1, Locale var2, XMLGregorianCalendar var3);

    public abstract TimeZone getTimeZone(int var1);

    public abstract Object clone();
}
