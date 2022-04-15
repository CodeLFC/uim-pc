package gaozhi.online.uim.example.ui;

import com.google.gson.Gson;
import gaozhi.online.uim.core.activity.Activity;
import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.Intent;
import gaozhi.online.uim.core.activity.widget.UTextField;
import gaozhi.online.uim.core.activity.widget.UToast;
import gaozhi.online.uim.core.net.Result;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.Friend;
import gaozhi.online.uim.example.entity.Token;
import gaozhi.online.uim.example.entity.UserInfo;
import gaozhi.online.uim.example.service.friend.AddAttentionService;
import gaozhi.online.uim.example.service.friend.GetFriendService;
import gaozhi.online.uim.example.service.user.UpdateUserInfoService;
import gaozhi.online.uim.example.ui.conversation.ConversationActivity;
import gaozhi.online.uim.example.utils.DateTimeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 修改个人信息
 * @date 2022/3/31 1:36
 * <p>
 * CREATE TABLE `user_info` (
 * `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户账号',
 * `head_url` varchar(2083) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像地址',
 * `nick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '西风吹瘦马新用户' COMMENT '昵称',
 * `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '快乐p2p' COMMENT '备注',
 * `gender` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'male' COMMENT '性别（male/female）',
 * `birth` bigint DEFAULT '0' COMMENT '出生日期',
 * `gps` varchar(255) DEFAULT NULL COMMENT 'gps坐标',
 * `cell_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
 * `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '电话',
 * `wechat` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信号',
 * `qq` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'qq号',
 * `email` varchar(320) DEFAULT NULL COMMENT '邮箱',
 * `visible` varchar(1000) DEFAULT NULL COMMENT '各个属性是否可见的json串',
 * `create_time` bigint DEFAULT '0' COMMENT '信息创建时间',
 * `update_time` bigint DEFAULT '0' COMMENT '信息更新时间',
 * `ban_time` bigint DEFAULT '0' COMMENT '用户封禁到期时间',
 * `status` int DEFAULT '0' COMMENT '用户状态',
 * `vip` int DEFAULT '0' COMMENT 'vip',
 * PRIMARY KEY (`id`)
 * ) ENGINE=MyISAM AUTO_INCREMENT=2147483652 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
public class UserInfoActivity extends Activity implements ApiRequest.ResultHandler {
    private static final int COLS = 6;
    private static final int ROWS = 11;

    private static final String INTENT_USER_TOKEN = "token";
    private static final String INTENT_USER_INFO = "userinfo";
    private Token token;
    private UserInfo userInfo;
    //ui head_url,nick,remark,gender,birth,gps,cell_phone,phone,wechat,qq,visible,email
    private UTextField static_id;
    private UTextField static_vip;
    private UTextField static_status;
    private UTextField static_create_time;
    private UTextField static_update_time;

    private UTextField text_nick;
    private UTextField text_remark;
    private UTextField text_head;
    private JComboBox<UserInfo.Gender> text_gender;
    private UTextField text_birth;
    private UTextField text_gps;
    private UTextField text_cell_phone;
    private UTextField text_phone;
    private UTextField text_wechat;
    private UTextField text_qq;
    private UTextField text_email;

    private JButton btn_update;
    //friend
    private JButton btn_chat;
    //service
    private final Gson gson = new Gson();
    private UpdateUserInfoService updateUserInfoService;
    private GetFriendService getFriendService;
    private AddAttentionService addAttentionService;
    private Friend friend;

    public UserInfoActivity(Context context, Intent intent, String title, long id) {
        super(context, intent, title, id);
    }

    @Override
    public void initParam(Intent intent) {
        token = intent.getObject(INTENT_USER_TOKEN);
        userInfo = intent.getObject(INTENT_USER_INFO);
        updateUserInfoService = new UpdateUserInfoService(this);
        getFriendService = new GetFriendService(this);
        addAttentionService = new AddAttentionService(this);
    }

    @Override
    public void initUI() {
        setResizable(false);
        setRootGridLayout(ROWS, COLS);
        setVGap(10);
        setHGap(10);
        static_id = new UTextField();
        static_vip = new UTextField();
        static_status = new UTextField();
        static_create_time = new UTextField();
        static_update_time = new UTextField();
        static_id.setEnabled(false);
        static_vip.setEnabled(false);
        static_status.setEnabled(false);
        static_update_time.setEnabled(false);
        static_create_time.setEnabled(false);

        text_nick = new UTextField();
        text_remark = new UTextField();
        text_head = new UTextField();
        text_gender = new JComboBox<>();
        text_birth = new UTextField();
        text_gps = new UTextField();
        text_cell_phone = new UTextField();
        text_phone = new UTextField();
        text_wechat = new UTextField();
        text_qq = new UTextField();
        text_email = new UTextField();


        for (int startColumn = 1; startColumn < COLS; startColumn++) {
            for (int startRow = 1; startRow <= ROWS; startRow++) {
                getChildPanel(startRow, startColumn).setLayout(new BorderLayout());
            }
        }

        int row = 2;
        int col = 2;
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_nick")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_id")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_remark")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_vip")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_status")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_head")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_gender")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_birth")), BorderLayout.EAST);
        row = 2;
        col = 3;
        getChildPanel(row++, col).add(text_nick);
        getChildPanel(row++, col).add(static_id);
        getChildPanel(row++, col).add(text_remark);
        getChildPanel(row++, col).add(static_vip);
        getChildPanel(row++, col).add(static_status);
        getChildPanel(row++, col).add(text_head);
        getChildPanel(row++, col).add(text_gender);
        getChildPanel(row++, col).add(text_birth);
        btn_update = new JButton();
        btn_update.setText(getContext().getString("update_info"));
        getChildPanel(row, col).add(btn_update);

        row = 2;
        col = 4;
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_gps")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_cell_phone")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_phone")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_we_chat")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_qq")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_email")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_update_time")), BorderLayout.EAST);
        getChildPanel(row++, col).add(new JLabel(getContext().getString("info_create_time")), BorderLayout.EAST);
        row = 2;
        col = 5;
        getChildPanel(row++, col).add(text_gps);
        getChildPanel(row++, col).add(text_cell_phone);
        getChildPanel(row++, col).add(text_phone);
        getChildPanel(row++, col).add(text_wechat);
        getChildPanel(row++, col).add(text_qq);
        getChildPanel(row++, col).add(text_email);
        getChildPanel(row++, col).add(static_update_time);
        getChildPanel(row++, col).add(static_create_time);

        btn_chat = new JButton();
        btn_chat.setText(getContext().getString("chat"));
        getChildPanel(row, col).add(btn_chat);

        btn_update.addActionListener(this);
        btn_chat.addActionListener(this);
    }

    @Override
    public void doBusiness() {

        for (UserInfo.Gender e : UserInfo.Gender.values()) {
            text_gender.addItem(e);
        }
        if (userInfo.getId() != token.getUserid()) {
            btn_update.setEnabled(false);
            setTitle(getContext().getString("friend"));
            getFriendService.request(token, token.getUserid(), userInfo.getId());
        } else {
            btn_chat.setEnabled(false);
            setTitle(getContext().getString("self"));
        }

        setIcon(userInfo.getHeadUrl());
        static_id.setText("" + userInfo.getId());
        static_vip.setText("" + userInfo.getVip());
        static_status.setText(UserInfo.Status.getStatus(userInfo.getStatus()).getDescription());
        static_update_time.setText(DateTimeUtil.getBirthTime(userInfo.getUpdateTime()));
        static_create_time.setText(DateTimeUtil.getBirthTime(userInfo.getCreateTime()));
        text_head.setText(userInfo.getHeadUrl());
        text_nick.setText(userInfo.getNick());
        text_remark.setText(userInfo.getRemark());

        text_gender.setSelectedItem(UserInfo.Gender.getGender(userInfo.getGender()));
        text_birth.setText(DateTimeUtil.getBirthTime(userInfo.getBirth()));
        text_gps.setText(userInfo.getGps());
        text_cell_phone.setText(userInfo.getCellPhone());
        text_phone.setText(userInfo.getPhone());
        text_wechat.setText(userInfo.getWechat());
        text_qq.setText(userInfo.getQq());
        text_email.setText(userInfo.getEmail());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_chat) {
            if (friend != null) {
                startChat(friend);
            } else {
                //关注用户
                addAttentionService.request(token, userInfo.getId());
            }
            return;
        }
        userInfo.setNick(text_nick.getUText());
        userInfo.setRemark(text_remark.getUText());
        userInfo.setHeadUrl(text_head.getUText());
        userInfo.setGps(text_gps.getUText());
        userInfo.setCellPhone(text_cell_phone.getUText());
        userInfo.setPhone(text_phone.getUText());
        userInfo.setWechat(text_wechat.getUText());
        userInfo.setQq(text_qq.getUText());
        userInfo.setEmail(text_email.getUText());
        userInfo.setGender(((UserInfo.Gender) text_gender.getSelectedItem()).getKey());
        try {
            userInfo.setBirth(DateTimeUtil.analyseBirthTime(text_birth.getUText()));
        } catch (ParseException ex) {
            UToast.show(this, ex.getMessage());
            return;
        }
        if (e.getSource() == btn_update) {
            updateUserInfoService.request(token, userInfo);
        }
    }

    /**
     * @description: TODO 打开聊天界面
     * @author LiFucheng
     * @date 2022/4/13 16:35
     * @version 1.0
     */
    private void startChat(Friend friend) {
        ConversationActivity.startActivity(getContext(), token, friend);
        dispose();
    }

    public static void startActivity(Context context, Token token, UserInfo userInfo) {
        Intent intent = new Intent();
        intent.put(INTENT_USER_INFO, userInfo);
        intent.put(INTENT_USER_TOKEN, token);
        Activity activity = context.getActivityList().getActivity(userInfo.getId());
        if (activity == null) {
            context.startActivity(UserInfoActivity.class, intent, userInfo.getId());
        } else {
            activity.showActivity();
        }
    }

    @Override
    public void start(int id) {
        if (id == updateUserInfoService.getId()) {
            btn_update.setText(getContext().getString("progressing"));
        }
    }

    @Override
    public void handle(int id, Result result) {
        if (id == updateUserInfoService.getId()) {
            btn_update.setText(getContext().getString("update_info"));
            userInfo = gson.fromJson(result.getData(), UserInfo.class);
            UToast.show(this, getContext().getString("success"));
            return;
        }
        if (id == getFriendService.getId()) {
            friend = gson.fromJson(result.getData(), Friend.class);
            setTitle(getTitle() + "  " + friend.getRemark());
        }
        if (id == addAttentionService.getId()) {
            Friend friend = gson.fromJson(result.getData(), Friend.class);
            UToast.show(this, getContext().getString("attention_success"));
            startChat(friend);
        }
    }

    @Override
    public void error(int id, int code, String message) {
        UToast.show(this, code + message);
        if (id == updateUserInfoService.getId()) {
            btn_update.setText(getContext().getString("update_info"));
            return;
        }
        if (id == addAttentionService.getId()) {
            btn_chat.setText(getContext().getString("chat"));
        }
    }
}
