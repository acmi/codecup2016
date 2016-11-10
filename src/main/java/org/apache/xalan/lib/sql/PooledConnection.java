/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

public class PooledConnection {
    private Connection connection = null;
    private boolean inuse = false;

    public PooledConnection(Connection connection) {
        if (connection != null) {
            this.connection = connection;
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setInUse(boolean bl) {
        this.inuse = bl;
    }

    public boolean inUse() {
        return this.inuse;
    }

    public void close() {
        try {
            this.connection.close();
        }
        catch (SQLException sQLException) {
            System.err.println(sQLException.getMessage());
        }
    }
}

