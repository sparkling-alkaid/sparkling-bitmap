
package com.sparkling.bitmap.core.impl;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import org.sparkling.bitmap.core.IBitmap;
import org.sparkling.bitmap.core.SegBitmapLoader;
import org.sparkling.bitmap.core.SegmentedBitmap;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * bitmap分段, 横向拆分
 *
 * @author sparkling_alkaid
 * @since 2020-11-09
 */
@NotThreadSafe
public class AbstractSegmentBitmap implements SegmentedBitmap {

    private String name;
    private Integer bitmapType;
    private Integer segCount;
    private Long step;
    private Map<Integer, IBitmap> bitmapMap;
    private SegBitmapLoader<? extends IBitmap> bitmapLoader;
    private boolean loaded;

    public AbstractSegmentBitmap(String name, SegBitmapLoader<? extends IBitmap> bitmapLoader) {
        this.name = name;
        this.bitmapLoader = bitmapLoader;
        this.loaded = false;
    }

    public AbstractSegmentBitmap(String name, Integer bitmapType, Integer segCount, Long step, Map<Integer, ? extends IBitmap> bitmapMap) {
        this.name = name;
        this.bitmapType = bitmapType;
        this.segCount = segCount;
        this.step = step;
        this.bitmapMap = Maps.newHashMapWithExpectedSize(bitmapMap.size());
        this.bitmapMap.putAll(bitmapMap);
        this.loaded = true;
    }

    private boolean isLoaded() {
        if (loaded) {
            return true;
        }
        if (bitmapMap != null && bitmapMap.size() == segCount) {
            loaded = true;
            return true;
        }
        return false;
    }


    @Override
    public Integer bitmapType() {
        return bitmapType;
    }

    @Override
    public IBitmap getBitmapSeg(int segNo) {
        if (isLoaded() || bitmapMap.containsKey(segNo)) {
            return bitmapMap.get(segNo);
        }
        IBitmap bitmap = bitmapLoader.loadSegment(name, segNo);
        bitmapMap.put(segNo, bitmap);
        return bitmap;
    }

    @Override
    public List<? extends IBitmap> getBitmapList() {
        List<IBitmap> ret = new ArrayList<>(segCount);
        for (int i = 0; i < segCount; i++) {
            ret.add(getBitmapSeg(i));
        }
        return ret;
    }

    @Override
    public Map<Integer, ? extends IBitmap> getBitmapMap() {
        return bitmapMap;
    }

    @Override
    public void dump(OutputStream outputStream) throws IOException {
        throw new RuntimeException("segmented bitmap direct dump not supported now");
    }

    @Override
    public void load(InputStream inputStream) throws IOException {
        throw new RuntimeException("segmented bitmap direct load not supported now");
    }

    @Override
    public void add(int index) {
        add(Integer.toUnsignedLong(index));
    }

    @Override
    public void add(int[] index) {
        for (int id : index) {
            add(id);
        }
    }

    @Override
    public boolean contains(int index) {
        return contains(Integer.toUnsignedLong(index));
    }


    @Override
    public void add(long index) {
        getBitmapSeg(segNo(index)).add(indexInSeg(index));
    }

    @Override
    public void add(long[] indexArr) {
        for (long index : indexArr) {
            add(index);
        }
    }

    @Override
    public boolean contains(long index) {
        return getBitmapSeg(segNo(index)).contains(indexInSeg(index));
    }

    private int segNo(long index) {
        return (int) (index / step);
    }

    private int indexInSeg(long index) {
        return (int) (index % step);
    }

    @Override
    public long longCardinality() {
        return getBitmapList().stream().map(b -> b.cardinality()).reduce((x, y) -> x + y).get();
    }

    private int getSegByIndex(Long index) {
        return (int) (index / step);
    }

    @Override
    public long cardinality() {
        return getBitmapList().stream().map(IBitmap::cardinality).reduce((x, y) -> x + y).get();
    }


    @Override
    public Iterator<Long> iterator() {
        List<Integer> segNumbers = bitmapMap.keySet().stream().filter(a -> a >= 0).collect(Collectors.toList());
        List<Iterator<Long>> iterators = new ArrayList<>(segNumbers.size());
        for (Integer segNo : segNumbers) {
            iterators.add(new AddStepIterator(step * segNo, bitmapMap.get(segNo).iterator()));
        }
        return Iterators.concat(iterators.iterator());
    }


    private class AddStepIterator implements Iterator<Long> {

        private long step;
        private Iterator<Long> wrapped;

        public AddStepIterator(long step, Iterator<Long> wrapped) {
            this.step = step;
            this.wrapped = wrapped;
        }

        @Override
        public boolean hasNext() {
            return wrapped.hasNext();
        }

        @Override
        public Long next() {
            return step + wrapped.next();
        }

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Long step() {
        return step;
    }

}
