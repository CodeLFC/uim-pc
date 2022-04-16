package gaozhi.online.uim.service.user;

import gaozhi.online.uim.entity.VerifyCode;
import gaozhi.online.uim.service.NetConfig;
import gaozhi.online.ugui.core.net.http.ApiRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送验证码
 */
public class SendVerifyCodeService extends ApiRequest {
    public SendVerifyCodeService(ResultHandler resultHandler) {
        super(NetConfig.userBaseURL, Type.POST, resultHandler);
    }

    public void request(VerifyCode.NotifyMethod method, VerifyCode.CodeTemplate type, String cell_phone){
        Map<String, String> params=new HashMap<>();
        params.put("cell_phone",cell_phone);
        params.put("type",type.getType());
        params.put("method",method.getMethod());
        request("post/send_verify_code",params);
    }
}
