package com.github.flyingglass.mybatis.cache;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fly
 */
@Slf4j
public class MybatisRedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    /**
     * 缓存刷新间隔，单位为毫秒
     * flushInterval 参数(自定义cache无法使用默认的flushInterval)
     */
    @Setter
    private long flushInterval = 0L;


    private RedisTemplate redisTemplate;

    private RedisTemplate getRedisTemplate() {
        if (null == redisTemplate) {
            redisTemplate = ApplicationContextHolder.getBean("redisTemplate", RedisTemplate.class);
        }
        return redisTemplate;
    }

    /**
     * cache instance id
     */
    private final String id;

    /**
     * @param id
     */
    public MybatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Put query result to redis
     *
     * @param key
     * @param value
     */
    @Override
    @SuppressWarnings("unchecked")
    public void putObject(Object key, Object value) {
        try {
            getRedisTemplate().opsForHash().put(getId(), key.toString(), value);

            if (flushInterval > 0L) {
                getRedisTemplate().expire(getId(), flushInterval, TimeUnit.MICROSECONDS);
            }
        }
        catch (Throwable t) {
            log.error("Redis put failed", t);
        }
    }

    /**
     * Get cached query result from redis
     *
     * @param key
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object getObject(Object key) {
        try {
            return getRedisTemplate().opsForHash().get(getId(), key.toString());
        }
        catch (Throwable t) {
            return null;
        }
    }

    /**
     * Remove cached query result from redis
     *
     * @param key
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object removeObject(Object key) {
        try {
            getRedisTemplate().opsForHash().delete(getId(), key.toString());
        }
        catch (Throwable t) {
            log.error("Redis remove failed", t);
        }
        return null;
    }

    /**
     * Clears this cache instance
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        getRedisTemplate().delete(getId());
    }

    /**
     * This method is not used
     *
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public int getSize() {
        return getRedisTemplate().opsForHash().size(getId()).intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
