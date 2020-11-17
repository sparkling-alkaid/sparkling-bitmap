package org.sparkling.bitmap.utils;

import org.roaringbitmap.RoaringBitmap;
import org.sparkling.bitmap.core.BitmapUnit;
import org.sparkling.bitmap.core.impl.BitmapUnitImpl;

public class Bitmaps {

    public static BitmapUnit newEmptyBitmapUnit(){
        return new BitmapUnitImpl();
    }

    public static BitmapUnit unitOr(BitmapUnit unit1, BitmapUnit unit2){
        if(unit1.internal() instanceof RoaringBitmap){
            return new BitmapUnitImpl(RoaringBitmap.or((RoaringBitmap) unit1.internal(), (RoaringBitmap) unit2.internal()));
        }
        return newEmptyBitmapUnit();
    }

    public static BitmapUnit unitAnd(BitmapUnit unit1, BitmapUnit unit2){
        if(unit1.internal() instanceof RoaringBitmap){
            return new BitmapUnitImpl(RoaringBitmap.and((RoaringBitmap) unit1.internal(), (RoaringBitmap) unit2.internal()));
        }
        return newEmptyBitmapUnit();
    }

    public static BitmapUnit unitXor(BitmapUnit unit1, BitmapUnit unit2){
        if(unit1.internal() instanceof RoaringBitmap){
            return new BitmapUnitImpl(RoaringBitmap.xor((RoaringBitmap) unit1.internal(), (RoaringBitmap) unit2.internal()));
        }
        return newEmptyBitmapUnit();
    }

    public static BitmapUnit unitAndNot(BitmapUnit unit1, BitmapUnit unit2){
        if(unit1.internal() instanceof RoaringBitmap){
            return new BitmapUnitImpl(RoaringBitmap.andNot((RoaringBitmap) unit1.internal(), (RoaringBitmap) unit2.internal()));
        }
        return newEmptyBitmapUnit();
    }


}
