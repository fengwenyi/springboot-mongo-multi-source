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
