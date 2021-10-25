package entity;

import util.TxtToImageUtil;

import java.util.*;

/**
 * newData.json 和 pastData.json的实体类
 */
public class Users {
    public List<Data> data;
    public String time;
    public Users(){
        data = new ArrayList<>();
    }

    /**
     * 获取两个Users的差值
     * @param anotherUsers
     * @return
     */
    public Users sub(Users anotherUsers){
        Users difCount = new Users();
        difCount.time = time;
        Map<Long, Integer> mp = new HashMap<>();
        for(Data item : anotherUsers.data) {
            mp.put(item.uid, item.passedProblemCount);
        }
        for(int i = 0; i < data.size(); i++) {
            if(mp.containsKey(data.get(i).uid)) {
                int difPassed = data.get(i).passedProblemCount - mp.get(data.get(i).uid);
                if (difPassed > 0) {
                    Data tmpData = data.get(i);
                    tmpData.passedProblemCount = difPassed;
                    difCount.data.add(tmpData);
                }
            } else if (data.get(i).passedProblemCount > 0) {
                difCount.data.add(data.get(i));
            }
        }
        return difCount;
    }

    /**
     * 用于格式化输出
     * 宽度权值 中文:英文 = 2:1
     * @return 格式化后的字符串
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("日期: " + time + "\n");
        int cnt = 0; // 记录刷题人数
        for (Data item : data) {
            if(item.passedProblemCount > 0) {
                cnt++;
                StringBuilder tmp = new StringBuilder();
                int widthWeight = 0, allWidthWeight = 0;
                for(int i = 0; i < item.name.length(); i++) {
                    if (TxtToImageUtil.isChinese(item.name.charAt(i))) allWidthWeight += 2;
                    else    allWidthWeight += 1;
                }
                for(int i = 0; i < item.name.length() && widthWeight < 12; i++) {
                    if (TxtToImageUtil.isChinese(item.name.charAt(i))) widthWeight += 2;
                    else    widthWeight += 1;
                    tmp.append(item.name.charAt(i));
                }
                while(widthWeight < 15) {
                    tmp.insert(0, " ");
                    widthWeight += 1;
                }
                res.append(tmp).append(": ")
                        .append(item.passedProblemCount)
                        .append("\n");
            }
        }
        res.append("刷过题的人数: ").append(cnt);
        return res.toString();
    }
}