package gaozhi.online.uim.example.im.conversation;


import gaozhi.online.uim.example.im.entity.IMMsg;
import gaozhi.online.uim.example.im.service.IMMsgService;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO c端 会话
 * @date 2022/3/22 15:21
 */
public class Conversation implements IMMsgService.IMMsgConsumer {
    private Logger logger = Logger.getGlobal();
    private final long selfId;
    private final long friendId;
    private final IMMsgService imMsgService;
    private final List<IMMessageConsumer> consumers = new LinkedList<>();
    private final List<IMMessage> historyMessage = new LinkedList<>();
    private int unReadMessageCount;

    public Conversation(long selfId, long friendId, IMMsgService imMsgService) {
        this.selfId = selfId;
        this.friendId = friendId;
        this.imMsgService = imMsgService;
        logger.info("构造会话："+selfId+":"+friendId);
    }

    public long getFriendId() {
        return friendId;
    }

    public long getSelfId() {
        return selfId;
    }

    public void addConsumer(IMMessageConsumer consumer) {
        consumers.add(consumer);
    }

    public void removeConsumer(IMMessageConsumer consumer) {
        consumers.remove(consumer);
    }

    @Override
    public void accept(IMMsg msg) {
        IMMessage message = IMMessage.Codec.decode(msg);
        addMessage(message);
        logger.info("收到消息："+message);
        for (IMMessageConsumer consumer : consumers) {
            consumer.accept(message);
        }
    }

    public void setUnReadMessageCount(int unReadMessageCount) {
        this.unReadMessageCount = unReadMessageCount;
    }

    public int getUnReadMessageCount() {
        return unReadMessageCount;
    }

    private void addMessage(IMMessage message) {
        if(message.getToId()==selfId) {
            unReadMessageCount++;
        }
        historyMessage.add(message);
        if (historyMessage.size() > 100) {
            historyMessage.remove(0);
        }
    }

    public int send2Center(IMMessage msg) throws IOException {
        addMessage(msg);
        return imMsgService.sendIMMsg2IMServer(IMMessage.Codec.encode(selfId, friendId, msg));
    }

    public int send(IMMessage msg, SocketAddress socketAddress) throws IOException {
        addMessage(msg);
        return imMsgService.sendIMMsg(IMMessage.Codec.encode(selfId, friendId, msg), socketAddress);
    }

    public List<IMMessage> getHistoryMessage() {
        return historyMessage;
    }

    @FunctionalInterface
    public interface IMMessageConsumer extends Consumer<IMMessage> {

    }
}
