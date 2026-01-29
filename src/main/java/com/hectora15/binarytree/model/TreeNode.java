package com.hectora15.binarytree.model;


public class TreeNode {
  private int weight;
  private TreeNode left;
  private TreeNode right;

  public TreeNode(int weight){
    this.weight = weight;
    this.left = null;
    this.right = null;
  }

  public int getWeight() {
    return this.weight;
  }
  public void setWeight(int weight) {
    this.weight = weight;
  }

  public TreeNode getLeft() {
    return this.left;
  }
  public void setLeft(TreeNode left) {
    this.left = left;
  }

  public TreeNode getRight() {
    return this.right;
  }
  public void setRight(TreeNode right) {
    this.right = right;
  }

}
