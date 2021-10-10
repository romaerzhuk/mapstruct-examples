/*
 * VTB Group. Do not reproduce without permission in writing.
 * Copyright (c) 2021 VTB Group. All rights reserved.
 */

package test.hamcrest;

import static org.hamcrest.Matchers.is;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Сравнивает объект по его свойствам.
 * @param <T> тип объекта
 *
 * @author Roman_Erzhukov
 */
public abstract class PropertiesMatcher<T> extends BaseMatcher<T> {
    private static class FieldName {
        final String name;
        int index = -1;

        FieldName(String name) {
            this.name = name;
        }
    }

    private final List<FieldName> fieldName = new ArrayList<>();
    private final String name;
    private List<String> list;
    private StringBuilder sb;

    /**
     * Сравнивает объект по его свойствам.
     *
     * @param <T>
     *            тип объекта
     */
    public interface Checker<T> {
        /**
         * Сравнивает объект по его свойствам.
         *
         * @param a
         *            актуальный объект
         * @param m
         *            создаёт результат сраврения
         */
        void check(T a, PropertiesMatcher<?> m);
    }

    /**
     * Создаёт эталонный объект для сравнения.
     *
     * @param name
     *            имя эталонного объекта
     * @param checker
     *            метод сравнения объектов
     * @return {@link Matcher}
     */
    public static <T, E extends T> Matcher<T> of(String name, final Checker<E> checker) {
        return new PropertiesMatcher<>(name) {
            @SuppressWarnings("unchecked")
            @Override
            protected void check(T item) {
                checker.check((E) item, this);
            }
        };
    }

    /**
     * Создаёт эталонный объект для сравнения.
     *
     * @param name
     *            имя эталонного объекта
     * @param checker
     *            метод сравнения объектов
     * @return {@link Matcher}
     */
    public static <T, E extends T> Matcher<T> of(Class<E> name, Checker<E> checker) {
        return of(name.getSimpleName(), checker);
    }

    public PropertiesMatcher(String name) {
        this.name = name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final boolean matches(Object item) {
        list = null;
        try {
            check((T) item);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return isEqual();
    }

    @Override
    public void describeTo(Description d) {
        d.appendText(name + (list != null ? list : ""));
    }

    /**
     * Проверяет актуальный объект на совпадение с ожидаемым.
     *
     * @param it
     *            актуальный объект
     * @throws Throwable исключение
     */
    protected abstract void check(T it) throws Throwable;

    /**
     * Проверяет свойства объекта на совпадение.
     *
     * @param name
     *            имя свойства
     * @param actual
     *            актуальное значение
     * @param expected
     *            ожидаемое значение, или {@link Matcher}
     * @return this
     */
    public PropertiesMatcher<T> add(String name, Object actual, Object expected) {
        if (expected instanceof Matcher) {
            return test(name, actual, (Matcher<?>) expected);
        }
        return test(name, actual, is(expected));
    }

    /**
     * Проверяет свойства объекта на совпадение с проверкой на null.
     *
     * @param <A> тип актуального значения
     * @param <E> тип ожидаемого значения
     * @param name наименование свойства
     * @param actual актуальное значение
     * @param expected ожидаемое значение
     * @param consumer проверка для не null-значений
     * @return this
     */
    public <A, E> PropertiesMatcher<T> add(String name, A actual, E expected, BiConsumer<A, E> consumer) {
        if (actual == null || expected == null) {
            add(name + " is null", actual == null, expected == null);
            return this;
        }
        FieldName field = new FieldName(name);
        fieldName.add(field);
        consumer.accept(actual, expected);
        fieldName.remove(fieldName.size() - 1);
        return this;
    }

    /**
     * Проверяет список-свойство объекта на совпадение.
     *
     * @param name имя спика
     * @param actual актуальное множество
     * @param comparator сортирует элементы множество
     * @param expected ожидаемый список
     * @param callback проверка элемента списка
     * @param <A> тип актуального элемента
     * @param <E> тип ожидаемого элемента
     * @return this
     */
    public <A, E> PropertiesMatcher<T> addList(String name, Iterable<A> actual, Comparator<A> comparator, List<E> expected,
                                               BiConsumer<A, E> callback) {
        List<A> actualList = actual == null ? null : StreamSupport.stream(actual.spliterator(), false)
                .sorted(comparator)
                .collect(Collectors.toList());
        return addList(name, actualList, expected, callback);
    }

    /**
     * Проверяет список-свойство объекта на совпадение.
     *
     * @param name имя спика
     * @param actual актуальное множество
     * @param expected ожидаемый список
     * @param comparator сортирует элементы множество
     * @param callback проверка элемента списка
     * @param <A> тип актуального элемента
     * @param <E> тип ожидаемого элемента
     * @return this
     */
    public <A, E> PropertiesMatcher<T> addList(String name, List<A> actual, Iterable<E> expected, Comparator<E> comparator,
                                               BiConsumer<A, E> callback) {
        List<E> expectedList = expected == null ? null : StreamSupport.stream(expected.spliterator(), false)
                .sorted(comparator)
                .collect(Collectors.toList());
        return addList(name, actual, expectedList, callback);
    }

    /**
     * Проверяет список-свойство объекта на совпадение.
     *
     * @param name имя спика
     * @param actual актуальный список
     * @param expected ожидаемый список
     * @param callback проверка элемента списка
     * @param <A> тип актуального элемента
     * @param <E> тип ожидаемого элемента
     * @return this
     */
    public <A, E> PropertiesMatcher<T> addList(String name, List<A> actual, List<E> expected, BiConsumer<A, E> callback) {
        if (actual == null && expected == null) {
            return this;
        }
        Integer actualSize = actual == null ? null : actual.size();
        Integer expectedSize = expected == null ? null : expected.size();
        if (!Objects.equals(actualSize, expectedSize)) {
            add(name + ".size", actualSize, expectedSize);
            return this;
        }
        FieldName field = new FieldName(name);
        fieldName.add(field);
        for (int i = 0; i < expected.size(); i++) {
            field.index = i;
            callback.accept(actual.get(i), expected.get(i));
        }
        fieldName.remove(fieldName.size() - 1);
        return this;
    }

    private PropertiesMatcher<T> test(String name, Object actual, Matcher<?> matcher) {
        if (!matcher.matches(actual)) {
            if (list == null) {
                list = new ArrayList<>();
                sb = new StringBuilder();
            }
            sb.append('{');
            String splitter = "";
            for (FieldName field : fieldName) {
                sb.append(splitter).append(field.name);
                if (field.index >= 0) {
                    sb.append('[').append(field.index).append(']');
                }
                splitter = ".";
            }
            sb.append(splitter).append(name).append(": ").append(actual);
            StringDescription description = new StringDescription(sb);
            sb.append(' ');
            matcher.describeTo(description);
            sb.append('}');
            list.add(sb.toString());
            sb.setLength(0);
        }
        return this;
    }

    /**
     * Возвращает признак равенства объектов.
     *
     * @return true, если объекты равны
     */
    public boolean isEqual() {
        return list == null;
    }
}
