package gaozhi.online.uim.example.service.im;

import com.google.gson.Gson;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.Token;
import gaozhi.online.uim.example.service.NetConfig;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 获取分配的即时通信服务器   - 正在测试
 * @date 2022/3/22 14:19
 */
public class GetIMServerService extends ApiRequest {
    public GetIMServerService(ResultHandler resultHandler) {
        super(NetConfig.imBaseURL, Type.GET, resultHandler);
    }

    public void request(Token token) {
        //temp
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("token", new Gson().toJson(token));
        request("get/one/random", headers, params);
    }
}
