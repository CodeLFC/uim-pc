package gaozhi.online.uim.ui.conversation;

import gaozhi.online.ugui.core.activity.Activity;
import gaozhi.online.ugui.core.activity.Context;
import gaozhi.online.ugui.core.activity.Intent;
import gaozhi.online.ugui.core.activity.layout.UGridBagLayout;
import gaozhi.online.ugui.core.activity.widget.*;
import gaozhi.online.ugui.core.utils.ImageUtil;
import gaozhi.online.uim.entity.Friend;
import gaozhi.online.uim.entity.Token;
import gaozhi.online.uim.im.conversation.Conversation;
import gaozhi.online.uim.im.conversation.message.IMMessage;
import gaozhi.online.uim.im.entity.UClient;
import gaozhi.online.uim.im.service.ConversationService;
import gaozhi.online.uim.im.service.IMServiceApplication;
import gaozhi.online.uim.im.service.UserPoolService;
import gaozhi.online.uim.ui.conversation.chat.IMMsgCellRender;
import gaozhi.online.uim.ui.conversation.chat.IMMsgListModel;
import gaozhi.online.uim.ui.conversation.function.UpdateFriendActivity;
import gaozhi.online.uim.utils.DateTimeUtil;
import gaozhi.online.uim.utils.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 会话界面
 * @date 2022/3/15 16:32
 */
public class ConversationActivity extends Activity implements Conversation.IMMessageConsumer, KeyListener {
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
    private URecyclerView<IMMessage> chatRecycler;
    private IMMsgListModel imMsgListModel;
    private UPanel showPanel;
    private UTextField field_msg;
    private JButton btn_send_msg;
    //bottom
    private UImageView image_real_video;
    private UImageView image_real_audio;
    private UImageView image_file;
    private UImageView image_update_remark;
    //top
    private String title;

    public ConversationActivity(Context context, Intent intent, String title, long id) {
        super(context, intent, title, id);
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
        UPanel panel = getChildPanel(1, 1);

        panel.setLayout(new BorderLayout());
        UPanel top = new UPanel();
        FlowLayout flowLayout_top = new FlowLayout();
        flowLayout_top.setAlignment(FlowLayout.LEFT);
        top.setLayout(flowLayout_top);
        panel.add(top, BorderLayout.NORTH);
        UPanel center = new UPanel();
        panel.add(center, BorderLayout.CENTER);
        UPanel right = new UPanel();
        panel.add(right, BorderLayout.EAST);
        UPanel bottom = new UPanel();
        FlowLayout flowLayout_bottom = new FlowLayout();
        flowLayout_bottom.setAlignment(FlowLayout.LEFT);
        bottom.setLayout(flowLayout_bottom);
        panel.add(bottom, BorderLayout.SOUTH);

        // -----------------------top
        //------------------------center
        UGridBagLayout gridBagLayout = new UGridBagLayout(center);
        center.setLayout(gridBagLayout);
        gridBagLayout.setWeight(1, 1);
        chatRecycler = new URecyclerView<>();
        gridBagLayout.addComponent(chatRecycler);
        gridBagLayout.setWeight(0.5, 1);
        showPanel = new UPanel();
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
        imMsgListModel = new IMMsgListModel(chatRecycler);
        chatRecycler.setListModel(imMsgListModel);
        chatRecycler.setCellRender(new IMMsgCellRender(getContext()));
        UPanel panel_enter = new UPanel();
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
        chatRecycler.setBottom(panel_enter);

        btn_send_msg.addActionListener(this);

        field_msg.addKeyListener(this);
    }

    @Override
    public void doBusiness() {
        UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
        userPoolService.getUserInfo(friend.getFriendId(), true, friendInfo -> {
            title = friend.getRemark() + "[" + friendInfo.getNick() + "] mark:" + friendInfo.getRemark();
            setTitle(title);
            setIcon(friendInfo.getHeadUrl());
        });

        for (IMMessage message : conversation.getHistoryMessage()) {
            imMsgListModel.addElement(message, true);
            logger.info("添加历史消息：" + message);
        }
        conversation.addConsumer(this);
        //清零未读消息
        conversation.setUnReadMessageCount(0);

        chatRecycler.refresh(1000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_send_msg) {
            String content = field_msg.getUText();
            if (StringUtil.isEmpty(content)) {
                UToast.show(this, getContext().getString("tip_content_is_empty"));
                return;
            }
            if (content.length() > 1000) {
                UToast.show(this, getContext().getString("tip_content_is_too_long"));
                return;
            }
            IMMessage msg = new IMMessage();
            msg.setTime(System.currentTimeMillis());
            msg.setMsgType(IMMessage.IMMsgType.TEXT);
            msg.setData(IMMessage.Codec.getDataCoder(IMMessage.IMMsgType.TEXT).parse2Binary(content));
            msg.setFromId(conversation.getSelfId());
            msg.setToId(conversation.getFriendId());
            int len = 0;
            try {
                len = conversation.sendIMMessage(msg);
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
    public void releaseResource() {
        super.releaseResource();
        chatRecycler.stopRefresh();
        conversation.removeConsumer(this);
    }

    @Override
    public void accept(IMMessage message) {
        logger.info("收到消息：" + message.toString());
        imMsgListModel.addElement(message, true);
        setTitle(title + " | p2p:" + conversation.getP2PConnectionState().getDescription());
        conversation.setUnReadMessageCount(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            //发送消息
            btn_send_msg.doClick();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
