package org.example;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Application;

public class UI extends Application {
  Button switchModeButton;
  Button modeButton;
  Button raiz;
  Button clearButton;

  TextField textField;
  ScrollPane scroll;
  BorderPane panelPrincipal;
  HBox bottomBar;
  Pane centralPanel;
  Pane edgesLayer;

  Node datoRaiz;
  int modeCount = 0;

  Image addImage;
  Image deleteImage;
  Image searchImage;
  Image clearImage;
  ImageView addView;
  ImageView deleteView;
  ImageView searchView;
  ImageView clearView;

  BinaryTree arbol;

  Text creditsText;
  Text orderText;
  Text orderPreText;
  Text orderPostText;

  private void createClearButton() {

    if (clearButton == null) {
      clearButton = new Button();
      clearButton.setGraphic(clearView);
      clearButton.getStyleClass().remove("button");
      clearButton.getStyleClass().add("clear-button");
      AnchorPane.setRightAnchor(clearButton, 10.0);
      AnchorPane.setTopAnchor(clearButton, 10.0);
    }

    if (!centralPanel.getChildren().contains(clearButton)) {
      centralPanel.getChildren().add(clearButton);
    }

    clearButton.setOnAction(
        e -> {
          if (datoRaiz == null) return; // nothing to clear
          // clear logical tree
          arbol = new BinaryTree();
          datoRaiz = null;
          raiz = null;
          // clear visual nodes
          centralPanel.getChildren().clear();
          centralPanel.getChildren().add(edgesLayer); // re-add edges layer
          edgesLayer.getChildren().clear();

          // re-add texts and buttons
          centralPanel
              .getChildren()
              .addAll(creditsText, orderText, orderPreText, orderPostText, clearButton);

          // update orders text
          updateOrdersText();
        });
  }

  private void createOrdersText() {
    orderText = new Text();
    orderText.getStyleClass().add("typed-text");
    orderText.setFill(javafx.scene.paint.Color.web("#b8c1cc"));
    AnchorPane.setLeftAnchor(orderText, 10.0);
    AnchorPane.setBottomAnchor(orderText, 10.0);
    // preOrder text
    orderPreText = new Text();
    orderPreText.getStyleClass().add("typed-text");
    orderPreText.setFill(javafx.scene.paint.Color.web("#b8c1cc"));
    AnchorPane.setLeftAnchor(orderPreText, 10.0);
    AnchorPane.setBottomAnchor(orderPreText, 30.0);
    // postOrder text
    orderPostText = new Text();
    orderPostText.getStyleClass().add("typed-text");
    orderPostText.setFill(javafx.scene.paint.Color.web("#b8c1cc"));
    AnchorPane.setLeftAnchor(orderPostText, 10.0);
    AnchorPane.setBottomAnchor(orderPostText, 50.0);

    centralPanel.getChildren().addAll(orderText, orderPreText, orderPostText);
    updateOrdersText();
  }

  private void createBottomBar() {
    bottomBar = new HBox();
    bottomBar.getStyleClass().add("bottom-bar");
    bottomBar.setMinHeight(60);
    bottomBar.setSpacing(10);
    bottomBar.setPadding(new Insets(10, 10, 10, 10));
    bottomBar.setAlignment(Pos.CENTER);

    // initialize buttons and text field
    switchModeButton = new Button("Switch Mode");
    modeButton = new Button();
    modeButton.setGraphic(addView);
    textField = new TextField();
    textField.getStyleClass().add("text-box");
    // make text field expand to fill available space
    textField.setMaxWidth(Double.MAX_VALUE);
    textField.setMaxHeight(Double.MAX_VALUE);
    HBox.setHgrow(textField, Priority.ALWAYS);

    bottomBar
        .getChildren()
        .addAll(
            switchModeButton, modeButton, textField); // add buttons and text field to bottom bar
  }

  private void createCreditsText() {
    creditsText = new Text();
    creditsText.setText("Made by @HectorA15 && @Angelsol2");
    creditsText.getStyleClass().add("typed-text");
    creditsText.setFill(Color.DIMGRAY);
    AnchorPane.setLeftAnchor(creditsText, 10.0);
    AnchorPane.setTopAnchor(creditsText, 10.0);
    centralPanel.getChildren().add(creditsText);
  }

  private void createActionButtons() {
    switchModeButton.setOnAction(
        Event -> {
          modeCount = (modeCount + 1) % 3;
          switch (modeCount) {
            case 0 -> modeButton.setGraphic(addView);
            case 1 -> modeButton.setGraphic(deleteView);
            case 2 -> modeButton.setGraphic(searchView);
          }
        });

    modeButton.setOnAction(
        event -> {
          if (modeCount == 0) { // add mode
            String txt = textField.getText();
            if (txt == null || txt.trim().isEmpty()) return;
            int val;
            try {
              val = Integer.parseInt(txt.trim());
            } catch (NumberFormatException ex) {
              return;
            }

            if (raiz == null) { // first node (root)
              raiz = new Button(String.valueOf(val));
              raiz.getStyleClass().add("button");
              datoRaiz = new Node(val);
              datoRaiz.visual = raiz;
              arbol.setRoot(datoRaiz);

              centralPanel.getChildren().add(raiz);
              raiz.setLayoutX(centralPanel.getWidth() / 2);
              raiz.setLayoutY(30);
              updateOrdersText();
              textField.clear();
            } else {
              Button newNode = new Button(String.valueOf(val));
              newNode.getStyleClass().add("button");
              calcularLugar(val, raiz, 1, datoRaiz, newNode);
              textField.clear();
              redrawTree();
              updateOrdersText();
            }
          } else if (modeCount == 1) { // delete mode
            String txt = textField.getText();
            if (txt == null || txt.trim().isEmpty()) return; // empty input
            int val;
            try {
              val = Integer.parseInt(txt.trim());
            } catch (NumberFormatException ex) {
              return;
            }

            boolean deleted = arbol.delete(val);
            if (deleted) {
              // search and remove the button from centralPanel
              Button toRemove = null;
              for (javafx.scene.Node child :
                  centralPanel.getChildren()) { // iterate through children
                if (child instanceof Button) { // check if child is a button
                  Button b = (Button) child; // cast to button
                  if (b.getText().equals(String.valueOf(val))) { // check if text matches
                    toRemove = b; // set button to remove
                    break; // exit loop once found
                  }
                }
              }
              if (toRemove != null)
                centralPanel.getChildren().remove(toRemove); // remove from panel

              textField.clear();

              // --- synchronize the UI with the logical tree after delete ---
              Node newRoot = arbol.getRoot(); // get current logical root
              datoRaiz = newRoot;
              if (newRoot != null) {
                if (newRoot.visual == null) {
                  Button b = new Button(String.valueOf(newRoot.getWeight()));
                  b.getStyleClass().add("button");
                  newRoot.visual = b;
                }
                raiz = newRoot.visual;
              } else {
                raiz = null;
              }
              redrawTree();
              updateOrdersText();
            }
          } else if (modeCount == 2) { // search mode
            String txt = textField.getText();
            if (txt == null || txt.trim().isEmpty()) return;
            int val;
            try {
              val = Integer.parseInt(txt.trim());
            } catch (NumberFormatException ex) {
              return;
            }

            Node foundNode = arbol.search(val);
            if (foundNode != null && foundNode.visual != null) {
              foundNode.visual.setStyle(
                  "-fx-border-color: red; -fx-border-width: 3px;  -fx-border-radius: 8px");
              PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(1));
              pause.setOnFinished(ev -> foundNode.visual.setStyle(""));
              pause.play();
            }
            textField.clear();
          }
        });
  }

  private void createCenterPanel() {
    centralPanel = new AnchorPane();
    centralPanel.getStyleClass().add("central-panel");
    centralPanel.setFocusTraversable(false);
    centralPanel.setMaxWidth(Double.MAX_VALUE);
    centralPanel.setMaxHeight(Double.MAX_VALUE);

    // create a dedicated layer for edges (lines) and add it as the bottom-most child
    edgesLayer = new Pane();
    edgesLayer.setPickOnBounds(false); // don't intercept mouse events
    edgesLayer.setMouseTransparent(true);

    // add edges layer first so it's at the back
    centralPanel.getChildren().add(edgesLayer);

    scroll = new ScrollPane(centralPanel);
    scroll.setFocusTraversable(false);
    scroll.getStyleClass().add("scroll-pane");
    scroll.setFitToWidth(true);
    scroll.setFitToHeight(true);

    // Initialize tree (no nodes yet)
    arbol.inOrder();
    arbol.preOrder();
    arbol.postOrder();
  }

  private void createImages() {
    addImage = new Image(this.getClass().getResourceAsStream("/images/add.png"));
    deleteImage = new Image(this.getClass().getResourceAsStream("/images/delete.png"));
    searchImage = new Image(this.getClass().getResourceAsStream("/images/search.png"));
    clearImage = new Image(this.getClass().getResourceAsStream("/images/clear.png"));
    addView = new ImageView(addImage);
    deleteView = new ImageView(deleteImage);
    searchView = new ImageView(searchImage);
    clearView = new ImageView(clearImage);
    // adjust icon size
    addView.setFitWidth(18);
    addView.setFitHeight(18);
    addView.setPreserveRatio(true);
    deleteView.setFitWidth(18);
    deleteView.setFitHeight(18);
    deleteView.setPreserveRatio(true);
    searchView.setFitWidth(18);
    searchView.setFitHeight(18);
    searchView.setPreserveRatio(true);
  }

  @Override
  public void start(Stage stage) throws Exception {
    arbol = new BinaryTree();

    panelPrincipal = new BorderPane();
    createImages();
    createBottomBar();
    createCenterPanel();

    createClearButton();
    createCreditsText();
    createOrdersText();
    createActionButtons();

    panelPrincipal.setBottom(bottomBar);
    panelPrincipal.setCenter(scroll);
    // show stage
    Scene escena = new Scene(panelPrincipal, 800, 600);
    String css = this.getClass().getResource("/styles.css").toExternalForm();
    escena.getStylesheets().add(css);
    stage.setTitle("Binary Tree Visualization");
    stage.setScene(escena);
    stage.show();

    // if the panel size changes, rearrange nodes
    centralPanel.widthProperty().addListener((obs, oldVal, newVal) -> redrawTree());
    centralPanel.heightProperty().addListener((obs, oldVal, newVal) -> redrawTree());

    escena.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            modeButton.fire();
          } else if (event.getCode() == KeyCode.ESCAPE) {
            Platform.exit();
          } else if (event.getCode() == KeyCode.SHIFT) {
            switchModeButton.fire();
          }
        });

    redrawTree();
    updateOrdersText();
  }

  // reposition all nodes and redraw edges
  public void redrawTree() {
    if (datoRaiz == null || datoRaiz.visual == null) {
      edgesLayer.getChildren().clear(); // clear edges if no nodes
      return;
    }
    // Ensure the root Button is present in the panel (after edgesLayer and orderText)
    if (!centralPanel.getChildren().contains(datoRaiz.visual)) {
      centralPanel.getChildren().add(datoRaiz.visual);
    }
    // Position the root at the center
    double centerX = getViewportWidth() / 2;
    datoRaiz.visual.setLayoutX(centerX);
    datoRaiz.visual.setLayoutY(30);
    // Reposition children recursively
    positionChildrenRecursively(datoRaiz, 1);
    // after layout updated, redraw edges using real bounds
    Platform.runLater(this::redrawEdges);
  }

  // helper to position children recursively
  private void positionChildrenRecursively(Node nodoLogicoPadre, int nivelActual) {
    if (nodoLogicoPadre == null || nodoLogicoPadre.visual == null) return;

    Button padreVisual = nodoLogicoPadre.visual;
    double available = getViewportWidth();
    double espacio = (available / 2) / Math.pow(2, nivelActual);

    if (nodoLogicoPadre.getLeft() != null) {
      Node leftNode = nodoLogicoPadre.getLeft();
      // ensure the Button exists in the panel
      if (leftNode.visual != null && !centralPanel.getChildren().contains(leftNode.visual)) {
        centralPanel.getChildren().add(leftNode.visual);
      }
      if (leftNode.visual != null) {
        leftNode.visual.setLayoutY(padreVisual.getLayoutY() + 70);
        leftNode.visual.setLayoutX(padreVisual.getLayoutX() - espacio);
      }
      positionChildrenRecursively(leftNode, nivelActual + 1);
    }

    if (nodoLogicoPadre.getRight() != null) {
      Node rightNode = nodoLogicoPadre.getRight();
      if (rightNode.visual != null && !centralPanel.getChildren().contains(rightNode.visual)) {
        centralPanel.getChildren().add(rightNode.visual);
      }
      if (rightNode.visual != null) {
        rightNode.visual.setLayoutY(padreVisual.getLayoutY() + 70);
        rightNode.visual.setLayoutX(padreVisual.getLayoutX() + espacio);
      }
      positionChildrenRecursively(rightNode, nivelActual + 1);
    }
  }

  // draw edges between parent and child nodes
  private void redrawEdges() {
    edgesLayer.getChildren().clear();
    if (datoRaiz == null) return;
    drawEdgesRecursively(datoRaiz);
  }

  // helper to draw edges recursively
  private void drawEdgesRecursively(Node nodoLogico) {
    if (nodoLogico == null || nodoLogico.visual == null) return;
    Button parentBtn = nodoLogico.visual;

    // left child
    if (nodoLogico.getLeft() != null && nodoLogico.getLeft().visual != null) {
      Button childBtn = nodoLogico.getLeft().visual;
      Line line = createLineBetween(parentBtn, childBtn);
      edgesLayer.getChildren().add(line);
      drawEdgesRecursively(nodoLogico.getLeft());
    }

    // right child
    if (nodoLogico.getRight() != null && nodoLogico.getRight().visual != null) {
      Button childBtn = nodoLogico.getRight().visual;
      Line line = createLineBetween(parentBtn, childBtn);
      edgesLayer.getChildren().add(line);
      drawEdgesRecursively(nodoLogico.getRight());
    }
  }

  // create a line between two buttons
  private Line createLineBetween(Button a, Button b) {
    // Force layout so bounds estén actualizados
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
    line.setStrokeWidth(2);
    line.setStroke(Color.LIGHTGRAY);
    line.setMouseTransparent(true);
    return line;
  }

  // update the traversal order texts
  public void updateOrdersText() {
    String textOrder = arbol.preOrder();
    orderText.setText("PreOrder:\t\t" + textOrder);

    String textPre = arbol.inOrder();
    orderPreText.setText("Inorder:\t\t" + textPre);

    String textPost = arbol.postOrder();
    orderPostText.setText("PostOrder:\t" + textPost);
  }

  // calcula el lugar del nuevo nodo y lo inserta en el panel central
  public void calcularLugar(
      int weight, Button padreVisual, int nivelActual, Node nodoLogicoPadre, Button newButton) {
    int datoPadre = (Integer.parseInt(padreVisual.getText()));
    double available = getViewportWidth();
    double espacio = (available / 2) / Math.pow(2, nivelActual);

    if (weight < datoPadre) {
      if (nodoLogicoPadre.getLeft() == null) {
        centralPanel.getChildren().add(newButton);
        newButton.setLayoutY(padreVisual.getLayoutY() + 70);
        newButton.setLayoutX(padreVisual.getLayoutX() - espacio);
        Node nuevoHijo = new Node(weight);
        nuevoHijo.visual = newButton;
        nodoLogicoPadre.setLeft(nuevoHijo);
      } else {

        calcularLugar(
            weight,
            nodoLogicoPadre.getLeft().visual,
            nivelActual + 1,
            nodoLogicoPadre.getLeft(),
            newButton);
      }
    } else if (weight > datoPadre) {
      if (nodoLogicoPadre.getRight() == null) {
        centralPanel.getChildren().add(newButton);
        newButton.setLayoutY(padreVisual.getLayoutY() + 70);
        newButton.setLayoutX(padreVisual.getLayoutX() + espacio);
        Node nuevoHijo = new Node(weight);
        nuevoHijo.visual = newButton;
        nodoLogicoPadre.setRight(nuevoHijo);
      } else {
        calcularLugar(
            weight,
            nodoLogicoPadre.getRight().visual,
            nivelActual + 1,
            nodoLogicoPadre.getRight(),
            newButton);
      }
    }
  }

  // get the current viewport width, fallback to centralPanel width
  private double getViewportWidth() {
    double vpWidth = scroll.getViewportBounds().getWidth();
    return vpWidth > 0 ? vpWidth : centralPanel.getWidth();
  }
}
