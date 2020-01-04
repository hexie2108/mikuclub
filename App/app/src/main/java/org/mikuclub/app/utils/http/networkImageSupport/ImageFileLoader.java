package org.mikuclub.app.utils.http.networkImageSupport;

import com.android.volley.toolbox.ImageLoader;

import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.utils.http.RequestQueue;

/**
 * 获取图片加载器实例
 */
public class ImageFileLoader
{

        private static ImageLoader imageLoader;
        /**
         * 获取远程图片加载器
         * @return
         */
        public static ImageLoader getImageLoader()
        {

                if (imageLoader == null)
                {
                        imageLoader = new ImageLoader(RequestQueue.getInstance(MyApplication.getContext()).getRequestQueue(), new ImageFileCache());
                }

                return imageLoader;
        }
}
