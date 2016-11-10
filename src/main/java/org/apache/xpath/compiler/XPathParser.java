/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xpath.compiler;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.ObjectVector;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathProcessorException;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.compiler.Keywords;
import org.apache.xpath.compiler.Lexer;
import org.apache.xpath.compiler.OpMap;
import org.apache.xpath.domapi.XPathStylesheetDOM3Exception;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XString;

public class XPathParser {
    private OpMap m_ops;
    transient String m_token;
    transient char m_tokenChar = '\u0000';
    int m_queueMark = 0;
    PrefixResolver m_namespaceContext;
    private ErrorListener m_errorListener;
    SourceLocator m_sourceLocator;
    private FunctionTable m_functionTable;

    public XPathParser(ErrorListener errorListener, SourceLocator sourceLocator) {
        this.m_errorListener = errorListener;
        this.m_sourceLocator = sourceLocator;
    }

    public void initXPath(Compiler compiler, String string, PrefixResolver prefixResolver) throws TransformerException {
        this.m_ops = compiler;
        this.m_namespaceContext = prefixResolver;
        this.m_functionTable = compiler.getFunctionTable();
        Lexer lexer = new Lexer(compiler, prefixResolver, this);
        lexer.tokenize(string);
        this.m_ops.setOp(0, 1);
        this.m_ops.setOp(1, 2);
        try {
            this.nextToken();
            this.Expr();
            if (null != this.m_token) {
                String string2 = "";
                while (null != this.m_token) {
                    string2 = string2 + "'" + this.m_token + "'";
                    this.nextToken();
                    if (null == this.m_token) continue;
                    string2 = string2 + ", ";
                }
                this.error("ER_EXTRA_ILLEGAL_TOKENS", new Object[]{string2});
            }
        }
        catch (XPathProcessorException xPathProcessorException) {
            if ("CONTINUE_AFTER_FATAL_ERROR".equals(xPathProcessorException.getMessage())) {
                this.initXPath(compiler, "/..", prefixResolver);
            }
            throw xPathProcessorException;
        }
        compiler.shrink();
    }

    public void initMatchPattern(Compiler compiler, String string, PrefixResolver prefixResolver) throws TransformerException {
        this.m_ops = compiler;
        this.m_namespaceContext = prefixResolver;
        this.m_functionTable = compiler.getFunctionTable();
        Lexer lexer = new Lexer(compiler, prefixResolver, this);
        lexer.tokenize(string);
        this.m_ops.setOp(0, 30);
        this.m_ops.setOp(1, 2);
        this.nextToken();
        this.Pattern();
        if (null != this.m_token) {
            String string2 = "";
            while (null != this.m_token) {
                string2 = string2 + "'" + this.m_token + "'";
                this.nextToken();
                if (null == this.m_token) continue;
                string2 = string2 + ", ";
            }
            this.error("ER_EXTRA_ILLEGAL_TOKENS", new Object[]{string2});
        }
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.shrink();
    }

    public ErrorListener getErrorListener() {
        return this.m_errorListener;
    }

    final boolean tokenIs(String string) {
        return this.m_token != null ? this.m_token.equals(string) : string == null;
    }

    final boolean tokenIs(char c2) {
        return this.m_token != null ? this.m_tokenChar == c2 : false;
    }

    final boolean lookahead(char c2, int n2) {
        String string;
        int n3 = this.m_queueMark + n2;
        boolean bl = n3 <= this.m_ops.getTokenQueueSize() && n3 > 0 && this.m_ops.getTokenQueueSize() != 0 ? ((string = (String)this.m_ops.m_tokenQueue.elementAt(n3 - 1)).length() == 1 ? string.charAt(0) == c2 : false) : false;
        return bl;
    }

    private final boolean lookahead(String string, int n2) {
        String string2;
        boolean bl = this.m_queueMark + n2 <= this.m_ops.getTokenQueueSize() ? ((string2 = (String)this.m_ops.m_tokenQueue.elementAt(this.m_queueMark + (n2 - 1))) != null ? string2.equals(string) : string == null) : null == string;
        return bl;
    }

    private final void nextToken() {
        if (this.m_queueMark < this.m_ops.getTokenQueueSize()) {
            this.m_token = (String)this.m_ops.m_tokenQueue.elementAt(this.m_queueMark++);
            this.m_tokenChar = this.m_token.charAt(0);
        } else {
            this.m_token = null;
            this.m_tokenChar = '\u0000';
        }
    }

    private final void consumeExpected(char c2) throws TransformerException {
        if (!this.tokenIs(c2)) {
            this.error("ER_EXPECTED_BUT_FOUND", new Object[]{String.valueOf(c2), this.m_token});
            throw new XPathProcessorException("CONTINUE_AFTER_FATAL_ERROR");
        }
        this.nextToken();
    }

    void error(String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createXPATHMessage(string, arrobject);
        ErrorListener errorListener = this.getErrorListener();
        TransformerException transformerException = new TransformerException(string2, this.m_sourceLocator);
        if (null == errorListener) {
            throw transformerException;
        }
        errorListener.fatalError(transformerException);
    }

    void errorForDOM3(String string, Object[] arrobject) throws TransformerException {
        String string2 = XSLMessages.createXPATHMessage(string, arrobject);
        ErrorListener errorListener = this.getErrorListener();
        XPathStylesheetDOM3Exception xPathStylesheetDOM3Exception = new XPathStylesheetDOM3Exception(string2, this.m_sourceLocator);
        if (null == errorListener) {
            throw xPathStylesheetDOM3Exception;
        }
        errorListener.fatalError(xPathStylesheetDOM3Exception);
    }

    final int getFunctionToken(String string) {
        int n2;
        try {
            Object object = Keywords.lookupNodeTest(string);
            if (null == object) {
                object = this.m_functionTable.getFunctionID(string);
            }
            n2 = (Integer)object;
        }
        catch (NullPointerException nullPointerException) {
            n2 = -1;
        }
        catch (ClassCastException classCastException) {
            n2 = -1;
        }
        return n2;
    }

    void insertOp(int n2, int n3, int n4) {
        int n5 = this.m_ops.getOp(1);
        for (int i2 = n5 - 1; i2 >= n2; --i2) {
            this.m_ops.setOp(i2 + n3, this.m_ops.getOp(i2));
        }
        this.m_ops.setOp(n2, n4);
        this.m_ops.setOp(1, n5 + n3);
    }

    void appendOp(int n2, int n3) {
        int n4 = this.m_ops.getOp(1);
        this.m_ops.setOp(n4, n3);
        this.m_ops.setOp(n4 + 1, n2);
        this.m_ops.setOp(1, n4 + n2);
    }

    protected void Expr() throws TransformerException {
        this.OrExpr();
    }

    protected void OrExpr() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        this.AndExpr();
        if (null != this.m_token && this.tokenIs("or")) {
            this.nextToken();
            this.insertOp(n2, 2, 2);
            this.OrExpr();
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
        }
    }

    protected void AndExpr() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        this.EqualityExpr(-1);
        if (null != this.m_token && this.tokenIs("and")) {
            this.nextToken();
            this.insertOp(n2, 2, 3);
            this.AndExpr();
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
        }
    }

    protected int EqualityExpr(int n2) throws TransformerException {
        int n3 = this.m_ops.getOp(1);
        if (-1 == n2) {
            n2 = n3;
        }
        this.RelationalExpr(-1);
        if (null != this.m_token) {
            if (this.tokenIs('!') && this.lookahead('=', 1)) {
                this.nextToken();
                this.nextToken();
                this.insertOp(n2, 2, 4);
                int n4 = this.m_ops.getOp(1) - n2;
                n2 = this.EqualityExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n4 + 1) + n4);
                n2 += 2;
            } else if (this.tokenIs('=')) {
                this.nextToken();
                this.insertOp(n2, 2, 5);
                int n5 = this.m_ops.getOp(1) - n2;
                n2 = this.EqualityExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n5 + 1) + n5);
                n2 += 2;
            }
        }
        return n2;
    }

    protected int RelationalExpr(int n2) throws TransformerException {
        int n3 = this.m_ops.getOp(1);
        if (-1 == n2) {
            n2 = n3;
        }
        this.AdditiveExpr(-1);
        if (null != this.m_token) {
            if (this.tokenIs('<')) {
                this.nextToken();
                if (this.tokenIs('=')) {
                    this.nextToken();
                    this.insertOp(n2, 2, 6);
                } else {
                    this.insertOp(n2, 2, 7);
                }
                int n4 = this.m_ops.getOp(1) - n2;
                n2 = this.RelationalExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n4 + 1) + n4);
                n2 += 2;
            } else if (this.tokenIs('>')) {
                this.nextToken();
                if (this.tokenIs('=')) {
                    this.nextToken();
                    this.insertOp(n2, 2, 8);
                } else {
                    this.insertOp(n2, 2, 9);
                }
                int n5 = this.m_ops.getOp(1) - n2;
                n2 = this.RelationalExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n5 + 1) + n5);
                n2 += 2;
            }
        }
        return n2;
    }

    protected int AdditiveExpr(int n2) throws TransformerException {
        int n3 = this.m_ops.getOp(1);
        if (-1 == n2) {
            n2 = n3;
        }
        this.MultiplicativeExpr(-1);
        if (null != this.m_token) {
            if (this.tokenIs('+')) {
                this.nextToken();
                this.insertOp(n2, 2, 10);
                int n4 = this.m_ops.getOp(1) - n2;
                n2 = this.AdditiveExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n4 + 1) + n4);
                n2 += 2;
            } else if (this.tokenIs('-')) {
                this.nextToken();
                this.insertOp(n2, 2, 11);
                int n5 = this.m_ops.getOp(1) - n2;
                n2 = this.AdditiveExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n5 + 1) + n5);
                n2 += 2;
            }
        }
        return n2;
    }

    protected int MultiplicativeExpr(int n2) throws TransformerException {
        int n3 = this.m_ops.getOp(1);
        if (-1 == n2) {
            n2 = n3;
        }
        this.UnaryExpr();
        if (null != this.m_token) {
            if (this.tokenIs('*')) {
                this.nextToken();
                this.insertOp(n2, 2, 12);
                int n4 = this.m_ops.getOp(1) - n2;
                n2 = this.MultiplicativeExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n4 + 1) + n4);
                n2 += 2;
            } else if (this.tokenIs("div")) {
                this.nextToken();
                this.insertOp(n2, 2, 13);
                int n5 = this.m_ops.getOp(1) - n2;
                n2 = this.MultiplicativeExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n5 + 1) + n5);
                n2 += 2;
            } else if (this.tokenIs("mod")) {
                this.nextToken();
                this.insertOp(n2, 2, 14);
                int n6 = this.m_ops.getOp(1) - n2;
                n2 = this.MultiplicativeExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n6 + 1) + n6);
                n2 += 2;
            } else if (this.tokenIs("quo")) {
                this.nextToken();
                this.insertOp(n2, 2, 15);
                int n7 = this.m_ops.getOp(1) - n2;
                n2 = this.MultiplicativeExpr(n2);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(n2 + n7 + 1) + n7);
                n2 += 2;
            }
        }
        return n2;
    }

    protected void UnaryExpr() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        boolean bl = false;
        if (this.m_tokenChar == '-') {
            this.nextToken();
            this.appendOp(2, 16);
            bl = true;
        }
        this.UnionExpr();
        if (bl) {
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
        }
    }

    protected void UnionExpr() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        boolean bl = true;
        boolean bl2 = false;
        do {
            this.PathExpr();
            if (!this.tokenIs('|')) break;
            if (!bl2) {
                bl2 = true;
                this.insertOp(n2, 2, 20);
            }
            this.nextToken();
        } while (bl);
        this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
    }

    protected void PathExpr() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        int n3 = this.FilterExpr();
        if (n3 != 0) {
            boolean bl;
            boolean bl2 = bl = n3 == 2;
            if (this.tokenIs('/')) {
                this.nextToken();
                if (!bl) {
                    this.insertOp(n2, 2, 28);
                    bl = true;
                }
                if (!this.RelativeLocationPath()) {
                    this.error("ER_EXPECTED_REL_LOC_PATH", null);
                }
            }
            if (bl) {
                this.m_ops.setOp(this.m_ops.getOp(1), -1);
                this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
                this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
            }
        } else {
            this.LocationPath();
        }
    }

    protected int FilterExpr() throws TransformerException {
        int n2;
        int n3 = this.m_ops.getOp(1);
        if (this.PrimaryExpr()) {
            if (this.tokenIs('[')) {
                this.insertOp(n3, 2, 28);
                while (this.tokenIs('[')) {
                    this.Predicate();
                }
                n2 = 2;
            } else {
                n2 = 1;
            }
        } else {
            n2 = 0;
        }
        return n2;
    }

    protected boolean PrimaryExpr() throws TransformerException {
        boolean bl;
        int n2 = this.m_ops.getOp(1);
        if (this.m_tokenChar == '\'' || this.m_tokenChar == '\"') {
            this.appendOp(2, 21);
            this.Literal();
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
            bl = true;
        } else if (this.m_tokenChar == '$') {
            this.nextToken();
            this.appendOp(2, 22);
            this.QName();
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
            bl = true;
        } else if (this.m_tokenChar == '(') {
            this.nextToken();
            this.appendOp(2, 23);
            this.Expr();
            this.consumeExpected(')');
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
            bl = true;
        } else if (null != this.m_token && ('.' == this.m_tokenChar && this.m_token.length() > 1 && Character.isDigit(this.m_token.charAt(1)) || Character.isDigit(this.m_tokenChar))) {
            this.appendOp(2, 27);
            this.Number();
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
            bl = true;
        } else {
            bl = this.lookahead('(', 1) || this.lookahead(':', 1) && this.lookahead('(', 3) ? this.FunctionCall() : false;
        }
        return bl;
    }

    protected void Argument() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        this.appendOp(2, 26);
        this.Expr();
        this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
    }

    protected boolean FunctionCall() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        if (this.lookahead(':', 1)) {
            this.appendOp(4, 24);
            this.m_ops.setOp(n2 + 1 + 1, this.m_queueMark - 1);
            this.nextToken();
            this.consumeExpected(':');
            this.m_ops.setOp(n2 + 1 + 2, this.m_queueMark - 1);
            this.nextToken();
        } else {
            int n3 = this.getFunctionToken(this.m_token);
            if (-1 == n3) {
                this.error("ER_COULDNOT_FIND_FUNCTION", new Object[]{this.m_token});
            }
            switch (n3) {
                case 1030: 
                case 1031: 
                case 1032: 
                case 1033: {
                    return false;
                }
            }
            this.appendOp(3, 25);
            this.m_ops.setOp(n2 + 1 + 1, n3);
            this.nextToken();
        }
        this.consumeExpected('(');
        while (!this.tokenIs(')') && this.m_token != null) {
            if (this.tokenIs(',')) {
                this.error("ER_FOUND_COMMA_BUT_NO_PRECEDING_ARG", null);
            }
            this.Argument();
            if (this.tokenIs(')')) continue;
            this.consumeExpected(',');
            if (!this.tokenIs(')')) continue;
            this.error("ER_FOUND_COMMA_BUT_NO_FOLLOWING_ARG", null);
        }
        this.consumeExpected(')');
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
        return true;
    }

    protected void LocationPath() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        this.appendOp(2, 28);
        boolean bl = this.tokenIs('/');
        if (bl) {
            this.appendOp(4, 50);
            this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
            this.m_ops.setOp(this.m_ops.getOp(1) - 1, 35);
            this.nextToken();
        } else if (this.m_token == null) {
            this.error("ER_EXPECTED_LOC_PATH_AT_END_EXPR", null);
        }
        if (this.m_token != null && !this.RelativeLocationPath() && !bl) {
            this.error("ER_EXPECTED_LOC_PATH", new Object[]{this.m_token});
        }
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
    }

    protected boolean RelativeLocationPath() throws TransformerException {
        if (!this.Step()) {
            return false;
        }
        while (this.tokenIs('/')) {
            this.nextToken();
            if (this.Step()) continue;
            this.error("ER_EXPECTED_LOC_STEP", null);
        }
        return true;
    }

    protected boolean Step() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        boolean bl = this.tokenIs('/');
        if (bl) {
            this.nextToken();
            this.appendOp(2, 42);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            this.m_ops.setOp(this.m_ops.getOp(1), 1033);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            this.m_ops.setOp(n2 + 1 + 1, this.m_ops.getOp(1) - n2);
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
            n2 = this.m_ops.getOp(1);
        }
        if (this.tokenIs(".")) {
            this.nextToken();
            if (this.tokenIs('[')) {
                this.error("ER_PREDICATE_ILLEGAL_SYNTAX", null);
            }
            this.appendOp(4, 48);
            this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
            this.m_ops.setOp(this.m_ops.getOp(1) - 1, 1033);
        } else if (this.tokenIs("..")) {
            this.nextToken();
            this.appendOp(4, 45);
            this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
            this.m_ops.setOp(this.m_ops.getOp(1) - 1, 1033);
        } else if (this.tokenIs('*') || this.tokenIs('@') || this.tokenIs('_') || this.m_token != null && Character.isLetter(this.m_token.charAt(0))) {
            this.Basis();
            while (this.tokenIs('[')) {
                this.Predicate();
            }
            this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
        } else {
            if (bl) {
                this.error("ER_EXPECTED_LOC_STEP", null);
            }
            return false;
        }
        return true;
    }

    protected void Basis() throws TransformerException {
        int n2;
        int n3 = this.m_ops.getOp(1);
        if (this.lookahead("::", 1)) {
            n2 = this.AxisName();
            this.nextToken();
            this.nextToken();
        } else if (this.tokenIs('@')) {
            n2 = 39;
            this.appendOp(2, n2);
            this.nextToken();
        } else {
            n2 = 40;
            this.appendOp(2, n2);
        }
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.NodeTest(n2);
        this.m_ops.setOp(n3 + 1 + 1, this.m_ops.getOp(1) - n3);
    }

    protected int AxisName() throws TransformerException {
        Object object = Keywords.getAxisName(this.m_token);
        if (null == object) {
            this.error("ER_ILLEGAL_AXIS_NAME", new Object[]{this.m_token});
        }
        int n2 = (Integer)object;
        this.appendOp(2, n2);
        return n2;
    }

    protected void NodeTest(int n2) throws TransformerException {
        if (this.lookahead('(', 1)) {
            Object object = Keywords.getNodeType(this.m_token);
            if (null == object) {
                this.error("ER_UNKNOWN_NODETYPE", new Object[]{this.m_token});
            } else {
                this.nextToken();
                int n3 = (Integer)object;
                this.m_ops.setOp(this.m_ops.getOp(1), n3);
                this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
                this.consumeExpected('(');
                if (1032 == n3 && !this.tokenIs(')')) {
                    this.Literal();
                }
                this.consumeExpected(')');
            }
        } else {
            this.m_ops.setOp(this.m_ops.getOp(1), 34);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            if (this.lookahead(':', 1)) {
                if (this.tokenIs('*')) {
                    this.m_ops.setOp(this.m_ops.getOp(1), -3);
                } else {
                    this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
                    if (!Character.isLetter(this.m_tokenChar) && !this.tokenIs('_')) {
                        this.error("ER_EXPECTED_NODE_TEST", null);
                    }
                }
                this.nextToken();
                this.consumeExpected(':');
            } else {
                this.m_ops.setOp(this.m_ops.getOp(1), -2);
            }
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            if (this.tokenIs('*')) {
                this.m_ops.setOp(this.m_ops.getOp(1), -3);
            } else {
                this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
                if (!Character.isLetter(this.m_tokenChar) && !this.tokenIs('_')) {
                    this.error("ER_EXPECTED_NODE_TEST", null);
                }
            }
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            this.nextToken();
        }
    }

    protected void Predicate() throws TransformerException {
        if (this.tokenIs('[')) {
            this.nextToken();
            this.PredicateExpr();
            this.consumeExpected(']');
        }
    }

    protected void PredicateExpr() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        this.appendOp(2, 29);
        this.Expr();
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
    }

    protected void QName() throws TransformerException {
        if (this.lookahead(':', 1)) {
            this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            this.nextToken();
            this.consumeExpected(':');
        } else {
            this.m_ops.setOp(this.m_ops.getOp(1), -2);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        }
        this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.nextToken();
    }

    protected void Literal() throws TransformerException {
        int n2 = this.m_token.length() - 1;
        char c2 = this.m_tokenChar;
        char c3 = this.m_token.charAt(n2);
        if (c2 == '\"' && c3 == '\"' || c2 == '\'' && c3 == '\'') {
            int n3 = this.m_queueMark - 1;
            this.m_ops.m_tokenQueue.setElementAt(null, n3);
            XString xString = new XString(this.m_token.substring(1, n2));
            this.m_ops.m_tokenQueue.setElementAt(xString, n3);
            this.m_ops.setOp(this.m_ops.getOp(1), n3);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            this.nextToken();
        } else {
            this.error("ER_PATTERN_LITERAL_NEEDS_BE_QUOTED", new Object[]{this.m_token});
        }
    }

    protected void Number() throws TransformerException {
        if (null != this.m_token) {
            double d2;
            try {
                if (this.m_token.indexOf(101) > -1 || this.m_token.indexOf(69) > -1) {
                    throw new NumberFormatException();
                }
                d2 = Double.valueOf(this.m_token);
            }
            catch (NumberFormatException numberFormatException) {
                d2 = 0.0;
                this.error("ER_COULDNOT_BE_FORMATTED_TO_NUMBER", new Object[]{this.m_token});
            }
            this.m_ops.m_tokenQueue.setElementAt(new XNumber(d2), this.m_queueMark - 1);
            this.m_ops.setOp(this.m_ops.getOp(1), this.m_queueMark - 1);
            this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
            this.nextToken();
        }
    }

    protected void Pattern() throws TransformerException {
        do {
            this.LocationPathPattern();
            if (!this.tokenIs('|')) break;
            this.nextToken();
        } while (true);
    }

    protected void LocationPathPattern() throws TransformerException {
        int n2 = this.m_ops.getOp(1);
        boolean bl = false;
        boolean bl2 = true;
        int n3 = 2;
        int n4 = 0;
        this.appendOp(2, 31);
        if (this.lookahead('(', 1) && (this.tokenIs("id") || this.tokenIs("key"))) {
            this.IdKeyPattern();
            if (this.tokenIs('/')) {
                this.nextToken();
                if (this.tokenIs('/')) {
                    this.appendOp(4, 52);
                    this.nextToken();
                } else {
                    this.appendOp(4, 53);
                }
                this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
                this.m_ops.setOp(this.m_ops.getOp(1) - 1, 1034);
                n4 = 2;
            }
        } else if (this.tokenIs('/')) {
            if (this.lookahead('/', 1)) {
                this.appendOp(4, 52);
                this.nextToken();
                n4 = 2;
            } else {
                this.appendOp(4, 50);
                n4 = 1;
            }
            this.m_ops.setOp(this.m_ops.getOp(1) - 2, 4);
            this.m_ops.setOp(this.m_ops.getOp(1) - 1, 35);
            this.nextToken();
        } else {
            n4 = 2;
        }
        if (n4 != 0) {
            if (!this.tokenIs('|') && null != this.m_token) {
                this.RelativePathPattern();
            } else if (n4 == 2) {
                this.error("ER_EXPECTED_REL_PATH_PATTERN", null);
            }
        }
        this.m_ops.setOp(this.m_ops.getOp(1), -1);
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.m_ops.setOp(n2 + 1, this.m_ops.getOp(1) - n2);
    }

    protected void IdKeyPattern() throws TransformerException {
        this.FunctionCall();
    }

    protected void RelativePathPattern() throws TransformerException {
        boolean bl = this.StepPattern(false);
        while (this.tokenIs('/')) {
            this.nextToken();
            bl = this.StepPattern(!bl);
        }
    }

    protected boolean StepPattern(boolean bl) throws TransformerException {
        return this.AbbreviatedNodeTestStep(bl);
    }

    protected boolean AbbreviatedNodeTestStep(boolean bl) throws TransformerException {
        int n2;
        boolean bl2;
        int n3 = this.m_ops.getOp(1);
        int n4 = -1;
        if (this.tokenIs('@')) {
            n2 = 51;
            this.appendOp(2, n2);
            this.nextToken();
        } else if (this.lookahead("::", 1)) {
            if (this.tokenIs("attribute")) {
                n2 = 51;
                this.appendOp(2, n2);
            } else if (this.tokenIs("child")) {
                n4 = this.m_ops.getOp(1);
                n2 = 53;
                this.appendOp(2, n2);
            } else {
                n2 = -1;
                this.error("ER_AXES_NOT_ALLOWED", new Object[]{this.m_token});
            }
            this.nextToken();
            this.nextToken();
        } else if (this.tokenIs('/')) {
            if (!bl) {
                this.error("ER_EXPECTED_STEP_PATTERN", null);
            }
            n2 = 52;
            this.appendOp(2, n2);
            this.nextToken();
        } else {
            n4 = this.m_ops.getOp(1);
            n2 = 53;
            this.appendOp(2, n2);
        }
        this.m_ops.setOp(1, this.m_ops.getOp(1) + 1);
        this.NodeTest(n2);
        this.m_ops.setOp(n3 + 1 + 1, this.m_ops.getOp(1) - n3);
        while (this.tokenIs('[')) {
            this.Predicate();
        }
        if (n4 > -1 && this.tokenIs('/') && this.lookahead('/', 1)) {
            this.m_ops.setOp(n4, 52);
            this.nextToken();
            bl2 = true;
        } else {
            bl2 = false;
        }
        this.m_ops.setOp(n3 + 1, this.m_ops.getOp(1) - n3);
        return bl2;
    }
}

