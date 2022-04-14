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
 * @description: TODO 添加关注关系
 * @date 2022/4/13 16:38
 */
public class AddAttentionService extends ApiRequest {
    public AddAttentionService(ResultHandler resultHandler) {
        super(NetConfig.friendBaseURL, Type.POST, resultHandler);
    }

    public void request(Token token, long friendId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", new Gson().toJson(token));
        Map<String, String> params = new HashMap<>();
        Friend friend = new Friend();
        friend.setFriendId(friendId);
        request("post/attention", headers, params,friend);
    }
}
