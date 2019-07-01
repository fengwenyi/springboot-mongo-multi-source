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
