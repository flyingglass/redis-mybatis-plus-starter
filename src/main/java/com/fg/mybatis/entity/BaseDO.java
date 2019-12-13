package com.fg.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author fly
 * @date 2019/5/21 12:12
 * description: MySql, Phoenix HBase的实体基类，用于映射数据库
 * 默认字段:
 * id: 主键
 * gmt_create：创建时间datetime
 * gmt_updated：更新时间datetime
 */
@Data
@Accessors(chain = true)
public class BaseDO implements Serializable {
    /**
     * 主键，默认自增
     */
    private Long id;

    /**
     * Insert时生成，MyMetaObjectHandler自动填充
     */
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private Timestamp gmtCreate;
    /**
     * Insert, Update时生成，MyMetaObjectHandler自动填充
     */
    @TableField(value = "gmt_updated", fill = FieldFill.INSERT_UPDATE)
    private Timestamp gmtUpdated;
}
