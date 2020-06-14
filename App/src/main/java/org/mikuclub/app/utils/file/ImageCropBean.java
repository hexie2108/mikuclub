package org.mikuclub.app.utils.file;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;

/**
 * 裁剪图片需要用到的参数类
 */

public class ImageCropBean
{

        //要裁剪的图片Uri
        private Uri dataUri;

        //裁剪宽度px
        private int outputX;
        //裁剪高度px
        private int outputY;

        //宽度比例
        private int aspectX;
        //高度比例
        private int aspectY;

        //是否保留比例
        private boolean scale;

        //是否将数据保存在Bitmap中返回
        private boolean isReturnData;
        //相应的Bitmap数据
        private Parcelable returnData;

        //如果不需要将图片在Bitmap中返回，需要传递保存图片的Uri
        private Uri saveUri;

        //圆形裁剪区域
        private String circleCrop;

        //图片输出格式，默认JPEG
        private String outputFormat = Bitmap.CompressFormat.JPEG.toString();

        /**
         * 根据宽高计算裁剪比例
         */
        public void calculateAspect()
        {

                scale = true;

                if (outputX == outputY)
                {
                        aspectX = 1;
                        aspectY = 1;
                        return;
                }
                float proportion = (float) outputX / (float) outputY;

                aspectX = (int) (proportion * 100);
                aspectY = 100;
        }

        public Uri getDataUri()
        {
                return dataUri;
        }

        public void setDataUri(Uri dataUri)
        {
                this.dataUri = dataUri;
        }

        public int getOutputX()
        {
                return outputX;
        }

        public void setOutputX(int outputX)
        {
                this.outputX = outputX;
        }

        public int getOutputY()
        {
                return outputY;
        }

        public void setOutputY(int outputY)
        {
                this.outputY = outputY;
        }

        public int getAspectX()
        {
                return aspectX;
        }

        public void setAspectX(int aspectX)
        {
                this.aspectX = aspectX;
        }

        public int getAspectY()
        {
                return aspectY;
        }

        public void setAspectY(int aspectY)
        {
                this.aspectY = aspectY;
        }

        public boolean isScale()
        {
                return scale;
        }

        public void setScale(boolean scale)
        {
                this.scale = scale;
        }

        public boolean isReturnData()
        {
                return isReturnData;
        }

        public void setReturnData(boolean returnData)
        {
                isReturnData = returnData;
        }

        public Parcelable getReturnData()
        {
                return returnData;
        }

        public void setReturnData(Parcelable returnData)
        {
                this.returnData = returnData;
        }

        public Uri getSaveUri()
        {
                return saveUri;
        }

        public void setSaveUri(Uri saveUri)
        {
                this.saveUri = saveUri;
        }

        public String getCircleCrop()
        {
                return circleCrop;
        }

        public void setCircleCrop(String circleCrop)
        {
                this.circleCrop = circleCrop;
        }

        public String getOutputFormat()
        {
                return outputFormat;
        }

        public void setOutputFormat(String outputFormat)
        {
                this.outputFormat = outputFormat;
        }

}
