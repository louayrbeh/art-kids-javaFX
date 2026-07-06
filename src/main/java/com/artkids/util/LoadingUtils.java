package com.artkids.util;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;

import java.util.function.Supplier;

public final class LoadingUtils {
    private LoadingUtils() {
    }

    public static <T> T runWithGlobalLoading(Supplier<T> supplier) {
        if (!Platform.isFxApplicationThread()) {
            return supplier.get();
        }

        Scene scene = Window.getWindows().stream()
                .filter(Window::isFocused)
                .findFirst()
                .map(Window::getScene)
                .orElse(null);

        if (scene == null) {
            return supplier.get();
        }

        Parent root = scene.getRoot();
        Cursor previousCursor = scene.getCursor();
        boolean previousDisabled = root.isDisable();
        try {
            scene.setCursor(Cursor.WAIT);
            root.setDisable(true);
            return supplier.get();
        } finally {
            root.setDisable(previousDisabled);
            scene.setCursor(previousCursor);
        }
    }
}
