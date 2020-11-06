package org.sparkling.bitmap.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import org.sparkling.bitmap.utils.BitmapUtils;
import org.sparkling.bitmap.utils.Bitmaps;

import java.io.*;
import java.util.*;

/**
 * 使用bit表示的数值性bitmap
 *
 * @author sparkling_alkaid
 * @since 2020-11-06
 */
public class BitmapBitwise implements IBitmap2D<BitmapBitwise> {

    private String name;

    private BitmapUnit notNullBitmap;

    private Map<Integer, BitmapUnit> bitmapMap;

    private int size;

    private char[] numberValueArr;

    public BitmapBitwise(int size) {
        this.notNullBitmap = Bitmaps.newEmptyBitmapUnit();
        this.bitmapMap = new HashMap<>((int) ((size + 1) / 0.75) + 1);
        for (int i = 0; i < size; i++) {
            bitmapMap.put(i, Bitmaps.newEmptyBitmapUnit());
        }
        this.size = size;
    }

    public BitmapBitwise(BitmapUnit nonNullBitmap, Map<Integer, BitmapUnit> bitmapMap) {
        Preconditions.checkNotNull(nonNullBitmap, "init bitmap-bitwise error, nonNullBitmap is null");
        Preconditions.checkNotNull(bitmapMap, "init bitmap-bitwise error, map is null");
        this.notNullBitmap = nonNullBitmap;
        this.bitmapMap = bitmapMap;
        this.size = bitmapMap.size();
    }

    /**
     * 格式 bitmapType(2字节)+size（4字节）+ 一个长度为size+1的list[bitIndex(四字节)+bitmap长度（4字节）+bitmap数组]
     *
     * @param outputStream
     * @throws IOException
     */
    @Override
    public void dump(OutputStream outputStream) throws IOException {
        outputStream.write(BitmapUtils.convertBitmapType(bitmapType()));
        outputStream.write(Ints.toByteArray(size));
        writeBitmap(outputStream, -1, notNullBitmap);
        for (Integer index : bitmapMap.keySet()) {
            writeBitmap(outputStream, index, bitmapMap.get(index));
        }
    }

    private void writeBitmap(OutputStream os, int bitmapIndex, BitmapUnit unit) throws IOException {
        try (OutputStream bos = BitmapUtils.bitmapDumpOutStream(os)) {
            bos.write(bitmapIndex);
            unit.dump(bos);
        }
    }

    @Override
    public void load(InputStream inputStream) throws IOException {
        clear();
        byte[] type = new byte[2];
        inputStream.read(type);
        if (!bitmapType().equals(BitmapUtils.parseBitmapType(type))) {
            throw new IOException("Bitmap type dis-match");
        }
        size = inputStream.read();
    }

    public void clear(){
        notNullBitmap = null;
        bitmapMap = new HashMap<>();
        size = 0;
        numberValueArr = null;
    }

    public BitmapUnit getBitmapAtIndex(int index) {
        BitmapUnit rb = bitmapMap.get(index);
        return rb == null ? Bitmaps.newEmptyBitmapUnit() : rb;
    }

    @Override
    public void add(int index) {
        notNullBitmap.add(index);
    }

    @Override
    public void add(int[] indexArr) {
        notNullBitmap.add(indexArr);
    }

    @Override
    public void add(int index, int value) {
        add(index);
        char[] chars = intValToCharArr(value, size);
        for (int i = 0; i < size; i++) {
            int operateIndex = chars.length - 1 - i;
            if (chars[operateIndex] == '0') {
                getBitmapAtIndex(operateIndex).add(index);
            }
        }
    }

    private char[] intValToCharArr(int value, int size) {
        if (numberValueArr != null) {
            return numberValueArr;
        }
        String binary = Integer.toBinaryString(value);
        String fullBinary = Strings.padStart(binary, size, '0');
        numberValueArr = fullBinary.toCharArray();
        return numberValueArr;
    }

    @Override
    public boolean contains(int index) {
        return notNullBitmap.contains(index);
    }

    @Override
    public int cardinality() {
        return notNullBitmap.cardinality();
    }

    @Override
    public int[] toArray() {
        return notNullBitmap.toArray();
    }

    @Override
    public Integer bitmapType() {
        return BitmapConst.BITMAP_TYPE_BITWISE;
    }

    @Override
    public Iterator<Integer> iterator() {
        return notNullBitmap.iterator();
    }

    @Override
    public void or(BitmapBitwise bitmapUnit) {
        // this method has no meaning in this type of bitmap, do nothing
    }

    @Override
    public void and(BitmapBitwise bitmapUnit) {
        // this method has no meaning in this type of bitmap, do nothing
    }

    @Override
    public void andNot(BitmapBitwise bitmapUnit) {
        // this method has no meaning in this type of bitmap, do nothing
    }

    @Override
    public void xor(BitmapBitwise bitmapUnit) {
        // this method has no meaning in this type of bitmap, do nothing
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    private class BitmapCalcUnit {

        private BitmapUnit ltBitmap;
        private BitmapUnit eqBitmap;

        public BitmapCalcUnit() {
        }

        public BitmapCalcUnit(BitmapUnit ltBitmap, BitmapUnit eqBitmap) {
            this.ltBitmap = ltBitmap;
            this.eqBitmap = eqBitmap;
        }

    }


    public BitmapUnit gte(int value) {
        return Bitmaps.unitAndNot(notNullBitmap, lt(value));
    }

    public BitmapUnit gt(int value) {
        return Bitmaps.unitAndNot(notNullBitmap, lte(value));
    }

    public BitmapUnit eq(int value) {
        char[] chars = intValToCharArr(value, size);
        BitmapUnit ret = null;
        BitmapUnit bitmapUnit = null;
        for (int i = 0; i < chars.length; i++) {
            bitmapUnit = getBitmapAtIndex(size - 1 - i);
            if (chars[i] == '1') {
                bitmapUnit = Bitmaps.unitAndNot(notNullBitmap, bitmapUnit);
                if (ret == null) {
                    ret = bitmapUnit;
                    continue;
                }
            } else {
                if (ret == null) {
                    ret = bitmapUnit.copy();
                    continue;
                }
            }
            ret.and(bitmapUnit);
        }
        return ret;
    }

    public BitmapUnit lte(int value) {
        return lt(value + 1);
    }

    public BitmapUnit lt(int value) {
        char[] chars = intValToCharArr(value, size);
        List<BitmapCalcUnit> list = new LinkedList<>();
        List<BitmapUnit> collects = new LinkedList<>();
        Character charPrevChar = null;
        for (int i = 0; i < chars.length; i++) {
            if (charPrevChar == null) {
                charPrevChar = chars[i];
                collects.add(getBitmapAtIndex(size - 1 - i));
            } else {
                if (chars[i] == charPrevChar) {
                    collects.add(getBitmapAtIndex(size - 1 - i));
                } else if (chars[i] != charPrevChar) {
                    // do merge
                    BitmapCalcUnit calc = makeRaw(charPrevChar, collects);
                    list.add(calc);
                    collects = new LinkedList<>();
                    charPrevChar = chars[i];
                    collects.add(getBitmapAtIndex(size - 1 - i));
                }
                if (i == chars.length - 1) {
                    // do merge
                    list.add(makeRaw(charPrevChar, collects));
                }
            }

        }
        return ltMergeList(list).ltBitmap;
    }

    private BitmapCalcUnit makeRaw(char flag, List<BitmapUnit> collects) {
        BitmapCalcUnit ret = new BitmapCalcUnit();
        BitmapUnit ltBitmap = null;
        BitmapUnit eqBitmap = null;
        if (flag == '0') {
            ltBitmap = Bitmaps.newEmptyBitmapUnit();
            eqBitmap = collects.get(0).copy();
            for (int i = 1; i < collects.size(); i++) {
                eqBitmap.and(collects.get(i));
            }
        } else {
            ltBitmap = collects.get(0).copy();
            for (int i = 1; i < collects.size(); i++) {
                ltBitmap.or(collects.get(i));
            }
            eqBitmap = Bitmaps.unitAndNot(notNullBitmap, ltBitmap);
        }
        ret.ltBitmap = ltBitmap;
        ret.eqBitmap = eqBitmap;
        return ret;
    }

    private BitmapCalcUnit ltMergeList(List<BitmapCalcUnit> bitmapList) {
        if (bitmapList.size() <= 0) {
            return null;
        } else if (bitmapList.size() == 1) {
            return bitmapList.get(0);
        } else {
            List<BitmapCalcUnit> merge = new ArrayList<>();
            BitmapCalcUnit odd = null;
            BitmapCalcUnit even = null;
            boolean isEven = ((bitmapList.size() & 1) == 0);
            for (int i = 0; isEven ? i < bitmapList.size() : i < bitmapList.size() - 1; i++) {

                boolean indexIsOdd = ((i & 1) == 0);
                if (indexIsOdd) {
                    odd = bitmapList.get(i);
                } else {
                    even = bitmapList.get(i);
                }
                if (odd != null && even != null) {
                    merge.add(ltMerge(odd, even, notNullBitmap));
                    odd = null;
                    even = null;
                }
            }
            if (!isEven) {
                merge.add(bitmapList.get(bitmapList.size() - 1));
            }
            return ltMergeList(merge);
        }
    }

    private BitmapCalcUnit ltMerge(BitmapCalcUnit unit1, BitmapCalcUnit unit2, BitmapUnit allBitmap) {
        BitmapCalcUnit unit = new BitmapCalcUnit();
        unit.eqBitmap = Bitmaps.unitAnd(unit1.eqBitmap, unit2.eqBitmap);
        unit.ltBitmap = Bitmaps.unitOr(unit1.ltBitmap, Bitmaps.unitAnd(unit1.eqBitmap, unit2.ltBitmap));
        return unit;
    }


}
