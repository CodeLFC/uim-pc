package gaozhi.online.uim.ui.main.friend;

import gaozhi.online.ugui.core.activity.Context;
import gaozhi.online.ugui.core.activity.widget.UPanel;
import gaozhi.online.ugui.core.activity.widget.URecyclerCellRender;
import gaozhi.online.uim.entity.Friend;
import gaozhi.online.uim.ui.main.friend.view.FriendBubble;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    public FriendCellRender(Context context) {
        this.context = context;
    }

    @Override
    protected ViewHolder<Friend> createViewHolder() {
        return new FriendViewHolder(context);
    }

    public static class FriendViewHolder extends ViewHolder<Friend> {
        private final FriendBubble friendBubble;

        public FriendViewHolder(Context context) {
            setLayout(new BorderLayout());
            //setBorder(new LineBorder(Color.GRAY));
            setBorder(new EmptyBorder(5,5,5,5));
            friendBubble = new FriendBubble(context);
            add(friendBubble);
            System.out.println(" FriendCellRender 渲染 ");
        }

        @Override
        public void bindView(int i, boolean b, boolean b1, Friend friend) {
            friendBubble.bindView(friend);
        }
    }
}
