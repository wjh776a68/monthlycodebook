package com.wjh776a68.monthlycodebook;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class fileio {

    //读取文件中的字符串
    public static String readFile(String filePath) {
        //try (FileInputStream fis = openFileInput(filePath)){
        File file = new File(filePath);
        try(FileInputStream fis = new FileInputStream(file)){
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e("read 文件失败",e.getMessage());
            } finally {
                return stringBuilder.toString();
            }
        }catch (IOException e) {
            Log.e("read 文件失败 no found",e.getMessage());
            return "";
        }

    }

    //将内容写入文件
    public static void writeToFile(String filePath,String content){
        File dir = new File(filePath);
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdirs();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean flag = file.createNewFile();
                if (!flag) {
                    Log.e("创建文件失败","createNewFile 失败");
                }
            } catch (Exception e) {
                Log.e("创建文件失败",e.getMessage());
            }
        }


        //try (FileOutputStream fos = openFileOutput(filePath, Context.MODE_PRIVATE)) {
        try(FileOutputStream fos = new FileOutputStream(file)){
            fos.write( content.getBytes());
        }catch (Exception e) {
            Log.e("write文件失败",e.getMessage());
        }
    }

    public static void CreateFile(String filePath){
        File dir = new File(filePath);
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdirs();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean flag = file.createNewFile();
                if (!flag) {
                    Log.e("创建文件失败","createNewFile 失败");
                }
            } catch (Exception e) {
                Log.e("创建文件失败",e.getMessage());
            }
        }
    }





    //根据路径获取文件
    public static File getFile(String filePath) {
        File dir = new File(filePath);
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdirs();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean flag = file.createNewFile();
                if (!flag) {
                    Log.e("创建文件失败","createNewFile 失败");
                }
            } catch (Exception e) {
                Log.e("创建文件失败",e.getMessage());
            }
        }
        return file;
    }
}
