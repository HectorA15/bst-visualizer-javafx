package org.example;

import javafx.scene.control.Button;

public class Node {
  public int weight;
  public Node left, right;
  public Node previous;
  public Button visual;

  public Node(int peso) {
    this.weight = peso;
    this.right = null;
    this.left = null;
    this.previous = null;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public Node getLeft() {
    return left;
  }

  public void setLeft(Node left) {
    this.left = left;
  }

  public Node getRight() {
    return right;
  }

  public void setRight(Node right) {
    this.right = right;
  }

  public Node getPrevious() {
    return previous;
  }

  public void setPrevious(Node previous) {
    this.previous = previous;
  }
}
