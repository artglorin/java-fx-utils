package com.artglorin.javaFxUtil;

import javafx.application.Platform;

/**
 * Created by V.Verminsky on 23.03.2017.
 */
public class JfxCommon {
    private JfxCommon(){}

    public static void runInFxThread(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

}
