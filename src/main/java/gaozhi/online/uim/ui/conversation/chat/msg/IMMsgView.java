package gaozhi.online.uim.ui.conversation.chat.msg;

import gaozhi.online.ugui.core.activity.widget.UPanel;
import gaozhi.online.uim.im.conversation.message.IMMessage;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/3/18 20:00
 */
public abstract class IMMsgView extends UPanel {
    abstract void bindView(IMMessage msg, boolean isSelf);
}
