package gaozhi.online.uim.service.user;

import gaozhi.online.uim.service.NetConfig;
import gaozhi.online.ugui.core.net.http.ApiRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 重置密码
 */
public class ResetPassService extends ApiRequest {
    public ResetPassService(ResultHandler resultHandler) {
        super(NetConfig.userBaseURL, Type.PUT, resultHandler);
    }

    public void request(String cell_phone, String verify_code, String pass) {
        Map<String, String> params = new HashMap<>();
        params.put("cell_phone", cell_phone);
        params.put("verify_code", verify_code);
        params.put("pass", pass);
        request("put/password", params);
    }
}
