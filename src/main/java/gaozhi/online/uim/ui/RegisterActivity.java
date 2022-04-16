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
import gaozhi.online.uim.entity.UserInfo;
import gaozhi.online.uim.entity.VerifyCode;
import gaozhi.online.uim.service.user.RegisterService;
import gaozhi.online.uim.service.user.SendVerifyCodeService;
import gaozhi.online.uim.utils.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 注册界面
 * @date 2022/3/24 10:53
 */
public class RegisterActivity extends Activity implements ApiRequest.ResultHandler, Runnable {
    private static final int COLS = 3;
    private static final int ROWS = 8;
    private UTextField text_phone;
    private UTextField text_verify_code;
    private JButton btn_send;
    private JButton btn_register;
    private UTextField text_new_pass;

    private Timer timer;
    //data
    private VerifyCode verifyCode;
    //service
    private SendVerifyCodeService sendVerifyCodeService;
    private String phoneTemp;
    private RegisterService registerService;
    private UserInfo registerUser;

    public RegisterActivity(Context context, Intent intent, String title,long id) {
        super(context, intent, title,id);
    }

    @Override
    public void initParam(Intent intent) {
        verifyCode = new VerifyCode();
        sendVerifyCodeService = new SendVerifyCodeService(this);
        registerService = new RegisterService(this);
    }

    @Override
    public void initUI() {
        setResizable(false);
        setRootGridLayout(ROWS, COLS);
        setVGap(20);
        text_phone = new UTextField();
        text_phone.setHint(getContext().getString("tip_enter_phone"));
        UPanel panelPhone = getChildPanel(4, 2);
        panelPhone.setLayout(new BorderLayout());
        panelPhone.add(text_phone);

        UPanel panelVerifyCode = getChildPanel(5, 2);
        BorderLayout panelVerifyCodeBorderLayout = new BorderLayout();
        panelVerifyCodeBorderLayout.setHgap(40);
        panelVerifyCode.setLayout(panelVerifyCodeBorderLayout);
        text_verify_code = new UTextField();
        text_verify_code.setHint(getContext().getString("tip_enter_verify_code"));
        panelVerifyCode.add(text_verify_code);
        btn_send = new JButton(getContext().getString("send"));
        panelVerifyCode.add(btn_send, BorderLayout.EAST);

        UPanel panelPass = getChildPanel(6, 2);
        panelPass.setLayout(new BorderLayout());
        text_new_pass = new UTextField();
        text_new_pass.setHint(getContext().getString("tip_new_pass"));
        panelPass.add(text_new_pass);
        btn_register = new JButton(getContext().getString("register"));
        UPanel panelRegister = getChildPanel(7, 2);
        panelRegister.setLayout(new BorderLayout());
        panelRegister.add(btn_register);

        btn_send.addActionListener(this);
        btn_register.addActionListener(this);
    }

    @Override
    public void doBusiness() {
        timer = new TaskExecutor().executeTimerTask(this, 1000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_send) {
            String phone = text_phone.getUText();
            if (StringUtil.isPhoneNum(phone)) {
                //模拟发送验证码
                phoneTemp = text_phone.getUText();
                sendVerifyCodeService.request(VerifyCode.NotifyMethod.SMS, VerifyCode.CodeTemplate.REGISTER, phoneTemp);
            } else {
                UToast.show(this, getContext().getString("tip_phone_format_error"));
            }
            return;
        }
        if (e.getSource() == btn_register) {
            String verifyCode = text_verify_code.getUText();
            if (StringUtil.isBlank(verifyCode)) {
                UToast.show(this, getContext().getString("tip_verify_code_error"));
                return;
            }
            registerService.request(phoneTemp, verifyCode,text_new_pass.getUText());
        }
    }

    //启动页面
    public static void startActivity(Context context) {
        Intent intent = new Intent();
        context.startActivity(RegisterActivity.class, intent);
    }

    @Override
    public void start(int id) {

    }

    @Override
    public void handle(int id, Result result) {
        Gson gson = new Gson();
        if (id == sendVerifyCodeService.getId()) {
            verifyCode = gson.fromJson(result.getData(), VerifyCode.class);
            return;
        }
        if (id == registerService.getId()) {
            registerUser = gson.fromJson(result.getData(), UserInfo.class);
            UToast.show(this, getContext().getString("success") + ":" + registerUser.getId());
        }
    }

    @Override
    public void error(int id, int code, String message) {
        UToast.show(this, code + message);
    }

    @Override
    public void run() {
        long least = verifyCode.getValidateTime() - System.currentTimeMillis();
        if (least < 0) {
            btn_send.setText(getContext().getString("send"));
            btn_send.setEnabled(true);
        } else {
            btn_send.setText(least / 1000 + "s");
            btn_send.setEnabled(false);
        }
    }

    @Override
    public void releaseResource() {
        timer.cancel();
    }
}
