package gaozhi.online.uim.example.service.friend;

import com.google.gson.Gson;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.Friend;
import gaozhi.online.uim.example.entity.Token;
import gaozhi.online.uim.example.service.NetConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 修改备注
 * @date 2022/4/13 16:53
 */
public class UpdateFriendService extends ApiRequest {
    public UpdateFriendService(ResultHandler resultHandler) {
        super(NetConfig.friendBaseURL, Type.PUT, resultHandler);
    }

    public void request(Token token, long friendShipId,String remark) {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", new Gson().toJson(token));
        Map<String, String> params = new HashMap<>();
        Friend friend = new Friend();
        friend.setId(friendShipId);
        friend.setRemark(remark);
        request("put/attention", headers, params,friend);
    }
}
