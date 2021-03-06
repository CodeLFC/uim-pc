package gaozhi.online.uim.ui.conversation.chat;

import gaozhi.online.ugui.core.activity.widget.URecyclerView;
import gaozhi.online.uim.im.conversation.message.IMMessage;

import javax.swing.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 聊天列表
 * @date 2022/3/17 10:26
 */
public class IMMsgListModel extends DefaultListModel<IMMessage> {
    private final URecyclerView<IMMessage> recyclerView;

    public IMMsgListModel(URecyclerView<IMMessage> recyclerView) {
        this.recyclerView = recyclerView;
    }

    public synchronized void addElement(IMMessage element, boolean move2End) {
        //p2p消息
        if (element.getMsgType() == IMMessage.IMMsgType.P2PNetInfoBeat||element.getMsgType() == IMMessage.IMMsgType.P2PConnectionSniffing) {
            return;
        }
        super.addElement(element);
        if (move2End) {
            recyclerView.move2End();
        }
    }
}
