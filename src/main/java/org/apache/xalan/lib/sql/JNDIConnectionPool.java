/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.xalan.lib.sql.ConnectionPool;

public class JNDIConnectionPool
implements ConnectionPool {
    protected Object jdbcSource = null;
    private Method getConnectionWithArgs = null;
    private Method getConnection = null;
    protected String jndiPath = null;
    protected String user = null;
    protected String pwd = null;
    static Class class$java$lang$String;

    public JNDIConnectionPool() {
    }

    public JNDIConnectionPool(String string) {
        this.jndiPath = string.trim();
    }

    public void setJndiPath(String string) {
        this.jndiPath = string;
    }

    public String getJndiPath() {
        return this.jndiPath;
    }

    public boolean isEnabled() {
        return true;
    }

    public void setDriver(String string) {
        throw new Error("This method is not supported. All connection information is handled by the JDBC datasource provider");
    }

    public void setURL(String string) {
        throw new Error("This method is not supported. All connection information is handled by the JDBC datasource provider");
    }

    public void freeUnused() {
    }

    public boolean hasActiveConnections() {
        return false;
    }

    public void setPassword(String string) {
        if (string != null) {
            string = string.trim();
        }
        if (string != null && string.length() == 0) {
            string = null;
        }
        this.pwd = string;
    }

    public void setUser(String string) {
        if (string != null) {
            string = string.trim();
        }
        if (string != null && string.length() == 0) {
            string = null;
        }
        this.user = string;
    }

    public Connection getConnection() throws SQLException {
        if (this.jdbcSource == null) {
            try {
                this.findDatasource();
            }
            catch (NamingException namingException) {
                throw new SQLException("Could not create jndi context for " + this.jndiPath + " - " + namingException.getLocalizedMessage());
            }
        }
        try {
            if (this.user != null || this.pwd != null) {
                Object[] arrobject = new Object[]{this.user, this.pwd};
                return (Connection)this.getConnectionWithArgs.invoke(this.jdbcSource, arrobject);
            }
            Object[] arrobject = new Object[]{};
            return (Connection)this.getConnection.invoke(this.jdbcSource, arrobject);
        }
        catch (Exception exception) {
            throw new SQLException("Could not create jndi connection for " + this.jndiPath + " - " + exception.getLocalizedMessage());
        }
    }

    protected void findDatasource() throws NamingException {
        try {
            InitialContext initialContext = new InitialContext();
            this.jdbcSource = initialContext.lookup(this.jndiPath);
            Class[] arrclass = new Class[2];
            Class class_ = class$java$lang$String == null ? (JNDIConnectionPool.class$java$lang$String = JNDIConnectionPool.class$("java.lang.String")) : class$java$lang$String;
            arrclass[0] = class_;
            arrclass[1] = class$java$lang$String == null ? (JNDIConnectionPool.class$java$lang$String = JNDIConnectionPool.class$("java.lang.String")) : class$java$lang$String;
            Class[] arrclass2 = arrclass;
            this.getConnectionWithArgs = this.jdbcSource.getClass().getDeclaredMethod("getConnection", arrclass2);
            Class[] arrclass3 = new Class[]{};
            this.getConnection = this.jdbcSource.getClass().getDeclaredMethod("getConnection", arrclass3);
        }
        catch (NamingException namingException) {
            throw namingException;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new NamingException("Unable to resolve JNDI DataSource - " + noSuchMethodException);
        }
    }

    public void releaseConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public void releaseConnectionOnError(Connection connection) throws SQLException {
        connection.close();
    }

    public void setPoolEnabled(boolean bl) {
        if (!bl) {
            this.jdbcSource = null;
        }
    }

    public void setProtocol(Properties properties) {
    }

    public void setMinConnections(int n2) {
    }

    public boolean testConnection() {
        if (this.jdbcSource == null) {
            try {
                this.findDatasource();
            }
            catch (NamingException namingException) {
                return false;
            }
        }
        return true;
    }

    static Class class$(String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }
}

