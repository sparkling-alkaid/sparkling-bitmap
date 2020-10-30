package org.sparkling.bitmap.core;

import java.util.List;

public interface SegmentedBitmap extends IBitmap{

    Long step();

    int segmentCount();

    List<? extends IBitmap> segments();

    IBitmap getSegment(int segNo);

}
