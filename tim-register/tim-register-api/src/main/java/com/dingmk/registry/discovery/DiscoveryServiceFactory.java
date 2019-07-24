package com.dingmk.registry.discovery;

import com.dingmk.registry.common.CustomURL;

/**
 * @author zt
 */
public interface DiscoveryServiceFactory {

    DiscoveryService getDiscoveryService(CustomURL url);

}
