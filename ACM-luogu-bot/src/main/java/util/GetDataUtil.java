package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.Apis;
import entity.Data;
import entity.Users;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetDataUtil {

    /**
     * 将Api获取到的json数据转成Data
     *
     * @param jsonObject
     * @return
     */
    public static List<Data> conversion(JSONObject jsonObject) {
        // 这两个类用于中间转换，没有什么实际用处
        class User {
            public Long uid;
            public String name;
            public String slogan;
            public String badge;
            public boolean isAdmin;
            public boolean isBanned;
            public String color;
            public int ccfLevel;
        }
        class Member {
            public String group;
            public User user;
            public int type;
            public int permission;
        }

        List<Data> res = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("members");
        List<Member> members = new ArrayList<>();
        for (Object obj : jsonArray) {
            members.add(JSON.toJavaObject((JSON) obj, Member.class));
        }
        for (Member member : members) {
            Data data = new Data();
            data.uid = member.user.uid;
            data.name = member.user.name;
            data.slogan = member.user.slogan;
            data.color = member.user.color;
            data.passedProblemCount = 0; // 先将题数初始化为0
            data.type = member.type;
            res.add(data);
        }
        return res;
    }

    /**
     * 从api中获取uid，并分别提取出每个人的详细信息。
     *
     * @return
     * @throws IOException
     */
    public static Users get() throws IOException {
        Users users = new Users();
        users.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // =======================================================================================
        // 通过api的url获取到uid的信息
        Apis apis = JsonUtils.readJSON("./resources/apis.json",
                Apis.class);
        Set<Long> uniqueUid = new HashSet<>(); // 用于去重
        for (String api : apis.apis) {
            String html = null;
            try {
                html = HtmlAnalyzeUtils.getHTML(api);
                List<Data> conversionData = conversion(JSONObject.parseObject(html));
                for (Data item : conversionData) {
                    if (item.type > 1) continue; // 特判管理员
                    if (!uniqueUid.contains(item.uid)) {
                        uniqueUid.add(item.uid);
                        users.data.add(item);
                    }
                }
            } catch (IOException e) {
                System.out.println("api的html获取失败！");
                e.printStackTrace();
            }
        }
        // =======================================================================================
        // 获取每个uid的passedProblemCount
        for (Data item : users.data) {
            String url_user = "https://www.luogu.com.cn/user/" + item.uid;
            String jsonstring = HtmlAnalyzeUtils.getJson(url_user).toString();
            // 用正则表达式筛选信息
            String regex = "passedProblemCount\":[0-9]*";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(jsonstring);
            while (m.find()) {
                String ans = m.group(0);
                item.passedProblemCount = Integer.parseInt(ans.substring(20));
            }
        }
        users.data.sort((o1, o2) -> o2.passedProblemCount - o1.passedProblemCount);
        return users;
    }
}