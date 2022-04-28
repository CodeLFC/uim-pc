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
import gaozhi.online.uim.service.friend.GetFanService;
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
 * @date 2022/4/13 9:41
 */
public class FanCard extends BaseCard<UserDTO> implements ApiRequest.ResultHandler, BiConsumer<Integer, Friend>, Consumer<UserInfo> {
    private URecyclerView<Friend> fanURecyclerView;
    private FriendListModel fanListModel;
    //service
    private UserDTO userDTO;
    private GetFanService getFanService;
    private PageInfo<Friend> fanPageInfo;
    private Gson gson;

    public FanCard(Context context, UserDTO userDTO) {
        super("fan", context, userDTO);
    }

    @Override
    public void initParam(UserDTO data) {
        userDTO = data;
        getFanService = new GetFanService(this);
        gson = new Gson();
        fanPageInfo = new PageInfo<>();
    }

    @Override
    public void initUI() {
        setLayout(new BorderLayout(10,10));
        fanURecyclerView = new URecyclerView<>();
        fanURecyclerView.setCellRender(new FriendCellRender(getContext()));
        fanListModel = new FriendListModel(fanURecyclerView,userDTO.getToken().getUserid());
        fanURecyclerView.setListModel(fanListModel);
        add(fanURecyclerView);
        fanURecyclerView.addItemClickedListener(this);
        fanURecyclerView.refresh(1000);
    }

    @Override
    public void doBusiness() {
        fanListModel.removeAllElements();
        getFanService.request(userDTO.getToken(), fanPageInfo.getNextPage());
    }

    @Override
    public void releaseResource() {
        fanURecyclerView.stopRefresh();
    }

    @Override
    public void start(int id) {

    }

    @Override
    public void handle(int id, Result result) {
        fanPageInfo = gson.fromJson(result.getData(), new TypeToken<PageInfo<Friend>>() {
        }.getType());
        if (fanPageInfo.isHasNextPage()) {
            getFanService.request(userDTO.getToken(), fanPageInfo.getNextPage());
        }
        for (Friend friend : fanPageInfo.getList()) {
            fanListModel.addElement(friend);
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
        userPoolService.getUserInfo(friend.getUserid(), false, this);
    }

    @Override
    public void accept(UserInfo userInfo) {
        UserInfoActivity.startActivity(getContext(), userDTO.getToken(), userInfo);
    }
}
