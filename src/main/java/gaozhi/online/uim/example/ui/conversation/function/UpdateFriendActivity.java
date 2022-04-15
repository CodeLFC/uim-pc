package gaozhi.online.uim.example.ui.conversation.function;

import gaozhi.online.uim.core.activity.Activity;
import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.Intent;
import gaozhi.online.uim.core.activity.widget.UTextField;
import gaozhi.online.uim.core.activity.widget.UToast;
import gaozhi.online.uim.core.net.Result;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.Friend;
import gaozhi.online.uim.example.entity.Token;
import gaozhi.online.uim.example.service.friend.DeleteFriendService;
import gaozhi.online.uim.example.service.friend.UpdateFriendService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 修改朋友关系
 * @date 2022/4/13 17:03
 */
public class UpdateFriendActivity extends Activity implements ApiRequest.ResultHandler {
    private static final int COLS = 3;
    private static final int ROWS = 8;
    //intent
    private static final String INTENT_TOKEN = "token";
    private static final String INTENT_FRIEND = "friend";
    private Token token;
    private Friend friend;

    //ui
    private UTextField text_remark;
    private JButton btn_update;
    private JButton btn_delete;
    //service
    private UpdateFriendService updateFriendService;
    private DeleteFriendService deleteFriendService;

    public UpdateFriendActivity(Context context, Intent intent, String title,long id) {
        super(context, intent, title,id);
    }

    @Override
    public void initParam(Intent intent) {
        token = intent.getObject(INTENT_TOKEN);
        friend = intent.getObject(INTENT_FRIEND);
        updateFriendService = new UpdateFriendService(this);
        deleteFriendService = new DeleteFriendService(this);
    }

    @Override
    public void initUI() {
        setResizable(false);
        setRootGridLayout(ROWS, COLS);
        setVGap(15);
        JPanel panel_remark = getChildPanel(4, 2);
        panel_remark.setLayout(new BorderLayout());
        text_remark = new UTextField();
        text_remark.setHint(getContext().getString("tip_enter_remark"));
        panel_remark.add(text_remark);
        JPanel panel_operator = getChildPanel(5, 2);
        panel_operator.setLayout(new BorderLayout());
        btn_delete = new JButton(getContext().getString("cancel_attention"));
        btn_update = new JButton(getContext().getString("update_attention"));
        panel_operator.add(btn_delete, BorderLayout.WEST);
        panel_operator.add(btn_update, BorderLayout.EAST);

        btn_delete.addActionListener(this);
        btn_update.addActionListener(this);
    }

    @Override
    public void doBusiness() {
        setTitle(getContext().getString("update_attention"));
        text_remark.setText(friend.getRemark());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_delete) {
            deleteFriendService.request(token,friend.getId());
            return;
        }
        if (e.getSource() == btn_update) {
            updateFriendService.request(token, friend.getId(), text_remark.getUText());
        }
    }

    public static void startActivity(Context context, Token token, Friend friend) {
        Intent intent = new Intent();
        intent.put(INTENT_FRIEND, friend);
        intent.put(INTENT_TOKEN, token);
        context.startActivity(UpdateFriendActivity.class, intent);
    }

    @Override
    public void start(int id) {

    }

    @Override
    public void handle(int id, Result result) {
        UToast.show(this, getContext().getString("success"));
        if(id == deleteFriendService.getId()){
            dispose();
        }
    }

    @Override
    public void error(int id, int code, String message) {
        UToast.show(this, code + message);
    }
}
