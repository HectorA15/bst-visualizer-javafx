package org.example;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

  Boolean eliminar = false;

  Node datoRaiz;
  int datoNuevo;
  int click;
  String text;

  Image addImage, deleteImage;
  ImageView addView, deleteView;

  @Override
  public void start(@NotNull Stage stage) throws Exception {
    addImage = new Image(this.getClass().getResourceAsStream("/images/add.png"));
    deleteImage = new Image(this.getClass().getResourceAsStream("/images/delete.png"));
    addView = new ImageView(addImage);
    deleteView = new ImageView(deleteImage);
    // ajustar tamaño del icono
    addView.setFitWidth(18);
    addView.setFitHeight(18);
    addView.setPreserveRatio(true);
    deleteView.setFitWidth(18);
    deleteView.setFitHeight(18);
    deleteView.setPreserveRatio(true);

    BinaryTree arbol = new BinaryTree();

    panelPrincipal = new BorderPane();
    // ------------- center -------------
    centralPanel = new AnchorPane();
    centralPanel.getStyleClass().add("central-panel");
    centralPanel.setFocusTraversable(false);
    centralPanel.setMaxWidth(Double.MAX_VALUE);
    centralPanel.setMaxHeight(Double.MAX_VALUE);
    // order text
    Text orderText = new Text();
    orderText.getStyleClass().add("typed-text");
    orderText.setFill(Color.WHITE);
    AnchorPane.setLeftAnchor(orderText, 10.0);
    AnchorPane.setBottomAnchor(orderText, 10.0);
    centralPanel.getChildren().add(orderText);
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
    recorridos = new Button("InOrder");
    cambioRec = new Button("Recorrido");
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
        .addAll(
            switchMode,
            mode,
            textField,
            recorridos,
            cambioRec); // add buttons and text field to bottom bar

    // Initialize tree
    arbol.inOrder();
    arbol.preOrder();
    arbol.postOrder();

    // layout
    panelPrincipal.setBottom(bottomBar);
    panelPrincipal.setCenter(scroll);

    switchMode.setOnAction(
        Event -> {
          if (eliminar == true) {
            mode.setGraphic(addView);
            eliminar = false;
          } else {
            mode.setGraphic(deleteView);
            eliminar = true;
          }
        });

    cambioRec.setOnAction(
        Event -> {
          click++;
          switch (click) {
            case 1:
              text = arbol.inOrder();
              orderText.setText("Inorder:\t\t"+text);

              break;
            case 2:
              recorridos.setText("Recorrido PostOrder");
              String textoPos = arbol.postOrder();
              orderText.setText("PostOrder:\t"+textoPos);
              break;
            case 3: // falla
              recorridos.setText("Recorrido PreOrder");
              String textoPre = arbol.preOrder();
              orderText.setText("PreOrder:\t\t"+textoPre);
              arbol.preOrder();
              click = 0;
              break;
          }
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
                          textField.clear();
                          raiz.setLayoutX(centralPanel.getWidth() / 2);
                          raiz.setLayoutY(30);
                      } else {
                          nodo = new Button(String.valueOf(val));
                          datoNuevo = val;
                          textField.clear();
                          calcularLugar(datoNuevo, raiz, 1, datoRaiz);
                          nodo.setStyle("-fx-background-radius: 50%;");
                      }
                  } else {
                      // modo eliminar: implementar según BinaryTree si se necesita
                  }
              });



    Scene escena = new Scene(panelPrincipal, 800, 600);
    String css = this.getClass().getResource("/styles.css").toExternalForm();
    escena.getStylesheets().add(css);
    stage.setTitle("prueba");
    stage.setScene(escena);
    stage.show();

      escena.setOnKeyPressed(event -> {
          if (event.getCode() == KeyCode.ENTER) {
              mode.fire();
          } else if (event.getCode() == KeyCode.ESCAPE) {
              Platform.exit();
          }
      });
  }

  public void calcularLugar(int peso, Button padreVisual, int nivelActual, Node nodoLogicoPadre) {
    int DatoPadre = (Integer.parseInt(padreVisual.getText()));
    double Espacio = (centralPanel.getWidth() / 2) / Math.pow(2, nivelActual);
    if (peso < DatoPadre) {
      if (nodoLogicoPadre.getLeft() == null) {
        centralPanel.getChildren().add(nodo);
        nodo.setLayoutY(padreVisual.getLayoutY() + 70);
        nodo.setLayoutX(padreVisual.getLayoutX() - Espacio);
        Node nuevoHijo = new Node(peso);
        nuevoHijo.visual = nodo;
        nodoLogicoPadre.setLeft(nuevoHijo);
      } else {
        calcularLugar(
            peso, nodoLogicoPadre.getLeft().visual, nivelActual + 1, nodoLogicoPadre.getLeft());
      }
    }
    if (peso > DatoPadre) {
      if (nodoLogicoPadre.getRight() == null) {
        centralPanel.getChildren().add(nodo);
        nodo.setLayoutY(padreVisual.getLayoutY() + 70);
        nodo.setLayoutX(padreVisual.getLayoutX() + Espacio);
        Node nuevoHijo = new Node(peso);
        nuevoHijo.visual = nodo;
        nodoLogicoPadre.setRight(nuevoHijo);
      } else {
        calcularLugar(
            peso, nodoLogicoPadre.getRight().visual, nivelActual + 1, nodoLogicoPadre.getRight());
      }
    }
  }
}
