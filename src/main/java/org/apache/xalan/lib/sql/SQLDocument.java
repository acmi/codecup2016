/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Vector;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.lib.sql.ConnectionPool;
import org.apache.xalan.lib.sql.DTMDocument;
import org.apache.xalan.lib.sql.ObjectArray;
import org.apache.xalan.lib.sql.QueryParameter;
import org.apache.xalan.lib.sql.SQLQueryParser;
import org.apache.xalan.lib.sql.XConnection;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xpath.XPathContext;

public class SQLDocument
extends DTMDocument {
    private boolean DEBUG = false;
    private static final String S_NAMESPACE = "http://xml.apache.org/xalan/SQLExtension";
    private static final String S_SQL = "sql";
    private static final String S_ROW_SET = "row-set";
    private static final String S_METADATA = "metadata";
    private static final String S_COLUMN_HEADER = "column-header";
    private static final String S_ROW = "row";
    private static final String S_COL = "col";
    private static final String S_OUT_PARAMETERS = "out-parameters";
    private static final String S_CATALOGUE_NAME = "catalogue-name";
    private static final String S_DISPLAY_SIZE = "column-display-size";
    private static final String S_COLUMN_LABEL = "column-label";
    private static final String S_COLUMN_NAME = "column-name";
    private static final String S_COLUMN_TYPE = "column-type";
    private static final String S_COLUMN_TYPENAME = "column-typename";
    private static final String S_PRECISION = "precision";
    private static final String S_SCALE = "scale";
    private static final String S_SCHEMA_NAME = "schema-name";
    private static final String S_TABLE_NAME = "table-name";
    private static final String S_CASESENSITIVE = "case-sensitive";
    private static final String S_DEFINITELYWRITABLE = "definitely-writable";
    private static final String S_ISNULLABLE = "nullable";
    private static final String S_ISSIGNED = "signed";
    private static final String S_ISWRITEABLE = "writable";
    private static final String S_ISSEARCHABLE = "searchable";
    private int m_SQL_TypeID = 0;
    private int m_MetaData_TypeID = 0;
    private int m_ColumnHeader_TypeID = 0;
    private int m_RowSet_TypeID = 0;
    private int m_Row_TypeID = 0;
    private int m_Col_TypeID = 0;
    private int m_OutParameter_TypeID = 0;
    private int m_ColAttrib_CATALOGUE_NAME_TypeID = 0;
    private int m_ColAttrib_DISPLAY_SIZE_TypeID = 0;
    private int m_ColAttrib_COLUMN_LABEL_TypeID = 0;
    private int m_ColAttrib_COLUMN_NAME_TypeID = 0;
    private int m_ColAttrib_COLUMN_TYPE_TypeID = 0;
    private int m_ColAttrib_COLUMN_TYPENAME_TypeID = 0;
    private int m_ColAttrib_PRECISION_TypeID = 0;
    private int m_ColAttrib_SCALE_TypeID = 0;
    private int m_ColAttrib_SCHEMA_NAME_TypeID = 0;
    private int m_ColAttrib_TABLE_NAME_TypeID = 0;
    private int m_ColAttrib_CASESENSITIVE_TypeID = 0;
    private int m_ColAttrib_DEFINITELYWRITEABLE_TypeID = 0;
    private int m_ColAttrib_ISNULLABLE_TypeID = 0;
    private int m_ColAttrib_ISSIGNED_TypeID = 0;
    private int m_ColAttrib_ISWRITEABLE_TypeID = 0;
    private int m_ColAttrib_ISSEARCHABLE_TypeID = 0;
    private Statement m_Statement = null;
    private ExpressionContext m_ExpressionContext = null;
    private ConnectionPool m_ConnectionPool = null;
    private ResultSet m_ResultSet = null;
    private SQLQueryParser m_QueryParser = null;
    private int[] m_ColHeadersIdx;
    private int m_ColCount;
    private int m_MetaDataIdx = -1;
    private int m_RowSetIdx = -1;
    private int m_SQLIdx = -1;
    private int m_FirstRowIdx = -1;
    private int m_LastRowIdx = -1;
    private boolean m_StreamingMode = true;
    private boolean m_MultipleResults = false;
    private boolean m_HasErrors = false;
    private boolean m_IsStatementCachingEnabled = false;
    private XConnection m_XConnection = null;

    public SQLDocument(DTMManager dTMManager, int n2) {
        super(dTMManager, n2);
    }

    public static SQLDocument getNewDocument(ExpressionContext expressionContext) {
        DTMManager dTMManager = ((XPathContext.XPathExpressionContext)expressionContext).getDTMManager();
        DTMManagerDefault dTMManagerDefault = (DTMManagerDefault)dTMManager;
        int n2 = dTMManagerDefault.getFirstFreeDTMID();
        SQLDocument sQLDocument = new SQLDocument(dTMManager, n2 << 16);
        dTMManagerDefault.addDTM(sQLDocument, n2);
        sQLDocument.setExpressionContext(expressionContext);
        return sQLDocument;
    }

    protected void setExpressionContext(ExpressionContext expressionContext) {
        this.m_ExpressionContext = expressionContext;
    }

    public ExpressionContext getExpressionContext() {
        return this.m_ExpressionContext;
    }

    public void execute(XConnection xConnection, SQLQueryParser sQLQueryParser) throws SQLException {
        try {
            this.m_StreamingMode = "true".equals(xConnection.getFeature("streaming"));
            this.m_MultipleResults = "true".equals(xConnection.getFeature("multiple-results"));
            this.m_IsStatementCachingEnabled = "true".equals(xConnection.getFeature("cache-statements"));
            this.m_XConnection = xConnection;
            this.m_QueryParser = sQLQueryParser;
            this.executeSQLStatement();
            this.createExpandedNameTable();
            this.m_DocumentIdx = this.addElement(0, this.m_Document_TypeID, -1, -1);
            this.m_SQLIdx = this.addElement(1, this.m_SQL_TypeID, this.m_DocumentIdx, -1);
            if (!this.m_MultipleResults) {
                this.extractSQLMetaData(this.m_ResultSet.getMetaData());
            }
        }
        catch (SQLException sQLException) {
            this.m_HasErrors = true;
            throw sQLException;
        }
    }

    private void executeSQLStatement() throws SQLException {
        this.m_ConnectionPool = this.m_XConnection.getConnectionPool();
        Connection connection = this.m_ConnectionPool.getConnection();
        if (!this.m_QueryParser.hasParameters()) {
            this.m_Statement = connection.createStatement();
            this.m_ResultSet = this.m_Statement.executeQuery(this.m_QueryParser.getSQLQuery());
        } else if (this.m_QueryParser.isCallable()) {
            CallableStatement callableStatement = connection.prepareCall(this.m_QueryParser.getSQLQuery());
            this.m_QueryParser.registerOutputParameters(callableStatement);
            this.m_QueryParser.populateStatement(callableStatement, this.m_ExpressionContext);
            this.m_Statement = callableStatement;
            if (!callableStatement.execute()) {
                throw new SQLException("Error in Callable Statement");
            }
            this.m_ResultSet = this.m_Statement.getResultSet();
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement(this.m_QueryParser.getSQLQuery());
            this.m_QueryParser.populateStatement(preparedStatement, this.m_ExpressionContext);
            this.m_Statement = preparedStatement;
            this.m_ResultSet = preparedStatement.executeQuery();
        }
    }

    public void skip(int n2) {
        try {
            if (this.m_ResultSet != null) {
                this.m_ResultSet.relative(n2);
            }
        }
        catch (Exception exception) {
            try {
                for (int i2 = 0; i2 < n2 && this.m_ResultSet.next(); ++i2) {
                }
            }
            catch (Exception exception2) {
                this.m_XConnection.setError(exception, this, this.checkWarnings());
                this.m_XConnection.setError(exception2, this, this.checkWarnings());
            }
        }
    }

    private void extractSQLMetaData(ResultSetMetaData resultSetMetaData) {
        this.m_MetaDataIdx = this.addElement(1, this.m_MetaData_TypeID, this.m_MultipleResults ? this.m_RowSetIdx : this.m_SQLIdx, -1);
        try {
            this.m_ColCount = resultSetMetaData.getColumnCount();
            this.m_ColHeadersIdx = new int[this.m_ColCount];
        }
        catch (Exception exception) {
            this.m_XConnection.setError(exception, this, this.checkWarnings());
        }
        int n2 = -1;
        int n3 = 1;
        for (n3 = 1; n3 <= this.m_ColCount; ++n3) {
            this.m_ColHeadersIdx[n3 - 1] = this.addElement(2, this.m_ColumnHeader_TypeID, this.m_MetaDataIdx, n2);
            n2 = this.m_ColHeadersIdx[n3 - 1];
            try {
                this.addAttributeToNode(resultSetMetaData.getColumnName(n3), this.m_ColAttrib_COLUMN_NAME_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_COLUMN_NAME_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.getColumnLabel(n3), this.m_ColAttrib_COLUMN_LABEL_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_COLUMN_LABEL_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.getCatalogName(n3), this.m_ColAttrib_CATALOGUE_NAME_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_CATALOGUE_NAME_TypeID, n2);
            }
            try {
                this.addAttributeToNode(new Integer(resultSetMetaData.getColumnDisplaySize(n3)), this.m_ColAttrib_DISPLAY_SIZE_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_DISPLAY_SIZE_TypeID, n2);
            }
            try {
                this.addAttributeToNode(new Integer(resultSetMetaData.getColumnType(n3)), this.m_ColAttrib_COLUMN_TYPE_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_COLUMN_TYPE_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.getColumnTypeName(n3), this.m_ColAttrib_COLUMN_TYPENAME_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_COLUMN_TYPENAME_TypeID, n2);
            }
            try {
                this.addAttributeToNode(new Integer(resultSetMetaData.getPrecision(n3)), this.m_ColAttrib_PRECISION_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_PRECISION_TypeID, n2);
            }
            try {
                this.addAttributeToNode(new Integer(resultSetMetaData.getScale(n3)), this.m_ColAttrib_SCALE_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_SCALE_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.getSchemaName(n3), this.m_ColAttrib_SCHEMA_NAME_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_SCHEMA_NAME_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.getTableName(n3), this.m_ColAttrib_TABLE_NAME_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_TABLE_NAME_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.isCaseSensitive(n3) ? "true" : "false", this.m_ColAttrib_CASESENSITIVE_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_CASESENSITIVE_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.isDefinitelyWritable(n3) ? "true" : "false", this.m_ColAttrib_DEFINITELYWRITEABLE_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_DEFINITELYWRITEABLE_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.isNullable(n3) != 0 ? "true" : "false", this.m_ColAttrib_ISNULLABLE_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_ISNULLABLE_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.isSigned(n3) ? "true" : "false", this.m_ColAttrib_ISSIGNED_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_ISSIGNED_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.isWritable(n3) ? "true" : "false", this.m_ColAttrib_ISWRITEABLE_TypeID, n2);
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_ISWRITEABLE_TypeID, n2);
            }
            try {
                this.addAttributeToNode(resultSetMetaData.isSearchable(n3) ? "true" : "false", this.m_ColAttrib_ISSEARCHABLE_TypeID, n2);
                continue;
            }
            catch (Exception exception) {
                this.addAttributeToNode("Not Supported", this.m_ColAttrib_ISSEARCHABLE_TypeID, n2);
            }
        }
    }

    protected void createExpandedNameTable() {
        super.createExpandedNameTable();
        this.m_SQL_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "sql", 1);
        this.m_MetaData_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "metadata", 1);
        this.m_ColumnHeader_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "column-header", 1);
        this.m_RowSet_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "row-set", 1);
        this.m_Row_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "row", 1);
        this.m_Col_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "col", 1);
        this.m_OutParameter_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "out-parameters", 1);
        this.m_ColAttrib_CATALOGUE_NAME_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "catalogue-name", 2);
        this.m_ColAttrib_DISPLAY_SIZE_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "column-display-size", 2);
        this.m_ColAttrib_COLUMN_LABEL_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "column-label", 2);
        this.m_ColAttrib_COLUMN_NAME_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "column-name", 2);
        this.m_ColAttrib_COLUMN_TYPE_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "column-type", 2);
        this.m_ColAttrib_COLUMN_TYPENAME_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "column-typename", 2);
        this.m_ColAttrib_PRECISION_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "precision", 2);
        this.m_ColAttrib_SCALE_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "scale", 2);
        this.m_ColAttrib_SCHEMA_NAME_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "schema-name", 2);
        this.m_ColAttrib_TABLE_NAME_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "table-name", 2);
        this.m_ColAttrib_CASESENSITIVE_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "case-sensitive", 2);
        this.m_ColAttrib_DEFINITELYWRITEABLE_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "definitely-writable", 2);
        this.m_ColAttrib_ISNULLABLE_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "nullable", 2);
        this.m_ColAttrib_ISSIGNED_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "signed", 2);
        this.m_ColAttrib_ISWRITEABLE_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "writable", 2);
        this.m_ColAttrib_ISSEARCHABLE_TypeID = this.m_expandedNameTable.getExpandedTypeID("http://xml.apache.org/xalan/SQLExtension", "searchable", 2);
    }

    private boolean addRowToDTMFromResultSet() {
        try {
            if (this.m_FirstRowIdx == -1) {
                this.m_RowSetIdx = this.addElement(1, this.m_RowSet_TypeID, this.m_SQLIdx, this.m_MultipleResults ? this.m_RowSetIdx : this.m_MetaDataIdx);
                if (this.m_MultipleResults) {
                    this.extractSQLMetaData(this.m_ResultSet.getMetaData());
                }
            }
            if (!this.m_ResultSet.next()) {
                if (this.m_StreamingMode && this.m_LastRowIdx != -1) {
                    this.m_nextsib.setElementAt(-1, this.m_LastRowIdx);
                }
                this.m_ResultSet.close();
                if (this.m_MultipleResults) {
                    while (!this.m_Statement.getMoreResults() && this.m_Statement.getUpdateCount() >= 0) {
                    }
                    this.m_ResultSet = this.m_Statement.getResultSet();
                } else {
                    this.m_ResultSet = null;
                }
                if (this.m_ResultSet != null) {
                    this.m_FirstRowIdx = -1;
                    this.addRowToDTMFromResultSet();
                } else {
                    SQLWarning sQLWarning;
                    Vector vector = this.m_QueryParser.getParameters();
                    if (vector != null) {
                        int n2 = this.addElement(1, this.m_OutParameter_TypeID, this.m_SQLIdx, this.m_RowSetIdx);
                        int n3 = -1;
                        for (int i2 = 0; i2 < vector.size(); ++i2) {
                            QueryParameter queryParameter = (QueryParameter)vector.elementAt(i2);
                            if (!queryParameter.isOutput()) continue;
                            Object object = ((CallableStatement)this.m_Statement).getObject(i2 + 1);
                            n3 = this.addElementWithData(object, 2, this.m_Col_TypeID, n2, n3);
                            this.addAttributeToNode(queryParameter.getName(), this.m_ColAttrib_COLUMN_NAME_TypeID, n3);
                            this.addAttributeToNode(queryParameter.getName(), this.m_ColAttrib_COLUMN_LABEL_TypeID, n3);
                            this.addAttributeToNode(new Integer(queryParameter.getType()), this.m_ColAttrib_COLUMN_TYPE_TypeID, n3);
                            this.addAttributeToNode(queryParameter.getTypeName(), this.m_ColAttrib_COLUMN_TYPENAME_TypeID, n3);
                        }
                    }
                    if ((sQLWarning = this.checkWarnings()) != null) {
                        this.m_XConnection.setError(null, null, sQLWarning);
                    }
                }
                return false;
            }
            if (this.m_FirstRowIdx == -1) {
                this.m_LastRowIdx = this.m_FirstRowIdx = this.addElement(2, this.m_Row_TypeID, this.m_RowSetIdx, this.m_MultipleResults ? this.m_MetaDataIdx : -1);
                if (this.m_StreamingMode) {
                    this.m_nextsib.setElementAt(this.m_LastRowIdx, this.m_LastRowIdx);
                }
            } else if (!this.m_StreamingMode) {
                this.m_LastRowIdx = this.addElement(2, this.m_Row_TypeID, this.m_RowSetIdx, this.m_LastRowIdx);
            }
            int n4 = this._firstch(this.m_LastRowIdx);
            int n5 = -1;
            for (int i3 = 1; i3 <= this.m_ColCount; ++i3) {
                Object object = this.m_ResultSet.getObject(i3);
                if (n4 == -1) {
                    n5 = this.addElementWithData(object, 3, this.m_Col_TypeID, this.m_LastRowIdx, n5);
                    this.cloneAttributeFromNode(n5, this.m_ColHeadersIdx[i3 - 1]);
                } else {
                    int n6 = this._firstch(n4);
                    if (n6 == -1) {
                        this.error("Streaming Mode, Data Error");
                    } else {
                        this.m_ObjectArray.setAt(n6, object);
                    }
                }
                if (n4 == -1) continue;
                n4 = this._nextsib(n4);
            }
        }
        catch (Exception exception) {
            if (this.DEBUG) {
                System.out.println("SQL Error Fetching next row [" + exception.getLocalizedMessage() + "]");
            }
            this.m_XConnection.setError(exception, this, this.checkWarnings());
            this.m_HasErrors = true;
        }
        return true;
    }

    public boolean hasErrors() {
        return this.m_HasErrors;
    }

    public void close(boolean bl) {
        Object object;
        try {
            object = this.checkWarnings();
            if (object != null) {
                this.m_XConnection.setError(null, null, (SQLWarning)object);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            if (null != this.m_ResultSet) {
                this.m_ResultSet.close();
                this.m_ResultSet = null;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        object = null;
        try {
            if (null != this.m_Statement) {
                object = this.m_Statement.getConnection();
                this.m_Statement.close();
                this.m_Statement = null;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            if (object != null) {
                if (this.m_HasErrors) {
                    this.m_ConnectionPool.releaseConnectionOnError((Connection)object);
                } else {
                    this.m_ConnectionPool.releaseConnection((Connection)object);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.getManager().release(this, true);
    }

    protected boolean nextNode() {
        if (this.DEBUG) {
            System.out.println("nextNode()");
        }
        try {
            return false;
        }
        catch (Exception exception) {
            return false;
        }
    }

    protected int _nextsib(int n2) {
        if (this.m_ResultSet != null) {
            int n3 = this._exptype(n2);
            if (this.m_FirstRowIdx == -1) {
                this.addRowToDTMFromResultSet();
            }
            if (n3 == this.m_Row_TypeID && n2 >= this.m_LastRowIdx) {
                if (this.DEBUG) {
                    System.out.println("reading from the ResultSet");
                }
                this.addRowToDTMFromResultSet();
            } else if (this.m_MultipleResults && n2 == this.m_RowSetIdx) {
                if (this.DEBUG) {
                    System.out.println("reading for next ResultSet");
                }
                int n4 = this.m_RowSetIdx;
                while (n4 == this.m_RowSetIdx && this.m_ResultSet != null) {
                    this.addRowToDTMFromResultSet();
                }
            }
        }
        return super._nextsib(n2);
    }

    public void documentRegistration() {
        if (this.DEBUG) {
            System.out.println("Document Registration");
        }
    }

    public void documentRelease() {
        if (this.DEBUG) {
            System.out.println("Document Release");
        }
    }

    public SQLWarning checkWarnings() {
        SQLWarning sQLWarning = null;
        if (this.m_Statement != null) {
            try {
                sQLWarning = this.m_Statement.getWarnings();
                this.m_Statement.clearWarnings();
            }
            catch (SQLException sQLException) {
                // empty catch block
            }
        }
        return sQLWarning;
    }
}

