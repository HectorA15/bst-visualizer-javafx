package com.hectora15.binarytree.renderer;

import com.hectora15.binarytree.mapper.NodeButtonMapper;
import com.hectora15.binarytree.model.BinaryTree;
import com.hectora15.binarytree.model.TreeNode;
import com.hectora15.binarytree.view.TreeView;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.Map;

import static com.hectora15.binarytree.view.TreeViewConstants.EDGE_LINE_WIDTH;

/**
 * Renderiza el árbol visualmente (nodos y aristas)
 */
public class TreeRenderer {
    private final TreeView view;
    private final NodeButtonMapper mapper;
    private final NodePositionCalculator calculator;

    public TreeRenderer(TreeView view, NodeButtonMapper mapper) {
        this.view = view;
        this.mapper = mapper;
        this.calculator = new NodePositionCalculator();
    }

    public void render(BinaryTree tree) {
        TreeNode root = tree.getRoot();
        if (root == null) {
            clearCanvas();
            return;
        }

        double viewportWidth = getViewportWidth();
        Map<TreeNode, Point2D> positions = calculator.calculatePositions(root, viewportWidth);

        drawNodes(positions);

        // Draw edges after layout is updated
        Platform.runLater(() -> drawEdges(root));
    }

    private void drawNodes(Map<TreeNode, Point2D> positions) {
        Pane canvas = view.getCentralPanel();

        for (Map.Entry<TreeNode, Point2D> entry : positions.entrySet()) {
            TreeNode node = entry.getKey();
            Point2D position = entry.getValue();

            Button button = mapper.getButton(node);
            if (button == null) {
                button = createNodeButton(node);
                mapper.register(node, button);
                canvas.getChildren().add(button);
            }

            // Force layout calculation
            button.applyCss();
            button.layout();

            double nodeWidth = button.getBoundsInParent().getWidth();
            double x = position.getX() - nodeWidth / 2.0;
            double y = position.getY();

            button.setLayoutX(x);
            button.setLayoutY(y);
        }
    }

    private Button createNodeButton(TreeNode node) {
        Button button = new Button(String.valueOf(node.getWeight()));
        button.getStyleClass().add("button");
        TreeView.addHoverEffect(button);
        return button;
    }

    private void drawEdges(TreeNode root) {
        Pane edgesLayer = view.getEdgesLayer();
        edgesLayer.getChildren().clear();
        drawEdgesRecursive(root, edgesLayer);
    }

    private void drawEdgesRecursive(TreeNode node, Pane edgesLayer) {
        if (node == null) return;

        Button parentButton = mapper.getButton(node);
        if (parentButton == null) return;

        if (node.getLeft() != null) {
            Button childButton = mapper.getButton(node.getLeft());
            if (childButton != null) {
                Line line = createLineBetween(parentButton, childButton);
                edgesLayer.getChildren().add(line);
            }
            drawEdgesRecursive(node.getLeft(), edgesLayer);
        }

        if (node.getRight() != null) {
            Button childButton = mapper.getButton(node.getRight());
            if (childButton != null) {
                Line line = createLineBetween(parentButton, childButton);
                edgesLayer.getChildren().add(line);
            }
            drawEdgesRecursive(node.getRight(), edgesLayer);
        }
    }

    private Line createLineBetween(Button a, Button b) {
        a.applyCss();
        a.layout();
        b.applyCss();
        b.layout();

        Bounds aBounds = a.getBoundsInParent();
        Bounds bBounds = b.getBoundsInParent();

        double startX = aBounds.getMinX() + aBounds.getWidth() / 2.0;
        double startY = aBounds.getMinY() + aBounds.getHeight() / 2.0;
        double endX = bBounds.getMinX() + bBounds.getWidth() / 2.0;
        double endY = bBounds.getMinY() + bBounds.getHeight() / 2.0;

        Line line = new Line(startX, startY, endX, endY);
        line.setStrokeWidth(EDGE_LINE_WIDTH);
        line.setStroke(Color.LIGHTGRAY);
        line.setMouseTransparent(true);
        return line;
    }

    public void clearCanvas() {
        view.getCentralPanel().getChildren().clear();
        view.getCentralPanel().getChildren().add(view.getEdgesLayer());
        view.getEdgesLayer().getChildren().clear();
        mapper.clear();
    }

    private double getViewportWidth() {
        double vpWidth = view.getScrollPane().getViewportBounds().getWidth();
        return vpWidth > 0 ? vpWidth : view.getCentralPanel().getWidth();
    }
}