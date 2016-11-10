/*
 * Decompiled with CFR 0_119.
 */
package b.a;

import b.a.a;
import b.a.b;
import b.a.d;
import java.io.PrintStream;
import java.util.Stack;

public abstract class c {
    protected static final int _error_sync_size = 3;
    protected boolean _done_parsing = false;
    protected int tos;
    protected b cur_token;
    protected Stack stack = new Stack();
    protected short[][] production_tab;
    protected short[][] action_tab;
    protected short[][] reduce_tab;
    private a _scanner;
    protected b[] lookahead;
    protected int lookahead_pos;

    public c() {
    }

    public c(a a2) {
        this();
        this.setScanner(a2);
    }

    public abstract int EOF_sym();

    public abstract short[][] action_table();

    protected boolean advance_lookahead() {
        ++this.lookahead_pos;
        return this.lookahead_pos < this.error_sync_size();
    }

    protected b cur_err_token() {
        return this.lookahead[this.lookahead_pos];
    }

    public void debug_message(String string) {
        System.err.println(string);
    }

    public b debug_parse() throws Exception {
        b b2 = null;
        this.production_tab = this.production_table();
        this.action_tab = this.action_table();
        this.reduce_tab = this.reduce_table();
        this.debug_message("# Initializing parser");
        this.init_actions();
        this.user_init();
        this.cur_token = this.scan();
        this.debug_message("# Current Symbol is #" + this.cur_token.a);
        this.stack.removeAllElements();
        this.stack.push(new b(0, this.start_state()));
        this.tos = 0;
        this._done_parsing = false;
        while (!this._done_parsing) {
            if (this.cur_token.c) {
                throw new Error("Symbol recycling detected (fix your scanner).");
            }
            short s2 = this.get_action(((b)this.stack.peek()).b, this.cur_token.a);
            if (s2 > 0) {
                this.cur_token.b = s2 - 1;
                this.cur_token.c = true;
                this.debug_shift(this.cur_token);
                this.stack.push(this.cur_token);
                ++this.tos;
                this.cur_token = this.scan();
                this.debug_message("# Current token is " + this.cur_token);
                continue;
            }
            if (s2 < 0) {
                b2 = this.do_action(- s2 - 1, this, this.stack, this.tos);
                short s3 = this.production_tab[- s2 - 1][0];
                int n2 = this.production_tab[- s2 - 1][1];
                this.debug_reduce(- s2 - 1, s3, n2);
                int n3 = 0;
                while (n3 < n2) {
                    this.stack.pop();
                    --this.tos;
                    ++n3;
                }
                s2 = this.get_reduce(((b)this.stack.peek()).b, s3);
                this.debug_message("# Reduce rule: top state " + ((b)this.stack.peek()).b + ", lhs sym " + s3 + " -> state " + s2);
                b2.b = s2;
                b2.c = true;
                this.stack.push(b2);
                ++this.tos;
                this.debug_message("# Goto state #" + s2);
                continue;
            }
            if (s2 != 0) continue;
            this.syntax_error(this.cur_token);
            if (!this.error_recovery(true)) {
                this.unrecovered_syntax_error(this.cur_token);
                this.done_parsing();
                continue;
            }
            b2 = (b)this.stack.peek();
        }
        return b2;
    }

    public void debug_reduce(int n2, int n3, int n4) {
        this.debug_message("# Reduce with prod #" + n2 + " [NT=" + n3 + ", " + "SZ=" + n4 + "]");
    }

    public void debug_shift(b b2) {
        this.debug_message("# Shift under term #" + b2.a + " to state #" + b2.b);
    }

    public void debug_stack() {
        StringBuffer stringBuffer = new StringBuffer("## STACK:");
        int n2 = 0;
        while (n2 < this.stack.size()) {
            b b2 = (b)this.stack.elementAt(n2);
            stringBuffer.append(" <state " + b2.b + ", sym " + b2.a + ">");
            if (n2 % 3 == 2 || n2 == this.stack.size() - 1) {
                this.debug_message(stringBuffer.toString());
                stringBuffer = new StringBuffer("         ");
            }
            ++n2;
        }
    }

    public abstract b do_action(int var1, c var2, Stack var3, int var4) throws Exception;

    public void done_parsing() {
        this._done_parsing = true;
    }

    public void dump_stack() {
        if (this.stack == null) {
            this.debug_message("# Stack dump requested, but stack is null");
            return;
        }
        this.debug_message("============ Parse Stack Dump ============");
        int n2 = 0;
        while (n2 < this.stack.size()) {
            this.debug_message("Symbol: " + ((b)this.stack.elementAt((int)n2)).a + " State: " + ((b)this.stack.elementAt((int)n2)).b);
            ++n2;
        }
        this.debug_message("==========================================");
    }

    protected boolean error_recovery(boolean bl) throws Exception {
        if (bl) {
            this.debug_message("# Attempting error recovery");
        }
        if (!this.find_recovery_config(bl)) {
            if (bl) {
                this.debug_message("# Error recovery fails");
            }
            return false;
        }
        this.read_lookahead();
        do {
            if (bl) {
                this.debug_message("# Trying to parse ahead");
            }
            if (this.try_parse_ahead(bl)) break;
            if (this.lookahead[0].a == this.EOF_sym()) {
                if (bl) {
                    this.debug_message("# Error recovery fails at EOF");
                }
                return false;
            }
            if (bl) {
                this.debug_message("# Consuming Symbol #" + this.lookahead[0].a);
            }
            this.restart_lookahead();
        } while (true);
        if (bl) {
            this.debug_message("# Parse-ahead ok, going back to normal parse");
        }
        this.parse_lookahead(bl);
        return true;
    }

    public abstract int error_sym();

    protected int error_sync_size() {
        return 3;
    }

    protected boolean find_recovery_config(boolean bl) {
        if (bl) {
            this.debug_message("# Finding recovery state on stack");
        }
        int n2 = ((b)this.stack.peek()).e;
        int n3 = ((b)this.stack.peek()).d;
        while (!this.shift_under_error()) {
            if (bl) {
                this.debug_message("# Pop stack by one, state was # " + ((b)this.stack.peek()).b);
            }
            n3 = ((b)this.stack.pop()).d;
            --this.tos;
            if (!this.stack.empty()) continue;
            if (bl) {
                this.debug_message("# No recovery state found on stack");
            }
            return false;
        }
        short s2 = this.get_action(((b)this.stack.peek()).b, this.error_sym());
        if (bl) {
            this.debug_message("# Recover state found (#" + ((b)this.stack.peek()).b + ")");
            this.debug_message("# Shifting on error to state #" + (s2 - 1));
        }
        b b2 = new b(this.error_sym(), n3, n2);
        b2.b = s2 - 1;
        b2.c = true;
        this.stack.push(b2);
        ++this.tos;
        return true;
    }

    public a getScanner() {
        return this._scanner;
    }

    protected final short get_action(int n2, int n3) {
        short[] arrs = this.action_tab[n2];
        if (arrs.length < 20) {
            int n4 = 0;
            while (n4 < arrs.length) {
                short s2;
                if ((s2 = arrs[n4++]) == n3 || s2 == -1) {
                    return arrs[n4];
                }
                ++n4;
            }
        } else {
            int n5 = 0;
            int n6 = (arrs.length - 1) / 2 - 1;
            while (n5 <= n6) {
                int n7 = (n5 + n6) / 2;
                if (n3 == arrs[n7 * 2]) {
                    return arrs[n7 * 2 + 1];
                }
                if (n3 > arrs[n7 * 2]) {
                    n5 = n7 + 1;
                    continue;
                }
                n6 = n7 - 1;
            }
            return arrs[arrs.length - 1];
        }
        return 0;
    }

    protected final short get_reduce(int n2, int n3) {
        short[] arrs = this.reduce_tab[n2];
        if (arrs == null) {
            return -1;
        }
        int n4 = 0;
        while (n4 < arrs.length) {
            short s2;
            if ((s2 = arrs[n4++]) == n3 || s2 == -1) {
                return arrs[n4];
            }
            ++n4;
        }
        return -1;
    }

    protected abstract void init_actions() throws Exception;

    public b parse() throws Exception {
        b b2 = null;
        this.production_tab = this.production_table();
        this.action_tab = this.action_table();
        this.reduce_tab = this.reduce_table();
        this.init_actions();
        this.user_init();
        this.cur_token = this.scan();
        this.stack.removeAllElements();
        this.stack.push(new b(0, this.start_state()));
        this.tos = 0;
        this._done_parsing = false;
        while (!this._done_parsing) {
            if (this.cur_token.c) {
                throw new Error("Symbol recycling detected (fix your scanner).");
            }
            short s2 = this.get_action(((b)this.stack.peek()).b, this.cur_token.a);
            if (s2 > 0) {
                this.cur_token.b = s2 - 1;
                this.cur_token.c = true;
                this.stack.push(this.cur_token);
                ++this.tos;
                this.cur_token = this.scan();
                continue;
            }
            if (s2 < 0) {
                b2 = this.do_action(- s2 - 1, this, this.stack, this.tos);
                short s3 = this.production_tab[- s2 - 1][0];
                int n2 = this.production_tab[- s2 - 1][1];
                int n3 = 0;
                while (n3 < n2) {
                    this.stack.pop();
                    --this.tos;
                    ++n3;
                }
                s2 = this.get_reduce(((b)this.stack.peek()).b, s3);
                b2.b = s2;
                b2.c = true;
                this.stack.push(b2);
                ++this.tos;
                continue;
            }
            if (s2 != 0) continue;
            this.syntax_error(this.cur_token);
            if (!this.error_recovery(false)) {
                this.unrecovered_syntax_error(this.cur_token);
                this.done_parsing();
                continue;
            }
            b2 = (b)this.stack.peek();
        }
        return b2;
    }

    protected void parse_lookahead(boolean bl) throws Exception {
        b b2 = null;
        this.lookahead_pos = 0;
        if (bl) {
            this.debug_message("# Reparsing saved input with actions");
            this.debug_message("# Current Symbol is #" + this.cur_err_token().a);
            this.debug_message("# Current state is #" + ((b)this.stack.peek()).b);
        }
        while (!this._done_parsing) {
            short s2 = this.get_action(((b)this.stack.peek()).b, this.cur_err_token().a);
            if (s2 > 0) {
                this.cur_err_token().b = s2 - 1;
                this.cur_err_token().c = true;
                if (bl) {
                    this.debug_shift(this.cur_err_token());
                }
                this.stack.push(this.cur_err_token());
                ++this.tos;
                if (!this.advance_lookahead()) {
                    if (bl) {
                        this.debug_message("# Completed reparse");
                    }
                    return;
                }
                if (!bl) continue;
                this.debug_message("# Current Symbol is #" + this.cur_err_token().a);
                continue;
            }
            if (s2 < 0) {
                b2 = this.do_action(- s2 - 1, this, this.stack, this.tos);
                short s3 = this.production_tab[- s2 - 1][0];
                int n2 = this.production_tab[- s2 - 1][1];
                if (bl) {
                    this.debug_reduce(- s2 - 1, s3, n2);
                }
                int n3 = 0;
                while (n3 < n2) {
                    this.stack.pop();
                    --this.tos;
                    ++n3;
                }
                s2 = this.get_reduce(((b)this.stack.peek()).b, s3);
                b2.b = s2;
                b2.c = true;
                this.stack.push(b2);
                ++this.tos;
                if (!bl) continue;
                this.debug_message("# Goto state #" + s2);
                continue;
            }
            if (s2 != 0) continue;
            this.report_fatal_error("Syntax error", b2);
            return;
        }
    }

    public abstract short[][] production_table();

    protected void read_lookahead() throws Exception {
        this.lookahead = new b[this.error_sync_size()];
        int n2 = 0;
        while (n2 < this.error_sync_size()) {
            this.lookahead[n2] = this.cur_token;
            this.cur_token = this.scan();
            ++n2;
        }
        this.lookahead_pos = 0;
    }

    public abstract short[][] reduce_table();

    public void report_error(String string, Object object) {
        System.err.print(string);
        if (object instanceof b) {
            if (((b)object).d != -1) {
                System.err.println(" at character " + ((b)object).d + " of input");
            } else {
                System.err.println("");
            }
        } else {
            System.err.println("");
        }
    }

    public void report_fatal_error(String string, Object object) throws Exception {
        this.done_parsing();
        this.report_error(string, object);
        throw new Exception("Can't recover from previous error(s)");
    }

    protected void restart_lookahead() throws Exception {
        int n2 = 1;
        while (n2 < this.error_sync_size()) {
            this.lookahead[n2 - 1] = this.lookahead[n2];
            ++n2;
        }
        this.lookahead[this.error_sync_size() - 1] = this.cur_token;
        this.cur_token = this.scan();
        this.lookahead_pos = 0;
    }

    public b scan() throws Exception {
        b b2 = this.getScanner().next_token();
        return b2 != null ? b2 : new b(this.EOF_sym());
    }

    public void setScanner(a a2) {
        this._scanner = a2;
    }

    protected boolean shift_under_error() {
        return this.get_action(((b)this.stack.peek()).b, this.error_sym()) > 0;
    }

    public abstract int start_production();

    public abstract int start_state();

    public void syntax_error(b b2) {
        this.report_error("Syntax error", b2);
    }

    protected boolean try_parse_ahead(boolean bl) throws Exception {
        d d2 = new d(this.stack);
        short s2;
        while ((s2 = this.get_action(d2.c(), this.cur_err_token().a)) != 0) {
            if (s2 > 0) {
                d2.a(s2 - 1);
                if (bl) {
                    this.debug_message("# Parse-ahead shifts Symbol #" + this.cur_err_token().a + " into state #" + (s2 - 1));
                }
                if (this.advance_lookahead()) continue;
                return true;
            }
            if (- s2 - 1 == this.start_production()) {
                if (bl) {
                    this.debug_message("# Parse-ahead accepts");
                }
                return true;
            }
            short s3 = this.production_tab[- s2 - 1][0];
            int n2 = this.production_tab[- s2 - 1][1];
            int n3 = 0;
            while (n3 < n2) {
                d2.b();
                ++n3;
            }
            if (bl) {
                this.debug_message("# Parse-ahead reduces: handle size = " + n2 + " lhs = #" + s3 + " from state #" + d2.c());
            }
            d2.a(this.get_reduce(d2.c(), s3));
            if (!bl) continue;
            this.debug_message("# Goto state #" + d2.c());
        }
        return false;
    }

    protected static short[][] unpackFromStrings(String[] arrstring) {
        StringBuffer stringBuffer = new StringBuffer(arrstring[0]);
        int n2 = 1;
        while (n2 < arrstring.length) {
            stringBuffer.append(arrstring[n2]);
            ++n2;
        }
        int n3 = 0;
        int n4 = stringBuffer.charAt(n3) << 16 | stringBuffer.charAt(n3 + 1);
        n3 += 2;
        short[][] arrs = new short[n4][];
        int n5 = 0;
        while (n5 < n4) {
            int n6 = stringBuffer.charAt(n3) << 16 | stringBuffer.charAt(n3 + 1);
            n3 += 2;
            arrs[n5] = new short[n6];
            int n7 = 0;
            while (n7 < n6) {
                arrs[n5][n7] = (short)(stringBuffer.charAt(n3++) - 2);
                ++n7;
            }
            ++n5;
        }
        return arrs;
    }

    public void unrecovered_syntax_error(b b2) throws Exception {
        this.report_fatal_error("Couldn't repair and continue parse", b2);
    }

    public void user_init() throws Exception {
    }
}

