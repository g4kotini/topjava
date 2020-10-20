package ru.javawebinar.topjava;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMatcher<T> {
    private final String[] ignoringFields;

    public TestMatcher(String[] ignoringFields) {
        this.ignoringFields = ignoringFields;
    }

    public static <T> TestMatcher<T> of(String... ignoringFields) {
        return new TestMatcher<>(ignoringFields);
    }

    public void assertMatch(T actual, T expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(ignoringFields).isEqualTo(expected);
    }

    @SafeVarargs
    public final void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoringFields).containsExactlyElementsOf(expected);
    }
}
