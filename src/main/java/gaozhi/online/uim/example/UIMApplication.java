package gaozhi.online.uim.example;

import gaozhi.online.uim.core.Application;
import gaozhi.online.uim.core.activity.Intent;
import gaozhi.online.uim.core.resource.Resources;
import gaozhi.online.uim.example.im.conversation.IMMessage;
import gaozhi.online.uim.example.im.conversation.message.IMTextDataCoder;
import gaozhi.online.uim.example.im.service.*;
import gaozhi.online.uim.example.ui.LoginActivity;
import gaozhi.online.uim.example.utils.FileUtil;
import org.pushingpixels.substance.api.skin.*;

import java.io.IOException;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 例子
 * @date 2022/1/25 23:31
 */
public class UIMApplication extends Application {
    private final IMServiceApplication serviceApplication;

    public UIMApplication(Resources resources) {
        super(resources);
        serviceApplication = IMServiceApplication.getInstance();
        registerMsgType();
    }

    private void registerMsgType() {
        //注册文本消息类型编解码器
        IMMessage.Codec.registerDataCoder(new IMTextDataCoder());
    }

    public void startService() {
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
        try {
            UIMApplication application = new UIMApplication(new Resources("config.application"));
            application.startService();
            application.configSkin(new BusinessBlueSteelSkin());
            application.startActivity(LoginActivity.class);
        } catch (Exception e) {
            FileUtil.write("exception.txt", e.getMessage(), true);
        }
    }
}
