package org.sparkling.bitmap.core;

public interface BitmapLoader<T> {
    
    T load(String bitmapName);

}
