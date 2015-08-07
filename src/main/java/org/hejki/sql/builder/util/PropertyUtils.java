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

        property(propertyName, parametersObject, parametersObject.getClass(), propertyValueConsumer);
    }

    private static void property(String propertyName, Object parametersObject, Class<?> objectClass, Consumer<Object> propertyValueConsumer) {
        try {
            Field field = objectClass.getDeclaredField(propertyName);
            field.setAccessible(true);
            propertyValueConsumer.accept(field.get(parametersObject));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot get value from property " + propertyName + " on object " + parametersObject, e);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = objectClass.getSuperclass();

            if (null != superclass && objectClass != superclass) {
                property(propertyName, parametersObject, superclass, propertyValueConsumer);
            }
            // it's ok (maybe)
        }
    }
}
