package com.hectora15.binarytree;

import com.hectora15.binarytree.controller.TreeViewController;
import com.hectora15.binarytree.mapper.NodeButtonMapper;
import com.hectora15.binarytree.renderer.TreeRenderer;
import com.hectora15.binarytree.service.NotificationService;
import com.hectora15.binarytree.service.TreeService;
import com.hectora15.binarytree.view.TreeView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Objects;

import static com.hectora15.binarytree.view.TreeViewConstants.*;

/**
 * Clase principal - solo inicialización y conexión de componentes
 */
public class UI extends Application {

  @Override
  public void start(Stage stage) {
    // Create core components
    TreeView view = new TreeView();
    TreeService treeService = new TreeService();
    NodeButtonMapper mapper = new NodeButtonMapper();
    NotificationService notifier = new NotificationService(view.getRootStack());
    TreeRenderer renderer = new TreeRenderer(view, mapper);

    // Create controller (connects everything)
    TreeViewController controller = new TreeViewController(
            view, treeService, renderer, notifier, mapper
    );

    // Create scene
    Scene scene = new Scene(view.getMainPanel(), INITIAL_WIDTH, INITIAL_HEIGHT);
    String css = Objects.requireNonNull(
            getClass().getResource("/styles.css")
    ).toExternalForm();
    scene.getStylesheets().add(css);

    // Setup keyboard shortcuts
    scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.ENTER) {
        view.getModeButton().fire();
      } else if (event.getCode() == KeyCode.ESCAPE) {
        Platform.exit();
      } else if (event.getCode() == KeyCode.TAB) {
        view.getSwitchModeButton().fire();
        event.consume();
      }
    });

    // Setup dynamic redraw on resize
    view.getCentralPanel().widthProperty().addListener(
            (obs, oldVal, newVal) -> renderer.render(treeService.getTree())
    );
    view.getCentralPanel().heightProperty().addListener(
            (obs, oldVal, newVal) -> renderer.render(treeService.getTree())
    );

    // Show stage
    stage.setTitle("Binary Tree Visualization");
    stage.setScene(scene);
    stage.show();

    // Initial render
    renderer.render(treeService.getTree());
    controller.updateOrderTexts();
  }

  public static void main(String[] args) {
    launch(args);
  }
}