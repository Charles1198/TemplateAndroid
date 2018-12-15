package com.charles.common.network.response;

import java.util.List;

/**
 * @author charles
 * @date 2018/11/20
 * @description
 */
public class ServerResp {


    /**
     * shortName : test
     * serverName : G
     * serverDesc : t
     * ServerURL : ["87.75.118.52","42.168.98.188","178.8.183.4"]
     */

    private String shortName;
    private String serverName;
    private String serverDesc;
    private List<String> ServerURL;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerDesc() {
        return serverDesc;
    }

    public void setServerDesc(String serverDesc) {
        this.serverDesc = serverDesc;
    }

    public List<String> getServerURL() {
        return ServerURL;
    }

    public void setServerURL(List<String> ServerURL) {
        this.ServerURL = ServerURL;
    }
}
