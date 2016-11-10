/*
 * Decompiled with CFR 0_119.
 */
package org.apache.log4j.helpers;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.log4j.Appender;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;

public class AppenderAttachableImpl
implements AppenderAttachable {
    protected Vector appenderList;

    public void addAppender(Appender appender) {
        if (appender == null) {
            return;
        }
        if (this.appenderList == null) {
            this.appenderList = new Vector(1);
        }
        if (!this.appenderList.contains(appender)) {
            this.appenderList.addElement(appender);
        }
    }

    public int appendLoopOnAppenders(LoggingEvent loggingEvent) {
        int n2 = 0;
        if (this.appenderList != null) {
            n2 = this.appenderList.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Appender appender = (Appender)this.appenderList.elementAt(i2);
                appender.doAppend(loggingEvent);
            }
        }
        return n2;
    }

    public Enumeration getAllAppenders() {
        if (this.appenderList == null) {
            return null;
        }
        return this.appenderList.elements();
    }

    public void removeAllAppenders() {
        if (this.appenderList != null) {
            int n2 = this.appenderList.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Appender appender = (Appender)this.appenderList.elementAt(i2);
                appender.close();
            }
            this.appenderList.removeAllElements();
            this.appenderList = null;
        }
    }
}

