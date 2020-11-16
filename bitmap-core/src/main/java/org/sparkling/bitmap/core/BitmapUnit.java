package org.sparkling.bitmap.core;


/**
 * Bitmap最小运算单位
 */
public interface BitmapUnit<T> extends BitmapLike, Named, CalculatableBitmap<BitmapUnit<T>> {

    T internal();

    BitmapUnit<T> copy();

    int serializedSizeInBytes();

}
