package com.hectora15.binarytree.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class IconFactory {

    public enum IconType {
        ADD("/images/add.png"),
        DELETE("/images/delete.png"),
        SEARCH("/images/search.png"),
        CLEAR("/images/clear.png");

        private final String path;

        IconType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static ImageView createIcon(IconType type, double size) {
        Image image = new Image(
                Objects.requireNonNull(
                        IconFactory.class.getResourceAsStream(type.getPath())
                )
        );
        ImageView view = new ImageView(image);
        view.setFitWidth(size);
        view.setFitHeight(size);
        view.setPreserveRatio(true);
        return view;
    }
}
