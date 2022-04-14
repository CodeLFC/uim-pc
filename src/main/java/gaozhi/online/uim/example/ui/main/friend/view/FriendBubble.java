package gaozhi.online.uim.example.ui.main.friend.view;

import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.widget.UImageView;
import gaozhi.online.uim.core.activity.widget.ULabel;
import gaozhi.online.uim.example.entity.Friend;
import gaozhi.online.uim.example.entity.UserInfo;
import gaozhi.online.uim.example.im.service.IMServiceApplication;
import gaozhi.online.uim.example.im.service.UserPoolService;
import gaozhi.online.uim.example.utils.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/4/10 14:02
 */
public class FriendBubble extends JPanel implements Consumer<UserInfo>{
    private Context context;
    private Image defaultHead;
    private UImageView head;
    private ULabel label_name;
    private ULabel label_remark;
    private ULabel label_friendship;
    public FriendBubble(Context context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0,0,0,10));
        try {
            defaultHead = ImageUtil.getScaleImage(context.getDrawable("app_logo"), 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
        head = new UImageView(ImageUtil.getScaleImage(defaultHead, 50));
        label_name = new ULabel();
        label_remark = new ULabel();
        label_friendship = new ULabel();
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.add(label_name);
        center.add(label_remark, BorderLayout.SOUTH);
        center.add(label_friendship, BorderLayout.EAST);
        add(head, BorderLayout.WEST);
        add(center);
    }

    public void bindView(Friend friend) {
        label_friendship.setText(friend.getRemark());
        UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
        long friendId = friend.getFriendId();
        if (friendId == userPoolService.getSelfId()) {
            friendId = friend.getUserid();
        }
        userPoolService.getUserInfo(friendId, false, this);
    }

    @Override
    public void accept(UserInfo userInfo) {
        head.setImageUrl(userInfo.getHeadUrl(), defaultHead, 50, false);
        label_name.setText(userInfo.getNick());
        label_remark.setText(userInfo.getRemark());
    }
}
