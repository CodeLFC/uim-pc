package gaozhi.online.uim.example.ui;

import com.google.gson.Gson;
import gaozhi.online.uim.core.activity.Activity;
import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.Intent;
import gaozhi.online.uim.core.activity.widget.ULabel;
import gaozhi.online.uim.core.activity.widget.UTextField;
import gaozhi.online.uim.core.activity.widget.UToast;
import gaozhi.online.uim.core.net.Result;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.LoginInfo;
import gaozhi.online.uim.example.entity.Token;
import gaozhi.online.uim.example.entity.UserAuth;
import gaozhi.online.uim.example.entity.dto.UserDTO;
import gaozhi.online.uim.example.im.service.IMServiceApplication;
import gaozhi.online.uim.example.im.service.UserPoolService;
import gaozhi.online.uim.example.service.user.LoginService;
import gaozhi.online.uim.example.ui.main.MainActivity;
import gaozhi.online.uim.example.utils.FileUtil;
import gaozhi.online.uim.example.utils.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 登陆页面
 * @date 2022/1/25 23:45
 */
public class LoginActivity extends Activity implements ApiRequest.ResultHandler {
    private static final String LOGIN_FILE = "login_user.data";
    private static final int COLS = 3;
    private static final int ROWS = 8;
    private UTextField text_id;
    private JPasswordField text_pass;
    private JButton btn_login;
    private ULabel text_register;
    private ULabel text_find_pass;
    private LoginService loginService;
    private Gson gson;
    private String account;
    private String pass;

    public LoginActivity(Context context, Intent intent, String title) {
        super(context, intent, title);
    }

    @Override
    public void initParam(Intent intent) {
        loginService = new LoginService(this);
        gson = new Gson();
    }

    @Override
    public void initUI() {
        setResizable(false);
        setRootGridLayout(ROWS, COLS);
        setVGap(15);

        JPanel center_id = getChildPanel(4, 2);
        center_id.setLayout(new BorderLayout());
        text_id = new UTextField();
        text_id.setHint(getContext().getString("account_tip"));
        center_id.add(text_id);

        JPanel center_pass = getChildPanel(5, 2);
        center_pass.setLayout(new BorderLayout());
        text_pass = new JPasswordField();
        center_pass.add(text_pass);

        JPanel center_btn = getChildPanel(6, 2);
        center_btn.setLayout(new BorderLayout());
        btn_login = new JButton(getContext().getString("login"));
        center_btn.add(btn_login);

        JPanel rb_label = getChildPanel(7, 2);
        rb_label.setLayout(new BorderLayout());
        JPanel north = new JPanel();
        north.setLayout(new BorderLayout());
        rb_label.add(north, BorderLayout.NORTH);

        text_register = new ULabel(getContext().getString("register"));
        north.add(text_register, BorderLayout.WEST);
        text_find_pass = new ULabel(getContext().getString("find_pass"));
        north.add(text_find_pass, BorderLayout.EAST);
        text_register.setActionListener(this);
        text_find_pass.setActionListener(this);
        btn_login.addActionListener(this);
    }

    @Override
    public void doBusiness() {
        String login_user = null;
        //business
        try {
            login_user = FileUtil.read(LOGIN_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtil.isBlank(login_user)) {
            return;
        }
        LoginInfo loginInfo = gson.fromJson(login_user, LoginInfo.class);
        if (loginInfo != null) {
            text_id.setText(loginInfo.getAccount());
            text_pass.setText(loginInfo.getPass());
            if (loginInfo.isAuthLogin()) {
                login();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_login) {
            login();
            return;
        }
        if (e.getSource() == text_register) {
            RegisterActivity.startActivity(getContext());
            return;
        }
        if (e.getSource() == text_find_pass) {
            FindPassActivity.startActivity(getContext());
        }
    }

    private void login() {
        account = text_id.getUText();
        pass = new String(text_pass.getPassword());
        if (account.isBlank() || pass.isBlank()) {
            UToast.show(this, "blank");
            return;
        }
        logger.info(account + ":" + pass);
        UserAuth.AccountType accountType = account.length() == 13 ? UserAuth.AccountType.PHONE : UserAuth.AccountType.ID;
        loginService.request(accountType, account, pass, Token.Device.COMPUTER.getDevice());
    }

    @Override
    public void start(int id) {
        btn_login.setEnabled(false);
        btn_login.setText(getContext().getString("progressing"));
    }

    @Override
    public void handle(int id, Result result) {
        UserDTO login_user = gson.fromJson(result.getData(), UserDTO.class);
        logger.info(login_user.toString());
        //登录
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setAuthLogin(false);
        loginInfo.setUserInfo(login_user.getUserInfo());
        loginInfo.setAccount(account);
        loginInfo.setPass(pass);
        try {
            FileUtil.write(LOGIN_FILE, gson.toJson(loginInfo));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //填充token信息
        UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
        userPoolService.setToken(login_user.getToken());
        MainActivity.startActivity(getContext(), login_user);
        dispose();
    }

    @Override
    public void error(int id, int code, String message) {
        UToast.show(this, code + ":" + message);
        btn_login.setEnabled(true);
        btn_login.setText(getContext().getString("login"));
    }
}
