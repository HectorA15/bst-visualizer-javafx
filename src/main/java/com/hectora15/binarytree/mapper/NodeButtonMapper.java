package com.hectora15.binarytree.mapper;

import com.hectora15.binarytree.model.TreeNode;
import javafx.scene.control.Button;
import java.util.HashMap;
import java.util.Map;

/**
 * Vincula TreeNodes (modelo) con Buttons (vista)
 * Esto desacopla el modelo de la UI
 */
public class NodeButtonMapper {
    private final Map<TreeNode, Button> nodeToButton = new HashMap<>();
    private final Map<Button, TreeNode> buttonToNode = new HashMap<>();

    public void register(TreeNode node, Button button) {
        nodeToButton.put(node, button);
        buttonToNode.put(button, node);
    }

    public Button getButton(TreeNode node) {
        return nodeToButton.get(node);
    }

    public TreeNode getNode(Button button) {
        return buttonToNode.get(button);
    }

    public void unregister(TreeNode node) {
        Button button = nodeToButton.remove(node);
        if (button != null) {
            buttonToNode.remove(button);
        }
    }

    public void clear() {
        nodeToButton.clear();
        buttonToNode.clear();
    }

    public boolean hasButton(TreeNode node) {
        return nodeToButton.containsKey(node);
    }
}