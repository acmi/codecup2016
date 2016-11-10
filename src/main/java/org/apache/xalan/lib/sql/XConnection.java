/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.lib.sql.ConnectionPool;
import org.apache.xalan.lib.sql.ConnectionPoolManager;
import org.apache.xalan.lib.sql.DefaultConnectionPool;
import org.apache.xalan.lib.sql.JNDIConnectionPool;
import org.apache.xalan.lib.sql.QueryParameter;
import org.apache.xalan.lib.sql.SQLDocument;
import org.apache.xalan.lib.sql.SQLErrorDocument;
import org.apache.xalan.lib.sql.SQLQueryParser;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.DTMManagerDefault;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBooleanStatic;
import org.apache.xpath.objects.XNodeSet;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XConnection {
    private static final boolean DEBUG = false;
    private ConnectionPool m_ConnectionPool = null;
    private Connection m_Connection = null;
    private boolean m_DefaultPoolingEnabled = false;
    private Vector m_OpenSQLDocuments = new Vector();
    private ConnectionPoolManager m_PoolMgr = new ConnectionPoolManager();
    private Vector m_ParameterList = new Vector();
    private Exception m_Error = null;
    private SQLDocument m_LastSQLDocumentWithError = null;
    private boolean m_FullErrors = false;
    private SQLQueryParser m_QueryParser = new SQLQueryParser();
    private boolean m_IsDefaultPool = false;
    private boolean m_IsStreamingEnabled = true;
    private boolean m_InlineVariables = false;
    private boolean m_IsMultipleResultsEnabled = false;
    private boolean m_IsStatementCachingEnabled = false;

    public XConnection() {
    }

    public XConnection(ExpressionContext expressionContext, String string) {
        this.connect(expressionContext, string);
    }

    public XConnection(ExpressionContext expressionContext, String string, String string2) {
        this.connect(expressionContext, string, string2);
    }

    public XConnection(ExpressionContext expressionContext, NodeList nodeList) {
        this.connect(expressionContext, nodeList);
    }

    public XConnection(ExpressionContext expressionContext, String string, String string2, String string3, String string4) {
        this.connect(expressionContext, string, string2, string3, string4);
    }

    public XConnection(ExpressionContext expressionContext, String string, String string2, Element element) {
        this.connect(expressionContext, string, string2, element);
    }

    public XBooleanStatic connect(ExpressionContext expressionContext, String string) {
        try {
            this.m_ConnectionPool = this.m_PoolMgr.getPool(string);
            if (this.m_ConnectionPool == null) {
                JNDIConnectionPool jNDIConnectionPool = new JNDIConnectionPool(string);
                if (jNDIConnectionPool.testConnection()) {
                    this.m_PoolMgr.registerPool(string, jNDIConnectionPool);
                    this.m_ConnectionPool = jNDIConnectionPool;
                    this.m_IsDefaultPool = false;
                    return new XBooleanStatic(true);
                }
                throw new IllegalArgumentException("Invalid ConnectionPool name or JNDI Datasource path: " + string);
            }
            this.m_IsDefaultPool = false;
            return new XBooleanStatic(true);
        }
        catch (Exception exception) {
            this.setError(exception, expressionContext);
            return new XBooleanStatic(false);
        }
    }

    public XBooleanStatic connect(ExpressionContext expressionContext, String string, String string2) {
        try {
            this.init(string, string2, new Properties());
            return new XBooleanStatic(true);
        }
        catch (SQLException sQLException) {
            this.setError(sQLException, expressionContext);
            return new XBooleanStatic(false);
        }
        catch (Exception exception) {
            this.setError(exception, expressionContext);
            return new XBooleanStatic(false);
        }
    }

    public XBooleanStatic connect(ExpressionContext expressionContext, Element element) {
        try {
            this.initFromElement(element);
            return new XBooleanStatic(true);
        }
        catch (SQLException sQLException) {
            this.setError(sQLException, expressionContext);
            return new XBooleanStatic(false);
        }
        catch (Exception exception) {
            this.setError(exception, expressionContext);
            return new XBooleanStatic(false);
        }
    }

    public XBooleanStatic connect(ExpressionContext expressionContext, NodeList nodeList) {
        try {
            this.initFromElement((Element)nodeList.item(0));
            return new XBooleanStatic(true);
        }
        catch (SQLException sQLException) {
            this.setError(sQLException, expressionContext);
            return new XBooleanStatic(false);
        }
        catch (Exception exception) {
            this.setError(exception, expressionContext);
            return new XBooleanStatic(false);
        }
    }

    public XBooleanStatic connect(ExpressionContext expressionContext, String string, String string2, String string3, String string4) {
        try {
            Properties properties = new Properties();
            properties.put("user", string3);
            properties.put("password", string4);
            this.init(string, string2, properties);
            return new XBooleanStatic(true);
        }
        catch (SQLException sQLException) {
            this.setError(sQLException, expressionContext);
            return new XBooleanStatic(false);
        }
        catch (Exception exception) {
            this.setError(exception, expressionContext);
            return new XBooleanStatic(false);
        }
    }

    public XBooleanStatic connect(ExpressionContext expressionContext, String string, String string2, Element element) {
        try {
            Properties properties = new Properties();
            NamedNodeMap namedNodeMap = element.getAttributes();
            for (int i2 = 0; i2 < namedNodeMap.getLength(); ++i2) {
                properties.put(namedNodeMap.item(i2).getNodeName(), namedNodeMap.item(i2).getNodeValue());
            }
            this.init(string, string2, properties);
            return new XBooleanStatic(true);
        }
        catch (SQLException sQLException) {
            this.setError(sQLException, expressionContext);
            return new XBooleanStatic(false);
        }
        catch (Exception exception) {
            this.setError(exception, expressionContext);
            return new XBooleanStatic(false);
        }
    }

    private void initFromElement(Element element) throws SQLException {
        Properties properties = new Properties();
        String string = "";
        String string2 = "";
        Node node = element.getFirstChild();
        if (null == node) {
            return;
        }
        do {
            String string3;
            Object object;
            Object object2;
            if ((string3 = node.getNodeName()).equalsIgnoreCase("dbdriver")) {
                string = "";
                object = node.getFirstChild();
                if (null != object) {
                    string = object.getNodeValue();
                }
            }
            if (string3.equalsIgnoreCase("dburl")) {
                string2 = "";
                object = node.getFirstChild();
                if (null != object) {
                    string2 = object.getNodeValue();
                }
            }
            if (string3.equalsIgnoreCase("password")) {
                object = "";
                object2 = node.getFirstChild();
                if (null != object2) {
                    object = object2.getNodeValue();
                }
                properties.put("password", object);
            }
            if (string3.equalsIgnoreCase("user")) {
                object = "";
                object2 = node.getFirstChild();
                if (null != object2) {
                    object = object2.getNodeValue();
                }
                properties.put("user", object);
            }
            if (!string3.equalsIgnoreCase("protocol")) continue;
            object = "";
            object2 = node.getAttributes();
            Node node2 = object2.getNamedItem("name");
            if (null == node2) continue;
            String string4 = "";
            object = node2.getNodeValue();
            Node node3 = node.getFirstChild();
            if (null != node3) {
                string4 = node3.getNodeValue();
            }
            properties.put(object, string4);
        } while ((node = node.getNextSibling()) != null);
        this.init(string, string2, properties);
    }

    private void init(String string, String string2, Properties properties) throws SQLException {
        ConnectionPool connectionPool;
        String string3;
        String string4;
        Connection connection = null;
        String string5 = properties.getProperty("user");
        if (string5 == null) {
            string5 = "";
        }
        if ((string3 = properties.getProperty("password")) == null) {
            string3 = "";
        }
        if ((connectionPool = this.m_PoolMgr.getPool(string4 = string + string2 + string5 + string3)) == null) {
            DefaultConnectionPool defaultConnectionPool = new DefaultConnectionPool();
            defaultConnectionPool.setDriver(string);
            defaultConnectionPool.setURL(string2);
            defaultConnectionPool.setProtocol(properties);
            if (this.m_DefaultPoolingEnabled) {
                defaultConnectionPool.setPoolEnabled(true);
            }
            this.m_PoolMgr.registerPool(string4, defaultConnectionPool);
            this.m_ConnectionPool = defaultConnectionPool;
        } else {
            this.m_ConnectionPool = connectionPool;
        }
        this.m_IsDefaultPool = true;
        try {
            connection = this.m_ConnectionPool.getConnection();
        }
        catch (SQLException sQLException) {
            if (connection != null) {
                this.m_ConnectionPool.releaseConnectionOnError(connection);
                connection = null;
            }
            throw sQLException;
        }
        finally {
            if (connection != null) {
                this.m_ConnectionPool.releaseConnection(connection);
            }
        }
    }

    public ConnectionPool getConnectionPool() {
        return this.m_ConnectionPool;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public DTM query(ExpressionContext expressionContext, String string) {
        SQLDocument sQLDocument;
        block7 : {
            sQLDocument = null;
            if (null != this.m_ConnectionPool) break block7;
            DTM dTM = null;
            return dTM;
        }
        try {
            SQLQueryParser sQLQueryParser = this.m_QueryParser.parse(this, string, 1);
            sQLDocument = SQLDocument.getNewDocument(expressionContext);
            sQLDocument.execute(this, sQLQueryParser);
            this.m_OpenSQLDocuments.addElement(sQLDocument);
        }
        catch (Exception exception) {
            if (sQLDocument != null) {
                if (sQLDocument.hasErrors()) {
                    this.setError(exception, sQLDocument, sQLDocument.checkWarnings());
                }
                sQLDocument.close(this.m_IsDefaultPool);
                sQLDocument = null;
            }
        }
        return sQLDocument;
    }

    public DTM pquery(ExpressionContext expressionContext, String string) {
        return this.pquery(expressionContext, string, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public DTM pquery(ExpressionContext expressionContext, String string, String string2) {
        SQLDocument sQLDocument;
        block8 : {
            sQLDocument = null;
            if (null != this.m_ConnectionPool) break block8;
            DTM dTM = null;
            return dTM;
        }
        try {
            SQLQueryParser sQLQueryParser = this.m_QueryParser.parse(this, string, 0);
            if (!this.m_InlineVariables) {
                this.addTypeToData(string2);
                sQLQueryParser.setParameters(this.m_ParameterList);
            }
            sQLDocument = SQLDocument.getNewDocument(expressionContext);
            sQLDocument.execute(this, sQLQueryParser);
            this.m_OpenSQLDocuments.addElement(sQLDocument);
        }
        catch (Exception exception) {
            if (sQLDocument != null) {
                if (sQLDocument.hasErrors()) {
                    this.setError(exception, sQLDocument, sQLDocument.checkWarnings());
                }
                sQLDocument.close(this.m_IsDefaultPool);
                sQLDocument = null;
            }
        }
        return sQLDocument;
    }

    public void skipRec(ExpressionContext expressionContext, Object object, int n2) {
        SQLDocument sQLDocument = null;
        Object var5_5 = null;
        sQLDocument = this.locateSQLDocument(expressionContext, object);
        if (sQLDocument != null) {
            sQLDocument.skip(n2);
        }
    }

    private void addTypeToData(String string) {
        if (string != null && this.m_ParameterList != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(string);
            int n2 = 0;
            while (stringTokenizer.hasMoreTokens()) {
                String string2 = stringTokenizer.nextToken();
                QueryParameter queryParameter = (QueryParameter)this.m_ParameterList.elementAt(n2);
                if (null != queryParameter) {
                    queryParameter.setTypeName(string2);
                }
                ++n2;
            }
        }
    }

    public void addParameter(String string) {
        this.addParameterWithType(string, null);
    }

    public void addParameterWithType(String string, String string2) {
        this.m_ParameterList.addElement(new QueryParameter(string, string2));
    }

    public void addParameterFromElement(Element element) {
        NamedNodeMap namedNodeMap = element.getAttributes();
        Node node = namedNodeMap.getNamedItem("type");
        Node node2 = element.getFirstChild();
        if (null != node2) {
            String string = node2.getNodeValue();
            if (string == null) {
                string = "";
            }
            this.m_ParameterList.addElement(new QueryParameter(string, node.getNodeValue()));
        }
    }

    public void addParameterFromElement(NodeList nodeList) {
        int n2 = nodeList.getLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.addParameters((Element)nodeList.item(i2));
        }
    }

    private void addParameters(Element element) {
        Node node = element.getFirstChild();
        if (null == node) {
            return;
        }
        do {
            if (node.getNodeType() != 1) continue;
            NamedNodeMap namedNodeMap = node.getAttributes();
            Node node2 = namedNodeMap.getNamedItem("type");
            String string = node2 == null ? "string" : node2.getNodeValue();
            Node node3 = node.getFirstChild();
            if (null == node3) continue;
            String string2 = node3.getNodeValue();
            if (string2 == null) {
                string2 = "";
            }
            this.m_ParameterList.addElement(new QueryParameter(string2, string));
        } while ((node = node.getNextSibling()) != null);
    }

    public void clearParameters() {
        this.m_ParameterList.removeAllElements();
    }

    public void enableDefaultConnectionPool() {
        this.m_DefaultPoolingEnabled = true;
        if (this.m_ConnectionPool == null) {
            return;
        }
        if (this.m_IsDefaultPool) {
            return;
        }
        this.m_ConnectionPool.setPoolEnabled(true);
    }

    public void disableDefaultConnectionPool() {
        this.m_DefaultPoolingEnabled = false;
        if (this.m_ConnectionPool == null) {
            return;
        }
        if (!this.m_IsDefaultPool) {
            return;
        }
        this.m_ConnectionPool.setPoolEnabled(false);
    }

    public void enableStreamingMode() {
        this.m_IsStreamingEnabled = true;
    }

    public void disableStreamingMode() {
        this.m_IsStreamingEnabled = false;
    }

    public DTM getError() {
        if (this.m_FullErrors) {
            for (int i2 = 0; i2 < this.m_OpenSQLDocuments.size(); ++i2) {
                SQLDocument sQLDocument = (SQLDocument)this.m_OpenSQLDocuments.elementAt(i2);
                SQLWarning sQLWarning = sQLDocument.checkWarnings();
                if (sQLWarning == null) continue;
                this.setError(null, sQLDocument, sQLWarning);
            }
        }
        return this.buildErrorDocument();
    }

    public void close() throws SQLException {
        while (this.m_OpenSQLDocuments.size() != 0) {
            SQLDocument sQLDocument = (SQLDocument)this.m_OpenSQLDocuments.elementAt(0);
            try {
                sQLDocument.close(this.m_IsDefaultPool);
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.m_OpenSQLDocuments.removeElementAt(0);
        }
        if (null != this.m_Connection) {
            this.m_ConnectionPool.releaseConnection(this.m_Connection);
            this.m_Connection = null;
        }
    }

    public void close(ExpressionContext expressionContext, Object object) throws SQLException {
        SQLDocument sQLDocument = this.locateSQLDocument(expressionContext, object);
        if (sQLDocument != null) {
            sQLDocument.close(this.m_IsDefaultPool);
            this.m_OpenSQLDocuments.remove(sQLDocument);
        }
    }

    private SQLDocument locateSQLDocument(ExpressionContext expressionContext, Object object) {
        try {
            if (object instanceof DTMNodeIterator) {
                DTMNodeIterator dTMNodeIterator = (DTMNodeIterator)object;
                try {
                    DTMNodeProxy dTMNodeProxy = (DTMNodeProxy)dTMNodeIterator.getRoot();
                    return (SQLDocument)dTMNodeProxy.getDTM();
                }
                catch (Exception exception) {
                    XNodeSet xNodeSet = (XNodeSet)dTMNodeIterator.getDTMIterator();
                    DTMIterator dTMIterator = xNodeSet.getContainedIter();
                    DTM dTM = dTMIterator.getDTM(xNodeSet.nextNode());
                    return (SQLDocument)dTM;
                }
            }
            this.setError(new Exception("SQL Extension:close - Can Not Identify SQLDocument"), expressionContext);
            return null;
        }
        catch (Exception exception) {
            this.setError(exception, expressionContext);
            return null;
        }
    }

    private SQLErrorDocument buildErrorDocument() {
        SQLErrorDocument sQLErrorDocument = null;
        if (this.m_LastSQLDocumentWithError != null) {
            ExpressionContext expressionContext = this.m_LastSQLDocumentWithError.getExpressionContext();
            SQLWarning sQLWarning = this.m_LastSQLDocumentWithError.checkWarnings();
            try {
                DTMManager dTMManager = ((XPathContext.XPathExpressionContext)expressionContext).getDTMManager();
                DTMManagerDefault dTMManagerDefault = (DTMManagerDefault)dTMManager;
                int n2 = dTMManagerDefault.getFirstFreeDTMID();
                sQLErrorDocument = new SQLErrorDocument(dTMManager, n2 << 16, this.m_Error, sQLWarning, this.m_FullErrors);
                dTMManagerDefault.addDTM(sQLErrorDocument, n2);
                this.m_Error = null;
                this.m_LastSQLDocumentWithError = null;
            }
            catch (Exception exception) {
                sQLErrorDocument = null;
            }
        }
        return sQLErrorDocument;
    }

    public void setError(Exception exception, ExpressionContext expressionContext) {
        try {
            ErrorListener errorListener = expressionContext.getErrorListener();
            if (errorListener != null && exception != null) {
                errorListener.warning(new TransformerException(exception.toString(), expressionContext.getXPathContext().getSAXLocator(), exception));
            }
        }
        catch (Exception exception2) {
            // empty catch block
        }
    }

    public void setError(Exception exception, SQLDocument sQLDocument, SQLWarning sQLWarning) {
        ExpressionContext expressionContext = sQLDocument.getExpressionContext();
        this.m_LastSQLDocumentWithError = sQLDocument;
        try {
            ErrorListener errorListener = expressionContext.getErrorListener();
            if (errorListener != null && exception != null) {
                errorListener.warning(new TransformerException(exception.toString(), expressionContext.getXPathContext().getSAXLocator(), exception));
            }
            if (errorListener != null && sQLWarning != null) {
                errorListener.warning(new TransformerException(sQLWarning.toString(), expressionContext.getXPathContext().getSAXLocator(), sQLWarning));
            }
            if (exception != null) {
                this.m_Error = exception;
            }
            if (sQLWarning != null) {
                SQLWarning sQLWarning2 = new SQLWarning(sQLWarning.getMessage(), sQLWarning.getSQLState(), sQLWarning.getErrorCode());
                for (SQLWarning sQLWarning3 = sQLWarning.getNextWarning(); sQLWarning3 != null; sQLWarning3 = sQLWarning3.getNextWarning()) {
                    sQLWarning2.setNextWarning(new SQLWarning(sQLWarning3.getMessage(), sQLWarning3.getSQLState(), sQLWarning3.getErrorCode()));
                }
                sQLWarning2.setNextWarning(new SQLWarning(sQLWarning.getMessage(), sQLWarning.getSQLState(), sQLWarning.getErrorCode()));
            }
        }
        catch (Exception exception2) {
            // empty catch block
        }
    }

    public void setFeature(String string, String string2) {
        boolean bl = false;
        if ("true".equalsIgnoreCase(string2)) {
            bl = true;
        }
        if ("streaming".equalsIgnoreCase(string)) {
            this.m_IsStreamingEnabled = bl;
        } else if ("inline-variables".equalsIgnoreCase(string)) {
            this.m_InlineVariables = bl;
        } else if ("multiple-results".equalsIgnoreCase(string)) {
            this.m_IsMultipleResultsEnabled = bl;
        } else if ("cache-statements".equalsIgnoreCase(string)) {
            this.m_IsStatementCachingEnabled = bl;
        } else if ("default-pool-enabled".equalsIgnoreCase(string)) {
            this.m_DefaultPoolingEnabled = bl;
            if (this.m_ConnectionPool == null) {
                return;
            }
            if (this.m_IsDefaultPool) {
                return;
            }
            this.m_ConnectionPool.setPoolEnabled(bl);
        } else if ("full-errors".equalsIgnoreCase(string)) {
            this.m_FullErrors = bl;
        }
    }

    public String getFeature(String string) {
        String string2 = null;
        if ("streaming".equalsIgnoreCase(string)) {
            string2 = this.m_IsStreamingEnabled ? "true" : "false";
        } else if ("inline-variables".equalsIgnoreCase(string)) {
            string2 = this.m_InlineVariables ? "true" : "false";
        } else if ("multiple-results".equalsIgnoreCase(string)) {
            string2 = this.m_IsMultipleResultsEnabled ? "true" : "false";
        } else if ("cache-statements".equalsIgnoreCase(string)) {
            string2 = this.m_IsStatementCachingEnabled ? "true" : "false";
        } else if ("default-pool-enabled".equalsIgnoreCase(string)) {
            string2 = this.m_DefaultPoolingEnabled ? "true" : "false";
        } else if ("full-errors".equalsIgnoreCase(string)) {
            string2 = this.m_FullErrors ? "true" : "false";
        }
        return string2;
    }

    protected void finalize() {
        try {
            this.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

