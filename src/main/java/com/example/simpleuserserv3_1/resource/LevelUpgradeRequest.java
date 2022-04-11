package com.example.simpleuserserv3_1.resource;

import lombok.Data;

@Data
public class LevelUpgradeRequest {

    private Long userId;
    private String remark;

}
