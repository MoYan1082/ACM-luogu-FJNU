package work;

import entity.Users;
import util.JsonUtils;
import util.GetDataUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 将每个人的信息保存到pastData.json
 * 定时任务，每天23:50刷新
 */
public class SavePastData implements Runnable {
    /**
     * 获取两个时间的毫秒差
     * @param date1
     * @param date2
     * @return
     */
    private int getDifSecond(Date date1, Date date2) {
        Long d1 = date1.getTime();
        Long d2 = date2.getTime();
        return (int) (d1 - d2);
    }

    @Override
    public void run() {
        // 设定刷新时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 0);
        while(true) {
            Date date = new Date();
            int dif = getDifSecond(calendar.getTime(), date);
            try {
                TimeUnit.MILLISECONDS.sleep(Math.max(0, dif));
                System.out.println("开始更新昨天数据！！");
                Users pastData = GetDataUtil.get();
                JsonUtils.writeJSON(pastData, "./resources/pastData.json");
                System.out.println("昨天数据更新昨天！！");
                TimeUnit.MINUTES.sleep(10); // 等待
            } catch (Exception e) {
                System.out.println("定时任务出现异常：" + e.getMessage());
            }
        }
    }
}

