package gaozhi.online.uim.ui;

import com.google.gson.Gson;
import gaozhi.online.ugui.core.activity.Activity;
import gaozhi.online.ugui.core.activity.Context;
import gaozhi.online.ugui.core.activity.Intent;
import gaozhi.online.ugui.core.activity.widget.UPanel;
import gaozhi.online.ugui.core.activity.widget.UTextField;
import gaozhi.online.ugui.core.activity.widget.UToast;
import gaozhi.online.ugui.core.asynchronization.TaskExecutor;
import gaozhi.online.ugui.core.net.Result;
import gaozhi.online.ugui.core.net.http.ApiRequest;
import gaozhi.online.uim.entity.VerifyCode;
import gaozhi.online.uim.service.user.ResetPassService;
import gaozhi.online.uim.service.user.SendVerifyCodeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 找回密码页面
 * @date 2022/3/24 10:54
 */
public class FindPassActivity extends Activity implements Runnable, ApiRequest.ResultHandler {
    private static final int COLS = 3;
    private static final int ROWS = 8;
    private UTextField text_phone;
    private UTextField text_verify_code;
    private JButton btn_send;
    private UTextField text_new_pass;
    private JButton btn_reset_pass;
    private Timer timer;
    //data
    private VerifyCode verifyCode;
    //service
    private SendVerifyCodeService sendVerifyCodeService;
    private ResetPassService resetPassService;
    private String phone;
    public FindPassActivity(Context context, Intent intent, String title,long id) {
        super(context, intent, title,id);
    }

    @Override
    public void initParam(Intent intent) {
        verifyCode = new VerifyCode();
        sendVerifyCodeService = new SendVerifyCodeService(this);
        resetPassService = new ResetPassService(this);
    }

    @Override
    public void initUI() {
        setResizable(false);
        setRootGridLayout(ROWS, COLS);
        setVGap(20);
        text_phone = new UTextField();
        text_phone.setHint(getContext().getString("tip_enter_phone"));
        UPanel panelPhone = getChildPanel(3, 2);
        panelPhone.setLayout(new BorderLayout());
        panelPhone.add(text_phone);

        UPanel panelVerifyCode = getChildPanel(4, 2);
        BorderLayout panelVerifyCodeBorderLayout = new BorderLayout();
        panelVerifyCodeBorderLayout.setHgap(40);
        panelVerifyCode.setLayout(panelVerifyCodeBorderLayout);
        text_verify_code = new UTextField();
        text_verify_code.setHint(getContext().getString("tip_enter_verify_code"));
        panelVerifyCode.add(text_verify_code);


        UPanel panelPass = getChildPanel(5, 2);
        panelPass.setLayout(new BorderLayout());
        text_new_pass = new UTextField();
        text_new_pass.setHint(getContext().getString("tip_new_pass"));
        panelPass.add(text_new_pass);


        btn_send = new JButton(getContext().getString("send"));
        panelVerifyCode.add(btn_send, BorderLayout.EAST);

        btn_reset_pass = new JButton(getContext().getString("update_pass"));
        UPanel panelRegister = getChildPanel(6, 2);
        panelRegister.setLayout(new BorderLayout());
        panelRegister.add(btn_reset_pass);

        btn_send.addActionListener(this);
        btn_reset_pass.addActionListener(this);
    }

    @Override
    public void doBusiness() {
        timer = new TaskExecutor().executeTimerTask(this, 1000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        phone = text_phone.getUText();
        if (e.getSource() == btn_send) {
            sendVerifyCodeService.request(VerifyCode.NotifyMethod.SMS, VerifyCode.CodeTemplate.FORGET_PASS,phone);
            return;
        }
        if(e.getSource() == btn_reset_pass){
            resetPassService.request(phone,text_verify_code.getUText(),text_new_pass.getUText());
        }
    }

    //启动页面
    public static void startActivity(Context context) {
        Intent intent = new Intent();
        context.startActivity(FindPassActivity.class, intent);
    }

    @Override
    public void run() {
        long least =verifyCode.getValidateTime()-System.currentTimeMillis();
        logger.info(verifyCode.toString());
        if (least < 0) {
            btn_send.setText(getContext().getString("send"));
            btn_send.setEnabled(true);
        } else {
            btn_send.setText(least / 1000 + "s");
            btn_send.setEnabled(false);
        }
    }
    @Override
    public void releaseResource(){
        timer.cancel();
    }

    @Override
    public void start(int id) {

    }

    @Override
    public void handle(int id, Result result) {
        if(id == sendVerifyCodeService.getId()) {
            verifyCode = new Gson().fromJson(result.getData(), VerifyCode.class);
            return;
        }
       if(id == resetPassService.getId()){
           UToast.show(this,getContext().getString("success"));
       }
    }

    @Override
    public void error(int id, int code, String message) {
        UToast.show(this,code+message);
    }
}
