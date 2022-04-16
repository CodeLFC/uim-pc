package gaozhi.online.uim.ui.conversation.chat;

import gaozhi.online.ugui.core.activity.Context;
import gaozhi.online.ugui.core.activity.widget.UPanel;
import gaozhi.online.ugui.core.activity.widget.URecyclerCellRender;
import gaozhi.online.uim.im.conversation.IMMessage;
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
    private final Map<Integer, IMMessageViewHolder> viewHolderMap = new HashMap<>();

    public IMMsgCellRender(Context context) {
        this.context = context;
    }


    @Override
    public Component getListCellRendererComponent(JList<? extends IMMessage> list, IMMessage value, int index, boolean isSelected, boolean cellHasFocus) {
        if (!viewHolderMap.containsKey(index)) {
            IMMessageViewHolder messageViewHolder = new IMMessageViewHolder(context);
            viewHolderMap.put(index, messageViewHolder);
        }
        IMMessageViewHolder component = viewHolderMap.get(index);
        component.bindView(value);
        return component;
    }

    private static class IMMessageViewHolder extends UPanel {
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

        /**
         * @description: TODO 刷新频率非常快，不要在这里创建对象
         * @author LiFucheng
         * @date 2022/3/18 19:09
         * @version 1.0
         */
        public void bindView(IMMessage msg) {
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
