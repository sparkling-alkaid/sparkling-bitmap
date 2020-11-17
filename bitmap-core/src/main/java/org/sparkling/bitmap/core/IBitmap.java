package org.sparkling.bitmap.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IBitmap extends BitmapLike, Named{

    /**
     * 仅序列话bitmap内部存储结构，序列化结构
     * type（两字节）+ type的自定义序列化结构
     * @param outputStream
     * @throws IOException
     */
    void dump(OutputStream outputStream) throws IOException;

    void load(InputStream inputStream) throws IOException;

    Integer bitmapType();


}
