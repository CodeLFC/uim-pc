package gaozhi.online.uim.im.entity;

import gaozhi.online.uim.utils.DateTimeUtil;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO im服务器
 * @date 2022/3/22 14:42
 */
public class UServer {
    private int id;
    private String remark;
    private String host;
    private String ip;
    private int port;
    private long updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    @Override
    public String toString() {
        return "UServer{" +
                "id=" + id +
                ", remark='" + remark + '\'' +
                ", host='" + host + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", updateTime=" + DateTimeUtil.getChatTime(updateTime) +
                '}';
    }
}
