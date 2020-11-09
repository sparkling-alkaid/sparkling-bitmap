package org.sparkling.bitmap.core;

public interface SegBitmapLoader<T extends SegmentedBitmap> extends BitmapLoader{

    IBitmap loadSegment(String name, Integer segIndex);

}
