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
