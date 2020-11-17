package com.glab.bitmap.test;


import com.google.common.collect.Lists;
import org.junit.Test;
import org.roaringbitmap.RoaringBitmap;
import org.sparkling.bitmap.core.impl.BitmapOneHot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class BitmapTest {

    public static String PARENT_DIR = "./test-case";

    @Test
    public void cleanAll() throws Exception{
        File dir = new File(PARENT_DIR);
        if(!dir.exists()){
            dir.mkdirs();
        }
        Files.delete(dir.toPath());
    }

    @Test
    public void testRoaringBitmap(){
        RoaringBitmap bitmap = new RoaringBitmap();
        RoaringBitmap bitmap2 = new RoaringBitmap();
        bitmap.add(3215,23454,21,324);
        bitmap2.add(3215,23454,24,324);
        bitmap.and(bitmap2);
        System.out.println(bitmap);
    }

    @Test
    public void testBitmapOneHot(){
        BitmapOneHot oneHot = new BitmapOneHot();
        int[] indexList = new int[]{3215,23454,21,324};
        oneHot.add(indexList);
        BitmapOneHot oneHot2 = new BitmapOneHot();
        int[] indexList2 = new int[]{3215,23454,24,324};
        oneHot2.add(indexList2);
        oneHot.and(oneHot2);
        System.out.println(Lists.newArrayList(oneHot));
    }

    @Test
    public void testBitmapOneHotClone() throws Exception{
        BitmapOneHot oneHot = new BitmapOneHot();
        int[] indexList = new int[]{3215,23454,21,324};
        oneHot.add(indexList);
        BitmapOneHot oneHotCloned = oneHot.clone();
        oneHotCloned.add(0);
        System.out.println(Lists.newArrayList(oneHotCloned));
        System.out.println(Lists.newArrayList(oneHot));
    }

    @Test
    public void testBitmapOneHotDump() throws Exception{
        BitmapOneHot oneHot = new BitmapOneHot();
        int[] indexList = new int[]{3215,23454,21,324};
        oneHot.add(indexList);
        File file = createNewFile(PARENT_DIR, "one_hot_test.bitmap");
        oneHot.dump(new FileOutputStream(file));

        BitmapOneHot input = new BitmapOneHot();
        input.load(new FileInputStream(file));

        System.out.println(Lists.newArrayList(input));

    }


    public static void createDir(String parent, String dirPath){
        File dir = new File(parent, dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public static void createDir(String dirPath){
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public static File createNewFile(String parent, String fileName) throws IOException {
        createDir(parent);
        File file = new File(parent, fileName);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        return file;
    }



}
