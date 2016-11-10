/*
 * Decompiled with CFR 0_119.
 */
package org.apache.xerces.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.xerces.xni.Augmentations;

public class AugmentationsImpl
implements Augmentations {
    private AugmentationsItemsContainer fAugmentationsContainer = new SmallContainer();

    public Object putItem(String string, Object object) {
        Object object2 = this.fAugmentationsContainer.putItem(string, object);
        if (object2 == null && this.fAugmentationsContainer.isFull()) {
            this.fAugmentationsContainer = this.fAugmentationsContainer.expand();
        }
        return object2;
    }

    public Object getItem(String string) {
        return this.fAugmentationsContainer.getItem(string);
    }

    public Object removeItem(String string) {
        return this.fAugmentationsContainer.removeItem(string);
    }

    public Enumeration keys() {
        return this.fAugmentationsContainer.keys();
    }

    public void removeAllItems() {
        this.fAugmentationsContainer.clear();
    }

    public String toString() {
        return this.fAugmentationsContainer.toString();
    }

    static final class LargeContainer
    extends AugmentationsItemsContainer {
        private final HashMap fAugmentations = new HashMap();

        LargeContainer() {
        }

        public Object getItem(Object object) {
            return this.fAugmentations.get(object);
        }

        public Object putItem(Object object, Object object2) {
            return this.fAugmentations.put(object, object2);
        }

        public Object removeItem(Object object) {
            return this.fAugmentations.remove(object);
        }

        public Enumeration keys() {
            return Collections.enumeration(this.fAugmentations.keySet());
        }

        public void clear() {
            this.fAugmentations.clear();
        }

        public boolean isFull() {
            return false;
        }

        public AugmentationsItemsContainer expand() {
            return this;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("LargeContainer");
            Iterator iterator = this.fAugmentations.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                stringBuffer.append("\nkey == ");
                stringBuffer.append(entry.getKey());
                stringBuffer.append("; value == ");
                stringBuffer.append(entry.getValue());
            }
            return stringBuffer.toString();
        }
    }

    static final class SmallContainer
    extends AugmentationsItemsContainer {
        static final int SIZE_LIMIT = 10;
        final Object[] fAugmentations = new Object[20];
        int fNumEntries = 0;

        SmallContainer() {
        }

        public Enumeration keys() {
            return new SmallContainerKeyEnumeration(this);
        }

        public Object getItem(Object object) {
            int n2 = 0;
            while (n2 < this.fNumEntries * 2) {
                if (this.fAugmentations[n2].equals(object)) {
                    return this.fAugmentations[n2 + 1];
                }
                n2 += 2;
            }
            return null;
        }

        public Object putItem(Object object, Object object2) {
            int n2 = 0;
            while (n2 < this.fNumEntries * 2) {
                if (this.fAugmentations[n2].equals(object)) {
                    Object object3 = this.fAugmentations[n2 + 1];
                    this.fAugmentations[n2 + 1] = object2;
                    return object3;
                }
                n2 += 2;
            }
            this.fAugmentations[this.fNumEntries * 2] = object;
            this.fAugmentations[this.fNumEntries * 2 + 1] = object2;
            ++this.fNumEntries;
            return null;
        }

        public Object removeItem(Object object) {
            int n2 = 0;
            while (n2 < this.fNumEntries * 2) {
                if (this.fAugmentations[n2].equals(object)) {
                    Object object2 = this.fAugmentations[n2 + 1];
                    int n3 = n2;
                    while (n3 < this.fNumEntries * 2 - 2) {
                        this.fAugmentations[n3] = this.fAugmentations[n3 + 2];
                        this.fAugmentations[n3 + 1] = this.fAugmentations[n3 + 3];
                        n3 += 2;
                    }
                    this.fAugmentations[this.fNumEntries * 2 - 2] = null;
                    this.fAugmentations[this.fNumEntries * 2 - 1] = null;
                    --this.fNumEntries;
                    return object2;
                }
                n2 += 2;
            }
            return null;
        }

        public void clear() {
            int n2 = 0;
            while (n2 < this.fNumEntries * 2) {
                this.fAugmentations[n2] = null;
                this.fAugmentations[n2 + 1] = null;
                n2 += 2;
            }
            this.fNumEntries = 0;
        }

        public boolean isFull() {
            return this.fNumEntries == 10;
        }

        public AugmentationsItemsContainer expand() {
            LargeContainer largeContainer = new LargeContainer();
            int n2 = 0;
            while (n2 < this.fNumEntries * 2) {
                largeContainer.putItem(this.fAugmentations[n2], this.fAugmentations[n2 + 1]);
                n2 += 2;
            }
            return largeContainer;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("SmallContainer - fNumEntries == ").append(this.fNumEntries);
            int n2 = 0;
            while (n2 < 20) {
                stringBuffer.append("\nfAugmentations[");
                stringBuffer.append(n2);
                stringBuffer.append("] == ");
                stringBuffer.append(this.fAugmentations[n2]);
                stringBuffer.append("; fAugmentations[");
                stringBuffer.append(n2 + 1);
                stringBuffer.append("] == ");
                stringBuffer.append(this.fAugmentations[n2 + 1]);
                n2 += 2;
            }
            return stringBuffer.toString();
        }

        final class SmallContainerKeyEnumeration
        implements Enumeration {
            Object[] enumArray;
            int next;
            private final SmallContainer this$0;

            SmallContainerKeyEnumeration(SmallContainer smallContainer) {
                this.this$0 = smallContainer;
                this.enumArray = new Object[this.this$0.fNumEntries];
                this.next = 0;
                int n2 = 0;
                while (n2 < smallContainer.fNumEntries) {
                    this.enumArray[n2] = smallContainer.fAugmentations[n2 * 2];
                    ++n2;
                }
            }

            public boolean hasMoreElements() {
                return this.next < this.enumArray.length;
            }

            public Object nextElement() {
                if (this.next >= this.enumArray.length) {
                    throw new NoSuchElementException();
                }
                Object object = this.enumArray[this.next];
                this.enumArray[this.next] = null;
                ++this.next;
                return object;
            }
        }

    }

    static abstract class AugmentationsItemsContainer {
        AugmentationsItemsContainer() {
        }

        public abstract Object putItem(Object var1, Object var2);

        public abstract Object getItem(Object var1);

        public abstract Object removeItem(Object var1);

        public abstract Enumeration keys();

        public abstract void clear();

        public abstract boolean isFull();

        public abstract AugmentationsItemsContainer expand();
    }

}

