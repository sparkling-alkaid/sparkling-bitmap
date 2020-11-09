package org.sparkling.bitmap.core;

public interface ComparableBitmap<T> extends IBitmap<T> {

    Iterable<Integer> gte(int value);

    Iterable<Integer> gt(int value);

    Iterable<Integer> eq(int value);

    Iterable<Integer> lt(int value);

    Iterable<Integer> lte(int value);

}
