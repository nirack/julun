package com.julun.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2015-10-29.
 */
public class FileUtils {
    /**
     * 返回应用的基本文件夹.
     * 当前App的默认数据存储目录.
     * @param context
     * @return
     */
    public File getBaseDir(Context context){
        return context.getFilesDir();
    }

    /**
     * 返回默认的缓存存放位置.
     * 用于存放不是非常重要的文件.
     * 如果手机内存不足的时候,系统会自动删除App里缓存的数据.
     * @param context
     * @return
     */
    public File getCacheDir(Context context){
        return context.getCacheDir();
    }

    /**
     * 指定权限，文件名，创建一个文件架.
     * @param context
     * @param mode
     *      Context.MODE_PRIVATE;  默认的模式,私有文件，只有当前app可以访问，文件以覆盖的方式写入
     *      Context.MODE_APPEND  ;// 如果要添加内容而不是覆盖，则需要 MODE_APPEND 模式,此模式下，如果文件不存在，则创建文件,否则，追加内容
     *      context.MODE_WORLD_READABLE ,context.MODE_WORLD_WRITEABLE 则是控制其他程序是否有权限
     * @param name
     * @return
     */
    public File getDir(Context context,Integer mode,String name){
//        int modePrivate = Context.MODE_PRIVATE;  默认的模式,私有文件，只有当前app可以访问，文件以覆盖的方式写入
//        Context.MODE_APPEND  ;// 如果要添加内容而不是覆盖，则需要 MODE_APPEND 模式,此模式下，如果文件不存在，则创建文件,否则，追加内容
        context.getDir(name, mode);
        return  null;
    }

    /**
     * api 8 以上的，可以使用外部文件 .
     * 该位置跟内置的使用一样，路径为  /mnt/sdcard/Android/$packageName/
     * 这个位置的数据跟内部一样，如果应用被删除,则这个位置的数据也会被删除.
     * @param context
     * @return
     */
    public File getExternalFile(Context context){
//        context.getExternalCacheDir()
        return  null;
    }

    /**
     * 获取输入流.
     * @param context
     * @param name
     * @param mode
     * @throws Exception
     */
    public FileOutputStream getFileOutputStream(Context context, String name, int mode) throws Exception {
        return context.openFileOutput(name, mode);
    }

    /**
     * 获取输出流.
     * @param context
     * @param fileName
     * @return
     * @throws Exception
     */
    public InputStream getInputStream(Context context,String fileName) throws Exception{

        return context.openFileInput(fileName);
    }

}
