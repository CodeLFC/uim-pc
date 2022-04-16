package gaozhi.online.uim.service.user;

import com.google.gson.Gson;
import gaozhi.online.uim.entity.Token;
import gaozhi.online.uim.service.NetConfig;
import gaozhi.online.ugui.core.net.http.ApiRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * @description: TODO 获取用户信息
 * @author LiFucheng
 * @date 2022/4/2 19:06
 * @version 1.0
 */
public class GetUserInfoService extends ApiRequest {

    public GetUserInfoService(ResultHandler resultHandler) {
        super(NetConfig.userBaseURL, Type.GET, resultHandler);
    }

    public void request(Token token, long friendID) {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", new Gson().toJson(token));
        Map<String, String> params = new HashMap<>();
        params.put("userId", "" + friendID);

        request("get/user_info", headers,params);
    }
}
