package com.example.simpleuserserv3_1.resource;

import lombok.Data;

@Data
public class PointsIncrementRequest {

    private Long userId;
    private Long points;
    private String remark;

}
