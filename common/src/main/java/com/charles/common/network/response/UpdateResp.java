package com.charles.common.network.response;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author charles
 * @date 2018/11/15
 * @description
 */
public class UpdateResp {

    private String appVersionDesc;
    private String forceUpdate;
    private String url;
    private String version;
    private AppVersionDesc desc;

    public String getAppVersionDesc() {
        return appVersionDesc;
    }

    public void setAppVersionDesc(String appVersionDesc) {
        this.appVersionDesc = appVersionDesc;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public AppVersionDesc getDesc() {
        Gson gson = new Gson();
        desc = gson.fromJson(appVersionDesc, AppVersionDesc.class);
        return desc;
    }

    public class AppVersionDesc {

        private List<String> features;
        private List<String> bugs;

        public List<String> getFeatures() {
            return features;
        }

        public void setFeatures(List<String> features) {
            this.features = features;
        }

        public List<String> getBugs() {
            return bugs;
        }

        public void setBugs(List<String> bugs) {
            this.bugs = bugs;
        }
    }
}
