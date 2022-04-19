package gaozhi.online.uim.im.conversation.p2p;

import gaozhi.online.uim.im.conversation.Conversation;
import gaozhi.online.uim.im.conversation.message.IMMessage;
import gaozhi.online.uim.im.entity.UClient;
import gaozhi.online.uim.im.service.CSBeatService;
import gaozhi.online.uim.im.service.IMServiceApplication;
import gaozhi.online.uim.utils.IpV4Util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 负责p2p连接的处理
 * @date 2022/4/18 15:15
 */
public class P2PConnection implements Consumer<UClient>, Runnable {
    private final Logger logger = Logger.getGlobal();
    private final Conversation conversation;
    private State p2pState;
    private NATType natType;
    private UClient selfNetInfo;
    private UClient friendNetInfo;
    private final IpV4Util ipV4Util;
    private InetSocketAddress natAddress;

    @Override
    public void run() {
        //同时双向打洞
        logger.info("嗅探到对方的网络信息：" + friendNetInfo + "开始打洞");
        p2pState = State.CONNECTING;

        IMMessage sniffingMsg = new IMMessage();
        sniffingMsg.setMsgType(IMMessage.IMMsgType.P2PConnectionSniffing);
        sniffingMsg.setTime(System.currentTimeMillis());
        sniffingMsg.setData(new byte[1]);
        int count = 0;
        while (p2pState != State.CONNECTED && count++ < 3) {
            String ip = friendNetInfo.getIp();
            int port = friendNetInfo.getPort();
            if (selfNetInfo != null && ipV4Util.checkSameSegment(selfNetInfo.getLocalIp(), friendNetInfo.getLocalIp())) {
                ip = friendNetInfo.getLocalIp();
                port = friendNetInfo.getLocalPort();
                natType = NATType.LAN;
            } else {
                logger.info("不在同一子网,需要判断NAT类型穿透外网：" + selfNetInfo + friendNetInfo);
            }
            natAddress = new InetSocketAddress(ip, port);
            try {
                conversation.send(sniffingMsg, natAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("打洞中：" + friendNetInfo + "：count:" + count);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO Nat的类型
     * @date 2022/4/19 8:56
     */
    public enum NATType {
        LAN,
        // 允许任何外部 IP 任何端⼝连⼊ NAT，只要 NAT 内部 host 产⽣过 IP 端⼝映射。 (不限制任何 IP)
        Full_Cone_NAT,
        // 只允许外部指定 IP 连⼊当前 NAT，即 NAT 内 host 主动连 接过的 IP。（限制 IP)
        Restricted_Cone_NAT,
        //只允许内⽹设备主动 连接过的外⽹ IP 和 Port 连⼊。（限制 IP + Port）
        Port_Restricted_Cone_NAT,
        //这种类型的 NAT ⾏为跟端⼝限制型的 NAT 类型相似，不同的是，对于向外连接的不同的 IP 和 Port，NAT 随机分配⼀个 Port 来完成地址转换，完成对外连接。（只要向外的 IP:Port 不一致则映射到不同端口）
        Symmetric_NAT;
    }

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO 链接状态
     * @date 2022/4/19 8:56
     */
    public enum State {
        UNREACHABLE("无法到达"),
        CONNECTING("正在嗅探Peer"),
        CONNECTED("P2P已连接"),
        DISCONNECT("P2P未连接");

        private final String description;

        State(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "State{" +
                    "description='" + description + '\'' +
                    '}';
        }
    }

    public P2PConnection(Conversation conversation) {
        ipV4Util = new IpV4Util();
        p2pState = State.DISCONNECT;
        this.conversation = conversation;
        CSBeatService csBeatService = IMServiceApplication.getInstance().getServiceInstance(CSBeatService.class);
        csBeatService.addBeatResponseListener(this);
        conversation.addConsumer(message -> {
            switch (message.getMsgType()) {
                //P2P网络信息
                case P2PNetInfoBeat:
                    IMMessage.DataCoder<UClient> clientDataCoder = IMMessage.Codec.getDataCoder(IMMessage.IMMsgType.P2PNetInfoBeat);
                    friendNetInfo = clientDataCoder.parse2T(message.getData());
                    logger.info("嗅探到对方的网络信息：" + friendNetInfo + "开始打洞");
                    new Thread(this).start();
                    break;
                //p2p嗅探打洞消息
                case P2PConnectionSniffing:
                    p2pState = State.CONNECTED;
                    logger.info("打洞成功，对方已将我穿透:" + natType);
                default:
                    logger.info(message.toString());
            }
        });
    }

    public State getP2pState() {
        return p2pState;
    }

    public NATType getNatType() {
        return natType;
    }

    public UClient getFriendNetInfo() {
        return friendNetInfo;
    }

    public int sendIMMessage2Nat(IMMessage message) throws IOException {
          return conversation.send(message,natAddress);
    }

    @Override
    public void accept(UClient uClient) {
        selfNetInfo = uClient;
        uClient.setLocalIp(conversation.getIp());
        uClient.setLocalPort(conversation.getPort());
        //开始连接
        IMMessage beat = new IMMessage();
        beat.setMsgType(IMMessage.IMMsgType.P2PNetInfoBeat);
        beat.setTime(System.currentTimeMillis());
        uClient.setUpdateTime(System.currentTimeMillis());
        beat.setData(IMMessage.Codec.getDataCoder(IMMessage.IMMsgType.P2PNetInfoBeat).parse2Binary(uClient));
        try {
            conversation.send2Center(beat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("向对方发送自己的网络信息：" + uClient);
    }
}
