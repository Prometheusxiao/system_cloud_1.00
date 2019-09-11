package com.system.basis.bean;

/**
 * @CreateTime: 2019-09-11 09:14
 * @Description:
 * @Author: WH
 */
public class AddressBasisInfo {
    private String code;

    private String superCode;

    private String categoryCode;

    private String addressName;

    private String addressVersion;

    private String addressUrl;

    private String status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getSuperCode() {
        return superCode;
    }

    public void setSuperCode(String superCode) {
        this.superCode = superCode == null ? null : superCode.trim();
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode == null ? null : categoryCode.trim();
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName == null ? null : addressName.trim();
    }

    public String getAddressVersion() {
        return addressVersion;
    }

    public void setAddressVersion(String addressVersion) {
        this.addressVersion = addressVersion == null ? null : addressVersion.trim();
    }

    public String getAddressUrl() {
        return addressUrl;
    }

    public void setAddressUrl(String addressUrl) {
        this.addressUrl = addressUrl == null ? null : addressUrl.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    @Override
    public String toString() {
        return "AddressBasisInfo{" +
            "code='" + code + '\'' +
            ", superCode='" + superCode + '\'' +
            ", categoryCode='" + categoryCode + '\'' +
            ", addressName='" + addressName + '\'' +
            ", addressVersion='" + addressVersion + '\'' +
            ", addressUrl='" + addressUrl + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}
