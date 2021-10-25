import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import java.io.File;
import java.io.IOException;

/**
 * 监听器只在被注册时被初始化一次
 * 每次监听到事件，会调用相应的方法，并且这个方法的运行是并发的。
 */
public class MyEventChannel extends SimpleListenerHost {

    @EventHandler()
    public ListeningStatus onGroupMessage(GroupMessageEvent event) throws IOException {
        groupMessageHandle(event);
        return ListeningStatus.LISTENING;// 保持监听
    }

    private void groupMessageHandle(GroupMessageEvent event) throws IOException{
        MessageChain messageChain = event.getMessage();
        String msgStr = messageChain.get(1).toString();
        if(msgStr.equals("洛谷题数")){
            File img = new File("./resources/newData.jpg");
            if(img.exists())
                Contact.sendImage(event.getGroup(), img).recallIn(30000);
        }  else if(msgStr.equals("今日题数")){
            File img = new File("./resources/upData.jpg");
            if(img.exists())
                Contact.sendImage(event.getGroup(), img).recallIn(30000);
        }  else if(msgStr.equals("排行榜")){
            File img = new File("./resources/ranklist.png");
            if(img.exists())
                Contact.sendImage(event.getGroup(), img).recallIn(30000);
        }
    }
}
