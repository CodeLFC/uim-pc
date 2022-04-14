package gaozhi.online.uim.example.im.entity;

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
    private long updateTime;
    private SocketAddress address;

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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public void setAddress(SocketAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UClient{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", updateTime=" + updateTime +
                '}';
    }
}
