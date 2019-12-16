package com.github.flyingglass.mybatis.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.github.flyingglass.mybatis.phoenix.Upsert;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fly
 * @date 2019/5/28 15:48
 * description: 注入PhoenixSqlInjector，用于扩展Phoenix的Hbase的Upsert方法
 */
@Component
@ConditionalOnMissingBean(DefaultSqlInjector.class)
public class PhoenixSqlInjector extends DefaultSqlInjector {
    /**
     * <p>
     * 获取 注入的方法
     * </p>
     *
     * @return 注入的方法集合
     */
    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> methodList = super.getMethodList();

        methodList.add(new Upsert());

        return methodList;
    }
}
