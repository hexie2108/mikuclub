package org.mikuclub.app.utils.http;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * @author HQnull
 * @date 2015/4/14
 * source: https://blog.csdn.net/hqocshheqing/article/details/47609023
 *
 * 实现图片缓存接口 的 自定义图片缓存策略
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


