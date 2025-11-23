package com.hectora15.binarytree;

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
import javafx.scene.control.Alert;

import javax.print.DocFlavor;
import java.util.Objects;

public class UI extends Application {
  Button switchModeButton;
  Button modeButton;
  Button root;
  Button clearButton;

  TextField textField;
  ScrollPane scroll;
  BorderPane panelPrincipal;
  HBox bottomBar;
  Pane centralPanel;
  Pane edgesLayer;
  StackPane rootStack;

  TreeNode datoRaiz;
  int modeCount = 0;

  Image addImage;
  Image deleteImage;
  Image searchImage;
  Image clearImage;
  Image aleatoryImage;
  ImageView addView;
  ImageView deleteView;
  ImageView searchView;
  ImageView clearView;
  ImageView aleatoryView;

  BinaryTree arbol;

  Text creditsText;
  Text orderText;
  Text orderPreText;
  Text orderPostText;

  // como es una variable estática final, o sea que nunca va a cambiar
  // se escribe en mayúsculas y se separa con guiones bajos
  private static final String ORDER_TEXT_COLOR_HEX = "#b8c1cc";

  private void createImages() {
    addImage =
        new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/add.png")));
    deleteImage =
        new Image(
            Objects.requireNonNull(this.getClass().getResourceAsStream("/images/delete.png")));
    searchImage =
        new Image(
            Objects.requireNonNull(this.getClass().getResourceAsStream("/images/search.png")));
    clearImage =
        new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/clear.png")));

    addView = new ImageView(addImage);
    deleteView = new ImageView(deleteImage);
    searchView = new ImageView(searchImage);
    clearView = new ImageView(clearImage);
    aleatoryView = new ImageView(aleatoryImage);
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

    clearView.setFitWidth(24);
    clearView.setFitHeight(24);
    clearView.setPreserveRatio(true);
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
    textField.setFocusTraversable(false);
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

  private void createClearButton() {

    if (clearButton == null) {
      clearButton = new Button();

      double size = 35;

      clearButton.setPrefSize(size, size);
      clearButton.setMinSize(size, size);
      clearButton.setMaxSize(size, size);

      clearButton.setAlignment(Pos.CENTER);
      clearButton.setPadding(Insets.EMPTY);
      clearButton.setFocusTraversable(false);
      clearButton.setGraphic(clearView);
      clearButton.getStyleClass().remove("button");
      clearButton.getStyleClass().add("clear-button");

      AnchorPane.setRightAnchor(clearButton, 10.0);
      AnchorPane.setTopAnchor(clearButton, 10.0);

      addEfectoHover(clearButton);
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
          root = null;
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
          Notification.show("SUCCESS", rootStack, "Tree cleared", 2000);
        });
  }

  private void createOrdersText() {
    orderText = new Text();
    orderText.getStyleClass().add("typed-text");
    orderText.setFill(javafx.scene.paint.Color.web(ORDER_TEXT_COLOR_HEX));
    AnchorPane.setLeftAnchor(orderText, 10.0);
    AnchorPane.setBottomAnchor(orderText, 10.0);
    // preOrder text
    orderPreText = new Text();
    orderPreText.getStyleClass().add("typed-text");
    orderPreText.setFill(javafx.scene.paint.Color.web(ORDER_TEXT_COLOR_HEX));
    AnchorPane.setLeftAnchor(orderPreText, 10.0);
    AnchorPane.setBottomAnchor(orderPreText, 30.0);
    // postOrder text
    orderPostText = new Text();
    orderPostText.getStyleClass().add("typed-text");
    orderPostText.setFill(javafx.scene.paint.Color.web(ORDER_TEXT_COLOR_HEX));
    AnchorPane.setLeftAnchor(orderPostText, 10.0);
    AnchorPane.setBottomAnchor(orderPostText, 50.0);

    centralPanel.getChildren().addAll(orderText, orderPreText, orderPostText);
    updateOrdersText();
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

  // -----------handlers for the method configureModeButton-----------
  private void handleAdd(int val) {
    if (root == null) { // first node (root)
      root = new Button(String.valueOf(val));
      addEfectoHover(root);
      root.getStyleClass().add("button");
      datoRaiz = new TreeNode(val);
      datoRaiz.setVisual(root);
      arbol.setRoot(datoRaiz);

      centralPanel.getChildren().add(root);
      root.setLayoutX(centralPanel.getWidth() / 2);
      root.setLayoutY(30);
      updateOrdersText();
      textField.clear();
    } else {

      Button newNode = new Button(String.valueOf(val));
      newNode.getStyleClass().add("button");
      addEfectoHover(newNode);
      calcularLugar(val, root, 1, datoRaiz, newNode);
      textField.clear();
      redrawTree();
      updateOrdersText();
    }
  }

  // angel lee esto!!!
  /* ¿Cómo funciona el javafx.Scene.Node? bueno es como la raíz, la base de todos los elementos visuales,
  tiene subclases como Control (para hacer botones, paneles, etc.), Parent (el que usas para el getChildren(),
   y Region. Imaginatelo como otro arbol que de ahi parten todos los métodos para crear lo visual*/
  private void handleDelete(int val) {
    boolean deleted = arbol.delete(val);
    if (!deleted) {
      Notification.show("WARNING", rootStack, "Node doesn't exists", 2000);
      return;
    }
    // search and remove the button from the centralPanel
    Button toRemove = null;
    /*lo que hace esto es que child es una variable que apunta a cada nodo (objeto visual que se crea)
    y por cada uno de esos nodos(objetos visuales como botones o paneles) va a verificar cuál es un boton que este dentro
    del panel central y si el boton tiene el mismo valor que el que querías eliminar, se elimina*/
    for (javafx.scene.Node child : centralPanel.getChildren()) { // iterate through children
      // instance of revisa en TIEMPO REAL, si el nodo por el que está pasando es un boton, y al
      // mismo tiempo se instancia
      if (child instanceof Button b
          && b.getText().equals(String.valueOf(val))) { // check if text matches
        toRemove = b; // set a button to remove

        break; // exit loop once found
      }
    }
    if (toRemove != null) centralPanel.getChildren().remove(toRemove); // remove from panel
    textField.clear();

    // --- synchronize the UI with the logical tree after delete ---
    TreeNode newRoot = arbol.getRoot(); // get current logical root
    datoRaiz = newRoot;
    if (newRoot != null) {
      if (newRoot.getVisual() == null) {
        Button b = new Button(String.valueOf(newRoot.getWeight()));
        b.getStyleClass().add("button");
        addEfectoHover(b);
        newRoot.setVisual(b);
      }
      root = newRoot.getVisual();
    } else {
      root = null;
    }
    redrawTree();
    updateOrdersText();
  }

  private void handleSearch(int val) {
    TreeNode foundTreeNode = arbol.search(val);
    if (foundTreeNode != null && foundTreeNode.getVisual() != null) {
      foundTreeNode
          .getVisual()
          .setStyle("-fx-border-color: red; -fx-border-width: 3px;  -fx-border-radius: 8px");
      PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(1));
      pause.setOnFinished(ev -> foundTreeNode.getVisual().setStyle(""));
      pause.play();
    } else {
      Notification.show("WARNING", rootStack, "Node not found", 2000);
    }
    textField.clear();
  }

  private Integer parseInput() {
    String txt = textField.getText();
    if (txt == null || txt.trim().isEmpty()) return null;
    try {
      return Integer.parseInt(txt.trim());
    } catch (NumberFormatException ex) {
      Notification.show("ERROR", rootStack, "Invalid Weight", 2000);
      textField.clear();
      return null;
    }
  }

  // ------------------------------------------------------------------
  private void configureSwitchMode() {
    switchModeButton.setFocusTraversable(false);

    addEfectoHover(switchModeButton);
    switchModeButton.setOnAction(
        Event -> {
          modeCount = (modeCount + 1) % 3;
          switch (modeCount) {
            case 0 -> modeButton.setGraphic(addView);
            case 1 -> modeButton.setGraphic(deleteView);
            case 2 -> modeButton.setGraphic(searchView);
            default -> Notification.show("ERROR", rootStack, "Invalid mode", 2000);
          }
        });
  }

  private void configureModeButton() {
    modeButton.setFocusTraversable(false);
    addEfectoHover(modeButton);
    modeButton.setOnAction(
        event -> {
          Integer val = parseInput();
          if (val == null) return;
          switch (modeCount) {
            case 0 -> handleAdd(val);
            case 1 -> handleDelete(val);
            case 2 -> handleSearch(val);
            default -> Notification.show("ERROR", rootStack, "Invalid mode", 2000);
          }
        });
  }

  private void createActionButtons() {
    configureSwitchMode();
    configureModeButton();
  }

  @Override
  public void start(Stage stage) throws Exception {
    arbol = new BinaryTree();
    // primero cargamos recursos
    createImages();
    panelPrincipal = new BorderPane();

    createCenterPanel();

    // barra inferior que usa las ImageViews ya cargadas
    createBottomBar();

    // botones y textos que dependen del centralPanel y las imagenes
    createClearButton();
    createCreditsText();

    // textos de recorridos
    createOrdersText();

    // configura listeners/acciones (usa los botones ya creados)
    createActionButtons();
    rootStack = new StackPane(scroll);
    panelPrincipal.setBottom(bottomBar);
    panelPrincipal.setCenter(rootStack);

    // show stage
    Scene escena = new Scene(panelPrincipal, 800, 600);
    String css =
        Objects.requireNonNull(this.getClass().getResource("/styles.css")).toExternalForm();
    escena.getStylesheets().add(css);
    stage.setTitle("Binary Tree Visualization");
    stage.setScene(escena);
    stage.show();

    // if the panel size changes, rearrange nodes === Esto es lambda ====
    // apenas le entiendo, pero básicamente cada vez que cambia un valor llama a redrawTree()
    // se les pone un _ al inicio de la variable para indicar que realmente no se utilizan
    centralPanel.widthProperty().addListener((_obs, _oldVal, _newVal) -> redrawTree());
    centralPanel.heightProperty().addListener((_obs, _oldVal, _newVal) -> redrawTree());

    escena.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            modeButton.fire();
          } else if (event.getCode() == KeyCode.ESCAPE) {
            Platform.exit();
          } else if (event.getCode() == KeyCode.TAB) {
            switchModeButton.fire();
          }
        });

    redrawTree();
    updateOrdersText();
  }

  // reposition all nodes and redraw edges
  public void redrawTree() {
    if (datoRaiz == null || datoRaiz.getVisual() == null) {
      edgesLayer.getChildren().clear(); // clear edges if no nodes
      return;
    }
    // Ensure the root Button is present in the panel (after edgesLayer and orderText)
    if (!centralPanel.getChildren().contains(datoRaiz.getVisual())) {
      centralPanel.getChildren().add(datoRaiz.getVisual());
    }
    // Position the root at the center
    double centerX = getViewportWidth() / 2;
    datoRaiz.getVisual().setLayoutX(centerX);
    datoRaiz.getVisual().setLayoutY(30);
    // Reposition children recursively
    positionChildrenRecursively(datoRaiz, 1);
    // after layout updated, redraw edges using real bounds
    Platform.runLater(this::redrawEdges);
  }

  // helper to position children recursively
  private void positionChildrenRecursively(TreeNode nodoLogicoPadre, int nivelActual) {
    if (nodoLogicoPadre == null || nodoLogicoPadre.getVisual() == null) return;

    Button padreVisual = nodoLogicoPadre.getVisual();
    double available = getViewportWidth();
    double espacio = (available / 2) / Math.pow(2, nivelActual);

    if (nodoLogicoPadre.getLeft() != null) {
      TreeNode leftTreeNode = nodoLogicoPadre.getLeft();
      // ensure the Button exists in the panel
      if (leftTreeNode.getVisual() != null
          && !centralPanel.getChildren().contains(leftTreeNode.getVisual())) {
        centralPanel.getChildren().add(leftTreeNode.getVisual());
      }
      if (leftTreeNode.getVisual() != null) {
        leftTreeNode.getVisual().setLayoutY(padreVisual.getLayoutY() + 70);
        leftTreeNode.getVisual().setLayoutX(padreVisual.getLayoutX() - espacio);
      }
      positionChildrenRecursively(leftTreeNode, nivelActual + 1);
    }

    if (nodoLogicoPadre.getRight() != null) {
      TreeNode rightTreeNode = nodoLogicoPadre.getRight();
      if (rightTreeNode.getVisual() != null
          && !centralPanel.getChildren().contains(rightTreeNode.getVisual())) {
        centralPanel.getChildren().add(rightTreeNode.getVisual());
      }
      if (rightTreeNode.getVisual() != null) {
        rightTreeNode.getVisual().setLayoutY(padreVisual.getLayoutY() + 70);
        rightTreeNode.getVisual().setLayoutX(padreVisual.getLayoutX() + espacio);
      }
      positionChildrenRecursively(rightTreeNode, nivelActual + 1);
    }
  }

  // draw edges between parent and child nodes
  private void redrawEdges() {
    edgesLayer.getChildren().clear();
    if (datoRaiz == null) return;
    drawEdgesRecursively(datoRaiz);
  }

  // helper to draw edges recursively
  private void drawEdgesRecursively(TreeNode nodoLogico) {
    if (nodoLogico == null || nodoLogico.getVisual() == null) return;
    Button parentBtn = nodoLogico.getVisual();

    // left child
    if (nodoLogico.getLeft() != null && nodoLogico.getLeft().getVisual() != null) {
      Button childBtn = nodoLogico.getLeft().getVisual();
      Line line = createLineBetween(parentBtn, childBtn);
      edgesLayer.getChildren().add(line);
      drawEdgesRecursively(nodoLogico.getLeft());
    }

    // right child
    if (nodoLogico.getRight() != null && nodoLogico.getRight().getVisual() != null) {
      Button childBtn = nodoLogico.getRight().getVisual();
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
      int weight, Button padreVisual, int nivelActual, TreeNode nodoLogicoPadre, Button newButton) {
    int datoPadre = (Integer.parseInt(padreVisual.getText()));
    double available = getViewportWidth();
    double espacio = (available / 2) / Math.pow(2, nivelActual);

    if (weight < datoPadre) {
      if (nodoLogicoPadre.getLeft() == null) {
        centralPanel.getChildren().add(newButton);
        newButton.setLayoutY(padreVisual.getLayoutY() + 70);
        newButton.setLayoutX(padreVisual.getLayoutX() - espacio);
        TreeNode nuevoHijo = new TreeNode(weight);
        nuevoHijo.setVisual(newButton);
        nodoLogicoPadre.setLeft(nuevoHijo);
      } else {

        calcularLugar(
            weight,
            nodoLogicoPadre.getLeft().getVisual(),
            nivelActual + 1,
            nodoLogicoPadre.getLeft(),
            newButton);
      }
    } else if (weight > datoPadre) {
      if (nodoLogicoPadre.getRight() == null) {
        centralPanel.getChildren().add(newButton);
        newButton.setLayoutY(padreVisual.getLayoutY() + 70);
        newButton.setLayoutX(padreVisual.getLayoutX() + espacio);
        TreeNode nuevoHijo = new TreeNode(weight);
        nuevoHijo.setVisual(newButton);
        nodoLogicoPadre.setRight(nuevoHijo);
      } else {

        calcularLugar(
            weight,
            nodoLogicoPadre.getRight().getVisual(),
            nivelActual + 1,
            nodoLogicoPadre.getRight(),
            newButton);
      }
    } else if (weight == datoPadre) {
      Notification.show("WARNING", rootStack, "Node already exists", 2000);
    } else {
      Notification.show("ERROR", rootStack, "Invalid weight", 2000);
    }
  }

  // get the current viewport width, fallback to centralPanel width
  private double getViewportWidth() {
    double vpWidth = scroll.getViewportBounds().getWidth();
    return vpWidth > 0 ? vpWidth : centralPanel.getWidth();
  }

  public void addEfectoHover(Button b) {
    b.setOnMouseEntered(
        e -> {
          b.getStyleClass().add("button-highlighted"); // cambio a el uluminado
        });
    b.setOnMouseExited(
        e -> {
          b.getStyleClass().remove("button-highlighted"); // volmevos a el boton normal
        });
  }
}
