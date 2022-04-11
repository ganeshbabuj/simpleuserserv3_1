package com.example.simpleuserserv3_1.service;

import com.example.simpleuserserv3_1.resource.User;
import com.example.simpleuserserv3_1.resource.UserCollection;

import java.util.Optional;

public interface UserService {

    User createUser(User user);
    User getUser(long id);
    UserCollection findUsers(Optional<String> firstName, int pageSize, int page);
}
