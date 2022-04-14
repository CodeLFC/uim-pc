package gaozhi.online.uim.example.ui.main;

import com.google.gson.Gson;
import gaozhi.online.uim.core.activity.Activity;
import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.Intent;
import gaozhi.online.uim.core.activity.widget.*;
import gaozhi.online.uim.core.asynchronization.TaskExecutor;
import gaozhi.online.uim.core.net.Result;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.UserInfo;
import gaozhi.online.uim.example.entity.dto.UserDTO;
import gaozhi.online.uim.example.im.entity.UServer;
import gaozhi.online.uim.example.im.service.CSBeatService;
import gaozhi.online.uim.example.im.service.IMMsgService;
import gaozhi.online.uim.example.im.service.IMServiceApplication;
import gaozhi.online.uim.example.im.service.UserPoolService;
import gaozhi.online.uim.example.service.im.GetIMServerService;
import gaozhi.online.uim.example.service.user.GetUserInfoService;
import gaozhi.online.uim.example.ui.UserInfoActivity;
import gaozhi.online.uim.example.ui.main.card.AttentionCard;
import gaozhi.online.uim.core.activity.widget.BaseCard;
import gaozhi.online.uim.example.ui.main.card.FanCard;
import gaozhi.online.uim.example.utils.DateTimeUtil;
import gaozhi.online.uim.example.utils.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 主页面
 * @date 2022/3/14 10:53
 */
public class MainActivity extends Activity implements ApiRequest.ResultHandler, UNavigationBar.UItemSelectedListener, KeyListener, Consumer<UserInfo>, BiConsumer<Integer, String> {
    //intent
    private static final String INTENT_USER = "intent_user";
    //ui
    //top
    private JPanel top;
    //private JLabel label_signature;
    private UTextField text_search_content;
    private UNavigationBar uNavigationBar;
    //center
    private JPanel center;
    private CardLayout centerLayout;
    private BaseCard<UserDTO>[] centerCards;

    //bottom
    private JPanel bottom;
    private UImageView label_info_image;
    private UImageView label_server_image;
    private JLabel label_server;
    //param
    private UserDTO userDTO;

    private int top_height;
    private int bottom_height;
    // service--------------
    private final Gson gson = new Gson();
    private GetIMServerService getIMServerService;
    private GetUserInfoService getUserInfoService;

    public MainActivity(Context context, Intent intent, String title) {
        super(context, intent, title);
    }

    @Override
    public void initParam(Intent intent) {
        userDTO = intent.getObject(INTENT_USER);
        logger.info(userDTO.toString());
        top_height = getContext().getDimen("main_top_height", 50);
        bottom_height = getContext().getDimen("main_bottom_height", 50);

        getIMServerService = new GetIMServerService(this);
        getUserInfoService = new GetUserInfoService(this);
    }

    @Override
    public void initUI() {
        setResizable(false);
        //一级布局
        setSize(getHeight(), getWidth());
        setLocation(getX() + getWidth() / 2, getY() - getHeight() / 5);
        setRootGridLayout(1, 1);
        JPanel panel = getChildPanel(1, 1);
        panel.setLayout(new BorderLayout());

        //二级子布局
        // ----top
        top = new JPanel();
        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(getWidth(), top_height));
        //-========center
        center = new JPanel();
        centerLayout = new CardLayout();
        center.setLayout(centerLayout);
        //====================bottom
        bottom = new JPanel();
        FlowLayout flowLayout_bottom = new FlowLayout();
        flowLayout_bottom.setAlignment(FlowLayout.LEFT);
        flowLayout_bottom.setVgap(10);
        flowLayout_bottom.setHgap(10);
        bottom.setLayout(flowLayout_bottom);
        bottom.setPreferredSize(new Dimension(getWidth(), bottom_height));

        panel.add(top, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);


        //三级布局

        //-------top
        //签名
        //label_signature = new JLabel(getContext().getString("signature"));
        //top.add(label_signature,BorderLayout.NORTH);
        text_search_content = new UTextField();
        text_search_content.setHint(getContext().getString("search_user"));
        top.add(text_search_content, BorderLayout.SOUTH);
        //导航栏
        uNavigationBar = new UNavigationBar();
        uNavigationBar.setItems(new String[]{getContext().getString("attention"), getContext().getString("fan")});
        uNavigationBar.setuItemSelectedListener(this);
        top.add(uNavigationBar);
        //--------center
        centerCards = new BaseCard[]{
                new AttentionCard(getContext(), userDTO), new FanCard(getContext(), userDTO)
        };
        for (BaseCard<UserDTO> baseCard : centerCards) {
            center.add(baseCard, baseCard.getKey());
        }
        //---------bottom
        try {
            label_info_image = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("bottom_info"), bottom_height / 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            label_server_image = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("bottom_server"), bottom_height / 2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        label_server = new JLabel(getContext().getString("tip_server"));
        bottom.add(label_info_image);
        bottom.add(label_server_image);
        bottom.add(label_server);
        //---------listener
        label_info_image.setActionListener(this);
        text_search_content.addKeyListener(this);
        addKeyListener(this);
    }

    @Override
    public void doBusiness() {
        //默认显示页面
        uNavigationBar.setSelected(0);
        String remark = userDTO.getUserInfo().getRemark();
        setTitle(userDTO.getUserInfo().getNick() + "[" + userDTO.getUserInfo().getId() + "] " + remark);
        setIcon(userDTO.getUserInfo().getHeadUrl());
        getIMServerService.request(userDTO.getToken());
        new TaskExecutor().executeDelayTask(() -> {
            logger.info("刷新");
            for (BaseCard<UserDTO> baseCard : centerCards) {
                baseCard.doBusiness();
            }
        },1000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == label_info_image) {
            if (getContext().getActivityList().getActivityList(UserInfoActivity.class).size() > 0) {
                return;
            }
            UserInfoActivity.startActivity(getContext(), userDTO.getToken(), userDTO.getUserInfo());
        }
    }

    //启动页面
    public static void startActivity(Context context, UserDTO userDTO) {
        Intent intent = new Intent();
        intent.put(INTENT_USER, userDTO);
        context.startActivity(MainActivity.class, intent);
    }

    @Override
    public void start(int id) {
        if (id == getIMServerService.getId()) {
            label_server.setText(getContext().getString("tip_pull_im_server_info"));
            label_server_image.setActionListener(null);
        }
    }

    @Override
    public void handle(int id, Result result) {
        if (id == getIMServerService.getId()) {
            //data
            UServer imServer = gson.fromJson(result.getData(), UServer.class);
            logger.info(imServer.toString());
            //启动im
            IMServiceApplication.getInstance().setImServer(imServer);
            IMServiceApplication.getInstance().getServiceInstance(IMMsgService.class).startService();
            IMServiceApplication.getInstance().getServiceInstance(CSBeatService.class).startService();
            label_server.setText(imServer.getRemark() + "  time at:" + DateTimeUtil.getChatTime(imServer.getUpdateTime()));
            label_server_image.setActionListener(this);
        }
    }

    @Override
    public void error(int id, int code, String message) {
        UToast.show(this, code + ":" + message);
    }

    @Override
    public void onClick(UNavigationBar.UItemView itemView) {
        logger.info(itemView.getItem());
        centerLayout.show(center, centerCards[itemView.getIndex()].getKey());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar() == KeyEvent.VK_SPACE){
            logger.info("刷新信息");
            for (BaseCard<UserDTO> baseCard : centerCards) {
                baseCard.doBusiness();
            }
            return;
        }
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            search(text_search_content.getUText());
            text_search_content.setText(null);
            return;
        }
    }

    private void search(String key) {
        long id = -1;
        try {
            id = Long.parseLong(key);
        } catch (Exception e) {
            UToast.show(this, e.getMessage());
            return;
        }

        IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class).getUserInfo(id, false, this, this);
    }

    @Override
    public void accept(UserInfo userInfo) {
        UserInfoActivity.startActivity(getContext(), userDTO.getToken(), userInfo);
    }

    @Override
    public void accept(Integer code, String message) {
        UToast.show(this, code + message);
    }
}
