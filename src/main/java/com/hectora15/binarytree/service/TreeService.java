package com.hectora15.binarytree.service;

import com.hectora15.binarytree.model.BinaryTree;
import com.hectora15.binarytree.model.TreeNode;

/**
 * Servicio que encapsula operaciones del árbol
 */
public class TreeService {
    private BinaryTree tree;

    public TreeService() {
        this.tree = new BinaryTree();
    }

    public InsertResult insert(int value) {
        if (tree.search(value) != null) {
            return InsertResult.DUPLICATE;
        }
        tree.insert(value);
        return InsertResult.SUCCESS;
    }

    public DeleteResult delete(int value) {
        TreeNode node = tree.search(value);
        if (node == null) {
            return DeleteResult.NOT_FOUND;
        }

        boolean deleted = tree.delete(value);
        return deleted ? DeleteResult.SUCCESS : DeleteResult.FAILED;
    }

    public SearchResult search(int value) {
        TreeNode node = tree.search(value);
        if (node == null) {
            return new SearchResult(false, null);
        }
        return new SearchResult(true, node);
    }

    public void clear() {
        tree.clear();
    }

    public BinaryTree getTree() {
        return tree;
    }

    public boolean isEmpty() {
        return tree.getRoot() == null;
    }

    // Result types
    public enum InsertResult { SUCCESS, DUPLICATE }
    public enum DeleteResult { SUCCESS, NOT_FOUND, FAILED }

    public static class SearchResult {
        private final boolean found;
        private final TreeNode node;

        public SearchResult(boolean found, TreeNode node) {
            this.found = found;
            this.node = node;
        }

        public boolean isFound() { return found; }
        public TreeNode getNode() { return node; }
    }
}