package gaozhi.online.uim.example.service.user;

import gaozhi.online.uim.example.service.NetConfig;
import gaozhi.online.uim.core.net.http.ApiRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取云存储临时秘钥
 */
@Deprecated
public class FetchTmpCosSecretService extends ApiRequest {
    public FetchTmpCosSecretService(ResultHandler resultHandler) {
        super(NetConfig.userBaseURL, Type.POST, resultHandler);
    }
    public void request(long userid,int device,String token){
        Map<String, String> params=new HashMap<>();
        params.put("userid",""+userid);
        params.put("token",token);
        params.put("device",""+device);
        request("fetch_tmp_cos_secret",params);
    }
}
