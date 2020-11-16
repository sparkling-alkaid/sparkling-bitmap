package org.sparkling.bitmap.core.impl;

import org.roaringbitmap.PeekableIntIterator;
import org.roaringbitmap.RoaringBitmap;
import org.sparkling.bitmap.core.BitmapConst;
import org.sparkling.bitmap.core.BitmapUnit;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;

/**
 * bitmap的最小单位，roaring bitmap简单封装
 * @author sparkling_alkaid
 * @since 2020-11-04
 */
public class BitmapUnitImpl implements BitmapUnit<RoaringBitmap> {

    private String name;

    private RoaringBitmap roaringBitmap;

    public BitmapUnitImpl() {
        this(new RoaringBitmap());
    }

    public BitmapUnitImpl(RoaringBitmap roaringBitmap) {
        this.roaringBitmap = roaringBitmap;
    }

    /**
     * 写入一个长度，加一个二进制
     * @param os
     * @throws IOException
     */
    @Override
    public void dump(OutputStream os) throws IOException {
        roaringBitmap.runOptimize();
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeInt(roaringBitmap.serializedSizeInBytes());
            roaringBitmap.serialize(dos);
        }
    }

    @Override
    public void load(InputStream inputStream) throws IOException {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            dis.readInt();
            roaringBitmap.deserialize(dis);
        }
    }

    @Override
    public void add(int index) {
        roaringBitmap.add(index);
    }

    @Override
    public void add(int[] index) {
        roaringBitmap.add(index);
    }

    @Override
    public boolean contains(int index) {
        return roaringBitmap.contains(index);
    }

    @Override
    public void or(BitmapUnit<RoaringBitmap> bitmapUnit) {
        roaringBitmap.or(bitmapUnit.internal());
    }

    @Override
    public void and(BitmapUnit<RoaringBitmap> bitmapUnit) {
        roaringBitmap.and(bitmapUnit.internal());
    }

    @Override
    public void andNot(BitmapUnit<RoaringBitmap> bitmapUnit) {
        roaringBitmap.andNot(bitmapUnit.internal());
    }

    @Override
    public void xor(BitmapUnit<RoaringBitmap> bitmapUnit) {
        roaringBitmap.xor(bitmapUnit.internal());
    }

    @Override
    public long cardinality() {
        return roaringBitmap.getCardinality();
    }


    @Override
    public RoaringBitmap internal() {
        return roaringBitmap;
    }

    @Override
    public BitmapUnit<RoaringBitmap> copy() {
        try {
            return clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterator<Long> iterator() {
        return new IntIterator(roaringBitmap.getIntIterator());
    }

    private static class IntIterator implements Iterator<Long> {

        private PeekableIntIterator intIterator;

        public IntIterator(PeekableIntIterator intIterator) {
            this.intIterator = intIterator;
        }

        @Override
        public boolean hasNext() {
            return intIterator.hasNext();
        }

        @Override
        public Long next() {
            return (long) intIterator.next();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitmapUnitImpl that = (BitmapUnitImpl) o;
        return Objects.equals(roaringBitmap, that.roaringBitmap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roaringBitmap);
    }

    @Override
    protected BitmapUnit<RoaringBitmap> clone() throws CloneNotSupportedException {
        BitmapUnitImpl ret = new BitmapUnitImpl();
        ret.roaringBitmap = this.roaringBitmap.clone();
        return ret;
    }

    @Override
    public int serializedSizeInBytes() {
        return roaringBitmap.serializedSizeInBytes() + Integer.BYTES;
    }

    @Override
    public Integer bitmapType() {
        return BitmapConst.BITMAP_TYPE_UNIT;
    }

}
