package org.example;

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
import org.jetbrains.annotations.NotNull;

public class UI extends Application {
  Button switchMode;
  Button mode;
  Button recorridos;
  Button raiz;
  Button nodo;
  Button cambioRec;

  TextField textField;
  ScrollPane scroll;

  BorderPane panelPrincipal;
  HBox bottomBar;
  Pane centralPanel;

  // capa para dibujar las líneas (debe estar detrás de los botones)
  Pane edgesLayer;

  Boolean eliminar = false;

  Node datoRaiz;
  int datoNuevo;
  int click;
  String text;

  Image addImage, deleteImage;
  ImageView addView, deleteView;

  // <-- keep a reference to the tree for general use
  BinaryTree arbol;

  @Override
  public void start(@NotNull Stage stage) throws Exception {
    addImage = new Image(this.getClass().getResourceAsStream("/images/add.png"));
     searchImage=new Image(this.getClass().getResourceAsStream("/images/search.jpg"));
    deleteImage = new Image(this.getClass().getResourceAsStream("/images/delete.png"));
    addView = new ImageView(addImage);
    deleteView = new ImageView(deleteImage);
    searchView= new ImageView(searchImage);
    // adjust icon size
     searchView.setFitWidth(18);
      searchView.setFitHeight(18);
      searchView.setPreserveRatio(true);
      searchView.setBlendMode(BlendMode.SCREEN);// quitar el fondo de la imagen 
    addView.setFitWidth(18);
    addView.setFitHeight(18);
    addView.setPreserveRatio(true);
    deleteView.setFitWidth(18);
    deleteView.setFitHeight(18);
    deleteView.setPreserveRatio(true);

    // move tree to a class field
    arbol = new BinaryTree();

    panelPrincipal = new BorderPane();
    // ------------- center -------------
    centralPanel = new AnchorPane();
    centralPanel.getStyleClass().add("central-panel");
    centralPanel.setFocusTraversable(false);
    centralPanel.setMaxWidth(Double.MAX_VALUE);
    centralPanel.setMaxHeight(Double.MAX_VALUE);

    // create a dedicated layer for edges (lines) and add it as the bottom-most child
    edgesLayer = new Pane();
    edgesLayer.setPickOnBounds(false); // don't intercept mouse events
    edgesLayer.setMouseTransparent(true);

    // add edgesLayer first so it's behind other children
    centralPanel.getChildren().add(edgesLayer);

    // order text
    Text orderText = new Text();
    orderText.getStyleClass().add("typed-text");
    orderText.setFill(Color.DIMGRAY);
    AnchorPane.setLeftAnchor(orderText, 10.0);
    AnchorPane.setBottomAnchor(orderText, 10.0);
    centralPanel.getChildren().add(orderText);
    // preOrder text
    Text orderPreText = new Text();
    orderPreText.getStyleClass().add("typed-text");
    orderPreText.setFill(Color.DIMGRAY);
    AnchorPane.setLeftAnchor(orderPreText, 10.0);
    AnchorPane.setBottomAnchor(orderPreText, 30.0);
    centralPanel.getChildren().add(orderPreText);

    // postOrder text
    Text orderPostText = new Text();
    orderPostText.getStyleClass().add("typed-text");
    orderPostText.setFill(Color.DIMGRAY);
    AnchorPane.setLeftAnchor(orderPostText, 10.0);
    AnchorPane.setBottomAnchor(orderPostText, 50.0);
    centralPanel.getChildren().add(orderPostText);

    // initialize orders text
    String textOrder = arbol.preOrder();
    orderText.setText("PreOrder:\t\t" + textOrder);

    String textPre = arbol.inOrder();
    orderPreText.setText("Inorder:\t\t" + textPre);

    String textPost = arbol.postOrder();
    orderPostText.setText("PostOrder:\t" + textPost);
    // scroll pane
    scroll = new ScrollPane(centralPanel);
    scroll.setFocusTraversable(false);
    scroll.getStyleClass().add("scroll-pane");
    scroll.setFitToWidth(true);
    scroll.setFitToHeight(true);

    // ------------- bottom -------------
    bottomBar = new HBox();
    bottomBar.getStyleClass().add("bottom-bar");
    bottomBar.setMinHeight(60);
    bottomBar.setSpacing(10);
    bottomBar.setPadding(new Insets(10, 10, 10, 10));
    bottomBar.setAlignment(Pos.CENTER);

    // initialize buttons and text field
    switchMode = new Button("Switch Mode");
    cambioRec = new Button("Switch Order");
    mode = new Button();
    mode.setGraphic(addView);
    textField = new TextField();
    textField.getStyleClass().add("text-box");
    // make text field expand to fill available space
    textField.setMaxWidth(Double.MAX_VALUE);
    textField.setMaxHeight(Double.MAX_VALUE);
    HBox.setHgrow(textField, Priority.ALWAYS);

    bottomBar
        .getChildren()
        .addAll(switchMode, mode, textField); // add buttons and text field to bottom bar

    // Initialize tree (no nodes yet)
    arbol.inOrder();
    arbol.preOrder();
    arbol.postOrder();

    // layout
    panelPrincipal.setBottom(bottomBar);
    panelPrincipal.setCenter(scroll);

 switchMode.setOnAction(
        Event -> {
            click++;
            switch (click) {
                case 1:
                    mode.setGraphic(addView);
                    break;
                case 2:
                    mode.setGraphic(deleteView);
                    break;
                case 3:
                    mode.setGraphic(searchView);
                    click=0;
                    break;
            }
          //if (eliminar == true) {
            //mode.setGraphic(addView);
            // eliminar = false;
            // } else {
            //  mode.setGraphic(deleteView);
            // eliminar = true;
            // }
        });

    mode.setOnAction(
        event -> {
          if (!eliminar) {
            String txt = textField.getText();
            if (txt == null || txt.trim().isEmpty()) return;
            int val;
            try {
              val = Integer.parseInt(txt.trim());
            } catch (NumberFormatException ex) {
              return;
            }

            if (raiz == null) {
              raiz = new Button(String.valueOf(val));
              datoRaiz = new Node(val);
              datoRaiz.visual = raiz;
              arbol.setRoot(datoRaiz);

              centralPanel.getChildren().add(raiz);
              updateOrdersText(orderText, orderPreText, orderPostText);
              textField.clear();
              raiz.setLayoutX(centralPanel.getWidth() / 2);
              raiz.setLayoutY(30);
            } else {
              nodo = new Button(String.valueOf(val));
              datoNuevo = val;
              textField.clear();
              calcularLugar(datoNuevo, raiz, 1, datoRaiz);
              nodo.setStyle("-fx-background-radius: 50%;");
              // after inserting, recalculate positions so everything stays aligned
              redrawTree();
              updateOrdersText(orderText, orderPreText, orderPostText);
            }
          } else {
            String txt = textField.getText();
            if (txt == null || txt.trim().isEmpty()) return;
            int val;
            try {
              val = Integer.parseInt(txt.trim());
            } catch (NumberFormatException ex) {
              return;
            }

            boolean deleted = arbol.delete(val);
            if (deleted) {
              // search and remove the button from centralPanel
              Button toRemove = null; // button to remove
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
              if (toRemove != null) { // if button found
                centralPanel.getChildren().remove(toRemove); // remove from panel
              }
              textField.clear();

              // --- synchronize the UI with the logical tree after delete ---
              Node newRoot = arbol.getRoot(); // get current logical root
              datoRaiz = newRoot;
              if (newRoot != null) {
                // if logical node has no visual Button, create one
                if (newRoot.visual == null) {
                  Button b = new Button(String.valueOf(newRoot.getWeight()));
                  newRoot.visual = b;
                }
                raiz = newRoot.visual;
              } else {
                raiz = null;
              }
              // after deleting, redraw to reposition remaining nodes and edges
              redrawTree();
              updateOrdersText(orderText, orderPreText, orderPostText);
            }
          }
        });

    Scene escena = new Scene(panelPrincipal, 800, 600);
    String css = this.getClass().getResource("/styles.css").toExternalForm();
    escena.getStylesheets().add(css);
    stage.setTitle("prueba");
    stage.setScene(escena);
    stage.show();

    // if the panel size changes, rearrange nodes
    centralPanel.widthProperty().addListener((obs, oldVal, newVal) -> redrawTree());
    centralPanel.heightProperty().addListener((obs, oldVal, newVal) -> redrawTree());

    // allow Enter/Escape/Shift shortcuts
    escena.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            mode.fire();
          } else if (event.getCode() == KeyCode.ESCAPE) {
            Platform.exit();
          } else if (event.getCode() == KeyCode.SHIFT) {
            switchMode.fire();
          }
        });

    // draw initially (in case there were already nodes)
    redrawTree();
    updateOrdersText(orderText, orderPreText, orderPostText);
  }

  // reposition all nodes and redraw edges
  public void redrawTree() {

    if (datoRaiz == null || datoRaiz.visual == null) {
      // clear edges if no nodes
      edgesLayer.getChildren().clear();
      return;
    }

    // Ensure the root Button is present in the panel (after edgesLayer and orderText)
    if (!centralPanel.getChildren().contains(datoRaiz.visual)) {
      centralPanel.getChildren().add(datoRaiz.visual);
    }

    // Position the root at the center
    double centerX = centralPanel.getWidth() / 2;
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
    double Espacio = (centralPanel.getWidth() / 2) / Math.pow(2, nivelActual);

    if (nodoLogicoPadre.getLeft() != null) {
      Node leftNode = nodoLogicoPadre.getLeft();
      // ensure the Button exists in the panel
      if (leftNode.visual != null && !centralPanel.getChildren().contains(leftNode.visual)) {
        centralPanel.getChildren().add(leftNode.visual);
      }
      if (leftNode.visual != null) {
        leftNode.visual.setLayoutY(padreVisual.getLayoutY() + 70);
        leftNode.visual.setLayoutX(padreVisual.getLayoutX() - Espacio);
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
        rightNode.visual.setLayoutX(padreVisual.getLayoutX() + Espacio);
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

  public void updateOrdersText(Text orderText, Text orderPreText, Text orderPostText) {
    String textOrder = arbol.preOrder();
    orderText.setText("PreOrder:\t\t" + textOrder);

    String textPre = arbol.inOrder();
    orderPreText.setText("Inorder:\t\t" + textPre);

    String textPost = arbol.postOrder();
    orderPostText.setText("PostOrder:\t" + textPost);
  }

  // calcula el lugar del nuevo nodo y lo inserta en el panel central
  public void calcularLugar(int weight, Button padreVisual, int nivelActual, Node nodoLogicoPadre) {
    int datoPadre = (Integer.parseInt(padreVisual.getText()));
    double espacio = (centralPanel.getWidth() / 2) / Math.pow(2, nivelActual);
    if (weight < datoPadre) {
      if (nodoLogicoPadre.getLeft() == null) {
        centralPanel.getChildren().add(nodo);
        nodo.setLayoutY(padreVisual.getLayoutY() + 70);
        nodo.setLayoutX(padreVisual.getLayoutX() - espacio);
        Node nuevoHijo = new Node(weight);
        nuevoHijo.visual = nodo;
        nodoLogicoPadre.setLeft(nuevoHijo);
      } else {
        calcularLugar(
            weight, nodoLogicoPadre.getLeft().visual, nivelActual + 1, nodoLogicoPadre.getLeft());
      }
    }
    if (weight > datoPadre) {
      if (nodoLogicoPadre.getRight() == null) {
        centralPanel.getChildren().add(nodo);
        nodo.setLayoutY(padreVisual.getLayoutY() + 70);
        nodo.setLayoutX(padreVisual.getLayoutX() + espacio);
        Node nuevoHijo = new Node(weight);
        nuevoHijo.visual = nodo;
        nodoLogicoPadre.setRight(nuevoHijo);
      } else {
        calcularLugar(
            weight, nodoLogicoPadre.getRight().visual, nivelActual + 1, nodoLogicoPadre.getRight());
      }
    }
  }
}
