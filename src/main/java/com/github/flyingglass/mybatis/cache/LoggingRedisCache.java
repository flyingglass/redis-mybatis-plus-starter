package com.github.flyingglass.mybatis.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * redis cache decorators with logging
 * @author: fly
 */
@Slf4j
public class LoggingRedisCache extends LoggingCache {

    /**
     * 构造函数，二级缓存必须提供id的构造函数
     * @param id 构造函数
     */
    public LoggingRedisCache(String id) {
        super(new MybatisRedisCache(id));
    }

}
