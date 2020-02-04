package org.mikuclub.app.utils.file;

import android.app.Activity;
import android.content.Intent;

/**
 * 启动intent 获取本地图片 和 处理本地图片
 */
public class LocalResourceIntent
{
        public static final int requestCodeToGetImage = 0;
        public static final int requestCodeToCropImage = 1;

        /**
         * 从设备里获取图片类型文件
         *
         * @param activity
         */
        public static void startActionForResultToGetImage(Activity activity)
        {
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);

                // 设置文件类型
                intent.setType("image/*");
                activity.startActivityForResult(intent, requestCodeToGetImage);
        }

        /**
         * 裁剪图片
         * 输入图片URI 裁剪完成, 返回对应的URI输出地址
         */
        public static void startActionForResultToCropImage(Activity activity, ImageCropBean cropBean)
        {

                Intent intent = new Intent("com.android.camera.action.CROP");
                //设置要裁剪的图片Uri
                intent.setDataAndType(cropBean.getDataUri(), "image/*");
                //授予裁剪应用 读取和写入uri文件的权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                //配置一系列裁剪参数
                intent.putExtra("outputX", cropBean.getOutputX());
                intent.putExtra("outputY", cropBean.getOutputY());
                intent.putExtra("scale", cropBean.isScale());
                intent.putExtra("aspectX", cropBean.getAspectX());
                intent.putExtra("aspectY", cropBean.getAspectY());
                intent.putExtra("outputFormat", cropBean.getOutputFormat());
                intent.putExtra("return-data", cropBean.isReturnData());
                intent.putExtra("output", cropBean.getSaveUri());
                intent.putExtra("circleCrop", cropBean.getCircleCrop());
                intent.putExtra("noFaceDetection", true);
                //跳转
                activity.startActivityForResult(intent, requestCodeToCropImage);
        }


}
