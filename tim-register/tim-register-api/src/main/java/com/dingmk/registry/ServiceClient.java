package com.dingmk.registry;

import com.dingmk.registry.common.CustomURL;
import com.dingmk.registry.discovery.DiscoveryService;
import com.dingmk.registry.discovery.DiscoveryServiceFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zt
 */
public final class ServiceClient {

    private static RegistryServiceFactory registryServiceFactory;
    private static DiscoveryServiceFactory discoveryServiceFactory;

    private static final Map<String/*registerURL*/, DiscoveryService> DISCOVERY_SERVICE_MAP = new ConcurrentHashMap<>(16);
    private static final Map<String/*registerURL*/, RegistryService> REGISTRY_SERVICE_MAP = new ConcurrentHashMap<>(16);

    private ServiceClient() {
    }

    static {
        ServiceLoader<RegistryServiceFactory> registryLoader = ServiceLoader.load(RegistryServiceFactory.class);
        Iterator<RegistryServiceFactory> registryIterator = registryLoader.iterator();
        if(registryIterator.hasNext()) {
            registryServiceFactory = registryIterator.next();
        }

        ServiceLoader<DiscoveryServiceFactory> discoveryLoader = ServiceLoader.load(DiscoveryServiceFactory.class);
        Iterator<DiscoveryServiceFactory> discoveryIterator = discoveryLoader.iterator();
        if (discoveryIterator.hasNext()) {
            discoveryServiceFactory = discoveryIterator.next();
        }
    }

    public static RegistryService getRegistryService(String registerURL) {
        if (REGISTRY_SERVICE_MAP.containsKey(registerURL)) {
            return REGISTRY_SERVICE_MAP.get(registerURL);
        }
        RegistryService registryService = registryServiceFactory.getRegistryService(new CustomURL(registerURL));
        REGISTRY_SERVICE_MAP.put(registerURL, registryService);
        return registryService;
    }

    public static DiscoveryService getDiscoveryService(String registerURL) {
        if (DISCOVERY_SERVICE_MAP.containsKey(registerURL)) {
            return DISCOVERY_SERVICE_MAP.get(registerURL);
        }
        DiscoveryService discoveryService = discoveryServiceFactory.getDiscoveryService(new CustomURL(registerURL));
        DISCOVERY_SERVICE_MAP.put(registerURL, discoveryService);
        return discoveryService;
    }
}
