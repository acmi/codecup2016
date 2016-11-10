/*
 * Decompiled with CFR 0_119.
 */
package com.a.b.a.a.d;

import com.a.b.a.a.d.a;
import com.codeforces.commons.concurrent.AtomicUtil;
import com.codeforces.commons.math.Math;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

class e
extends KeyAdapter {
    final /* synthetic */ a a;

    e(a a2) {
        this.a = a2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isConsumed()) {
            return;
        }
        boolean bl = true;
        if (keyEvent.getKeyChar() == ' ') {
            AtomicUtil.invert(a.f(this.a));
        } else if (keyEvent.getKeyCode() == 9) {
            AtomicUtil.invert(a.g(this.a));
        } else if (keyEvent.getKeyCode() == 39) {
            if (a.h(this.a).incrementAndGet() > 32767) {
                a.h(this.a).decrementAndGet();
            }
        } else if (keyEvent.getKeyCode() == 72) {
            AtomicUtil.decrement(a.i(this.a), 2);
        } else if (keyEvent.getKeyCode() == 38) {
            long l2 = a.a(this.a).get();
            int n2 = Arrays.binarySearch(a.c(), l2);
            if (n2 > 0) {
                a.a(this.a).compareAndSet(l2, a.c()[n2 - 1]);
            }
        } else if (keyEvent.getKeyCode() == 40) {
            long l3 = a.a(this.a).get();
            int n3 = Arrays.binarySearch(a.c(), l3);
            if (n3 < a.c().length - 1) {
                a.a(this.a).compareAndSet(l3, a.c()[n3 + 1]);
            }
        } else if (keyEvent.getKeyCode() == 18) {
            a.j(this.a).set(true);
        } else {
            if ((keyEvent.getKeyCode() == 521 || keyEvent.getKeyCode() == 61) && keyEvent.isControlDown()) {
                a.k(this.a).lock();
                try {
                    double d2 = a.l(this.a) - 250.0;
                    double d3 = a.m(this.a) * (d2 / a.l(this.a));
                    if (a.l(this.a) > 500.0) {
                        a.a(this.a, (double)Math.round(d2));
                        a.b(this.a, (double)Math.round(d3));
                    }
                    this.a();
                }
                finally {
                    a.k(this.a).unlock();
                }
            }
            if (keyEvent.getKeyCode() == 45 && keyEvent.isControlDown()) {
                a.k(this.a).lock();
                try {
                    double d4 = a.l(this.a) + 250.0;
                    double d5 = a.m(this.a) * (d4 / a.l(this.a));
                    if (a.l(this.a) < 4000.0 || a.m(this.a) < 4000.0) {
                        a.a(this.a, (double)Math.round(d4));
                        a.b(this.a, (double)Math.round(d5));
                    }
                    this.a();
                }
                finally {
                    a.k(this.a).unlock();
                }
            }
            if (keyEvent.getKeyCode() == 48 && keyEvent.isControlDown()) {
                a.k(this.a).lock();
                try {
                    a.a(this.a, 1250.0);
                    a.b(this.a, a.l(this.a) * (double)a.n(this.a).c() / (double)a.n(this.a).b());
                }
                finally {
                    a.k(this.a).unlock();
                }
            }
            if (keyEvent.getKeyCode() >= 49 && keyEvent.getKeyCode() <= 57) {
                Long l4 = (Long)a.o(this.a).get(keyEvent.getKeyCode() - 49);
                if (l4 != null) {
                    a.p(this.a).set(l4);
                }
            } else if (keyEvent.getKeyCode() == 48) {
                Long l5 = (Long)a.o(this.a).get(9);
                if (l5 != null) {
                    a.p(this.a).set(l5);
                }
            } else if (keyEvent.getKeyCode() == 87 || keyEvent.getKeyCode() == 104) {
                a.q(this.a).a(0, true);
            } else if (keyEvent.getKeyCode() == 83 || keyEvent.getKeyCode() == 98) {
                a.q(this.a).b(0, true);
            } else if (keyEvent.getKeyCode() == 65) {
                a.q(this.a).c(0, true);
            } else if (keyEvent.getKeyCode() == 68) {
                a.q(this.a).d(0, true);
            } else if (keyEvent.getKeyCode() == 81) {
                a.q(this.a).g(0, true);
            } else if (keyEvent.getKeyCode() == 69) {
                a.q(this.a).h(0, true);
            } else if (keyEvent.getKeyCode() == 82) {
                a.q(this.a).i(0, true);
            } else if (keyEvent.getKeyCode() == 70) {
                a.q(this.a).j(0, true);
            } else if (keyEvent.getKeyCode() == 88) {
                a.q(this.a).k(0, true);
            } else if (keyEvent.getKeyCode() == 67) {
                a.q(this.a).l(0, true);
            } else if (keyEvent.getKeyCode() == 100 || keyEvent.getKeyCode() == 44 || keyEvent.getKeyCode() == 153 || keyEvent.getKeyChar() == '\u0431' || keyEvent.getKeyChar() == '\u0411') {
                a.q(this.a).e(0, true);
            } else if (keyEvent.getKeyCode() == 102 || keyEvent.getKeyCode() == 46 || keyEvent.getKeyCode() == 160 || keyEvent.getKeyChar() == '\u044e' || keyEvent.getKeyChar() == '\u042e') {
                a.q(this.a).f(0, true);
            } else if (keyEvent.getKeyCode() != 73 && keyEvent.getKeyCode() != 75 && keyEvent.getKeyCode() != 74 && keyEvent.getKeyCode() != 76 && keyEvent.getKeyCode() != 85 && keyEvent.getKeyCode() != 79 && keyEvent.getKeyCode() != 46 && keyEvent.getKeyCode() != 160 && keyEvent.getKeyCode() != 44 && keyEvent.getKeyCode() != 153 && keyEvent.getKeyCode() != 104 && keyEvent.getKeyCode() != 101 && keyEvent.getKeyCode() != 100 && keyEvent.getKeyCode() != 102 && keyEvent.getKeyCode() != 103 && keyEvent.getKeyCode() != 105 && keyEvent.getKeyCode() != 99 && keyEvent.getKeyCode() != 98) {
                bl = false;
            }
        }
        if (bl) {
            keyEvent.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.isConsumed()) {
            return;
        }
        boolean bl = true;
        if (keyEvent.getKeyCode() == 18) {
            a.j(this.a).set(false);
        } else if (keyEvent.getKeyCode() == 87 || keyEvent.getKeyCode() == 104) {
            a.q(this.a).a(0, false);
        } else if (keyEvent.getKeyCode() == 83 || keyEvent.getKeyCode() == 98) {
            a.q(this.a).b(0, false);
        } else if (keyEvent.getKeyCode() == 65) {
            a.q(this.a).c(0, false);
        } else if (keyEvent.getKeyCode() == 68) {
            a.q(this.a).d(0, false);
        } else if (keyEvent.getKeyCode() == 81) {
            a.q(this.a).g(0, false);
        } else if (keyEvent.getKeyCode() == 69) {
            a.q(this.a).h(0, false);
        } else if (keyEvent.getKeyCode() == 82) {
            a.q(this.a).i(0, false);
        } else if (keyEvent.getKeyCode() == 70) {
            a.q(this.a).j(0, false);
        } else if (keyEvent.getKeyCode() == 88) {
            a.q(this.a).k(0, false);
        } else if (keyEvent.getKeyCode() == 67) {
            a.q(this.a).l(0, false);
        } else if (keyEvent.getKeyCode() == 100 || keyEvent.getKeyCode() == 44 || keyEvent.getKeyCode() == 153 || keyEvent.getKeyChar() == '\u0431' || keyEvent.getKeyChar() == '\u0411') {
            a.q(this.a).e(0, false);
        } else if (keyEvent.getKeyCode() == 102 || keyEvent.getKeyCode() == 46 || keyEvent.getKeyCode() == 160 || keyEvent.getKeyChar() == '\u044e' || keyEvent.getKeyChar() == '\u042e') {
            a.q(this.a).f(0, false);
        } else if (keyEvent.getKeyCode() != 73 && keyEvent.getKeyCode() != 75 && keyEvent.getKeyCode() != 74 && keyEvent.getKeyCode() != 76 && keyEvent.getKeyCode() != 85 && keyEvent.getKeyCode() != 79 && keyEvent.getKeyCode() != 46 && keyEvent.getKeyCode() != 160 && keyEvent.getKeyCode() != 44 && keyEvent.getKeyCode() != 153 && keyEvent.getKeyCode() != 104 && keyEvent.getKeyCode() != 101 && keyEvent.getKeyCode() != 100 && keyEvent.getKeyCode() != 102 && keyEvent.getKeyCode() != 103 && keyEvent.getKeyCode() != 105 && keyEvent.getKeyCode() != 99 && keyEvent.getKeyCode() != 98) {
            bl = false;
        }
        if (bl) {
            keyEvent.consume();
        }
    }

    private void a() {
        if (a.r(this.a) + a.l(this.a) > 4000.0) {
            a.c(this.a, 4000.0 - a.l(this.a));
        }
        if (a.r(this.a) < 0.0) {
            a.c(this.a, 0.0);
        }
        if (a.s(this.a) + a.m(this.a) > 4000.0) {
            a.d(this.a, 4000.0 - a.m(this.a));
        }
        if (a.s(this.a) < 0.0) {
            a.d(this.a, 0.0);
        }
    }
}

