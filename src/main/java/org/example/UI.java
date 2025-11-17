package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UI extends Application {
    int datoNuevo;
    Node datoIntRaiz;
    Button raiz;
    int x, y;
    Button botonAgregar;
    TextField miTexto;
    BorderPane miPanel;
    Button agregar;
    Button nodo;
    HBox horizontal;
    String texto;
    Pane lienzo;

    @Override
    public void start(Stage prueba) {
        horizontal = new HBox();
        lienzo = new Pane();
        botonAgregar = new Button("Agregar Nodo");
        agregar = new Button("agregar");
        miTexto = new TextField();
        miPanel = new BorderPane();

        miPanel.setCenter(lienzo);
        miPanel.setLeft(botonAgregar);


        botonAgregar.setOnAction(Event -> {
            if (!horizontal.getChildren().contains(miTexto)) {
                horizontal.getChildren().addAll(miTexto, agregar);
                miPanel.setBottom(horizontal);
            }
        });
        lienzo.widthProperty().addListener((propiedad, oldvalor, newValor) -> { // obtiene la anchura y altura del layaout del medio(linzo)cuando cambia
            y = (int) (lienzo.getHeight() / 2);
            x = (int) (newValor.doubleValue() / 2);
        });

        agregar.setOnAction(Event -> {
            texto = miTexto.getText();
            horizontal.getChildren().remove(miTexto);
            miTexto.clear();
            agregarNodos(texto);
            horizontal.getChildren().remove(agregar);
        });

        Scene escena = new Scene(miPanel, 500, 400);
        prueba.setTitle("Binary Tree");
        prueba.setScene(escena);
        prueba.show();

    }

    public void agregarNodos(String Valor) {
        if (raiz == null) {
            raiz = new Button(Valor);
            datoIntRaiz = new Node(Integer.parseInt(Valor));
            datoIntRaiz.visual = raiz;
            lienzo.getChildren().add(raiz);
            raiz.setLayoutX(x);
            raiz.setLayoutY(y);
        } else {
            nodo = new Button(Valor);
            datoNuevo = Integer.parseInt(Valor);
            calculoEspacio(datoNuevo, raiz, 1, datoIntRaiz);
            nodo.setStyle("-fx-background-radius: 50%;");

        }
    }

    public void calculoEspacio(int peso, Button padre, int nivelActual, Node nodoEspia) {
        int datoPadre = Integer.parseInt(padre.getText());
        if (peso < datoPadre) {
            if (nodoEspia.getLeft() == null) {
                lienzo.getChildren().add(nodo);
                double Xlimitacion = (x) / Math.pow(2, nivelActual);
                nodo.setLayoutY(padre.getLayoutY() + 80);
                nodo.setLayoutX(padre.getLayoutX() - Xlimitacion);// despues del nivel 4 se juntan los botones no pude arreglarlo
                Node nodeHijo = new Node(peso);
                nodeHijo.visual = nodo;
                nodoEspia.setLeft(nodeHijo);
            } else {
                calculoEspacio(peso, nodoEspia.getLeft().visual, nivelActual + 1, nodoEspia.getLeft());
            }
        } else if (peso > datoPadre) {
            if (nodoEspia.getRight() == null) {
                lienzo.getChildren().add(nodo);
                double Xlimitacion = (x) / Math.pow(2, nivelActual); // despues del nivel 4 se juntan los botones no pude arreglarlo lol
                nodo.setLayoutY(padre.getLayoutY() + 80);
                nodo.setLayoutX(padre.getLayoutX() + Xlimitacion);
                Node nodeHijo = new Node(peso);
                nodeHijo.visual = nodo;
                nodoEspia.setRight(nodeHijo);
            } else {
                calculoEspacio(peso, nodoEspia.getRight().visual, nivelActual + 1, nodoEspia.getRight());
            }
        }// subeme el sueldo we <-- ganatelo
    }
}