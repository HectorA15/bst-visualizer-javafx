package com.hectora15.binarytree.renderer;

import com.hectora15.binarytree.model.TreeNode;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hectora15.binarytree.view.TreeViewConstants.*;

/**
 * Calcula posiciones X/Y para cada nodo del árbol
 */
public class NodePositionCalculator {

    public Map<TreeNode, Point2D> calculatePositions(TreeNode root, double viewportWidth) {
        Map<TreeNode, Point2D> positions = new HashMap<>();
        Map<TreeNode, Integer> indexMap = new HashMap<>();
        Map<TreeNode, Integer> levelMap = new HashMap<>();

        AtomicInteger counter = new AtomicInteger(0);
        inOrderTraversal(root, counter, 0, levelMap, indexMap);

        int maxIndex = indexMap.values().stream().max(Integer::compareTo).orElse(0);
        double totalCols = Math.max(1, maxIndex + 1);
        double hSpacing = viewportWidth / totalCols;
        double offsetX = hSpacing * HORIZONTAL_OFFSET_PERCENT;

        for (Map.Entry<TreeNode, Integer> entry : indexMap.entrySet()) {
            TreeNode node = entry.getKey();
            int idx = entry.getValue();
            int lvl = levelMap.getOrDefault(node, 0);

            double xCenter = offsetX + idx * hSpacing + hSpacing / 2.0;
            double y = TOP_MARGIN + lvl * VERTICAL_NODE_SPACING;

            positions.put(node, new Point2D(xCenter, y));
        }

        return positions;
    }

    private void inOrderTraversal(
            TreeNode node,
            AtomicInteger counter,
            int level,
            Map<TreeNode, Integer> levelMap,
            Map<TreeNode, Integer> indexMap
    ) {
        if (node == null) return;

        inOrderTraversal(node.getLeft(), counter, level + 1, levelMap, indexMap);
        levelMap.put(node, level);
        indexMap.put(node, counter.getAndIncrement());
        inOrderTraversal(node.getRight(), counter, level + 1, levelMap, indexMap);
    }
}