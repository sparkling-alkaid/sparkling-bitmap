package org.sparkling.bitmap.core;

public interface CalculatableBitmap<T> extends IBitmap{

    void or(T bitmapUnit);

    void and(T bitmapUnit);

    void andNot(T bitmapUnit);

    void xor(T bitmapUnit);

}
