package gaozhi.online.uim.example.ui.conversation.chat.msg;

import gaozhi.online.uim.example.im.conversation.IMMessage;

import javax.swing.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/3/18 20:00
 */
public abstract class IMMsgView extends JPanel {
    abstract void bindView(IMMessage msg, boolean isSelf);
}
