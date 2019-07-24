package com.dingmk.registry;

import com.dingmk.registry.common.CustomURL;

/**
 * @author zt
 */
public interface RegistryServiceFactory {

    /**
     * 获取egistryService
     * @param url
     * @return
     */
    RegistryService getRegistryService(CustomURL url);

}
