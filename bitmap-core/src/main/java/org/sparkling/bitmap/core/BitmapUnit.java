package org.sparkling.bitmap.core;

import org.roaringbitmap.RoaringBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;


/**
 * Bitmap最小运算单位
 */
public interface BitmapUnit extends BitmapLike, Named, Calculatable<BitmapUnit> {

    RoaringBitmap internal();

    BitmapUnit copy();

    int serializedSizeInBytes();

}
