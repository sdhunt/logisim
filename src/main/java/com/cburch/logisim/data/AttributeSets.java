/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides useful methods for operating on attribute sets.
 */
public final class AttributeSets {
    private static final String ABSENT = "absent";
    private static final String READ_ONLY = "read only";

    private static final String E_NOT_SAME_LENGTH =
            "attribute and value arrays must have same length";
    private static final String E_ATTRS_MAX =
            "cannot handle more than 32 attributes";
    private static final int MAX_ATTRS = 32;

    // non-instantiable
    private AttributeSets() {
    }

    // TODO: consider implementing AttributeSetAdapter and extending that
    /**
     * Designates an empty attribute set.
     */
    public static final AttributeSet EMPTY = new AttributeSet() {
        @Override
        public Object clone() {
            return this;
        }

        @Override
        public void addAttributeListener(AttributeListener l) {
        }

        @Override
        public void removeAttributeListener(AttributeListener l) {
        }

        @Override
        public List<Attribute<?>> getAttributes() {
            return Collections.emptyList();
        }

        @Override
        public boolean containsAttribute(Attribute<?> attr) {
            return false;
        }

        @Override
        public Attribute<?> getAttribute(String name) {
            return null;
        }

        @Override
        public boolean isReadOnly(Attribute<?> attr) {
            return true;
        }

        @Override
        public void setReadOnly(Attribute<?> attr, boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isToSave(Attribute<?> attr) {
            return true;
        }

        @Override
        public <V> V getValue(Attribute<V> attr) {
            return null;
        }

        @Override
        public <V> void setValue(Attribute<V> attr, V value) {
        }
    };

    /**
     * Returns a fixed set containing the given attributes and their initial
     * values.
     *
     * @param attrs      the attributes
     * @param initValues their initial values
     * @return the attribute set
     */
    public static AttributeSet fixedSet(Attribute<?>[] attrs,
                                        Object[] initValues) {
        if (attrs.length > 1) {
            return new FixedSet(attrs, initValues);
        }
        if (attrs.length == 1) {
            if (initValues.length != 1) {
                throw new IllegalArgumentException(E_NOT_SAME_LENGTH);
            }
            return new SingletonSet(attrs[0], initValues[0]);
        }
        return EMPTY;
    }

    /**
     * Copies all the attributes from the source set to the destination set.
     *
     * @param src the source attribute set
     * @param dst the destination attribute set
     */
    public static void copy(AttributeSet src, AttributeSet dst) {
        if (src == null || src.getAttributes() == null) {
            return;
        }

        for (Attribute<?> attr : src.getAttributes()) {
            @SuppressWarnings("unchecked")
            Attribute<Object> attrObj = (Attribute<Object>) attr;
            Object value = src.getValue(attr);
            dst.setValue(attrObj, value);
        }
    }

    private static String eMsg(Attribute<?> attr, String suffix) {
        return "attribute " + attr.getName() + " " + suffix;
    }

    // an sttribute set that contains a single attribute
    private static class SingletonSet extends AbstractAttributeSet {
        private List<Attribute<?>> attrs;
        private Object value;
        private boolean readOnly = false;

        SingletonSet(Attribute<?> attr, Object initValue) {
            this.attrs = new ArrayList<>(1);
            this.attrs.add(attr);
            this.value = initValue;
        }

        @Override
        protected void copyInto(AbstractAttributeSet destSet) {
            SingletonSet dest = (SingletonSet) destSet;
            dest.attrs = this.attrs;
            dest.value = this.value;
            dest.readOnly = this.readOnly;
        }

        @Override
        public List<Attribute<?>> getAttributes() {
            return attrs;
        }

        @Override
        public boolean isReadOnly(Attribute<?> attr) {
            return readOnly;
        }

        @Override
        public void setReadOnly(Attribute<?> attr, boolean value) {
            int index = attrs.indexOf(attr);
            if (index < 0) {
                throw new IllegalArgumentException(eMsg(attr, ABSENT));
            }
            readOnly = value;
        }

        @Override
        public <V> V getValue(Attribute<V> attr) {
            int index = attrs.indexOf(attr);
            @SuppressWarnings("unchecked")
            V ret = (V) (index >= 0 ? value : null);
            return ret;
        }

        @Override
        public <V> void setValue(Attribute<V> attr, V value) {
            int index = attrs.indexOf(attr);
            if (index < 0) {
                throw new IllegalArgumentException(eMsg(attr, ABSENT));
            }
            if (readOnly) {
                throw new IllegalArgumentException(eMsg(attr, READ_ONLY));
            }
            this.value = value;
            fireAttributeValueChanged(attr, value);
        }
    }


    // an attribute set that contains a fixed set of attributes
    // (no more than 32).
    private static class FixedSet extends AbstractAttributeSet {
        private List<Attribute<?>> attrs;
        private Object[] values;
        private int readOnly = 0;

        FixedSet(Attribute<?>[] attrs, Object[] initValues) {
            if (attrs.length != initValues.length) {
                throw new IllegalArgumentException(E_NOT_SAME_LENGTH);
            }
            if (attrs.length > MAX_ATTRS) {
                throw new IllegalArgumentException(E_ATTRS_MAX);
            }
            this.attrs = Arrays.asList(attrs);
            this.values = initValues.clone();
        }

        @Override
        protected void copyInto(AbstractAttributeSet destSet) {
            FixedSet dest = (FixedSet) destSet;
            dest.attrs = this.attrs;
            dest.values = this.values.clone();
            dest.readOnly = this.readOnly;
        }

        @Override
        public List<Attribute<?>> getAttributes() {
            return attrs;
        }

        @Override
        public boolean isReadOnly(Attribute<?> attr) {
            int index = attrs.indexOf(attr);
            return index < 0 || isReadOnly(index);
        }

        @Override
        public void setReadOnly(Attribute<?> attr, boolean value) {
            int index = attrs.indexOf(attr);
            if (index < 0) {
                throw new IllegalArgumentException(eMsg(attr, ABSENT));
            }

            if (value) {
                readOnly |= (1 << index);
            } else {
                readOnly &= ~(1 << index);
            }

        }

        @Override
        public <V> V getValue(Attribute<V> attr) {
            int index = attrs.indexOf(attr);
            if (index < 0) {
                return null;
            }
            @SuppressWarnings("unchecked")
            V ret = (V) values[index];
            return ret;
        }

        @Override
        public <V> void setValue(Attribute<V> attr, V value) {
            int index = attrs.indexOf(attr);
            if (index < 0) {
                throw new IllegalArgumentException(eMsg(attr, ABSENT));
            }
            if (isReadOnly(index)) {
                throw new IllegalArgumentException(eMsg(attr, READ_ONLY));
            }
            values[index] = value;
            fireAttributeValueChanged(attr, value);
        }

        private boolean isReadOnly(int index) {
            return ((readOnly >> index) & 1) == 1;
        }
    }

}
