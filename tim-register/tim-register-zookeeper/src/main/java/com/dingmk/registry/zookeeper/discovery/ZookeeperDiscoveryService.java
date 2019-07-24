package com.dingmk.registry.zookeeper.discovery;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.dingmk.registry.common.CustomURL;
import com.dingmk.registry.discovery.DiscoveryService;
import com.dingmk.registry.exception.ServiceNotFoundException;
import com.dingmk.registry.zookeeper.util.PathUtil;
import com.dingmk.registry.zookeeper.zkclient.ChildListener;
import com.dingmk.registry.zookeeper.zkclient.ListenerType;
import com.dingmk.registry.zookeeper.zkclient.ZkClient;
import com.dingmk.registry.zookeeper.zkclient.ZkTransport;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zt
 */
@Slf4j
public class ZookeeperDiscoveryService implements DiscoveryService {

    private final ZkClient zkClient;
    private final String root;

    private static final Map<String/*serviceName*/, List<String>/*ip:port/contextPath*/> REGISTRIES = new ConcurrentHashMap<>(16);

    private static final Interner<String> POOL = Interners.newWeakInterner();

    public ZookeeperDiscoveryService(CustomURL url, ZkTransport zkTransport) {
        this.zkClient = zkTransport.connect(url.getRegisterURL());
        this.root = PathUtil.DEFAULT_ROOT;

        listenerRoot(root, REGISTRIES);
    }

    @Override
    public List<String> getServiceUrl(String serviceName) throws ServiceNotFoundException {
        if (!REGISTRIES.containsKey(serviceName) || REGISTRIES.get(serviceName).isEmpty()) {
            throw new IllegalArgumentException("serviceName not found");
        }
        return Collections.unmodifiableList(REGISTRIES.get(serviceName));
    }

    @Override
    public String getFirstServiceUrl(String serviceName) throws ServiceNotFoundException {
        return getServiceUrl(serviceName).get(0);
    }

    @Override
    public Map<String, List<String>> getServiceUrl() throws ServiceNotFoundException {
        return Collections.unmodifiableMap(REGISTRIES);
    }

    private void listenerRoot(final String root, final Map<String, List<String>> registries) {
        zkClient.childListener(PathUtil.addPrefixPath(root), new ChildListener() {
            @Override
            public void childChanged(String path, ListenerType type) {
                List<String> strings = PathUtil.splitPath(path);
                log.debug("strings:{}", strings);
                // 表示监听3级节点
                if (strings.size() == 3) {
                    String serviceName = strings.get(1);
                    String server = PathUtil.decode(strings.get(2));

                    synchronized (POOL.intern("sn" + serviceName)) {
                        List<String> list = registries.get(serviceName);
                        if(list != null && !list.isEmpty()) {
                            if (type == ListenerType.REMOVE) {
                                list.remove(server);
                            } else if (type == ListenerType.ADD){
                                if (!list.contains(server)) {
                                    list.add(server);
                                }
                            }
                        } else {
                            if (type == ListenerType.ADD) {
                                List<String> subList = new ArrayList<>();
                                subList.add(server);
                                registries.put(serviceName, subList);
                            }
                        }
                    }
                    log.debug("registries:" + registries);
                }
            }
        });
    }

}
