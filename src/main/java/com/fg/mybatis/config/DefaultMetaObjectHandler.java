package com.fg.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author fly
 * @date 2019/5/28 11:12
 * description: 自定义字段填充，gmt_updated和gmt_create自动填充
 */
@Component
@ConditionalOnMissingBean(MetaObjectHandler.class)
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        this.setInsertFieldValByName("gmtCreate", ts, metaObject);
        this.setInsertFieldValByName("gmtUpdated", ts, metaObject);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        this.setUpdateFieldValByName("gmtUpdated", ts, metaObject);
    }
}
