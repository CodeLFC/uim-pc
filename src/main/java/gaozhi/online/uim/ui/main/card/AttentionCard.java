package gaozhi.online.uim.ui.main.card;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gaozhi.online.ugui.core.activity.Context;
import gaozhi.online.ugui.core.activity.widget.BaseCard;
import gaozhi.online.ugui.core.activity.widget.URecyclerView;
import gaozhi.online.ugui.core.activity.widget.UToast;
import gaozhi.online.ugui.core.net.Result;
import gaozhi.online.ugui.core.net.http.ApiRequest;
import gaozhi.online.uim.entity.UserInfo;
import gaozhi.online.uim.entity.dto.UserDTO;
import gaozhi.online.uim.entity.Friend;
import gaozhi.online.uim.im.service.IMServiceApplication;
import gaozhi.online.uim.im.service.UserPoolService;
import gaozhi.online.uim.service.friend.GetAttentionService;
import gaozhi.online.uim.ui.UserInfoActivity;
import gaozhi.online.uim.ui.main.friend.FriendCellRender;
import gaozhi.online.uim.ui.main.friend.FriendListModel;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/4/13 9:40
 */
public class AttentionCard extends BaseCard<UserDTO> implements ApiRequest.ResultHandler, BiConsumer<Integer, Friend>, Consumer<UserInfo> {
    private URecyclerView<Friend> friendURecyclerView;
    private FriendListModel friendListModel;
    private FriendCellRender friendCellRender;
    //service
    private UserDTO userDTO;
    private GetAttentionService getAttentionService;
    private PageInfo<Friend> friendPageInfo;
    private Gson gson;

    public AttentionCard(Context context, UserDTO userDTO) {
        super("attention", context, userDTO);
    }

    @Override
    public void initParam(UserDTO data) {
        userDTO = data;
        getAttentionService = new GetAttentionService(this);
        gson = new Gson();
        friendPageInfo = new PageInfo<>();
    }

    @Override
    public void initUI() {
        setLayout(new BorderLayout(10,10));
        friendURecyclerView = new URecyclerView<>();
        friendCellRender = new FriendCellRender(getContext());
        friendURecyclerView.setCellRender(friendCellRender);
        friendListModel = new FriendListModel(friendURecyclerView, userDTO.getToken().getUserid());
        friendURecyclerView.setListModel(friendListModel);
        add(friendURecyclerView);

        friendURecyclerView.addItemClickedListener(this);

        friendURecyclerView.refresh(1000);
    }

    @Override
    public void doBusiness() {
        friendListModel.removeAllElements();
        getAttentionService.request(userDTO.getToken(), friendPageInfo.getNextPage());
    }

    @Override
    public void releaseResource() {
        friendURecyclerView.stopRefresh();
    }

    @Override
    public void start(int id) {

    }

    @Override
    public void handle(int id, Result result) {
        friendPageInfo = gson.fromJson(result.getData(), new TypeToken<PageInfo<Friend>>() {
        }.getType());
        if (friendPageInfo.isHasNextPage()) {
            getAttentionService.request(userDTO.getToken(), friendPageInfo.getNextPage());
        }

        for (Friend friend : friendPageInfo.getList()) {
            friendListModel.addElement(friend);
            System.out.println(friend);
        }
    }

    @Override
    public void error(int id, int code, String message) {
        UToast.show(getContext(), code + message);
    }

    @Override
    public void accept(Integer integer, Friend friend) {
        UserPoolService userPoolService = IMServiceApplication.getInstance().getServiceInstance(UserPoolService.class);
        userPoolService.getUserInfo(friend.getFriendId(), false, this);
    }

    @Override
    public void accept(UserInfo userInfo) {
        UserInfoActivity.startActivity(getContext(), userDTO.getToken(), userInfo);
    }

}
