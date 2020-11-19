package org.sparkling.bitmap.core;

import java.util.Map;

public interface SegBitmapLoader<T extends IBitmap> extends BitmapLoader<T>{

    T loadSegment(String name, Integer segIndex);

    Map<Integer, T> loadAllSeg(String name);

}
