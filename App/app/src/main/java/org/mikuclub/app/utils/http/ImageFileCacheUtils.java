package org.mikuclub.app.utils.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.utils.LogUtils;


/**
 * @author HQnull
 * @date 2015/4/14
 * source: https://blog.csdn.net/hqocshheqing/article/details/47609023
 * <p>
 * the cache system to storage the img file in external storage
 */
public class ImageFileCacheUtils
{
        //cache dir
        private static final String CACHE_DIR = "image_cache";
        //cache file extension
        private static final String CACHE_FILE_EXTENSION = ".cache";
        //the max size of cache, if exceeds it will remove the least recently used cache file
        private static final int CACHE_MAX_SIZE = GlobalConfig.CACHE_MAX_SIZE;
        //缓存所需SD卡所剩的最小容量 必须 大于缓存大小1.5倍
        private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = CACHE_MAX_SIZE + (CACHE_MAX_SIZE / 2);

        private static final int MB = 1024 * 1024;

        private static ImageFileCacheUtils instance = null;

        private boolean activeCache;

        //Singleton 模式
        public static ImageFileCacheUtils getInstance()
        {
                if (instance == null)
                {
                        synchronized (ImageFileCacheUtils.class)
                        {
                                if (instance == null)
                                {
                                        instance = new ImageFileCacheUtils();
                                }
                        }
                }
                return instance;
        }

        /**
         * 构建函数
         */
        private ImageFileCacheUtils()
        {
                this.activeCache = checkCacheStatus(getDirectory());
        }


        /**
         * 从缓存里获取图片
         *
         * @param url
         * @return Bitmap image if file exists, null if file isn't exist
         */
        public Bitmap getImage(final String url)
        {

                final String path = getDirectory() + File.separator + convertUrlToFileName(url);
                Bitmap output = null;

                File file = new File(path);
                //如果文件存在
                if (file.exists())
                {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        //如果无法转换则删除文件
                        if (bitmap == null)
                        {
                                file.delete();
                        }
                        else
                        {
                                //更新文件的最后修改时间
                                updateFileTime(path);
                                output = bitmap;
                                LogUtils.v("图片从缓存中读取成功: "+file.getPath());
                        }
                }

                return output;
        }


        /**
         * 保存图片到缓存
         *
         * @param bitmap
         * @param url
         */
        public void saveBitmap(Bitmap bitmap, String url)
        {
                //如果图片未空 或者 缓存图片功能已关闭
                if (bitmap == null || activeCache == false)
                {
                        return;
                }
                //如果剩余存储空间不足
                if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd())
                {
                        //关闭缓存功能
                        activeCache = false;
                        return;
                }

                //截取文件名称
                String fileName = convertUrlToFileName(url);
                //创建保存图片
                File file = new File(getDirectory() + File.separator + fileName);
                try
                {
                        file.createNewFile();
                        OutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        LogUtils.v("图片文件保存成功: "+file.getPath());
                }
                catch (IOException e)
                {
                        LogUtils.w("图片文件创建错误");
                        e.printStackTrace();
                }
        }


        /**
         * 获得图片缓存文件夹路径
         */
        private String getDirectory()
        {

                String imageCachePath;
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                {
                        //外部存储可用
                        //获取外部空间地址
                        imageCachePath = MyApplication.getContext().getExternalCacheDir().getPath();
                }
                else
                {
                        //外部存储不可用
                        //获取内部空间地址
                        imageCachePath = MyApplication.getContext().getExternalCacheDir().getPath();
                }

                //获取文件夹
                File dir = new File(imageCachePath);
                //如果文件夹还不存在
                if (!dir.exists())
                {
                        //创建文件夹
                        boolean success = dir.mkdir();
                        //如果创建失败
                        if (!success)
                        {
                                LogUtils.w("文件夹创建错误");
                        }
                }

                return imageCachePath;
        }

        /**
         * 更新目前文件的最后修改日期
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
         * 计算设备剩余的可用空间
         */
        private int freeSpaceOnSd()
        {
                StatFs statFs = new StatFs(MyApplication.getContext().getExternalFilesDir(null).getPath());
                double sdFreeMB = ((double) statFs.getAvailableBlocksLong() * (double) statFs.getBlockSizeLong()) / MB;
                LogUtils.v("储存剩余空间为：" + sdFreeMB);
                return (int) sdFreeMB;
        }

        /**
         * 转换url地址成对应的缓存文件名
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
         * 检测可用空间, 来开启或关闭 图片缓存功能
         * 如空间不足则会删除50%的文件
         * <p>
         * Calculate the file size in the storage directory
         *  When the total file size is larger than the specified size or the remaining space of the sd card is less than the limit of FREE_SD_SPACE_NEEDED_TO_CACHE
         *   delete 50% of files that have not been used recently
         *
         * @param dirPath
         * @return
         */
        private boolean checkCacheStatus(String dirPath)
        {
                File dirFile = new File(dirPath);
                File[] files = dirFile.listFiles();

                //如果文件夹不存在
                if (files == null || files.length <= 0)
                {
                        return true;
                }

                //遍历文件夹内的所有文件
                //计算总大小
                int dirSize = 0;
                for (int i = 0; i < files.length; i++)
                {
                        if (files[i].getName().contains(CACHE_FILE_EXTENSION))
                        {
                                dirSize += files[i].length();
                        }
                }

                //如果实际大小超过缓存上限大小 或者 允许缓存的最小容量 低于 设备空余空间
                if (dirSize > CACHE_MAX_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd())
                {
                        //计算需要删除的文件数量 (50%文件)
                        int removeFactor = (int) ((0.5 * files.length) + 1);
                        //根据文件最后修改时间 来排序数组
                        Arrays.sort(files, new FileLastModifySoft());
                        //遍历删除最老的文件
                        for (int i = 0; i < removeFactor; i++)
                        {
                                if (files[i].getName().contains(CACHE_FILE_EXTENSION))
                                {
                                        files[i].delete();
                                }
                        }
                }

                //重新获取设备可用空间
                //如果还是低于最低可缓存大小
                if (CACHE_MAX_SIZE >= freeSpaceOnSd() )
                {
                        //返回false
                        return false;
                }

                return true;
        }

        /**
         * 自定义文件对比器 (按照最后修改时间 来对比文件)
         */
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
