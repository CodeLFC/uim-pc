package gaozhi.online.uim;

import gaozhi.online.ugui.core.Application;
import gaozhi.online.ugui.core.resource.StyleClass;
import gaozhi.online.uim.im.conversation.message.IMMessage;
import gaozhi.online.uim.im.conversation.message.IMP2PNetInfoBeat;
import gaozhi.online.uim.im.conversation.message.IMString;
import gaozhi.online.uim.im.service.*;
import gaozhi.online.uim.ui.LoginActivity;
import gaozhi.online.uim.utils.FileUtil;

import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 例子
 * @date 2022/1/25 23:31
 */
public class UIMApplication extends Application {
    private final IMServiceApplication serviceApplication;

    public UIMApplication() {
        super();
        serviceApplication = IMServiceApplication.getInstance();
        registerMsgType();
    }

    private void registerMsgType() {
        //注册文本消息类型编解码器
        IMMessage.Codec.registerDataCoder(new IMString.DataCoder());
        //注册p2p心跳类型消息编解码器
        IMMessage.Codec.registerDataCoder(new IMP2PNetInfoBeat.DataCoder());
    }

    public void startService() throws UnknownHostException {
        //用户池服务
        UserPoolService userPoolService = new UserPoolService();
        //底层消息服务
        IMMsgService imMsgService = new IMMsgService(getDimen("im_port", -1), getDimen("im_mtu", 572));
        //CS心跳服务
        CSBeatService csBeatService = new CSBeatService();
        //会话服务
        ConversationService conversationService = new ConversationService();

        serviceApplication.registerService(userPoolService);
        serviceApplication.registerService(imMsgService);
        serviceApplication.registerService(csBeatService);
        serviceApplication.registerService(conversationService);
    }

    public static void main(String[] args) throws IOException {
        StyleClass.setFont(StyleClass.FontExample.SONG16.getFont());
        StyleClass.setForeground(Color.WHITE);
        StyleClass.setBackground(Color.GRAY);
        StyleClass.setPanelColor(Color.DARK_GRAY);
        try {
            UIMApplication application = new UIMApplication();
            application.startService();
            application.startActivity(LoginActivity.class);
        } catch (Exception e) {
            FileUtil.write("exception.txt", e.getMessage(), true);
        }
    }
}
