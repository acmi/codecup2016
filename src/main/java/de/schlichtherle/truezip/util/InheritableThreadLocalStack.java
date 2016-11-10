/*
 * Decompiled with CFR 0_119.
 */
package de.schlichtherle.truezip.util;

import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;

public final class InheritableThreadLocalStack<T> {
    private final InheritableThreadLocal<Node<T>> nodes = new InheritableThreadLocal();

    public T peekOrElse(T t2) {
        Node<T> node = this.nodes.get();
        return null != node ? node.element : t2;
    }

    public T push(T t2) {
        Node<T> node = this.nodes.get();
        Node<T> node2 = new Node<T>(node, t2);
        this.nodes.set(node2);
        return t2;
    }

    public T pop() {
        Node<T> node = this.nodes.get();
        if (null == node) {
            throw new NoSuchElementException();
        }
        if (!Thread.currentThread().equals(node.get())) {
            throw new NoSuchElementException();
        }
        this.nodes.set(node.previous);
        return node.element;
    }

    public void popIf(T t2) {
        try {
            T t3 = this.pop();
            if (t3 != t2) {
                this.push(t3);
                throw new IllegalStateException(t3 + " (expected " + t2 + " as the top element of the inheritable thread local stack)");
            }
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IllegalStateException("The inheritable thread local stack is empty!", noSuchElementException);
        }
    }

    private static class Node<T>
    extends WeakReference<Thread> {
        final Node<T> previous;
        T element;

        Node(Node<T> node, T t2) {
            super(Thread.currentThread());
            this.previous = node;
            this.element = t2;
        }
    }

}

