package gaozhi.online.uim.core.net.http;


import gaozhi.online.uim.core.asynchronization.Handler;
import gaozhi.online.uim.core.net.Result;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class HttpRunnable implements Runnable, Handler.Worker {
    private final RestfulHTTP restfulHTTP;
    private final HttpHandler httpHandler;
    private final String type;
    private Map<String, String> headers;
    private String body;
    /**
     * @param url
     * @param encode 编码格式
     */
    private final Handler handler;

    public HttpRunnable(String type, String url, Charset encode, HttpHandler httpHandler) {
        restfulHTTP = new RestfulHTTP(url, encode);
        this.httpHandler = httpHandler;
        this.type = type;
        handler = new Handler(this);
    }

    /**
     * @param url 默认使用UTF8
     */
    public HttpRunnable(String type, String url, HttpHandler httpHandler) {
        this(type, url, null, httpHandler);
    }

    @Override
    public void run() {
        Handler.Message message = new Handler.Message();
        message.what = 0;
        try {
            message.obj = restfulHTTP.setHeaderParams(headers)
                    .writeBody(body)
                    .open(type)
                    .read()
                    .toString();
            handler.sendMessage(message);
        } catch (IOException e) {
            Handler.Message errorMessage = new Handler.Message();
            errorMessage.what = Result.NET_ERROR;
            handler.sendMessage(errorMessage);
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Handler.Message msg) {
        if (httpHandler != null) {
            if (msg.what == Result.NET_ERROR) {
                httpHandler.error(Result.NET_ERROR);
            } else {
                httpHandler.handle((String) (msg.obj));
            }
        }
    }

    public void setHeaderParams(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public interface HttpHandler {
        void handle(String text);

        void error(int code);
    }
}
