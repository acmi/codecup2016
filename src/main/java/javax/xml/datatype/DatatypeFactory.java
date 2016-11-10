/*
 * Decompiled with CFR 0_119.
 */
package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.Duration;
import javax.xml.datatype.FactoryFinder;
import javax.xml.datatype.SecuritySupport;
import javax.xml.datatype.XMLGregorianCalendar;

public abstract class DatatypeFactory {
    public static final String DATATYPEFACTORY_PROPERTY = "javax.xml.datatype.DatatypeFactory";
    public static final String DATATYPEFACTORY_IMPLEMENTATION_CLASS = new String("org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl");

    protected DatatypeFactory() {
    }

    public static DatatypeFactory newInstance() throws DatatypeConfigurationException {
        try {
            return (DatatypeFactory)FactoryFinder.find("javax.xml.datatype.DatatypeFactory", DATATYPEFACTORY_IMPLEMENTATION_CLASS);
        }
        catch (FactoryFinder.ConfigurationError configurationError) {
            throw new DatatypeConfigurationException(configurationError.getMessage(), configurationError.getException());
        }
    }

    public static DatatypeFactory newInstance(String string, ClassLoader classLoader) throws DatatypeConfigurationException {
        if (string == null) {
            throw new DatatypeConfigurationException("factoryClassName cannot be null.");
        }
        if (classLoader == null) {
            classLoader = SecuritySupport.getContextClassLoader();
        }
        try {
            return (DatatypeFactory)FactoryFinder.newInstance(string, classLoader);
        }
        catch (FactoryFinder.ConfigurationError configurationError) {
            throw new DatatypeConfigurationException(configurationError.getMessage(), configurationError.getException());
        }
    }

    public abstract Duration newDuration(String var1);

    public abstract Duration newDuration(long var1);

    public abstract Duration newDuration(boolean var1, BigInteger var2, BigInteger var3, BigInteger var4, BigInteger var5, BigInteger var6, BigDecimal var7);

    public Duration newDuration(boolean bl, int n2, int n3, int n4, int n5, int n6, int n7) {
        BigInteger bigInteger = n2 != Integer.MIN_VALUE ? BigInteger.valueOf(n2) : null;
        BigInteger bigInteger2 = n3 != Integer.MIN_VALUE ? BigInteger.valueOf(n3) : null;
        BigInteger bigInteger3 = n4 != Integer.MIN_VALUE ? BigInteger.valueOf(n4) : null;
        BigInteger bigInteger4 = n5 != Integer.MIN_VALUE ? BigInteger.valueOf(n5) : null;
        BigInteger bigInteger5 = n6 != Integer.MIN_VALUE ? BigInteger.valueOf(n6) : null;
        BigDecimal bigDecimal = n7 != Integer.MIN_VALUE ? BigDecimal.valueOf(n7) : null;
        return this.newDuration(bl, bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5, bigDecimal);
    }

    public Duration newDurationDayTime(String string) {
        if (string == null) {
            throw new NullPointerException("The lexical representation cannot be null.");
        }
        int n2 = string.indexOf(84);
        int n3 = n2 >= 0 ? n2 : string.length();
        int n4 = 0;
        while (n4 < n3) {
            char c2 = string.charAt(n4);
            if (c2 == 'Y' || c2 == 'M') {
                throw new IllegalArgumentException("Invalid dayTimeDuration value: " + string);
            }
            ++n4;
        }
        return this.newDuration(string);
    }

    public Duration newDurationDayTime(long l2) {
        boolean bl;
        long l3 = l2;
        if (l3 == 0) {
            return this.newDuration(true, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0, 0, 0);
        }
        boolean bl2 = false;
        if (l3 < 0) {
            bl = false;
            if (l3 == Long.MIN_VALUE) {
                ++l3;
                bl2 = true;
            }
            l3 *= -1;
        } else {
            bl = true;
        }
        long l4 = l3;
        int n2 = (int)(l4 % 60000);
        if (bl2) {
            ++n2;
        }
        if (n2 % 1000 == 0) {
            int n3 = n2 / 1000;
            int n4 = (int)((l4 /= 60000) % 60);
            int n5 = (int)((l4 /= 60) % 24);
            long l5 = l4 / 24;
            if (l5 <= Integer.MAX_VALUE) {
                return this.newDuration(bl, Integer.MIN_VALUE, Integer.MIN_VALUE, (int)l5, n5, n4, n3);
            }
            return this.newDuration(bl, null, null, BigInteger.valueOf(l5), BigInteger.valueOf(n5), BigInteger.valueOf(n4), BigDecimal.valueOf(n2, 3));
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(n2, 3);
        BigInteger bigInteger = BigInteger.valueOf((l4 /= 60000) % 60);
        BigInteger bigInteger2 = BigInteger.valueOf((l4 /= 60) % 24);
        BigInteger bigInteger3 = BigInteger.valueOf(l4 /= 24);
        return this.newDuration(bl, null, null, bigInteger3, bigInteger2, bigInteger, bigDecimal);
    }

    public Duration newDurationDayTime(boolean bl, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        return this.newDuration(bl, null, null, bigInteger, bigInteger2, bigInteger3, bigInteger4 != null ? new BigDecimal(bigInteger4) : null);
    }

    public Duration newDurationDayTime(boolean bl, int n2, int n3, int n4, int n5) {
        return this.newDuration(bl, Integer.MIN_VALUE, Integer.MIN_VALUE, n2, n3, n4, n5);
    }

    public Duration newDurationYearMonth(String string) {
        if (string == null) {
            throw new NullPointerException("The lexical representation cannot be null.");
        }
        int n2 = string.length();
        int n3 = 0;
        while (n3 < n2) {
            char c2 = string.charAt(n3);
            if (c2 == 'D' || c2 == 'T') {
                throw new IllegalArgumentException("Invalid yearMonthDuration value: " + string);
            }
            ++n3;
        }
        return this.newDuration(string);
    }

    public Duration newDurationYearMonth(long l2) {
        return this.newDuration(l2);
    }

    public Duration newDurationYearMonth(boolean bl, BigInteger bigInteger, BigInteger bigInteger2) {
        return this.newDuration(bl, bigInteger, bigInteger2, null, null, null, null);
    }

    public Duration newDurationYearMonth(boolean bl, int n2, int n3) {
        return this.newDuration(bl, n2, n3, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public abstract XMLGregorianCalendar newXMLGregorianCalendar();

    public abstract XMLGregorianCalendar newXMLGregorianCalendar(String var1);

    public abstract XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar var1);

    public abstract XMLGregorianCalendar newXMLGregorianCalendar(BigInteger var1, int var2, int var3, int var4, int var5, int var6, BigDecimal var7, int var8);

    public XMLGregorianCalendar newXMLGregorianCalendar(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        BigInteger bigInteger = n2 != Integer.MIN_VALUE ? BigInteger.valueOf(n2) : null;
        BigDecimal bigDecimal = null;
        if (n8 != Integer.MIN_VALUE) {
            if (n8 < 0 || n8 > 1000) {
                throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(int year, int month, int day, int hour, int minute, int second, int millisecond, int timezone)with invalid millisecond: " + n8);
            }
            bigDecimal = BigDecimal.valueOf(n8, 3);
        }
        return this.newXMLGregorianCalendar(bigInteger, n3, n4, n5, n6, n7, bigDecimal, n9);
    }

    public XMLGregorianCalendar newXMLGregorianCalendarDate(int n2, int n3, int n4, int n5) {
        return this.newXMLGregorianCalendar(n2, n3, n4, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, n5);
    }

    public XMLGregorianCalendar newXMLGregorianCalendarTime(int n2, int n3, int n4, int n5) {
        return this.newXMLGregorianCalendar(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, n2, n3, n4, Integer.MIN_VALUE, n5);
    }

    public XMLGregorianCalendar newXMLGregorianCalendarTime(int n2, int n3, int n4, BigDecimal bigDecimal, int n5) {
        return this.newXMLGregorianCalendar(null, Integer.MIN_VALUE, Integer.MIN_VALUE, n2, n3, n4, bigDecimal, n5);
    }

    public XMLGregorianCalendar newXMLGregorianCalendarTime(int n2, int n3, int n4, int n5, int n6) {
        BigDecimal bigDecimal = null;
        if (n5 != Integer.MIN_VALUE) {
            if (n5 < 0 || n5 > 1000) {
                throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarTime(int hours, int minutes, int seconds, int milliseconds, int timezone)with invalid milliseconds: " + n5);
            }
            bigDecimal = BigDecimal.valueOf(n5, 3);
        }
        return this.newXMLGregorianCalendarTime(n2, n3, n4, bigDecimal, n6);
    }
}

