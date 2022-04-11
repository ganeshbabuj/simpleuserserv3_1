package com.example.simpleuserserv3_1.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "address")
@Getter
@Setter
@Entity
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String state;

    // Owning side
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
