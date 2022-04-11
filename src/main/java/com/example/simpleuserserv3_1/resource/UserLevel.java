package com.example.simpleuserserv3_1.resource;

import lombok.Data;

@Data
public class UserLevel {

    private long id;
    private Long version;
    private long userId;
    private long points;
    private Level level;
    private String remark;

}
