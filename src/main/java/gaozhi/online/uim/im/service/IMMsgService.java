package gaozhi.online.uim.im.service;

import gaozhi.online.ubtb.core.net.UBTPSocket;
import gaozhi.online.ubtb.core.net.UCommunicationType;
import gaozhi.online.ubtb.core.net.UMsg;
import gaozhi.online.ubtb.core.util.StringUtils;
import gaozhi.online.uim.im.entity.IMMsg;
import gaozhi.online.uim.im.entity.UServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO   消息服务:负责消息的收发
 * @date 2022/3/17 17:52
 */
public class IMMsgService extends UBTPSocket implements UService, UBTPSocket.UMsgConsumer {
    private static final int INITIAL_QUEUE_SIZE = 100;
    private final List<IMMsgConsumer> consumers = new LinkedList<>();
    private final Map<Long, IMMsg.Codec.UMsgQueue> msgQueueMap = new WeakHashMap<>();
    private IMServiceApplication imServiceApplication;
    //server  & address
    private UServer imServerTemp = new UServer();
    private SocketAddress imServerAddress;

    public IMMsgService(int port, int mtu) {
        super(port, mtu);
        addUMsgConsumer(this);
    }

    @Override
    public void accept(UMsg uMsg, SocketAddress socketAddress) {
        UCommunicationType type = UCommunicationType.getType(uMsg.getFromId(), uMsg.getToId());
        //不是客户端对客户端和客户端对从属者消息不予处理
        if (type != UCommunicationType.C2C && type != UCommunicationType.C2SUBS) {
            return;
        }
       // getLogger().info(uMsg.toString());
        //对应用户的队列，每位用户需要保证消息序列号的同步
        IMMsg.Codec.UMsgQueue msgQueue;
        if (!msgQueueMap.containsKey(uMsg.getFromId())) {
            msgQueue = new IMMsg.Codec.UMsgQueue(INITIAL_QUEUE_SIZE);
            msgQueueMap.put(uMsg.getFromId(), msgQueue);
        } else {
            msgQueue = msgQueueMap.get(uMsg.getFromId());
        }
        //入队
        msgQueue.add(uMsg);

        //检查
        UMsg peek = msgQueue.peek();
        int size = IMMsg.Codec.getSize(peek);
        int startSerial = IMMsg.Codec.getStartSerial(peek);

        //提取消息碎片
        if (size <= msgQueue.size()) {
            IMMsg.Codec.UMsgQueue tempQueue = new IMMsg.Codec.UMsgQueue(size);
            //
            int count =size;
            while (count > 0) {
                UMsg pollMsg = msgQueue.poll();
                int serial = IMMsg.Codec.getSerial(pollMsg);
                getLogger().info("size : "+size+" startSerial:"+startSerial+" serial:"+serial);
                if (serial >= size + startSerial) {
                    msgQueue.add(pollMsg);
                    return;
                }
                tempQueue.add(pollMsg);
                count--;
            }

            //解码与消费
            IMMsg msg = IMMsg.Codec.decode(tempQueue);
            for (IMMsgConsumer consumer : consumers) {
                consumer.accept(msg);
            }
        }
    }

    /**
     * @description: TODO 发送消息到指定地址
     * @author LiFucheng
     * @date 2022/3/18 9:24
     * @version 1.0
     */
    public synchronized int sendIMMsg(IMMsg msg, SocketAddress serverAddress) throws IOException {
        IMMsg.Codec.UMsgQueue msgQueue = IMMsg.Codec.encode(msg);
        int sum = 0;
        while (msgQueue.size() > 0) {
            sum += send(msgQueue.poll(), serverAddress);
        }
        return sum;
    }

    /**
     * @description: TODO 发送消息到指定的地址列表
     * @author LiFucheng
     * @date 2022/3/18 9:26
     * @version 1.0
     */
    public synchronized int sendIMMsg(IMMsg msg, List<SocketAddress> serverAddress) throws IOException {
        IMMsg.Codec.UMsgQueue msgQueue = IMMsg.Codec.encode(msg);
        int sum = 0;
        while (msgQueue.size() > 0) {
            sum += send(msgQueue.poll(), serverAddress);
        }
        return sum;
    }

    /**
     * @description: TODO 发送消息到消息服务器
     * @author LiFucheng
     * @date 2022/3/23 15:07
     * @version 1.0
     */
    public int sendUMsg2IMServer(UMsg msg) throws IOException {
        checkUServerAddress();
        return send(msg, imServerAddress);
    }

    public int sendIMMsg2IMServer(IMMsg msg) throws IOException {
        checkUServerAddress();
        return sendIMMsg(msg, imServerAddress);
    }

    /**
     * @description: TODO 检查服务器地址是否是最新
     * @author LiFucheng
     * @date 2022/3/23 15:15
     * @version 1.0
     */
    private void checkUServerAddress() {
        //检查服务器地址
        UServer imServer = imServiceApplication.getImServer();
        if (!StringUtils.equals(imServerTemp.getIp(), imServer.getIp()) || imServerTemp.getPort() != imServer.getPort()) {
            imServerAddress = new InetSocketAddress(imServer.getIp(), imServer.getPort());
            imServerTemp = imServer;
        }
    }

    public void addIMMsgConsumer(IMMsgConsumer consumer) {
        consumers.add(consumer);
    }

    public void removeIMMsgConsumer(IMMsgConsumer consumer) {
        consumers.remove(consumer);
    }

    @Override
    public void setIMApplication(IMServiceApplication imApplication) {
        imServiceApplication = imApplication;
    }

    @Override
    public void startService() {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeService() {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO  消息消费者
     * @date 2022/3/17 18:45
     */
    @FunctionalInterface
    public interface IMMsgConsumer extends Consumer<IMMsg> {

    }
}
