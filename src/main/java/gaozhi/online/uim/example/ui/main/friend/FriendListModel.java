package gaozhi.online.uim.example.ui.main.friend;

import gaozhi.online.uim.core.activity.widget.URecyclerView;
import gaozhi.online.uim.example.entity.Friend;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 朋友适配器
 * @date 2022/4/10 13:56
 */
public class FriendListModel extends DefaultListModel<Friend> {
    private final long selfId;
    private final URecyclerView<Friend> recyclerView;
    private final Map<Long, Integer> friendIdIndexMap = new HashMap<>();

    public FriendListModel(URecyclerView<Friend> recyclerView, long selfId) {
        this.recyclerView = recyclerView;
        this.selfId = selfId;

    }

    @Override
    public synchronized void addElement(Friend element) {
        if (element.getFriendId() == selfId) {
            friendIdIndexMap.put(element.getUserid(), getSize());
        } else {
            friendIdIndexMap.put(element.getFriendId(), getSize());
        }
        super.addElement(element);
    }

    public int getIndex(long friendId) {
        if (!friendIdIndexMap.containsKey(friendId)) {
            return -1;
        }
        return friendIdIndexMap.get(friendId);
    }
}
