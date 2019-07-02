<h1 align="center">SpringBoot整合MongoDB多数据源</h1>

## 依赖

```groovy
// build.gradle

implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
```



## 配置文件

```yaml
# application.yml

spring:
  data:
    mongodb:
      primary:
        uri: mongodb://localhost:27017/db1
      secondary:
        uri: mongodb://localhost:27017/db2
```



## 主数据库配置

```java
// PrimaryMongoConfig.java

package com.fengwenyi.springboot_mongo_multi_source.config;

import com.mongodb.MongoClientURI;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Primary Config
 * @author Erwin Feng
 * @since 2019-07-01 17:12
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.fengwenyi.springboot_mongo_multi_source.primary",
        mongoTemplateRef = "primaryMongoTemplate")
public class PrimaryMongoConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.data.mongodb.primary")
    public MongoProperties primaryMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        return new MongoTemplate(primaryFactory(primaryMongoProperties()));
    }

    @Bean
    @Primary
    public MongoDbFactory primaryFactory(MongoProperties mongoProperties) throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(primaryMongoProperties().getUri()));
    }
}
```



## 副数据库配置

```java
// SecondaryMongoConfig.java

package com.fengwenyi.springboot_mongo_multi_source.config;

import com.mongodb.MongoClientURI;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Secondary Config
 * @author Erwin Feng
 * @since 2019-07-01 17:12
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.fengwenyi.springboot_mongo_multi_source.secondary",
        mongoTemplateRef = "secondaryMongoTemplate")
public class SecondaryMongoConfig {

    @Bean
    @ConfigurationProperties(prefix="spring.data.mongodb.secondary")
    public MongoProperties secondaryMongoProperties() {
        return new MongoProperties();
    }

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() throws Exception {
        return new MongoTemplate(secondaryFactory(secondaryMongoProperties()));
    }

    @Bean
    public MongoDbFactory secondaryFactory(MongoProperties mongoProperties) throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(secondaryMongoProperties().getUri()));
    }
}
```



## 用户实体，副

```java
// User.java

package com.fengwenyi.springboot_mongo_multi_source.secondary.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

/**
 * 用户
 * @author Erwin Feng
 * @since 2019-07-01 17:15
 */
@Data
@Accessors(chain = true)
@Document(collection = "t_user")
public class User implements Serializable {

    private static final long serialVersionUID = -7229906944062898852L;

    /** ID */
    @Id
    private String id;

    /** 用户名 */
    private String username;

    /** 年龄 */
    private Integer age;

    /** 注册时间 */
    private Instant registerTime;
}
```



## 用户查询仓库，副

```java
// UserRepository.java

package com.fengwenyi.springboot_mongo_multi_source.secondary.repository;

import com.fengwenyi.springboot_mongo_multi_source.secondary.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 用户
 * @author Erwin Feng
 * @since 2019-07-01 17:18
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * 通过用户名查询
     * @param username 用户名
     * @return
     */
    List<User> findAllByUsername(String username);

}
```



## 登录日志实体，主

```java
// LoginLog.java

package com.fengwenyi.springboot_mongo_multi_source.primary.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Instant;

/**
 * 登录日志
 * @author Erwin Feng
 * @since 2019-07-01 17:18
 */
@Data
@Accessors(chain = true)
public class LoginLog implements Serializable {

    private static final long serialVersionUID = -6694661682102504919L;

    /** ID */
    @Id
    private String id;

    /** 用户ID */
    private String uid;

    /** 用户名 */
    private String username;

    /** 登录时间 */
    private Instant loginTime;
}
```



## 登录日志查询仓库，主

```java
// LoginLogRepository.java

package com.fengwenyi.springboot_mongo_multi_source.primary.repository;

import com.fengwenyi.springboot_mongo_multi_source.primary.entity.LoginLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 登录日志
 * @author Erwin Feng
 * @since 2019-07-01 17:21
 */
public interface LoginLogRepository extends MongoRepository<LoginLog, String> {
}
```



## 初始化

```java
// InitController.java

package com.fengwenyi.springboot_mongo_multi_source.controller;

import com.fengwenyi.springboot_mongo_multi_source.secondary.entity.User;
import com.fengwenyi.springboot_mongo_multi_source.secondary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化工具类
 * @author Erwin Feng
 * @since 2019-07-01 17:24
 */
@RestController
public class InitController {

    /** [mongo] 用户 */
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        List<User> all = userRepository.findAll();
        if (all.size() > 0)
            return;
        userRepository.save(new User().setUsername("Zhangsan").setAge(20).setRegisterTime(Instant.now()));
        List<User> users = new ArrayList<>();
        User u1 = new User().setUsername("u1").setAge(19).setRegisterTime(Instant.now());
        User u2 = new User().setUsername("u2").setAge(20).setRegisterTime(Instant.now());
        User u3 = new User().setUsername("u3").setAge(10).setRegisterTime(Instant.now());
        users.add(u1);
        users.add(u2);
        users.add(u3);
        userRepository.saveAll(users);
    }

}
```



## 测试代码

```java
// TestController.java

package com.fengwenyi.springboot_mongo_multi_source.controller;

import com.fengwenyi.springboot_mongo_multi_source.primary.entity.LoginLog;
import com.fengwenyi.springboot_mongo_multi_source.primary.repository.LoginLogRepository;
import com.fengwenyi.springboot_mongo_multi_source.secondary.entity.User;
import com.fengwenyi.springboot_mongo_multi_source.secondary.repository.UserRepository;
import net.iutil.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

/**
 * 测试
 * @author Erwin Feng
 * @since 2019-07-01 17:28
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /** [mongo] 用户 */
    @Autowired
    private UserRepository userRepository;

    /** [mongo] 登录日志 */
    @Autowired
    private LoginLogRepository loginLogRepository;

    /** [mongo] */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 登录
     * @param username
     * @return
     */
    @GetMapping("/login")
    public ApiResult login(String username) {
        if (StringUtils.isEmpty(username))
            return ApiResult.error().setMsg("用户名不能为空");
        List<User> users = userRepository.findAllByUsername(username);
        if (users.size() == 1) {
            // 记录日志
            loginLogRepository.save(new LoginLog().setUid(users.get(0).getId()).setUsername(username).setLoginTime(Instant.now()));
            return ApiResult.success();
        }
        if (users.size() == 0)
            return ApiResult.error().setMsg("用户名查询失败");

        return ApiResult.error().setMsg("用户异常");
    }

    /**
     * 登录日志
     * @return
     */
    @GetMapping("/login-log")
    public ApiResult loginLog() {
        Query query = new Query();
        List<LoginLog> loginLogs = mongoTemplate.find(query, LoginLog.class);
        return ApiResult.success(loginLogs);
    }

}
```



## 测试用户登录

GET http://localhost:8080/test/login?username=Zhangsan

响应：

```
{
    "code": 0,
    "msg": "Success"
}
```



## 测试登录日志

GET http://localhost:8080/test/login-log

响应：

```
{
    "code": 0,
    "msg": "Success",
    "data": [
        {
            "id": "5d19d7f5cede54c46b6b20c5",
            "uid": "5d19d560cede54c45701e12a",
            "username": "Zhangsan",
            "loginTime": "2019-07-01T09:52:53.447Z"
        },
        {
            "id": "5d19da82cede54c46f77579a",
            "uid": "5d19d560cede54c45701e12a",
            "username": "Zhangsan",
            "loginTime": "2019-07-01T10:03:46.496Z"
        },
        {
            "id": "5d19df5fcede54c46f77579b",
            "uid": "5d19d560cede54c45701e12a",
            "username": "Zhangsan",
            "loginTime": "2019-07-01T10:24:31.272Z"
        },
        {
            "id": "5d19df6acede54c46f77579c",
            "uid": "5d19d560cede54c45701e12b",
            "username": "u1",
            "loginTime": "2019-07-01T10:24:42.199Z"
        },
        {
            "id": "5d19df6dcede54c46f77579d",
            "uid": "5d19d560cede54c45701e12d",
            "username": "u3",
            "loginTime": "2019-07-01T10:24:45.421Z"
        }
    ]
}
```

## 代码

[springboot-mongo-multi-source](https://github.com/fengwenyi/springboot-mongo-multi-source)

