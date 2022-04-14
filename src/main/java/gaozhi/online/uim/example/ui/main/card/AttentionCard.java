package gaozhi.online.uim.example.ui.main.card;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.widget.BaseCard;
import gaozhi.online.uim.core.activity.widget.URecyclerView;
import gaozhi.online.uim.core.activity.widget.UToast;
import gaozhi.online.uim.core.net.Result;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.Friend;
import gaozhi.online.uim.example.entity.UserInfo;
import gaozhi.online.uim.example.entity.dto.UserDTO;
import gaozhi.online.uim.example.im.service.IMServiceApplication;
import gaozhi.online.uim.example.im.service.UserPoolService;
import gaozhi.online.uim.example.service.friend.GetAttentionService;
import gaozhi.online.uim.example.ui.UserInfoActivity;
import gaozhi.online.uim.example.ui.main.friend.FriendCellRender;
import gaozhi.online.uim.example.ui.main.friend.FriendListModel;

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
        setLayout(new BorderLayout());
        friendURecyclerView = new URecyclerView<>();
        friendURecyclerView.setCellRender(new FriendCellRender(getContext()));
        friendListModel = new FriendListModel(friendURecyclerView);
        friendURecyclerView.setListModel(friendListModel);
        add(friendURecyclerView);

        friendURecyclerView.addItemClickedListener(this);
    }

    @Override
    public void doBusiness() {
        friendListModel.removeAllElements();
        getAttentionService.request(userDTO.getToken(), friendPageInfo.getNextPage());
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
        UToast.show(this, code + message);
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
