package com.glab.bitmap.test;


import com.google.common.collect.Lists;
import org.junit.Test;
import org.roaringbitmap.RoaringBitmap;
import org.sparkling.bitmap.core.BitmapConst;
import org.sparkling.bitmap.core.SegBitmapLoader;
import org.sparkling.bitmap.core.impl.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

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


    private BitmapBitwise newBitmapBitwize(){
        BitmapBitwise b = new BitmapBitwise(10);
        b.add(1, 10);
        b.add(2, 20);
        b.add(30, 300);
        b.add(4, 40);
        b.add(50, 500);
        return b;
    }

    @Test
    public void testBitmapBitwize(){
        BitmapBitwise b = newBitmapBitwize();
        System.out.println(b.cardinality());
        System.out.println(b.lt(31));
        System.out.println(b.eq(10));
        System.out.println();
    }


    @Test
    public void testBitmapBitwizeDump() throws Exception{
        BitmapBitwise b = newBitmapBitwize();
        File file = createNewFile(PARENT_DIR, "bitwize_test.bitmap");
        b.dump(new FileOutputStream(file));

        BitmapBitwise load = new BitmapBitwise();
        load.load(new FileInputStream(file));

        System.out.println(load.cardinality());
        System.out.println(load.lt(31));
        System.out.println(load.eq(10));
        System.out.println();
    }


    @Test
    public void testSegmentedBitmapOneHot(){
        int[] indexList = new int[]{12,13};
        int[] indexList2 = new int[]{223,324};
        BitmapOneHot oneHot = new BitmapOneHot();
        oneHot.add(indexList);
        BitmapOneHot oneHot2 = new BitmapOneHot();
        oneHot2.add(indexList2);
        Map<Integer, BitmapOneHot> segMap = new HashMap<>();
        segMap.put(0, oneHot);
        segMap.put(1, oneHot2);
        BaseSegmentBitmap<BitmapOneHot> ret = new BaseSegmentBitmap<BitmapOneHot>("one-hot-seg-bitmap", BitmapConst.BITMAP_TYPE_ONEHOT, 2, 1000L, segMap);
        ret.add(1238);
        System.out.println(Lists.newArrayList(ret));
        System.out.println(ret.getIndexList(3));
        System.out.println();

    }


    @Test
    public void testSegmentedBitmapBitwise(){
        Map<Integer, BitmapBitwise> segMap = new HashMap<>();
        segMap.put(0, newBitmapBitwize());
        segMap.put(1, newBitmapBitwize());
        LoadableComparableSegmentBitmap b = new LoadableComparableSegmentBitmap("bit-wise-seg-bitmap", BitmapConst.BITMAP_TYPE_ONEHOT, 2, 1000L, new SegBitmapLoader<BitmapBitwise>() {
            @Override
            public BitmapBitwise loadSegment(String name, Integer segIndex) {
                return segMap.get(segIndex);
            }

            @Override
            public BitmapBitwise load(String bitmapName) {
                return null;
            }
        });

        System.out.println(Lists.newArrayList(b.lt(1001L)));
        System.out.println();

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
