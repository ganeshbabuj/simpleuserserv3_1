package com.example.simpleuserserv3_1.entity;

import com.example.simpleuserserv3_1.resource.Level;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_level")
@Data
public class UserLevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(unique = true, nullable = false)
    private long userId;

    private Long points = 0L;

    private Level level;

    private String remark;

}
