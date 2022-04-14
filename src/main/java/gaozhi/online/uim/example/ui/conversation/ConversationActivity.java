package gaozhi.online.uim.example.ui.conversation;

import gaozhi.online.uim.core.activity.Activity;
import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.Intent;
import gaozhi.online.uim.core.activity.layout.UGridBagLayout;
import gaozhi.online.uim.core.activity.widget.UImageView;
import gaozhi.online.uim.core.activity.widget.URecyclerView;
import gaozhi.online.uim.core.activity.widget.UTextField;
import gaozhi.online.uim.example.entity.Friend;
import gaozhi.online.uim.example.entity.Token;
import gaozhi.online.uim.example.im.conversation.Conversation;
import gaozhi.online.uim.example.im.conversation.IMMessage;
import gaozhi.online.uim.example.im.conversation.message.IMMsgType;
import gaozhi.online.uim.example.im.service.ConversationService;
import gaozhi.online.uim.example.im.service.IMServiceApplication;
import gaozhi.online.uim.example.im.service.UserPoolService;
import gaozhi.online.uim.example.ui.conversation.chat.IMMsgCellRender;
import gaozhi.online.uim.example.ui.conversation.chat.IMMsgListModel;
import gaozhi.online.uim.example.ui.conversation.function.UpdateFriendActivity;
import gaozhi.online.uim.example.utils.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 会话界面
 * @date 2022/3/15 16:32
 */
public class ConversationActivity extends Activity implements Conversation.IMMessageConsumer {
    //intent
    private static final String INTENT_TOKEN = "token";
    private static final String INTENT_FRIEND = "friend";
    private Token token;
    private Friend friend;
    private Conversation conversation;
    //param
    private int bottom_height;

    //ui
    //center
    private URecyclerView<IMMessage> chatList;
    private IMMsgListModel imMsgListModel;
    private JPanel showPanel;
    private UTextField field_msg;
    private JButton btn_send_msg;
    //bottom
    private UImageView image_real_video;
    private UImageView image_real_audio;
    private UImageView image_file;
    private UImageView image_update_remark;

    public ConversationActivity(Context context, Intent intent, String title) {
        super(context, intent, title);
    }

    @Override
    public void initParam(Intent intent) {
        token = intent.getObject(INTENT_TOKEN);
        friend = intent.getObject(INTENT_FRIEND);
        conversation = IMServiceApplication.getInstance().getServiceInstance(ConversationService.class).getConversation(friend.getFriendId());
        bottom_height = getContext().getDimen("main_bottom_height", 50);
    }

    @Override
    public void initUI() {
        //设置大小
        Dimension screenSize = screenSize();
        setLocation(screenSize.width / 4, screenSize.height / 4);
        setSize(screenSize.width / 2, screenSize.height / 2);

        setRootGridLayout(1, 1);
        JPanel panel = getChildPanel(1, 1);

        panel.setLayout(new BorderLayout());
        JPanel top = new JPanel();
        FlowLayout flowLayout_top = new FlowLayout();
        flowLayout_top.setAlignment(FlowLayout.LEFT);
        top.setLayout(flowLayout_top);
        panel.add(top, BorderLayout.NORTH);
        JPanel center = new JPanel();
        panel.add(center, BorderLayout.CENTER);
        JPanel right = new JPanel();
        panel.add(right, BorderLayout.EAST);
        JPanel bottom = new JPanel();
        FlowLayout flowLayout_bottom = new FlowLayout();
        flowLayout_bottom.setAlignment(FlowLayout.LEFT);
        bottom.setLayout(flowLayout_bottom);
        panel.add(bottom, BorderLayout.SOUTH);

        // -----------------------top
        //------------------------center
        UGridBagLayout gridBagLayout = new UGridBagLayout(center);
        center.setLayout(gridBagLayout);
        gridBagLayout.setWeight(1, 1);
        chatList = new URecyclerView<>();
        gridBagLayout.addComponent(chatList);
        gridBagLayout.setWeight(0.5, 1);
        showPanel = new JPanel();
        //暂时屏蔽右半部分
        // gridBagLayout.addComponent(showPanel);
        //------------------------bottom
        try {
            image_real_video = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("conversation_real_video"), bottom_height / 2));
            image_real_audio = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("conversation_real_audio"), bottom_height / 2));
            image_file = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("conversation_file"), bottom_height / 2));
            image_update_remark = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("bottom_info"), bottom_height / 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bottom.add(image_real_video);
        bottom.add(image_real_audio);
        bottom.add(image_file);
        bottom.add(new JLabel("|"));
        bottom.add(image_update_remark);
        //onclick
        image_file.setActionListener(this);
        image_real_audio.setActionListener(this);
        image_real_video.setActionListener(this);
        image_update_remark.setActionListener(this);
        initCenterLayout();
    }

    //初始化中间部分的布局
    private void initCenterLayout() {
        imMsgListModel = new IMMsgListModel(chatList);
        chatList.setListModel(imMsgListModel);
        chatList.setCellRender(new IMMsgCellRender(getContext()));
        JPanel panel_enter = new JPanel();
        UGridBagLayout gridBagLayout = new UGridBagLayout(panel_enter);
        gridBagLayout.setWeight(2, 0);
        field_msg = new UTextField();
        field_msg.setHint(getContext().getString("msg_enter_tip"));
        gridBagLayout.addComponent(field_msg);
        gridBagLayout.setWeight(0.01, 0);
        gridBagLayout.addComponent(new Container());
        btn_send_msg = new JButton(getContext().getString("send"));
        gridBagLayout.setWeight(0.1, 0);
        gridBagLayout.addComponent(btn_send_msg);
        gridBagLayout.setWeight(0.01, 0);
        gridBagLayout.addComponent(new Container());
        chatList.setBottom(panel_enter);

        btn_send_msg.addActionListener(this);
    }

    @Override
    public void doBusiness() {
        UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
        userPoolService.getUserInfo(friend.getFriendId(), true, friendInfo -> {
            setTitle(friend.getRemark() + "[" + friendInfo.getNick() + "] mark:" + friendInfo.getRemark());
            setIcon(friendInfo.getHeadUrl());
        });
        logger.info("添加历史消息");
        for (IMMessage message : conversation.getHistoryMessage()) {
            imMsgListModel.addElement(message, true);
            logger.info("添加历史消息："+message);
        }
        conversation.addConsumer(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_send_msg) {
            IMMessage msg = new IMMessage();
            msg.setTime(System.currentTimeMillis());
            msg.setMsgType(IMMsgType.TEXT);
            msg.setData(IMMessage.Codec.getDataCoder(IMMsgType.TEXT).parse2Binary(field_msg.getUText()));
            msg.setFromId(conversation.getSelfId());
            msg.setToId(conversation.getFriendId());
            int len = 0;
            try {
                len = conversation.send2Center(msg);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (len > 0) {
                imMsgListModel.addElement(msg, true);
                field_msg.setText(null);
            }
            return;
        }
        if (e.getSource() == image_file) {
            logger.info("发送文件");
            return;
        }
        if (e.getSource() == image_real_video) {
            logger.info("视频通话");
            return;
        }
        if (e.getSource() == image_real_audio) {
            logger.info("音频通话");
        }
        if (e.getSource() == image_update_remark) {
            UpdateFriendActivity.startActivity(getContext(), token, friend);
        }
    }

    public static void startActivity(Context context, Token token, Friend friend) {
        Intent intent = new Intent();
        intent.put(INTENT_TOKEN, token);
        intent.put(INTENT_FRIEND, friend);
        context.startActivity(ConversationActivity.class, intent);
    }

    @Override
    public void accept(IMMessage message) {
        logger.info("收到消息：" + message.toString());
        imMsgListModel.addElement(message, true);
    }
}