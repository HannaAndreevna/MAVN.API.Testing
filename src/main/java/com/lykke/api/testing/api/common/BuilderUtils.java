package com.lykke.api.testing.api.common;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BuilderUtils {

    public static <E> E getObjectWithData(E objectToUpdate, Consumer<E>... actions) {
        if (null == actions || 0 == actions.length) {
            return objectToUpdate;
        }
        Stream.of(actions)
                .forEach(action -> action.accept(objectToUpdate));
        return objectToUpdate;
    }

    public static <E> E getObjectWithData(E objectToUpdate, List<Consumer<E>> actions) {
        if (null == actions || 0 == actions.size()) {
            return objectToUpdate;
        }
        actions.stream()
                .forEach(action -> action.accept(objectToUpdate));
        return objectToUpdate;
    }
}
