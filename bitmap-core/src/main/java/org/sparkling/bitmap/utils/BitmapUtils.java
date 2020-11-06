package org.sparkling.bitmap.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtils {

    public static byte[] convertBitmapType(int type) {
        byte[] ret = new byte[2];
        ret[0] = (byte) (type & 0xff);
        ret[1] = (byte) ((type & 0xff00) >> 8);
        return ret;
    }

    public static Integer parseBitmapType(byte[] type) {
        if (type == null || type.length != 2) {
            return null;
        }
        int ret = 0;
        ret = (ret | type[1]) << 8;
        ret = ret | type[0];
        return ret;
    }

    public static OutputStream bitmapDumpOutStream(OutputStream parent){
        return new NonCloseInternalOS(parent);
    }

    public static InputStream bitmapLoadInStream(InputStream parent){
        return new NonCloseInternalIS(parent);
    }


    public static class NonCloseInternalIS extends InputStream implements AutoCloseable{

        private InputStream parent;

        public NonCloseInternalIS(InputStream parent) {
            this.parent = parent;
        }

        @Override
        public int read() throws IOException {
            return parent.read();
        }

        @Override
        public void close() throws IOException {
            super.close();
            parent = null;
        }

    }

    private static class NonCloseInternalOS extends OutputStream implements AutoCloseable{

        private OutputStream parent;

        public NonCloseInternalOS(OutputStream parent) {
            this.parent = parent;
        }

        @Override
        public void write(int b) throws IOException {
            parent.write(b);
        }

        @Override
        public void close() throws IOException {
            super.close();
            parent.flush();
            parent = null;
        }

    }

}
