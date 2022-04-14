package gaozhi.online.uim.example.ui.main.friend;

import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.widget.URecyclerCellRender;
import gaozhi.online.uim.example.entity.Friend;
import gaozhi.online.uim.example.ui.main.friend.view.FriendBubble;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 朋友渲染
 * @date 2022/4/10 13:58
 */
public class FriendCellRender extends URecyclerCellRender<Friend> {
    private final Context context;
    private final Map<Integer, FriendViewHolder> viewHolderMap = new HashMap<>();

    public FriendCellRender(Context context) {
        this.context = context;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Friend> list, Friend value, int index, boolean isSelected, boolean cellHasFocus) {
        if (!viewHolderMap.containsKey(index)) {
            FriendViewHolder friendViewHolder = new FriendViewHolder(context);
            viewHolderMap.put(index, friendViewHolder);
        }
        viewHolderMap.get(index).bindView(value);
        return viewHolderMap.get(index);
    }

    private static class FriendViewHolder extends JPanel {
        private final FriendBubble friendBubble;

        public FriendViewHolder(Context context) {
            setLayout(new BorderLayout());
            setBorder(new LineBorder(Color.WHITE));
            friendBubble = new FriendBubble(context);
            add(friendBubble);
            System.out.println(" FriendCellRender 渲染 ");
        }

        /**
         * @description: TODO 刷新频率非常快，不要在这里创建对象
         * @author LiFucheng
         * @date 2022/3/18 19:09
         * @version 1.0
         */
        public void bindView(Friend friend) {
            friendBubble.bindView(friend);
        }
    }
}
