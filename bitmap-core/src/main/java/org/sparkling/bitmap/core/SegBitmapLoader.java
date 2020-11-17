package org.sparkling.bitmap.core;

public interface SegBitmapLoader<T extends IBitmap> extends BitmapLoader<T>{

    T loadSegment(String name, Integer segIndex);

}
