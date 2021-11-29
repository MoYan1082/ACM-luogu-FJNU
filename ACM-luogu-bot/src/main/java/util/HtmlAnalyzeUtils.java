package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlAnalyzeUtils {
    /**
     * 将Unicode转成中文
     * @param str
     * @return
     */
    public static synchronized String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch+"" );
        }
        return str;
    }

    /**
     * 读取txt文件中的数据
     * @param filename 文件路径
     * @return 文件中的文本数据
     * @throws IOException
     */
    public static synchronized String readTxt(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String s;
        StringBuilder sb = new StringBuilder();
        while((s = in.readLine()) != null) {
            if(!sb.isEmpty())  sb.append("\n");
            sb.append(s);
        }
        in.close();
        return sb.toString();
    }


    /**
     * 获取目标url的HTML
     * @param url
     * @return HTML:String
     * @throws IOException
     */
    public static synchronized String getHTML(String url) throws IOException {
        String res = "";
        URL realUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36");
        try {
            String cookie = readTxt("./resources/cookie.txt");
            connection.setRequestProperty("cookie", cookie);
        } catch (Exception e) {
            System.out.println("设置cookie失败!\n" + e.getMessage());
        }
        connection.setConnectTimeout(3000); // 设置超时
        connection.setReadTimeout(3000);
        try {
            connection.connect(); // 建立实际的连接
            if (connection.getResponseCode() == 200) { // 请求成功
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuffer = new StringBuilder();
                String text = null;
                while ((text = bufferedReader.readLine()) != null) {
                    stringBuffer.append(text).append("\n");
                }

                res += stringBuffer;// 添加
                inputStreamReader.close();
                bufferedReader.close();
            } else {
                System.out.println("连接失败：" + connection.getResponseCode());
            }
        }
        catch (IllegalArgumentException e){
            System.out.println("timeout can not be negative: " + e);
        }
        return res.toString();
    }

    /**
     * 将目标url的信息打包成JSON信息（只针对个人页面）
     * @param url
     * @return
     * @throws IOException
     */
    public static synchronized JSONObject getJson(String url) throws IOException {
        String html = getHTML(url);
        String dataMessage = "";
        String regex = "decodeURIComponent\\(\\\".*\\\"\\)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(html);
        while (m.find()) {
            String ans = m.group(0);
            dataMessage = ans.substring(20, ans.length() - 2);
        }
        dataMessage = URLDecoder.decode(dataMessage);   // URLCode编码转成Unicode
        String res = unicodeToString(dataMessage);      // Unicode编码转成中文
        JSONObject jsonObject = JSON.parseObject(res);  // 将String转成JSON对象
        return jsonObject;
    }

}


