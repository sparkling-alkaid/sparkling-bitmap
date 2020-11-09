package org.sparkling.bitmap.core;

import afu.org.checkerframework.checker.oigj.qual.O;

import java.util.List;
import java.util.Map;

public interface SegmentedBitmap extends IBitmap<SegmentedBitmap>, Named{

    Integer bitmapType();

    IBitmap getBitmapSeg(int segNo);

    Long step();

    void add(long index);

    void add(long[] index);

    boolean contains(long index);

    long longCardinality();

    List<? extends IBitmap> getBitmapList();

    Map<Integer, ? extends IBitmap> getBitmapMap();

}
