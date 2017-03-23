package com.artglorin.javaFxUtil;

import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by V.Verminsky on 23.03.2017.
 */
public class JfxLayoutUtil {

    private static final Logger LOGGER = getLogger(JfxLayoutUtil.class);

    private JfxLayoutUtil(){}

    private static URL getResource(String name) {
        return JfxLayoutUtil.class.getClassLoader().getResource(name);
    }

    public static <T> void setUpView(String fxml, Consumer<T> action) {
        final URL resource = getResource(fxml + ".fxml");
        if (resource != null) {
            try {
                action.accept(FXMLLoader.load(resource));
            } catch (IOException e) {
                LOGGER.error("cannot load fxml file: {}.fxml\nException: {}", fxml,  e);
            }
        }
    }

    public static <T> void setUpController(String fxml, Consumer<T> consumer) {
        final URL resource = getResource(fxml + ".fxml");
        if (resource != null) {
            final FXMLLoader loader = new FXMLLoader();
            try {
                loader.load(resource.openStream());
                consumer.accept(loader.getController());
            } catch (IOException e) {
                LOGGER.error("cannot load fxml file: {}.fxml\nException: {}", fxml,  e);
            }
        }
    }

    public static <C,V> void setUpControllerAndView(String fxml, BiConsumer<C,V> consumer) {
        final URL resource = getResource(fxml + ".fxml");
        if (resource != null) {
            final FXMLLoader loader = new FXMLLoader();
            try {
                V view = loader.load(resource.openStream());
                consumer.accept(loader.getController(), view);
            } catch (IOException e) {
                LOGGER.error("cannot load fxml file: {}.fxml\nException: {}", fxml,  e);
            }
        }
    }

}
