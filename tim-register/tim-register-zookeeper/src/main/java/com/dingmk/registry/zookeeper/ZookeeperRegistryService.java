package com.dingmk.registry.zookeeper;

import com.dingmk.registry.RegistryService;
import com.dingmk.registry.common.CustomURL;
import com.dingmk.registry.zookeeper.util.NamedThreadFactory;
import com.dingmk.registry.zookeeper.util.PathUtil;
import com.dingmk.registry.zookeeper.zkclient.ZkClient;
import com.dingmk.registry.zookeeper.zkclient.ZkTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zt
 */
public class ZookeeperRegistryService implements RegistryService {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperRegistryService.class);

    private final String root;
    private final ZkClient zkClient;

    private final ScheduledExecutorService retryExecutor = Executors.newScheduledThreadPool(1, new NamedThreadFactory("registry-retry", true));

    public ZookeeperRegistryService(String connectString, ZkTransport transport) {
        this.root = PathUtil.DEFAULT_ROOT;
        this.zkClient = transport.connect(connectString);
    }

    @Override
    public void register(CustomURL url) {
        final String path = PathUtil.toUrlPath(root, url);
        try {
            zkClient.create(path);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to register " + path + " to zookeeper , cause: " + e.getMessage(), e);
        }
    }

    @Override
    public void unregister(CustomURL url) {
        String path = PathUtil.toUrlPath(root, url);
        try {
            zkClient.delete(path);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to unregister " + path + " to zookeeper, cause: " + e.getMessage(), e);
        }
    }

    /**
     * 定时重复注册
     * @param client
     * @param path
     */
    private void retryRegister(final ZkClient client, final String path) {
        log.info("start retry register [{}]", path);
        retryExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                log.debug("retry register [{}]", path);
                client.create(path);
            }
        }, 30L, 30L, TimeUnit.SECONDS);
    }

    /**
     * 停止重复注册
     */
    private void retryShutdown() {
        try {
            retryExecutor.shutdownNow();
        } catch (SecurityException e) {
            log.error("retryExecutor shutdownNow error", e);
        }
    }

}
