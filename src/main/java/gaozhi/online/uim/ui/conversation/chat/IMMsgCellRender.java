package gaozhi.online.uim.ui.conversation.chat;

import gaozhi.online.ugui.core.activity.Context;
import gaozhi.online.ugui.core.activity.widget.UPanel;
import gaozhi.online.ugui.core.activity.widget.URecyclerCellRender;
import gaozhi.online.uim.im.conversation.message.IMMessage;
import gaozhi.online.uim.im.service.IMServiceApplication;
import gaozhi.online.uim.im.service.UserPoolService;
import gaozhi.online.uim.ui.conversation.chat.msg.IMMsgBubble;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO   消息渲染
 * @date 2022/3/17 10:33
 */
public class IMMsgCellRender extends URecyclerCellRender<IMMessage> {

    private final Context context;

    public IMMsgCellRender(Context context) {
        this.context = context;
    }

    @Override
    protected ViewHolder<IMMessage> createViewHolder() {
        return new IMMessageViewHolder(context);
    }

    private static class IMMessageViewHolder extends ViewHolder<IMMessage> {
        private final IMMsgBubble msgBubble;
        //布局
        private final FlowLayout flowLayout;

        public IMMessageViewHolder(Context context) {
            msgBubble = new IMMsgBubble(context);
            flowLayout = new FlowLayout();
            setLayout(flowLayout);
            add(msgBubble);
            System.out.println(" IMMsgCellRender 渲染 ");
        }

        @Override
        public void bindView(int i, boolean b, boolean b1, IMMessage msg) {
            UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
            if (msg.getFromId() == userPoolService.getSelfId()) {
                flowLayout.setAlignment(FlowLayout.RIGHT);
            } else {
                flowLayout.setAlignment(FlowLayout.LEFT);
            }
            msgBubble.bindView(msg);
        }
    }
}
