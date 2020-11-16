package org.sparkling.bitmap.core;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

public interface BitmapLike extends Serializable, Cloneable, Iterable<Long> {

    void dump(OutputStream outputStream) throws IOException;

    void load(InputStream inputStream) throws IOException;

    void add(int index);

    void add(int[] index);

    boolean contains(int index);

    long cardinality();

    default List<Long> getIndexList(int limit){
        return Lists.newArrayList(Iterables.limit(this, limit));
    }

}
