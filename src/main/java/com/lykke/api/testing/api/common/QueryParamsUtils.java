package com.lykke.api.testing.api.common;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.lykke.api.testing.annotations.QueryParameters;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

@Slf4j
@UtilityClass
public class QueryParamsUtils {

    private static final String STRING_TYPE_NAME = "String";
    private static final String STRING_ARRAY_TYPE_MAME = "java.lang.String[]";

    @SneakyThrows
    public static <E> Map<String, String> getQueryParams(E requestObject) {
        if (null == requestObject) {
            return new HashMap<>();
        }
        return getObjectFields(requestObject).entrySet().stream()
                .filter(item -> !EMPTY.equalsIgnoreCase(item.getValue()))
                .collect(toMap(item -> pascalizeFieldName(item.getKey()), item -> item.getValue()));
    }

    @SneakyThrows
    public static <E> Map<String, String> getQueryParams(E requestObject, boolean camelCaseFormat) {
        if (null == requestObject) {
            return new HashMap<>();
        }
        Stream<Map.Entry<String, String>> stream = getObjectFields(requestObject).entrySet().stream()
                .filter(item -> !EMPTY.equalsIgnoreCase(item.getValue()));
        return camelCaseFormat
                ? stream
                .collect(toMap(item -> camelizeFieldName(item.getKey()), item -> item.getValue()))
                : stream
                        .collect(toMap(item -> pascalizeFieldName(item.getKey()), item -> item.getValue()));
    }

    @SneakyThrows
    public static <E> Map<String, String> getQueryParams(E requestObject, Predicate<String> predicate) {
        if (null == requestObject) {
            return new HashMap<>();
        }
        return getObjectFields(requestObject).entrySet().stream()
                .filter(item -> predicate.test(item.getValue()))
                .collect(toMap(item -> pascalizeFieldName(item.getKey()), item -> item.getValue()));
    }

    @SneakyThrows
    public static <E> org.apache.commons.collections4.MultiMap<String, String> getQueryParams(E[] requestObject,
            String fieldName) {
        if (null == requestObject) {
            return new MultiValueMap<>();
        }
        MultiMap<String, String> resultMap = new MultiValueMap<String, String>();
        Arrays.stream(requestObject)
                .forEach(arrayItem -> resultMap.put(fieldName, arrayItem));
        return resultMap;
    }

    @SneakyThrows
    private static <E> Map<String, String> getObjectFields(E requestObject) {
        Map<String, String> mainObjectFieldsStream = Arrays.stream(requestObject
                .getClass()
                .getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !field.getType().isMemberClass() && !field.getType()
                        .isAnnotationPresent(QueryParameters.class))
                .collect(toMap(field -> field.getName(), field -> getValue(requestObject, buildGetterName(field))));

        Map<String, String> parentObjectFieldStream = Arrays.stream(requestObject
                .getClass()
                .getSuperclass()
                .getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !field.getType().isMemberClass() && !field.getType()
                        .isAnnotationPresent(QueryParameters.class))
                .collect(toMap(field -> field.getName(), field -> getValue(requestObject, buildGetterName(field))));

        Map<String, String> memberClassFieldsStream = Arrays.stream(requestObject
                .getClass()
                .getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
                .filter(field -> field.getType().isMemberClass() || field.getType()
                        .isAnnotationPresent(QueryParameters.class)
                )
                .flatMap(field -> Stream.concat(Arrays.stream(field.getType().getDeclaredFields()),
                        Arrays.stream(field.getType().getSuperclass().getDeclaredFields()))
                        .map(subClassField -> FieldAndSubClassFieldPair.builder().field(field)
                                .subClassField(subClassField).build()))
                .distinct()
                .collect(toMap(fieldPair -> pascalizeFieldName(fieldPair.getField().getName()) + "."
                                + pascalizeFieldName(fieldPair.getSubClassField().getName()),
                        fieldPair -> {
                            try {
                                Object memberObject = requestObject.getClass()
                                        .getMethod("get" + pascalizeFieldName(fieldPair.getField().getName()))
                                        .invoke(requestObject);

                                Object result = memberObject.getClass()
                                        .getMethod("get" + pascalizeFieldName(fieldPair.getSubClassField().getName()))
                                        .invoke(memberObject);

                                return null == result ? EMPTY : String.valueOf(result);
                            } catch (IllegalAccessException e) {
                                log.info(e.getMessage());
                            } catch (SecurityException e) {
                                log.info(e.getMessage());
                            } catch (NoSuchMethodException e) {
                                log.info(e.getMessage());
                            } catch (InvocationTargetException e) {
                                log.info(e.getMessage());
                            }
                            return EMPTY;
                        }));

        val twoStreams = Stream
                .concat(mainObjectFieldsStream.entrySet().stream(), parentObjectFieldStream.entrySet().stream());
        return Stream.concat(twoStreams, memberClassFieldsStream.entrySet().stream())
                .distinct()
                .filter(entry -> !EMPTY.equalsIgnoreCase(entry.getValue())
                        && null != entry.getValue()
                        && !"0".equalsIgnoreCase(entry.getValue())
                        && !"0.0".equalsIgnoreCase(entry.getValue()))
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    @SneakyThrows
    private static <E> String getValue(E requestObject, String methodName) {
        try {
            if (null == requestObject) {
                return EMPTY;
            }

            Method method =
                    Stream.concat(
                            Stream.of(getMethod(requestObject, methodName)),
                            Arrays.stream(requestObject
                                    .getClass()
                                    .getDeclaredFields())
                                    .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier
                                            .isFinal(field.getModifiers()))
                                    .filter(field -> field.getType().isMemberClass()
                                            || field.getType().isAnnotationPresent(QueryParameters.class)
                                    )
                                    .map(field -> {
                                        try {
                                            return getMethod(field.getDeclaringClass(), methodName);
                                        } catch (NoSuchMethodException e) {
                                            log.info(e.getMessage());
                                        }
                                        return null;
                                    }))
                            .findFirst()
                            .orElseThrow(NoSuchMethodException::new);

            if (null == method) {
                return EMPTY;
            }

            val getterInvocationResult = method.invoke(requestObject);
            if (null == getterInvocationResult) {
                return EMPTY;
            }

            if (getterInvocationResult.getClass().isArray()) {
                if (STRING_ARRAY_TYPE_MAME.equals(getterInvocationResult.getClass().getTypeName())) {
                    return null == String[].class.cast(getterInvocationResult) || 0 == String[].class
                            .cast(getterInvocationResult).length
                            ? EMPTY
                            : String[].class.cast(getterInvocationResult)[0];
                } else if (0 == getterInvocationResult.getClass().getFields().length) {
                    return null == Object[].class.cast(getterInvocationResult) || 0 == Object[].class
                            .cast(getterInvocationResult).length
                            ? EMPTY
                            : String.valueOf(Object[].class.cast(getterInvocationResult)[0]);
                } else {
                    return null == Object[].class.cast(getterInvocationResult) || 0 == Object[].class
                            .cast(getterInvocationResult).length
                            ? EMPTY
                            : String.valueOf(Object[].class.cast(getterInvocationResult)[0]);
                }
            }
            if (!STRING_TYPE_NAME.equals(getterInvocationResult.getClass().getTypeName())) {
                if (getterInvocationResult.getClass().isMemberClass()) {
                    return EMPTY;
                }
                return String.valueOf(getterInvocationResult);
            }
        } catch (IllegalAccessException e) {
            return EMPTY;
        } catch (InvocationTargetException e) {
            return EMPTY;
        }
        return EMPTY;
    }

    private static <E> Method getMethod(E object, String methodName) throws NoSuchMethodException {
        try {
            return object.getClass().getMethod(methodName);
        } catch (Exception e) {
            return null;
        }
    }

    private static String buildGetterName(Field field) {
        return "get"
                + field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);
    }

    private static String buildFieldName(Field field) {
        val preliminaryFieldName = field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);

        if (!field.getType().isPrimitive() && !field.getType().isArray() && field.getType().isMemberClass()) {
            return field.getType().getSimpleName() + "." + preliminaryFieldName;
        }

        return preliminaryFieldName;
    }

    private static String pascalizeFieldName(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    private static String camelizeFieldName(String fieldName) {
        return fieldName.substring(0, 1).toLowerCase()
                + fieldName.substring(1);
    }

    private static <E> E[] unboxPrimitiveArray(Object inputObject) {
        val typeName = inputObject.getClass().getSimpleName();
        if (!(inputObject instanceof Object[])) {
            if (inputObject instanceof int[]
                    || inputObject instanceof long[]
                    || inputObject instanceof float[]
                    || inputObject instanceof double[]) {
                return (E[]) inputObject;
            }
            return (E[]) inputObject;
        }
        return (E[]) inputObject;
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class FieldAndSubClassFieldPair {

        private Field field;
        private Field subClassField;
    }
}
