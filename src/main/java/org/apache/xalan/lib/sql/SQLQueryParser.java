/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xalan.lib.sql;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.lib.sql.QueryParameter;
import org.apache.xalan.lib.sql.XConnection;
import org.apache.xml.utils.QName;
import org.apache.xpath.objects.XObject;

public class SQLQueryParser {
    private boolean m_InlineVariables = false;
    private boolean m_IsCallable = false;
    private String m_OrigQuery = null;
    private StringBuffer m_ParsedQuery = null;
    private Vector m_Parameters = null;
    private boolean m_hasOutput = false;
    private boolean m_HasParameters;
    public static final int NO_OVERRIDE = 0;
    public static final int NO_INLINE_PARSER = 1;
    public static final int INLINE_PARSER = 2;

    public SQLQueryParser() {
        this.init();
    }

    private SQLQueryParser(String string) {
        this.m_OrigQuery = string;
    }

    private void init() {
    }

    public SQLQueryParser parse(XConnection xConnection, String string, int n2) {
        SQLQueryParser sQLQueryParser = new SQLQueryParser(string);
        sQLQueryParser.parse(xConnection, n2);
        return sQLQueryParser;
    }

    private void parse(XConnection xConnection, int n2) {
        this.m_InlineVariables = "true".equals(xConnection.getFeature("inline-variables"));
        if (n2 == 1) {
            this.m_InlineVariables = false;
        } else if (n2 == 2) {
            this.m_InlineVariables = true;
        }
        if (this.m_InlineVariables) {
            this.inlineParser();
        }
    }

    public boolean hasParameters() {
        return this.m_HasParameters;
    }

    public boolean isCallable() {
        return this.m_IsCallable;
    }

    public Vector getParameters() {
        return this.m_Parameters;
    }

    public void setParameters(Vector vector) {
        this.m_HasParameters = true;
        this.m_Parameters = vector;
    }

    public String getSQLQuery() {
        if (this.m_InlineVariables) {
            return this.m_ParsedQuery.toString();
        }
        return this.m_OrigQuery;
    }

    public void populateStatement(PreparedStatement preparedStatement, ExpressionContext expressionContext) {
        for (int i2 = 0; i2 < this.m_Parameters.size(); ++i2) {
            QueryParameter queryParameter = (QueryParameter)this.m_Parameters.elementAt(i2);
            try {
                Object object;
                if (this.m_InlineVariables) {
                    object = expressionContext.getVariableOrParam(new QName(queryParameter.getName()));
                    if (object != null) {
                        preparedStatement.setObject(i2 + 1, object.object(), queryParameter.getType(), 4);
                        continue;
                    }
                    preparedStatement.setNull(i2 + 1, queryParameter.getType());
                    continue;
                }
                object = queryParameter.getValue();
                if (object != null) {
                    preparedStatement.setObject(i2 + 1, object, queryParameter.getType(), 4);
                    continue;
                }
                preparedStatement.setNull(i2 + 1, queryParameter.getType());
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public void registerOutputParameters(CallableStatement callableStatement) throws SQLException {
        if (this.m_IsCallable && this.m_hasOutput) {
            for (int i2 = 0; i2 < this.m_Parameters.size(); ++i2) {
                QueryParameter queryParameter = (QueryParameter)this.m_Parameters.elementAt(i2);
                if (!queryParameter.isOutput()) continue;
                callableStatement.registerOutParameter(i2 + 1, queryParameter.getType());
            }
        }
    }

    protected void inlineParser() {
        QueryParameter queryParameter = null;
        int n2 = 0;
        StringBuffer stringBuffer = new StringBuffer();
        boolean bl = true;
        if (this.m_Parameters == null) {
            this.m_Parameters = new Vector();
        }
        if (this.m_ParsedQuery == null) {
            this.m_ParsedQuery = new StringBuffer();
        }
        block11 : for (int i2 = 0; i2 < this.m_OrigQuery.length(); ++i2) {
            char c2 = this.m_OrigQuery.charAt(i2);
            switch (n2) {
                case 0: {
                    if (c2 == '\'') {
                        n2 = 1;
                    } else if (c2 == '?') {
                        n2 = 4;
                    } else if (bl && (Character.isLetterOrDigit(c2) || c2 == '#')) {
                        stringBuffer.append(c2);
                        n2 = 3;
                    }
                    this.m_ParsedQuery.append(c2);
                    continue block11;
                }
                case 1: {
                    if (c2 == '\'') {
                        n2 = 0;
                    } else if (c2 == '\\') {
                        n2 = 2;
                    }
                    this.m_ParsedQuery.append(c2);
                    continue block11;
                }
                case 2: {
                    n2 = 1;
                    this.m_ParsedQuery.append(c2);
                    continue block11;
                }
                case 3: {
                    if (Character.isLetterOrDigit(c2) || c2 == '#' || c2 == '_') {
                        stringBuffer.append(c2);
                    } else {
                        if (stringBuffer.toString().equalsIgnoreCase("call")) {
                            this.m_IsCallable = true;
                            if (queryParameter != null) {
                                queryParameter.setIsOutput(true);
                            }
                        }
                        bl = false;
                        stringBuffer = new StringBuffer();
                        n2 = c2 == '\'' ? 1 : (c2 == '?' ? 4 : 0);
                    }
                    this.m_ParsedQuery.append(c2);
                    continue block11;
                }
                case 4: {
                    if (c2 != '[') continue block11;
                    n2 = 5;
                    continue block11;
                }
                case 5: {
                    if (!Character.isWhitespace(c2) && c2 != '=') {
                        stringBuffer.append(Character.toUpperCase(c2));
                        continue block11;
                    }
                    if (stringBuffer.length() <= 0) continue block11;
                    this.m_HasParameters = true;
                    queryParameter = new QueryParameter();
                    queryParameter.setTypeName(stringBuffer.toString());
                    this.m_Parameters.addElement(queryParameter);
                    stringBuffer = new StringBuffer();
                    if (c2 == '=') {
                        n2 = 7;
                        continue block11;
                    }
                    n2 = 6;
                    continue block11;
                }
                case 6: {
                    if (c2 != '=') continue block11;
                    n2 = 7;
                    continue block11;
                }
                case 7: {
                    if (!Character.isWhitespace(c2) && c2 != ']') {
                        stringBuffer.append(c2);
                        continue block11;
                    }
                    if (stringBuffer.length() <= 0) continue block11;
                    queryParameter.setName(stringBuffer.toString());
                    stringBuffer = new StringBuffer();
                    if (c2 == ']') {
                        n2 = 0;
                        continue block11;
                    }
                    n2 = 8;
                    continue block11;
                }
                case 8: {
                    if (!Character.isWhitespace(c2) && c2 != ']') {
                        stringBuffer.append(c2);
                        continue block11;
                    }
                    if (stringBuffer.length() <= 0) continue block11;
                    stringBuffer.setLength(3);
                    if (stringBuffer.toString().equalsIgnoreCase("OUT")) {
                        queryParameter.setIsOutput(true);
                        this.m_hasOutput = true;
                    }
                    stringBuffer = new StringBuffer();
                    if (c2 != ']') continue block11;
                    n2 = 0;
                }
            }
        }
        if (this.m_IsCallable) {
            this.m_ParsedQuery.insert(0, '{');
            this.m_ParsedQuery.append('}');
        }
    }
}

