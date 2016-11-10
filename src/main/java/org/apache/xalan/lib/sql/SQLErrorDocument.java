/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.sql.SQLException;
import java.sql.SQLWarning;
import org.apache.xalan.lib.sql.DTMDocument;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.ExpandedNameTable;

public class SQLErrorDocument
extends DTMDocument {
    private static final String S_EXT_ERROR = "ext-error";
    private static final String S_SQL_ERROR = "sql-error";
    private static final String S_MESSAGE = "message";
    private static final String S_CODE = "code";
    private static final String S_STATE = "state";
    private static final String S_SQL_WARNING = "sql-warning";
    private int m_ErrorExt_TypeID = -1;
    private int m_Message_TypeID = -1;
    private int m_Code_TypeID = -1;
    private int m_State_TypeID = -1;
    private int m_SQLWarning_TypeID = -1;
    private int m_SQLError_TypeID = -1;
    private int m_rootID = -1;
    private int m_extErrorID = -1;
    private int m_MainMessageID = -1;

    public SQLErrorDocument(DTMManager dTMManager, int n2, SQLException sQLException) {
        super(dTMManager, n2);
        this.createExpandedNameTable();
        this.buildBasicStructure(sQLException);
        int n3 = this.addElement(2, this.m_SQLError_TypeID, this.m_extErrorID, this.m_MainMessageID);
        int n4 = -1;
        n4 = this.addElementWithData(new Integer(sQLException.getErrorCode()), 3, this.m_Code_TypeID, n3, n4);
        n4 = this.addElementWithData(sQLException.getLocalizedMessage(), 3, this.m_Message_TypeID, n3, n4);
    }

    public SQLErrorDocument(DTMManager dTMManager, int n2, Exception exception) {
        super(dTMManager, n2);
        this.createExpandedNameTable();
        this.buildBasicStructure(exception);
    }

    public SQLErrorDocument(DTMManager dTMManager, int n2, Exception exception, SQLWarning sQLWarning, boolean bl) {
        super(dTMManager, n2);
        this.createExpandedNameTable();
        this.buildBasicStructure(exception);
        SQLException sQLException = null;
        int n3 = this.m_MainMessageID;
        boolean bl2 = false;
        if (exception != null && exception instanceof SQLException) {
            sQLException = (SQLException)exception;
        } else if (bl && sQLWarning != null) {
            sQLException = sQLWarning;
            bl2 = true;
        }
        while (sQLException != null) {
            int n4;
            n3 = n4 = this.addElement(2, bl2 ? this.m_SQLWarning_TypeID : this.m_SQLError_TypeID, this.m_extErrorID, n3);
            int n5 = -1;
            n5 = this.addElementWithData(new Integer(sQLException.getErrorCode()), 3, this.m_Code_TypeID, n4, n5);
            n5 = this.addElementWithData(sQLException.getLocalizedMessage(), 3, this.m_Message_TypeID, n4, n5);
            if (bl) {
                String string = sQLException.getSQLState();
                if (string != null && string.length() > 0) {
                    n5 = this.addElementWithData(string, 3, this.m_State_TypeID, n4, n5);
                }
                if (bl2) {
                    sQLException = ((SQLWarning)sQLException).getNextWarning();
                    continue;
                }
                sQLException = sQLException.getNextException();
                continue;
            }
            sQLException = null;
        }
    }

    private void buildBasicStructure(Exception exception) {
        this.m_rootID = this.addElement(0, this.m_Document_TypeID, -1, -1);
        this.m_extErrorID = this.addElement(1, this.m_ErrorExt_TypeID, this.m_rootID, -1);
        this.m_MainMessageID = this.addElementWithData(exception != null ? exception.getLocalizedMessage() : "SQLWarning", 2, this.m_Message_TypeID, this.m_extErrorID, -1);
    }

    protected void createExpandedNameTable() {
        super.createExpandedNameTable();
        this.m_ErrorExt_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "ext-error", 1);
        this.m_SQLError_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "sql-error", 1);
        this.m_Message_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "message", 1);
        this.m_Code_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "code", 1);
        this.m_State_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "state", 1);
        this.m_SQLWarning_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "sql-warning", 1);
    }
}

