package gaozhi.online.uim.example.im.service;

import com.google.gson.Gson;
import gaozhi.online.uim.core.net.Result;
import gaozhi.online.uim.core.net.http.ApiRequest;
import gaozhi.online.uim.example.entity.Token;
import gaozhi.online.uim.example.entity.UserInfo;
import gaozhi.online.uim.example.service.user.GetUserInfoService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 用户信息池
 * @date 2022/3/18 18:31
 */
public class UserPoolService implements UService {
    private final Map<Long, UserInfo> userInfoMap = new HashMap<>();
    private Token token;
    private final Gson gson = new Gson();
    //同步消费者
    private final Map<Long, List<Consumer<UserInfo>>> idUserConsumerHashMap = new ConcurrentHashMap<>();

    public void setToken(Token token) {
        this.token = token;
    }

    public long getSelfId() {
        return token.getUserid();
    }

    public void getUserInfo(long id, boolean update, Consumer<UserInfo> userInfoConsumer) {
        getUserInfo(id, update, userInfoConsumer, null);
    }

    public void getUserInfo(long id, boolean update, Consumer<UserInfo> userInfoConsumer, BiConsumer<Integer, String> errorConsumer) {
        if (update || !userInfoMap.containsKey(id)) {
            userInfoMap.put(id, null);
            new GetUserInfoService(new ApiRequest.ResultHandler() {
                @Override
                public void start(int id) {

                }

                @Override
                public void handle(int id, Result result) {
                    UserInfo userInfo = gson.fromJson(result.getData(), UserInfo.class);
                    userInfoMap.put(userInfo.getId(), userInfo);
                    if (userInfoConsumer != null) {
                        userInfoConsumer.accept(userInfo);
                    }
                    List<Consumer<UserInfo>> oldConsumer = idUserConsumerHashMap.get(userInfo.getId());
                    if (oldConsumer != null) {
                        idUserConsumerHashMap.remove(userInfo.getId());
                        for (Consumer<UserInfo> e : oldConsumer) {
                            e.accept(userInfo);
                        }
                    }
                }

                @Override
                public void error(int id, int code, String message) {
                    if (errorConsumer != null) {
                        errorConsumer.accept(code, message);
                    }
                }
            }).request(token, id);
        }

        if (userInfoMap.containsKey(id)) {
            UserInfo info = userInfoMap.get(id);
            if (info != null) {
                userInfoConsumer.accept(info);
            } else {
                List<Consumer<UserInfo>> userInfoConsumerList = idUserConsumerHashMap.get(id);
                if (userInfoConsumerList == null) {
                    userInfoConsumerList = new LinkedList<>();
                }
                userInfoConsumerList.add(userInfoConsumer);
                idUserConsumerHashMap.put(id, userInfoConsumerList);
            }
        }
    }

    @Override
    public void setIMApplication(IMServiceApplication imApplication) {

    }

    @Override
    public void startService() {

    }

    @Override
    public void closeService() {

    }
}
