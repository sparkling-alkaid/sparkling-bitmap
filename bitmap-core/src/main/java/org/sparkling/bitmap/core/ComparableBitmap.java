package org.sparkling.bitmap.core;

public interface ComparableBitmap<T, CALC extends CalculatableBitmap> extends IBitmap{

    Iterable<Long> gte(long value);

    Iterable<Long> gt(long value);

    Iterable<Long> eq(long value);

    Iterable<Long> lt(long value);

    Iterable<Long> lte(long value);

    CALC gteAsCalc(long value);

    CALC gtAsCalc(long value);

    CALC eqAsCalc(long value);

    CALC ltAsCalc(long value);

    CALC lteAsCalc(long value);


}
