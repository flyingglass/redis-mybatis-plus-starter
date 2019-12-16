package com.github.flyingglass.mybatis.boot;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.github.flyingglass.mybatis.cache.ApplicationContextHolder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fly
 * @date 2019/12/10 19:58
 * desc:
 */
@Configuration
@Import(ApplicationContextHolder.class)
@AutoConfigureAfter({MybatisPlusAutoConfiguration.class})
public class FgMybatisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
