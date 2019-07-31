package com.fengwenyi.springboot_mongo_multi_source;

import com.fengwenyi.springboot_mongo_multi_source.primary.entity.LoginLog;
import com.fengwenyi.springboot_mongo_multi_source.secondary.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MongoTemplate test
 *  * 查
 *  * 增
 *  * 改
 *  * 删
 * @author Erwin Feng
 * @since 2019-07-31 23:06
 */
@Component
public class MongoTemplateTests extends SpringbootMongoMultiSourceApplicationTests {

    // 方式1注入
    @Autowired
    @Qualifier("primaryMongoTemplate")
    private MongoTemplate primaryMongoTemplate;

    // 方式2注入
    @Resource(name = "secondaryMongoTemplate")
//    @Autowired
//    @Qualifier("secondaryMongoTemplate")
    private MongoTemplate secondaryMongoTemplate;

    // 测试 primary 查询
    @Test
    public void testQueryPrimary() {
        List<LoginLog> loginLogList = primaryMongoTemplate.find(new Query(), LoginLog.class);
        loginLogList.forEach(System.out::println);
    }

    // 测试 secondary 查询
    @Test
    public void testQuerySecondary() {
        List<User> userList = secondaryMongoTemplate.find(new Query(), User.class);
        userList.forEach(System.out::println);
    }

}
