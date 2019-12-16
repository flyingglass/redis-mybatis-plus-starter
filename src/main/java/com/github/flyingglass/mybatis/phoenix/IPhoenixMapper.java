package com.github.flyingglass.mybatis.phoenix;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author fly
 * @date 2019/5/28 16:11
 * desc: PhoenixMapper基类
 */
public interface IPhoenixMapper<T> extends BaseMapper<T> {

    /**
     * Phoenix Hbase插入一条记录
     * @param entity 实体对象
     * @return 插入条数,1成功,0失败
     */
    int upsert(T entity);
}
