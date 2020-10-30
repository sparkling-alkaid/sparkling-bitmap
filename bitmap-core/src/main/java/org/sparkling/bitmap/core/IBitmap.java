package org.sparkling.bitmap.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public interface IBitmap extends Serializable {

    String name();

    OutputStream dump() throws IOException;

    void load(InputStream inputStream) throws IOException;

    void add(int index);

    void add(int[] index);

    void or(IBitmap iBitmap);

    void not(IBitmap iBitmap);

    void and(IBitmap iBitmap);

    void andNot(IBitmap iBitmap);

    void xor(IBitmap iBitmap);

}
