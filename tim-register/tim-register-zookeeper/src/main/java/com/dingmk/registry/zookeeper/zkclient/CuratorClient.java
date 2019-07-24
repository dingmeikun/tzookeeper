package com.dingmk.registry.zookeeper.zkclient;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zt
 */
@Slf4j
public class CuratorClient implements ZkClient {

    private final CuratorFramework client;

    private volatile boolean closed = false;

    public CuratorClient(String connectString) {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(new RetryNTimes(1, 1000))
                .connectionTimeoutMs(5000);
        client = builder.build();
        client.start();
    }

    @Override
    public void create(String path) {
        create(path, true);
    }

    @Override
    public void create(String path, boolean ephemeral) {
        try {
            if (ephemeral) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
                return;
            }
            client.create().creatingParentsIfNeeded().forPath(path);
        } catch (KeeperException.NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            client.delete().guaranteed().forPath(path);
        } catch (KeeperException.NoNodeException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void childListener(final String path, final ChildListener listener) {
        TreeCache cache = new TreeCache(client, path);
        cache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case NODE_ADDED: {
                        String path = event.getData().getPath();
                        listener.childChanged(path, ListenerType.ADD);
                        break;
                    }
                    case NODE_REMOVED: {
                        String path = event.getData().getPath();
                        listener.childChanged(path, ListenerType.REMOVE);
                        break;
                    }
                }
            }
        });
        try {
            cache.start();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            client.close();
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public boolean checkExists(String path) {
        try {
            if (client.checkExists().forPath(path) != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

}
