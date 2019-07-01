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
