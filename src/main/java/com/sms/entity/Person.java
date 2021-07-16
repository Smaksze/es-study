package com.sms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author v-sunms.gd@chinatelecom.cn
 * @date 2020-12-25 9:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person {
    private String name;
    private Integer age;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;

}
