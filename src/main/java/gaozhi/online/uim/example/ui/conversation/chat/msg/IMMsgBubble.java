package gaozhi.online.uim.example.ui.conversation.chat.msg;

import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.widget.UImageView;
import gaozhi.online.uim.example.entity.UserInfo;
import gaozhi.online.uim.example.im.conversation.IMMessage;
import gaozhi.online.uim.example.im.service.IMServiceApplication;
import gaozhi.online.uim.example.im.conversation.message.IMMsgType;
import gaozhi.online.uim.example.im.service.UserPoolService;
import gaozhi.online.uim.example.utils.DateTimeUtil;
import gaozhi.online.uim.example.utils.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  显示消息的bubble
 * @date 2022/3/17 10:48
 */
public class IMMsgBubble extends JPanel implements Consumer<UserInfo> {
    private final Context context;
    //布局
    private FlowLayout flowLayout;
    private boolean isSelf;
    private IMMessage msg;
    //头像内容
    private Image defaultHead;
    private UImageView image_head;
    //名字
//    private JLabel label_name;
//    private JPanel panel_name;
//    private FlowLayout flowLayout_name;
    //时间
    private JLabel label_time;
    private JPanel panel_time;
    private FlowLayout flowLayout_time;
    //显示内容
    private JPanel panelMsg;
    //消息内容显示区域
    private Map<IMMsgType, IMMsgView> imMsgViewMap;

    public IMMsgBubble(Context context) {
        this.context = context;
        imMsgViewMap = new HashMap<>();
        flowLayout = new FlowLayout();
        setLayout(flowLayout);

        image_head = new UImageView();

        panelMsg = new JPanel();

        panelMsg.setLayout(new BorderLayout());
        //label_name = new JLabel();
        label_time = new JLabel();
        //flowLayout_name = new FlowLayout();
        flowLayout_time = new FlowLayout();
        //panel_name = new JPanel();
        panel_time = new JPanel();
        //panel_name.setLayout(flowLayout_name);
        panel_time.setLayout(flowLayout_time);
        //panel_name.add(label_name);
        panel_time.add(label_time);
        try {
            defaultHead = ImageUtil.getScaleImage(context.getDrawable("app_logo"), 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //注册显示不同消息类型的视图
        registerMsgView();
    }

    private void registerMsgView() {
        TextMsgView textMsgView = new TextMsgView();
        imMsgViewMap.put(IMMsgType.TEXT, textMsgView);
    }


    public void bindView(IMMessage msg) {
        this.msg = msg;
        UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
        isSelf = msg.getFromId() == userPoolService.getSelfId();
        userPoolService.getUserInfo(msg.getFromId(), false, this);
    }

    @Override
    public void accept(UserInfo showUser) {
        label_time.setText(DateTimeUtil.getChatTime(msg.getTime()));
        image_head.setImageUrl(showUser.getHeadUrl(), defaultHead, 50,false);
        if (isSelf) {
            add(panelMsg);
            add(image_head);
            //flowLayout_name.setAlignment(FlowLayout.RIGHT);
            flowLayout_time.setAlignment(FlowLayout.RIGHT);
            //label_name.setText(showUser.getNick() + "[" + showUser.getId() + "]");
        } else {
            add(image_head);
            add(panelMsg);
            //flowLayout_name.setAlignment(FlowLayout.LEFT);
            flowLayout_time.setAlignment(FlowLayout.LEFT);
            //label_name.setText(showUser.getNick());
        }

        //panelMsg.add(panel_name, BorderLayout.NORTH);

        //----------------动态加载显示内容
        IMMsgView view = imMsgViewMap.get(msg.getMsgType());
        if (view == null) {
            throw new NullPointerException("没有注册此消息类型所对应的视图");
        }
        view.bindView(msg, isSelf);
        panelMsg.add(view, BorderLayout.CENTER);
        //
        panelMsg.add(panel_time, BorderLayout.SOUTH);
    }
}
