package com.github.flyingglass.mybatis.boot;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.flyingglass.mybatis.cache.ApplicationContextHolder;
import com.github.flyingglass.mybatis.config.DefaultMetaObjectHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * AutoConfiguration
 * @author: fly
 */
@Configuration
@Import(ApplicationContextHolder.class)
@AutoConfigureAfter({MybatisPlusAutoConfiguration.class})
public class FgMybatisAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new DefaultMetaObjectHandler();
    }
}
