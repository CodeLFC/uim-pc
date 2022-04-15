package gaozhi.online.uim.example.im.service;

import gaozhi.online.ubtb.core.net.UCommunicationType;
import gaozhi.online.uim.example.im.conversation.Conversation;
import gaozhi.online.uim.example.im.entity.IMMsg;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  会话服务 创建会话，销毁会话
 * @date 2022/3/22 15:07
 */
public class ConversationService implements UService, IMMsgService.IMMsgConsumer {

    //缓存会话
    private final Map<Long, Conversation> conversationMap = new ConcurrentHashMap<>();
    private IMMsgService imMsgService;
    private UserPoolService userPoolService;

    @Override
    public void setIMApplication(IMServiceApplication imApplication) {
        userPoolService = imApplication.getServiceInstance(UserPoolService.class);
        imMsgService = imApplication.getServiceInstance(IMMsgService.class);
        imMsgService.addIMMsgConsumer(this);
    }

    @Override
    public void startService() {

    }

    @Override
    public void closeService() {
          conversationMap.clear();
    }

    public Conversation getConversation(long friendId) {
        if (!containsConversation(friendId)) {
            conversationMap.put(friendId, new Conversation(userPoolService.getSelfId(), friendId, imMsgService));
        }
        return conversationMap.get(friendId);
    }

    public boolean containsConversation(long fromId) {
        return conversationMap.containsKey(fromId);
    }

    @Override
    public void accept(IMMsg msg) {
        UCommunicationType communicationType = UCommunicationType.getType(msg.getFromId(), msg.getToId());
        if (communicationType != UCommunicationType.C2SUBS && communicationType != UCommunicationType.C2C) {
            return;
        }
        //获取对话消费
        getConversation(msg.getFromId()).accept(msg);
    }
}
