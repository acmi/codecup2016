/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.config.PropertySetterException;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.OptionHandler;

public class PropertySetter {
    protected Object obj;
    protected PropertyDescriptor[] props;
    static Class class$org$apache$log4j$spi$OptionHandler;
    static Class class$java$lang$String;
    static Class class$org$apache$log4j$Priority;
    static Class class$org$apache$log4j$spi$ErrorHandler;

    public PropertySetter(Object object) {
        this.obj = object;
    }

    protected void introspect() {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.obj.getClass());
            this.props = beanInfo.getPropertyDescriptors();
        }
        catch (IntrospectionException introspectionException) {
            LogLog.error("Failed to introspect " + this.obj + ": " + introspectionException.getMessage());
            this.props = new PropertyDescriptor[0];
        }
    }

    public static void setProperties(Object object, Properties properties, String string) {
        new PropertySetter(object).setProperties(properties, string);
    }

    public void setProperties(Properties properties, String string) {
        int n2 = string.length();
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String string2 = (String)enumeration.nextElement();
            if (!string2.startsWith(string) || string2.indexOf(46, n2 + 1) > 0) continue;
            String string3 = OptionConverter.findAndSubst(string2, properties);
            if (("layout".equals(string2 = string2.substring(n2)) || "errorhandler".equals(string2)) && this.obj instanceof Appender) continue;
            PropertyDescriptor propertyDescriptor = this.getPropertyDescriptor(Introspector.decapitalize(string2));
            if (propertyDescriptor != null && (class$org$apache$log4j$spi$OptionHandler == null ? PropertySetter.class$("org.apache.log4j.spi.OptionHandler") : class$org$apache$log4j$spi$OptionHandler).isAssignableFrom(propertyDescriptor.getPropertyType()) && propertyDescriptor.getWriteMethod() != null) {
                OptionHandler optionHandler = (OptionHandler)OptionConverter.instantiateByKey(properties, string + string2, propertyDescriptor.getPropertyType(), null);
                PropertySetter propertySetter = new PropertySetter(optionHandler);
                propertySetter.setProperties(properties, string + string2 + ".");
                try {
                    propertyDescriptor.getWriteMethod().invoke(this.obj, optionHandler);
                }
                catch (IllegalAccessException illegalAccessException) {
                    LogLog.warn("Failed to set property [" + string2 + "] to value \"" + string3 + "\". ", illegalAccessException);
                }
                catch (InvocationTargetException invocationTargetException) {
                    if (invocationTargetException.getTargetException() instanceof InterruptedException || invocationTargetException.getTargetException() instanceof InterruptedIOException) {
                        Thread.currentThread().interrupt();
                    }
                    LogLog.warn("Failed to set property [" + string2 + "] to value \"" + string3 + "\". ", invocationTargetException);
                }
                catch (RuntimeException runtimeException) {
                    LogLog.warn("Failed to set property [" + string2 + "] to value \"" + string3 + "\". ", runtimeException);
                }
                continue;
            }
            this.setProperty(string2, string3);
        }
        this.activate();
    }

    public void setProperty(String string, String string2) {
        if (string2 == null) {
            return;
        }
        PropertyDescriptor propertyDescriptor = this.getPropertyDescriptor(string = Introspector.decapitalize(string));
        if (propertyDescriptor == null) {
            LogLog.warn("No such property [" + string + "] in " + this.obj.getClass().getName() + ".");
        } else {
            try {
                this.setProperty(propertyDescriptor, string, string2);
            }
            catch (PropertySetterException propertySetterException) {
                LogLog.warn("Failed to set property [" + string + "] to value \"" + string2 + "\". ", propertySetterException.rootCause);
            }
        }
    }

    public void setProperty(PropertyDescriptor propertyDescriptor, String string, String string2) throws PropertySetterException {
        Object object;
        Method method = propertyDescriptor.getWriteMethod();
        if (method == null) {
            throw new PropertySetterException("No setter for property [" + string + "].");
        }
        Class<?>[] arrclass = method.getParameterTypes();
        if (arrclass.length != 1) {
            throw new PropertySetterException("#params for setter != 1");
        }
        try {
            object = this.convertArg(string2, arrclass[0]);
        }
        catch (Throwable throwable) {
            throw new PropertySetterException("Conversion to type [" + arrclass[0] + "] failed. Reason: " + throwable);
        }
        if (object == null) {
            throw new PropertySetterException("Conversion to type [" + arrclass[0] + "] failed.");
        }
        LogLog.debug("Setting property [" + string + "] to [" + object + "].");
        try {
            method.invoke(this.obj, object);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new PropertySetterException(illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getTargetException() instanceof InterruptedException || invocationTargetException.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            throw new PropertySetterException(invocationTargetException);
        }
        catch (RuntimeException runtimeException) {
            throw new PropertySetterException(runtimeException);
        }
    }

    protected Object convertArg(String string, Class class_) {
        if (string == null) {
            return null;
        }
        String string2 = string.trim();
        Class class_2 = class$java$lang$String == null ? (PropertySetter.class$java$lang$String = PropertySetter.class$("java.lang.String")) : class$java$lang$String;
        if (class_2.isAssignableFrom(class_)) {
            return string;
        }
        if (Integer.TYPE.isAssignableFrom(class_)) {
            return new Integer(string2);
        }
        if (Long.TYPE.isAssignableFrom(class_)) {
            return new Long(string2);
        }
        if (Boolean.TYPE.isAssignableFrom(class_)) {
            if ("true".equalsIgnoreCase(string2)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(string2)) {
                return Boolean.FALSE;
            }
        } else {
            Class class_3 = class$org$apache$log4j$Priority == null ? (PropertySetter.class$org$apache$log4j$Priority = PropertySetter.class$("org.apache.log4j.Priority")) : class$org$apache$log4j$Priority;
            if (class_3.isAssignableFrom(class_)) {
                return OptionConverter.toLevel(string2, Level.DEBUG);
            }
            Class class_4 = class$org$apache$log4j$spi$ErrorHandler == null ? (PropertySetter.class$org$apache$log4j$spi$ErrorHandler = PropertySetter.class$("org.apache.log4j.spi.ErrorHandler")) : class$org$apache$log4j$spi$ErrorHandler;
            if (class_4.isAssignableFrom(class_)) {
                return OptionConverter.instantiateByClassName(string2, class$org$apache$log4j$spi$ErrorHandler == null ? (PropertySetter.class$org$apache$log4j$spi$ErrorHandler = PropertySetter.class$("org.apache.log4j.spi.ErrorHandler")) : class$org$apache$log4j$spi$ErrorHandler, null);
            }
        }
        return null;
    }

    protected PropertyDescriptor getPropertyDescriptor(String string) {
        if (this.props == null) {
            this.introspect();
        }
        for (int i2 = 0; i2 < this.props.length; ++i2) {
            if (!string.equals(this.props[i2].getName())) continue;
            return this.props[i2];
        }
        return null;
    }

    public void activate() {
        if (this.obj instanceof OptionHandler) {
            ((OptionHandler)this.obj).activateOptions();
        }
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError().initCause(classNotFoundException);
        }
    }
}

