package gaozhi.online.uim.im.conversation;


import gaozhi.online.uim.im.conversation.message.IMMessage;
import gaozhi.online.uim.im.conversation.p2p.P2PConnection;
import gaozhi.online.uim.im.entity.IMMsg;
import gaozhi.online.uim.im.service.IMMsgService;

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
    private final Logger logger = Logger.getGlobal();
    private final long selfId;
    private final long friendId;
    private final IMMsgService imMsgService;
    private final List<IMMessageConsumer> consumers = new LinkedList<>();
    private final List<IMMessage> historyMessage = new LinkedList<>();
    private int unReadMessageCount;

    private final P2PConnection p2PConnection;

    public Conversation(long selfId, long friendId, IMMsgService imMsgService) {
        this.selfId = selfId;
        this.friendId = friendId;
        this.imMsgService = imMsgService;
        logger.info("构造会话：" + selfId + ":" + friendId);
        p2PConnection = new P2PConnection(this);
    }

    public P2PConnection.State getP2PConnectionState() {
        return p2PConnection.getP2pState();
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
        logger.info("收到消息：" + message);
        addMessage(message);
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
        //排除p2p心跳消息进入历史消息
        if (message.getMsgType() == IMMessage.IMMsgType.P2PNetInfoBeat || message.getMsgType() == IMMessage.IMMsgType.P2PConnectionSniffing) {
            return;
        }
        if (message.getToId() == selfId) {
            unReadMessageCount++;
        }
        historyMessage.add(message);
        if (historyMessage.size() > 100) {
            historyMessage.remove(0);
        }
    }

    public int sendIMMessage(IMMessage message) throws IOException {
        if (p2PConnection.getP2pState() == P2PConnection.State.CONNECTED) {
            return p2PConnection.sendIMMessage2Nat(message);
        }
        return send2Center(message);
    }

    public int send2Center(IMMessage msg) throws IOException {
        logger.info("发送服务器中转消息：" + msg);
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

    public String getIp() {
        return imMsgService.getIp();
    }

    public int getPort() {
        return imMsgService.getPort();
    }

    @FunctionalInterface
    public interface IMMessageConsumer extends Consumer<IMMessage> {

    }
}
