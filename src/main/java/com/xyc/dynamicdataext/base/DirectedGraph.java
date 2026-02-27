package com.xyc.dynamicdataext.base;

import com.google.common.collect.Sets;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DirectedGraph<T> implements Iterable<Map.Entry<T, DirectedGraph.Node<T>>> {
    protected Map<T, Node<T>> map = new LinkedHashMap<>();

    @CanIgnoreReturnValue
    public boolean addNode(T content) {
        if (this.map.containsKey(content))
            return false;
        else {
            this.map.put(content, new Node<>(content));
            return true;
        }
    }

    @CanIgnoreReturnValue
    public boolean addEdge(T start, T end) {
        this.addNode(start);
        this.addNode(end);
        return this.map.get(start).addChild(end) && this.map.get(end).addParent(start);
    }

    public Node<T> getNode(T content) {
        return this.map.get(content);
    }

    public Set<T> getParents(T content) {
        return this.getNode(content).getParents();
    }

    public Set<T> getChildren(T content) {
        return this.getNode(content).getChildren();
    }

    public Set<T> getSharedParents(T content1, T content2) {
        return Sets.intersection(this.getParents(content1), this.getParents(content2));
    }

    public Set<T> getSharedChildren(T content1, T content2) {
        return Sets.intersection(this.getChildren(content1), this.getChildren(content2));
    }

    public void clear() {
        this.map.clear();
    }

    public int maxDistance(T child, T parent) {
        Map<T, Integer> memo = new HashMap<>();
        return dfs(child, parent, memo);
    }

    private int dfs(T current, T target, Map<T, Integer> memo) {
        if (current.equals(target)) {
            return 0;
        }
        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        Set<T> parents = this.getParents(current);
        int maxDist = -1; // 初始化为不可达
        for (T next : parents) {
            int dist = dfs(next, target, memo);
            if (dist != -1) {
                maxDist = Math.max(maxDist, dist + 1);
            }
        }
        memo.put(current, maxDist);
        return maxDist;
    }

    @Override
    public @NotNull Iterator<Map.Entry<T, Node<T>>> iterator() {
        return this.map.entrySet().iterator();
    }

    public static class Node<T> {
        protected final T content;
        protected final Set<T> children = new LinkedHashSet<>();
        protected final Set<T> parents = new LinkedHashSet<>();

        public Node(T content) {
            this.content = content;
        }

        public boolean hasChild(T content) {
            return this.children.contains(content);
        }

        public boolean hasParent(T content) {
            return this.parents.contains(content);
        }

        @CanIgnoreReturnValue
        public boolean addChild(T content) {
            return this.children.add(content);
        }

        @CanIgnoreReturnValue
        public boolean addParent(T content) {
            return this.parents.add(content);
        }

        public boolean isRoot() {
            return this.parents.isEmpty();
        }

        public boolean isEnd() {
            return this.children.isEmpty();
        }

        public T getContent() {
            return this.content;
        }

        public Set<T> getChildren() {
            return this.children;
        }

        public Set<T> getParents() {
            return this.parents;
        }
    }
}
