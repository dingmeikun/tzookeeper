package com.dingmk.registry.zookeeper.zkclient;

import java.util.List;

/**
 * @author zt
 */
public interface ZkClient {

    /**
     * 创建节点
     * @param path
     */
    void create(String path);

    /**
     * 创建节点
     * @param path
     * @param ephemeral
     */
    void create(String path, boolean ephemeral);

    /**
     * 删除节点
     * @param path
     */
    void delete(String path);

    /**
     * 获取子节点
     * @param path
     * @return
     */
    List<String> getChildren(String path);

    /**
     * 某个节点下所有子节点监听
     * @param path
     * @param listener
     */
    void childListener(String path, ChildListener listener);

    /**
     * 判断是否连接
     * @return
     */
    boolean isConnected();

    /**
     * 关闭
     */
    void close();

    /**
     * 检查是否存在
     * @param path
     * @return
     */
    boolean checkExists(String path);

}
