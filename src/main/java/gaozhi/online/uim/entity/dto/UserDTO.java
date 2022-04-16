package gaozhi.online.uim.entity.dto;

import gaozhi.online.uim.entity.Token;
import gaozhi.online.uim.entity.UserInfo;

/**
 * 用户信息
 */

public class UserDTO {
    private UserInfo userInfo;
    private Token token;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userInfo=" + userInfo +
                ", token=" + token +
                '}';
    }
}
