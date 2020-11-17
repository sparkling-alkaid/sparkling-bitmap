package org.sparkling.bitmap.core.impl;

import org.sparkling.bitmap.core.*;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class LoadableComparableSegmentBitmap extends LazyLoadSegmentBitmap<BitmapBitwise> implements ComparableBitmap<SegmentedBitmap<BitmapBitwise>, CalculatableSegBitmap<CalculatableBitmap>> {


    private static AtomicLong COUNTER = new AtomicLong(0L);

    public LoadableComparableSegmentBitmap(String name, Integer bitmapType, Integer segCount, Long step, SegBitmapLoader<BitmapBitwise> bitmapLoader) {
        super(name, bitmapType, segCount, step, bitmapLoader);
    }

    @Override
    public Iterable<Long> gte(long value) {
        return makeIterables((segNo, bitmapSeg) -> new AddOffsetIterable(segNo * step(), ((ComparableBitmap) bitmapSeg).gte(value)));

    }

    @Override
    public Iterable<Long> gt(long value) {
        return makeIterables((segNo, bitmapSeg) -> new AddOffsetIterable(segNo * step(), ((ComparableBitmap) bitmapSeg).gt(value)));
    }

    @Override
    public Iterable<Long> eq(long value) {
        return makeIterables((segNo, bitmapSeg) -> new AddOffsetIterable(segNo * step(), ((ComparableBitmap) bitmapSeg).eq(value)));
    }

    @Override
    public Iterable<Long> lt(long value) {
        return makeIterables((segNo, bitmapSeg) -> new AddOffsetIterable(segNo * step(), ((ComparableBitmap) bitmapSeg).lt(value)));
    }

    @Override
    public Iterable<Long> lte(long value) {
        return makeIterables((segNo, bitmapSeg) -> new AddOffsetIterable(segNo * step(), ((ComparableBitmap) bitmapSeg).lte(value)));
    }

    @Override
    public CalculatableSegBitmap<CalculatableBitmap> gteAsCalc(long value) {
        CalculatableSegBitmapInner ret = new CalculatableSegBitmapInner("calc-result-"+COUNTER.incrementAndGet(), bitmapType(), getSegCount(), step());
        segOperations((segNo, bitmapSeg) ->  ret.setBitmapSeg(segNo, ((ComparableBitmap) bitmapSeg).eqAsCalc(value)));
        return ret;
    }

    @Override
    public CalculatableSegBitmap<CalculatableBitmap> gtAsCalc(long value) {
        CalculatableSegBitmapInner ret = new CalculatableSegBitmapInner("calc-result-"+COUNTER.incrementAndGet(), bitmapType(), getSegCount(), step());
        segOperations((segNo, bitmapSeg) ->  ret.setBitmapSeg(segNo, ((ComparableBitmap) bitmapSeg).gtAsCalc(value)));
        return ret;
    }

    @Override
    public CalculatableSegBitmap<CalculatableBitmap> eqAsCalc(long value) {
        CalculatableSegBitmapInner ret = new CalculatableSegBitmapInner("calc-result-"+COUNTER.incrementAndGet(), bitmapType(), getSegCount(), step());
        segOperations((segNo, bitmapSeg) ->  ret.setBitmapSeg(segNo, ((ComparableBitmap) bitmapSeg).eqAsCalc(value)));
        return ret;
    }

    @Override
    public CalculatableSegBitmap<CalculatableBitmap> ltAsCalc(long value) {
        CalculatableSegBitmapInner ret = new CalculatableSegBitmapInner("calc-result-"+COUNTER.incrementAndGet(), bitmapType(), getSegCount(), step());
        segOperations((segNo, bitmapSeg) ->  ret.setBitmapSeg(segNo, ((ComparableBitmap) bitmapSeg).ltAsCalc(value)));
        return ret;
    }

    @Override
    public CalculatableSegBitmap<CalculatableBitmap> lteAsCalc(long value) {
        CalculatableSegBitmapInner ret = new CalculatableSegBitmapInner("calc-result-"+COUNTER.incrementAndGet(), bitmapType(), getSegCount(), step());
        segOperations((segNo, bitmapSeg) ->  ret.setBitmapSeg(segNo, ((ComparableBitmap) bitmapSeg).lteAsCalc(value)));
        return ret;
    }


    private class CalculatableSegBitmapInner extends BaseSegmentBitmap<CalculatableBitmap> implements CalculatableSegBitmap<CalculatableBitmap>{

        public CalculatableSegBitmapInner(String name, Integer bitmapType, Integer segCount, Long step) {
            super(name, bitmapType, segCount, step);
        }

        public CalculatableBitmap setBitmapSeg(int segNo, CalculatableBitmap unit){
            return getBitmapMap().put(segNo, unit);
        }

        @Override
        public void or(CalculatableSegBitmap<CalculatableBitmap> bitmapUnit) {
            List<Integer> segList  = getSegNumbers();
            for(Integer seg : segList){
                getBitmapMap().get(seg).or(bitmapUnit.getBitmapSeg(seg));
            }
        }

        @Override
        public void and(CalculatableSegBitmap<CalculatableBitmap> bitmapUnit) {
            List<Integer> segList  = getSegNumbers();
            for(Integer seg : segList){
                getBitmapMap().get(seg).and(bitmapUnit.getBitmapSeg(seg));
            }
        }

        @Override
        public void andNot(CalculatableSegBitmap<CalculatableBitmap> bitmapUnit) {
            List<Integer> segList  = getSegNumbers();
            for(Integer seg : segList){
                getBitmapMap().get(seg).andNot(bitmapUnit.getBitmapSeg(seg));
            }
        }

        @Override
        public void xor(CalculatableSegBitmap<CalculatableBitmap> bitmapUnit) {
            List<Integer> segList  = getSegNumbers();
            for(Integer seg : segList){
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
