package com.feng.pigp.fans.model;

/**
 * 代理IP
 *
 * @author logic
 * @date 2019/7/12 3:50 PM
 * @since 1.0
 */
public class ProxyIp {
    private String Ip;
    private int port;

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProxyIp{");
        sb.append("Ip='").append(Ip).append('\'');
        sb.append(", port=").append(port);
        sb.append('}');
        return sb.toString();
    }
}
