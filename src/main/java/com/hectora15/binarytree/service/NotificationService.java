package com.hectora15.binarytree.service;

import com.hectora15.binarytree.util.Notification;
import javafx.scene.layout.StackPane;

import static com.hectora15.binarytree.view.TreeViewConstants.*;

/**
 * Servicio para mostrar notificaciones
 */
public class NotificationService {
    private final StackPane container;

    public NotificationService(StackPane container) {
        this.container = container;
    }

    public void showSuccess(String message) {
        Notification.show("SUCCESS", container, message, NOTIFICATION_MEDIUM);
    }

    public void showWarning(String message) {
        Notification.show("WARNING", container, message, NOTIFICATION_MEDIUM);
    }

    public void showError(String message) {
        Notification.show("ERROR", container, message, NOTIFICATION_MEDIUM);
    }

    public void showInfo(String message) {
        Notification.show("INFO", container, message, NOTIFICATION_SHORT);
    }
}