package work;

import manager.MessageManager;
import net.mamoe.mirai.message.data.*;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务：定时下午5点提醒群友写题
 */
public class Reminder implements Runnable {
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
        while(true) {
            // 设定刷新时间
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date date = new Date();
            try {
                TimeUnit.MILLISECONDS.sleep(Math.max(0, getDifSecond(calendar.getTime(), date)));
                System.out.println("开始提醒群友写题！！");

                MessageChain chain = new MessageChainBuilder()
                        .append(new At(2737664805L))
                        .append(new PlainText("别忘了今天要写题哦♪(^∇^*)"))
                        .build();
                MessageManager.sendMessageToQQGroup(418507282L, chain);

                System.out.println("提醒群友写题结束！！");
                calendar.set(Calendar.HOUR_OF_DAY, 24);
                TimeUnit.MILLISECONDS.sleep(Math.max(0, getDifSecond(calendar.getTime(), date)) + 1);
            } catch (Exception e) {
                System.out.println("提醒任务出现异常：" + e.getMessage());
            }
        }
    }
}
