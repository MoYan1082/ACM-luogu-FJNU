package com.moyan.util;


import java.io.*;
import java.net.URL;

public class SaveImage {
    public static File saveImage(String urlStr, String pathName) throws IOException {
        URL url = new URL(urlStr);
        final InputStream inputStream = url.openConnection().getInputStream();

        final File imageFile = new File(pathName);
        if (!imageFile.isFile()) {
            imageFile.createNewFile();
        }

        int packSize = 1024;
        byte[] bytes = new byte[packSize];
        int len = 0;
        try (OutputStream outputStream = new FileOutputStream(imageFile)) {
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        }
        inputStream.close();

        return imageFile;
    }
}
