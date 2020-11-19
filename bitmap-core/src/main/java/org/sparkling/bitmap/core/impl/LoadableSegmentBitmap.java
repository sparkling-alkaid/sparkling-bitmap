package org.sparkling.bitmap.core.impl;

import org.sparkling.bitmap.core.IBitmap;
import org.sparkling.bitmap.core.SegBitmapLoader;

public class LoadableSegmentBitmap<T extends IBitmap> extends BaseSegmentBitmap<T> {

    private SegBitmapLoader<T> bitmapLoader;
    private boolean loaded;

    public LoadableSegmentBitmap(String name, Integer bitmapType, Integer segCount, Long step, SegBitmapLoader<T> bitmapLoader) {
        super(name, bitmapType, segCount, step);
        this.bitmapLoader = bitmapLoader;
        this.loaded = false;
    }


    protected boolean isLoaded() {
        if (loaded) {
            return true;
        }
        if (getBitmapMap() != null && getBitmapMap().size() == getSegCount()) {
            loaded = true;
            return true;
        }
        return false;
    }


    @Override
    public T getBitmapSeg(int segNo) {
        if (isLoaded() || getBitmapMap().containsKey(segNo)) {
            return getBitmapMap().get(segNo);
        }
        T bitmap = bitmapLoader.loadSegment(name(), segNo);
        getBitmapMap().put(segNo, bitmap);
        return bitmap;
    }




}
