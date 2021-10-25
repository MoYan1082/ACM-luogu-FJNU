package com.moyan.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RWJson {
    public static <T> T readJSON(String path, Class<T> targetClass){
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

    public static void writeJSON(Object object, String path){
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
