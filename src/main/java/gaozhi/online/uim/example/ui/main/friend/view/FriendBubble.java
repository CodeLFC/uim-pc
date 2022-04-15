package gaozhi.online.uim.example.ui.main.friend.view;

import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.widget.UImageView;
import gaozhi.online.uim.core.activity.widget.ULabel;
import gaozhi.online.uim.core.activity.widget.UPanel;
import gaozhi.online.uim.core.resource.FontClass;
import gaozhi.online.uim.core.utils.ImageUtil;
import gaozhi.online.uim.example.entity.Friend;
import gaozhi.online.uim.example.entity.UserInfo;
import gaozhi.online.uim.example.im.conversation.Conversation;
import gaozhi.online.uim.example.im.conversation.IMMessage;
import gaozhi.online.uim.example.im.service.ConversationService;
import gaozhi.online.uim.example.im.service.IMServiceApplication;
import gaozhi.online.uim.example.im.service.UserPoolService;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/4/10 14:02
 */
public class FriendBubble extends UPanel implements Consumer<UserInfo> {
    private Context context;
    private Image defaultHead;
    private UImageView head;
    private ULabel label_name;
    private ULabel label_remark;
    private ULabel label_friendship;
    private ULabel label_unread;
    private Conversation conversation;

    public FriendBubble(Context context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 10));
        try {
            defaultHead = ImageUtil.getScaleImage(context.getDrawable("app_logo"), 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
        head = new UImageView(ImageUtil.getScaleImage(defaultHead, 50));
        head.setBorder(new LineBorder(new Color(217,217,25),2));
        label_name = new ULabel();
        label_name.setFont(FontClass.FontExample.SONG14.getFont());
        label_remark = new ULabel();
        label_remark.setFont(FontClass.FontExample.SONG14.getFont());
        label_friendship = new ULabel();
        label_unread = new ULabel();
        label_unread.setForeground(Color.RED);
        label_friendship.setForeground(Color.RED);
        label_remark.setForeground(Color.GRAY);
        UPanel center_panel = new UPanel();
        center_panel.setLayout(new BorderLayout());
        center_panel.setBorder(new EmptyBorder(0, 10, 0, 10));
        center_panel.add(label_friendship,BorderLayout.WEST);
        center_panel.add(label_name,BorderLayout.CENTER);
        center_panel.add(label_remark,BorderLayout.SOUTH);
        add(head, BorderLayout.WEST);
        add(center_panel, BorderLayout.CENTER);
        add(label_unread, BorderLayout.EAST);
    }

    public void bindView(Friend friend) {

        UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
        long friendId = friend.getFriendId();
        if (friendId == userPoolService.getSelfId()) {
            friendId = friend.getUserid();
        }else{
            label_friendship.setText(friend.getRemark());
        }
        //
        if (conversation == null) {
            conversation = IMServiceApplication.getInstance().getServiceInstance(ConversationService.class).getConversation(friendId);
        }
        label_unread.setText(null);
        List<IMMessage> history = conversation.getHistoryMessage();
        if (!history.isEmpty()) {
            IMMessage message = history.get(history.size() - 1);
            IMMessage.DataCoder<?> dataCoder = IMMessage.Codec.getDataCoder(message.getMsgType());
            if (dataCoder == null) {
                throw new NullPointerException("IMMessage.DataCoder<?> dataCoder == null");
            }
            label_remark.setText(dataCoder.parse2Tip(message.getData()));
            label_unread.setText("" + conversation.getUnReadMessageCount());
            System.out.println("刷新-未读信息:"+conversation.getUnReadMessageCount());
        }
        userPoolService.getUserInfo(friendId, false, this);
    }

    @Override
    public void accept(UserInfo userInfo) {
        head.setImageUrl(userInfo.getHeadUrl(), defaultHead, 50, false);
        label_name.setText(userInfo.getNick());
        label_remark.setText(userInfo.getRemark());
        System.out.println("刷新-用户信息:"+conversation.getUnReadMessageCount());
    }
}
