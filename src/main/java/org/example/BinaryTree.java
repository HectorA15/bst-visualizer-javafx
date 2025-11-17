package org.example;

public class BinaryTree {

    private Node root = null;

    public BinaryTree() {
        this.root = null;
    }

    public void insert(int weight) {
        Node newNode = new Node(weight);
        if (root == null) {
            root = newNode;
            return;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            if (weight < current.getWeight()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        if (weight < parent.getWeight()) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }
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
            return;
        }

        // case 3: node has right child
        Node succParent = node;
        Node successor = node.getRight();
        while (successor.getLeft() != null) {
            succParent = successor;
            successor = successor.getLeft();
        }

        // Eliminar successor de su posición actual y moverlo a la posición de node
        if (succParent != node) {
            // reemplazamos el enlace del succParent por el right del successor
            succParent.setLeft(successor.getRight());
            // successor toma el right original de node
            successor.setRight(node.getRight());
        }
        // successor toma el left original de node
        successor.setLeft(node.getLeft());

        // Enlazar successor en el padre de node (o como root)
        if (parent == null) {
            root = successor;
        } else if (parent.getLeft() == node) {
            parent.setLeft(successor);
        } else {
            parent.setRight(successor);
        }

        node.setLeft(null);
        node.setRight(null);
    }


    public void preOrder() {
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(Node node) {
        if (node == null) return;
        System.out.print(node.getWeight() + " ");
        preOrderRec(node.getLeft());
        preOrderRec(node.getRight());
    }
}
