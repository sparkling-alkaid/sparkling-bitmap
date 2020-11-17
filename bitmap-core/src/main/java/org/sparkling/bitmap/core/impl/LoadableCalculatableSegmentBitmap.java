package org.sparkling.bitmap.core.impl;

import org.sparkling.bitmap.core.CalculatableBitmap;
import org.sparkling.bitmap.core.CalculatableSegBitmap;
import org.sparkling.bitmap.core.SegBitmapLoader;

import java.util.List;

public class LoadableCalculatableSegmentBitmap extends LazyLoadSegmentBitmap<CalculatableBitmap> implements CalculatableBitmap<CalculatableSegBitmap> {

    public LoadableCalculatableSegmentBitmap(String name, Integer bitmapType, Integer segCount, Long step, SegBitmapLoader<CalculatableBitmap> bitmapLoader) {
        super(name, bitmapType, segCount, step, bitmapLoader);
    }

    protected void calc(CalculatableSegBitmap bitmapUnit, CalcOperation op) {
        List<Integer> segNoList = getSegNumbers();
        for (Integer seg : segNoList) {
            CalculatableBitmap origin = getBitmapSeg(seg);
            CalculatableBitmap toCalc = (CalculatableBitmap) bitmapUnit.getBitmapSeg(seg);
            if (origin == null || toCalc == null) {
                continue;
            } else {
                op.calc(origin, toCalc);
            }
        }
    }

    @Override
    public void or(CalculatableSegBitmap bitmapUnit) {
        calc(bitmapUnit, (b1, b2) -> b1.or(b2));
    }


    @Override
    public void and(CalculatableSegBitmap bitmapUnit) {
        calc(bitmapUnit, (b1, b2) -> b1.and(b2));
    }

    @Override
    public void andNot(CalculatableSegBitmap bitmapUnit) {
        calc(bitmapUnit, (b1, b2) -> b1.andNot(b2));
    }

    @Override
    public void xor(CalculatableSegBitmap bitmapUnit) {
        calc(bitmapUnit, (b1, b2) -> b1.xor(b2));
    }


    @FunctionalInterface
    private interface CalcOperation {

        void calc(CalculatableBitmap b1, CalculatableBitmap b2);

    }


}
