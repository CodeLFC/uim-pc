package gaozhi.online.uim.core.net.http;

import gaozhi.online.ugui.core.net.Result;
import gaozhi.online.ugui.core.net.http.ApiRequest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class ApiRequestTest {

    @Test
    public void test() throws InterruptedException {
        Map<String, String> params = new HashMap<>();
        new ApiRequest("https://poll.kuaidi100.com/poll/", ApiRequest.Type.GET, new ApiRequest.ResultHandler() {
            @Override
            public void start(int id) {

            }

            @Override
            public void handle(int id, Result result) {
                System.out.println(result);
            }

            @Override
            public void error(int id, int code, String message) {
                System.out.println(id + code + message);
            }
        }).request("query.do", params);

        Thread.sleep(3000);
    }
}