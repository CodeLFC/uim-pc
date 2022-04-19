package gaozhi.online.uim.im.service;

import com.google.gson.Gson;
import gaozhi.online.ubtb.core.net.UBTPSocket;
import gaozhi.online.ubtb.core.net.UCommunicationType;
import gaozhi.online.ubtb.core.net.UMsg;
import gaozhi.online.ubtb.core.net.UMsgType;
import gaozhi.online.ubtb.core.util.ByteUtil;
import gaozhi.online.uim.im.entity.UClient;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 心跳 负责维持与服务器的心跳链接
 * @date 2022/3/20 18:39
 */
public class CSBeatService implements UService, UBTPSocket.UMsgConsumer, Runnable {
    private Timer timer;
    private IMServiceApplication imServiceApplication;
    private IMMsgService imMsgService;
    private UserPoolService userPoolService;
    private final List<Consumer<UClient>> beatResponseListenerList;
    //client & self
    private final Gson gson = new Gson();

    public CSBeatService() {
        beatResponseListenerList = new LinkedList<>();
    }

    @Override
    public void accept(UMsg uMsg, SocketAddress socketAddress) {
        if (UCommunicationType.getType(uMsg.getFromId(), uMsg.getToId()) != UCommunicationType.S2C) {
            return;
        }
        //s2c 心跳回执
        UClient temp = gson.fromJson(ByteUtil.bytesToString(uMsg.getData()), UClient.class);
        for (Consumer<UClient> beatResponseListener : beatResponseListenerList) {
            beatResponseListener.accept(temp);
        }
        // getLogger().info("服务器发送的心跳回执（客户端的公网信息）：" + temp);

    }

    @Override
    public void run() {
        //发送心跳包
        UMsg uMsg = new UMsg();
        uMsg.setFromId(userPoolService.getSelfId());
        uMsg.setMsgType(UMsgType.C2S__BEAT_REQUEST.getType());
        uMsg.setToId(imServiceApplication.getImServer().getId());
        //getLogger().info("向服务器发送心跳包:" + uMsg);
        try {
            imMsgService.sendUMsg2IMServer(uMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setIMApplication(IMServiceApplication imApplication) {
        imServiceApplication = imApplication;
        imMsgService = imApplication.getServiceInstance(IMMsgService.class);
        imMsgService.addUMsgConsumer(this);

        userPoolService = imApplication.getServiceInstance(UserPoolService.class);
    }

    @Override
    public void startService() {
        //启动向服务器发送心跳
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CSBeatService.this.run();
            }
        }, 0, 10000);
    }

    @Override
    public void closeService() {
        timer.cancel();
    }

    public void removeBeatResponseListener(Consumer<UClient> beatResponseListener) {
        beatResponseListenerList.remove(beatResponseListener);
    }

    public void addBeatResponseListener(Consumer<UClient> beatResponseListener) {
        beatResponseListenerList.add(beatResponseListener);
    }
}
