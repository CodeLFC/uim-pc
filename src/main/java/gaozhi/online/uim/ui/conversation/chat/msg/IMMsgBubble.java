package gaozhi.online.uim.ui.conversation.chat.msg;

import gaozhi.online.ugui.core.activity.Context;
import gaozhi.online.ugui.core.activity.widget.UImageView;
import gaozhi.online.ugui.core.activity.widget.UPanel;
import gaozhi.online.ugui.core.asynchronization.TaskExecutor;
import gaozhi.online.ugui.core.utils.ImageUtil;
import gaozhi.online.uim.entity.UserInfo;
import gaozhi.online.uim.im.conversation.message.IMMessage;
import gaozhi.online.uim.im.service.IMServiceApplication;
import gaozhi.online.uim.im.service.UserPoolService;
import gaozhi.online.uim.utils.DateTimeUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  显示消息的bubble
 * @date 2022/3/17 10:48
 */
public class IMMsgBubble extends UPanel implements Consumer<UserInfo> {
    private final Logger logger = Logger.getGlobal();
    private final Context context;
    //布局
    private FlowLayout flowLayout;
    //头像内容
    private Image defaultHead;
    private UImageView image_head;
    //名字
//    private JLabel label_name;
//    private JPanel panel_name;
//    private FlowLayout flowLayout_name;
    //时间
    private JLabel label_time;
    private UPanel panel_time;
    private FlowLayout flowLayout_time;
    //显示内容
    private UPanel panelMsg;
    //消息内容显示区域
    private Map<IMMessage.IMMsgType, IMMsgView> imMsgViewMap;
    //是否已经设置了布局
    private boolean isBindView = false;

    public IMMsgBubble(Context context) {
        this.context = context;
        imMsgViewMap = new HashMap<>();
        flowLayout = new FlowLayout();
        setLayout(flowLayout);

        image_head = new UImageView();

        panelMsg = new UPanel();

        panelMsg.setLayout(new BorderLayout());
        //label_name = new JLabel();
        label_time = new JLabel();
        //flowLayout_name = new FlowLayout();
        flowLayout_time = new FlowLayout();
        //panel_name = new JPanel();
        panel_time = new UPanel();
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
        //注册文本视图
        imMsgViewMap.put(IMMessage.IMMsgType.TEXT, new TextMsgView());
    }


    public void bindView(IMMessage msg) {
        label_time.setText(DateTimeUtil.getChatTime(msg.getTime()));
        //----------------动态加载显示内容
        IMMsgView view = imMsgViewMap.get(msg.getMsgType());
        if (view == null) {
            logger.warning("没有注册此消息类型所对应的视图:"+msg.getMsgType());
            return;
        }
        UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
        boolean isSelf = msg.getFromId() == userPoolService.getSelfId();
        userPoolService.getUserInfo(msg.getFromId(), false, this);
        view.bindView(msg, isSelf);
        if (!isBindView) {
            if (isSelf) {
                add(panelMsg);
                add(image_head);
                flowLayout_time.setAlignment(FlowLayout.RIGHT);
            } else {
                add(image_head);
                add(panelMsg);
                flowLayout_time.setAlignment(FlowLayout.LEFT);
            }
            panelMsg.add(panel_time, BorderLayout.SOUTH);
            panelMsg.add(view, BorderLayout.CENTER);
            isBindView = true;
        }
    }

    @Override
    public void accept(UserInfo showUser) {
        image_head.setImageUrl(showUser.getHeadUrl(), defaultHead, 50, false);
    }
}
