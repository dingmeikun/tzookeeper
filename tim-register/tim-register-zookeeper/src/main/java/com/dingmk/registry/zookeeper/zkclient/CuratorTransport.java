package com.dingmk.registry.zookeeper.zkclient;

/**
 * @author zt
 */
public class CuratorTransport implements ZkTransport {

    @Override
    public ZkClient connect(String connectString) {
        return new CuratorClient(connectString);
    }
}
