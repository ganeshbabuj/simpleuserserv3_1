package com.example.simpleuserserv3_1.resource;

import com.example.simpleuserserv3_1.client.ActivationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private int age;

    private Set<Address> addresses;
    private Set<Group> groups;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ActivationResponse marketing;

}
