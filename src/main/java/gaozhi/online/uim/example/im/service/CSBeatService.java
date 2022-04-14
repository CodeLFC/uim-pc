package gaozhi.online.uim.example.im.service;

import com.google.gson.Gson;
import gaozhi.online.ubtb.core.net.UBTPSocket;
import gaozhi.online.ubtb.core.net.UCommunicationType;
import gaozhi.online.ubtb.core.net.UMsg;
import gaozhi.online.ubtb.core.net.UMsgType;
import gaozhi.online.ubtb.core.util.ByteUtil;
import gaozhi.online.uim.core.asynchronization.TaskExecutor;
import gaozhi.online.uim.example.im.entity.UClient;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.Timer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 心跳 负责维持与服务器的心跳链接
 * @date 2022/3/20 18:39
 */
public class CSBeatService implements UService, UBTPSocket.UMsgConsumer, Runnable {
    private final TaskExecutor taskExecutor = new TaskExecutor();
    private Timer timer;
    private IMServiceApplication imServiceApplication;
    private IMMsgService imMsgService;
    private UserPoolService userPoolService;

    //client & self
    private final Gson gson = new Gson();
    private final UClient self = new UClient();

    @Override
    public void accept(UMsg uMsg, SocketAddress socketAddress) {
        if (UCommunicationType.getType(uMsg.getFromId(), uMsg.getToId()) != UCommunicationType.S2C) {
            return;
        }
        //s2c 心跳回执
        UClient temp = gson.fromJson(ByteUtil.bytesToString(uMsg.getData()), UClient.class);
        self.setId(temp.getId());
        self.setIp(temp.getIp());
        self.setPort(temp.getPort());
        self.setUpdateTime(temp.getUpdateTime());

        getLogger().info("服务器发送的心跳回执（客户端的公网信息）：" + self);
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
        timer = taskExecutor.executeTimerTask(this, 10000);
    }

    @Override
    public void closeService() {
        timer.cancel();
    }

    /**
     * @description: TODO 获取客户端的公网ip信息
     * @author LiFucheng
     * @date 2022/3/23 9:54
     * @version 1.0
     */
    public UClient getSelfInfo() {
        return self;
    }
}
