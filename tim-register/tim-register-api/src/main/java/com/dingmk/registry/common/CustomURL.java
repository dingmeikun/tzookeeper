package com.dingmk.registry.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zt
 */
@Getter
@Setter
@ToString
public class CustomURL {

    public CustomURL() {}

    public CustomURL(String registerURL) {
        this.registerURL = registerURL;
    }

    public CustomURL(String registerURL, String serviceName, String server) {
        this.registerURL = registerURL;
        this.serviceName = serviceName;
        this.server = server;
    }

    /**
     * 注册中心地址
     */
    private String registerURL;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务所在应用ip:port/contextPath
     */
    private String server;

}
