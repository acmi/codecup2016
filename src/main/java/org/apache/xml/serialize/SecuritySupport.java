/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xml.serialize;

import java.security.AccessController;
import java.security.PrivilegedAction;

final class SecuritySupport {
    static String getSystemProperty(String string) {
        return (String)AccessController.doPrivileged(new PrivilegedAction(string){
            private final String val$propName;

            public Object run() {
                return System.getProperty(this.val$propName);
            }
        });
    }

}

