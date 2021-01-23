package org.mikuclub.app.utils.file;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.yalantis.ucrop.UCrop;

import org.mikuclub.app.config.GlobalConfig;

import androidx.appcompat.app.AppCompatActivity;
import mikuclub.app.R;

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

        /**
         * 裁剪图片
         * 输入图片URI 裁剪完成, 返回对应的URI输出地址
         * @param activity
         * @param imageUri
         * @param outputUri
         */
        public static void startActionForResultToCropImageWithUCrop(AppCompatActivity activity, Uri imageUri, Uri outputUri)
        {
                UCrop.Options options = new UCrop.Options();
                // 修改标题栏颜色
                options.setToolbarColor(activity.getResources().getColor(R.color.defaultMikuBackground));
                // 修改状态栏颜色
                options.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));



                // 隐藏底部工具
                options.setHideBottomControls(true);
                // 图片格式
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                // 设置图片压缩质量
                options.setCompressionQuality(90);

                // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
                // 如果不开启，用户不能拖动选框，只能缩放图片

                options.setFreeStyleCropEnabled(false);

                // 圆
                options.setCircleDimmedLayer(true);
                // 不显示网格线
                options.setShowCropGrid(false);

                // 设置源uri及目标uri
                UCrop.of(imageUri, outputUri)
                        // 长宽比
                        .withAspectRatio(1, 1)
                        // 图片大小
                        .withMaxResultSize(GlobalConfig.USER_AVATAR_SIZE, GlobalConfig.USER_AVATAR_SIZE)
                        // 配置参数
                        .withOptions(options)
                        .start(activity);

        }


}
