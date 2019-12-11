# backend-starter-mybatis

## 简介
该项目主要利用SpringBoot的自动化配置特性来实现快速的将Mybatis-Plus + 多数据源 + Redis二级缓存 + Phoenix引入Springboot项目中，简化原生Mybatis-Plus整合多数据源、Mybatis Redis二级缓存和Phoenix使用。

- [完整使用样例Demo参考](https://github.com/FlyingGlass/backend-starter-mybatis-demo)

欢迎使用和star支持，如使用中碰到问题，可以提出Issue，我会尽力完善该Starter

### 版本基础
- Mybatis-Plus: 3.1.1
- Spring-Boot: 2.1.5.RELEASE

### Quick-Start

轻松引入SpringBoot工程中，需要如下步骤:

- 在`pom.xml`中引入依赖:
```xml
<dependency>
    <groupId>com.fly</groupId>
    <artifactId>backend-starter-mybatis</artifactId>
    <version>1.0.0</version>
</dependency>
```

- 在应用主类中增加`@MapperScan(basePackages = "com.xxx.mapper")`注解，用于扫描`Mapper`的`Interface`，并且排除`DruidDataSourceAutoConfigure`

```java
@MapperScan(basePackages = "com.xxx.mapper")
@SpringBootApplication(
        exclude = DruidDataSourceAutoConfigure.class
)
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

}
```

- 配置`application.yml`
```yml
spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        reset-enable: true
        url-pattern: /druid/*
        allow:
        deny:
      web-stat-filter:
        enabled: true
        exclusions: '*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*'
        profile-enable: true
        session-stat-enable: false

    dynamic:
      # 全局配置，数据源可覆盖
      druid:
        initial-size: 1 # 初始连接数
        max-active: 3 # 最大连接池数量
        min-idle: 1 # 最小连接池数量
        max-wait: 60000
        pool-prepared-statements: false
        validation-query: "SELECT 1"
        test-on-borrow: false
        test-on-return: false
        test-while-idle: true
        time-between-eviction-runs-millis: 3000
        min-evictable-idle-time-millis: 300000
        # Filter
        # wall不兼容phoenix
        filters: stat
        stat:
          log-slow-sql: true
          slow-sql-millis: 5000
          merge-sql: true

      primary: master
      datasource:
        master:
          username: ${mysql.username}
          password: ${mysql.password}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/master_db
                  
mybatis-plus:
  # classpath要加上后面的*，表示通配符，匹配任意路径
  mapper-locations: classpath*:/mapper/*Mapper.xml
  typeAliasesPackage: com.fly.demo.entity
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: true # 开启xml缓存
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
```

### 配置说明

#### 配置redis


