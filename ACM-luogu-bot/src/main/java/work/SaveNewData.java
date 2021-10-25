package work;


import entity.Users;
import util.JsonUtils;
import util.GetDataUtil;
import util.TxtToImageUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 将每个人的信息保存到newData.json
 * 并根据pastData.json的信息获取到upData.json
 * 将newData.json和upData.json的信息保存至jpg
 * 每间隔10分钟刷新一次数据
 */
public class SaveNewData implements Runnable {
    @Override
    public void run() {
        while(true) {
            try {
                System.out.println("开始刷新数据！！");
                Users newData = GetDataUtil.get();
                newData.data.sort((o1, o2) -> o2.passedProblemCount - o1.passedProblemCount);
                JsonUtils.writeJSON(newData, "./resources/newData.json");
                TxtToImageUtil.createImage(newData.toString(), "./resources/newData.jpg");

                Users upData = newData.sub(JsonUtils.readJSON("./resources/pastData.json", Users.class));
                upData.data.sort((o1, o2) -> o2.passedProblemCount - o1.passedProblemCount);
                JsonUtils.writeJSON(upData, "./resources/upData.json");
                TxtToImageUtil.createImage(upData.toString(), "./resources/upData.jpg");
                System.out.println("数据刷新成功！！");

                try {
                    // 使用phantomjs工具获取web页面的png图片
                    ProcessBuilder processBuilder = new ProcessBuilder();
                    processBuilder.command("bash", "-c", "phantomjs ./resources/getPng.js");
                    Process process = processBuilder.start();
                    process.waitFor();  // 等待进程结束
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                TimeUnit.MINUTES.sleep(10);
            } catch (Exception e) {
                System.out.println("newData出现异常：" + e.getMessage());
            }
        }
    }
}

