package org.sparkling.bitmap.core.impl;


import org.sparkling.bitmap.core.BitmapConst;
import org.sparkling.bitmap.core.BitmapUnit;
import org.sparkling.bitmap.core.CalculatableBitmap;
import org.sparkling.bitmap.utils.BitmapUtils;
import org.sparkling.bitmap.utils.Bitmaps;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class BitmapOneHot implements CalculatableBitmap<BitmapOneHot> {

    private String name;
    private BitmapUnit unit;

    public BitmapOneHot() {
        unit = Bitmaps.newEmptyBitmapUnit();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void dump(OutputStream outputStream) throws IOException {
        outputStream.write(BitmapUtils.convertBitmapType(bitmapType()));
        unit.dump(outputStream);
    }

    @Override
    public void load(InputStream inputStream) throws IOException {
        byte[] type = new byte[2];
        inputStream.read(type);
        if (!bitmapType().equals(BitmapUtils.parseBitmapType(type))) {
            throw new IOException("Bitmap type dis-match");
        }
        unit.load(inputStream);
    }

    @Override
    public void add(int index) {
        unit.add(index);
    }

    @Override
    public void add(int[] index) {
        unit.add(index);
    }

    @Override
    public void or(BitmapOneHot iBitmap) {
        unit.or(iBitmap.unit);
    }


    @Override
    public void and(BitmapOneHot iBitmap) {
        unit.and(iBitmap.unit);
    }

    @Override
    public void andNot(BitmapOneHot iBitmap) {
        unit.andNot(iBitmap.unit);
    }

    @Override
    public void xor(BitmapOneHot iBitmap) {
        unit.xor(iBitmap.unit);
    }

    @Override
    public Iterator<Long> iterator() {
        return unit.iterator();
    }

    @Override
    public int cardinality() {
        return unit.cardinality();
    }

    @Override
    public int[] toArray() {
        return unit.toArray();
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.unit.setName(this.name + "_unit");
    }

    @Override
    public Integer bitmapType() {
        return BitmapConst.BITMAP_TYPE_ONEHOT;
    }

    @Override
    public boolean contains(int index) {
        return this.unit.contains(index);
    }



}
