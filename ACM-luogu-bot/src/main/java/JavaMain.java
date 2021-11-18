import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import work.Reminder;
import work.SaveNewData;
import work.SavePastData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaMain {
    private static Bot CURRENT_BOT = null;

    public static Bot getCurrentBot() {
        return CURRENT_BOT;
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new SaveNewData());
        exec.execute(new SavePastData());
        exec.execute(new Reminder());

        CURRENT_BOT = BotFactory.INSTANCE.newBot(QQcount.USER_NAME, QQcount.PASSWARD, new BotConfiguration() {{
            fileBasedDeviceInfo(); // 使用 device.json 存储设备信息
            setProtocol(MiraiProtocol.ANDROID_PAD); // 切换协议
        }});

        // https://mirai.mamoe.net/topic/223/无法登录的临时处理方案
        CURRENT_BOT.login(); // 登录bot

        EventChannel<BotEvent> eventChannel = CURRENT_BOT.getEventChannel();
        eventChannel.registerListenerHost(new MyEventChannel());

        exec.shutdown();
    }
}
