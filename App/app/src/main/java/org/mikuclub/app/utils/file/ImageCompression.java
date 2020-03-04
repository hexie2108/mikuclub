package org.mikuclub.app.utils.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.mikuclub.app.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageCompression
{
        // 图片的最大大小
        private static final int MAX_WIDTH = 1920, MAX_HEIGHT = 1920;

        /**
         * 压缩生成新的图片文件
         * 不管图片大小 都会生成一个新的缓存文件 ,
         * 避免文件名不规范乱码导致的服务器识别错误
         *
         * @param context
         * @param fileUri
         * @return
         */
        public static File compressFileIfTooLarge(Context context, Uri fileUri)
        {

                File outputFile;

                LogUtils.v("创建压缩后的图片");

                try
                {
                        //先创建一个bitmap参数 来获取图片文件的信息
                        // BitmapFactory options to downsize the image
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        o.inSampleSize = 1;

                       InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
                        //Bitmap selectedBitmap = null;
                        BitmapFactory.decodeStream(inputStream, null, o);
                        //关闭输入流
                       inputStream.close();

                        //只有在图片 超过 最大尺寸两倍的情况下 就进行数据层面的缩放 节省内存, 图片实际大小不会变
                        // Find the correct scale value. It should be the power of 2.
                        int scale = 1;
                        while (o.outWidth / scale / 2 >= MAX_WIDTH &&
                                o.outHeight / scale / 2 >= MAX_HEIGHT)
                        {
                                scale *= 2;
                        }

                        //设置新图片的缩放比例参数
                        o.inJustDecodeBounds = false;
                        o.inSampleSize = scale;
                        //重新打开新的输入流
                        inputStream = context.getContentResolver().openInputStream(fileUri);

                        //创建成压缩后的图片
                        Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o);
                        //关闭输入流
                        inputStream.close();

                        //如果超过规定大小 就缩放图片
                        selectedBitmap = scaleBitmapIfTooLarge(selectedBitmap);

                        //创建缓存文件
                        outputFile = FileUtils.createNewCacheFile(context);
                        //开启缓存文件的输出流
                        FileOutputStream outputStream = new FileOutputStream(outputFile);

                        //把图片压缩成JPEG写入缓存文件
                       selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                        //关闭输出流
                        outputStream.close();

                        //如果文件是空的, 则把输出改为null
                        if(outputFile.length()==0){
                                outputFile=null;
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                        return null;
                }

                return outputFile;
        }

        /**
         * 如果图片太大就 缩小图片
         */
        private static Bitmap scaleBitmapIfTooLarge(Bitmap bitmap)
        {
                Bitmap outputBitmap = bitmap;
                //如果图片不是null
                if (bitmap != null)
                {
                        // 获取新图片的大小
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();

                        //如果图片宽或高超过了规定的最大尺寸
                        if (width > MAX_WIDTH || height > MAX_HEIGHT)
                        {

                                //宽高比
                                double ration;
                                //如果宽比高 大
                                if (width >= height)
                                {
                                        //计算需要缩放的比例
                                        ration = ((double) width) / MAX_WIDTH;
                                        //缩放高度
                                        height = (int) (height / ration);
                                        width = MAX_WIDTH;

                                }
                                //如果高比宽 大
                                else
                                {
                                        //计算需要缩放的比例
                                        ration = ((double) height) / MAX_HEIGHT;
                                        //缩放宽度
                                        width = (int) (width / ration);
                                        height = MAX_HEIGHT;
                                }
                                //生成新的缩放图片
                                outputBitmap = Bitmap.createScaledBitmap(bitmap, width,
                                        height, true);
                                //清理旧图片
                                bitmap.recycle();

                                LogUtils.v("已缩小图片, 新图宽 " + width + " 高 " + height);
                        }

                }

                return outputBitmap;


        }


        public static Bitmap getThumbnail(Uri uri, Context context) throws FileNotFoundException, IOException
        {
                InputStream input = context.getContentResolver().openInputStream(uri);

                BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
                onlyBoundsOptions.inJustDecodeBounds = true;
                onlyBoundsOptions.inDither = true;//optional
                onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
                BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
                input.close();
                if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
                {
                        return null;
                }
                int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

                double ratio = (originalSize > 500) ? (originalSize / 500) : 1.0;

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
                bitmapOptions.inDither = true;//optional
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
                input = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                input.close();
                return bitmap;
        }

        private static int getPowerOfTwoForSampleRatio(double ratio)
        {
                int k = Integer.highestOneBit((int) Math.floor(ratio));
                if (k == 0)
                {
                        return 1;
                }
                else
                {
                        return k;
                }
        }


}
