/**
 * Class Name: UTime
 * Description:
 * Author:liuyouqing Create Date:16/5/19 12:23.
 * Warn: 此代码由liuyouqing编写，未经liuyouqing本人允许，不得私自挪用。
 */
package com.example.administrator.widgetdemo.bean;

import java.io.Serializable;

/**
 * Author: huangxiaoming
 * Date: 2018/4/26
 * Desc: 版本更新实体
 * Version: 1.0
 */
public class VersionEntity implements Serializable {

    public VersionEntity(String url, String versionName) {
        this.url = url;
        this.versionName = versionName;
    }

    private String version;
    private String versionName;
    private String versionDeclare;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDeclare() {
        return versionDeclare;
    }

    public void setVersionDeclare(String versionDeclare) {
        this.versionDeclare = versionDeclare;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VersionEntity{" +
                "version='" + version + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionDeclare='" + versionDeclare + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
