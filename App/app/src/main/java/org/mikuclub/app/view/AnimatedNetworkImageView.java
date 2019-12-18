package org.mikuclub.app.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


/**
 * 有淡入特效的 NetworkImageView
 * NetworkImageView that fades-in the drawable upon complete download
 * @author  whlk
 * source https://stackoverflow.com/questions/16778274/android-volley-how-to-animate-image-loading
 */
public class AnimatedNetworkImageView extends NetworkImageView
{
        private static final int ANIM_DURATION = 500;
        private boolean shouldAnimate = true;

        public AnimatedNetworkImageView(Context context)
        {
                super(context);
                init();
        }

        public AnimatedNetworkImageView(Context context, AttributeSet attrs)
        {
                super(context, attrs);
                init();
        }

        public AnimatedNetworkImageView(Context context, AttributeSet attrs, int defStyle)
        {
                super(context, attrs, defStyle);
                init();
        }

        private void init()
        {
                // animate by default. Only when {@link #determineIfAnimationIsNecessary} is called animation is dependent upon cache status
                shouldAnimate = true;
        }

        @Override
        public void setImageBitmap(Bitmap bm)
        {
                super.setImageBitmap(bm);
                if (shouldAnimate)
                {
                        // your animation. Here with ObjectAnimator for example
                        ObjectAnimator.ofFloat(this, "alpha", 0, 1).setDuration(ANIM_DURATION).start();
                }
        }

        @Override
        public void setImageUrl(String url, ImageLoader imageLoader)
        {
                shouldAnimate = determineIfAnimationIsNecessary(url, imageLoader);
                super.setImageUrl(url, imageLoader);
        }

        /**
         * checks if for the given imgUrl and imageLoader the view should animate when a bitmap is set.
         * If this method is called before {@link NetworkImageView#setImageUrl(String, ImageLoader)} is called the view would not be animated if the image comes from the cache.
         *
         * @param imgUrl      the image url
         * @param imageLoader the image loader
         */
        public boolean determineIfAnimationIsNecessary(String imgUrl, ImageLoader imageLoader)
        {
                int width = getWidth();
                int height = getHeight();
                ScaleType scaleType = getScaleType();

                boolean wrapWidth = false, wrapHeight = false;
                if (getLayoutParams() != null)
                {
                        wrapWidth = getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
                        wrapHeight = getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT;
                }

                // Calculate the max image width / height to use while ignoring WRAP_CONTENT dimens.
                int maxWidth = wrapWidth ? 0 : width;
                int maxHeight = wrapHeight ? 0 : height;

                return !imageLoader.isCached(imgUrl, maxWidth, maxHeight, scaleType);
        }

}