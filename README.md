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
        initial-size: 5 # 初始连接数
        max-active: 10 # 最大连接池数量
        min-idle: 5 # 最小连接池数量
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

#### 配置`Redis`作为`Mybatis`的二级缓存

主要实现了`MybatisRedisCache`和`LoggingRedisCache`，其中`LoggingRedisCache`为`MybatisRedisCache`的装饰类，主要用日志输出，其中`MybatisRedisCache`实现了`Mybatis`二级缓存的`Cache`接口，通过`flushInterval`（精确到毫秒）参数控制缓存过期，缓存过期策略为`Redis`默认的`lazy`和定期删除策略，默认的过期策略可能`expire`时间会出现微小偏差，几乎可以忽略。

使用MybatisRedisCache需要如下配置：

- 在`application.yml`添加`Redis`配置:
```yml
spring:
  redis:
    database: 0
    host: localhost
    port: 6379
    timeout: 5000
    jedis:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: -1
```

- 配置`CacheNamespace`，支持注解或`xml`配置（记得在`Mybatis-Plus`打开`cache-enabled`属性），演示注解例子:
```java
@CacheNamespace(
        implementation = MybatisRedisCache.class,
        properties = { @Property(
                name = "flushInterval",
                value = "5000"
        )}
)
public interface TestMapper extends BaseMapper<Test> {

}
```

- 可自定义`RedisTemplate`，控制`Cache`的序列化或者反序列，`starter`默认注入`spring-data-redis`默认的`RedisTemplate<Object, Object>`作为默认的`RedisTemplate`（可选）


#### 配置多数据源(参考Mybatis-Plus的多数据源)

- 简单样例，配置`application.yml`
```yml
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          username: ${mysql.username}
          password: ${mysql.password}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://${mysql.host}:${mysql.port}/master_db

        slave_1:
          username: ${mysql.username}
          password: ${mysql.password}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://${mysql.host}:${mysql.port}/slave_db?
```
- 使用`@DS`注解进行切换，建议在`Service`层添加注解，使用样例:
```java
@DS("slave_1")
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
```

#### 配置`phoenix`数据源，已针对`phoenix-core`进行`shaded repackage`解决`Springboot`兼容问题
- 配置`pom.xml`
```xml
<dependency>
    <groupId>com.fly</groupId>
    <artifactId>phoenix-core-shaded</artifactId>
    <version>1.0.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.apache.phoenix</groupId>
            <artifactId>phoenix-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

- 配置`application.yml`
```yml
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          username: ${mysql.username}
          password: ${mysql.password}
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://${mysql.host}:${mysql.port}/master_db

        phoenix:
          username:
          password:
          url: jdbc:phoenix:znode01,znode02,znode03:2181
          driver-class-name: org.apache.phoenix.jdbc.PhoenixDriver
          druid:
            filters: stat
            connection-properties:
              schema: "\"TEST\""
```

#### 默认的自定义特性

- `Starter`中的`BaseDO`和`DefaultMetaObjectHandler`会默认填充`gmtCreate`和`gmtUpdated`字段，可以自定义`MetaObjectHandler`的`Bean`进行覆盖。
- 默认的`PhoenixSqlInjector`注入了`Upsert`方法对应`Phoenix`的`upsert`，后续的`Service`继承`PhoenixServiceImpl`即可复用`Mybatis-Plus`的原生的`Save`和`SaveBatch`方法
