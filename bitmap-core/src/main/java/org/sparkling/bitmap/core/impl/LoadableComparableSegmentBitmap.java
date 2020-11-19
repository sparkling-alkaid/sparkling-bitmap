package org.sparkling.bitmap.core.impl;

import org.sparkling.bitmap.core.*;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


// todo test
public class LoadableComparableSegmentBitmap extends LoadableSegmentBitmap<BitmapBitwise> implements ComparableBitmap<SegmentedBitmap<BitmapBitwise>, CalculatableSegBitmap<CalculatableBitmap>> {


    private static AtomicLong COUNTER = new AtomicLong(0L);

    public LoadableComparableSegmentBitmap(String name, Integer bitmapType, Integer segCount, Long step, SegBitmapLoader<BitmapBitwise> bitmapLoader) {
        super(name, bitmapType, segCount, step, bitmapLoader);
    }

    @Override
    public CalculatableSegBitmapInner gte(long value) {
        CalculatableSegBitmapInner ret = new CalculatableSegBitmapInner("calc-result-" + COUNTER.incrementAndGet(), bitmapType(), getSegCount(), step());
        segOperations((segNo, bitmapSeg) -> ret.setBitmapSeg(segNo, ((BitmapBitwise) bitmapSeg).gte(value)));
        return ret;
    }

    @Override
    public CalculatableSegBitmapInner gt(long value) {
        return gte(value + 1);
    }

    @Override
    public CalculatableSegBitmapInner eq(long value) {
        CalculatableSegBitmapInner ret = new CalculatableSegBitmapInner("calc-result-" + COUNTER.incrementAndGet(), bitmapType(), getSegCount(), step());
        segOperations((segNo, bitmapSeg) -> ret.setBitmapSeg(segNo, ((BitmapBitwise) bitmapSeg).eq(value)));
        return ret;
    }

    @Override
    public CalculatableSegBitmapInner lt(long value) {
        CalculatableSegBitmapInner ret = new CalculatableSegBitmapInner("calc-result-" + COUNTER.incrementAndGet(), bitmapType(), getSegCount(), step());
        segOperations((segNo, bitmapSeg) -> ret.setBitmapSeg(segNo, ((BitmapBitwise) bitmapSeg).lt(value)));
        return ret;
    }

    @Override
    public CalculatableSegBitmapInner lte(long value) {
        return lt(value + 1);
    }



    private class CalculatableSegBitmapInner extends BaseSegmentBitmap<CalculatableBitmap> implements CalculatableSegBitmap<CalculatableBitmap> {

        public CalculatableSegBitmapInner(String name, Integer bitmapType, Integer segCount, Long step) {
            super(name, bitmapType, segCount, step);
        }

        public CalculatableBitmap setBitmapSeg(int segNo, CalculatableBitmap unit) {
            return getBitmapMap().put(segNo, unit);
        }

        @Override
        public void or(CalculatableSegBitmap<CalculatableBitmap> bitmapUnit) {
            List<Integer> segList = getSegNumbers();
            for (Integer seg : segList) {
                getBitmapMap().get(seg).or(bitmapUnit.getBitmapSeg(seg));
            }
        }

        @Override
        public void and(CalculatableSegBitmap<CalculatableBitmap> bitmapUnit) {
            List<Integer> segList = getSegNumbers();
            for (Integer seg : segList) {
                getBitmapMap().get(seg).and(bitmapUnit.getBitmapSeg(seg));
            }
        }

        @Override
        public void andNot(CalculatableSegBitmap<CalculatableBitmap> bitmapUnit) {
            List<Integer> segList = getSegNumbers();
            for (Integer seg : segList) {
                getBitmapMap().get(seg).andNot(bitmapUnit.getBitmapSeg(seg));
            }
        }

        @Override
        public void xor(CalculatableSegBitmap<CalculatableBitmap> bitmapUnit) {
            List<Integer> segList = getSegNumbers();
            for (Integer seg : segList) {
                getBitmapMap().get(seg).xor(bitmapUnit.getBitmapSeg(seg));
            }
        }

    }

    private class AddOffsetIterable implements Iterable<Long> {

        private long offset;
        private Iterable<Long> wrapped;

        public AddOffsetIterable(long offset, Iterable<Long> wrapped) {
            this.offset = offset;
            this.wrapped = wrapped;
        }

        @Override
        public Iterator<Long> iterator() {
            return new AddOffsetIterator(offset, wrapped.iterator());
        }

    }

}
