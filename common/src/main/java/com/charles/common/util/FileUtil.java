package com.charles.common.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author charles
 * @date 16/10/17
 */

public class FileUtil {
    /**
     * 是否含有文件file
     *
     * @param context
     * @param fileName
     * @return
     */
    public static boolean hasFile(Context context, String fileName) {
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean existsFile(String filePath, String filename) {
        File file = new File(filePath, filename);
        return file.exists();
    }

    public static String getImageDirectory(Context context) {
        File file = new File(context.getExternalCacheDir() + "/image");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();

    }

    public static String getFileDirectory(Context context) {
        File file = new File(context.getExternalCacheDir() + "/file");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    /**
     * 将文件存储在本地
     *
     * @param bytes    二进制形式文件
     * @param filePath 目录
     * @param fileName 文件名
     * @return 操作成功/失败
     */
    public static boolean save(byte[] bytes, String filePath, String fileName) {
        File file = new File(filePath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除指定文件
     *
     * @param filePath 目录
     * @param fileName 文件名
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除指定文件夹
     *
     * @param dir 目录
     */
    public static void deleteDirectory(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    deleteDirectory(file1.getAbsolutePath());
                } else {
                    file1.delete();
                }
            }
        }
    }

    /**
     * 存一段字符串
     *
     * @param context
     * @param s
     * @param fileName
     */
    public static void writeStringToFile(Context context, String s, String fileName) {
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读一段字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readStringFromFile(Context context, String fileName) {
        String string = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                string = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("activity_login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("activity_login activity", "Can not read file: " + e.toString());
        }
        return string;
    }

    /**
     * 从assets目录中读取字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getStringFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
