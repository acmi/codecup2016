/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.jaxp.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.jaxp.datatype.DurationImpl;
import org.apache.xerces.jaxp.datatype.XMLGregorianCalendarImpl;

public class DatatypeFactoryImpl
extends DatatypeFactory {
    public Duration newDuration(String string) {
        return new DurationImpl(string);
    }

    public Duration newDuration(long l2) {
        return new DurationImpl(l2);
    }

    public Duration newDuration(boolean bl, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigDecimal bigDecimal) {
        return new DurationImpl(bl, bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5, bigDecimal);
    }

    public XMLGregorianCalendar newXMLGregorianCalendar() {
        return new XMLGregorianCalendarImpl();
    }

    public XMLGregorianCalendar newXMLGregorianCalendar(String string) {
        return new XMLGregorianCalendarImpl(string);
    }

    public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar gregorianCalendar) {
        return new XMLGregorianCalendarImpl(gregorianCalendar);
    }

    public XMLGregorianCalendar newXMLGregorianCalendar(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        return XMLGregorianCalendarImpl.createDateTime(n2, n3, n4, n5, n6, n7, n8, n9);
    }

    public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger bigInteger, int n2, int n3, int n4, int n5, int n6, BigDecimal bigDecimal, int n7) {
        return new XMLGregorianCalendarImpl(bigInteger, n2, n3, n4, n5, n6, bigDecimal, n7);
    }
}

