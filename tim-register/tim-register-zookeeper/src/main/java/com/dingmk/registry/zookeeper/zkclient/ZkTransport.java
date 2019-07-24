package com.dingmk.registry.zookeeper.zkclient;

/**
 * @author zt
 */
public interface ZkTransport {

    ZkClient connect(String connectString);

}
