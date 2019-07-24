package com.dingmk.registry.zookeeper;

import com.dingmk.registry.RegistryService;
import com.dingmk.registry.RegistryServiceFactory;
import com.dingmk.registry.common.CustomURL;
import com.dingmk.registry.zookeeper.zkclient.CuratorTransport;

/**
 * @author zt
 */
public class ZookeeperRegistryServiceFactory implements RegistryServiceFactory {

    @Override
    public RegistryService getRegistryService(CustomURL url) {
        return new ZookeeperRegistryService(url.getRegisterURL(), new CuratorTransport());
    }

}
