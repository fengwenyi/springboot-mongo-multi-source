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
