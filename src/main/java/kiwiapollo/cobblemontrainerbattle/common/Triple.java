package kiwiapollo.cobblemontrainerbattle.common;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class Triple<T> implements Iterable<T> {
    public static final int SIZE = 3;

    private T first;
    private T second;
    private T third;

    public Triple() {
        this(null, null, null);
    }

    public Triple(T first, T second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public T getThird() {
        return third;
    }

    public void setThird(T third) {
        this.third = third;
    }

    public void set(int index, T element) {
        if (index == 0) {
            setFirst(element);

        } else if (index == 1) {
            setSecond(element);

        } else if (index == 2) {
            setThird(element);

        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public T get(int index) {
        if (index == 0) {
            return getFirst();

        } else if (index == 1) {
            return getSecond();

        } else if (index == 2) {
            return getThird();

        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void clear() {
        first = null;
        second = null;
        third = null;
    }

    public int occupied() {
        return getAsList().stream().filter(Objects::nonNull).toList().size();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return getAsList().iterator();
    }

    public Stream<T> stream() {
        return getAsList().stream();
    }

    private List<T> getAsList() {
        try {
            return List.of(first, second, third);

        } catch (NullPointerException e) {
            return List.of();
        }
    }
}
