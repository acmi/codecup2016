/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.util.Hashtable;
import org.apache.xalan.lib.sql.ConnectionPool;
import org.apache.xalan.res.XSLMessages;

public class ConnectionPoolManager {
    private static Hashtable m_poolTable = null;

    public ConnectionPoolManager() {
        this.init();
    }

    private synchronized void init() {
        if (m_poolTable == null) {
            m_poolTable = new Hashtable();
        }
    }

    public synchronized void registerPool(String string, ConnectionPool connectionPool) {
        if (m_poolTable.containsKey(string)) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_POOL_EXISTS", null));
        }
        m_poolTable.put(string, connectionPool);
    }

    public synchronized void removePool(String string) {
        ConnectionPool connectionPool = this.getPool(string);
        if (null != connectionPool) {
            connectionPool.setPoolEnabled(false);
            if (!connectionPool.hasActiveConnections()) {
                m_poolTable.remove(string);
            }
        }
    }

    public synchronized ConnectionPool getPool(String string) {
        return (ConnectionPool)m_poolTable.get(string);
    }
}

