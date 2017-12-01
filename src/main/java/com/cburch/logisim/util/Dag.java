/*
 * Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/.
 */

package com.cburch.logisim.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Represents a Directed-Acyclic-Graph.
 *
 * @param <E> the type of elements maintained by this DAG
 */
public class Dag<E> {

    // represents a node in the DAG
    private static class Node<E> {
        E data;

        // of Nodes
        private final Set<Node<E>> succs = new HashSet<>();
        int numPreds = 0;
        boolean mark;

        Node(E data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return String.valueOf(data) + "->" + succs;
        }
    }

    private final Map<E, Node<E>> nodes = new HashMap<>();

    /**
     * Returns true if the node in the DAG for the given data object has
     * one or more predecessors.
     *
     * @param data the data object
     * @return true, if the data's node has predecessors; false otherwise
     */
    public boolean hasPredecessors(E data) {
        Node<E> from = findNode(data);
        return from != null && from.numPreds != 0;
    }

    /**
     * Returns true if the node in the DAG for the given data object has
     * one or more successors.
     *
     * @param data the data object
     * @return true, if the data's node has successors; false otherwise
     */
    public boolean hasSuccessors(E data) {
        Node<E> to = findNode(data);
        return to != null && !to.succs.isEmpty();
    }

    /**
     * Returns true if the given query object could be placed in the DAG after
     * the given base object, and not break the acyclic guarantee.
     *
     * @param query the query data object
     * @param base  the base data object
     * @return true, if the query data could be placed after the base data;
     * false otherwise
     */
    public boolean canFollow(E query, E base) {
        Node<E> queryNode = findNode(query);
        Node<E> baseNode = findNode(base);
        if (baseNode == null || queryNode == null) {
            return !base.equals(query);
        }
        return canFollow(queryNode, baseNode);
    }

    /**
     * Adds an edge between nodes containing the source and destination data,
     * if permissible. Note that an edge can only be added if the destination
     * can follow the source.
     *
     * @param srcData the source node data
     * @param dstData the destination node data
     * @return true, if an edge was successfully installed; false otherwise
     */
    public boolean addEdge(E srcData, E dstData) {
        if (!canFollow(dstData, srcData)) {
            return false;
        }

        Node<E> src = createNode(srcData);
        Node<E> dst = createNode(dstData);
        if (src == null || dst == null) {
            return false;
        }

        // add since not already present
        if (src.succs.add(dst)) {
            ++dst.numPreds;
        }

        return true;
    }

    /**
     * Removes an edge between nodes containing the source and destination data,
     * if permissible.
     *
     * @param srcData the source node data
     * @param dstData the destination node data
     * @return true, if the edge was successfully removed
     */
    public boolean removeEdge(E srcData, E dstData) {
        Node<E> src = findNode(srcData);
        Node<E> dst = findNode(dstData);

        if (src == null || dst == null || !src.succs.remove(dst)) {
            return false;
        }


        --dst.numPreds;
        if (dst.numPreds == 0 && dst.succs.isEmpty()) {
            nodes.remove(dstData);
        }

        if (src.numPreds == 0 && src.succs.isEmpty()) {
            nodes.remove(srcData);
        }

        return true;
    }

    /**
     * Removes the node with the given data from the DAG.
     *
     * @param data the data
     */
    public void removeNode(E data) {
        Node<E> n = findNode(data);
        if (n == null) {
            return;
        }

        for (Iterator<Node<E>> it = n.succs.iterator(); it.hasNext(); ) {
            Node<E> succ = it.next();
            --(succ.numPreds);
            if (succ.numPreds == 0 && succ.succs.isEmpty()) {
                it.remove();
            }

        }

        if (n.numPreds > 0) {
            for (Iterator<Node<E>> it = nodes.values().iterator(); it.hasNext(); ) {
                Node<E> q = it.next();
                if (q.succs.remove(n) && q.numPreds == 0 && q.succs.isEmpty()) {
                    it.remove();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "DAG:" + nodes.values();
    }

    private Node<E> findNode(E data) {
        if (data == null) {
            return null;
        }

        return nodes.get(data);
    }

    private Node<E> createNode(E data) {
        Node<E> ret = findNode(data);
        if (ret != null) {
            return ret;
        }

        if (data == null) {
            return null;
        }

        ret = new Node<>(data);
        nodes.put(data, ret);
        return ret;
    }

    private boolean canFollow(Node<E> query, Node<E> base) {
        if (base == query) {
            return false;
        }


        // mark all as unvisited
        for (Node<E> n : nodes.values()) {
            // will become true once reached
            n.mark = false;
        }

        // Search starting at query: If base is found, then it follows
        // the query already, and so query cannot follow base.
        LinkedList<Node<E>> fringe = new LinkedList<>();
        fringe.add(query);
        while (!fringe.isEmpty()) {
            Node<E> n = fringe.removeFirst();
            for (Node<E> next : n.succs) {
                if (!next.mark) {
                    if (next == base) {
                        return false;
                    }

                    next.mark = true;
                    fringe.addLast(next);
                }
            }
        }
        return true;
    }
}
