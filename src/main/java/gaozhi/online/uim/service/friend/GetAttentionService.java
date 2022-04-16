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
 * @description: TODO 获取关注关系
 * @date 2022/4/13 10:22
 */
public class GetAttentionService extends ApiRequest {
    public GetAttentionService(ResultHandler resultHandler) {
        super(NetConfig.friendBaseURL, Type.GET, resultHandler);
    }

    public void request(Token token, int pageNum) {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", new Gson().toJson(token));
        Map<String, String> params = new HashMap<>();
        params.put("pageNum", "" + pageNum);
        params.put("pageSize", "" + 100);
        request("get/attentions", headers, params);
    }
}
