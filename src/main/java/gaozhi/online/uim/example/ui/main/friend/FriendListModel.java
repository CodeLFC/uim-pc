package gaozhi.online.uim.example.ui.main.friend;

import gaozhi.online.uim.core.activity.widget.URecyclerView;
import gaozhi.online.uim.example.entity.Friend;

import javax.swing.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 朋友适配器
 * @date 2022/4/10 13:56
 */
public class FriendListModel extends DefaultListModel<Friend> {
    private final URecyclerView<Friend> recyclerView;

    public FriendListModel(URecyclerView<Friend> recyclerView) {
        this.recyclerView = recyclerView;
    }
}
