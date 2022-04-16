package gaozhi.online.uim.service.friend;

import com.google.gson.Gson;
import gaozhi.online.ugui.core.net.http.ApiRequest;
import gaozhi.online.uim.entity.Token;
import gaozhi.online.uim.service.NetConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 获取指定的朋友关系
 * @date 2022/4/13 16:25
 */
public class GetFriendService extends ApiRequest {
    public GetFriendService(ResultHandler resultHandler) {
        super(NetConfig.friendBaseURL, Type.GET, resultHandler);
    }

    public void request(Token token, long userid, long friendId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", new Gson().toJson(token));
        Map<String, String> params = new HashMap<>();
        params.put("userid", "" + userid);
        params.put("friendId", "" + friendId);
        request("get/friend", headers, params);
    }
}
