package manager;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;

import java.lang.reflect.Method;

public class MessageManager {
    public static void sendMessageToQQGroup(long group, MessageChain messageChain) {
        try {
            // 反射调用默认包下的类
            Class<?> cla = Class.forName("JavaMain");
            Method method = cla.getMethod("getCurrentBot", null);
            Bot bot = (Bot) method.invoke(cla.newInstance(), null);
            bot.getGroup(group).sendMessage(messageChain);
        } catch (Exception e) {
            // TODO 异常捕获还没写
            e.printStackTrace();
        }

    }
}
