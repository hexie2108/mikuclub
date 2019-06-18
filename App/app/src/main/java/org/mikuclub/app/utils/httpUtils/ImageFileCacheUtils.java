package org.mikuclub.app.utils.httpUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.mikuclub.app.configs.GlobalConfig;


/**
 * @author HQnull
 * @date 2015/4/14
 * source: https://blog.csdn.net/hqocshheqing/article/details/47609023
 *
 * the cache system to storage the img file in external storage

 */
public class ImageFileCacheUtils 
{
        //cache dir
        private static final String CACHE_DIR = "image_cache";
        //cache file extension
        private static final String CACHE_FILE_EXTENSION = ".cache";
        private static final int MB = 1024 * 1024;
        //the max size of cache, if exceeds it will remove the least recently used cache file
        private static final int CACHE_MAX_SIZE = GlobalConfig.CACHE_MAX_SIZE;
        private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = CACHE_MAX_SIZE + (CACHE_MAX_SIZE / 2);//缓存所需SD卡所剩的最小容量

        private static ImageFileCacheUtils  instance = null;

        //Singleton mode
        public static ImageFileCacheUtils  getInstance()
        {
                if (instance == null)
                {
                        synchronized (ImageFileCacheUtils .class)
                        {
                                if (instance == null)
                                {
                                        instance = new ImageFileCacheUtils ();
                                }
                        }
                }
                return instance;
        }

        /**
         * construct method
         */
        private ImageFileCacheUtils ()
        {
                removeCache(getDirectory());
        }

        /**
         * get image file from cache
         *
         * @param url
         * @return Bitmap image if file exists, null if file isn't exist
         */
        public Bitmap getImage(final String url)
        {
                final String path = getDirectory() + "/" + convertUrlToFileName(url);
                File file = new File(path);
                //if file exists
                if (file.exists())
                {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        if (bitmap == null)
                        {
                                file.delete();
                                return null;
                        }
                        else
                        {
                                //update the last modified time of file
                                updateFileTime(path);
                                return bitmap;
                        }
                }
                else
                {
                        return null;
                }
        }

        /**
         * get cache directory
         */
        private String getDirectory()
        {
                return getSDPath() + "/" + CACHE_DIR;
        }

        /**
         * get  the external storage path
         */
        private String getSDPath()
        {
                File sdDir = null;
                //check the status of external storage
                boolean adCardExit = Environment.getExternalStorageState().endsWith(Environment.MEDIA_MOUNTED);
                if (adCardExit)
                {
                        //get root path
                        sdDir = Environment.getExternalStorageDirectory();
                }
                if (sdDir != null)
                {
                        return sdDir.toString();
                }
                else
                {
                        return "";
                }
        }

        /**
         * update the last modified time of file
         *
         * @param path
         */
        private void updateFileTime(String path)
        {
                File file = new File(path);
                long newModeifyTime = System.currentTimeMillis();
                file.setLastModified(newModeifyTime);
        }

        /**
         * save the image
         *
         * @param bitmap
         * @param url
         */
        public void saveBitmap(Bitmap bitmap, String url)
        {
                if (bitmap == null)
                {
                        return;
                }
                //if the free space is so low
                if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd())
                {
                        return;
                }

                String fileName = convertUrlToFileName(url);
                String dir = getDirectory();
                File dirFile = new File(dir);
                if (!dirFile.exists())
                {
                        dirFile.mkdirs();
                }
                File file = new File(dir + "/" + fileName);
                try
                {
                        file.createNewFile();
                        OutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                        outputStream.flush();
                        outputStream.close();
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
        }

        /**
         * calculate the free space of external storage
         */
        private int freeSpaceOnSd()
        {
                StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                double sdFreeMB = ((double) statFs.getAvailableBlocksLong() * (double) statFs.getBlockSizeLong()) / MB;
                Log.i("test", "剩余空间为：" + sdFreeMB);
                return (int) sdFreeMB;
        }

        /**
         * convert the url to fileName
         *
         * @param url
         * @return
         */
        private String convertUrlToFileName(String url)
        {
                String[] strs = url.split("/");
                return strs[strs.length - 1] + CACHE_FILE_EXTENSION;
        }

        /**
         * Calculate the file size in the storage directory
         *  When the total file size is larger than the specified size or the remaining space of the sd card is less than the limit of FREE_SD_SPACE_NEEDED_TO_CACHE
         *   delete 50% of files that have not been used recently
         *
         * @param dirPath
         * @return
         */
        private boolean removeCache(String dirPath)
        {
                File dirFile = new File(dirPath);
                File[] files = dirFile.listFiles();
                if (files == null || files.length <= 0)
                {
                        return true;
                }
                //if it haven't external storage
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                        return false;
                }
                int dirSize = 0;
                for (int i = 0; i < files.length; i++)
                {
                        if (files[i].getName().contains(CACHE_FILE_EXTENSION))
                        {
                                dirSize += files[i].length();
                        }
                }
                if (dirSize > CACHE_MAX_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd())
                {
                        int removeFactor = (int) ((0.5 * files.length) + 1);
                        Arrays.sort(files, new FileLastModifySoft());
                        for (int i = 0; i < removeFactor; i++)
                        {
                                if (files[i].getName().contains(CACHE_FILE_EXTENSION))
                                {
                                        files[i].delete();
                                }
                        }
                }
                if (freeSpaceOnSd() <= CACHE_MAX_SIZE)
                {
                        return false;
                }
                return true;
        }

        //custom comparator to sort the file
        private class FileLastModifySoft implements Comparator<File>
        {
                @Override
                public int compare(File arg0, File arg1)
                {
                        if (arg0.lastModified() > arg1.lastModified())
                        {
                                return 1;
                        }
                        else if (arg0.lastModified() == arg1.lastModified())
                        {
                                return 0;
                        }
                        else
                        {
                                return -1;
                        }
                }
        }
}
