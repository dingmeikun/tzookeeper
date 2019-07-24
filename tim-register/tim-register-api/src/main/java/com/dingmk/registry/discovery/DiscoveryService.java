package com.dingmk.registry.discovery;

import java.util.List;
import java.util.Map;

import com.dingmk.registry.exception.ServiceNotFoundException;

/**
 * @author zt
 */
public interface DiscoveryService {

    /**
     * 根据指定服务名称获取url
     * @param serviceName
     * @return
     */
    List<String> getServiceUrl(String serviceName) throws ServiceNotFoundException;

    /**
     * 根据指定服务名称获取第一个服务url
     * @param serviceName
     * @return
     */
    String getFirstServiceUrl(String serviceName) throws ServiceNotFoundException;

    /**
     * 获取所有服务url
     * @return
     */
    Map<String/*serviceName*/, List<String>/*ip:port/contextPath*/> getServiceUrl() throws ServiceNotFoundException;

}
