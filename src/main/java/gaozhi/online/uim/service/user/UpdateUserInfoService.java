package gaozhi.online.uim.service.user;


import com.google.gson.Gson;
import gaozhi.online.uim.entity.Token;
import gaozhi.online.uim.entity.UserInfo;
import gaozhi.online.uim.service.NetConfig;
import gaozhi.online.ugui.core.net.http.ApiRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新用户资料
 * "set head_url=#{head_url},nick=#{nick},remark=#{remark},gender=#{gender},birth=#{birth},gps=#{gps},cell_phone=#{cellPhone},wechat=#{wechat},qq=#{qq},visible=#{visible},email=#{email},update_time=#{updateTime} " +
 */
public class UpdateUserInfoService extends ApiRequest {
    public UpdateUserInfoService(ResultHandler resultHandler) {
        super(NetConfig.userBaseURL, Type.PUT, resultHandler);
    }

    public void request(Token token, UserInfo userInfo) {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", new Gson().toJson(token));
        request("put/user_info",headers,null,userInfo);
    }
}
