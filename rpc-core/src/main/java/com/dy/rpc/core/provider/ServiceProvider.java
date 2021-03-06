package com.dy.rpc.core.provider;

import com.dy.rpc.common.extension.SPI;

/**
 * 服务注册表通用接口
 *
 * @Author: chenyibai
 * @Date: 2021/1/19 15:47
 */
@SPI
public interface ServiceProvider {

    /**
     * 将一个服务注册进注册表
     *
     * @param service 待注册的服务实体
     * @param <T>     服务实体类
     */
    <T> void registerService(T service, String serviceName);

    /**
     * 根据服务名称获取服务实体
     *
     * @param serviceName 服务名称
     * @return 服务实体
     */
    Object getService(String serviceName);

}
