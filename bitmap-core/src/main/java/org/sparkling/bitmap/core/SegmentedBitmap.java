package org.sparkling.bitmap.core;

import java.util.List;
import java.util.Map;

public interface SegmentedBitmap<T extends IBitmap> extends IBitmap, Named{

    Integer bitmapType();

    T getBitmapSeg(int segNo);

    Long step();

    void add(long index);

    void add(long[] index);

    boolean contains(long index);

    long longCardinality();

    List<T> getBitmapList();

    Map<Integer, T> getBitmapMap();

}
