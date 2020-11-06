package org.sparkling.bitmap.core;

public interface Calculatable<T> {

    void or(T bitmapUnit);

    void and(T bitmapUnit);

    void andNot(T bitmapUnit);

    void xor(T bitmapUnit);

}
