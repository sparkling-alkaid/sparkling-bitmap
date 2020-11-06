package org.sparkling.bitmap.utils;

import org.roaringbitmap.RoaringBitmap;
import org.sparkling.bitmap.core.BitmapUnit;
import org.sparkling.bitmap.core.impl.BitmapUnitImpl;

public class Bitmaps {

    public static BitmapUnit newEmptyBitmapUnit(){
        return new BitmapUnitImpl();
    }

    public static BitmapUnit unitOr(BitmapUnit unit1, BitmapUnit unit2){
        return new BitmapUnitImpl(RoaringBitmap.or(unit1.internal(), unit2.internal()));
    }

    public static BitmapUnit unitAnd(BitmapUnit unit1, BitmapUnit unit2){
        return new BitmapUnitImpl(RoaringBitmap.and(unit1.internal(), unit2.internal()));
    }

    public static BitmapUnit unitXor(BitmapUnit unit1, BitmapUnit unit2){
        return new BitmapUnitImpl(RoaringBitmap.xor(unit1.internal(), unit2.internal()));
    }

    public static BitmapUnit unitAndNot(BitmapUnit unit1, BitmapUnit unit2){
        return new BitmapUnitImpl(RoaringBitmap.andNot(unit1.internal(), unit2.internal()));
    }


}
