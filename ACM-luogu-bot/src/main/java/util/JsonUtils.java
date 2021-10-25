package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import java.io.*;
import java.nio.charset.StandardCharsets;


public class JsonUtils {

    /**
     * 读取json文件，并返回targetClass类
     * @param path
     * @param targetClass
     * @param <T>
     * @return
     */
    public static synchronized <T> T readJSON(String path, Class<T> targetClass){
        String jsonString;
        File file = new File(path);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
            T object = JSON.parseObject(jsonString, targetClass);
            return object;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO exception");
        }
    }

    /**
     * 将目标类object保存至json文件
     * @param object
     * @param path
     */
    public static synchronized void writeJSON(Object object, String path){
        JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8);
            osw.write(jsonObject.toJSONString());
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}