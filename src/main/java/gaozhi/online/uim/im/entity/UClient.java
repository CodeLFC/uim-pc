package gaozhi.online.uim.im.entity;

import java.net.SocketAddress;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 客户端
 * @date 2022/3/23 9:46
 */
public class UClient {
    private long id;
    private String ip;
    private int port;
    private String localIp;
    private int localPort;
    private long updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UClient{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", localIp='" + localIp + '\'' +
                ", localPort=" + localPort +
                ", updateTime=" + updateTime +
                '}';
    }
}
