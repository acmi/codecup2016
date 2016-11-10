/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

public class ThreadGroups {
    public static ThreadGroup getThreadGroup() {
        SecurityManager securityManager = System.getSecurityManager();
        return null != securityManager ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    public static ThreadGroup getServerThreadGroup() {
        ThreadGroup threadGroup;
        ThreadGroup threadGroup2 = ThreadGroups.getThreadGroup();
        while (null != (threadGroup = threadGroup2.getParent())) {
            try {
                threadGroup.checkAccess();
            }
            catch (SecurityException securityException) {
                break;
            }
            threadGroup2 = threadGroup;
        }
        return threadGroup2;
    }
}

