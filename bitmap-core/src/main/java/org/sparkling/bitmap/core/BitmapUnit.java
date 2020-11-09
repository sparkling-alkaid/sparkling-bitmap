package org.sparkling.bitmap.core;

import org.roaringbitmap.RoaringBitmap;


/**
 * Bitmap最小运算单位
 */
public interface BitmapUnit extends BitmapLike, Named, CalculatableBitmap<BitmapUnit> {

    RoaringBitmap internal();

    BitmapUnit copy();

    int serializedSizeInBytes();

}
