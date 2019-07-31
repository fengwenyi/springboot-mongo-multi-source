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
        List<User> userList = mongoTemplate.find(query, User.class);
//        return ApiResult.success(loginLogs);
        return ApiResult.success(userList);
    }

}
