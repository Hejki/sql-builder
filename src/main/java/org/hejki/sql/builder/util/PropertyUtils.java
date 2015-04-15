package org.hejki.sql.builder.util;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * TODO Document me.
 *
 * @author Petr Hejkal <petr.hejkal@doxologic.com>
 */
public class PropertyUtils {

    public static void property(String propertyName, Object parametersObject, Consumer<Object> propertyValueConsumer) {
        if (null == parametersObject) {
            return;
        }

        try {
            Field field = parametersObject.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            propertyValueConsumer.accept(field.get(parametersObject));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot get value from property " + propertyName + " on object " + parametersObject, e);
        } catch (NoSuchFieldException e) {
            // it's ok (maybe)
        }
    }
}
