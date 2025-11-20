package org.example;

public class BinaryTree {

  private Node root = null;

  public BinaryTree() {
    this.root = null;
  }

  public void insert(int weight) {
    root = insertRec(root, weight);
  }

  private Node insertRec(Node node, int weight) {
    if (node == null) return new Node(weight);
    if (weight < node.getWeight()) {
      node.setLeft(insertRec(node.getLeft(), weight));
    } else if (weight > node.getWeight()) {
      node.setRight(insertRec(node.getRight(), weight));
    } else {
    }
    return node;
  }

  public void delete(int weight) {
    Node parent = null;
    Node node = root;

    // case 1: search for the node to delete
    while (node != null && node.getWeight() != weight) {
      parent = node;
      if (weight < node.getWeight()) node = node.getLeft();
      else node = node.getRight();
    }

    if (node == null) {
      System.out.println("Node not found: " + weight);
      return;
    }

    // case 2: node has no right child
    if (node.getRight() == null) {
      if (parent == null) {
        root = node.getLeft();
      } else if (parent.getLeft() == node) {
        parent.setLeft(node.getLeft());
      } else {
        parent.setRight(node.getLeft());
      }
      node.setLeft(null);
      node.setRight(null);
      System.out.println("Deleted node: " + weight);
      return;
    }

    // case 3: node has right child
    Node succParent = node;
    Node successor = node.getRight();
    while (successor.getLeft() != null) {
      succParent = successor;
      successor = successor.getLeft();
    }

    if (succParent != node) {
      succParent.setLeft(successor.getRight()); // Link successor's right child to its parent
      successor.setRight(node.getRight()); // Link node's right child to successor
    }
    System.out.println("Deleted node: " + weight);
    successor.setLeft(node.getLeft()); // Link node's left child to successor

    if (parent == null) {
      root = successor; // Deleting the root node
    } else if (parent.getLeft() == node) {
      parent.setLeft(successor); // Link parent to successor
    } else {
      parent.setRight(successor); // Link parent to successor
    }
    // Clean up the deleted node
    node.setLeft(null);
    node.setRight(null);
  }

  public Node setRoot(Node root) {
    this.root = root;
    return root;
  }

  public String preOrder() {
    StringBuilder sb = new StringBuilder();
    preOrderRec(root, sb);
    return sb.toString();
  }

  private void preOrderRec(Node node, StringBuilder s) {
    if (node == null) return;
    s.append(node.weight + ",").append("");
    preOrderRec(node.getLeft(), s);
    preOrderRec(node.getRight(), s);
  }

  public String inOrder() {
    StringBuilder sb = new StringBuilder();
    inOrderRec(root, sb);
    return sb.toString();
  }

  private void inOrderRec(Node node, StringBuilder s) {
    if (node == null) return;
    inOrderRec(node.getLeft(), s);
    s.append(node.weight + ",").append("");
    inOrderRec(node.getRight(), s);
  }

  public String postOrder() {
    StringBuilder sb = new StringBuilder();
    postOrderRec(root, sb);
    return sb.toString();
  }

  private void postOrderRec(Node node, StringBuilder s) {
    if (node == null) return;
    preOrderRec(node.getLeft(), s);
    preOrderRec(node.getRight(), s);
    s.append(node.weight + ",").append("");
  }
}
