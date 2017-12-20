/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implements a listener collection using weak references.
 *
 * @param <L> the listener type
 */
public class EventSourceWeakSupport<L> implements Iterable<L> {

    private ConcurrentLinkedQueue<WeakReference<L>> listeners =
            new ConcurrentLinkedQueue<>();

    /**
     * Adds the given listener to this collection.
     *
     * @param listener the listener to add
     */
    public void add(L listener) {
        listeners.add(new WeakReference<>(listener));
    }

    /**
     * Removes the given listener from this collection.
     * Also prunes any "expired" listener references.
     *
     * @param listener the listener to remove
     */
    public void remove(L listener) {
        for (Iterator<WeakReference<L>> it = listeners.iterator(); it.hasNext(); ) {
            L lsnr = it.next().get();
            if (lsnr == null || lsnr == listener) {
                it.remove();
            }
        }
    }

    /**
     * Returns true if there are no more registered listeners in this
     * collection; false otherwise.
     * Takes the opportunity to prune "expired" references.
     *
     * @return true if the collection is empty; false otherwise
     */
    public boolean isEmpty() {
        for (Iterator<WeakReference<L>> it = listeners.iterator(); it.hasNext(); ) {
            L listener = it.next().get();
            if (listener == null) {
                it.remove();
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<L> iterator() {
        // copy elements into another list in case any event handlers
        // want to add a listener
        List<L> result = new ArrayList<>(listeners.size());
        for (Iterator<WeakReference<L>> it = listeners.iterator(); it.hasNext(); ) {
            L listener = it.next().get();
            if (listener == null) {
                it.remove();
            } else {
                result.add(listener);
            }
        }
        return result.iterator();
    }
}
