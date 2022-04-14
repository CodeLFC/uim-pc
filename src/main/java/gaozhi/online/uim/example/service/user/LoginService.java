package gaozhi.online.uim.example.service.user;


import gaozhi.online.uim.example.service.NetConfig;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.UserAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录方法
 */
public class LoginService extends ApiRequest {
    public LoginService(ResultHandler resultHandler) {
        super(NetConfig.userBaseURL, Type.POST, resultHandler);
    }

    /**
     * @param type 登录方式 ---
     * @param account 账号
     * @param pass 密码
     * @param device 设备类型 UserDTO 中的常量
     */
    public void request(UserAuth.AccountType type, String account, String pass, int device){
        Map<String, String> params=new HashMap<>();
        params.put("account",account);
        params.put("pass",pass);
        params.put("device",""+device);
        params.put("type",type.getType());
        request("post/login",params);
    }
}
