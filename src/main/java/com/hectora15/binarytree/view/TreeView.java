package com.hectora15.binarytree.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.Cursor;

import static com.hectora15.binarytree.view.TreeViewConstants.*;

/**
 * Factory for visual components (no business logic)
 */
public class TreeView {

    private final BorderPane mainPanel;
    private final HBox bottomBar;
    private final ScrollPane scrollPane;
    private final Pane centralPanel;
    private final Pane edgesLayer;
    private final AnchorPane overlayPane;
    private final StackPane rootStack;

    private final Button switchModeButton;
    private final Button modeButton;
    private final Button clearButton;
    private final TextField textField;

    private final Text inOrderText;
    private final Text preOrderText;
    private final Text postOrderText;
    private final Text creditsText;

    public TreeView() {
        // Create layouts
        mainPanel = new BorderPane();
        centralPanel = new Pane();
        centralPanel.getStyleClass().add("central-panel");

        edgesLayer = new Pane();
        edgesLayer.setPickOnBounds(false);
        edgesLayer.setMouseTransparent(true);
        centralPanel.getChildren().add(edgesLayer);

        scrollPane = new ScrollPane(centralPanel);
        scrollPane.setFocusTraversable(false);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        overlayPane = new AnchorPane();
        overlayPane.getStyleClass().add("overlay-pane");
        overlayPane.setFocusTraversable(false);
        overlayPane.setMouseTransparent(false);
        overlayPane.setPickOnBounds(false);

        rootStack = new StackPane(scrollPane, overlayPane);

        // Create bottom bar
        bottomBar = createBottomBar();
        switchModeButton = (Button) bottomBar.getChildren().get(0);
        modeButton = (Button) bottomBar.getChildren().get(1);
        textField = (TextField) bottomBar.getChildren().get(2);

        // Create overlay components
        clearButton = createClearButton();
        overlayPane.getChildren().add(clearButton);

        creditsText = createCreditsText();
        overlayPane.getChildren().add(creditsText);

        inOrderText = createOrderText(10.0);
        preOrderText = createOrderText(30.0);
        postOrderText = createOrderText(50.0);
        overlayPane.getChildren().addAll(postOrderText, preOrderText, inOrderText);

        // Assemble main panel
        mainPanel.setCenter(rootStack);
        mainPanel.setBottom(bottomBar);
    }

    private HBox createBottomBar() {
        HBox bar = new HBox(BOTTOM_BAR_SPACING);
        bar.getStyleClass().add("bottom-bar");
        bar.setMinHeight(BOTTOM_BAR_HEIGHT);
        bar.setPadding(new Insets(BOTTOM_BAR_PADDING));
        bar.setAlignment(Pos.CENTER);
        bar.setFocusTraversable(false);

        Button switchMode = new Button("Switch Mode");
        switchMode.setFocusTraversable(false);
        addHoverEffect(switchMode);

        Button mode = new Button();
        mode.setGraphic(IconFactory.createIcon(IconFactory.IconType.ADD, ICON_SIZE_SMALL));
        mode.setFocusTraversable(false);
        addHoverEffect(mode);

        TextField input = new TextField();
        input.setFocusTraversable(false);
        input.getStyleClass().add("text-box");
        input.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(input, Priority.ALWAYS);

        bar.getChildren().addAll(switchMode, mode, input);
        return bar;
    }

    private Button createClearButton() {
        Button btn = new Button();
        btn.setPrefSize(CLEAR_BUTTON_SIZE, CLEAR_BUTTON_SIZE);
        btn.setMinSize(CLEAR_BUTTON_SIZE, CLEAR_BUTTON_SIZE);
        btn.setMaxSize(CLEAR_BUTTON_SIZE, CLEAR_BUTTON_SIZE);
        btn.setAlignment(Pos.CENTER);
        btn.setPadding(Insets.EMPTY);
        btn.setFocusTraversable(false);
        btn.setGraphic(IconFactory.createIcon(IconFactory.IconType.CLEAR, ICON_SIZE_LARGE));
        btn.getStyleClass().remove("button");
        btn.getStyleClass().add("clear-button");

        AnchorPane.setRightAnchor(btn, 10.0);
        AnchorPane.setTopAnchor(btn, 10.0);

        addHoverEffect(btn);
        return btn;
    }

    private Text createOrderText(double bottomOffset) {
        Text text = new Text();
        text.getStyleClass().add("typed-text");
        text.setFill(Color.web(ORDER_TEXT_COLOR));
        AnchorPane.setLeftAnchor(text, 10.0);
        AnchorPane.setBottomAnchor(text, bottomOffset);
        return text;
    }

    private Text createCreditsText() {
        Text text = new Text("Made by @HectorA15 && @Angelsol2");
        text.getStyleClass().add("typed-text");
        text.setFill(Color.DIMGRAY);
        AnchorPane.setLeftAnchor(text, 10.0);
        AnchorPane.setTopAnchor(text, 10.0);
        return text;
    }

    public static void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            button.getStyleClass().add("button-highlighted");
            button.setCursor(Cursor.HAND);
        });
        button.setOnMouseExited(e -> {
            button.getStyleClass().remove("button-highlighted");
        });
    }

    // Getters
    public BorderPane getMainPanel() { return mainPanel; }
    public Pane getCentralPanel() { return centralPanel; }
    public Pane getEdgesLayer() { return edgesLayer; }
    public ScrollPane getScrollPane() { return scrollPane; }
    public StackPane getRootStack() { return rootStack; }

    public Button getSwitchModeButton() { return switchModeButton; }
    public Button getModeButton() { return modeButton; }
    public Button getClearButton() { return clearButton; }
    public TextField getTextField() { return textField; }

    public Text getInOrderText() { return inOrderText; }
    public Text getPreOrderText() { return preOrderText; }
    public Text getPostOrderText() { return postOrderText; }
}
