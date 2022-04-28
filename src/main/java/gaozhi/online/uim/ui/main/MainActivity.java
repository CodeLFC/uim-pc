package gaozhi.online.uim.ui.main;

import com.google.gson.Gson;
import gaozhi.online.ugui.core.activity.Activity;
import gaozhi.online.ugui.core.activity.Context;
import gaozhi.online.ugui.core.activity.Intent;
import gaozhi.online.ugui.core.activity.widget.*;
import gaozhi.online.ugui.core.net.Result;
import gaozhi.online.ugui.core.net.http.ApiRequest;
import gaozhi.online.ugui.core.utils.ImageUtil;
import gaozhi.online.uim.entity.UserInfo;
import gaozhi.online.uim.entity.dto.UserDTO;
import gaozhi.online.uim.im.entity.UServer;
import gaozhi.online.uim.im.service.CSBeatService;
import gaozhi.online.uim.im.service.IMMsgService;
import gaozhi.online.uim.im.service.IMServiceApplication;
import gaozhi.online.uim.im.service.UserPoolService;
import gaozhi.online.uim.service.im.GetIMServerService;
import gaozhi.online.uim.ui.LoginActivity;
import gaozhi.online.uim.ui.UserInfoActivity;
import gaozhi.online.uim.ui.main.card.AttentionCard;
import gaozhi.online.ugui.core.activity.widget.BaseCard;
import gaozhi.online.uim.ui.main.card.FanCard;
import gaozhi.online.uim.utils.DateTimeUtil;

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
    private UPanel top;
    //private JLabel label_signature;
    private UTextField text_search_content;
    private UNavigationBar uNavigationBar;
    //center
    private UPanel center;
    private CardLayout centerLayout;
    private BaseCard<UserDTO>[] centerCards;

    //bottom
    private UPanel bottom;

    private UImageView image_info;
    private UImageView image_exit_account;
    private UImageView image_server;
    //private JLabel label_server;
    private JLabel label_beat;

    //param
    private UserDTO userDTO;

    private int top_height;
    private int bottom_height;
    // service--------------
    private final Gson gson = new Gson();
    private GetIMServerService getIMServerService;

    public MainActivity(Context context, Intent intent, String title, long id) {
        super(context, intent, title, id);
    }

    @Override
    public void initParam(Intent intent) {
        userDTO = intent.getObject(INTENT_USER);
        logger.info(userDTO.toString());
        top_height = getContext().getDimen("main_top_height", 50);
        bottom_height = getContext().getDimen("main_bottom_height",50);
        getIMServerService = new GetIMServerService(this);

    }

    @Override
    public void initUI() {
        setResizable(false);
        //一级布局
        setSize(getHeight(), getWidth());
        setLocation(getX() + getWidth() / 2, getY() - getHeight() / 5);
        setRootGridLayout(1, 1);
        UPanel panel = getChildPanel(1, 1);
        panel.setLayout(new BorderLayout());

        //二级子布局
        // ----top
        top = new UPanel();
        top.setLayout( new BorderLayout());
        top.setPreferredSize(new Dimension(getWidth(), top_height));
        //-========center
        center = new UPanel();
        centerLayout = new CardLayout();
        center.setLayout(centerLayout);
        //====================bottom
        bottom = new UPanel();
        bottom.setLayout(new BorderLayout());
        panel.add(top, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);


        //三级布局

        //-------top
        //签名
        //label_signature = new JLabel(getContext().getString("signature"));
        //top.add(label_signature,BorderLayout.NORTH);


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
        text_search_content = new UTextField();
        text_search_content.setHint(getContext().getString("search_user"));
        bottom.add(text_search_content);

        try {
            image_info = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("bottom_info"), bottom_height / 2));
            image_server = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("bottom_server"), bottom_height / 2));
            image_exit_account = new UImageView(ImageUtil.getScaleImage(getContext().getDrawable("bottom_exit_account"), bottom_height / 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
       // label_server = new JLabel(getContext().getString("tip_server"));
        label_beat = new JLabel();
        UPanel imagePanel = new UPanel();
        FlowLayout flowLayout_bottom = new FlowLayout();
        flowLayout_bottom.setAlignment(FlowLayout.LEFT);
        flowLayout_bottom.setVgap(10);
        flowLayout_bottom.setHgap(10);
        imagePanel.setLayout(flowLayout_bottom);

        imagePanel.add(image_info);
        imagePanel.add(image_exit_account);
        imagePanel.add(image_server);
        imagePanel.add(label_beat);
        bottom.add(imagePanel,BorderLayout.SOUTH);
        //  bottom.add(label_server);



        //---------listener
        image_info.setActionListener(this);
        text_search_content.addKeyListener(this);
        image_exit_account.setActionListener(this);
    }

    @Override
    public void doBusiness() {
        //默认显示页面
        uNavigationBar.setSelected(0);
        String remark = userDTO.getUserInfo().getRemark();
        setTitle(userDTO.getUserInfo().getNick() + "[" + userDTO.getUserInfo().getId() + "] " + remark);
        setIcon(userDTO.getUserInfo().getHeadUrl());
        getIMServerService.request(userDTO.getToken());

        CSBeatService beatService = IMServiceApplication.getInstance().getServiceInstance(CSBeatService.class);
        beatService.addBeatResponseListener(uClient ->{
         label_beat.setText(uClient.getIp() + ":" + uClient.getPort() + " " + DateTimeUtil.getHMSTime(uClient.getUpdateTime()));
         if(!isIconified()){
             stopTwinkle();
         }
        });

        IMMsgService imMsgService = IMServiceApplication.getInstance().getServiceInstance(IMMsgService.class);
        imMsgService.addIMMsgConsumer(msg -> {
            logger.info("收到UMsg:"+msg.toString());
            if(isIconified()&&!isTwinkle()){
                startTwinkle(1000);
            }
        });
    }

    @Override
    public void dispose() {
        //关闭服务
        IMServiceApplication.getInstance().getServiceInstance(IMMsgService.class).closeService();
        IMServiceApplication.getInstance().getServiceInstance(CSBeatService.class).closeService();
        //主页关闭后直接退出程序
        for (Activity activity : getContext().getActivityList().getActivityList()) {
            if (activity != this) {
                activity.dispose();
            }
        }
        super.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == image_info) {
            if (getContext().getActivityList().getActivityList(UserInfoActivity.class).size() > 0) {
                return;
            }
            UserInfoActivity.startActivity(getContext(), userDTO.getToken(), userDTO.getUserInfo());
        }
        if (e.getSource() == image_exit_account) {
            LoginActivity.startActivity(getContext(), false);
            dispose();
        }
    }

    @Override
    public void releaseResource() {
        super.releaseResource();
        for (BaseCard<UserDTO> baseCard : centerCards) {
            baseCard.releaseResource();
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
            //label_server.setText(getContext().getString("tip_pull_im_server_info"));
            image_server.setActionListener(null);
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
           // label_server.setText(imServer.getRemark());
            image_server.setActionListener(this);
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
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
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
