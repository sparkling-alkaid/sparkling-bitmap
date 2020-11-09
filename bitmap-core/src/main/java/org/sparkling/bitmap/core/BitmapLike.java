package org.sparkling.bitmap.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public interface BitmapLike extends Serializable, Cloneable, Iterable<Long> {

    void dump(OutputStream outputStream) throws IOException;

    void load(InputStream inputStream) throws IOException;

    void add(int index);

    void add(int[] index);

    boolean contains(int index);

    int cardinality();

    int[] toArray();

}
