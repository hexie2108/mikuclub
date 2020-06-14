package org.mikuclub.app.utils.file;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import org.mikuclub.app.utils.LogUtils;

import java.io.File;
import java.io.IOException;

public class FileUtils
{


        /**
         * 把文件转换成对应URI
         *
         * @param file
         * @return
         */
        public static Uri getUri(File file)
        {
                Uri output = null;
                //如果文件不是空
                if (file != null)
                {
                        //把文件转换成uri
                        output = Uri.fromFile(file);
                }
                return output;
        }

        /**
         * 在缓存文件夹里创建新JPG文件
         *
         * @param context
         * @return
         */
        public static File createNewCacheFile(Context context)
        {
                //获取外部缓存文件夹
                File cacheDirectory = context.getExternalCacheDir();
                //如果不存在
                if (cacheDirectory == null)
                {
                        //获取内部缓存文件夹
                        cacheDirectory = context.getCacheDir();
                }
                //输出文件
                File outputFile = null;
                //如果缓存文件夹不是null
                if (cacheDirectory != null)
                {
                        //生成随机文件名, 后缀加上jpg
                        String fileName = System.currentTimeMillis() / 1000 + ".jpg";
                        outputFile = new File(cacheDirectory, fileName);
                }


                return outputFile;
        }

        /**
         * 把图片 保存到相册文件夹
         * 并通知系统更新
         *
         * @param context  上下文
         * @return true 保存成功 | false 保存失败
         */
        public static Uri createImageFileUri(Context context)
        {
                Uri uri = null;
                final String fileName = System.currentTimeMillis() / 1000 + "";
                final ContentResolver resolver = context.getContentResolver();

                try
                {

                        //如果是 安卓10
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        {
                                final ContentValues contentValues = new ContentValues();
                                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                                final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                                uri = resolver.insert(contentUri, contentValues);

                                if (uri == null)
                                {
                                        throw new IOException("Failed to create new MediaStore record.");
                                }
                        }
                        //如果是低版本的系统
                        else
                        {
                                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                                File file = new File(imagesDir, fileName + ".jpg");
                                //如果文件已存在
                                if (file.exists())
                                {
                                        //覆盖掉
                                        file.delete();
                                        file.createNewFile();
                                }
                                uri = getUri(file);
                        }

                        //发送广播 通知系统 已添加了新图片
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        //如果uri获取成功
                        if (uri != null)
                        {
                                //通知相册更新
                                mediaScanIntent.setData(uri);
                                context.sendBroadcast(mediaScanIntent);
                        }

                }
                catch (IOException e)
                {
                        if (uri != null)
                        {
                                // Don't leave an orphan entry in the MediaStore
                                resolver.delete(uri, null, null);
                        }
                        e.printStackTrace();
                }


                return uri;

        }


        /**
         * 清空缓存文件夹
         * 在退出应用时调用
         */
        public static void clearCacheDirectory(Context context)
        {
                //获取缓存文件夹
                File cacheDirectory = context.getExternalCacheDir();
                if (cacheDirectory != null)
                {
                        //循环清空缓存文件夹
                        deleteFileRecursion(cacheDirectory.listFiles());
                }
                //如果外部缓存文件夹不存在
                else
                {
                        //获取内部缓存文件夹
                        cacheDirectory = context.getCacheDir();
                        if (cacheDirectory != null)
                        {
                                //循环清空缓存文件夹
                                deleteFileRecursion(cacheDirectory.listFiles());
                        }
                }

        }

        /**
         * 递归删除文件
         * 不删除文件夹
         *
         * @param entries
         */
        private static void deleteFileRecursion(File[] entries)
        {
                if (entries != null)
                {
                        for (File entry : entries)
                        {
                                //如果不是文件夹
                                if (!entry.isDirectory())
                                {
                                        if (!entry.delete())
                                        {
                                                LogUtils.v("文件删除失败");
                                        }
                                }

                        }
                }


        }


        /**
         * 根据URI 获取对应的文件对象
         *
         * @param context
         * @param uri
         * @return
         */
        public static File getFileByUri(final Context context, final Uri uri)
        {

                File file = null;
                //获取文件真实路径
                String path = FileUtils.getPath(context, uri);
                if (path != null)
                {
                        //根据路径获取文件对象
                        file = new File(path);
                }
                return file;


        }

        /**
         * 通过URI获取文件真实地址
         * Get a file path from a Uri. This will get the the path for Storage Access
         * Framework Documents, as well as the _data field for the MediaStore and
         * other file-based ContentProviders.
         * https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework
         *
         * @param context The context.
         * @param uri     The Uri to query.
         * @author paulburke
         */
        public static String getPath(final Context context, final Uri uri)
        {

                // DocumentProvider
                if (DocumentsContract.isDocumentUri(context, uri))
                {
                        // ExternalStorageProvider
                        if (isExternalStorageDocument(uri))
                        {
                                final String docId = DocumentsContract.getDocumentId(uri);
                                final String[] split = docId.split(":");
                                final String type = split[0];

                                if ("primary".equalsIgnoreCase(type))
                                {
                                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                                }

                        }
                        // DownloadsProvider
                        else if (isDownloadsDocument(uri))
                        {
                                final String id = DocumentsContract.getDocumentId(uri);
                                //如果已经获取到了真实地址
                                if (id.startsWith("raw:"))
                                {
                                        //直接去除raw头部 然后返回地址
                                        return id.replaceFirst("raw:", "");
                                }

                                final Uri contentUri = ContentUris.withAppendedId(
                                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                                return getDataColumn(context, contentUri, null, null);
                        }
                        // MediaProvider
                        else if (isMediaDocument(uri))
                        {
                                final String docId = DocumentsContract.getDocumentId(uri);
                                final String[] split = docId.split(":");
                                final String type = split[0];

                                Uri contentUri = null;
                                if ("image".equals(type))
                                {
                                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                }
                                else if ("video".equals(type))
                                {
                                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                }
                                else if ("audio".equals(type))
                                {
                                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                                }

                                final String selection = "_id=?";
                                final String[] selectionArgs = new String[]{
                                        split[1]
                                };

                                return getDataColumn(context, contentUri, selection, selectionArgs);
                        }
                }
                // MediaStore (and general)
                else if ("content".equalsIgnoreCase(uri.getScheme()))
                {
                        return getDataColumn(context, uri, null, null);
                }
                // File
                else if ("file".equalsIgnoreCase(uri.getScheme()))
                {
                        return uri.getPath();
                }
                return null;
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        public static String getDataColumn(Context context, Uri uri, String selection,
                                           String[] selectionArgs)
        {

                Cursor cursor = null;
                final String column = "_data";
                final String[] projection = {
                        column
                };

                try
                {
                        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                                null);
                        if (cursor != null && cursor.moveToFirst())
                        {
                                final int column_index = cursor.getColumnIndexOrThrow(column);
                                return cursor.getString(column_index);
                        }
                }
                finally
                {
                        if (cursor != null)
                        {
                                cursor.close();
                        }
                }
                return null;
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri)
        {
                return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri)
        {
                return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri)
        {
                return "com.android.providers.media.documents".equals(uri.getAuthority());
        }


}
