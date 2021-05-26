package com.github.flyingglass.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.sql.Timestamp;

/**
 * 自定义字段填充，gmt_updated和gmt_create自动填充
 * @author: fly
 */
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        this.strictInsertFill(metaObject, "gmtCreate", Timestamp.class, ts);
        this.strictUpdateFill(metaObject, "gmtUpdated", Timestamp.class, ts);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "gmtUpdated", Timestamp.class, new Timestamp(System.currentTimeMillis()));
    }
}
