package gaozhi.online.uim.example.ui.conversation.chat;

import gaozhi.online.uim.core.activity.widget.URecyclerView;
import gaozhi.online.uim.example.im.conversation.IMMessage;

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

    public void addElement(IMMessage element,boolean move2End) {
        super.addElement(element);
        if(move2End) {
            recyclerView.move2End();
        }
    }
}
