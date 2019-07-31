package com.fengwenyi.springboot_mongo_multi_source;

import com.fengwenyi.springboot_mongo_multi_source.primary.repository.LoginLogRepository;
import com.fengwenyi.springboot_mongo_multi_source.secondary.entity.User;
import com.fengwenyi.springboot_mongo_multi_source.secondary.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MongoRepository test
 * 查
 * 增
 * 改
 * 删
 *
 * @author Erwin Feng
 * @since 2019-07-31 22:51
 */
@Component
public class RepositoryTests extends SpringbootMongoMultiSourceApplicationTests {

    /**
     * [mongo] 用户
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * [mongo] 登录日志
     */
    @Autowired
    private LoginLogRepository loginLogRepository;

    // 测试增加
    @Test
    public void testAdd() {
        userRepository.save(
                new User()
                        .setId(UUID.randomUUID().toString())
                        .setUsername("admin")
                        .setAge(20)
                        .setRegisterTime(Instant.now()));
    }

    // 测试查询
    @Test
    public void testQuery() {
        List<User> all = userRepository.findAll();
        all.forEach(System.out::println);
    }

    // 测试修改
    @Test
    public void testUpdate() {
        Optional<User> user = userRepository.findById("5d19d560cede54c45701e12d");
        user.ifPresent(value -> userRepository.save(value.setUsername("user")));
    }

    // 测试删除
    @Test
    public void testDelete() {
        userRepository.deleteById("5d19d560cede54c45701e12d");
    }

}
