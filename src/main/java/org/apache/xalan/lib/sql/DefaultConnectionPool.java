/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import org.apache.xalan.lib.sql.ConnectionPool;
import org.apache.xalan.lib.sql.ObjectFactory;
import org.apache.xalan.lib.sql.PooledConnection;
import org.apache.xalan.res.XSLMessages;

public class DefaultConnectionPool
implements ConnectionPool {
    private Driver m_Driver = null;
    private static final boolean DEBUG = false;
    private String m_driver = new String("");
    private String m_url = new String("");
    private int m_PoolMinSize = 1;
    private Properties m_ConnectionProtocol = new Properties();
    private Vector m_pool = new Vector();
    private boolean m_IsActive = false;

    public boolean isEnabled() {
        return this.m_IsActive;
    }

    public void setDriver(String string) {
        this.m_driver = string;
    }

    public void setURL(String string) {
        this.m_url = string;
    }

    public void freeUnused() {
        Iterator iterator = this.m_pool.iterator();
        while (iterator.hasNext()) {
            PooledConnection pooledConnection = (PooledConnection)iterator.next();
            if (pooledConnection.inUse()) continue;
            pooledConnection.close();
            iterator.remove();
        }
    }

    public boolean hasActiveConnections() {
        return this.m_pool.size() > 0;
    }

    public void setPassword(String string) {
        this.m_ConnectionProtocol.put("password", string);
    }

    public void setUser(String string) {
        this.m_ConnectionProtocol.put("user", string);
    }

    public void setProtocol(Properties properties) {
        Enumeration enumeration = properties.keys();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            this.m_ConnectionProtocol.put(string, properties.getProperty(string));
        }
    }

    public void setMinConnections(int n2) {
        this.m_PoolMinSize = n2;
    }

    public boolean testConnection() {
        try {
            Connection connection = this.getConnection();
            if (connection == null) {
                return false;
            }
            this.releaseConnection(connection);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public synchronized Connection getConnection() throws IllegalArgumentException, SQLException {
        PooledConnection pooledConnection = null;
        if (this.m_pool.size() < this.m_PoolMinSize) {
            this.initializePool();
        }
        for (int i2 = 0; i2 < this.m_pool.size(); ++i2) {
            pooledConnection = (PooledConnection)this.m_pool.elementAt(i2);
            if (pooledConnection.inUse()) continue;
            pooledConnection.setInUse(true);
            return pooledConnection.getConnection();
        }
        Connection connection = this.createConnection();
        pooledConnection = new PooledConnection(connection);
        pooledConnection.setInUse(true);
        this.m_pool.addElement(pooledConnection);
        return pooledConnection.getConnection();
    }

    public synchronized void releaseConnection(Connection connection) throws SQLException {
        for (int i2 = 0; i2 < this.m_pool.size(); ++i2) {
            PooledConnection pooledConnection = (PooledConnection)this.m_pool.elementAt(i2);
            if (pooledConnection.getConnection() != connection) continue;
            if (!this.isEnabled()) {
                connection.close();
                this.m_pool.removeElementAt(i2);
                break;
            }
            pooledConnection.setInUse(false);
            break;
        }
    }

    public synchronized void releaseConnectionOnError(Connection connection) throws SQLException {
        for (int i2 = 0; i2 < this.m_pool.size(); ++i2) {
            PooledConnection pooledConnection = (PooledConnection)this.m_pool.elementAt(i2);
            if (pooledConnection.getConnection() != connection) continue;
            connection.close();
            this.m_pool.removeElementAt(i2);
            break;
        }
    }

    private Connection createConnection() throws SQLException {
        Connection connection = null;
        connection = this.m_Driver.connect(this.m_url, this.m_ConnectionProtocol);
        return connection;
    }

    public synchronized void initializePool() throws IllegalArgumentException, SQLException {
        if (this.m_driver == null) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_NO_DRIVER_NAME_SPECIFIED", null));
        }
        if (this.m_url == null) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_NO_URL_SPECIFIED", null));
        }
        if (this.m_PoolMinSize < 1) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_POOLSIZE_LESS_THAN_ONE", null));
        }
        try {
            this.m_Driver = (Driver)ObjectFactory.newInstance(this.m_driver, ObjectFactory.findClassLoader(), true);
            DriverManager.registerDriver(this.m_Driver);
        }
        catch (ObjectFactory.ConfigurationError configurationError) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_DRIVER_NAME", null));
        }
        catch (Exception exception) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_DRIVER_NAME", null));
        }
        if (!this.m_IsActive) {
            return;
        }
        do {
            Connection connection;
            if ((connection = this.createConnection()) == null) continue;
            PooledConnection pooledConnection = new PooledConnection(connection);
            this.addConnection(pooledConnection);
        } while (this.m_pool.size() < this.m_PoolMinSize);
    }

    private void addConnection(PooledConnection pooledConnection) {
        this.m_pool.addElement(pooledConnection);
    }

    protected void finalize() throws Throwable {
        for (int i2 = 0; i2 < this.m_pool.size(); ++i2) {
            PooledConnection pooledConnection = (PooledConnection)this.m_pool.elementAt(i2);
            if (!pooledConnection.inUse()) {
                pooledConnection.close();
                continue;
            }
            try {
                Thread.sleep(30000);
                pooledConnection.close();
                continue;
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        super.finalize();
    }

    public void setPoolEnabled(boolean bl) {
        this.m_IsActive = bl;
        if (!bl) {
            this.freeUnused();
        }
    }
}

