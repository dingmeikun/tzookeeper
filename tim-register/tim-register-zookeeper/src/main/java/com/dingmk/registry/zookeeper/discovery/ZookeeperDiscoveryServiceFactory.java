package com.dingmk.registry.zookeeper.discovery;

import com.dingmk.registry.discovery.DiscoveryService;
import com.dingmk.registry.discovery.DiscoveryServiceFactory;
import com.dingmk.registry.common.CustomURL;
import com.dingmk.registry.zookeeper.zkclient.CuratorTransport;

/**
 * @author zt
 */
public class ZookeeperDiscoveryServiceFactory implements DiscoveryServiceFactory {

    @Override
    public DiscoveryService getDiscoveryService(CustomURL url) {
        return new ZookeeperDiscoveryService(url, new CuratorTransport());
    }
}
