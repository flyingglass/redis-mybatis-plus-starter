package com.github.flyingglass.mybatis.cache;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author fly
 * @date 2019/12/11 16:44
 * desc:
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext ctx;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return ctx.getBean(beanName, clazz);
    }
}
