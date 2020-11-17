
package org.sparkling.bitmap.core.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import org.sparkling.bitmap.core.IBitmap;
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
public class BaseSegmentBitmap<T extends IBitmap> implements SegmentedBitmap<T> {

    private String name;
    private Integer bitmapType;
    private Integer segCount;
    private Long step;
    private Map<Integer, T> bitmapMap;

    protected BaseSegmentBitmap(String name, Integer bitmapType, Integer segCount, Long step){
        this.name = name;
        this.bitmapType = bitmapType;
        this.segCount = segCount;
        this.step = step;
        this.bitmapMap = Maps.newHashMapWithExpectedSize(bitmapMap.size());
    }

    public BaseSegmentBitmap(String name, Integer bitmapType, Integer segCount, Long step, Map<Integer, T> bitmapMap) {
        this(name, bitmapType, segCount, step);
        this.bitmapMap.putAll(bitmapMap);
    }


    protected Integer getSegCount(){
        return segCount;
    }

    @Override
    public Integer bitmapType() {
        return bitmapType;
    }

    @Override
    public T getBitmapSeg(int segNo) {
        if (bitmapMap.containsKey(segNo)) {
            return bitmapMap.get(segNo);
        }
        return null;
    }

    @Override
    public List<T> getBitmapList() {
        List<T> ret = new ArrayList<>(segCount);
        for (int i = 0; i < segCount; i++) {
            ret.add(getBitmapSeg(i));
        }
        return ret;
    }

    @Override
    public Map<Integer, T> getBitmapMap() {
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
        return makeIterators((segNo, bitmapSeg) -> new AddOffsetIterator(step * segNo, bitmapSeg.iterator()));
    }


    protected List<Integer> getSegNumbers(){
        return bitmapMap.keySet().stream().filter(a -> a >= 0).collect(Collectors.toList());
    }

    protected Iterator<Long> makeIterators(SegmentOperation<Iterator<Long>> operation){
        List<Integer> segNumbers = getSegNumbers();
        List<Iterator<Long>> iterators = new ArrayList<>(segNumbers.size());
        for (Integer segNo : segNumbers) {
            iterators.add(operation.apply(segNo, getBitmapSeg(segNo)));
        }
        return Iterators.concat(iterators.iterator());
    }

    protected Iterable<Long> makeIterables(SegmentOperation<Iterable<Long>> operation){
        List<Integer> segNumbers = getSegNumbers();
        List<Iterable<Long>> iterables = new ArrayList<>(segNumbers.size());
        for (Integer segNo : segNumbers) {
            iterables.add(operation.apply(segNo, getBitmapSeg(segNo)));
        }
        return Iterables.concat(iterables);
    }

    protected void segOperations(SegmentOperation<?> operation){
        List<Integer> segNumbers = getSegNumbers();
        for (Integer segNo : segNumbers) {
            operation.apply(segNo, getBitmapSeg(segNo));
        }
    }



    @FunctionalInterface
    public interface SegmentOperation<S> {

        S apply(int segNo, IBitmap bitmapSeg);

    }

    protected class AddOffsetIterator implements Iterator<Long> {

        private long offset;
        private Iterator<Long> wrapped;

        public AddOffsetIterator(long offset, Iterator<Long> wrapped) {
            this.offset = offset;
            this.wrapped = wrapped;
        }

        @Override
        public boolean hasNext() {
            return wrapped.hasNext();
        }

        @Override
        public Long next() {
            return offset + wrapped.next();
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
