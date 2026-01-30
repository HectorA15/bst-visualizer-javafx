package com.hectora15.binarytree.util;

import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class Notification {
  public static void show(String type, StackPane root, String message, int durationMillis) {
    Platform.runLater(
        () -> {
          double fixedH = 50;

          HBox box = new HBox();
          box.setAlignment(Pos.BOTTOM_RIGHT);
          box.setPadding(new Insets(8));

          String typeClass;
          if (type.equals("ERROR")) {
            typeClass = "notif-error";
          } else if (type.equals("SUCCESS")) {
            typeClass = "notif-success";
          } else if (type.equals("INFO")) {
            typeClass = "notif-info";
          } else if (type.equals("WARNING")) {
            typeClass = "notif-warning";
          } else {
            typeClass = "notif-default";
          }
          box.getStyleClass().addAll("notification-popup", typeClass);

          Text text = new Text(message);
          text.setTextAlignment(TextAlignment.CENTER);
          text.setFill(Color.WHITE);
          // Wrap text relative to the viewport width
          text.wrappingWidthProperty().bind(root.widthProperty().multiply(0.2));
          // Limit notification width and height to 50% of the viewport
          box.maxWidthProperty().bind(root.widthProperty().multiply(0.15));
          box.setMaxHeight(fixedH);
          box.setMinHeight(fixedH);
          box.setPrefHeight(fixedH);

          box.getChildren().add(text);

          StackPane.setAlignment(box, Pos.BOTTOM_RIGHT);
          StackPane.setMargin(box, new Insets(12));

          box.setOpacity(0);
          root.getChildren().add(box);

          FadeTransition fadeIn = new FadeTransition(Duration.millis(200), box);
          fadeIn.setFromValue(0);
          fadeIn.setToValue(1);

          PauseTransition pause = new PauseTransition(Duration.millis(durationMillis));

          FadeTransition fadeOut = new FadeTransition(Duration.millis(300), box);
          fadeOut.setFromValue(1);
          fadeOut.setToValue(0);

          SequentialTransition seq = new SequentialTransition(fadeIn, pause, fadeOut);
          seq.setOnFinished(evt -> root.getChildren().remove(box));
          seq.play();
        });
  }
}
