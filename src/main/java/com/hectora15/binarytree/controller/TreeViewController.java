package com.hectora15.binarytree.controller;

import com.hectora15.binarytree.mapper.NodeButtonMapper;
import com.hectora15.binarytree.renderer.TreeRenderer;
import com.hectora15.binarytree.service.NotificationService;
import com.hectora15.binarytree.service.TreeService;
import com.hectora15.binarytree.view.IconFactory;
import com.hectora15.binarytree.view.TreeView;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.hectora15.binarytree.view.TreeViewConstants.*;

/**
 * Controller that manages the event logic
 */
public class TreeViewController {
    private final TreeView view;
    private final TreeService treeService;
    private final TreeRenderer renderer;
    private final NotificationService notifier;
    private final NodeButtonMapper mapper;

    private int modeIndex = 0;
    private final ImageView[] modeIcons;

    public TreeViewController(
            TreeView view,
            TreeService treeService,
            TreeRenderer renderer,
            NotificationService notifier,
            NodeButtonMapper mapper
    ) {
        this.view = view;
        this.treeService = treeService;
        this.renderer = renderer;
        this.notifier = notifier;
        this.mapper = mapper;

        this.modeIcons = new ImageView[] {
                IconFactory.createIcon(IconFactory.IconType.ADD, ICON_SIZE_SMALL),
                IconFactory.createIcon(IconFactory.IconType.DELETE, ICON_SIZE_SMALL),
                IconFactory.createIcon(IconFactory.IconType.SEARCH, ICON_SIZE_SMALL)
        };

        setupEventHandlers();
    }

    private void setupEventHandlers() {
        view.getSwitchModeButton().setOnAction(e -> handleSwitchMode());
        view.getModeButton().setOnAction(e -> handleModeAction());
        view.getClearButton().setOnAction(e -> handleClear());
    }

    private void handleSwitchMode() {
        modeIndex = (modeIndex + 1) % 3;
        view.getModeButton().setGraphic(modeIcons[modeIndex]);
    }

    private void handleModeAction() {
        Integer value = parseInput();
        if (value == null) return;

        switch (modeIndex) {
            case 0 -> handleInsert(value);
            case 1 -> handleDelete(value);
            case 2 -> handleSearch(value);
        }
    }

    private void handleInsert(int value) {
        TreeService.InsertResult result = treeService.insert(value);

        if (result == TreeService.InsertResult.DUPLICATE) {
            notifier.showWarning("Node already exists");
            clearTextField();
            return;
        }

        renderer.render(treeService.getTree());
        updateOrderTexts();
        clearTextField();
    }

    private void handleDelete(int value) {
        TreeService.DeleteResult result = treeService.delete(value);

        switch (result) {
            case NOT_FOUND -> {
                notifier.showWarning("Node doesn't exist");
                return;
            }
            case FAILED -> {
                notifier.showError("Delete failed");
                return;
            }
            case SUCCESS -> {
                renderer.render(treeService.getTree());
                updateOrderTexts();
                clearTextField();
            }
        }
    }

    private void handleSearch(int value) {
        TreeService.SearchResult result = treeService.search(value);

        if (!result.isFound()) {
            notifier.showWarning("Node not found");
            clearTextField();
            return;
        }

        Button button = mapper.getButton(result.getNode());
        if (button != null) {
            highlightButton(button);
        }

        clearTextField();
    }

    private void handleClear() {
        if (treeService.isEmpty()) return;

        treeService.clear();
        renderer.clearCanvas();
        updateOrderTexts();
        notifier.showSuccess("Tree cleared");
    }

    private void highlightButton(Button button) {
        String originalStyle = button.getStyle();
        button.setStyle(
                "-fx-border-color: " + SEARCH_HIGHLIGHT_COLOR + "; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 8px"
        );

        PauseTransition pause = new PauseTransition(Duration.seconds(SEARCH_HIGHLIGHT_DURATION));
        pause.setOnFinished(e -> button.setStyle(originalStyle));
        pause.play();
    }

    private Integer parseInput() {
        TextField textField = view.getTextField();
        String text = textField.getText();

        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            notifier.showError("Invalid number");
            clearTextField();
            return null;
        }
    }

    private void clearTextField() {
        view.getTextField().clear();
    }

    public void updateOrderTexts() {
        Text inOrder = view.getInOrderText();
        Text preOrder = view.getPreOrderText();
        Text postOrder = view.getPostOrderText();

        inOrder.setText("InOrder:\t\t" + treeService.getTree().inOrder());
        preOrder.setText("PreOrder:\t\t" + treeService.getTree().preOrder());
        postOrder.setText("PostOrder:\t" + treeService.getTree().postOrder());
    }
}