package org.mikuclub.app.utils.httpUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;

import org.mikuclub.app.configs.GlobalConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author HQnull
 * @date 2015/4/14
 * source: https://blog.csdn.net/hqocshheqing/article/details/47609023
 *
 * classe personalizzato che sostituisce la politica  di cache default per immagini remoti
 */
public class ImageFileCache implements ImageLoader.ImageCache
{

        @Override
        public Bitmap getBitmap(String url) {
                return ImageFileCacheUtils.getInstance().getImage(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap)
        {
                ImageFileCacheUtils.getInstance().saveBitmap(bitmap, url);
        }


}


