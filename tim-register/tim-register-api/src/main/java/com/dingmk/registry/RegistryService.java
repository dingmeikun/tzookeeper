package com.dingmk.registry;

import com.dingmk.registry.common.CustomURL;

/**
 * @author zt
 */
public interface RegistryService {

    /**
     * 注册服务信息
     * @param url 服务地址
     */
    void register(CustomURL url);

    /**
     * 卸载服务信息
     * @param url 服务地址
     */
    void unregister(CustomURL url);

}
