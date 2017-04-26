package com.artglorin.javaFxUtil;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by V.Verminsky on 23.03.2017.
 */
@SuppressWarnings("WeakerAccess")
public class JfxLayoutUtil {

    private static final Logger LOGGER = getLogger(JfxLayoutUtil.class);
    private static Callback<Class<?>, Object> controllerFactory;

    private static Optional<URL> getResource(String name) {
        final URL resource = JfxLayoutUtil.class.getClassLoader().getResource(name + ".fxml");
        if (resource == null) {
            LOGGER.error("cannot load fxml file: {}.fxml\nException: {}", name);
        }
        return Optional.ofNullable(resource);
    }

    private static FXMLLoader newLoader() {
        final FXMLLoader loader = new FXMLLoader();
        if (controllerFactory != null) {
            loader.setControllerFactory(controllerFactory);
        }
        return loader;
    }

    public static void setControllerFactory(Callback<Class<?>, Object> controllerFactory) {
        JfxLayoutUtil.controllerFactory = controllerFactory;
    }

    public static <T> void setUpController(String fxml, Consumer<T> consumer) {
        setUpControllerAndView(fxml, (BiConsumer<T, ? extends Object>) (controller, view) -> consumer.accept(controller));
    }

    public static <C, V> void setUpControllerAndView(String fxml, BiConsumer<C, V> consumer) {
        getResource(fxml).ifPresent(url -> {
            final FXMLLoader loader = newLoader();
            try (InputStream inputStream = url.openStream()) {
                V view = loader.load(inputStream);
                consumer.accept(loader.getController(), view);
            } catch (IOException e) {
                LOGGER.error("cannot load fxml file: {}.fxml\nException: {}", fxml, e);
            }
        });
    }

    public  static <T> Optional<T> createNewController(String fxml, Class<T> tClass) {
        return getResource(fxml).map(url -> {
            final FXMLLoader loader = newLoader();
            try (InputStream inputStream = url.openStream()){
                loader.load(inputStream);
                return loader.getController();
            } catch (IOException e) {
                LOGGER.error("Cannot load fxml file: {}.fxml\nException: {}", fxml, e);
            }
            return null;
        });
    }

    public static <T> void setUpView(String fxml, Consumer<T> action) {
        setUpControllerAndView(fxml, (BiConsumer<? extends Object, T>) (controller, view) -> action.accept(view));
    }

    private JfxLayoutUtil() {
    }

}
