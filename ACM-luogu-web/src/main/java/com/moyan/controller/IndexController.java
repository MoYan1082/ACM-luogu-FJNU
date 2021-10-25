package com.moyan.controller;

import com.moyan.entity.Data;
import com.moyan.entity.DataWithRank;
import com.moyan.entity.User;
import com.moyan.entity.UserWithRank;
import com.moyan.util.RWJson;
import com.moyan.util.SaveImage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class IndexController {

    public void saveUserAvatar(Data data) throws IOException {
        String prePath = "./resources/avatars/";
        for(int i = 0; i < data.data.size(); i++){
            String tmp = prePath + data.data.get(i).uid.toString() + ".png";
            File image = new File(tmp);
            String url = "https://cdn.luogu.com.cn/upload/usericon/"+data.data.get(i).uid.toString()+".png";
            if(image.exists()) image.delete();

            SaveImage.saveImage(url, tmp);
            data.data.get(i).avatar = "avatars/" + data.data.get(i).uid.toString() + ".png";
        }
    }

    @RequestMapping("/1")
    public String index1(Model mode) throws IOException {
        String newDataPath = "./resources/newData.json";

        Data newData = RWJson.readJSON(newDataPath, Data.class);
        newData.data.sort((o1, o2) -> o2.passedProblemCount - o1.passedProblemCount);
        saveUserAvatar(newData);// 保存图片和刷新background

        Data Top3 = new Data();// 排名前三的先拿出来
        DataWithRank other = new DataWithRank();
        Top3.time = newData.time;
        for(int i = 0; i < newData.data.size(); i++){
            if(newData.data.get(i).passedProblemCount == 0) continue;
            if(i >= 3){
                UserWithRank userWithRank = new UserWithRank();
                userWithRank.avatar = newData.data.get(i).avatar;
                userWithRank.color = newData.data.get(i).color;
                userWithRank.name = newData.data.get(i).name;
                userWithRank.passedProblemCount = newData.data.get(i).passedProblemCount;
                userWithRank.uid = newData.data.get(i).uid;
                userWithRank.rank = i + 1;
                other.data.add(userWithRank);
            }
            else {
                Top3.data.add(newData.data.get(i));
            }
        }

        mode.addAttribute("Top3", Top3);
        mode.addAttribute("other", other);
        return "index1";
    }
}
