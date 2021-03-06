package org.sparkling.bitmap.core;

public interface IBitmap2D<T, RET extends CalculatableBitmap> extends ComparableBitmap<T, RET>, IBitmap{

    /**
     * 添加二维数值
     * @param index 被提数的id
     * @param value 数值性特征
     */
    void add(int index, int value);


}
