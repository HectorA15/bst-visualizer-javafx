package com.hectora15.binarytree.model;

/**
 * Árbol binario de búsqueda (lógica pura)
 */
public class BinaryTree {
  private TreeNode root;

  public TreeNode getRoot() { return root; }

  public void insert(int value) {
    root = insertRec(root, value);
  }

  private TreeNode insertRec(TreeNode node, int value) {
    if (node == null) {
      return new TreeNode(value);
    }
    if (value < node.getWeight()) {
      node.setLeft(insertRec(node.getLeft(), value));
    } else if (value > node.getWeight()) {
      node.setRight(insertRec(node.getRight(), value));
    }
    return node;
  }

  public boolean delete(int value) {
    int initialSize = size();
    root = deleteRec(root, value);
    return size() < initialSize;
  }

  private TreeNode deleteRec(TreeNode node, int value) {
    if (node == null) return null;

    if (value < node.getWeight()) {
      node.setLeft(deleteRec(node.getLeft(), value));
    } else if (value > node.getWeight()) {
      node.setRight(deleteRec(node.getRight(), value));
    } else {
      // Node found - handle deletion cases
      if (node.getLeft() == null) return node.getRight();
      if (node.getRight() == null) return node.getLeft();

      // Node with two children: get inorder successor
      TreeNode successor = findMin(node.getRight());
      node.setWeight(successor.getWeight());
      node.setRight(deleteRec(node.getRight(), successor.getWeight()));
    }
    return node;
  }

  private TreeNode findMin(TreeNode node) {
    while (node.getLeft() != null) {
      node = node.getLeft();
    }
    return node;
  }

  public TreeNode search(int value) {
    return searchRec(root, value);
  }

  private TreeNode searchRec(TreeNode node, int value) {
    if (node == null || node.getWeight() == value) {
      return node;
    }
    if (value < node.getWeight()) {
      return searchRec(node.getLeft(), value);
    }
    return searchRec(node.getRight(), value);
  }

  public String inOrder() {
    StringBuilder sb = new StringBuilder();
    inOrderRec(root, sb);
    return sb.toString().trim();
  }

  private void inOrderRec(TreeNode node, StringBuilder sb) {
    if (node != null) {
      inOrderRec(node.getLeft(), sb);
      sb.append(node.getWeight()).append(" ");
      inOrderRec(node.getRight(), sb);
    }
  }

  public String preOrder() {
    StringBuilder sb = new StringBuilder();
    preOrderRec(root, sb);
    return sb.toString().trim();
  }

  private void preOrderRec(TreeNode node, StringBuilder sb) {
    if (node != null) {
      sb.append(node.getWeight()).append(" ");
      preOrderRec(node.getLeft(), sb);
      preOrderRec(node.getRight(), sb);
    }
  }

  public String postOrder() {
    StringBuilder sb = new StringBuilder();
    postOrderRec(root, sb);
    return sb.toString().trim();
  }

  private void postOrderRec(TreeNode node, StringBuilder sb) {
    if (node != null) {
      postOrderRec(node.getLeft(), sb);
      postOrderRec(node.getRight(), sb);
      sb.append(node.getWeight()).append(" ");
    }
  }

  public int size() {
    return sizeRec(root);
  }

  private int sizeRec(TreeNode node) {
    if (node == null) return 0;
    return 1 + sizeRec(node.getLeft()) + sizeRec(node.getRight());
  }

  public void clear() {
    root = null;
  }
}